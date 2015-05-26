package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IOrdeqryinforeqOrRespService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.OrdeqryinforeqOrRespServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.OrderInfoByTelnum;
import com.xwtech.xwecp.service.logic.pojo.QRY010040Result;
import com.xwtech.xwecp.service.logic.pojo.TrackInfo;
/**
 * 查询订单信息与订单轨迹
 * 
 * @author xufan
 * 2014-06-17
 */
public class QRY010040Test
{
	private static final Logger logger = Logger.getLogger(QRY010040Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
//        props.put("platform.url", "http://10.32.229.109:10004/wap_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
//		props.put("platform.user", "xl");
//		props.put("platform.password", "xl");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
//		lic.setUserMobile("18751430391");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "18751430391");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "22");
//		ic.addContextParameter("ddr_city", "22");
//		ic.addContextParameter("user_id", "2264300010365120");
		
//		lic.setUserMobile("15952578369");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "15952578369");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "23");
//		ic.addContextParameter("ddr_city", "23");
//		ic.addContextParameter("user_id", "2374200007013567");
		
		
		lic.setUserCity("14");
		lic.setUserMobile("18861270244");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15005156863");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
	
		Map<String,Object>map=new HashMap<String,Object>();
		//*****************************************根据手机号码查询订单信息**************************************//
//		map.put("telnum","15162621935");//手机号码
//		map.put("status","0");//验证类型
//		map.put("startdate","20150101");//开始时间点
//		map.put("enddate","20150216");//结束时间点
//		//************************************ 根据订单编号查询订单轨迹信息  **************************************//
//		map.put("orderid","211201412290443210276");//订单编号
//		map.put("orderregion","11");//地区编码
//		
//		map.put("flag", "2");
		
		
		//启传
		map.put("telnum","13605201444");//手机号码
		map.put("status","0");//验证类型
		map.put("startdate","20110101");//开始时间点
		map.put("enddate","20150323");//结束时间点
		//************************************ 根据订单编号查询订单轨迹信息  **************************************//
		map.put("orderid","214201503240818994603");//订单编号
		map.put("orderregion","14");//地区编码
		map.put("flag", "1");
		
		IOrdeqryinforeqOrRespService co = new OrdeqryinforeqOrRespServiceClientImpl();
		QRY010040Result re = co.ordeqryinforeqOrResp(map);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
//			System.out.println(" ====== orderinfo订单号 ======" + re.getOrderInfo().getOrderid());
//			System.out.println(" ====== Trackinfo的name ======" + re.getTrackInfo().getName());
//			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			if(null !=re.getOrderInfo() && 0 < re.getOrderInfo().size())
			{
				for(OrderInfoByTelnum infoByTelnum : re.getOrderInfo())
				{
					System.out.println(infoByTelnum);
				}
			}
			if(null !=re.getTrackInfo() && 0 < re.getTrackInfo().size())
			{
				for(TrackInfo infoByTelnum : re.getTrackInfo())
				{
					System.out.println(infoByTelnum);
				}
			}
					
				
		}
	}
}
