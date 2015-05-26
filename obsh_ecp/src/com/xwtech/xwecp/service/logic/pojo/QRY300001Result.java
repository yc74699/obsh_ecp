package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.BankRelatInfo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY300001Result extends BaseServiceInvocationResult
{
	
	private String contractcount;
	
	
	private List<BankRelatInfo> bankRelatInfo = new ArrayList<BankRelatInfo>();

	

	public String getContractcount() {
		return contractcount;
	}

	public void setContractcount(String contractcount) {
		this.contractcount = contractcount;
	}

	public List<BankRelatInfo> getBankRelatInfo() {
		return bankRelatInfo;
	}

	public void setBankRelatInfo(List<BankRelatInfo> bankRelatInfo) {
		this.bankRelatInfo = bankRelatInfo;
	}

	

}