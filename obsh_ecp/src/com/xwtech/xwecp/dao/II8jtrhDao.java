package com.xwtech.xwecp.dao;

import java.util.List;


public interface II8jtrhDao {
	/**
	 * 是否是家庭I8系列套餐
	 * @param tcId
	 * @return
	 */
	public boolean isT8JTHRH(String tcId) throws DAOException;
	
	/**
	 * 查询I8表
	 * @return
	 */
	public List<String>  getI8TC() throws DAOException;

}
