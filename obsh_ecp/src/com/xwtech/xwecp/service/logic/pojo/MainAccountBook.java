package com.xwtech.xwecp.service.logic.pojo;

public class MainAccountBook {
   
	//主账本id
	private String bookId;
	//主账本余额
	private String balance;
	//主账本状态
	private String status;
	//主账本所属账户
	private String accountId;
	
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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	
}
