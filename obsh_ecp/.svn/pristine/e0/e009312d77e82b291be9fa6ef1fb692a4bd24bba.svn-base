package com.xwtech.xwecp.flow.works.chains.nodes;


import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.flow.works.chains.AbstractFlowControl;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.pojo.NumberSegment;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.web.XWECPWebApp;

import java.util.List;

/**
 * 地市CRM系统崩溃控制访问节点
 * 
 * @author TG
 * 
 */
public class CheckLCRMRegionNode extends AbstractFlowControl
{
	// 执行判断
	@Override
	public boolean execute(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo, Object o)
	{
		// 接入渠道的渠道名
		String channel = inputMessage.getHead().getChannel();
		// 地市编码
		String region = inputMessage.getHead().getUserCity();
		//获取手机号码
		String phoneNum = inputMessage.getHead().getUserMobile();
		
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		WellFormedDAO wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		//获取屏蔽的地市集合
		//List<String> regionList = wellFormedDAO.getSystemEmerRegionInfo(channel);
		List<String> regionList = wellFormedDAO.getCRMRegionChannel(channel);
		
		//如果地市集合为空，那么就没有被屏蔽服务的地市
		if( null != regionList && regionList.size() > 0)
		{
			//如果地市编码集合中包含当前地市，那么就报错
			if(regionList.contains(region))
			{
				return false;
			}
			else
			{
				//如果当前获取报文里面的地市编码为空的话就根据屏蔽的地市编码查找号码段来和当前的手机号码匹配
				if(null == region || "".equals(region))
				{
					List<NumberSegment> phoneList = XWECPWebApp.NUMBERSEGMENTS.get(phoneNum.substring(0,3));

					if(null != phoneList && phoneList.size() > 0)
					{
						NumberSegment numberSegment = searchNumberSegment(phoneList,Integer.parseInt(phoneNum.substring(0,7)));
						//号码段
						if(null != numberSegment && regionList.contains(numberSegment.getCity()+""))
						{
							//inputMessage.getHead().setUserCity(numberSegment.getCity()+"");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 二分查找出对应的元素 根据给定的值
	 * @return
	 */
	private NumberSegment searchNumberSegment(List<NumberSegment> phoneList,int phone)
	{
     int low = 0;   
     int high = phoneList.size()-1;   
        while(low <= high)
        {   
            int middle = (low + high)/2;
            
            if(phone == phoneList.get(middle).getNumSegment())
            {   
                return phoneList.get(middle);
            }
            else if(phone < phoneList.get(middle).getNumSegment())
            {   
                high = middle - 1;
            }
            else
            {   
                low = middle + 1;
            }  
        }  
		return null;
	}
	
	public ServiceMessage failed(MessageHelper messageHelper, ServiceMessage inputMessage, Object o)
	{
		return messageHelper.createNoCRMRegionResponseMessage(inputMessage);
	}
}