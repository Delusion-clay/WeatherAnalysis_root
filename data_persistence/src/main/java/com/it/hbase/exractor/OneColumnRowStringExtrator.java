/**
 * 
 */
package com.it.hbase.exractor;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.Serializable;


public class OneColumnRowStringExtrator implements RowExtractor<String>  , Serializable{
	
	private static final long serialVersionUID = -8585637277902568648L;
	
	private byte[] cf ;
	private byte[] cl ;

	public OneColumnRowStringExtrator( byte[] cf , byte[] cl ){
		
		this.cf = cf;
		this.cl = cl;
	}
	
	/* (non-Javadoc)
	 * @see com.bh.d406.bigdata.hbase.extractor.RowExtractor#extractRowData(org.apache.hadoop.hbase.client.Result, int)
	 */
	@Override
	public String extractRowData(Result result, int rowNum) throws IOException {
		
		byte[] value = result.getValue(cf, cl);
		if( value == null ) return null;
		
		return  Bytes.toString( value ) ;
	}

}
