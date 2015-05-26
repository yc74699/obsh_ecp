package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.GroupMemberInfo;

public class QRY090002Result extends BaseServiceInvocationResult
{
	private String groupSubSid;

	private List<GroupMemberInfo> groupMemberInfos = new ArrayList<GroupMemberInfo>();

	public void setGroupSubSid(String groupSubSid)
	{
		this.groupSubSid = groupSubSid;
	}

	public String getGroupSubSid()
	{
		return this.groupSubSid;
	}

	public void setGroupMemberInfos(List<GroupMemberInfo> groupMemberInfos)
	{
		this.groupMemberInfos = groupMemberInfos;
	}

	public List<GroupMemberInfo> getGroupMemberInfos()
	{
		return this.groupMemberInfos;
	}

}