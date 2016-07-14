package org.brave.spark.ml

import org.apache.spark._
import org.apache.spark.sql._
import main.scala.org.brave.spark.base.BaseConf
import main.scala.org.brave.spark.caseclass._

object MovieTagging extends BaseConf {
  def main(args: Array[String]) {
    //before run this main class ,run movietag.sh first
    //movies with tag '(no genres listed)' is not included 
    conf.setAppName("MovieTagging")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._

    val rawMovieTag = sc.textFile("/user/hadoop/data/movietags.txt", 6).filter { !_.equals("(no genres listed)") }.map { _.replace("|", "\n") }.distinct().map(m =>MovieTag(m.trim()))
    val movieTag = rawMovieTag.toDF()
    
    movieTag.write.saveAsTable("mtag")
    movieTag.show()
    movieTag.count()
  }
}