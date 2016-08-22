#Product Introduction of BraveFirst Team
This document illustrates the product we BraveFirst Team developed, which aims at providing suitable movie recommend result for different users.
BraveFirst is a Data Mining use case of  Apache Spark and relative tools. 
It is also developed for IBM Spark competition.
##Product Name:Recommendation System For movies
##Data Introduction
Our Data is downloaded from http://files.grouplens.org/datasets/movielens/ml-latest.zip
This dataset (ml-latest) describes 5-star rating and free-text tagging activity from [MovieLens](http://movielens.org), a movie recommendation service. It contains 21622187 ratings and 516139 tag applications across 30106 movies. These data were created by 234934 users between January 09, 1995 and August 06, 2015. This dataset was generated on August 06, 2015.
Users were selected at random for inclusion. All selected users had rated at least 1 movies. No demographic information is included. Each user is represented by an id, and no other information is provided.
The data are contained in four files, `links.csv`, `movies.csv`, `ratings.csv` and `tags.csv`. More details about the contents and use of all these files please refer to README.txt.
##Github Link(currently a private project)
https://github.com/luohui2000/BraveFirst
##Product Video Link
https://youtu.be/-In1Nw5CHas
http://v.youku.com/v_show/id_XMTY5MTE4NTEyOA==.html?from=y1.7-2
##Demo Video Link
https://youtu.be/n4iP1eM_eas
http://v.youku.com/v_show/id_XMTY5MTE3OTMzMg==.html?from=y1.7-2
##Product Requirement
To command suitable movies for movie watchers. Due to we are using the data of MovieLens, we are developing aim to provide more suitable movies via our system.
##Archtecture
Data visualization: JavaScript

Web System:Spring MVC

Spider Module:Java Program

RDBMS:MYSQL

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