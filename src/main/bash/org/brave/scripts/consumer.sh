
#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.streaming.KafkaSparkStreaming2 $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar master60:2181,slave61:2181,slave62:2181 1 test 1
