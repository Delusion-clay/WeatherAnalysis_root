package com.it.sparkstearming

import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.Date

import com.it.common.SscFactory
import com.it.config.KafkaConfig
import com.it.time.TimeTranstationUtils
import com.it.utils.KafkaSparkUtil

/**
 * @description:
 * @author: Delusion
 * @date: 2021-09-08 15:45
 */
object SparkWarning extends App {

  val cityFields = Array("anshun", "guiyang", "liupanshui", "zunyi", "tongren", "kaili", "bijie", "qiandongnan", "qiannan")
  val topic = "weather"
  val groupId = "warning"
  val ssc = SscFactory.newLocalSSC("warning", 5L)
  val kafkaParams = KafkaConfig.getKafkaConfig(groupId)
  val kafkaSparkUtil = new KafkaSparkUtil(false)

  val resultDS = kafkaSparkUtil.getMapDSwithOffset(ssc,
    kafkaParams.asInstanceOf[java.util.Map[String, String]], groupId, topic)
      .map(map =>{
        val dateString = new Date()
        val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM")
        var date=""
        if(map.get("date_14")=="30"){
         date = dateFormat.format(dateString)+"-"+map.get("date_14")
        }
        date="2021-10-"+map.get("date_14")
        var cityString = map.get("city")
        cityString match {
          case "anshun" => cityString = "安顺"
          case "guiyang" => cityString = "贵阳"
          case "liupanshui" => cityString = "六盘水"
          case "zunyi" => cityString = "遵义"
          case "tongren" => cityString = "铜仁"
          case "kaili" => cityString = "凯里"
          case "bijie" => cityString = "毕节"
          case "qiandongnan" => cityString = "黔东南"
          case "qiannan" => cityString = "黔南"
        }
        map.put("cityString",cityString)
        map.put("date_string",date)
        map
      })
//  resultDS.print()
  cityFields.foreach(city=>{
    val cityDS = resultDS.filter(rdd =>{
      rdd.get("city").equals(city)
    })
    cityDS.foreachRDD(rdd=>{
     rdd.foreachPartition(partition=>{
       Class.forName("com.mysql.jdbc.Driver")
       val connection = DriverManager.getConnection("jdbc:mysql://hadoop-101:3306/weather?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false","root","_Qq3pw34w9bqa")
//       val sql1 = "truncate table warn"
//       connection.prepareStatement(sql1).executeLargeUpdate()
       while (partition.hasNext){
         val map = partition.next()
         val wind = map.get("level")
         val city = map.get("cityString")
         val date_string = map.get("date_string")
         val level = map.get("level")
         val temp = map.get("max_temp")
         val weather = map.get("weather")
         var message = ""
         if (wind.toInt>4){
           message = s"${city}市在==${date_string}" +
             s"===会有大风预警，风级为${level}级"
           println(message)
           val sql = s"replace into warn(date_str," +
             s"message,city,city_name)" +
             s" values('"+date_string+"','"+message+"" +
             "','"+map.get("city")+"','"+city+"市"+"')"
//           connection.prepareStatement(sql).executeLargeUpdate()
         }
         if (temp.toInt>26){
           message = s"${city}市在==${date_string}" +
             s"==会有高温预警，最高温度为${temp}°C"
           println(message)
           val sql = s"replace into warn(date_str,message," +
             s"city,city_name)" +
             s" values('"+date_string+"','"+message+"'" +
             ",'"+map.get("city")+"','"+city+"市"+"')"
//           connection.prepareStatement(sql).executeLargeUpdate()
         }
         if (weather=="雨"){
           message = s"${city}市在==${date_string}" +
             s"==会有降雨预警"
           println(message)
           val sql = s"replace into warn(date_str" +
             s",message,city,city_name)" +
             s" values('"+date_string+"','"+message+
             "','"+map.get("city")+"','"+city+"市"+"')"
//           connection.prepareStatement(sql).executeLargeUpdate()
         }
       }
     })
    })
  })
  ssc.start()
  ssc.awaitTermination()

}
