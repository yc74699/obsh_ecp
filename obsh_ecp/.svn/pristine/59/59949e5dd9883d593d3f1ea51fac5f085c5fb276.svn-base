package com.xwtech.xwecp.service.logic.invocation;
import com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPoint;
import com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPointServiceLocator;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.FeeDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010099Result;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 小区查询信息
 * @author xufan
 * 2014-05-08
 */
public class QryGmisInfoInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QryGmisInfoInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public QryGmisInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY010099Result res = new QRY010099Result();
		res.setResultCode("0"); // 成功
		String reqXml = ""; 	// 发送报文
		String rspXml = ""; 	// 接收报文
		String resp_code = ""; 	// 返回码
		String uptownId = (String)this.getParameters(params, "uptownId");
		String addressId = (String)this.getParameters(params, "addressId");
		try
		{
			final String req = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Header/><SOAP-ENV:Body><GetNetWorkAccess.Request><UptownId>" + uptownId + "</UptownId><AddressId>" + addressId + "</AddressId></GetNetWorkAccess.Request></SOAP-ENV:Body></SOAP-ENV:Envelope>";
			NetWorkAccessPointServiceLocator  npl = new NetWorkAccessPointServiceLocator();
			NetWorkAccessPoint np = (NetWorkAccessPoint)npl.getNetWorkAccessService();
			rspXml = np.getNetWorkAccess(req);
			//强制转码防止中文解析失败
			rspXml = URLDecoder.decode(URLEncoder.encode(rspXml,"utf-8"),"utf-8");
//			rspXml = URLEncoder.encode(rspXml,"utf-8");
//			rspXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Header></SOAP-ENV:Header><SOAP-ENV:Body><GetNetWorkAccess.Response><ResultCode>0</ResultCode><Description>11</Description><Result><UptownId>4475</UptownId><AddressId>514442</AddressId><NetWorkType>0</NetWorkType></Result></GetNetWorkAccess.Response></SOAP-ENV:Body></SOAP-ENV:Envelope>";			
//			rspXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Header></SOAP-ENV:Header><SOAP-ENV:Body><GetNetWorkAccess.Response><ResultCode>1</ResultCode><Description>地址和小区不匹配!</Description><Result><UptownId>4475</UptownId><AddressId>514442</AddressId><NetWorkType></NetWorkType></Result></GetNetWorkAccess.Response></SOAP-ENV:Body></SOAP-ENV:Envelope>";
			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getWebServiceElement(rspXml);
				Element response = (Element)((Element) root.getContent(1)).getContent(0);
				// 获取错误编码
				resp_code = this.config.getChildText(response, "ResultCode");
				
				// 成功
				if ("0".equals(resp_code)) {
					// 取第一层节点
					res.setResultCode(resp_code==null?"1":resp_code); 						// 返回码
					res.setErrorMessage(response.getChildText("Description"));
					res.setAddressId(addressId);//
					res.setUptownId(uptownId);// 

					//取第二层节点
					response = this.config.getElement(response, "Result");
					res.setNetWorkType(response.getChildText("NetWorkType")==null?"":response.getChildText("NetWorkType"));//
				}else
		{
					res.setResultCode("1");
					res.setAddressId(addressId);//
					res.setUptownId(uptownId);// 
					res.setErrorMessage(response.getChildText("Description"));
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		
		return res;
	}
	
	

}