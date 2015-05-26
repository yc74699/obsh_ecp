package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.FamilyMember;
import com.xwtech.xwecp.service.logic.pojo.QRY020012Result;

public interface IQueryFamilyPackageService
{
	public QRY020012Result queryFamilyPackage(String phoneNum, String extId, List<FamilyMember> memberMobileNum) throws LIException;

}