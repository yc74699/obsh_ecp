package com.xwtech.xwecp.service.logic.resolver;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

/**
 * 查询wifi套餐关联号码
 * @author yg
 *
 */
public class QryMifiRelationNumResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QryMifiRelationNumResolver.class);
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
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
				

				list = ret.getGommonBusiness();
				
				if (null != list && list.size() > 0)
				{
					for (GommonBusiness d : list)
					{
						d.setId(bizId);
					}
				}
				
				
				for (GommonBusiness busi : ret.getGommonBusiness())
				{
					if(null != busi.getReserve1()  && !"".equals(busi.getReserve1())){
						//解析prod_attr，格式：RelSubsID=141920014891401#RelNumber=13813800000#
						String prod_attr = busi.getReserve1();
						String[] arrProd = prod_attr.split("#");
						if(null != arrProd && arrProd.length != 2){
							busi.setReserve2("-");
						}else{
							if(arrProd.length == 2){
								for(int i=0;i<arrProd.length;i++){
									String[] arrTemp =  arrProd[i].split("=");
									if(arrTemp.length ==2){
										if("RelNumber".equals(arrTemp[0])){
											busi.setReserve2(arrTemp[1]);
											break;
										}
									}else{
										busi.setReserve2("-");
									}
								}
							}else{
								busi.setReserve2("-");
							}
						}
					}
				}

				}
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }

}
