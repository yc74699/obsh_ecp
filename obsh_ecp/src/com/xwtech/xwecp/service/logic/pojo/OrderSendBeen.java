package com.xwtech.xwecp.service.logic.pojo;

public class OrderSendBeen {
	private String orderId;
	private String orderDate;
	private String custName;
	private String certType;
	/**证件类型
	 *  PLA军官证，ChuanminID	船民证
		IdCardWJ	外交人员身份证
		BMCRJCard	边民出入境通行证
		Conflict	资料冲突待补录证件
		EnteAggrCredential	事业单位法人登记证
		JuliuID	居留证
		CanjiID	残疾证
		HKMCPassport	港澳通行证
		BusinessLicence	营业执照
		DriverIC	驾驶证
		FuYuanZheng	退伍证
		HKIdCard	港澳身份证
		HuKouBu	户口簿
		IdCard	居民身份证
		IdTypeJSX	介绍信
		IdVipCard	VIP卡
		MarryUnkown	手机号(资料不全)
		PLA	军官证
		Passport	护照
		PolicePaper	武装警察身份证件
		RetNative	回乡证
		SoldierID	士兵证
		StudentID	学生证
		TaiBaoZheng	台胞证
		TeacherID	教师证
		TempId	临时身份证
		WorkID	工作证
	*/
	//证件ID，默认用身份证
	private String certId;
	//地址，至少16个字符
	private String certAddr;
	private String telNum;
	private String provId;
	private String region;
	private String mainProdid;
	private String mainProdidName;
	private String prodTempalteId;
	private String actId;
	private String packId;
	private String busidPackId;
	private String rewardList;
	private String totalPrice;
	private String privPrice;
	private String orderSource;
	private String notes;
	private String privId;
	public OrderSendBeen()
	{
		certType="IdCard";
		prodTempalteId="";
		actId="";
		packId="";
		busidPackId="";
		rewardList="";
		privPrice="";
		notes="";
		privId="";
	}
	public String getPrivId() {
		return privId;
	}

	public void setPrivId(String privId) {
		this.privId = privId;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}
	public String getCertAddr() {
		return certAddr;
	}
	public void setCertAddr(String certAddr) {
		this.certAddr = certAddr;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public String getProvId() {
		return provId;
	}
	public void setProvId(String provId) {
		this.provId = provId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getMainProdid() {
		return mainProdid;
	}
	public void setMainProdid(String mainProdid) {
		this.mainProdid = mainProdid;
	}
	public String getMainProdidName() {
		return mainProdidName;
	}
	public void setMainProdidName(String mainProdidName) {
		this.mainProdidName = mainProdidName;
	}
	public String getProdTempalteId() {
		return prodTempalteId;
	}
	public void setProdTempalteId(String prodTempalteId) {
		this.prodTempalteId = prodTempalteId;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	public String getPackId() {
		return packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}
	public String getBusidPackId() {
		return busidPackId;
	}
	public void setBusidPackId(String busidPackId) {
		this.busidPackId = busidPackId;
	}
	public String getRewardList() {
		return rewardList;
	}
	public void setRewardList(String rewardList) {
		this.rewardList = rewardList;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPrivPrice() {
		return privPrice;
	}
	public void setPrivPrice(String privPrice) {
		this.privPrice = privPrice;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

}
