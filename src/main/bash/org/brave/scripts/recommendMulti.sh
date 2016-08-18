#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.RecommandForMultiUsers $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar model/ALS_20160818 200 10.0.10.62 hadoop hadoop
#$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.RecommandForMultiUsers $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar data/alsModel 100 user pw

