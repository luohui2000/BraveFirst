#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.streaming.Producer --jars $SPARK_HOME/BraveFirst.jar,$SPARK_HOME/lib/mysql-connector-java-5.1.39.jar,$SPARK_HOME/lib/kafka-clients-0.10.0.0.jar,$SPARK_HOME/lib/kafka_2.10-0.10.0.0.jar $SPARK_HOME/BraveFirst.jar ratings
