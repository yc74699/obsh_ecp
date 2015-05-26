package com.xwtech.xwecp.service.logic.invocation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.FluxPkgInfo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FamilyNumber;
import com.xwtech.xwecp.service.logic.pojo.GetMsngSubPackDT;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUseState;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040011Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040020Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.StringUtil;
 
/**
 * 套餐使用情况查询 QRY040020 语音套餐、动感地带套餐使用情况查询、 新欢乐送使用情况查询、亲情号码组合查询、省内亲情号码查询、国内移动数据套餐查询、动感地带情侣套餐查询、家庭套餐查询
 * 
 * @author 邵琪 2010-3-10
 */
public class QueryPkgUsedInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryPkgUsedInfoInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private IPackageChangeDAO packageChangeDAO;
	
	private Map<String,Object> map; 
 
	private Map<String, Integer> transferMap;

	private Map<String, String> useDetailMap;

	private ParseXmlConfig config;

	// 新全包大类1049(含旧大类)
	//private static final String[] GPRS_PACKAGE_TYPES_NEW = { "1049", "1046", "1047", "1048" };

	// 屏蔽20元封顶(CMWAP)
	private static final String[] GPRS_PACKAGE_TYPES_NEW_CMWAP = { "1049","1047", "1048" };

	// 新欢乐送子代码
	private static final String[] NEW_HAPPY_PRESENT_1 = { "1605","1604","2000002660","2000002661"};

	// 家庭套餐查询
//	private static final String[] FAMILY_PACKAGE_CODES = { "1613", "1615", "1616", "1617", "1618", "1619", "1620", "1621", "1622", "1672", "1674", "1711", "1712", "1751", "1752",
//			"1753", "1754", "1755", "1756", "1757", "1758", "1759", "1760", "1761", "1762", "1763", "1764", "1765", "1766", "1767", "1768", "1783", "2023", "2024", "2025", "2039",
//			"2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050", "2065", "2066", "2067", "2068", "2069" };

	// 随E行套餐编码（短码）
	private String[] E_PKG_S_CODES = {"1540","1541","1538","1539","1536","1537","2099","2100","2098","1687","1688","1689","1686"};
	//国内移动数据套餐3元、10元、20元叠加包	
	private static final String[] PKG_GNYDSJTCYDJB_CODES = {"2000002631","2000002504","2000002505"};	

	public QueryPkgUsedInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		//查询流量套餐配置dao
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));

		if (null == this.map) {
			this.map = new HashMap<String,Object>();
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
		QRY040020Result res = new QRY040020Result();
		List<PkgInfo> reList = new ArrayList<PkgInfo>();
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
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("随E行套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 套餐明细
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
					
					for (PkgUsedInfo item : tcInfo.getSubUsedInfoList()) {
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
			tcList = getPackageListByType(tcList_all, new String[] {"1018","1022", "1031" ,"1063" ,"1023"}, false);

			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("语音套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					//动感融合无限xx套餐也是调用ac_agetfreeitem，并且排除不在以下套餐之列的套餐
					if("1063".equals(pkg.getPkgType()) 
							&& !("1775".equals(pkg.getPkgId()) ||   "1776".equals(pkg.getPkgId()) || "1777".equals(pkg.getPkgId())
									|| "1778".equals(pkg.getPkgId()) || "1846".equals(pkg.getPkgId()))){
						//什么都不做
					}else{
						// 套餐明细
						this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
					}
				}
			}

			// 动感地带套餐使用情况查询
			// 查询动感地带必选套餐信息
			// 查询动感地带可选套餐信息
			// 动感融合无限50元B套餐
			tcList = getPackageListByType(tcList_all, new String[] {"1019"}, false);

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
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("动感地带套餐");
					tcInfo.setPkgId(pkgUseState.getPkgId());
					reList.add(tcInfo);
					// 获取套餐优惠信息
					this.getAcAGetZoneInfo(accessId, config, params, pkgUseState.getPkgId(), tcInfo, pkgUseState.getPkgName(), res);

				}
			}

			// 新欢乐送使用情况查询
			// 套餐大类1013
			tcList = getPackageListByType(tcList_all, new String[] { "1013" }, true);
			// 筛选套餐 新欢乐送
			tcList = getPackageListByCode(tcList, NEW_HAPPY_PRESENT_1, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("新欢乐送");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询欢乐送使用情况
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
				}
			}

			// 亲情号码组合查询
			// 查询用户加入的亲情组合套餐信息
			List<GetMsngSubPackDT> groupList = this.getRelationGroupPckInfo(accessId, config, params, res);
			if (null != groupList && groupList.size() > 0) {

				for (GetMsngSubPackDT packDt : groupList) {
					if (Long.parseLong(this.config.getDistanceDT(packDt.getPackage_use_date(), this.config.getTodayChar14(), "s")) >= 0) {
						PkgInfo tcInfo = new PkgInfo();
						tcInfo.setPkgName("亲情号码组合");
						tcInfo.setPkgId(packDt.getPackage_code());
						reList.add(tcInfo);
						// 查询亲情号码优惠情况
						this.getAcAGetZoneInfo(accessId, config, params, res, packDt.getPackage_code(), this.map.get(packDt.getPackage_code()) == null ? "1元功能费" : String
								.valueOf(this.map.get(packDt.getPackage_code())), tcInfo);
					}
				}
			}

			// 省内亲情号码查询
			// 省内亲情号码查询
			List<FamilyNumber> snqqlist = this.queryProFamailyMsisdn(accessId, config, params, res);
			// 存在亲情号码信息
			if (snqqlist != null && snqqlist.size() > 0) {
				boolean isUserd = false; // 使用状况
				for (FamilyNumber dt : snqqlist) {
					// 对比开始时间
					String distanc = this.config.getDistanceDT(this.config.getTodayChar14(), dt.getBeginDate(), "s");
					if (Long.parseLong(distanc) <= 0) {
						isUserd = true;
						break;
					}
				}
				// 正在使用
				if (isUserd) {
					// 当前日期
					String queryDate = this.config.getTodayChar6();
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("省内亲情号码");
					tcInfo.setPkgId("1840");
					reList.add(tcInfo);
					// 优惠信息查询
					this.doAcAGetFreeItem2(accessId, config, params, queryDate, "1840", res, tcInfo);
				}
			}

			// 国内移动数据套餐查询
			// 筛选套餐
			tcList = getPackageListByType(tcList_all, GPRS_PACKAGE_TYPES_NEW_CMWAP, true);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("国内移动数据套餐");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 语音套餐查看
					this.doAcAGetFreeItem(accessId, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), tcInfo, res);
				}
			}
			
			//国内移动数据套餐3、10、20元叠加包
			tcList = getPackageListByCode(tcList_all, PKG_GNYDSJTCYDJB_CODES, true);
			if (null != tcList && tcList.size() > 0) {
				Map<String,String> qryList = new HashMap<String,String>();
				for (PkgUseState pkg : tcList) {
					//每个叠加包只查询一次
					if(qryList.containsKey(pkg.getPkgId()))
					{
						continue;
					}
					String queryDate = this.config.getTodayChar6();
					PkgInfo tcInfo = new PkgInfo();
					tcInfo.setPkgName("国内移动数据套餐叠加包");
					tcInfo.setPkgId(pkg.getPkgId());
					reList.add(tcInfo);
					// 查询套餐使用情况
					this.doAcAGetFreeItem3(accessId, config, params, queryDate,
							tcInfo.getPkgId(), res, tcInfo);
					qryList.put(pkg.getPkgId(),pkg.getPkgId());
				}
			}
			
			//=========================================================
			//TODO yangg add 查询含有流量的套餐  
			String[] fluxArr = {"1013","6020","6025","6023","1065","6024","6200",
								"6155","1009","6022","6215","6021","2000","6142","1046",
								"1019","6027","6040","6026","1050","1039","2004","1086","1085"}; //流量套餐大类目前就提供这些
			String fluxType = "";
			boolean flg = false;
			if (tcList_all != null){
				
				for(PkgUseState usePkg :tcList_all){
					
					
					for(int i = 0;i< fluxArr.length;i++){
						if(String.valueOf(usePkg.getPkgType()).equals(fluxArr[i])){ //主题
							fluxType = fluxArr[i];
							flg = true;
							break;
						}
					}
					//break;
					//if(fluxArr.usePkg.getPkgType())
				}
			}
				
			
			if(flg){ //如果已开通套餐中含有配置表中的大类，再查询该大类的套子中有误配置表中的套餐编码
				if(tcList_all != null && tcList_all.size() > 0){
					List<Map>  fluxPkgList = packageChangeDAO.getFluxPkgCode(fluxType); //根据大类key值取出缓存套餐信息
					List<FluxPkgInfo> list = null;
					if(fluxPkgList != null && fluxPkgList.size() > 0){
						list = new ArrayList<FluxPkgInfo>(fluxPkgList.size());
						FluxPkgInfo bean = null;
						for (Map<String,String> m:fluxPkgList) {
							bean = new FluxPkgInfo();
							
							bean.setPkgCode(String.valueOf(StringUtil.convertNull((String) m.get("F_PKG_CODE"))));
							bean.setPkgType(String.valueOf(StringUtil.convertNull((String) m.get("F_PKG_TYPE"))));
							bean.setTypeName(String.valueOf(StringUtil.convertNull((String) m.get("F_TYPE_NAME"))));
							bean.setPkgName(String.valueOf(StringUtil.convertNull((String) m.get("F_PKG_NAME"))));
							list.add(bean);
						}
					}
					for(PkgUseState usePkg :tcList_all){
						if(String.valueOf(usePkg.getPkgType()).equals(fluxType))
						{
							for(FluxPkgInfo b :list){
								if(b.getPkgCode().trim().equals(usePkg.getPkgId())){
									String queryDate = this.config.getTodayChar6();
									PkgInfo tcInfo = new PkgInfo();
									tcInfo.setPkgName(b.getPkgName());
									tcInfo.setPkgId(b.getPkgCode());
									reList.add(tcInfo);
									// 查询GPRS流量使用情况
									this.doAcAGetFreeItem3(accessId, config, params, queryDate, tcInfo.getPkgId(), res, tcInfo);
									break;
								}
							}
						}
					}
				}
			}
		
			
			// WLAN套餐
			// 筛选套餐
			tcList = getPackageListByType(tcList_all, new String[] {"1401","1402","1403","1010"}, false);
			if (null != tcList && tcList.size() > 0) {
				for (PkgUseState pkg : tcList) {
					PkgInfo tcInfo = new PkgInfo();
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
					PkgInfo tcInfo = new PkgInfo();
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
		return res;
	}
	
	/**
	 * 查询动感无线套餐和WLAN套餐使用情况
	 * @param accessId
	 * @param params
	 * @param tcList
	 */
	@SuppressWarnings("unchecked")
	private void getMZonePkgAndWLanPkg(String accessId, List<RequestParameter> params, List<PkgInfo> tcList) {
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
					PkgInfo info = new PkgInfo();
					if("1623,1624,1625,1626,1627,1628".indexOf(packageCode) > -1){
						//动感无限套餐
						info.setPkgName("动感无限套餐");
						
					}
					else if("1629,1630,1631,1632,1633,1634".indexOf(packageCode) > -1){
						//WLAN分区套餐
						info.setPkgName("WLAN分区套餐");
					}
					
					List<PkgUsedInfo> pkgUsedInfos = new ArrayList<PkgUsedInfo>();
					for(int i = 0; i < discountCfgDtNodes.size(); i ++){
						Node discountCfgDtNode = discountCfgDtNodes.get(i);
						Node discountUseDtNode = discountUseDtNodes.get(i);
						
						String packcode1 = discountCfgDtNode.selectSingleNode("./package_code").getText().trim();
						String packcode2 = discountUseDtNode.selectSingleNode("./package_code").getText().trim();
						
						if(packcode1.equals(packageCode) && packcode2.equals(packageCode)){
							PkgUsedInfo pkgUsedInfo = new PkgUsedInfo();
							pkgUsedInfo.setPkgName(packageName);
							
							//总量
							String totalStr = discountCfgDtNode.selectSingleNode("./discount_amount").getText().trim();
							pkgUsedInfo.setTotal(Long.valueOf(totalStr));
							
							//使用量
							String remainStr = discountUseDtNode.selectSingleNode("./use_amount").getText().trim();
							pkgUsedInfo.setRemain(Long.valueOf(remainStr));
							
							//单位
							String flag = discountUseDtNode.selectSingleNode("./discount_type").getText().trim();
							pkgUsedInfo.setFlag(changeLocalType(flag));
							pkgUsedInfos.add(pkgUsedInfo);
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
	private void getAcAGetZoneInfo(String accessId, ServiceConfig config, List<RequestParameter> params, String acrelation_revision, PkgInfo pkgInfo, String pkgName,
			QRY040020Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		RequestParameter par = null; // 参数
		String resp_code = ""; // 返回编码
		PkgUsedInfo pkg = null; // 套餐使用情况

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
					List<PkgUsedInfo> reList = new ArrayList<PkgUsedInfo>();
					// 短信
					if (!"".equals(this.config.getChildText(content, "arecord_count"))) {
						pkg = new PkgUsedInfo();
						pkg.setPkgName(pkgName + "短信");
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
						pkg = new PkgUsedInfo();
						pkg.setPkgName(pkgName + "IP电话");
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
						pkg = new PkgUsedInfo();
						pkg.setPkgName(pkgName + "通话时间");
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
						pkg = new PkgUsedInfo();
						pkg.setPkgName(pkgName + "彩信");
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
					if (!"".equals(gprs) && !"0".equals(gprs)) {
						pkg = new PkgUsedInfo();
						pkg.setPkgName(pkgName + "GPRS");
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
					pkgInfo.setSubUsedInfoList(reList);
				}
				//增加套餐失败信息显示
				else
				{
					List<PkgUsedInfo> subErrorList = new ArrayList<PkgUsedInfo>();
					PkgUsedInfo errorInfo = new PkgUsedInfo();
					errorInfo.setPkgName(pkgName);
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					pkgInfo.setSubUsedInfoList(subErrorList);
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
	private void doAcAGetFreeItem(String accessId, List<RequestParameter> params, String queryDate, String packageCode, String pkgName, PkgInfo tcInfo, QRY040020Result res) {
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
					errDt = this.wellFormedDAO.transBossErrCode("QRY040020", "ac_agetfreeitem_517", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && "0000".equals(resp_code)) {
					List freeitemList = this.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						List<PkgUsedInfo> subList = new ArrayList<PkgUsedInfo>();
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

							PkgUsedInfo info = new PkgUsedInfo();
							//对于88套餐c明细做的修改
							if("2376".equals(packageId)||"2377".equals(packageId)||"2378".equals(packageId)){
								if("3".equals(a_freeitem_id)){
									info.setPkgName("赠送GPRS上网流量");
								}else if("4".equals(a_freeitem_id)){
									info.setPkgName("赠送WLAN上网分钟数");
								}else if("7".equals(a_freeitem_id)){
									info.setPkgName("国内（不含港澳台）主叫分钟数");
								}
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
						tcInfo.setSubUsedInfoList(subList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<PkgUsedInfo> subErrorList = new ArrayList<PkgUsedInfo>();
					PkgUsedInfo errorInfo = new PkgUsedInfo();
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
	private List<PkgUseState> queryAllPersonalPackage(String accessId, List<RequestParameter> params, QRY040020Result res) throws Exception {
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
				errDt = this.wellFormedDAO.transBossErrCode("QRY040020", "cc_find_package_62_YYTC", resp_code);
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
	private void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040020Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) || "-14198".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY040020", xmlName, resp_code);
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
	private List getRelationGroupPckInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040020Result res) {
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
					errDt = this.wellFormedDAO.transBossErrCode("QRY040020", "cc_cgetmsngsubpack_352", resp_code);
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
	private void getAcAGetZoneInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040020Result res, String acrelation_revision, String pkgName,
			PkgInfo pkgInfo) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		Element content = null;
		RequestParameter r = null;
		PkgUsedInfo pkg = null;
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
					errDt = this.wellFormedDAO.transBossErrCode("QRY040020", "ac_agetzoneinfo_518", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				content = this.config.getElement(root, "content");
				List<PkgUsedInfo> reList = new ArrayList<PkgUsedInfo>();
				// 短信
				if (!"".equals(this.config.getChildText(content, "arecord_count"))) {
					pkg = new PkgUsedInfo();
					pkg.setPkgName(pkgName + "短信");
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
					pkg = new PkgUsedInfo();
					pkg.setPkgName(pkgName + "IP电话");
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
					pkg = new PkgUsedInfo();
					pkg.setPkgName(pkgName + "通话时间");
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
					pkg = new PkgUsedInfo();
					pkg.setPkgName(pkgName + "彩信");
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
					pkg = new PkgUsedInfo();
					pkg.setPkgName(pkgName + "GPRS");
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
				pkgInfo.setSubUsedInfoList(reList);
			}
			//增加套餐失败信息显示
			else
			{
				List<PkgUsedInfo> subErrorList = new ArrayList<PkgUsedInfo>();
				PkgUsedInfo errorInfo = new PkgUsedInfo();
				errorInfo.setPkgName(pkgName);
				errorInfo.setFlag(-1);
				errorInfo.setRemain(-1);
				errorInfo.setTotal(-1);
				subErrorList.add(errorInfo);
				pkgInfo.setSubUsedInfoList(subErrorList);
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
	private List<FamilyNumber> queryProFamailyMsisdn(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040020Result res) {
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
	 * 语音套餐查看
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param queryDate
	 * @param packageCode
	 */
	private void doAcAGetFreeItem2(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, QRY040020Result res, PkgInfo pkgInfo) {
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
						PkgUsedInfo pkg = new PkgUsedInfo();
						String strTotal = this.config.getChildText(e, "a_freeitem_total_value");
						String strUse = this.config.getChildText(e, "a_freeitem_value");
						if (strTotal != null && strTotal.trim().length() > 0 && strUse != null && strUse.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strTotal));
							pkg.setRemain(Long.parseLong(strTotal) - Long.parseLong(strUse));
						}
						List<PkgUsedInfo> reList = new ArrayList<PkgUsedInfo>();
						reList.add(pkg);
						pkgInfo.setSubUsedInfoList(reList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<PkgUsedInfo> subErrorList = new ArrayList<PkgUsedInfo>();
					PkgUsedInfo errorInfo = new PkgUsedInfo();
					errorInfo.setPkgName("省内亲情号码组合");
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					pkgInfo.setSubUsedInfoList(subErrorList);
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
			List<PkgInfo> reList, QRY040011Result res)
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
								for (PkgInfo pkg : reList)
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
	
	private void doAcAGetFreeItem3(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, QRY040020Result res, PkgInfo pkgInfo) {
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
						PkgUsedInfo pkg = new PkgUsedInfo();
						String strTotal = this.config.getChildText(e, "a_freeitem_total_value");
						String strUse = this.config.getChildText(e, "a_freeitem_value");
						if (strTotal != null && strTotal.trim().length() > 0 && strUse != null && strUse.trim().length() > 0) {
							pkg.setPkgName("GPRS");
							pkg.setTotal(Long.parseLong(strTotal)*1024);
							pkg.setFlag(4);
							pkg.setRemain((Long.parseLong(strTotal) - Long.parseLong(strUse))*1024);
						}
						List<PkgUsedInfo> reList = new ArrayList<PkgUsedInfo>();
						reList.add(pkg);
						pkgInfo.setSubUsedInfoList(reList);
					}
				}
				//增加套餐失败信息显示
				else
				{
					List<PkgUsedInfo> subErrorList = new ArrayList<PkgUsedInfo>();
					PkgUsedInfo errorInfo = new PkgUsedInfo();
					errorInfo.setPkgName(pkgInfo.getPkgName());
					errorInfo.setFlag(-1);
					errorInfo.setRemain(-1);
					errorInfo.setTotal(-1);
					subErrorList.add(errorInfo);
					pkgInfo.setSubUsedInfoList(subErrorList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
