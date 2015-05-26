package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY040021Result;

/**
 * 根据宽带账号查询
 * @author Tkk
 *
 */
public class QueryNetUserInfoOrFixedPhoneUserInfoResolver implements ITeletextResolver {
	
	private static Logger logger = Logger.getLogger(QueryNetUserInfoOrFixedPhoneUserInfoResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public QueryNetUserInfoOrFixedPhoneUserInfoResolver(){
		//获取Spring上下文
		ApplicationContext ctx = XWECPApp.SPRING_CONTEXT;
		
		wellFormedDAO = (WellFormedDAO) ctx.getBean("wellFormedDAO");
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception {
		if(result instanceof QRY040021Result){
			QRY040021Result userInfoResult = (QRY040021Result) result;
			
			//获取地市
			String cityBossNum = userInfoResult.getCity();
			
			//获取县
			String countyBossNum = userInfoResult.getCounty();
			
			try{
				if(!"".equals(cityBossNum) && !"".equals(countyBossNum)){
					//本地保存的值
					String [] cityNameAndNum = wellFormedDAO.getCityNameAndNum(cityBossNum).split(",");
					
					String [] countyNameAndNum = wellFormedDAO.getCityNameAndNum(countyBossNum).split(",");
					
					//进行转换
					userInfoResult.setCityName(cityNameAndNum[0]);
					userInfoResult.setCounty(countyNameAndNum[0]);
					userInfoResult.setCity(cityNameAndNum[1]);
					userInfoResult.setCounty(countyNameAndNum[1]);
				}
			}
			catch (Exception e) {
				logger.error(e , e);
			}
		}
	}

}
