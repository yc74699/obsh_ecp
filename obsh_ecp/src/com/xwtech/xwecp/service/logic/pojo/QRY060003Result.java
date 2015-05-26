package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 营销推荐专区查询
 * @author 张斌
 * 2015-4-14
 */
public class QRY060003Result extends BaseServiceInvocationResult
{
	private List<ReferralsMarketInfo> reMarketInfo  = new ArrayList<ReferralsMarketInfo>();

	public List<ReferralsMarketInfo> getReMarketInfo() {
		return reMarketInfo;
	}

	public void setReMarketInfo(List<ReferralsMarketInfo> reMarketInfo) {
		this.reMarketInfo = reMarketInfo;
	}

}