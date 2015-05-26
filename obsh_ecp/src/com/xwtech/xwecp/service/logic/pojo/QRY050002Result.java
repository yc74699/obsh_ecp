package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.Monternet;
import com.xwtech.xwecp.service.logic.pojo.SelfBusiness;

public class QRY050002Result extends BaseServiceInvocationResult
{
	private List<Monternet> monternet = new ArrayList<Monternet>();

	private List<SelfBusiness> selfBusiness = new ArrayList<SelfBusiness>();

	public void setMonternet(List<Monternet> monternet)
	{
		this.monternet = monternet;
	}

	public List<Monternet> getMonternet()
	{
		return this.monternet;
	}

	public void setSelfBusiness(List<SelfBusiness> selfBusiness)
	{
		this.selfBusiness = selfBusiness;
	}

	public List<SelfBusiness> getSelfBusiness()
	{
		return this.selfBusiness;
	}

}