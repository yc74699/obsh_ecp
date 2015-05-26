package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 主号查询V网产品信息
 * @author Administrator
 *
 */
public class QRY610003Result extends BaseServiceInvocationResult implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	List<Product_Info> productInfo = new ArrayList<Product_Info>();

	public List<Product_Info> getProductInfo()
	{
		return productInfo;
	}

	public void setProductInfo(List<Product_Info> productInfo)
	{
		this.productInfo = productInfo;
	}

	
}
