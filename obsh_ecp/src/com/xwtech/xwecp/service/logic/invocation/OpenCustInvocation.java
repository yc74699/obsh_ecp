package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL030003Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040028Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 夹寄卡开户
 * 
 * @author Tkk
 * 
 */
public class OpenCustInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(OpenCustInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	/**
	 * 夹寄卡客户开户
	 */
	private static final String BOSS_INTERFACE_OPEN_CUST = "cc_copenpcust_251";
	
	/**
	 * 客户资料补录
	 */
	private static final String BOSS_INTERFACE_CUST_FULL = "cc_pcustfull_250";

	/**
	 * 未开户BOSS代码
	 */
	private static final String NO_OPEN_CUST_BOSS_CODE = "-2027";
	
	/**
	 * 华为未开户BOSS代码
	 */
	private static final String HW_NO_OPEN_CUST_BOSS_CODE = "-1";
	
	public OpenCustInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		DEL030003Result result = new DEL030003Result();
		String rspXml = null;
		
		try {
			//1.根据证件号码查询是否开户过
			ServiceLocator sl = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
			ServiceInfo si = sl.locate("QRY040028", params);
			QRY040028Result res = (QRY040028Result) si.getServiceInstance().execute(accessId);

			//2.是否开户
			String custId = null;
			if(NO_OPEN_CUST_BOSS_CODE.equals(res.getErrorCode()) || HW_NO_OPEN_CUST_BOSS_CODE.equals(res.getErrorCode()))
			{
				//未开户,则开户
				rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(BOSS_INTERFACE_OPEN_CUST, params), accessId, BOSS_INTERFACE_OPEN_CUST, super.generateCity(params)));
				Document doc = DocumentHelper.parseText(rspXml);
				boolean isSuccess = setResultCode(result, doc, BOSS_INTERFACE_OPEN_CUST);
				
				if(isSuccess)
				{
					custId = doc.selectSingleNode("/operation_out/content/cust_id").getText().trim();
				}
				else
				{
					return result;
				}
			}
			else
			{
				//已开户,更新客户资料
				custId = res.getCustId();
				
				//验证用户输入的信息和接口返回的信息是否一致
				String bossUserName = res.getCustName();
				String userName = (String) this.getParameters(params, "custName");
				if(bossUserName != null && !bossUserName.equals(userName))
				{
					result.setResultCode(LOGIC_ERROR);
					result.setErrorCode("-10000");
					result.setErrorMessage("用户名不一致");
					return result;
				}
			}
			
			//3.客户资料补录
			this.setParameter(params, "custId", custId);
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(BOSS_INTERFACE_CUST_FULL, params), accessId, BOSS_INTERFACE_CUST_FULL, super.generateCity(params)));
			Document doc = DocumentHelper.parseText(rspXml);
			if(setResultCode(result, doc, BOSS_INTERFACE_OPEN_CUST))
			{
				
				String phone = (String)getParameters(params,"phoneNum");
				//增加删除接口QRY040001的数据。
				XWECPApp.redisCli.del(phone+"_QRY040001");
				//增加渠道参数
				String channel = (String)getParameters(params,"channel");
				//增加地市字段
				String cityNum = (String)getParameters(params,"context_ddr_city");
				
				//增加空判断，获取其它的值
				if(null == cityNum || "".equals(cityNum))
				{
					cityNum = (String)getParameters(params,"fixed_ddr_city");
				}
				if("obsh_channel".equals(channel))
				{
					channel = "1";
				}
				else if("sms_channel".equals(channel))
				{
					channel = "3";
				}
				else if("wap_channel".equals(channel))
				{
					channel = "2";
				}
				else
				{
					channel = "4";
				}
				//用于记录是否有数据库报错
				String error = "false";
				if(Integer.parseInt(DateTimeUtil.getTodayChar8()) < 20140915)
				{
					try{
						boolean rest = wellFormedDAO.insertGXYX(phone,DateTimeUtil.getTodayChar17(),channel,cityNum);
						//数据插入活动组数据库报错或者失败，都记录到本地数据库中
						if(!rest)
						{
							error = "true";
						}
						result.setErrorCode(result.getErrorCode()+" huodon "+rest);
					}
					catch(Exception e)
					{
						error = e.getClass().toString();
						result.setErrorMessage(error);
					}
				}
				String name = (String)getParameters(params,"custName");
				String card = (String)getParameters(params,"cardNum");
				String addr = (String)getParameters(params,"addr");

				boolean res2 = wellFormedDAO.insertUserIcard(phone,name,card,addr+"   "+error);
				
				result.setErrorCode(result.getErrorCode()+" "+res2);
			}
		} 
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return result;
	}
	
	/**
	 * 设置错误码
	 * 
	 * @param result
	 * @param doc
	 * @param bossTemplate
	 */
	private boolean setResultCode(BaseServiceInvocationResult result, Document doc, String bossTemplate) {
		boolean validate = true;

		// 结果码
		String errCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();

		// 返回信息
		String errDesc = doc.selectSingleNode("/operation_out/response/resp_desc").getText();

		// 转换本地化结果信息
		ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL030003", bossTemplate, errCode);
		
		if (null != errDt) 
		{
			errCode = errDt.getLiErrCode();
			errDesc = errDt.getLiErrMsg();
		}
		result.setErrorCode(errCode);
		result.setErrorMessage(errDesc);
		result.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
		
		if(!BOSS_SUCCESS.equals(errCode))
		{
			validate = false;
		}
		return validate;
	}
}