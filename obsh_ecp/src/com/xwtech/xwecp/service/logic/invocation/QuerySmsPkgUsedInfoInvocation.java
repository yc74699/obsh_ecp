package com.xwtech.xwecp.service.logic.invocation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
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
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FamilyNumber;
import com.xwtech.xwecp.service.logic.pojo.GetMsngSubPackDT;
import com.xwtech.xwecp.service.logic.pojo.PkgUseState;
import com.xwtech.xwecp.service.logic.pojo.QRY040011Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040031Result;
import com.xwtech.xwecp.service.logic.pojo.SmsPkgInfo;
import com.xwtech.xwecp.service.logic.pojo.SmsPkgUsedInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
 
/**
 * 套餐使用情况查询 QRY040031 语音套餐、动感地带套餐使用情况查询、 新欢乐送使用情况查询、亲情号码组合查询、省内亲情号码查询、国内移动数据套餐查询、动感地带情侣套餐查询、家庭套餐查询
 * @author yg 2011-11-01
 */
public class QuerySmsPkgUsedInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QuerySmsPkgUsedInfoInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map map; 
 
	private Map<String, Integer> transferMap;
	
	private Map<String, String> useDetailMap;

	private ParseXmlConfig config;

	// 屏蔽20元封顶(CMWAP)
	private static final String[] GPRS_PACKAGE_TYPES_NEW_CMWAP = { "1049","1047", "1048"};

	// 新欢乐送（"1605","1604"）和神州行轻松卡R、H套餐（"2609","2000001967）
	private static final String[] NEW_HAPPY_PRESENT_1 = { "1605","1604","2609","2000001967"};

	//GPRS神州行轻松卡G套餐(2019) F套餐(1780)
	private static final String[] SZX_QSK = {"2019","1780","2000002491","2000001273"};
	// 随E行套餐编码（短码）
	private String[] E_PKG_S_CODES = {"1540","1541","1538","1539","1536","1537","2099","2100","2098","1687","1688","1689","1686"};
	
	// 动感网聊G3版
	private String[] PKG_G3_CODES = {"2309","2310","2311","2312","2313","2314","2628"};
	
	//短信套餐
	private static final String[] PKG_SMS_CODES = {"2565","2566","2567","2568"};
	
	//国内亲情号码
	private static final String[] PKG_GNFN_CODES = {"1840"};
	
	//彩信套餐2元、5元、15元
	private static final String[] PKG_CXTC_CODES = {"1389","4758","2916"};
	
	public QuerySmsPkgUsedInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();

		if (null == this.map) {
			this.map = new HashMap();
			this.map.put("337", 9);
			this.map.put("332", 4);
			this.map.put("373", 4);
			this.map.put("390", 7);
			this.map.put("787", 4);
			this.map.put("788", 8);
			this.map.put("610", 5);
			this.map.put("1", 2);
			this.map.put("2", 3);
			this.map.put("3", 4);
			this.map.put("4", 8);
			this.map.put("5", 5);
			this.map.put("6", 10);
			this.map.put("7", 7);
//			this.map.put("8", 0);
			this.map.put("9", 6);
			
			this.map.put("4778", "最低消费30元");
			this.map.put("4779", "最低消费50元");
			this.map.put("4907", "1元功能费");

		}

		if (null == this.transferMap) {
			this.transferMap = new HashMap<String, Integer>();
			this.transferMap.put("332", 10240);// 国内移动数据套餐将KB转成B
			this.transferMap.put("373", 10240);// 国内移动数据套餐将KB转成B
			this.transferMap.put("3", 1024);
		}
		
		if (null == this.useDetailMap) {
			this.useDetailMap = new HashMap<String, String>();
			this.useDetailMap.put("701", "家庭成员间通话免费分钟数");
			this.useDetailMap.put("801", "家庭成员共享免费分钟数（语音）");
			this.useDetailMap.put("802", "家庭成员共享免费分钟数（wlan）");
			this.useDetailMap.put("803", "家庭成员共享免费分钟数（wlan&gprs省内共享）");
			this.useDetailMap.put("804", "家庭成员共享免费分钟数（wlan&gprs漫游共享）");
			this.useDetailMap.put("1", "短信");
			this.useDetailMap.put("2", "彩信");
			this.useDetailMap.put("3", "GPRS");
			this.useDetailMap.put("4", "WLAN");
			this.useDetailMap.put("5", "金额");
			this.useDetailMap.put("6", "ip通话");
			this.useDetailMap.put("7", "通话时长");
			this.useDetailMap.put("8", "宽带时长");
			this.useDetailMap.put("9", "彩铃");
		}
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY040031Result res = new QRY040031Result();
		List<SmsPkgInfo> reList = new ArrayList<SmsPkgInfo>();
		RequestParameter r = null;
		List<PkgUseState> tcList = null;
		List<PkgUseState> tcList_all = null;
		Date now = new Date();
		String pkgId = "";

		try {
			res.setResultCode("0");
			res.setErrorMessage("");

			// 查询所有在用和预约的套餐
			tcList_all = this.queryAllPersonalPackage(accessId, params, res);

			//随E行套餐使用情况查询（套餐查询使用的是短码）
			tcList = this.getPackageListByCode(tcList_all, E_PKG_S_CODES, true);
			//删除套餐列表中的随E型套餐
			for (int i =0; i < tcList_all.size(); i++) {
				for (int j =0; j < tcList.size(); j++) {
					if (tcList_all.get(i).getPkgId().equals(tcList.get(j).getPkgId())) {
						tcList_all.remove(i);
					}
				}
			}
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("随E行套餐");
					tcInfo.setPkgId(pkg.getPkgId());
//					tcInfo.setPkgType(String.valueOf(pkg.getPkgType()));
					reList.add(tcInfo);
					// 套餐明细
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
					
					for (SmsPkgUsedInfo item : tcInfo.getSubUsedInfoList()) {
						if (isTimePackage(pkg.getPkgId())) {
							item.setFlag(8);
						} else {
							item.setFlag(4);
						}
					}
				}
			}
			
			// 语音套餐
			// 套餐大类1022，1031
			tcList = getPackageListByType(tcList_all, new String[] { "1022", "1031","1018" }, false);

			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					//过滤必选包
					if(!isValidPackage(pkg))
					{
						continue;
					}
					if(!"1431".equals(pkg.getPkgId()) && !"2545".equals(pkg.getPkgId()) ){  //增加网聊套餐G3版查询1018.原来的网聊套餐不用这个接口查，因此过滤掉
						SmsPkgInfo tcInfo = new SmsPkgInfo();
						tcInfo.setPkgName("语音套餐");
						tcInfo.setPkgId(pkg.getPkgId());
						tcInfo.setPkgType(String.valueOf(pkg.getPkgType()));
						reList.add(tcInfo);
					// 套餐明细
						this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
					}
				}
			}

			// 动感地带套餐使用情况查询
			// 查询动感地带必选套餐信息
			// 查询动感地带可选套餐信息
			// 动感融合无限50元B套餐
			tcList = getPackageListByType(tcList_all, new String[] { "1018", "1019", "1063" }, false);

			if (null != tcList && tcList.size() > 0) {
				// 新增参数
				r = new RequestParameter();
				r.setParameterName("dbi_month_pr_number"); // 月份
				r.setParameterValue(this.config.getTodayChar6()); // 在用套餐
				params.add(r);
				r = new RequestParameter();
				r.setParameterName("cdr_reduce_total");
				r.setParameterValue("1");
				params.add(r);
				r = new RequestParameter();
				r.setParameterName("a_bg_bill_month"); // 半月标志
				r.setParameterValue("1");
				params.add(r);

				for (PkgUseState pkgUseState : tcList) {
					
					if(!isG3Package(pkgUseState.getPkgId())){ //G3套餐不调用此接口
						SmsPkgInfo tcInfo = new SmsPkgInfo();
						tcInfo.setPkgName("动感地带套餐");
						tcInfo.setPkgType(String.valueOf(pkgUseState.getPkgType()));
						tcInfo.setPkgId(pkgUseState.getPkgId());
						reList.add(tcInfo);
						// 获取套餐优惠信息
						this.getAcAGetZoneInfo(accessId, config, params, pkgUseState.getPkgId(), tcInfo, pkgUseState.getPkgName(), res);
					}
				}
			}

			// 新欢乐送使用情况查询
			// 套餐大类1013
			tcList = getPackageListByType(tcList_all, new String[] { "1013" }, true);
			// 筛选套餐 新欢乐送
			tcList = getPackageListByCode(tcList, NEW_HAPPY_PRESENT_1, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("新欢乐送");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询欢乐送使用情况
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
				}
			}

			// 神州行轻松卡G套餐，赠送GPRS流量30MB
			tcList = getPackageListByCode(tcList_all, SZX_QSK, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					String queryDate = this.config.getTodayChar6();
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("GPRS流量");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询GPRS流量使用情况
					this.doAcAGetFreeItem3(accessId, config, params, queryDate, tcInfo.getPkgId(), res, tcInfo);
				}
			}
			
			//短信套餐
			tcList = getPackageListByCode(tcList_all, PKG_SMS_CODES, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					String queryDate = this.config.getTodayChar6();
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("短信套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询短信套餐使用情况
					this.doAcAGetFreeItem4SMSTC(accessId, config, params, queryDate, tcInfo.getPkgId(), res, tcInfo,2);
				}
			}
			
			// 亲情号码组合查询
			// 查询用户加入的亲情组合套餐信息
			List<GetMsngSubPackDT> groupList = this.getRelationGroupPckInfo(accessId, config, params, res);
			if (null != groupList && groupList.size() > 0) {

				for (GetMsngSubPackDT packDt : groupList) {
					if (Long.parseLong(this.config.getDistanceDT(packDt.getPackage_use_date(), this.config.getTodayChar14(), "s")) >= 0) {
						SmsPkgInfo tcInfo = new SmsPkgInfo();
						tcInfo.setPkgName("亲情号码组合");
						tcInfo.setPkgId(packDt.getPackage_code());
						reList.add(tcInfo);
						// 查询亲情号码优惠情况
						this.getAcAGetZoneInfo(accessId, config, params, res, packDt.getPackage_code(), this.map.get(packDt.getPackage_code()) == null ? "1元功能费" : String
								.valueOf(this.map.get(packDt.getPackage_code())), tcInfo);
					}
				}
			}

			// 国内亲情号码查询
			tcList = getPackageListByCode(tcList_all, PKG_GNFN_CODES, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					String queryDate = this.config.getTodayChar6();
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("国内亲情号码");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询国内亲情号码使用情况
					this.doAcAGetFreeItem2(accessId, config, params, queryDate, tcInfo.getPkgId(), res, tcInfo);
				}
			}
			//彩信套餐
			tcList = getPackageListByCode(tcList_all, PKG_CXTC_CODES, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					String queryDate = this.config.getTodayChar6();
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("彩信套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询彩信套餐使用情况
					this.doAcAGetFreeItem4SMSTC(accessId, config, params, queryDate, tcInfo.getPkgId(), res, tcInfo,3);
				}
			}
			
//			List<FamilyNumber> snqqlist = this.queryProFamailyMsisdn(accessId, config, params, res);
//			// 存在亲情号码信息
//			if (snqqlist != null && snqqlist.size() > 0) {
//				boolean isUserd = false; // 使用状况
//				for (FamilyNumber dt : snqqlist) {
//					// 对比开始时间
//					String distanc = this.config.getDistanceDT(this.config.getTodayChar14(), dt.getBeginDate(), "s");
//					if (Long.parseLong(distanc) <= 0) {
//						isUserd = true;
//						break;
//					}
//				}
//				// 正在使用
//				if (isUserd) {
//					// 当前日期
//					String queryDate = this.config.getTodayChar6();
//					SmsPkgInfo tcInfo = new SmsPkgInfo();
//					tcInfo.setPkgName("省内亲情号码");
//					tcInfo.setPkgId("1840");
//					reList.add(tcInfo);
//					// 优惠信息查询
//					this.doAcAGetFreeItem2(accessId, config, params, queryDate, "1840", res, tcInfo);
//				}
//			}

			// 国内移动数据套餐查询
			// 筛选套餐
			tcList = getPackageListByType(tcList_all, GPRS_PACKAGE_TYPES_NEW_CMWAP, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("国内移动数据套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 语音套餐查看
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
				}
			}
			
			// WLAN套餐
			// 筛选套餐
			tcList = getPackageListByType(tcList_all, new String[] {"1401","1402","1403","1010"}, false);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("WLAN套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 语音套餐查看
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
				}
			}
			
			// 动感地带情侣套餐查询
			tcList = getPackageListByType(tcList_all, new String[] { "1019" }, false);
			// 过滤已办理动感地带情侣套餐
			tcList = getPackageListByCode(tcList, new String[] { "4817" }, false);

			if (null != tcList && tcList.size() >= 0) {
				for (PkgUseState pkg : tcList) {
					SmsPkgInfo tcInfo = new SmsPkgInfo();
					tcInfo.setPkgName("动感地带情侣套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 语音套餐查看
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
				}
			}
			
			//add by tkk 2010-7-28
			//增加动感无限套餐及WLAN分区套餐使用情况查询
			//getMZonePkgAndWLanPkg(accessId, params, reList);

			QRY040011Result resPkDe = new QRY040011Result();
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			List<BossParmDT> parList = null;
			BossParmDT bossDt = null;
			if (null != reList && reList.size() > 0)
			{
				parList = new ArrayList();
				for (int i = 0; i < reList.size(); i++)
				{
					bossDt = new BossParmDT();
					bossDt.setParm1(p.matcher(reList.get(i).getPkgId()).replaceAll(""));
					parList.add(bossDt);
				}
				if (null != reList && reList.size() > 0)
				{
					this.getPackByCode(accessId, config, params, parList, reList, resPkDe);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		res.setPkgInfoList(reList);
		
		//
		if(res == null || res.getPkgInfoList() == null ){
			res.setResultCode("1");
		}
		//
		return res;
	}
	
	/**
	 * 查询动感无线套餐和WLAN套餐使用情况
	 * @param accessId
	 * @param params
	 * @param tcList
	 */
	@SuppressWarnings("unchecked")
	private void getMZonePkgAndWLanPkg(String accessId, List<RequestParameter> params, List<SmsPkgInfo> tcList) {
		final String boss_interface = "cc_cqrynetdetail232";
		try {
			String rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(boss_interface, params), accessId, boss_interface, super.generateCity(params)));
			Document doc = DocumentHelper.parseText(rspXml);
			String resultCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();
			if("0000".equals(resultCode)){
				//有该套餐
				List<Node> packageDtNodes = doc.selectNodes("/operation_out/content/package_dt");
				List<Node> discountCfgDtNodes = doc.selectNodes("/operation_out/content/discount_cfg_dt");
				List<Node> discountUseDtNodes = doc.selectNodes("/operation_out/content/discount_use_dt");
				for(Node packageDtNode : packageDtNodes){
					String packageCode = packageDtNode.selectSingleNode("./package_code").getText().trim();
					String packageName = packageDtNode.selectSingleNode("./package_name").getText().trim();
					SmsPkgInfo info = new SmsPkgInfo();
					if("1623,1624,1625,1626,1627,1628".indexOf(packageCode) > -1){
						//动感无限套餐
						info.setPkgName("动感无限套餐");
						
					}
					else if("1629,1630,1631,1632,1633,1634".indexOf(packageCode) > -1){
						//WLAN分区套餐
						info.setPkgName("WLAN分区套餐");
					}
					
					List<SmsPkgUsedInfo> pkgUsedInfos = new ArrayList<SmsPkgUsedInfo>();
					for(int i = 0; i < discountCfgDtNodes.size(); i ++){
						Node discountCfgDtNode = discountCfgDtNodes.get(i);
						Node discountUseDtNode = discountUseDtNodes.get(i);
						
						String packcode1 = discountCfgDtNode.selectSingleNode("./package_code").getText().trim();
						String packcode2 = discountUseDtNode.selectSingleNode("./package_code").getText().trim();
						
						if(packcode1.equals(packageCode) && packcode2.equals(packageCode)){
							SmsPkgUsedInfo SmsPkgUsedInfo = new SmsPkgUsedInfo();
							SmsPkgUsedInfo.setPkgName(packageName);
							
							//总量
							String totalStr = discountCfgDtNode.selectSingleNode("./discount_amount").getText().trim();
							SmsPkgUsedInfo.setTotal(Long.valueOf(totalStr));
							
							//使用量
							String remainStr = discountUseDtNode.selectSingleNode("./use_amount").getText().trim();
							SmsPkgUsedInfo.setRemain(Long.valueOf(remainStr));
							
							//单位
							String flag = discountUseDtNode.selectSingleNode("./discount_type").getText().trim();
							SmsPkgUsedInfo.setFlag(changeLocalType(flag));
							pkgUsedInfos.add(SmsPkgUsedInfo);
						}
					}
					
					if(pkgUsedInfos.size() > 0){
						info.setSubUsedInfoList(pkgUsedInfos);
						tcList.add(info);
					}
				}
			}
			else{
				//没有该套餐
			}
		}catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	/**
	 * 转换为本地类型
	 * @param flag
	 * @return
	 */
	private int changeLocalType(String src){
		int flag = 0;
		int temp = Integer.parseInt(src);
		switch(temp){
		
			//短信
			case 7 :
				flag = 2;break;
			
			//GPRS流量
			case 9 :
				flag = 4;break;
				
			//上网本流量
			case 10 :
				flag = 4;break;
			
			//WLAN时长
			case 12 :
				flag = 8;break;
			
			//个人宽带
			case 13 :
				flag = 4;break;
		}
		return flag;
	}
	
	/**
	 * 从套餐列表中按类别取出需要的套餐
	 * 
	 * @param tcList_all
	 *            所有套餐列表
	 * @param types
	 *            套餐类别
	 * @param includeYuyue
	 *            是否包含预约套餐
	 * @return
	 * @throws ParseException
	 */
	private List<PkgUseState> getPackageListByType(final List<PkgUseState> srcList, String[] types, boolean includeYuyue) throws ParseException {
		Date now = new Date();
		List<PkgUseState> reList = new ArrayList<PkgUseState>();
		for (PkgUseState pkg : srcList) {
			if (!includeYuyue) {
				if (now.before(new SimpleDateFormat("yyyyMM").parse(pkg.getBeginDate().substring(0, 6)))) {
					continue;
				}
			}
			
			if (ArrayUtils.indexOf(types, pkg.getPkgType() + "") >= 0) {
				reList.add(pkg);
			}
		}
		return reList;
	}

	/**
	 * 从套餐列表中按编码取出需要的套餐
	 * 
	 * @param tcList_all
	 *            所有套餐列表
	 * @param ids
	 *            套餐编号
	 * @param includeYuyue
	 *            是否包含预约套餐
	 * @return
	 * @throws ParseException
	 */
	private List<PkgUseState> getPackageListByCode(final List<PkgUseState> srcList, String[] ids, boolean includeYuyue) throws ParseException {
		Date now = new Date();
		List<PkgUseState> reList = new ArrayList<PkgUseState>();
		for (PkgUseState pkg : srcList) {
			if (!includeYuyue) {
				if (now.before(new SimpleDateFormat("yyyyMM").parse(pkg.getBeginDate().substring(0, 6)))) {
					continue;
				}
			}
			if (ArrayUtils.indexOf(ids, pkg.getPkgId() + "") >= 0) {
				reList.add(pkg);
			}
		}
		return reList;
	}

	/**
	 * 套餐优惠信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param acrelation_revision
	 * @param res
	 * @param pkgName
	 */
	private void getAcAGetZoneInfo(String accessId, ServiceConfig config, List<RequestParameter> params, String acrelation_revision, SmsPkgInfo SmsPkgInfo, String pkgName,
			QRY040031Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		RequestParameter par = null; // 参数
		String resp_code = ""; // 返回编码
		SmsPkgUsedInfo pkg = null; // 套餐使用情况

		try {
			// 替换业务编码
			boolean codeBoolean = true;
			for (RequestParameter p : params) {
				// 已存在
				if ("acrelation_revision".equals(p.getParameterName())) {
					codeBoolean = false;
					p.setParameterValue(acrelation_revision);
				}
			}
			if (codeBoolean) {
				// 新增参数
				par = new RequestParameter();
				par.setParameterName("acrelation_revision");
				par.setParameterValue(acrelation_revision);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetzoneinfo_518", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetzoneinfo_518", super.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "ac_agetzoneinfo_518");

				if ("0000".equals(resp_code)) {
					Element content = this.config.getElement(root, "content");
					List<SmsPkgUsedInfo> reList = new ArrayList<SmsPkgUsedInfo>();
					// 短信
					if (!"".equals(this.config.getChildText(content, "arecord_count"))) {
						pkg = new SmsPkgUsedInfo();
						pkg.setPkgName(pkgName + "[短信]");
						pkg.setFlag(2);
						String strUse = this.config.getChildText(content, "arecord_count");
						String strRemain = this.config.getChildText(content, "mms_len");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
							reList.add(pkg);
					}
					// IP电话
					if (!"".equals(this.config.getChildText(content, "data_down"))) {
						pkg = new SmsPkgUsedInfo();
						pkg.setPkgName(pkgName + "[IP电话]");
						pkg.setFlag(10);
						String strUse = this.config.getChildText(content, "data_down");
						String strRemain = this.config.getChildText(content, "fax_time");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
							reList.add(pkg);
					}
					// 通话时间
					if (!"".equals(this.config.getChildText(content, "data_up"))) {
						pkg = new SmsPkgUsedInfo();
						pkg.setPkgName(pkgName + "[通话时间]");
						pkg.setFlag(9);
						String strUse = this.config.getChildText(content, "data_up");
						String strRemain = this.config.getChildText(content, "rec_time");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
							reList.add(pkg);
					}
					// 彩信
					if (!"".equals(this.config.getChildText(content, "acctbkitem_istransferable"))) {
						pkg = new SmsPkgUsedInfo();
						pkg.setPkgName(pkgName + "[彩信]");
						pkg.setFlag(3);
						String strUse = this.config.getChildText(content, "acctbkitem_istransferable");
						String strRemain = this.config.getChildText(content, "acctbkitem_usage_type");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
							reList.add(pkg);
					}

					// GPRS //TODO:特殊套餐才显示GPRS/WLAN，否则不显示
					String gprs = this.config.getChildText(content, "acctbkitem_default_flag");
					if (!"".equals(gprs)) {
						pkg = new SmsPkgUsedInfo();
						pkg.setPkgName(pkgName + "[GPRS]");
						pkg.setFlag(4);
						String strUse = this.config.getChildText(content, "acctbkitem_default_flag");
						String strRemain = this.config.getChildText(content, "acctbkitem_invprn_flag");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal((Long.parseLong(strUse) + Long.parseLong(strRemain)) * 1024);
							pkg.setRemain(Long.parseLong(strRemain) * 1024);
						}
						if (pkg.getTotal() > 0)
							reList.add(pkg);
					}
					SmsPkgInfo.setSubUsedInfoList(reList);
				}
				//增加套餐失败信息显示
				else
				{
					List<SmsPkgUsedInfo> subErrorList = new ArrayList<SmsPkgUsedInfo>();
					SmsPkgUsedInfo errorInfo = new SmsPkgUsedInfo();
					errorInfo.setPkgName(pkgName);
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					SmsPkgInfo.setSubUsedInfoList(subErrorList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 语音套餐查看
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param queryDate
	 * @param packageCode
	 */
	private void doAcAGetFreeItem(String accessId, List<RequestParameter> params, String queryDate, String packageCode, String pkgName, SmsPkgInfo tcInfo, QRY040031Result res) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;
		ErrorMapping errDt = null;

		try {
			boolean booleanDate = true;
			boolean booleanCode = true;
			for (RequestParameter p : params) {
				if ("dbi_month".equals(p.getParameterName())) {
					booleanDate = false;
					p.setParameterValue(queryDate);
				}
				if ("a_package_id".equals(p.getParameterName())) {
					booleanCode = false;
					p.setParameterValue(packageCode);
				}
			}
			if (booleanDate) {
				par = new RequestParameter();
				par.setParameterName("dbi_month");
				par.setParameterValue(queryDate);
				params.add(par);
			}
			if (booleanCode) {
				par = new RequestParameter();
				par.setParameterName("a_package_id");
				par.setParameterValue(packageCode);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_517", params);
			logger.debug(" ====== 语音套餐查看 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetfreeitem_517", super.generateCity(params)));
				logger.debug(" ====== 语音套餐查看 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040031", "ac_agetfreeitem_517", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && "0000".equals(resp_code)) {
					List freeitemList = this.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						List<SmsPkgUsedInfo> subList = new ArrayList<SmsPkgUsedInfo>();
						for (int i = 0; i < freeitemList.size(); i++) {
							//套餐编码
							String packageId = this.getChildText((Element) freeitemList.get(i), "a_package_id");
							// 总量
							String strTotal = this.getChildText((Element) freeitemList.get(i), "a_freeitem_total_value");
							long total = "".equals(strTotal) ? 0 : Long.parseLong(strTotal);
							// 已使用情况
							String strUse = this.getChildText((Element) freeitemList.get(i), "a_freeitem_value");
							long use = "".equals(strUse) ? 0 : Long.parseLong(strUse);
							// 标识位：用以表示使用情况的表示的是时间，还是短信条数等
							String a_freeitem_id = this.getChildText((Element) freeitemList.get(i), "a_freeitem_id");

							SmsPkgUsedInfo info = new SmsPkgUsedInfo();
							//对于88套餐c明细做的修改
							if("2376".equals(packageId)||"2377".equals(packageId)||"2378".equals(packageId)){
								if("3".equals(a_freeitem_id)){
									info.setPkgName("赠送GPRS上网流量");
								}else if("4".equals(a_freeitem_id)){
									info.setPkgName("赠送WLAN上网分钟数");
								}else if("7".equals(a_freeitem_id)){
									info.setPkgName("国内（不含港澳台）主叫分钟数");
								}
							}else if(isG3Package(packageId)){
								info.setPkgName("网聊套餐G3版通话时长");
							}else{
								info.setPkgName(this.useDetailMap.get(a_freeitem_id) == null ? pkgName : this.useDetailMap.get(a_freeitem_id));
							}
							//info.setPkgName(this.useDetailMap.get(a_freeitem_id) == null ? pkgName : this.useDetailMap.get(a_freeitem_id));
							info.setFlag(this.map.get(a_freeitem_id) == null ? 0 : Integer.parseInt(String.valueOf(this.map.get(a_freeitem_id))));
							if (this.transferMap.containsKey(a_freeitem_id)) {
								total = total * this.transferMap.get(a_freeitem_id);
								use = use * this.transferMap.get(a_freeitem_id);
							}
							//xx元包xx元套餐，金额：a_freeitem_id 为5.单位分
							if("5".equals(a_freeitem_id)){
								total = total/100;
								use = use/100;
							}
							
							info.setTotal(total);// 总量
							info.setRemain(total - use);// 剩余使用情况
							subList.add(info);
						}
						
						//add start by yangg
						if(subList != null && subList.size() > 0){
							long total_g3 = 0l;
							long remain_g3 = 0l;
							int flag_g3 = 0;
							String name_g3 = "";
							boolean temp_flag = false;
							SmsPkgUsedInfo info_g3 = new SmsPkgUsedInfo();
							for (int i =0;i< subList.size(); i++ ){
								if(subList.get(i).getFlag() ==7 && "网聊套餐G3版通话时长".equals(subList.get(i).getPkgName())){
									
									total_g3 +=subList.get(i).getTotal();
									remain_g3 +=subList.get(i).getRemain();
									flag_g3 = subList.get(i).getFlag();
									name_g3 =  "网聊套餐G3版通话总时长";
									temp_flag = true;
								}
							}
							if(temp_flag){
								info_g3.setFlag(flag_g3);// 
								info_g3.setPkgName(name_g3);// 
								info_g3.setTotal(total_g3);// 总量
								info_g3.setRemain(remain_g3);// 剩余使用情况
								subList.add(info_g3);
							}
						}
						//去除G3套餐中重复的
						if (subList != null && subList.size() > 0) {
							Iterator it = subList.iterator();
							SmsPkgUsedInfo g3_info = null;
							while (it.hasNext()) {
								g3_info = (SmsPkgUsedInfo) it.next();
								
								//过滤掉通话时长（7）并且不是总时长的项
								if(g3_info.getFlag() ==7 && "网聊套餐G3版通话时长".equals(g3_info.getPkgName())){
									it.remove();
								}
							}
						}
						
						//add end by yangg
						tcInfo.setSubUsedInfoList(subList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<SmsPkgUsedInfo> subErrorList = new ArrayList<SmsPkgUsedInfo>();
					SmsPkgUsedInfo errorInfo = new SmsPkgUsedInfo();
					errorInfo.setPkgName(pkgName);
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					tcInfo.setSubUsedInfoList(subErrorList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 查询语音套餐
	 * 
	 * @param accessId
	 * @param res
	 * @throws Exception
	 */
	private List<PkgUseState> queryAllPersonalPackage(String accessId, List<RequestParameter> params, QRY040031Result res) throws Exception {
		String reqXml = "";
		String rspXml = "";
		List<PkgUseState> reList = new ArrayList<PkgUseState>();
		RequestParameter par = null;
		PkgUseState dt = null;
		ErrorMapping errDt = null;

		par = new RequestParameter();
		par.setParameterName("biz_pkg_qry_scope"); // 查询方式
		par.setParameterValue("1"); // 在用和预约套餐
		params.add(par);

		par = new RequestParameter();
		par.setParameterName("package_type");
		par.setParameterValue(0); // 查询所有套餐
		params.add(par);

		reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_YYTC", params);
		logger.debug(" ====== 语音套餐 发送报文 ====== \n" + reqXml);
		if (null != reqXml && !"".equals(reqXml)) {
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_YYTC", super.generateCity(params)));
			logger.debug(" ====== 语音套餐 接收报文 ====== \n" + rspXml);
		}
		if (null != rspXml && !"".equals(rspXml)) {
			Element root = this.getElement(rspXml.getBytes());
			String resp_code = root.getChild("response").getChildText("resp_code");
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");
			if (!"0000".equals(resp_code)) {
				errDt = this.wellFormedDAO.transBossErrCode("QRY040031", "cc_find_package_62_YYTC", resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode());
					res.setErrorMessage(errDt.getLiErrMsg());
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List packList = this.getContentList(root, "package_code");
				if (null != packList && packList.size() > 0) {
					for (int i = 0; i < packList.size(); i++) {
						Element packageDt = this.getElement((Element) packList.get(i), "cplanpackagedt");
						if (null != packageDt) {
							dt = new PkgUseState();
							// 套餐id
							dt.setPkgId(this.getChildText(packageDt, "package_code"));
							// 套餐名称
							dt.setPkgName(this.getChildText(packageDt, "package_name"));
							// 套餐描述
							// dt.setPkgDesc("");
							// 套餐类型 this.getChildText(packageDt,
							// "package_type")
							dt.setPkgType(Integer.parseInt(this.getChildText(packageDt, "package_type")));
							// dt.setPkgType(0);
							// 开始日期
							dt.setBeginDate(this.getChildText(packageDt, "package_use_date"));
							// 结束日期
							dt.setEndDate(this.getChildText(packageDt, "package_end_date"));
							reList.add(dt);
						}
					}
				}
			}
		}
		return reList;
	}

	/**
	 * 获取content下父节点信息
	 * 
	 * @param root
	 * @param name
	 * @return
	 */
	private List getContentList(Element root, String name) {
		List list = null;
		try {
			list = root.getChild("content").getChildren(name);
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	/**
	 * 获取子节点
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	private Element getElement(Element e, String name) {
		Element dt = null;
		try {
			dt = e.getChild(name);
		} catch (Exception ex) {
			dt = null;
		}
		return dt;
	}

	/**
	 * 获取子节点信息
	 * 
	 * @param e
	 * @param childName
	 * @return
	 */
	private String getChildText(Element e, String childName) {
		String str = "";

		try {
			str = e.getChildText(childName) == null ? "" : e.getChildText(childName).trim();
		} catch (Exception ex) {
			// logger.error(ex, ex);
			str = "";
		}

		return str;
	}

	/**
	 * 设置结果信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	private void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040031Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) || "-14198".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY040031", xmlName, resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode()); // 设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg()); // 设置错误信息
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 查询用户加入的亲情组合套餐信息 cc_cgetmsngsubpack_352
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private List getRelationGroupPckInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040031Result res) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		GetMsngSubPackDT dt = null;
		List<GetMsngSubPackDT> list = null;
		String usingPkgCode = "";
		String useState = "";
		String canOpenPkgCode = "";
		Set<String> pkgSet = new HashSet<String>();
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmsngsubpack_352", params);
			logger.debug(" ====== 查询用户加入的亲情组合套餐信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetmsngsubpack_352", super.generateCity(params)));
				logger.debug(" ====== 查询用户加入的亲情组合套餐信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040031", "cc_cgetmsngsubpack_352", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List userList = this.config.getContentList(root, "package_user_id");
				if (null != userList && userList.size() > 0) {
					list = new ArrayList<GetMsngSubPackDT>();
					for (int i = 0; i < userList.size(); i++) {
						Element packDt = this.config.getElement((Element) userList.get(i), "cplanpackagedt");
						if (null != packDt) {
							if ("1035".equals(this.config.getChildText(packDt, "package_type"))) {
								String package_code = this.config.getChildText(packDt, "package_code");
								if ("4778".equals(package_code) || "4779".equals(package_code) || "4907".equals(package_code)) {
									if (pkgSet.contains(this.config.getChildText(packDt, "package_code")))
										continue;
									dt = new GetMsngSubPackDT();
									dt.setPackage_user_id(this.config.getChildText(packDt, "package_user_id"));
									dt.setPackage_type(this.config.getChildText(packDt, "package_type"));
									dt.setPackage_code(this.config.getChildText(packDt, "package_code"));
									dt.setPackage_use_date(this.config.dateToString(this.config
											.stringToDate(this.config.getChildText(packDt, "package_use_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
									dt.setPackage_end_date(this.config.dateToString(this.config
											.stringToDate(this.config.getChildText(packDt, "package_end_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
									dt.setPackage_state(this.config.getChildText(packDt, "package_state"));
									dt.setPackage_change_date(this.config.getChildText(packDt, "package_change_date"));
									dt.setPackage_history_srl(this.config.getChildText(packDt, "package_history_srl"));
									dt.setPackage_apply_date(this.config.getChildText(packDt, "package_apply_date"));
									dt.setPackage_level(this.config.getChildText(packDt, "package_level"));

									// 正在使用亲情号码组合
									if (Long.parseLong(this.config.getDistanceDT(dt.getPackage_use_date(), this.config.getTodayChar14(), "s")) >= 0) {
										usingPkgCode = dt.getPackage_code();
										if (dt.getPackage_end_date().length() == 0) {
											canOpenPkgCode = dt.getPackage_code();
											useState = "10";
										} else {
											useState = useState == "13" ? "12" : "11";
										}
									} else {
										// 预约亲情号码组合
										canOpenPkgCode = dt.getPackage_code();
										useState = useState == "11" ? "12" : "13";
									}

									list.add(dt);
									pkgSet.add(dt.getPackage_code());
								}
							}
						}
					}
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setUsingPkgCode(usingPkgCode);
						list.get(i).setCanOpenPkgCode(canOpenPkgCode);
						list.get(i).setUseState(useState);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return list;
	}

	/**
	 * 动感套餐优惠查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param pkgName
	 */
	private void getAcAGetZoneInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040031Result res, String acrelation_revision, String pkgName,
			SmsPkgInfo SmsPkgInfo) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		Element content = null;
		RequestParameter r = null;
		SmsPkgUsedInfo pkg = null;
		ErrorMapping errDt = null;

		try {
			r = new RequestParameter();
			r.setParameterName("dbi_month_pr_number");
			r.setParameterValue(this.config.getTodayChar6());
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("cdr_reduce_total");
			r.setParameterValue("1");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("a_bg_bill_month");
			r.setParameterValue("0");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("acrelation_revision");
			r.setParameterValue(acrelation_revision);
			params.add(r);

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetzoneinfo_518", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetzoneinfo_518", super.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040031", "ac_agetzoneinfo_518", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				content = this.config.getElement(root, "content");
				List<SmsPkgUsedInfo> reList = new ArrayList<SmsPkgUsedInfo>();
				// 短信
				if (!"".equals(this.config.getChildText(content, "arecord_count"))) {
					pkg = new SmsPkgUsedInfo();
					pkg.setPkgName(pkgName + "[短信]");
					pkg.setFlag(2);
					String strUse = this.config.getChildText(content, "arecord_count");
					String strRemain = this.config.getChildText(content, "mms_len");
					if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
						pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
						pkg.setRemain(Long.parseLong(strRemain));
					}
					if (pkg.getTotal() > 0)
						reList.add(pkg);
				}
				// IP电话
				if (!"".equals(this.config.getChildText(content, "data_down"))) {
					pkg = new SmsPkgUsedInfo();
					pkg.setPkgName(pkgName + "[IP电话]");
					pkg.setFlag(10);
					String strUse = this.config.getChildText(content, "data_down");
					String strRemain = this.config.getChildText(content, "fax_time");
					if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
						pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
						pkg.setRemain(Long.parseLong(strRemain));
					}
					if (pkg.getTotal() > 0)
						reList.add(pkg);
				}
				// 通话时间
				if (!"".equals(this.config.getChildText(content, "data_up"))) {
					pkg = new SmsPkgUsedInfo();
					pkg.setPkgName(pkgName + "[通话时间]");
					pkg.setFlag(9);
					String strUse = this.config.getChildText(content, "data_up");
					String strRemain = this.config.getChildText(content, "rec_time");
					if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
						pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
						pkg.setRemain(Long.parseLong(strRemain));
					}
					if (pkg.getTotal() > 0)
						reList.add(pkg);
				}
				// 彩信
				if (!"".equals(this.config.getChildText(content, "acctbkitem_istransferable"))) {
					pkg = new SmsPkgUsedInfo();
					pkg.setPkgName(pkgName + "[彩信]");
					pkg.setFlag(9);
					String strUse = this.config.getChildText(content, "acctbkitem_istransferable");
					String strRemain = this.config.getChildText(content, "acctbkitem_usage_type");
					if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
						pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
						pkg.setRemain(Long.parseLong(strRemain));
					}
					if (pkg.getTotal() > 0)
						reList.add(pkg);
				}
				// GPRS
				if (!"".equals(this.config.getChildText(content, "acctbkitem_default_flag"))) {
					pkg = new SmsPkgUsedInfo();
					pkg.setPkgName(pkgName + "[GPRS]");
					pkg.setFlag(9);
					String strUse = this.config.getChildText(content, "acctbkitem_default_flag");
					String strRemain = this.config.getChildText(content, "acctbkitem_invprn_flag");
					if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
						pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
						pkg.setRemain(Long.parseLong(strRemain));
					}
					if (pkg.getTotal() > 0)
						reList.add(pkg);
				}
				SmsPkgInfo.setSubUsedInfoList(reList);
			}
			//增加套餐失败信息显示
			else
			{
				List<SmsPkgUsedInfo> subErrorList = new ArrayList<SmsPkgUsedInfo>();
				SmsPkgUsedInfo errorInfo = new SmsPkgUsedInfo();
				errorInfo.setPkgName(pkgName);
				errorInfo.setFlag(-1);
				errorInfo.setRemain(-1);
				errorInfo.setTotal(-1);
				subErrorList.add(errorInfo);
				SmsPkgInfo.setSubUsedInfoList(subErrorList);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 省内亲情号码查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private List<FamilyNumber> queryProFamailyMsisdn(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040031Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回码
		FamilyNumber dt = null; // 亲情号码信息
		List<FamilyNumber> list = new ArrayList(); // 亲情号码列表

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryprovfamily_419", params);
			logger.debug(" ====== 查询用户在用/预约的所有省内亲情 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryprovfamily_419", super.generateCity(params)));
				logger.debug(" ====== 查询用户在用/预约的所有省内亲情 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cqryprovfamily_419");
				
				//此接口只有网营crm有，设置成成功不影响其他种类套餐使用情况查询。
				res.setResultCode("0");
				// 成功
				if ("0000".equals(resp_code)) {
					List<Element> familyList = this.config.getContentList(root, "family_dt");
					if (null != familyList && familyList.size() > 0) {
						for (Element e : familyList) {
							dt = new FamilyNumber();
							dt.setName("国内亲情号码组合"); // 套餐名称
							// 对方号码
							dt.setFamilyMsisdn(this.config.getChildText(e, "family_msisdn"));
							// 开始日期
							dt.setBeginDate(this.config.getChildText(e, "family_Start_date"));
							// 结束日期
							dt.setEndDate(this.config.getChildText(e, "family_End_date"));
							list.add(dt);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return list;
	}

	/**
	 * 套餐使用情况信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param queryDate
	 * @param packageCode
	 */
	private void doAcAGetFreeItem2(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, QRY040031Result res, SmsPkgInfo SmsPkgInfo) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;

		try {
			boolean booleanDate = true;
			boolean booleanCode = true;
			for (RequestParameter p : params) {
				if ("dbi_month".equals(p.getParameterName())) {
					booleanDate = false;
					p.setParameterValue(queryDate);
				}
				if ("a_package_id".equals(p.getParameterName())) {
					booleanCode = false;
					p.setParameterValue(packageCode);
				}
			}
			if (booleanDate) {
				par = new RequestParameter();
				par.setParameterName("dbi_month");
				par.setParameterValue(queryDate);
				params.add(par);
			}
			if (booleanCode) {
				par = new RequestParameter();
				par.setParameterName("a_package_id");
				par.setParameterValue(packageCode);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_517", params);
			logger.debug(" ====== 语音套餐查看 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetfreeitem_517", super.generateCity(params)));
				logger.debug(" ====== 语音套餐查看 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cqryprovfamily_419");
				// 成功
				if (null != resp_code && "0000".equals(resp_code)) {
					List<Element> freeitemList = this.config.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						Element e = (Element) freeitemList.get(0);
						SmsPkgUsedInfo pkg = new SmsPkgUsedInfo();
						String strTotal = this.config.getChildText(e, "a_freeitem_total_value");
						String strUse = this.config.getChildText(e, "a_freeitem_value");
						if (strTotal != null && strTotal.trim().length() > 0 && strUse != null && strUse.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strTotal));
							pkg.setRemain(Long.parseLong(strTotal) - Long.parseLong(strUse));
						}
						pkg.setFlag(9);
						List<SmsPkgUsedInfo> reList = new ArrayList<SmsPkgUsedInfo>();
						reList.add(pkg);
						SmsPkgInfo.setSubUsedInfoList(reList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<SmsPkgUsedInfo> subErrorList = new ArrayList<SmsPkgUsedInfo>();
					SmsPkgUsedInfo errorInfo = new SmsPkgUsedInfo();
					errorInfo.setPkgName(SmsPkgInfo.getPkgName());
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					SmsPkgInfo.setSubUsedInfoList(subErrorList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	
	private void doAcAGetFreeItem3(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, QRY040031Result res, SmsPkgInfo SmsPkgInfo) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;

		try {
			boolean booleanDate = true;
			boolean booleanCode = true;
			for (RequestParameter p : params) {
				if ("dbi_month".equals(p.getParameterName())) {
					booleanDate = false;
					p.setParameterValue(queryDate);
				}
				if ("a_package_id".equals(p.getParameterName())) {
					booleanCode = false;
					p.setParameterValue(packageCode);
				}
			}
			if (booleanDate) {
				par = new RequestParameter();
				par.setParameterName("dbi_month");
				par.setParameterValue(queryDate);
				params.add(par);
			}
			if (booleanCode) {
				par = new RequestParameter();
				par.setParameterName("a_package_id");
				par.setParameterValue(packageCode);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_517", params);
			logger.debug(" ====== 语音套餐查看 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetfreeitem_517", super.generateCity(params)));
				logger.debug(" ====== 语音套餐查看 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cqryprovfamily_419");
				// 成功
				if (null != resp_code && "0000".equals(resp_code)) {
					List<Element> freeitemList = this.config.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						Element e = (Element) freeitemList.get(0);
						SmsPkgUsedInfo pkg = new SmsPkgUsedInfo();
						String strTotal = this.config.getChildText(e, "a_freeitem_total_value");
						String strUse = this.config.getChildText(e, "a_freeitem_value");
						if (strTotal != null && strTotal.trim().length() > 0 && strUse != null && strUse.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strTotal)*1024);
							pkg.setFlag(4);
							pkg.setRemain((Long.parseLong(strTotal) - Long.parseLong(strUse))*1024);
						}
						List<SmsPkgUsedInfo> reList = new ArrayList<SmsPkgUsedInfo>();
						reList.add(pkg);
						SmsPkgInfo.setSubUsedInfoList(reList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<SmsPkgUsedInfo> subErrorList = new ArrayList<SmsPkgUsedInfo>();
					SmsPkgUsedInfo errorInfo = new SmsPkgUsedInfo();
					errorInfo.setPkgName("神州行轻松卡G套餐，赠送GPRS流量30MB");
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					SmsPkgInfo.setSubUsedInfoList(subErrorList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	private void doAcAGetFreeItem4SMSTC(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, QRY040031Result res, SmsPkgInfo SmsPkgInfo,int flag) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;

		try {
			boolean booleanDate = true;
			boolean booleanCode = true;
			for (RequestParameter p : params) {
				if ("dbi_month".equals(p.getParameterName())) {
					booleanDate = false;
					p.setParameterValue(queryDate);
				}
				if ("a_package_id".equals(p.getParameterName())) {
					booleanCode = false;
					p.setParameterValue(packageCode);
				}
			}
			if (booleanDate) {
				par = new RequestParameter();
				par.setParameterName("dbi_month");
				par.setParameterValue(queryDate);
				params.add(par);
			}
			if (booleanCode) {
				par = new RequestParameter();
				par.setParameterName("a_package_id");
				par.setParameterValue(packageCode);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_517", params);
			logger.debug(" ====== 语音套餐查看 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetfreeitem_517", super.generateCity(params)));
				logger.debug(" ====== 语音套餐查看 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				// 成功
				if (null != resp_code && "0000".equals(resp_code)) {
					List<Element> freeitemList = this.config.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						Element e = (Element) freeitemList.get(0);
						SmsPkgUsedInfo pkg = new SmsPkgUsedInfo();
						String strTotal = this.config.getChildText(e, "a_freeitem_total_value");
						String strUse = this.config.getChildText(e, "a_freeitem_value");
						if (strTotal != null && strTotal.trim().length() > 0 && strUse != null && strUse.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strTotal));
							pkg.setFlag(flag);
							pkg.setRemain((Long.parseLong(strTotal) - Long.parseLong(strUse)));
						}
						List<SmsPkgUsedInfo> reList = new ArrayList<SmsPkgUsedInfo>();
						reList.add(pkg);
						SmsPkgInfo.setSubUsedInfoList(reList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<SmsPkgUsedInfo> subErrorList = new ArrayList<SmsPkgUsedInfo>();
					SmsPkgUsedInfo errorInfo = new SmsPkgUsedInfo();
					errorInfo.setPkgName("短信套餐");
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					SmsPkgInfo.setSubUsedInfoList(subErrorList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	private static final String[] TIME_PACKAGE_CODES = {"1270","1271","1448","1449","1558","1559","1560","1561","1539","1540","1538","1541","1270","1271",
		"4151","1401","1402","1403","4839","4840","4808","4809","1629","1630","1631","1632","1633","1634","1755","1756","1757","1758","1765","1766","2024","2040",
		"2043","2046","2049" };
	
	private boolean isTimePackage(String pckCode){
		boolean result = false;
		if(TIME_PACKAGE_CODES != null && TIME_PACKAGE_CODES.length > 0 ){
			for(int i = 0; i< TIME_PACKAGE_CODES.length; i++){
				if(TIME_PACKAGE_CODES[i].equals(pckCode)){
					result = true;
				}
			}
		}
		return result;
	}
	/**
	 * 根据套餐代码查询套餐配置信息取
	 * @param accessId
	 * @param config
	 * @param params
	 * @param parList
	 * @param pkgList
	 */
	public void getPackByCode(String accessId, ServiceConfig config, List<RequestParameter> params, List<BossParmDT> parList, 
			List<SmsPkgInfo> reList, QRY040011Result res)
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
								for (SmsPkgInfo pkg : reList)
								{
									if (pkgCode.equals(pkg.getPkgId()))
									{
										pkg.setPkgName(p.matcher(pDt.getChildText("productbusinesspackage_package_name")).replaceAll(""));
										pkg.setPkgDec(p.matcher(pDt.getChildText("productbusinesspackage_package_desc")).replaceAll(""));
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	private boolean isValidPackage(PkgUseState pkg)
	{
		if (pkg.getPkgId().endsWith(String.valueOf(pkg.getPkgType()))
				&& (pkg.getPkgId().length() > String.valueOf(pkg.getPkgType())
						.length())) 
		{
			return false;
		}
		return true;
	}
	//判断是否G3套餐
	private boolean isG3Package(String pkgCode)
	{
		if (ArrayUtils.indexOf(PKG_G3_CODES, pkgCode + "") >= 0) 
		{
			return true;
		}
		return false;
	}
	
	

}
