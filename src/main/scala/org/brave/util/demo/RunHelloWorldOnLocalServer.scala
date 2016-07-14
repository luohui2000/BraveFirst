package org.brave.util.demo

import java.util.Properties


import org.apache.spark._
import org.brave.util.PropertiesUtil


object RunHelloWorldOnLocalServer {
  var properties: Properties = PropertiesUtil.loadProperties()
  var sparkMaster: String = properties.getProperty("spark.master.local")
  var sparkDriverMemory: String = properties.getProperty("spark.driver.memory")
  var demoFilePathLocal: String = properties.getProperty("demo.file.path.local")

  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster(sparkMaster)
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