package com.xwtech.xwecp.dao;


import java.sql.Types;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xwtech.xwecp.memcached.IMemcachedManager;


public class BaseDAO extends JdbcDaoSupport
{
	private static final Logger logger = Logger.getLogger(BaseDAO.class);
	
	private IMemcachedManager cache;

	public IMemcachedManager getCache()
	{
		return cache;
	}

	public void setCache(IMemcachedManager cache)
	{
		this.cache = cache;
	}
	
	/**
     * Clob字段更新
     * 
     * @param sql
     * @param args
     * @return intResult
     * @throws DataAccessException
     */
    protected void updateForClob(String sql, Object[] args)
            throws DataAccessException {
        String className = "";
        int[] argTypes = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            className = args[i].getClass().getName();
            if (className.indexOf("String") > 0) {
                argTypes[i] = Types.VARCHAR;
            } else if (className.indexOf("Long") > 0
                    || className.indexOf("Integer") > 0
                    || className.indexOf("Double") > 0
                    || className.indexOf("Float") > 0) {
                argTypes[i] = Types.NUMERIC;
            } else if (className.indexOf("SqlLobValue") > 0) {
                argTypes[i] = Types.CLOB;
            } else {
                argTypes[i] = Types.VARCHAR;
            }
        }
        this.getJdbcTemplate().update(sql, args, argTypes);
    }
    
    /**
     * 修改数据，加Stringj类型字段NULL值校验
     * @param sql
     * @param args
     * @throws DAOException
     */
    protected void update(String sql , Object[] args) throws DataAccessException{
    	if(null != args){
    		for(int i = 0 ;i < args.length;i++)
    		{
    			if( args[i] == null ){
    				args[i] = "";
    			}
    		}
    	}
    	int iResult = getJdbcTemplate().update(sql, args);
    }
}
