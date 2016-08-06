#Product Instruction of BraveFirst Team
This document illustrates the product we BraveFirst Team developed.
BraveFirst is a Data Mining use case of  Apache Spark and relative tools. 
It is developed for IBM Spark competition.
##Product Name:Recommandation System For movies
##Github Link(currently a private project)
https://github.com/luohui2000/BraveFirst
##Product Video Link
TBD
##Product Requirement
To command suitable movies for movie watchers. Due to we are using the data of MovieLens, we are developing aim to provide more suitable movies via our system.
##Archtecture
Data visualization: JavaScript

Web System:Spring MVC

Spider Module:

RMDBS:MYSQL

Machine Learning Framework: Spark MLlib

RealTime Streaming Data Flow:Kafka + SparkStreaming

Batch Data Processing And Tranformation:SparkSQL+SparkCore

Data Warehouse and ETL:SparkSQL + Hive

Data Storage: HDFS
##Data Usage
Data Source: MovieLens Open Data.
DownLoad Link:http://files.grouplens.org/datasets/movielens/ml-latest.zip
We used:
Links.csv to link movieid and the website of imdb movie page.
movies.csv to get the movie tags and training model for movies, and calcute the similarity of movies.
ratings.csv to do ALS model training, user and item based recommandations.
tag.csv currently is under study.
##Algorithm
Currently we are using ALS to train recommandation model.
##Product Business Value
This product uses batch data processing and realtime streaming data processing method to provide movie watchers most suitable and hottest movies, giving them a better movie watching experience to increase the income of the movie company or online video provides.
##Improvement Direction
It is supposed to complishment the realtime recommandation on both user based and item based type. The fine tuning of model precision is also an important consideration point.
##Development Prospect
It is considered to bring a more pricise recommand experience for movie watchers.