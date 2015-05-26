package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryDdbQqueryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryDdbQqueryServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010037Result;
import com.xwtech.xwecp.util.DESEncrypt;
/**
 * 分布式清单查询
 * @author xufan
 * 2014-06-11
 */
public class QRY010037Test {
	private static final Logger logger = Logger.getLogger(QRY010037Test.class);
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
		lic.setUserCity("12");
		lic.setUserMobile("13615276875");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13615276875");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
     	ic.addContextParameter("city_id", "12");	
		ic.addContextParameter("user_passwd", DESEncrypt.desString("123123", BOSS_SECRET_KEY));
		
		lic.setContextParameter(ic);
		Map<String,Object>map=new HashMap<String,Object>();//入参
		map.put("user_id", "1210300002578196");//用户号
		map.put("begin_day", "20141001");      //开始日期
		map.put("end_day", "20141031");        //结束日期
		map.put("qry_no", "0");        //查询序号---为-1的情况是导出全部，大于等于0是分页
		map.put("page_num", "5");      //一页的话单数量(不能太大或太小，建议100左右，如果不符合规范或不传，则使用BOSS默认值)
		map.put("key", "");            //分页查询下页话单起始标记(首次查询是传空，后续查询每次将上次查询结果中的key传过来)
		map.put("is_detect", "0");     //是否敏感号码(网营使用)
		map.put("req_channel", "4");     ////渠道号网厅4，掌厅19
		
		List<Map<String,Object>>paramList=new ArrayList<Map<String,Object>>();//paramList
		Map<String,Object>mapParam1=new HashMap<String,Object>();
		mapParam1.put("param_id", "cdr_type");//参数cdr_type类型，清单类型。
		mapParam1.put("param_value", "1");    //参数值
		paramList.add(mapParam1);
		Map<String,Object>mapParam2=new HashMap<String,Object>();
		mapParam2.put("param_id", "version");//版本
		mapParam2.put("param_value", "1");   //参数值
		paramList.add(mapParam2);
		
		IQueryDdbQqueryService co = new QueryDdbQqueryServiceClientImpl();
		QRY010037Result re = co.qryDdbQquery(map,paramList);
		
		logger.info(" ====== 开始返回参数 ======"+re.getGsmBillDetail().size());
	}
}
