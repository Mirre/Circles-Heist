package com.mirre.ball.utils;

import java.io.Serializable;

public class BiValue<T1, T2> implements Serializable {
	
	private static final long serialVersionUID = 0L;
	private T1 first;
	private T2 second;
	
	protected BiValue() {}
	
	public BiValue(T1 first, T2 second){
	  this.first = first;
	  this.second = second;
	}
	
	public T1 getFirst(){
	  return this.first;
	}
	
	public void setFirst(T1 first){
	  this.first = first;
	}
	
	public T2 getSecond(){
	  return this.second;
	}
	
	public void setSecond(T2 second){
	  this.second = second;
	}
	
	public String toString(){
	  return "" + this.first + " & " + this.second + "";
	}
	
	@SuppressWarnings("rawtypes")
	public boolean equals(Object o){
	  if (this == o) {
	    return true;
	  }
	  if ((o == null) || (getClass() != o.getClass())) {
	    return false;
	  }
	  BiValue biValue = (BiValue)o;
	  if (this.first != null ? this.first.equals(biValue.first) : biValue.first == null) {}
	  return this.second != null ? this.second.equals(biValue.second) : biValue.second == null;
	}
	
	public int hashCode(){
	  int result = this.first != null ? this.first.hashCode() : 0;
	  result = 31 * result + (this.second != null ? this.second.hashCode() : 0);
	  return result;
	}
}
