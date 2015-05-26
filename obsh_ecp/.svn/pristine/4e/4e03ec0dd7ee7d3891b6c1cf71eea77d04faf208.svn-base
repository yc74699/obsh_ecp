package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
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
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050053Result;
import com.xwtech.xwecp.service.logic.pojo.TransProPackageBusi;
import com.xwtech.xwecp.service.logic.pojo.UsrTransSelectBean;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

public class GetPkgInfoInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(GetProBusinessInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;
	
//	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public GetPkgInfoInvocation()
	{  
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY050053Result res = new QRY050053Result();
		try
		{
			BaseResult proBusinessList = this.getPkgBusinessList(accessId, config, params);
			if (LOGIC_SUCESS.equals(proBusinessList.getResultCode())) {
				res = (QRY050053Result) proBusinessList.getReObj();
				
			}
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	protected BaseResult getPkgBusinessList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult result = new BaseResult();
		QRY050053Result res = new QRY050053Result();
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_qrychangemainprolist_338", params);
			
			logger.debug(" ====== 查询产品详细信息请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_qrychangemainprolist_338", this.generateCity(params)));
//			logger.debug(" ====== 查询产品详细信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				result.setResultCode(BOSS_SUCCESS.equals(resp_code)?LOGIC_SUCESS:LOGIC_ERROR);
//				String errCode = root.getChild("response").getChildText("resp_code");
//				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050053", "cc_qrychangemainprolist_338", resp_code);
					if (null != errDt)
					{
						result.setErrorCode(errDt.getLiErrCode());
						result.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && (BOSS_SUCCESS.equals(resp_code))) {
					result.setResultCode(LOGIC_SUCESS);
					//List<TransProPackageBusi> transproPkgList = new ArrayList<TransProPackageBusi>();
					//增加必选套餐LIST
					List<TransProPackageBusi> bxtc  = new ArrayList<TransProPackageBusi>();
					//增值业务
					List<TransProPackageBusi> zzyw  = new ArrayList<TransProPackageBusi>();
					//删除业务
					List<TransProPackageBusi> scyw  = new ArrayList<TransProPackageBusi>();
					List add_package_info = this.config.getContentList(root, "usableprodlist");
					List del_package_info = this.config.getContentList(root, "delprodlist");
					Map<String, UsrTransSelectBean> bxtcMap = new HashMap<String, UsrTransSelectBean>();
					//取可转换的产品
					for (int i = 0; i < add_package_info.size();i++) {
						BaseResult addProduct = null;
						Element addproddt = ((Element)add_package_info.get(i)).getChild("usableproddt");
						String isPkg = getNodeValueByNodeName(addproddt,"is_package");
						String pkgId = getNodeValueByNodeName(addproddt,"package_id");
						String pkgName = getNodeValueByNodeName(addproddt,"package_name");
						String selectType = getNodeValueByNodeName(addproddt,"select_type");
						String isDelBusi = "1";
						//套餐包，掉050054查询包下产品
						if ("1".equals(isPkg)) {
							params.add(new RequestParameter("packageId",pkgId));
							addProduct = this.getDelBusinessList(accessId, config, params);
							List<TransProPackageBusi> ls = (List<TransProPackageBusi>)addProduct.getReObj();
							for (TransProPackageBusi tans : ls)
							{
								tans.setIsPkg(isPkg);
								tans.setPkgid(pkgId);
								tans.setPkgName(pkgName);
								tans.setSelectType(selectType);
								tans.setIsDelBusi(isDelBusi);
								//transproPkgList.add(tans);
								bxtc.add(tans);
								
							}
							if(0 < bxtc.size())
							{
								for(TransProPackageBusi busi : bxtc)
								{
									
									UsrTransSelectBean bxtctemp = (UsrTransSelectBean) bxtcMap.get(busi.getPkgid());
									//只放必选的套餐
									if(null!= busi.getSelectType() && "1".equals(busi.getSelectType()))
									{
										if (null != bxtctemp && null !=bxtctemp.getTransBusiList() && !bxtctemp.getTransBusiList().contains(busi))
			                            {
			                                   bxtctemp.getTransBusiList().add(busi);
			                             }
			                             else
			                             {
			                            	 	bxtctemp = new UsrTransSelectBean();
			                                	bxtctemp.setSelecttype(busi.getSelectType());
			                                	//bxtctemp.setSelecttype("1");
			                                	List<TransProPackageBusi> transBusiList = new ArrayList<TransProPackageBusi>();
			                                	transBusiList.add(busi);
			                                	bxtctemp.setTransBusiList(transBusiList);
			                             }
			                             bxtcMap.put(busi.getPkgid(), bxtctemp);
									}
									
								}
							}
							
						} else {
							if (null != pkgId && !"".equals(pkgId) && (null == getNodeValueByNodeName(addproddt,"product_id") || "".equals(getNodeValueByNodeName(addproddt,"product_id")))) {
								TransProPackageBusi tansBusi = new TransProPackageBusi();
								tansBusi.setPkgid("");
								tansBusi.setPkgName(pkgName);
								tansBusi.setProdId(pkgId);
								tansBusi.setProdName(getNodeValueByNodeName(addproddt,"product_name"));
								tansBusi.setSelectType(selectType);
								tansBusi.setPgkDesc(getNodeValueByNodeName(addproddt,"prodprice"));
								tansBusi.setIsDelBusi(isDelBusi);
								zzyw.add(tansBusi);
								
							} else {
								TransProPackageBusi tansBusi = new TransProPackageBusi();
								tansBusi.setIsPkg(isPkg);
								tansBusi.setPkgid(pkgId);
								tansBusi.setPkgName(pkgName);
								tansBusi.setProdId(getNodeValueByNodeName(addproddt,"product_id"));
								tansBusi.setProdName(getNodeValueByNodeName(addproddt,"product_name"));
								tansBusi.setSelectType(selectType);
								tansBusi.setPgkDesc(getNodeValueByNodeName(addproddt,"prodprice"));
								tansBusi.setIsDelBusi(isDelBusi);
								zzyw.add(tansBusi);
							}
						}
					}
					
					//取要删除的产品
					for (int j = 0;j < del_package_info.size();j++) {
						Element addproddt = ((Element)del_package_info.get(j)).getChild("delproddt");
						String pkgId = getNodeValueByNodeName(addproddt,"package_id");
						String isDelBusi = "0";
						TransProPackageBusi delBusi = new TransProPackageBusi();
						delBusi.setPkgid(pkgId);
						delBusi.setProdId(getNodeValueByNodeName(addproddt,"product_id"));
						delBusi.setProdName(getNodeValueByNodeName(addproddt,"product_name"));
						delBusi.setPgkDesc(getNodeValueByNodeName(addproddt,"prodprice"));
						delBusi.setIsDelBusi(isDelBusi);
						scyw.add(delBusi);
					}
					res.setZzyw(zzyw);
					res.setScyw(scyw);
					res.setBxtcMap(bxtcMap);
					result.setReObj(res);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//logger.info(e)
		}
		return result;
	}
	
	protected BaseResult getDelBusinessList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		ErrorMapping errDt = null;
		List<TransProPackageBusi> proList = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_getpkgmemprod_339", params);
			
			logger.debug(" ====== 查询包下产品请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_getpkgmemprod_339", this.generateCity(params)));
//			logger.debug(" ====== 查询产品详细信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode(BOSS_SUCCESS.equals(resp_code)?LOGIC_SUCESS:LOGIC_ERROR);
//				String errCode = root.getChild("response").getChildText("resp_code");
//				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (BOSS_SUCCESS.equals(resp_code))
				{
					res.setResultCode(LOGIC_SUCESS);
					List del_info = this.config.getContentList(root, "prodinfodt");
					proList = new ArrayList<TransProPackageBusi>(del_info.size());
					for (int i = 0;i < del_info.size();i++) {
						TransProPackageBusi transProPackageBusi = new TransProPackageBusi();
						transProPackageBusi.setProdId(this.config.getChildText((Element) del_info.get(i), "product_id"));
						transProPackageBusi.setProdName(this.config.getChildText((Element) del_info.get(i), "product_name"));
						transProPackageBusi.setPgkDesc(this.config.getChildText((Element) del_info.get(i), "price"));
						transProPackageBusi.setSelectType(this.config.getChildText((Element) del_info.get(i), "select_type"));
						proList.add(transProPackageBusi);
					}
				}
				res.setReObj(proList);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return res;
	}	
	

	
	public BossTeletextUtil getBossTeletextUtil() {
		return bossTeletextUtil;
	}

	public void setBossTeletextUtil(BossTeletextUtil bossTeletextUtil) {
		this.bossTeletextUtil = bossTeletextUtil;
	}

	public IRemote getRemote() {
		return remote;
	}

	public void setRemote(IRemote remote) {
		this.remote = remote;
	}

	public WellFormedDAO getWellFormedDAO() {
		return wellFormedDAO;
	}

	public void setWellFormedDAO(WellFormedDAO wellFormedDAO) {
		this.wellFormedDAO = wellFormedDAO;
	}

}
