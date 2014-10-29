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
public abstract class ObjectWork implements Work {

	private Object object;
	/* (non-Javadoc)
	 * @see org.hibernate.jdbc.Work#execute(java.sql.Connection)
	 */
	@Override
	public abstract void execute(Connection arg0) throws SQLException ;
	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

}
