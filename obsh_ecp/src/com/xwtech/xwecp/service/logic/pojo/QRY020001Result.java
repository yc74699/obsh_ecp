package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;

public class QRY020001Result extends BaseServiceInvocationResult
{
	private List<GommonBusiness> gommonBusiness = new ArrayList<GommonBusiness>();

	private List<VIPUser> vipUser = new ArrayList<VIPUser>();

	public List<VIPUser> getVipUser() {
		return vipUser;
	}

	public void setVipUser(List<VIPUser> vipUser) {
		this.vipUser = vipUser;
	}

	public void setGommonBusiness(List<GommonBusiness> gommonBusiness)
	{
		this.gommonBusiness = gommonBusiness;
	}

	public List<GommonBusiness> getGommonBusiness()
	{
		return this.gommonBusiness;
	}

}