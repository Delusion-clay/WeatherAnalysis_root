/**
 * 
 */
package com.it.hbase.exractor;

import org.apache.hadoop.hbase.client.Result;

import java.io.IOException;


public class OnlyRowKeyExtrator implements RowExtractor<byte[]> {

	
	@Override
	public byte[] extractRowData(Result result, int rowNum) throws IOException {
		// TODO Auto-generated method stub
		return result.getRow();
	}

}
