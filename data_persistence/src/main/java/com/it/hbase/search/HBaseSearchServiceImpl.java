package com.it.hbase.search;

import com.it.hbase.config.HBaseTableUtil;
import com.it.hbase.exractor.RowExtractor;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class HBaseSearchServiceImpl implements HBaseSearchService,Serializable{

	private static final long serialVersionUID = -8657479861137115645L;

	private static final Logger LOG = LoggerFactory.getLogger(HBaseSearchServiceImpl.class);
	
	//private HBaseTableFactory factory = new HBaseTableFactory();
	private int poolCapacity = 6;


	@Override
	public <T> List<T> search(String tableName, Scan scan, RowExtractor<T> extractor) throws IOException {
		return null;
	}

	@Override
	public <T> List<T> searchMore(String tableName, List<Scan> scans, RowExtractor<T> extractor) throws IOException {
		return null;
	}

	@Override
	public <T> Map<String, List<T>> searchMore(List<SearchMoreTable<T>> more) throws IOException {
		return null;
	}

	@Override
	public <T> List<T> search(String tableName, Scan scan, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {
		return null;
	}

	@Override
	public <T> List<T> searchMore(String tableName, List<Scan> scans, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {
		return null;
	}

	@Override
	public <T> List<T> search(String tableName, List<Get> gets, RowExtractor<T> extractor) throws IOException {
		//最终返回结果集合
		List<T> data = new ArrayList<T>();
		search(tableName, gets, extractor,data);
		return data;
	}

	@Override
	public <T> List<T> searchMore(String tableName, List<Get> gets, int perThreadExtractorGetNum, RowExtractor<T> extractor) throws IOException {
		return null;
	}

	@Override
	public <T> List<T> search(String tableName, List<Get> gets, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {
		return null;
	}

	@Override
	public <T> List<T> searchMore(String tableName, List<Get> gets, int perThreadExtractorGetNum, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {
		return null;
	}

	@Override
	public <T> T search(String tableName, Get get, RowExtractor<T> extractor) throws IOException {

		T obj = null;
		List<T> res = search(tableName,Arrays.asList(get),extractor);
		if( !res.isEmpty()){
			obj = res.get(0);
		}

		return obj;
	}

	@Override
	public <T> T search(String tableName, Get get, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {
		return null;
	}

	private <T> void search(String tableName, List<Get> gets,
							RowExtractor<T> extractor , List<T> data ) throws IOException {

		//根据table名获取表连接
		//Table table = factory.getHBaseTableInstance(tableName);
		//获取表连接
		Table table = HBaseTableUtil.getTable(tableName);
		if(table != null ){
			Result[] results = table.get(gets);
			int n = 0;
			T row = null;
			for( Result result : results){
				if( !result.isEmpty() ){
					row = extractor.extractRowData(result, n);
					if(row != null )data.add(row);
					n++;
				}
			}
			close( table, null);
		}else{
			throw new IOException(" table  " + tableName + " is not exists ..");
		}

	}


	public static boolean  existsRowkey(Table table, String rowkey){
		boolean exists =true;
		try {
			exists = table.exists(new Get(rowkey.getBytes()));
		} catch (IOException e) {
			LOG.error("失败。", e );
		}
		return exists;
	}




	public static void  close(Table table, ResultScanner scanner ){

		try {
			if( table != null ){
				table.close();
				table = null;
			}
			if( scanner != null ){
				scanner.close();
				scanner = null;
			}
		} catch (IOException e) {
			LOG.error("关闭 HBase的表  " + table.getName().toString() + " 失败。", e );
		}

	}
}
