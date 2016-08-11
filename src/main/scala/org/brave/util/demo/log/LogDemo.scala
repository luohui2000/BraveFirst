
package org.brave.util.demo.log

import org.apache.spark.SparkContext
import org.brave.spark.base.BaseConf


/**
 *
 * 物品推荐的例子
 * yuchen
 * 2016-07-22
 */
object LogDemo extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("LogDemo")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
   //var dasdf= sc.textFile("data/log.txt").map(_.split("  INFO (com.honghairuitao.mobitask.action.MobileTaskController:192) - ----------phonePost---------"))
    //dasdf.collect().foreach(println)

    val ratings = sc.textFile("data/log.txt").map(_.split("  INFO (com.honghairuitao.mobitask.action.MobileTaskController:192) - ----------phonePost---------")).map(x => org.apache.spark.mllib.recommendation.Rating(
      x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim().toFloat))
  }

}