package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY610037Result;

public interface IQueryTelnumSchoolService {

	public QRY610037Result qryTelnumSchool(String telnum,String schoolcode) throws LIException;
}
