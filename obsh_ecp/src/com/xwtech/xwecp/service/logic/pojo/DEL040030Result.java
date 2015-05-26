package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL040030Result extends BaseServiceInvocationResult
{
	private String invaliddate;

	private long balance;

	private long transfee;

	public void setInvaliddate(String invaliddate)
	{
		this.invaliddate = invaliddate;
	}

	public String getInvaliddate()
	{
		return this.invaliddate;
	}

	public void setBalance(long balance)
	{
		this.balance = balance;
	}

	public long getBalance()
	{
		return this.balance;
	}

	public void setTransfee(long transfee)
	{
		this.transfee = transfee;
	}

	public long getTransfee()
	{
		return this.transfee;
	}

}