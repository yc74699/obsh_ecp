package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.FamilyPay;

public class QRY050059Result extends BaseServiceInvocationResult
{
	private String familytype = "";

	private List<FamilyPay> familylist = new ArrayList<FamilyPay>();

	public void setFamilytype(String familytype)
	{
		this.familytype = familytype;
	}

	public String getFamilytype()
	{
		return this.familytype;
	}

	public void setFamilylist(List<FamilyPay> familylist)
	{
		this.familylist = familylist;
	}

	public List<FamilyPay> getFamilylist()
	{
		return this.familylist;
	}

}