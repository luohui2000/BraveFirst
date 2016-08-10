#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.RecommandForMultiUsers $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar /user/root/model/myCollaborativeFilter20160802 100
