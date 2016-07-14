package org.brave.util.demo

import java.util.Properties

import org.apache.spark._
import org.brave.util.PropertiesUtil

object RunHelloWorldOnRemoteServer {
   var properties: Properties = PropertiesUtil.loadProperties()
   var sparkMaster: String = properties.getProperty("spark.master.remote")
   var sparkDriverMemory: String = properties.getProperty("spark.driver.memory")
  var demoFilePathRemote: String = properties.getProperty("demo.file.path.remote")
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster(sparkMaster)
    conf.set("spark.executor.memory", sparkDriverMemory)
    conf.setAppName("RunSparkOnRemoteServer")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val df = sqlContext.read.json(demoFilePathRemote)
    df.show()
    df.printSchema();
    df.registerTempTable("people")
    //var peopleName=sqlContext.sql("select  name,count(name) from demo group by name having(count(name)>0) ")
    var peopleName=sqlContext.sql("select  * from people")
    peopleName.collect().foreach(println)
  }
}