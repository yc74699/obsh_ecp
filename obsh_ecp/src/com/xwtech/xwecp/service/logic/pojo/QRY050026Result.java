package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.SelfDonate;

public class QRY050026Result extends BaseServiceInvocationResult
{
	private List<SelfDonate> selfDonate = new ArrayList<SelfDonate>();

	public void setSelfDonate(List<SelfDonate> selfDonate)
	{
		this.selfDonate = selfDonate;
	}

	public List<SelfDonate> getSelfDonate()
	{
		return this.selfDonate;
	}

}