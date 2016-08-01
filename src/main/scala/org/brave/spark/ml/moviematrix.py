'''
Created on Jul 30, 2016

@author: Xiongjian
'''
#!/usr/bin/env python
#-*- coding:UTF-8 -*- 

import codecs
from itertools import groupby
from operator import itemgetter

fl=codecs.open("C:/Users/Xiongjian/Desktop/matrix.txt","r","utf-8")
wf=codecs.open("C:/Users/Xiongjian/Desktop/matrix2.txt","w","utf-8")
all=fl.readlines()
allrevise=[]
genres=[]
for i in range(len(all)):
    allrevise.append(all[i].strip('\n').split(','))
allsort=sorted(allrevise,key=lambda v:v[1])#将key相同的先排在一起，groupby会将相同key记录加入到一个iterator，如果无此步，分类无法正确进行
for key1,item1 in groupby(allsort, itemgetter(1)):#实际上groupby已经将结果做好，此循环的功能是提取结果
    genres.append(key1)
for key2,item2 in groupby(allrevise,itemgetter(0)):
    movieId=[]
    movieId.append(key2)
    for i in range(len(genres)-1):
        movieId.append('0')        #构造初始化列表，各维度值设为0，
    cont=list(item2)
    for j in range(len(cont)):
        if cont[j][1]!='(no genres listed)': #如果为（no genres listed）,则初始化的值不变
           for m in range(len(genres)-1):  #逐一与genres中的各元素(除第一个)比较，完成movieId表的赋值
               if cont[j][1]==genres[m+1]:
                   movieId[m+1]='1'
    wf.write(','.join(movieId)+'\n')
wf.close()
fl.close()