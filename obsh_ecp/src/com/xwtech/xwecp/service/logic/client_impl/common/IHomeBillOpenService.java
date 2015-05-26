package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.Memberdt;
import com.xwtech.xwecp.service.logic.pojo.DEL040026Result;

public interface IHomeBillOpenService
{
	public DEL040026Result openHomeBill(String phoneNum, String inuremode, String familytype, String isOld, List<Memberdt> memberList) throws LIException;

}