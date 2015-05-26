package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 高校迎新
 * 通过新生随机码查询可用的在线入网号码
 * @author wang.h
 *
 */
public class QRY610044Result extends BaseServiceInvocationResult implements Serializable {
	/**
	 * content	usabletellist	1	struct	—	号码信息
		usabletellist	usableteldt	*	struct	—	号码信息列表
		usableteldt	telnum	1	string	V12	在线入网号码
	 */
	private List<Usableteldt>  usableteldtList = new ArrayList<Usableteldt>();

	public List<Usableteldt> getUsableteldtList() {
		return usableteldtList;
	}

	public void setUsableteldtList(List<Usableteldt> usableteldtList) {
		this.usableteldtList = usableteldtList;
	}
	
	
	
}
