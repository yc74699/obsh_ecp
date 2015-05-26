package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;

public class QRY010029Result extends BaseServiceInvocationResult {
	private String subSid;
	private Integer cycle;
	private List<PkgInfoBean> pkgInfoList = new ArrayList<PkgInfoBean>();
	private List<List<String>> dateStrList = new ArrayList<List<String>>();
	

	public void setSubSid(String subSid) {
		this.subSid = subSid;
	}

	public String getSubSid() {
		return this.subSid;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	public Integer getCycle() {
		return this.cycle;
	}

	public List<List<String>> getDateStrList() {
		return dateStrList;
	}
	public void setDateStrList(List<List<String>> dateStrList) {
		this.dateStrList = dateStrList;
	}
	public List<PkgInfoBean> getPkgInfoList() {
		return pkgInfoList;
	}
	public void setPkgInfoList(List<PkgInfoBean> pkgInfoList) {
		this.pkgInfoList = pkgInfoList;
	}
	
	
	

}