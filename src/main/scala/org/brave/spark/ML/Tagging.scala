package org.brave.spark.ml

import org.apache.spark._
import org.brave.spark.base.BaseConf
import org.brave.util.demo.RunHelloWorldOnLocalServer._

object Tagging extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("Tagging")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)

    //    val movietype = hc.sql("select distinct(genres) from movies").map(_.getString(0).split("|"))
    val movietype = hc.sql("select distinct(genres) from movies").map(_.getString(0))
    movietype.take(10)
  }
}