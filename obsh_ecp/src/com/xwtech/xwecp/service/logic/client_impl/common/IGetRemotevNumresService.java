package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040054Result;

public interface IGetRemotevNumresService {
   public QRY040054Result getRemotevNumres(String vnumCity,String maxCount)throws LIException;
}
