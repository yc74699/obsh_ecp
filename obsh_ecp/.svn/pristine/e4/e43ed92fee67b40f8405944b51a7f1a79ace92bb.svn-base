package com.xwtech.xwecp.service.logic.invocation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.EXT010002Result;
import com.xwtech.xwecp.util.JsonResponseUtil;
import com.xwtech.xwecp.web.serverHttp.SendHttpMall;
import org.apache.commons.beanutils.PropertyUtils;

public class GetMallMobileInfoInvocation extends BaseInvocation implements ILogicalService {
	private SendHttpMall sendHttpMall;
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		// TODO Auto-generated method stub
		EXT010002Result result = new EXT010002Result();
		 Map<String, Object> map = new HashMap<String, Object>();
		String userMobile = (String)this.getParameters(params, "userMobile");
		String brandNum = (String)this.getParameters(params, "userMobile");
		String areaNum = (String)this.getParameters(params, "areaNum");
		String userId = (String)this.getParameters(params, "userId");
		String consume = (String)this.getParameters(params, "consume");
		String methodName = (String)this.getParameters(params, "methodName");
		String placeId="";
		String channelId = "";
		String outXml = sendHttpMall.sendPost(userMobile, brandNum, areaNum, userId, consume, methodName,placeId,channelId);
		Document doc;
		if (null != outXml) {
			try {
				doc = DocumentHelper.parseText(outXml);
				map = execute(doc);
				result.setResultCode(LOGIC_SUCESS);
				result.setMap(map);
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
	
	public  Map<String, Object> execute(Document doc) throws ServletException, IOException {
		 Map<String, Object> ls = new HashMap<String, Object>();
		List<Element> mallList = doc.selectNodes("/result");
		Iterator<Element> it = mallList.iterator();
		String resultCode = "";
		String resultMsg = "";
		String retJson = "";
		while (it.hasNext())
		{
			Element element = (Element) it.next();
			retJson = element.element("resultObj")== null ? "" : element.element("resultObj").getStringValue();
			resultCode = element.element("resultCode")== null ? "" : element.element("resultCode").getStringValue();
			resultMsg = element.element("resultMsg")== null ? "" : element.element("resultMsg").getStringValue();
		}
		Object ob = new JsonResponseUtil().toBeanByJSONLib(retJson);
		try {
			ls = (Map<String, Object>) PropertyUtils.getProperty(ob, "all");
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return ls;
	}
	
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
}
