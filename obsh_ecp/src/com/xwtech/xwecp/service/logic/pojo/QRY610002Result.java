package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 校验用户是否加入V网
 * @author Administrator
 *
 */
public class QRY610002Result extends BaseServiceInvocationResult implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private String ISHOUSEHOLD;//是否户主
	
	private String HASFMYPROD;//是否有V网产品
	
	

	public String getISHOUSEHOLD()
	{
		return ISHOUSEHOLD;
	}

	public void setISHOUSEHOLD(String iSHOUSEHOLD)
	{
		ISHOUSEHOLD = iSHOUSEHOLD;
	}

	public String getHASFMYPROD()
	{
		return HASFMYPROD;
	}

	public void setHASFMYPROD(String hASFMYPROD)
	{
		HASFMYPROD = hASFMYPROD;
	}

	
}
