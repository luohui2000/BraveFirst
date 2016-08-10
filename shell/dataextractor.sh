#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.util.datacleaner.DataExtractor $SPARK_HOME/BraveFirst.jar  0.8 0.1 0.1 ratings timestamp
