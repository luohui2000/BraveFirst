package org.brave.util.demo

import org.apache.spark._
import org.brave.spark.base.BaseConf

object RunHelloWorldOnRemoteServer extends BaseConf{
  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster(sparkMasterRemote)
    conf.set("spark.executor.memory", sparkDriverMemory)
    conf.setAppName("RunHelloWorldOnRemoteServer")
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