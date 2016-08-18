package org.brave.util.demo.json

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
    conf.setMaster("spark://master60:7077")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
//    val df = sc.textFile("hdfs://master:9000/logs/demo.json")
    val df = sqlContext.read.json(demoFilePathLocal)
    //df.show()
   // df.printSchema();
   df.registerTempTable("shop3")
   var peopleName = sqlContext.sql(
     "select count(*) as day1  from shop3 where (shop3.visitTime/1000) >= 1467302400 and (shop3.visitTime/1000) <= 1467388800")
    peopleName.collect().foreach(println)


    //val count=file.flatMap(line => line.split(" ")).map(word => (word,1)).reduceByKey(_+_)

  }
}