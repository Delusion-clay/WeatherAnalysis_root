/**
  * Copyright (c) 2016, jechedo All Rights Reserved.
  *
 */
package com.it.hbase.exractor;

import com.google.common.collect.Maps;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;


public class BeanRowExtrator<T> implements RowExtractor<T> {

	private static final Logger LOG = LoggerFactory.getLogger(BeanRowExtrator.class);
	
	private Class<T> clazz;
	private Map<String,Field> fieldMap;
	
	public BeanRowExtrator(Class<T> clazz){
		this.clazz = clazz;
		this.fieldMap = getDeclaredFields(clazz);
	}

	public T extractRowData(Result result, int rowNum) throws IOException {
		
		return resultReflectToClass(result, rowNum);
	}
	
	private T resultReflectToClass(Result result, int rowNum){
		
		String column = null;
		Field field = null;
		T obj = null;
		try {
			obj = clazz.newInstance();
			for(Cell cell : result.listCells()){
				column = Bytes.toString(cell.getQualifierArray(),
						cell.getQualifierOffset(), cell.getQualifierLength());
				/*检查该列是否在实体类中存在对应的属性,若存在则 为其赋值*/
				if((field = fieldMap.get(column.toLowerCase())) != null){
					field.set(obj, Bytes.toString(cell.getValueArray(),
							cell.getValueOffset(), cell.getValueLength()));
				}
			}
			
		} catch (InstantiationException e) {
			LOG.error(String.format("解析第%个满足条件的记录%s失败。", rowNum, result), e);
		} catch (IllegalAccessException e) {
			LOG.error(String.format("解析第%s个满足条件的记录%s失败。", rowNum, result), e);
		}
		return obj;
	}
	
	private  Map<String,Field>  getDeclaredFields(Class<?> clazz){
		
		Field[] fields = clazz.getDeclaredFields();
		Field field = null;
		Map<String,Field> fieldMap = Maps.newHashMapWithExpectedSize(fields.length);
		
		for(int i = 0; i < fields.length; i++){
			field = fields[i];
			if(field.getModifiers() == 2){
				field.setAccessible(true);
				fieldMap.put(field.getName().toLowerCase(), field);
			}
		}
		fields = null;
		
		return fieldMap;
	}

}
