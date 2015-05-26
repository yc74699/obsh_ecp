package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.Edition;

public class QRY040027Result extends BaseServiceInvocationResult
{
	private List<Edition> edtList = new ArrayList<Edition>();

	public void setEdtList(List<Edition> edtList)
	{
		this.edtList = edtList;
	}

	public List<Edition> getEdtList()
	{
		return this.edtList;
	}

}