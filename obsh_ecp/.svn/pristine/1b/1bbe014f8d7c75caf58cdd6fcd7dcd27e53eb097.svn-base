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
import com.xwtech.xwecp.dao.IYingXingCardDao;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.HalfFlow;
import com.xwtech.xwecp.service.logic.pojo.PackageFlow;
import com.xwtech.xwecp.service.logic.pojo.QRY040084Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.CommonUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 20元封顶业务
 * @author YangXQ
 * 20140724
 */
public class QueryGPSFluxInfo20Invocation extends BaseInvocation implements
		ILogicalService {	
	private static final Logger logger = Logger.getLogger(QueryGPSFluxInfo20Invocation.class); 
	private BossTeletextUtil bossTeletextUtil; 
	private IRemote remote;
	//迎新号码DAO
	private IYingXingCardDao yingXingDao;
	//默认目标库里没有，为false
	private boolean Flag = false;
	
	/*20元流量封顶的套餐类型编码*/
	private static String unlimitedBandwidthType = "1046";
	/*存放20元流量封顶的产品编码*/
	private Set<String> maxCode;
	/*存放随E玩的产品编码*/
	private Set<String> sEWCode;
	/*标示用户是否办理了20元流量封顶业务*/
	private String isUnlimitedBandwidth = "0";	
	/*存放流量叠加包编码*/
	private Map<String,Integer> fluxPackage ;
	/*用于屏蔽Wlan包流量*/
	private Set<String> wlan;
	/*流量季包半年包流量*/
	private Set<String> quaGprs;
 	
	public QueryGPSFluxInfo20Invocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		isUnlimitedBandwidth = "0";
		maxCode = new HashSet<String>();
		sEWCode = new HashSet<String>();
		wlan = new HashSet<String>();
		fluxPackage = new HashMap<String,Integer>();
		quaGprs = new HashSet<String>();				
		maxCode.add("4052"); // 存放20元流量封顶的产品编码
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
		/*流量季包半年包*/
		quaGprs.add("2000003748");
		quaGprs.add("2000003749");
		quaGprs.add("2000003750");
		quaGprs.add("2000003751");
		quaGprs.add("2000003752");
		quaGprs.add("2000003753");
	}
	
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY040084Result res = new QRY040084Result();
		
		//判断业务中是否有20元封顶和随E玩
		checkPackage(params,accessId,res);
		
		if(null == res.getErrorCode() || "".equals(res.getErrorCode()))
		{
			res.setIsUnlimitedBandwidth(isUnlimitedBandwidth);
 
			// 用户是否办理了20元流量封顶业务 
			if("1".equals(isUnlimitedBandwidth))
			{
				getUsedFlux(res,params,accessId);
				return res;
			}
			
			// 获取用户流量使用明细
			getUserFluxInfo(res,params,accessId,config);
		}
		return res;
	}
	
	/*判断业务中是否有20元封顶和随E玩*/
	private void checkPackage(List<RequestParameter> params ,String accessId,QRY040084Result res)
	{
		String phone = (String)getParameters(params,"phoneNum");
		String city  = (String)getParameters(params,"context_ddr_city");
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		int nowDate = Integer.parseInt(DateTimeUtil.getTodayChar8());
		try{
			// 根据当前地市，查出流量封顶的目标库
			ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
			yingXingDao = (IYingXingCardDao) (springCtx.getBean("yingXingDao"));
			List<String> phoneList = yingXingDao.getFluxFD(city);
			
			// 查询目标库是否有当前这个手机号码
			this.Flag =CommonUtil.secondSearch(phoneList,Long.valueOf(phone).longValue());
			
			// 若目标库里没有这个号码，即当前用户未开通
			if (this.Flag == false) {
				return;
			}
			
			
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
						List<?> package_code = personalInfo.getChild("content").getChildren("package_code");
						int size = package_code.size();						
						if ( size > 0)
						{
							for (int i = 0; i < size; i++)
							{
								Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");
								String  packageType    = p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll("");
								String  packageCode    = p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll("");
								int userDate = Integer.parseInt(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll("").substring(0, 8));
								
								
								// 20元流量封顶的套餐类型编码 1046 && 检查有无20元封顶业务 -判断是否是否匹配编码-maxCode &&
								if(unlimitedBandwidthType.equals(packageType) && (checkMaxFlux(packageCode,res)) && nowDate >= userDate)
								{
									/*标示用户是否办理了20元GPS流量封顶业务,如果有封顶业务无需循环直接跳出*/
									isUnlimitedBandwidth = "1";	
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
	private void getUsedFlux(QRY040084Result res,List<RequestParameter> params ,String accessId)
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
	
	/**
	 * 查询不规则流量GPRS
	 * @param  
	 * @return 
	 */
//	private  List<String> getAnomalyGprs() {
//		return this.wellFormedDAO.getAnomalyGprs();
//	}
	/**
	 * 获取用户流量使用明细
	 * */
	private void getUserFluxInfo(QRY040084Result res,List<RequestParameter> params ,String accessId,ServiceConfig config)
	{ 
		int totalValue = 0;
		int userdValue = 0;
//	    List<String> anomalylist  = this.getAnomalyGprs();
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
	 * 查询用户个人4G功能套餐的使用明细
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Node> queryGPRSPackageInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040084Result res)
	{
		setParameter(params, "gprstype", "3");
		String dateNow = DateTimeUtil.getTodayChar6();
		setParameter(params, "cycle",dateNow);

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsallpkgflux_70", params);
			logger.debug(" ====== 查询用户个人4G功能套餐的使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_4gqrygprsallpkgflux_70", this.generateCity(params)));
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

	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040084Result res)
	{	
		if(null != rspXml && !"".equals(rspXml))
		{
			Element root = this.getElement(rspXml.getBytes());
			String resp_code = root.getChild("response").getChildText("resp_code");
			String resp_desc = root.getChild("response").getChildText("resp_desc");			
			String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
			
			if(!LOGIC_SUCESS.equals(resultCode))
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
			else
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				return root;
			}
		}
		return null;
	}
	/**
	 * 检查有无20元封顶业务
	 * @param packageCode
	 * @param res
	 * @return
//	 */
//	private Set<String> maxCode;
//	maxCode = new HashSet<String>();
//	maxCode.add("4052"); // 存放20元流量封顶的产品编码
//	maxCode.add("1277");
//	maxCode.add("5652");
//	maxCode.add("4051");
//	maxCode.add("4825");
	private boolean checkMaxFlux(String packageCode,QRY040084Result res)
	{
		//判断是否是否匹配编码
		if(maxCode.contains(packageCode)||maxCode.contains(packageCode.substring(packageCode.length()-4, packageCode.length())))
		{
			return true;
		}
		return false;
	}

}
