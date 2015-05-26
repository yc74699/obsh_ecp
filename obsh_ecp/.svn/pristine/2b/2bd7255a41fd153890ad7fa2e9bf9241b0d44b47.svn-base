package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProResource;
import com.xwtech.xwecp.service.logic.pojo.QRY050022Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品互转 可转产品查询  
 * 
 * @author 吴宗德
 *
 */
public class GetUserProResourceInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(GetUserProResourceInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public GetUserProResourceInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050022Result res = new QRY050022Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			BaseResult proList = this.getProList(accessId, config, params);
			if (LOGIC_SUCESS.equals(proList.getResultCode())) {
				//BaseResult proCfgList = this.getProCfgList(accessId, config, params);
					//用户品牌字典
					Map<String, String> brandDic = getBrandNameMap();
					List<CWebProductCfgDtBean> pList = (List<CWebProductCfgDtBean>)proList.getReObj();
					//List<CcCGetProByCityBean> pCfgList = (List<CcCGetProByCityBean>)proCfgList.getReObj();
					List<ProResource> proResource = new ArrayList<ProResource>(pList.size());
					ProResource bean = null;
					for (CWebProductCfgDtBean obj:pList) {
						bean = new ProResource();
						//for (CcCGetProByCityBean objCfg:pCfgList) {
							if (!"100073".equals(obj.getIn_product_id()) && !"100071".equals(obj.getIn_product_id()))//20100812屏蔽神州行轻松卡3（100073）和	神州行标准卡2（100071
							{
								
								bean.setProId(obj.getIn_product_id());
								bean.setProName(obj.getIn_product_name());
								//bean.setProDesc(objCfg.getProduct_desc());
								bean.setBrandId(obj.getIn_brand_id());
								//bean.setBrandName(brandDic.get(objCfg.getBrand_id()));
								//bean.setPayMode(Integer.parseInt(objCfg.getPay_mode()));
								
								proResource.add(bean);
							}
					}
					res.setProResources(proResource);
					
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(proList.getErrorCode());
				res.setErrorMessage(proList.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 网营全品牌查询可用产品
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getProList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		List<CWebProductCfgDtBean> proList = null;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetwebprocfg_337", params);
				
				logger.debug(" ====== 查询 产品互转 可转产品 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetwebprocfg_337", this.generateCity(params)));
				logger.debug(" ====== 查询 产品互转 可转产品 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050022", "cc_cgetwebprocfg_337", errCode);
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
						List webproductcfg_out_product_id = null;
						try
						{
							webproductcfg_out_product_id = root.getChild("content").getChildren("webproductcfg_out_product_id");
						}
						catch (Exception e)
						{
							webproductcfg_out_product_id = null;
						}
						if (null != webproductcfg_out_product_id && webproductcfg_out_product_id.size() > 0) {
							proList = new ArrayList<CWebProductCfgDtBean>(webproductcfg_out_product_id.size());
							CWebProductCfgDtBean bean = null;
							for (int i = 0; i < webproductcfg_out_product_id.size(); i++)
							{
								Element cwebproductcfgdt = ((Element)webproductcfg_out_product_id.get(i)).getChild("cwebproductcfgdt");
								if (null != cwebproductcfgdt)
								{
									bean = new CWebProductCfgDtBean();
									
									//bean.setOut_product_id(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_out_product_id")).replaceAll(""));
									bean.setIn_product_id(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_in_product_id")).replaceAll(""));
									bean.setIn_product_name(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_in_product_name")).replaceAll(""));
									bean.setIn_brand_id(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_in_brand_id")).replaceAll(""));
//									bean.setCity_id(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_city_id")).replaceAll(""));
//									bean.setOperator_id(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_operator_id")).replaceAll(""));               	
//									bean.setCreate_date(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_create_date")).replaceAll(""));
//									bean.setChange_operator(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_change_operator")).replaceAll(""));
//									bean.setChange_date(p.matcher(cwebproductcfgdt.getChildText("webproductcfg_change_date")).replaceAll(""));
//									
				    				proList.add(bean);
								}
							}
//							this.wellFormedDAO.getCache().add(key, proList);
							res.setReObj(proList);
						}
					}
				}
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 网营产品配置信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
//	protected BaseResult getProCfgList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
//		BaseResult res = new BaseResult();
//		String reqXml = "";
//		String rspXml = "";
//		ErrorMapping errDt = null;
////		String key = "PRO_CFG_LIST_";
//		List<CcCGetProByCityBean> proCfgList = null;
//		try {
////			for (RequestParameter param:params) {
////				if ("city".equals(param.getParameterName())) {
////					key += param.getParameterValue();
////				}
////			}
////			
////			Object obj = this.wellFormedDAO.getCache().get(key);
////			if(obj != null && obj instanceof List)
////			{
////				proCfgList = (List<CcCGetProByCityBean>)obj;
////			} else {
//				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetprobycity_342_CPHZ", params);
//				
//				logger.debug(" ====== 查询产品配置请求报文 ======\n" + reqXml);
//				
//				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetprobycity_342_CPHZ", this.generateCity(params)));
////				logger.debug(" ====== 查询产品配置返回报文 ======\n" + rspXml);
//				if (null != rspXml && !"".equals(rspXml))
//				{
//					Element root = this.getElement(rspXml.getBytes());
//					String errCode = root.getChild("response").getChildText("resp_code");
//					String errDesc = root.getChild("response").getChildText("resp_desc");
//					
//					if (!BOSS_SUCCESS.equals(errCode))
//					{
//						errDt = this.wellFormedDAO.transBossErrCode("QRY050022", "cc_cgetprobycity_342_CPHZ", errCode);
//						if (null != errDt)
//						{
//							errCode = errDt.getLiErrCode();
//							errDesc = errDt.getLiErrMsg();
//						}
//					}
//					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
//					res.setErrorCode(errCode);
//					res.setErrorMessage(errDesc);
//					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
//						List productbasecfg_product_id = null;
//						try
//						{
//							productbasecfg_product_id = root.getChild("content").getChildren("productbasecfg_product_id");
//						}
//						catch (Exception e)
//						{
//							productbasecfg_product_id = null;
//						}
//						if (null != productbasecfg_product_id && productbasecfg_product_id.size() > 0) {
//							proCfgList = new ArrayList<CcCGetProByCityBean>(productbasecfg_product_id.size());
//							CcCGetProByCityBean bean = null;
//							for (int i = 0; i < productbasecfg_product_id.size(); i++)
//							{
//								bean = new CcCGetProByCityBean();
//								Element cwebproductopencfgdt = ((Element)productbasecfg_product_id.get(i)).getChild("productbasecfgdt");
//								if (null != cwebproductopencfgdt)
//								{
//				    				bean.setProduct_id(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_product_id")).replaceAll(""));
//				    				bean.setCity_id(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_city_id")).replaceAll(""));
//				    				bean.setProduct_desc(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_product_desc")).replaceAll(""));
//				    				bean.setPay_mode(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_pay_mode")).replaceAll(""));                	
//				                	bean.setEnd_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_end_date")).replaceAll(""));
//				                	bean.setCreate_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_create_date")).replaceAll(""));
//				                	bean.setChange_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_change_date")).replaceAll(""));
//				                	bean.setIs_province(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_is_province")).replaceAll("")); 
//				                	bean.setChange_operator(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_change_operator")).replaceAll(""));
//				                	bean.setCreate_operator(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_create_operator")).replaceAll(""));                	
//				                	bean.setBegin_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_begin_date")).replaceAll(""));
//				                	bean.setBrand_id(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_brand_id")).replaceAll(""));
//				                	bean.setProduct_name(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_product_name")).replaceAll(""));
//				    				
//				                	proCfgList.add(bean);
//								}
//							}
////							this.wellFormedDAO.getCache().add(key, proCfgList);
//							res.setReObj(proCfgList);
//						}
//					}
//				}
////			}
////			res.setReObj(proCfgList);
//		} catch (Exception e) {
//			logger.error(e, e);
//		}
//		return res;
//	}
	
	public class CWebProductCfgDtBean {
		private String out_product_id;
		private String in_product_id;
		private String city_id;
		private String operator_id;                	
		private String create_date;
		private String change_operator;
		private String change_date;
		private String in_product_name;
		private String in_brand_id;
		public String getIn_product_name() {
			return in_product_name;
		}
		public void setIn_product_name(String inProductName) {
			in_product_name = inProductName;
		}
		
		public String getIn_brand_id() {
			return in_brand_id;
		}
		public void setIn_brand_id(String inBrandId) {
			in_brand_id = inBrandId;
		}
		public String getChange_date() {
			return change_date;
		}
		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}
		public String getChange_operator() {
			return change_operator;
		}
		public void setChange_operator(String change_operator) {
			this.change_operator = change_operator;
		}
		public String getCity_id() {
			return city_id;
		}
		public void setCity_id(String city_id) {
			this.city_id = city_id;
		}
		public String getCreate_date() {
			return create_date;
		}
		public void setCreate_date(String create_date) {
			this.create_date = create_date;
		}
		public String getIn_product_id() {
			return in_product_id;
		}
		public void setIn_product_id(String in_product_id) {
			this.in_product_id = in_product_id;
		}
		public String getOperator_id() {
			return operator_id;
		}
		public void setOperator_id(String operator_id) {
			this.operator_id = operator_id;
		}
		public String getOut_product_id() {
			return out_product_id;
		}
		public void setOut_product_id(String out_product_id) {
			this.out_product_id = out_product_id;
		}

	}
	
	public class CcCGetProByCityBean {
		
		private String product_id;
		
		private String city_id;
		
		private String product_desc;
		
		private String pay_mode;
		
		private String end_date;
		
		private String create_date;
		
		private String change_date;
		
		private String is_province;
		
		private String change_operator;
		
		private String create_operator;
		
		private String begin_date;
		
		private String brand_id;
		
		private String product_name;

		public String getBegin_date() {
			return begin_date;
		}

		public void setBegin_date(String begin_date) {
			this.begin_date = begin_date;
		}

		public String getBrand_id() {
			return brand_id;
		}

		public void setBrand_id(String brand_id) {
			this.brand_id = brand_id;
		}

		public String getChange_date() {
			return change_date;
		}

		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}

		public String getChange_operator() {
			return change_operator;
		}

		public void setChange_operator(String change_operator) {
			this.change_operator = change_operator;
		}

		public String getCity_id() {
			return city_id;
		}

		public void setCity_id(String city_id) {
			this.city_id = city_id;
		}

		public String getCreate_date() {
			return create_date;
		}

		public void setCreate_date(String create_date) {
			this.create_date = create_date;
		}

		public String getCreate_operator() {
			return create_operator;
		}

		public void setCreate_operator(String create_operator) {
			this.create_operator = create_operator;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getIs_province() {
			return is_province;
		}

		public void setIs_province(String is_province) {
			this.is_province = is_province;
		}

		public String getPay_mode() {
			return pay_mode;
		}

		public void setPay_mode(String pay_mode) {
			this.pay_mode = pay_mode;
		}

		public String getProduct_desc() {
			return product_desc;
		}

		public void setProduct_desc(String product_desc) {
			this.product_desc = product_desc;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
	}
}