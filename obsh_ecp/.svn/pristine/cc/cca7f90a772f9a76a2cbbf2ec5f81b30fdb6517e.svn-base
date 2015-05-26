package com.xwtech.xwecp.communication;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.QryInterfaceConnetionTimeDAOImpl;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * http请求方法公共类
 * @param 
 * @return
 */
public class HttpCommunicator implements ICommunicator
{
	private static final Logger logger = Logger.getLogger(HttpCommunicator.class);
	
	private String remoteURL;
	
	//通过网络与服务器建立连接的超时时间
	private int connectionTimeout = 5000;
	
	//Socket读数据的超时时间
	private int soTimeout = 15000;
	
	//全局最多保持的连接数
	private int maxThreads = 100;
	
	//每主机可保持连接的连接数，默认情况不指定主机，所以所有连接都是共用的默认主机
	private int maxConnectionsPerHost = 200;
	
	//获取每个接口连接时间
	private QryInterfaceConnetionTimeDAOImpl qryInterfaceConnectionTimeDAOImpl;
	
	private String encoding = "UTF-8";
	
	private String keepAlive = "false";
	
	private String usePool = "false";
	
	private Map<String, HttpClient> httpClientMap = new HashMap<String, HttpClient>();
	
	private static final String DEFAULT_CHANNEL = "DEFAULT_CHANNEL";
	
	private static final Map<Integer,String> statusCode = new HashMap<Integer,String>(){
		{
			put(400,"错误请求");
			put(401,"未授权");
			put(403,"禁止");
			put(404,"未找到");
			put(408,"请求超时");
			put(500,"服务器内部错误");
			put(501,"尚未实施");
			put(502,"错误网关");
			put(503,"服务不可用");
			put(504,"网关超时");
			put(505,"http版本不受支持");
			
		}
	};
	public void initialize()
	{
		this.createHttpClient(DEFAULT_CHANNEL);
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
	
	public byte[] send(byte[] data) throws CommunicateException
	{
		return this.send(data, DEFAULT_CHANNEL);
	}
	
	public byte[] send(byte[] data, String channel) throws CommunicateException
	{
		String s = ""; 
		try
		{
			s = new String(data, encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error(e, e);
		}
		String retStr = this.send(s, channel);
		byte ret[] = new byte[0];
		try
		{
			ret = retStr.getBytes(encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error(e, e);
		}
		return ret;
	}

	public String send(String data) throws CommunicateException
	{
		return this.send(data, DEFAULT_CHANNEL);
	}
	
	/**
     * 提交url通过post
     * 
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public String sendPost(Map<String, Object> map ,String url)
        throws Exception
    {

        PostMethod postMethod = null;

        String result = "";
        try
        {
            postMethod = new PostMethod(url);
            if (map != null)
            {
                Iterator it = map.entrySet().iterator();
                Entry entry = null;
                while (it.hasNext())
                {
                    entry = (Entry)it.next();
                    if (entry.getKey() == null || entry.getValue() == null)
                    {
                        throw new Exception("键值不能为null" + entry.getKey() + "=" + entry.getValue());
                    }
                    postMethod.addParameter((String)entry.getKey(), (String)entry.getValue());
                }
            }
            HttpClient httpClient = new HttpClient();
            httpClient.setConnectionTimeout(connectionTimeout);
            httpClient.getParams().setSoTimeout(soTimeout);
            httpClient.getParams().setContentCharset("UTF-8");
            int code = httpClient.executeMethod(postMethod);
            if (code == 404)
            {
                throw new Exception("返回404错误！");
            }
            else if (code == 500)
            {
                throw new Exception("返回500错误！");
            }
            else if (code == 200)
            {
                result = postMethod.getResponseBodyAsString();
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (postMethod != null)
            {
                postMethod.releaseConnection();
            }
        }
        return result;
    }
	// 调本地
	public String send(String data, String channel) throws CommunicateException
	{
//		//获取接口标识符
//		String processCode = XMLUtil.getChildTextEx(data,"process_code");
//		int connectTime = 0;
//		try {
//			connectTime = qryInterfaceConnectionTimeDAOImpl.qryInterfaceConnetionTime(processCode);
//			if(connectTime != 0){
//				this.connectionTimeout = connectTime;
//			}
//		} catch (Exception e1) {
//			logger.error(e1);
//		}
		
		HttpClient httpClient = this.createHttpClient(channel);
			
		PostMethod post = new PostMethod(this.remoteURL);
		InputStream is = null;
		ByteArrayOutputStream in = null;
		ByteArrayOutputStream out = null;
		int ret =0;
		try
		{
			in = new ByteArrayOutputStream();
			in.write(data.getBytes(encoding));
			in.flush();
			RequestEntity entity = new ByteArrayRequestEntity(in.toByteArray());
			post.setRequestEntity(entity);
			
			httpClient.setConnectionTimeout(this.connectionTimeout);
			
			 ret = httpClient.executeMethod(post);// 调Invocation / Resolver 

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
			logger.error(e, e);
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
				logger.error(e, e);
			}
			post.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return null;
	}
	
	//  调CRM地址 , QRY050015 超时，单独设置超时时间而用
	public String send3CRM(String data, String channel)
	throws CommunicateException {
		if(data.indexOf("QRY050015") != -1) {
			this.soTimeout=50000;
		}
		HttpClient httpClient = this.createHttpClient(channel);
		
		PostMethod post = new PostMethod(this.remoteURL);
//		remoteURL="http://10.32.229.70:10006/jsmcc_forward/jsmcc_js";
//		remoteURL="http://10.32.229.70:10006/wap_forward/wap_js";
		System.out.println("****************************  HttpCommunicator . send3CRM () remoteURL : "+remoteURL);
		
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
			else if(null != statusCode.get(ret))
			{
				logger.error("Http请求出错, 返回码["+ret+"]!!");
				return String.valueOf(ret);
			}
		}
		catch(SocketTimeoutException se)
		{
			logger.error(se);
			//请求超时，返回码是408
			return "408";
		}
		catch (Exception e)
		{
			logger.error(e, e);
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
				logger.error(e, e);
			}
			post.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return null;
	}
    // 调CRM地址
	public String send2CRM(String data, String channel) throws CommunicateException
	{
		//获取接口标识符
		String processCode = XMLUtil.getChildTextEx(data,"process_code");
		int connectTime = 0;
		try {
			connectTime = qryInterfaceConnectionTimeDAOImpl.qryInterfaceConnetionTime(processCode);
			if(connectTime != 0){
				this.connectionTimeout = connectTime;
			}
		} catch (Exception e1) {
			logger.error(e1);
		}
		
		HttpClient httpClient = this.createHttpClient(channel);
		
		PostMethod post = new PostMethod(this.remoteURL);
//		remoteURL="http://10.32.229.70:10006/jsmcc_forward/jsmcc_js";
//		remoteURL="http://10.32.229.70:10006/wap_forward/wap_js";
		System.out.println("****************************  HttpCommunicator . send2CRM () remoteURL : "+remoteURL);
		
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
			
			httpClient.setConnectionTimeout(this.connectionTimeout);
			
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
			else if(null != statusCode.get(ret))
			{
				logger.error("Http请求出错, 返回码["+ret+"]!!");
				return String.valueOf(ret);
			}
		}
		catch(SocketTimeoutException se)
		{
			logger.error(se);
			//请求超时，返回码是408
			return "408";
		}
		catch (Exception e)
		{
			logger.error(e, e);
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
				logger.error(e, e);
			}
			post.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return null;
	}

	public String getRemoteURL()
	{
		return remoteURL;
	}

	public void setRemoteURL(String remoteURL)
	{
		this.remoteURL = remoteURL;
	}

	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	public int getSoTimeout()
	{
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout)
	{
		this.soTimeout = soTimeout;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public int getMaxThreads()
	{
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads)
	{
		this.maxThreads = maxThreads;
	}
	
	public static void main(String args[]) throws Exception
	{
		final String req = readFromFile("E:/request.txt");
		final HttpCommunicator comm = new HttpCommunicator();
		comm.remoteURL = "http://10.32.122.152:10003/xwecp/xwecp.do";
		//comm.initialize();
		if(true)
		{
			System.exit(0);
		}
		int t = 0;
		while (true) {
			if (t >= 20) {
				break;
			}
			new Thread(){
				public void run() {
					int i = 0;
					int s = 0;
					int f = 0;
					
					long total = 0;
					while (true) {
						if (i >= 30) {
							break;
						}
						long bg = System.currentTimeMillis();
						String ret = null;
						try
						{
							ret = comm.send(req, "");
						}
						catch (CommunicateException e1)
						{
							logger.error(e1, e1);
						}
						long ed = System.currentTimeMillis();
						if(ret != null)
						{
							logger.info("逻辑接口测试OK!!!!!!!!!!!!!!!!!!!!!花费： " + (ed - bg) + " ms!");
							s++;
							
//							if (i == 19) {
//								List<GommonBusiness> list = r.getGommonBusiness();
//								for (GommonBusiness b:list) {
//									System.out.println("id： " + b.getId() + " state： " + b.getState() + " beginDate： " + b.getBeginDate() + " endDate： " + b.getEndDate());
//								}
//							}
							
						} else {
							f++;
						}
						total += (ed - bg);
						i ++;
						try
						{
							//Thread.sleep(100);
						}
						catch (Exception e)
						{
							logger.error(e, e);
						}
					}
					
					System.out.println("共运行： " + (s+f) + " 次！成功： "  + s + " 次！ 失败： " + f + "次！ 平均时间： " + (total/(s+f)) + " ms!!");
				}
			}.start();
			t++;
		}
	}
	
	private static String readFromFile(String f) throws Exception
	{
		FileInputStream fis = new FileInputStream(new File(f));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[10240];
		int nRead = 0;
		while((nRead = fis.read(buf)) > 0)
		{
			baos.write(buf, 0, nRead);
		}
		fis.close();
		return baos.toString("UTF-8");
	}

	public int getMaxConnectionsPerHost()
	{
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(int maxConnectionsPerHost)
	{
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public String getKeepAlive()
	{
		return keepAlive;
	}

	public void setKeepAlive(String keepAlive)
	{
		this.keepAlive = keepAlive;
	}

	public String getUsePool()
	{
		return usePool;
	}

	public void setUsePool(String usePool)
	{
		this.usePool = usePool;
	}

	public Map getHttpClientMap() {
		return httpClientMap;
	}

	public void setHttpClientMap(Map httpClientMap) {
		this.httpClientMap = httpClientMap;
	}

	public QryInterfaceConnetionTimeDAOImpl getQryInterfaceConnectionTimeDAOImpl() {
		return qryInterfaceConnectionTimeDAOImpl;
	}

	public void setQryInterfaceConnectionTimeDAOImpl(
			QryInterfaceConnetionTimeDAOImpl qryInterfaceConnectionTimeDAOImpl) {
		this.qryInterfaceConnectionTimeDAOImpl = qryInterfaceConnectionTimeDAOImpl;
	}

	
	
}
