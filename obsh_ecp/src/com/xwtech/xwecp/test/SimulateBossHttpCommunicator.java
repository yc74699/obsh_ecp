package com.xwtech.xwecp.test;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.HttpCommunicator;


public class SimulateBossHttpCommunicator extends HttpCommunicator
{
	private static final Logger logger = Logger.getLogger(SimulateBossHttpCommunicator.class);

	@Override
	public String send(String data) throws CommunicateException
	{
		//请求BOSS时只传CMD, 可以在boss-teletext-util里面把这个搞一下. 
		String cmd = data;
		File responseFile = new File("D:\\MyProjects\\网营产品化\\boss-simulate\\ret-"+cmd+".xml");
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(responseFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int nRead = 0;
			while((nRead = fis.read(buff)) > 0)
			{
				baos.write(buff, 0, nRead);
				baos.flush();
			}
			String s = new String(baos.toByteArray(), "UTF-8");
			logger.info((char)s.getBytes()[0]);
			logger.info((char)s.getBytes()[1]);
			return s;
		}
		catch (IOException e)
		{ 
			logger.error(e, e);
		}
		finally
		{
			if(fis != null)
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
					logger.error(e, e);
				}
			}
		}
		return null;
	}
}
