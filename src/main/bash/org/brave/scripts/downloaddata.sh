#!/bin/bash
rm -rf ml-latest.zip
rm -rf ml-100k.zip
rm -rf ml-latest
rm -rf ml-100k
wget http://files.grouplens.org/datasets/movielens/ml-latest.zip
#wget http://files.grouplens.org/datasets/movielens/ml-100k.zip
unzip ml-latest.zip
#unzip ml-100k.zip 
mv ml-latest ml-test
#mv ml-100k ml-test
mv ml-test/*.csv ml-test/*.txt
sed -i 's/\\//g' data/tags.txt
mv ml-test data
hdfs dfsadmin -safemode leave
hadoop fs -rmr data
hadoop fs -put data
