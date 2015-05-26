/*
 * 文 件 名:  RedisUtil.java
 * 版   权:  xwtec
 * 描     述:  <描述>
 * 修 改 人:  xwtec
 * 修改时间:  Apr 21, 2014
 * 修改内容:  <修改内容>
 */
package com.xwtech.xwecp.Jedis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 工具类
 * 
 * @author zhuxiaobo
 * @version [版本号, Apr 21, 2014]
 * @since [产品/模块版本]
 */
public class CommonUtil {
	
	/**
	 * 日志对象
	 */
	private static final Logger LOG = Logger.getLogger(CommonUtil.class);

	/**
	 * list是否存在
	 * @param list 列表
	 * @return 布尔值
	 */
	@SuppressWarnings("unchecked")
	public static boolean isExist(List list) {
		if (null != list && list.size() > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 序列化对象
	 * @param object 需要序列化的对象
	 * @return 字节数组
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try
		{
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}
		catch (Exception e) 
		{
			LOG.error("序列化对象出现异常！", e);
		}
		return null;
	}

	/**
	 * 反序列化对象
	 * @param bytes 对象序列化后的字节数组
	 * @return Object
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		g:try
		{
			if(null == bytes || bytes.length == 0)
			{
				break g;
			}
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} 
		catch (Exception e)
		{
			LOG.error("反序列化对象出现异常！", e);
		}
		return null;
	}
}
