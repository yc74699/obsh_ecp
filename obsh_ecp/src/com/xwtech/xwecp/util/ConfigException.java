/*
 * Copyright 2007 XWTECH INC. All Rights reserved
 * XWTECH INC.
 * 创建日期: 2007/12/03
 * 创建人：  黄毅
 * 修改履历：
 */
package com.xwtech.xwecp.util;

/**
 * <p>
 * 配置异常类<BR>
 * </p>
 * <p>
 * 当从配置文件获取数据异常时抛出此异常。
 * </p>
 * 
 * @author huangyi
 * @version 1.0
 */
public class ConfigException extends Exception {
	public ConfigException(String str) {
		super(str);
	}
}
