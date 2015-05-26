package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.FeeDetail;

public class QRY010042Result extends BaseServiceInvocationResult
{
	/**
	 * 电子渠道历史记录查询实体类
	 * @param args
	 * @throws Exception
	 */
	private List<PayHistory> payHistory = new ArrayList<PayHistory>();

	public void setPayHistory(List<PayHistory> payHistory)
	{
		this.payHistory = payHistory;
	}

	public List<PayHistory> getPayHistory()
	{
		return this.payHistory;
	}

}