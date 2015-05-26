package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.DEL011003Result;

public interface ISwitchProductService
{
	public DEL011003Result switchProduct(String phoneNum, String oldProId, String newProId, List<ProPackage> oldPackages, List<ProService> oldServices, List<ProIncrement> oldIncrements, List<ProPackage> closePackages, List<ProService> closeServices, List<ProIncrement> closeIncrements, List<ProPackage> openPackages, List<ProService> openServices, List<ProIncrement> openIncrements, List<ProSelf> openSelfs) throws LIException;

}