package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040011Result;

public interface ISetMobileCfgService
{
	public DEL040011Result setMobileCfg(String type, String phoneNum, String configType, String emiId, String brandId, String typeId) throws LIException;

}