#!/bin/bash
if [ $# != 2 ] ; then
echo "USAGE: $0 userid movienumber"
echo " e.g.: $0 54234 3"
exit 1;
fi
$SPARK_HOME/bin/spark-submit --class org.brave.spark.ml.Recommandation --jars $SPARK_HOME/BraveFirst.jar $SPARK_HOME/BraveFirst.jar $1 $2
