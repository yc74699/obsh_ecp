package com.xwtech.xwecp.pojo;


/**
 * 原始订单表单
 * 
 * @author zhangbin
 * 
 */
public class OrderInitForm {
	/**
	 * 下单人信息
	 */
//	//预约订单ID
//	private String orderId;
//	
	//预约手机号码
	private String orderMobile;
	
	//地市编码
	private String cityCode;
	
	//品牌编码
	private String brandCode;
	
	//业务名称
	private String busiName;
	
	//营销案名称
	private String marketName;
	
	//业务系统编码（各渠道统一）或营销案系统ID（各渠道统一）
	private String busiNum;
	
	//预约渠道（01-网厅；02-掌厅；03-短厅）
	private String orderChannel;
	
	//生效方式（1-次日；2-次月)
	private String applyFlag;
	
	//申请时间
	private String orderTime;
	
	//处理完成时间
	private String doneTime;
	
	//处理状态（0-未处理；1-等待处理；2-处理中；3-处理成功；4-处理失败）
	private String state;
	
	//订单状态（0-无效；1-有效）
	private String orderState;
	
	//备注
	private String resBz;
	
	public String getResBz() {
		return resBz;
	}

	public void setResBz(String resBz) {
		this.resBz = resBz;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	//备注
	private String operateResult;
	
	//操作类型	1-	预约 2-	取消预约
	private String operType;
	
	//业务种类	1-	业务 2-	营销案
	private String busiType;

	//营业厅编码
	private String officeId;
	
	//预约办理日期 YYYYMMDD
	private String expectTime;
	
	//预约办理时间段 1-上午 2-下午 3-全天
	private String expectPeriod;
	
	public String getOrderMobile() {
		return orderMobile;
	}

	public void setOrderMobile(String orderMobile) {
		this.orderMobile = orderMobile;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBusiName() {
		return busiName;
	}

	public void setBusiName(String busiName) {
		this.busiName = busiName;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getBusiNum() {
		return busiNum;
	}

	public void setBusiNum(String busiNum) {
		this.busiNum = busiNum;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getApplyFlag() {
		return applyFlag;
	}

	public void setApplyFlag(String applyFlag) {
		this.applyFlag = applyFlag;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(String operateResult) {
		this.operateResult = operateResult;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(String expectTime) {
		this.expectTime = expectTime;
	}
	
	public String getExpectPeriod() {
		return expectPeriod;
	}

	public void setExpectPeriod(String expectPeriod) {
		this.expectPeriod = expectPeriod;
	}
	
	
	
}
