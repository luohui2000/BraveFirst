rm(list=ls())
rawdata=read.csv("C:/Users/Xiongjian/Desktop/matrix2.txt",header=F)
data=as.matrix(rawdata[,2:20])
moviename=rawdata[,1]
recommend=matrix(rep(0,301060),nrow=30106,ncol=10)
#comp=dist(data,diag=TRUE,upper=TRUE)
#result=as.matrix(comp)
#length(result)
cosdis<-function(x,y){
   res=sum(x*y)/sqrt(sum(x*x)*sum(y*y))
   return(res)
}
for (i in 1:length(moviename)){
  res=vector(mode="numeric",length=0)
  for (j in 1:length(moviename)){
    res=c(res,cosdis(data[i,],data[j,]))
 }
  recommend[i,]=moviename[which(rank(res)>length(moviename)-10)]
}
write.table(recommend,file="C:/Users/Xiongjian/Desktop/rec.txt",sep=",")