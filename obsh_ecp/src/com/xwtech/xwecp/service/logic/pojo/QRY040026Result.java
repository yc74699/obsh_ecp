package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ContractItem;

public class QRY040026Result extends BaseServiceInvocationResult
{
	private List<ContractItem> contractItemList = new ArrayList<ContractItem>();

	public void setContractItemList(List<ContractItem> contractItemList)
	{
		this.contractItemList = contractItemList;
	}

	public List<ContractItem> getContractItemList()
	{
		return this.contractItemList;
	}

}