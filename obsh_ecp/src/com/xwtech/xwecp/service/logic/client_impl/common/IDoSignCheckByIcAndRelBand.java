package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040063Result;

public interface IDoSignCheckByIcAndRelBand {
	public DEL040063Result doSignCheckByIcAndRelBand(String phoneNum,String certType,String certId,String custName,String vtelNum,String recType) throws LIException;

}
