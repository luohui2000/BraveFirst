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

    val links = hc.sql("select * from links")
    links.printSchema()

    val lr = new LogisticRegression().setFeaturesCol("movieId")
    println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
    lr.setMaxIter(10).setRegParam(0.01)
    
    val model1 = lr.fit(links)
    println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)

  }
}