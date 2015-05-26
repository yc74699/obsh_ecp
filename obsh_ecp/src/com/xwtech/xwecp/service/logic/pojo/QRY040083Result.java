package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040083Result extends BaseServiceInvocationResult{

	private List<TbankDetail> tbankDetails = new ArrayList<TbankDetail>();

	public List<TbankDetail> getTbankDetails() {
		return tbankDetails;
	}

	public void setTbankDetails(List<TbankDetail> tbankDetails) {
		this.tbankDetails = tbankDetails;
	}
	
	
}
