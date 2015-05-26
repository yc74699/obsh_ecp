package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL040109Result extends BaseServiceInvocationResult {
	/*cc_operating_srl	1	string		客服受理流水
	webcustinfo_web_booking_id	1	string		订单号*/
	private String cc_operating_srl;
	private String webcustinfo_web_booking_id;
	public String getCc_operating_srl() {
		return cc_operating_srl;
	}
	public void setCc_operating_srl(String cc_operating_srl) {
		this.cc_operating_srl = cc_operating_srl;
	}
	public String getWebcustinfo_web_booking_id() {
		return webcustinfo_web_booking_id;
	}
	public void setWebcustinfo_web_booking_id(String webcustinfo_web_booking_id) {
		this.webcustinfo_web_booking_id = webcustinfo_web_booking_id;
	}
	
	

}
