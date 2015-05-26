package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.ws.lableInfoClient.BodyReq;
import com.xwtech.xwecp.communication.ws.lableInfoClient.HeaderReq;
import com.xwtech.xwecp.communication.ws.lableInfoClient.IdentityInfo;
import com.xwtech.xwecp.communication.ws.lableInfoClient.QueryLableService;
import com.xwtech.xwecp.communication.ws.lableInfoClient.QueryLableServiceImplService;
import com.xwtech.xwecp.communication.ws.lableInfoClient.ReqData;
import com.xwtech.xwecp.communication.ws.lableInfoClient.RequestObject;
import com.xwtech.xwecp.communication.ws.lableInfoClient.ResponseObject;
import com.xwtech.xwecp.communication.ws.lableInfoClient.User;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.LableResultBean;
import com.xwtech.xwecp.service.logic.pojo.QRY120001Result;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 数据标签查询
 * @author 陈小明
 * 2013-10-16
 */
public class QueryLableInfoInvocation extends BaseInvocation implements ILogicalService 
{
	private static final Logger logger = Logger.getLogger(QueryLableInfoInvocation.class);

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY120001Result res = new QRY120001Result();
		String bossTempate = "nl_queryLable_100";
		setCommonResult(res, "1", "查询失败");
		try {
			//获取数据标签信息，采用有两种方式 1：webservice客户端 、2:通过http+soap，
			//第一种方式 1：webservice客户端，建议采用
			getLableInfo(accessId, params, res, bossTempate);
			
			//第二种:通过http+soap报文
//			getLableInfo2(accessId, params, res, bossTempate);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;

	}

	private void getLableInfo2(String accessId, List<RequestParameter> params,
			QRY120001Result res,  String bossTempate) 
	{
		 String reqXml;
		 String rspXml;
		 try
		 {
			 params.add(new RequestParameter("reqTime", new
			 SimpleDateFormat("yyyyMMddhhmmsssssss").format(new Date())));
			 //组装报文
			 reqXml = mergeReqXML2Boss(params,bossTempate);
			 if (!StringUtil.isNull(reqXml))
			 {
				 //发送请求报文
				 rspXml = sendReqXML2BOSS(accessId, params, reqXml, bossTempate);
				 logger.info("响应报文：----------"+rspXml.replace("&gt;", ">"));
				 if (!StringUtil.isNull(rspXml))
				 {
					logger.info("报文解析..........");
				    Document xmldoc=DocumentHelper.parseText(rspXml.replace("&gt;", ">"));
				    Element root=xmldoc.getRootElement();
				    List<Element> list=root.elements("Body");
				    Element ele = recursList(list);
				    ele = getContent(ele);
				    if(null != ele)
					{
//					    setResult4Ele(res, ele);
					}
				 }
			 }
		 }catch(Exception e)
		 {
			 logger.info(e);
			 setCommonResult(res, "1", "数据异常");
		 }
		
	}

//	private void setResult4Ele(QRY120001Result res, Element ele) 
//	{
//		res.setR_call_prefer(ele.elementText("r_call_prefer"));
//		res.setArea_id(ele.elementText("area_id"));
//		res.setFlow_free_percent(ele.elementText("flow_free_percent"));
//		res.setPer_free_call(ele.elementText("per_free_call"));
//		res.setStat_date(ele.elementText("stat_date"));
//		res.setRoam_call_dur(ele.elementText("roam_call_dur"));
////		res.setRelation_type(ele.elementText("relation_type"));
//		res.setMechine_brand(ele.elementText("mechine_brand"));
//		res.setUser_id(ele.elementText("user_id"));
//		res.setTwomodes_type_socre(ele.elementText("twomodes_type_socre"));
////		res.setIs_kd_user(ele.elementText("is_kd_user"));
//		res.setHob_net(ele.elementText("hob_net"));
//		res.setPrice_desc(ele.elementText("price_desc"));
//		res.setIs_caption(ele.elementText("is_caption"));
//		res.setMarket_type(ele.elementText("market_type"));
//		res.setMsisdn(ele.elementText("msisdn"));
//		res.setIs_sms_pot(ele.elementText("is_sms_pot"));
//		res.setPer_busy_call(ele.elementText("per_busy_call"));
//		res.setIs_many_m(ele.elementText("is_many_m"));
//		res.setPer_pack_saturation2(ele.elementText("per_pack_saturation2"));
//		res.setIs_priv_type_reward(ele.elementText("is_priv_type_reward"));
//		res.setL_call_prefer(ele.elementText("l_call_prefer"));
//		res.setAgent_type(ele.elementText("agent_type"));
//		res.setLocal_call_dur(ele.elementText("local_call_dur"));
//		res.setPer_pack_saturation(ele.elementText("per_pack_saturation"));
//		res.setIs_priv_type_fee(ele.elementText("is_priv_type_fee"));
//		res.setIs_home(ele.elementText("is_home"));
//		setCommonResult(res, "0000", "成功");
//	}

	private static Element getContent(Element ele)
	{
		if(null != ele && "respObjectString".equals(ele.getName()))
		{
		    ele = (Element) ele.elements().get(0);
		}
		if(null != ele && "content".equals(ele.getName()))
		{
			ele = (Element) ele.elements().get(0);
			return ele;
		}
		return null;
	}

	private void getLableInfo(String accessId, List<RequestParameter> params,
			QRY120001Result res, String bossTempate)
			throws CommunicateException, Exception 
		{
			String phoneNum = (String) getParameters(params, "phoneNum");
			
			QueryLableServiceImplService serialServerImplServic=new QueryLableServiceImplService();
			QueryLableService serialServer=serialServerImplServic.getQueryLableServiceImplPort();


			RequestObject requestObject=new RequestObject();
			HeaderReq headerReq=new HeaderReq();
			com.xwtech.xwecp.communication.ws.lableInfoClient.System system=new  com.xwtech.xwecp.communication.ws.lableInfoClient.System();
			system.setReqSource("59190013");
			headerReq.setSystem(system);
			User user=new User();
			
			IdentityInfo idennIdentityInfo = new IdentityInfo();
			// 设置服务鉴权字段：服务标识+对端系统标识组合
			idennIdentityInfo.setClientID("lableService,59190013");
			// 设置对端系统访问密码
			idennIdentityInfo.setPassWord("999623");

			
			
			user.setIdentityInfo(idennIdentityInfo);
			headerReq.setUser(user);
			requestObject.setHeaderReq(headerReq);
			BodyReq bodyReq=new BodyReq();
			ReqData reqData=new ReqData();
			reqData.setReqObjectString("<content>"
					+ "<phoneNum>"+phoneNum+"</phoneNum>" + //  数据时间serviceId
					"</content>"); 
			bodyReq.setReqData(reqData);
			requestObject.setBodyReq(bodyReq);
			ResponseObject responseObject=serialServer.queryLable(requestObject);
			String resp = responseObject.getBodyResp().getRespData().getRespObjectString();
			logger.info("响应报文: "+resp);
			List<LableResultBean> lableList = responseObject.parseToBean(LableResultBean.class);
			res.setLableList(lableList);
			setCommonResult(res, "0000", "成功");
			
//			//创建服务对象
//			QueryLableServiceImplService serialServerImplServic=new QueryLableServiceImplService();
//			
//			//获取服务接口
//			QueryLableService serialServer=serialServerImplServic.getQueryLableServiceImplPort();
//			
//			//创建服务调用内容参数的bean
//			RequestObject requestObject=new RequestObject();
//			
//			HeaderReq headerReq=new HeaderReq();
//			
//			//设置对端系统编号
//			com.xwtech.xwecp.communication.ws.lableInfoClient.System system=new com.xwtech.xwecp.communication.ws.lableInfoClient.System();
//			system.setReqSource("59190013");
//			headerReq.setSystem(system);
//			
//			User user=new User();
//			IdentityInfo idennIdentityInfo = new IdentityInfo();
//			// 设置服务鉴权字段：服务标识+对端系统标识组合
//			idennIdentityInfo.setClientID("lableService,59190013");
//			// 设置对端系统访问密码
//			idennIdentityInfo.setPassWord("999623");
//			
//			user.setIdentityInfo(idennIdentityInfo);
//			headerReq.setUser(user);
//			
//			requestObject.setHeaderReq(headerReq);
//			
//			BodyReq bodyReq=new BodyReq();
//			ReqData reqData=new ReqData();
//			
//			reqData.setReqObjectString("<content><phoneNum>"+phoneNum+"</phoneNum></content>"); 
//			bodyReq.setReqData(reqData);
//			
//			requestObject.setBodyReq(bodyReq);
//			
//			ResponseObject responseObject = serialServer.queryLable(requestObject);
//			
//			String resp = responseObject.getBodyResp().getRespData().getRespObjectString();
//			logger.info("响应报文: "+resp);
//			
//			List<LableResultBean> lableList = responseObject.parseToBean(LableResultBean.class);
//			res.setLableList(lableList);
//			setCommonResult(res, "0000", "成功");
//			//服务返回的内容
//			if(null != responseObject && responseObject.getBodyResp().getRespData() != null)
//			{
//				setResult(res,responseObject);
//			}
		}

	
	private void setResult(QRY120001Result res, ResponseObject responseObject) 
	{
		String resp = responseObject.getBodyResp().getRespData().getRespObjectString();
		logger.info("响应报文: "+resp);
		try {
			Document xmldoc = DocumentHelper.parseText(resp);
		    Element root = xmldoc.getRootElement();
		    List<Element> list = root.elements();
		    if(null != list && list.size() > 0)
		    {
		    	 Element ele = list.get(0);
		    	 if(null != ele)
				 {
//				    setResult4Ele(res, ele);
				 }
		    }
		   
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 递归查询节点为respObjectString
	 */
	private static Element recursList(List list)
	{
		Element ele = null;
		List nextList = null;
		for(int i=0; i<list.size();i++)
		{
		    ele=(Element)list.get(i);
		    nextList = ele.elements();
		    if("respObjectString".equals(ele.getName()))
		    {
		    	return ele;
		    }
		}
		return recursList(nextList);
	}
	
	private static String readFromFile(String f) throws Exception
	{
		FileInputStream fis = new FileInputStream(new File(f));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[10240];
		int nRead = 0;
		while((nRead = fis.read(buf)) > 0)
		{
			baos.write(buf, 0, nRead);
			baos.flush();
		}
		String response = new String(baos.toByteArray(),"GBK");
		baos.close();
		return response;
	}
}