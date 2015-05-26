package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY610037Result extends BaseServiceInvocationResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String isactive;
	
	private String issameschool;
	
	private String schoolname;

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getIssameschool() {
		return issameschool;
	}

	public void setIssameschool(String issameschool) {
		this.issameschool = issameschool;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	
	

}
