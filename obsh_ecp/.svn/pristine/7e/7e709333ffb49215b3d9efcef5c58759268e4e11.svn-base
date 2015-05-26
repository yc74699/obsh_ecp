package com.xwtech.xwecp.flow.works.chains.nodes;

import com.xwtech.xwecp.flow.works.chains.AbstractFlowControl;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.ServiceExecutor;

/**
 * 接入权限验证
 * 
 * @author maofw
 * 
 */
public class CheckAccessPrivilegeNode extends AbstractFlowControl
{

	// 执行判断
	@Override
	public boolean execute(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo, Object o)
	{
		//获取外围渠道传的操作员的值   
		RequestData request = (RequestData)inputMessage.getData();
		InvocationContext contextParameter = request.getContext();
		if(contextParameter != null)
		{
		   //操作员ID
		   String operid = contextParameter.getContextParameter("oper_id");
		   //分销操作员ID
		   String fixed_oper_id = contextParameter.getContextParameter("fixed_oper_id");
		   
		   //外围渠道之前操作员的值没有传,未传或传空时在数据库中读取。营业厅操作员必须传操作号
		   if(null == fixed_oper_id || "".equals(fixed_oper_id))
		   {
			   //如果操作员ID为空的时候，网厅要保证CRM模板oper_id传值为空
			   if(null == operid || "".equals(operid) )
			   {
				  contextParameter.addContextParameter("oper_id", "");
			   }
			   //操作员ID不为空的时候，要记录操作员ID
			   else
			   {
				  contextParameter.addContextParameter("fixed_oper_id", "operid_"+operid);//操作员id规则operid_xxxx
			   }
		   }
		    //分销操作ID不为空的时候，但是操作员ID不为空的时候。去除掉营业员操作ID
		   else
		  {
			if(null != contextParameter.getContextParameter("oper_id") )
			{
				contextParameter.removeContextParameter("oper_id");
			}
		  }
		}
		
		if ( channelInfo == null ) { return false; }
		// 接入渠道的用户名
		String user = inputMessage.getHead().getUser();
		// 接入渠道的密码
		String password = inputMessage.getHead().getPwd();
		// 接入渠道编码
		// String channel = inputMessage.getHead().getChannel();

		// 是否需要鉴权IP
		boolean isNeedAuthIp = false;
		// 是否需要鉴权密码
		boolean isNeedAuthPwd = false;

		if ( "1".equals(channelInfo.getNeedAuthIp()) )
			isNeedAuthIp = true;
		if ( "1".equals(channelInfo.getNeedAuthPwd()) )
			isNeedAuthPwd = true;

		// IP验证
		if ( isNeedAuthIp && !serviceExecutor.getServiceCallerPrivilegeDAO().getCallerAcceptAddress(channelInfo.getChannelId()).contains(clientIp) ) { return false; }
		// 用户名密码验证
		if ( isNeedAuthPwd )
		{
			if ( !user.equals(channelInfo.getUserNum()) || !password.equals(channelInfo.getPassword()) ) { return false; }
		}
		return true;
	}

	// 无接入权限
	@Override
	public ServiceMessage failed(MessageHelper messageHelper, ServiceMessage inputMessage, Object o)
	{
		return messageHelper.createNoAccessPrivilegeResponseMessage(inputMessage);
	}

}
