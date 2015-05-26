package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetUsrTransPkgListByTempNo;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetUsrTransPkgListByTempNoClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050045Result;
import com.xwtech.xwecp.service.logic.pojo.TransIncrementInfo;

/**
 * 
 * @author 张成东
 * @mail   zhangcd@mail.xwtec.cn
 * 创建于：Jun 22, 2011
 * 描述：携号转网 查产品列表
 */
public class QRY050045Test
{
	private static final Logger logger = Logger.getLogger(QRY050045Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13952395959");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13952395959");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13952395959");
		ic.addContextParameter("ddr_city", "12");
		
		ic.addContextParameter("user_id", "1208200009257044");  //2056200011182291
		ic.addContextParameter("phoneNum", "15949152677");
		ic.addContextParameter("fromCity", "12");
		ic.addContextParameter("toCity", "13");
		ic.addContextParameter("toCity", "13");
		lic.setContextParameter(ic);
		
		IGetUsrTransPkgListByTempNo co = new GetUsrTransPkgListByTempNoClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		QRY050045Result re = co.getUsrTransPkgListByTempNo("15261779085", "1000100070", "88009898562462","12");
		
		
		List<TransIncrementInfo> incrementInfos = re.getIncrementInfos();

		//以编码以20开头的均为套餐，select_type=1为必选套餐====以下拉列表展开
		//增值业务（非20开头），以及非必选套餐====用checkbox多选，其中select_type=1的业务默认勾选并且灰化
		
		List<TransIncrementInfo> bxtc = new ArrayList<TransIncrementInfo>();
		List<TransIncrementInfo> zzyw = new ArrayList<TransIncrementInfo>();
		Map bxtcAll = new HashMap();
		
		
		
		for(TransIncrementInfo incrementsInfo : incrementInfos)
		{
			//1:如果prodid为空，并且该sub_prodid不是某个产品的prodid，则为增值业务，用checkbox多选，其中select_type=1的业务默认勾选并且灰化
			if(incrementsInfo.getPkgId()==null || "".equals(incrementsInfo.getPkgId()))
			{
				boolean isPack = true;
				for(TransIncrementInfo incrementsInfo2 : incrementInfos)
				{
					if(incrementsInfo.getSubPkgId()!=null && incrementsInfo.getSubPkgId().equals(incrementsInfo2.getPkgId()))
					{
						isPack = false;
					}
				}
				if(isPack)
				{
					zzyw.add(incrementsInfo);
				}
			}else
			{
				//2:如果prodid不为空，则在select框中显示prodid相同的一类套餐（是否必选根据sub_prodid为该prodid的产品包下的select_type）
				for(TransIncrementInfo incrementsInfo2 : incrementInfos)
				{
					//System.out.println(incrementsInfo2.getSubPkgId()+"-------"+incrementsInfo.getPkgId());
					if(incrementsInfo2.getSubPkgId()!=null && incrementsInfo2.getSubPkgId().equals(incrementsInfo.getPkgId()))
					{
						bxtc.add(incrementsInfo2);
						continue;
					}
				}
			}
		}
		
		//2:如果prodid不为空，则在select框中显示prodid相同的一类套餐（是否必选根据sub_prodid为该prodid的产品包下的select_type）
		for(TransIncrementInfo bxtcincrementsInfo : bxtc)
		{
			for(TransIncrementInfo incrementsInfo2 : incrementInfos)
			{
				if(bxtcincrementsInfo.getSubPkgId()!=null && bxtcincrementsInfo.getSubPkgId().equals(incrementsInfo2.getPkgId()))
				{
					if(bxtcAll.get(bxtcincrementsInfo.getSubPkgId())!=null)
					{
						UsrTransSelectBean bxtctemp = (UsrTransSelectBean)bxtcAll.get(bxtcincrementsInfo.getSubPkgId());
						if(!bxtctemp.getTransBusiList().contains(incrementsInfo2)){
							bxtctemp.getTransBusiList().add(incrementsInfo2);
						}
						bxtcAll.put(bxtcincrementsInfo.getSubPkgId(), bxtctemp);
						continue;
					}
					else
					{
						UsrTransSelectBean selectBean = new UsrTransSelectBean();
						List<TransIncrementInfo> bxtctemp = new ArrayList<TransIncrementInfo>();
						if(!bxtctemp.contains(incrementsInfo2)){
							bxtctemp.add(incrementsInfo2);
						}
						selectBean.setSelecttype(String.valueOf(bxtcincrementsInfo.getSelectType()));
						selectBean.setTransBusiList(bxtctemp);
						bxtcAll.put(bxtcincrementsInfo.getSubPkgId(), selectBean);
						continue;
					}
					
				}
			}
		}
		
		
		
		for(TransIncrementInfo incrementsInfo2 : zzyw)
		{
			System.out.println("增值业务==========="+incrementsInfo2.getSubPkgName()+"----"+incrementsInfo2.getSubPkgId());
		}
		
		System.out.println(bxtcAll);
		Set set  = bxtcAll.keySet();
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			System.out.println("====》》"+key);
			UsrTransSelectBean list = (UsrTransSelectBean)bxtcAll.get(key);
			
			System.out.println("$$$$$$$$$$$$$$"+list.getSelecttype()+"---"+list.getTransBusiList().size());
			for(int a=0;a<list.getTransBusiList().size();a++)
			{
				System.out.println(((TransIncrementInfo)list.getTransBusiList().get(a)).getSubPkgId()+"-----"+((TransIncrementInfo)list.getTransBusiList().get(a)).getSubPkgName());
				
			}
		}
	}
}
