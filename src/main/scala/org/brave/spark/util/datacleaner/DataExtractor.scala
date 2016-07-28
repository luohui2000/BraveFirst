package org.brave.spark.util.datacleaner

import org.apache.spark._
import org.brave.spark.base.BaseConf

/**
 * 这个类是抽一部分的ratings表数据来做当作不同的数据集。
 * 这个比例是80%：10%：10%，也就是80%的数据做训练数据集，10%的做离线的测试数据集，10%的做实时测试数据集
 *
 */
object DataExtractor extends BaseConf {
  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.print(s"""
        |Usage: DataExtractor <trainingDataPercent> <batchDataPercent> <streamingDataPercent> <tableName> <orderbyField>
        |example: DataExtractor 0.8 0.1 0.1 ratings timestamp
        """.stripMargin)
      System.exit(1)
    }
    val trainingDataPercent = 0.8
    val batchDataPercent = 0.1
    val streamingDataPercent = 0.1
    val tableName = "ratings"
    val orderbyField = "timestamp"
/*    val trainingDataPercent = args(0).toDouble
    val batchDataPercent = args(1).toDouble
    val streamingDataPercent = args(2).toDouble
    val tableName = args(3)
    val orderbyField = args(4)*/
    conf.setAppName(s"Extract Data from table $tableName")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)

    //ratings数据拆分
    hc.sql(s"cache table $tableName")
    val dataTable = hc.sql(s"select * from $tableName")
    val dataCount = hc.sql(s"select count(*) from $tableName")
    val trainingDataCount = dataCount.collect().apply(0).getLong(0).*(trainingDataPercent).toInt
    val batchDataCount = dataCount.collect().apply(0).getLong(0).*(batchDataPercent).toInt
    val streamingDataCount = dataCount.collect().apply(0).getLong(0).*(streamingDataPercent).toInt
    val trainingData = hc.sql(s"select * from $tableName order by $orderbyField desc limit $streamingDataCount")
    val batchData = hc.sql(s"select * from $tableName order by $orderbyField asc limit $batchDataCount")
    val streamingData = dataTable.except(trainingData).except(batchData)
    
    //验证3个数据集的切分是否正确，看他们的数据是否有交集,0就是对的。这个步骤会很慢
/*    trainingData.persist()
    batchData.persist()
    streamingData.persist()
    val i1 = trainingData.intersect(batchData).count()
    val i2 = trainingData.intersect(streamingData).count()
    val i3 = streamingData.intersect(batchData).count()
    trainingData.unpersist()
    batchData.unpersist()
    streamingData.unpersist()*/
    
    //保存成新表
    hc.sql(s"drop table ${tableName}_training")
    hc.sql(s"drop table ${tableName}_batch")
    hc.sql(s"drop table ${tableName}_streaming")
    trainingData.write.saveAsTable(s"${tableName}_training")
    batchData.write.saveAsTable(s"${tableName}_batch")
    streamingData.write.saveAsTable(s"${tableName}_streaming")
    streamingData.write.text(s"/data/${tableName}_streaming")
  }
}