package com.xwtech.xwecp.test;

/**
 * 订单提交子结点 -有线业务产品套餐子节点
 * @author 仰孝庆
 * 2014 -5 -29
 */
public class Productinfo {
	String prodid="";//  产品编码
	String pkgprodid="";// 归属的产品包编码
	String ispackage="";// 是否产品包
	String effecttype="";// 生效方式
	public Productinfo() {
		super();
	}
	public Productinfo(String prodid, String pkgprodid, String ispackage,
			String effecttype) {
		super();
		this.prodid = prodid;
		this.pkgprodid = pkgprodid;
		this.ispackage = ispackage;
		this.effecttype = effecttype;
	}
	public String getProdid() {
		return prodid;
	}
	public void setProdid(String prodid) {
		this.prodid = prodid;
	}
	public String getPkgprodid() {
		return pkgprodid;
	}
	public void setPkgprodid(String pkgprodid) {
		this.pkgprodid = pkgprodid;
	}
	public String getIspackage() {
		return ispackage;
	}
	public void setIspackage(String ispackage) {
		this.ispackage = ispackage;
	}
	public String getEffecttype() {
		return effecttype;
	}
	public void setEffecttype(String effecttype) {
		this.effecttype = effecttype;
	}
	
	
}
