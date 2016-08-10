package org.brave.spark.ml

import org.apache.spark._
import org.apache.spark.ml.classification._
import org.apache.spark.mllib.linalg.Vectors
import org.brave.spark.base.BaseConf
import org.apache.commons.math3.linear._

object ALSRec extends BaseConf {
  //based on https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/SparkALS.scala 
  // Parameters set through command line arguments
  var M = 0 // Number of movies
  var U = 0 // Number of users
  var F = 0 // Number of features
  var ITERATIONS = 0
  val LAMBDA = 0.01

  def main(args: Array[String]) {
    val slices = 2
    val M = 100
    val U = 500
    val F = 10
    val ITERATIONS = 5

    conf.setAppName("ALSRec")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    val R = generateR() //产生一个随机矩阵
    var ms = Array.fill(M)(randomVector(F)) //随机产生的movie向量数组
    var us = Array.fill(U)(randomVector(F)) //随机产生的user向量数组

    val Rc = sc.broadcast(R) //随机矩阵R的广播变量
    var msb = sc.broadcast(ms) //movie向量数组的广播变量
    var usb = sc.broadcast(us) //user向量数组的广播变量

    for (iter <- 1 to ITERATIONS) {
      println(s"Iteration $iter:")
      ms = sc.parallelize(0 until M, slices).map(i => update(i, msb.value(i), usb.value, Rc.value)).collect()
      msb = sc.broadcast(ms) // Re-broadcast ms because it was updated
      us = sc.parallelize(0 until U, slices).map(i => update(i, usb.value(i), msb.value, Rc.value.transpose())).collect()
      usb = sc.broadcast(us) // Re-broadcast us because it was updated
      println("RMSE = " + rmse(R, ms, us))
      println()
    }
  }
  def generateR(): RealMatrix = {
    val mh = randomMatrix(M, F) //产生一个随机矩阵X，这是个稠密矩阵
    val uh = randomMatrix(U, F) //产生一个随机矩阵Y，这是个稠密矩阵
    mh.multiply(uh.transpose()) //用随机矩阵X乘以随机矩阵Y的转置矩阵YT,产生原始矩阵A，这个矩阵是稀疏的
  }
  private def randomVector(n: Int): RealVector =
    new ArrayRealVector(Array.fill(n)(math.random))

  private def randomMatrix(rows: Int, cols: Int): RealMatrix =
    new Array2DRowRealMatrix(Array.fill(rows, cols)(math.random))

  def update(i: Int, m: RealVector, us: Array[RealVector], R: RealMatrix): RealVector = {
    val U = us.size
    val F = us(0).getDimension
    var XtX: RealMatrix = new Array2DRowRealMatrix(F, F)
    var Xty: RealVector = new ArrayRealVector(F)
    // For each user that rated the movie
    for (j <- 0 until U) {
      val u = us(j)
      // Add u * u^t to XtX
      XtX = XtX.add(u.outerProduct(u))
      // Add u * rating to Xty
      Xty = Xty.add(u.mapMultiply(R.getEntry(i, j)))
    }
    // Add regularization coefs to diagonal terms
    for (d <- 0 until F) {
      XtX.addToEntry(d, d, LAMBDA * U)
    }
    // Solve it with Cholesky
    new CholeskyDecomposition(XtX).getSolver.solve(Xty)
  }

  def rmse(targetR: RealMatrix, ms: Array[RealVector], us: Array[RealVector]): Double = {
    val r = new Array2DRowRealMatrix(M, U)
    for (i <- 0 until M; j <- 0 until U) {
      r.setEntry(i, j, ms(i).dotProduct(us(j)))
    }
    val diffs = r.subtract(targetR)
    var sumSqs = 0.0
    for (i <- 0 until M; j <- 0 until U) {
      val diff = diffs.getEntry(i, j)
      sumSqs += diff * diff
    }
    math.sqrt(sumSqs / (M.toDouble * U.toDouble))
  }
}