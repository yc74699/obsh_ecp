package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL090001Result;

public interface ITransactYxfaNewService
{
	public DEL090001Result transactYxfaNew(String bossmms_services_type, String ddr_city, String usermarketingbaseinfo_user_id, int id_type, String detail_operating_srl, String marketingbusiinfo_busi_pack_id, String bossmms_pack_id, String creditreleasegrade_grade_amount, String usermarketingbaseinfo_plan_id, String bossmarket_rec_type) throws LIException;

}