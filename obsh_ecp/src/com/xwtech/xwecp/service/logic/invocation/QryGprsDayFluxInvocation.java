package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.xwtech.xwecp.service.logic.pojo.GprsDayFlux;
import com.xwtech.xwecp.service.logic.pojo.QRY040064Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

public class QryGprsDayFluxInvocation extends BaseInvocation implements
		ILogicalService {
private static final Logger logger = Logger.getLogger(QryGprsDayFluxInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	public QryGprsDayFluxInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY040064Result res = new QRY040064Result();
		//放置查询的日期
		setParameter(params, "daynumber", DateTimeUtil.getTodayDay());
		
		try{
			//根据数据库中的报文模板和传入参数组装好XML报文
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsdayflux_70", params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (checkObject(reqXml))
			{
				//发送报文到对应的地市
				String rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_4gqrygprsdayflux_70", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				
				if (checkReturnXml(rspXml,res))
				{
					Element personalInfo = this.getElement(rspXml.getBytes());
					List<?> gprsList = personalInfo.getChild("content").getChildren("gprs_list");
					int size = gprsList.size();
					
					List<GprsDayFlux> gprsDayFluxList = new ArrayList<GprsDayFlux>();
					GprsDayFlux gprsDayFlux;
					
					if(checkObject(gprsList))
					{
						for(int i = 0;i < size;i++)
						{
							Element gprs = ((Element)gprsList.get(i));
							//日期
							int day = Integer.parseInt(gprs.getChildText("day"));
							//日总流量
							String flux = gprs.getChildText("gprs_flux");
							//数据类型，2G，3G，4G
							String type = gprs.getChildText("gprs_net_type");
							
							if(i != (day-1) && i > (day-1))
							{
								gprsDayFlux = gprsDayFluxList.get(day-1);
								gprsDayFluxList.remove(day-1);
							}
							else
							{
								gprsDayFlux = new GprsDayFlux();
								gprsDayFlux.setDayNum(day+"");
							}
							
							//根据报文里面的内容来把值放进对应的数据流量格式
							if("2".equals(type))
							{
								gprsDayFlux.setGprs2GFlux(flux);
							}
							else if("3".equals(type))
							{
								gprsDayFlux.setGprs3GFlux(flux);
							}
							else if("4".equals(type))
							{
								gprsDayFlux.setGprs4GFlux(flux);
							}
							
							//重新把值放进map
							gprsDayFluxList.add(day-1, gprsDayFlux);
						}
						res.setGprsDayFluxList(gprsDayFluxList);
					}			
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return res;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return boolean
	 */
	private boolean checkReturnXml(String rspXml,BaseServiceInvocationResult res)
	{
		Element root = this.getElement(rspXml.getBytes());
		String resp_code = root.getChild("response").getChildText("resp_code");
		String resp_desc = root.getChild("response").getChildText("resp_desc");
		
		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		if(checkObject(rspXml))
		{
			//如果返回的报文是失败，或者没有报文返回，返回失败
			if(!LOGIC_SUCESS.equals(resultCode) || !checkObject(root))
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
			else
			{
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				return true;
			}
		}
		return false;
	}
	/**
	 * 通用判空方法，判断对象是否为空
	 * @param obg
	 * @return boolean
	 */
	private boolean checkObject(Object obj)
	{
		boolean result = false;
		//一般判空
		result = null == obj ? false :  true;
		//List类型
		if(obj instanceof List)
		{
			//如果对象是list，就转成List类型来判空
			List<?> list = (List<?>)obj;
			result = (null == list || list.size() <= 0)? false :  true;
		}
		//String类型
		if(obj instanceof String)
		{
			String str = (String)obj;
			result = ("".equals(str) || null == str) ? false :  true;
		}
		return result;
	}
}
