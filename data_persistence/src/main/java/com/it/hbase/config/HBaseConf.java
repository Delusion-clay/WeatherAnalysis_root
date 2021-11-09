package com.it.hbase.config;

import com.it.hbase.split.SpiltRegionUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author:
 * @description:  读取HBASE配置文件  获取连接
 * @Date:Created in 2019-03-29 22:15
 */
public class HBaseConf implements Serializable {
    //读取HBASE配置文件
    //获取连接
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(HBaseConf.class);
    //配置路径
    private static final String HBASE_SITE = "hbase/hbase-site.xml";

    private volatile static HBaseConf hbaseConf;

    //hbase 配置文件
    private Configuration configuration;
    //hbase 连接
    private volatile Connection conn;

    /**
     * 初始化HBaseConf的时候加载配置文件
     */
    private HBaseConf() {
        getHconnection();
    }

    /**
     * 单例 初始化HBaseConf
     * @return
     */
    public static HBaseConf getInstance() {
        if (hbaseConf == null) {
            synchronized (HBaseConf.class) {
                if (hbaseConf == null) {
                    hbaseConf = new HBaseConf();
                }
            }
        }
        return hbaseConf;
    }


    //获取连接
    public Configuration getConfiguration(){
        if(configuration==null){
            configuration = HBaseConfiguration.create();
            //通过addResource方法把hbase配置文件加载进来
            configuration.addResource(HBASE_SITE);
            System.out.println("加载配置文件" + HBASE_SITE + "成功");
            LOG.info("加载配置文件" + HBASE_SITE + "成功");
        }
        return configuration;
    }

    public BufferedMutator getBufferedMutator(String tableName) throws IOException {
        return getHconnection().getBufferedMutator(TableName.valueOf(tableName));
    }


    public Connection getHconnection(){

        if(conn==null){
            //加载配置文件
            getConfiguration();
            synchronized (HBaseConf.class) {
                if (conn == null) {
                    try {
                        conn = ConnectionFactory.createConnection(configuration);
                    } catch (IOException e) {
                        LOG.error(String.format("获取hbase的连接失败  参数为： %s", toString()), e);
                    }
                }
            }
        }
        return conn;
    }


    public static void main(String[] args) {
        String hbase_table = "bbb:aaaaa";
        //System.out.println(HBaseTableUtil.deleteTable("test:aaaa"));
        HBaseTableUtil.createTable(hbase_table, "cf", true, -1, 1, SpiltRegionUtil.getSplitKeysBydinct());
    }
}
