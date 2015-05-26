package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 新生随机码查询学校接口
 * @author YangXQ
 * 2015-04-14
 */
public class QRY610040Result extends BaseServiceInvocationResult implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SCHOOLCODE ="";
	private String SCHOOLNAME ="";
	public String getSCHOOLCODE() {
		return SCHOOLCODE;
	}
	public void setSCHOOLCODE(String sCHOOLCODE) {
		SCHOOLCODE = sCHOOLCODE;
	}
	public String getSCHOOLNAME() {
		return SCHOOLNAME;
	}
	public void setSCHOOLNAME(String sCHOOLNAME) {
		SCHOOLNAME = sCHOOLNAME;
	}
}