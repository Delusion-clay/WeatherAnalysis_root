package com.it.hbase.exractor;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 把hbase最终结果转为MAP存储
 */
public class BaseMapRowExtrator implements RowExtractor<Map<String,String>> {

	private Map<String,String> row;
	
	private List<byte[]> rows;
	private String longTimeField;
	private SimpleDateFormat format;
	
	private String field;
	private String value;
	
	private long time;
	
	public BaseMapRowExtrator(){}
	
	/**
	 * @param rows   需要提取 所有的 rowKey  , null 则不提取
	 */
	public BaseMapRowExtrator(List<byte[]> rows){
		
		this.rows = rows;
	}
	
	
	/**
	 * @param rows             需要提取 所有的 rowKey  , null 则不提取
	 * @param longTimeField    long类型的时间字段   表示需要将其转换称 String 类型
	 */
	public BaseMapRowExtrator(List<byte[]> rows,String longTimeField){
		
		this.rows = rows;
		this.longTimeField = longTimeField;
	}
	
	/**
	 * @param rows                  需要提取 所有的 rowKey  , null 则不提取
	 * @param longTimeField         long类型的时间字段
	 * @param timePattern           表示需要已该指定的格式  将时间字段的值转换成字符串
	 */
	public BaseMapRowExtrator(List<byte[]> rows,String longTimeField,String timePattern){
		
		this.rows = rows;
		this.longTimeField = longTimeField;
		
		if(StringUtils.isNotBlank(timePattern)){
			format = new SimpleDateFormat(timePattern);
		}
	}


	/**
	 * 实现接口的解析方法，将result解析为map
	 * @param result   result 解析器
	 * @param rowNum
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> extractRowData(Result result, int rowNum)
			throws IOException {
		
			row = new HashMap<String,String>();
			
			List<Cell> cells = result.listCells();
			for(Cell cell :  cells) {
				
				field = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
				if( field.equals(longTimeField)  ){
					time = Bytes.toLong(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
					if( format != null ){
						value = format.format(new Date(time));
					}else{
						value = String.valueOf(time);
					}
				}else{
					value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
				}
				row.put(field,value);
			}
			
			if( rows != null ){
				rows.add(result.getRow());
			}
		return row;
	}

}
