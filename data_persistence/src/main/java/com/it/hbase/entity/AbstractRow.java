/**
  * Copyright (c) 2016, jechedo All Rights Reserved.
  *
 */
package com.it.hbase.entity;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class AbstractRow<T extends HBaseCell> {
	
	protected String rowKey;
	protected HashMultimap<String, T> cells;

    protected Set<String> fields;
	protected long maxCapTime;
	
	public AbstractRow(String rowKey){
		this.rowKey = rowKey;
		cells = HashMultimap.create();
        fields = Sets.newHashSet();
    }
	
	public boolean addCell(String field, String value, long capTime){
		
		return addCell(field, createCell(field, value, capTime));
	}
	
	public boolean addCell(String field, T cell){

        fields.add(cell.getField());

		if(cell.getCapTime() > maxCapTime)
			maxCapTime = cell.getCapTime();

		return cells.put(field, cell);
	}
	
	public boolean[] addCell(String field, Collection<T> cells){
		
		boolean[] status = new boolean[cells.size()];
		int n = 0;
		for(T cell : cells){
			status[n] = addCell(field, cell);
			n++;
		}
		return status;
	}

	public String getRowKey() {
		return rowKey;
	}
	
	protected abstract T createCell(String field, String value, long capTime);

	public Map<String, Collection<T>> getCell() {
		return cells.asMap();
	}
	
	public Collection<T> getCellByField(String field){
		return cells.get(field);
	}

	public Set<Map.Entry<String, T>> entries(){
		return  cells.entries();
	}

	@Override
	public String toString() {
		return "AbstractRow [rowKey=" + rowKey + ", cells=" + cells + "]";
	}
	
	public boolean equals(Object obj) {
		
	   if(this == obj)return true ;
	   if(!(obj instanceof AbstractRow))return false ;
		
	   @SuppressWarnings("unchecked")
	   AbstractRow<T> row = (AbstractRow<T>) obj;
	   if(rowKey.equals(row.getRowKey()))return true;
	   return false;
	}
			
	public int hashCode(){
		return this.rowKey.hashCode();
	}

	public long getMaxCapTime() {
		return maxCapTime;
	}

    public Set<String> getFields() {
        return Sets.newHashSet(fields);
    }
}
