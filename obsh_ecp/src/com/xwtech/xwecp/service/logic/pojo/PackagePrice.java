package com.xwtech.xwecp.service.logic.pojo;

public class PackagePrice {
	private String package_code;
	private String price;
	private String pkgTime;
	
	public String getPkgTime() {
		return pkgTime;
	}
	public void setPkgTime(String pkgTime) {
		this.pkgTime = pkgTime;
	}
	public void setPackage_code(String packageCode) {
		package_code = packageCode;
	}
	public String getPackage_code() {
		return package_code;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return price;
	}
}
