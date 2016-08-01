'''
Created on Jul 30, 2016

@author: Xiongjian
'''
#!/usr/bin/env python
#-*- coding:UTF-8 -*- 
import codecs
import re

fl=codecs.open("C:/Users/Xiongjian/Desktop/movies.txt","r","utf-8")#这样的文件打开方式可以避免编码问题
wf=codecs.open("C:/Users/Xiongjian/Desktop/matrix.txt","w","utf-8")
all=fl.readlines()
for i in range(len(all)):
    linerevised=all[i].strip('\n')
    linesplit=list(linerevised)
    linesplit.reverse()
    linereverse=''.join(linesplit)
    regex=re.compile(r'.+?(?=,)',re.S)#这里的第一个+指的是最小匹配集，而不是匹配到的结果最短,findall默认使用+形式，并且找到一个删除一个
    match1=re.search(regex,linerevised)
    match2=re.search(regex,linereverse)
    resultsplit=list(match2.group())
    resultsplit.reverse()
    resultreverse=''.join(resultsplit)
    result=resultreverse.split('|')
    for i in range(len(result)):
        wf.write(match1.group()+','+result[i]+'\n')
fl.close()
wf.close()