package com.it.common

import org.apache.hadoop.conf.Configuration
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession


object SessionFactory extends Serializable with Logging{

    def newLocalHiveSession(appName:String,threads:Int): SparkSession ={

      //System.load("H:\\hadoop-common-2.6.0-bin-master\\bin\\hadoop.dll")
      //System.load("H:\\hadoop-common-2.6.0-bin-master\\bin\\winutils.exe")

      val sparkBuild = SparkSession
        .builder()
        .appName("Spark SQL basic example")
        .master(s"local[${threads}]")
        .enableHiveSupport()

      val configuration = new Configuration()
      configuration.addResource("spark/hive/core-site.xml")
      configuration.addResource("spark/hive/hdfs-site.xml")
      configuration.addResource("spark/hive/hive-site.xml")
      val iterator = configuration.iterator()

      while (iterator.hasNext){
        val next = iterator.next()
        sparkBuild.config(next.getKey,next.getValue)
      }
      sparkBuild.getOrCreate()
    }
}
