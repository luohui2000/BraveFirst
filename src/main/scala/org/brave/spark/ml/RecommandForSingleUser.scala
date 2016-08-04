package org.brave.spark.ml

import java.util.Properties

import org.apache.spark._
import org.apache.spark.mllib._
import org.apache.spark.sql._
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

import org.brave.spark.base.BaseConf
import org.brave.util.util._
import org.brave.spark.caseclass._

object RecommandForSingleUser extends BaseConf {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.print(s"""
                          |Usage: RecommandForAllUsers <ModelPath> <UserID>
        """.stripMargin)
      System.exit(1)
    }
    conf.setAppName("Recommandation")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    
    val modelpath = args(0)
    val userid = args(1).toInt
    val modelpath1 = "/user/root/model/myCollaborativeFilter20160802"
    val model = MatrixFactorizationModel.load(sc, modelpath1)
    import sqlContext.implicits._
    val recResult = model.recommendProducts(userid, 10).map { x =>  userid.toString()+"|"+x.product.toString()+"|"+x.rating.toString()}
    val recRDD = sc.parallelize(recResult)
    val recDF = recRDD.map ( _.split("|")).map(x=> Result(x(0).toInt,x(1).toInt,x(2).toDouble))
    val recDF2 = recDF.toDF()
    val prop = new Properties
    prop.put("username", "root")
    prop.put("password", "Spark@123")
    recDF2.write.jdbc("jdbc:mysql://master:3306/hive_db", "hive_db.user_movie_recommandation", prop)
  }
}