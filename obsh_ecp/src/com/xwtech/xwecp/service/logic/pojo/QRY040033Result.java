package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.OperLogDetail;

public class QRY040033Result extends BaseServiceInvocationResult
{
	private List<OperLogDetail> operLogDetail = new ArrayList<OperLogDetail>();

	public void setOperLogDetail(List<OperLogDetail> operLogDetail)
	{
		this.operLogDetail = operLogDetail;
	}

	public List<OperLogDetail> getOperLogDetail()
	{
		return this.operLogDetail;
	}

}