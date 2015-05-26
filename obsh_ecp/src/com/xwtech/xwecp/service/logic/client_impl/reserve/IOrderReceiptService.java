package com.xwtech.xwecp.service.logic.client_impl.reserve;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.OrderReceiptInfo;
import com.xwtech.xwecp.service.logic.pojo.RES005Result;

public interface IOrderReceiptService {
   
   //预约订单修改
   public RES005Result orderReceipt(String orderId,OrderReceiptInfo orderReceiptInfo) throws LIException;
}
