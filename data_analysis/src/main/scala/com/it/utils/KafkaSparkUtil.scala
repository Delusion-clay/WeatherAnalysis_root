package com.it.utils

import com.alibaba.fastjson.{JSON, TypeReference}
import com.it.offset.ManageOffsetRedis
import org.apache.kafka.common.TopicPartition
import org.apache.spark.internal.Logging
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies}

import scala.collection.JavaConversions._


class KafkaSparkUtil(isUpte:Boolean=true) extends Serializable with Logging {


  def getMapDSwithOffset(ssc: StreamingContext,
                         kafkaParams: java.util.Map[String, String],
                         groupId: String,
                         topic: String): DStream[java.util.HashMap[String, String]] = {
    //从redis中获取偏移,只是在任务启动的时候
    val offsetMap = ManageOffsetRedis.getOffsetFromRedis(10,groupId,topic,kafkaParams.asInstanceOf[java.util.Map[String,String]])
    //offsets: collection.Map[TopicPartition, Long])
    val offsets= offsetMap.map(offset=>{
      //对offsetMap中的每条数据进行转换
      val topicPartition = new TopicPartition(topic,offset._1.toInt)
      val offset_p = offset._2.toLong
      topicPartition -> offset_p
    }).toMap

    val DS = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Assign[String,String](offsets.keys.toList,kafkaParams,offsets))
    DS.foreachRDD(rdd=>{
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      if(isUpte){
        ManageOffsetRedis.saveOffsetToRedis(10,groupId,offsetRanges)
      }
      offsetRanges.foreach(offset=>{
        println("===========================")
        println("offset.topic:" + offset.topic)
        println("offset.partition:" + offset.partition)
        println("offset.fromOffset:" + offset.fromOffset)
        println("offset.untilOffset:" + offset.untilOffset)
      })
    })

    val dsMap = DS.map(kafkaString=>{
      kafkaString.key()  //分区key
      val jsonString = kafkaString.value()
      //转map
      val map = JSON.parseObject(jsonString,new TypeReference[java.util.HashMap[String, String]](){})
//      println(map.toString)
      map
    }).filter(x=>x!=null)
    dsMap
  }

}