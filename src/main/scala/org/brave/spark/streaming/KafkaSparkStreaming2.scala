package org.brave.spark.streaming

import java.util.Properties

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.sql.{ Row, SQLContext }
import org.apache.spark.sql.types.{ DoubleType, IntegerType, StructField, StructType, StringType }
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.mllib.recommendation.{ MatrixFactorizationModel, Rating }
import org.json.JSONObject

import org.brave.spark.base.BaseConf

/**
 * Created by San on 2016/8/2.
 */
object KafkaSparkStreaming2 extends BaseConf {

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.print(s"""
                          |Usage: KafkaSparkStreaming <zkQuorum> <group> <topics> <numThreads>
        """.stripMargin)
      System.exit(1)
    }

    val Array(zkQuorum, group, topics, numThreads) = args
    println("zkQuorum:" + zkQuorum)
    println("group:" + group)
    println("topics:" + topics)
    println("numThreads:" + numThreads)
    conf.setAppName("KafkaStreaming")
    val storageLevel = StorageLevel.DISK_ONLY
    val ssc = new StreamingContext(conf, Seconds(batchInterval.toInt / 1000))
    val checkpointDirectory = "/checkpoint/KafkaStream"
    ssc.checkpoint(checkpointDirectory)
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val kafkaStream = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap, storageLevel)
    val schema = new StructType(
      Array(StructField("userid", IntegerType, true),
        StructField("productid", StringType, true)))

    kafkaStream.foreachRDD { rdd =>
      val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
      val ALSModel = MatrixFactorizationModel.load(rdd.sparkContext, "/user/hadoop/model/myCollaborativeFilter20160802/")
      val recDF = sqlContext.read.json(rdd.values)
      val recTuple2RDD = sqlContext.read.json(rdd.values).map { x => Tuple2(x.getLong(3).toInt, x.getLong(0).toInt) }
      recDF.persist()
      recTuple2RDD.persist()

      //实时预测用户对电影的评分
      /*      val predictedRatings = ALSModel.predict(recTuple2RDD)
      predictedRatings.collect.foreach(println)*/

      //实时为用户推荐10部电影,用迭代器的方式，避免在一个RDD的transformation里调用recommandproduct时报异常的问题
      recDF.printSchema()
      val useridRDD = recDF.select("userid").map(_.mkString)
      val useridItr = useridRDD.toLocalIterator
      var tmpuserid = ""
      while (useridItr.hasNext) {
        tmpuserid = useridItr.next()
        println("the user id is:" + tmpuserid)
        val result = ALSModel.recommendProducts(tmpuserid.trim.toInt, 10)
        println("The recommanded Movies for user " + tmpuserid + " are:\n")
        for(i <- 0 to 9){
          println("MovieID:" +  result(i).product + "|Rating:" + result(i).rating)
        }
      }

      //实时为用户推荐10部电影,存在在map中又调用了transformation的问题。
      /*      val getRecResult = org.apache.spark.sql.functions.udf((x: Long) =>
        ALSModel.recommendProducts(x.toInt, 10).mkString
        )
      recDF.printSchema()
      println("input data:")
      recDF.collect.foreach(println)
      val resultDF2 = recDF.map { x =>
        ALSModel.recommendProducts(x.getLong(3).toInt, 10)
      }
      val resultDF = recDF.withColumn("recommandresult", getRecResult(recDF.col("userid")))
      println("output result:")
      resultDF.collect.foreach(println)
      resultDF2.collect.foreach(println)*/

      recDF.unpersist()
      recTuple2RDD.unpersist()
    }
    ssc.start()
    ssc.awaitTermination()
  }

  def dbProperties(username: String, password: String): Properties = {
    val prop = new Properties()
    prop.setProperty("username", username)
    prop.setProperty("password", password)
    prop
  }

  def jsonUserProduct(json: String): Int = {
    val jsonob = new JSONObject(json)
    jsonob.getInt("userid")
  }
}
