package org.brave.spark.streaming

import org.apache.spark._
import java.util.Properties
import scala.io._
import kafka.producer._
import org.apache.kafka.clients.producer._

import org.brave.spark.base.BaseConf
import org.brave.util.util.CalendarTool

/*
 * 运行此类之前，需要先运行DataExtractor类。
 * 读取DataExtractor产生的ratings_streaming数据，把它按照一定的速率打入到kafka消息队列里。
 * 把producer通过spark-submit提交到Spark集群运行，即可正常发送消息。通过Producer.sh来运行。
 * 如果以java -jar的方式，会出现异常com.typesafe.config.ConfigException$Missing: No configuration setting found for key 'akka.version'
 * 
 */
object Producer extends BaseConf {
  def main(args: Array[String]) {
    conf.setAppName("Producer")
    val sc = new SparkContext(conf)
    val Array(brokers, topic) = Array("master60:9092", "test")
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
    val c = new CalendarTool

    while(true) {
      val itr = file.toLocalIterator
      while (itr.hasNext) {
        val time = c.getCurrentTime
        val r = itr.next()
        val str = time + "\n" + r
        val messages = new ProducerRecord[String, String](topic, str)
        println(messages)
        producer.send(messages)
        Thread.sleep(batchInterval.toLong)
      }
    }
  }
}