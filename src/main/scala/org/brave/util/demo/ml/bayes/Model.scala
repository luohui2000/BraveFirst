package org.brave.util.demo.ml.bayes


import org.apache.spark.mllib.classification.{NaiveBayes, LogisticRegressionWithSGD}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yuchen
  * on 2016/8/10 0010.
  */
object Model {
   def main(args: Array[String]) {

     val conf = new SparkConf()
     conf.set("spark.driver.memory", "5g")

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

     val modelpath="hdfs://slave3:9000/logs/model2016813";
    val model=NaiveBayes.train(parsedData,lambda=1.0);
    model.save(sc,modelpath);

   }
 }