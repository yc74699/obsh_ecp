package com.xwtech.xwecp.service;


import java.io.Serializable;

import org.apache.log4j.Logger;

//import com.xwtech.xwecp.service.logic.pojo.EcpLogger;

public class BaseServiceInvocationResult implements Serializable
{
	private static final Logger logger = Logger.getLogger(BaseServiceInvocationResult.class);
	
	protected String resultCode;
	
	protected String errorCode;
	
	protected String errorMessage;
	
	protected String bossCode;
	
	protected String accessId;
	
//	protected EcpLogger ecpLogger;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getResultCode()
	{
		return resultCode;
	}

	public void setResultCode(String resultCode)
	{
		this.resultCode = resultCode;
	}

	public String getBossCode() {
		return bossCode;
	}

	public void setBossCode(String bossCode) {
		this.bossCode = bossCode;
	}
	
//	public EcpLogger getEcpLogger() {
//		return ecpLogger;
//	}
//
//	public void setEcpLogger(EcpLogger ecpLogger) {
//		this.ecpLogger = ecpLogger;
//	}
}
