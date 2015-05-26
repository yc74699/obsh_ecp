package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 集团V网业务办理前的套餐编码校验 DEL010001
 * @author taogao
 * 2014-04-02
 */
public class CheckJTVWCodeInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryPkgUseStateInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	private String bossService = "cc_caddvnetmemout_463";
	
	public CheckJTVWCodeInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL010001Result res = new DEL010001Result ();
		String id = (String) getParameters(params,"id");
		
		if("JTVW_ADD".equals(id))
		{
			String code = (String) getParameters(params,"reserve2");
			
			if(checkCode(code))
			{
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(LOGIC_ERROR);
				res.setErrorMessage("该业务因为权限问题无法在电子渠道办理，详细信息请拨打10086咨询");
				return res;
			}
		}
		getResponeMssage(accessId,params,res);
		
		return res;
	}
	/**
	 * 正常办理业务
	 * @param accessId
	 * @param params
	 * @param res
	 */
	private void getResponeMssage(String accessId, List<RequestParameter> params,DEL010001Result res)
	{
		String rspXml = "";
		Element root = null;
		String reqXml = this.bossTeletextUtil.mergeTeletext(bossService, params);
		logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			//发送报文
			try {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossService, super.generateCity(params)));
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
				
				if (null != rspXml && !"".equals(rspXml)) 
				{
					root = this.config.getElement(rspXml);
					
					String resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					
					if (LOGIC_SUCESS.equals(resultCode))
					{
					   String errCode = this.config.getChildText(this.config.getElement(root, "content"), "error_code");
					}
					
					res.setResultCode(resultCode);
					res.setErrorCode(resp_code);
					res.setErrorMessage(resp_desc);
				}
			} catch (CommunicateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	/**
	 * 判断该集团V网是否在黑名单中
	 * @param code
	 * @return
	 */
	private boolean checkCode(String code)
	{
		List<String> codeList = this.wellFormedDAO.getJTVWCodeSet();
		if(codeList.contains(code))
		{
			return true;
		}
		return false;
	}
}
