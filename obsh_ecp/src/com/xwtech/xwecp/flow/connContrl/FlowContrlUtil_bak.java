package com.xwtech.xwecp.flow.connContrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.flow.ChannelFlowContainer;


public class FlowContrlUtil_bak 
{

	/**
	 * 验证当前渠道的并发量是否已经超出阀值
	 * @param channel 渠道编码
	 * @param maxConnNum 最大并发连接数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean checkCurrConnNums(String cmd, String clientAccessId, String channel, long maxConnNum)
	{
		boolean result = false;
		int connNums = 0;
		
		//获取存放各渠道并发连接的容器
		Map flowContainer = ChannelFlowContainer.getInstance().getFlowContainer();
		
		//如果当前渠道是第一次接入
		if(!flowContainer.containsKey(channel))
		{
			synchronized(flowContainer)
			{
				if(!flowContainer.containsKey(channel))
				{
					flowContainer.put(channel, new ArrayList());
				}
			}
		}
		
		List connLst = ((List)flowContainer.get(channel));
		
		//同步块
		synchronized(connLst)
		{
			connNums = connLst.size();
			//当前并发连接数小于等于阀值
			if(connNums <= maxConnNum)
			{
				int connNum = getConnNumByCmd(cmd);
				for(int i = 0; i < connNum; i ++)
				{
					connLst.add(clientAccessId);
				}
				result = true;
			}
//			//备用：如果已经超过阀值，设置100毫秒的缓冲时间，如果并发连接数降下去了再处理当前接口调用
//			else
//			{
//				try {
//					//等待100毫秒
//					connLst.wait(100);
//				} catch (InterruptedException e) {}
//				
//				connNums = connLst.size();
//				if(connNums <= maxConnNum)
//				{
//					connLst.add(clientAccessId);
//					result = true;
//				}
//			}
		}
		
		return result;
	}
	
	/**
	 * 删除并发连接
	 * @param channel
	 */
	@SuppressWarnings("unchecked")
	public static void removeCurrConn(String cmd, String clientAccessId, String channel)
	{
		List connLst = ((List)ChannelFlowContainer.getInstance().getFlowContainer().get(channel));
		
		int connNum = getConnNumByCmd(cmd);
		for(int i = 0; i < connNum; i ++)
		{
			connLst.remove(clientAccessId);
		}
	}
	
	/**
	 * 将具体的接口转化为连接数
	 * @param cmd
	 * @return
	 */
	public static int getConnNumByCmd(String cmd)
	{
		//读取配置文件或者查询数据库，获取每个接口对应的连接数
		return 1;
	}
	
	public static void main(String[] args)
	{
		
		for(int i = 0; i < 100; i ++)
		{
			final String channel1 = "channel1";
			new Thread()
			{
				public void run()
				{
					if(FlowContrlUtil_bak.checkCurrConnNums("cmd", Thread.currentThread().getName(), channel1, 5))
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						FlowContrlUtil_bak.removeCurrConn("cmd", Thread.currentThread().getName(), channel1);
					}
					else
					{
						System.out.println(Thread.currentThread().getName() + " channel1 is Max ConnNum.");
					}
				}
			}.start();
			
			final String channel2 = "channel2";
			new Thread()
			{
				public void run()
				{
					if(FlowContrlUtil_bak.checkCurrConnNums("cmd", Thread.currentThread().getName(), channel2, 5))
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						FlowContrlUtil_bak.removeCurrConn("cmd", Thread.currentThread().getName(), channel2);
					}
					else
					{
						System.out.println(Thread.currentThread().getName() + " channel2 is Max ConnNum.");
					}
				}
			}.start();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}	
