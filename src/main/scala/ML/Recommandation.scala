package main.scala.ML

import org.apache.spark._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.mllib.linalg.{ Vector, Vectors }
import org.apache.spark.sql.Row

object Recommandation {
  def main(args: Array[String]) {
    val sparkMasterUrlDev = "spark://master60:7077"
    val sparkMasterUrlLocal = "local[2]"
    val conf = new SparkConf().setAppName("Recommandation").setMaster(sparkMasterUrlDev)
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    hc.sql("cache table movies")
    hc.sql("cache table ratings")
    hc.sql("cache table links")
    hc.sql("cache table tags")
    hc.sql("alter table ratings change timestamp time double")
    
//    val ratings = hc.sql("select * from ratings")
    val ratings = hc.sql("select * from ratings group by userId,movieid,rating,timestamp")
    ratings.printSchema()
    ratings.show()

    val lr = new LogisticRegression().setFeaturesCol("movieId")
    println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
    lr.setMaxIter(10).setRegParam(0.01)
    
    val model1 = lr.fit(ratings)
    println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)

  }
}