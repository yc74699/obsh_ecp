package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.MyTotalInfo;

public class QRY040047Result extends BaseServiceInvocationResult
{
	private List<MyTotalInfo> myTotalInfoList = new ArrayList<MyTotalInfo>();
	private String puk1;
	private String puk2;

	public String getPuk1() {
		return puk1;
	}

	public void setPuk1(String puk1) {
		this.puk1 = puk1;
	}

	public String getPuk2() {
		return puk2;
	}

	public void setPuk2(String puk2) {
		this.puk2 = puk2;
	}

	public void setMyTotalInfoList(List<MyTotalInfo> myTotalInfoList)
	{
		this.myTotalInfoList = myTotalInfoList;
	}

	public List<MyTotalInfo> getMyTotalInfoList()
	{
		return this.myTotalInfoList;
	}

}