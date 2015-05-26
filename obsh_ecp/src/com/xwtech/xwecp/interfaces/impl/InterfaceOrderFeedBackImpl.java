package com.xwtech.xwecp.interfaces.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.InterfaceDAO;
import com.xwtech.xwecp.dao.SmsDao;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.exception.BaseException;
import com.xwtech.xwecp.interfaces.InterfaceBase;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.util.XMLUtil;
/**
 * @author        :  xufan
 * @Create Date   :  2014-5-19
 */
public class InterfaceOrderFeedBackImpl implements InterfaceBase {
	
	private static final Logger logger = Logger.getLogger(InterfaceOrderFeedBackImpl.class);
	
	private InterfaceDAO interfaceDAO;
	
	private WellFormedDAO wellFormedDAO;
	
	private SmsDao smsDao;

	public List<RequestParameter> execute(String reqXML) throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();
		
		String orderId = XMLUtil.getChildText(reqXML,"content","orderid");
//		String telnum = XMLUtil.getChildText(reqXML,"content","telnum");
		String orderstatus = XMLUtil.getChildText(reqXML,"content","orderstatus");
		int result = 0;
		
		RequestParameter code = new RequestParameter();
		code.setParameterName("ret_code");// 错误代码 代码   0000为成功   -1为失败
		RequestParameter msg = new RequestParameter();
		msg.setParameterName("ret_msg");// 错误描述
		
		try {
			result = interfaceDAO.updateOrderNetState(orderId,orderstatus);
			
			if(result > 0){
				code.setParameterValue("0000");
				msg.setParameterValue("操作成功!");
			}else{
				code.setParameterValue("-1");
				msg.setParameterValue("操作失败!");
			}
			res.add(code);
			res.add(msg);
		} catch (Exception e) {
			logger.error(e.getMessage());
			code.setParameterValue("-1");
			msg.setParameterValue("操作失败!");
			res.add(code);
			res.add(msg);
			return res;
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