package com.it.hbase.exractor;

import org.apache.hadoop.hbase.client.Result;

import java.io.IOException;
import java.io.Serializable;


public class OneColumnRowByteExtrator implements RowExtractor<byte[]> ,Serializable{

	
	private static final long serialVersionUID = -3420092335124240222L;
	
	private byte[] cf;
	private byte[] cl;

	public OneColumnRowByteExtrator( byte[] cf,byte[] cl ){
		
		this.cf = cf;
		this.cl = cl;
	}
	
	public byte[] extractRowData(Result result, int rowNum) throws IOException {
		
		return result.getValue(cf, cl);
	}

}
