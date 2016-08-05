package org.brave.spark.caseclass

case class Ratings(userId: Int, movieId: Int, rating: Float, timestamp: Int)
//配合ETL构建表