package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 红包信息
 * 
 * @author xufan
 * 2014-03-27
 * 
 */
public class QRY010031Result extends BaseServiceInvocationResult
{

	private String retCode;

	private String msisdn;

	private String userid;
	

	
	private List<RedPack> listRedPack =new ArrayList<RedPack>();


	public String getRetCode() {
		return retCode;
	}


	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}


	public String getMsisdn() {
		return msisdn;
	}


	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public List<RedPack> getListRedPack() {
		return listRedPack;
	}


	public void setListRedPack(List<RedPack> listRedPack) {
		this.listRedPack = listRedPack;
	}
	
	}