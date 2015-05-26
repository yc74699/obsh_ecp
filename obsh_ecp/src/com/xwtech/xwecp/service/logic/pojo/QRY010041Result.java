package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 根据CRM工号查询出员工姓名等详细信息实体类
 * @author xufan
 * 20140918
 */
public class QRY010041Result extends BaseServiceInvocationResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String staffname;//员工姓名
	private String servnumber;//手机号码
	private String staffidstatus;//工号状态
	private String reserved1;//预留字段1
	private String reserved2;//预留字段2
	private String reserved3;//预留字段3
	private List<Roleslist> roleslist=new ArrayList<Roleslist>();//角色列表
	public String getStaffname() {
		return staffname;
	}
	public List<Roleslist> getRoleslist() {
		return roleslist;
	}
	public void setRoleslist(List<Roleslist> roleslist) {
		this.roleslist = roleslist;
	}
	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}
	public String getServnumber() {
		return servnumber;
	}
	public void setServnumber(String servnumber) {
		this.servnumber = servnumber;
	}
	public String getStaffidstatus() {
		return staffidstatus;
	}
	public void setStaffidstatus(String staffidstatus) {
		this.staffidstatus = staffidstatus;
	}
	public String getReserved1() {
		return reserved1;
	}
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}
	public String getReserved2() {
		return reserved2;
	}
	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}
	public String getReserved3() {
		return reserved3;
	}
	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}
}
