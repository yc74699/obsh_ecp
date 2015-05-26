package com.xwtech.xwecp.pojo;

public class FluxPkgInfo {
	
	//套餐编码
	private String pkgCode;
	//套餐大类
	private String pkgType;
	//套餐名称
	private String pkgName;
	//大类名称
	private String typeName;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	//单位（4：流量 3：时长）
	private String pkgUnit;
	public String getPkgCode() {
		return pkgCode;
	}
	public void setPkgCode(String pkgCode) {
		this.pkgCode = pkgCode;
	}
	
	public String getPkgType() {
		return pkgType;
	}
	public void setPkgType(String pkgType) {
		this.pkgType = pkgType;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getPkgUnit() {
		return pkgUnit;
	}
	public void setPkgUnit(String pkgUnit) {
		this.pkgUnit = pkgUnit;
	}
	

}
