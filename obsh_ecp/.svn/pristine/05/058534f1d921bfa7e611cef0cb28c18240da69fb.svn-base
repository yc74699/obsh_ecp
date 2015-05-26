package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
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
import com.xwtech.xwecp.service.logic.pojo.NewUserIncrements;
import com.xwtech.xwecp.service.logic.pojo.QRY040060Result;
import com.xwtech.xwecp.service.logic.pojo.UserIncrements;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 增值业务统一查询 -QRY040060
 * 
 */
public class NewUserIncrementsInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(NewUserIncrementsInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private static SimpleDateFormat dateFormat_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	public NewUserIncrementsInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY040060Result res = new QRY040060Result();
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			List<NewUserIncrements> userIncrementsList = new ArrayList<NewUserIncrements>(); 
			//查询用户全部增值业务信息
			BaseResult userIncrementsResult = null;
			//镇江调用华为接口
			userIncrementsResult = this.getHWIncrementsResult(accessId, config, params, res);
				
			if(LOGIC_SUCESS.equals(userIncrementsResult.getResultCode())){
				userIncrementsList = (List<NewUserIncrements>)userIncrementsResult.getReObj();
			}
			
			res.setUserIncrementsList(userIncrementsList);
			

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}


	/**
	 * 查询用户增值业务信息--华为
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getHWIncrementsResult(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final QRY040060Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String teletextTempate = "cc_cqryuserspinfonew_798";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(teletextTempate, params);
			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, teletextTempate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY040060", teletextTempate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/cusermoninfodt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
	
					List<NewUserIncrements> rtnList = new ArrayList<NewUserIncrements>();
					for (Element element : list) {

						NewUserIncrements incbean= new NewUserIncrements();
						incbean.setRegName(element.getChildTextTrim("usermoninfo_spbizname").trim());
						incbean.setRegSpId(element.getChildTextTrim("usermoninfo_spid").trim());
						incbean.setRegBizId(element.getChildTextTrim("usermoninfo_spbizid").trim());
						incbean.setRegSpName(element.getChildTextTrim("usermoninfo_spname").trim());
						incbean.setRegOrderId(element.getChildTextTrim("usermoninfo_seq").trim());
						//华为定义业务类型和新大陆不统一，华为做特殊处理
                		if ("1".equals(element.getChildTextTrim("usermoninfo_deal_type").trim())){
                			incbean.setRegTypeClass("3");
                		}else if ("2".equals(element.getChildTextTrim("usermoninfo_deal_type").trim())){
                			incbean.setRegTypeClass("1");
                		}else if ("3".equals(element.getChildTextTrim("usermoninfo_deal_type").trim())){
                			incbean.setRegTypeClass("2");
                		}else{
                			incbean.setRegTypeClass("4");
                		}
                    	if ("1".equals(element.getChildTextTrim("usermoninfo_deal_type").trim()) &&
                    			element.getChildTextTrim("usermoninfo_spname").trim().length() >=2 &&
                    			"中国".equals(element.getChildTextTrim("usermoninfo_spname").trim().substring(0,2))){
                    		incbean.setRegSpName("中国移动");
                    	}else{
                    		incbean.setRegSpName(element.getChildTextTrim("usermoninfo_spname").trim());
                    	}
						incbean.setRegMonthFee(element.getChildTextTrim("usermoninfo_price").trim());
						
						//2014-11-20 00:00:00格式的日期转换成20141120000000格式输出
						incbean.setRegBeginDate(formatStrToOtherStr(element.getChildTextTrim("usermoninfo_start_time").trim()));
						
						incbean.setRegEndTime(element.getChildTextTrim("usermoninfo_end_time").trim());
						incbean.setReqReplaceCharge(element.getChildTextTrim("usermoninfo_replace_charge").trim());
						incbean.setReqSpbiztype(element.getChildTextTrim("usermoninfo_spbiztype").trim());
						incbean.setRegDesc(element.getChildTextTrim("usermoninfo_domain").trim());
						rtnList.add(incbean);
					}
					
					
					res.setReObj(rtnList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp) {
		Element root = null;
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return root;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss格式的日期转换成yyyyMMddHHmmss格式输出
	 */
	public static String formatStrToOtherStr(String time) {
		String reTime = "";
		try {
			reTime = dateFormat.format(dateFormat_2.parse(time));
		}
		catch (ParseException e) {
			
		}
		return reTime;
	}
}