package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.DEL011002Result;

public interface ICustomDataSubmitService
{
	public DEL011002Result customDataSubmit(String phoneNum, String custName, String city, String country, String icNo, String icAddr, String postNo, String postAddr, String phoneCall, String siteId, String siteName, String siteAddr, String fetchType, String totalFee, String emsNo, String proId, String brandId, String payType, List<ProPackage> proPackages, List<ProService> proServices, List<ProIncrement> proIncrements, List<ProSelf> proSelfs, String marketId, String marketLevelId, String busiPackId,String busiGoodsId, String marketfee,String recId,String bookingId) throws LIException;

}