package org.brave.spark.base

import java.util.Properties


import org.apache.spark.SparkConf
import org.brave.util.util.{CalendarTool, PropertiesUtil}


/**
 * Created by yuchen
 * on 2016-07-14.
 *
 * spark运行的基本参数
 * 详细请参见：src/main/resources/util.properties
 * 写到配置文件的好处：不需要编译
 */
class BaseConf {
  val properties: Properties = PropertiesUtil.loadProperties("/util.properties")
  /**
   * 青云的集群URL
   *
   */
  val sparkMasterRemote: String = properties.getProperty("spark.master.remote")
  /**
   * 本地的集群URL
   */
  val sparkMasterRemoteLocal: String = properties.getProperty("spark.master.remote.local")
    /**
   * 本地的集群URL2
   */
  val sparkMasterRemoteLocal2: String = properties.getProperty("spark.master.remote.local2")
  /**
   * 本地local
   */
  val sparkMasterLocal: String = properties.getProperty("spark.master.local")
  /**
   * spark运行内存可以是m为单位，也可以是以g为单位
   */
  val sparkDriverMemory: String = properties.getProperty("spark.driver.memory")
  /**
   * 集群demo文件的地址
   */
  val demoFilePathRemote: String = properties.getProperty("demo.file.path.remote")
  /**
   * 本地demo文件的地址
   */
  val demoFilePathLocal: String = properties.getProperty("demo.file.path.local")
  /**
   * Hadoop的安装路径
   */
  val hadoop_home: String = properties.getProperty("hadoop.home")
    /**
   * kafka producer每条发送记录的条数
   */
  val messagesPerBatch: String = properties.getProperty("kafka.messagePerBatch")
    /**
   * Hadoop的安装路径
   */
  val batchInterval: String = properties.getProperty("kafka.batch.interval")

  val conf = new SparkConf()
  conf.set("spark.driver.memory", sparkDriverMemory)


  val c = new CalendarTool
  val now=c.getToday

}
