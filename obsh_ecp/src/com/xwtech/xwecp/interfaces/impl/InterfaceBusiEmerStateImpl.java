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
 * @Create Date   :  2013-7-16
 */
public class InterfaceBusiEmerStateImpl implements InterfaceBase {
	
	private InterfaceDAO interfaceDAO;
	
	private WellFormedDAO wellFormedDAO;
	
	private SmsDao smsDao;

	public List<RequestParameter> execute(String reqXML) throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();
		List<ESInfo> esinfoList = new ArrayList<ESInfo>();
		String seqNum = "";
		String region = "";
		String reclist = "";
		String[] interfaceArray ;
		ESInfo info = null;
		String channelNum = XMLUtil.getChildText(reqXML,"content","eslist_channel");
		String status = XMLUtil.getChildText(reqXML,"content","eslist_status");
		List eslist = XMLUtil.getChildList(reqXML,"content","eslists","eslist");
		for(int i = 0; i < eslist.size(); i++)
		{	
			info = new ESInfo();
			Element es = (Element)eslist.get(i);
			seqNum = XMLUtil.getChildText(es,"sequence_number");
			region = XMLUtil.getChildText(es,"region"); // 地市
			reclist = XMLUtil.getChildText(es,"reclist"); //应急业务列表BOSS接口命令字,按逗号（，）分隔，如ac_acqryrealtimebill，ac_acqryspandproduse，ac_acquerybalanceflowlog

			interfaceArray = reclist.split(",");
			int result = 0;
			
			//crmstatus状态  0正常  1 应急,应急业务列表不能为空
			if(!"".equals(status) && null != interfaceArray && interfaceArray.length > 0)
			{
				result = interfaceDAO.updateState(channelNum,status,region,interfaceArray);
			}
			info.setSeqNum(seqNum);
			info.setDoneTime(DateTimeUtil.getTodayChar14());
			info.setResult( result > 0 ? SUCCESS : FAIL);
			info.setNote(result > 0 ? "" : "no records update");
			esinfoList.add(info);
			//发送短信通知
			sendSMS(channelNum,status,interfaceArray,region);
		}
		res.add(new RequestParameter("resList",esinfoList));
		return res;
	}
	
	
	private void sendSMS(String channelNum,String status, String[] interfaceArray, String region) 
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
			smsContent += areaName+"("+region+"),CRM业务应急切换成了"+statusVal+"("+status+")"+"状态，切换接口为："+buildStrMethod(interfaceArray);
		}
		try {
			int smsflag = smsDao.sendSms("13815890413", smsContent, "10086", "free");//陶刚
			    smsflag = smsDao.sendSms("13913814503", smsContent, "10086", "free");//杨光
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	private String buildStrMethod(String[] interfaceArray) {
		StringBuffer strBossId = new StringBuffer();
		for(int i = 0; i < interfaceArray.length; i++)
		{
			strBossId.append(interfaceArray[i]);
			strBossId.append(",");
		}
		return strBossId.toString().substring(0,strBossId.toString().lastIndexOf(","));
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
