package com.xwtech.xwecp.service.logic.pojo;


public class ProResource
{
	private String proId;

	private String proName;

	private String proDesc;

	private String brandId;

	private String brandName;

	private int payMode;

	public void setProId(String proId)
	{
		this.proId = proId;
	}

	public String getProId()
	{
		return this.proId;
	}

	public void setProName(String proName)
	{
		this.proName = proName;
	}

	public String getProName()
	{
		return this.proName;
	}

	public void setProDesc(String proDesc)
	{
		this.proDesc = proDesc;
	}

	public String getProDesc()
	{
		return this.proDesc;
	}

	public void setBrandId(String brandId)
	{
		this.brandId = brandId;
	}

	public String getBrandId()
	{
		return this.brandId;
	}

	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}

	public String getBrandName()
	{
		return this.brandName;
	}

	public void setPayMode(int payMode)
	{
		this.payMode = payMode;
	}

	public int getPayMode()
	{
		return this.payMode;
	}

}