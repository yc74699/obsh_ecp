package com.xwtech.xwecp.service.logic.invocation;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
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
import com.xwtech.xwecp.service.logic.pojo.FluxHisInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040113Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 
 * @author wangh
 *
 */
public class QueryFluxHisCHInvocation extends BaseInvocation implements
		ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryFluxHisInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private ParseXmlConfig config;
	
	private String usedBossService= "ac_agetgprsflux_717";
	
	private String  TotalBossService= "cc_4gqrygprsallpkgflux_ch";
		
	public QueryFluxHisCHInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY040113Result res = new QRY040113Result();
		String startDate = (String) getParameters(params,"startMonth");
		String endDate = (String) getParameters(params,"endMonth");
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
		List<FluxHisInfo> pkgFluxHisInfoList = new ArrayList<FluxHisInfo>();

		for(int i = 0;i < size;i++)
		{
			FluxHisInfo fhi= new FluxHisInfo();
			
			String date = dateList.get(i).toString();
			fhi.setMonth(date);
			
			setParameter(params, "date",date);
			String usedFlux = formatGPRSDetail(queryGPRSUsed(accessId,params,res),(1024*1024));
			fhi.setActureUsed(usedFlux);
			
			setParameter(params, "cycle",date);
			queryGPRSTotal(accessId,params,res,fhi);
//			fhi.setPkgTotal(totalFlux);
			pkgFluxHisInfoList.add(fhi);
		}

		res.setPkgFluxHisInfoList(pkgFluxHisInfoList);
		return res;
	}
	/**
	 * 把获取到的GPRS量换算成兆为单位
	 * @param gprsFlux
	 * @param ma
	 * @return
	 */
	private String formatGPRSDetail(String gprsFlux,int ma)
	{
		double flux = Double.parseDouble((String) gprsFlux);
		DecimalFormat df = new DecimalFormat("#0.00");
		gprsFlux = df.format(flux /ma);
		
		return gprsFlux;
	}
	
	/**
	 * 根据月份获取GPRS当月的总使用量
	 * @param accessId
	 * @param params
	 * @param res
	 * @return
	 * @throws CommunicateException
	 * @throws Exception
	 */
	private String queryGPRSUsed(String accessId, List<RequestParameter> params, QRY040113Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String gprsFlux = "0";
		String errCode = "";

		reqXml = this.bossTeletextUtil.mergeTeletext(usedBossService, params);
		logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			try
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, usedBossService, super.generateCity(params)));
				
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
				
				root = checkReturnXml(rspXml,res);
				if (null != root) 
				{
					resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					
					if (LOGIC_SUCESS.equals(resultCode))
					{
						Element content = root.getChild("content");//this.config.getElement(root, "");
						errCode = content.getChildText("error_code"); //this.config.getChildText(content, "error_code");
						gprsFlux = content.getChildText("gprsbill_total_fee");//this.config.getChildText(content, "gprsbill_total_fee");
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
			} catch (CommunicateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return gprsFlux;
	}
	/**
	 * 根据月份获取GPRS当月的套餐总量
	 * @param accessId
	 * @param params
	 * @param res
	 * @return
	 * @throws CommunicateException
	 * @throws Exception
	 */
	private void queryGPRSTotal(String accessId, List<RequestParameter> params, QRY040113Result res,FluxHisInfo fhi)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String[] gprsFlux;
		String errCode = "";
		String used = "";
		String totale = "";
		int flagJIBAO =0; // 季包标识 0：没有季包，1有季包；
		reqXml = this.bossTeletextUtil.mergeTeletext(TotalBossService, params);
		logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			try
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, TotalBossService, super.generateCity(params)));
				
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
				
				root = checkReturnXml(rspXml,res);
				if (null != root) 
				{
					resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					
					if (LOGIC_SUCESS.equals(resultCode))
					{
						Element content = root.getChild("content");
						errCode = content.getChildText("error_code"); 
	
						Document doc = DocumentHelper.parseText(rspXml);
						List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
						
						gprsFlux = getTotalGPRS(freeItemNodes).split(",");
						used = formatGPRSDetail(gprsFlux[1],1024); 
						totale = formatGPRSDetail(gprsFlux[0],1024);
						flagJIBAO = Integer.valueOf(gprsFlux[2]);// 季包标识 0：没有季包，1有季包；
						res.setFlagJIBAO(flagJIBAO);
						res.setResultCode(resultCode);
						res.setErrorCode(errCode);
					}
					else
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
				}
			} catch (CommunicateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fhi.setPkgTotal(totale);
		fhi.setPkgUsed(used);
	}
	/**
	 * 计算套餐内的全包总流量
	 * @param freeItemNodes
	 * @return
	 */
	private String getTotalGPRS(List<Node> freeItemNodes)
	{
		int total = 0;
		int used  = 0;
		int gprs_cumulate_type = 0; // 季包标识 0：没有季包，1有季包；
		int size = freeItemNodes.size();
		for(int i = 0;i < size;i++)
		{
			Node nodes = freeItemNodes.get(i);
			String type = nodes.selectSingleNode("gprs_product_type").getText().trim();//半包还是全包
			
			String gprs_cumulate_typeStr  =  nodes.valueOf("gprs_cumulate_type");
			if(null != gprs_cumulate_typeStr && "1".equals("gprs_cumulate_typeStr")){
				gprs_cumulate_type = 1;
			}
			
			String rate = nodes.selectSingleNode("gprs_rate_type").getText().trim();
			if("1".equals(rate))
			{
				total += Integer.parseInt(nodes.selectSingleNode("gprs_max_value").getText().trim());
				used += Integer.parseInt(nodes.selectSingleNode("gprs_cumulate_value").getText().trim());
			}
			
		}
		return total + ","+ used+","+gprs_cumulate_type;
	}
	
	/**
	 * 获取入参从开始时间到结束时间中的所有时间 （yyyyMM）
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
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
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040113Result res)
	{
		Element root = this.getElement(rspXml.getBytes());
		String resp_code = root.getChild("response").getChildText("resp_code");
		String resp_desc = root.getChild("response").getChildText("resp_desc");
		
		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		if(null != rspXml && !"".equals(rspXml))
		{
			if(!LOGIC_SUCESS.equals(resultCode) || null == root)
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
				return root;
			}
		}
		return null;
	}
}