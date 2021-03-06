package org.brave.spark.ml

import org.apache.log4j.{Logger, Level}
import org.apache.spark.SparkContext
import org.apache.spark.ml.{PipelineModel, Pipeline}
import org.apache.spark.ml.recommendation.ALS.Rating
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.DataFrame
import org.brave.spark.base.BaseConf


/*
 * 本类专门用来训练模型，并将训练好的模型保存到本地或者HDFS上，供推荐环节调用
 * 
*/
object AlsModelTraning extends BaseConf {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

    val filepath = "data/"
    conf.setAppName("Collaborative Filtering ")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    val ratingsListTuple = sc.textFile(filepath + "ratings.txt").map(_.split(",")).map(x =>
      Tuple4(x(0).toInt,
        x(1).toInt,
        x(2).toFloat,
        x(3).toLong % 10))

    //将整理好的打分数据转换成键值对，并按比例分成训练数据集和测试数据集
    val ratings_KV = ratingsListTuple.map(x =>
      (x._4, Rating(x._1, x._2, x._3)))

    //整体分为3块
    val numPartitions = 3
    val traningData = ratings_KV.filter(_._1 < 8).values.repartition(numPartitions).cache
    val validateData = ratings_KV.filter(x => x._1 >= 6 && x._1 < 8).values.repartition(numPartitions).cache
    val testData = ratings_KV.filter(_._1 >= 8).values.repartition(numPartitions).cache

    val training = sqlContext.createDataFrame(traningData)
    val validate = sqlContext.createDataFrame(validateData)
    val test = sqlContext.createDataFrame(testData)

    //将之前保存的模型删除并开始训练新的模型
    //val file_path = filepath + "alsModel"
    /*val path = Path(file_path)
    if(path.exists) {
      import scala.sys.process._
      "rm -r /tmp/result".!
    }*/
    val ranks = List(22)
    val lambdas = List(0.1, 10.0)
    val iters = List(5, 7)
    var bestModel: PipelineModel = null
    var bestValidateRmse = Double.MaxValue
    var bestRank = 0
//    var bestLambda = -1.0
    var bestIter = -1

    for (rank <- ranks; iter <- iters) {
      val als = new ALS().setRank(rank).setMaxIter(iter).setRegParam(0.01)
      val pipeline = new Pipeline().setStages(Array(als))
      val model = pipeline.fit(training)
      val validateRmse = computeRmse(model, validate) //使用训练出来的模型计算验证数据集得出rmse值

      if (validateRmse < bestValidateRmse) {
        //筛选出最小rmse值对应的模型和参数
        bestModel = model
        println("validateRmse: " + validateRmse + " bestValidateRmse: " + bestValidateRmse)
        bestValidateRmse = validateRmse
        bestRank = rank
        bestIter = iter
      }
    }

    //使用模型对测试数据集进行预测，并计算出rmse值
   /* val testDataRmse = computeRmse(bestModel, test)
    println(
      "the best model was trained with rank :" + bestRank +
//      "  lambda :" + bestLambda +
      "  numIter :" + bestIter +
      "  Rmse :" + testDataRmse
    )*/
    println("===============Best Model Training Completed!==============")
    println("bestRank:" + bestRank)
    println("bestIter:" + bestIter)
    bestModel.write.overwrite().save(filepath + "alsModel")
  }

  /**
    * 均方根误差：它是观测值与真值偏差的平方和观测次数n比值的平方根，在实际测量中，观测次数n总是有限的，
    * 真值只能用最可信赖（最佳）值来代替.方根误差对一组测量中的特大或特小误差反映非常敏感，
    * 所以，均方根误差能够很好地反映出测量的精密度，RMSE的值越小说明预测值越精确
    */
  def computeRmse (model: PipelineModel, data:DataFrame): Double = {

   math.sqrt(model.transform(data).select("rating", "prediction").map(x =>
      (x.getFloat(0) - x.getFloat(1)) * (x.getFloat(0) - x.getFloat(1))
      ).mean())

  }
}