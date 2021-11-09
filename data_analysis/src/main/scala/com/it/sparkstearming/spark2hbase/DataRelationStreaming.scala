package com.it.sparkstearming.spark2hbase

import com.it.common.SscFactory
import com.it.config.KafkaConfig
import com.it.hbase.config.HBaseTableUtil
import com.it.hbase.insert.HBaseInsertHelper
import com.it.hbase.split.SpiltRegionUtil
import com.it.time.TimeTranstationUtils
import com.it.utils.KafkaSparkUtil
import org.apache.hadoop.hbase.client.Put
import org.apache.spark.internal.Logging

//TODO spark写入hbase
object DataRelationStreaming extends Serializable with Logging {
  def main(args: Array[String]): Unit = {


    //TODO 初始化表
    val cityFields = Array("anshun", "guiyang", "liupanshui", "zunyi", "tongren", "qianxinan", "bijie", "qiandongnan", "qiannan")

    //initHbaseTable(cityFields)
    val topic = "analy"
    val groupId = "DataRelationStreaming"

    val ssc = SscFactory.newLocalSSC("DataRelationStreaming", 5L)
    val kafkaParams = KafkaConfig.getKafkaConfig(groupId)
    val kafkaSparkUtil = new KafkaSparkUtil(false)

    val resultDS = kafkaSparkUtil.getMapDSwithOffset(ssc,
      kafkaParams.asInstanceOf[java.util.Map[String, String]], groupId, topic)
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


    resultDS.foreachRDD(rdd=>{
        rdd.foreachPartition(partion=>{
          while (partion.hasNext){
            val map = partion.next()
            //判断map里面是不是包含索引字段
            println(map.get("city").equals("anshun"))
            cityFields.foreach(city=>{
              if(map.get("city").equals(city)){
                //map包含索引字段
                val rowkey = map.get("new_date").getBytes()
                print(rowkey)
                val tableName = s"${city}"
                val put = new Put(rowkey)
                //val ts = map.get("city").hashCode&Integer.MAX_VALUE
                put.addColumn("cf".getBytes(),"city".getBytes(),
                  map.get("city").getBytes())
                put.addColumn("cf".getBytes(),"year".getBytes(),
                  map.get("year").getBytes())
                put.addColumn("cf".getBytes(),"month".getBytes(),
                  map.get("month").getBytes())
                put.addColumn("cf".getBytes(),"week".getBytes(),
                  map.get("week").getBytes())
                put.addColumn("cf".getBytes(),"temperature".getBytes(),
                  map.get("temperature").getBytes())
                put.addColumn("cf".getBytes(),"new_date".getBytes(),
                  map.get("new_date").getBytes())
                put.addColumn("cf".getBytes(),"meteo".getBytes(),
                  map.get("meteo").getBytes())
                put.addColumn("cf".getBytes(),"wind".getBytes(),
                  map.get("wind").getBytes())
                print(put.toJSON())
                Thread.sleep(100)
//                HBaseInsertHelper.put(tableName,put)
              }
            })
          }
        })
      })
    ssc.start()
    ssc.awaitTermination()
  }

  def initHbaseTable(arrayFields:Array[String]): Unit ={
    arrayFields.foreach(field=>{
     HBaseTableUtil.deleteTable(s"test:${field}")
      println(s"=====删除表${field}")
      HBaseTableUtil.createTable(s"test:${field}", "cf", true, -1, 100, SpiltRegionUtil.getSplitKeysBydinct)
      println(s"=====新建表${field}")
    })
  }

}
