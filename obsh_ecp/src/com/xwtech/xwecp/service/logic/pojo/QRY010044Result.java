package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY010044Result extends BaseServiceInvocationResult
{
	private static final long serialVersionUID = 1L;

	private String telnum;

	private String rangeType;

	private String districtName;

	private String addrName;

	private String factoryType;

	private String supplyType;

	private String radiusType;

	private String linkMan;

	private String contactPhone;

	private String gimsUserType;

	private String districtId;

	private String addrId;

	private String netWorkType;

	private String limitBandWidth;

	public void setTelnum(String telnum)
	{
		this.telnum = telnum;
	}

	public String getTelnum()
	{
		return this.telnum;
	}

	public void setRangeType(String rangeType)
	{
		this.rangeType = rangeType;
	}

	public String getRangeType()
	{
		return this.rangeType;
	}

	public void setDistrictName(String districtName)
	{
		this.districtName = districtName;
	}

	public String getDistrictName()
	{
		return this.districtName;
	}

	public void setAddrName(String addrName)
	{
		this.addrName = addrName;
	}

	public String getAddrName()
	{
		return this.addrName;
	}

	public void setFactoryType(String factoryType)
	{
		this.factoryType = factoryType;
	}

	public String getFactoryType()
	{
		return this.factoryType;
	}

	public void setSupplyType(String supplyType)
	{
		this.supplyType = supplyType;
	}

	public String getSupplyType()
	{
		return this.supplyType;
	}

	public void setRadiusType(String radiusType)
	{
		this.radiusType = radiusType;
	}

	public String getRadiusType()
	{
		return this.radiusType;
	}

	public void setLinkMan(String linkMan)
	{
		this.linkMan = linkMan;
	}

	public String getLinkMan()
	{
		return this.linkMan;
	}

	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = contactPhone;
	}

	public String getContactPhone()
	{
		return this.contactPhone;
	}

	public void setGimsUserType(String gimsUserType)
	{
		this.gimsUserType = gimsUserType;
	}

	public String getGimsUserType()
	{
		return this.gimsUserType;
	}

	public void setDistrictId(String districtId)
	{
		this.districtId = districtId;
	}

	public String getDistrictId()
	{
		return this.districtId;
	}

	public void setAddrId(String addrId)
	{
		this.addrId = addrId;
	}

	public String getAddrId()
	{
		return this.addrId;
	}

	public void setNetWorkType(String netWorkType)
	{
		this.netWorkType = netWorkType;
	}

	public String getNetWorkType()
	{
		return this.netWorkType;
	}

	public void setLimitBandWidth(String limitBandWidth)
	{
		this.limitBandWidth = limitBandWidth;
	}

	public String getLimitBandWidth()
	{
		return this.limitBandWidth;
	}

	public String toString()
	{
		return "QRY010044Result [addrId=" + addrId + ", addrName=" + addrName
				+ ", contactPhone=" + contactPhone + ", districtId="
				+ districtId + ", districtName=" + districtName
				+ ", factoryType=" + factoryType + ", gimsUserType="
				+ gimsUserType + ", limitBandWidth=" + limitBandWidth
				+ ", linkMan=" + linkMan + ", netWorkType=" + netWorkType
				+ ", radiusType=" + radiusType + ", rangeType=" + rangeType
				+ ", supplyType=" + supplyType + ", telnum=" + telnum + "]";
	}

}