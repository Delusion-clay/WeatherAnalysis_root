package com.it.common

import org.apache.spark.SparkContext
import org.apache.spark.internal.Logging


object SparkContextFactory extends Serializable with Logging{

  /**
    * 本地模式SparkContext 工厂
    * @param appName
    * @param threads
    * @return
    */
   def newLocalSparkContext(appName:String="default",threads: Int=2): SparkContext ={
       val sparkConf = SparkConfFactory.newLocalSparkConf(appName,threads)
       new SparkContext(sparkConf)
   }


  /**
    * 本地模式SparkContext 工厂
    * @param appName
    * @return
    */
  def newSparkContext(appName:String="default"): SparkContext ={
    val sparkConf = SparkConfFactory.newSparkConf(appName)
    new SparkContext(sparkConf)
  }

}
