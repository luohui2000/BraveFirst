青云集群说明
=============================
我在余陈搭建的集群基础上做了一下修改了。绝大部分还是余陈辛苦搭建的，在此感谢下。
青云控制台的控制台页面：https://console.qingcloud.com/gd1/instances/
我们现在的集群有2个节点，master和slave1。可以自由加机器和资源。加资源需要关机后操作。
然后有一个网络，公网IP和路由，一个最简单的2层网络。公网IP绑定了路由。就是跟你家里的网络一样啦。
大家可以通过公网IP  121.201.8.24  的22端口做远程登陆。
slave1节点可以通过master SSH过去。免密登陆。

我在路由上开通了常用的Spark集群的端口，8080,4040,9000,8088，等等。地址是: http://121.201.8.24:8080
有需要访问集群的某个其他的端口，可以自己开：https://console.qingcloud.com/gd1/routers/rtr-sy75tycy/#，在端口转发下面，添加规则即可。

目前已经安装了Spark，HDFS,kafka,zeppelin等，后面可能会用到ZK，那再说。目前Spark看大家需要，可以on yarn,standalone都行。
目录在/usr/lib/spark,/usr/lib/hadoop,/usr/lib/kafka,......在/etc/profile.d中有他们对应的脚本设置对应的SPARK_HOME,HADOOP_HOME等环境变量。
JDK切换到了ORACLE的JDK1.8，跟咱们的项目代码保持一致。JAVA_HOME=/usr/lib/jdk.

在集群中添加了自定义环境变量，在~/.bash_profile里。具体如下：
h=/usr/lib/hadoop
s=/usr/lib/spark
b=/usr/lib/hbase
v=/usr/lib/hive
p=/usr/lib/phoenix
z=/usr/lib/zeppelin
f=/usr/lib/flume
k=/usr/lib/kafka
大家可以快速访问对应的路径

咱们现在movielens的数据放在了master的$SPARK_HOME/data文件夹中。yelp的数据有需要再传吧。网络是按流量收费的。

