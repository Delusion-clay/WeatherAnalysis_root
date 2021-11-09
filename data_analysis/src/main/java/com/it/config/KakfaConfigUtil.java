package com.it.config;

import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * @description: 读取kafka的配置
 */
public class KakfaConfigUtil {
    private static final Logger LOG = Logger.getLogger(KakfaConfigUtil.class);

    //定义kafka的配置文件路径
    private static final String DEFAULT_KAFKA_CONFIG_PATH = "kafka/kafka-server-config.properties";

    private static volatile KakfaConfigUtil kakfaConfigUtil =null;

    //这个是最终需要的
    private Properties kafkaProperties;
    private KakfaConfigUtil(){
        try {
            //初始化配置
            LOG.info("开始读取配置文件"+ DEFAULT_KAFKA_CONFIG_PATH);
            kafkaProperties = ConfigUtil.getInstance().getProperties(DEFAULT_KAFKA_CONFIG_PATH);
            LOG.info("获取配置文件成功");
        } catch (Exception e) {
            LOG.error(null,e);
            e.printStackTrace();
        }

    }
    //对外访问
    public static KakfaConfigUtil getInstance(){
        //双重否定
        if(kakfaConfigUtil == null){
            synchronized (KakfaConfigUtil.class){
                if (kakfaConfigUtil == null){
                    kakfaConfigUtil = new KakfaConfigUtil();
                }
            }
        }
        return kakfaConfigUtil;
    }
    public Properties getKafkaProperties(){
        return kafkaProperties;
    }

}
