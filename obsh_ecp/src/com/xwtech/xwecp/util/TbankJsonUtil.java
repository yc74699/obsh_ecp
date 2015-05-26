package com.xwtech.xwecp.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.xwtech.tbank.rpc.message.TBankResponseMessage;

/**
 * 流量银行请求数据组装json报文、解析返回json报文
 * @version 1.0
 */
public class TbankJsonUtil {
	
	//组装json报文
	public static String internalWriteRequest(String methodName,Object[] arguments, String id) throws IOException 
	{
		//创建请求对象
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode request = mapper.createObjectNode();
		//添加入参ID,json报文头
		if (id != null)
		{
			request.put("id", id);
		}
        //添加json版本
		request.put("jsonrpc", "2.0");
		//json请求的接口方法
		request.put("method", methodName);
		//组装json报文体中的内容(根据入参的多态对象类型判断)
		if (arguments != null && arguments.getClass().isArray()) 
		{
			Object[] args = Object[].class.cast(arguments);
			if (args.length > 0) 
			{
				ArrayNode paramsNode = new ArrayNode(mapper.getNodeFactory());
				for (Object arg : args)
				{
					JsonNode argNode = mapper.valueToTree(arg);
					paramsNode.add(argNode);
				}
				request.put("params", paramsNode);
			}
		} 
		else if (arguments != null && Collection.class.isInstance(arguments)) 
		{
			Collection<?> args = Collection.class.cast(arguments);
			if (!args.isEmpty()) 
			{
				ArrayNode paramsNode = new ArrayNode(mapper.getNodeFactory());
				for (Object arg : args) 
				{
					JsonNode argNode = mapper.valueToTree(arg);
					paramsNode.add(argNode);
				}
				request.put("params", paramsNode);
			}
		} 
		else if (arguments != null && Map.class.isInstance(arguments)) 
		{
			if (!Map.class.cast(arguments).isEmpty()) 
			{
				request.put("params", mapper.valueToTree(arguments));
			}
		} 
		else if (arguments != null)
		{
			request.put("params", mapper.valueToTree(arguments));
		}
		return request.toString();
	}
    //解析json返回的报文
	public static TBankResponseMessage readResponse(Type returnType, InputStream ips)throws Exception 
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			//根据请求json字节流装换成ReadContext对象
			ReadContext ctx = ReadContext.getReadContext(ips, mapper);
			//读取json的字符串
			ctx.assertReadable();
			JsonNode response = ctx.nextValue();
			if (!response.isObject())
			{
				throw new Exception("Invalid JSON-RPC response");
			}
			ObjectNode jsonObject = ObjectNode.class.cast(response);
			//判断返回json中是否有error结点
			if (jsonObject.has("error") && jsonObject.get("error") != null && !jsonObject.get("error").isNull())
			{
				// resolve and throw the exception
			}
			//解析返回json中result结点中数据
			if (jsonObject.has("result") && !jsonObject.get("result").isNull()&& jsonObject.get("result") != null)
			{
				if (returnType == null)
				{
					return null;
				}
				JsonParser returnJsonParser = mapper.treeAsTokens(jsonObject.get("result"));
				JavaType returnJavaType = TypeFactory.defaultInstance().constructType(returnType);
				return mapper.readValue(returnJsonParser, returnJavaType);
			}
		} 
		catch (Throwable t)
		{
			throw new Exception(t);
		}
		return null;

	}
}
