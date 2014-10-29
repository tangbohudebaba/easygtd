package com.nationsky.backstage.core.bsc.dao;

import java.io.Serializable;
import java.util.List;

import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.PO;

/**
 * 功能：通用的DAO处理接口
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface IDAO {
	/**
	 * 插入实体
	 * @param po
	 */
	void insert(PO po);
	/**
	 * 删除实体
	 * @param po
	 */
	int delete(PO po);
	/**
	 * 通过主键删除实体
	 * @param poc
	 * @param id
	 */
	int deleteById(Class<? extends PO> poc,Serializable id);
	/**
	 * 删除实体对象
	 * @param pos
	 */
	int delete(List<? extends PO> pos);
	
	/**
	 * 清空整张表
	 * @param poc
	 */
	void deleteAll(Class<? extends PO> poc);
	/**
	 * 更新实体
	 * @param po
	 */
	int update(PO po);
	/**
	 * 插入或者更新实体
	 * @param po
	 */
	int insertOrUpdate(PO po);
	/**
	 * 获得指定实体对象
	 * @param <T>
	 * @param poc
	 * @param id
	 * @return
	 */
	<T extends PO> T get(Class<T> poc,Serializable id);
	/**
	 * 是否加锁获得实体对象
	 * @param <T>
	 * @param poc
	 * @param id
	 * @param lock
	 * @return
	 */
	<T extends PO> T get(Class<T> poc,Serializable id,boolean lock);
	/**
	 * 通过条件获得单一实体
	 * @param poc
	 * @param factors
	 * @return
	 */
	<T extends PO> T getUnique(Class<T> poc,Factor...factors);
	/**
	 * 通过条件获得单一实体,列表中的第一个
	 * @param poc
	 * @param factors
	 * @return
	 */
	<T extends PO> T getUnique(Class<T> poc,String sort,Factor...factors);
	/**
	 * 通过条件获得单一实体,列表中的第一个
	 * @param <T>
	 * @param poc
	 * @param sort
	 * @param first
	 * @param factors
	 * @return
	 */
	<T extends PO> T getUnique(Class<T> poc,String sort,int first,Factor...factors);
	/**
	 * 查看某个实体对象是否存在
	 * @param poc
	 * @param factors
	 * @return
	 */
	boolean exists(Class<? extends PO> poc,Factor...factors);
	/**
	 * 通过Example获得实体对象
	 * @param po
	 * @return
	 */
	<T extends PO> List<T> findByExample(T po);
	/**
	 * 查询所有通过sort排序
	 * @param poc
	 * @param sort
	 * @return
	 */
	<T extends PO> List<T> findAll(Class<T> poc,String sort);
	/**
	 * 通过条件获得实体条数
	 * @param poc
	 * @param factors
	 * @return
	 */
	<T extends PO> int getCount(Class<T> poc,Factor...factors);
	/**
	 * 搜索指定类的某些属性
	 * @param <T>
	 * @param poc
	 * @param propNames
	 * @param first
	 * @param size
	 * @param sort
	 * @param factors
	 * @return
	 */
	<T extends PO> List<?> findPropsList(Class<T> poc,List<String> propNames,int first,int size,String sort,Factor...factors);
	/**
	 * 通过条件获得实体列表
	 * @param poc
	 * @param first
	 * @param size
	 * @param sort
	 * @param factors
	 * @return
	 */
	<T extends PO> List<T> find(Class<T> poc,int first,int size,String sort,Factor...factors);
	/**
	 * 通过hql语句获得实体条数
	 * @param hql
	 * @return
	 */
	int countQuery(String hql);
	/**
	 * 通过hql语句获得实体列表
	 * @param hql
	 * @param first
	 * @param size
	 * @param sort
	 * @return
	 */
	List<PO> query(String hql,int first,int size,String sort);
	/**
	 * 执行指定hql更新操作
	 * @param hql
	 * @return
	 */
	int execute(String hql);
	/**
	 * 执行hql搜索
	 * @param hql
	 * @return
	 */
	List<?> findList(String hql,int start,int size);
}
