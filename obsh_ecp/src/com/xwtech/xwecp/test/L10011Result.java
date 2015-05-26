package com.xwtech.xwecp.test;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;


public class L10011Result extends BaseServiceInvocationResult
{
	private static final Logger logger = Logger.getLogger(L10011Result.class);
	
	private String userId;
	
	private String userName;
	
	private String userBrand;
	
	private int userBrandId;
	
	private int userBalance;

	public int getUserBalance()
	{
		return userBalance;
	}

	public void setUserBalance(int userBalance)
	{
		this.userBalance = userBalance;
	}

	public String getUserBrand()
	{
		return userBrand;
	}

	public void setUserBrand(String userBrand)
	{
		this.userBrand = userBrand;
	}

	public int getUserBrandId()
	{
		return userBrandId;
	}

	public void setUserBrandId(int userBrandId)
	{
		this.userBrandId = userBrandId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
