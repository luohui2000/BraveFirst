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
    if (args.length < 4) {
      System.err.print(s"""
                          |Usage: RecommandForAllUsers <ModelPath> <UserID> <username> <password>
        """.stripMargin)
      System.exit(1)
    }
    conf.setAppName("RecommandationOne")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val user = args(2)
    val password = args(3)
    
    val c = new CalendarTool
    val last_upadte_time = c.getCurrentTime
    val modelpath = args(0)
    val userid = args(1).toInt
    val modelpath1 = "/user/root/model/myCollaborativeFilter20160802"
    val model = MatrixFactorizationModel.load(sc, modelpath)
    import sqlContext.implicits._
    val recResult = model.recommendProducts(userid, 12).map{ x =>  userid.toString()+"|"+x.product.toString()+"|"+x.rating.toString()}
    val recRDD = sc.parallelize(recResult)
    val recDF = recRDD.map ( _.split('|')).map(x=> Result(x(0).toInt,x(1).toInt,x(2).toDouble,last_upadte_time))
    val recDF2 = recDF.toDF()
    recDF2.first()
    recDF2.printSchema()
    
    val prop = new Properties
    prop.put("username", user)
    prop.put("password", password)
    prop.put("driver", "com.mysql.jdbc.Driver")
    recDF2.write.mode(SaveMode.Append).jdbc("jdbc:mysql://master:3306/hive_db", "hive_db.user_movie_recommandation", prop)
  }
}