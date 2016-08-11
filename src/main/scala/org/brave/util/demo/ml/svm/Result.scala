package org.brave.util.demo.ml.svm

import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD, LogisticRegressionModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import org.brave.spark.base.BaseConf

/**
  * Created by yuchen
  * on 2016/8/10 0010.
  */
object Result extends BaseConf{
   def main(args: Array[String]) {
      conf.setAppName("S")
      conf.setMaster("local[4]")
     val sc = new SparkContext(conf)
     val modelpath=s"hdfs://slave3:9000/logs/svm/model$now";
     val model = SVMModel.load(sc, modelpath)
     //val result =model2.predict(Vectors.dense(30.0,0.69,0.07,2.0,1.51,1.0,1.0,10.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.11,9.0,5.0,0.7,3.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3031.0,61.0,54.0,1.86,245.0,0.0,0.12))
     println("-----------------------------begin---------------------------------");
     println(model);
     println("------------------------------end----------------------------------");
     val result =model.predict(Vectors.dense(1000.0,291.5,0.4,3.0,0.93,3.0,3.0,202113.0,1781.0,0.88,674.02,0.38,24552.0,29.0,38.0,1.63,36.43,0.6,6.0,4.0,0.3,2.0,192.0,2.0,1.04,0.88,0.44,0.0,0.0,0.0,0.0,0.0,18.0,1090.0,22.0,11.0,1.88,68.0,0.0,0.0))
     println("-----------------------------begin---------------------------------");
     println(result);
     println("------------------------------end----------------------------------");
   }
 }