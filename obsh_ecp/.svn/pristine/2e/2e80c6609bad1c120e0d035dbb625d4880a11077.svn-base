package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.PackageBean;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.DEL040023Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.PackageDepend;
import com.xwtech.xwecp.service.logic.pojo.QRY050028Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 套餐变更
 * 
 * @author 杨光
 *
 */
public class PackageChangeInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(PackageChangeInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private IPackageChangeDAO packageChangeDAO;

	public PackageChangeInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL040023Result res = new DEL040023Result();	
		try
		{
			String city = (String)getParameters(params, "city");
			String oldPckCode = (String)getParameters(params, "oldPckCode");
			String oldPckBossCode = this.getPackageCodeToChange(oldPckCode);
			
			
			String newPckCode = (String)getParameters(params, "newPckCode");
			String newPckBossCode = this.getPackageCodeToChange(newPckCode);
			String packageType = (String)getParameters(params, "packageType");
			if(newPckBossCode.equals("2064") || newPckBossCode.equals("1431") || newPckBossCode.equals("4991")) {
				ServiceLocator sl = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
				ServiceInfo si = sl.locate("QRY050028", params);
				QRY050028Result re = (QRY050028Result)si.getServiceInstance().execute(accessId);
				//取得套餐依赖业务查询返回结果
				List<PackageDepend> appendList = re.getPackageDependList();
				res.setResultCode(LOGIC_SUCESS);
				res.setErrorMessage("");
	
	//			0 – 简单业务
	//			1 – 特殊业务
				BaseResult br = new BaseResult();
				for(PackageDepend bean : appendList) {
					String oprateBizCode = bean.getOprateBizCode();
					String optWay = bean.getOptWay();
					String curStatus = bean.getCurStatus();
					//简单业务调用统一接口
					if("0".equals(optWay) && !"1".equals(curStatus)){
						ServiceLocator sl2 = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
						List<RequestParameter> paramNew = copyParam(params);
						paramNew.add(new RequestParameter("id",oprateBizCode));//业务编码
						paramNew.add(new RequestParameter("oprType","1"));//1、	开通 2、关闭 3、变更
						paramNew.add(new RequestParameter("chooseFlag","3"));//1、立即 2、次日 3、次月
						ServiceInfo si2 = sl2.locate("DEL010001", paramNew);
						DEL010001Result delResult = (DEL010001Result)si2.getServiceInstance().execute(accessId);
						br.setResultCode(delResult.getResultCode());
						if(!br.getResultCode().equals("0")){
							br.setErrorCode(delResult.getErrorCode());
							br.setErrorMessage(delResult.getErrorMessage());
						}
						
					}else if("WXYYJLB_GJHY".equals(oprateBizCode)){
						//无线音乐俱乐部特殊处理
						List<RequestParameter> paramNew = copyParam(params);
						BaseResult openMusicRes = this.dealMusicInfoClub(accessId, config, paramNew);
						if(LOGIC_SUCESS.equals(openMusicRes.getResultCode())){
							List<RequestParameter> paramNew2 = copyParam(params);
							BaseResult openMusicBookRes = this.dealMusicBookClub(accessId, config, paramNew2);
							br.setResultCode(openMusicBookRes.getResultCode());
							if(!br.getResultCode().equals("0")){
								br.setErrorCode(openMusicBookRes.getErrorCode());
								br.setErrorMessage(openMusicBookRes.getErrorMessage());
							}
						}else{
							br.setResultCode(openMusicRes.getResultCode());
							br.setErrorCode(openMusicRes.getErrorCode());
							br.setErrorMessage(openMusicRes.getErrorMessage());
						}
					}
					//其他业务开通结果
					res.setResultCode(br.getResultCode());
					res.setErrorCode(br.getErrorCode());
					res.setErrorMessage(br.getErrorMessage());
				}
			}
			if(!StringUtil.isNull(newPckBossCode) && !StringUtil.isNull(oldPckBossCode)){
				List tempList = new ArrayList();
				Map map =  new HashMap();
    			map.put("pckCode", newPckBossCode);
    			map.put("pckType", packageType);
    			map.put("oldPckCode", oldPckBossCode);
    			tempList.add(map);
    			List<RequestParameter> paramNew = copyParam(params);
    			paramNew.add(new RequestParameter("codeCount",tempList));
        		paramNew.add(new RequestParameter("oprType","2"));
        		BaseResult packageChangeResult = this.transPackageChg(accessId, config, paramNew);
        		//res.setResultCode("0");
        		if(LOGIC_SUCESS.equals(packageChangeResult.getResultCode())){
//	        		if("NJDQ".equals(city) && "1031".equals(packageType) 
//	        				&& ("1741".equals(newPckBossCode) || "1742".equals(newPckBossCode))
//							&& !"1741".equals(oldPckBossCode) && !"1742".equals(oldPckBossCode) ){
//	        			//开10元
//	        			List<RequestParameter> paramNew2 = copyParam(params);
//						paramNew2.add(new RequestParameter("oprType","1"));
//						BaseResult openPckResult = this.transPackage(accessId, config, paramNew2);
//						res.setResultCode(openPckResult.getResultCode());
//	        		}else if("NJDQ".equals(city) && "1031".equals(packageType) 
//	        				&& ("1741".equals(oldPckBossCode) || "1742".equals(oldPckBossCode))
//	        				&& !"1741".equals(newPckBossCode) && !"1742".equals(newPckBossCode)){
//	        			//关10元
//	        			List<RequestParameter> paramNew3 = copyParam(params);
//						paramNew3.add(new RequestParameter("oprType","2"));
//						BaseResult closePckResult = this.transPackage(accessId, config, paramNew3);
//						res.setResultCode(closePckResult.getResultCode());
//	        		}else{
	        			//其他套餐变更涉及到开关其他附加套餐的逻辑暂不实现
	        			res.setResultCode(packageChangeResult.getResultCode());
	        		//}
        		}
        		else{
        			res.setResultCode(packageChangeResult.getResultCode());
        			res.setErrorCode(packageChangeResult.getErrorCode());
        			res.setErrorMessage(packageChangeResult.getErrorMessage());
        		}
			}

		}catch (Exception e){
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 开通增值业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult openInc(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cupuserincrem_607_boss", params);
			
			//logger.debug(" ====== 开通增值业务请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cupuserincrem_607_boss", this.generateCity(params)));
			//logger.debug(" ====== 开通增值业务返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cupuserincrem_607_boss", errCode);
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
	 * 开通梦网业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult openMontnet(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {	
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgspuserreg_70", params);
			//logger.debug(" ====== 开通139邮箱梦网业务请求报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchgspuserreg_70", this.generateCity(params)));
			//logger.debug(" ====== 开通139邮箱梦网业务返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cchgspuserreg_70", errCode);
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
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	
	/**
	 * 开通其他梦网业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult dealMonternets(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealmonternet_72", params);
				//logger.debug(" ====== 开通其他梦网业务请求报文 ======\n" + reqXml);
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cdealmonternet_72", this.generateCity(params)));
				//logger.debug(" ====== 开通其他梦网业务返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cdealmonternet_72", errCode);
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
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 处理无线音乐俱乐部相关
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult dealMusicInfoClub(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			List<RequestParameter> paramNew = copyParam(params);
			//查询无限音乐俱乐部
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmusicinfo_391", paramNew);
			//logger.debug(" ====== 查询无限音乐俱乐部请求报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetmusicinfo_391", this.generateCity(params)));
			//logger.debug(" ====== 查询无线音乐俱乐部返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				if("-10000".equals(errCode)){
					res.setReObj(errCode);
				}
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cgetmusicinfo_391", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				
				if(null != errCode && LOGIC_SUCESS.equals(res.getResultCode())){
					//成功开始校验
					XPath xpath = XPath.newInstance("/operation_out/content/musicclubmember_operating_srl/cmusicclubmemberdt");
					List<Element> list = xpath.selectNodes(root);
					if(list != null){
						String musicType = "";
						for (Element element : list) {
							musicType = element.getChildText("musicclubmember_member_level").trim();
							if ("2".equals(musicType)) {
								//已经是高级会员 返回成功
								res.setReObj("LOGIC_SUCESS");
		    					return res ;
		    				} else {	//已经开通无限普通会员，变更会员等级
		    			    	//无线音乐受理变更无限高级会员
		    					List<RequestParameter> paramNew2 = copyParam(paramNew);
		    					paramNew2.add(new RequestParameter("chooseFlag", "3"));
		    					paramNew2.add(new RequestParameter("id", "WXYYJLB_GJHY"));
		    					paramNew2.add(new RequestParameter("oprType", "3"));
		    			    	return this.musicInfoChange(accessId, config, paramNew2);
		    				}
						}
					}
				}else if("-10000".equals(res.getResultCode())){
					List<RequestParameter> paramNew3 = copyParam(paramNew);
					paramNew3.add(new RequestParameter("chooseFlag", "1"));
					paramNew3.add(new RequestParameter("id", "WXYYJLB_GJHY"));
					paramNew3.add(new RequestParameter("oprType", "1"));
					//未开通无线音乐
					return this.musicInfoChange(accessId, config, paramNew3);
				}else{
					//失败
					
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 处理无线音乐俱乐部---预约相关
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult dealMusicBookClub(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			//查询无限音乐俱乐部 预约
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cquerymusic_392", params);
			//logger.debug(" ====== 查询无限音乐俱乐部预约请求报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cquerymusic_392", this.generateCity(params)));
			//logger.debug(" ====== 查询无线音乐俱乐部预约返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cquerymusic_392", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				
				if(null != errCode && LOGIC_SUCESS.equals(res.getResultCode())){
					//成功开始校验
					XPath xpath = XPath.newInstance("/operation_out/content/cmusicclubmemberdt");
					List<Element> list = xpath.selectNodes(root);
					if(list != null){
						String musicType = "";
						for (Element element : list) {
							musicType = element.getChildText("musiccluboperationbk_member_level").trim();
							if ("2".equals(musicType)) {
		    					return res ;
		    				} else {	
		    					//无线音乐预约撤销
		    					List<RequestParameter> paramNew = copyParam(params);
		    			    	res = musicCancel(accessId, config, paramNew);
		    			    	if(null != errCode && LOGIC_SUCESS.equals(res.getResultCode())){
		    			    		//开通无限高级会员
		    			    		//无线音乐受理变更无限高级会员
			    					paramNew.add(new RequestParameter("chooseFlag", "3"));
			    					paramNew.add(new RequestParameter("id", "WXYYJLB_GJHY"));
			    					paramNew.add(new RequestParameter("oprType", "3"));
			    			    	return this.musicInfoChange(accessId, config, paramNew);
		    			    	}
		    				}
						}
					}
				}else if("-13088".equals(res.getResultCode())){
					List<RequestParameter> paramNew2 = copyParam(params);
					paramNew2.add(new RequestParameter("chooseFlag", "1"));
					paramNew2.add(new RequestParameter("id", "WXYYJLB_GJHY"));
					paramNew2.add(new RequestParameter("oprType", "1"));
					//未开通无线音乐
					return this.musicChange(accessId, config, paramNew2);
				}else{
					//失败
					
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 无线音乐受理变更无限高级会员
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult musicInfoChange(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cmsccluboptdeal_75", params);
			// 设置用户产品ID参数
//			paramNew.add(new RequestParameter("chooseFlag", "3"));
//			paramNew.add(new RequestParameter("id", "WXYYJLB_GJHY"));
//			paramNew.add(new RequestParameter("oprType", "3"));
			//logger.debug(" ====== 无线音乐受理请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cmsccluboptdeal_75", this.generateCity(params)));
			//logger.debug(" ====== 无线音乐受理返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cmsccluboptdeal_75", errCode);
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
	 * 无线音乐受理变更无限高级会员-------预约
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult musicChange(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			List<RequestParameter> paramNew = copyParam(params);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cmsccluboptdeal_75", paramNew);
			// 设置用户产品ID参数
			paramNew.add(new RequestParameter("chooseFlag", "3"));
			paramNew.add(new RequestParameter("id", "WXYYJLB_GJHY"));
			paramNew.add(new RequestParameter("oprType", "3"));
			//logger.debug(" ====== 无线音乐受理变更无限高级会员请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cmsccluboptdeal_75", this.generateCity(paramNew)));
			//logger.debug(" ====== 无线音乐受理变更无限高级会员返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cmsccluboptdeal_75", errCode);
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
	 * 无线音乐预约撤销
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult musicCancel(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			List<RequestParameter> paramNew = copyParam(params);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cmsccluboptdeal_75", paramNew);
			// 设置用户产品ID参数
			paramNew.add(new RequestParameter("chooseFlag", "3"));
			paramNew.add(new RequestParameter("id", "WXYYJLB_GJHY"));
			paramNew.add(new RequestParameter("oprType", "3"));
			//logger.debug(" ====== 无线音乐受理变更无限高级会员请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cmsccluboptdeal_75", this.generateCity(paramNew)));
			//logger.debug(" ====== 无线音乐受理变更无限高级会员返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cmsccluboptdeal_75", errCode);
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
	 * 附加功能定制提交
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult transactAddon(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			List<RequestParameter> paramNew = copyParam(params);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cupuserseropt_74_boss", paramNew);
			// 设置用户产品ID参数
			//paramNew.add(new RequestParameter("id", "3"));

			//logger.debug(" ====== 开通serviceList请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cupuserseropt_74_boss", this.generateCity(paramNew)));
			//logger.debug(" ====== 开通serviceList返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cupuserseropt_74_boss", errCode);
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
	 * 查询梦网业务2
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryMonternet(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<MonternetBusinessBean> rtnList = new ArrayList<MonternetBusinessBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqrymonternet_73", params);
			
			//logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqrymonternet_73", this.generateCity(params)));
			//logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cqrymonternet_73", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/monternetcfg_spid/cmonternetcfgdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
	
						MonternetBusinessBean monternetBean = new MonternetBusinessBean();
		
						monternetBean.setBizType(element.getChildTextTrim( "monternetcfg_biztypecode"));
						monternetBean.setBizTypeName(element.getChildTextTrim( "monternetcfg_biztype"));
						monternetBean.setBizCode(element.getChildTextTrim( "monternetcfg_bizcode"));
						monternetBean.setBizName(element.getChildTextTrim( "monternetcfg_bizname"));
						monternetBean.setBizDesc(element.getChildTextTrim( "monternetcfg_bizdesc"));
						monternetBean.setSpId(element.getChildTextTrim( "monternetcfg_spid"));
						monternetBean.setSpServiceId(element.getChildTextTrim( "monternetcfg_spsvcid"));
						monternetBean.setSpShortName(element.getChildTextTrim( "monternetcfg_spshortname"));			
						monternetBean.setAccessModel(element.getChildTextTrim( "monternetcfg_accessmodel"));
						monternetBean.setBillingType(element.getChildTextTrim( "monternetcfg_billingtype"));
						monternetBean.setPrice(element.getChildTextTrim( "monternetcfg_price"));
						monternetBean.setCityId(element.getChildTextTrim( "monternetcfg_city_id"));
						rtnList.add(monternetBean);
	            
					}
					res.setReObj(rtnList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 执行套餐变更
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult transPackageChg(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgpkgforpro_76_TCBG", params);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchgpkgforpro_76_TCBG", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cchgpkgforpro_76_TCBG", errCode);
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
	 * 开通关闭10元套餐
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult transPackage(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			//params.add(new RequestParameter("oprType","1"));
			params.add(new RequestParameter("package_code","2013"));
			params.add(new RequestParameter("package_type","1038"));
			params.add(new RequestParameter("chooseFlag","3"));
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgpkgforpro_76_boss", params);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchgpkgforpro_76_boss", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040023", "cc_cchgpkgforpro_76_boss", errCode);
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
	
	/*
	 * 新编码转成boss编码
	 */
	private String getPackageCodeToChange(String pckCode){
		//查询数据库中所有套餐列表,转换boss_packageCode为ECP_packageCode
		List<PackageBean> packageList = null;
		String packageCode = "";
		try {
			packageList = packageChangeDAO.getPackageInfo();
			if(packageList != null && packageList.size() > 0) {
				for(PackageBean bean : packageList) {
					String bossPackageCode = bean.getPkgNum();
					if(!StringUtil.isNull(pckCode)) {
						if(pckCode.equals(bossPackageCode)) {
							packageCode += bean.getPkgNumBoss();
							break;
						}
						
					}
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return packageCode;
	}
	
	
	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	
	
	
	
	//Bean ====
	/**
	 * 梦网业务bean
	 *
	 */
	public class MonternetBusinessBean {
		
		/** 业务代码 */
		private String bizCode;
		
		/** 业务名称 */
		private String bizName;
		
		/** 业务类型 */
		private String bizType;
		
		/** 业务类型名称 */
		private String bizTypeName;
		
		/** 业务描述 */
		private String bizDesc;
		
		/** SP代码 */
		private String spId;
		
		/** SP服务代码 */
		private String spServiceId;
		
		/** SP简称 */
		private String spShortName;
		
		/** 费率（单位：厘） */
		private String price;
		
		/** 开通时间 */
		private String timeOpened;
		
		/** 关闭时间 */
		private String timeEnded;
		
		/** 状态 */
		private String status;
		
		/**
		 * 计费类型  0-免费 1-按条计费 2-包月计费 3-包时计费 4-包次计费 
		 */
		private String billingType;
		
		/** 地市 */
		private String cityId;
		
		/** 访问模式 */
		private String accessModel;

		/** 功能编码 */
		private String functionCode = "000000";
		/** 功能编码 */
		private String remark = "";
		
		public String getBillingType() {
			return billingType;
		}

		public void setBillingType(String billingType) {
			this.billingType = billingType;
		}

		public String getBizCode() {
			return bizCode;
		}

		public void setBizCode(String bizCode) {
			this.bizCode = bizCode;
		}

		public String getBizDesc() {
			return bizDesc;
		}

		public void setBizDesc(String bizDesc) {
			this.bizDesc = bizDesc;
		}

		public String getBizName() {
			return bizName;
		}

		public void setBizName(String bizName) {
			this.bizName = bizName;
		}

		public String getBizType() {
			return bizType;
		}

		public void setBizType(String bizType) {
			this.bizType = bizType;
		}

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getSpId() {
			return spId;
		}

		public void setSpId(String spId) {
			this.spId = spId;
		}

		public String getSpServiceId() {
			return spServiceId;
		}

		public void setSpServiceId(String spServiceId) {
			this.spServiceId = spServiceId;
		}

		public String getSpShortName() {
			return spShortName;
		}

		public void setSpShortName(String spShortName) {
			this.spShortName = spShortName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getTimeOpened() {
			return timeOpened;
		}

		public String getTimeEnded() {
			return timeEnded;
		}

		public void setTimeEnded(String timeEnded) {
			this.timeEnded = timeEnded;
		}

		public void setTimeOpened(String timeOpened) {
			this.timeOpened = timeOpened;
		}

		public String getBizTypeName() {
			return bizTypeName;
		}

		public void setBizTypeName(String bizTypeName) {
			this.bizTypeName = bizTypeName;
		}

		public String getAccessModel() {
			return accessModel;
		}

		public void setAccessModel(String accessModel) {
			this.accessModel = accessModel;
		}

		public String getFunctionCode()
		{
			return functionCode;
		}

		public void setFunctionCode(String functionCode)
		{
			this.functionCode = functionCode;
		}

		public String getRemark()
		{
			return remark;
		}

		public void setRemark(String remark)
		{
			this.remark = remark;
		}	
	}
	
	/**
	 * 套餐变更实体信息
	 * @author 
	 * @version 1.0
	 */
	public class ChangePackageBean {
		//套餐类型
		private String packageType;
		
		//套餐代码
		private String packageCode;
		
		//原套餐代码 修改时用，其他写0
		private String packageOldCode;
		
		//生效方式 1：立即，2：次日 3：次月
		private String packageInureMode;
		
		//开关标志 1：开通	2：修改3：关闭
		private String packageCmdCode;
		
		//开关标志 1：开通	2：修改3：关闭
		private String package_end_date;
		
		/**
		 * @return the packageType
		 */
		public String getPackageType() {
			return packageType;
		}
		/**
		 * @param packageType the packageType to set
		 */
		public void setPackageType(String packageType) {
			this.packageType = packageType;
		}
		/**
		 * @return the packageCode
		 */
		public String getPackageCode() {
			return packageCode;
		}
		/**
		 * @param packageCode the packageCode to set
		 */
		public void setPackageCode(String packageCode) {
			this.packageCode = packageCode;
		}
		/**
		 * @return the packageOldCode
		 */
		public String getPackageOldCode() {
			return packageOldCode;
		}
		/**
		 * @param packageOldCode the packageOldCode to set
		 */
		public void setPackageOldCode(String packageOldCode) {
			this.packageOldCode = packageOldCode;
		}
		/**
		 * @return the packageInureMode
		 */
		public String getPackageInureMode() {
			return packageInureMode;
		}
		/**
		 * @param packageInureMode the packageInureMode to set
		 */
		public void setPackageInureMode(String packageInureMode) {
			this.packageInureMode = packageInureMode;
		}
		/**
		 * @return the packageCmdCode
		 */
		public String getPackageCmdCode() {
			return packageCmdCode;
		}
		/**
		 * @param packageCmdCode the packageCmdCode to set
		 */
		public void setPackageCmdCode(String packageCmdCode) {
			this.packageCmdCode = packageCmdCode;
		}
		public String getPackage_end_date() {
			return package_end_date;
		}
		public void setPackage_end_date(String package_end_date) {
			this.package_end_date = package_end_date;
		}
	}

	
}
