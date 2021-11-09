/**
 * 
 */
package com.it.hbase.exractor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MapLongRowExtrator implements RowExtractor<Map<String,Long>> {

	private Map<String,Long> row;
	
	@Override
	public Map<String, Long> extractRowData(Result result, int rowNum)
			throws IOException {
		
		row = new HashMap<String,Long>();
		
		for(Cell cell :  result.listCells()) {
			row.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),Bytes.toLong(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
		}
		return row;
	}

}
