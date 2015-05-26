package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.EdtinfoBean;

public class QRY050065Result extends BaseServiceInvocationResult
{
	private int ret_code;

	private List<EdtinfoBean> edtInfo = new ArrayList<EdtinfoBean>();

	public void setRet_code(int ret_code)
	{
		this.ret_code = ret_code;
	}

	public int getRet_code()
	{
		return this.ret_code;
	}

	public void setEdtInfo(List<EdtinfoBean> edtInfo)
	{
		this.edtInfo = edtInfo;
	}

	public List<EdtinfoBean> getEdtInfo()
	{
		return this.edtInfo;
	}

}