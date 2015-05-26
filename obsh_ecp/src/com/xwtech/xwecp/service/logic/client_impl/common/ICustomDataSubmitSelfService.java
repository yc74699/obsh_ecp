package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.pojo.DEL040109Result;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;

public interface ICustomDataSubmitSelfService {
	
	public DEL040109Result customDataSubmitSelf(String phoneNum, String custName, String city, String country, String icNo, String icAddr, String postNo, String postAddr, String phoneCall, String siteId, String siteName, String siteAddr, String fetchType, String totalFee, String emsNo, String proId, String brandId, String payType, List<ProPackage> proPackages, List<ProService> proServices, List<ProIncrement> proIncrements, List<ProSelf> proSelfs, String marketId, String marketLevelId, String busiPackId,String busiGoodsId, String marketfee,String recId,String bookingId,
			String IMSI,String REGION) throws LIException;
}
