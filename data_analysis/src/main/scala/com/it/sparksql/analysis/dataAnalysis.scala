package com.it.sparksql.analysis

import com.it.common.SessionFactory
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType, TimestampType}

import scala.collection.JavaConversions._

/**
 * @description: saprk sql
 * @author: Delusion
 * @date: 2021-08-01 22:18
 */
object dataAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SessionFactory.newLocalHiveSession("data_analysis", 8)
    val citys: List[String] = List("anshun", "guiyang", "liupanshui",
      "zunyi", "tongren", "qianxinan", "bijie", "qiandongnan", "qiannan")
    val year = List("2020", "2021")
    for (year <- year) {
      for (city <- citys) {
        val df_avg = spark.sql(s"select month," +
          s"cast(avg((((max_temp+min_temp))/2))" +
          s" as int) from weather where city='${city}' group by month").rdd
        val df_max_min = spark.sql(s"select month,max(max_temp),min(min_temp) from  weather where city='${city}' group by month").rdd
        val result_avg = df_avg.map(x => Row(year, x.get(0)
          .toString, x.get(1).toString, city))
        val result_max_min = df_max_min.map(x => Row(year, x.get(0)
          .toString, x.get(1).toString, x.get(2).toString, city))
        val resultAvgSchema = StructType(
          List(
            StructField("year", StringType, true),
            StructField("month", StringType, true),
            StructField("temp_avg", StringType, true),
            StructField("city", StringType, true)
          )
        )
        val resultSchema = StructType(
          List(
            StructField("year", StringType, true),
            StructField("month", StringType, true),
            StructField("temp_max", StringType, true),
            StructField("temp_min", StringType, true),
            StructField("city", StringType, true)
          )
        )
        val DF_avg = spark.createDataFrame(result_avg, resultAvgSchema)
        val DF_max_min = spark.createDataFrame(result_max_min, resultSchema)
        DF_avg.write.mode("append")
          .format("jdbc")
          .option("url", "jdbc:mysql://hadoop-101:3306/weather?useSSL=false")
          .option("dbtable", "avg_city") //表名
          .option("user", "root")
          .option("password", "_Qq3pw34w9bqa")
          .save()
        DF_max_min.write.mode("append")
          .format("jdbc")
          .option("url", "jdbc:mysql://hadoop-101:3306/weather?useSSL=false")
          .option("dbtable", "weather_city") //表名
          .option("user", "root")
          .option("password", "_Qq3pw34w9bqa")
          .save()
      }
    }
    spark.stop()
  }
}
