package com.xwtech.xwecp.util;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 包装http请求的工具类
 * @author 邵琪
 * 创建日期 2009-12-28
 */
public class HttpClientUtil {
	
	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

	private HttpClient httpClient = new HttpClient();
	private String mucEncode = "GB2312";
	private String timeOut = "10000";
	

	/**
	 * 获取包装类实例，使用此方法前请确认配置文件里有如下些包含前缀配置信息：“XXX.encode” “XXX.host” “XXX.timeout”（可选，默认3000毫秒）
	 * @param type 在项目properties里配置的选项的前缀
	 * @return
	 */
	public static HttpClientUtil getInstance(){
		HttpClientUtil HttpClientUtil = new HttpClientUtil();
		HttpClientUtil.httpClient = new HttpClient();
		String timeout = HttpClientUtil.timeOut;
		//在没取到超时参数时默认用3秒
		if(StringUtils.isEmpty(timeout) || !StringUtils.isNumeric(timeout))
			timeout = "3000";
//		HttpClientParams params = HttpClientUtil.httpClient.getParams();
//		params.setSoTimeout(Integer.parseInt(timeout));
//		params.setConnectionManagerTimeout(Integer.parseInt(timeout));
		HttpConnectionManagerParams managerParams = HttpClientUtil.httpClient.getHttpConnectionManager().getParams();
		managerParams.setSoTimeout(Integer.parseInt(timeout));
		managerParams.setConnectionTimeout(Integer.parseInt(timeout));
		return HttpClientUtil;
	}
	
	/**
	 * 发送http请求
	 * @param requestUrl 请求的路径，域名的前缀信息请在项目配置文件的“XXX.host”中配置
	 * @param requestBody 请求的内容
	 * @return
	 * @throws Exception
	 */
	public String sendData(String url,String requestBody) throws Exception{
		PostMethod postMethod = new PostMethod(url);
		try{
			RequestEntity entity = new StringRequestEntity(requestBody, "text/html", mucEncode);
			postMethod.setRequestEntity(entity);
			int statusCode = httpClient.executeMethod(postMethod);
			if(statusCode == 200){
				//请求正常
				byte[] responseBodyByte = postMethod.getResponseBody();
				//返回相应的数据
				return new String(responseBodyByte,mucEncode);
			}else{
				logger.error("HttpClient 请求错误！错误代码："+statusCode);
				throw new Exception("HttpClient 请求错误！错误代码："+statusCode);
			}
		}catch (Exception e) {
			logger.error(e, e);
			throw new Exception("HttpClient 请求错误！",e);
		}
	}
	
	
	/**
	 * 获取客户端IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String strUserIp="127.0.0.1";
		/****/
		//Apache 代理 解决IP地址问题
		strUserIp=request.getHeader("X-Forwarded-For");
	    if(strUserIp == null || strUserIp.length() == 0 || "unknown".equalsIgnoreCase(strUserIp)) {
	    	strUserIp=request.getHeader("Proxy-Client-IP");
	    }
	    if(strUserIp == null || strUserIp.length() == 0 || "unknown".equalsIgnoreCase(strUserIp)) {
	    	strUserIp=request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(strUserIp == null || strUserIp.length() == 0 || "unknown".equalsIgnoreCase(strUserIp)) {
	    	strUserIp=request.getRemoteAddr();
	    }
	    
	    //解决获取多网卡的IP地址问题
	    if (strUserIp != null) {
	    	strUserIp = strUserIp.split(",")[0];
	    }else{
	    	strUserIp="127.0.0.1";
	    }

	    //解决获取IPv6地址 暂时改为本机地址模式
	    if (strUserIp.length()>16){strUserIp="127.0.0.1";}

	    return strUserIp;
		
		//没有IP Apache 代理 解决IP地址问题
//		strUserIp=request.getRemoteAddr();
//	    if (strUserIp != null) {strUserIp = strUserIp.split(",")[0];}
//	    return strUserIp;
	}
	
	public static String format(Document document) {
		StringWriter writer = new StringWriter();

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GB2312");
		XMLWriter xmlwriter = new XMLWriter(writer, format);
		try {
			xmlwriter.write(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return writer.toString();
	}

}
