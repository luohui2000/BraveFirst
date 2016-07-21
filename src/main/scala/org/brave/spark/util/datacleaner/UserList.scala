package org.brave.spark.util.datacleaner

import org.apache.spark.SparkContext
import org.apache.spark._
import org.brave.spark.base.BaseConf
import org.brave.spark.base.BaseConf
import org.brave.spark.caseclass._
import org.brave.spark.util.datacleaner.ETL._

import scala.util.matching.Regex

/**
  * Created by qingyanghong on 21/07/2016.
  *
  * 过滤非数字id, 创建用户表
  *
  */

object UserList extends BaseConf {
  def main(args: Array[String]) {
    val filepath = "data"

    conf.setMaster(sparkMasterRemote)
    conf.setAppName("Create user list from " + filepath)
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    import sqlContext.implicits._

    val users = sc.textFile("data/ratings.txt").map(_.split(","))
        .map(x => x(0).trim().toInt)
        .distinct()
        .filter(isNum)
        .map(z => User(z)).toDF()
    users.write.parquet("/data/users")
    hc.sql("CREATE TABLE IF NOT EXISTS users (userId int)  STORED AS PARQUET")
    hc.sql("LOAD DATA INPATH 'hdfs://data/users' OVERWRITE INTO TABLE users")

  }

  def isNum : Int => Boolean = id => {
    val regex = new Regex("""([0-9]+)""")
    id match{
      case regex(num) => true
      case _=> false
    }
  }
}