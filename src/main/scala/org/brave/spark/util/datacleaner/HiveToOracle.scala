package org.brave.spark.util.datacleaner

import org.apache.spark._
import org.apache.spark.sql._
import org.brave.spark.base.BaseConf

/*
 * 这是个示例
 * 预先需要青云能访问到ORACLE，程序放到青云上跑
 * 
*/
object HiveToOracle extends BaseConf {
  def main(args: Array[String]) {
    conf.setMaster(sparkMasterLocal)
        val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    val movies = hc.sql("select * from movies")
    val jdbcUrl = "jdbc:oracle://****"
    val database = "yourDB.yourTable"
    val username = "yourusername"
    val password = "yourpassword"
    movies.write.format("jdbc").options(
      Map("url" -> jdbcUrl,
        "dbtable" -> database,
        "driver" -> "com.oracle.jdbc.Driver",
        "username" -> username,
        "password" -> password)).save()
  }
}