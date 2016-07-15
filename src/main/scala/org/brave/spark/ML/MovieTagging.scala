package org.brave.spark.ml

import org.apache.spark._
import org.brave.spark.base.BaseConf
import org.brave.spark.caseclass.MovieTag

object MovieTagging extends BaseConf {
  def main(args: Array[String]) {
    //before run this main class ,run movietag.sh first
    //movies with tag '(no genres listed)' is not included 
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