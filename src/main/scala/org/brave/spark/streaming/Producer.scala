package org.brave.spark.streaming

import org.apache.spark._
import java.util.Properties
import scala.io._
import kafka.producer._
import org.apache.kafka.clients.producer._

import org.brave.spark.base.BaseConf
import org.brave.util.util.CalendarTool

/*
 * 把producer通过spark-submit提交到Spark集群运行，即可正常发送消息。
 * 如果以java -jar的方式，会出现异常com.typesafe.config.ConfigException$Missing: No configuration setting found for key 'akka.version'
 * 
 */ 
object Producer extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("Producer")
    val sc = new SparkContext(conf)
    val Array(brokers, topic, messagesPerBatch) = Array("master60:9092", "test", "200")
    val props = new Properties()
    props.put("bootstrap.servers", "master60:9092")
    props.put("acks", "all")
    props.put("topic", "test")
    //    props.put("retries", 0)
    //    props.put("batch.size", "16384")
    //    props.put("linger.ms", "1")
    //    props.put("buffer.memory", 33554432)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val file = sc.textFile("/data/ratings_streaming")
    val recordNum = file.count().toInt
    val batchNum = recordNum/messagesPerBatch.toInt
    val fileP = file.repartition(batchNum)
    val filePA = fileP.partitions
    val c = new CalendarTool
    val arr1 = Array(0,1,2)
    val e2 = arr1(2)

    //正常应该是while(true)，让这个producer一直读取某个文件夹，并发送更新的记录到队列里
    for(i<- 0 to batchNum){
      val time = c.getCurrentTime
      val p = filePA.apply(i).toString()
      val str =time+p
      val messages = new ProducerRecord[String, String](topic, str)
      producer.send(messages)
      println(messages)
      Thread.sleep(messagesPerBatch.toLong)
    }
  }
}