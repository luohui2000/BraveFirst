package org.brave.util.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

/**
 * Created by yuchen
 * on 2016-07-06.
 */
public class MySqlJdbcReader {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local[2]");
        conf.setAppName("sparksql---jdbc--mysql");
        conf.set("spark.executor.memory", "2g");
        conf.set("spark.cores.max", "2");
        conf.setJars(new String[]{"mysql-connector-java-5.1.31.jar"});
        SparkContext sc = new SparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        String url = "jdbc:mysql://192.168.90.51:3306/apitest?user=honghailt&password=honghailt";
        DataFrame df = sqlContext
                .read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "todo627")
                .load();

// Looks the schema of this DataFrame.
        df.printSchema();

// Counts people by age
        //DataFrame countsByAge = df.groupBy("isdiff").count();

        // countsByAge.show();

        df.registerTempTable("todo627");
        DataFrame teenagers = sqlContext.sql("SELECT keyword,click FROM todo627 WHERE click >= 50 order by click desc limit 50");

// The results of SQL queries are DataFrames and support all the normal RDD operations.
// The columns of a row in the result can be accessed by ordinal.
        List<String> teenagerNames = teenagers.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                System.out.print(row.getString(0) + "," + row.getString(1) + "\n");
                return "keyword: " + row.getString(0);
            }
        }).collect();
        // df = sqlContext.read().json("people.json");

// Show the content of the DataFrame
        //  df.show();

// Saves countsByAge to S3 in the JSON format.
        // countsByAge.write().format("json").json("adf5.json");
    }
}
