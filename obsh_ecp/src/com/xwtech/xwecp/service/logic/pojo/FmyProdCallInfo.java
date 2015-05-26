package com.xwtech.xwecp.service.logic.pojo;

public class FmyProdCallInfo {
	private String packName;			// 套餐名称
	private String freeitemName;		// 资费子项量名称
	private Integer cyjth = 0;			// 成员间通话免费
	private Integer cyjgxyy = 0;		// 成员共享免费语音
	private Integer cyjgxwlan = 0;		// 成员共享免费WLAN
	private Integer cyjgxwlangprs = 0;	// 成员共享免费省内WLAN&GPRS
	private Integer cyjgxmy = 0;		// 成员共享免费漫游WLAN&GPRS
	private String phoneNum;			//号码
	private String userId; 	// 用户id
	private String typeId; //累计量ID
	
	

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getPackName() {
		return this.packName;
	}

	public void setCyjth(Integer cyjth) {
		this.cyjth = cyjth;
	}

	public Integer getCyjth() {
		return this.cyjth;
	}

	public void setCyjgxyy(Integer cyjgxyy) {
		this.cyjgxyy = cyjgxyy;
	}

	public Integer getCyjgxyy() {
		return this.cyjgxyy;
	}

	public void setCyjgxwlan(Integer cyjgxwlan) {
		this.cyjgxwlan = cyjgxwlan;
	}

	public Integer getCyjgxwlan() {
		return this.cyjgxwlan;
	}

	public void setCyjgxwlangprs(Integer cyjgxwlangprs) {
		this.cyjgxwlangprs = cyjgxwlangprs;
	}

	public Integer getCyjgxwlangprs() {
		return this.cyjgxwlangprs;
	}

	public void setCyjgxmy(Integer cyjgxmy) {
		this.cyjgxmy = cyjgxmy;
	}

	public Integer getCyjgxmy() {
		return this.cyjgxmy;
	}

	public String getFreeitemName() {
		return freeitemName;
	}

	public void setFreeitemName(String freeitemName) {
		this.freeitemName = freeitemName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}