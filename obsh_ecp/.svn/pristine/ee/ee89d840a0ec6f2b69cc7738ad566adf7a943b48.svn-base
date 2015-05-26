package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040051Result;
import com.xwtech.xwecp.util.CommonUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 集团V网使用情况查询门户和手机客户端使用
 * 
 * @author 陈小明
 * 
 */
public class QueryGroupUserUsedInfoInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(QueryGroupUserUsedInfoInvocation.class);

	private Map<String,String> map;

	public QueryGroupUserUsedInfoInvocation() {
		if (null == this.map) {
			map = new HashMap<String, String>();
			this.map.put("1586", "1元包本地主叫200分钟(09版集团套餐)");
			this.map.put("1587", "3元包本地主叫500分钟(09版集团套餐)");
			this.map.put("1588", "5元包本地主叫800分钟(09版集团套餐)");
			this.map.put("1589", "5元包省内主叫500分钟(09版集团套餐)");
			this.map.put("1590", "10元包省内主叫800分钟(09版集团套餐)");
			this.map.put("2000003711", "5元包集团200分钟和本地网内100分钟");
			this.map.put("2000003712", "10元包集团500分钟和本地网内200分钟");
			this.map.put("2000003713", "15元包集团800分钟和本地网内300分钟");
		}
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY040051Result res = new QRY040051Result();
		String newProdid="";
		String bossTempate = "cc_grpvpmnusequery";
		try {
			this.getPkgId(accessId, config, params, res);
			if(!CommonUtil.isNull(res.getPkgInfoList()))
			{
				//套餐编码设置为长编码
				if(res.getPkgInfoList().get(0).getPkgId().length()==10)
					newProdid  =  res.getPkgInfoList().get(0).getPkgId().toString();
				else
					newProdid  =  "200000" + res.getPkgInfoList().get(0).getPkgId().toString();
			}
			else
			{
				return res;
			}
			//获取集团使用情况信息
			getVNetUseInfo(accessId, params, res, newProdid, bossTempate);

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;

	}

	private void getVNetUseInfo(String accessId, List<RequestParameter> params,
			QRY040051Result res, String newProdid, String bossTempate)
			throws CommunicateException, Exception {
		String reqXml;
		String rspXml;
		params.add(new RequestParameter("prodid", newProdid));
		params.add(new RequestParameter("qryMonth", DateTimeUtil.getTodayChar6()));
		if (res.getPkgInfoList().size() > 0) {
			//组装报文
			reqXml = mergeReqXML2Boss(params,bossTempate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,
						bossTempate);
				if (!StringUtil.isNull(rspXml))
				{
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
					setCommonResult(res, resp_code, resp_desc);

					if (BOSS_SUCCESS.equals(resp_code)) {
						List<PkgUsedInfo> pkgUsedList = new ArrayList<PkgUsedInfo>();
						PkgUsedInfo pkgUsedSing = null;
						List GrpsSingleList = XMLUtil.getChildList(rspXml,"content","grpvpmninfo");
						if (!CommonUtil.isNull(GrpsSingleList)) {
							for (int i = 0; i < GrpsSingleList.size(); i++) {
								pkgUsedSing = new PkgUsedInfo();
								Element grpDT = ((Element) GrpsSingleList.get(i));
								if (null != grpDT) {
									pkgUsedSing.setTotal(Long.parseLong(grpDT.getChildText("alldata")));
									pkgUsedSing.setRemain(Long.parseLong(grpDT.getChildText("leftdata")));
									pkgUsedSing.setFlag(7);
									pkgUsedSing.setPkgName("通话时长");
								}
								//修改  boss返回三条重复数据
								if(!CommonUtil.isNull(pkgUsedList))
								{
									continue;
								}
								pkgUsedList.add(pkgUsedSing);
							}
						}
						res.getPkgInfoList().get(0).setSubUsedInfoList(
								pkgUsedList);
					}
				}
			}
		}
	}

	public void getPkgId(String accessId, ServiceConfig config,
			List<RequestParameter> params, QRY040051Result res) {

		String reqXml = "";
		String rspXml = "";
		String pkgId = "";
		String pkgType = "";
		String pkgName = "";
		String pkgStartDate="";
		String pkgEndDate="";
		String bossTempate = "cc_cgetgroupackage_552";
		try {
			//组装报文
			reqXml = mergeReqXML2Boss(params,bossTempate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,
						bossTempate);
				if (!StringUtil.isNull(rspXml))
				{
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
					setCommonResult(res, resp_code, resp_desc);
					
					if (BOSS_SUCCESS.equals(resp_code)) {
						List gsubs = XMLUtil.getChildList(rspXml,"content","package_user_id");
						List<PkgInfo> pkgInfoList = new ArrayList<PkgInfo>();
						PkgInfo pkgInfo = null;
						if (null != gsubs && gsubs.size() > 0) {
							for (int i = 0; i < gsubs.size(); i++) {
								
								Element gsubDT = XMLUtil.getChild((Element)gsubs.get(i), "cplanpackagedt");
								if (null != gsubDT) {
									pkgInfo = new PkgInfo();
									pkgType = XMLUtil.getChildText(gsubDT,"package_type");
									pkgId = XMLUtil.getChildText(gsubDT,"package_code");
									pkgStartDate = XMLUtil.getChildText(gsubDT,"package_use_date");
									pkgEndDate = XMLUtil.getChildText(gsubDT,"package_end_date");
									if ((pkgType.equals("1001")&& "".equals(pkgEndDate)&& pkgStartDate.compareTo(DateTimeUtil.getTodayChar8())<0)
											|| (pkgType.equals("1001") && pkgEndDate.compareTo(DateTimeUtil.getTodayChar8()) > 0) &&
											pkgStartDate.compareTo(DateTimeUtil.getTodayChar8())<0) {
										pkgName = getPkgName(accessId, config, params,
												pkgId);
										pkgInfo.setPkgId(pkgId);
										pkgInfo.setPkgName(pkgName);
										pkgInfo.setPkgDec("");
										pkgInfo.setGroupName(getGroupName(accessId, config,params));
										pkgInfoList.add(pkgInfo);
										res.setPkgInfoList(pkgInfoList);
									}
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public String getPkgName(String accessId, ServiceConfig config,
			List<RequestParameter> params, String pkgId) {

		String reqXml = "";
		String rspXml = "";
		String bossTempate = "cc_cqryoptpackage_466";
		try {
			//组装报文
			reqXml = mergeReqXML2Boss(params,bossTempate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,
						bossTempate);
				if (!StringUtil.isNull(rspXml))
				{
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					if (BOSS_SUCCESS.equals(resp_code)) {
						List gsubs = XMLUtil.getChildList(rspXml,"content","businesspackagechoice_business_id");
						if (null != gsubs && gsubs.size() > 0) {
							for (int i = 0; i < gsubs.size(); i++) {
								Element gsubDT = XMLUtil.getChild((Element)gsubs.get(i), "cbusinesspackagechoicedt");
								if (null != gsubDT) {
									if (pkgId.equals(XMLUtil.getChildText(gsubDT,"businesspackagechoice_package_code"))) {
										return XMLUtil.getChildText(gsubDT,"businesspackagechoice_package_name");
									}
								}
							}
						}
					} else if ("1001".equals(resp_code)) {
						return this.map.get(pkgId).toString();
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e, e);
		}

		return "集团V网分组产品";
	}
	
	private String getGroupName(String accessId, ServiceConfig config2,
			List<RequestParameter> params) {
		String reqXml = "";
		String rspXml = "";
		List<GommonBusiness> gbList = new ArrayList<GommonBusiness>();
		String bossTempate = "cc_cqryvnetinfo_461";
		try {
			
			//组装报文
			reqXml = mergeReqXML2Boss(params,bossTempate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,
						bossTempate);
				if (!StringUtil.isNull(rspXml))
				{
					String resp_code = XMLUtil.getChildText(rspXml, "response","resp_code");
					if (!BOSS_SUCCESS.equals(resp_code)) {
						return null;
					} else {
						GommonBusiness gb = null;
						List gsubs = XMLUtil.getChildList(rspXml,"content","gsubmember_customer_id");
						if (!CommonUtil.isNull(gsubs)) {
							for (int i = 0; i < gsubs.size(); i++) {
								gb = new GommonBusiness();
								Element gsubDT = XMLUtil.getChild((Element)gsubs.get(i), "cgsubmemberdt");
								if (null != gsubDT) {
									setCommonBusi(gbList, gb, gsubDT);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null !=gbList && gbList.size() > 0)
		{
			return gbList.get(0).getReserve1(); 
		}
		return null;
	}

	private void setCommonBusi(List<GommonBusiness> gbList, GommonBusiness gb,
			Element gsubDT) {
		String pkgEndDate;
		String pkgStartDate;
		String pkgId;
		gb.setReserve1(XMLUtil.getChildText(gsubDT,"groupcustomerpre_customer_name"));
		gb.setReserve2(XMLUtil.getChildText(gsubDT,"productbusinesspackage_package_name"));
		gb.setBeginDate(XMLUtil.getChildText(gsubDT,"gsubmember_use_date"));
		gb.setEndDate(XMLUtil.getChildText(gsubDT,"gsubmember_end_date"));
		pkgId = XMLUtil.getChildText(gsubDT,"productbusinesspackage_package_code");
		pkgEndDate = Long.toString(DateTimeUtil.currentDateMillis(gb.getEndDate()));
		pkgStartDate = Long.toString(DateTimeUtil.currentDateMillis(gb.getBeginDate()));
		if ("1110000026".equals(pkgId))
			if (pkgEndDate.compareTo(DateTimeUtil.getTodayChar14()) > 0
					&& pkgStartDate.compareTo(DateTimeUtil.getTodayChar14()) < 0) {
				gbList.add(gb);
			} else if (pkgEndDate.equals("0")
					&& pkgStartDate.compareTo(DateTimeUtil.getTodayChar14()) < 0) {
				gbList.add(gb);
			} else if (pkgEndDate.compareTo(DateTimeUtil.getTodayChar14()) > 0
					&& pkgStartDate.compareTo(DateTimeUtil.getTodayChar14()) > 0) {
				gbList.add(gb);
			} else if (pkgEndDate.equals("0")
					&& pkgStartDate.compareTo(DateTimeUtil.getTodayChar14()) > 0) {
				gbList.add(gb);
			}
	}
}