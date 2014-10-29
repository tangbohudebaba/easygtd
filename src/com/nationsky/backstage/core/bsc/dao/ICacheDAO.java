/**
 * 
 */
package com.nationsky.backstage.core.bsc.dao;

import java.io.Serializable;
import java.util.List;

import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.PO;

/**
 * 功能：处理缓存的DAO访问方法
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface ICacheDAO<T extends PO, ID extends Serializable>{

	/**
	 * 插入实体
	 * @param po
	 */
	void insert(PO po);
	/**
	 * 删除实体
	 * @param po
	 */
	void delete(PO po);
	/**
	 * 通过主键删除实体
	 * @param poc
	 * @param id
	 */
	void deleteById(ID id);
	/**
	 * 在缓存中删除
	 * @param poc
	 * @param id
	 */
	void removeFromCache(ID id);
	/**
	 * 删除List缓存
	 * @param po
	 */
	void removeListCache(PO po);
	/**
	 * 获得指定实体对象
	 * @param <T>
	 * @param poc
	 * @param id
	 * @return
	 */
	T get(ID id);
	/**
	 * 清空缓存实体
	 */
	void clearAllCache();
	/**
	 * 更新一个数据库对象。如修改一个用户昵称时，不会影响任何排序，那么就不需要清除列表缓存。
	 * @param record 要更新的对象
	 * @param clearListCache true表示需要清除列表缓存  false表示不需要
	 * @return boolean
	 */
	void update(PO po,boolean clearListCache);
	/**
	 * 通过条件获得单一实体
	 * @param poc
	 * @param factors
	 * @return
	 */
	T getUnique(Factor...factors);
	/**
	 * 通过条件获得实体条数
	 * @param poc
	 * @param factors
	 * @return
	 */
	int getCount(Factor...factors);
	/**
	 * 通过条件获得实体列表
	 * @param poc
	 * @param first
	 * @param size
	 * @param sort
	 * @param factors
	 * @return
	 */
	List<T> find(int first,int size,String sort,Factor...factors);
	/**
	 * 通用在数据字典所有加载中
	 * @return
	 */
	List<T> findAll();
	
}
