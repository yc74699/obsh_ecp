package com.xwtech.xwecp.service.logic.invocation;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.HalfFlow;
import com.xwtech.xwecp.service.logic.pojo.PackageFlow;
import com.xwtech.xwecp.service.logic.pojo.QRY040048Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;


public class QueryGPSFluxInfoNewInvocation extends BaseInvocation implements
		ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryGPSFluxInfoNewInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
//	private WellFormedDAO wellFormedDAO;
	/*20元流量封顶的套餐类型编码*/
	private static String unlimitedBandwidthType = "1046";
	/*存放20元流量封顶的产品编码*/
	private Set<String> maxCode;
	/*存放随E玩的产品编码*/
	private Set<String> sEWCode;
	/*标示用户是否办理了20元流量封顶业务*/
	private String isUnlimitedBandwidth = "0";
	/*标示用户是否办理了随E玩业务*/ 
	private String isPlayAt = "0";
	/*附加套餐业务标 1:流量季包半年包标识*/
	private String isSpecilFlag = "0";
	/*4G半包套餐业务标识*/
	private String isHalfFlag = "0";
	/*存放流量叠加包编码*/
	private Map<String,Integer> fluxPackage ;
	/*用于屏蔽Wlan包流量*/
	private Set<String> wlan;
	/*流量季包半年包流量*/
	private Set<String> quaGprs;
	
	/*4G产品对照表(全包)*/
	//private Set<String> LTEGprs;
	
	/*用于屏蔽4G产品对照表(半包) 隨意玩*/
	private Set<String> halfGprs;
	
	public QueryGPSFluxInfoNewInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		isUnlimitedBandwidth = "0";
		isPlayAt = "0";
		maxCode = new HashSet<String>();
		sEWCode = new HashSet<String>();
		wlan = new HashSet<String>();
		fluxPackage = new HashMap<String,Integer>();
		quaGprs = new HashSet<String>();
		
		//查询4G全包类、叠加包类和半包类业务编码
		//LTEGprs = getLTEGRPS("1,2","3");
		//过滤掉随意玩两个编码
		//LTEGprs.remove("2000002660");
	    //LTEGprs.remove("2000002661");
		
		//查询半包类业务编码
		halfGprs = getLTEGRPS("3");
		maxCode.add("4052");
		maxCode.add("1277");
		maxCode.add("5652");
		maxCode.add("4051");
		maxCode.add("4825");

		sEWCode.add("2000002660");
		sEWCode.add("2000002661");
		wlan.add("2000003177");
		wlan.add("2000003178");
		wlan.add("2000003179");
		/*港澳台GPRS流量漫游*/
		wlan.add("3223");
		wlan.add("1979");
		wlan.add("3221");
		wlan.add("3222");
		wlan.add("2000003223");
		wlan.add("2000001979");
		wlan.add("2000003221");
		wlan.add("2000003222");
		/*存放流量叠加包编码*/
		fluxPackage.put("2000002505", 0);
		fluxPackage.put("2000002504", 0);
		fluxPackage.put("2000002631", 0);
		fluxPackage.put("2000003020", 0);
		fluxPackage.put("2000003540", 0);
//		fluxPackage.put("2000003541", 0);
		/*流量季包半年包*/
		quaGprs.add("2000003748");
		quaGprs.add("2000003749");
		quaGprs.add("2000003750");
		quaGprs.add("2000003751");
		quaGprs.add("2000003752");
		quaGprs.add("2000003753");
		/*4G产品对照编码*/
//		LTEGprs.add("2000003806");
//		LTEGprs.add("2000003790");  LTEGprs.add("2000003791");  LTEGprs.add("2000003772");  LTEGprs.add("2000003773");  LTEGprs.add("2000003774");  LTEGprs.add("2000003775");    
//		LTEGprs.add("2000003776");  LTEGprs.add("2000003777");  LTEGprs.add("2000003778");  LTEGprs.add("2000003785");  LTEGprs.add("2000003759");  LTEGprs.add("2000003760");  LTEGprs.add("2000003761");    
//		LTEGprs.add("2000003762");  LTEGprs.add("2000003763");  LTEGprs.add("2000003764");  LTEGprs.add("2000003765");  LTEGprs.add("2000003766");  LTEGprs.add("2000003767");  LTEGprs.add("2000003768");    
//		LTEGprs.add("2000003769");  LTEGprs.add("2000003770");  LTEGprs.add("2000003771");  LTEGprs.add("2000003779");  LTEGprs.add("2000003780");  LTEGprs.add("2000003781");  LTEGprs.add("2000003782");    
//		LTEGprs.add("2000003783");  LTEGprs.add("2000003784");  LTEGprs.add("2000003797");
		/*4G产品对照编码(半包)、隨意玩*/
//		halfGprs.add("2000003790"); halfGprs.add("2000003791"); halfGprs.add("2000002660"); halfGprs.add("2000002661"); halfGprs.add("2000003806");
	}
	
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY040048Result res = new QRY040048Result();
		checkPackage(params,accessId,res);
		if(null == res.getErrorCode() || "".equals(res.getErrorCode()))
		{
			res.setIsUnlimitedBandwidth(isUnlimitedBandwidth);
			res.setIsPlayAt(isPlayAt);
			res.setIsSpecilFlag(isSpecilFlag);
			res.setIsHalfFlag(isHalfFlag);
			
			if("1".equals(isUnlimitedBandwidth))
			{
				getUsedFlux(res,params,accessId);
				return res;
			}
			getUserFluxInfo(res,params,accessId,config);
		}
		return res;
	}
	
	/*判断业务中是否有20元封顶和随E玩*/
	private void checkPackage(List<RequestParameter> params ,String accessId,QRY040048Result res)
	{
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		int nowDate = Integer.parseInt(DateTimeUtil.getTodayChar8());
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element personalInfo = this.getElement(rspXml.getBytes());
					String resp_code = personalInfo.getChild("response").getChildText("resp_code");
					String resp_desc = personalInfo.getChild("response").getChildText("resp_desc");
				
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					if(!LOGIC_SUCESS.equals(resultCode))
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
					else
					{
						List package_code = personalInfo.getChild("content").getChildren("package_code");
						int size = package_code.size();
						
						if (null != package_code && size > 0)
						{
							for (int i = 0; i < size; i++)
							{
								Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");

								String packageType = p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll("");
								String packageCode = p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll("");
								int userDate = Integer.parseInt(p.matcher(
										cplanpackagedt.getChildText("package_use_date")).replaceAll("").substring(0, 8));
								
								if(unlimitedBandwidthType.equals(packageType) && (checkMaxFlux(packageCode,res)) && nowDate >= userDate)
								{
									/*标示用户是否办理了20元GPS流量封顶业务,如果有封顶业务无需循环直接跳出*/
									isUnlimitedBandwidth = "1";
								}
								if(sEWCode.contains(packageCode)&& nowDate >= userDate)
								{	
									isPlayAt = "1";
								}
								if(quaGprs.contains(packageCode) && nowDate >= userDate){
									isSpecilFlag = "1";
								}
								if(halfGprs.contains(packageCode) && nowDate >= userDate){
									isHalfFlag = "1";
								}
							}							
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
	}
	private String formatGPRSDetail(String gprsFlux)
	{
		double flux = Double.parseDouble((String) gprsFlux);
		DecimalFormat df = new DecimalFormat("#0.00");
		gprsFlux = df.format(flux / 1024);
		
		return gprsFlux;
	}
	/*获取20元封顶的月流量使用情况*/
	private void getUsedFlux(QRY040048Result res,List<RequestParameter> params ,String accessId)
	{
		setParameter(params, "dayNumber", "31");
		setParameter(params, "idType", "0");
		String date = (String)getParameters(params,"cycle");
		setParameter(params, "date",date);
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		try{
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetgprsflux_717", params);
			logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetgprsflux_717", super.generateCity(params)));
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
				if (null != rspXml && !"".equals(rspXml)) 
				{
					root = this.getElement(rspXml.getBytes());
					resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;

					if (LOGIC_SUCESS.equals(resultCode))
					{
						String errCode = root.getChild("content").getChildText("error_code");
						String useFlux = root.getChild("content").getChildText("gprsbill_total_fee");
						String tempFlux = formatGPRSDetail(useFlux);
						
						res.setResultCode(resultCode);
						res.setErrorCode(errCode);
						res.setErrorMessage(resp_desc);
						res.setUseFlux(tempFlux);
						return;
					}
					else
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e,e);
		}
	}
	/*
	 * 获取用户流量使用明细
	 * */
	private void getUserFluxInfo(QRY040048Result res,List<RequestParameter> params ,String accessId,ServiceConfig config)
	{ 
		int totalValue = 0;
		int userdValue = 0;
		int gprs_cumulate_type =0; // 季包标识 0：没有季包，1有季包；
		try{
	       //查询办理4G套餐功能的产品使用情况（全包和半年包）
	       List<Node> lteList = queryGPRSPackageInfo(accessId,config,params,res);
	        
		    if(null != lteList && lteList.size() > 0)
		    {
			   for(int i=0;i<lteList.size();i++)
			   {
		         Node lteNode = lteList.get(i);
			     String productId = lteNode.selectSingleNode("gprs_product_id").getText().trim();
			     String productType = lteNode.selectSingleNode("gprs_product_type").getText().trim();
			     String rateType = lteNode.selectSingleNode("gprs_rate_type").getText().trim();
			     
			     int tempTotal = Integer.parseInt(lteNode.selectSingleNode("gprs_max_value").getText().trim());
			     int tempUsed = Integer.parseInt(lteNode.selectSingleNode("gprs_cumulate_value").getText().trim());
			     /**跨月套餐使用流量查询，增加3个出参：类型:0当月，1跨月，开始时间，结束时间*/
				 String gprs_cumulate_typeStr  =  lteNode.valueOf("gprs_cumulate_type"); 
				 if(null != gprs_cumulate_typeStr && "1".equals(gprs_cumulate_typeStr)){
					 gprs_cumulate_type = 1;
				 }

			     //处理随意玩套餐流量的使用情况
			     if(sEWCode.contains(productId))
			     {
			    	 HalfFlow halfFlow = new HalfFlow();
					 halfFlow.setTotalFlow(String.valueOf(tempTotal));
					 halfFlow.setUsedFlow(String.valueOf(tempUsed));
					 res.setHalfFlow(halfFlow);
					 continue;
				  //全包的套餐流量总量和使用量的总和。(productType： 1:半包，2：全包，3：全包，半包)
			     }
			     else if("2".equals(productType) && !"2".equals(rateType))
			     {
			    	 totalValue = totalValue + tempTotal;
				     userdValue = userdValue + tempUsed;
			     }
			  }
		   }

		   PackageFlow packageFlow = new PackageFlow();
		   packageFlow.setTotalFlow(String.valueOf(totalValue));
		   packageFlow.setUsedFlow(String.valueOf(userdValue));
		   res.setFlagJIBAO(gprs_cumulate_type);
		   res.setPackageFlow(packageFlow);
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}	
    }

	/**
	 * 查询用户个人4G功能套餐的使用明细------新改造直联boss
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> queryGPRSPackageInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040048Result res)
	{
		setParameter(params, "gprstype", "3");
		String dateNow = DateTimeUtil.getTodayChar6();
		setParameter(params, "cycle",dateNow);

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("QUERYGPRSALLPKGFLUX_814", params);
			logger.debug(" ====== 查询用户个人4G功能套餐的使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "QUERYGPRSALLPKGFLUX_814", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						return freeItemNodes;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}
//	/**
//	 * 查询用户个人4G功能套餐的使用明细
//	 * @param accessId
//	 * @param config
//	 * @param params
//	 * @param res
//	 * @return
//	 */
//	private List<Node> queryGPRSPackageInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040048Result res)
//	{
//		setParameter(params, "gprstype", "3");
//		String dateNow = DateTimeUtil.getTodayChar6();
//		setParameter(params, "cycle",dateNow);
//		
//		try{
//			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsallpkgflux_70", params);
//			logger.debug(" ====== 查询用户个人4G功能套餐的使用明细 ======\n" + reqXml);
//			if (null != reqXml && !"".equals(reqXml))
//			{
//				String rspXml = (String)this.remote.callRemote(
//						new StringTeletext(reqXml, accessId, "cc_4gqrygprsallpkgflux_70", this.generateCity(params)));
//				logger.debug(" ====== 返回报文 ======\n" + rspXml);
//				Element root = checkReturnXml(rspXml,res);
//				if(null != root)
//				{
//					Document doc = DocumentHelper.parseText(rspXml);
//					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
//					if(null != freeItemNodes && freeItemNodes.size()>0)
//					{
//						return freeItemNodes;
//					}
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			logger.error(e, e);
//		}
//		return null;
//	}

	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040048Result res)
	{
		Element root = this.getElement(rspXml.getBytes());
		String resp_code = root.getChild("response").getChildText("resp_code");
		String resp_desc = root.getChild("response").getChildText("resp_desc");
		//Boss一期割接到Boss二期处理 Boss一期为一个0 Boss二期为4个0
		if("0000".equals(resp_code))
		{
			resp_code = "0";
		}
//		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		res.setResultCode("0".equals(resp_code)?"0":"1");
		res.setErrorCode(resp_code);
		res.setErrorMessage(resp_desc);
		if(null != rspXml && !"".equals(rspXml))
		{
			if(!LOGIC_SUCESS.equals(res.getResultCode()) || null == root)
			{
				res.setResultCode(res.getResultCode());
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
			else
			{
				res.setResultCode(res.getResultCode());
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				return root;
			}
		}
		return null;
	}
	
	
	
	/*private boolean checkFluxPackage(String packageId)
	{
		if(fluxPackage.containsKey(packageId))
		{
			int count = Integer.valueOf(fluxPackage.get(packageId).toString()) + 1;
			if(count > 1)
			{
				return false;
			}
			fluxPackage.put(packageId,count);	
		}
		return true;
	}*/

/**
 * 检查有无20元封顶业务
 * @param packageCode
 * @param res
 * @return
 */
	private boolean checkMaxFlux(String packageCode,QRY040048Result res)
	{
		//判断是否是否匹配编码
		if(maxCode.contains(packageCode)||maxCode.contains(packageCode.substring(packageCode.length()-4, packageCode.length())))
		{
			return true;
		}
		return false;
	}
	private Set<String> getLTEGRPS(String ... type) {
		return this.wellFormedDAO.getLTE4GCode(type);
	}
}
