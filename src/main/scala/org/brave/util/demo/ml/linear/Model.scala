package org.brave.util.demo.ml.linear

import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LinearRegressionWithSGD, LabeledPoint}
import org.apache.spark.{SparkConf, SparkContext}
import org.brave.spark.base.BaseConf

/**
 * 线性回归
  * Created by yuchen
  * on 2016/8/10 0010.
  */
object Model extends BaseConf{
   def main(args: Array[String]) {
      conf.setAppName("Model")
      conf.setMaster("local[2]")
     val sc = new SparkContext(conf)
     val data=sc.textFile("data/keywordprice.txt")
     val testData=null
     val parsedData=data.map{
       line=>
         val parts=line.split(",")
         LabeledPoint(
           parts(1).toDouble,
           Vectors.dense(
             parts(0).split(" ").map(_.toDouble)
           )
         )
     }.cache()
     val modelpath=s"hdfs://master60:9000/logs/linear/model$now";
    val model=LinearRegressionWithSGD.train(parsedData,100,0.00000001);
    model.save(sc,modelpath);
   }
 }