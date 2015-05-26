package com.xwtech.xwecp.flow.works.chains.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.flow.ChannelFlowContainer;
import com.xwtech.xwecp.flow.works.chains.AbstractFlowControl;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.pojo.ChannelLimitInfo;
import com.xwtech.xwecp.pojo.IterfaceCapacity;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 流量控制节点
 * 
 * @author maofw
 * 
 */
public class CheckChannelFlowNode extends AbstractFlowControl
{
	// 执行判断
	@Override
	public boolean execute(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo, Object o)
	{
		//System.out.println("---------" + this.getClass() + " execute!!!");
		if ( channelInfo == null ) { return false; }
		// 接入渠道编码
		// String channel = msg.getHead().getChannel();
		String clientAccessId = inputMessage.getHead().getSequence();
		String cmd = inputMessage.getHead().getCmd();
		if ( "1".equals(channelInfo.getNeedFlowContrl()) )
		{
			long maxSize = StringUtil.isNull(channelInfo.getMaxConnNum()) ? 0 : Long.parseLong(channelInfo.getMaxConnNum());
			// 获得当日 渠道流量详情配置情况
			List<ChannelLimitInfo> list = serviceExecutor.getServiceCallerPrivilegeDAO().getChannelLimitInfoList(channelInfo.getChannelId(), DateTimeUtil.getTodayDay());
			if ( list != null && list.size() > 0 )
			{
				// 获得当前时间阶段的配置详情信息
				ChannelLimitInfo channelLimitInfo = this.searChannelLimitInfo(list, DateTimeUtil.getTodayChar14().substring(8), 0, list.size() - 1);
				if ( channelLimitInfo != null )
				{
					// 设置為时间段配置的最大连接数
					maxSize = channelLimitInfo.getMaxConnums();
				}
			}
			if ( !this.checkCurrConnNums(serviceExecutor, cmd, clientAccessId + "_" + ((Long) o), channelInfo.getChannelId(), maxSize) ) { return false; }
		}

		return true;
	}

	// 失败执行方法
	@Override
	public ServiceMessage failed(MessageHelper messageHelper, ServiceMessage inputMessage, Object o)
	{
		// 接入渠道编码
		//String channel = inputMessage.getHead().getChannel();
		//System.out.println("over flow!!!"+((List)ChannelFlowContainer.getInstance().getFlowContainer().get(channel)).size());
		//System.out.println("over flow!!!");
		return messageHelper.createOverFlowResponseMessage(inputMessage);
	}
	// 成功执行之后是否执行
	public boolean isExecuteAfterSuccess()
	{
		return true;
	}
	// 成功执行之后 执行的方法 执行移除list操作 解除本次连接占用
	public void executeAfterSuccess(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, ChannelInfo channelInfo, Object o)
	{
		//System.out.println(this.getClass()+" removeCurrConn!");
		if ( "1".equals(channelInfo.getNeedFlowContrl()) )
		{
			// 接入渠道编码
			String channel = inputMessage.getHead().getChannel();
			String cmd = inputMessage.getHead().getCmd();
			String clientAccessId = inputMessage.getHead().getSequence();
			this.removeCurrConn(serviceExecutor, cmd, clientAccessId + "_" + ((Long) o), channel);
		}
	}

	// 创建自定义对象(当前时间)
	public Object createCustomObject()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 获得渠道流量限制详情对象
	 * 
	 * @param msg
	 * @return
	 */
	private ChannelLimitInfo searChannelLimitInfo(List<ChannelLimitInfo> list, String currentTime, int start, int end)
	{
		// System.out.println("--start:" + start + "-end:" + end);
		ChannelLimitInfo channelLimitInfo = null;

		if ( start <= end )
		{
			int mid = (start + end) / 2;
			// System.out.println("--mid:" + mid);
			// 获得当前时间段的流量配置
			channelLimitInfo = list.get(mid);
			// 比较时间段范围
			// 当前时间比开始时间小 则查找下一个对象
			if ( !StringUtil.isNull(channelLimitInfo.getStartTime()) && (channelLimitInfo.getStartTime().compareTo(currentTime) > 0) )
			{
				channelLimitInfo = searChannelLimitInfo(list, currentTime, start, mid - 1);
			}
			else if ( !StringUtil.isNull(channelLimitInfo.getEndTime()) && (channelLimitInfo.getEndTime().compareTo(currentTime) < 0) )
			{
				channelLimitInfo = searChannelLimitInfo(list, currentTime, mid + 1, end);
			}

		}

		return channelLimitInfo;
	}

	/**
	 * 验证当前渠道的并发量是否已经超出阀值
	 * 
	 * @param channel
	 *            渠道编码
	 * @param maxConnNum
	 *            最大并发连接数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkCurrConnNums(ServiceExecutor serviceExecutor, String cmd, String clientAccessId, String channel, long maxConnNum)
	{
		boolean result = false;

		// 获取存放各渠道并发连接的容器
		Map flowContainer = ChannelFlowContainer.getInstance().getFlowContainer();

		// 如果当前渠道是第一次接入
		if ( !flowContainer.containsKey(channel) )
		{
			synchronized (flowContainer)
			{
				if ( !flowContainer.containsKey(channel) )
				{
					flowContainer.put(channel, new ArrayList());
				}
			}
		}

		List connLst = ((List) flowContainer.get(channel));

		int connNum = getConnNumByCmd(serviceExecutor, cmd);
		// 同步块
		synchronized (connLst)
		{
			// 当前并发连接数小于等于阀值
			if ( (connLst.size() + connNum) <= maxConnNum )
			{
				for (int i = 0; i < connNum; i++)
				{
					connLst.add(clientAccessId);
				}
				result = true;
			}
		}

		return result;
	}

	/**
	 * 删除并发连接
	 * 
	 * @param channel
	 */
	@SuppressWarnings("unchecked")
	private void removeCurrConn(ServiceExecutor serviceExecutor, String cmd, String clientAccessId, String channel)
	{
		List connLst = ((List) ChannelFlowContainer.getInstance().getFlowContainer().get(channel));

		// System.out.println("remove before:"+connLst.toString());
		int connNum = getConnNumByCmd(serviceExecutor, cmd);
		for (int i = 0; i < connNum; i++)
		{
			connLst.remove(clientAccessId);
		}
		// System.out.println("remove before:"+connLst.toString());
	}

	/**
	 * 将具体的接口转化为连接数
	 * 
	 * @param cmd
	 * @return
	 */
	private int getConnNumByCmd(ServiceExecutor serviceExecutor, String cmd)
	{
		// 读取配置文件或者查询数据库，获取每个接口对应的连接数
		IterfaceCapacity iterfaceCapacity = serviceExecutor.getServiceCallerPrivilegeDAO().getIterfaceCapacity(cmd);
		return iterfaceCapacity == null ? 1 : iterfaceCapacity.getNums();
	}

	public static void main(String[] args)
	{
		List<ChannelLimitInfo> list = new ArrayList<ChannelLimitInfo>();
		for (int i = 0; i < 1; i++)
		{
			ChannelLimitInfo channelLimitInfo = new ChannelLimitInfo();
			channelLimitInfo.setStartTime((i < 10 ? "0" : "") + i + "0000");
			channelLimitInfo.setEndTime((i < 10 ? "0" : "") + i + "5959");
			channelLimitInfo.setMaxConnums(i);
			list.add(channelLimitInfo);

			System.out.println(channelLimitInfo.getStartTime() + "-" + channelLimitInfo.getEndTime());
		}
		String str = "223300";// DateTimeUtil.getTodayChar14().substring(8);

		CheckChannelFlowNode checkChannelFlowNode = new CheckChannelFlowNode();
		ChannelLimitInfo channelLimitInfo = checkChannelFlowNode.searChannelLimitInfo(list, str, 0, list.size() - 1);

		System.out.println("---:" + str);
		long s = System.currentTimeMillis();
		System.out.println("---:" + channelLimitInfo.getStartTime() + "-" + channelLimitInfo.getEndTime() + "-" + channelLimitInfo.getMaxConnums());
		System.out.println("---times:" + (System.currentTimeMillis() - s));

	}

}
