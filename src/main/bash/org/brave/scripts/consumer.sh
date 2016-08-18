
#!/bin/bash
$SPARK_HOME/bin/spark-submit --total-executor-cores 10 --class org.brave.spark.streaming.KafkaSparkStreaming $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar master60:2181,slave61:2181,slave62:2181 1 test 1 hadoop hadoop slave62 model/ALS_20160818
