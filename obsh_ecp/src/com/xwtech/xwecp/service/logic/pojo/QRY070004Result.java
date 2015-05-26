package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.YxfaLpbInfo;

/**
 * 奖品包查询
 * 2014 - 09 -24
 */
public class QRY070004Result extends BaseServiceInvocationResult
{
	private List<YxfaLpbInfo> yxfaLpbInfo = new ArrayList<YxfaLpbInfo>();
    private String ret_code;
	private String ret_msg;
	private String Limit_brand;
	private String mindrawcount;
	private String maxdrawcount;
	private String mainprodid;
	
	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String retCode) {
		ret_code = retCode;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String retMsg) {
		ret_msg = retMsg;
	}

	public String getLimit_brand() {
		return Limit_brand;
	}

	public void setLimit_brand(String limitBrand) {
		Limit_brand = limitBrand;
	}

	public String getMindrawcount() {
		return mindrawcount;
	}

	public void setMindrawcount(String mindrawcount) {
		this.mindrawcount = mindrawcount;
	}

	public String getMaxdrawcount() {
		return maxdrawcount;
	}

	public void setMaxdrawcount(String maxdrawcount) {
		this.maxdrawcount = maxdrawcount;
	}

	public String getMainprodid() {
		return mainprodid;
	}

	public void setMainprodid(String mainprodid) {
		this.mainprodid = mainprodid;
	}

	public void setYxfaLpbInfo(List<YxfaLpbInfo> yxfaLpbInfo)
	{
		this.yxfaLpbInfo = yxfaLpbInfo;
	}

	public List<YxfaLpbInfo> getYxfaLpbInfo()
	{
		return this.yxfaLpbInfo;
	}

}