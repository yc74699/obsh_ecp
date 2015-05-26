package com.xwtech.xwecp.interfaces.impl;

import java.util.ArrayList;
import java.util.List;
import com.xwtech.xwecp.dao.InterfaceDAO;
import com.xwtech.xwecp.dao.SmsDao;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.exception.BaseException;
import com.xwtech.xwecp.interfaces.InterfaceBase;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.util.XMLUtil;
/**
 * @author        :  TG
 * @Create Date   :  2014-2-26
 */
public class InterfaceNetinstallImpl implements InterfaceBase {
	
	private InterfaceDAO interfaceDAO;
	
	private WellFormedDAO wellFormedDAO;
	
	private SmsDao smsDao;

	public List<RequestParameter> execute(String reqXML) throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();
		
		String orderId = XMLUtil.getChildText(reqXML,"content","orderid");
		String feedOrder = XMLUtil.getChildText(reqXML,"content","feed_order");
		int result = 0;
		
		if("1".equals(feedOrder))
		{
			result = interfaceDAO.updateOrderState(orderId);
			if( 0 == result)
			{
			}
		}
		return res;
	}

	public InterfaceDAO getInterfaceDAO() {
		return interfaceDAO;
	}

	public void setInterfaceDAO(InterfaceDAO interfaceDAO) {
		this.interfaceDAO = interfaceDAO;
	}

	public WellFormedDAO getWellFormedDAO() {
		return wellFormedDAO;
	}

	public void setWellFormedDAO(WellFormedDAO wellFormedDAO) {
		this.wellFormedDAO = wellFormedDAO;
	}

	public SmsDao getSmsDao() {
		return smsDao;
	}

	public void setSmsDao(SmsDao smsDao) {
		this.smsDao = smsDao;
	}
}