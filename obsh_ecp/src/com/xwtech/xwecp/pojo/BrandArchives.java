package com.xwtech.xwecp.pojo;

import java.io.Serializable;

/**
 * 品牌档案信息
 * @author 吴宗德
 *
 */
public class BrandArchives implements Serializable {
	/**
	 * 品牌编码
	 */
	private String brandNum;
	
	/**
	 * 品牌名称
	 */
	private String brandName;
	
	/**
	 * boss编码
	 */
	private String bossCode;

	public String getBossCode() {
		return bossCode;
	}

	public void setBossCode(String bossCode) {
		this.bossCode = bossCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandNum() {
		return brandNum;
	}

	public void setBrandNum(String brandNum) {
		this.brandNum = brandNum;
	}

}
