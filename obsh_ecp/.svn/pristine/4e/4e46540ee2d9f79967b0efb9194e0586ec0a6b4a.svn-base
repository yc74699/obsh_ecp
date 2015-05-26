package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
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
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;


/**
 * 积分M值扣减
 * 2012-09-06
 * @author chenxiaoming_1037
 * 
 */
public class ScoreChgInvocation extends BaseInvocation implements ILogicalService   
{
	
	private static final Logger logger = Logger.getLogger(ScoreChgInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	
	private static final String GB_BOSS_INTERFACE_SCORECHG = "cc_cscorechg_361";
	
	private ParseXmlConfig config;

	public ScoreChgInvocation() 
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params)
	{
		DEL010001Result result = new DEL010001Result();
		result.setResultCode(LOGIC_ERROR);
		//int optType = Integer.parseInt(getParameters(params,"oprType").toString());
		RequestParameter paramMonth = new RequestParameter("currentDate",DateTimeUtil.getTodayChar8());
		params.add(paramMonth);
		//接口要求传入流水号  由渠道自己生成，生成一个
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String workfid = this.generateOrderId(sdf.format(new Date()),"2");
		params.add(new RequestParameter("workfid",workfid));
		
		scoreChg(GB_BOSS_INTERFACE_SCORECHG,params, accessId,result);
			
	
		return result;
	}
	
	private void setErrResult(DEL010001Result res) 
	{
		res.setResultCode(LOGIC_ERROR);
		res.setErrorCode("-20001");
		res.setErrorMessage("操作类型不正确，应为1或2！");
	}

	private void scoreChg(String bossTemplate, List<RequestParameter> params,
			String accessId, DEL010001Result res)
	{
		try
		{
			String rspXml = (String) this.remote.callRemote(new StringTeletext(
					this.bossTeletextUtil.mergeTeletext(bossTemplate, params),
					accessId, bossTemplate, this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				setResResult(res, rspXml);
			}
			else
			{
				setErrResult(res);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			setErrResult(res);
		}
	}


	private void setResResult(DEL010001Result res, String rspXml) {
		Element root = this.config.getElement(rspXml);
		String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
		String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
		res.setResultCode("0".equals(resp_code)?"0":"1");
		res.setErrorCode(resp_code);
		res.setErrorMessage(resp_desc);
	}
	
	/**
	 * 生成20位订单号
	 */
	private String generateOrderId(String strDateTime,String type){
		String orderId = null;
		Random r = new Random(); 
		Double d = r.nextDouble(); 
		String s = d + ""; 
		if(StringUtils.isNotBlank(s) && StringUtils.isNotBlank(type) && StringUtils.isNotBlank(strDateTime)){
			s = s.substring(3,5); 
			orderId = strDateTime + type + s;
		}
		return orderId;
		
	}
}
