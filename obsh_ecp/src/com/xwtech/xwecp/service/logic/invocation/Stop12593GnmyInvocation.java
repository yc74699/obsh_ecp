package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.MarketActInfo;
import com.xwtech.xwecp.service.logic.pojo.RewardInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

public class Stop12593GnmyInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger.getLogger(Stop12593GnmyInvocation.class);

	/*新加入无线公话无线赏花禁止办理GJTGAMY和GJTGACT业务*/
	private Set<String> wXGHSets;
	
	private Set<String> wXSHSets;

	private String GNMY = "12593GNMY";
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	//请求获取个人产品信息失败
	private static  final String errorGetProInfo = "-345";
	/*end*/
	
	//gprs套餐编码
	private Map<String, String> GNYDSJTC = new HashMap<String, String>();
	
	//gprs套餐编码级别关系 ,升档使用
	private Map<String, Integer> GNYDSJTCJB = new HashMap<String, Integer>();
	
	private final String ERRCODE = "979111";
	public Stop12593GnmyInvocation() {
		wXSHSets = new HashSet<String>();
		wXGHSets = new HashSet<String>();
		wXGHSets.add("1000100001");
		wXGHSets.add("1000100005");
		wXGHSets.add("1000100040");
		wXGHSets.add("100001");
		wXGHSets.add("100005");
		wXGHSets.add("100040");
		
		wXSHSets.add("1000100097");
		wXSHSets.add("1000100098");
		wXSHSets.add("1000100133");
		wXSHSets.add("1000100134");
		wXSHSets.add("1100006660");
		wXSHSets.add("1400100133");
		wXSHSets.add("1400100134");
		wXSHSets.add("100097");
		wXSHSets.add("100098");
		wXSHSets.add("100133");
		wXSHSets.add("100134");
		wXSHSets.add("006660");
		wXSHSets.add("100133");
		wXSHSets.add("100134");
		
		GNYDSJTC.put("GNYDSJTC_5Y","2000001531");   //5元  
		GNYDSJTC.put("GNYDSJTC_10Y", "2000001678"); //10元 
		GNYDSJTC.put("GNYDSJTC_20Y", "2000001532"); //20元 
		GNYDSJTC.put("GNYDSJTC_30Y", "2000003020"); //30元 
		GNYDSJTC.put("GNYDSJTC_50Y", "2000001533"); //50元 
		GNYDSJTC.put("GNYDSJTC_100Y", "2000001534");//100元 
		GNYDSJTC.put("GNYDSJTC_200Y", "2000001535");//200元 
		
		GNYDSJTCJB.put("2000001531", 1); 
		GNYDSJTCJB.put("2000001678", 2);
		GNYDSJTCJB.put("2000001532", 3); 
		GNYDSJTCJB.put("2000003020", 4); 
		GNYDSJTCJB.put("2000001533", 5); 
		GNYDSJTCJB.put("2000001534", 6); 
		GNYDSJTCJB.put("2000001535", 7); 
	}
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL010001Result res = new DEL010001Result();
		String id = String.valueOf(getParameters(params,"id"));
		String oprType = String.valueOf(getParameters(params,"oprType"));
		
		if(GNMY.equals(id) && ("1".equals(oprType) || "3".equals(oprType)))
		{
			if(stopGNMY(res,accessId,config,params))
			{
				return res;
			}
		}
		res = cchgpkgforpro(res,accessId,config,params);
		//GPRS套餐无法受理,并且错误码为979111，走营销案办理接口
		if((null != GNYDSJTC.get(id)) && (res.getErrorCode().equals(ERRCODE)))
		{
			applyMarketChg(res,params,accessId,id);
		}
		return res;
	}
	
	private DEL010001Result applyMarketChg(DEL010001Result res,List<RequestParameter> params,String accessId, String id)
	{
		MarketActInfo info = null;
		RewardInfo reInfo = null;
		try{
			//1.查询用户可变更的营销案
			info = qryUserMarketPkg(params,accessId,"cc_market_chgprod_qry_100");
			if(null == info)
			{
				setCommonResult(res, "9999100", "您好，系统正忙，建议稍后再试!");
				return res;
			}
			//2.查询奖品包下定义的奖品查询接口
			List<RewardInfo> rInfo = qryRewardMarket(res,params,accessId,"cc_market_reward_qry_100",info);
			if(rInfo.size() < 0)
			{
				setCommonResult(res, "9999100", "您好，系统正忙，建议稍后再试!");
				return res;
			}
			//3.获取用户当前的套餐是否可以办理
			reInfo = getReInfo(rInfo); 
			if(reInfo == null)
			{
				setCommonResult(res, "9999100", "您好，系统正忙，建议稍后再试!");
				return res;
			}
			
			//4.判断要办理套餐是否是升档套餐
//			if(!isUpPkg(id,reInfo))
//			{
//				setCommonResult(res, "9999101", "您好，您参加的活动尚未到期，如需变更业务，请选择高于目前您正在使用的流量套餐档次办理，如有疑问，请致电10086！");
//				return res;
//			}
			//5. 营销方案产品变更提交接口
			marketApplyChg(res,params,accessId,"cc_market_chgprod_100",info,reInfo,id);
		}
		catch(Exception e)
		{
			logger.info(e);
			setCommonResult(res, "9999100", "您好，系统正忙，建议稍后再试!");
		}
		return res;
	}
	
	private void marketApplyChg(DEL010001Result res,
			List<RequestParameter> params, String accessId, String bossTemplate,
			MarketActInfo info, RewardInfo reInfo,String id) throws CommunicateException, Exception 
	{
		params.add(new RequestParameter("pack_oid",info.getPacklist()));
		params.add(new RequestParameter("del_rwd_list",reInfo.getOid()));
		params.add(new RequestParameter("prodid",GNYDSJTC.get(id)));
		String rspXml ;
		String reqXml = mergeReqXML2Boss(params,bossTemplate);
		if (!StringUtil.isNull(reqXml))
		{
			//发送请求报文
			rspXml = sendReqXML2BOSS(accessId, params, reqXml,
					bossTemplate);
			if (!StringUtil.isNull(rspXml))
			{		
				String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
				String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
				setCommonResult(res, resp_code, resp_desc);
			}
		}
	}
	private boolean isUpPkg(String id, RewardInfo reInfo)
	{
		String bosscode = GNYDSJTC.get(id);
		//判断当前要办理的套餐是否大于用户已办理的套餐
		if(GNYDSJTCJB.get(bosscode) > GNYDSJTCJB.get(reInfo.getProdId()))
		{
			return true;
		}
		return false;
	}
	private RewardInfo getReInfo(List<RewardInfo> infos)
	{
		for(RewardInfo info : infos)
		{
			//当前办理的奖品oid不为空，可办理改套餐的升档套餐
			if((null != info.getOid() && !info.getOid().equals("")))
			{
				return info;
			}
		}
		return null;
	}
	private List<RewardInfo> qryRewardMarket(DEL010001Result res,
			List<RequestParameter> params, String accessId, String bossTemplate,
			MarketActInfo actinfo) throws CommunicateException, Exception 
	{
		params.add(new RequestParameter("rewardid",actinfo.getActId()));
		params.add(new RequestParameter("rewardoid",actinfo.getPacklist()));
		String rspXml ;
		RewardInfo info ;
		List<RewardInfo> rewardlist = new ArrayList<RewardInfo>();
		String reqXml = mergeReqXML2Boss(params,bossTemplate);
		if (!StringUtil.isNull(reqXml))
		{
			//发送请求报文
			rspXml = sendReqXML2BOSS(accessId, params, reqXml,
					bossTemplate);
			if (!StringUtil.isNull(rspXml))
			{		
				String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
				if (BOSS_SUCCESS.equals(resp_code)) 
				{
					List eslist = XMLUtil.getChildList(rspXml,"content","rwd_present_pack_content");
					for(int i = 0 ; i < eslist.size(); i++)
					{
						info = new RewardInfo();
						Element es = (Element)eslist.get(i);
						info.setProdId(XMLUtil.getChildText(es,"rwd_prod_id"));
						info.setProdName(XMLUtil.getChildText(es,"rwd_prod_name")); 
						info.setOid(XMLUtil.getChildText(es,"usr_rwd_oid"));
						rewardlist.add(info);
					}
				}
			}
		}
		return rewardlist;
	}
	private MarketActInfo qryUserMarketPkg(List<RequestParameter> params,
			String accessId, String bossTemplate) throws CommunicateException, Exception 
	{
		String rspXml ;
		MarketActInfo info = null;
		String reqXml = mergeReqXML2Boss(params,bossTemplate);
		if (!StringUtil.isNull(reqXml))
		{
			//发送请求报文
			rspXml = sendReqXML2BOSS(accessId, params, reqXml,
					bossTemplate);
			if (!StringUtil.isNull(rspXml))
			{		
				String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
				String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
				if (BOSS_SUCCESS.equals(resp_code)) 
				{
					List eslist = XMLUtil.getChildList(rspXml,"content","usr_rwd_info");
					info = new MarketActInfo();
					//默认取第一条
					Element es = (Element)eslist.get(0);
					info.setActId(XMLUtil.getChildText(es,"rwd_action_id"));
					info.setActName(XMLUtil.getChildText(es,"rwd_action_name")); 
					info.setLevelId(XMLUtil.getChildText(es,"rwd_level_id"));
					info.setLevelName(XMLUtil.getChildText(es,"rwd_level_name"));
					info.setStartDate(XMLUtil.getChildText(es,"rwd_level_satrtdate"));
					info.setEndDate(XMLUtil.getChildText(es,"rwd_level_enddate"));
					info.setPacklist(XMLUtil.getChildText(es,"Canchg_present_pack_list"));
				}
			}
		}
		return info;
	}
	private boolean stopGNMY(DEL010001Result res,String accessId, ServiceConfig config,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetproinfo_345", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR;
				if(LOGIC_SUCESS.equals(resultCode))
				{
					List userproductinfo_product_info_id = null;
					try {
						userproductinfo_product_info_id = root.getChild("content").getChildren("userproductinfo_product_info_id");
					}
					catch(Exception e)
					{
						userproductinfo_product_info_id = null;
						return false;
					}
					
					if (null != userproductinfo_product_info_id && userproductinfo_product_info_id.size() > 0)
					{
						int size =  userproductinfo_product_info_id.size();
						for (int i = 0; i <size; i++)
						{
							Element cuserproductinfodt = ((Element) userproductinfo_product_info_id.get(i)).getChild("cuserproductinfodt");
							if (null != cuserproductinfodt)
							{
								String prodctId = p.matcher(cuserproductinfodt.getChildText("userproductinfo_product_id")).replaceAll("");
								if(wXGHSets.contains(prodctId))
								{
									res.setErrorMessage("您已经办理无线公话业务，不能开通国际及港澳台漫游和国际及港澳台长途功能");
									res.setErrorCode(LOGIC_INFO);
									res.setResultCode(LOGIC_ERROR);
									return true;
								}
								else if(wXSHSets.contains(prodctId))
								{
									res.setErrorMessage("您已经办理无线商话业务，不能开通国际及港澳台漫游和国际及港澳台长途功能");
									res.setErrorCode(LOGIC_INFO);
									res.setResultCode(LOGIC_ERROR);
									return true;
								}
							}
							return false;
						}
					}
				}
				else
				{
					res.setAccessId(accessId);
					res.setErrorMessage(errDesc);
					res.setErrorCode(errorGetProInfo);
					res.setResultCode(resultCode);
					return true;
				}
			}
		}catch(Exception e)
		{
			logger.error(e, e);
		}
		return true;
	}
	
	private DEL010001Result cchgpkgforpro(DEL010001Result res,String accessId, ServiceConfig config,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgpkgforpro_76", params);
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchgpkgforpro_76", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = XMLUtil.getChildText(root,"response","resp_code");
				String errDesc = XMLUtil.getChildText(root,"response","resp_desc");
				String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR;

				res.setErrorMessage(errDesc);
				res.setErrorCode(errCode);
				res.setResultCode(resultCode);
			}
		}catch(Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
}