package com.it.config;

import java.util.HashMap;
import java.util.Map;


public class KafkaConfig {

    public static Map<String,Object> getKafkaConfig(String groupId){
        Map map = new HashMap<String,String>();
        map.put("bootstrap.servers","hadoop-101:9092");
        map.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        map.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        map.put("group.id",groupId);
        map.put("auto.offset.reset","earliest");
        map.put("enable.auto.commit",false);
        return map;
    }
}
