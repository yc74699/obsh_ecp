package com.xwtech.xwecp.interfaces.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.ChargingOrderFeedbackDAOImpl;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.exception.BaseException;
import com.xwtech.xwecp.interfaces.InterfaceBase;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 充值订单反馈接口
 * @author wang.huan
 *
 */
public class ChargingOrderFeedbackImpl implements InterfaceBase  {
	
	private static final Logger logger = Logger.getLogger(ChargingOrderFeedbackImpl.class);
	
	private SimpleDateFormat format1=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private ChargingOrderFeedbackDAOImpl chargingOrderFeedBackDAO;
	
	public ChargingOrderFeedbackDAOImpl getChargingOrderFeedBackDAO() {
		return chargingOrderFeedBackDAO;
	}

	public void setChargingOrderFeedBackDAO(
			ChargingOrderFeedbackDAOImpl chargingOrderFeedBackDAO) {
		this.chargingOrderFeedBackDAO = chargingOrderFeedBackDAO;
	}

	public List<RequestParameter> execute(String requestXML)
			throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();
		RequestParameter code = new RequestParameter();
		code.setParameterName("ret_code");// 错误代码 代码   0000为成功   -1为失败
		RequestParameter msg = new RequestParameter();
		msg.setParameterName("ret_msg");// 错误描述
		
		String order_id = XMLUtil.getChildTextEx(requestXML,"content","order_id");
		if(StringUtil.isNull(order_id)){
			code.setParameterValue("-1");
			msg.setParameterValue("订单编号(order_id)为空!");
			res.add(code);
			res.add(msg);
			return res;
		}
		String pay_money = XMLUtil.getChildTextEx(requestXML, "content","pay_money");
		if(StringUtil.isNull(pay_money)){
			code.setParameterValue("-1");
			msg.setParameterValue("充值金额(pay_money)为空!");
			res.add(code);
			res.add(msg);
			return res;
		}
		String pay_mobile = XMLUtil.getChildTextEx(requestXML, "content","pay_mobile");
		if(StringUtil.isNull(pay_mobile)){
			code.setParameterValue("-1");
			msg.setParameterValue("充值号码(pay_mobile)为空!");
			res.add(code);
			res.add(msg);
			return res;
		}
		String pay_time = XMLUtil.getChildTextEx(requestXML, "content","pay_time");
		if(StringUtil.isNull(pay_time)){
			code.setParameterValue("-1");
			msg.setParameterValue("充值时间(pay_time)为空!");
			res.add(code);
			res.add(msg);
			return res;
		}
		String pay_channel = XMLUtil.getChildTextEx(requestXML, "content","pay_channel");
		if(StringUtil.isNull(pay_channel)){
			code.setParameterValue("-1");
			msg.setParameterValue("充值渠道(pay_channel)为空!");
			res.add(code);
			res.add(msg);
			return res;
		}
		String pay_type = XMLUtil.getChildTextEx(requestXML, "content","pay_type");
		if(StringUtil.isNull(pay_type)){
			code.setParameterValue("-1");
			msg.setParameterValue("充值银行(pay_type)为空!");
			res.add(code);
			res.add(msg);
			return res;
		}
		String reserve = XMLUtil.getChildTextEx(requestXML, "content","reserve");
		String reserve1 = XMLUtil.getChildTextEx(requestXML, "content","reserve1");
		
		String log_id = this.format1.format(new Date());
		String [] args = {log_id,order_id,pay_mobile,pay_money,pay_type,pay_channel,pay_time,reserve,reserve1};
		try {
			int result = this.chargingOrderFeedBackDAO.addChargingOrder(args);
			if(result > 0){
				code.setParameterValue("0000");
				msg.setParameterValue("");
			}else{
				code.setParameterValue("-1");
				msg.setParameterValue("入库操作失败!");
			}
			res.add(code);
			res.add(msg);
		} catch (DAOException e) {
			logger.error(e.getMessage());
			code.setParameterValue("-1");
			msg.setParameterValue("入库操作失败!");
			res.add(code);
			res.add(msg);
			return res;
		}
		return res;
	}
}
