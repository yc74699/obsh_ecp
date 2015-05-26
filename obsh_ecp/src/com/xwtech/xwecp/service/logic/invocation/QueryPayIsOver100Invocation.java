package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY010034Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;

public class QueryPayIsOver100Invocation extends BaseInvocation implements	ILogicalService
{
	
	private static final Logger logger = Logger.getLogger(QueryPayIsOver100Invocation.class);
	private  static final List<String> channelList = new ArrayList<String>();
	
	static
	{
//		channelList.add("18");
		channelList.add("21");
		channelList.add("22");
		channelList.add("36");
		channelList.add("38");
		channelList.add("39");
		channelList.add("41");
		channelList.add("42");
		channelList.add("44");
		channelList.add("45");
		channelList.add("46");
		channelList.add("47");
		channelList.add("51");
		channelList.add("66");
		channelList.add("67");
		channelList.add("68");
		channelList.add("75");
		channelList.add("76");
		channelList.add("88");
		channelList.add("111");
		channelList.add("112");
		channelList.add("113");
		channelList.add("121");
		channelList.add("135");
		channelList.add("142");
		channelList.add("143");
		channelList.add("144");
		channelList.add("156");
		channelList.add("157");
		channelList.add("158");
		channelList.add("159");
		channelList.add("160");
		channelList.add("161");
		channelList.add("162");
		channelList.add("167");
		channelList.add("175");
		channelList.add("184");
		channelList.add("185");
		channelList.add("188");
		channelList.add("189");
		channelList.add("194");
		channelList.add("195");
		channelList.add("196");
		channelList.add("197");
		channelList.add("198");
		channelList.add("199");
		channelList.add("200");
		channelList.add("201");
		channelList.add("202");
		channelList.add("203");
		channelList.add("204");
		channelList.add("205");
		channelList.add("211");
		channelList.add("212");
		channelList.add("214");
		channelList.add("218");
		channelList.add("221");
		channelList.add("226");
		channelList.add("227");
		channelList.add("228");
		channelList.add("229");
		channelList.add("230");
		channelList.add("232");
		channelList.add("234");
		channelList.add("235");
		channelList.add("243");
		channelList.add("245");
		channelList.add("246");
		channelList.add("255");
		channelList.add("256");
		channelList.add("257");
		channelList.add("260");
		channelList.add("263");
		channelList.add("264");
		channelList.add("265");
		channelList.add("267");
		channelList.add("269");
		channelList.add("270");
		channelList.add("271");
		channelList.add("283");
		channelList.add("152");
		channelList.add("153");
		channelList.add("154");
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		
		QRY010034Result result = new QRY010034Result();
		result = getUserPayList(result,accessId,config,params);
		return result;
	}

	@SuppressWarnings("unchecked")
	private QRY010034Result getUserPayList(QRY010034Result result,
			String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		String rspXml = "";
		
		//组装参数，查询当前6个月
		String currMonth = DateTimeUtil.getTodayChar8();
		String monthHalfYear = get6MonthBefore(currMonth);
		params.add(new RequestParameter("eDate", currMonth));
		params.add(new RequestParameter("sDate", monthHalfYear));
		result.setTypeReuslt("0");
		try
		{
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("ac_agetacctbkseqjk_515", params), accessId, "ac_agetacctbkseqjk_515", super.generateCity(params)));
			if(null !=rspXml &&  !"".equals(rspXml))
			{
				Element root = checkReturnXml(rspXml,result);
				if(null != root)
				{
					List<Element> arecord_count = root.getChild("content").getChildren("arecord_count");
					if(null != arecord_count && 0 < arecord_count.size())
					{
						for(Element element : arecord_count)
						{
							String channel = element.getChild("acctbkpayseq_dt").getChildText("acctbkpayseq_channel_id");
							if(!StringUtil.isNull(channel) && QueryPayIsOver100Invocation.channelList.contains(channel))
							{
								String money = element.getChild("acctbkpayseq_dt").getChildText("acctbkpayseq_amount");
								int  moneyd = Integer.parseInt(money);
								if(9900 <= moneyd)
								{
									result.setTypeReuslt("1");
									break;
								}
								
							}
						}
					}
				}
				
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		return result;
	}

	private static String get6MonthBefore(String currMonth)
	{
		String year = currMonth.substring(0,4);
		String month = currMonth.substring(4,6);
		String day = currMonth.substring(6,8);
		int month6before = Integer.parseInt(month)-6;
		if(month6before <= 0)
		{
			int monthInt = (month6before + 12);
			if(monthInt < 10)
			{
				month = "0"+monthInt + "";
			}
			else
			{
				month = monthInt + "";
			}
			year = (Integer.parseInt(year)-1) + "";
		}
		else
		{
			month = "0"+month6before;
		}
		return year+month+day;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY010034Result res)
	{
		if(null != rspXml)
		{
			Element root = this.getElement(rspXml.getBytes());
			if(null != root)
			{
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
			}
		}
		
		return null;
	}
	public static void main(String[] args)
	{
		System.out.println(get6MonthBefore("20140522"));
	}
}
