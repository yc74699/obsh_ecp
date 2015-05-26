package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.GprsPkg;
import com.xwtech.xwecp.service.logic.pojo.QRY040057Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;

public class QueryGPRSPkgInvocation extends BaseInvocation implements ILogicalService{
	
	private static final Logger logger = Logger.getLogger(QueryGPRSPkgInvocation.class);
	
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY040057Result res = new QRY040057Result();
		
		if(queryGPRSPkgFlux(accessId,config,params,res))
		{
			querGPRSPkgFluxInfo(accessId,config,params,res);
		}
		return res;
	}
	
	/**
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private boolean queryGPRSPkgFlux(String accessId,
			ServiceConfig config, List<RequestParameter> params,QRY040057Result res)
	{
		
		Element root = sendXml("ac_querygprspkgflux_69",params,accessId,res);
		if(null != root)
		{
			String totalFlux = root.getChild("content").getChild("gprsnode").getChildText("gprs_total_flux");
			String usedFlux = root.getChild("content").getChild("gprsnode").getChildText("gprs_used_flux");
			String type = root.getChild("content").getChild("gprsnode").getChildText("gprs_pkg_type");
			res.setTotalFlux(totalFlux);
			res.setUsedFlux(usedFlux);
			res.setPkgType(type);
		}
		else
		{
			return false;
		}
		return true;
	}
	
	private void querGPRSPkgFluxInfo(String accessId,
			ServiceConfig config, List<RequestParameter> params,QRY040057Result res)
	{
		Element root = sendXml("QUERYGPRSALLPKGFLUX",params,accessId,res);
		if(null != root)
		{
			Element content = root.getChild("content");
			List<Element> pkgList = content.getChildren("gprs_all_pkg_list");
			List<GprsPkg> gprsList = new ArrayList<GprsPkg>();
			if(null != pkgList && pkgList.size() > 0)
			{
				for(int i=0;i<pkgList.size();i++)
				{
					Element pkg = pkgList.get(i);
					
					GprsPkg gps = new GprsPkg();
					gps.setNum(pkg.getChildText("gprs_product_num"));
					gps.setId(pkg.getChildText("gprs_product_id"));
					gps.setTotalFlux(pkg.getChildText("gprs_max_value"));
					gps.setUsedFlux(pkg.getChildText("gprs_cumulate_value"));
					gps.setName(pkg.getChildText("gprs_product_name"));
					gps.setProductType(pkg.getChildText("gprs_product_type"));
					gps.setRateType(pkg.getChildText("gprs_rate_type"));
					gps.setUseType(pkg.getChildText("gprs_use_type"));
					
					gprsList.add(gps);
				}
				res.setGprsPkgList(gprsList);
			}
		}
	}
	
	/**
	 * 组装报文模板，并发送报文
	 * @param interfaceName
	 * @param params
	 * @param accessId
	 * @param res
	 * @return
	 */
	private Element sendXml(String interfaceName,List<RequestParameter> params,String accessId,QRY040057Result res)
	{
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext(interfaceName, params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, interfaceName, this.generateCity(params)));
				Element personalInfo = checkReturnXml(rspXml,res);
				return personalInfo;
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040057Result res)
	{
		Element root = this.getElement(rspXml.getBytes());
		String resp_code = root.getChild("response").getChildText("resp_code");
		String resp_desc = root.getChild("response").getChildText("resp_desc");
		
		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		if(null != rspXml && !"".equals(rspXml))
		{
			if(!LOGIC_SUCESS.equals(resultCode) || null == root)
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
			else
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				return root;
			}
		}
		return null;
	}
}
