package com.xwtech.xwecp.Jedis;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClient {
	/**
     * 服务器地址
     */
    private static final String HOST = "host";

    /**
     * 服务器端口
     */
    private static final String PORT = "port";
    
    private static final Logger logger = Logger
	.getLogger(RedisClient.class);

    /**
     * 服务器信息列表
     */
    private List<Map<String,String>> serverList = null;
    /**
     * 初始化shardedJedisPool
     */
    private ShardedJedisPool shardedJedisPool = null;
    /**
     * redis分片
     */
    private List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
    
    /**
     * 字符集：UTF-8
     */
    private Charset UTF= Charset.forName("utf-8");
    /**
     * 初始化客户端 
     * @param serverFile redis服务器配置信息文件
     */
    public void init(File serverFile)
    {
        // 解析server配置文件
        if (null == serverList)
        {
            serverList = ParseRedisXml.parseXml(serverFile);
        }
     // 初始化redis分片信息
        initShardedJedisPool();
        
        if(null == shardedJedisPool)
        {
        	 System.out.println("<请确认redis服务器是否启动!>");
        	 logger.info("jsmcc_redis请确认redis服务器是否启动!");
             return;
        }
    }

    /**
     * 初始化ShardedJedisPool。
     * 其存放的是所有服务器的连接
     */
    private void initShardedJedisPool()
    {
        if (null == shards || shards.size() == 0)
        {
            String host = null;
            Integer port = null;
            JedisShardInfo si = null;
            
            for (Map<String,String> map : serverList)
            {
                host = map.get(HOST).toString();
                port = Integer.parseInt(map.get(PORT).toString());
                si = new JedisShardInfo(host, port);
                shards.add(si);
            }
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(200);
            shardedJedisPool = new ShardedJedisPool(config, shards);
            
            System.out.println("redis 已启动");
            logger.info("jsmcc_redis--redis 已启动");
        }
    }
    
    /**
     * 销毁连接池
     */
    public void destroy()
    {
        serverList = null;
        shardedJedisPool.destroy();
    }
    
    /**
     * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
     * @param key 关键字
     * @param value 值
     * @return 状态码 OK
     * <br><b>Tips:</b>若要使用返回值，请做null值校验。和服务器建立连接多次失败，则返回null
     */    
    public String set(String key, String value)
    {
    	ShardedJedis shardJedis = null;
  
        String status = null;
        try
        {
        	shardJedis = shardedJedisPool.getResource();
            status = shardJedis.set(key.getBytes(UTF), value.getBytes(UTF));
        }
        catch (Exception e)
        {
          e.printStackTrace();
          logger.info("jsmcc_redis_set"+e);
        }
        finally
        {
        	shardedJedisPool.returnResource(shardJedis);
        }
        return status;
    }
    /**
     * 将Object对象值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
     * @param key 关键字
     * @param value 对象值
     * @return 状态码 OK
     * <br><b>Tips:</b>若要使用返回值，请做null值校验。和服务器建立连接多次失败，则返回null
     */
    public String set(String key, Object value)
    {
    	Long beginTime,endTime;
		beginTime = System.currentTimeMillis();
		
    	ShardedJedis shardJedis = null;
        String statusCode = null;
        byte[] valueBytes = CommonUtil.serialize(value);
        try
        {
        	shardJedis = shardedJedisPool.getResource();
            statusCode = shardJedis.set(key.getBytes(UTF), valueBytes);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          logger.info("jsmcc_redis_set"+e);
        }
        finally
        {
        	shardedJedisPool.returnResource(shardJedis);
        }
        endTime = System.currentTimeMillis();
		Long resultTime = endTime - beginTime;
		if(resultTime >= 1000)
		{
			logger.info("jsmcc_redis_get_:"+resultTime);
		}
        return statusCode;
    }
    
    /**
     * 这个命令和 EXPIREAT 命令类似，但它以毫秒为单位设置 key 的过期 unix 时间戳，而不是像 EXPIREAT 那样，以秒为单位。
     * @param key 关键字
     * @param millisecondsTimestamp
     * @return 如果生存时间设置成功，返回 1 。
     *         <br>当 key 不存在或没办法设置生存时间时，返回 0 。
     * <br><b>Tips:</b>若要使用返回值，请做null值校验。和服务器建立连接多次失败，则返回null
     */
    public Long pExpireAt(String key, long millisecondsTimestamp)
    {
    	ShardedJedis shardJedis = null;
        Long resultCode = null;
        try
        {
        	shardJedis = shardedJedisPool.getResource();
        	resultCode = shardJedis.expireAt(key, millisecondsTimestamp);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          logger.info("jsmcc_redis_pExpireAt"+e);
        }
        finally
        {
        	shardedJedisPool.returnResource(shardJedis);
        }
        return resultCode;
    }
    /**
     * 返回 key 所关联的Object值。
     * <br>获取之后需转换成对应的数据类型
     * @param key 关键字
     * @return Object值。当 key 不存在时，返回 nil.
     * <br><b>Tips:</b>若要使用返回值，请做null值校验。和服务器建立连接多次失败，则返回null
     */
    public Object get(byte[] key)
    {
    	Long beginTime,endTime;
		beginTime = System.currentTimeMillis();
		
		ShardedJedis shardJedis = null;
        byte[] value = null;
        try
        {
            shardJedis = shardedJedisPool.getResource();
            value = shardJedis.get(key);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          logger.info("jsmcc_redis_get"+e);
        }
        finally
        {
        	shardedJedisPool.returnResource(shardJedis);
        }
        Object obj = CommonUtil.unserialize(value);
        
		endTime = System.currentTimeMillis();
		Long resultTime = endTime - beginTime;
		if(resultTime >= 1000)
		{
			logger.info("jsmcc_redis_get_:"+resultTime);
		}
        return obj;
    }
    
    public String get(String key)
    {
    	Long beginTime,endTime;
		beginTime = System.currentTimeMillis();
		
        ShardedJedis shardJedis = null;
        String value = null;
        try
        {
            shardJedis = shardedJedisPool.getResource();
            byte[] by = shardJedis.get(key.getBytes(UTF));
            value = new String(by,UTF);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
        	shardedJedisPool.returnResource(shardJedis);
        }
    	endTime = System.currentTimeMillis();
		Long resultTime = endTime - beginTime;
		if(resultTime >= 1000)
		{
			logger.info("jsmcc_redis_get_:"+resultTime);
		}
        return value;
    }
    /**
     * 删除给定的 key 。
     * <br>不存在的 key 会被忽略。
     * @param key 关键字
     * @return 被删除 key 的数量。
     * <br><b>Tips:</b>若要使用返回值，请做null值校验。和服务器建立连接多次失败，则返回null
     * 
     */
    public Long del(String key)
    {
    	Long beginTime,endTime;
		beginTime = System.currentTimeMillis();
		
    	ShardedJedis shardJedis = null;
        Long quantity = null;
        try
        {
        	shardJedis = shardedJedisPool.getResource();
            quantity = shardJedis.del(key);
        }
        catch (Exception e)
        {
        	  e.printStackTrace();
        }
        finally
        {
        	shardedJedisPool.returnResource(shardJedis);
        }
    	endTime = System.currentTimeMillis();
		Long resultTime = endTime - beginTime;
		if(resultTime >= 1000)
		{
			logger.info("jsmcc_redis_del_:"+resultTime);
		}
        
        return quantity;
    }
}