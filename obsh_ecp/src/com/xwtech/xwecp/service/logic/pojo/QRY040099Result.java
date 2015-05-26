package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserPkgInfo;

public class QRY040099Result extends BaseServiceInvocationResult
{
	private String prodId;
	
	private List<UserPkgInfo> userPkgList = new ArrayList<UserPkgInfo>();

	public void setUserPkgList(List<UserPkgInfo> userPkgList)
	{
		this.userPkgList = userPkgList;
	}

	public List<UserPkgInfo> getUserPkgList()
	{
		return this.userPkgList;
	}

	
	public String getProdId()
	{
		return prodId;
	}

	
	public void setProdId(String prodId)
	{
		this.prodId = prodId;
	}

}