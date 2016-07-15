#!/bin/bash
#这个脚本会对movies.txt做一个二次的清洗，只提取电影的标签字段。
#to transform movies.txt to movietags.txt, to avoid the bad impact of ',' in the second field of movies.txt
while read LINE
 do 
 echo ${LINE##*,} >> movietags.txt
done < movies.txt
hadoop fs -put movietags.txt /user/hadoop/data/