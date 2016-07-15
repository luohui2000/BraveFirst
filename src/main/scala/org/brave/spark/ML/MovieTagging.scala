package org.brave.spark.ml

import org.apache.spark._
import org.brave.spark.base.BaseConf
import org.brave.spark.caseclass.MovieTag

object MovieTagging extends BaseConf {
  def main(args: Array[String]) {
    //运行这个类之前需要先运行src/main/bash/org/brave/scripts里的movietag.sh
    //用来提取电影的标签，并做成一张表放到Spark-sql里
    conf.setAppName("MovieTagging")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val rawMovieTag = sc.textFile("/user/hadoop/data/movietags.txt", 6).filter { !_.equals("(no genres listed)") }.map { _.replace("|", "\n") }.distinct().map(m =>MovieTag(m.trim()))
//    val movieTag = rawMovieTag.toDF()
//
//    movieTag.write.saveAsTable("mtag")
//    movieTag.show()
//    movieTag.count()
  }
}