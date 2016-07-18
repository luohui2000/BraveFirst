#!/bin/bash
$SPARK_HOME/bin/spark-submit BraveFirst.jar 1000 60 5 sparkmovie 'zookeeper.connect=192.168.100.2:2181/mdata;group.id=spark-streaming-movieStream1;zookeeper.connection.timeout.ms=10000'
