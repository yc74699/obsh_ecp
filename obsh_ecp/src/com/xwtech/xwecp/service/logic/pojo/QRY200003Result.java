package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ProductInfo;

public class QRY200003Result extends BaseServiceInvocationResult
{
	private List<ProductInfo> productInfo = new ArrayList<ProductInfo>();

	public void setProductInfo(List<ProductInfo> productInfo)
	{
		this.productInfo = productInfo;
	}

	public List<ProductInfo> getProductInfo()
	{
		return this.productInfo;
	}

}