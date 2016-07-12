package main.scala.ML

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.mllib.linalg.{ Vector, Vectors }
import org.apache.spark.sql.Row

object Recommandation {
  def main(args: Array[String]) {
    val sparkMasterUrlDev = "spark://master60:7077"
    val sparkMasterUrlLocal = "local[2]"
    val conf = new SparkConf().setAppName("Recommandation").setMaster(sparkMasterUrlDev)
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