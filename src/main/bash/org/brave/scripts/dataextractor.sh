#!/bin/bash
#hadoop fs -rmr /data/trainingData
#hadoop fs -rmr /data/batchData
#hadoop fs -rmr /data/streamingData

$SPARK_HOME/bin/spark-submit  --class org.brave.spark.util.datacleaner.DataExtractor $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar  0.8 0.1 0.1 ratings timestamp
