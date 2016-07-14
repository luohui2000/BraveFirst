package org.brave.util.demo

import org.apache.spark._
import org.brave.spark.base.BaseConf

/**
 * 去spark集群上面以jar文件的方式运行
 *
 * 执行命令（运行）：
 * cd /opt/spark-1.6.1/bin
 * ./spark-submit  /opt/data/jar/BraveFirst.jar
 * 运行demo的结果：
 * +----+-------+
 * | age|   name|
 * +----+-------+
 * |null|Michael|
 * |  30|   Andy|
 * |  19| Justin|
 * +----+-------+
 * 访问页面地址：http://121.201.8.24:8080
 *
 * Created by yuchen
 * on 2016-07-14.
 */
object RunHelloWorldOnRemoteServer extends BaseConf {
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
    var peopleName = sqlContext.sql("select  * from people")
    peopleName.collect().foreach(println)
  }
}