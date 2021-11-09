package com.it.hbase.search;

import com.it.hbase.exractor.RowExtractor;
import org.apache.hadoop.hbase.client.Scan;

public class SearchMoreTable<T> {
	
	private String tableName;
	private Scan scan;
	private RowExtractor<T> extractor;
	
	public SearchMoreTable() {
		super();
	}
	
	public SearchMoreTable(String tableName, Scan scan,
			RowExtractor<T> extractor) {
		super();
		this.tableName = tableName;
		this.scan = scan;
		this.extractor = extractor;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Scan getScan() {
		return scan;
	}
	public void setScan(Scan scan) {
		this.scan = scan;
	}
	public RowExtractor<T> getExtractor() {
		return extractor;
	}
	public void setExtractor(RowExtractor<T> extractor) {
		this.extractor = extractor;
	}
	
	

}
