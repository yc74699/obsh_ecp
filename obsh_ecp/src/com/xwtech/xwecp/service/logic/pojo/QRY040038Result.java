package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040038Result extends BaseServiceInvocationResult
{
	private String ifCanUse = "";

	private String availCount = "";

	private List<GiftInfo> giftList = new ArrayList<GiftInfo>();;
	public void setIfCanUse(String ifCanUse)
	{
		this.ifCanUse = ifCanUse;
	}

	public String getIfCanUse()
	{
		return this.ifCanUse;
	}

	public void setAvailCount(String availCount)
	{
		this.availCount = availCount;
	}

	public String getAvailCount()
	{
		return this.availCount;
	}

	
	public List<GiftInfo> getGiftList()
	{
		return giftList;
	}

	
	public void setGiftList(List<GiftInfo> giftList)
	{
		this.giftList = giftList;
	}

	public String toString()
	{
		return "QRY040038Result [availCount=" + availCount + ", giftList="
				+ giftList + ", ifCanUse=" + ifCanUse + "]";
	}

}