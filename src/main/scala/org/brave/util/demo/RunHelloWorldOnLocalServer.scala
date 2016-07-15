package org.brave.util.demo

import org.apache.spark._
import org.brave.spark.base.BaseConf

/**
 * Created by yuchen
 * on 2016-07-14.
 *
 * 以本地local的方式直接运行不需要依赖spark集群
 */
object RunHelloWorldOnLocalServer extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("RunHelloWorldOnLocalServer")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val df = sqlContext.read.json(demoFilePathLocal)
    df.show()
    df.printSchema();
    df.registerTempTable("demo")
    var peopleName = sqlContext.sql("select  *  from demo  ")
    peopleName.collect().foreach(println)
  }
}