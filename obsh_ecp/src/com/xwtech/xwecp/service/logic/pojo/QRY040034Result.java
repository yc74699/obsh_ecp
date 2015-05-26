package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.pojo.AcctInfo;
import com.xwtech.xwecp.service.logic.pojo.PayplanInfo;
import com.xwtech.xwecp.service.logic.pojo.MailInfo;

public class QRY040034Result extends BaseServiceInvocationResult
{
	private String acct_id;

	private AcctInfo acct_crontent;

	private PayplanInfo payplan_content;

	private MailInfo mail_content;

	public void setAcct_id(String acct_id)
	{
		this.acct_id = acct_id;
	}

	public String getAcct_id()
	{
		return this.acct_id;
	}

	public void setAcct_crontent(AcctInfo acct_crontent)
	{
		this.acct_crontent = acct_crontent;
	}

	public AcctInfo getAcct_crontent()
	{
		return this.acct_crontent;
	}

	public void setPayplan_content(PayplanInfo payplan_content)
	{
		this.payplan_content = payplan_content;
	}

	public PayplanInfo getPayplan_content()
	{
		return this.payplan_content;
	}

	public void setMail_content(MailInfo mail_content)
	{
		this.mail_content = mail_content;
	}

	public MailInfo getMail_content()
	{
		return this.mail_content;
	}

}