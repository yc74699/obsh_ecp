package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.YxfaIdInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY070005Result;

public interface IQueryYxfaCheckService
{
	public QRY070005Result queryYxfaCheck(String ddr_city, String usermarketingbaseinfo_user_id, List<YxfaIdInfo> yxfaIdInfo) throws LIException;

}