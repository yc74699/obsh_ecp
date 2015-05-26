package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.DealDetail;
import com.xwtech.xwecp.service.logic.pojo.PhonesDetail;
import com.xwtech.xwecp.service.logic.pojo.TicketsDetail;

public class MmsDealDetail
{
	private String dealName;

	private String applyDate;

	private String beginDate;

	private String endDate;

	private String dealDesc;

	private String feeAcc;

	private String newbizAcc;

	private String favAcc;

	private String credit1;

	private String credit2;

	private String credit3;

	private List<DealDetail> dealDetail = new ArrayList<DealDetail>();

	private List<PhonesDetail> phonesDetail = new ArrayList<PhonesDetail>();

	private List<TicketsDetail> ticketsDetail = new ArrayList<TicketsDetail>();

	private String planId;

	private String planInfoId;

	public void setDealName(String dealName)
	{
		this.dealName = dealName;
	}

	public String getDealName()
	{
		return this.dealName;
	}

	public void setApplyDate(String applyDate)
	{
		this.applyDate = applyDate;
	}

	public String getApplyDate()
	{
		return this.applyDate;
	}

	public void setBeginDate(String beginDate)
	{
		this.beginDate = beginDate;
	}

	public String getBeginDate()
	{
		return this.beginDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate()
	{
		return this.endDate;
	}

	public void setDealDesc(String dealDesc)
	{
		this.dealDesc = dealDesc;
	}

	public String getDealDesc()
	{
		return this.dealDesc;
	}

	public void setFeeAcc(String feeAcc)
	{
		this.feeAcc = feeAcc;
	}

	public String getFeeAcc()
	{
		return this.feeAcc;
	}

	public void setNewbizAcc(String newbizAcc)
	{
		this.newbizAcc = newbizAcc;
	}

	public String getNewbizAcc()
	{
		return this.newbizAcc;
	}

	public void setFavAcc(String favAcc)
	{
		this.favAcc = favAcc;
	}

	public String getFavAcc()
	{
		return this.favAcc;
	}

	public void setCredit1(String credit1)
	{
		this.credit1 = credit1;
	}

	public String getCredit1()
	{
		return this.credit1;
	}

	public void setCredit2(String credit2)
	{
		this.credit2 = credit2;
	}

	public String getCredit2()
	{
		return this.credit2;
	}

	public void setCredit3(String credit3)
	{
		this.credit3 = credit3;
	}

	public String getCredit3()
	{
		return this.credit3;
	}

	public void setDealDetail(List<DealDetail> dealDetail)
	{
		this.dealDetail = dealDetail;
	}

	public List<DealDetail> getDealDetail()
	{
		return this.dealDetail;
	}

	public void setPhonesDetail(List<PhonesDetail> phonesDetail)
	{
		this.phonesDetail = phonesDetail;
	}

	public List<PhonesDetail> getPhonesDetail()
	{
		return this.phonesDetail;
	}

	public void setTicketsDetail(List<TicketsDetail> ticketsDetail)
	{
		this.ticketsDetail = ticketsDetail;
	}

	public List<TicketsDetail> getTicketsDetail()
	{
		return this.ticketsDetail;
	}

	public void setPlanId(String planId)
	{
		this.planId = planId;
	}

	public String getPlanId()
	{
		return this.planId;
	}

	public void setPlanInfoId(String planInfoId)
	{
		this.planInfoId = planInfoId;
	}

	public String getPlanInfoId()
	{
		return this.planInfoId;
	}

}