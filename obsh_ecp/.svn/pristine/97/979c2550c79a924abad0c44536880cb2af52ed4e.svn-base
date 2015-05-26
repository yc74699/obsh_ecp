package com.xwtech.xwecp.service.server;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.xwtech.xwecp.dao.BaseDAO;
import com.xwtech.xwecp.memcached.MemCachedKey;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.util.StringUtil;


public class JSBossResponseValidatorImpl extends BaseDAO implements IBossResponseValidator
{
	private static final Logger logger = Logger.getLogger(JSBossResponseValidatorImpl.class);
	
	/** boss接口成功编码 */
	private static final String BOSS_SUCCESS = "0000";
	
	/** 逻辑接口成功编码 */
	private static final String LOGIC_SUCCESS = "0";
	
	/** 逻辑接口失败编码 */
	private static final String LOGIC_FAILURE = "1";
	
	/**
	 * 树
	 */
	public Document m_Document;

	/**
	 * 根元素
	 */
	public Element m_rootElement;

	/**
	 * 返回业务元素区
	 */
	public Element m_Response;
	
	public boolean validate(String cmd, String bossFunctionId, Object item, BaseServiceInvocationResult result)
	{
		try
		{
			if (((List)item).size() > 0)
			{
				m_rootElement = (Element)((Document)((List)item).get(0)).getRootElement();
				m_Response = m_rootElement.element("response");
				
				String errCode = this.getElementStringValue(m_Response, "resp_code");
				String errDesc = this.getElementStringValue(m_Response, "resp_desc");
				//BOSS返回码
				result.setBossCode(errCode);
				
				logger.info(" ====== JSBossResponseValidatorImpl.resp_code ====== " + errCode);
				logger.info(" ====== JSBossResponseValidatorImpl.resp_desc ====== " + errDesc);
				logger.info(" ====== logicId ==================================== " + cmd);
				logger.info(" ====== bossId ===================================== " + bossFunctionId);
				
				ErrorMapping em = this.transBossErrCode(cmd, bossFunctionId, errCode);
				
				if (em != null) {
					errCode = em.getLiErrCode();
					errDesc = em.getLiErrMsg();
				}
				
				String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCCESS : LOGIC_FAILURE;
				result.setResultCode(resultCode);
				result.setErrorCode(errCode);
//				if (errDesc != null && !"".equals(errDesc) && errDesc.indexOf("(") != -1) {
//					errDesc = errDesc.substring(0, errDesc.indexOf("("));
//				}
				result.setErrorMessage(errDesc);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	public String getElementStringValue(Element m_Father, String strSonName)
	{
		return m_Father.element(strSonName).getText();
	}
	
	/**
	 * boss接口转码
	 *
	 */
	public ErrorMapping transBossErrCode (String logicId, String bossId, String bossErrCode) {
		ErrorMapping errM = null;
		List<Map> list = this.getErrMapping();
		if (list != null && list.size() > 0) {
			ErrorMapping em = null;
			for (Map m:list) {
				em = new ErrorMapping();
				
				em.setLiNumber(String.valueOf(StringUtil.convertNull((String) m.get("F_LI_NUMBER"))));
				em.setBossIntNum(String.valueOf(StringUtil.convertNull((String) m.get("F_BOSS_INT_NUM"))));
				em.setBossErrCode(String.valueOf(StringUtil.convertNull((String) m.get("F_BOSS_ERR_CODE"))));
				em.setBossErrMsg(String.valueOf(StringUtil.convertNull((String) m.get("F_BOSS_ERR_MSG"))));
				em.setLiErrCode(String.valueOf(StringUtil.convertNull((String) m.get("F_LI_ERR_CODE"))));
				em.setLiErrMsg(String.valueOf(StringUtil.convertNull((String) m.get("F_LI_ERR_MSG"))));
				
				if (logicId.equals(em.getLiNumber()) && bossId.equals(em.getBossIntNum()) && bossErrCode.equals(em.getBossErrCode())) {
					errM = em;
					break;
				}
			}
		}
		return errM;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> getErrMapping() {
		List<Map> retList = null;
		String key = MemCachedKey.KEY_T_ERR_MAPPING;
		try {
			Object obj = this.getCache().get(key);
			if(obj != null && obj instanceof List)
			{
				retList =  (List<Map>)obj;
			}
			else
			{
				String SQL = "SELECT F_LI_NUMBER, F_BOSS_INT_NUM, F_BOSS_ERR_CODE, F_BOSS_ERR_MSG, F_LI_ERR_CODE, F_LI_ERR_MSG FROM T_ERR_MAPPING";
				
				retList = this.getJdbcTemplate().queryForList(SQL);
				
				this.getCache().add(key, retList);
			}
		} catch (Exception e) {
			logger.error(e, e);
			retList = null;
		}
		return retList;
	}
	
}
