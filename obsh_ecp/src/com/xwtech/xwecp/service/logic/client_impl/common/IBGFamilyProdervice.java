package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.NNMemberInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL200004Result;

public interface IBGFamilyProdervice
{
	public DEL200004Result bgFamilyProdService(String familysubsid, String increprodid, String effecttype, List<NNMemberInfo> nNMemberInfo) throws LIException;

}