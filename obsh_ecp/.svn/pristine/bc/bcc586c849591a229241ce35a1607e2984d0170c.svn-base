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
import com.xwtech.xwecp.service.logic.client_impl.common.IFmyorderVerifyService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBugGetStrService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FmyorderVerifyServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBugGetStrServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610040Result;
import com.xwtech.xwecp.service.logic.pojo.Deviceinfo;
import com.xwtech.xwecp.service.logic.pojo.Fmymeminfo;
import com.xwtech.xwecp.service.logic.pojo.ProductInfoSubmit;
import com.xwtech.xwecp.service.logic.pojo.Propertyinfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040067Result;
import com.xwtech.xwecp.service.logic.pojo.RWDInfo;
/**
 * 订单提交
 * @author 仰孝庆   常州现金帐本
 * 2014-05-29
 */
public class DEL610040Test
{
	private static final Logger logger = Logger.getLogger(DEL610040Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
//		props.put("client.channel",    "obsh_channel");
		props.put("client.channel",    "obsh_channel");
    	props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");
//    	props.put("platform.url", "http://10.32.229.70:10006/wap_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");

		props.put("platform.user",     "jhr");
		props.put("platform.password", "jhr");
//		props.put("platform.user",     "xl");
//		props.put("platform.password", "xl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13401852832");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401852832");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		
		IQueryBugGetStrService coo = new QueryBugGetStrServiceClientImpl();
//		QRY040067Result re = co.queryBugGetStr("15189898871", "23","","");
		
		QRY040067Result ree = coo.queryBugGetStr("13401852832", "12","","");
		System.out.println("流水号"+ree.getBugGetStr());
		
		Map<Object,String>map=new HashMap<Object,String>();
		map.put("telnum", "13401852832");// 手机号码
		map.put("totalfee","0");//  费用总额
		map.put("paytype","1");// 支付类型
		map.put("ispayed","0");// 是否支付完成
		
		
		map.put("taskoid",ree.getBugGetStr());// 在线支付唯一序号
//		map.put("taskoid","1288122536565104");// 在线支付唯一序号
		map.put("fmyprodid", "");// 家庭增值产品(套餐种类)
		map.put("fmyeffecttype","1");// 产品生效方式
		
		map.put("virtualprod", "2000003319,1");// 19人家庭V网
		map.put("virtualffecttype","1");// 产品生效方式
		map.put("recommendertel","");// 推荐人
		List<RWDInfo> rwdinfoList = new ArrayList<RWDInfo>();//营销方案子节点,属性:活动编码,档次编码,奖品列表,领取方式,奖品串号,奖品数量,奖品附加属性
// 		RWDInfo ri=new RWDInfo("2512104963","300003172847","12100982731|88014907626930|12100982731|88017690217846","0","","","");
 		RWDInfo ri=new RWDInfo("3001044125","300001504182","88009948362869","0","","","");
// 		RWDInfo ri=new RWDInfo("3000286570","300003171144","88015387934350","0","","","");
// 		RWDInfo ri=new RWDInfo("3001018021","300001474032","88009946978157","0","","","");
// 		RWDInfo ri=new RWDInfo("3000228671","300003128123","88017309724466","0","","","");
// 		RWDInfo ri=new RWDInfo("3001944035","300003028078","88017070191498|88017336796878|88017070191662|88017070190874|88017070191662|88017070190898","0","","","");
// 		RWDInfo ri=new RWDInfo("3001944035","300003028082","88017144453894|88016729656058|88017070191822|88017070191030|88017070191822|88017070191126","0","","","");
// 		RWDInfo ri=new RWDInfo("3000228671","300003128124","88017309724426","0","","","");

		rwdinfoList.add(ri);

		List<ProductInfoSubmit> productionList=new ArrayList<ProductInfoSubmit>();//有线业务产品套餐子节点，属性: 产品编码,归属的产品包编码,是否产品包,生效方式
		ProductInfoSubmit pr=new ProductInfoSubmit("2013000001","","1","0");
		productionList.add(pr);
		ProductInfoSubmit	pr1=new ProductInfoSubmit("2000002848","2013000001","0","0");
		productionList.add(pr1);
		
		ProductInfoSubmit	pr2=new ProductInfoSubmit("2011002057","2013000001","0","0");
		productionList.add(pr2);
//		pr=new ProductInfoSubmit("2000003058","2013000001","0","0");
		System.out.println(productionList.size());
	
		List<Deviceinfo> deviceinfoList=new ArrayList<Deviceinfo>();//家庭设备子节点，属性: 设备大类,设备小类,终端发放方式,归属的有线业务类型
		Deviceinfo dr=new Deviceinfo("FTTH_ONU","","3","Band");
		deviceinfoList.add(dr);
		
		List<Propertyinfo> propertyinfoList=new ArrayList<Propertyinfo>();//有线业务属性子节点,属性: 属性编码，属性值
		Propertyinfo pi=new Propertyinfo("addrName","临淮门古街一网通FTTH");//地址名称
		propertyinfoList.add(pi);
		pi=new Propertyinfo("addrId","3561537");//地址编码
		propertyinfoList.add(pi);
		pi=new Propertyinfo("districtName","临淮门古街一网通FTTH  东郊小镇");//小区名称
		propertyinfoList.add(pi);
		pi=new Propertyinfo("districtId","27291");//小区编码
		propertyinfoList.add(pi);
		pi=new Propertyinfo("areaType","1");//宽带区域类型
		propertyinfoList.add(pi);
		pi=new Propertyinfo("userType","1");//宽带用户类型
		propertyinfoList.add(pi);
		pi=new Propertyinfo("radiusType","1");//Radius归属
		propertyinfoList.add(pi);
		pi=new Propertyinfo("supplyType","1");//宽带提供方
		propertyinfoList.add(pi);
		pi=new Propertyinfo("factoryType","1");//施工厂商类型
		propertyinfoList.add(pi);
		pi=new Propertyinfo("networkType","1");//网络类型
		propertyinfoList.add(pi);
		//手动输入
		pi=new Propertyinfo("passWord ","123456");//宽带密码，6位数字
		propertyinfoList.add(pi);
		pi=new Propertyinfo("linkMan","张三");// 联系人
		propertyinfoList.add(pi);
		pi=new Propertyinfo("contactPhone","13401852832");//联系电话
		propertyinfoList.add(pi);
		pi=new Propertyinfo("appointDate","2015-03-15 00:00:00");//预约安装时间 格式：yyyymmdd，需限定3个月之内
		propertyinfoList.add(pi);
		pi=new Propertyinfo("appointAffix","安装");//预约附加信息 最多500字节
		propertyinfoList.add(pi);
		//默认值
		pi=new Propertyinfo("isBind","0");//联系电话
		propertyinfoList.add(pi);
		pi=new Propertyinfo("loginNum","1");//预约安装时间 格式：yyyymmdd，需限定3个月之内
		propertyinfoList.add(pi);
		pi=new Propertyinfo("limitBandWidth ","2048");//预约附加信息 最多500字节
		propertyinfoList.add(pi);
		pi=new Propertyinfo("rangeType  ","1");//预约附加信息 最多500字节
		propertyinfoList.add(pi);
		List<Fmymeminfo> fmymeminfoList=new ArrayList<Fmymeminfo>();
//		Fmymeminfo fi=new Fmymeminfo("18751446961","12","0","");
//		fmymeminfoList.add(fi);
	
		List<Fmymeminfo> vitualinfoList=new ArrayList<Fmymeminfo>();
		Fmymeminfo v1=new Fmymeminfo("13626182916","12","766");
		vitualinfoList.add(v1);
//		
		
//		rwdinfoList = null;
////		productionList = null;
//		deviceinfoList = null;
//		deviceinfoList = null;
//		propertyinfoList = null;
//		fmymeminfoList = null;
		IFmyorderVerifyService co = new FmyorderVerifyServiceClientImpl();
		DEL610040Result re = co.myorderVerify(map, rwdinfoList,productionList,deviceinfoList,propertyinfoList,fmymeminfoList,vitualinfoList);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode()); 
			System.out.println(" ====== 错误编码 ======"   + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======"   + re.getErrorMessage());	
			System.out.println(" ====== 错误信息 ======"   + re.getOrderid());	
		} 

	}
}
