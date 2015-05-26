package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserTransactionContract;
import com.xwtech.xwecp.service.logic.pojo.EsmsInfo;

public class QRY050039Result extends BaseServiceInvocationResult
{
	private List<UserTransactionContract> userTranConList = new ArrayList<UserTransactionContract>();

	private List<EsmsInfo> esmsInfoList = new ArrayList<EsmsInfo>();

	public void setUserTranConList(List<UserTransactionContract> userTranConList)
	{
		this.userTranConList = userTranConList;
	}

	public List<UserTransactionContract> getUserTranConList()
	{
		return this.userTranConList;
	}

	public void setEsmsInfoList(List<EsmsInfo> esmsInfoList)
	{
		this.esmsInfoList = esmsInfoList;
	}

	public List<EsmsInfo> getEsmsInfoList()
	{
		return this.esmsInfoList;
	}

}