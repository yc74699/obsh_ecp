package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.FamilyMemInfoBean;

public class QRY050060Result extends BaseServiceInvocationResult
{
	private int retCode;

	private List<FamilyMemInfoBean> familyMemInfo = new ArrayList<FamilyMemInfoBean>();

	public void setRetCode(int retCode)
	{
		this.retCode = retCode;
	}

	public int getRetCode()
	{
		return this.retCode;
	}

	public void setFamilyMemInfo(List<FamilyMemInfoBean> familyMemInfo)
	{
		this.familyMemInfo = familyMemInfo;
	}

	public List<FamilyMemInfoBean> getFamilyMemInfo()
	{
		return this.familyMemInfo;
	}

}