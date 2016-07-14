package org.brave.spark.util.datacleaner

import org.apache.spark._
import main.scala.org.brave.spark.base.BaseConf
import org.brave.spark.caseclass.{Links, Movies, Ratings, Tags}
import main.scala.org.brave.util.demo.RunHelloWorldOnLocalServer._

object ETL extends BaseConf {
  def main(args: Array[String]) {
    var filepath = "data";
    conf.setAppName("ETL for files from " + filepath)
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    import sqlContext.implicits._
    val movies = sc.textFile("data/movies.txt").map(_.split(",")).map(x => Movies(x(0).trim().toInt,
      x(1).trim(),
      x(2).trim())).toDF()
    movies.write.saveAsTable("movies")

    val ratings = sc.textFile("data/ratings.txt").map(_.split(",")).map(x => Ratings(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim().toFloat,
      x(3).trim().toDouble)).toDF()
    ratings.write.saveAsTable("ratings")

    val links = sc.textFile("data/links.txt").filter {
      !_.endsWith(",")
    }.map(_.split(",")).map(x => Links(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim().toInt)).toDF()
    links.write.saveAsTable("links")

    val tags = sc.textFile("data/tags.txt").filter {
      !_.endsWith(",")
    }.filter {
      !_.toString().contains('\"')
    }.map(_.split(",")).map(x => Tags(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim(),
      x(3).trim().toDouble)).toDF()
    tags.write.saveAsTable("tags")
  }
}