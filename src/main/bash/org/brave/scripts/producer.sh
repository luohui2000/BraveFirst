
#!/bin/bash
$SPARK_HOME/bin/spark-submit --class org.brave.spark.streaming.Producer $SPARK_HOME/BraveFirst-0.0.1-SNAPSHOT-jar-with-dependencies.jar master60:9092 test
