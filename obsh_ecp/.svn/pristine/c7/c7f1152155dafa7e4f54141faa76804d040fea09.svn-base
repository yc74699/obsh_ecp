package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610048Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;

import java.util.List;

public interface IVirtualProdInstallMemChkService
{
	/**
	 * 10.家庭V网订购时校验副号
	 * @param familysubsid 主号用户编号
	 * @param pkgprodid 增值包编码
	 * @param increprodid 增值产品编码
	 * @param effecttype 产品生效方式
	 * @param memberInfoIn 新增副号成员列表
	 * @return
	 * @throws LIException
	 */
	public DEL610048Result virtualProdInstallMemChk(String familysubsid,String pkgprodid,String increprodid,String effecttype,List<MemberinfoIn> memberInfoIn) throws LIException;
	
}
