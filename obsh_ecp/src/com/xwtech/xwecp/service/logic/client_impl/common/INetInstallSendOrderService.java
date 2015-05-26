package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.OrderSendBeen;
import com.xwtech.xwecp.service.logic.pojo.QRY050074Result;

public interface INetInstallSendOrderService {

	/*public QRY050074Result netInstallSendOrder(String orderId,
			String orderDate, String custName, String certType, String certId,
			String certAddr, String telNum, String provId, String region,
			String mainProdid, String mainProdidName, String prodTempalteId,
			String actId, String packId, String busidPackId, String rewardList,
			String totalPrice, String privPrice, String orderSource,
			String notes) throws LIException;*/
	
	public QRY050074Result netInstallSendOrder(OrderSendBeen orderBeen) throws LIException;

}