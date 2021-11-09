package com.it.hbase.search;

import com.it.hbase.exractor.RowExtractor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HBaseSearchService {

	/**
	  * description:  根据  用户 给定的解析类  解析  查询结果
	  * @param tableName
	  * @param scan
	  * @param extractor  用户自定义的 结果解析 类
	  * @return
	  * @throws IOException
	  * List<T>
	  * 2014-1-30 下午3:33:40
	  * by 
	 */
	<T> List<T> search(String tableName, Scan scan, RowExtractor<T> extractor) throws IOException;

	/**
	  * description: 当存在多个  scan时  采用多线程查询  
	  * @param tableName
	  * @param scans
	  * @param extractor  用户自定义的 结果解析 类
	  * @return
	  * @throws IOException
	  * List<T>
	  * 2014-1-30 下午3:35:16
	  * by 
	 */
	<T> List<T> searchMore(String tableName, List<Scan> scans, RowExtractor<T> extractor) throws IOException;
	
	/**
	  * description:  采用多线程  同时查询多个表
	  * @param more
	  * @return
	  * @throws IOException
	  * List<T>
	  * 2014-1-30 下午3:44:18
	  * by 
	 */
	<T> Map<String,List<T>> searchMore(List<SearchMoreTable<T>> more) throws IOException;
	
	/**
	  * description:  利用反射  自动封装实体类
	  * @param tableName
	  * @param scan    
	  * @param cls   HBase表对应的实体类，属性只包含对应表的 列 ， 不区分大小写
	  * @return
	  * @throws IOException
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * List<T>
	  * 2014-1-30 下午2:05:15
	  * by 
	 */
	<T> List<T> search(String tableName, Scan scan, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException;
	
	/**
	  * description:   当存在多个 scan 时  采用多线程查询
	  * @param tableName
	  * @param scans
	  * @param cls   HBase表对应的实体类，属性只包含对应表的 列 ， 不区分大小写
	  * @return
	  * @throws IOException
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * List<T>
	  * 2014-1-30 下午3:35:46
	  * by 
	 */
	<T> List<T> searchMore(String tableName, List<Scan> scans, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException;

	
	/**
	  * description: 批量 get 查询  并按自定义的方式解析结果集
	  * @param tableName
	  * @param gets
	  * @param extractor  用户自定义的 结果解析 类
	  * @return
	  * @throws IOException
	  * List<T>
	  * 2014-1-30 下午4:30:27
	  * by 
	 */
	<T> List<T> search(String tableName, List<Get> gets, RowExtractor<T> extractor) throws IOException;
	
	/**
	  * description:  多线程批量get, 并按自定义的方式解析结果集
	  * 			      建议 : perThreadExtractorGetNum >= 100
	  * @param tableName
	  * @param gets
	  * @param perThreadExtractorGetNum    每个线程处理的 get的个数 
	  * @param extractor  用户自定义的 结果解析 类
	  * @return
	  * @throws IOException
	  * List<T>
	  * 2015-9-25 上午9:08:23
	 */
	<T> List<T> searchMore(String tableName, List<Get> gets, int perThreadExtractorGetNum, RowExtractor<T> extractor) throws IOException;
	
	/**
	  * description: 批量 get 查询  并利用反射 封装到指定的实体类中
	  * @param tableName
	  * @param gets
	  * @param  cls   HBase表对应的实体类，属性只包含对应表的 列 ， 不区分大小写
	  * @return      
	  * @throws IOException
	  * @throws InstantiationException
	  * List<T>
	  * 2014-1-30 下午4:30:27
	 */
	<T> List<T> search(String tableName, List<Get> gets, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException;
	
	/**
	  * description: 多线程批量 get 查询  并利用反射 封装到指定的实体类中
	  *              建议 : perThreadExtractorGetNum >= 100
	  * @param tableName
	  * @param gets
	  * @param perThreadExtractorGetNum  每个线程处理的 get的个数 
	  * @param  cls   HBase表对应的实体类，属性只包含对应表的 列 ， 不区分大小写
	  * @return
	  * @throws IOException
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * List<T>
	  * 2015-9-25 上午9:11:23
	 */
	<T> List<T> searchMore(String tableName, List<Get> gets, int perThreadExtractorGetNum, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException;
	
	/**
	  * description:  get 查询  并按自定义的方式解析结果集
	  * @param tableName
	  * @param extractor   用户自定义的 结果解析 类
	  * @return     如果 查询不到  则 返回  null
	  * @throws IOException
	  * List<T>
	  * 2014-1-30 下午4:30:27
	 */
	<T> T search(String tableName, Get get, RowExtractor<T> extractor) throws IOException;
	
	/**
	  * description:  get 查询  并利用反射 封装到指定的实体类中
	  * @param tableName
	  * @param  cls   HBase表对应的实体类，属性只包含对应表的 列 ， 不区分大小写
	  * @return     如果 查询不到  则 返回  null
	  * @throws IOException
	  * @throws InstantiationException
	  * List<T>
	  * 2014-1-30 下午4:30:27
	  * by
	 */
	<T> T search(String tableName, Get get, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException;



}


