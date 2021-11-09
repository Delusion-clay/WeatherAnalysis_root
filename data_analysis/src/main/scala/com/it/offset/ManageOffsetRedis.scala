package com.it.offset


import com.it.client.JedisUtil
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.spark.streaming.kafka010.OffsetRange

import scala.collection.JavaConversions._

/**
 * redis 偏移管理
 */
object ManageOffsetRedis {

  /**
   * 获取redis中offset的偏移
   * @param db
   * @param groupId
   * @param topic
   */
  def getOffsetFromRedis(db: Int, groupId: String,topic:String,kafkaParams:java.util.Map[String, String]):java.util.Map[String,String] ={
    //获取redis连接
    val jedis = JedisUtil.getJedis(db)
    val redisKey = s"${groupId}_${topic}"
    var offsetMap = jedis.hgetAll(redisKey)
    //TODO 如果是第一次运行,redis为空,需要初始化redis
    if(offsetMap.size()==0){
      //TODO 怎么初始化,拿kafka中的偏移来初始化
      offsetMap = initOffset2Redis(topic,kafkaParams.asInstanceOf[java.util.Map[String,Object]])
    }
    jedis.close()
    offsetMap
  }

  /**
   *  初始化redis中的偏移
   * @param topic
   * @param kafkaParams
   * @return
   */
  def initOffset2Redis(topic:String,kafkaParams:java.util.Map[String, Object]): java.util.Map[String,String] ={
    //TODO 获取kafka中的初始偏移
    //构造消费者
    val consumer = new KafkaConsumer(kafkaParams)
    //订阅主题
    consumer.subscribe(java.util.Arrays.asList(topic))
    consumer.poll(100);
    //获取分区信息
    val topicPartitionsSet = consumer.assignment()
    println("topicPartitionsSet" + topicPartitionsSet)
    //获取开始的位移
    consumer.seekToBeginning(topicPartitionsSet)
    consumer.pause(topicPartitionsSet)
    val offsets = topicPartitionsSet.map(tp=>{
      tp.partition().toString ->  consumer.position(tp).toString
    }).toMap
    consumer.unsubscribe()
    consumer.close()
    offsets
  }

  /**
   * 保存偏移到REDIS
   * @param db
   * @param groupId
   * @param offsetRanges
   */
  def saveOffsetToRedis(db: Int, groupId: String, offsetRanges: Array[OffsetRange]): Unit = {
    //获取redis连接
    val jedis = JedisUtil.getJedis(db)
    offsetRanges.foreach(offsetRange=>{
      //构建key,redis  key在topic和groupId唯一
      val redisKey = s"${groupId}_${offsetRange.topic}"
      jedis.hset(redisKey,offsetRange.partition.toString,offsetRange.untilOffset.toString)
    })
    JedisUtil.close(jedis)
  }

}
