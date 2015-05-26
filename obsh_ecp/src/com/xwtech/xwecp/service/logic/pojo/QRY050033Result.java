package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ActivityBean;

public class QRY050033Result extends BaseServiceInvocationResult
{
	private List<ActivityBean> activityBean = new ArrayList<ActivityBean>();

	public void setActivityBean(List<ActivityBean> activityBean)
	{
		this.activityBean = activityBean;
	}

	public List<ActivityBean> getActivityBean()
	{
		return this.activityBean;
	}

}