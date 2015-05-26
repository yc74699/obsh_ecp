package com.xwtech.xwecp.communication.ws.gsmClient;

/**
 * 
 *  Copyright (c) 2012,JSNewland All rights reserved。
 *  FileName：com.newland.nubass.service.serial.entity.ServiceSerialBean.java
 *  Description: 交往圈服务请求bean 
 *  CreateDate: 2013-10-19
 *  @author gej
 *  @version 1.0
 */
public class ServiceSerialBean
{
	private String other_party;//对方号码
	private String other_party_big_brand_p;//品牌
	private String jwq_exp_serial;//频次
	//	private int object_type;//信息包括类型（1语音、2短信）
	private String month_number;//月份
	private String msisdn;//本人手机号码

	public String getMonth_number()
	{
		return month_number;
	}

	public void setMonth_number(String month_number)
	{
		this.month_number = month_number;
	}

	public String getMsisdn()
	{
		return msisdn;
	}

	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	public String getOther_party()
	{
		return other_party;
	}

	public void setOther_party(String other_party)
	{
		this.other_party = other_party;
	}

	public String getOther_party_big_brand_p()
	{
		return other_party_big_brand_p;
	}

	public void setOther_party_big_brand_p(String other_party_big_brand_p)
	{
		this.other_party_big_brand_p = other_party_big_brand_p;
	}

	public String getJwq_exp_serial()
	{
		return jwq_exp_serial;
	}

	public void setJwq_exp_serial(String jwq_exp_serial)
	{
		this.jwq_exp_serial = jwq_exp_serial;
	}

	//	public int getObject_type() {
	//		return object_type;
	//	}
	//	public void setObject_type(int object_type) {
	//		this.object_type = object_type;
	//	}

	public String toXml()
	{
		return "<content>" + "<month_number>" + this.getMonth_number() + "</month_number>" + "<msisdn>" + this.getMsisdn()
				+ "</msisdn>" + "</content>";
	}
}
