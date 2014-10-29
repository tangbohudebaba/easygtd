package com.nationsky.backstage.core.bsc.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.Paging;
import com.nationsky.backstage.core.bsc.CRUD;
import com.nationsky.backstage.core.bsc.IPersistentService;
import com.nationsky.backstage.core.bsc.dao.IDAO;

/**
 * 功能：所有持久化模块可以继承此类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public abstract class BasePersistentServiceImpl implements IPersistentService {
	
	@Autowired
	@Qualifier("system.runtime.IDAO")
	protected IDAO dao;
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	public void setDao(IDAO dao) {
		this.dao = dao;
	}
	
	public IDAO getDao() {
		return dao;
	}

	public void create(PO po) {
		
		if(po.getClass().isAnnotationPresent(CRUD.class)){
			CRUD cdus = po.getClass().getAnnotation(CRUD.class);
			if(cdus.create())
				dao.insert(po);
			else
				throw new UnsupportedOperationException();
		}
	}

	public <T extends PO> T  get(Class<T> poc, Serializable id) {
		
		if(poc.isAnnotationPresent(CRUD.class)){
			CRUD cdus = poc.getAnnotation(CRUD.class);
			if(!cdus.read())throw new UnsupportedOperationException();
		}
		return (T) dao.get(poc,id);
	}
	
	public <T extends PO> T  get(Class<T> poc, Serializable id,boolean lock) {
		
		if(poc.isAnnotationPresent(CRUD.class)){
			CRUD cdus = poc.getAnnotation(CRUD.class);
			if(!cdus.read())throw new UnsupportedOperationException();
		}
		return (T) dao.get(poc,id,lock);
	}

	public void remove(PO po) {
		
		if(po.getClass().isAnnotationPresent(CRUD.class)){
			CRUD cdus = po.getClass().getAnnotation(CRUD.class);
			if(cdus.delete())dao.delete(po);
		}else
			throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see eg.core.bsc.PersistentService#remove(java.lang.Class, java.io.Serializable)
	 */
	public <T extends PO> void remove(Class<T> poc, Serializable ID){
		if(poc.isAnnotationPresent(CRUD.class)){
			CRUD cdus = poc.getAnnotation(CRUD.class);
			if(cdus.delete())
				dao.deleteById(poc, ID);
		}else
			throw new UnsupportedOperationException();
	}

	public int update(PO po) {
		
		if(po.getClass().isAnnotationPresent(CRUD.class)){
			CRUD cdus = po.getClass().getAnnotation(CRUD.class);
			if(cdus.update()){
				dao.update(po);
				return 1;
			}
		}else
			throw new UnsupportedOperationException();
		return 0;
	}
	
	public int getCount(Class<? extends PO> poc, Factor...factors) {
		
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return dao.getCount(poc, factors);
		}else
			throw new UnsupportedOperationException();
	}

	public <T extends PO> T  getUnique(Class<T> poc, Factor... factors) {
			
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return (T) dao.getUnique(poc, factors);
		}else
			throw new UnsupportedOperationException();
	}
	
	public <T extends PO> T  getUnique(Class<T> poc,String sort, Factor... factors) {
			
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return (T) dao.getUnique( poc, sort, factors);
		}else
			throw new UnsupportedOperationException();
	}
	
	public <T extends PO> T  getUnique(Class<T> poc,String sort,int first , Factor... factors) {
			
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return (T) dao.getUnique( poc, sort, first, factors);
		}else
			throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eg.core.bsc.service.PersistentService#isHasMatch(java.lang.Class, eg.core.Factor[])
	 */
	public boolean isExists(Class<? extends PO> poc, Factor... factors) {
		
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return dao.exists(poc, factors);
		}else
			throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see eg.core.bsc.PersistentService#findPropsList(java.lang.Class, java.util.List, int, int, java.lang.String, eg.core.Factor[])
	 */
	public <T extends PO> List<?> findPropsList(Class<T> poc,
			List<String> propNames, int first, int size, String sort,
			Factor... factors) {
			
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return dao.findPropsList(poc,propNames,first,size,sort,factors);
		}else
			throw new UnsupportedOperationException();
	}

	public <T extends PO> List<T> findList(Class<T> poc, int first, int size,
			String sort, Factor... factors) {
		
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			return dao.find(poc, first, size, sort, factors);
		}else
			throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see eg.core.bsc.PersistentService#find(java.lang.Class, int, int, java.lang.String, eg.core.Factor[])
	 * first为负数时候取最后一页
	 */
	public <T extends PO> Paging find(Class<T> poc, int first, int size, String sort, Factor... factors) {
		
		if(poc.isAnnotationPresent(CRUD.class) && poc.getAnnotation(CRUD.class).read()){
			int count = dao.getCount(poc, factors);
			Paging page = null;
			if(count>0 && count>first){
				page = new Paging((List<T>) dao.find(poc, first>=0?first:Paging.getLastPageStartIndex(count, size), size, sort, factors),first>=0?first:Paging.getLastPageStartIndex(count, size),size,count);
			}
			return Paging.getPage(page);
		}else
			throw new UnsupportedOperationException();		
	}

	/* (non-Javadoc)
	 * @see eg.core.bsc.PersistentService#findList(java.lang.String)
	 */
	@Override
	public List<?> findList(String hql, int start,int size) {
		
		return dao.findList(hql,start,size);
	}

	/* (non-Javadoc)
	 * @see com.nsc.meap.core.bsc.IPersistentService#execute(java.lang.String)
	 */
	@Override
	public int execute(String hql) {
		
		return dao.execute(hql);
	}

}
