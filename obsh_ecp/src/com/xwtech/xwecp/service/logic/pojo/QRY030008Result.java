package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ExchangeType;
import com.xwtech.xwecp.service.logic.pojo.PackageType;
import com.xwtech.xwecp.service.logic.pojo.ExchangeBizInfo;

public class QRY030008Result extends BaseServiceInvocationResult
{
	private List<ExchangeType> exchangeTypes = new ArrayList<ExchangeType>();

	private List<PackageType> packageTypes = new ArrayList<PackageType>();

	private List<ExchangeBizInfo> exchangeBizInfos = new ArrayList<ExchangeBizInfo>();

	public void setExchangeTypes(List<ExchangeType> exchangeTypes)
	{
		this.exchangeTypes = exchangeTypes;
	}

	public List<ExchangeType> getExchangeTypes()
	{
		return this.exchangeTypes;
	}

	public void setPackageTypes(List<PackageType> packageTypes)
	{
		this.packageTypes = packageTypes;
	}

	public List<PackageType> getPackageTypes()
	{
		return this.packageTypes;
	}

	public void setExchangeBizInfos(List<ExchangeBizInfo> exchangeBizInfos)
	{
		this.exchangeBizInfos = exchangeBizInfos;
	}

	public List<ExchangeBizInfo> getExchangeBizInfos()
	{
		return this.exchangeBizInfos;
	}

}