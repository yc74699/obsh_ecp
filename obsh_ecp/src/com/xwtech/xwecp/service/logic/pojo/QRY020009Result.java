package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.FamilyNumber;
import com.xwtech.xwecp.service.logic.pojo.ProPackAgeInfo;

public class QRY020009Result extends BaseServiceInvocationResult
{
	private List<FamilyNumber> familyNumber = new ArrayList<FamilyNumber>();

	private ProPackAgeInfo proPackAgeInfo;

	public void setFamilyNumber(List<FamilyNumber> familyNumber)
	{
		this.familyNumber = familyNumber;
	}

	public List<FamilyNumber> getFamilyNumber()
	{
		return this.familyNumber;
	}

	public void setProPackAgeInfo(ProPackAgeInfo proPackAgeInfo)
	{
		this.proPackAgeInfo = proPackAgeInfo;
	}

	public ProPackAgeInfo getProPackAgeInfo()
	{
		return this.proPackAgeInfo;
	}

}