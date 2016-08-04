package org.brave.spark.ml

import org.apache.spark._
import org.apache.spark.mllib._
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

import org.brave.spark.base.BaseConf
import org.brave.util.util._

object RecommandForAllUsers extends BaseConf {
  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.print(s"""
                          |Usage: RecommandForAllUsers <ModelPath> <MysqlTable>
        """.stripMargin)
      System.exit(1)
    }
    conf.setAppName("Recommandation")
    val sc = new SparkContext(conf)
    
    val modelpath = args(0)
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
            ).mkString
    }.saveAsTextFile("/data/txt/recResultForAllUsers0803")
    

  }
}