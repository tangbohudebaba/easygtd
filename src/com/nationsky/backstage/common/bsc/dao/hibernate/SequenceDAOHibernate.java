/**
 * 
 */
package com.nationsky.backstage.common.bsc.dao.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.nationsky.backstage.common.bsc.dao.SequenceDAO;
import com.nationsky.backstage.core.bsc.dao.hibernate.DAOHibernate;
import com.nationsky.backstage.core.bsc.dao.hibernate.work.NumberWork;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
@Repository
public class SequenceDAOHibernate extends DAOHibernate implements SequenceDAO {

	
	public Long genNextLongPK(final String key) {
		Long result = this.getHibernateTemplate().execute(new HibernateCallback<Long>(){
			public Long doInHibernate(Session session) throws HibernateException,
					SQLException {
				NumberWork work = new NumberWork(){

					@Override
					public void execute(Connection conn) throws SQLException {
						CallableStatement cs =  conn.prepareCall("{call gen_next_sequence_pk(?,?)}");
						cs.setString(1, key);
						cs.registerOutParameter(2, java.sql.Types.INTEGER);
						cs.execute();
						setNumber(cs.getInt(2));
					}
					
				};
				session.doWork(work);
				return Long.valueOf(work.getNumber());
			}
		});
		return result;
	}

}
