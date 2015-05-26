package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL010021Result extends BaseServiceInvocationResult
{
	private String phoneNum;

	private String pkgProdId;

	private String prodId;

	private String oprType;

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public String getPhoneNum()
	{
		return this.phoneNum;
	}

	public void setPkgProdId(String pkgProdId)
	{
		this.pkgProdId = pkgProdId;
	}

	public String getPkgProdId()
	{
		return this.pkgProdId;
	}

	public void setProdId(String prodId)
	{
		this.prodId = prodId;
	}

	public String getProdId()
	{
		return this.prodId;
	}

	public void setOprType(String oprType)
	{
		this.oprType = oprType;
	}

	public String getOprType()
	{
		return this.oprType;
	}

}