package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserXp;

public class QRY020005Result extends BaseServiceInvocationResult
{
	private List<UserXp> userXp = new ArrayList<UserXp>();

	public void setUserXp(List<UserXp> userXp)
	{
		this.userXp = userXp;
	}

	public List<UserXp> getUserXp()
	{
		return this.userXp;
	}

}