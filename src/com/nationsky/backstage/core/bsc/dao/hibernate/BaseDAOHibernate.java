package com.nationsky.backstage.core.bsc.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.PK;
import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.bsc.dao.IDAO;
import com.nationsky.backstage.util.StringUtil;

/**
 * 功能：DAO的基本实现类(非缓存)
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class BaseDAOHibernate extends DAOHibernate implements IDAO{
	
	public int countQuery(final String hql) {
				
		return this.getHibernateTemplate().execute(new HibernateCallback<Integer>(){
			public Integer doInHibernate(Session session){
				Query query = session.createQuery(hql);
				return StringUtil.getInt(query.uniqueResult());
			}
		});
	}

	
	public int delete(List<? extends PO> pos) {
		
		getHibernateTemplate().deleteAll(pos);
		return pos.size();
	}

	
	public int delete(PO po) {
		
		this.getHibernateTemplate().delete(po);
		return 1;
	}

	
	public int deleteById(Class<? extends PO> poc, Serializable id) {
		
		if(id instanceof PK){
			getHibernateTemplate().delete(getHibernateTemplate().load(poc, id));
		}else{
			this.execute(StringUtil.concat(" DELETE FROM ",poc.getSimpleName()," WHERE id='",id,"'"));
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see eg.core.bsc.dao.DAO#findPropsList(java.lang.Class, java.util.List, int, int, java.lang.String, eg.core.Factor[])
	 */	
	public <T extends PO> List<?> findPropsList(final Class<T> poc,
			final List<String> propNames,final int first,final int size,final String sort,
			final Factor... factors) {
		
		return (List<?>)this.getHibernateTemplate().executeFind(new HibernateCallback(){

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				if(propNames!=null && !propNames.isEmpty()){
					ProjectionList pl = Projections.projectionList();
					for(String propName:propNames){
						pl.add(Projections.property(propName));
					}
					criteria.setProjection(pl);
				}
				criteria.setFirstResult(first).setMaxResults(size);
				return addSort(criteria, sort).list();
			}
			
		});
	}


	public <T extends PO> List<T> find(final Class<T> poc,final int first,final int size,
			final String sort,final Factor... factors) {
				
		return (List<T>)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				criteria.setFirstResult(first).setMaxResults(size);
				return addSort(criteria, sort).list();
			}
		});
	}

	public List<PO> findByExample(PO po) {
		
		return getHibernateTemplate().findByExample(po);
	}

	public <T extends PO> List<T> findAll(Class<T> poc, String sort) {
		
		return this.find(poc ,0, Integer.MAX_VALUE, StringUtil.get(sort,"id:DESC"));
	}

	public <T extends PO> T get(Class<T> poc, Serializable id, boolean lock) {
		
		PO po;
		if(lock){
			po = (PO)this.getHibernateTemplate().get(poc, id, LockMode.PESSIMISTIC_WRITE);
		}
		else{
			po = (PO)this.getHibernateTemplate().get(poc, id);
		}
		return (T) po;
	}

	
	public <T extends PO> T get(Class<T> poc, Serializable id) {
		
		return this.get(poc, id, false);
	}

	
	public <T extends PO> int getCount(final Class<T> poc,final Factor... factors) {
				
		return (Integer)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				return StringUtil.getInt(criteria.setProjection(Projections.rowCount()).uniqueResult());
			}
		});
	}
	
	public <T extends PO> T getUnique(final Class<T> poc,final Factor... factors) {
				
		return (T)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				criteria.setMaxResults(1);
				return (PO)criteria.uniqueResult();
			}
		});
	}
	
	public <T extends PO> T getUnique(final Class<T> poc,final String sort, final Factor... factors) {
				
		return (T)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				addSort(criteria, sort);
				criteria.setMaxResults(1);
				return (PO)criteria.uniqueResult();
			}
		});
	}
	
	public <T extends PO> T getUnique(final Class<T> poc,final String sort,final int first, final Factor... factors) {
				
		return (T)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				addSort(criteria, sort);
				criteria.setFirstResult(first);
				criteria.setMaxResults(1);
				return (PO)criteria.uniqueResult();
			}
		});
	}

	
	/* (non-Javadoc)
	 * @see eg.core.bsc.dao.DAO#isHasMatch(java.lang.Class, eg.core.Factor[])
	 */
	public boolean exists(final Class<? extends PO> poc, final Factor... factors) {
		
		return (Boolean)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Criteria criteria = session.createCriteria(poc);
				if(factors!=null)
					for(Factor f : factors){
						Criterion c = addFactor(null, f);
						if(c!=null)criteria.add(c);
					}
				criteria.setProjection(Projections.rowCount());
				return StringUtil.getInt(criteria.uniqueResult())>0;
			}
		});
	}


	public void insert(PO po) {
		
		this.getHibernateTemplate().save(po);
	}

	
	public int insertOrUpdate(PO po) {
		
		this.getHibernateTemplate().saveOrUpdate(po);
		return 1;
	}	

	public List<PO> query(final String hql,final int first,final int size,final String sort) {
				
		return (List<PO>)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Query query = session.createQuery(addSort(new StringBuilder().append(hql), sort).toString());
				query.setFirstResult(first).setMaxResults(size);
				return query.list();
			}
		});
	}

	
	public int update(PO po) {
		
		this.getHibernateTemplate().update(po);
		return 1;
	}


	/* (non-Javadoc)
	 * @see eg.core.bsc.dao.DAO#deleteAll(java.lang.Class)
	 */
	public void deleteAll(final Class<? extends PO> poc) {
		
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				String hql = " DELETE FROM " + poc.getName();
				return session.createQuery(hql).executeUpdate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see eg.core.bsc.dao.DAO#execute(String)
	 */
	public int execute(final String hql) {
				
		return (Integer)this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session){
				Query query = session.createQuery(hql);
				return query.executeUpdate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see eg.core.bsc.dao.DAO#find(java.lang.String)
	 */
	@Override
	public List<?> findList(final String hql ,final int start, final int size) {
		
		return (List<?>)this.getHibernateTemplate().executeFind(new HibernateCallback<List<?>>(){

			@Override
			public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(start>0)query.setFirstResult(start);
				if(size>0)query.setMaxResults(size);
				return query.list();
			}
		});
	}	

}
