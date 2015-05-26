package com.xwtech.xwecp.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import com.xwtech.xwecp.dao.TeletextDispatchDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.pojo.NumberSegment;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.web.XWECPWebApp;


public class DefaultCommunicateAdapter  implements ICommunicateAdapter
{
	private static final Logger logger = Logger.getLogger(DefaultCommunicateAdapter.class);
	
	private ICommunicator communicator;
	
	private Map<String,ICommunicator> communicatorMap; 
	
	private TeletextDispatchDAO teletextDispatchDAO; 
	
	private static Map<String,String> bossAddr = new HashMap<String,String>();
	
	static{
		bossAddr.put("boss1", "");
		bossAddr.put("boss111", "");
		bossAddr.put("boss112", "");
		bossAddr.put("boss113", "");
		bossAddr.put("boss114", "");
		bossAddr.put("boss115", "");
		bossAddr.put("boss116", "");
		bossAddr.put("boss117", "");
		bossAddr.put("boss118", "");
		bossAddr.put("boss119", "");
		bossAddr.put("boss120", "");
		bossAddr.put("boss121", "");
		bossAddr.put("boss122", "");
		bossAddr.put("boss123", "");
	}
	
	
	public ICommunicator getCommunicator()
	{
		return communicator;
	}

	public void setCommunicator(ICommunicator communicator)
	{
		this.communicator = communicator;
	}

	public Map<String, ICommunicator> getCommunicatorMap() {
		return communicatorMap;
	}

	public void setCommunicatorMap(Map<String, ICommunicator> communicatorMap) {
		this.communicatorMap = communicatorMap;
	}

	public TeletextDispatchDAO getTeletextDispatchDAO() {
		return teletextDispatchDAO;
	}

	public void setTeletextDispatchDAO(TeletextDispatchDAO teletextDispatchDAO) {
		this.teletextDispatchDAO = teletextDispatchDAO;
	}
	

	/**
	 * 需要转发的接口根据号段对应
	 */
	private static Map<String,String> AREACODES = new HashMap<String,String>();
	
	/**
	 * 抢流量红包四个接口对应的boss接口命令字
	 */
	private static StringBuffer BOSSFLUXCODES =  new StringBuffer();
	
	/**
	 * 抢流量红包地址转发
	 */
	private static StringBuffer BOSSCITYS_ONE = new StringBuffer();
	
	/**
	 * 抢流量红包地址转发
	 */
	private static StringBuffer BOSSCITYS_TWO = new StringBuffer();
	
	/**
	 * 抢流量红包地址转发
	 */
	private static StringBuffer BOSSCITYS_THREE = new StringBuffer();
	
	/**
	 * 抢流量红包地址转发
	 */
	private static StringBuffer BOSSCITYS_FOUR = new StringBuffer();
	
	//转发的Boss6接口
	private static StringBuffer BOSS6CODES = new StringBuffer();
	
	static{
		/**平台转发开始**/
		//查询用户及客户资料
		AREACODES.put("@cc_cgetusercust_69@","msisdn");
			
		//用户登录
		AREACODES.put("@cc_cgetuserinfo_771@","msisdn");
		
		//归属地查询接口
		AREACODES.put("@cc_cgetnumseg_551@","nhnumbersegment_begin_msisdn");
		
		//充值卡充值校验
		BOSS6CODES.append("@ac_civrcheckuser_610@");
	
		//充值卡充值
		BOSS6CODES.append("@cc_csmsnhdeposit_421@");
		
		//密码重置
		BOSS6CODES.append("@cc_csetpassword_554@");
		
		//(新大陆抢流量红包)GPRS分套餐流量
		BOSSFLUXCODES.append("QUERYGPRSALLPKGFLUX");
		
		//(新大陆抢流量红包)GPRS流量查询
		BOSSFLUXCODES.append("QUERYGPRSFLUX");
		
		//(新大陆抢流量红包)GPRS日流量查询
		BOSSFLUXCODES.append("QUERYGPRSDAYFLUX");
		
		//(新大陆抢流量红包)实时积分查询
		BOSSFLUXCODES.append("GETAVAILINTEGRAL");
		
		//(新大陆抢流量红包)查询帐户余额   第二批割接
		BOSSFLUXCODES.append("cc_cgetusercustaccbalance");
		
		//(新大陆抢流量红包)实时帐单查询  第二批割接
		BOSSFLUXCODES.append("ac_acqryrealtimebill");
		
		//(新大陆抢流量红包)查询用户余额和积分M值信息  第二批割接
		BOSSFLUXCODES.append("cc_cgetuseraccscore");
		
		//(新大陆抢流量红包)套餐优惠查询  第二批割接
		BOSSFLUXCODES.append("ac_agetfreeitem");
		
		//(新大陆抢流量红包)查询专有账户余额 第二批割接
		BOSSFLUXCODES.append("cc_aqueryspaccbal");
		
		
		//新大陆的现网接口调用地址一库
		BOSSCITYS_ONE.append("20").append("23").append("22").append("21");
		
		//新大陆的现网接口调用地址二库
		BOSSCITYS_TWO.append("19").append("18").append("17").append("12");
		
		////新大陆的现网接口调用地址三库
		BOSSCITYS_THREE.append("16").append("15").append("14").append("13");
		
		//新大陆的现网接口调用地址四库
		BOSSCITYS_FOUR.append("11");
		
		
		/**平台转发结束**/
	}
	
	@SuppressWarnings("unchecked")
	public ICommunicator findCommunicatorForRequest(Object request)
	{
		
		ICommunicator communicator = null;
		String process_code = "";
		if(request instanceof IStreamableMessage){
			IStreamableMessage msg = (IStreamableMessage)request;
			
			//1.平台转发
			String processCode = null;
			try {
				if(msg instanceof StringTeletext){
					StringTeletext teletext = (StringTeletext) msg;
					
					//获取渠道编码
					String channelId = "";
					RemoteCallContext context = teletext.getContext();
					if (context != null) {
						channelId = context.getInvokeChannel();
					}
					String usercity = "";
					if(null != context)
					{
					   usercity = context.getUserCity();
					}
					if(usercity == "4"){
						return communicatorMap.get("boss4");
					}
					
					//流量银行地址
 			     	if("10".equals(usercity))
 			     	{
					   return communicatorMap.get("boss10");
					}
					//获取发送给boss的报文
					String message = teletext.getTeletext();
					
					//使用Dom4j解析
					Document doc = DocumentHelper.parseText(message.trim());
					
					//获取process_code的值
					List<Node> nodeList = doc.selectNodes("/operation_in/process_code");
					for(Node node : nodeList){
						processCode = '@' + node.getText();
						process_code = node.getText();
					}
					
					//获取sysfunc_id的值
					String sysfunc_id = "";
					nodeList = doc.selectNodes("/operation_in/sysfunc_id");
					for(Node node : nodeList){
						processCode += '_' + node.getText() + '@';
						sysfunc_id = node.getText();
					}
					
					//TODO 根据渠道编码, process code, function id, 查数据库, 修改传给BOSS的报文. 数据库表是: T_CHANNEL_TELETEXT_MODIFY
//					if (StringUtils.isNotBlank(channelId) && StringUtils.isNotBlank(process_code) && StringUtils.isNotBlank(sysfunc_id)) 
//					{
//						Map<String, String> configMap = teletextDispatchDAO.getChannelTeletextConfig(channelId, process_code, sysfunc_id);
//						if (configMap != null && !configMap.isEmpty()) 
//						{
//							Set set = configMap.keySet();
//							Iterator it = set.iterator();
//							while (it.hasNext())
//							{
//								String key = (String)it.next();
//								String value = configMap.get(key);
//								//找到匹配项重新赋值
//								doc.selectSingleNode(key).setText(value);
//							}
//							//请求报文修改后重写回去
//							message = doc.asXML();
//							teletext.setTeletext(message);
//						}
//					}

					//判断是否是需要转发的Boss6接口
					if(BOSS6CODES.toString().indexOf(processCode) != -1){
						showLog(processCode, "该接口需要平台转发", "boss6", "");
						return communicatorMap.get("boss6");
					}
					if(null != AREACODES.get(processCode))
					{
						String userCity = getUserCity(doc,AREACODES.get(processCode));
						if(null == userCity)
						{
							return communicatorMap.get("boss6");
						}
						msg.getContext().setUserCity(userCity);
					}
					//抢流量红包根据地市和BOSS接口分发请求URL 第一批Boss割接
					if(BOSSFLUXCODES.toString().indexOf(process_code) != -1 && "".equals(sysfunc_id))
					{
						String boss = "";
						//一批 BOSS负载均衡地址  一库
						if(BOSSCITYS_ONE.toString().indexOf(usercity) != -1)
						{
							boss = "boss001";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发 一批割接 一库", "boss001", "");
						}
						//一批 BOSS负载均衡地址  二库
						else if(BOSSCITYS_TWO.toString().indexOf(usercity) != -1)
						{
							boss = "boss002";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发  一批割接 二库", "boss002", "");
						}
						//一批 BOSS负载均衡地址  三库
						else if(BOSSCITYS_THREE.toString().indexOf(usercity) != -1)
						{
							boss = "boss003";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发  一批割接 三库", "boss003", "");
						}
						//一批 BOSS负载均衡地址  四库
						else if(BOSSCITYS_FOUR.toString().indexOf(usercity) != -1)
						{
							boss = "boss004";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发 一批割接  四库", "boss004", "");
						}
					    return communicatorMap.get(boss);
					}
					//抢流量红包根据地市和BOSS接口分发请求URL 第二批Boss割接
					else if(BOSSFLUXCODES.toString().indexOf(process_code) != -1 && "361".equals(sysfunc_id))
					{
						String boss = "";
						//二批 BOSS负载均衡地址  一库
						if(BOSSCITYS_ONE.toString().indexOf(usercity) != -1)
						{
							boss = "boss005";
							showLog(process_code, "抢流量红包BOSS割接接口ECP平台转发 二批割接Boss 一库", "boss005", "");
						}
						//二批 BOSS负载均衡地址  二库
						else if(BOSSCITYS_TWO.toString().indexOf(usercity) != -1)
						{
							boss = "boss006";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发 二批割接Boss 二库", "boss006", "");
						}
						//二批 BOSS负载均衡地址  三库
						else if(BOSSCITYS_THREE.toString().indexOf(usercity) != -1)
						{
							boss = "boss007";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发  二批割接Boss 三库", "boss007", "");
						}
						//二批 BOSS负载均衡地址  四库
						else if(BOSSCITYS_FOUR.toString().indexOf(usercity) != -1)
						{
							boss = "boss008";
							showLog(process_code, "抢流量红包BOSS接口ECP平台转发 二批割接Boss 四库", "boss008", "");
						}
					    return communicatorMap.get(boss);
					}
					
				}
			} 
			catch (Throwable e) {
				logger.error(e, e);
			}
			
			//2.默认处理
			String boss = null;
			String id = msg.getIdentity();
			Map<String, String> dispatchMap = teletextDispatchDAO.getTeletextDispatchMap();
			if(dispatchMap.get(id) != null){
				boss = dispatchMap.get(id);
				communicator = communicatorMap.get(boss);
			}
			
			if(communicator == null)
			{
				boss = "boss1";
				communicator = this.communicator;
			}
			//切换CRM紧急环境
			Map<String, String> urgentDispatchMap = teletextDispatchDAO.getUrgentDispatchMap(msg.getContext().getUserCity());
			if(null != urgentDispatchMap)
			{
				if(urgentDispatchMap.get(id) != null){
					//应急环境BOSS7
					boss = "boss7";
					communicator = communicatorMap.get(boss);
				}
			}
			
			//3.地市割接
			//是否需要进行割接处理
			boolean needCutCity = false;
			
			//对业务处理boss1, 清单查询boss2, 大批量查询boss3进行割接处理
//			if("boss1,boss2,boss3".indexOf(boss) != -1){
				if("boss1,boss2,boss3,boss7".indexOf(boss) != -1){
//				if ("boss2".equals(boss)) {
//					try {
//						if(msg instanceof StringTeletext)
//						{
//							StringTeletext teletext = (StringTeletext) msg;
							//获取发送给boss的报文
//							String message = teletext.getTeletext();
							
							//使用Dom4j解析
//							Document doc = DocumentHelper.parseText(message.trim());
							
							//获取process_code的值
//							String dbiTime = "";
//							List<Node> nodeList = doc.selectNodes("/operation_in/content/dbi_month");
//							for(Node node : nodeList){
//								dbiTime = node.getText();
//							}
//							String busi = "";
//							nodeList = doc.selectNodes("/operation_in/process_code");
//							for(Node node : nodeList){
//								busi = node.getText();
//							}
//							if (Integer.parseInt(dbiTime) <= 201105) {
//								if (!"ac_gsmcdr_video_query".equals(busi)) {
//									if (!"ac_montnetcdr_query".equals(busi)) {
//										if (!"ac_montnettcdr_query".equals(busi)) {
//											boss = "boss4";
//										}
//									}
//								}
//							}
//						}
//					} catch (Throwable e) {
//						logger.error(e, e);
//					}
//				}
				needCutCity = true;
			}
			
			//查询手机游戏点数消费记录使用大批量接口
			if(null != processCode && processCode.indexOf("cc_cgetgamepoint_701") != -1){
				needCutCity = true;
				boss = "boss3";
			}
			
//查询分布式清单菜单，查询
//			if(processCode.indexOf("QRYMENU") != -1||processCode.indexOf("DDBQUERY") != -1){
//				needCutCity = false;
//				boss = "boss1114";
//				//割接地市,获取该地市需要发送的地址
//				RemoteCallContext callContext = msg.getContext();
//				String city = null;
//				if(callContext != null){
//					city = callContext.getUserCity();
//				}
//				city = city == null ? "" : city;
//				showLog(processCode,"分布式详单割接", boss, city);
//				return communicatorMap.get(boss);
//			}
			//查询分布式清单菜单，查询
			if(null !=processCode && (processCode.indexOf("QRYMENU") != -1||processCode.indexOf("DDBQUERY") != -1)){
				needCutCity = false;
				if(dispatchMap.get(id) != null){
					boss = dispatchMap.get(id);
				        //割接地市,获取该地市需要发送的地址 4XX
				        RemoteCallContext callContext = msg.getContext();
			 	        String city = null;
				        if(callContext != null){
					      city = callContext.getUserCity();
				        }
				        city = city == null ? "" : city;
				        boss=boss+city;
				        
				        //添加節點 wang.h
				        String requestXmlByAdd = addParametersBoss(boss,msg);
				        if(requestXmlByAdd != null && msg instanceof StringTeletext) {
				        	((StringTeletext) msg).setTeletext(requestXmlByAdd);
				        }
				        
				        showLog(processCode,"分布式详单割接", boss, city);
				        return communicatorMap.get(boss);
				}
			}
			
			
			if(null != processCode && processCode.indexOf("cc_GPRSGrpMemberMgr_211") != -1){
				needCutCity = true;
				boss = "boss9";
			}
			//割接地市,获取该地市需要发送的地址
			RemoteCallContext callContext = msg.getContext();
			String city = null;
			if(callContext != null){
				city = callContext.getUserCity();
			}
			city = city == null ? "" : city;
			if(needCutCity){
				if(!"".equals(city) && city != null){
					boss = boss + city;
					
					//添加節點 wang.h
			        String requestXmlByAdd = addParametersBoss(boss,msg);
			        if(requestXmlByAdd != null && msg instanceof StringTeletext) {
			        	((StringTeletext) msg).setTeletext(requestXmlByAdd);
			        }
			        
					showLog(processCode,"地市割接中", boss, city);
					return communicatorMap.get(boss);
				}
			}
			//添加節點 wang.h
	        String requestXmlByAdd = addParametersBoss(boss,msg);
	        if(requestXmlByAdd != null && msg instanceof StringTeletext) {
	        	((StringTeletext) msg).setTeletext(requestXmlByAdd);
	        }
			showLog(processCode, "默认处理", boss, city);
		}
		return communicator;
	}
	
	private String addParametersBoss(String boss, IStreamableMessage msg) {
		if(bossAddr.containsKey(boss)){
			String channel_name = msg.getContext().getInvokeChannel();
			String requestXml = msg.toMessage();
			Map channelInfo = (Map)this.teletextDispatchDAO.getCache().get("CALLER_CHANNEL_INFO_"+channel_name);
			if(channelInfo == null) { channelInfo = new HashMap(); }
			requestXml = requestXml.replace("</process_code>", "</process_code>\n\t<app_id>"+ (channelInfo.get("APP_ID") == null ? "" : channelInfo.get("APP_ID").toString())+ "</app_id>\n\t" +
					"<access_token>"+(channelInfo.get("ACCESS_TOKEN") == null ? "" : channelInfo.get("ACCESS_TOKEN").toString())+"</access_token>\n\t" +
							"<sign>"+(channelInfo.get("SIGN") == null ? "" : channelInfo.get("SIGN").toString())+"</sign>");
			return requestXml;
		}
		return null;
	}

	private String getUserCity(Document doc, String phoneNumberNode) 
	{
		String phoneNumber ="";
		List<Node> nodeList = doc.selectNodes("/operation_in/content/"+phoneNumberNode);
		for(Node node : nodeList)
		{
			phoneNumber = node.getText();
		}
		return findCity(phoneNumber);
	}

	private String findCity(String phoneNumber)
	{
		if ( null != phoneNumber && null != XWECPWebApp.NUMBERSEGMENTS && 
				null != XWECPWebApp.NUMBERSEGMENTS.get(phoneNumber.substring(0, 3)) 
				)
			{
				NumberSegment ns = searchByHalf(XWECPWebApp.NUMBERSEGMENTS.get(phoneNumber.substring(0, 3)), 0,
						                        XWECPWebApp.NUMBERSEGMENTS.get(phoneNumber.substring(0, 3)).size() - 1, phoneNumber.substring(0, 7));
				return ns != null ? String.valueOf(ns.getCity()) : null;
			}
			return null;
		}


	private NumberSegment searchByHalf(List<NumberSegment> list,
			int startSegment, int endSegment,
			String numSegment) {
		NumberSegment sg = null;
		if(startSegment <= endSegment)
		{
			int mid = (startSegment + endSegment) / 2;
			sg = list.get(mid);
			if(sg.getNumSegment() < Integer.parseInt(numSegment))
			{
				sg = searchByHalf(list,mid+1,endSegment,numSegment);
			}
			else if (sg.getNumSegment() > Integer.parseInt(numSegment))
			{
				sg = searchByHalf(list,0,mid-1,numSegment);
			}
		}
		return sg;
	}

	/**
	 * 显示日志
	 * @param processCode
	 * @param message
	 */
	private void showLog(String processCode, String message, String boss, String city){
		StringBuffer sbb = new StringBuffer();
		sbb.append("信息为:【" + message + "】");
		sbb.append("报文为:【" + processCode + "】");
		sbb.append("地址为:【" + boss + "】");
		sbb.append("地市为:【" + city + "】");
		logger.info(sbb.toString());
	}
	
	
}
