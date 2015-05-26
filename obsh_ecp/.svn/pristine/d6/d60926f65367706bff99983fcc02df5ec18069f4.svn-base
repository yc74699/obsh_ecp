package com.xwtech.xwecp.interfaces.impl;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import sun.net.ftp.FtpClient;

import com.xwtech.xwecp.communication.HttpCommunicator;
import com.xwtech.xwecp.exception.BaseException;
import com.xwtech.xwecp.interfaces.InterfaceBase;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.util.ConfigurationRead;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * @流量阀值通知处理
 * @author        :  taogang
 * @Create Date   :  2014-3-20
 */
public class PushFluxRemindImpl implements InterfaceBase {
	
	private static final Logger logger = Logger.getLogger(PushFluxRemindImpl.class);
	
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

	private SimpleDateFormat format1=new SimpleDateFormat("yyyyMMddHHmmss");
	
	private SimpleDateFormat format2=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
	
	private static String remindUrl =  ConfigurationRead.getInstance().getValue("pushFluxRemind.url");
	
	public List<RequestParameter> execute(String reqXML) throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();
		String mobile = XMLUtil.getChildTextEx(reqXML,"content","mobile")==null?"":XMLUtil.getChildTextEx(reqXML,"content","mobile");
		String total = XMLUtil.getChildTextEx(reqXML,"content","total")==null?"":XMLUtil.getChildTextEx(reqXML,"content","total");
		String used = XMLUtil.getChildTextEx(reqXML,"content","used")==null?"":XMLUtil.getChildTextEx(reqXML,"content","used");
		String remindTime = XMLUtil.getChildTextEx(reqXML,"content","remindTime")==null?"":XMLUtil.getChildTextEx(reqXML,"content","remindTime");
		String reserve1 = XMLUtil.getChildTextEx(reqXML,"content","reserve1")==null?"":XMLUtil.getChildTextEx(reqXML,"content","reserve1");
		String reserve2 = XMLUtil.getChildTextEx(reqXML,"content","reserve2")==null?"":XMLUtil.getChildTextEx(reqXML,"content","reserve2");
		try {
			remindTime = format2.format(format1.parse(remindTime));
		} catch (ParseException e1) {
			logger.error(e1.getMessage());
		}
//		String content = "截止" + remindTime + "为止,您本月的移动数据已使用流量为"+ (double)(Math.round(Integer.parseInt(used)/1024)) + "M,总流量为" + (double)(Math.round(Integer.parseInt(total)/1024)) + "M";
//		logger.info("content----------------------" + content);
		HashMap<String, Object> postMap = new HashMap<String, Object>();
		StringBuffer ftpStr = new StringBuffer();//ftp内容
		String term = "|";
		postMap.put("mobile", mobile);
		postMap.put("total", total);
		postMap.put("used", used);
		postMap.put("remindTime", remindTime);
		postMap.put("reserve1", reserve1);
		postMap.put("reserve2", reserve2);
		ftpStr.append(mobile + term + total + term + used +term + remindTime + term + reserve1 + term +reserve2 + "\n");
		
		RequestParameter code = new RequestParameter();
		code.setParameterName("ret_code");// 错误代码 代码   0000为成功   -1为失败
		RequestParameter msg = new RequestParameter();
		msg.setParameterName("ret_msg");// 错误描述
		RequestParameter phoneNum = new RequestParameter();//手机号
		phoneNum.setParameterName("phoneNum");//手机号
		
		try {
			String str = this.sendPost(postMap, remindUrl);
//			//写入Ftp信息
//			FtpClient ftpClient = new  FtpClient();
//			ftpClient.openServer( "" , 21 ); // IP地址和端口 
//			ftpClient.login( "" , "" ); // 用户名和密码，匿名登陆的话用户名为anonymous,密码为非空字符串 
//			ftpClient.cd( "" ); // 切换到test目录 
//			PrintWriter pw = new  PrintWriter(ftpClient.put( "javaa.txt" )); // 写入的文件名 
//			pw.write(ftpStr.toString());
//			pw.flush();
//			pw.close();
			
			code.setParameterValue("0000");
			msg.setParameterValue("操作成功!");
			phoneNum.setParameterValue(mobile);
		} catch (Exception e) {
			code.setParameterValue("-1");
			msg.setParameterValue(e.getMessage());
			phoneNum.setParameterValue(mobile);
			logger.error("PushFluxRemindImpl.execute error :" + e);
			res.add(code);
			res.add(msg);
			res.add(phoneNum);
			return res;
		}
		res.add(code);
		res.add(msg);
		res.add(phoneNum);
		return res;
	}

	
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
	            httpClient.setConnectionTimeout(1000);
	            httpClient.getParams().setSoTimeout(5000);
	            httpClient.getParams().setContentCharset("UTF-8");
	            int code = httpClient.executeMethod(postMethod);

	            if (code == 200)
	            {
	                result = postMethod.getResponseBodyAsString();
	            }
	            else if(null != statusCode.get(code))
				{
					logger.error("Http请求出错, 返回码["+code+"]!!");
					throw new Exception(String.valueOf(code));
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
}
