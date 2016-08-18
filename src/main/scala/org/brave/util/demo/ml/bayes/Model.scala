package org.brave.util.demo.ml.bayes


import org.apache.spark.mllib.classification.{NaiveBayes, LogisticRegressionWithSGD}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
import org.brave.spark.base.BaseConf

/**
 *  贝叶斯分类
  * Created by yuchen
  * on 2016/8/10 0010.
  */
object Model extends BaseConf{
   def main(args: Array[String]) {


      conf.setAppName("Model")
      conf.setMaster("local[4]")
     val sc = new SparkContext(conf)
     val data=sc.textFile("data/keywordclean.txt")
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

   // println(parsedData);

     val modelpath=s"hdfs://slave3:9000/logs/bayes/model$now";
    val model=NaiveBayes.train(parsedData,lambda=1.0);
    model.save(sc,modelpath);

   }
 }