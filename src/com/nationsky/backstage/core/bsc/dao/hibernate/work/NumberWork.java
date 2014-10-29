/**
 * 
 */
package com.nationsky.backstage.core.bsc.dao.hibernate.work;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;

/**
 * 功能：hibernate调用底层或者存储过程时候用来保存一些值
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public abstract class NumberWork implements Work {

	private int number;
	/* (non-Javadoc)
	 * @see org.hibernate.jdbc.Work#execute(java.sql.Connection)
	 */
	@Override
	public abstract void execute(Connection conn) throws SQLException;
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	

}
