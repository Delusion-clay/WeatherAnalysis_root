/**
  * Copyright (c) 2016, jechedo All Rights Reserved.
  *
 */
package com.it.hbase.entity;


/**
 * 封装的是一个hbase cell
 */
public class HBaseCell implements Comparable<HBaseCell>{
	
	protected String field;           
	protected String value;
	protected Long capTime;

	public HBaseCell(String field, String value, long capTime){
		
		this.field = field;
		this.capTime = capTime;
		this.value = value;
	}
	
	public String getField(){
		return field;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setCapTime(long capTime) {
		this.capTime = capTime;
	}

	public Long getCapTime() {
		return capTime;
	}

	public String toString(){
		return String.format("%s_[%s]_%s", field, capTime, value);
	}

	public int compareTo(HBaseCell o) {
		return o.getCapTime().compareTo(this.capTime);
	}
	
	public boolean equals(Object obj) {
		
	   if(this == obj)return true ;
	   if(!(obj instanceof HBaseCell))return false ;
		
	   HBaseCell cell = (HBaseCell)obj;
	   if(field.equals(cell.getField()) && value.equals(cell.getValue())){
		   if(cell.getCapTime() < capTime){
			   cell.setCapTime(this.capTime);
		   }
		   return true;
	   }
	   return false;
	}
		
	public int hashCode(){
		return this.field.hashCode() +  31*this.value.hashCode();
	}

}
