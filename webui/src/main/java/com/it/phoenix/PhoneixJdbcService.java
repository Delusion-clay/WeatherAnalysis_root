package com.it.phoenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-08-05 15:35
 */
public class PhoneixJdbcService {
    public static void main(String[] args) throws Exception {

    }
    /**
     * @Description //TODO 历史数据查询展示
     * @Date 17:34 2021/8/31
     * @Param [city, year, month] 城市，年份，月份
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public static List<Map<String,Object>> getAll(String city,String year,String month) throws Exception {
        // 1. 加载Phoenix驱动类
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        //Class.forName("org.apache.phoenix.jdbc.PhoenixEmbeddedDriver");
        String url = "jdbc:phoenix:hadoop-101:2181";
        // String url = "jdbc:phoenix:hadoop-11,hadoop-12,hadoop-13:2181";
        // 2. 创建数据库连接
        Connection connection = DriverManager.getConnection(url);
        // 3. 获取Statement对象
        Statement statement = connection.createStatement();
        System.out.println(city+"\t"+year+"\t"+month);
        System.out.println(city+"\t"+year+"\t"+month);
        String sql = "select * from \""
                +city+"\" where \"year\" = \'"
                +year+ "\' and \"month\" = \'"+month+"\'";
        System.out.println(sql);

        // 4. 执行SQL，获取结果
        ResultSet resultSet = statement.executeQuery(sql);
        List<Map<String, Object>> mapList = new ArrayList<>();
        while(resultSet.next()){
            HashMap<String, Object> map = new HashMap<>();
            String rowkey = resultSet.getString("rowkey");
            String meteo = resultSet.getString("meteo");
            String week = resultSet.getString("week");
            String wind = resultSet.getString("wind");
            String temperature = resultSet.getString("temperature");
            map.put("rowkey",rowkey);
            map.put("city",city);
            map.put("meteo",meteo);
            map.put("week",week);
            map.put("wind",wind);
//            map.put("cityName",cityName);
            map.put("temperature",temperature);
            mapList.add(map);
        }
        // 6. 关闭连接
        resultSet.close();
        statement.close();
        connection.close();
        return mapList;
    }
    public static List<Map<String,Object>> getTemperature(String city,String year,String month) throws Exception {
        // 1. 加载Phoenix驱动类
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        //Class.forName("org.apache.phoenix.jdbc.PhoenixEmbeddedDriver");
        String url = "jdbc:phoenix:hadoop-101:2181";
        // String url = "jdbc:phoenix:hadoop-11,hadoop-12,hadoop-13:2181";
        // 2. 创建数据库连接
        Connection connection = DriverManager.getConnection(url);
        // 3. 获取Statement对象
        Statement statement = connection.createStatement();
        System.out.println(city+"\t"+year+"\t"+month);
        System.out.println(city+"\t"+year+"\t"+month);
        String sql = "select \"rowkey\",\"temperature\",\"meteo\" from \""
                +city+"\" where \"year\" = \'"
                +year+ "\' and \"month\" = \'"+month+"\'";
        System.out.println(sql);
        // 4. 执行SQL，获取结果
        ResultSet resultSet = statement.executeQuery(sql);
        List<Map<String, Object>> mapList = new ArrayList<>();
        while(resultSet.next()){
            HashMap<String, Object> map = new HashMap<>();
            String rowkey = resultSet.getString("rowkey");
            String temperature = resultSet.getString("temperature");
            map.put("rowkey",rowkey);
            map.put("city",city);
            map.put("temperature",temperature);
            mapList.add(map);
        }
        // 6. 关闭连接
        resultSet.close();
        statement.close();
        connection.close();
        return mapList;
    }
    public static List<Map<String,Object>> getMeteo(String city,String year,String month) throws Exception {
        // 1. 加载Phoenix驱动类
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        //Class.forName("org.apache.phoenix.jdbc.PhoenixEmbeddedDriver");
        String url = "jdbc:phoenix:hadoop-101:2181";
        // String url = "jdbc:phoenix:hadoop-11,hadoop-12,hadoop-13:2181";
        // 2. 创建数据库连接
        Connection connection = DriverManager.getConnection(url);
        // 3. 获取Statement对象
        Statement statement = connection.createStatement();
        System.out.println(city+"\t"+year+"\t"+month);
        System.out.println(city+"\t"+year+"\t"+month);
//        select "meteo" as "name",count("meteo") as "value" from "anshun" where "year"='2020' and "month" = '01' group by "meteo";

        String sql = "select \"meteo\" as \"name\",count(\"meteo\") as \"value\" from \""
                +city+"\" where \"year\" = \'"
                +year+ "\' and \"month\" = \'"+month+"\'"+" group by \"meteo\"";
        System.out.println(sql);
        // 4. 执行SQL，获取结果
        ResultSet resultSet = statement.executeQuery(sql);
        List<Map<String, Object>> mapList = new ArrayList<>();
        while(resultSet.next()){
            HashMap<String, Object> map = new HashMap<>();
            String name = resultSet.getString("name");
            String value = resultSet.getString("value");
            map.put("name",name);
            map.put("value",value);
            mapList.add(map);
        }
        // 6. 关闭连接
        resultSet.close();
        statement.close();
        connection.close();
        return mapList;
    }
}
