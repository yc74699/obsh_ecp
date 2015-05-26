package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL610049Result;

/**
 * 高校迎新
 * 新增在线入网号码加锁加锁接口
 * @author wang.h
 *
 */
public interface ILockCTelNumService {
	public DEL610049Result lockCTelnum(String telNum, String operType) throws LIException;
}
