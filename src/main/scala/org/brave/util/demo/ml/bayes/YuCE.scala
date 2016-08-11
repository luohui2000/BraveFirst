package org.brave.util.demo.ml.bayes

import org.apache.spark.mllib.classification.{NaiveBayesModel, LogisticRegressionModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by yuchen
  * on 2016/8/10 0010.
  */
object YuCE {
   def main(args: Array[String]) {

     val conf = new SparkConf()
     conf.set("spark.driver.memory", "5g")

      conf.setAppName("Model")
      conf.setMaster("local[4]")
     val sc = new SparkContext(conf)
 //    val data=sc.textFile("data/33.txt")
 //    val testData=null
 //    val parsedData=data.map{
 //      line=>
 //        val parts=line.split(",")
 //        LabeledPoint(
 //          parts(1).toDouble,
 //          Vectors.dense(
 //            parts(0).split(" ").map(_.toDouble)
 //          )
 //        )
 //    }.cache()

   // println(parsedData);

 //    val model=LinearRegressionWithSGD.train(parsedData,2,0.1);
     val modelpath="hdfs://slave3:9000/logs/model2016813";
    // val model=LogisticRegressionWithSGD.train(parsedData,50);
    // model.save(sc,modelpath);
 //    println(model.weights.size);
 //    println(model.weights);
 //    println(model.weights.toArray.filter(_!=0).size)

     val model2 = NaiveBayesModel.load(sc, modelpath)
     //val result =model2.predict(Vectors.dense(30.0,0.69,0.07,2.0,1.51,1.0,1.0,10.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.11,9.0,5.0,0.7,3.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3031.0,61.0,54.0,1.86,245.0,0.0,0.12))
     println("-----------------------------begin---------------------------------");
     println(model2);
     println("------------------------------end----------------------------------");
     val result =model2.predict(Vectors.dense(1000.0,291.5,0.4,3.0,0.93,3.0,3.0,202113.0,1781.0,0.88,674.02,0.38,24552.0,29.0,38.0,1.63,36.43,0.6,6.0,4.0,0.3,2.0,192.0,2.0,1.04,0.88,0.44,0.0,0.0,0.0,0.0,0.0,18.0,1090.0,22.0,11.0,1.88,68.0,0.0,0.0))
     println("-----------------------------begin---------------------------------");
     println(result);
     println("------------------------------end----------------------------------");
   }
 }