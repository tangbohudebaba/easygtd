/**
 * 
 */
package com.nationsky.backstage.core.bsc.dao.hibernate.work;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.jdbc.Work;

/**
 * 功能：调用hibernate的存储过程或者底层sql时候使用
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public abstract class ListWork implements Work {

	private List<?> list;
	/* (non-Javadoc)
	 * @see org.hibernate.jdbc.Work#execute(java.sql.Connection)
	 */
	@Override
	public abstract void execute(Connection arg0) throws SQLException ;
	/**
	 * @return the list
	 */
	public List<?> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Object> list) {
		this.list = list;
	}

}
