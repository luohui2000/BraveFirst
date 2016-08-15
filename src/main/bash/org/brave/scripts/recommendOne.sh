#!/bin/bash
echo $SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.RecommandForSingleUser $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar data/alsModel 1234 root Spark@123
$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.RecommandForSingleUser $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar "data/alsModel" 1234 root Spark@123

