package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.INotDisplayPackageCode;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.EPkgDetail;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040014Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryUserEPackageInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryUserEPackageInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryUserEPackageInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {

		QRY040014Result res = new QRY040014Result();

		List<EPkgDetail> ePkgDetailList = null;

		EPkgDetail ePkgDetail = null;

		try {
			// 随E行套餐使用情况
			ePkgDetailList = queryPersonalPackage(accessId, params, res);

			RequestParameter p = new RequestParameter("idType", "0");
			params.add(p);

			RequestParameter p1 = new RequestParameter("date", DateTimeUtil.getTodayChar6());
			params.add(p1);
			
			//接口 ac_agetgprsflux 用当前时间减去28小时，得到的时间中取出日期传入
			 p1 = new RequestParameter("dayNumber",DateTimeUtil.getDayBfHour(DateTimeUtil.getTodayChar14(),-28));
			params.add(p1);

			if (ePkgDetailList != null && ePkgDetailList.size() > 0) {
				//过滤显示的套餐列表
				
					Iterator iterInfo = ePkgDetailList.iterator();
					while(iterInfo.hasNext()) {
						EPkgDetail bean = (EPkgDetail)iterInfo.next();
						if(isNotDispPackage(bean.getPkgCode()))
						{
							iterInfo.remove();
						}
					}
				
				
				for (int i = 0; i < ePkgDetailList.size(); i++) {
					//if(!isDispPackage(ePkgDetailList.get(i).getPkgCode())){
						int idType = 0;
						ePkgDetail = ePkgDetailList.get(i);
						if (isTimePackage(ePkgDetail.getPkgCode())) {
							idType = 1;
							this.setParameter(params, "idType", "1");
						} else {
							idType = 2;
							this.setParameter(params, "idType", "0");
						}
	
						// 使用情况(已经使用的流量/时长)
						String gprsInfo = queryGPRSDetail(accessId, params, res);
						String gprsInfoResult = "0";
						double d = Double.parseDouble((String) gprsInfo);
	
						// 换算时长
						if (idType == 1) {
							gprsInfoResult = String.valueOf(Math.round(d));
						}
						// 换算流量
						if (idType == 2) {
							java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
							gprsInfoResult = df.format(d / 1024 / 1024);
						}
						
						ePkgDetail.setFlag(String.valueOf(idType));
	
						ePkgDetail.setUse(gprsInfoResult);
					
				}
			}
			res.setEPkgDetail(ePkgDetailList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 查询随E行套餐查询信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<EPkgDetail> queryPersonalPackage(String accessId, List<RequestParameter> params, QRY040014Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<EPkgDetail> ePkgDetailList = null;
		EPkgDetail ePkgDetail = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", super.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					res.setErrorCode(root.getChild("response").getChildText("resp_code"));
					res.setErrorMessage(root.getChild("response").getChildText("resp_desc"));
					errDt = this.wellFormedDAO.transBossErrCode("QRY040014", "cc_find_package_62_TC", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			else
			{
				setErrorResult(res);
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List packageList = this.config.getContentList(root, "package_code");
				if (null != packageList && packageList.size() > 0) {
					ePkgDetailList = new ArrayList<EPkgDetail>(packageList.size());
					for (int i = 0; i < packageList.size(); i++) {
						ePkgDetail = new EPkgDetail();
						Element ePkgDetailInfo = this.config.getElement((Element) packageList.get(i), "cplanpackagedt");
						ePkgDetail.setPkgName(this.config.getChildText(ePkgDetailInfo, "package_name"));
						ePkgDetail.setPkgCode(this.config.getChildText(ePkgDetailInfo, "package_code"));
						ePkgDetail.setBeginDate(this.config.getChildText(ePkgDetailInfo, "package_use_date"));
						ePkgDetail.setEndDate(this.config.getChildText(ePkgDetailInfo, "package_end_date"));
//						String packageState = this.config.getChildText(ePkgDetailInfo, "package_state").equals("0") ? "1" : "2";
//						ePkgDetail.setFlag(packageState);
						ePkgDetailList.add(ePkgDetail);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return ePkgDetailList;
	}

	/**
	 * GPRS流量查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public String queryGPRSDetail(String accessId, List<RequestParameter> params, QRY040014Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String GPRSInfo = "";
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetgprsflux_717", params);
			logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetgprsflux_717", super.generateCity(params)));
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					res.setErrorCode(root.getChild("response").getChildText("resp_code"));
					res.setErrorMessage(root.getChild("response").getChildText("resp_desc"));
					errDt = this.wellFormedDAO.transBossErrCode("QRY040014", "ac_agetgprsflux_717", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			else
			{
				setErrorResult(res);
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				GPRSInfo = this.config.getChildText(this.config.getElement(root, "content"), "gprsbill_total_fee");
			}
		} catch (Exception e) {
			logger.error(e, e); 
		}
		return GPRSInfo;
	}
	
	private static final String[] TIME_PACKAGE_CODES = {"1270","1271","1448","1449","1558","1559","1560","1561","1539","1540","1538","1541","1270","1271",
		"4151","1401","1402","1403","4839","4840","4808","4809","1629","1630","1631","1632","1633","1634","1755","1756","1757","1758","1765","1766","2024","2040",
		"2043","2046","2049","1049","1068","1065","1686","1687","1688","1689","1699" ,"2097","2098","2099","2100","4007","1049",
		"1065","1068","1401","1402","1403","1629","1630","1631","1632","1633","1634","1686","1687","1688",
		"1689",
		"1699",
		"2024",
		"2040",
		"2043",
		"2046",
		"2049",
		"2097",
		"2098",
		"2099",
		"2100",
		"4007",
		"4151",
		"4808",
		"4809",
		"4839",
		"4840" ,
		"2000001270",
		"2000001271",
		"2000001448",
		"2000001449",
		"2000001538",
		"2000001539",
		"2000001540",
		"2000001541",
		"2000001600",
		"2000001601",
		"2000001602",
		"2000001603",
		"2000001615",
		"2000001616",
		"2000001617",
		"2000001620",
		"2000001621",
		"2000001622",
		"2000002024",
		"2000002040",
		"2000002043",
		"2000002046",
		"2000002049",
		"2000002097"};
	
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
	public static void main(String[] aa)
	{
		boolean result = false;
		if(TIME_PACKAGE_CODES != null && TIME_PACKAGE_CODES.length > 0 ){
			for(int i = 0; i< TIME_PACKAGE_CODES.length; i++){
				if(TIME_PACKAGE_CODES[i].equals("1540")){
					result = true;
				}
			}
		}
		System.out.println(result);
	}
	
	private boolean isNotDispPackage(String pckCode){
		boolean result = false;
		String[] arr = INotDisplayPackageCode.NOT_DISP_PCK_CODES;
		
		if(arr != null && arr.length > 0 ){
			for(int i = 0; i< arr.length; i++){
				if(arr[i].equals(pckCode)){
					result = true;
					break;
				}
			}
		}
		return result;
	}

}
