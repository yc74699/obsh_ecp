package com.xwtech.xwecp.service.logic.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.IncrementInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY050024Result;

public class GetCloseIncrementResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetCloseIncrementResolver.class);
	
	public GetCloseIncrementResolver()
	{
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		try
		{
			QRY050024Result ret = (QRY050024Result)result;
			List<IncrementInfo> list = ret.getIncrementInfos();
			if (list != null && list.size() > 0) {
				Map map = this.getAddIncDic();
				for (IncrementInfo obj:list) {
					obj.setIncrementName((String)map.get(obj.getIncrementId()));
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	private static Map<String, String> mapAddIncDic = null;
	
	/**
	 * 新业务字典
	 * @return
	 */
	private Map<String, String> getAddIncDic() {
		if (mapAddIncDic == null) {
			mapAddIncDic = new HashMap<String, String>();
			mapAddIncDic.put("5000", "短信呼");
			mapAddIncDic.put("5001", "短信话费提醒(原)");
			mapAddIncDic.put("5002", "无线终端");
			mapAddIncDic.put("5003", "盐城特预卡");
			mapAddIncDic.put("5004", "手机证券");
			mapAddIncDic.put("5005", "集团GPRS");
			mapAddIncDic.put("5006", "彩铃");
			mapAddIncDic.put("5007", "双号传真");
			mapAddIncDic.put("5008", "双号数据");
			mapAddIncDic.put("5010", "短信帐单");
			mapAddIncDic.put("5011", "纸帐单");
			mapAddIncDic.put("5012", "话费易");
			mapAddIncDic.put("5013", "动感易");
			mapAddIncDic.put("5014", "积分短信申请");
			mapAddIncDic.put("5015", "套餐易");
			mapAddIncDic.put("5016", "呼转特定号码");
			mapAddIncDic.put("5017", "生日祝福");
			mapAddIncDic.put("5018", "积分申请");
			mapAddIncDic.put("5019", "动感M值计划");
			mapAddIncDic.put("5020", "彩音");
			mapAddIncDic.put("5021", "亲情易");
			mapAddIncDic.put("5022", "被叫易");
			mapAddIncDic.put("5023", "短信回呼");
			mapAddIncDic.put("5024", "忙时通");
			mapAddIncDic.put("5025", "CMNET业务");
			mapAddIncDic.put("5026", "CMWAP业务");
			mapAddIncDic.put("5027", "梦网易");
			mapAddIncDic.put("5028", "终端管理CMDM");
			mapAddIncDic.put("5029", "彩信帐单");
			mapAddIncDic.put("5030", "来电提醒");
		}

		return mapAddIncDic;
	}
}
