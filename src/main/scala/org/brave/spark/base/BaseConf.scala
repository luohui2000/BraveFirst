package org.brave.spark.base

import java.util.Properties


import org.apache.spark.SparkConf
import org.brave.util.util.PropertiesUtil


/**
 * Created by yuchen
 * on 2016-07-14.
 *
 * spark运行的基本参数
 * 详细请参见：src/main/resources/util.properties
 * 写到配置文件的好处：不需要编译
 */
class BaseConf {
  var properties: Properties = PropertiesUtil.loadProperties("/util.properties")
  /**
   * 青云的集群URL
   *
   */
  var sparkMasterRemote: String = properties.getProperty("spark.master.remote")
  /**
   * 本地的集群URL
   */
  var sparkMasterRemoteLocal: String = properties.getProperty("spark.master.remote.local")
  /**
   * 本地local
   */
  var sparkMasterLocal: String = properties.getProperty("spark.master.local")
  /**
   * spark运行内存可以是m为单位，也可以是以g为单位
   */
  var sparkDriverMemory: String = properties.getProperty("spark.driver.memory")
  /**
   * 集群demo文件的地址
   */
  var demoFilePathRemote: String = properties.getProperty("demo.file.path.remote")
  /**
   * 本地demo文件的地址
   */
  var demoFilePathLocal: String = properties.getProperty("demo.file.path.local")

  val conf = new SparkConf()
  conf.setMaster(sparkMasterLocal)
  conf.set("spark.executor.memory", sparkDriverMemory)
}
