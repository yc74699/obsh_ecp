package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040055Result;

public interface IQueryOwnMonterBunsiService {
   public QRY040055Result queryOwnMonterBunsi(String phoneNum)throws LIException;
}
