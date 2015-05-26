package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050028Result;

public interface IQueryPackageDependService
{
	public QRY050028Result queryPackageDepend(String phoneNum, String city, String proId, String oldPckCode, String newPckCode, String packageType) throws LIException;

}