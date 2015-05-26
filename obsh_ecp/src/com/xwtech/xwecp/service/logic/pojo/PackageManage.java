package com.xwtech.xwecp.service.logic.pojo;


public class PackageManage
{
	private String packageCode;

	private String package_type;

	private String package_name;

	private String package_number;
	
	private String prodname;
	private String prodid;
	
	public void setProdid(String prodid) {
		this.prodid = prodid;
	}
	
	public String getProdid() {
		return prodid;
	}
	public void setProdname(String prodname) {
		this.prodname = prodname;
	}
	public String getProdname() {
		return prodname;
	}

	public void setPackageCode(String packageCode)
	{
		this.packageCode = packageCode;
	}

	public String getPackageCode()
	{
		return this.packageCode;
	}

	public void setPackage_type(String package_type)
	{
		this.package_type = package_type;
	}

	public String getPackage_type()
	{
		return this.package_type;
	}

	public void setPackage_name(String package_name)
	{
		this.package_name = package_name;
	}

	public String getPackage_name()
	{
		return this.package_name;
	}

	public void setPackage_number(String package_number)
	{
		this.package_number = package_number;
	}

	public String getPackage_number()
	{
		return this.package_number;
	}

}