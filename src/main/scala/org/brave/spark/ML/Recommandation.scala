package org.brave.spark.ml

import org.apache.spark._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.linalg.Vectors
import org.brave.spark.base.BaseConf

object Recommandation extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("Recommandation")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    hc.sql("cache table movies")
    hc.sql("cache table ratings")
    hc.sql("cache table links")
    hc.sql("cache table tags")

    //    val ratings = hc.sql("select * from ratings")
    val ratings = hc.sql("select * from ratings")
    ratings.printSchema()
    ratings.show()
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