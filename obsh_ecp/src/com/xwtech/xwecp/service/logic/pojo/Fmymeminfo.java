package com.xwtech.xwecp.service.logic.pojo;

/**
 * 订单提交子结点 - 家庭成员子节点
 * @author 仰孝庆
 * 2014 -5 -29
 */
public class Fmymeminfo {
	String memtel="";// 副号号码
	String memregion="";// 副号归属地
	String payplantype="";//  成员代付关系
	String memshortnum="";// 成员短号
	public Fmymeminfo() {
		super();
	}
	public Fmymeminfo(String memtel, String memregion, String payplantype,
			String memshortnum) {
		super();
		this.memtel = memtel;
		this.memregion = memregion;
		this.payplantype = payplantype;
		this.memshortnum = memshortnum;
	}
	
	//设置家庭V网信息
	public Fmymeminfo(String memtel, String memregion, String memshortnum)
	{
		super();
		this.memtel = memtel;
		this.memregion = memregion;
		this.memshortnum = memshortnum;
	}
	
	public String getMemtel() {
		return memtel;
	}
	public void setMemtel(String memtel) {
		this.memtel = memtel;
	}
	public String getMemregion() {
		return memregion;
	}
	public void setMemregion(String memregion) {
		this.memregion = memregion;
	}
	public String getPayplantype() {
		return payplantype;
	}
	public void setPayplantype(String payplantype) {
		this.payplantype = payplantype;
	}
	public String getMemshortnum() {
		return memshortnum;
	}
	public void setMemshortnum(String memshortnum) {
		this.memshortnum = memshortnum;
	}
	
	
}
