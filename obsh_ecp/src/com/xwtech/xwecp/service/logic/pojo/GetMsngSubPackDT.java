package com.xwtech.xwecp.service.logic.pojo;

/**
 * 用户加入的亲情组合及集团语音套餐查询 cc_cgetmsngsubpack 352
 * @author yuantao
 *
 */
public class GetMsngSubPackDT implements java.io.Serializable
{
	public GetMsngSubPackDT (){}
	
	public String package_user_id;
	
	public String package_type;
	
	public String package_code;
	
	public String package_use_date;
	
	public String package_end_date;
	
	public String package_state;
	
	public String package_change_date;
	
	public String package_history_srl;
	
	public String package_apply_date;
	
	public String package_level;
	
	public String usingPkgCode;
	
	public String canOpenPkgCode;
	
	public String useState;

	public String getPackage_user_id() {
		return package_user_id;
	}

	public void setPackage_user_id(String package_user_id) {
		this.package_user_id = package_user_id;
	}

	public String getPackage_type() {
		return package_type;
	}

	public void setPackage_type(String package_type) {
		this.package_type = package_type;
	}

	public String getPackage_code() {
		return package_code;
	}

	public void setPackage_code(String package_code) {
		this.package_code = package_code;
	}

	public String getPackage_use_date() {
		return package_use_date;
	}

	public void setPackage_use_date(String package_use_date) {
		this.package_use_date = package_use_date;
	}

	public String getPackage_end_date() {
		return package_end_date;
	}

	public void setPackage_end_date(String package_end_date) {
		this.package_end_date = package_end_date;
	}

	public String getPackage_state() {
		return package_state;
	}

	public void setPackage_state(String package_state) {
		this.package_state = package_state;
	}

	public String getPackage_change_date() {
		return package_change_date;
	}

	public void setPackage_change_date(String package_change_date) {
		this.package_change_date = package_change_date;
	}

	public String getPackage_history_srl() {
		return package_history_srl;
	}

	public void setPackage_history_srl(String package_history_srl) {
		this.package_history_srl = package_history_srl;
	}

	public String getPackage_apply_date() {
		return package_apply_date;
	}

	public void setPackage_apply_date(String package_apply_date) {
		this.package_apply_date = package_apply_date;
	}

	public String getPackage_level() {
		return package_level;
	}

	public void setPackage_level(String package_level) {
		this.package_level = package_level;
	}

	public String getUsingPkgCode() {
		return usingPkgCode;
	}

	public void setUsingPkgCode(String usingPkgCode) {
		this.usingPkgCode = usingPkgCode;
	}

	public String getCanOpenPkgCode() {
		return canOpenPkgCode;
	}

	public void setCanOpenPkgCode(String canOpenPkgCode) {
		this.canOpenPkgCode = canOpenPkgCode;
	}

	public String getUseState() {
		return useState;
	}

	public void setUseState(String useState) {
		this.useState = useState;
	}
}
