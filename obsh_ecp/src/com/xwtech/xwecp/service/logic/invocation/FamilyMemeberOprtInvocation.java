package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.logic.pojo.DEL040027Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;


/**
 * 家庭帐户增删接口
 * @author xwtec
 *
 */
public class FamilyMemeberOprtInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(FamilyMemeberOprtInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	private Map map;
	
	public FamilyMemeberOprtInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		
		if (null == this.map)
		{
			this.map = new HashMap();
		}
	}

	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params) {
		DEL040027Result res = new DEL040027Result();
		try {
			String mainNum = ""; // 主卡号码
			String subNum = ""; // 副卡号码
			String memetype = "";// 类型
			boolean segBoolean = true; // 归属地
			if (null != params && params.size() > 0) {

				for (RequestParameter p : params) {
					if ("context_ddr_city".equals(p.getParameterName())) {
						mainNum = String.valueOf(p.getParameterValue());
					}
					if ("memberPhoneNum".equals(p.getParameterName())) {
						subNum = String.valueOf(p.getParameterValue());
					}

					if ("memeberType".equals(p.getParameterName())) {
						memetype = String.valueOf(p.getParameterValue());
					}

				}
			}

			// 1为手机用户，才进行校验
			if ("1".equals(memetype)) {
				// 主号归属地市
				String[] myStr = this.getNumSeg(accessId, config, params, res,
						mainNum);
				// 副号归属地市
				String[] subStr = this.getNumSeg(accessId, config, params, res,
						subNum);
				// 判断预约亲情号码是否和用户手机号省内异地
				if (null != myStr && null != subStr && myStr.length > 0
						&& subStr.length > 0) {
					if ((!myStr[0].equals(subStr[0]))
							|| (!myStr[1].equals(subStr[1]))) {
						segBoolean = false;
					}
				}
			}

			if (segBoolean) {
				String reqXml = ""; // 发送报文
				String rspXml = ""; // 接收报文
				String resp_code = ""; // 返回码
				// 组装发送报文
				reqXml = this.bossTeletextUtil.mergeTeletext(
						"cc_cupdhmember_185", params);
				logger.debug(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml)) {
					// 发送并接收报文
					rspXml = (String) this.remote
							.callRemote(new StringTeletext(reqXml, accessId,
									"cc_cupdhmember_185", this
											.generateCity(params)));
					logger.debug(" ====== 返回报文 ======\n" + rspXml);
				}
				// 解析BOSS报文
				if (null != rspXml && !"".equals(rspXml)) {
					// 解析报文 根节点
					Element root = this.config.getElement(rspXml);
					// 获取错误编码
					resp_code = this.config.getChildText(this.config
							.getElement(root, "response"), "resp_code");
					// 错误描述
					String resp_desc = this.config.getChildText(this.config
							.getElement(root, "response"), "resp_desc");
					// 设置结果信息
					this.getErrInfo(accessId, config, params, res, resp_code,
							resp_desc, "cc_cupdhmember_185");

				}
			}else  //归属地不一致,同一地市不能办理
			{
				res.setResultCode("1");  //失败
				res.setErrorCode("-12383");  //错误编码
				res.setErrorMessage("新增用户与户主不是同一归属地，不能加入！");  //描述信息
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}
	
	/**
	 * 归属地信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	public String[] getNumSeg (String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, DEL040027Result res, 
			                 String phoneNum)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		String[] str = {"", ""};  //省，地市
		boolean phoneBoolean = true;  //参数标识
		RequestParameter par = null;
		
		try
		{
			//新增参数
			if (null != params && params.size() > 0)
			{
				for (RequestParameter p : params)
				{
					if ("phoneNum".equals(p.getParameterName()))
					{
						phoneBoolean = false;
						p.setParameterValue(phoneNum);
					}
				}
			}
			//修改参数
			if (phoneBoolean)
			{
				par = new RequestParameter ();
				par.setParameterName("phoneNum");
				par.setParameterValue(phoneNum);
				params.add(par);
			}
			
			//组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetnumseg_551", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetnumseg_551", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}
			//解析BOSS报文
			if (null != rspXml && !"".equals(rspXml))
			{
				//解析报文 根节点
				Element root = this.config.getElement(rspXml);
				//获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				//错误描述
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				//设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cgetnumseg_551");
				//成功
				if (null != resp_code && "0000".equals(resp_code))
				{
					str [0] = this.config.getChildText(root, "nhnumbersegment_name");
					str [1] = this.config.getChildText(root, "nhnumbersegment_city_name");
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return str;
	}
	/**
	 * 设置结果信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo (String accessId, ServiceConfig config, 
                            List<RequestParameter> params, DEL040027Result res, 
                            String resp_code, String resp_desc, String xmlName)
	{
		ErrorMapping errDt = null;  //错误编码解析
		
		try
		{
			//失败
			if (!"0000".equals(resp_code))
			{
				//解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("DEL040017", xmlName, resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();  //设置错误编码
					resp_desc = errDt.getLiErrMsg();  //设置错误信息
				}
			}
			//设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code)?"0":"1");
			res.setErrorCode(resp_code);  //设置错误编码
			res.setErrorMessage(resp_desc);  //设置错误信息
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
}
