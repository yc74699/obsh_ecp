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
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040020Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.CommonUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 集团V网使用情况查询
 * 
 * @author 汪洪广
 * 
 */
public class QueryUserJTUsedInfoInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(QueryUserJTUsedInfoInvocation.class);

	private ParseXmlConfig config;

	private Map map;
	
	//特殊集团单位是分。
	private Map specMap;
	/*使用情况的类型和中文对应*/
	private Map useDetailMap;

	//处理CRM返回不对应的flag字段
	private Map<String,Integer> drawMap;
	public QueryUserJTUsedInfoInvocation() {
		
		this.config = new ParseXmlConfig();

		if (null == this.map) {
			map = new HashMap<String, String>();
			this.map.put("2000001586", "1元包本地主叫200分钟(09版集团套餐)");
			this.map.put("2000001587", "3元包本地主叫500分钟(09版集团套餐)");
			this.map.put("2000001588", "5元包本地主叫800分钟(09版集团套餐)");
			this.map.put("2000001589", "5元包省内主叫500分钟(09版集团套餐)");
			this.map.put("2000001590", "10元包省内主叫800分钟(09版集团套餐)");
			this.map.put("2000003711", "5元包集团200分钟和本地网内100分钟");
			this.map.put("2000003712", "10元包集团500分钟和本地网内200分钟");
			this.map.put("2000003713", "15元包集团800分钟和本地网内300分钟");
			this.map.put("2000005873", "1元包本地主叫300分钟");
			this.map.put("2000005874", "3元包省内主叫500分钟");
			this.map.put("2000005875", "5元包省内主叫800分钟");
			this.map.put("2000005876", "10元包省内主叫1500分钟");
			this.map.put("2000003711", "5元包集团200分钟和本地网内100分钟（月最低消费68元）");
			this.map.put("2000003712", "集团500分钟和本地网内200分钟（月最低消费68元）");
			this.map.put("2000003713", "15元包集团800分钟和本地网内300分钟（月最低消费68元）");
			this.map.put("2000003342", "5元包集团200分钟和本地网内100分钟（月最低消费48元）");
			this.map.put("2000003343", "10元包集团500分钟和本地网内200分钟（月最低消费48元）");
			this.map.put("2000003344", "15元包集团800分钟和本地网内300分钟（月最低消费48元）");
			this.map.put("2000003316", "5元包集团200分钟和本地网内100分钟（月最低消费58元）");
			this.map.put("2000003317", "10元包集团500分钟和本地网内200分钟（月最低消费58元）");
			this.map.put("2000003318", "15元包集团800分钟和本地网内300分钟（月最低消费58元）");
		}
		
		if (null == this.specMap) {
			specMap = new HashMap<String, String>();
			specMap.put("2000005124", "10元包主叫300元");
			specMap.put("2000005504", "5元包主叫300元");
			specMap.put("2000003527", "10元包主叫300元");
			specMap.put("2000005133", "5元包主叫200元");
			specMap.put("2000005224", "10元包主叫300元");
			specMap.put("2000005056", "30元包主叫600元");
			specMap.put("2000005055", "30元包主叫500元");

		}
		if (null == this.useDetailMap)
		{
			useDetailMap = new HashMap<String, String>();
			useDetailMap.put("1", "短信");
			useDetailMap.put("2", "彩信");
			useDetailMap.put("3", "移动数据流量");
			useDetailMap.put("4", "WLAN");
			useDetailMap.put("5", "金额");
			useDetailMap.put("6", "ip通话");
			useDetailMap.put("7", "通话时长");
			useDetailMap.put("8", "宽带时长");
		}
		if (null == this.drawMap)
		{
			drawMap = new HashMap<String, Integer>();
			this.drawMap.put("337", 9);
			this.drawMap.put("332", 4);
			this.drawMap.put("373", 4);
			this.drawMap.put("390", 7);
			this.drawMap.put("787", 4);
			this.drawMap.put("788", 8);
			this.drawMap.put("610", 5);
			this.drawMap.put("1", 2);
			this.drawMap.put("2", 3);
			this.drawMap.put("3", 4);
			this.drawMap.put("4", 4);
			this.drawMap.put("5", 5);
			this.drawMap.put("6", 10);
			this.drawMap.put("7", 7);
			this.drawMap.put("8", 8);
			this.drawMap.put("9", 6);
		}
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY040020Result res = new QRY040020Result();

		String reqXml = "";
		String rspXml = "";
		String newProdid="";
   
		try {
			this.getPkgId(accessId, config, params, res);
			
			if (res.getPkgInfoList().size() > 0) {
				params.add(new RequestParameter("prodid", res.getPkgInfoList().get(0).getPkgId()));
				reqXml = this.bossTeletextUtil.mergeTeletext(
						"cc_grpvpmnusequery", params);
				logger.info(reqXml);
				rspXml = (String) this.remote
						.callRemote(new StringTeletext(reqXml, accessId,
								"cc_grpvpmnusequery", this
										.generateCity(params)));
				logger.info(rspXml);
				if (null != rspXml && !"".equals(rspXml)) {
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response")
							.getChildText("resp_code");
					res
							.setResultCode(BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS
									: LOGIC_ERROR);
					if ("0000".equals(resp_code)) {
						res.setErrorCode(resp_code);
						res.setErrorMessage(this.config.getChildText(
								this.config.getElement(root, "response"),
								"resp_desc"));
						List<PkgUsedInfo> pkgUsedList = new ArrayList<PkgUsedInfo>();
						PkgUsedInfo pkgUsedSing = null;
						List GrpsSingleList = this.getContentList(root,
								"grpvpmninfo");
						String dataType = "";
						String privid = "";
						if (null != GrpsSingleList
								&& GrpsSingleList.size() > 0) {
							for (int i = 0; i < GrpsSingleList.size(); i++) {
								pkgUsedSing = new PkgUsedInfo();
								Element grpDT = ((Element) GrpsSingleList
										.get(i));
								if (null != grpDT) {
									dataType = XMLUtil.getChildText(grpDT,"dataunit");//数据类型   1钱，2时间，4流量，        单位：时间-"分钟"，钱-"分",流量-"KB"
									privid = XMLUtil.getChildText(grpDT,"privid");    //优惠代码  - - 对应单位flag字段
									String alldata = grpDT.getChildText("alldata");   //总的优惠上限
									String leftdata = grpDT.getChildText("leftdata"); //剩余优惠
									if("1".equals(dataType)) //1: 表示优惠金额，单位为分
									{   // 以前数据类型没有返回，现在返回
										pkgUsedSing.setFreeItemId(dataType); 
										// flag 5:单位是分(金额)                7：分钟(时间) "7 通话时长"，"8 宽带时长" 
										pkgUsedSing.setFlag(5);
										pkgUsedSing.setPkgName(specMap.get(newProdid)==null ? "" : specMap.get(newProdid).toString());
										pkgUsedSing.setTotal(Long.valueOf(alldata));
										pkgUsedSing.setRemain(Long.valueOf(leftdata));
									}
									else 
									{   // 3，4都是对应4，对应流量单位KB
										if(!"".equals(privid) && "3".equals(privid) || "4".equals(privid))
										{
											alldata = Long.valueOf(alldata) * 1024 + "";
											leftdata = Long.valueOf(leftdata) * 1024 + "";
										}
										pkgUsedSing.setTotal(Long.valueOf(alldata));
										pkgUsedSing.setRemain(Long.valueOf(leftdata));
										// 以前数据类型没有返回，现在返回
										pkgUsedSing.setFreeItemId(dataType); 
										//单位
										pkgUsedSing.setFlag(this.drawMap.get(privid));//单位转 换
										pkgUsedSing.setPkgName(String.valueOf(useDetailMap.get(privid)));
									}
								}
								pkgUsedList.add(pkgUsedSing);
							}
						}
						res.getPkgInfoList().get(0).setSubUsedInfoList(
								pkgUsedList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;

	}

	public void getPkgId(String accessId, ServiceConfig config,
			List<RequestParameter> params, QRY040020Result res) {

		String reqXml = "";
		String rspXml = "";
		List<PkgInfo> pkgInfoList = new ArrayList<PkgInfo>();
		try {	
			reqXml = this.bossTeletextUtil.mergeTeletext(
					"ccqueryvwinfo", params);
			logger.info(" ====== 发送报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,
					accessId, "ccqueryvwinfo", this
							.generateCity(params)));
			logger.info(" ====== 返回报文 ======\n" + rspXml);
			Element root = this.config.getElement(rspXml);
			String resp_code = this.config.getChildText(this.config.getElement(
					root, "response"), "resp_code");
			String resp_desc = this.config.getChildText(this.config.getElement(
					root, "response"), "resp_desc");
			setCommonResult(res, resp_code, resp_desc);

			if ("0000".equals(resp_code)) {
				List gsubs = this.getContentList(root, "order_info");
			
				PkgInfo pkgInfo = null;
				String prodid="";
				if (null != gsubs && gsubs.size() > 0) {
					for (int i = 0; i < gsubs.size(); i++) {						
						Element grpDT = ((Element) gsubs
								.get(i));
						
						if (null != grpDT) {
							
							pkgInfo = new PkgInfo();
							prodid = XMLUtil.getChildText(grpDT,"product_code");
							if("".equals(prodid) || null == prodid  )
							{
								prodid = XMLUtil.getChildText(grpDT,"package_code");
							}
							pkgInfo.setPkgId(prodid);
							pkgInfo.setPkgName(this.map.get(prodid) == null ? "集团V网分组产品" : this.map.get(prodid).toString());
							String ss1 = XMLUtil.getChildText(grpDT,"start_date").substring(0, 8);
							
							String ss2 = DateTimeUtil.getTodayChar8();
							if(XMLUtil.getChildText(grpDT,"start_date").substring(0, 8).compareTo(DateTimeUtil.getTodayChar8())<0 )
							pkgInfoList.add(pkgInfo);
						}
					
					}
				}
			}
			res.setPkgInfoList(pkgInfoList);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	

	/**
	 * 获取子节点
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	public Element getElement(Element e, String name) {
		Element dt = null;
		try {
			dt = e.getChild(name);
		} catch (Exception ex) {
			dt = null;
		}
		return dt;
	}

	/**
	 * 获取content下父节点信息
	 * 
	 * @param root
	 * @param name
	 * @return
	 */
	public List getContentList(Element root, String name) {
		List list = null;
		try {
			if (null != root && null != name && !"".equals(name)) {
				list = root.getChild("content").getChildren(name);
			}
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

}