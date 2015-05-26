package com.xwtech.xwecp.service.logic.pojo;

public class ChildAccountBook {

	//子账本id
	private String childBookId;
	//子账本所属主账本
	private String bookId;
	//剩余流量
	private String balance;
	//支取流量
	private String withDrawlAmount;
	//子账本状态
	private String status;
	//记录子账本开始时间
	private String deadlineBegin;
	//记录子账本结束时间
	private String deadlineEnd;
	
	public String getChildBookId() {
		return childBookId;
	}
	public void setChildBookId(String childBookId) {
		this.childBookId = childBookId;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWithDrawlAmount() {
		return withDrawlAmount;
	}
	public void setWithDrawlAmount(String withDrawlAmount) {
		this.withDrawlAmount = withDrawlAmount;
	}
	public String getDeadlineBegin() {
		return deadlineBegin;
	}
	public void setDeadlineBegin(String deadlineBegin) {
		this.deadlineBegin = deadlineBegin;
	}
	public String getDeadlineEnd() {
		return deadlineEnd;
	}
	public void setDeadlineEnd(String deadlineEnd) {
		this.deadlineEnd = deadlineEnd;
	}
	
	
	
}
