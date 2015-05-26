package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * QRY040002、QRY040048、QRY040065 合成 
 * @author YangXQ
 * 2014-6-30
 */
public class QRY040075Result extends BaseServiceInvocationResult
{
	private String balance = "0";	//余额 
	private String newBalance = "0";	//新业务余额 
	private String scoreChangeFlag = "0";	//积分 
	private String accountExpireDate = "0";	//有效期 
	private String scoreGiftScore = "0";	//转增积分 
	private String scoreExchangedScore = "0";	//已兑换积分 
	private String scoreLeavingsScore = "0";	//可兑换积分,用户剩余积分 
	private String scoreUserId = "";	        //用户标识 
	private List<ScoreYearNum> scoreYearNum = new ArrayList<ScoreYearNum>();	//cscoreinfo_dt的字段列表 
	private String zoneMvaleTotalMvalue = "0";	  //客户M值 
	private String zoneMvlaueMvalue = "0";	      //价值M值 
	private String zoneMvalueLastMvalue = "0";	  //上年剩余回馈M值 
	private String zoneMvaluePreMvalue = "0";	  //前年客户M值 
	private String zoneMvaluePresentMvalue = "0"; //今年消费M值 
	private String zoneMvalueBountyMvalue = "0";  //今年奖励M值 
	private String zoneMvalueUsedMvalue = "0";	//已使用回馈M值 
	private String isUnlimitedBandwidth; 	//1：用户订购20流量封顶业务0：未订购 
	private String isPlayAt;	            //1：用户订购随E玩业务0：未订购 
	private String isSpecilFlag;            //1：用户订购流量季包、半年包0：未订购2：用户开通B业务3:用户开通C业务 
	private String isHalfFlag;	 	        //1：用户订购4G半包套餐或随E玩套餐 0：未订购 
	private String useFlux = "0";		    //用户当月流量使用情况 
	private HalfFlow halfFlow;              //半包类套餐使用情况 
	private PackageFlow packageFlow;        //全包类套餐使用情况 
	private String twoNetFlux;   //	2G网络流量使用 
	private String threeNetFlux; //	3G网络流量使用 
	private String fourNetFlux;	 //	4G网络流量使用 
	private int flagJIBAO; //季包标识
	
	public int getFlagJIBAO() {
		return flagJIBAO;
	}
	public void setFlagJIBAO(int flagJIBAO) {
		this.flagJIBAO = flagJIBAO;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getNewBalance() {
		return newBalance;
	}
	public void setNewBalance(String newBalance) {
		this.newBalance = newBalance;
	}
	public String getScoreChangeFlag() {
		return scoreChangeFlag;
	}
	public void setScoreChangeFlag(String scoreChangeFlag) {
		this.scoreChangeFlag = scoreChangeFlag;
	}
	public String getAccountExpireDate() {
		return accountExpireDate;
	}
	public void setAccountExpireDate(String accountExpireDate) {
		this.accountExpireDate = accountExpireDate;
	}
	public String getScoreGiftScore() {
		return scoreGiftScore;
	}
	public void setScoreGiftScore(String scoreGiftScore) {
		this.scoreGiftScore = scoreGiftScore;
	}
	public String getScoreExchangedScore() {
		return scoreExchangedScore;
	}
	public void setScoreExchangedScore(String scoreExchangedScore) {
		this.scoreExchangedScore = scoreExchangedScore;
	}
	public String getScoreLeavingsScore() {
		return scoreLeavingsScore;
	}
	public void setScoreLeavingsScore(String scoreLeavingsScore) {
		this.scoreLeavingsScore = scoreLeavingsScore;
	}
	public String getScoreUserId() {
		return scoreUserId;
	}
	public void setScoreUserId(String scoreUserId) {
		this.scoreUserId = scoreUserId;
	}
	public List<ScoreYearNum> getScoreYearNum() {
		return scoreYearNum;
	}
	public void setScoreYearNum(List<ScoreYearNum> scoreYearNum) {
		this.scoreYearNum = scoreYearNum;
	}
	public String getZoneMvaleTotalMvalue() {
		return zoneMvaleTotalMvalue;
	}
	public void setZoneMvaleTotalMvalue(String zoneMvaleTotalMvalue) {
		this.zoneMvaleTotalMvalue = zoneMvaleTotalMvalue;
	}
	public String getZoneMvlaueMvalue() {
		return zoneMvlaueMvalue;
	}
	public void setZoneMvlaueMvalue(String zoneMvlaueMvalue) {
		this.zoneMvlaueMvalue = zoneMvlaueMvalue;
	}
	public String getZoneMvalueLastMvalue() {
		return zoneMvalueLastMvalue;
	}
	public void setZoneMvalueLastMvalue(String zoneMvalueLastMvalue) {
		this.zoneMvalueLastMvalue = zoneMvalueLastMvalue;
	}
	public String getZoneMvaluePreMvalue() {
		return zoneMvaluePreMvalue;
	}
	public void setZoneMvaluePreMvalue(String zoneMvaluePreMvalue) {
		this.zoneMvaluePreMvalue = zoneMvaluePreMvalue;
	}
	public String getZoneMvaluePresentMvalue() {
		return zoneMvaluePresentMvalue;
	}
	public void setZoneMvaluePresentMvalue(String zoneMvaluePresentMvalue) {
		this.zoneMvaluePresentMvalue = zoneMvaluePresentMvalue;
	}
	public String getZoneMvalueBountyMvalue() {
		return zoneMvalueBountyMvalue;
	}
	public void setZoneMvalueBountyMvalue(String zoneMvalueBountyMvalue) {
		this.zoneMvalueBountyMvalue = zoneMvalueBountyMvalue;
	}
	public String getZoneMvalueUsedMvalue() {
		return zoneMvalueUsedMvalue;
	}
	public void setZoneMvalueUsedMvalue(String zoneMvalueUsedMvalue) {
		this.zoneMvalueUsedMvalue = zoneMvalueUsedMvalue;
	}
	public String getIsUnlimitedBandwidth() {
		return isUnlimitedBandwidth;
	}
	public void setIsUnlimitedBandwidth(String isUnlimitedBandwidth) {
		this.isUnlimitedBandwidth = isUnlimitedBandwidth;
	}
	public String getIsPlayAt() {
		return isPlayAt;
	}
	public void setIsPlayAt(String isPlayAt) {
		this.isPlayAt = isPlayAt;
	}
	public String getUseFlux() {
		return useFlux;
	}
	public void setUseFlux(String useFlux) {
		this.useFlux = useFlux;
	}
	public HalfFlow getHalfFlow() {
		return halfFlow;
	}
	public void setHalfFlow(HalfFlow halfFlow) {
		this.halfFlow = halfFlow;
	}
	public PackageFlow getPackageFlow() {
		return packageFlow;
	}
	public void setPackageFlow(PackageFlow packageFlow) {
		this.packageFlow = packageFlow;
	}
	public String getIsSpecilFlag() {
		return isSpecilFlag;
	}
	public void setIsSpecilFlag(String isSpecilFlag) {
		this.isSpecilFlag = isSpecilFlag;
	}
	public String getIsHalfFlag() {
		return isHalfFlag;
	}
	public void setIsHalfFlag(String isHalfFlag) {
		this.isHalfFlag = isHalfFlag;
	}
	public String getTwoNetFlux() {
		return twoNetFlux;
	}
	public void setTwoNetFlux(String twoNetFlux) {
		this.twoNetFlux = twoNetFlux;
	}
	public String getThreeNetFlux() {
		return threeNetFlux;
	}
	public void setThreeNetFlux(String threeNetFlux) {
		this.threeNetFlux = threeNetFlux;
	}
	public String getFourNetFlux() {
		return fourNetFlux;
	}
	public void setFourNetFlux(String fourNetFlux) {
		this.fourNetFlux = fourNetFlux;
	}
	
}