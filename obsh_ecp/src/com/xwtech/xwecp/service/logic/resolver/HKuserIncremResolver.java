package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY020013Result;

/**
 * 增值业务
 * @author Tkk
 *
 */
public class HKuserIncremResolver implements ITeletextResolver
{
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse,List<RequestParameter> param) throws Exception
	{
		QRY020013Result re = (QRY020013Result) result;
		
		//1.获取地市
		String city = null;
		for(RequestParameter rp : param){
			if(rp.getParameterName().equals("context_ddr_city")){
				city = (String) rp.getParameterValue();
				break;
			}
		}
		
		//2.判断是否是割接地市
		//if(XWECPApp.NEED_CUT_CITYS.toString().indexOf(city) > -1){
			re.setRetCode("0");
		//}
	}
}
