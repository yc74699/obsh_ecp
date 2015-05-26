package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.IReserveDAO;
import com.xwtech.xwecp.dao.SmsDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.OrderInitForm;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.RES003Result;
import com.xwtech.xwecp.service.logic.pojo.ReserveOfficeInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 生成订单（购买、撤销订单）服务
 * @author yg
 */
public class DoReservationInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(DoReservationInvocation.class);
	private IReserveDAO reserveDAO;
	private SmsDao smsDao;
	
	public DoReservationInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.reserveDAO = (IReserveDAO) (springCtx.getBean("reserveDAO"));
		this.smsDao = (SmsDao)(springCtx.getBean("smsDao"));
	
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
	
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		RES003Result res = new RES003Result();
		//下发短信内容
		String smsContent = "";
		
		try {
			String mobile = (String) getParameters(params, "mobile");
			String busiNum = (String) getParameters(params, "busiNum");
			String operType = (String) getParameters(params, "operType");
			String busiType = (String) getParameters(params, "busiType");
			String officeId = (String) getParameters(params, "officeId");
			String expectTime = (String) getParameters(params, "expertTime");
			String expectPeriod = (String) getParameters(params, "expectPeriod");
			String resBz = (String) getParameters(params, "resBz");
			
			//构建OrderInitForm
			OrderInitForm __form = new OrderInitForm();
			__form.setOrderMobile(mobile);
			__form.setBusiNum(busiNum);
			__form.setOperType(operType);
			__form.setBusiType(busiType);
			__form.setOfficeId(officeId);
			__form.setExpectTime(expectTime);
			__form.setExpectPeriod(expectPeriod);
			__form.setCityCode((String) this.getParameters(params, "context_ddr_city"));
			__form.setBrandCode((String) this.getParameters(params, "context_brand"));
			__form.setOrderChannel((String) this.getParameters(params, "context_channel"));
			__form.setResBz(resBz);
			
			
			//短信内容
			//预计到厅时间
			String formatExpectTime = DateTimeUtil.fromChar8ToStandard(expectTime);
			String fmtexpectPeriod = (StringUtils.isBlank(expectPeriod) || "3".equals(expectPeriod))? "":("1".equals(expectPeriod)?"上午":"下午");
			
			if("1".equals(operType))  //操作类型 //业务
			{
				String orderId = ""; //用于返回订单号
				if("1".equals(busiType))	//业务
				{
					//业务订单信息
					String busiName = reserveDAO.getBusiName(busiNum);
					
					//如果该业务在档案表里存在,插入业务订单表
					if(!"".equals(busiName))
					{
						
						__form.setBusiName(busiName);
						orderId = reserveDAO.addBusiOrder(__form);
						//成功下发短信
						res.setErrorMessage("成功预约业务");
						res.setResultCode(LOGIC_SUCESS);
						res.setOrderId(orderId);
					
						if(res.getResultCode().equals("0")&& null!=orderId && ""!=orderId){
							this.setParameter(params, "busiName", busiName);
							this.setParameter(params, "orderId", orderId);
							this.setParameter(params, "orderTime", DateTimeUtil.getTodayChar14());
							//执行数据同步
							excuteDataCopy( accessId,  config,
									 params,  res);
							if(!res.getResultCode().equals(BOSS_SUCCESS)){
								res.setResultCode(LOGIC_SUCESS);
							}
						}
						
						smsContent = "尊敬的中国移动江苏客户，您好！您已成功预约开通" + busiName + "业务，"
						+"预约号：" + orderId + "，我们将在24小时内为您办理。"; 
						
						int smsflag = smsDao.sendSms(mobile, smsContent, "10086", "free");
					}else{
						res.setErrorMessage("获取业务信息错误，提交订单失败");
						res.setResultCode(LOGIC_ERROR);
					}
				}else if("2".equals(busiType)) { //预约营销案
					//营销案订单信息
					String marketName = reserveDAO.getMarketName(busiNum);
					if(!"".equals(marketName))
					{
						__form.setMarketName(marketName);
						orderId = reserveDAO.addMarketOrder(__form);
						String officeName = "";
						String officeAddress = "";
						String officeTel = "";
						if(!StringUtils.isBlank(orderId)){ //插入表成功,取营业厅名称
							ReserveOfficeInfo officeInfo  = reserveDAO.getReserveOfficeInfo(officeId);
							officeName = officeInfo == null ? "" : officeInfo.getOfficeName() ;
							officeAddress = officeInfo == null ? "" : officeInfo.getOfficeAddress() ;
							officeTel = officeInfo == null ? "" : officeInfo.getOfficeTel() ;
							
						}
						
						res.setOrderId(orderId);
						res.setResultCode(LOGIC_SUCESS);
						res.setErrorMessage("成功预约营销案");
						if(res.getResultCode().equals("0")&& null!=orderId && ""!=orderId){
							this.setParameter(params, "busiName", marketName);
							this.setParameter(params, "orderId", orderId);
							this.setParameter(params, "orderTime", DateTimeUtil.getTodayChar14());
							//执行数据同步
							excuteDataCopy( accessId,  config,
									 params,  res);
							if(!res.getResultCode().equals(BOSS_SUCCESS)){
								res.setResultCode(LOGIC_SUCESS);
							}
						}
						//下发短信
						if(StringUtils.isBlank(officeId)){ //不是到厅办理
							smsContent = "尊敬的中国移动江苏客户，您好！您已成功预约办理 " + marketName + "，"+"预约号：" + orderId + "，我们将在24小时内为您办理。"; 
						}else{				//到厅办理
							smsContent = genSMSContent(formatExpectTime,
									fmtexpectPeriod, orderId, marketName,
									officeName,1); 
						}
						int smsflag = smsDao.sendSms(mobile, smsContent, "10086", "free");
						//发送营业厅信息短信
					    smsflag = smsDao.sendSms(mobile, getYYTSmsContent(officeName,officeAddress,officeTel), "10086", "free");
					}else
					{
						res.setErrorMessage("获取营销案信息错误，提交订单失败");
						res.setResultCode(LOGIC_ERROR);
					}
				}
			}else if("2".equals(operType)){  //取消订单
				if("1".equals(busiType))	//取消业务订单
				{
					//删除业务订单信息
					reserveDAO.cancelBusiName(busiNum,mobile);
					res.setResultCode(LOGIC_SUCESS);
					res.setErrorMessage("成功取消业务订单");
				}else if("2".equals(busiType))  //取消营销案订单 --暂时不支持
				{
					//删除营销案订单信息
					reserveDAO.cancelMarketName(busiNum,mobile);
					res.setResultCode(LOGIC_SUCESS);
					res.setErrorMessage("成功取消营销案订单");
				}
			}
			
		}
		catch (Exception e) {
			logger.error("数据库失败");
			e.printStackTrace();
			res.setResultCode(LOGIC_ERROR);
			res.setErrorMessage("数据库失败");
		
			return res;
		}
		
		return res;
	}
	
	public void excuteDataCopy(String accessId, ServiceConfig config,
			List<RequestParameter> params, RES003Result res) {

		String reqXml = "";
		String rspXml = "";
	
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(
					"cc_destineinfo", params);
			logger.info(" ====== 发送报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,
					accessId, "cc_destineinfo", this
							.generateCity(params)));
			logger.info(" ====== 返回报文 ======\n" + rspXml);
			if(null != rspXml && !"".equals(rspXml)){
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
			}
		 catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	private String getYYTSmsContent(String officeName, String officeAddress,
			String officeTel)
	{
		String content = "";
		content = officeName+"办理："+officeAddress+",联系电话"+officeTel+"，欢迎您前来办理!";
		return content;
	}
}


