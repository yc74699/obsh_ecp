package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
/**
 * 红包记录实体类
 * 
 * @author xufan
 * 2014-03-27
 */
public class RedPack implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String giveuserid;
	
	private String givemsisdn;
	
	private String takemsisdn;
	
	private String takeuserid;
	
	private String givedate;
	
	private String takedate ;
	
	private String status;
	
	private String expiredate;

	private String package_code;
	private String donateoid;
	private String 	takeoid;
	
	public String getPackage_code() {
		return package_code;
	}

	public void setPackage_code(String package_code) {
		this.package_code = package_code;
	}

	public String getDonateoid() {
		return donateoid;
	}

	public void setDonateoid(String donateoid) {
		this.donateoid = donateoid;
	}

	public String getTakeoid() {
		return takeoid;
	}

	public void setTakeoid(String takeoid) {
		this.takeoid = takeoid;
	}

	public String getGiveuserid() {
		return giveuserid;
	}

	public void setGiveuserid(String giveuserid) {
		this.giveuserid = giveuserid;
	}

	public String getGivemsisdn() {
		return givemsisdn;
	}

	public void setGivemsisdn(String givemsisdn) {
		this.givemsisdn = givemsisdn;
	}

	public String getTakemsisdn() {
		return takemsisdn;
	}

	public void setTakemsisdn(String takemsisdn) {
		this.takemsisdn = takemsisdn;
	}

	public String getTakeuserid() {
		return takeuserid;
	}

	public void setTakeuserid(String takeuserid) {
		this.takeuserid = takeuserid;
	}

	public String getGivedate() {
		return givedate;
	}

	public void setGivedate(String givedate) {
		this.givedate = givedate;
	}

	public String getTakedate() {
		return takedate;
	}

	public void setTakedate(String takedate) {
		this.takedate = takedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}

	
	}