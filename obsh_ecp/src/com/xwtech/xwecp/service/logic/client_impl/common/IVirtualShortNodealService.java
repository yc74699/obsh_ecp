package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610046Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;

public interface IVirtualShortNodealService
{
	/**
	 * 家庭短号开关
	 * @param familysubsid 主号用户编号
	 * @param oprttype 操作类型
	 * @param memberInfoIn
	 * @return
	 * @throws LIException
	 */
	public DEL610046Result virtualShortNodeal(String familysubsid,String oprttype,List<MemberinfoIn> memberInfoIn) throws LIException;
}
