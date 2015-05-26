package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY010016Result extends BaseServiceInvocationResult
{
	private String newBalance = "";

	private String balance = "";

	private String accountExpireDate = "";

	private String errorCode = "";

	public void setNewBalance(String newBalance)
	{
		this.newBalance = newBalance;
	}

	public String getNewBalance()
	{
		return this.newBalance;
	}

	public void setBalance(String balance)
	{
		this.balance = balance;
	}

	public String getBalance()
	{
		return this.balance;
	}

	public void setAccountExpireDate(String accountExpireDate)
	{
		this.accountExpireDate = accountExpireDate;
	}

	public String getAccountExpireDate()
	{
		return this.accountExpireDate;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorCode()
	{
		return this.errorCode;
	}

}