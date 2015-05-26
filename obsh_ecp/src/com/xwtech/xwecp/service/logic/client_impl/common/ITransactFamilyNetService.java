package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.FamilyShortInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL080002Result;

public interface ITransactFamilyNetService
{
	public DEL080002Result transactFamilyNet(String operateType, String availableDate, String homeMemberUserId, String productId, List<FamilyShortInfo> familyShorts) throws LIException;

}