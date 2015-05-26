package com.xwtech.xwecp.memcached;

/**
 * Memchache KEY
 * @author Wayne Gong
 *
 */
public interface MemCachedKey
{
	//错误信息对照表KEY
	String KEY_T_ERR_MAPPING = "T_ERR_MAPPING";
	
	//缓存数据保存时间
	long KEY_EXPIREINSECOND = 259200;
	
	//根据地区查询业务信息
	String KEY_GET_BUSINESS_BY_AREA = "getBusinessByArea";
	
	//根据品牌查询业务信息
	String KEY_GET_BUSINESS_BY_BRAND = "getBusinessByBrand";
	
	//判断该地区是否在给定的时间内是否开通了该业务
	String KEY_HAS_BUSINESS_FORM_AREA = "hasBusinessFormArea";
	
	//判断该品牌是否在给定的时间内是否开通了该业务
	String KEY_HAS_BUSINESS_FORM_BRAND = "hasBusinessFormBrand";
	
	//根据Boss提供的地区code查找本地保存的数据
	String KEY_GET_AREA_NUM_FORM_BOSS_CODE = "getAreaNumFormBossCode";
	
	//根据Boss提供的地区code查找本地保存的数据
	String KEY_GET_BRAND_NUM_FORM_BOSS_CODE = "getBrandNumFormBossCode";
}
