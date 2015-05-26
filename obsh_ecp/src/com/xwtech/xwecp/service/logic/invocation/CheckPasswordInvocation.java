package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040003Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DESEncrypt;

/**
 * QRY040003_密码校验
 * @author Tkk
 *
 */
public class CheckPasswordInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(CheckPasswordInvocation.class);
	
	/**
	 * 手机号
	 */
	private static final String PHONE_NUM = "phoneNum";
	
	/**
	 * 手机密码
	 */
	private static final String PASSWORD = "password";
	
	/**
	 * 请求Boss的接口
	 */
	private static final String REQUEST_BOSSTELETEXT = "cc_chkpass_61";
	
	private static final String META_QRY040003 = "QRY040003";
	
	/**
	 * 新大陆提供的密钥，需要每两位转成1个字节
	 */
	private static byte[] BOSS_SECRET_KEY = {
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3,0x4e,
		(byte)0xdd,(byte)0x3b,(byte)0x51,0x24,0x36,(byte)0xa8,(byte)0x28,
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3	
	};
	
	private BossTeletextUtil bossTeletextUtil = null;
	
	private IRemote remote = null;
	
	private WellFormedDAO wellFormedDAO = null;
	
	public CheckPasswordInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY040003Result result = new QRY040003Result();
		result.setResultCode(LOGIC_SUCESS);
		result.setErrorMessage("");
		for(RequestParameter param : params)
		{
			//如果为密码
			if(PASSWORD.equals(param.getParameterName()))
			{
				//需要加密
				String password = (String) param.getParameterValue();
				password = DESEncrypt.desString(password, BOSS_SECRET_KEY);
				param.setParameterValue(password);
			}
		}
		String reqXml = bossTeletextUtil.mergeTeletext(REQUEST_BOSSTELETEXT, params);
		try {
			String rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, REQUEST_BOSSTELETEXT, this.generateCity(params)));
			//解析boss返回的报文
			parseResponseXml(rspXml, result);
		} catch (CommunicateException e) {
			logger.error(e, e);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}
	
	/**
	 * 解析boss返回的报文
	 * @param rspXml
	 * @return 
	 */
	private void parseResponseXml(String rspXml, QRY040003Result result) {
		ByteArrayInputStream ins = new ByteArrayInputStream(rspXml.getBytes());
		SAXBuilder sax = new SAXBuilder();
		Document doc;
		try 
		{
			doc = sax.build(ins);
			Element root = doc.getRootElement();
			//错误码
			String errCode = root.getChild("response").getChildText("resp_code");
			//错误说明
			String errDesc = root.getChild("response").getChildText("resp_desc");
			
			ErrorMapping errDt = this.wellFormedDAO.transBossErrCode(META_QRY040003, REQUEST_BOSSTELETEXT, errCode);
			if (null != errDt)
			{
				errCode = errDt.getLiErrCode();
				errDesc = errDt.getLiErrMsg();
			}
			result.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
			result.setErrorCode(errCode);
			result.setErrorMessage(errDesc);
		}
		catch (Exception e) 
		{
			logger.error(e.getMessage());
		}
	}

}
