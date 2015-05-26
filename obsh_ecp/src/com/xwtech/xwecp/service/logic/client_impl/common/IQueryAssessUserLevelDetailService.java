package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040104Result;

/**
 * 评定用户星级明细
 * @author wang.huan
 *
 */
public interface IQueryAssessUserLevelDetailService {
	public QRY040104Result queryAssessLevelDetail(String subsid)  throws LIException;
}
