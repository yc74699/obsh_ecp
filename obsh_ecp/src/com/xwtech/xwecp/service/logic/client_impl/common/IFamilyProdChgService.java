package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.pojo.DEL610042Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;

public interface IFamilyProdChgService 
{
	/**
	 * 家庭V网短号套餐变更
	 * @param familysubsid 主号用户编号
	 * @param pkgprodid 增值包编码
	 * @param addincreprodid 增值产品编码生效方式
	 * @param delincreprodid 增值产品编码失效方式
	 * @param memberInfoIn 新增副号成员列表
	 * @return
	 * @throws LIException
	 */
	public DEL610042Result familyProdChg(String familysubsid,String pkgprodid,String effecttype,String addincreprodid,String delincreprodid,List<MemberinfoIn> memberInfoIn) 
	throws LIException;
}
