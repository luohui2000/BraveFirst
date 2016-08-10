package org.brave.spark.util.datacleaner

import org.apache.spark._
import org.apache.spark.sql._

/**
 * 这个类是抽一部分的ratings表数据来做当作不同的数据集。
 * 这个比例是80%：10%：10%，也就是80%的数据做训练数据集，10%的做离线的测试数据集，10%的做实时测试数据集
 *
 */
object DataExtractor {
  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.print(s"""
        |Usage: DataExtractor <trainingDataPercent> <batchDataPercent> <streamingDataPercent> <tableName> <orderbyField>
        |example: DataExtractor 0.8 0.1 0.1 ratings timestamp
        """.stripMargin)
      System.exit(1)
    }
    val trainingDataPercent = args(0).toFloat
    val batchDataPercent = args(1).toFloat
    val streamingDataPercent = args(2).toFloat
    val tableName = args(3)
    val orderbyField = args(4)
    /*    val trainingDataPercent = 0.8
    val batchDataPercent = 0.1
    val streamingDataPercent = 0.1
    val tableName = "ratings"
    val orderbyField = "timestamp"*/
    val conf = new SparkConf()
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
    val orderByDFAsc = hc.sql(s"select * from $tableName order by $orderbyField asc")
    val orderByDFDesc = hc.sql(s"select * from $tableName order by $orderbyField desc")
    orderByDFDesc.write.mode(SaveMode.Overwrite).saveAsTable(s"${tableName}_${orderbyField}_asc")
    orderByDFDesc.write.mode(SaveMode.Overwrite).saveAsTable(s"${tableName}_${orderbyField}_desc")

    /*    val trainingData = hc.sql(s"select * from ${tableName}_${orderbyField} limit $trainingDataCount")
    trainingData.count()
    trainingData.registerTempTable("trainingData")
    val maxTrainingTimestamp = hc.sql(s"select max($orderbyField) from trainingData").first().getInt(0)
    println(maxTrainingTimestamp)
    val batchData = hc.sql(s"select * from ${tableName}_${orderbyField} where $orderbyField > $maxTrainingTimestamp limit $batchDataCount")
    batchData.count()
    batchData.registerTempTable("batchData")
    val maxBatchDataTimestamp = hc.sql(s"select max($orderbyField) from batchData").first().getInt(0)
    println(maxBatchDataTimestamp)
    val streamingData = hc.sql(s"select * from ${tableName}_${orderbyField} where $orderbyField > $maxBatchDataTimestamp")
    streamingData.count()*/
    val trainingData = hc.sql(s"select * from ${tableName}_${orderbyField}_asc limit $trainingDataCount")
    val validationData = dataTable.except(trainingData)
    validationData.registerTempTable("validationData")
    val streamingData = hc.sql(s"select * from ${tableName}_${orderbyField}_desc limit $streamingDataCount")
    streamingData.registerTempTable("streamingData")
    val min = hc.sql(s"select min(${orderbyField}) from streamingData").first().getInt(0)
    val batchData = hc.sql(s"select * from validationData where ${orderbyField}< $min")

    //验证3个数据集的切分是否正确，看他们的数据是否有交集,0就是对的。这个步骤会很慢
    /*    trainingData.persist()
    batchData.persist()
    streamingData.persist()
    val i1 = trainingData.intersect(batchData).count()
    println(i1)
    val i2 = trainingData.intersect(streamingData).count()
    println(i2)
    val i3 = streamingData.intersect(batchData).count()
    println(i3)
    trainingData.unpersist()
    batchData.unpersist()
    streamingData.unpersist()*/

    //保存成新表
    trainingData.write.mode(SaveMode.Overwrite).saveAsTable(s"${tableName}_training")
    batchData.write.mode(SaveMode.Overwrite).saveAsTable(s"${tableName}_batch")
    streamingData.write.mode(SaveMode.Overwrite).saveAsTable(s"${tableName}_streaming")

    println("============Save Streaming Data as JSON============")
    streamingData.write.mode(SaveMode.Overwrite).json(s"/data/${tableName}_streaming")
  }
}