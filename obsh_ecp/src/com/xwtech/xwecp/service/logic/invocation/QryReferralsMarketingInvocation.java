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
import com.xwtech.xwecp.service.logic.pojo.QRY060003Result;
import com.xwtech.xwecp.service.logic.pojo.RecommendBizInfo;
import com.xwtech.xwecp.service.logic.pojo.ReferralsMarketInfo;
import com.xwtech.xwecp.util.CommonUtil;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 营销推荐专区查询
 * @author 张斌
 * 2015-4-14
 */
public class QryReferralsMarketingInvocation extends BaseInvocation implements ILogicalService 
{
	private static final Logger logger = Logger.getLogger(QryReferralsMarketingInvocation.class);

	private List<ReferralsMarketInfo> rInfos; 
	
	private String bossTemplateQRY = "qryreferralsmarketing";
	
	private String bossTemplateCHK = "cc_checkrecmdprod_101";
	
	private IItBossDao itBossDao;
	
	public QryReferralsMarketingInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.itBossDao = (IItBossDao) (springCtx.getBean("itBossDao"));
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		QRY060003Result res = new QRY060003Result();
		setCommonResult(res, "1", "查询失败");
		List<CombineBean> comBineLs = new ArrayList<CombineBean>();
		String channelId = "";
		for (RequestParameter par : params) {
			if ("channelId".equals(par.getParameterName())) {
				channelId = String.valueOf(par.getParameterValue());
			}
		}
		try {
			rInfos = queryReferralsMarketInfo(accessId, params, res, bossTemplateQRY);
			String areaId = (String)getParameters(params, "fixed_ddr_city");
//			String isSplitChk = (String)getParameters(params, "isSplitChk");;
//			//是否分离冲突校验，为１时该接口中将不再进行校验，为０时保持原逻辑进行校验
//			if("0".equals(isSplitChk))
//			{
//				//校验产品是否符合推荐条件
//				checkRecommendInfo(res,rInfos,params,accessId,bossTemplateCHK);
//			}
			if (rInfos.size() > 0) {
				String actid = "";
				for (int i = 0; i < rInfos.size(); i++) {
					comBineLs = itBossDao.getCombineByChannel(rInfos.get(i).getActioncode(), channelId, areaId);
					for (CombineBean co : comBineLs) {
						for (ReferralsMarketInfo re : rInfos) {
							
							if (re.getActioncode().equals(co.getBusi_num())) {
								//修改此次 不再返回 链接给网厅，返回userSeq
								co.setUserSeq(re.getUserseq());
							}
						}	
					}
					rInfos.get(i).setComBineLs(comBineLs);
				}
				res.setReMarketInfo(rInfos);
			}
				
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

	private void checkRecommendInfo(QRY060003Result res, List<RecommendBizInfo> infos, List<RequestParameter> params,
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

	private List<ReferralsMarketInfo> queryReferralsMarketInfo(String accessId, List<RequestParameter> params,
			QRY060003Result res,  String bossTempate) 
	{
		 rInfos = new ArrayList<ReferralsMarketInfo>();
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
					List reList = XMLUtil.getChildList(rspXml,"content","list");
					if (!CommonUtil.isNull(reList)) {
						for (int i = 0; i < reList.size(); i++) {
							setReferralsMarketInfo(rInfos, (Element)reList.get(i));
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
	
	private void setReferralsMarketInfo(List<ReferralsMarketInfo> rInfos,
			Element gsubDT) 
	{
		ReferralsMarketInfo rInfo = new ReferralsMarketInfo();
		rInfo.setBigclass(XMLUtil.getChildText(gsubDT,"bigclass"));//推荐大类编码
		rInfo.setBigclassname(XMLUtil.getChildText(gsubDT,"bigclassname"));	//推荐大类名称
		rInfo.setSmallclass(XMLUtil.getChildText(gsubDT,"smallclass")); //推荐小类
		rInfo.setSmallclassname(XMLUtil.getChildText(gsubDT,"smallclassname"));	//推荐小类名称
		rInfo.setActioncode(XMLUtil.getChildText(gsubDT,"actioncode"));	//推荐活动编码 ty...
		rInfo.setSubactid(XMLUtil.getChildText(gsubDT,"subactid"));	//子活动编码
		rInfo.setProdamount(XMLUtil.getChildText(gsubDT,"prodamount"));	//推荐产品个数大于1时需要调用“获取活动可推荐产品”接口查询产品信息rectype（推荐类型）为1、4时有效。为3时，不需判断此字段
		rInfo.setRectype(XMLUtil.getChildText(gsubDT,"rectype"));	//推荐类型1：产品类推荐（支持推荐多个）3：营销案推荐（暂不支持推荐多个）4：产品包下产品推荐（支持推荐多个，如果有多个推荐产品，获取活动可推荐产品接口返回的产品信息中，也是有多个产品包的情况）
		rInfo.setProdid(XMLUtil.getChildText(gsubDT,"prodid"));	//产品编码
		rInfo.setProdpkgid(XMLUtil.getChildText(gsubDT,"prodpkgid"));	//产品包编码 推荐类型为4产品编码生效 有产品包的时候提交需要传递2条数据调用5.3.1接口
		rInfo.setMainprodid(XMLUtil.getChildText(gsubDT,"mainprodid"));	//主体产品编码
		rInfo.setActid(XMLUtil.getChildText(gsubDT,"actid"));	//营销方案批次编码
		rInfo.setLevelid(XMLUtil.getChildText(gsubDT,"levelid"));		//营销方案批档次
		rInfo.setUserseq(XMLUtil.getChildText(gsubDT,"userseq"));		//推荐流水号
		rInfo.setRecommendInfo(XMLUtil.getChildText(gsubDT,"recommendInfo"));		//推荐术语
		rInfo.setProdname(XMLUtil.getChildText(gsubDT,"prodname"));		//产品名称
		rInfo.setCurrusage(XMLUtil.getChildText(gsubDT,"currusage"));	//当前使用情况
		rInfo.setOpertype(XMLUtil.getChildText(gsubDT,"opertype"));		//操作类型 0 可推荐 1 不可推荐 为空查询全部
		rInfos.add(rInfo);
	}
}