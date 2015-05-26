package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040084Result;

/**
 * 20元封顶业务
 * @author YangXQ
 * 20140724
 */
public interface IQueryGPSFluxInfo20Service {
	public QRY040084Result queryGPSFluxInfo20(String phoneNum,String cycle)throws LIException;
}
