package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.PackageBizId;
import com.xwtech.xwecp.service.logic.pojo.QRY050062Result;

public interface IQryPkgDetailInfoService
{
	public QRY050062Result qryPkgDetailInfo(List<PackageBizId> packageBizIds) throws LIException;

}