package com.xwtech.xwecp.service.logic.invocation; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProdCallInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010019Result;
import com.xwtech.xwecp.service.logic.pojo.SPProdInfo;
import com.xwtech.xwecp.service.logic.pojo.SpDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 根据meta文件设置的参数
 * 解析通信量和SP查询
 * @author 丁亮
 * Apr 19, 2011
 */
public class QueryNewSpandProduseInvocation extends BaseInvocation implements ILogicalService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(QueryNewRealTimeBillingInvocation.class);

	private ParseXmlConfig config;
	
	public QueryNewSpandProduseInvocation() {
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY010019Result res = new QRY010019Result();
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
			if (null != rspXml && !"".equals(rspXml)) {
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
					List<Element> childElmProdCallInfos = this.config.getContentList(root, "prodcallinfo");		// 取产品通信量信息列表
					
					List<ProdCallInfo> prodCallInfos = new ArrayList<ProdCallInfo>();
					Map<String,ProdCallInfo> prodMap = new HashMap<String,ProdCallInfo>();
					List<Element> prodinfo = this.config.getContentList(root, "prodinfo");	
					List<SPProdInfo> prodList = new ArrayList<SPProdInfo>();
					if(null != prodinfo && !"".equals(prodinfo))
					{
						for(Element pIf : prodinfo)
						{
							SPProdInfo spInfo = new SPProdInfo();
							spInfo.setDateunit(pIf.getChildText("dataunit"));
							spInfo.setFreeitemDesc(pIf.getChildText("freeitem_desc"));
							spInfo.setFreeitemId(pIf.getChildText("freeitem_id"));
							spInfo.setFreeitemValue(pIf.getChildText("freeitem_value"));
							spInfo.setProdId(pIf.getChildText("typeid"));
							spInfo.setTypeId(pIf.getChildText("dataunit"));
							
							prodList.add(spInfo);
						}
					}
					res.setProdInfoList(prodList);
					
					for (Element elm : childElmProdCallInfos) {						
						/**
						 * 取资费子项编码
						 * 1：本地主叫(分钟),2 本地被叫(分钟),3 国内长途(分钟),4 国内漫游(分钟),5 国际长途/漫游(分钟),6 国际长途/漫游(分钟),7 短信(条),8 彩信(条),9 GPRS流量(kb),10 上网本流量(kb)
						 * 12 WLAN时长(分钟),13 个人宽带(分钟),14 彩铃(个),15 国内漫游（主叫）(分钟),16 国内漫游（被叫）(分钟),17 GPRS时长（分钟）,18 WLAN流量（kb）,19 个人宽带流量（kb）
						 * 20 上网本时长（分钟）
						 */
						String calltip = elm.getChildText("calltip");								// 获取子项量编码						
						if(!calltip.equalsIgnoreCase("1") && !calltip.equalsIgnoreCase("2") && !calltip.equalsIgnoreCase("3") &&
								!calltip.equalsIgnoreCase("4") && !calltip.equalsIgnoreCase("5") && !calltip.equalsIgnoreCase("20") &&
								!calltip.equalsIgnoreCase("7") && !calltip.equalsIgnoreCase("8") && !calltip.equalsIgnoreCase("9") && 
								!calltip.equalsIgnoreCase("10") && !calltip.equalsIgnoreCase("12") && !calltip.equalsIgnoreCase("13") &&
								!calltip.equalsIgnoreCase("14") && !calltip.equalsIgnoreCase("15") && !calltip.equalsIgnoreCase("16") &&
								!calltip.equalsIgnoreCase("17") && !calltip.equalsIgnoreCase("18") && !calltip.equalsIgnoreCase("19")){
							continue;
						}
						
						String code = elm.getChildText("package_package_code");						// 获取编码						
						String calltipName = elm.getChildText("package_freeitem_name");				// 获取子项量名称
						int freeVal = Integer.parseInt(elm.getChildText("calldata"));				// 实扣的金额	
						ProdCallInfo prodCallInfo;
						if (prodMap.containsKey(code)) {
							prodCallInfo = prodMap.get(code);
						} else {
							prodCallInfo = new ProdCallInfo();
							String name = elm.getChildText("package_package_name");					// 获取套餐名称
							prodCallInfo.setPackName(name);							
							int order = Integer.parseInt(elm.getChildText("package_display_order"));
							prodCallInfo.setOrderNo(order);
							prodMap.put(code, prodCallInfo);
						}
						prodCallInfo.setPackId(code);
						prodCallInfo.setFreeitemName(calltipName);
						prodCallInfo.setShowType(elm.getChildText("package_show_type"));
						String totalVal = elm.getChildText("package_freeitem_value");
						
						// 本地主叫
						if ("1".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getBdzj() == null)
								prodCallInfo.setBdzj(freeVal);
							else
								prodCallInfo.setBdzj(prodCallInfo.getBdzj() + freeVal);
							//总通话时长
							if( null == prodCallInfo.getCallTotal())
							{
								prodCallInfo.setCallTotal(Integer.parseInt(totalVal));
							}
						}
						
						// 本地被叫
						if ("2".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getBdbj() == null)
								prodCallInfo.setBdbj(freeVal);
							else
								prodCallInfo.setBdbj(prodCallInfo.getBdbj() + freeVal);
						}
						
						// 国内长途
						if ("3".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGnct() == null)
								prodCallInfo.setGnct(freeVal);
							else
								prodCallInfo.setGnct(prodCallInfo.getGnct() + freeVal);
							//总通话时长
							if( null == prodCallInfo.getCallTotal())
							{
								prodCallInfo.setCallTotal(Integer.parseInt(totalVal));
							}
						}
						
						// 国内漫游gnmy
						if ("4".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGnmy() == null)
								prodCallInfo.setGnmy(freeVal);
							else
								prodCallInfo.setGnmy(prodCallInfo.getGnmy() + freeVal);
							//总通话时长
							if( null == prodCallInfo.getCallTotal())
							{
								prodCallInfo.setCallTotal(Integer.parseInt(totalVal));
							}
						}
						
						// 国际长途漫游gjctmy
						if ("5".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGjctmy() == null)
								prodCallInfo.setGjctmy(freeVal);
							else
								prodCallInfo.setGjctmy(prodCallInfo.getGjctmy() + freeVal);
						}
						
						// 短信
						if ("7".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getDx() == null)
								prodCallInfo.setDx(freeVal);
							else
								prodCallInfo.setDx(prodCallInfo.getDx() + freeVal);
							//总短信条数
							if( null == prodCallInfo.getDxTotal())
							{
								prodCallInfo.setDxTotal(Integer.parseInt(totalVal));
							}
						}
						
						// 彩信sx
						if ("8".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getSx() == null)
								prodCallInfo.setSx(freeVal);
							else
								prodCallInfo.setSx(prodCallInfo.getSx() + freeVal);
							//总彩信条数
							if( null == prodCallInfo.getSxTotal())
							{
								prodCallInfo.setSxTotal(Integer.parseInt(totalVal));
							}
						}
												
						// GPRS流量
						if ("9".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGprs() == null)
								prodCallInfo.setGprs(freeVal);
							else
								prodCallInfo.setGprs(prodCallInfo.getGprs() + freeVal);
							//GPRS流量
							if( null == prodCallInfo.getGprsTotal())
							{
								prodCallInfo.setGprsTotal(Integer.parseInt(totalVal));
							}
						}
						
						// 上网本流量swbll
						if ("10".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getSwbll() == null)
								prodCallInfo.setSwbll(freeVal);
							else
								prodCallInfo.setSwbll(prodCallInfo.getSwbll() + freeVal);
						}
						
						// wlan时长walnsc分钟
						if ("12".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getWalnsc() == null)
								prodCallInfo.setWalnsc(freeVal);
							else
								prodCallInfo.setWalnsc(prodCallInfo.getWalnsc() + freeVal);
							//wlan总时长
							if( null == prodCallInfo.getWlanTotal())
							{
								prodCallInfo.setWlanTotal(Integer.parseInt(totalVal));
							}
						}
						
						// 个人宽带grkd分钟
						if ("13".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGrkd() == null)
								prodCallInfo.setGrkd(freeVal);
							else
								prodCallInfo.setGrkd(prodCallInfo.getGrkd() + freeVal);
						}
						
						// 彩铃cl
						if ("14".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getCl() == null)
								prodCallInfo.setCl(freeVal);
							else
								prodCallInfo.setCl(prodCallInfo.getCl() + freeVal);
						}
						
						
						// 国内漫游主叫
						if ("15".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGnmyzj() == null)
								prodCallInfo.setGnmyzj(freeVal);
							else
								prodCallInfo.setGnmyzj(prodCallInfo.getGnmyzj() + freeVal);
						}
						
						// 国内漫流被叫
						if ("16".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGnmybj() == null)
								prodCallInfo.setGnmybj(freeVal);
							else
								prodCallInfo.setGnmybj(prodCallInfo.getGnmybj() + freeVal);
						}
						
						// GPRS时长gprssc分钟
						if ("17".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getGprssc() == null)
								prodCallInfo.setGprssc(freeVal);
							else
								prodCallInfo.setGprssc(prodCallInfo.getGprssc() + freeVal);
						}
												
						// WLAN流量
						if ("18".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getWlan() == null){
								prodCallInfo.setWlan(freeVal);
								//修改用于区分wlan时长与wlan流量 showType=18 表示wlan流量
								prodCallInfo.setShowType("18");
								//end
							}
							else{
								prodCallInfo.setWlan(prodCallInfo.getWlan() + freeVal);
							}
						}
						
						// 宽带流量
						if ("19".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getKd() == null)
								prodCallInfo.setKd(freeVal);
							else
								prodCallInfo.setKd(prodCallInfo.getKd() + freeVal);
						}
						
						// 上网本
						if ("20".equalsIgnoreCase(calltip)) {
							if(prodCallInfo.getSwb() == null)
								prodCallInfo.setSwb(freeVal);
							else
								prodCallInfo.setSwb(prodCallInfo.getSwb() + freeVal);
						}
					}
					
					prodCallInfos.addAll(prodMap.values());				// 添加实体至List列表里					
					res.setProdCallInfoList(prodCallInfos);
					
					/**
					 * 取第二层节点
					 * 取产品通信量信息列表
					 */
					List<Element> childElmSpDetails = this.config.getContentList(root, "spdetail_list");		// 取产品通信量信息列表
					List<SpDetail> spDetails = new ArrayList<SpDetail>();
					for (Element elm : childElmSpDetails) {
						Element e = (Element) elm.getChild("spdetail");
						// 只展示为0的类型
						String spType = e.getChildText("sptype");
						if(!spType.equals("0")){
							continue;
						}
						
						SpDetail spDetail = new SpDetail();
						spDetail.setSpName(e.getChildText("spname"));														// 服务商名称
						spDetail.setBusCode(e.getChildText("buscode"));														// 服务代码
						spDetail.setBusName(e.getChildText("busname"));														// 订购业务名称
						spDetail.setFee(e.getChildText("fee") == "" ? null : Long.parseLong(e.getChildText("fee")));		// 费用金额
						spDetail.setFeeType(e.getChildText("feetype"));														// 费用类型
						spDetail.setSvcCode(e.getChildText("svccode"));														// 服务代码(业务端口)
						spDetail.setUseType(e.getChildText("usetype"));														// 使用方式
						spDetail.setSpType(e.getChildText("sptype") == "" ? null : Long.parseLong(e.getChildText("sptype")));	// 0：普通SP(代收费业务)，1：自有服务类SP(自有增值业务)

						spDetails.add(spDetail); // 添加SP服务实体至列表
					}
					
					res.setSpDetailList(spDetails);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			res.setResultCode("1"); // 失败
		}
		return res;
	}
	
	/**
	 * 设置结果信息
	 * @param res - 实体类
	 * @param resp_code - 返回代码
	 * @param xmlName - xml报文
	 */
	public void getErrInfo(QRY010019Result res, String resp_code, String xmlName) {
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
