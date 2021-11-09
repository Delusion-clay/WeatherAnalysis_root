package com.it.hbase.exractor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class BaseMapWithRowKeyExtrator implements RowExtractor<Map<String,String>> {

	private Map<String,String> row;

	/* (non-Javadoc)
	 * @see com.bh.d406.bigdata.hbase.extractor.RowExtractor#extractRowData(org.apache.hadoop.hbase.client.Result, int)
	 */
	@Override
	public Map<String, String> extractRowData(Result result, int rowNum)
			throws IOException {
		
		row = new HashMap<String,String>();
		row.put("rowKey", Bytes.toString( result.getRow() ));
		
		for(Cell cell :  result.listCells()) {
			row.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
		}
		return row;
	}
}
