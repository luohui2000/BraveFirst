package org.brave.spark.streaming

import java.util.Properties

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.sql.{ Row, SQLContext }
import org.apache.spark.sql.types.{ DoubleType, IntegerType, StructField, StructType,StringType }
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
//    if (args.length < 5) {
//      System.err.print(s"""
//                          |Usage: KafkaSparkStreaming <zkQuorum> <group> <topics> <numThreads>
//        """.stripMargin)
//      System.exit(1)
//    }

    val Array(zkQuorum, group, topics, numThreads) = args
    println("zkQuorum:"+zkQuorum)
    println("group:"+group)
    println("topics:"+topics)
    println("numThreads:"+numThreads)
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
      rdd.keys.first()
      sqlContext.read.json(rdd.keys).printSchema()
      val ratingRdd = sqlContext.read.json(rdd.keys).foreach(r => 
        println(r)
//        println(ALSModel.predict(r.getInt(0),5))
      )
//      sqlContext.createDataFrame(ratingRdd, schema).write.jdbc(url, table, connectionProperties)
//      sqlContext.createDataFrame(ratingRdd, schema).collect().foreach { println}
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
