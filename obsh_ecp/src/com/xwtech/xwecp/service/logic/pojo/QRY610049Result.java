package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 流量账户余额明细查询
 * @author YangXQ
 * 2015-04-22
 */
public class QRY610049Result extends BaseServiceInvocationResult implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String  ret_code;
    private String  operating_srl;
    private List<Balance> balance_list;
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String retCode) {
		ret_code = retCode;
	}
	public String getOperating_srl() {
		return operating_srl;
	}
	public void setOperating_srl(String operatingSrl) {
		operating_srl = operatingSrl;
	}
	public List<Balance> getBalance_list() {
		return balance_list;
	}
	public void setBalance_list(List<Balance> balanceList) {
		balance_list = balanceList;
	}
	
    
    
}