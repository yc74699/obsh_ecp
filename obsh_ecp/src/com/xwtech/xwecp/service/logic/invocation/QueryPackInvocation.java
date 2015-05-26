package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProductBusinessPackage;
import com.xwtech.xwecp.service.logic.pojo.QRY040024Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 查询套餐信息
 * 
 * @author Tkk
 * 
 */
public class QueryPackInvocation extends BaseInvocation implements ILogicalService   {
	
	private static final Logger logger = Logger.getLogger(QueryPackInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	/**
	 * BOSS接口
	 */
	private static final String BOSS_INTERFACE = "cc_cgetpackbycode_605";
	
	public QueryPackInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		QRY040024Result result = new QRY040024Result();
		result.setResultCode(LOGIC_SUCESS);
		
		//转换参数
		List<String> codes = (List<String>) this.getParameters(params, "codes");
		List<BusinessBoss> list = new ArrayList();
		for(String code : codes){
			list.add(new BusinessBoss(code));
		}
		params.add(new RequestParameter("codeCount", list));
		
		//请求BOSS
		try {
			String rspXml = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(BOSS_INTERFACE, params), accessId, BOSS_INTERFACE, this.generateCity(params)));
			parseXml(rspXml, result);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}
	
	/**
	 * 解析Xml
	 * @param rspXml
	 * @param result
	 * @throws DocumentException 
	 */
	private void parseXml(String rspXml, QRY040024Result result) throws DocumentException {
		Document doc = DocumentHelper.parseText(rspXml);
		
		//验证返回值
		boolean validate = setResultCode(result, doc, "QRY040024");
		if(validate){
			
			//号码预约
			List<Node> nodes = doc.selectNodes("/operation_out/content/productbusinesspackage_package_code");
			List<ProductBusinessPackage> productBusinessPackageList = new ArrayList<ProductBusinessPackage>();
			for(Node node : nodes){
				ProductBusinessPackage bean = new ProductBusinessPackage();
				bean.setPackageName(node.selectSingleNode("./cproductbusinesspackagedt/productbusinesspackage_package_name").getText());
				bean.setPackageDesc(node.selectSingleNode("./cproductbusinesspackagedt/productbusinesspackage_package_desc").getText());
				productBusinessPackageList.add(bean);
			}
			result.setProductBusinessListPackage(productBusinessPackageList);
		}
	}
	
	/**
	 * 设返回值
	 * @param result
	 * @param doc
	 * @param bossTemplate
	 * @return
	 */
	private boolean setResultCode(BaseServiceInvocationResult result, Document doc, String bossTemplate) {
		boolean validate = true;

		// 结果码
		String errCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();

		// 返回信息
		String errDesc = doc.selectSingleNode("/operation_out/response/resp_desc").getText();
		result.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);

		// 转换本地化结果信息
		if (!BOSS_SUCCESS.equals(errCode)) {
			ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY040024", bossTemplate, errCode);
			if (null != errDt) {
				errCode = errDt.getLiErrCode();
				errDesc = errDt.getLiErrMsg();
			}
			result.setErrorCode(errCode);
			result.setErrorMessage(errDesc);
			validate = false;
		}
		return validate;
	}
	
	public class BusinessBoss {
		private String parm1;

		public BusinessBoss(String parm1) {
			super();
			this.parm1 = parm1;
		}

		public String getParm1() {
			return parm1;
		}

		public void setParm1(String parm1) {
			this.parm1 = parm1;
		}
	}
}
