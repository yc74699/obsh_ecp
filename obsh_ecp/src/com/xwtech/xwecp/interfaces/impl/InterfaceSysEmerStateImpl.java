package com.xwtech.xwecp.interfaces.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.InterfaceDAO;
import com.xwtech.xwecp.dao.SmsDao;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.exception.BaseException;
import com.xwtech.xwecp.interfaces.InterfaceBase;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.CityArchives;
import com.xwtech.xwecp.pojo.ESInfo;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * @author        :  xmchen
 * @Create Date   :  2013-10-28
 */
public class InterfaceSysEmerStateImpl implements InterfaceBase {
	
	private InterfaceDAO interfaceDAO;
	
	private WellFormedDAO wellFormedDAO;
	
	private SmsDao smsDao;

	public List<RequestParameter> execute(String reqXML) throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();
		List<ESInfo> esinfoList = new ArrayList<ESInfo>();
		String seqNum = "";
		String region = "";
		String channelNum = "";
		String crmstatus = "";
		String bossstatus = "";
		String status = "";
		ESInfo info = null;
		List eslist = XMLUtil.getChildList(reqXML,"content","eslists","eslist");
		for(int i = 0; i < eslist.size(); i++)
		{	
			info = new ESInfo();
			Element es = (Element)eslist.get(i);
			seqNum = XMLUtil.getChildText(es,"sequence_number"); // 地市
			region = XMLUtil.getChildText(es,"region"); // 地市
			channelNum = XMLUtil.getChildText(es,"channel"); // 渠道编号
			crmstatus = XMLUtil.getChildText(es,"crmstatus"); // crm状态  1应急  0正常
			bossstatus = XMLUtil.getChildText(es,"bossstatus"); // boss状态  1应急 0正常
			int result = 0;
			status = getStatus(crmstatus,bossstatus);
			//crmstatus状态 0正常  1应急,应急业务列表不能为空
			result = interfaceDAO.updatesSysState(status,region,channelNum);
			//如果系统应急切换为正常状态，业务应急也切换正常状态
			if("0".equals(status))
			{
				result = interfaceDAO.updateBusiState(status,region,channelNum);
			}
			info.setSeqNum(seqNum);
			info.setDoneTime(DateTimeUtil.getTodayChar14());
			info.setResult( result > 0 ? SUCCESS : FAIL);
			info.setNote(result > 0 ? "" : "no records update");
			esinfoList.add(info);
			//发送系统短信通知
			sendSMS(channelNum,status,region);
			//
		}
		res.add(new RequestParameter("resList",esinfoList));
		return res;
	}
	
	//crm和boss状态只要有一个应急状态就切换。1：应急，0正常
	private String getStatus(String crmstatus, String bossstatus)
	{
		String status = "0";
		if(crmstatus != null && crmstatus.equals("1"))
		{
			return crmstatus;
		}	
		if(bossstatus != null && bossstatus.equals("1"))
		{
			return bossstatus;
		}
		return status;
	}


	private void sendSMS(String channelNum,String status,String region) 
	{
		String areaName = getAreaName(region);
		String statusVal = status.equals("0") ? "正常" : "应急";
		String smsContent = "渠道编号："+channelNum+",";
		if(null == areaName)
		{
			smsContent += "地市编码("+region+")为空或为非江苏地市编码";
		}
		else
		{
			smsContent += areaName+"("+region+"),CRM系统应急切换成了"+statusVal+"("+status+")"+"状态";
		}
		if(!"1".equals(status))
		{
			smsContent += ",业务应急也切换成为"+statusVal+"("+status+")"+"状态!";
		}
		try {
			int smsflag = smsDao.sendSms("15150556323", smsContent, "10086", "free");
				smsflag = smsDao.sendSms("13913814503", smsContent, "10086", "free");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	private String getAreaName(String region) {
		List ars = wellFormedDAO.getCityArchives();
		CityArchives ar = null;
		for(int i = 0; i < ars.size(); i++ )
		{
			ar = (CityArchives) ars.get(i);
			if(region.equals(ar.getBossCode()))
			{
				return ar.getAreaName();
			}
		}
		return null;
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
