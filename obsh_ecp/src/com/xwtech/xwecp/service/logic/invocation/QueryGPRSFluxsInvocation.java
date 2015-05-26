package com.xwtech.xwecp.service.logic.invocation;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040049Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryGPRSFluxsInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryGPRSFluxsInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private ParseXmlConfig config;

	private String bossService = "ac_agetgprsflux_717";
	
	public QueryGPRSFluxsInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY040049Result res = new QRY040049Result();

		String startDate = (String) getParameters(params,"startDate");
		Map<String,String> totalFee= new HashMap<String,String>();
		String endDate = (String) getParameters(params,"endDate");
		List<String> dateList;
		try{
			dateList = getDateList(startDate,endDate);
		}
		catch(ParseException pe)
		{
			res.setErrorCode(LOGIC_ERROR);
			res.setErrorMessage("入参格式不正确");
			logger.error(pe, pe);
			return res;
		}
		int size = dateList.size();
		try{
			for(int i = 0;i < size;i++)
			{
				String date = dateList.get(i);
				setParameter(params, "date",date);
				String gprsFlux = formatGPRSDetail(queryGPRSDetail(accessId,params,res));
				totalFee.put(date, gprsFlux);
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
			return res;
		}

		res.setTotalFee(totalFee);
		return res;
	}
	
	private String formatGPRSDetail(String gprsFlux)
	{
		double flux = Double.parseDouble((String) gprsFlux);
		DecimalFormat df = new DecimalFormat("#0.00");
		gprsFlux = df.format(flux / 1024 / 1024);
		
		return gprsFlux;
	}
	
	private String queryGPRSDetail(String accessId, List<RequestParameter> params, QRY040049Result res) throws CommunicateException, Exception
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String gprsFlux = "0";
		String errCode = "";

		reqXml = this.bossTeletextUtil.mergeTeletext(bossService, params);
		logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
		if (null != reqXml && !"".equals(reqXml)) {
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossService, super.generateCity(params)));
			logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) 
			{
				root = this.config.getElement(rspXml);
				resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				
				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				
				
				if (LOGIC_SUCESS.equals(resultCode))
				{
					errCode = this.config.getChildText(this.config.getElement(root, "content"), "error_code");
					gprsFlux = this.config.getChildText(this.config.getElement(root, "content"), "gprsbill_total_fee");
					res.setResultCode(resultCode);
					res.setErrorCode(errCode);
					return gprsFlux;
				}
				else
				{
					res.setResultCode(resultCode);
					res.setErrorCode(resp_code);
					res.setErrorMessage(resp_desc);
				}
			}
		}
		return gprsFlux;
	}
	
	private static List<String> getDateList(String startDate,String endDate) throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
	
		Date date = format.parse(endDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<String> dateSet = new ArrayList<String>();
		dateSet.add(endDate);
		int i ;
		if(startDate.equals(endDate))
		{
			i = 0;
		}
		else
		{
			i = -1;
		}
		Date dateTemp;
		String tempDate ="";
		do
		{
			cal.add(Calendar.MONTH,i);
			dateTemp = cal.getTime();
			tempDate = format.format(dateTemp);
			dateSet.add(tempDate);
		}
		while(!startDate.equals(tempDate));
		
		return dateSet;
	}
}
