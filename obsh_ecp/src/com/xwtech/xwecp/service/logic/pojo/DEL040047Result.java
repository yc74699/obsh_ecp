package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ProdInfo;

public class DEL040047Result extends BaseServiceInvocationResult
{
	private List<ProdInfo> prodInfos = new ArrayList<ProdInfo>();

	public void setProdInfos(List<ProdInfo> prodInfos)
	{
		this.prodInfos = prodInfos;
	}

	public List<ProdInfo> getProdInfos()
	{
		return this.prodInfos;
	}

}