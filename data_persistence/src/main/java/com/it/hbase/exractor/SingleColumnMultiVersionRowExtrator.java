package com.it.hbase.exractor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Set;


/**
 * 单字段多版本解析器
 */
public class SingleColumnMultiVersionRowExtrator implements RowExtractor<Set<String>>{
	
	private Set<String> values;
	private byte[] cf;
	private byte[] cl;


	/**
	 * 单列解析器  获取hbase 单列多版本数据
	 * @param cf     列簇
	 * @param cl     列
	 * @param values 返回值
	 */
	public SingleColumnMultiVersionRowExtrator(byte[] cf, byte[] cl, Set<String> values){
		this.cf = cf;
		this.cl = cl;
		this.values = values;
	}
	
	public Set<String> extractRowData(Result result, int rowNum) throws IOException {

		for(Cell cell : result.getColumnCells(cf, cl)){
			values.add(Bytes.toString(cell.getValueArray(),cell.getValueOffset(), cell.getValueLength()));
		}
		return values;
	}

}
