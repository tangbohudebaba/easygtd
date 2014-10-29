package com.nationsky.backstage.core.bsc;

import java.io.Serializable;
import java.util.List;

import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.Paging;

/**
 * 具有持久化模块的对象操作通用服务接口
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public interface IPersistentService extends Service{
	/**
	 * 增加PO实体数据
	 * @param po
	 */
	void create(PO po);
	/**
	 * 删除PO实体数据,这里面是对象,必须保证对象为实体
	 * @param po
	 */
	void remove(PO po);
	/**
	 * 删除PO实体数据
	 * @param <T>
	 * @param poc
	 * @param ID
	 */
	<T extends PO> void remove(Class<T> poc, Serializable ID);
	/**
	 * 更新PO实体数据
	 * @param po
	 */
	int update(PO po);
	/**
	 * 通过主键获得实体数据
	 * @param po
	 * @param id
	 * @return
	 */
	<T extends PO> T get(Class<T> poc,Serializable id);
	/**
	 * 通过主键获得实体数据,是否锁定
	 * @param <T>
	 * @param poc
	 * @param id
	 * @param lock
	 * @return
	 */
	<T extends PO> T get(Class<T> poc,Serializable id,boolean lock);
	/**
	 * 获得单一对象实例
	 * @param poc
	 * @param factors
	 * @return
	 */
	<T extends PO> T getUnique(Class<T> poc,Factor...factors);
	/**
	 * 获得单一对象,按排序中的第一个
	 * @param <T>
	 * @param poc
	 * @param sort
	 * @param factors
	 * @return
	 */
	<T extends PO> T getUnique(Class<T> poc,String sort,Factor...factors);
	/**
	 * 获得单一对象,按排序中的第一个,指定开始值
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
	boolean isExists(Class<? extends PO> poc,Factor...factors);
	/**
	 * 获得对象条数
	 * @param poc
	 * @param factors
	 * @return
	 */
	int getCount(Class<? extends PO> poc,Factor...factors);
	
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
	 * 搜索列表通用方法
	 * @param <T>
	 * @param poc
	 * @param first
	 * @param size
	 * @param sort
	 * @param factors
	 * @return
	 */
	<T extends PO> List<T> findList(Class<T> poc,int first,int size,String sort,Factor...factors);
	/**
	 * 搜索通用分页方法
	 * @param <T>
	 * @param poc
	 * @param first
	 * @param size
	 * @param sort
	 * @param factors
	 * @return
	 */
	<T extends PO> Paging find(Class<T> poc,int first,int size,String sort, Factor...factors);
	/**
	 * 执行hql搜索
	 * @param hql
	 * @return
	 */
	List<?> findList(String hql, int start, int size);
	
	/**
	 * 调用动态的hql
	 * @param hql
	 * @return
	 */
	int execute(String hql);
}
