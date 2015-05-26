package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.SimMarketInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL110004Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface ITpSimOpenPrdService
{
	public DEL110004Result doTpSimopenPrd(String msisdn, String imsi, String custName, String custAddr, String city, String country, String icNo, String icAddr, String totalFee, String proId, String brandId, String payType, List<ProPackage> proPackages, List<ProService> proServices, List<ProIncrement> proIncrements, List<ProSelf> proSelfs, List<SimMarketInfo> simMarketInfo) throws LIException;

}