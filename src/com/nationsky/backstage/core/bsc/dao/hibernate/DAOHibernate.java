package com.nationsky.backstage.core.bsc.dao.hibernate;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.util.ValidateUtil;

/**
 * 功能：DAO实现的基础类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DAOHibernate {

	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate){
		this.hibernateTemplate = hibernateTemplate;
	}
	
	protected String getTableName(Class<? extends PO> poc){
		return poc.getSimpleName();  
	}

	protected  Criterion addFactor(Criterion criterion , Factor factor){
		if(factor.getValue()==null)return criterion;
		//if(f.getValue() instanceof Number && StringUtil.getInt(f.getValue())<0)return criterion;
		switch(factor.getC()){
			case Or:{
				return Restrictions.or(this.addFactor(criterion , ((Factor[])factor.getValue())[0]), this.addFactor(criterion , ((Factor[])factor.getValue())[1]));
			}
			case And:{
				return Restrictions.and(this.addFactor(criterion , ((Factor[])factor.getValue())[0]), this.addFactor(criterion , ((Factor[])factor.getValue())[1]));
			}
			case Between:{
				return Restrictions.between(factor.getProperty(),((Object[])factor.getValue())[0],((Object[])factor.getValue())[1]);
			}
			case In:{
				return Restrictions.in(factor.getProperty(), (Object[])factor.getValue());
			}
			case Eq:{
				return Restrictions.eq(factor.getProperty(), factor.getValue());
			}
			case Ge:{
				return Restrictions.ge(factor.getProperty(), factor.getValue());
			}
			case Gt:{
				return Restrictions.gt(factor.getProperty(), factor.getValue());
			}
			case Le:{
				return Restrictions.le(factor.getProperty(), factor.getValue());
			}
			case Lt:{
				return Restrictions.lt(factor.getProperty(), factor.getValue());
			}
			case Ne:{
				return Restrictions.ne(factor.getProperty(), factor.getValue());
			}
			case Like:{
				return Restrictions.like(factor.getProperty(), factor.getValue());
			}
			case Sql:{
				return Restrictions.sqlRestriction(factor.getValue().toString());
			}
			case Null:{
				return Restrictions.isNull(factor.getProperty());
			}
			case NotNull:{
				return Restrictions.isNotNull(factor.getProperty());
			}
			default:{
				throw new java.lang.UnsupportedOperationException();
			}
		}
	}

	protected Criteria addSort(Criteria criteria , String sort){
		if(criteria == null)throw new IllegalArgumentException();
		if(ValidateUtil.isNotNull(sort)){
			Map<String,String> sorts = this.parseSort(sort);
			for(Iterator<String> it = sorts.keySet().iterator();it.hasNext();){
				String key = it.next().toString();
				criteria.addOrder("DESC".equalsIgnoreCase((String)sorts.get(key))?Order.desc(key):Order.asc(key));
			}
		}
		return criteria;
	}

	protected StringBuilder addSort(StringBuilder sb,String sort){
		if(sb==null)throw new IllegalArgumentException();
		if(ValidateUtil.isNotNull(sort)){
			Map<String,String> sorts = this.parseSort(sort);
			Iterator<String> it = sorts.keySet().iterator();
			for(int i=0;it.hasNext();i++){
				String key = it.next();
				if(i<1)sb.append(" order by ");
				else sb.append(" , ");
				sb.append(key).append(" ").append(sorts.get(key));
			}
		}
		return sb;
	}
	
	private Map<String,String> parseSort(String sort){
		if(ValidateUtil.isNull(sort))return Collections.emptyMap();
		Map<String,String> map = new LinkedHashMap<String,String>();
		try{
			StringTokenizer st = new StringTokenizer(sort,";");
			for(;st.hasMoreTokens();){
				StringTokenizer st1 = new StringTokenizer(st.nextToken(),":");
				map.put(st1.nextToken(),st1.nextToken());
			}
		}catch(Exception e){
			return Collections.emptyMap();
		}
		return map;
	}
}
