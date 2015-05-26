package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.EPkgDetail;

public class QRY040014Result extends BaseServiceInvocationResult
{
	private List<EPkgDetail> ePkgDetail = new ArrayList<EPkgDetail>();

	public void setEPkgDetail(List<EPkgDetail> ePkgDetail)
	{
		this.ePkgDetail = ePkgDetail;
	}

	public List<EPkgDetail> getEPkgDetail()
	{
		return this.ePkgDetail;
	}

}