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
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BillMenuList;
import com.xwtech.xwecp.service.logic.pojo.BillParamList;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY010037Result;
import com.xwtech.xwecp.service.logic.pojo.QRY010039Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.TeletextParseUtils;

/**
 * 分布式清单菜单
 * @author xufan
 * 2014-06-23
 */
public class BillMenuInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(BillMenuInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public BillMenuInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY010039Result res = new QRY010039Result();
		List<BillMenuList> billMenuList=new ArrayList<BillMenuList>();
		List<BillParamList> billParamList=new ArrayList<BillParamList>();
		String rspXml = "";
		ErrorMapping errDt = null;
		Map<String,String>mapBill=new HashMap<String,String>();
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_QRYMENU", params), 
					 accessId, "cc_QRYMENU", city));
			rspXml=rspXml.trim();
			logger.debug(" ====== 查询语音清单返回报文 ======\n" + rspXml);
//			rspXml="<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\" ?><operation_out><req_seq>0_14</req_seq><resp_seq>20140623105455</resp_seq><resp_time>20140623105455</resp_time><response><resp_type>0</resp_type><resp_code>0</resp_code><resp_desc><![CDATA[成功]]></resp_desc></response><content><ddb_nextno>1</ddb_nextno><Key>1:1002:-1</Key><is_end>1</is_end><name_data>状态类型~对方号码~通话时间~通话时长~通话地点~通信类型~基本话费~长途话费~小计~套餐费~套餐</name_data><length_data>30~24~14~6~40~22~12~12~12~12~256</length_data><xtable_data></xtable_data></content></operation_out>";
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010039", "cc_QRYMENU", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
					
							String month_no = root.getChild("content").getChildText("month_no");
							res.setMonth_no(month_no);
							List<Element> menuList = root.getChild("content").getChildren("menu_list");//是否可以打印	0：不可以，1：可以
							for (Element element : menuList) {
								List<Element> menList =element.getChildren("menu");
								for(Element element2 :menList){
									BillMenuList bml=new BillMenuList();
									String menu_name =element2.getChildText("menu_name");
									String process_code =element2.getChildText("process_code");
									String menu_level =element2.getChildText("menu_level");
									String is_leaf =element2.getChildText("is_leaf");
									String menu_id =element2.getChildText("menu_id");
									String pre_menu_id =element2.getChildText("pre_menu_id");
									bml.setMenu_name(menu_name);
									bml.setIs_leaf(is_leaf);
									bml.setMenu_id(menu_id);
									bml.setMenu_level(menu_level);
									bml.setPre_menu_id(pre_menu_id);
									bml.setProcess_code(process_code);
									billMenuList.add(bml);
									List<Element> paramList =element2.getChildren("param_list");
									if(paramList.size()>0){
									for(Element element3 :paramList){
										List<Element> param =element3.getChildren("param");
										for(Element element4 :param){
										BillParamList bpl=new BillParamList();
										String param_id= element4.getChildText("param_id");
										if("cdr_type".equals(param_id)){
											bpl.setParam_id(element4.getChildText("param_id"));
											bpl.setParam_value(element4.getChildText("param_value"));
											bml.getBillParamList().add(bpl);
										}else{
											bpl.setParam_id(element4.getChildText("param_id"));
											bpl.setParam_value(element4.getChildText("param_value"));
											bml.getBillParamList().add(bpl);
												
										}
										}
										
									}
									}
								}
							}
					}
					catch (Exception ex)
					{
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		res.setBillMenuList(billMenuList);
		return res;
	}
	
	
	
}