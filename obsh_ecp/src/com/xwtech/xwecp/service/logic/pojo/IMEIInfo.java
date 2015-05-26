package com.xwtech.xwecp.service.logic.pojo;


public class IMEIInfo
{
	private String resTypeId;
	
	private String imeiId;
	
	private String errMsg;

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getResTypeId() {
		return resTypeId;
	}

	public void setResTypeId(String resTypeId) {
		this.resTypeId = resTypeId;
	}
	
	public void setImeiId(String imeiId)
	{
		this.imeiId = imeiId;
	}

	public String getImeiId()
	{
		return this.imeiId;
	}
}