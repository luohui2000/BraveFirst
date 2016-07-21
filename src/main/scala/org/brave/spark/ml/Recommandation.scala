package org.brave.spark.ml

import org.apache.spark._
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.recommendation.Rating

import org.brave.spark.base.BaseConf

/**
 * 运行完ETL后可以运行这个类。这个是一个实验性质的类，
 * 取某个表的数据提取特征向量，然后用ALS来训练一个模型。
 * BASED ON https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/mllib/RecommendationExample.scala
 */
object Recommandation extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("Recommandation")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    hc.sql("cache table movies")
    hc.sql("cache table ratings")
    hc.sql("cache table links")
    hc.sql("cache table tags")

    val ratings = hc.sql("select userid,movieid,rating from ratings")
    val ratingRDD = ratings.rdd.map(x =>
      Array(x.getInt(0), x.getInt(1), x.getFloat(2).toDouble)).map {_ match {
        case Array(userid, movieid, rating) => Rating(userid.toInt, movieid.toInt, rating.toDouble)
      }
    }

    val rank = 10
    val numIterations = 10
    val model = ALS.train(ratingRDD, rank, numIterations, 0.01)
    
    val userAndMovie = ratingRDD.map{
      case Rating(user, product, rate) => (user, product)
    }
    
    val pred = model.predict(userAndMovie).map {
      case Rating(user, product, rate) => ((user, product), rate)
    }

    val vectors = org.apache.spark.sql.functions.udf((movieId: Int, rating: Float) =>
      Vectors.dense(movieId, rating))
    val label = org.apache.spark.sql.functions.udf((rating: Float) =>
      rating.toDouble)
    val newratings = ratings.withColumn("featureV", vectors(ratings.col("movieId"), ratings.col("rating"))).withColumn("label", label(ratings.col("rating")))
    newratings.printSchema()
    newratings.registerTempTable("newratings")
    hc.sql("select distinct(label) from newratings").collect.foreach(println)

    val lr = new LogisticRegression().setFeaturesCol("featureV").setLabelCol("label")
    println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
    lr.setMaxIter(10).setRegParam(0.01)

    val model1 = lr.fit(newratings)
    println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)
  }
}