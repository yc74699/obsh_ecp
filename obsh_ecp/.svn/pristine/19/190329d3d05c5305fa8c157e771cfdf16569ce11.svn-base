package com.xwtech.xwecp.Jedis;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ServiceInfo;

public class RedisInformation {

	private static final Logger logger = Logger.getLogger(RedisInformation.class);
	
	public BaseServiceInvocationResult handleResultToRedis(ServiceInfo sInfo)throws Exception
	{
		ServiceMessage msg = sInfo.getServiceMessage();
		RequestData requestDate = (RequestData)msg.getData();
		String mobile = (String)getParameters(requestDate.getParams(),"context_login_msisdn");
		String phoneNum = (String)getParameters(requestDate.getParams(),"phoneNum");
		String cmd = msg.getHead().getCmd();
		String phone = msg.getHead().getUserMobile();
		BaseServiceInvocationResult res = null;
		Map<String,String> redisData = ((WellFormedDAO) XWECPApp.SPRING_CONTEXT.getBean("wellFormedDAO")).getRedisData();

		//如果数据库配置了该接口编号需要缓存，就从缓存里面查询数据
		if(null==mobile){
			mobile="";
		}
		if(null==phoneNum){
			phoneNum="";
		}
		if(redisData.containsKey(cmd) && mobile.equals(phoneNum) && phoneNum.equals(phone))
		{	
//			logger.info("null    ====    "+XWECPApp.redisCli.get((phone+"_"+cmd).getBytes()));
			//如果redis服务器中信息为空就按原流程走
			if(null == XWECPApp.redisCli.get((phone+"_"+cmd).getBytes()))
			{
				
				res = sInfo.getServiceInstance().execute(msg.getHead().getSequence());
				//返回报文成功时记录
				if("0".equals(res.getResultCode()))
				{
					XWECPApp.redisCli.set((phone+"_"+cmd), res);
					XWECPApp.redisCli.pExpireAt((phone+"_"+cmd), returnTime((String)redisData.get(cmd)));
				}
			}
			else
			{
				res = (BaseServiceInvocationResult)XWECPApp.redisCli.get((phone+"_"+cmd).getBytes());
			}
		}
		else
		{
			res = sInfo.getServiceInstance().execute(msg.getHead().getSequence());
		}
		
		return res;
	}
	
	protected Object getParameters(final List<RequestParameter> params, String parameterName) {
		Object rtnVal = null;
		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals(parameterName)) {
				rtnVal = parameter.getParameterValue();
				break;
			}
		}
		return rtnVal;
	}
	/**
	 * 获取到期的时间戳
	 * @return
	 */
	public long returnTime(String times)
	{
		Calendar calender = new GregorianCalendar();
		calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE), 0, 0, 0);
		calender.add(Calendar.MONTH, 1);
		calender.set(Calendar.DAY_OF_MONTH, 1);
//		calender.set(Calendar.HOUR, 9);
		calender.add(Calendar.SECOND,-1);
//		calender.getTime();
		long time = calender.getTimeInMillis();
		return time;
	}
}