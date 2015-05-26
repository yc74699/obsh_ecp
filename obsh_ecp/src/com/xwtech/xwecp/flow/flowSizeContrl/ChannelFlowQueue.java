package com.xwtech.xwecp.flow.flowSizeContrl;

import java.util.ArrayList;
import java.util.List;

public class ChannelFlowQueue 
{
	private List<ChannelFlowInfo> flowQueue = null;
	
	private static ChannelFlowQueue self = new ChannelFlowQueue();
	
	private ChannelFlowQueue()
	{
		flowQueue = new ArrayList<ChannelFlowInfo>();
	}
	
	public static ChannelFlowQueue getInstance()
	{
		return self;
	}
	
	public void push(ChannelFlowInfo flowInfo)
	{
		flowQueue.add(flowInfo);
	}
	
	public void push(List<ChannelFlowInfo> v)
	{
		flowQueue.addAll(v);
	}
	
	public List<ChannelFlowInfo> pop()
	{
		List<ChannelFlowInfo> tempFlowQueue = new ArrayList<ChannelFlowInfo>();
		//synchronized(flowQueue)
		//{
			//此处用clear可能导致会删除在addAll之后添加到队列里面的数据
			//tempFlowQueue.addAll(flowQueue);
			//flowQueue.clear();
		//}
		
		synchronized(flowQueue)
		{
			//用这种方式，可以确保不会丢掉数据
			int queuesize = flowQueue.size();
			for(int i = 0; i < queuesize; i ++)
			{
				tempFlowQueue.add(flowQueue.remove(0));
			}
		}
		
		return tempFlowQueue;
	}

	public List<ChannelFlowInfo> getFlowQueue() {
		return flowQueue;
	}

	public void setFlowQueue(List<ChannelFlowInfo> flowQueue) {
		this.flowQueue = flowQueue;
	}
	
	public static void main(String[] args)
	{
		List<String> a = new ArrayList<String>();
		List<Object> b = new ArrayList<Object>();
		
		a.add("1");
		a.add("2");
		a.add("3");
		a.add("4");
		
		a.add("1");
		a.add("1");
		a.add("1");
		a.add("1");
		a.add("1");
		a.add("1");
		a.add("1");
		
		//b.addAll(a);
		b.add("1");
//		a.clear();
		
		for(int i = 0; i < a.size(); i ++)
		{
			System.out.println(a.get(i));
		}
		
		a.removeAll(b);
		//a.remove("1");
		System.out.println("=============");
		
		for(int i = 0; i < a.size(); i ++)
		{
			System.out.println(a.get(i));
		}
//		System.out.println("=============");
//		
//		for(int i = 0; i < b.size(); i ++)
//		{
//			System.out.println(b.get(i));
//		}
		
		
	}
	
}
