package org.brave.spark.ml

import org.apache.spark._
import org.apache.spark.ml.classification._
import org.apache.spark.mllib.linalg.Vectors
import org.brave.spark.base.BaseConf
import org.apache.commons.math3.linear._

object ALSRec extends BaseConf {
  // Parameters set through command line arguments
  var M = 0 // Number of movies
  var U = 0 // Number of users
  var F = 0 // Number of features
  var ITERATIONS = 0
  val LAMBDA = 0.01
  
  def main(args: Array[String]) {
    var slices = 0
    val options = (0 to 4).map(i => if (i < args.length) Some(args(i)) else None)

    conf.setAppName("ALSRec")
    val sc = new SparkContext(conf)
    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    val R = generateR()
  }
  def generateR(): RealMatrix = {
    val mh = randomMatrix(M, F)
    val uh = randomMatrix(U, F)
    mh.multiply(uh.transpose())
  }
  private def randomVector(n: Int): RealVector =
    new ArrayRealVector(Array.fill(n)(math.random))

  private def randomMatrix(rows: Int, cols: Int): RealMatrix =
    new Array2DRowRealMatrix(Array.fill(rows, cols)(math.random))
}