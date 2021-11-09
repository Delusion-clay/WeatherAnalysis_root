package com.it.sparkstearming.spark2hive

import com.it.common.SessionFactory
import com.it.hive.{HIveConfig, HdfsAdmin}
import org.apache.hadoop.fs.{FileStatus, FileSystem, FileUtil, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SaveMode

import scala.collection.JavaConversions._

/**
 * @description:小文件合并
 * @author: Delusion
 * @date: 2021-07-31 23:31
 */
object CombineHdfs extends Serializable with Logging{

  def main(args: Array[String]): Unit = {
      // 1.获取HDFS连接
      val spark = SessionFactory.newLocalHiveSession("CombineHdfs", 2)
      // 2.读取數據

      HIveConfig.tables.foreach(table=>{
        val citys: List[String] = List("anshun","guiyang","liupanshui","zunyi","tongren","qianxinan","bijie","qiandongnan","qiannan")
        for (city <- citys){
          val table_path = s"hdfs://hadoop-101:9820${HIveConfig.rootPath}/${table}/${city}"
          val tableDF = spark.read.parquet(table_path) //所有的數據拿到了
          //3.獲取所有的文件
          val fs:FileSystem = HdfsAdmin.get().getFs
          val statuses:Array[FileStatus] = fs.globStatus(new Path(table_path+"/part*"))

          val paths = FileUtil.stat2Paths(statuses)
          paths.foreach(x=>{
            println(x)
          })

          //4.重新写回
          tableDF.repartition(1).write.mode(SaveMode.Append).parquet(table_path)
          //5.刪除小文件
          paths.foreach(path=>{
            fs.delete(path)
            println(s"刪除小文件${path}成功")
          })
        }
      })
  }

}
