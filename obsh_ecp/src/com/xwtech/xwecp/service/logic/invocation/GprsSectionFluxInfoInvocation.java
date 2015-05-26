package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040065Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.DateTimeUtil;

public class GprsSectionFluxInfoInvocation extends BaseInvocation implements ILogicalService{
	private static final Logger logger = Logger.getLogger(GprsSectionFluxInfoInvocation.class);
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY040065Result res = new QRY040065Result();
	    res = qryGprsSectionUsedFlux(accessId,config,params,res);
		return res;
	}
	/**
	 * 根据网络统计流量使用量
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private QRY040065Result qryGprsSectionUsedFlux(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040065Result res){
		int TwoNetFlux = 0;
		int ThreeNetFlux = 0;
		int FlourNetFlux = 0;
		List<Node> sectionList = qryGprsSectionFluxInfo(accessId,config,params,res); 
		if(null != sectionList && sectionList.size() > 0){
			for(int i = 0;i < sectionList.size();i++){
				Node sectionNode = sectionList.get(i);
				String netType = sectionNode.selectSingleNode("gprs_net_type").getText().trim();
				int tempTotal = Integer.parseInt(sectionNode.selectSingleNode("gprs_flux").getText().trim());
				if(!"".equals(netType) && "2".equals(netType)){
					TwoNetFlux = TwoNetFlux + tempTotal;
				}
				if(!"".equals(netType) && "3".equals(netType)){
					ThreeNetFlux = ThreeNetFlux + tempTotal;
				}
				if(!"".equals(netType) && "4".equals(netType)){
					FlourNetFlux = FlourNetFlux + tempTotal;
				}
			}
		}
		res.setTwoNetFlux(String.valueOf(TwoNetFlux));
		res.setThreeNetFlux(String.valueOf(ThreeNetFlux));
		res.setFourNetFlux(String.valueOf(FlourNetFlux));
		return res;
	}
	
	/**
	 * 查询用户日流量使用情况
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> qryGprsSectionFluxInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040065Result res)
	{
		//若传的年月份是当月的去当前天数，不是则取每个月的31天
		String cycle =(String)getParameters(params,"cycle");
		String dateNow = DateTimeUtil.getTodayChar6();
		if(!"".equals(cycle) && cycle.equals(dateNow)){
			setParameter(params, "daynumber",DateTimeUtil.getTodayDay());
		}else{
			setParameter(params, "daynumber","31");
		}
		

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsdayflux_70", params);
			logger.debug(" ====== 查询用户日流量使用情况 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_4gqrygprsdayflux_70", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_list");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						return freeItemNodes;
					}
				}
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
	private Element checkReturnXml(String rspXml,QRY040065Result res)
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
