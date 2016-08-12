package org.brave.spark.ml

import org.apache.log4j.{ Logger, Level }
import org.apache.spark.SparkContext
import org.apache.spark.ml.{ PipelineModel, Pipeline }
import org.apache.spark.ml.recommendation.ALS.Rating
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.DataFrame
import org.brave.spark.base.BaseConf

/*
 * 本类专门用来训练模型，并将训练好的模型保存到本地或者HDFS上，供推荐环节调用
 * 运行之前需要运行DataExtractor类，准备好训练数据集和验证数据集
*/
object AlsModelTraning2 extends BaseConf {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

    val filepath = "data/"
    conf.setAppName("Best Model Training")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    val training = hc.sql("select userid,movieid,rating from ratings_training").withColumnRenamed("userid", "user").withColumnRenamed("movieid", "item")
    val validate = hc.sql("select userid,movieid,rating from ratings_batch").withColumnRenamed("userid", "user").withColumnRenamed("movieid", "item")

    val ranks = Tuple2(22, 22)
    val lambdas = List(0.1, 10.0)
    val iters = Tuple2(7, 7)
    var bestModel: PipelineModel = null
    var bestValidateRmse = Double.MaxValue
    var bestRank = 0
    var bestIter = -1
    
    val rank = 22
    val iter = 7

    for (rank <- ranks._1 to ranks._2; iter <- iters._1 to iters._2) {
      val als = new ALS().setRank(rank).setMaxIter(iter).setRegParam(0.01)
      val pipeline = new Pipeline().setStages(Array(als))
      val model = pipeline.fit(training)
      val validateRmse = computeRmse(model, validate) //使用训练出来的模型计算验证数据集得出rmse值
      println("validateRmse:" + validateRmse)
      if (validateRmse <= bestValidateRmse) {
        //筛选出最小rmse值对应的模型和参数
        var bestModel = model
        var bestValidateRmse = validateRmse
        var bestRank = rank
        var bestIter = iter
        println("rank: " + rank + " iter" +  " validateRmse: " + validateRmse + " bestValidateRmse: " + bestValidateRmse)
      }
    }
    bestModel.write.overwrite().save(filepath + "alsModel")
  }

  /**
   * 均方根误差：它是观测值与真值偏差的平方和观测次数n比值的平方根，在实际测量中，观测次数n总是有限的，
   * 真值只能用最可信赖（最佳）值来代替.方根误差对一组测量中的特大或特小误差反映非常敏感，
   * 所以，均方根误差能够很好地反映出测量的精密度，RMSE的值越小说明预测值越精确
   */
  def computeRmse(model: PipelineModel, data: DataFrame): Double = {
    math.sqrt(model.transform(data).select("rating", "prediction").map(x =>
      (x.getFloat(0) - x.getFloat(1)) * (x.getFloat(0) - x.getFloat(1))).mean())
  }
}