package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;


import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;

public class CheckChgcalltransInvocation extends BaseInvocation implements ILogicalService{
    
private static final Logger logger = Logger.getLogger(CheckChgcalltransInvocation.class);
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		
		DEL010001Result result = new DEL010001Result();
		Boolean flag =isChgcalltrans(accessId,config,params,result);
		if(false == flag)
		{
			result.setResultCode(LOGIC_ERROR);
			result.setErrorCode(LOGIC_ERROR);
			result.setErrorMessage("该手机号码已经转移给另一个号码，不能办理呼叫转移！");
		}
		else
		{
			 sendXml("cc_chgcalltrans_700",params,accessId,result);
				
		}
		return result;
	}
	
   //判断所加入的转移号码是否已经转移给别的号码
    public Boolean isChgcalltrans(String accessId, ServiceConfig config, List<RequestParameter> params,DEL010001Result result) {
		String phoneNum = getParameters(params,"reserve1").toString();
		String bizId = getParameters(params,"id").toString();
		addParameter("phoneNum",phoneNum,params);
		addParameter("bizId",bizId,params);
		Element root =sendXml("cc_cgetsmscall_602",params,accessId,result);
		Boolean flag = true;
		if(null != root)
		{
			String reCode = root.getChild("response").getChildText("resp_code");
			if(BOSS_SUCCESS.equals(reCode))
			{
				flag = false;
			}
			
		}
		return flag;
	}
    
	private void addParameter(String paraName,String paraValue, List<RequestParameter> params)
	{
		RequestParameter para = new RequestParameter();
		para.setParameterName(paraName);
		para.setParameterValue(paraValue);
		params.add(para);
	}
	
    /**
	 * 组装报文模板，并发送报文
	 * @param interfaceName
	 * @param params
	 * @param accessId
	 * @param res
	 * @return
	 */
	private Element sendXml(String interfaceName,List<RequestParameter> params,String accessId,DEL010001Result res)
	{
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext(interfaceName, params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, interfaceName, this.generateCity(params)));
				Element personalInfo = checkReturnXml(rspXml,res);
			
				return personalInfo;
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,DEL010001Result res)
	{
		Element root = this.getElement(rspXml.getBytes());
		String resp_code = root.getChild("response").getChildText("resp_code");
		String resp_desc = root.getChild("response").getChildText("resp_desc");
		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		if(null != rspXml && !"".equals(rspXml))
		{
			if(!LOGIC_SUCESS.equals(resultCode) || null == root)
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
			else
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				return root;
			}
		}
		return null;
	}

}
