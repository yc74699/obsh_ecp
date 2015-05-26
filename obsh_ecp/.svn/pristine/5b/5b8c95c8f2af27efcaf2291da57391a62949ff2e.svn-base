package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.IItBossDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.CombineBean;
import com.xwtech.xwecp.service.logic.pojo.QRY060002Result;
import com.xwtech.xwecp.service.logic.pojo.RecommendBizInfo;
import com.xwtech.xwecp.util.CommonUtil;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 精确营销案接口查询（新）
 * @author 陈小明
 * 2013-12-11
 */
public class QueryRecommendBizInvocation extends BaseInvocation implements ILogicalService 
{
	private static final Logger logger = Logger.getLogger(QueryRecommendBizInvocation.class);

	private List<RecommendBizInfo> rInfos; 
	
	private String bossTemplateQRY = "cc_qryrecommact_icrm_101";
	
	private String bossTemplateCHK = "cc_checkrecmdprod_101";
	
	private IItBossDao itBossDao;
	
	public QueryRecommendBizInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.itBossDao = (IItBossDao) (springCtx.getBean("itBossDao"));
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		QRY060002Result res = new QRY060002Result();
		setCommonResult(res, "1", "查询失败");
		List<CombineBean> comBineLs = new ArrayList<CombineBean>();
		String channelId = "";
		for (RequestParameter par : params) {
			if ("channelId".equals(par.getParameterName())) {
				channelId = String.valueOf(par.getParameterValue());
			}
		}
		try {
			rInfos = queryRecommendBizInfo(accessId, params, res, bossTemplateQRY);
			String areaId = (String)getParameters(params, "fixed_ddr_city");
			String isSplitChk = (String)getParameters(params, "isSplitChk");;
			//是否分离冲突校验，为１时该接口中将不再进行校验，为０时保持原逻辑进行校验
			if("0".equals(isSplitChk))
			{
				//校验产品是否符合推荐条件
				checkRecommendInfo(res,rInfos,params,accessId,bossTemplateCHK);
			}
			if (rInfos.size() > 0) {
				String actid = "";
				for (int i = 0; i < rInfos.size(); i++) {
					if (i == (rInfos.size() -1)) {
						actid += rInfos.get(i).getActid();
					} else {
						actid += rInfos.get(i).getActid()+",";
					}
				}
				comBineLs = itBossDao.getCombineByChannel(actid, channelId, areaId);
				for (CombineBean co : comBineLs) {
					for (RecommendBizInfo re : rInfos) {
						
						if (re.getActid().equals(co.getBusi_num())) {
							//修改此次 不再返回 链接给网厅，返回userSeq
							co.setUserSeq(re.getUserSeq());
						}
					}	
					
					
				}
				
			}
			res.setComBineLs(comBineLs);
		} catch (Exception e) 
		{
			logger.error(e, e);
		}
		return res;

	}

	private void buildRInfos(List<RecommendBizInfo> infos)
	{
		RecommendBizInfo info = null;
		for(int i = 0; i < 5; i++)
		{
			info = new RecommendBizInfo();
			info.setActid("10000");
			info.setOid("999"+i);
			infos.add(info);
		}
		
	}

	private void checkRecommendInfo(QRY060002Result res, List<RecommendBizInfo> infos, List<RequestParameter> params,
									String accessId, String bossTemplateCHK)
	{
		String oid = "";
		params.add(new RequestParameter("oid",oid));
		String reqXml;
		String rspXml;
		for(Iterator iter = infos.iterator(); iter.hasNext();)
		{
			oid = ((RecommendBizInfo) iter.next()).getOid();
			params.add(new RequestParameter("oid",oid));
			try
			 {
				 //组装报文
				 reqXml = mergeReqXML2Boss(params,bossTemplateCHK);
				 if (!StringUtil.isNull(reqXml))
				 {
					 //发送请求报文
					 rspXml = sendReqXML2BOSS(accessId, params, reqXml, bossTemplateCHK);
					 if (!StringUtil.isNull(rspXml))
					 {
						 String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
						 if(!"0000".equals(resp_code))
						 {
							 iter.remove();
						 }
					 }
				 }
			 }catch(Exception e)
			 {
				 logger.error(e);
				 setCommonResult(res, "1", "数据异常");
			 }
			 removeRequest(params,"oid");
		}
	}

	private List<RecommendBizInfo> queryRecommendBizInfo(String accessId, List<RequestParameter> params,
			QRY060002Result res,  String bossTempate) 
	{
		 rInfos = new ArrayList<RecommendBizInfo>();
		 String reqXml;
		 String rspXml;
		 try
		 {
			 //组装报文
			 reqXml = mergeReqXML2Boss(params,bossTempate);
			 if (!StringUtil.isNull(reqXml))
			 {
				 //发送请求报文
				 rspXml = sendReqXML2BOSS(accessId, params, reqXml, bossTempate);
				 if (!StringUtil.isNull(rspXml))
				 {
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
					setCommonResult(res, resp_code, resp_desc);
					List reList = XMLUtil.getChildList(rspXml,"content","recommact_list");
					if (!CommonUtil.isNull(reList)) {
						for (int i = 0; i < reList.size(); i++) {
							setRecommendInfo(rInfos, (Element)reList.get(i));
						}
					}
				 }
			 }
		 }catch(Exception e)
		 {
			 logger.info(e);
			 setCommonResult(res, "1", "数据异常");
		 }
		 return rInfos;
		
	}
	
	private void setRecommendInfo(List<RecommendBizInfo> rInfos,
			Element gsubDT) 
	{
		RecommendBizInfo rInfo = new RecommendBizInfo();
		rInfo.setOid(XMLUtil.getChildText(gsubDT,"oid"));                  //推荐流水号 ，校验时需
		rInfo.setEntityName(XMLUtil.getChildText(gsubDT,"entityName"));    //推荐的业务名称
		rInfo.setRecmdDiction(XMLUtil.getChildText(gsubDT,"recmdDiction"));//推荐用语
		rInfo.setNCode(XMLUtil.getChildText(gsubDT,"nCode"));              //接口NCODE编码
		rInfo.setProdid(XMLUtil.getChildText(gsubDT,"prodid"));            //增值产品编码
		rInfo.setPrivid(XMLUtil.getChildText(gsubDT,"privid"));            //优惠编码
		rInfo.setUserSeq(XMLUtil.getChildText(gsubDT,"userSeq"));          //用户办理业务推荐唯一编码,反馈时需
		rInfo.setKmsLink(XMLUtil.getChildText(gsubDT,"kmsLink"));          //短信端口
		rInfo.setTemplateId(XMLUtil.getChildText(gsubDT,"templateId"));    //短信模板号
		rInfo.setTimeType(XMLUtil.getChildText(gsubDT,"timeType"));        //推荐时间段
		rInfo.setActid(XMLUtil.getChildText(gsubDT,"actid"));              //活动编码
		rInfo.setActName(XMLUtil.getChildText(gsubDT,"actName"));          //活动名称
		rInfo.setContactTimes(XMLUtil.getChildText(gsubDT,"contactTimes"));//已推荐次数
		rInfo.setNotes(XMLUtil.getChildText(gsubDT,"notes"));              //备注:卖点介绍
		rInfo.setProdName(XMLUtil.getChildText(gsubDT,"prodName"));        //增值产品名称
		rInfo.setPrivName(XMLUtil.getChildText(gsubDT,"privName"));        //优惠名称
		rInfo.setRecmdType(XMLUtil.getChildText(gsubDT,"recmdType"));      //推荐类型
		rInfos.add(rInfo);
	}
}