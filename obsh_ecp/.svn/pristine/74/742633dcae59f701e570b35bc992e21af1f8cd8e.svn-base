package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY040002Result;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 查询用户余额和积分M值信息
 * @author yuantao
 * 2009-12-25
 */
public class GetUserAccscoreResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetUserAccscoreResolver.class);
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
			                      List<RequestParameter> param) throws Exception
	{
		QRY040002Result res = (QRY040002Result)result;
		ParseXmlConfig config = null;
		Element root = null;
		String resCode = "";
		
		try
		{
			config = new ParseXmlConfig();
			//解析返回报文
			root = config.getElement(String.valueOf(bossResponse));
			if (null != root)
			{
				List list = config.getContentList(root, "balance_response");
				if (null != list && list.size() > 0)
				{
					Element response = config.getElement((Element)list.get(0), "response");
					if (null != response)
					{
						//获取返回错误码
						resCode = config.getChildText(response, "resp_code");
						if ("0000".equals(resCode))
						{
							//置状态为 成功
							res.setResultCode("0");
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
