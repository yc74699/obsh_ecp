package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.xwtech.xwecp.service.logic.pojo.DEL040003Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class ChgFN4SMSInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(ChgFN4SMSInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	
	public ChgFN4SMSInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();

	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL040003Result res = new DEL040003Result();
		res.setResultCode("0");
		res.setErrorMessage("");
		String reqXml = "";
		String rspXml = "";
		String optType = getParameters(params,"oprType").toString();
		try
		{
			//短信邀请成员加入亲情号码组
			if("4".equals(optType))
			{
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_csmsjionmsn_69", params);
				logger.debug(" ====== 亲情号码邀请 发送报文 ====== \n" + reqXml);
				if (null != reqXml && !"".equals(reqXml)) {
						rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_csmsjionmsn_69", super.generateCity(params)));
					logger.debug(" ====== 亲情号码邀请 接收报文 ====== \n" + rspXml);
				}
				if (null != rspXml && !"".equals(rspXml)) {
					Element root = this.config.getElement(rspXml);
					String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
					res.setResultCode("0000".equals(resp_code) ? "0" : "1");
					if (!"0000".equals(resp_code)) {
						ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040003", "cc_csmsjionmsn_69", resp_code);
						res.setErrorCode(resp_code);
						res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"), "resp_desc"));
						if (null != errDt) {
							res.setErrorCode(errDt.getLiErrCode());
							res.setErrorMessage(errDt.getLiErrMsg());
						}
					}
				}
			}
			//成员退订亲情号码组
			else if("5".equals(optType))
			{
				res = menQuitFN(accessId,config,params,res);
			}
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		
		return res;
	}

	private DEL040003Result menQuitFN(String accessId, ServiceConfig config,
			List<RequestParameter> params, DEL040003Result res) 
	{
		String packageType = "";
		String packageCode = "";
		List<PkgDetail> groupList = this.getRelationGroupPckInfo(accessId, config, params, res);
		
		// 套餐类型编码
		if (null != groupList && groupList.size() > 0)
		{
			for (PkgDetail packDt : groupList) 
			{
				packageType = packDt.getPackageType();
				packageCode = packDt.getPkgType();
			}
		}
		else
		{
			return res;
		}
		addParameter("packageType",packageType,params);
		addParameter("packageCode",packageCode,params);
		
		FNProcess4exit(accessId, config, params, res);
		
		return res;
	}
	private void FNProcess4exit(String accessId, ServiceConfig config,
			List<RequestParameter> params, DEL040003Result res) 
	{
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cmodifamimem_441", params);
			logger.debug(" ====== 短厅亲情号码退订处理 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cmodifamimem_441", super.generateCity(params)));
				logger.debug(" ====== 短厅亲情号码退订处理 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040031", "cc_cmodifamimem_441", resp_code);
					res.setErrorCode(this.config.getChildText(this.config.getElement(root, "response"), "resp_code"));
					res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"), "resp_desc"));
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	private void addParameter(String paraName,String paraValue, List<RequestParameter> params)
	{
		RequestParameter para = new RequestParameter();
		para.setParameterName(paraName);
		para.setParameterValue(paraValue);
		params.add(para);
	}

	private List getRelationGroupPckInfo(String accessId, ServiceConfig config, List<RequestParameter> params, DEL040003Result res) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		PkgDetail dt = null;
		List<PkgDetail> list = null;
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
					list = new ArrayList<PkgDetail>();
					for (int i = 0; i < userList.size(); i++) {
						Element packDt = this.config.getElement((Element) userList.get(i), "cplanpackagedt");
						if (null != packDt) {
							if ("1035".equals(this.config.getChildText(packDt, "package_type"))) {
								String package_code = this.config.getChildText(packDt, "package_code");
								if ("4778".equals(package_code) || "4779".equals(package_code) || "4907".equals(package_code)) {
									if (pkgSet.contains(this.config.getChildText(packDt, "package_code")))
										continue;
									dt = new PkgDetail();
									dt.setPackageType(this.config.getChildText(packDt, "package_type"));
									dt.setPkgType(this.config.getChildText(packDt, "package_code"));
									dt.setBeginDate(this.config.dateToString(this.config
											.stringToDate(this.config.getChildText(packDt, "package_use_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
									dt.setEndDate(this.config.dateToString(this.config
											.stringToDate(this.config.getChildText(packDt, "package_end_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
									dt.setPkgState(this.config.getChildText(packDt, "package_state"));

									list.add(dt);
									pkgSet.add(dt.getPkgType());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		if(null == list || list.size() <= 0)
		{
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_ERROR);
			res.setErrorMessage("用户未加入任何亲情号码组!");
		}
		return list;
	}
}
