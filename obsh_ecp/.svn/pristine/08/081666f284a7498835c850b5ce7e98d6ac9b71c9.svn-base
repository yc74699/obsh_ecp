package com.xwtech.xwecp.service.logic.invocation;


import java.util.ArrayList;
import java.util.List;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.ILogicalService;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.MonternetBean;
import com.xwtech.xwecp.service.logic.pojo.OwnBunsiBean;
import com.xwtech.xwecp.service.logic.pojo.QRY040055Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 套餐使用情况查询 QRY040055 
 * 
 * @author taogang 2013-4-02
 */
public class QueryOwnMonterBunsiInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryOwnMonterBunsiInvocation.class);
	/*CRM查询自由业务的接口*/
	private String OWNBUNSI = "cc_cgetspuserinfo_358";
	/*CRM查询梦网业务的接口*/
	private String MONTERBUNSI = "cc_cqryspuserreg_65";
	public QueryOwnMonterBunsiInvocation()
	{
		
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY040055Result res = new QRY040055Result();
		List<OwnBunsiBean>  ownBunsiList = queryOwnBunsiList(accessId,config,params,res);
		List<MonternetBean>  monternetList = queryMonternetList(accessId,config,params,res);
		
		if(null != monternetList && monternetList.size() > 0)
		{
			res.setMonternetList(monternetList);
		}
		if(null != ownBunsiList && ownBunsiList.size() > 0)
		{
			res.setOwnBunsiList(ownBunsiList);
		}

		return res;
	}
	
	/**
	 * 查询自有业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<OwnBunsiBean> queryOwnBunsiList(String accessId,
			ServiceConfig config, List<RequestParameter> params,QRY040055Result res)
	{
		String reqXml = getRequstXml(OWNBUNSI,params,accessId);
		Element root = checkReturnXml(reqXml,res);
		List<OwnBunsiBean>  ownBunsiList = new ArrayList<OwnBunsiBean>();
		
		if(null != root)
		{
			Document doc;
			try {
				doc = DocumentHelper.parseText(reqXml);
				List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/selfplatuserreg_reg_id/cselfplatuserregdt");
				if(null != freeItemNodes && freeItemNodes.size()>0)
				{
					for(Node freeItemNode : freeItemNodes)
					{
						OwnBunsiBean obb = new OwnBunsiBean();
						obb.setBizCode(freeItemNode.selectSingleNode("selfplatuserreg_biz_code").getText().trim());
						obb.setDomainCode(freeItemNode.selectSingleNode("selfplatuserreg_domain_code").getText().trim());
						obb.setPrdCode(freeItemNode.selectSingleNode("selfplatuserreg_prd_code").getText().trim());
						
						String startTime = DateTimeUtil.formatTime(
								freeItemNode.selectSingleNode("selfplatuserreg_begin_date").getText().trim());
						String endTime = freeItemNode.selectSingleNode("selfplatuserreg_end_date").getText();
						obb.setStartTime(startTime);
						if(null != endTime && "".equals(endTime))
						{
							obb.setEndTime(DateTimeUtil.formatTime(endTime.trim()));
						}

						obb.setState(getState(startTime,endTime));
						ownBunsiList.add(obb);
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return ownBunsiList;
	}
	/**
	 * 查询梦网业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<MonternetBean> queryMonternetList(String accessId,
			ServiceConfig config, List<RequestParameter> params,QRY040055Result res)
	{
		String reqXml = getRequstXml(MONTERBUNSI,params,accessId);
		Element root = checkReturnXml(reqXml,res);
		List<MonternetBean>  monternetList = new ArrayList<MonternetBean>();
		if(null != root)
		{
			Document doc;
			try {
				doc = DocumentHelper.parseText(reqXml);
				List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/spbizreg_gsm_user_id/cspbizregdt");
				if(null != freeItemNodes && freeItemNodes.size()>0)
				{
					for(Node freeItemNode : freeItemNodes)
					{
						MonternetBean mtb = new MonternetBean();
						mtb.setName(freeItemNode.selectSingleNode("spbizreg_busi_name").getText().trim());
						mtb.setVal(freeItemNode.selectSingleNode("spbizreg_sub_biz_val").getText().trim());
						mtb.setBizType(freeItemNode.selectSingleNode("spbizreg_biz_type").getText().trim());//spbizreg_sub_biz_type
						String startTime = DateTimeUtil.formatTime(
								freeItemNode.selectSingleNode("spbizreg_chg_time").getText().trim());
						String endTime = freeItemNode.selectSingleNode("spbizreg_end_time").getText();
						mtb.setStartTime(startTime);
						if(null != endTime && "".equals(endTime))
						{
							mtb.setEndTime(DateTimeUtil.formatTime(endTime.trim()));
						}
						mtb.setState(getState(startTime,endTime));
						monternetList.add(mtb);
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return monternetList;
	}
	/**
	 * 用于根据时间判断是否开通该业务 返回业务状态
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private String getState(String startTime,String endTime)
	{
		long nowTime = Long.parseLong(DateTimeUtil.getTodayChar14());
		long begTime = Long.parseLong(startTime);
		if(begTime > nowTime)
		{
			if(null == endTime || "".equals(endTime))
			{
				return "3";//3-预约开通
			}
			else
			{
				return "4";//4-预约关闭
			}
		}
		else
		{
			if(null == endTime || "".equals(endTime))
			{
				return "2";//2-已开通
			}
			else if(Long.parseLong(DateTimeUtil.formatTime(endTime.trim())) > nowTime)
			{
				return "4";//4-预约关闭
			}
			else
			{
				return "1";//1-未开通
			}
		}
	}
	/**
	 * 用于发送和接受报文
	 * @param crmInterface
	 * @param params
	 * @param accessId
	 * @return
	 */
	private String getRequstXml(String crmInterface,List<RequestParameter> params,String accessId)
	{
		String reqXml = this.bossTeletextUtil.mergeTeletext(crmInterface, params);
		String rspXml = "";
		if (null != reqXml && !"".equals(reqXml))
		{
			try {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, crmInterface, super.generateCity(params)));
			} catch (CommunicateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rspXml;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040055Result res)
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