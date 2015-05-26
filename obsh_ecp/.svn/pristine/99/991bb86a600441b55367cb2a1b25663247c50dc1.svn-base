package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


public class FluxDetail implements Serializable
{
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	//增加套餐码
	private String pkgId;
	
	private String fluxPkgName;

	private String total;

	private String used;

	private String rateType;

	private String pkgType;
	//1是WLAN,0不是WLAN
	private String isWlan;
	/**新增：跨月套餐使用流量查询*/
	private int gprs_cumulate_type;
	private int begin_date;
	private int end_date;
	public int getGprs_cumulate_type() {
		return gprs_cumulate_type;
	}

	public void setGprs_cumulate_type(int gprsCumulateType) {
		gprs_cumulate_type = gprsCumulateType;
	}

	public int getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(int beginDate) {
		begin_date = beginDate;
	}

	public int getEnd_date() {
		return end_date;
	}

	public void setEnd_date(int endDate) {
		end_date = endDate;
	}

	public String getIsWlan() {
		return isWlan;
	}

	public void setIsWlan(String isWlan) {
		this.isWlan = isWlan;
	}

	public void setFluxPkgName(String fluxPkgName)
	{
		this.fluxPkgName = fluxPkgName;
	}

	public String getFluxPkgName()
	{
		return this.fluxPkgName;
	}

	public void setTotal(String total)
	{
		this.total = total;
	}

	public String getTotal()
	{
		return this.total;
	}

	public void setUsed(String used)
	{
		this.used = used;
	}

	public String getUsed()
	{
		return this.used;
	}

	public void setRateType(String rateType)
	{
		this.rateType = rateType;
	}

	public String getRateType()
	{
		return this.rateType;
	}

	public void setPkgType(String pkgType)
	{
		this.pkgType = pkgType;
	}

	public String getPkgType()
	{
		return this.pkgType;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String toString() {
		return "FluxDetail [fluxPkgName=" + fluxPkgName + ", isWlan=" + isWlan
				+ ", pkgId=" + pkgId + ", pkgType=" + pkgType + ", rateType="
				+ rateType + ", total=" + total + ", used=" + used + "]";
	}
	
	// 显示总量，和余量
	public String show() { 
		return  fluxPkgName + ", 业务编码 =" + pkgId + ", 总流量=" + total + ", 剩余流量=" + (Integer.parseInt(total)-Integer.parseInt(used)) + "]";
	}
}