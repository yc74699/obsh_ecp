package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.IpcarBillDetail;
import com.xwtech.xwecp.service.logic.pojo.IspBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MontnetBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MmsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.SmsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.GprsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.VpmnBillDetail;
import com.xwtech.xwecp.service.logic.pojo.WlanBillDetail;
import com.xwtech.xwecp.service.logic.pojo.Ac121BillDetail;
import com.xwtech.xwecp.service.logic.pojo.Isp2BillDetail;
import com.xwtech.xwecp.service.logic.pojo.UssdBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MpmusicBillDetail;
import com.xwtech.xwecp.service.logic.pojo.LbsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MeetBillDetail;
import com.xwtech.xwecp.service.logic.pojo.GsmVideoBillDetail;
import com.xwtech.xwecp.service.logic.pojo.CmnetBillDetail;

public class QRY010001Result extends BaseServiceInvocationResult
{
	private List<GsmBillDetail> gsmBillDetail = new ArrayList<GsmBillDetail>();

	private List<IpcarBillDetail> ipcarBillDetail = new ArrayList<IpcarBillDetail>();

	private List<IspBillDetail> ispBillDetail = new ArrayList<IspBillDetail>();

	private List<MontnetBillDetail> montnetBillDetail = new ArrayList<MontnetBillDetail>();

	private List<MmsBillDetail> mmsBillDetail = new ArrayList<MmsBillDetail>();

	private List<SmsBillDetail> smsBillDetail = new ArrayList<SmsBillDetail>();

	private List<SmsBillDetail> ineSmsBillDetail = new ArrayList<SmsBillDetail>();

	private List<GprsBillDetail> gprsBillDetail = new ArrayList<GprsBillDetail>();

	private List<VpmnBillDetail> vpmnBillDetail = new ArrayList<VpmnBillDetail>();

	private List<WlanBillDetail> wlanBillDetail = new ArrayList<WlanBillDetail>();

	private List<Ac121BillDetail> ac121BillDetail = new ArrayList<Ac121BillDetail>();

	private List<Isp2BillDetail> isp2BillDetail = new ArrayList<Isp2BillDetail>();

	private List<UssdBillDetail> ussdBillDetail = new ArrayList<UssdBillDetail>();

	private List<MpmusicBillDetail> mpmusicBillDetail = new ArrayList<MpmusicBillDetail>();

	private List<LbsBillDetail> lbsBillDetail = new ArrayList<LbsBillDetail>();

	private List<MeetBillDetail> meetBillDetail = new ArrayList<MeetBillDetail>();

	private List<GsmVideoBillDetail> gsmVideoBillDetail = new ArrayList<GsmVideoBillDetail>();

	private List<CmnetBillDetail> cmnetBillDetail = new ArrayList<CmnetBillDetail>();

	private List<GsmMagaBillDetail> gsmMagaBillDetail = new ArrayList<GsmMagaBillDetail>();
	private List<CdrsBillDetail> cdrsBillDetail = new ArrayList<CdrsBillDetail>();
	private List<CdrsPointBillDetail> cdrsPointBillDetail = new ArrayList<CdrsPointBillDetail>();

	

	public List<CdrsBillDetail> getCdrsBillDetail() {
		return cdrsBillDetail;
	}

	public void setCdrsBillDetail(List<CdrsBillDetail> cdrsBillDetail) {
		this.cdrsBillDetail = cdrsBillDetail;
	}

	public List<CdrsPointBillDetail> getCdrsPointBillDetail() {
		return cdrsPointBillDetail;
	}

	public void setCdrsPointBillDetail(List<CdrsPointBillDetail> cdrsPointBillDetail) {
		this.cdrsPointBillDetail = cdrsPointBillDetail;
	}

	public List<GsmMagaBillDetail> getGsmMagaBillDetail() {
		return gsmMagaBillDetail;
	}

	public void setGsmMagaBillDetail(List<GsmMagaBillDetail> gsmMagaBillDetail) {
		this.gsmMagaBillDetail = gsmMagaBillDetail;
	}

	public void setGsmBillDetail(List<GsmBillDetail> gsmBillDetail)
	{
		this.gsmBillDetail = gsmBillDetail;
	}

	public List<GsmBillDetail> getGsmBillDetail()
	{
		return this.gsmBillDetail;
	}

	public void setIpcarBillDetail(List<IpcarBillDetail> ipcarBillDetail)
	{
		this.ipcarBillDetail = ipcarBillDetail;
	}

	public List<IpcarBillDetail> getIpcarBillDetail()
	{
		return this.ipcarBillDetail;
	}

	public void setIspBillDetail(List<IspBillDetail> ispBillDetail)
	{
		this.ispBillDetail = ispBillDetail;
	}

	public List<IspBillDetail> getIspBillDetail()
	{
		return this.ispBillDetail;
	}

	public void setMontnetBillDetail(List<MontnetBillDetail> montnetBillDetail)
	{
		this.montnetBillDetail = montnetBillDetail;
	}

	public List<MontnetBillDetail> getMontnetBillDetail()
	{
		return this.montnetBillDetail;
	}

	public void setMmsBillDetail(List<MmsBillDetail> mmsBillDetail)
	{
		this.mmsBillDetail = mmsBillDetail;
	}

	public List<MmsBillDetail> getMmsBillDetail()
	{
		return this.mmsBillDetail;
	}

	public void setSmsBillDetail(List<SmsBillDetail> smsBillDetail)
	{
		this.smsBillDetail = smsBillDetail;
	}

	public List<SmsBillDetail> getSmsBillDetail()
	{
		return this.smsBillDetail;
	}

	public void setIneSmsBillDetail(List<SmsBillDetail> ineSmsBillDetail)
	{
		this.ineSmsBillDetail = ineSmsBillDetail;
	}

	public List<SmsBillDetail> getIneSmsBillDetail()
	{
		return this.ineSmsBillDetail;
	}

	public void setGprsBillDetail(List<GprsBillDetail> gprsBillDetail)
	{
		this.gprsBillDetail = gprsBillDetail;
	}

	public List<GprsBillDetail> getGprsBillDetail()
	{
		return this.gprsBillDetail;
	}

	public void setVpmnBillDetail(List<VpmnBillDetail> vpmnBillDetail)
	{
		this.vpmnBillDetail = vpmnBillDetail;
	}

	public List<VpmnBillDetail> getVpmnBillDetail()
	{
		return this.vpmnBillDetail;
	}

	public void setWlanBillDetail(List<WlanBillDetail> wlanBillDetail)
	{
		this.wlanBillDetail = wlanBillDetail;
	}

	public List<WlanBillDetail> getWlanBillDetail()
	{
		return this.wlanBillDetail;
	}

	public void setAc121BillDetail(List<Ac121BillDetail> ac121BillDetail)
	{
		this.ac121BillDetail = ac121BillDetail;
	}

	public List<Ac121BillDetail> getAc121BillDetail()
	{
		return this.ac121BillDetail;
	}

	public void setIsp2BillDetail(List<Isp2BillDetail> isp2BillDetail)
	{
		this.isp2BillDetail = isp2BillDetail;
	}

	public List<Isp2BillDetail> getIsp2BillDetail()
	{
		return this.isp2BillDetail;
	}

	public void setUssdBillDetail(List<UssdBillDetail> ussdBillDetail)
	{
		this.ussdBillDetail = ussdBillDetail;
	}

	public List<UssdBillDetail> getUssdBillDetail()
	{
		return this.ussdBillDetail;
	}

	public void setMpmusicBillDetail(List<MpmusicBillDetail> mpmusicBillDetail)
	{
		this.mpmusicBillDetail = mpmusicBillDetail;
	}

	public List<MpmusicBillDetail> getMpmusicBillDetail()
	{
		return this.mpmusicBillDetail;
	}

	public void setLbsBillDetail(List<LbsBillDetail> lbsBillDetail)
	{
		this.lbsBillDetail = lbsBillDetail;
	}

	public List<LbsBillDetail> getLbsBillDetail()
	{
		return this.lbsBillDetail;
	}

	public void setMeetBillDetail(List<MeetBillDetail> meetBillDetail)
	{
		this.meetBillDetail = meetBillDetail;
	}

	public List<MeetBillDetail> getMeetBillDetail()
	{
		return this.meetBillDetail;
	}

	public void setGsmVideoBillDetail(List<GsmVideoBillDetail> gsmVideoBillDetail)
	{
		this.gsmVideoBillDetail = gsmVideoBillDetail;
	}

	public List<GsmVideoBillDetail> getGsmVideoBillDetail()
	{
		return this.gsmVideoBillDetail;
	}

	public void setCmnetBillDetail(List<CmnetBillDetail> cmnetBillDetail)
	{
		this.cmnetBillDetail = cmnetBillDetail;
	}

	public List<CmnetBillDetail> getCmnetBillDetail()
	{
		return this.cmnetBillDetail;
	}


}