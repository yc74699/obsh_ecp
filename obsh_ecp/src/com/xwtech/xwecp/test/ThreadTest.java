package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBusinessService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBusinessServiceClientImpl;

public class ThreadTest
{
	//允许并发数
	private static int thread_num = 3;
	//客户端请求数
	private static int client_num = 1;
	private static Map keywordMap = new HashMap();

	public static void main(String[] args)
	{

		// 初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/xw_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		// 逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913032424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913032424");
		ic.addContextParameter("ddr_city", "17");

		ic.addContextParameter("user_id", "1419200008195160"); // 2056200011182291

		lic.setContextParameter(ic);

		final IQueryBusinessService co = new QueryBusinessServiceClientImpl();
		/*
		long s = System.currentTimeMillis() ;
		for (int i = 0; i < 1; i++)
		{
			try
			{
				co.queryBusiness("13913032424", 2, "FXYW");
			}
			catch (LIException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - s);
		*/
		
		int size = keywordMap.size();
		// TODO Auto-generated method stub
		ExecutorService exec = Executors.newCachedThreadPool();
		// 50个线程可以同时访问
		final Semaphore semp = new Semaphore(thread_num);
		// 模拟2000个客户端访问
		for (int index = 0; index < client_num; index++)
		{
			final int NO = index;
			Runnable run = new Runnable()
			{
				public void run()
				{
					try
					{
						// 获取许可
						semp.acquire();
						System.out.println("Thread:" + NO);
						try
						{
							co.queryBusiness("13913032424", 2, "FXYW");
						}
						catch (LIException e)
						{
							e.printStackTrace();
						}
						System.out.println("第：" + NO + " 个");
						semp.release();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			};
			exec.execute(run);
		}
		// 退出线程池
		exec.shutdown();
		 
	}

	private static String getRandomSearchKey(final int no)
	{
		String ret = "";
		int size = keywordMap.size();
		// int wanna = (int) (Math.random()) * (size - 1);
		ret = (keywordMap.entrySet().toArray())[no].toString();
		ret = ret.substring(0, ret.lastIndexOf("="));
		System.out.println("\t" + ret);
		return ret;
	}
}
