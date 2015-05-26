package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品增值业务列表查询
 * cc_cgetincinfo
 * @author yuantao
 *
 */
public class GetIncInfoInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(GetIncInfoInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Map<String,String> map;
	
	public GetIncInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
		if (null == this.map)
		{
			map = new HashMap<String,String>();
			this.map.put("5000", "短信呼");
			this.map.put("5001", "短信话费提醒(原)");
			this.map.put("5002", "无线终端");
			this.map.put("5003", "盐城特预卡");
			this.map.put("5004", "手机证券");
			this.map.put("5005", "集团GPRS");
			this.map.put("5006", "彩铃");
			this.map.put("5007", "双号传真");
			this.map.put("5008", "双号数据");
			this.map.put("5010", "短信帐单");
			this.map.put("5011", "纸帐单");
			this.map.put("5012", "理财助手(话费易)");
			this.map.put("5013", "动感易");
			this.map.put("5014", "积分短信申请");
			this.map.put("5015", "套餐易");
			this.map.put("5016", "呼转特定号码");
			this.map.put("5017", "生日祝福");
			this.map.put("5018", "积分申请");
			this.map.put("5019", "动感M值计划");
			this.map.put("5020", "彩音");
			this.map.put("5021", "亲情易");
			this.map.put("5022", "被叫易");
			this.map.put("5023", "短信回呼");
			this.map.put("5024", "忙时通");
			this.map.put("5025", "CMNET业务");
			this.map.put("5026", "CMWAP业务");
			this.map.put("5027", "梦网易");
			this.map.put("5028", "终端管理CMDM");
			this.map.put("5029", "彩信账单");
			this.map.put("5030", "来电提醒");
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020001Result res = new QRY020001Result();
		List<BossParmDT> parList = null;
		List<GommonBusiness> reList = null;
		String reqXml = "";
		String rspXml = "";
		GommonBusiness dt = null;
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String bizId = "";
		ErrorMapping errDt = null;
		
		try
		{
			//根据业务编码获取其子业务所对应的BOSS实现
			for (RequestParameter r : params)
			{
				if ("bizId".equals(r.getParameterName()))
				{
					bizId = String.valueOf(r.getParameterValue());
					//其他业务 无子业务 展现全返回信息
					parList = this.wellFormedDAO.getSubBossParmList(bizId);
					/*if (!"TCJYWCX_QTYW".equals(bizId))
					{
						
					}*/
				}
			}
			
			if (!"TCJYWCX_QTYW".equals(bizId) && null != parList && parList.size() > 0)
			{
				RequestParameter par = new RequestParameter();
				par.setParameterName("codeCount");
				par.setParameterValue(parList);
				params.add(par);
			}
			
			//组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetincinfo_346", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetincinfo_346", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					reList = new ArrayList();
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					res.setResultCode("0000".equals(resp_code)?"0":"1");
					if (!"0000".equals(resp_code))
					{
						res.setResultCode(resp_code);
						res.setErrorMessage(resp_desc);
						
						errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetincinfo_346", resp_code);
						if (null != errDt)
						{
							res.setErrorCode(errDt.getLiErrCode());
							res.setErrorMessage(errDt.getLiErrMsg());
						}
					}
//					res.setErrorCode(resp_code);
//					res.setResultCode(resp_code);
//					res.setErrorMessage(resp_desc);
					
					if ("0000".equals(resp_code))
					{
						List spbizList = root.getChild("content").getChildren("smscall_deal_code");
						if (null != spbizList && spbizList.size() > 0)
						{
							for (int i = 0; i < spbizList.size(); i++)
							{
								int state = 0;
								Element csmscalldt = ((Element)spbizList.get(i)).getChild("csmscalldt");
								dt = new GommonBusiness();
								dt.setId(p.matcher(csmscalldt.getChildText("smscall_deal_code")).replaceAll("").trim());
								dt.setName(String.valueOf(this.map.get(dt.getId())==null?"":this.map.get(dt.getId())));
								dt.setReserve1(dt.getId().trim());
								dt.setBeginDate(p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll(""));
								dt.setEndDate(p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll(""));
								String strState = p.matcher(csmscalldt.getChildText("smscall_state")).replaceAll("");
								if (null != strState && !"".equals(strState))
								{
									if ("1".equals(strState))
									{
										state = 2;
									}
									else if ("2".equals(strState))
									{
										state = 1;
									}
									else
									{
										state = Integer.parseInt(strState);
									}
								}else
								{
									state = 1;
								}
								
								//现在接口smscall_state 这个字段一直是空，测试发现没有在用的增值业务不返回节点
								// 20111102 yangg mod====================================================
								String dealCode = p.matcher(csmscalldt.getChildText("smscall_deal_code")).replaceAll("").trim();
								if(StringUtils.isBlank(strState) &&  bizId != null  && ("5025".equals(dealCode) || "5026".equals(dealCode))&& bizId.startsWith("GPRSGN")){
									String startDate = p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll("");
									String endDate = p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll("");
									if(StringUtils.isNotBlank(startDate) ||StringUtils.isBlank(endDate)){
										state = 2;
									}else if (StringUtils.isNotBlank(startDate) ||StringUtils.isNotBlank(endDate)){
										state = 4;
									}else{
										state = 1;
									}
								}
								
								if(StringUtils.isBlank(strState) &&  bizId != null  
										&& ("5021".equals(dealCode) || "5022".equals(dealCode) || "5013".equals(dealCode) || "5012".equals(dealCode) || "5015".equals(dealCode) || "5017".equals(dealCode))
										&& bizId.startsWith("YCXXL")){
									String startDate = p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll("");
									String endDate = p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll("");
									if(StringUtils.isNotBlank(startDate) ||StringUtils.isBlank(endDate)){
										state = 2;
									}else if (StringUtils.isNotBlank(startDate) ||StringUtils.isNotBlank(endDate)){
										state = 4;
									}else{
										state = 1;
									}
								}
								//========================================================================
								
								
								dt.setState(state);
								reList.add(dt);
							}
							
							if (null != parList && parList.size() > 0 && null != reList && reList.size() > 0)
							{
								//彩铃特殊处理
								if ("CL".equals(bizId)) {
									int count = 0;
									gg : for(GommonBusiness dt2 : reList){
										//GommonBusiness base = reList.get(0);
										reList = new ArrayList();
										for (BossParmDT bDT : parList)
										{
											GommonBusiness newCom = copyCom(dt2);
											//如果没有开通，不返回空，而是返回所有，跟以前不一样，我处需加5006条件
											if("5006".equals(dt2.getId()))
											{ 
												newCom.setId("CL");
												reList.add(newCom);
												count ++ ;
												break gg;
											}
										}
									}
									if(count ==0)
									{
										GommonBusiness newCom2 = new GommonBusiness();
										newCom2.setId("CL");
										newCom2.setState(1);
										//newCom.setBeginDate("");
										reList.add(newCom2);
									}
								}
								else
								{
									for (BossParmDT bDT : parList)
									{
										boolean flag = true;
										for (GommonBusiness gDT : reList)
										{
											if (bDT.getParm1().equals(gDT.getReserve1()))
											{
												flag = false;
												gDT.setId(bDT.getBusiNum());
											}
										}

										if (!"TCJYWCX_QTYW".equals(bizId))
										{
											if (flag)
											{
												dt = new GommonBusiness();
												dt.setId(bDT.getBusiNum());
												dt.setState(1);
												dt.setName(String.valueOf(this.map.get(bDT.getParm1())==null?"":this.map.get(bDT.getParm1())));
												reList.add(dt);
											}
										}
									}
								}
							}
						}
						else
						{
							if (null != parList && parList.size() > 0)
							{
								for (BossParmDT bDT : parList)
								{
									dt = new GommonBusiness();
									dt.setId(bDT.getBusiNum());
									dt.setState(1);
									reList.add(dt);
								}
							}
						}
					}
					else
					{
						dt = new GommonBusiness();
						dt.setState(1);
						reList.add(dt);
					}
				}
				
				for (GommonBusiness busi : reList)
				{
					if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
					{
						busi.setState(4);
					}
					//短厅地址和网厅地址有查返回数据有差异 yangg add =====
					if(StringUtils.isNotBlank(bizId) && bizId.startsWith("CL"))
					{
						if (null != busi.getBeginDate() && !"".equals(busi.getBeginDate()))
						{
							if(null != busi.getEndDate() && !"".equals(busi.getEndDate()))
							{
								busi.setState(4);
							}
							else
							{
								busi.setState(2);
							}
						}
					}
				}
				res.setGommonBusiness(reList);
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	private GommonBusiness copyCom(GommonBusiness obj) {
		GommonBusiness copy = new GommonBusiness();

		copy.setId(obj.getId());
		copy.setName(obj.getName());
		copy.setBeginDate(obj.getBeginDate());
		copy.setEndDate(obj.getEndDate());
		copy.setState(obj.getState());
		copy.setReserve1(obj.getReserve1());
		copy.setReserve2(obj.getReserve2());

		return copy;
	}

}