package org.brave.spark.util.datacleaner

import org.apache.spark._
import org.apache.spark.mllib.recommendation.{Rating, ALS, MatrixFactorizationModel}
import org.apache.spark.rdd.RDD
import org.brave.spark.base.BaseConf

object Model extends BaseConf {
  def main(args: Array[String]): Unit = {
    val filepath = "data/"
    conf.setAppName("Collaborative Filtering ")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val ratingsListTuple = sc.textFile(filepath + "ratings.txt").map(_.split(",")).map(x =>
      Tuple4(x(0).toInt,
        x(1).toInt,
        x(2).toFloat,
        x(3).toLong % 10))

    //将整理好的打分数据转换成键值对，并按比例分成训练数据集和测试数据集
    val ratings_KV = ratingsListTuple.map(x =>
      (x._4, Rating(x._1, x._2, x._3)))

    /*println("ratings:" + ratings_KV.count
      + "\n users" + ratings_KV.map(_._2._1).distinct.count
      + "\n movies" + ratings_KV.map(_._2._2).distinct.count)*/

    //整体分为3块
    val numPartitions = 3
    //训练数据集80%
    val traningData = ratings_KV.filter(_._1 < 8).values.repartition(numPartitions).cache
    //验证数据集20%
    val validateData = ratings_KV.filter(x => x._1 >= 6 && x._1 < 8).values.repartition(numPartitions).cache
    //测试数据集20%
    val testData = ratings_KV.filter(_._1 >= 8).values.repartition(numPartitions).cache

    /*println("traningData:" + traningData.count)
    println("validateData:" + validateData.count)   
    println("testData:" + testData.count)*/

    //开始训练模型
    val ranks = List(8, 22)
    val lambdas = List(0.1, 10.0)
    val iters = List(5, 7)

    var bestModel: MatrixFactorizationModel = null
    var bestValidateRnse: Double = Double.MaxValue

    var bestRank = 0
    var bestLambda = -1.0
    var bestIter = -1

    for (rank <- ranks; lam <- lambdas; iter <- iters) {
      val model = ALS.train(traningData, rank, iter, lam) //使用训练集训练模型

      val validateRnse = rnse(model, validateData, validateData.count) //使用训练出来的模型计算验证数据集得出rnse值
      println("validation = " + validateRnse +
        "for the model trained with rank = " + rank +
        "lambda = " + lam +
        "and numIter = " + iter
      )

      if (validateRnse < bestValidateRnse) {
        //筛选出最小rnse值对应的模型和参数
        bestModel = model
        bestValidateRnse = validateRnse
        bestRank = rank
        bestLambda = lam
        bestIter = iter
      }
    }

    //使用最佳模型验证测试数据集
    val testDataRnse = rnse(bestModel, testData, testData.count)
    println("the best model was trained with rank :" + bestRank +
      "  lambda :" + bestLambda +
      "  numIter :" + bestIter +
      "  Rnse :" + testDataRnse
    )


    /**
      * 推荐思路1：拿到电影菜单，剔除用户A已经看过的电影，将用户A没看过的电影放进模型进行评分预测
      * 然后按预测评分高到低排序，获取前n条记录推荐给用户A:userID = 1
      */
    val movieListTuple = sc.textFile(filepath + "movies.txt").map(_.split(",")).map(x =>
    		Tuple3(x(0).toInt, x(1), x(2))
    	)
    val movieMenu = movieListTuple.map(x => (x._1, x._3)).collect().toMap//电影菜单

    val userHaveSeenID = traningData.filter(_.user == 1).map(_.product).collect().toSet//用户看过的电影ID
    val userNotSeenList = sc.parallelize(movieMenu.keys.filter(userHaveSeenID.contains(_)).toSeq)//用户没看过的电影列表

    bestModel.predict(userNotSeenList.map((0, _))).collect().sortBy(_.rating).take(10).foreach(println)//预测评分前十打印出来
  }
      /**方差计算函数：使用上面训练好的模型来验证数据集进行预测，预测的结果和验证数据集进行
      *  join之后计算评分的方差并返回
      */
    def rnse(model: MatrixFactorizationModel, predictionData: RDD[Rating], n: Long): Double = {
      val predition = model.predict(predictionData.map(x => (x.user, x.product)))
      val preditionAndOldRatings = predition.map(x => ((x.user, x.product), x.rating))
        .join(predictionData.map(x => ((x.user, x.product), x.rating))).values
      math.sqrt(preditionAndOldRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).reduce(_ - _) / n)
    }
}