package com.xwtech.xwecp.service.logic.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY050023Result;
import com.xwtech.xwecp.service.logic.pojo.ServiceInfo;

public class GetCloseServiceResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetCloseServiceResolver.class);
	
	public GetCloseServiceResolver()
	{
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		try
		{
			QRY050023Result ret = (QRY050023Result)result;
			List<ServiceInfo> list = ret.getServiceInfos();
			if (list != null && list.size() > 0) {
				Map map = this.getAdjFunDic();
				for (ServiceInfo obj:list) {
					obj.setServiceName((String)map.get(obj.getServiceId()));
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	private static Map<String, String> mapAdjFunDic = null;
	
	/**
	 * 附加功能字典
	 * @return
	 */
	private Map<String, String> getAdjFunDic() {
		if (mapAdjFunDic == null) {
			mapAdjFunDic = new HashMap<String, String>();
			mapAdjFunDic.put("1", "台港澳国际漫游");
			mapAdjFunDic.put("2", "省际漫游");
			mapAdjFunDic.put("3", "省内漫游");
			mapAdjFunDic.put("4", "短信");
			mapAdjFunDic.put("5", "语音信箱");
			mapAdjFunDic.put("6", "传真");
			mapAdjFunDic.put("7", "数据");
			mapAdjFunDic.put("8", "台港澳国际长权");
			mapAdjFunDic.put("9", "呼叫转移");
			mapAdjFunDic.put("10", "呼叫等待");
			mapAdjFunDic.put("12", "多方通话");
			mapAdjFunDic.put("13", "来电显示");
			mapAdjFunDic.put("14", "大众卡本地通");
			mapAdjFunDic.put("15", "呼叫保持");
			mapAdjFunDic.put("18", "主叫隐藏");
		}
		return mapAdjFunDic;
	}
}
