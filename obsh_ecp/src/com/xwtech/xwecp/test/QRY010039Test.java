package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMenuService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMenuServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010039Result;
import com.xwtech.xwecp.util.DESEncrypt;
/**
 * 分布式清单菜单查询
 * @author xufan
 * 2014-06-20
 */
public class QRY010039Test {
	private static final Logger logger = Logger.getLogger(QRY010039Test.class);
	/**
	 * 新大陆提供的密钥，需要每两位转成1个字节
	 */
	private static byte[] BOSS_SECRET_KEY = {
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3,0x4e,
		(byte)0xdd,(byte)0x3b,(byte)0x51,0x24,0x36,(byte)0xa8,(byte)0x28,
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3	
	};
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");
// 		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("2");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "2");
		ic.addContextParameter("req_channel", "4");//渠道号网厅4    掌厅19
     	ic.addContextParameter("city_id", "14");	
		lic.setContextParameter(ic);
		IQueryMenuService co = new QueryMenuServiceClientImpl();
		QRY010039Result re = co.qryMenu("");
		logger.info(" ====== 开始返回参数 ======");
		System.out.println(re.getBillMenuList().size());
	}
}
