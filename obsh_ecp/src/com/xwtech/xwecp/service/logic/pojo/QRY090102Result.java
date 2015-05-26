package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.Imei;

public class QRY090102Result extends BaseServiceInvocationResult
{
	private List<Imei> imeiList = new ArrayList<Imei>();

	public void setImeiList(List<Imei> imeiList)
	{
		this.imeiList = imeiList;
	}

	public List<Imei> getImeiList()
	{
		return this.imeiList;
	}

}