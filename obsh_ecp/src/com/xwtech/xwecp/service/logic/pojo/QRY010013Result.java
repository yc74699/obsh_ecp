package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ChargeInfo;

public class QRY010013Result extends BaseServiceInvocationResult
{
	private List<ChargeInfo> chargeInfo = new ArrayList<ChargeInfo>();

	public void setChargeInfo(List<ChargeInfo> chargeInfo)
	{
		this.chargeInfo = chargeInfo;
	}

	public List<ChargeInfo> getChargeInfo()
	{
		return this.chargeInfo;
	}

}