package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.VnetGroupProductInfo;

public class DEL010016Result extends BaseServiceInvocationResult
{
	private String resultCode;

	private List<VnetGroupProductInfo> vnetGroupProductInfo = new ArrayList<VnetGroupProductInfo>();

	public void setResultCode(String resultCode)
	{
		this.resultCode = resultCode;
	}

	public String getResultCode()
	{
		return this.resultCode;
	}

	public void setVnetGroupProductInfo(List<VnetGroupProductInfo> vnetGroupProductInfo)
	{
		this.vnetGroupProductInfo = vnetGroupProductInfo;
	}

	public List<VnetGroupProductInfo> getVnetGroupProductInfo()
	{
		return this.vnetGroupProductInfo;
	}

}