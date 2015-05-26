package com.xwtech.xwecp.service.logic.invocation;

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
import com.xwtech.xwecp.service.logic.pojo.DEL020001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.StringUtil;

public class ScoreExchangeInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryUserEPackageInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	private ParseXmlConfig config;

	public ScoreExchangeInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {

		DEL020001Result res = new DEL020001Result();
		// biztNo 1:积分兑换话费接口 2:M值兑换话费接口
		String biztNo = "";
		String bossInput = "";
		try {
			for (int j = 0; j < params.size(); j++) {
				RequestParameter pTmp = params.get(j);
				if ("biztNo".equals(pTmp.getParameterName())) {
					biztNo = (String) pTmp.getParameterValue();

					if (biztNo.equals("1")) {
						bossInput = "cc_cscoreredeem_559";
					} else if (biztNo.equals("2")) {
						bossInput = "cc_czonem_576";
					}
					break;
				}
			} 
			res = mChangeFee(accessId, params, bossInput);
		} catch (Exception e) {
			res.setResultCode("1");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * M值/积分兑换话费
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public DEL020001Result mChangeFee(String accessId, List<RequestParameter> params, String bossInput) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String resp_desc = "";
		ErrorMapping errDt = null;
		DEL020001Result res = new DEL020001Result();
		try {
			//应市场部门要求：M值兑换话费暂时不做修改，保持原有的比例。积分兑换标准还是：全球通积分扣减配置项调整为：原扣减积分*(0.035/0.015)，小数部分向下取整。
			if(!StringUtil.isNull(bossInput)&& "cc_cscoreredeem_559".equals(bossInput)){
				int  parmScoreNum= Integer.valueOf((String)getParameters(params, "reserve1"));
				//传入报文的参数reserve1
				String reserve1 = String.valueOf((int) Math.floor(parmScoreNum*7/3));
				params.add(new RequestParameter("reserve1",reserve1));
				
			}else if (!StringUtil.isNull(bossInput) && "cc_czonem_576".equals(bossInput)){ //M值暂不做调整
				
				
			}
			reqXml = this.bossTeletextUtil.mergeTeletext(bossInput, params);
			logger.debug(" ====== 积分/M值兑换话费 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossInput, super.generateCity(params)));
				logger.debug(" ====== 积分/M值兑换话费 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("DEL020001", bossInput, resp_code);
				if (null != errDt) {
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}
				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				//成功开始校验
				if (null != resp_code && (BOSS_SUCCESS.equals(resp_code))) {
					Element content = root.getChild("content");
					String operating_srl = p.matcher(content.getChildText("operating_srl")).replaceAll("");
					res.setOperating_srl(operating_srl);
				}
			}
		} catch (Exception e) {
			res.setResultCode("1");
			logger.error(e, e);
		}
		return res;
	}

}
