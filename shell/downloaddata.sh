#!/bin/bash
rm -rf ml-latest.zip
rm -rf ml-latest
wget http://files.grouplens.org/datasets/movielens/ml-latest.zip
unzip ml-latest.zip
mv ml-test/*.csv ml-test/*.txt
sed -i 's/\\//g' data/tags.txt
mv ml-test data
hdfs dfsadmin -safemode leave
hadoop fs -rmr data
hadoop fs -put data
