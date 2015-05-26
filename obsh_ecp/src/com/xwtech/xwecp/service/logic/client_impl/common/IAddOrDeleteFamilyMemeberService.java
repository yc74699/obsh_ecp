package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040027Result;

public interface IAddOrDeleteFamilyMemeberService
{
	public DEL040027Result addOrDeleteFamilyMemeber(int ddr_city, int opTypeNum, int effect_way, String familyVirtualId, String customer_id, String memeberType, String memberPhoneNum, String memberName, int hostFlag, String homeId) throws LIException;

}