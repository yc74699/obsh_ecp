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
import com.xwtech.xwecp.service.logic.pojo.PkgInfoBean;
import com.xwtech.xwecp.service.logic.pojo.QRY010029Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 根据meta文件设置的参数
 * 解析家庭套餐通信量明细查询
 * @author Mr Ou
 * Apr 19, 2014
 */
public class QueryNewWestFmySpandProduseInvocation extends BaseInvocation implements ILogicalService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(QueryNewRealTimeBillingInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public QueryNewWestFmySpandProduseInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY010029Result res = new QRY010029Result();
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
					List<Element> prodinfo = this.config.getContentList(root, "prodinfo"); //家庭账单名称
					
					List<List<String>> dateLists = new ArrayList<List<String>>(); //存放家庭账单列表信息
					List<PkgInfoBean> pkgInfoList = res.getPkgInfoList();  //存放家庭账单名称
					//设置家庭账单
					PkgInfoBean pkgs = new PkgInfoBean();
					pkgs.setFreeitem_id("1");
					pkgs.setFreeitem_desc("家庭成员");
					pkgInfoList.add(pkgs);
					for (Element elm : prodinfo){
						String freeitemId = elm.getChildText("freeitem_id");
						String freeitemDesc = elm.getChildText("freeitem_desc");
						PkgInfoBean pkgInfo = new PkgInfoBean();
						pkgInfo.setFreeitem_id(freeitemId);
						pkgInfo.setFreeitem_desc(freeitemDesc);
						pkgInfoList.add(pkgInfo);
					}
					//将家庭账单列表中的手机号码、产品ID拼接字符串作为主键用来存放套餐使用量。
					Map<String,String> map = new HashMap<String,String>();
					for (Element elm : childElmProdCallInfos){
						String freeitemId = elm.getChildText("freeitem_id");
						String servnumber = elm.getChildText("servnumber");
						String str = freeitemId+","+servnumber;
						map.put(str, "0");
					}
					//设置套餐使用量，根据手机号码、产品ID分类
					for (Element elm : childElmProdCallInfos){
						int temval = Integer.parseInt(elm.getChildText("calldata"));
						String freeitemId = elm.getChildText("freeitem_id");
						String servnumber = elm.getChildText("servnumber");
						String calltipId = elm.getChildText("calltip");
						String str = freeitemId+","+servnumber;
                        //如果是国内漫游（主叫|被叫）不取值
						if("801".equals(freeitemId) && ("15".equals(calltipId) || "16".equals(calltipId))){
							continue;
						}
						//计算由手机号码、产品ID分类得使用量值
						if(map.containsKey(str)){
							int tem = Integer.parseInt(map.get(str));
							tem = temval+tem;
							map.put(str, String.valueOf(tem));
						}
					}
					//设置由手机号码为主键对象List的Map结合
					Map<String,List<String>> m = new HashMap<String,List<String>>();
					for (Element elm : childElmProdCallInfos){
						List<String> listTem = new ArrayList<String>();
						String servnumber = elm.getChildText("servnumber");
						m.put(servnumber, listTem);
						
					}
					//将报文返回的集合，由手机号码为主键存放到指定的List中
					for(String nMark :m.keySet()){
						//加入家庭成员号码
						 m.get(nMark).add(nMark);
						for(int i = 1; i < pkgInfoList.size();i++){
							
							String strMark = pkgInfoList.get(i).getFreeitem_id()+","+nMark;
							//设置家庭成员信息的值
							if(map.containsKey(strMark)){
								m.get(nMark).add(map.get(strMark));
							}else{
								m.get(nMark).add("0");
							}
						}
						dateLists.add(m.get(nMark));
					}
					res.setPkgInfoList(pkgInfoList);
					res.setDateStrList(dateLists);
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
	public void getErrInfo(QRY010029Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");
			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY010029", xmlName, resp_code);
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
