package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.FluxDetail;
import com.xwtech.xwecp.service.logic.pojo.NumResource;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040065Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040072Result;
import com.xwtech.xwecp.service.logic.pojo.QRY050015Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;

/**
 *  在线入网 网上选号
 *  YangXQ 
 *  2015-1-8
 */
public class QueryGetNumResourceInvocation extends BaseInvocation implements ILogicalService
{

	private static final Logger logger = Logger.getLogger(QueryGetNumResourceInvocation.class);


	public QueryGetNumResourceInvocation()
	{
		
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		QRY050015Result res = new QRY050015Result();
		List<Node> lteList = qryGprsSectionFluxInfo(accessId,config,params,res);
		
		List<NumResource> listP = new ArrayList<NumResource>();
		if(null != lteList && lteList.size() > 0)
		{
			for(int i=0;i<lteList.size();i++)
			{
				Node lteNode = lteList.get(i);
				String mobile   = lteNode.selectSingleNode("msisdn_id").getText().trim();  
				String feeNeed  = lteNode.selectSingleNode("msisdn_choice_fee").getText().trim();	 
				String reserve1 = lteNode.selectSingleNode("msisdn_is_lock").getText().trim(); 
				NumResource numResource = new NumResource();
				numResource.setMobile(mobile);
				numResource.setFeeNeed(feeNeed);
				numResource.setReserve1(Integer.parseInt(reserve1));
				if(null !=reserve1 && Integer.parseInt(reserve1)!=1) {
					listP.add(numResource);
				}
			}
		}
		res.setNumResource(listP);
		return res;
	}
	
	/**
	 * 在线入网 网上选号
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> qryGprsSectionFluxInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY050015Result res)
	{
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetnumressite_565", params);
			logger.debug(" ====== 请求报文   ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote2(
						 new StringTeletext(reqXml, accessId, "cc_cgetnumressite_565", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/msisdn_id/cmsisdn_dt");
					
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
	private Element checkReturnXml(String rspXml,QRY050015Result res)
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
