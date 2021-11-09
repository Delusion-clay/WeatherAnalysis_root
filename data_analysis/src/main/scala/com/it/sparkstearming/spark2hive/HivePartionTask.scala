package com.it.sparkstearming.spark2hive

import java.util

import com.it.common.{SessionFactory, SscFactory}
import com.it.config.KafkaConfig
import com.it.hive.HIveConfig
import com.it.time.TimeTranstationUtils
import com.it.utils.KafkaSparkUtil
import org.apache.spark.sql.{Row, SaveMode}

import scala.collection.JavaConversions._

/**
 * @description:Spark写入hive
 * @author: Delusion
 * @date: 2021-07-28 14:12
 */
object HivePartionTask {
  def main(args: Array[String]): Unit = {
    val spark = SessionFactory.newLocalHiveSession("HivePartionTask", 2)
    spark.sql("use default")
    //TODO 创建hive表
    HIveConfig.hivePartionTableSQL.foreach(table => {
      spark.sql(table._2.toString())
      println(s"=============创建分区表${table._2}成功==============")
    })
    val sparkContext = spark.sparkContext
    //TODO 往HDFS加载数据
    val topic = "analy"
    val groupId = "HistoryDataAnalys"
    val ssc = SscFactory.newLocalSscBySc(sparkContext,5L)
    val kafkaParams = KafkaConfig.getKafkaConfig(groupId)
    val kafkaSparkUtil = new KafkaSparkUtil(true)
    val resultDS = kafkaSparkUtil.getMapDSwithOffset(ssc,kafkaParams.asInstanceOf[java.util.Map[String,String]],groupId,topic)
        .map(map=>{
          //{"new_date": "2021-01-31",
          // "meteo": "小雨/中雨",
          // "temperature": "8℃/3℃",
          // "wind": "北风1-2级/北风1-2级"}
          val date = map.get("new_date")
//          map.put("monthPartition",date.substring(0,7))
          val year = date.substring(0,4)
          val month = date.substring(5,7)
          map.put("year",year)
//          val year_month = date.substring()
          map.put("month",month)
          val week = TimeTranstationUtils.getWeek(date)
          map.put("week",week)
          val temperature = map.get("temperature")
          val tempArray = temperature.split("/")
          val max_temp = tempArray(0).replace("℃","")
          val min_temp = tempArray(1).replace("℃","")
          map.put("max_temp",max_temp)
          map.put("min_temp",min_temp)
          map
        })

//    HistoryWeatherProcess.process(resultDS)
      HIveConfig.tables.foreach(table => {
        resultDS.foreachRDD( rdd => {
          val city =  rdd.map(x => x.get("city"))
            .distinct().collect()
          val tableSchema = HIveConfig.mapSchema.get(table)
          val schemaFields = tableSchema.fieldNames
          city.foreach(city => {
            val rowRDD = rdd.filter(map => {
              city.equals(map.get("city"))
            }).map( map => {
              val list = new util.ArrayList[Object]()
              for (schemaField <- schemaFields){
                list.add(map.get(schemaField))
              }
             Row.fromSeq(list)
            })
            val tableDF = spark.createDataFrame(rowRDD,tableSchema)

            tableDF.show(2)
            //开始写入HDFS
            val tableFdfsPath = s"hdfs://hadoop-101:9820" +
              s"${HIveConfig.rootPath}" +
              s"/${table}/${city}"
            tableDF.write.mode(SaveMode.Append).parquet(tableFdfsPath)
            //TODO 数据加载
            val sql = s"ALTER TABLE ${table} ADD IF NOT" +
              s" EXISTS PARTITION(`city`='${city}')" +
              s" LOCATION '${tableFdfsPath}'"
            println(s"sql========================${sql}")
            spark.sql(sql)
          })
        })
      })
    ssc.start()
    ssc.awaitTermination()
  }
}
