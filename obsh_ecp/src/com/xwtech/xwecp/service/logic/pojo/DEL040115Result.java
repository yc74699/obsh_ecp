package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 终端串号信息同步
 * @author wanghuan
 *
 */
public class DEL040115Result extends BaseServiceInvocationResult {
	/**
	 * <resp_type>1</resp_type>
	<resp_code>101</resp_code>
	<resp_desc><![CDATA[全部失败]]></resp_desc>
	*/
	private String respType;
	private String respCode;
	private String respDesc;
	
	public String getRespType() {
		return respType;
	}
	public void setRespType(String respType) {
		this.respType = respType;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	
	

}
