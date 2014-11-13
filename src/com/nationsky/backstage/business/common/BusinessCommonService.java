package com.nationsky.backstage.business.common;

import com.nationsky.backstage.ServiceLocator;
import com.nationsky.backstage.common.bsc.ICommonService;

public class BusinessCommonService {
	public static ICommonService commonService = ServiceLocator.getService(ICommonService.class);
}

