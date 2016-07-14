package org.brave.spark.ml

import org.apache.spark._

object Tagging {
  def main(args: Array[String]) {
    val sparkMasterUrlDev = "spark://master60:7077"
    val sparkMasterUrlLocal = "local[2]"
    val conf = new SparkConf().setAppName("Tagging").setMaster(sparkMasterUrlLocal)
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)

//    val movietype = hc.sql("select distinct(genres) from movies").map(_.getString(0).split("|"))
    val movietype = hc.sql("select distinct(genres) from movies").map(_.getString(0))
    movietype.take(10)
  }
}