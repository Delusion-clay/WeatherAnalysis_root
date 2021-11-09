package com.it.hbase.exractor;

import com.it.hbase.entity.HBaseRow;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class MultiVersionRowExtrator implements RowExtractor<HBaseRow>{
	
	private HBaseRow row;
	
	public HBaseRow extractRowData(Result result, int rowNum)  {
		//构造一个HBaseRow
		row = new HBaseRow(Bytes.toString(result.getRow()));

		String field = null;
		String value = null;
		long capTime = 0L;

		//遍历所有的cells 将最后的字段 值  时间放入row
		for(Cell cell : result.listCells()){
			field = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
			value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
			capTime = cell.getTimestamp();
			row.addCell(field, value, capTime);
		}
		return  row ;
	}

}
