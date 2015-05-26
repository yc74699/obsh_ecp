package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040037Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface ICountryFamilyMemManageService
{
	public DEL040037Result countryFamilyMemManage(int idType, String oid, String homeUserId, String servNumber, int isHouseHold, String notes, String operId, String createTime, String houseHolderName, String houseHolderPhs, String houseHolderVocation, String houseHolderAddress) throws LIException;

}