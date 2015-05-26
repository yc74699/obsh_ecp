package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;

public class QRY070007Result extends BaseServiceInvocationResult
{
	private List<GoodsPkgInfo> goodsPkg = new ArrayList<GoodsPkgInfo>();

	public void setGoodsPkg(List<GoodsPkgInfo> goodsPkg)
	{
		this.goodsPkg = goodsPkg;
	}

	public List<GoodsPkgInfo> getGoodsPkg()
	{
		return this.goodsPkg;
	}

}