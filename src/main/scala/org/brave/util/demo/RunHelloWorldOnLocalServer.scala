package org.brave.util.demo

import org.apache.spark._
import org.brave.spark.base.BaseConf


object RunHelloWorldOnLocalServer extends BaseConf{
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster(sparkMasterLocal)
    conf.set("spark.executor.memory", sparkDriverMemory)
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