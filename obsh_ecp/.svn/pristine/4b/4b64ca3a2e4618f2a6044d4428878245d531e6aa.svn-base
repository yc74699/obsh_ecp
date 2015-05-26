package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050041Result;
import com.xwtech.xwecp.service.logic.pojo.TransProductBean;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * @author 张成东
 * @mail   zhangcd@mail.xwtec.cn
 * 创建于：Jun 22, 2011
 * 描述：携号转网，获取用户可携转的主体产品列表
 */
public class GetUsrTransMainProductInvocation  extends BaseInvocation implements ILogicalService{

	private static final Logger logger = Logger.getLogger(GetUsrTransMainProductInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public GetUsrTransMainProductInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params) {
		QRY050041Result res = new QRY050041Result();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		ArrayList<TransProductBean> proList = null;
		
		try {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgettransuserprod", params);
				
				logger.debug(" ====== 查询用户携号转移办理可选择的携入地主体产品列表 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgettransuserprod", this.generateCity(params)));
				
				logger.debug(" ====== 查询用户携号转移办理可选择的携入地主体产品列表返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050041", "cc_cgettransuserprod", errCode);
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
						List webproductopencfg_product_id = null;
						try
						{
							webproductopencfg_product_id = root.getChild("content").getChildren("cusrproductcfgdt");
						}
						catch (Exception e)
						{
							webproductopencfg_product_id = null;
						}
						if (null != webproductopencfg_product_id && webproductopencfg_product_id.size() > 0) {
							proList = new ArrayList<TransProductBean>(webproductopencfg_product_id.size());
							TransProductBean bean = null;
							for (int i = 0; i < webproductopencfg_product_id.size(); i++)
							{
								bean = new TransProductBean();
								Element cwebproductopencfgdt = ((Element)webproductopencfg_product_id.get(i));
								if (null != cwebproductopencfgdt)
								{
									bean.setProId(p.matcher(cwebproductopencfgdt.getChildText("usrproductcfg_product_id")).replaceAll(""));
				    				bean.setProName(p.matcher(cwebproductopencfgdt.getChildText("usrproductcfg_product_name")).replaceAll(""));
				    				bean.setProDesc(p.matcher(cwebproductopencfgdt.getChildText("usrproductcfg_product_desc")).replaceAll(""));
				    				proList.add(bean);
								}
							}

							res.setTransProList(proList);
						}
					}
				}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

}
