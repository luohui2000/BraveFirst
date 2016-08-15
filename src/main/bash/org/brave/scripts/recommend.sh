#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.Recommandation $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar 1234 5
