package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.ws.gsmClient.BodyReq;
import com.xwtech.xwecp.communication.ws.gsmClient.HeaderReq;
import com.xwtech.xwecp.communication.ws.gsmClient.IdentityInfo;
import com.xwtech.xwecp.communication.ws.gsmClient.ReqData;
import com.xwtech.xwecp.communication.ws.gsmClient.RequestObject;
import com.xwtech.xwecp.communication.ws.gsmClient.ResponseObject;
import com.xwtech.xwecp.communication.ws.gsmClient.SerialServer;
import com.xwtech.xwecp.communication.ws.gsmClient.SerialServerImplService;
import com.xwtech.xwecp.communication.ws.gsmClient.User;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY120002Result;
import com.xwtech.xwecp.service.logic.pojo.ServiceSerialBean;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 紧密交友圈接口
 * @author 陈小明
 * 2013-11-28
 */
public class QueryGSMInvocation extends BaseInvocation implements ILogicalService 
{
	private static final Logger logger = Logger.getLogger(QueryGSMInvocation.class);

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY120002Result res = new QRY120002Result();
		setCommonResult(res, "1", "查询失败");
		try {
			getGSMInfo(accessId, params, res);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	private void getGSMInfo(String accessId, List<RequestParameter> params,
			QRY120002Result res)
			throws CommunicateException, Exception 
		{
			String phoneNum = (String) getParameters(params, "phoneNum");
			String month = String.valueOf(getMonth());
			
			ServiceSerialBean requestInfo = new ServiceSerialBean();
			requestInfo.setMsisdn(phoneNum);
			requestInfo.setMonth_number(month);
			String xml = requestInfo.toXml();

			SerialServerImplService webService = new SerialServerImplService();
			SerialServer server = webService.getSerialServerImplPort();

			RequestObject requestObject = new RequestObject();
			HeaderReq headerReq = new HeaderReq();
			User user = new User();
			//设置对端系统编号
			com.xwtech.xwecp.communication.ws.gsmClient.System system=new com.xwtech.xwecp.communication.ws.gsmClient.System();
			system.setReqSource("59190013");
			headerReq.setSystem(system);
			
			IdentityInfo idennIdentityInfo = new IdentityInfo();
			// 设置服务鉴权字段：服务标识+对端系统标识组合
			idennIdentityInfo.setClientID("SerialService,59190013");
			// 设置对端系统访问密码
			idennIdentityInfo.setPassWord("460279");
			
			user.setIdentityInfo(idennIdentityInfo);
			headerReq.setUser(user);
			
			requestObject.setHeaderReq(headerReq);
			
			BodyReq bodyReq=new BodyReq();
			ReqData reqData=new ReqData();
			
			reqData.setReqObjectString(xml); 
			bodyReq.setReqData(reqData);
			
			requestObject.setBodyReq(bodyReq);
			
			ResponseObject gsmResponse = null;
			ResponseObject smsResponse = null;
			List<ServiceSerialBean> gsmList = new ArrayList<ServiceSerialBean>();
			List<ServiceSerialBean> smsList = new ArrayList<ServiceSerialBean>();

			gsmResponse = server.queryGsm(requestObject);
			gsmList = gsmResponse.parseToBean(ServiceSerialBean.class);
			
			smsResponse = server.querySms(requestObject);
			smsList = smsResponse.parseToBean(ServiceSerialBean.class);
			
			res.setGsmList(gsmList);
			res.setSmsList(smsList);
			setCommonResult(res, "0000", "查询成功");
		}
	
	private int getMonth() {
		String today = DateTimeUtil.getTodayDay();
		String month = DateTimeUtil.getTodayMonth();
		int intToday = Integer.parseInt(today);
		int intMonth = Integer.parseInt(month);
		intMonth = intMonth == 1 ? 13 : intMonth;
		if(intToday >= 1 && intToday <= 15)
		{
			return intMonth - 2;
		}
		if(intToday >= 11 && intToday <= 31)
		{
			return intMonth - 1;
		}
		return intMonth;
	}
}