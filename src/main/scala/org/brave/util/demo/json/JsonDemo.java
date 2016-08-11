package org.brave.util.demo.json;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.brave.spark.base.BaseConf;

import java.util.List;

/**
 * Created by yuchen
 * on 2016/8/4 0004.
 */
public class JsonDemo {

    public static void main(String[] args){
        SparkConf conf = new SparkConf();
        conf.setMaster("local[6]");
        conf.set("spark.driver.memory", "8g");

        conf.setAppName("JsonDemo");
        SparkContext sc = new SparkContext(conf);
        SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
        DataFrame df = sqlContext.read().json("data/shop.json");
        df.printSchema();
        df.registerTempTable("shop3");

        DataFrame shop = sqlContext.sql("select count(*) as month  from shop3 where telephone is not null");
        List<String> teenagerNames = shop.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                System.out.println("count:"+row.getLong(0));
                return "Name: " + row.getLong(0);

            }
        }).collect();

    }
}
