
package org.brave.util.demo

import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.brave.spark.base.BaseConf

import org.brave.util.demo.RunHelloWorldOnLocalServer._


/**
 *
 * 物品推荐的例子
 * yuchen
 * 2016-07-22
 */
object TuiJianDemo extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("TuiJianDemo")
    val sc = new SparkContext(conf)
    val ratings = sc.textFile("data/ratings.txt").map(_.split(",")).map(x => org.apache.spark.mllib.recommendation.Rating(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim().toFloat))
    val model=ALS.train(ratings,50,10,0.01)
    val userId=200
    val k=5
    val topString=model.recommendProducts(userId,k)
    println(topString.mkString("\n"))
  }
}