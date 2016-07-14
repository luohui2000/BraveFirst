package org.brave.spark.base

import java.util.Properties

import org.brave.util.PropertiesUtil


/**
 * Created by yuchen
 * on 2016-07-14.
 *
 * spark运行的基本参数
 * 详细请参见：src/main/resources/util.properties
 */
class BaseConf {
  var properties: Properties = PropertiesUtil.loadProperties("/util.properties")
  var sparkMasterRemote: String = properties.getProperty("spark.master.remote")
  var sparkMasterLocal: String = properties.getProperty("spark.master.local")
  var sparkDriverMemory: String = properties.getProperty("spark.driver.memory")
  var demoFilePathRemote: String = properties.getProperty("demo.file.path.remote")
  var demoFilePathLocal: String = properties.getProperty("demo.file.path.local")
}
