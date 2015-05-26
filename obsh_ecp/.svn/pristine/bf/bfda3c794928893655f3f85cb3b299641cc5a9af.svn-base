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
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.invocation.UserPackageCodeInvocation.ProductBusinessPackageBean;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY010030Result;
import com.xwtech.xwecp.service.logic.pojo.QryDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 小额话费支付查询
 * @author xufan
 * 2014-05-08
 */
public class QryPayQueryInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QryPayQueryInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public QryPayQueryInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY010030Result res = new QRY010030Result();

		try
		{
			Map<String,String> mapResult=getSmallPayQryInfo(accessId, config, params,res);
			List<QryDetail> qryDetail=qryDetail(accessId, config, params,res);
			res.setQryDetail(qryDetail);
			res.setMapResult(mapResult);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		
		return res;
	}
	
	
	/**
	 * 查询小额话费支付
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public Map<String,String> getSmallPayQryInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010030Result res)
	{
		Map<String ,String> mapResult=new HashMap<String,String>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					new StringTeletext(
							this.bossTeletextUtil.mergeTeletext("cc_smallpayqrybalance_1", params), 
							accessId, "cc_smallpayqrybalance_1", city));
			logger.debug(" ====== 查询小额话费支付返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010030", "cc_smallpayqrybalance_1", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try

					{
					String	invalid_date =this.config.getChildText(this.config.getElement(root, "content"), "invalid_date");
					String	balance =this.config.getChildText(this.config.getElement(root, "content"), "balance");
					String	payfee =this.config.getChildText(this.config.getElement(root, "content"), "payfee");
					String	limitfee =this.config.getChildText(this.config.getElement(root, "content"), "limitfee");
					String	usefee =this.config.getChildText(this.config.getElement(root, "content"), "usefee");
					mapResult.put("invalid_date", invalid_date);
					mapResult.put("balance", StringUtil.mobileFront(balance));
					mapResult.put("payfee", StringUtil.mobileFront(payfee));
					mapResult.put("limitfee", StringUtil.mobileFront(limitfee));
					mapResult.put("usefee", StringUtil.mobileFront(usefee));
					
					}
					catch (Exception ex)
					{
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return mapResult;
	}
	
	public List<QryDetail> qryDetail(String accessId, ServiceConfig config, List<RequestParameter> params, QRY010030Result res){
		List<QryDetail> qrySmallList=new ArrayList<QryDetail>();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<ProductBusinessPackageBean> pkgCfgList = null;
		try {
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_smallpayqrydetail_1", params);

			logger.debug(" ====== 查询套餐配置请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_smallpayqrydetail_1", super.generateCity(params)));
			logger.debug(" ====== 查询套餐配置返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY010030", "cc_smallpayqrydetail_1", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List qrySmallListCode = null;
					Element content = this.config.getElement(root, "content");
					QryDetail qryDetail =null;
					try {
						qrySmallListCode =content.getChildren("business_info");
						for (int i = 0; i < qrySmallListCode.size(); i++) {
							qryDetail=new QryDetail();
							String business_id= this.getChildText((Element) qrySmallListCode.get(i), "business_id");
							String crm_businessid= this.getChildText((Element) qrySmallListCode.get(i), "crm_businessid");
							String pantform_date= this.getChildText((Element) qrySmallListCode.get(i), "pantform_date");
							String status_date= this.getChildText((Element) qrySmallListCode.get(i), "status_date");
							String payfee= this.getChildText((Element) qrySmallListCode.get(i), "payfee");
							String status= this.getChildText((Element) qrySmallListCode.get(i), "status");
							String error_code= this.getChildText((Element) qrySmallListCode.get(i), "error_code");
							String error_desc= this.getChildText((Element) qrySmallListCode.get(i), "error_desc");
							String merchant_info= this.getChildText((Element) qrySmallListCode.get(i), "merchant_info");
							String queryType= this.getChildText((Element) qrySmallListCode.get(i), "querytype");
							qryDetail.setBusiness_id(business_id);
							qryDetail.setCrm_businessid(crm_businessid);
							qryDetail.setError_code(error_code);
							qryDetail.setError_desc(error_desc);
							qryDetail.setMerchant_info(merchant_info);
							qryDetail.setPantform_date(pantform_date);
							qryDetail.setPantform_date(pantform_date);
							qryDetail.setPayfee(StringUtil.mobileFront(payfee));
							qryDetail.setQueryType(queryType);
							qryDetail.setStatus(status);
							qryDetail.setStatus_date(status_date);
							qrySmallList.add(qryDetail);
						}
					} catch (Exception e) {
						qrySmallList = null;
					}
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return qrySmallList;
	}
	/**
	 * 获取子节点信息
	 * 
	 * @param e
	 * @param childName
	 * @return
	 */
	public String getChildText(Element e, String childName) {
		String str = "";

		try {
			str = e.getChildText(childName) == null ? "" : e.getChildText(childName).trim();
		} catch (Exception ex) {
			// logger.error(ex, ex);
			str = "";
		}

		return str;
	}

}