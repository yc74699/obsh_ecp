package com.xwtech.xwecp.web.action;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.xwtech.xwecp.log.PerformanceTracer;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.util.SavePortUtil;

/**
 * ECP入口
 * @author Administrator
 *
 */
public class ServiceInvocationAction extends MultiActionController
{
	private static Logger logger = Logger.getLogger(ServiceInvocationAction.class);
	
	private ServiceExecutor serviceExecutor = null;
	
	private String charset = "UTF-8";

	/**
	 * 默认处理方法
	 * @param request
	 * @param response
	 */
	public void defaultHandle(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			this.doService(request, response);
		}
		catch(Throwable e)
		{
			this.handleException(e, request, response);
		}
	}
	
	/**
	 * 异常处理
	 * @param e
	 * @param request
	 * @param response
	 */
	protected void handleException(Throwable e, HttpServletRequest request, HttpServletResponse response)
	{
		logger.error(e, e);
		this.responseError(request, response, e);
	}
	
	protected void responseError(HttpServletRequest request, HttpServletResponse response, Throwable cause)
	{
		throw new RuntimeException("not implied!", cause);
	}
	
	private void doService(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long beginTime = System.currentTimeMillis();
		pt.init();
		long n = pt.addBreakPoint();
		String xmlRequest = this.readXMLRequest(request);
		pt.trace("请求报文长度["+xmlRequest.length()+"]", n);
		logger.debug("request: \n" + xmlRequest);
		String xmlResponse = this.serviceExecutor.executeService(xmlRequest, this.getClientIP(request));
		pt.trace("响应报文长度["+xmlResponse.length()+"]", n);
		logger.debug("response: \n" + xmlResponse);
		pt.trace("ECP处理总时间...", beginTime);
		pt.log();
		this.writeXMLResponse(response, xmlResponse);
	}
	
	private String getClientIP(HttpServletRequest request)
	{
		String port = request.getLocalPort()+"";//獲取服務器端  端口
		String localAddr = request.getLocalAddr() + ":" + port;
		SavePortUtil.getInstance().port = port;
		SavePortUtil.getInstance().url = localAddr;
		
		return request.getRemoteAddr() +"#" + localAddr;
	}
	
	private void writeXMLResponse(HttpServletResponse response, String xmlResponse) throws Exception
	{
		ServletOutputStream sos = response.getOutputStream();
		sos.write(xmlResponse.getBytes(charset));
		sos.flush();
		sos.close();
	}
	
	private String readXMLRequest(HttpServletRequest request) throws Exception
	{
		ServletInputStream sis = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int read = 0;
		byte[] buf = new byte[4096];
		while((read = sis.read(buf)) > 0)
		{
			baos.write(buf, 0, read);
			baos.flush();
		}
		String xmlRequest = new String(baos.toByteArray(), charset);
		sis.close();
		baos.close();
		return xmlRequest;
	}

	public ServiceExecutor getServiceExecutor()
	{
		return serviceExecutor;
	}

	public void setServiceExecutor(ServiceExecutor serviceExecutor)
	{
		this.serviceExecutor = serviceExecutor;
	}

	public String getCharset()
	{
		return charset;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}
}