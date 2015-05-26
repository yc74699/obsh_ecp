package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IYingXingCardDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY050076Result;

public class QueryIsYingXingCardInvocation extends BaseInvocation implements ILogicalService 
{
	private static final Logger logger = Logger.getLogger(QueryIsYingXingCardInvocation.class);
	
	//迎新号码DAO
	private IYingXingCardDao yingXingDao;

	public IYingXingCardDao getYingXingDao() {
		return yingXingDao;
	}

	public void setYingXingDao(IYingXingCardDao yingXingDao) {
		this.yingXingDao = yingXingDao;
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		List<String> list;
		QRY050076Result result = new QRY050076Result();
		RequestParameter parameterNum = getParameter(params, "phoneNum");
		 String phoneNum = "";
		 if(null != parameterNum)
		 {
			 phoneNum = (String) parameterNum.getParameterValue();
		 }
		 RequestParameter areaNumP = getParameter(params, "areaNum");
		 String areaNum = "";
		 if(null != parameterNum)
		 {
			 areaNum = (String) areaNumP.getParameterValue();
		 }
		result.setIsYingXingNum("0");
		
		try {
			ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
			yingXingDao = (IYingXingCardDao) (springCtx.getBean("yingXingDao"));
			list = yingXingDao.getYingXingNum(phoneNum, areaNum);
			
			if(searchYXNum(list,Long.parseLong(phoneNum)))
			{
				result.setIsYingXingNum("1");
			}
			result.setResultCode("0");
			result.setErrorMessage("");
		} 
		catch (DAOException e)
		{
			// TODO Auto-generated catch block
			result.setResultCode("1");
			result.setErrorMessage("");
			e.printStackTrace();
			logger.error(e);
		}
		
		return result;
	}
	/**
	 * 二分查找出对应的元素 根据给定的值
	 * @return
	 */
	private boolean searchYXNum(List<String> phoneList,long phone)
	{
     int low = 0;   
     int high = phoneList.size()-1;   
        while(low <= high)
        {   
            int middle = (low + high)/2;
            
            if(phone == Long.parseLong(phoneList.get(middle)))
            {   
                return true;
            }
            else if(phone < Long.parseLong(phoneList.get(middle)))
            {   
                high = middle - 1;
            }
            else
            {   
                low = middle + 1;
            }  
        }  
		return false;
	}
}
