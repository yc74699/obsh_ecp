package com.xwtech.xwecp.service.logic.invocation;

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
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY200002Result;
import com.xwtech.xwecp.service.logic.pojo.OutMemberInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import java.util.ArrayList;
import java.util.regex.Pattern;
/**
 * 新版家庭成员管理
 * 
 * @author wanghg
 * 
 */
public class ManageFamilyMemInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(ManageFamilyMemInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");


	public ManageFamilyMemInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	
		


	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY200002Result res = new QRY200002Result();
		String oprType = null;
		String bossTemplate_01 = "cc_cngmanagefamilymem_verify";
		String bossTemplate_02 = "cc_cngmanagefamilymem";
		String reqXml = "";
		String rspXml = "";
		List member_info = null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			oprType = (String)this.getParameters(params, "oprType");
			//校验
			if("1".equals(oprType)){
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_01,
						params);
				logger.info("reqXml="+ reqXml);
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, bossTemplate_01, this
								.generateCity(params)));
				logger.info(rspXml);
				if(null != rspXml && !"".equals(rspXml)){
					
					Element root = this.getElement(rspXml.getBytes());
					String resp_code= root.getChild("response").getChildText("resp_code");
					if(resp_code.equals("0000")){
						res.setResultCode("0");	
					List<OutMemberInfo> outMemberList = new ArrayList<OutMemberInfo>();
					OutMemberInfo outMember = null;
					member_info = root.getChild("content").getChildren("member_info");
					if(null != member_info && member_info.size() > 0){
						for(int i=0;i<member_info.size();i++){
							Element memberInfo = ((Element)member_info.get(i));

							outMember = new OutMemberInfo();
							outMember.setMemretcode(p.matcher(memberInfo.getChildText("memretcode")).replaceAll(""));
							outMember.setMemretmsg(p.matcher(memberInfo.getChildText("memretmsg")).replaceAll(""));
							outMember.setMemsubsid(p.matcher(memberInfo.getChildText("servnumber")).replaceAll(""));
							outMemberList.add(outMember);
						}
						res.setOutMemberInfo(outMemberList);
					}
				}else
				{
					res.setResultCode("1");
					res.setErrorCode(resp_code);
					res.setErrorMessage(root.getChild("response").getChildText("resp_desc"));
				}
				

			}}
			else
			{
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_02,
						params);
				logger.info("reqXml="+ reqXml);

				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, bossTemplate_02, this.generateCity(params)));
				logger.info("rspXml="+ rspXml);
				if(null != rspXml && !"".equals(rspXml)){
					Element root = this.getElement(rspXml.getBytes());
					String resp_code= root.getChild("response").getChildText("resp_code");
					String resp_desc= root.getChild("response").getChildText("resp_desc");
					res.setResultCode(BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS
							: LOGIC_ERROR);
					if(!"0000".equals(resp_code)){
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
				}
				
				
				}}

			
				
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
}