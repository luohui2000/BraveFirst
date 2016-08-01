package org.brave.util.demo

import org.apache.spark._
import org.brave.spark.base.BaseConf

/**
 * Created by yuchen
 * on 2016-07-14.
 *
 * ./hadoop fs -mkdir logs
 * ./hadoop fs -ls -R /
 * ./hadoop fs -put /home/jar/demo.json  /
 *  hdfs://master:9000/logs/demo.json
 *
 * 以本地local的方式直接运行不需要依赖spark集群
 */
object HDFSDemo extends BaseConf {
  def main(args: Array[String]) {

    conf.setAppName("RunHelloWorldOnLocalServer")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val df = sc.textFile("hdfs://master:9000/logs/demo.json")
    val count=df.flatMap(line => line.split(":")).map(word => (word,1)).reduceByKey(_+_)
    count.collect().foreach(println)

  }
}