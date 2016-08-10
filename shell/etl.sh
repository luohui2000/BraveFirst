#!/bin/bash
hadoop fs -rmr /data/movies
hadoop fs -rmr /data/ratings
hadoop fs -rmr /data/links
hadoop fs -rmr /data/tags
$SPARK_HOME/bin/spark-submit --class org.brave.spark.util.datacleaner.ETL $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar
