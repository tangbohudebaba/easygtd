package com.nationsky.backstage.core;

/**
 * 功能：hibernate查询因子
 * 说明：其中In And Between or 都是数组 
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class Factor {
	
	public enum C{Eq,Ne,Ge,Gt,Le,Lt,Like,And,Or,Between,In,Sql,Null,NotNull};
	public static final Factor NULL_FACTOR = Factor.create(null, C.Eq, null);
	
	private String property;
	private Object value;
	private C c;
	
	public static Factor create(String property,String cs,Object value){
		try{
			return create(property,C.valueOf(cs),value);
		}catch(Exception e){
			return NULL_FACTOR;
		}
	}
	
	public static Factor create(String property,C c,Object value){
		Factor f = new Factor();
		f.setC(c);
		f.setProperty(property);
		f.setValue(value);
		return f;
	}
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return the c
	 */
	public C getC() {
		return c;
	}
	/**
	 * @param c the c to set
	 */
	public void setC(C c) {
		this.c = c;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getProperty()).append(this.getC() == Factor.C.Eq?"=":this.getC()).append(this.getValue().toString());
		return sb.toString();
	}
}
