package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.AlreadyExperienceBiz;
import com.xwtech.xwecp.service.logic.pojo.CanExperienceBiz;

public class QRY050031Result extends BaseServiceInvocationResult
{
	private List<AlreadyExperienceBiz> alreadyExperienceBizs = new ArrayList<AlreadyExperienceBiz>();

	private List<CanExperienceBiz> canExperienceBizs = new ArrayList<CanExperienceBiz>();

	public void setAlreadyExperienceBizs(List<AlreadyExperienceBiz> alreadyExperienceBizs)
	{
		this.alreadyExperienceBizs = alreadyExperienceBizs;
	}

	public List<AlreadyExperienceBiz> getAlreadyExperienceBizs()
	{
		return this.alreadyExperienceBizs;
	}

	public void setCanExperienceBizs(List<CanExperienceBiz> canExperienceBizs)
	{
		this.canExperienceBizs = canExperienceBizs;
	}

	public List<CanExperienceBiz> getCanExperienceBizs()
	{
		return this.canExperienceBizs;
	}

}