package org.brave.spark.caseclass

case class Tags(userId: Int, movieId: Int, tag: String, timestamp: Double)
//配合ETL构建表