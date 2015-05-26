package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.User17202;

public class QRY050021Result extends BaseServiceInvocationResult
{
	private List<User17202> user17202 = new ArrayList<User17202>();

	public void setUser17202(List<User17202> user17202)
	{
		this.user17202 = user17202;
	}

	public List<User17202> getUser17202()
	{
		return this.user17202;
	}

}