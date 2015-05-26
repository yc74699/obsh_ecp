package com.xwtech.xwecp.interfaces;

import java.util.List;
import com.xwtech.xwecp.exception.BaseException;
import com.xwtech.xwecp.msg.RequestParameter;

/**
 * @author        :  xmchen
 * @Create Date   :  2013-7-16
 */
public interface InterfaceBase {
	public final static String RESULT_CODE = "resultCode";
	public final static String SUCCESS = "0";
	public final static String FAIL = "1";
	
	public List<RequestParameter> execute(String requestXML) throws BaseException;
}
