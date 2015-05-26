package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.MTribe;

public class QRY050011Result extends BaseServiceInvocationResult
{
	private List<MTribe> mTribeList = new ArrayList<MTribe>();

	public void setMTribeList(List<MTribe> mTribeList)
	{
		this.mTribeList = mTribeList;
	}

	public List<MTribe> getMTribeList()
	{
		return this.mTribeList;
	}

}