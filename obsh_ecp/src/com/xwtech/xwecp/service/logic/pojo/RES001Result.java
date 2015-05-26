package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;

public class RES001Result extends BaseServiceInvocationResult
{
	private List<ReserveOrderInfo> reserveBusiList = new ArrayList<ReserveOrderInfo>();

	private List<ReserveOrderInfo> reserveMarketList = new ArrayList<ReserveOrderInfo>();
	
	private List<ReserveOrderInfo> reserveAllList = new ArrayList<ReserveOrderInfo>();

	public void setReserveBusiList(List<ReserveOrderInfo> reserveBusiList)
	{
		this.reserveBusiList = reserveBusiList;
	}

	public List<ReserveOrderInfo> getReserveAllList() {
		return reserveAllList;
	}

	public void setReserveAllList(List<ReserveOrderInfo> reserveAllList) {
		this.reserveAllList = reserveAllList;
	}

	public List<ReserveOrderInfo> getReserveBusiList()
	{
		return this.reserveBusiList;
	}

	public void setReserveMarketList(List<ReserveOrderInfo> reserveMarketList)
	{
		this.reserveMarketList = reserveMarketList;
	}

	public List<ReserveOrderInfo> getReserveMarketList()
	{
		return this.reserveMarketList;
	}

}