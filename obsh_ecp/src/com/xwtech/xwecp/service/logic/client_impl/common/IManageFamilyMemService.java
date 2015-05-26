package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.NewMemberInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY200002Result;

public interface IManageFamilyMemService
{
	public QRY200002Result manageFamilyMemService(String familysubsid, String oprtsrc, String oprType, List<NewMemberInfo> newMemberInfo) throws LIException;

}