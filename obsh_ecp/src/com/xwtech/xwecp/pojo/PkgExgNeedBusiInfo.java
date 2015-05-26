package com.xwtech.xwecp.pojo;

import java.io.Serializable;

public class PkgExgNeedBusiInfo implements Serializable {
	
	//渠道编码
	private String channelNum;
	//地市编码
	private String areaNum;
	//ECP系统定义的套餐编码
	private String pkgNumEcp;
	//ECP系统定义的必选业务编码
	private String busiNumEcp;
	//BOSS侧的业务定义1
	private String busiNumBoss1;
	//BOSS侧的业务定义2
	private String busiNumBoss2;
	//BOSS侧的业务定义3
	private String busiNumBoss3;
	//BOSS侧的业务定义4
	private String busiNumBoss4;
	//BOSS侧的业务定义5
	private String busiNumBoss5;
	//BOSS侧的业务定义6
	private String busiNumBoss6;
	//备注说明
	private String bz;
	//业务名称
	private String bizName;
	
	public String getBizName() {
		return bizName;
	}
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	public String getBusiNumBoss1() {
		return busiNumBoss1;
	}
	public void setBusiNumBoss1(String busiNumBoss1) {
		this.busiNumBoss1 = busiNumBoss1;
	}
	public String getBusiNumBoss2() {
		return busiNumBoss2;
	}
	public void setBusiNumBoss2(String busiNumBoss2) {
		this.busiNumBoss2 = busiNumBoss2;
	}
	public String getBusiNumBoss3() {
		return busiNumBoss3;
	}
	public void setBusiNumBoss3(String busiNumBoss3) {
		this.busiNumBoss3 = busiNumBoss3;
	}
	public String getBusiNumBoss4() {
		return busiNumBoss4;
	}
	public void setBusiNumBoss4(String busiNumBoss4) {
		this.busiNumBoss4 = busiNumBoss4;
	}
	public String getBusiNumBoss5() {
		return busiNumBoss5;
	}
	public void setBusiNumBoss5(String busiNumBoss5) {
		this.busiNumBoss5 = busiNumBoss5;
	}
	public String getBusiNumBoss6() {
		return busiNumBoss6;
	}
	public void setBusiNumBoss6(String busiNumBoss6) {
		this.busiNumBoss6 = busiNumBoss6;
	}
	public String getBusiNumEcp() {
		return busiNumEcp;
	}
	public void setBusiNumEcp(String busiNumEcp) {
		this.busiNumEcp = busiNumEcp;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getChannelNum() {
		return channelNum;
	}
	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}
	public String getPkgNumEcp() {
		return pkgNumEcp;
	}
	public void setPkgNumEcp(String pkgNumEcp) {
		this.pkgNumEcp = pkgNumEcp;
	}

}
