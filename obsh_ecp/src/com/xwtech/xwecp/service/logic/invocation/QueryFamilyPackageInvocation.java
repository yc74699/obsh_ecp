package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceException;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FamilyMember;
import com.xwtech.xwecp.service.logic.pojo.FamilyMemberInfoBean;
import com.xwtech.xwecp.service.logic.pojo.FamilyPkgUse;
import com.xwtech.xwecp.service.logic.pojo.FamilyPkgUseState;
import com.xwtech.xwecp.service.logic.pojo.QRY020012Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040011Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 家庭套餐使用情况查询
 * 
 * @author 杨光
 * 
 */
public class QueryFamilyPackageInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(UserIncrementsInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	//家庭成员信息： 号码，用户ID,品牌
	private List<FamilyMemberInfoBean> memberInfoList = null;
	
	//家庭套餐的套餐编码
	private static final String[] FAMILY_PACKAGE_CODES = {"1613","1615","1616","1617","1618","1619","1620","1621","1622","1672","1674","1711","1712","1751","1752","1753","1754","1755","1756",
		"1757","1758","1759","1760","1761","1762","1763","1764","1765","1766","1767","1768","1783","2023","2024","2025","2039","2040",
		"2041","2042","2043","2044","2045","2046","2047","2048","2049","2050","2065","2066","2067","2068","2069"}; 
	

	public QueryFamilyPackageInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY020012Result res = new QRY020012Result();
		QRY040011Result res2 = new QRY040011Result();
		List<FamilyMember> memberList = null;
		
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			String user_id = (String)this.getParameters(params, "context_user_id");
			//在用的家庭套餐及套餐使用情况详细
			List<FamilyPkgUseState> useStateList = new ArrayList<FamilyPkgUseState>();
			//用于返回的家庭套餐及套餐使用情况详细
			List<FamilyPkgUseState> retList = new ArrayList<FamilyPkgUseState>();
			//查询用户当前使用的家庭套餐信息
			BaseResult FamilyPkgUseStateResult = null;
			FamilyPkgUseStateResult = this.queryFamilyPackage(accessId, config, params, res2);
			if(LOGIC_SUCESS.equals(FamilyPkgUseStateResult.getResultCode())){
				useStateList = (List<FamilyPkgUseState>)FamilyPkgUseStateResult.getReObj();
				//家庭套餐只有一个。
				//begin 修改查询结果：同时开通家庭短号免费版和移动之家i系列139元套餐(省内)
				if (useStateList != null && useStateList.size() > 0) {
					for (FamilyPkgUseState fs : useStateList) {
						// end
						// 取得memberList
						for (RequestParameter par : params) {
							// 转换寄送类型
							if ("memberMobileNum"
									.equals(par.getParameterName())) {
								memberList = (List) par.getParameterValue();
								if (null != memberList && memberList.size() > 0) {
									memberInfoList = new ArrayList<FamilyMemberInfoBean>();
									for (FamilyMember dt : memberList) {
										FamilyMemberInfoBean tempMember = new FamilyMemberInfoBean();
										tempMember
												.setPhoneNum(dt.getPhoneNum());
										tempMember.setUserId(dt.getUserId());
										tempMember.setQQT(this.isQQT(dt
												.getPhoneNum(), accessId));
										memberInfoList.add(tempMember); //家庭成员信息：
																		// 号码
																		// ，用户ID
																		// ,品牌
									}
								}
							}
						}
						BaseResult allPckInfoResult = null;

						// 存在家庭套餐查询详细
						String todayDate = DateTimeUtil.getTodayChar6();
						params.add(new RequestParameter("queryType", "1"));
						params
								.add(new RequestParameter("queryMonth",
										todayDate));
						params.add(new RequestParameter("pckCode", fs
								.getPkgId()));
						allPckInfoResult = this.getFamilyPackageDetailResult(
								accessId, config, params, res);
						params.remove(params.size()-1);
						if (LOGIC_SUCESS.equals(allPckInfoResult
								.getResultCode())) {
							List<AcAcQueryhdivBillPackageBean> detailList = (List<AcAcQueryhdivBillPackageBean>) allPckInfoResult
									.getReObj();
							List<FamilyPkgUse> faPckList = new ArrayList<FamilyPkgUse>();
							if(detailList.size() > 0)
							{
								faPckList = (List<FamilyPkgUse>) buildDetailInfo(
										detailList, user_id);
								retList = buildRetList(useStateList, faPckList,fs.getPkgId());
							}
							
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(allPckInfoResult.getErrorCode());
							res.setErrorMessage(allPckInfoResult
									.getErrorMessage());

						}
					}
				}else{
					//多个家庭套餐。出错TODO
					retList.clear();
					res.setResultCode(LOGIC_ERROR);
				}
			}else{
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(FamilyPkgUseStateResult.getErrorCode());
				res.setErrorMessage(FamilyPkgUseStateResult.getErrorMessage());
			}
			res.setFamilyPkgUseState(retList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		return res;
	}

	
	/**
	 * 构建标准的数据结构
	 * @param List1
	 * @param pkgUseList
	 * @return
	 */
	private List<FamilyPkgUseState>  buildRetList(List<FamilyPkgUseState> pkgUseStateList, List<FamilyPkgUse> pkgUseList,String pkgId){
		List<FamilyPkgUseState> retList = new  ArrayList<FamilyPkgUseState>();
//		for(FamilyPkgUseState b1: List1){
		//begin 修改查询结果：同时开通家庭短号免费版和移动之家i系列139元套餐(省内)
		for(int i = 0; i < pkgUseStateList.size(); i++)
		{
			FamilyPkgUseState b1 = pkgUseStateList.get(i);
			if(b1.getPkgId().equals(pkgId))
			{
				FamilyPkgUseState bean1 = new FamilyPkgUseState();
				bean1.setPkgId(b1.getPkgId());
				if(pkgUseList != null && pkgUseList.size() > 0){
					bean1.setPkgName(pkgUseList.get(0).getName());
				}
				bean1.setPkgDesc(b1.getPkgDesc());
				bean1.setPkgType(b1.getPkgType());
				bean1.setBeginDate(b1.getBeginDate());
				bean1.setEndDate(b1.getEndDate());
				bean1.setPkgUse(pkgUseList);
				retList.add(bean1);
			}
			else
			{
				retList.add(b1);
			}
			//end
		}
		
		return retList;
	}
	
	/**
	 * 构建"通信量信息"数据
	 * 
	 * @param pkgList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<FamilyPkgUse> buildDetailInfo(List<AcAcQueryhdivBillPackageBean> pkgList,String userId) {
		FamilyPkgUse retBean = new FamilyPkgUse();
		List retList = new ArrayList();
		String pckName = "";
	
		long bill701 = 0;
		long bill801 = 0;
		long bill802 = 0;
		long bill803 = 0;
		long bill804 = 0;
		long tootlebill701 = 0;
		long tootlebill801 = 0;
		long tootlebill802 = 0;
		long tootlebill803 = 0;
		long tootlebill804 = 0;
		for (AcAcQueryhdivBillPackageBean billbean : pkgList) {
			pckName = billbean.getPackage_name();
			//过滤：套餐所有的实际消费
			if ("-1".equals(billbean.getPackage_fee_sub_id())) {
				continue;
			} 
			// Show_type 2类型才为流量计算，其他忽略
			if (!"2".equalsIgnoreCase(billbean.getPackage_show_type())) {
				continue;
			}
			if ("701".equals(billbean.getPackage_freeitem_id()) && userId.equals(billbean.getPackage_user_id())) {
				if (!StringUtil.isNull(billbean.getPackage_package_value())){
					Long package_value = new Long(billbean.getPackage_package_value());
					bill701 = bill701 + package_value.longValue();
				}
				if (!StringUtil.isNull(billbean.getPackage_package_fee())) {
					tootlebill701 = new Long(billbean.getPackage_freeitem_value()).longValue();
				}
			}
			//801需特殊处理
			//全球通用户，则通信量明细不展示4 国内漫游(分钟)，而展示15 国内漫游（主叫）(分钟)和16 国内漫游（被叫）(分钟)；其他都算
			//如果用户非全球通用户，则展示4 国内漫游(分钟)，不展示展示15 国内漫游（主叫）(分钟)和16 国内漫游（被叫）(分钟)。其他都算

			 
			if ("801".equals(billbean.getPackage_freeitem_id())) {
				if(memberInfoList !=null && memberInfoList.size()>0){
					for(FamilyMemberInfoBean bean : memberInfoList){
						if(bean.getUserId().equals(billbean.getPackage_user_id()) && bean.isQQT()){
							if(!"4".equals(billbean.getPackage_fee_sub_id())){
								if (!StringUtil.isNull(billbean.getPackage_package_value())){
									Long package_value = new Long(billbean.getPackage_package_value());
									bill801 = bill801 + package_value.longValue();
								}
								if (!StringUtil.isNull(billbean.getPackage_package_fee())) {
									tootlebill801 = new Long(billbean.getPackage_freeitem_value()).longValue();
								}
							}
						}else if (bean.getUserId().equals(billbean.getPackage_user_id()) && !bean.isQQT()){
							if(!("15".equals(billbean.getPackage_fee_sub_id()) || "16".equals(billbean.getPackage_fee_sub_id()))){
								if (!StringUtil.isNull(billbean.getPackage_package_value())){
									Long package_value = new Long(billbean.getPackage_package_value());
									bill801 = bill801 + package_value.longValue();
								}
								if (!StringUtil.isNull(billbean.getPackage_package_fee())) {
									tootlebill801 = new Long(billbean.getPackage_freeitem_value()).longValue();
								}
							}
						}
						
					}
					
					
					
				}
			}
			if ("802".equals(billbean.getPackage_freeitem_id())) {
				if (!StringUtil.isNull(billbean.getPackage_package_value())){
					Long package_value = new Long(billbean.getPackage_package_value());
					bill802 = bill802 + package_value.longValue();
				}
				
				if (!StringUtil.isNull(billbean.getPackage_package_fee())) {
					tootlebill802 = new Long(billbean.getPackage_freeitem_value()).longValue();
				}
			}
			if ("803".equals(billbean.getPackage_freeitem_id())) {
				if (!StringUtil.isNull(billbean.getPackage_package_value())){
					Long package_value = new Long(billbean.getPackage_package_value());
					bill803 = bill803 + package_value.longValue();
					
				}
				if (!StringUtil.isNull(billbean.getPackage_package_fee())) {
					tootlebill803 = new Long(billbean.getPackage_freeitem_value()).longValue();
				}
			}
			if ("804".equals(billbean.getPackage_freeitem_id())) {
				if (!StringUtil.isNull(billbean.getPackage_package_value())){
					Long package_value = new Long(billbean.getPackage_package_value());
					bill804 = bill804 + package_value.longValue();
					Long total = new Long(billbean.getPackage_freeitem_value()).longValue();
				}
				if (!StringUtil.isNull(billbean.getPackage_package_fee())) {
					tootlebill804 = new Long(billbean.getPackage_freeitem_value()).longValue();
				}
			}
		}
		
		if (tootlebill701 != 0) {
			retList.add(setFreeItem("11", bill701, pckName, tootlebill701));
		}
		if (tootlebill801 != 0) {
			retList.add(setFreeItem("12", bill801, pckName, tootlebill801));
		}
		if (tootlebill802 != 0) {
			retList.add(setFreeItem("13", bill802, pckName, tootlebill802));
		}
		if (tootlebill803 != 0) {
			retList.add(setFreeItem("14", bill803, pckName, tootlebill803));
		}
		if (tootlebill804 != 0) {
			retList.add(setFreeItem("15", bill804, pckName, tootlebill804));
		}
		return retList;
	}
	
	/**
	 * 
	 * @param freeItemId
	 * @param value
	 * @param pkgName
	 * @param totalValue
	 * @return
	 */
	private FamilyPkgUse setFreeItem(String freeItemId, long value, String pkgName,long totalValue) {
		FamilyPkgUse freeItem = new FamilyPkgUse();
			freeItem.setName(pkgName);
			freeItem.setTotal(String.valueOf(totalValue));
			freeItem.setUse(String.valueOf(value));
			freeItem.setRemain(String.valueOf(totalValue - value > 0?(totalValue - value):0));
			freeItem.setFlag(freeItemId);
		return freeItem;
	}
	
	/**
	 * 查询已开通的家庭套餐-用于查询家庭套餐使用情况
	 * 家庭套餐通过集团套餐查询接口查得。
	 * 
	 * @param accessId
	 * @param res
	 * @throws Exception
	 */
	protected BaseResult queryFamilyPackage(String accessId, ServiceConfig config, List<RequestParameter> params, QRY040011Result result) throws Exception {
		BaseResult res = new BaseResult();
		String rspXml = "";
		List<FamilyPkgUseState> reList = new ArrayList<FamilyPkgUseState>();

		FamilyPkgUseState dt = null;

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
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					List userList = root.getChild("content").getChildren("package_user_id");
					
					if (null != userList && userList.size() > 0 )
					{

						for (int i = 0; i < userList.size(); i++)
						{
							Element cplanpackagedt = ((Element)userList.get(i)).getChild("cplanpackagedt");
							if (null != cplanpackagedt)
							{
								String tempType = p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll("");
								String tempEndDate = p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll("");
								
								//集团套餐中根据套餐大类过滤出家庭套餐 。已知 2000是家庭套餐套餐大类
								if("2000".equals(tempType) && StringUtil.isNull(tempEndDate)){
									dt = new FamilyPkgUseState();
									dt.setPkgId(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
									dt.setPkgType(tempType);
									dt.setBeginDate(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll(""));
									dt.setEndDate(tempEndDate);
									reList.add(dt);
								}
							}
						}
						res.setReObj(reList);
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	
		return res;
	}
	
	
	/**
	 * 家庭套餐使用情况查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getFamilyPackageDetailResult(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final QRY020012Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String pckCode = (String) getParameters(params, "pckCode");
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_acqueryhdivbill_772", params);

			//logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_acqueryhdivbill_772", super.generateCity(params)));
			//logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020012", "ac_acqueryhdivbill_772", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
				
					XPath xpath = XPath.newInstance("/operation_out/content/package_user_id/package_dt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					AcAcQueryhdivBillPackageBean packBean = null;
					List<AcAcQueryhdivBillPackageBean> listPackage = new ArrayList<AcAcQueryhdivBillPackageBean>();
					String tempPckCode = "";
					for (Element element : list) {
						//老套餐是短编码；新套餐是长编码，做以下处理
						if(pckCode != null && pckCode.startsWith("200000")){
							tempPckCode = pckCode;
						}else if (pckCode != null && !pckCode.startsWith("200000")){
							tempPckCode = "200000" + pckCode;
						} 
						if(tempPckCode.equals(element.getChildTextTrim("package_package_code")) &&
								!"-1".equals(element.getChildTextTrim("package_fee_sub_id"))){
							packBean = new AcAcQueryhdivBillPackageBean();
							packBean.setPackage_display_order(element.getChildTextTrim( "package_display_order"));
							packBean.setPackage_name(element.getChildTextTrim( "package_name"));
							packBean.setPackage_sub_name(element.getChildTextTrim( "package_sub_name"));
							packBean.setPackage_show_type(element.getChildTextTrim( "package_show_type"));
							packBean.setPackage_month(element.getChildTextTrim( "package_month"));
							packBean.setPackage_business_id(element.getChildTextTrim( "package_business_id"));
							packBean.setPackage_package_code(element.getChildTextTrim( "package_package_code"));
							packBean.setPackage_freeitem_value(element.getChildTextTrim( "package_freeitem_value"));
							packBean.setPackage_package_fee(element.getChildTextTrim( "package_package_fee"));
							packBean.setPackage_package_value(element.getChildTextTrim( "package_package_value"));
							packBean.setPackage_freeitem_id(element.getChildTextTrim( "package_freeitem_id"));
							packBean.setPackage_fee_sub_id(element.getChildTextTrim( "package_fee_sub_id"));
							packBean.setPackage_user_id(element.getChildTextTrim( "package_user_id"));
							listPackage.add(packBean);
						}
					}
					res.setReObj(listPackage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * 根据手机号码查brandid
	 */
	private boolean isQQT(String phoneNum,String accessId) throws ServiceException {
		boolean isqqtFlag = false;
		String reqXml = "";
		String rspXml = "";
		List<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(new RequestParameter("phoneNum",phoneNum));
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params);

			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", this.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/user_info");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
						String brandid = element.getChildText("user_brand_id").trim();
						//判断是否全球通
						isqqtFlag = "1".equals(brandid)? true:false;
						break;
					}
				}else{
					
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return isqqtFlag;
		
		
	}


	/**
	 * 解析报文
	 * 
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp) {
		Element root = null;
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return root;
	}

	private Boolean isSpecilPackage(String pckCode, String[] arr){

		boolean retCode = false;
		if(arr != null  && arr.length>0)
		for(int i = 0; i<arr.length; i++){
			if(pckCode.equals(arr[i])){
				retCode = true;
				break;
			}
		}
		return retCode;
	}
	public class AcAcQueryhdivBillPackageBean {

		/**用户标识*/
		private String package_user_id;
		
		/**资费子项*/
		private String package_fee_sub_id;
		
		/**资费项*/
		private String package_freeitem_id;
		
		/**实扣金额*/
		private String package_package_value;
		
		private String package_package_fee;
		
		private String package_freeitem_value;
		
		/**套餐代码*/
		private String package_package_code;
		
		private String package_business_id;
		
		/**月份*/
		private String package_month;
		
		/**套餐类型*/
		private String package_show_type;
		
		/**资费子项名称*/
		private String package_sub_name;
		
		/**套餐名称*/
		private String package_name;
		
		/**显示顺序*/
		private String package_display_order;

		public String getPackage_business_id() {
			return package_business_id;
		}

		public void setPackage_business_id(String package_business_id) {
			this.package_business_id = package_business_id;
		}

		public String getPackage_display_order() {
			return package_display_order;
		}

		public void setPackage_display_order(String package_display_order) {
			this.package_display_order = package_display_order;
		}

		public String getPackage_fee_sub_id() {
			return package_fee_sub_id;
		}

		public void setPackage_fee_sub_id(String package_fee_sub_id) {
			this.package_fee_sub_id = package_fee_sub_id;
		}

		public String getPackage_freeitem_id() {
			return package_freeitem_id;
		}

		public void setPackage_freeitem_id(String package_freeitem_id) {
			this.package_freeitem_id = package_freeitem_id;
		}

		public String getPackage_freeitem_value() {
			return package_freeitem_value;
		}

		public void setPackage_freeitem_value(String package_freeitem_value) {
			this.package_freeitem_value = package_freeitem_value;
		}

		public String getPackage_month() {
			return package_month;
		}

		public void setPackage_month(String package_month) {
			this.package_month = package_month;
		}

		public String getPackage_name() {
			return package_name;
		}

		public void setPackage_name(String package_name) {
			this.package_name = package_name;
		}

		public String getPackage_package_code() {
			return package_package_code;
		}

		public void setPackage_package_code(String package_package_code) {
			this.package_package_code = package_package_code;
		}

		public String getPackage_package_fee() {
			return package_package_fee;
		}

		public void setPackage_package_fee(String package_package_fee) {
			this.package_package_fee = package_package_fee;
		}

		public String getPackage_package_value() {
			return package_package_value;
		}

		public void setPackage_package_value(String package_package_value) {
			this.package_package_value = package_package_value;
		}

		public String getPackage_show_type() {
			return package_show_type;
		}

		public void setPackage_show_type(String package_show_type) {
			this.package_show_type = package_show_type;
		}

		public String getPackage_sub_name() {
			return package_sub_name;
		}

		public void setPackage_sub_name(String package_sub_name) {
			this.package_sub_name = package_sub_name;
		}

		public String getPackage_user_id() {
			return package_user_id;
		}

		public void setPackage_user_id(String package_user_id) {
			this.package_user_id = package_user_id;
		}
		
		
	}
}