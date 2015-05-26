package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL030004Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 宽带密码重置
 * 
 * @author 吴宗德
 *
 */
public class InternetPwdResetInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(InternetPwdResetInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private static StringBuffer internetCitys = new StringBuffer();
	
	private static Map<String, String> internetCitysMap = new HashMap<String, String>();
	
	static {
		internetCitys.append("baoy_,")
		.append("bh_,")
		.append("bn_,")
		.append("chs_,")
		.append("chz_,")
		.append("chzh_,")
		.append("df_,")
		.append("dh_,")
		.append("dt_,")
		.append("dy_,")
		.append("fx_,")
		.append("gaoy_,")
		.append("gch_,")
		.append("gn_,")
		.append("gy_,")
		.append("gyu_,")
		.append("ha_,")
		.append("hai_,")
		.append("hm_,")
		.append("hy_,")
		.append("hzh_,")
		.append("jd_,")
		.append("jh_,")
		.append("jhu_,")
		.append("jj_,")
		.append("jn_,")
		.append("jp_,")
		.append("jr_,")
		.append("jt_,")
		.append("jy_,")
		.append("jyan_,")
		.append("ksh_,")
		.append("lh_,")
		.append("liy_,")
		.append("lsh_,")
		.append("lyg_,")
		.append("nj_,")
		.append("nsh_,")
		.append("nt_,")
		.append("px_,")
		.append("pzh_,")
		.append("qd_,")
		.append("rd_,")
		.append("rg_,")
		.append("sh_,")
		.append("sn_,")
		.append("shq_,")
		.append("shy_,")
		.append("suy_,")
		.append("sy_,")
		.append("sz_,")
		.append("tch_,")
		.append("tx_,")
		.append("tz_,")
		.append("tzh_,")
		.append("wj_,")
		.append("wx_,")
		.append("xh_,")
		.append("xyi_,")
		.append("xsh_,")
		.append("xy_,")
		.append("xzh_,")
		.append("yang_,")
		.append("ych_,")
		.append("yx_,")
		.append("yz_,")
		.append("yzh_,")
		.append("zhj_,")
		.append("zjg_,")
		.append("suyu_,")
		.append("tsh_,")
		.append("jw_,")
		.append("gg_,")
		.append("gj_,")
		.append("ly_,")
		.append("sz,")
		.append("ha,")
		.append("sq,")
		.append("nj,")
		.append("ly,")
		.append("xz,")
		.append("cz,")
		.append("zj,")
		.append("wx,")
		.append("nt,")
		.append("tz,")
		.append("yc,")
		.append("yz,");
		
		internetCitysMap.put("baoy_", "23");
		internetCitysMap.put("bh_", "22");
		internetCitysMap.put("bn_", "22");
		internetCitysMap.put("chs_", "11");
		internetCitysMap.put("chz_", "17");
		internetCitysMap.put("chzh_", "12");
		internetCitysMap.put("df_", "22");
		internetCitysMap.put("dh_", "15");
		internetCitysMap.put("dt_", "22");
		internetCitysMap.put("dy_", "18");
		internetCitysMap.put("fx_", "16");
		internetCitysMap.put("gaoy_", "23");
		internetCitysMap.put("gch_", "14");
		internetCitysMap.put("gn_", "15");
		internetCitysMap.put("gy_", "15");
		internetCitysMap.put("gyu_", "15");
		internetCitysMap.put("ha_", "12");
		internetCitysMap.put("hai_", "20");
		internetCitysMap.put("hm_", "20");
		internetCitysMap.put("hy_", "12");
		internetCitysMap.put("hzh_", "12");
		internetCitysMap.put("jd_", "23");
		internetCitysMap.put("jh_", "22");
		internetCitysMap.put("jhu_", "12");
		internetCitysMap.put("jj_", "21");
		internetCitysMap.put("jn_", "14");
		internetCitysMap.put("jp_", "14");
		internetCitysMap.put("jr_", "18");
		internetCitysMap.put("jt_", "17");
		internetCitysMap.put("jy_", "19");
		internetCitysMap.put("jyan_", "21");
		internetCitysMap.put("ksh_", "11");
		internetCitysMap.put("lh_", "14");
		internetCitysMap.put("liy_", "17");
		internetCitysMap.put("lsh_", "12");
		internetCitysMap.put("lyg_", "15");
		internetCitysMap.put("nj_", "14");
		internetCitysMap.put("nsh_", "14");
		internetCitysMap.put("nt_", "20");
		internetCitysMap.put("px_", "16");
		internetCitysMap.put("pzh_", "16");
		internetCitysMap.put("qd_", "20");
		internetCitysMap.put("rd_", "20");
		internetCitysMap.put("rg_", "20");
		internetCitysMap.put("sh_", "13");
		internetCitysMap.put("sn_", "16");
		internetCitysMap.put("shq_", "13");
		internetCitysMap.put("shy_", "22");
		internetCitysMap.put("suy_", "13");
		internetCitysMap.put("sy_", "13");
		internetCitysMap.put("sz_", "11");
		internetCitysMap.put("tch_", "11");
		internetCitysMap.put("tx_", "21");
		internetCitysMap.put("tz_", "20");
		internetCitysMap.put("tzh_", "21");
		internetCitysMap.put("wj_", "11");
		internetCitysMap.put("wx_", "19");
		internetCitysMap.put("xh_", "21");
		internetCitysMap.put("xyi_", "16");
		internetCitysMap.put("xsh_", "22");
		internetCitysMap.put("xy_", "12");
		internetCitysMap.put("xzh_", "16");
		internetCitysMap.put("yang_", "18");
		internetCitysMap.put("ych_", "22");
		internetCitysMap.put("yx_", "19");
		internetCitysMap.put("yz_", "23");
		internetCitysMap.put("yzh_", "23");
		internetCitysMap.put("zhj_", "18");
		internetCitysMap.put("zjg_", "11");
		internetCitysMap.put("suyu_", "13");
		internetCitysMap.put("tsh_", "16");
		internetCitysMap.put("jw_", "16");
		internetCitysMap.put("gg_", "21");
		internetCitysMap.put("gj_", "23");
		internetCitysMap.put("ly_", "15");
		internetCitysMap.put("sz", "11");
		internetCitysMap.put("ha", "12");
		internetCitysMap.put("sq", "13");
		internetCitysMap.put("nj", "14");
		internetCitysMap.put("ly", "15");
		internetCitysMap.put("xz", "16");
		internetCitysMap.put("cz", "17");
		internetCitysMap.put("zj", "18");
		internetCitysMap.put("wx", "19");
		internetCitysMap.put("nt", "20");
		internetCitysMap.put("tz", "21");
		internetCitysMap.put("yc", "22");
		internetCitysMap.put("yz", "23");
	}
	
	public InternetPwdResetInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL030004Result res = new DEL030004Result();
		String city = null;
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			city = (String)this.getParameters(params, "context_ddr_city");
			if (StringUtils.isBlank(city)) {
				String phoneNum = (String)this.getParameters(params, "internetNum");
				String mu = getPhone(phoneNum);
				
				if (!"".equals(mu)) {
					city = internetCitysMap.get(mu);
				} else {
					this.setParameter(params, "phoneNum", phoneNum);
					
					//根据手机号码查询地市信息
					BaseResult userInfoResult = getCity(accessId, config, params);
					if (LOGIC_SUCESS.equals(userInfoResult.getResultCode())) {
						Map userInfoMap = (Map)userInfoResult.getReObj();
						
						city = (String)userInfoMap.get("city");
					} else {
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(userInfoResult.getErrorCode());
						res.setErrorMessage(userInfoResult.getErrorMessage());
						return res;
					}
				}
			}
			
			this.setParameter(params, ServiceExecutor.ServiceConstants.USER_CITY, city);
			this.setParameter(params, "context_ddr_city", city);
			

			String bossTemplate = "cc_cinternetuser_451_mmcz";

			//宽带密码重置
			BaseResult internetPwdResetResult = internetPwdReset(accessId, config, params, bossTemplate);

			if (!LOGIC_SUCESS.equals(internetPwdResetResult.getResultCode())) {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(internetPwdResetResult.getErrorCode());
				res.setErrorMessage(internetPwdResetResult.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 宽带密码重置
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult internetPwdReset(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL030004", bossTemplate, errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询用户信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getCity(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params);

			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", super.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL030004", "cc_cgetusercust_69", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					Map retMap = new HashMap();
					
					XPath xpath = XPath.newInstance("/operation_out/content/user_info/user_city");
					String city = ((Element) xpath.selectSingleNode(root)).getText();
					xpath = XPath.newInstance("/operation_out/content/user_info/user_id");
					String userId = ((Element) xpath.selectSingleNode(root)).getText();
					retMap.put("city", city);
					retMap.put("userId", userId);
					//res.setReObj();
					res.setReObj(retMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	private static String getPhone(String phone) {
		String mu = "";
		
		String [] arr = internetCitys.toString().split(",");
		
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i ++) {
				if (phone.startsWith(arr[i])) {
					mu = arr[i];
					break;
				}
			}
		}
		
		return mu;
	}
	
}