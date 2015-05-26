package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 高校迎新
 * 新增在线入网订单查询接口
 * @author wang.h
 *
 */
public class QRY610043Result extends BaseServiceInvocationResult implements Serializable {
	/**
	 * orderid	1	string	V20	订单号
telnum	1	string	V12	在线入网号码
studentno	1	string	V20	新生随机码
schoolno	1	string	V10	学校识别码
schoolname	1	string	V128	学校名称
prodinfo	1	string	V1024	产品信息
linkname	1	string	V32	收货人名称
linkaddr	1	string	V128	收货人地址
linknum	1	string	V15	收货人电话
deliveryno	1	string	V20	物流单号
delivername	1	string	V128	物流公司

	 */
	private String orderId;
	private String telNum;
	private String studentNo;
	private String schoolNo;
	private String schooolName;
	private String prodInfo;
	private String linkName;
	private String linkAddr;
	private String linkNum;
	private String deliveryNo;
	private String deliverName;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getSchoolNo() {
		return schoolNo;
	}
	public void setSchoolNo(String schoolNo) {
		this.schoolNo = schoolNo;
	}
	public String getSchooolName() {
		return schooolName;
	}
	public void setSchooolName(String schooolName) {
		this.schooolName = schooolName;
	}
	public String getProdInfo() {
		return prodInfo;
	}
	public void setProdInfo(String prodInfo) {
		this.prodInfo = prodInfo;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getLinkAddr() {
		return linkAddr;
	}
	public void setLinkAddr(String linkAddr) {
		this.linkAddr = linkAddr;
	}
	public String getLinkNum() {
		return linkNum;
	}
	public void setLinkNum(String linkNum) {
		this.linkNum = linkNum;
	}
	public String getDeliveryNo() {
		return deliveryNo;
	}
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	public String getDeliverName() {
		return deliverName;
	}
	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	
}
