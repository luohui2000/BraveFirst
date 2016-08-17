package org.brave.spark.ml

import scala.util.Random

import org.apache.spark._
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

import org.brave.spark.base.BaseConf
import org.brave.util.util._

/**
 * 通过尝试不同的参数，产生一个最佳的模型
 */
object getBestModel extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("getBestModel")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)

    val ratings = hc.sql("select userid,movieid,rating from ratings_training")
    val ratingRDD = ratings.rdd.map(x =>
      Array(x.getInt(0), x.getInt(1), x.getFloat(2).toDouble)).map {
      _ match {
        case Array(userid, movieid, rating) => Rating(userid.toInt, movieid.toInt, rating.toDouble)
      }
    }
    ratingRDD.persist()
    val userAndMovie = ratingRDD.map {
      case Rating(user, product, rate) => (user, product)
    }
    userAndMovie.persist()
    var bestmodel: MatrixFactorizationModel = null
    var bestRMSE: Double = Double.MaxValue
    var bestRank = 0
    var bestIter = -1

    val ranks = Tuple2(5, 22)
    val iters = Tuple2(3, 7)
    for (rank <- ranks._1 to ranks._2; iter <- iters._1 to iters._2) {
      val model = ALS.train(ratingRDD, rank, iter, 0.01)

      val pred = model.predict(userAndMovie).map {
        case Rating(user, product, rate) => ((user, product), rate)
      }

      val ratesAndPreds = ratingRDD.map {
        case Rating(user, product, rate) => ((user, product), rate)
      }.join(pred)

      val MSE = ratesAndPreds.map {
        case ((user, product), (r1, r2)) =>
          val err = (r1 - r2)
          err * err
      }.mean()
      val RMSE = math.sqrt(MSE)
      println("Current RMSE = " + RMSE)
      if (RMSE < bestRMSE) {
        bestmodel = model
        bestRMSE = RMSE
        bestRank = rank
        bestIter = iter
      }
    }
    println("===============Best Model Training Completed!==============")
    println("bestRank:" + bestRank)
    println("bestIter:" + bestIter)
    println("bestRMSE:" + bestRMSE)
    ratingRDD.unpersist(true)
    userAndMovie.unpersist(true)

    val ct = new CalendarTool
    val today = ct.getToday

    bestmodel.save(sc, s"model/ALS_$today")
  }
}