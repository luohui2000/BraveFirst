#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.util.datacleaner.ETL --jars $SPARK_HOME/BraveFirst.jar,$SPARK_HOME/lib/mysql-connector-java-5.1.39.jar $SPARK_HOME/BraveFirst.jar
