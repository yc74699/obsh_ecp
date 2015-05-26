package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceException;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040011Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 动感优惠语音包
 * 
 * @author Tkk
 * 
 */
public class MZonePrivilegeVoicePkgInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(MZonePrivilegeVoicePkgInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	/**
	 * 校园行E网,套餐编码
	 */
	private static final String XIAO_YUAN_XING_E_WANG_PKG_CODE = "1854";
	
	/**
	 * 校园V网,正在表达式
	 */
	private static final String XIAO_YUAN_V_WANG_PATTERN_STR = "(V网)|(大学)";
	
	/**
	 * 1元包200分钟套餐编码
	 */
	private static final String YI_YUAN_BAO_200_FEN_ZHONG_PKG_CODE = "1586";
	
	/**
	 * BOSS模板
	 */
	private static final String BOSS_INTERFACE = "cc_cchgpkgforpro_76";

	public MZonePrivilegeVoicePkgInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		DEL010001Result result = new DEL010001Result();
		
		//1.查询所有的套餐
		try {
			
			//2是否开通校园E网
			boolean isE = isPkgE(result, params, accessId);
			
			//3是否开通校园V网和1元200分钟
			if(isE){
				
				boolean isEAndIs200minutes = isPkgVAndPkg200Miniuts(result, params, accessId);
				if(isEAndIs200minutes){
					
					//4.可以开通动感语音套餐包
					String rspXml = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(BOSS_INTERFACE, params),accessId, BOSS_INTERFACE, this.generateCity(params)));
					Document doc = DocumentHelper.parseText(rspXml);
					setResultCode(result, doc, BOSS_INTERFACE);
				}else{
					result.setResultCode(LOGIC_ERROR);
					result.setErrorMessage("未开通同学网（原校园V网）套餐");
				}
			}
			else{
				result.setResultCode(LOGIC_ERROR);
				result.setErrorCode(LOGIC_ERROR);
				result.setErrorMessage("未开通校园行E网套餐");
			}
			
		} catch (ServiceException e) {
			logger.error(e, e);
		} catch (CommunicateException e) {
			logger.error(e, e);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}

	/**
	 * 判断是否开通的校园E网
	 * @param result
	 * @param params
	 * @param accessId
	 * @return
	 * @throws ServiceException
	 */
	private boolean isPkgE(DEL010001Result result, List<RequestParameter> params, String accessId) throws ServiceException{
		ServiceLocator sl = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
		params.add(new RequestParameter("type","1"));
		params.add(new RequestParameter("scope","3"));
		ServiceInfo si = sl.locate("QRY040011", params);
		QRY040011Result re = (QRY040011Result)si.getServiceInstance().execute(accessId);
		
		boolean isSuccess = setResultCode(re, result);
		boolean isE = false;
		if(isSuccess){
			List<PkgDetail> userPkgs = re.getPkgDetail();
			//2限已开通语音套餐为：校园行E套餐的用户办理（必须是已生效的，预约转的不算）
			for(PkgDetail pkgDetail : userPkgs){
				if(XIAO_YUAN_XING_E_WANG_PKG_CODE.equals(pkgDetail.getPkgId())){
					
					//2.1判断是否是预约
					float beginDate = Float.parseFloat(pkgDetail.getBeginDate());
					float nowDate = Float.parseFloat(DateTimeUtil.getTodayChar14());
					if(nowDate - beginDate > 0){
						isE = true;
					}
					break;
				}
			}
		}
		return isE;
	}
	
	/**
	 * 是否开通校园V网和1元200元套餐
	 * @param result
	 * @param params
	 * @param accessId
	 * @return
	 * @throws ServiceException
	 */
	private boolean isPkgVAndPkg200Miniuts(DEL010001Result result, List<RequestParameter> params, String accessId) throws ServiceException{
		for(RequestParameter rp : params){
			if(rp.getParameterName().equals("type")){
				rp.setParameterValue("2");
				break;
			}
		}
		ServiceLocator sl = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
		ServiceInfo si = sl.locate("QRY040011", params);
		QRY040011Result re = (QRY040011Result)si.getServiceInstance().execute(accessId);
		boolean isSuccess = setResultCode(re, result);
		boolean isV = false;
		boolean is200minutes = false;
		
		if(isSuccess){
			List<PkgDetail> userPkgs = re.getPkgDetail();
			Pattern pattern = Pattern.compile(XIAO_YUAN_V_WANG_PATTERN_STR);
			for(PkgDetail pkgDetail : userPkgs){
				
				//3.1 集团名称like‘ %V网%’或‘大学’，但不含‘%中学V网%’
				String name = pkgDetail.getPkgType();
				Matcher matcher = pattern.matcher(name);
				if(matcher.find()){
					isV = true;
				}
				
				//3.2 并开通1元包200分钟套餐
				String code = pkgDetail.getPkgId();
				if(YI_YUAN_BAO_200_FEN_ZHONG_PKG_CODE.equals(code)){
					is200minutes = true;
				}
			}
		}
		return isV && is200minutes;
	}
	
	/**
	 * 设置错误码
	 * @param result
	 * @param result
	 * @return
	 */
	private boolean setResultCode(BaseServiceInvocationResult re, DEL010001Result result){
		result.setResultCode(re.getResultCode());
		result.setErrorCode(re.getErrorCode());
		result.setErrorMessage(re.getErrorMessage());
		return result.getResultCode().equals(LOGIC_SUCESS);
	}

	/**
	 * 设置错误码
	 * 
	 * @param result
	 * @param doc
	 * @param bossTemplate
	 */
	private boolean setResultCode(BaseServiceInvocationResult result, Document doc, String bossTemplate) {
		boolean validate = true;

		// 结果码
		String errCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();

		// 返回信息
		String errDesc = doc.selectSingleNode("/operation_out/response/resp_desc").getText();
		result.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);

		// 转换本地化结果信息
		ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040025", bossTemplate, errCode);
		if (null != errDt) {
			errCode = errDt.getLiErrCode();
			errDesc = errDt.getLiErrMsg();
		}
		result.setErrorCode(errCode);
		result.setErrorMessage(errDesc);
		validate = false;
		
		return validate;
	}

}