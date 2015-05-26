package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包信息列表
 * @author Administrator
 *
 */
public class GiftList
{
	//红包信息
	private List<GiftInfo> giftInfo = new ArrayList<GiftInfo>();

	public List<GiftInfo> getGiftInfo()
	{
		return giftInfo;
	}

	public void setGiftInfo(List<GiftInfo> giftInfo)
	{
		this.giftInfo = giftInfo;
	}
	

}
