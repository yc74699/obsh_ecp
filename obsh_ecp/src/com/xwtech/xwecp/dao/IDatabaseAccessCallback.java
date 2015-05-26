package com.xwtech.xwecp.dao;


import java.sql.Connection;
import java.sql.SQLException;


public interface IDatabaseAccessCallback
{
	public Object execute(Connection conn) throws SQLException;
}
