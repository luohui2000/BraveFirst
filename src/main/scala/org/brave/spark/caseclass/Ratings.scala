package org.brave.spark.caseclass

case class Ratings(userId: Int, movieId: Int, rating: Float, timestamp: Double)
//配合ETL构建表