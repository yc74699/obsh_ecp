package com.xwtech.xwecp.service.logic.resolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

/**
 * 查询用户增值业务信息
 * @author yuantao
 *
 */
public class GetSmsCallResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetSmsCallResolver.class);
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		String bizId = "";
		
		try
		{
 			QRY020001Result ret = (QRY020001Result)result;
			
			if (null != ret && null != ret.getResultCode())
			{
				for (RequestParameter p : param)
				{
					if (p.getParameterName().equals("bizId"))
					{
						bizId = String.valueOf(p.getParameterValue());
					}
				}
				
				//if ("-2727".equals(ret.getResultCode()))
				if ("-2727".equals(ret.getBossCode()))
				{
					dt = new GommonBusiness();
					dt.setId(bizId);
					dt.setState(1);
					ret.setResultCode("0");
					list = new ArrayList<GommonBusiness>();
					list.add(dt);
					ret.setGommonBusiness(list);
				}
				else
				{
					list = ret.getGommonBusiness();
					
					if (null != list && list.size() > 0)
					{
						for (GommonBusiness d : list)
						{
							d.setId(bizId);
						}
					}
				}
				
				for (GommonBusiness busi : ret.getGommonBusiness())
				{
					//增值业务订购关系增加生效方式 yangg add 20111024
					if (null != busi.getBeginDate() && !"".equals(busi.getBeginDate()))
					{
						
						if (busi.getBeginDate().equals(getFirstdayOfNextMonth()) || busi.getBeginDate().equals(getNextDayOfMonth())) {
							busi.setState(3);
						}else{
						
							busi.setState(2);
						}
					}
					//=============

					if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
					{
						busi.setState(4);
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	/*public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
                        List<RequestParameter> param) throws Exception
    {
		GommonBusiness gommonBusiness = null;
		List list = new ArrayList();
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			byte[] tmp = new String((String)bossResponse).getBytes();
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			Element root = doc.getRootElement();
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			
			String resp_code = p.matcher(root.getChild("response").getChildText("resp_code")).replaceAll("");
			String resp_desc = p.matcher(root.getChild("response").getChildText("resp_desc")).replaceAll("");
			ret.setResultCode(resp_code);
			ret.setErrorMessage(resp_desc);
			
			if ("0000".equals(resp_code))
			{
				Element csmscalldt = root.getChild("content").getChild("csmscalldt");
				gommonBusiness = new GommonBusiness();
				gommonBusiness.setId(csmscalldt.getChildText("smscall_deal_code"));
				gommonBusiness.setState(Integer.parseInt(csmscalldt.getChildText("smscall_state").trim()));
				gommonBusiness.setBeginDate(csmscalldt.getChildText("smscall_start_date"));
				gommonBusiness.setEndDate(csmscalldt.getChildText("smscall_end_date"));
				gommonBusiness.setReserve1(csmscalldt.getChildText("smscall_reserved1"));
				gommonBusiness.setReserve2(csmscalldt.getChildText("smscall_reserved2"));
				list.add(gommonBusiness);
				ret.setGommonBusiness(list);
			}
			else if ("-2727".equals(resp_code))
			{
				gommonBusiness = new GommonBusiness();
				gommonBusiness.setState(1);
				list.add(gommonBusiness);
				ret.setGommonBusiness(list);
			}
			
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}
    }*/
	private String getNextDayOfMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}
	
	private String getFirstdayOfNextMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}
}
