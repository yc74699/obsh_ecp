package com.xwtech.xwecp.service.logic.invocation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.EXT010001Result;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.web.serverHttp.SendHttpMall;
import com.xwtech.xwecp.web.serverHttp.TableServerInfoSync;

public class GetTableServerInfoInvocation extends BaseInvocation implements ILogicalService {

	private SendHttpMall sendHttpMall;
	
	private TableServerInfoSync tableServerInfoSync;
	
	public GetTableServerInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.sendHttpMall = (SendHttpMall)(springCtx.getBean("sendHttpMall"));
		this.tableServerInfoSync = (TableServerInfoSync)(springCtx.getBean("tableServerInfoSync"));
		
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		// TODO Auto-generated method stub
		EXT010001Result result = new EXT010001Result();
		String userMobile = (String)this.getParameters(params, "phoneNum");
		String brandNum = (String)this.getParameters(params, "brandNum");
		String areaNum = (String)this.getParameters(params, "areaNum");
		String userId = (String)this.getParameters(params, "userId");
		String consume = (String)this.getParameters(params, "consume");
		String methodName = (String)this.getParameters(params, "methodName");
		String placeId = (String)this.getParameters(params, "placeId");
		String channelId = (String)this.getParameters(params, "channelId");
		String outXml = sendHttpMall.sendPost(userMobile, brandNum, areaNum, userId, consume, methodName,placeId,channelId);
		Document doc;
		if (null != outXml) {
			try {
				doc = DocumentHelper.parseText(outXml);
				String jsonRet = tableServerInfoSync.execute(doc);
				result.setResultCode(LOGIC_SUCESS);
				result.setResultJson(jsonRet);
			} catch (DocumentException e) {
				result.setResultCode(LOGIC_ERROR);
				//logger.error(e,e);
			} catch (ServletException e) {
				result.setResultCode(LOGIC_ERROR);
				//logger.error(e,e);
			} catch (IOException e) {
				result.setResultCode(LOGIC_ERROR);
				//logger.error(e,e);
			}
		} else {
			result.setResultCode(LOGIC_ERROR);
		}
		return result;
	}
	
	/**
	 * 获取参数值
	 * @param params
	 * @param name
	 * @return
	 */
	protected RequestParameter getParameter(List<RequestParameter> params, String name)
	{
		if (params != null && params.size() > 0) {
			for(int i = 0; i<params.size(); i++)
			{
				RequestParameter param = params.get(i);
				if(param.getParameterName().equals(name))
				{
					return param;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 从参数列表里获取参数值
	 * 
	 * @param params
	 * @param parameterName
	 * @return
	 */
	protected Object getParameters(final List<RequestParameter> params, String parameterName) {
		Object rtnVal = null;
		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals(parameterName)) {
				rtnVal = parameter.getParameterValue();
				break;
			}
		}
		return rtnVal;
	}

	public SendHttpMall getSendHttpMall() {
		return sendHttpMall;
	}

	public void setSendHttpMall(SendHttpMall sendHttpMall) {
		this.sendHttpMall = sendHttpMall;
	}

	public TableServerInfoSync getTableServerInfoSync() {
		return tableServerInfoSync;
	}

	public void setTableServerInfoSync(TableServerInfoSync tableServerInfoSync) {
		this.tableServerInfoSync = tableServerInfoSync;
	}
}
