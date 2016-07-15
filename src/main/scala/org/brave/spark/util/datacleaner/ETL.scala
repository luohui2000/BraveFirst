package org.brave.spark.util.datacleaner

import org.apache.spark._
import org.brave.spark.base.BaseConf
import org.brave.spark.caseclass.{Links, Movies, Ratings, Tags}

object ETL extends BaseConf {
  def main(args: Array[String]) {
/*    要运行这个类需要先做2件：
    1.把movielens的txt数据放到一个data目录下，建议把data文件夹建在$SPARK_HOME所在目录，也就是Spark的安装路径。
    2.配置Spark-SQL连接hive（无需安装hive,需要有HDFS）.可参考http://spark.apache.org/docs/latest/sql-programming-guide.html#hive-tables
           运行这个程序会先过滤掉一部分的一场数据，做一个简单的清洗，然后在spark-sql里建立4张表，分别是movies，ratings，links，tags，具体表的数据和字段可以看开发日记，里面有提，也可以看docs文件夹里的readme.txt
*/
    var filepath = "data";
    conf.setAppName("ETL for files from " + filepath)
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    import sqlContext.implicits._
    val movies = sc.textFile("data/movies.txt").map(_.split(",")).map(x => Movies(x(0).trim().toInt,
      x(1).trim(),
      x(2).trim())).toDF()
    movies.write.saveAsTable("movies")

    val ratings = sc.textFile("data/ratings.txt").map(_.split(",")).map(x => Ratings(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim().toFloat,
      x(3).trim().toDouble)).toDF()
    ratings.write.saveAsTable("ratings")

    val links = sc.textFile("data/links.txt").filter {
      !_.endsWith(",")
    }.map(_.split(",")).map(x => Links(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim().toInt)).toDF()
    links.write.saveAsTable("links")

    val tags = sc.textFile("data/tags.txt").filter {
      !_.endsWith(",")
    }.filter {
      !_.toString().contains('\"')
    }.map(_.split(",")).map(x => Tags(x(0).trim().toInt,
      x(1).trim().toInt,
      x(2).trim(),
      x(3).trim().toDouble)).toDF()
    tags.write.saveAsTable("tags")
  }
}