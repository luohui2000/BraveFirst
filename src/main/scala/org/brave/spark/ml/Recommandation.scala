package org.brave.spark.ml

import scala.util.Random

import org.apache.spark._
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

import org.brave.spark.base.BaseConf
import org.brave.util.util._

/**
 * 运行完ETL后可以运行这个类。这个是一个实验性质的类，
 * 取某个表的数据提取特征向量，然后用ALS来训练一个模型。
 * BASED ON https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/mllib/RecommendationExample.scala
 */
object Recommandation extends BaseConf{
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.print(s"""
                          |Usage: Recommandation <inputUserId> <recommandResultNum>
        """.stripMargin)
      System.exit(1)
    }
    val inputUserId = args(0).trim().toInt
    val recommandResultNum = args(1).trim().toInt

    //随机产生一个userid和一个随机的推荐电影数量
    val randomNumber1 = scala.util.Random.nextInt().%(234934)
    val randomUserId = if (randomNumber1 > 0) randomNumber1 else -randomNumber1
    val randomNumber2 = scala.util.Random.nextInt().%(5)
    val randomResultNum = if (randomNumber2 > 0) randomNumber2 + 1 else -randomNumber2 + 1

    conf.setAppName("Recommandation")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    hc.sql("cache table ratings")
    hc.sql("cache table movies")
    //    val allUsers = hc.sql("select distinct(userid) from ratings")
    if (hc.sql(s"select userid from ratings where userid=$inputUserId").count() == 0) {
      println("Invalid user id!")
      println("User id ranges from 1 to 234934")
      sc.stop()
    }

    val ratings = hc.sql("select userid,movieid,rating from ratings")
    val ratingRDD = ratings.rdd.map(x =>
      Array(x.getInt(0), x.getInt(1), x.getFloat(2).toDouble)).map {
      _ match {
        case Array(userid, movieid, rating) => Rating(userid.toInt, movieid.toInt, rating.toDouble)
      }
    }
    ratingRDD.persist()

    val rank = 10
    val numIterations = 10
    val model = ALS.train(ratingRDD, rank, numIterations, 0.01)
    ratingRDD.unpersist(true)

    //为输入的用户推荐
    val recommandResult = model.recommendProducts(inputUserId, recommandResultNum)
    val recommandMovies = recommandResult.map { x => x.product.toString() }
    println(s"the recommanded movies for specified user NO.$inputUserId is :")
    //    recommandMovies.foreach { println }
    val recommandMovieIDs = recommandMovies.map(_.concat(",")).mkString
    hc.sql(s"select title from movies where movieId in (${recommandMovieIDs.substring(0, recommandMovieIDs.length - 1)})").collect().foreach(println)
    //为随机用户推荐
    val randomRecommandResult = model.recommendProducts(randomUserId, randomResultNum)
    val randomRecommandMovies = randomRecommandResult.map { x => x.product.toString() }
    println(s"the recommanded movies for a random user NO.$randomUserId is :")
    //    randomRecommandMovies.foreach { println }
    val randomRecommandMovieIDs = randomRecommandMovies.map(_.concat(",")).mkString
    hc.sql(s"select title from movies where movieId in (${randomRecommandMovieIDs.substring(0, randomRecommandMovieIDs.length - 1)})").collect().foreach(println)

    hc.sql("uncache table ratings")
    hc.sql("uncache table movies")

    val userAndMovie = ratingRDD.map {
      case Rating(user, product, rate) => (user, product)
    }

    val pred = model.predict(userAndMovie).map {
      case Rating(user, product, rate) => ((user, product), rate)
    }

    val ratesAndPreds = ratingRDD.map {
      case Rating(user, product, rate) => ((user, product), rate)
    }.join(pred)

    val MSE = ratesAndPreds.map {
      case ((user, product), (r1, r2)) =>
        val err = (r1 - r2)
        err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    val ct = new CalendarTool
    val today = ct.getToday

    //模型的保存和加载
        model.save(sc, s"model/myCollaborativeFilter$today")
    //    val safedModel = MatrixFactorizationModel.load(sc, s"model/myCollaborativeFilter$today")
  }
}