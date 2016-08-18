package org.brave.spark.ml

import java.util.Properties

import org.apache.spark._
import org.apache.spark.mllib._
import org.apache.spark.sql._
import org.apache.spark.mllib.recommendation._

import org.brave.spark.base.BaseConf
import org.brave.util.util._
import org.brave.spark.caseclass._

object RecommandForMultiUsers extends BaseConf {
  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.print(s"""
                          |Usage: RecommandForMultiUsers <ModelPath> <SubsetUserCount> <MysqlHostName> <username> <password>
        """.stripMargin)
      System.exit(1)
    }
    conf.setAppName("RecommandationMulti")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    val modelpath = args(0).mkString
    val someUsers = args(1).toInt
    val mysqlHostName = args(2)
    val user = args(3)
    val password = args(4)
    println(modelpath)
    println(someUsers)

    //通过recommendProductsForUsers方法来跑，集群无法成功运行起来
    /*    val modelpath = args(0)
    val modelpath1 = "/user/root/model/myCollaborativeFilter20160802"
    val model = MatrixFactorizationModel.load(sc, modelpath1)
    val recResultForAllUsers = model.recommendProductsForUsers(10).map {x =>
      Array(Tuple2(x._1,x._2(0)),
            Tuple2(x._1,x._2(1)),
            Tuple2(x._1,x._2(2)),
            Tuple2(x._1,x._2(3)),
            Tuple2(x._1,x._2(4)),
            Tuple2(x._1,x._2(5)),
            Tuple2(x._1,x._2(6)),
            Tuple2(x._1,x._2(7)),
            Tuple2(x._1,x._2(8)),
            Tuple2(x._1,x._2(9))
            )
    }.repartition(20)
    recResultForAllUsers.first()
    recResultForAllUsers.saveAsTextFile("/data/txt/recResultForAllUsers0803")*/

    val users = hc.sql(s"select distinct(userid) from ratings limit $someUsers")
    users.show()
    val itr = users.rdd.map { x => x.getInt(0) }.toLocalIterator
    val model = MatrixFactorizationModel.load(sc, modelpath)

    println("==================write rec rsult to mysql=====================")
    while (itr.hasNext) {
      val userid = itr.next()
      println(userid)
      saveRecResultToMysql(userid, model, sc, sqlContext,mysqlHostName,user,password)
    }
  }
  
  def saveRecResultToMysql(userid: Int, model: MatrixFactorizationModel, sc: SparkContext, sqlContext: SQLContext,mysqlHostName:String,user:String,password:String) {
    val c = new CalendarTool
    val last_upadte_time = c.getCurrentTime
    val recResult = model.recommendProducts(userid, 12).map { x => userid.toString() + "|" + x.product.toString() + "|" + x.rating.toString() }
    val recRDD = sc.parallelize(recResult)
    import sqlContext.implicits._
    val recDF = recRDD.map(_.split('|')).map(x => Result(x(0).toInt, x(1).toInt, x(2).toDouble, last_upadte_time))
    val recDF2 = recDF.toDF()
    val prop = new Properties
    prop.put("driver", "com.mysql.jdbc.Driver")
    prop.put("user", user)
    prop.put("password", password)
    recDF2.write.mode(SaveMode.Append).jdbc(s"jdbc:mysql://${mysqlHostName}:3306/hive_db", "hive_db.user_movie_recommandation", prop)
  }
}