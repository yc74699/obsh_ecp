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
 * @author        :  xufan
 * @Create Date   :  2014-5-19
 */
public class InterfaceCfmyordersubmitImpl implements InterfaceBase {
	
	private InterfaceDAO interfaceDAO;
	
	private WellFormedDAO wellFormedDAO;
	
	private SmsDao smsDao;

	public List<RequestParameter> execute(String reqXML) throws BaseException {
		List<RequestParameter> res = new ArrayList<RequestParameter>();		
		String telnum = XMLUtil.getChildText(reqXML,"content","telnum");
		String totalfee = XMLUtil.getChildText(reqXML,"content","totalfee");
		String paytype = XMLUtil.getChildText(reqXML,"content","paytype");
		String payplat = XMLUtil.getChildText(reqXML,"content","payplat");
		String ispayed = XMLUtil.getChildText(reqXML,"content","ispayed");
		String fmyprodid = XMLUtil.getChildText(reqXML,"content","fmyprodid");
		String fmyeffecttype = XMLUtil.getChildText(reqXML,"content","fmyeffecttype");
		String rwdinfo = XMLUtil.getChildText(reqXML,"content","rwdinfo");
		String actid = XMLUtil.getChildText(reqXML,"rwdinfo","actid");
		String privid = XMLUtil.getChildText(reqXML,"rwdinfo","privid");
		String rewardlist = XMLUtil.getChildText(reqXML,"rwdinfo","rewardlist");
		String drawtype = XMLUtil.getChildText(reqXML,"rwdinfo","drawtype");
		String rewardsn = XMLUtil.getChildText(reqXML,"rwdinfo","rewardsn");
		String rewardquantity = XMLUtil.getChildText(reqXML,"rwdinfo","rewardquantity");
		String rewardapd = XMLUtil.getChildText(reqXML,"rwdinfo","rewardapd");	
		String propertyinfo = XMLUtil.getChildText(reqXML,"content","propertyinfo");
		String itemid = XMLUtil.getChildText(reqXML,"propertyinfo","itemid");
		String itemvalue = XMLUtil.getChildText(reqXML,"propertyinfo","itemvalue");
		String productinfo = XMLUtil.getChildText(reqXML,"content","productinfo");
		String prodid = XMLUtil.getChildText(reqXML,"productinfo","prodid");		
		String pkgprodid = XMLUtil.getChildText(reqXML,"productinfo","pkgprodid");
		String ispackage = XMLUtil.getChildText(reqXML,"productinfo","ispackage");
		String effecttype = XMLUtil.getChildText(reqXML,"productinfo","effecttype");	
		String deviceinfo = XMLUtil.getChildText(reqXML,"content","deviceinfo");
		String deviceclass = XMLUtil.getChildText(reqXML,"deviceinfo","deviceclass");		
		String restypeid = XMLUtil.getChildText(reqXML,"deviceinfo","restypeid");
		String getmode = XMLUtil.getChildText(reqXML,"deviceinfo","getmode");
		String bandtype = XMLUtil.getChildText(reqXML,"deviceinfo","bandtype");		
		String fmymeminfo = XMLUtil.getChildText(reqXML,"content","fmymeminfo");
		String memtel = XMLUtil.getChildText(reqXML,"fmymeminfo","memtel");
		String memregion = XMLUtil.getChildText(reqXML,"fmymeminfo","memregion");
		String payplantype = XMLUtil.getChildText(reqXML,"fmymeminfo","payplantype");
		String memshortnum = XMLUtil.getChildText(reqXML,"fmymeminfo","memshortnum");
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