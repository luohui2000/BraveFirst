#!/bin/bash
today=$(date +%Y%m%d)
echo $today
hadoop fs -rmr data/ALS_$today
$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.getBestModel $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar
