package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
 

/**
 * 流量账户余额明细查询
 * @author YangXQ
 * 2015-04-22
 */
public class Balance 
{
    public String getAccout_type() {
		return accout_type;
	}
	public void setAccout_type(String accoutType) {
		accout_type = accoutType;
	}
	public String getAcctbk_id() {
		return acctbk_id;
	}
	public void setAcctbk_id(String acctbkId) {
		acctbk_id = acctbkId;
	}
	public String getFlow_balance() {
		return flow_balance;
	}
	public void setFlow_balance(String flowBalance) {
		flow_balance = flowBalance;
	}
	public String getInvalid_date() {
		return invalid_date;
	}
	public void setInvalid_date(String invalidDate) {
		invalid_date = invalidDate;
	}
	private String  accout_type;
    private String  acctbk_id;
    private String  flow_balance;
    private String  invalid_date;
	
}