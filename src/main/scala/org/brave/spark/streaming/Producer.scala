package org.brave.spark.streaming

import java.util.Properties
import scala.io._
import kafka.producer._

import org.brave.spark.base.BaseConf

object Producer extends BaseConf {
  def main(args: Array[String]){
    val Array(brokers, topic,messagesPerSec, wordsPerMessage) = Array("master:9092", "test","200","200")
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")

    val config = new ProducerConfig(props)
    val producer = new Producer[String, String](config)
    
    while (true) {
      val messages =  new KeyedMessage[String, String](topic, Source.fromFile("sample.txt").getLines.mkString(" "))
      producer.send(messages)
      println(messages)
      Thread.sleep(100)
    }
  }

}