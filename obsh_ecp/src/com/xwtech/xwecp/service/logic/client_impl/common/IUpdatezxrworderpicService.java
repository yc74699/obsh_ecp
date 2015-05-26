package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL010024Result;
import com.xwtech.xwecp.service.logic.pojo.Paralist;

/**
 * 在线入网订单实名制图片更新接口
 * @author YangXQ
 * 2014-10-24
 */
public interface IUpdatezxrworderpicService
{
	public DEL010024Result updatezxrworderpic(String order_id,String ddr_city,String humanpic,String certpicfront,String certpicback,String humanpic_ext,String certpicfront_ext,String certpicback_ext) throws LIException;

}