package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ViceNumberDetail;

public class QRY050014Result extends BaseServiceInvocationResult
{
	private List<ViceNumberDetail> viceNumberDetail = new ArrayList<ViceNumberDetail>();

	public void setViceNumberDetail(List<ViceNumberDetail> viceNumberDetail)
	{
		this.viceNumberDetail = viceNumberDetail;
	}

	public List<ViceNumberDetail> getViceNumberDetail()
	{
		return this.viceNumberDetail;
	}

}