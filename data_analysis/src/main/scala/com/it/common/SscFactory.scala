package com.it.common

import org.apache.spark.SparkContext
import org.apache.spark.internal.Logging
import org.apache.spark.streaming.{Seconds, StreamingContext}


object SscFactory extends Serializable with Logging{


  /**
    * SSC 本地模式
    * @param appName
    * @param batchInterval
    * @param threads
    */
    def newLocalSSC(appName:String="default",batchInterval:Long=5L,threads: Int=2): StreamingContext ={
      val sparkConf = SparkConfFactory.newLocalSparkConf(appName,threads)

        new StreamingContext(sparkConf,Seconds(batchInterval))
    }


  /**
    * SSC 本地模式
    * @param appName
    * @param batchInterval
    */
  def newSSC(appName:String="default",batchInterval:Long=5L): StreamingContext ={
    val sparkConf = SparkConfFactory.newSparkConf(appName)
    new StreamingContext(sparkConf,Seconds(batchInterval))
  }


  def newLocalSscBySc(sparkContext:SparkContext,batchInterval:Long=5L):StreamingContext ={

    new StreamingContext(sparkContext,Seconds(batchInterval))
  }

}
