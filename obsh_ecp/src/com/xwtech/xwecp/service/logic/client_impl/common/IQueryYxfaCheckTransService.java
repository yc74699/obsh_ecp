package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.YxfaIdInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY090005Result;

public interface IQueryYxfaCheckTransService
{
	public QRY090005Result queryYxfaCheckTrans(String ddr_city, String usermarketingbaseinfo_user_id, List<YxfaIdInfo> yxfaIdInfo, String bossmarket_rec_type) throws LIException;

}