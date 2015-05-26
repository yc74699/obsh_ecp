package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.VnetGroupBean;

public class QRY040030Result extends BaseServiceInvocationResult
{
	private List<VnetGroupBean> vnetGroupBean = new ArrayList<VnetGroupBean>();

	public void setVnetGroupBean(List<VnetGroupBean> vnetGroupBean)
	{
		this.vnetGroupBean = vnetGroupBean;
	}

	public List<VnetGroupBean> getVnetGroupBean()
	{
		return this.vnetGroupBean;
	}

}