/**
 * 
 */
package com.it.hbase.exractor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StrToByteExtrator implements RowExtractor<Map<String,byte[]>> ,Serializable {

	private static final long serialVersionUID = 4633698173362569711L;
	
	private Map<String,byte[]> row;
	
	@Override
	public Map<String, byte[]> extractRowData(Result result, int rowNum)
														throws IOException {
		
		row = new HashMap<String,byte[]>();
		
		for(Cell cell :  result.listCells()) {
			row.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
					Bytes.copy(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
		}
		return row;
	}

}
