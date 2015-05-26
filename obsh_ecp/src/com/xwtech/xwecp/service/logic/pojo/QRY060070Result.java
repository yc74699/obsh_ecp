package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserActList;

public class QRY060070Result extends BaseServiceInvocationResult
{
	private List<UserActList> userActLists = new ArrayList<UserActList>();

	public void setUserActLists(List<UserActList> userActLists)
	{
		this.userActLists = userActLists;
	}

	public List<UserActList> getUserActLists()
	{
		return this.userActLists;
	}

}