package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610035Result;
import com.xwtech.xwecp.service.logic.pojo.Deviceinfo;
import com.xwtech.xwecp.service.logic.pojo.Fmymeminfo;
import com.xwtech.xwecp.service.logic.pojo.ProductInfoSubmit;
import com.xwtech.xwecp.service.logic.pojo.Propertyinfo;
import com.xwtech.xwecp.service.logic.pojo.RWDInfo;

/**
 * 订单提交
 * @author 仰孝庆
 * 2014-5-29
 *
 */
public interface IFmyordersubmitService
{
	public DEL610035Result myordersubmit(Map<Object,String> mapSubmit,
			List<RWDInfo> rwdinfoList,
			List<ProductInfoSubmit> productionList,
			List<Deviceinfo> deviceinfoList,
			List<Propertyinfo> propertyinfoList,
			List<Fmymeminfo> fmymeminfoList, 
			List<Fmymeminfo> vitualinfoList 
			) throws  LIException;

}