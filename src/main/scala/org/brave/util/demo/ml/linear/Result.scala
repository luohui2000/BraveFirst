package org.brave.util.demo.ml.linear

import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.{SparkConf, SparkContext}
import org.brave.spark.base.BaseConf

/**
  * Created by yuchen
  * on 2016/8/10 0010.
  */
object Result extends BaseConf{
   def main(args: Array[String]) {
      conf.setAppName("Model")
      conf.setMaster("local[4]")
     val sc = new SparkContext(conf)
     val modelpath=s"hdfs://master60:9000/logs/linear/model$now";
     val model2 = LinearRegressionModel.load(sc, modelpath)
     //val result =model2.predict(Vectors.dense(30.0,0.69,0.07,2.0,1.51,1.0,1.0,10.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.11,9.0,5.0,0.7,3.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3031.0,61.0,54.0,1.86,245.0,0.0,0.12))
     println("-----------------------------begin---------------------------------");
     println(model2);
     println("------------------------------end----------------------------------");
     val result =model2.predict(Vectors.dense(600.0,516.53,2.02,3.0,2.44,1.0,3.0,40537.0,91.0,0.22,327.47,3.6,527.0,3.0,1.0,3.3,1.61,2.78,7.0,8.0,10.4,1.0,141.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,59.0,2236.0,13.0,231.0,0.53,142.0,1.98))
     println("-----------------------------begin---------------------------------");
     println(result);
     println("------------------------------end----------------------------------");
   }
 }