package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import com.xwtech.xwecp.service.logic.pojo.ABCOrderInfoBean;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050058Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 农行直连充值订单查询
 * 
 * @author 杨可帆
 *
 */
public class QueryOrderInfoABCInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryOrderInfoABCInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public QueryOrderInfoABCInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050058Result res = new QRY050058Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			//查询订单信息
			BaseResult orderlistRet = this.checkPackageConflict(accessId, config, params);
			if (LOGIC_SUCESS.equals(orderlistRet.getResultCode())) {
				Map orderlistMap = (Map) orderlistRet.getReObj();
				if (null != orderlistMap && !orderlistMap.isEmpty()) {
					String retCode = (String) orderlistMap.get("retCode");
					String errMsg = (String) orderlistMap.get("errMsg");
					res.setErrMsg(errMsg);
					res.setRetCode(retCode);
					if(null != orderlistMap.get("pkgList")){
						List<ABCOrderInfoBean> orderlist = (List<ABCOrderInfoBean>) orderlistMap.get("pkgList");
						res.setOrderlist(orderlist);
					}
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(orderlistRet.getErrorCode());
				res.setErrorMessage(orderlistRet.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询订单信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult checkPackageConflict(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		Map objMap = new HashMap();
		List<ABCOrderInfoBean> pkgList = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_QryOrderInfo_abc_360", params);

			logger.debug(" ====== 农行直连充值订单查询 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_QryOrderInfo_abc_360", this.generateCity(params)));
			logger.debug(" ====== 农行直连充值订单查询 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050058", "cc_QryOrderInfo_abc_360", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List orderlist = null;
					String retCode = null;
					String errMsg = null;
					try
					{
						orderlist = root.getChild("content").getChildren("orderlist");
						if(null != root.getChild("content")){
							retCode = root.getChild("content").getChildText("ret_code");
							errMsg = root.getChild("content").getChildText("ret_msg");
						}
					}
					catch (Exception e)
					{
						orderlist = null;
						retCode = null;
						errMsg = null;
					}
					if (null != orderlist && orderlist.size() > 0) {
						pkgList = new ArrayList<ABCOrderInfoBean>(orderlist.size());
						ABCOrderInfoBean bean = null;
						for (int i = 0; i < orderlist.size(); i++)
						{
							Element orderlist_element = (Element)orderlist.get(i);
							if (null != orderlist_element)
							{
								bean = new ABCOrderInfoBean();
								bean.setOrderid(p.matcher(orderlist_element.getChildText("orderid")).replaceAll(""));
			                    bean.setServnumber(p.matcher(orderlist_element.getChildText("servnumber")).replaceAll(""));
			                    bean.setExchangeamt(p.matcher(orderlist_element.getChildText("exchangeamt")).replaceAll(""));
			                    bean.setBankstatus(p.matcher(orderlist_element.getChildText("bankstatus")).replaceAll(""));
			                    bean.setIsadjustatus(p.matcher(orderlist_element.getChildText("isadjustatus")).replaceAll(""));
			                    bean.setChargestatus(p.matcher(orderlist_element.getChildText("chargestatus")).replaceAll(""));
			                    pkgList.add(bean);
							}
						}
						objMap.put("pkgList", pkgList);
					}
					objMap.put("retCode", retCode);
					objMap.put("errMsg", errMsg);
					res.setReObj(objMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
}