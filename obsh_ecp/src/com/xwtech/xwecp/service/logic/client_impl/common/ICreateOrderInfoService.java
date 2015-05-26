package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040106Result;

public interface ICreateOrderInfoService {
	
	public DEL040106Result createOrderInfo(String phoneNum, int paytype, String payfee, int busitype, String operid, 
			String marketplanid, String goodspackid, String busipackid, String drawflag,String scoreclass, String smsflag, 
			String rewardlist, String remark) throws LIException;
}
