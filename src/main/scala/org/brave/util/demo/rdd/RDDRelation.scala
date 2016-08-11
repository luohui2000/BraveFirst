/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package org.brave.util.demo.rdd

import org.apache.spark.SparkContext
import org.brave.spark.base.BaseConf

object RDDRelation extends BaseConf{
  def main(args: Array[String]) {
    conf.setAppName("RDDRelation")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(1 to 10) //创建RDD

    //一、乘以2
    val map = rdd.map(_ * 2) //对RDD中的每个元素都乘于2
    map.foreach(x => print(x + " "))
    //    2 4 6 8 10 12 14 16 18 20


    //二、flatmap
    val fm = rdd.flatMap(x => (1 to x)).collect()
    fm.foreach(x => print(x + " "))
    //    1 1 2 1 2 3 1 2 3 4 1 2 3 4 5 1 2 3 4 5 6 1 2 3 4 5 6 7 1 2 3 4 5 6 7 8 1 2 3 4 5 6 7 8 9 1 2 3 4 5 6 7 8 9 10


    //三、抽样
    val sample1 = rdd.sample(true, 0.7, 4)
    sample1.collect.foreach(x => print(x + " "))
    //    2 3 7 7 9

    //并集
    val rdd2 = sc.parallelize(3 to 5)
    val unionRDD = rdd.union(rdd2)
    unionRDD.collect.foreach(x => print(x + " "))
    //    1 2 3 4 5 6 7 8 9 10 3 4 5

    //交集
    val interRDD = rdd.intersection(rdd2)
    interRDD.collect.foreach(x => print(x + " "))
    //    4 3 5

    //去重
    val list = List(1, 1, 2, 5, 2, 9, 6, 1)
    val RDD2 = sc.parallelize(list)
    val dicRDD = RDD2.distinct()
    dicRDD.collect.foreach(x => print(x + " "))
    //    1 6 9 5 2
    var dicRDD2 = unionRDD.distinct()
    dicRDD2.collect.foreach(x => print(x + " "))

    //    4 6 8 10 2 1 3 7 9 5


    //笛卡尔积
    val rdd11 = sc.parallelize(1 to 3)
    val rdd22 = sc.parallelize(2 to 5)
    val cartesianRDD = rdd11.cartesian(rdd22)
    cartesianRDD.foreach(x => println(x + " "))
    //    (1,2)
    //    (1,3)
    //    (1,4)
    //    (1,5)
    //    (2,2)
    //    (2,3)
    //    (2,4)
    //    (2,5)
    //    (3,2)
    //    (3,3)
    //    (3,4)
    //    (3,5)


    //重新分区
    val rdd4 = sc.parallelize(1 to 16, 4)
    val coalesceRDD4 = rdd4.coalesce(2) //当suffle的值为false时，不能增加分区数(即分区数不能从5->7)
    println("重新分区后的分区个数:" + coalesceRDD4.partitions.size)
    println("RDD依赖关系:" + coalesceRDD4.toDebugString)



    //RDD的每个分区中的类型为T的元素转换换数组Array[T]
    val glomRDD = rdd4.glom() //RDD[Array[T]]
    glomRDD.foreach(rdd => println(rdd4.getClass.getSimpleName))



    //根据weight权重值将一个RDD划分成多个RDD,权重越高划分得到的元素较多的几率就越大
    val randomSplitRDD = rdd.randomSplit(Array(1.0, 2.0, 7.0))
    randomSplitRDD(0).foreach(x => print(x + " "))
    randomSplitRDD(1).foreach(x => print(x + " "))
    randomSplitRDD(2).foreach(x => print(x + " "))


    val list2 = List(("mobin", 22), ("kpop", 20), ("lufei", 23))
    val rddFlatMapValuesX = sc.parallelize(list2)
    val mapValuesRDD = rddFlatMapValuesX.flatMapValues(x => Seq(x, "male"))
    mapValuesRDD.foreach(println)


    val rddMapValuesX = sc.parallelize(list2)
    val mapRDDx = rddMapValuesX.mapValues(x => Seq(x, "male"))
    mapRDDx.foreach(println)



    val people = List(("male", "Mobin"), ("male", "Kpop"), ("female", "Lucy"), ("male", "Lufei"), ("female", "Amy"))
    val rdd10 = sc.parallelize(people)
    val combinByKeyRDD = rdd10.combineByKey(
      (x: String) => (List(x), 1),
      (peo: (List[String], Int), x: String) => (x :: peo._1, peo._2 + 1),
      (sex1: (List[String], Int), sex2: (List[String], Int)) => (sex1._1 ::: sex2._1, sex1._2 + sex2._2))
    combinByKeyRDD.foreach(println)




    //3表示指定为3个Partitions
    val a = sc.parallelize(List("dog", "salmon", "salmon", "rat", "elephant"), 3)
    //以a各元素的长度建议新的RDD
    a.collect.foreach(x => print(x + " "))
//    dog salmon salmon rat elephant

    val b = a.map(_.length)
    b.collect.foreach(x => print(x + " "))
//    3 6 6 3 8

    //将两个RDD组合新一个新的RDD
    val c = a.zip(b)
    c.collect.foreach(x => print(x + " "))
//    (dog,3) (salmon,6) (salmon,6) (rat,3) (elephant,8)

    sc.stop()
  }
}