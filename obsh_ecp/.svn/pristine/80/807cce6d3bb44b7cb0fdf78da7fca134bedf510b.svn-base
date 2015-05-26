package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

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
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL100003Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 商城活动-实现出货、退货、换货功能
 * @author YG
 * 
 */
public class TerminalSaleChangeInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(TerminalSaleChangeInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
//	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public TerminalSaleChangeInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL100003Result res = new DEL100003Result();

		try
		{
			//res.setResultCode(LOGIC_SUCESS);
			//res.setErrorMessage("");
			//功能类型
			String functionType = (String)getParameters(params, "functionType"); //1出货或退货功能     ;2换货功能
			//父订单号
			String paorderid  = (String)getParameters(params, "paorderid"); // 出货退货操作（functionType =1）：父订单号  ;换货（functionType =2）：空
			//子订单号
//			String orderid  = (String)getParameters(params, "orderid"); 
			//终端品牌
//			String terminalBrand  = (String)getParameters(params, "terminalBrand"); 
			//终端型号
//			String terminalType  = (String)getParameters(params, "terminalType"); 
			//IMEI1
//			String imei1 = (String)getParameters(params, "imei1"); //出货退货操作（functionType =1）：终端IMEI  换货（functionType =2）：换前IMEI
			//IMEI2
//			String imei2 = (String)getParameters(params, "imei2"); // 换后IMEI
			//操作类型
			String opertype = (String)getParameters(params, "opertype"); //出货退货操作（functionType =1）如下定义： OUTSTORE 出货 BACKGOODS 退货  换货（functionType =2）：空
			//销售价格
			String saleprice = (String)getParameters(params, "saleprice"); //出货退货操作（functionType =1）：销售价格 换货（functionType =2）：空
				
			
			
			
			BaseResult terminalSaleRet = null;
					
			//1-	出货或退货功能
			if ("1".equals(functionType)) {
				//传入参数校验
				if(StringUtils.isBlank(paorderid) || StringUtils.isBlank(opertype) || StringUtils.isBlank(saleprice)){
					res.setResultCode(LOGIC_PARAM_ERROR);
					return res;
				}
				//TODO 其他操作
				terminalSaleRet = this.doTerminalSale(accessId, config, params);
				if (!LOGIC_SUCESS.equals(terminalSaleRet.getResultCode())) {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(terminalSaleRet.getErrorCode());
					res.setErrorMessage(terminalSaleRet.getErrorMessage());
				}else{
					res.setResultCode(terminalSaleRet.getResultCode());
					res.setErrorCode(terminalSaleRet.getErrorCode());
					res.setErrorMessage(terminalSaleRet.getErrorMessage());
					
				}
				
			} else { //2-	换货功能
				terminalSaleRet = this.doTerminalChange(accessId, config, params);
				if (!LOGIC_SUCESS.equals(terminalSaleRet.getResultCode())) {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(terminalSaleRet.getErrorCode());
					res.setErrorMessage(terminalSaleRet.getErrorMessage());
				}else{
					res.setResultCode(terminalSaleRet.getResultCode());
					res.setErrorCode(terminalSaleRet.getErrorCode());
					res.setErrorMessage(terminalSaleRet.getErrorMessage());
					
				}
				
				
			}
			
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 出货或退货功能
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult doTerminalSale(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_netterminalsale_1003", params);

			logger.debug(" ====== 出货、退货接口请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_netterminalsale_1003", this.generateCity(params)));
			logger.debug(" ====== 出货、退货接口 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
//				String errCode = root.getChild("content").getChildText("ret_code");
//				String errDesc = root.getChild("content").getChildText("ret_msg");
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL010003", "cc_netterminalsale_1003", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 出货或退货功能
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult doTerminalChange(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_changenetterminal_1004", params);

			logger.debug(" ====== 换货接口 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_changenetterminal_1004", this.generateCity(params)));
			logger.debug(" ====== 换货接口 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL010003", "cc_changenetterminal_1004", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
}
