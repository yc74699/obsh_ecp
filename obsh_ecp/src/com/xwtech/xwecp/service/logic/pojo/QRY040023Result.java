package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserIncrements;

public class QRY040023Result extends BaseServiceInvocationResult
{
	private List<UserIncrements> userIncrementsList = new ArrayList<UserIncrements>();

	public void setUserIncrementsList(List<UserIncrements> userIncrementsList)
	{
		this.userIncrementsList = userIncrementsList;
	}

	public List<UserIncrements> getUserIncrementsList()
	{
		return this.userIncrementsList;
	}

}