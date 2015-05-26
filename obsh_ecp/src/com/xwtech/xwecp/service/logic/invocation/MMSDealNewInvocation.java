package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DealDetail;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.MmsDealDetail;
import com.xwtech.xwecp.service.logic.pojo.PhonesDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040012Result;
import com.xwtech.xwecp.service.logic.pojo.QRY090012Result;
import com.xwtech.xwecp.service.logic.pojo.TicketsDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 营销方案查询
 * @author yuantao
 *
 */
public class MMSDealNewInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(MMSDealNewInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	private Map map;
	
	public MMSDealNewInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
		if (this.map == null)
		{
			this.map = new HashMap();
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY040012Result res = new QRY040012Result();
		QRY090012Result res1 = new QRY090012Result();
		List<MmsDealDetail> reList = new ArrayList(); 
		RequestParameter r = null;
		
		try
		{
			//增加参数：当前日期 YYYYMM
			r = new RequestParameter();
			r.setParameterName("date");
			r.setParameterValue(new SimpleDateFormat("yyyyMM").format(new Date()));
			params.add(r);
			
			//营销方案查询
			this.MMSDeal(accessId, config, params, reList, res1);
			if (null != reList && reList.size() > 0)
			{
				//月释放信息
				this.queryMarketingPlanDetailInfo(accessId, config, params, reList, res);
				//充值送手机
				this.queryMarketingPlanPhoneInfo(accessId, config, params, reList, res);
				//协议消费
				this.queryMarketingPlanConsumeInfo(accessId, config, params, reList, res);
				//字典信息
				this.getCcGetDictlist(accessId, config, params, reList, res);
				
				
				if (null != this.map && this.map.size() > 0)
				{
					for (MmsDealDetail p : reList)
					{
						if (null != p.getPhonesDetail() && p.getPhonesDetail().size() > 0)
						{
							for (PhonesDetail detail : p.getPhonesDetail())
							{
								//手机品牌
								detail.setPhone2(this.map.get(detail.getPhone2())==null?"":String.valueOf(this.map.get(detail.getPhone2())));
							}
						}
					}
				}
			}
			res.setMmsDealDetail(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	/**
	 * 字典查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param reList
	 */
	public void getCcGetDictlist(String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<MmsDealDetail> reList, QRY040012Result res)
	{
		String reqXml = "";
		String rspXml = "";
		RequestParameter r = null;
		ErrorMapping errDt = null;
		
		try
		{
			r = new RequestParameter();
			r.setParameterName("dict_type");
			r.setParameterValue("618");
			params.add(r);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", params);
			logger.debug(" ====== 字典查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_get_dictlist_78", this.generateCity(params)));
				logger.debug(" ====== 字典查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY040012", "cc_get_dictlist_78", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && "0000".equals(resp_code))
				{
					List dictList = this.getContentList(root, "list_size");
					if (null != dictList && dictList.size() > 0)
					{
						for (int i = 0; i < dictList.size(); i++)
						{
							if (null == this.map.get(this.getChildText((Element)dictList.get(i), "dict_code")))
							{
								this.map.put(
										this.getChildText((Element)dictList.get(i), "dict_code"), 
										this.getChildText((Element)dictList.get(i), "dict_code_desc"));
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 营销方案查询，协议消费
	 * @param accessId
	 * @param config
	 * @param params
	 * @param reList
	 */
	public void queryMarketingPlanConsumeInfo (String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<MmsDealDetail> reList, QRY040012Result res)
	{
		String reqXml = "";
		String rspXml = "";
		RequestParameter r = null;
		ErrorMapping errDt = null;
		
		try
		{
			for (MmsDealDetail pDt : reList)
			{
				boolean serType = true;
				boolean baseInfo = true;
				for (RequestParameter p : params)
				{
					if ("bossmms_services_type".equals(p.getParameterName()))
					{
						serType = false;
						p.setParameterValue("501");
					}
					if ("usermarketingbaseinfo_plan_info_id".equals(p.getParameterName()))
					{
						baseInfo = false;
						p.setParameterValue(pDt.getPlanInfoId());
					}
				}
				if (serType)
				{
					r = new RequestParameter();
					r.setParameterName("bossmms_services_type");
					r.setParameterValue("501");
					params.add(r);
				}
				if (baseInfo)
				{
					r = new RequestParameter();
					r.setParameterName("usermarketingbaseinfo_plan_info_id");
					r.setParameterValue(pDt.getPlanInfoId());
					params.add(r);
				}
				
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdeal_361", params);
				logger.debug(" ====== 协议消费 发送报文 ====== \n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_mmsdeal_361", this.generateCity(params)));
					logger.debug(" ====== 协议消费 接收报文 ====== \n" + rspXml);
				}
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					res.setResultCode("0000".equals(resp_code)?"0":"1");
					if (!"0000".equals(resp_code))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY040012", "cc_mmsdeal_361", resp_code);
						if (null != errDt)
						{
							res.setErrorCode(errDt.getLiErrCode());
							res.setErrorMessage(errDt.getLiErrMsg());
						}
					}
					if (null != resp_code && "0000".equals(resp_code))
					{
						List amountList = this.getContentList(root, "creditamountinfo_plan_info_id");
						if (null != amountList)
						{
							for (int i = 0; i < amountList.size(); i++)
							{
								Element amountDt = this.getElement((Element)amountList.get(i), "ccreditamountinfodt");
								if (null != amountDt)
								{
									logger.debug(" ====== pDt.getPlanInfoId()  ====== " + pDt.getPlanInfoId());
									if (pDt.getPlanInfoId().equals(this.getChildText(amountDt, "creditamountinfo_plan_info_id")))
									{
										//缴纳信用金
										pDt.setCredit1(this.getChildText(amountDt, "creditamountinfo_credit_amount"));
										//可返信用金
										pDt.setCredit2(this.getChildText(amountDt, "creditamountinfo_should_release_amount"));
										//累计消费
										pDt.setCredit3(this.getChildText(amountDt, "creditamountinfo_total_consume_amount"));
									}
								}
							}
							
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 营销方案查询，充值送手机
	 * @param accessId
	 * @param config
	 * @param params
	 * @param reList
	 */
	public void queryMarketingPlanPhoneInfo(String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<MmsDealDetail> reList, QRY040012Result res)
	{
		List<PhonesDetail> pList = null;
		List<TicketsDetail> tList = null;
		PhonesDetail dt = null;
		TicketsDetail tDetail = null;
		String reqXml = "";
		String rspXml = "";
		RequestParameter r = null;
		ErrorMapping errDt = null;
		
		try
		{
			for (MmsDealDetail pDt : reList)
			{
				boolean serType = true;
				boolean baseInfo = true;
				for (RequestParameter p : params)
				{
					if ("bossmms_services_type".equals(p.getParameterName()))
					{
						serType = false;
						p.setParameterValue("301");
					}
					if ("usermarketingbaseinfo_plan_info_id".equals(p.getParameterName()))
					{
						baseInfo = false;
						p.setParameterValue(pDt.getPlanInfoId());
					}
				}
				if (serType)
				{
					r = new RequestParameter();
					r.setParameterName("bossmms_services_type");
					r.setParameterValue("301");
					params.add(r);
				}
				if (baseInfo)
				{
					r = new RequestParameter();
					r.setParameterName("usermarketingbaseinfo_plan_info_id");
					r.setParameterValue(pDt.getPlanInfoId());
					params.add(r);
				}
				
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdeal_361", params);
				logger.debug(" ====== 充值送手机 发送报文 ====== \n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_mmsdeal_361", this.generateCity(params)));
					logger.debug(" ====== 充值送手机 接收报文 ====== \n" + rspXml);
				}
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					res.setResultCode("0000".equals(resp_code)?"0":"1");
					if (!"0000".equals(resp_code))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY040012", "cc_mmsdeal_361", resp_code);
						if (null != errDt)
						{
							res.setErrorCode(errDt.getLiErrCode());
							res.setErrorMessage(errDt.getLiErrMsg());
						}
					}
					
					if (null != resp_code && "0000".equals(resp_code))
					{
						//营销案赠送手机列表
						List infoList = this.getContentList(root, "presentmobileinfo_plan_info_id");
						if (null != infoList && infoList.size() > 0)
						{
							pList = new ArrayList();
							for (int i = 0; i < infoList.size(); i++)
							{
								Element infoDt = this.getElement((Element)infoList.get(i), "cpresentmobileinfodt");
								if (null != infoDt)
								{
									if (pDt.getPlanInfoId().equals(this.getChildText(infoDt, "presentmobileinfo_plan_info_id")))
									{
										dt = new PhonesDetail();
										//定制终端
										dt.setPhone1("1".equals(this.getChildText(infoDt, "monthreleaserecord_release_time"))?"是":"否");
										//手机品牌
										dt.setPhone2(this.getChildText(infoDt, "presentmobileinfo_mobile_brand"));
										//手机型号
										dt.setPhone3(this.getChildText(infoDt, "presentmobileinfo_mobile_type"));
										//差价（元）
										dt.setPhone4(this.getChildText(infoDt, "presentmobileinfo_price_difference"));
										//手机IMEI
										dt.setPhone5(this.getChildText(infoDt, "presentmobileinfo_imei"));
										//领取时间 格式为：YYYYMMDD
										dt.setPhone6(this.getChildText(infoDt, "presentmobileinfo_real_present_time"));
										pList.add(dt);
									}
								}
							}
						}
						//营销案赠送购机券列表
						List amountList = this.getContentList(root, "presentticketinfo_ticket_amount");
						if (null != amountList && amountList.size() > 0)
						{
							tList = new ArrayList();
							for (int i = 0; i < amountList.size(); i++)
							{
								Element ticket = this.getElement((Element)amountList.get(i), "cpresentticketinfodt");
								if (null != ticket)
								{
									if (pDt.getPlanInfoId().equals(this.getChildText(ticket, "presentticketinfo_plan_info_id")))
									{
										tDetail = new TicketsDetail();
										//购机券金额（元）
										tDetail.setTicket1(this.getChildText(ticket, "presentticketinfo_ticket_amount"));
										//领取状态
										tDetail.setTicket2("1".equals(this.getChildText(ticket, "presentticketinfo_present_flag"))?"已领取":"未领取");
										//购机券编号
										tDetail.setTicket3(this.getChildText(ticket, "presentticketinfo_ticket_num"));
										//可领用日期
										tDetail.setTicket4(this.getChildText(ticket, "presentticketinfo_should_present_time"));
										//领取时间
										tDetail.setTicket5(this.getChildText(ticket, "presentticketinfo_real_present_time"));
										tList.add(tDetail);
									}
								}
							}
						}
					}
				}
				pDt.setPhonesDetail(pList);
				pDt.setTicketsDetail(tList);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 月释放信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param reList
	 */
	public void queryMarketingPlanDetailInfo(String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<MmsDealDetail> reList, QRY040012Result res)
	{
		List<DealDetail> dList = null;
		String reqXml = "";
		String rspXml = "";
		RequestParameter r = null;
		DealDetail dt = null;
		ErrorMapping errDt = null;
		
		try
		{
			for (MmsDealDetail pDt : reList)
			{
				boolean serType = true;
				boolean baseInfo = true;
				for (RequestParameter p : params)
				{
					if ("bossmms_services_type".equals(p.getParameterName()))
					{
						serType = false;
						p.setParameterValue("1102");
					}
					if ("usermarketingbaseinfo_plan_info_id".equals(p.getParameterName()))
					{
						baseInfo = false;
						p.setParameterValue(pDt.getPlanInfoId());
					}
				}
				if (serType)
				{
					r = new RequestParameter();
					r.setParameterName("bossmms_services_type");
					r.setParameterValue("1102");
					params.add(r);
				}
				if (baseInfo)
				{
					r = new RequestParameter();
					r.setParameterName("usermarketingbaseinfo_plan_info_id");
					r.setParameterValue(pDt.getPlanInfoId());
					params.add(r);
				}
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdeal_361", params);
				logger.debug(" ====== 月释放信息 发送报文 ====== \n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_mmsdeal_361", this.generateCity(params)));
					logger.debug(" ====== 月释放信息 接收报文 ====== \n" + rspXml);
				}
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					res.setResultCode("0000".equals(resp_code)?"0":"1");
					if (!"0000".equals(resp_code))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY040012", "cc_mmsdeal_361", resp_code);
						if (null != errDt)
						{
							res.setErrorCode(errDt.getLiErrCode());
							res.setErrorMessage(errDt.getLiErrMsg());
						}
					}
					
					if (null != resp_code && "0000".equals(resp_code))
					{
						List infoList = root.getChild("content").getChildren("monthreleaserecord_plan_info_id");
						if (null != infoList && infoList.size() > 0)
						{
							dList = new ArrayList();
							for (int i = 0; i < infoList.size(); i++)
							{
								Element record = ((Element)infoList.get(i)).getChild("cmonthreleaserecorddt");
								
								dt = new DealDetail();
								//释放日期
								dt.setRelaceDate(this.getChildText(record, "monthreleaserecord_release_time"));
								//应释放金额（元）
								dt.setShouldAcc(this.getChildText(record, "monthreleaserecord_should_release_amount"));
								//实际释放金额
								dt.setActAcc(this.getChildText(record, "monthreleaserecord_real_release_amount"));
								//释放状态
								dt.setRelaceState("1".equals(this.getChildText(record, "monthreleaserecord_release_status"))?"成功":"失败");
								//释放原因
								dt.setRelaceReson(this.getChildText(record, "monthreleaserecord_release_reason"));
								//释放描述
								dt.setRelaceDesc(this.getChildText(record, "monthreleaserecord_reason_desc"));
								dList.add(dt);
							}
						}
					}
				}
				pDt.setDealDetail(dList);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 营销方案查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param reList
	 * @param res
	 */
	public void MMSDeal(String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<MmsDealDetail> reList, QRY090012Result res)
	{
		String reqXml = "";
		String rspXml = "";
		MmsDealDetail dt = null;
		RequestParameter r = null;
		ErrorMapping errDt = null;
		
		try
		{
			r = new RequestParameter();
			r.setParameterName("bossmms_services_type");
			r.setParameterValue("101");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("usermarketingbaseinfo_plan_info_id");
			r.setParameterValue("");
			params.add(r);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdeal_new_361", params);
			logger.debug(" ====== 营销管理系统服务 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_mmsdeal_new_361", this.generateCity(params)));
				logger.debug(" ====== 营销管理系统服务 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY09" +
							"0012", "cc_mmsdeal_new_361", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
//				res.setResultCode(resp_code);
//				res.setErrorMessage(resp_desc);
				if (null != res.getResultCode() && "0".equals(res.getResultCode()))
				{
					List userList = this.getContentList(root, "usermarketingbaseinfo_user_id");
					if (null != userList && userList.size() > 0)
					{
						for (int i = 0; i < userList.size(); i++)
						{
							Element base = this.getElement((Element)userList.get(i), "usermarketingbaseinfo");
							if (null != base)
							{
								dt = new MmsDealDetail();

								//营销方案编号
								dt.setPlanId(this.getChildText(base, "usermarketingbaseinfo_plan_id"));
								//申请时间
								dt.setApplyDate(this.getChildText(base, "usermarketingbaseinfo_create_time"));
								//开始日期
								dt.setBeginDate(this.getChildText(base, "usermarketingbaseinfo_inure_date"));
								//结束日期
								dt.setEndDate(this.getChildText(base, "usermarketingbaseinfo_end_date"));
								//usermarketingbaseinfo_plan_info_id
								dt.setPlanInfoId(this.getChildText(base, "usermarketingbaseinfo_plan_info_id"));
								
								reList.add(dt);
							}
						}
					}
					
					if (null != reList && reList.size() > 0)
					{
						List planList = this.getContentList(root, "marketingplan_plan_id");
						if (null != planList && planList.size() > 0)
						{
							String plan_id = "";
							for (int i = 0; i < planList.size(); i++)
							{
								Element planDt = this.getElement((Element)planList.get(i), "cmarketingplandt");
								if (null != planDt)
								{
									plan_id = this.getChildText(planDt, "marketingplan_plan_id");
									
									for (MmsDealDetail pDt : reList)
									{
										if (pDt.getPlanId().equals(plan_id))
										{
											//方案名称
											pDt.setDealName(this.getChildText(planDt, "marketingplan_name"));
											//方案说明
											pDt.setDealDesc(this.getChildText(planDt, "marketingplan_remark"));
											//现金充值
											pDt.setFeeAcc(this.getChildText(planDt, "marketingplan_cash_amount"));
											//新业务充值
											pDt.setNewbizAcc(this.getChildText(planDt, "marketingplan_new_business_amount"));
											//优惠充值
											pDt.setFavAcc(this.getChildText(planDt, "marketingplan_discount_amount"));
										}
									}
								}
							}
						}
					}
					
					
//					List planList = this.getContentList(root, "marketingplan_plan_id");
//					if (null != planList && planList.size() > 0)
//					{
//						for (int i = 0; i < planList.size(); i++)
//						{
//							Element planDt = this.getElement((Element)planList.get(i), "cmarketingplandt");
//							if (null != planDt)
//							{
//								dt = new MmsDealDetail();
//								//营销方案编号
//								dt.setPlanId(this.getChildText(planDt, "marketingplan_plan_id"));
//								//方案名称
//								dt.setDealName(this.getChildText(planDt, "marketingplan_name"));
//								//方案说明
//								dt.setDealDesc(this.getChildText(planDt, "marketingplan_remark"));
//								//现金充值
//								dt.setFeeAcc(this.getChildText(planDt, "marketingplan_cash_amount"));
//								//新业务充值
//								dt.setNewbizAcc(this.getChildText(planDt, "marketingplan_new_business_amount"));
//								//优惠充值
//								dt.setFavAcc(this.getChildText(planDt, "marketingplan_discount_amount"));
//								reList.add(dt);
//							}
//						}
//					}
//					
//					if (null != reList && reList.size() > 0)
//					{
//						List userList = this.getContentList(root, "usermarketingbaseinfo_user_id");
//						if (null != userList && userList.size() > 0)
//						{
//							String plan_id = "";
//							for (int i = 0; i < userList.size(); i++)
//							{
//								Element base = this.getElement((Element)userList.get(i), "usermarketingbaseinfo");
//								if (null != base)
//								{
//									plan_id = this.getChildText(base, "usermarketingbaseinfo_plan_id");
//									for (MmsDealDetail pDt : reList)
//									{
//										if (pDt.getPlanId().equals(plan_id))
//										{
//											//申请时间
//											pDt.setApplyDate(this.getChildText(base, "usermarketingbaseinfo_create_time"));
//											//开始日期
//											pDt.setBeginDate(this.getChildText(base, "usermarketingbaseinfo_inure_date"));
//											//结束日期
//											pDt.setEndDate(this.getChildText(base, "usermarketingbaseinfo_end_date"));
//											//usermarketingbaseinfo_plan_info_id
//											pDt.setPlanInfoId(this.getChildText(base, "usermarketingbaseinfo_plan_info_id"));
//										}
//									}
//								}
//							}
//						}
//					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 获取content下父节点信息
	 * @param root
	 * @param name
	 * @return
	 */
	public List getContentList(Element root, String name)
	{
		List list = null;
		try
		{
			list = root.getChild("content").getChildren(name);
		}
		catch (Exception e)
		{
			list = null;
		}
		return list;
	}
	
	/**
	 * 获取子节点
	 * @param e
	 * @param name
	 * @return
	 */
	public Element getElement(Element e, String name)
	{
		Element dt = null;
		try
		{
			dt = e.getChild(name);
		}
		catch (Exception ex)
		{
			dt = null;
		}
		return dt;
	}
	
	/**
	 * 获取子节点信息
	 * @param e
	 * @param childName
	 * @return
	 */
	public String getChildText(Element e, String childName)
	{
		String str = "";
		
		try
		{
			str = e.getChildText(childName)==null?"":e.getChildText(childName).trim();
		}
		catch(Exception ex)
		{
			//logger.error(ex, ex);
			str = "";
		}
		
		return str;
	}
	
	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	
	public String toDateStr (String date)
	{
		String str = "";
		
		try
		{
			if (null != date && !"".equals(date) && date.length() >= 8)
			{
				str = date.substring(0, 4) + "-" +date.substring(4, 6) + "-" + date.substring(6, 8);
			}
		}
		catch (Exception e)
		{
			str = "";
		}
		
		return str;
	}
}
