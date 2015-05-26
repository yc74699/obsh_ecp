package com.xwtech.xwecp.service.logic.invocation; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FmyProdCallInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010023Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 根据meta文件设置的参数
 * 解析家庭套餐通信量明细查询
 * @author 丁亮
 * Apr 19, 2011
 */
public class QueryNewFmySpandProduseInvocation extends BaseInvocation implements ILogicalService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(QueryNewRealTimeBillingInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public QueryNewFmySpandProduseInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY010023Result res = new QRY010023Result();
		res.setResultCode("0"); // 成功
		String reqXml = ""; 	// 发送报文
		String rspXml = ""; 	// 接收报文
		String resp_code = ""; 	// 返回码
		try {
			// 初始化准备
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_acqryspandproduse_309", params);
			logger.debug(" ====== 查询用户在用/(新)账单查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_acqryspandproduse_309", super.generateCity(params)));
				logger.debug(" ====== 查询用户在用/(新)账单查询 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) 
			{
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(res, resp_code, "ac_acqryspandproduse_309");
				
				Element resRoot = this.config.getElement(root, "content");
				// 成功
				if ("0000".equals(resp_code)) 
				{
					// 取第一层节点
					res.setSubSid(this.config.getChildText(resRoot, "subsid")); 							// 用户号
					res.setCycle(this.config.getChildText(resRoot, "cycle") == null ? null : Integer.parseInt(this.config.getChildText(resRoot, "cycle")));			// 帐期
					
					// 取第二层节点
					List<Element> childElmProdCallInfos = this.config.getContentList(root, "prodcallinfo");		// 取家庭产品通信量信息列表
					List<FmyProdCallInfo> fmyProdCallInfos = new ArrayList<FmyProdCallInfo>();
					
//					Map<String,FmyProdCallInfo> prodMap = new HashMap<String,FmyProdCallInfo>();
					
					Map<String,Map> subMap = new HashMap<String,Map>(); 
					
					for (Element elm : childElmProdCallInfos)
					{						
						/**
						 * 取资费项编码
						 * 701：成员间通话免费（分钟）
						 * 801：成员共享免费语音（分钟）
						 * 802：成员共享免费WLAN（分钟）
						 * 803：成员共享免费省内WLAN&GPRS（分钟）
						 * 804：成员共享免费漫游WLAN&GPRS（分钟）
						 */					
						String freeitemId = elm.getChildText("freeitem_id");						// 获取资费项编码
						if(!freeitemId.equalsIgnoreCase("701") && !freeitemId.equalsIgnoreCase("801") && !freeitemId.equalsIgnoreCase("802") &&
								!freeitemId.equalsIgnoreCase("803") && !freeitemId.equalsIgnoreCase("804"))
						{
							continue;
						}
						
						String code = elm.getChildText("package_package_code");						// 获取编码
						String calltipName = elm.getChildText("package_freeitem_name");				// 获取子项量名称
						int pgkVal = Integer.parseInt(elm.getChildText("calldata"));				// 实扣的费用
						String calltipId = elm.getChildText("calltip");								// 获取编号
						String subId = elm.getChildText("userid");
						
						FmyProdCallInfo fmyProdCallInfo;
						Map<String,FmyProdCallInfo> prodMap ;
						
						//如果用户id相同和subMap的key相同就不生成新的prodMap，否则就生成新的map
						if(subMap.containsKey(subId))
						{
							prodMap = subMap.get(subId);
						}
						else
						{
							prodMap = new HashMap<String,FmyProdCallInfo>();
							subMap.put(subId, prodMap);
						}
						
						if (prodMap.containsKey(code))
						{
							fmyProdCallInfo = prodMap.get(code);
						} 
						else
						{
							fmyProdCallInfo = new FmyProdCallInfo();
							String name = elm.getChildText("package_package_name");					// 获取套餐名称
							fmyProdCallInfo.setPackName(name);
							prodMap.put(code, fmyProdCallInfo);
							fmyProdCallInfo.setUserId(subId); 
							fmyProdCallInfo.setPhoneNum(elm.getChildText("servnumber")); //把手机号码加入对象
						}
						
						fmyProdCallInfo.setFreeitemName(calltipName);
//						fmyProdCallInfo.setPhoneNum(elm.getChildText("servnumber")); //把手机号码加入对象
						
						
						// 成员间通话免费
						if ("701".equalsIgnoreCase(freeitemId))
						{
							if(fmyProdCallInfo.getCyjth() == null)
							{
								fmyProdCallInfo.setCyjth(pgkVal);
							}
							else
							{
								fmyProdCallInfo.setCyjth(fmyProdCallInfo.getCyjth() + pgkVal);
							}
						}
						
						// 成员共享免费语音
						if ("801".equalsIgnoreCase(freeitemId))
						{
							// 如果是国内漫游（主叫|被叫）不取值，应该会有4这个值
							if("15".equals(calltipId) || "16".equals(calltipId))
							{
								continue;
							}
							
							if(fmyProdCallInfo.getCyjgxyy() == null)
							{
								fmyProdCallInfo.setCyjgxyy(pgkVal);
							}
							else
							{
								fmyProdCallInfo.setCyjgxyy(fmyProdCallInfo.getCyjgxyy() + pgkVal);
							}
						}
						
						// 成员共享免费WLAN
						if ("802".equalsIgnoreCase(freeitemId)) 
						{
							if(fmyProdCallInfo.getCyjgxwlan() == null)
							{
								fmyProdCallInfo.setCyjgxwlan(pgkVal);
							}
							else
							{
								fmyProdCallInfo.setCyjgxwlan(fmyProdCallInfo.getCyjgxwlan() + pgkVal);
							}
						}
						
						// 成员共享免费省内WLAN&GPRS
						if ("803".equalsIgnoreCase(freeitemId)) 
						{
							if(fmyProdCallInfo.getCyjgxwlangprs() == null)
							{
								fmyProdCallInfo.setCyjgxwlangprs(pgkVal);
							}
							else
							{
								fmyProdCallInfo.setCyjgxwlangprs(fmyProdCallInfo.getCyjgxwlangprs() + pgkVal);
							}
						}
						
						// 成员共享免费漫游WLAN&GPRS
						if ("804".equalsIgnoreCase(freeitemId))
						{
							if(fmyProdCallInfo.getCyjgxmy() == null)
								fmyProdCallInfo.setCyjgxmy(pgkVal);
							else
								fmyProdCallInfo.setCyjgxmy(fmyProdCallInfo.getCyjgxmy() + pgkVal);
						}
					}
					//遍历map把里面的数据全部放入到要返回的结果集中
					for(Map.Entry<String,Map> key:subMap.entrySet())
					{
						fmyProdCallInfos.addAll(key.getValue().values());	
					}
							// 添加实体至List列表里					
					res.setFmyProdCallInfoList(fmyProdCallInfos);
				}
			}
		} 
		catch (Exception e) 
		{
			logger.error(e, e);
			res.setResultCode("1");
		}
		return res;
	}
	
	/**
	 * 设置结果信息
	 * @param res - 实体类
	 * @param resp_code - 返回代码
	 * @param xmlName - xml报文
	 */
	public void getErrInfo(QRY010023Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");
			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY010019", xmlName, resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode()); 	// 设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg()); 	// 设置错误信息
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
