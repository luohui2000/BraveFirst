package main.scala.ML

import org.apache.spark._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.mllib.linalg.{ Vector, Vectors }
import org.apache.spark.sql.Row

object Tagging {
  def main(args: Array[String]) {
    val sparkMasterUrlDev = "spark://master60:7077"
    val sparkMasterUrlLocal = "local[2]"
    val conf = new SparkConf().setAppName("Tagging").setMaster(sparkMasterUrlDev)
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)

//    val movietype = hc.sql("select distinct(genres) from movies").map(_.getString(0).split("|"))
    val movietype = hc.sql("select distinct(genres) from movies").map(_.getString(0))
    movietype.take(10)
  }
}