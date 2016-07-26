package org.brave.spark.streaming

import java.util.Properties
import scala.io._
import kafka.producer._
import org.apache.kafka.clients.producer._

import org.brave.spark.base.BaseConf

object Producer{
  def main(args: Array[String]) {
    val Array(brokers, topic, messagesPerSec, wordsPerMessage) = Array("master:9092", "test", "200", "200")
    val props = new Properties()
    props.put("bootstrap.servers", "master60:9092")
    props.put("acks", "all")
//    props.put("retries", 0)
//    props.put("batch.size", "16384")
//    props.put("linger.ms", "1")
//    props.put("buffer.memory", 33554432)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    while (true) {
      val messages = new ProducerRecord[String, String](topic, Source.fromFile("E://ratings_streaming//").getLines.mkString(" "))
      producer.send(messages)
      println(messages)
      Thread.sleep(100)
    }
  }

}