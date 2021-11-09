package com.it.hive

import org.apache.commons.configuration.{CompositeConfiguration, PropertiesConfiguration}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer


object HIveConfig extends Serializable with Logging {
  val hiveFilePath = "spark/hive/fieldmapping.properties"

  val rootPath = "/user/hive/external"

  var config: CompositeConfiguration = null //配置文件
  var tables: java.util.List[_] = null //所有表名
  var hiveTableSQL: java.util.HashMap[String, String] = null //存放表名与表SQL的映射
  var mapSchema:java.util.HashMap[String,StructType] = null //存放表名与表Schema的映射


  var hivePartionTableSQL: java.util.HashMap[String, String] = null //存放分区表名与表SQL的映射

  init

  def main(args: Array[String]): Unit = {

  }

  def init() = {

    //TODO 读取配置文件
    println("====================加载配置文件config=========================")
    config = readCompositeConfiguration(hiveFilePath)
    val keys = config.getKeys
    while (keys.hasNext) {
      println(keys.next())
    }
    //TODO 获取所有的表名
    println("====================初始化tables=========================")
    tables = config.getList("tables")
    tables.foreach(table => {
      println(table)
    })


    println("====================初始化hiveTableSQL=========================")
    hiveTableSQL = createSQL()
    hiveTableSQL.foreach(table=>{
      println(table)
    })

    println("====================初始化分区表=========================")
    hivePartionTableSQL = createPartionSQL()
    hivePartionTableSQL.foreach(table=>{
      println(table)
    })

    mapSchema = getSchema()

  }


  /**
    * 初始化hivesql
    *
    * @return
    */
  def createPartionSQL(): java.util.HashMap[String, String] = {

    val hiveSqlMap = new java.util.HashMap[String, String]()

    tables.foreach(table => {
      var sql = s"CREATE EXTERNAL TABLE IF NOT EXISTS ${table} ("
      //中间的字段
      val fields = config.getKeys(table.toString)
      //字段拼接
      fields.foreach(tableField=>{
        val field =  tableField.toString.split("\\.")(1)
        val value =  config.getProperty(tableField.toString)
        sql = sql + field
        value match {
          case "string"=> sql = sql + " string,"
          case "long"=> sql = sql + " string,"
          case "double"=> sql = sql + " string,"
          case _ => sql = sql + " string,"
        }
      })

      sql =sql.substring(0,sql.length-1)
      print(sql)
      sql = sql + s") partitioned by (city string) STORED AS PARQUET LOCATION '${rootPath}/${table}'"
      hiveSqlMap.put(table.toString,sql)
    })
    hiveSqlMap
  }


  /**
    * 获取数据结构
    * @return
    */
  def getSchema(): java.util.HashMap[String, StructType] ={

    val mapStructType = new java.util.HashMap[String, StructType]
    //对表进行遍历构造
    for(table<-tables){
      val arrayStructFields = ArrayBuffer[StructField]()

      //构造一个arrayStructFields
      //获取该类型的所有字段
      val tableFields = config.getKeys(table.toString)
      while (tableFields.hasNext){
        val key = tableFields.next()   //带前缀
        val field = key.toString.split("\\.")(1)  //真实字段
        val fieldType = config.getProperty(key.toString)   //配置文件里面的数据类型

        //模式匹配进行类型转化
        fieldType match {
          case "string" =>arrayStructFields += StructField(field,StringType,true)
          case "long" =>arrayStructFields += StructField(field,StringType,true)
          case "double" =>arrayStructFields += StructField(field,StringType,true)
          case _ =>
        }
      }
      val schema = StructType(arrayStructFields) //接受一个DtructField的Seq
      mapStructType.put(table.toString,schema)
    }
    mapStructType
  }



  /**
    * 初始化hivesql
    *
    * @return
    */
  def createSQL(): java.util.HashMap[String, String] = {

    val hiveSqlMap = new java.util.HashMap[String, String]()

    tables.foreach(table => {
      var sql = s"CREATE EXTERNAL TABLE IF NOT EXISTS ${table} ("
      //中间的字段
      val fields = config.getKeys(table.toString)
      //字段拼接
      fields.foreach(tableField=>{
          val field =  tableField.toString.split("\\.")(1)
          val value =  config.getProperty(tableField.toString)
          sql = sql + field
          value match {
            case "string"=> sql = sql + " string,"
            case "long"=> sql = sql + " string,"
            case "double"=> sql = sql + " string,"
            case _ => sql = sql + " string,"
          }
      })

      sql =sql.substring(0,sql.length-1)
      sql = sql + s")STORED AS PARQUET LOCATION '/user/hive/external/${table}'"
      hiveSqlMap.put(table.toString,sql)
    })
    hiveSqlMap
  }


  /**
    * 字段映射配置文件读取
    *
    * @param path
    * @return
    */
  def readCompositeConfiguration(path: String): CompositeConfiguration = {
    //配置文件集合
    val compositeConfiguration = new CompositeConfiguration()
    val configuration = new PropertiesConfiguration(path)
    compositeConfiguration.addConfiguration(configuration)
    compositeConfiguration
  }


}
