package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY050049Result extends BaseServiceInvocationResult
{
	private String retCode = "";

	private String retMsg = "";

	private String productId = "";

	private String productName = "";

	private String productDes = "";

	public void setRetCode(String retCode)
	{
		this.retCode = retCode;
	}

	public String getRetCode()
	{
		return this.retCode;
	}

	public void setRetMsg(String retMsg)
	{
		this.retMsg = retMsg;
	}

	public String getRetMsg()
	{
		return this.retMsg;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getProductId()
	{
		return this.productId;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getProductName()
	{
		return this.productName;
	}

	public void setProductDes(String productDes)
	{
		this.productDes = productDes;
	}

	public String getProductDes()
	{
		return this.productDes;
	}

}