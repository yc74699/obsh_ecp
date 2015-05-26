package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.FamilyinfoBean;

public class QRY050061Result extends BaseServiceInvocationResult
{
	private int ret_code;

	private List<FamilyinfoBean> familyinfo = new ArrayList<FamilyinfoBean>();

	public void setRet_code(int ret_code)
	{
		this.ret_code = ret_code;
	}

	public int getRet_code()
	{
		return this.ret_code;
	}

	public void setFamilyinfo(List<FamilyinfoBean> familyinfo)
	{
		this.familyinfo = familyinfo;
	}

	public List<FamilyinfoBean> getFamilyinfo()
	{
		return this.familyinfo;
	}

}