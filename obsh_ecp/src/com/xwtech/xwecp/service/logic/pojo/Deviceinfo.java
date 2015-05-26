package com.xwtech.xwecp.service.logic.pojo;
/**
 * 订单提交子结点 - 家庭设备子节点
 * @author 仰孝庆
 * 2014 -5 -29
 */
public class Deviceinfo {
	String deviceclass="";//  设备大类
	String restypeid="";// 设备小类
	String getmode="";// 终端发放方式
	String bandtype="";// 归属的有线业务类型
	public Deviceinfo() {
		super();
	}
	public Deviceinfo(String deviceclass, String restypeid, String getmode,
			String bandtype) {
		super();
		this.deviceclass = deviceclass;
		this.restypeid = restypeid;
		this.getmode = getmode;
		this.bandtype = bandtype;
	}
	public String getDeviceclass() {
		return deviceclass;
	}
	public void setDeviceclass(String deviceclass) {
		this.deviceclass = deviceclass;
	}
	public String getRestypeid() {
		return restypeid;
	}
	public void setRestypeid(String restypeid) {
		this.restypeid = restypeid;
	}
	public String getGetmode() {
		return getmode;
	}
	public void setGetmode(String getmode) {
		this.getmode = getmode;
	}
	public String getBandtype() {
		return bandtype;
	}
	public void setBandtype(String bandtype) {
		this.bandtype = bandtype;
	}
	
	
}
