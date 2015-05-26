package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL080003Result;
import com.xwtech.xwecp.service.logic.pojo.ChgProdInfo;

public interface IChgFamilyMainProdService {
	public DEL080003Result chgFamilyMainProd(String phoneNum, String oldMainProd, String newMainProd, List<ChgProdInfo> chgProdInfos) throws LIException;
}
