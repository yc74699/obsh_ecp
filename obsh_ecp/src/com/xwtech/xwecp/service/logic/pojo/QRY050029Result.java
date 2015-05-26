package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.RelationPkg;
import com.xwtech.xwecp.service.logic.pojo.Effect;

public class QRY050029Result extends BaseServiceInvocationResult
{
	private List<RelationPkg> relationPkgList = new ArrayList<RelationPkg>();

	private List<Effect> effectList = new ArrayList<Effect>();

	private String isExchange = "";

	private String operType = "open";

	public void setRelationPkgList(List<RelationPkg> relationPkgList)
	{
		this.relationPkgList = relationPkgList;
	}

	public List<RelationPkg> getRelationPkgList()
	{
		return this.relationPkgList;
	}

	public void setEffectList(List<Effect> effectList)
	{
		this.effectList = effectList;
	}

	public List<Effect> getEffectList()
	{
		return this.effectList;
	}

	public void setIsExchange(String isExchange)
	{
		this.isExchange = isExchange;
	}

	public String getIsExchange()
	{
		return this.isExchange;
	}

	public void setOperType(String operType)
	{
		this.operType = operType;
	}

	public String getOperType()
	{
		return this.operType;
	}

}