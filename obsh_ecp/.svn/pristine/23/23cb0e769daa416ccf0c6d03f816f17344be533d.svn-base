package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040063Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;



public class DoSignCheckByIcAndRelBandInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger.getLogger(DoSignCheckByIcAndRelBandInvocation.class);
	
	public DoSignCheckByIcAndRelBandInvocation()
	{
		
	}
	
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		DEL040063Result res = new DEL040063Result();
		if(chkMobile(accessId,res,params) && aBankAddvtelChk(accessId,res,params))
		{
			res.setResultCode("0");
			res.setErrorCode("");
			res.setErrorMessage("");
		}
		else
		{
			res.setResultCode("1");
			res.setErrorCode("1");
			res.setErrorMessage("校验失败");
		}
		return res;
	}
	
	private boolean chkMobile(String accessId,DEL040063Result res,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_userinfochk", params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_userinfochk", this.generateCity(params)));
				if (null != rspXml && !"".equals(rspXml))
				{
					Element personalInfo = this.getElement(rspXml.getBytes());
					String resp_code = personalInfo.getChild("response").getChildText("resp_code");
					String resp_desc = personalInfo.getChild("response").getChildText("resp_desc");
				
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					if(!LOGIC_SUCESS.equals(resultCode))
					{
						return false;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return true;
	}
	
	private boolean aBankAddvtelChk(String accessId,DEL040063Result res,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_abankaddvtelchk", params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_abankaddvtelchk", this.generateCity(params)));
				if (null != rspXml && !"".equals(rspXml))
				{
					Element personalInfo = this.getElement(rspXml.getBytes());
					String resp_code = personalInfo.getChild("response").getChildText("resp_code");
					String resp_desc = personalInfo.getChild("response").getChildText("resp_desc");
				
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					if(!LOGIC_SUCESS.equals(resultCode))
					{
						return false;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return true;
	}
	
}
