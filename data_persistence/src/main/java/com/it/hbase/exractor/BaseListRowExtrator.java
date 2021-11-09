package com.it.hbase.exractor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseListRowExtrator implements RowExtractor<List<String>>{

	private List<String> row;

	public Long lastcjtime = 0l;

	public Long firstcjtime = 0l;

	@Override
	public List<String> extractRowData(Result result, int rowNum)
			throws IOException {

		row = new ArrayList<String>();
		for(Cell cell :  result.listCells()) {
			String column = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
			String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
			if(column.equalsIgnoreCase("cjtime")) {
				Long v = Long.parseLong(value);
				if(lastcjtime < v) {
					lastcjtime = v;
				}else if(firstcjtime > v) {
					firstcjtime = v;
				}
			}
			row.add(value);
		}
		return row;
	}

}
