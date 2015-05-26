package com.xwtech.xwecp.web.serverHttp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.dom4j.Document;
import org.xml.sax.InputSource;

import com.xwtech.xwecp.communication.CommunicateException;

public class SendHttpMall {
	public static final String ENCODING = "UTF-8";

	//private static final String sendUrl = "http://10.32.65.111:8080/mall_out_service/goodsWTShelf/goodsWTShelf.do?method=getSynGoodsWTShelfData";

	private String remoteURL;
	
	//通过网络与服务器建立连接的超时时间
	private int connectionTimeout = 10000;
	
	//Socket读数据的超时时间
	private int soTimeout = 45000;
	
	//全局最多保持的连接数
	private int maxThreads = 100;
	
	//每主机可保持连接的连接数，默认情况不指定主机，所以所有连接都是共用的默认主机
	private int maxConnectionsPerHost = 100;
	
	private String encoding = "UTF-8";
	
	private String keepAlive = "false";
	
	private String usePool = "false";
	
	private Map<String, HttpClient> httpClientMap = new HashMap<String, HttpClient>();
	
	private static final String DEFAULT_CHANNEL = "DEFAULT_CHANNEL";
	
	/**
	 *
	 * @param httpurl
	 * @param params
	 *            参数
	 * @return
	 */
	public  String sendPost(String userMobile,String brandNum,String areaNum,String userId,String consume,String methodName,String placeId,String channelId)
	{
		String xml = "<?xml version='1.0' encoding='utf-8'?><operation_in><content><tableServer><serverInfo>";
		xml += "<methodName>"+methodName+"</methodName>";
		xml += "<mobile>"+userMobile+"</mobile>";
		xml += "<brandNum>"+brandNum+"</brandNum>";
		xml += "<areaNum>"+areaNum+"</areaNum>";
		xml += "<userId>"+userId+"</userId>";
		xml += "<consume>"+consume+"</consume>";
		xml += "<placeId>"+placeId+"</placeId>";
		xml += "<channelId>"+channelId+"</channelId>";
		xml += "</serverInfo></tableServer></content></operation_in>";
		String channel = "obsh";
		String outXml = "";
		try {
			outXml = this.send(xml,channel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outXml;
	}
	
	public static void main(String[] args) {
		SendHttpMall s = new SendHttpMall();
		//String out = s.sendPost("","NJDQ","");
	}
	
	
	public  String send(String data, String channel) throws CommunicateException
	{
		HttpClient httpClient = this.createHttpClient(channel);
		String sendUrl = "http://10.32.238.218:80/js_bigtable_service/bigtable";
		PostMethod post = new PostMethod(sendUrl);
		InputStream is = null;
		ByteArrayOutputStream in = null;
		ByteArrayOutputStream out = null;
		try
		{
			in = new ByteArrayOutputStream();
			in.write(data.getBytes(encoding));
			in.flush();
			RequestEntity entity = new ByteArrayRequestEntity(in.toByteArray());
			post.setRequestEntity(entity);
			
			int ret = httpClient.executeMethod(post);
			if(ret == 200)
			{
				is = post.getResponseBodyAsStream();
				out = new ByteArrayOutputStream();
				int nRead = 0;
				byte[] buf = new byte[10240];
				while((nRead=is.read(buf)) > 0)
				{
					out.write(buf, 0, nRead);
					out.flush();
				}
				return out.toString(encoding);		
			}
		}
		catch (Exception e)
		{

			e.printStackTrace();
			throw new CommunicateException(e);
		}
		finally
		{
			try
			{
				if(is != null)
				{
					is.close();
				}
				if(in != null)
				{
					in.close();
				}
				if(out != null)
				{
					out.close();
				}
			}
			catch(Exception e)
			{
			}
			post.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return null;
	}
	
	
	protected HttpClient createHttpClient(String channel)
	{
		if(!httpClientMap.containsKey(channel))
		{
			synchronized(httpClientMap)
			{
				if(!httpClientMap.containsKey(channel))
				{
					MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
					HttpConnectionManagerParams params = connectionManager.getParams();
					params.setConnectionTimeout(connectionTimeout);
					params.setSoTimeout(soTimeout);
					params.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
					params.setMaxTotalConnections(maxThreads);
					
					HttpClient httpClient = new HttpClient(connectionManager);
					HostConfiguration hostConf = httpClient.getHostConfiguration();
					//connectionManager.getParams().setMaxConnectionsPerHost(hostConf, maxThreads);
					ArrayList<Header> headers = new ArrayList<Header>();
					headers.add(new Header("Accept-Language","en-us,zh-cn,zh-tw,en-gb,en;q=0.7,*;q=0.3"));
					headers.add(new Header("Accept-Charset","big5,gb2312,gbk,utf-8,ISO-8859-1;q=0.7,*;q=0.7"));
					headers.add(new Header("Accept","text/html,application/xml;q=0.9,application/xhtml+xml,text/xml;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"));
					headers.add(new Header("Accept-Encoding", "x-gzip,gzip"));
					if("true".equalsIgnoreCase(keepAlive))
					{
						headers.add(new Header("Connection", "keep-alive"));
					}
					else
					{
						headers.add(new Header("Connection", "close"));
					}
					hostConf.getParams().setParameter("http.default-headers", headers);
					
					httpClientMap.put(channel, httpClient);
				}
			}
		}
		HttpClient httpClient = (HttpClient)httpClientMap.get(channel);
		return httpClient;
	}
	
	
	private static HttpURLConnection getHttpPostConn(URL url) throws IOException
	{
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		// 设置是否向connection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true
		conn.setDoOutput(true);
		// Post 请求不能使用缓存
		conn.setUseCaches(false);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(60000);
		// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
		conn.setInstanceFollowRedirects(true);
		conn.setRequestProperty("Content-Type", "text/xml;charset=" + ENCODING);
		return conn;

	}
}
