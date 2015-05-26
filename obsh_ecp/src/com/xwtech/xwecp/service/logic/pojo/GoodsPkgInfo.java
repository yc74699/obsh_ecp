package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


public class GoodsPkgInfo implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	private String goodsId;

	private String goodsName;

	private String goodsType;

	private String bossId;
	public void setGoodsId(String goodsId)
	{
		this.goodsId = goodsId;
	}

	public String getGoodsId()
	{
		return this.goodsId;
	}

	public void setGoodsName(String goodsName)
	{
		this.goodsName = goodsName;
	}

	public String getGoodsName()
	{
		return this.goodsName;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	
	public String getBossId()
	{
		return bossId;
	}

	
	public void setBossId(String bossId)
	{
		this.bossId = bossId;
	}
	
}