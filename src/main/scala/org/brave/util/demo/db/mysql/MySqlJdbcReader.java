package org.brave.util.demo.db.mysql;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.brave.spark.base.BaseConf;

import java.util.List;

/**
 * Created by yuchen
 * on 2016-07-06.
 */
public class MySqlJdbcReader extends BaseConf{

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local[6]");
        conf.setAppName("sparksql---jdbc--mysql");
        conf.setJars(new String[]{"mysql-connector-java-5.1.31.jar"});
        SparkContext sc = new SparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        String url = "jdbc:mysql://192.168.90.51:3306/apitest";
        DataFrame df = sqlContext
                .read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "shop2")
                .load();

        df.printSchema();
        df.registerTempTable("shop2");
        DataFrame teenagers = sqlContext.
                sql("SELECT * FROM shop2 " +
                        "WHERE creditGrade >= 9  limit 10");
/*order by creditGrade desc*/
        List<String> teenagerNames = teenagers.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                System.out.print(row+ "\n\n");
                return "keyword: " + row.getString(0);
            }
        }).collect();
    }
}
