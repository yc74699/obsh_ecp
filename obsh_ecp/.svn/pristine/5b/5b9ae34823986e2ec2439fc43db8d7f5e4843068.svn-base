package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040011Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateOperator;

/**
 * 查询个人、集团套餐信息 
 * @author yuantao
 * 2009-12-04
 */
public class FindPackageInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(FindPackageInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public FindPackageInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY040011Result res = new QRY040011Result();
		List<BossParmDT> parList = null;
		List<PkgDetail> reList = new ArrayList<PkgDetail>();
		String reqXml = "";
		String rspXml = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");

		PkgDetail dt = null;
		int type = 1;
		String oTime = "";
		String biz_id = "";
		ErrorMapping errDt = null;
		
		try
		{
			//根据业务编码获取其子业务所对应的BOSS实现
			for (RequestParameter r : params)
			{
				/*if ("bizId".equals(r.getParameterName()))
				{
					bizId = String.valueOf(r.getParameterValue());
					parList = this.wellFormedDAO.getSubBossParmList(bizId);
				}*/
				if ("type".equals(r.getParameterName()))
				{
					type = Integer.parseInt(String.valueOf(r.getParameterValue()));
				}
			}
			
			//查询个人套餐信息
			if (1 == type)
			{
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
				logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(params)));
					logger.debug(" ====== 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						String resp_code = root.getChild("response").getChildText("resp_code");
						String resp_desc = root.getChild("response").getChildText("resp_desc");
						errDt = this.wellFormedDAO.transBossErrCode("QRY040011", "cc_find_package_62_TC", resp_code);
						if (null != errDt)
						{
							resp_code = errDt.getLiErrCode();
							resp_desc = errDt.getLiErrMsg();
						}
						String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
						
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
						
						if ("0000".equals(resp_code))
						{
							List package_code = root.getChild("content").getChildren("package_code");
							if (null != package_code && package_code.size() > 0)
							{
								parList = new ArrayList();
								for (int i = 0; i < package_code.size(); i++)
								{
									Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");
									
									String endDate = p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll("");
									if (StringUtils.isNotBlank(endDate) 
										&& DateOperator.fromChar14(endDate).getTime() < new Date().getTime()) {
										continue;
									}
									dt = new PkgDetail ();
									dt.setPkgId(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
									//套餐大类
									dt.setPackageType(p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll(""));
									dt.setPkgLevel(p.matcher(cplanpackagedt.getChildText("package_level")).replaceAll(""));
									oTime = p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll("");
									//状态
									long disDay = Long.parseLong(this.getDistanceDT(this.getTodayChar14(), oTime, "d"));
									dt.setPkgState(disDay > 0?"预约套餐":"当前套餐");
									//套餐类别
									biz_id = p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll("");
									dt.setPkgType(this.getPkgType(biz_id, accessId, params));
									//套餐名称
									dt.setPkgName(p.matcher(cplanpackagedt.getChildText("package_name")).replaceAll(""));
									//开始日期
									dt.setBeginDate(oTime);
									//结束日期
									dt.setEndDate(endDate);
									//资费描述 个人无资费信息
									dt.setFeeDesc("");
									dt.setState(p.matcher(cplanpackagedt.getChildText("package_state")).replaceAll(""));
									
									//TODO 从数据库取可关闭的套餐列表
									reList.add(dt);
									BossParmDT bossDt = new BossParmDT();
									bossDt.setParm1(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
									parList.add(bossDt);
								}
								if (null != reList && reList.size() > 0)
								{
									this.getPackByCode(accessId, config, params, parList, reList, res);
								}

							}
						}
					}
					else
					{
						setErrorResult(res);
					}
				}
			}
			else if (2 == type)  //集团套餐
			{
				this.getGroupackage(accessId, config, params, reList, res);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		res.setPkgDetail(reList);
		return res;
	}
	
	/**
	 * 查询集团信息
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public void getGroupackage (String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<PkgDetail> reList, QRY040011Result res)
	{
		PkgDetail dt = null;
		String rspXml = "";
		List<PkgDetail> pkgList = null;
		List<BossParmDT> parList = null;
		BossParmDT bossDt = null;
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cgetgroupackage_552", params), 
					 accessId, "cc_cgetgroupackage_552", this.generateCity(params)));
			logger.debug(" ====== 集团套餐返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("QRY040011", "cc_cgetgroupackage_552", resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}
				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (null != resp_code && ("0000".equals(resp_code)))//邵琪 2010-03-09 修改
				{
					res.setResultCode("0");
					List userList = root.getChild("content").getChildren("package_user_id");
					List nameList = root.getChild("content").getChildren("cust_name");
					
					if (null != userList && userList.size() > 0 && null != nameList && nameList.size() > 0)
					{
						pkgList = new ArrayList();
						parList = new ArrayList();
						
						for (int i = 0; i < userList.size(); i++)
						{
							if (i <= nameList.size())
							{
								Element cplanpackagedt = ((Element)userList.get(i)).getChild("cplanpackagedt");
								String mapName = ((Element)nameList.get(i)).getChildText("cust_name");
								
								if (null != cplanpackagedt && null != mapName)
								{
									dt = new PkgDetail();
									dt.setPkgId(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
									bossDt = new BossParmDT();
									long disDay = Long.parseLong(this.getDistanceDT(this.getTodayChar14(), 
											p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll("")
											, "d"));
									dt.setPkgState(disDay > 0?"预约套餐":"当前套餐");
									dt.setPkgType(mapName.trim());
									dt.setPackageType(p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll(""));
									dt.setPkgName(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
									dt.setBeginDate(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll(""));
									dt.setEndDate(p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll(""));
									bossDt.setParm1(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
									parList.add(bossDt);
									pkgList.add(dt);
									reList.add(dt);
								}
							}
						}
						
						if (null != parList && parList.size() > 0)
						{
							this.getPackByCode(accessId, config, params, parList, pkgList, res);
						}
					}
				}
				if("-2652".equals(resp_code)){//邵琪 2010-03-09 修改
					res.setResultCode("0");
				}
			}
			else
			{
				setErrorResult(res);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 根据套餐代码查询套餐配置信息取
	 * @param accessId
	 * @param config
	 * @param params
	 * @param parList
	 * @param pkgList
	 */
	public void getPackByCode (String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<BossParmDT> parList, 
			List<PkgDetail> pkgList, QRY040011Result res)
	{
		String rspXml = "";
		String pkgCode = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		ErrorMapping errDt = null;
		
		try
		{
			if (null != parList && parList.size() > 0)
			{
				RequestParameter par = new RequestParameter();
				par.setParameterName("codeCount");
				par.setParameterValue(parList);
				params.add(par);
			}
			
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params), 
					 accessId, "cc_cgetpackbycode_605", this.generateCity(params)));
			logger.debug(" ====== 查询套餐配置信息发送报文 ======\n" + this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params));
			logger.debug(" ====== 查询套餐配置信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("QRY040011", "cc_cgetpackbycode_605", resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}
				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (null != resp_code && "0000".equals(resp_code))
				{
					List packList = root.getChild("content").getChildren("productbusinesspackage_package_code");
					List idList = root.getChild("content").getChildren("productbusiness_business_id");
					
					if (null != packList && packList.size() > 0)
					{
						for (int i = 0; i < packList.size(); i++)
						{
							Element pDt = ((Element)packList.get(i)).getChild("cproductbusinesspackagedt");
							if (null != pDt)
							{
								pkgCode = p.matcher(pDt.getChildText("productbusinesspackage_package_code")).replaceAll("");
								for (PkgDetail pkg : pkgList)
								{
									if (pkgCode.equals(pkg.getPkgId()))
									{
										pkg.setPkgName(p.matcher(pDt.getChildText("productbusinesspackage_package_name")).replaceAll(""));
										pkg.setFeeDesc(p.matcher(pDt.getChildText("productbusinesspackage_package_desc")).replaceAll(""));
										pkg.setTariff(p.matcher(pDt.getChildText("productbusinesspackage_tariff")== null ? "" : pDt.getChildText("productbusinesspackage_tariff")).replaceAll(""));
									}
								}
							}
						}
					}
				}
			}
			else
			{
				setErrorResult(res);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 获取套餐类型名称
	 * @param biz_id
	 * @param accessId
	 * @param params
	 * @return
	 */
	public String getPkgType (String biz_id, String accessId, List<RequestParameter> params)
	{
		String rspXml = "";
		String bizName = "";
		
		try
		{
			if (null != biz_id && !"".equals(biz_id))
			{
				bizName = XWECPApp.redisCli.get("cc_cgetbizname"+biz_id);
				
				if(null == bizName || "".equals(bizName))
				{
					RequestParameter par = new RequestParameter();
					par.setParameterName("biz_id");
					par.setParameterValue(biz_id);
					params.add(par);
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(
							 this.bossTeletextUtil.mergeTeletext("cc_cgetbizname_713", params), 
							 accessId, "cc_cgetbizname_713", this.generateCity(params)));
					
					if (null != rspXml && !"".equals(rspXml))
					{
						Element bizRoot = this.getElement(rspXml.getBytes());
						String respCode ="";
						if(null != bizRoot && bizRoot.getChild("response") != null)
						{
							respCode = bizRoot.getChild("response").getChildText("resp_code");
							if ("0000".equals(respCode))
							{
								bizName = bizRoot.getChild("content").getChildText("biz_name").trim();
								if (null != bizName && !"".equals(bizName))
								{
									XWECPApp.redisCli.set(("cc_cgetbizname"+biz_id), bizName);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			bizName = "";
			logger.error(e, e);
		}
		return bizName;
	}
	
	/**
	 * 从Boss返回的报文体中取出String类型值
	 * @param bossMap  boss值Map
	 * @param key 键值
	 * @return
	 */
	protected String getString(Map bossMap, String key) {
		if (bossMap != null) {
			Object o = bossMap.get(key);
			if (o != null) {
				return o.toString().trim();
			}
		}
		return "";
	}
	
	/**
	 * 比对两个时间间隔
	 * @param startDateTime 开始时间
	 * @param endDateTime 结束时间
	 * @param distanceType 计算间隔类型 天d 小时h 分钟m 秒s
	 * @return
	 */
    public static String getDistanceDT(String startDateTime,String endDateTime,String distanceType){
        String strResult="";
        long lngDistancVal=0;
        try{
            SimpleDateFormat tempDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date startDate=tempDateFormat.parse(startDateTime);
            Date endDate=tempDateFormat.parse(endDateTime);
            if(distanceType.equals("d")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            }else if(distanceType.equals("h")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
            }else if(distanceType.equals("m")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60);
            }else if(distanceType.equals("s")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000);
            }
            strResult=String.valueOf(lngDistancVal);
        }catch(Exception e){
        	strResult="0";
        }
        return strResult;
    }
    
    /**
	 * 返回 年月日小时分秒
	 * @return
	 */
	public static String getTodayChar14(){
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}
	
	/**
	 * 把14字符的格式日期字符串转换成日期
	 * @param char14  14字符的格式日期字符串，如2008-01-08
	 * @return Date
	 */
	public static String fromChar14toStandard(String charDateTime)
	{
		
		String  strResult = "";
		if(null != charDateTime && !"".equals(charDateTime) && charDateTime.length() >= 8){
			strResult=charDateTime.substring(0, 4) + "-"
			+ charDateTime.substring(4, 6) + "-"
		    + charDateTime.substring(6, 8);
		}
		return strResult;
	}
}
