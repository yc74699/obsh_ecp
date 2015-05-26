package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 查询渠道网点信息
 * @author YangXQ
 * 2015-03-17
 */
public class QRY040101Result extends BaseServiceInvocationResult implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ret_code;
	private String orgname;
	private String address;
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String retCode) {
		ret_code = retCode;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}