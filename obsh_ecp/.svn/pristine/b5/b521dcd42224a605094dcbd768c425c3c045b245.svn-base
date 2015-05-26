package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 营销推荐专区信息
 * @author zhangb
 * @data  20150414
 */
public class ReferralsMarketInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bigclass;//推荐大类编码
	
	private String bigclassname;	//推荐大类名称
	
	private String smallclass; //推荐小类
	
	private String smallclassname;	//推荐小类名称
	
	private String actioncode;	//推荐活动编码 ty...
	
	private String subactid;	//子活动编码
	
	private String prodamount;	//推荐产品个数大于1时需要调用“获取活动可推荐产品”接口查询产品信息rectype（推荐类型）为1、4时有效。为3时，不需判断此字段
	
	private String rectype;	//推荐类型1：产品类推荐（支持推荐多个）3：营销案推荐（暂不支持推荐多个）4：产品包下产品推荐（支持推荐多个，如果有多个推荐产品，获取活动可推荐产品接口返回的产品信息中，也是有多个产品包的情况）
	
	private String prodid;	//产品编码
	
	private String prodpkgid;	//产品包编码 推荐类型为4产品编码生效 有产品包的时候提交需要传递2条数据调用5.3.1接口
	
	private String mainprodid;	//主体产品编码
	
	private String actid;	//营销方案批次编码
	
	private String levelid;		//营销方案批档次
	
	private String userseq;		//推荐流水号
	
	private String recommendInfo;		//推荐术语
	
	private String prodname;		//产品名称
	
	private String currusage;	//当前使用情况
	
	private String opertype;		//操作类型 0 可推荐 1 不可推荐 为空查询全部
    
	private List<CombineBean> comBineLs  = new ArrayList<CombineBean>();

	public String getBigclass() {
		return bigclass;
	}

	public void setBigclass(String bigclass) {
		this.bigclass = bigclass;
	}

	public String getBigclassname() {
		return bigclassname;
	}

	public void setBigclassname(String bigclassname) {
		this.bigclassname = bigclassname;
	}

	public String getSmallclass() {
		return smallclass;
	}

	public void setSmallclass(String smallclass) {
		this.smallclass = smallclass;
	}

	public String getSmallclassname() {
		return smallclassname;
	}

	public void setSmallclassname(String smallclassname) {
		this.smallclassname = smallclassname;
	}

	public String getActioncode() {
		return actioncode;
	}

	public void setActioncode(String actioncode) {
		this.actioncode = actioncode;
	}

	public String getSubactid() {
		return subactid;
	}

	public void setSubactid(String subactid) {
		this.subactid = subactid;
	}

	public String getProdamount() {
		return prodamount;
	}

	public void setProdamount(String prodamount) {
		this.prodamount = prodamount;
	}

	public String getRectype() {
		return rectype;
	}

	public void setRectype(String rectype) {
		this.rectype = rectype;
	}

	public String getProdid() {
		return prodid;
	}

	public void setProdid(String prodid) {
		this.prodid = prodid;
	}

	public String getProdpkgid() {
		return prodpkgid;
	}

	public void setProdpkgid(String prodpkgid) {
		this.prodpkgid = prodpkgid;
	}

	public String getMainprodid() {
		return mainprodid;
	}

	public void setMainprodid(String mainprodid) {
		this.mainprodid = mainprodid;
	}

	public String getActid() {
		return actid;
	}

	public void setActid(String actid) {
		this.actid = actid;
	}

	public String getLevelid() {
		return levelid;
	}

	public void setLevelid(String levelid) {
		this.levelid = levelid;
	}

	public String getUserseq() {
		return userseq;
	}

	public void setUserseq(String userseq) {
		this.userseq = userseq;
	}

	public String getRecommendInfo() {
		return recommendInfo;
	}

	public void setRecommendInfo(String recommendInfo) {
		this.recommendInfo = recommendInfo;
	}

	public String getProdname() {
		return prodname;
	}

	public void setProdname(String prodname) {
		this.prodname = prodname;
	}

	public String getCurrusage() {
		return currusage;
	}

	public void setCurrusage(String currusage) {
		this.currusage = currusage;
	}

	public String getOpertype() {
		return opertype;
	}

	public void setOpertype(String opertype) {
		this.opertype = opertype;
	}

	public List<CombineBean> getComBineLs() {
		return comBineLs;
	}

	public void setComBineLs(List<CombineBean> comBineLs) {
		this.comBineLs = comBineLs;
	}
	
}