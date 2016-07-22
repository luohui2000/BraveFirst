#!/bin/bash

systemctl restart mysqld
hive --service metastore &
$h/sbin/start-dfs.sh
$s/sbin/start-all.sh
$s/sbin/start-history-server.sh
$k/bin/zookeeper-server-start.sh /usr/lib/kafka/config/zookeeper.properties &
$k/bin/kafka-server-start.sh /usr/lib/kafka/config/server.properties &
$k/bin/kafka-topics.sh --list --zookeeper localhost:2181
$t/bin/startup.sh
# $s/bin/spark-submit BraveFirst.jar 1000 60 5 sparkmovie 'zookeeper.connect=192.168.100.2:2181/mdata;group.id=spark-streaming-movieStream1;zookeeper.connection.timeout.ms=10000'