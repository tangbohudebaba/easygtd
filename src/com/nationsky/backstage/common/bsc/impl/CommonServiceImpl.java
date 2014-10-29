/**
 * 
 */
package com.nationsky.backstage.common.bsc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nationsky.backstage.common.bsc.ICommonService;
import com.nationsky.backstage.common.bsc.dao.SequenceDAO;
import com.nationsky.backstage.core.bsc.impl.BasePersistentServiceImpl;

/**
 * 功能：
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
@Service
public class CommonServiceImpl extends BasePersistentServiceImpl implements
		ICommonService {

	@Autowired
	private SequenceDAO sequenceDao;
	/* (non-Javadoc)
	 * @see com.nsc.meap.common.bsc.ICommonService#genNextLongPK(java.lang.String)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public synchronized Long genNextLongPK(String key) {
		return sequenceDao.genNextLongPK(key);
	}
	
}
