package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.Caky;

public class QRY010025Result extends BaseServiceInvocationResult
{
	private List<Caky> cakys = new ArrayList<Caky>();

	public void setCakys(List<Caky> cakys)
	{
		this.cakys = cakys;
	}

	public List<Caky> getCakys()
	{
		return this.cakys;
	}

}