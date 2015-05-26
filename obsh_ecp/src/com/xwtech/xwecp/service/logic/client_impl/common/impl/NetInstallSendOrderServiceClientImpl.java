package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.INetInstallSendOrderService;
import com.xwtech.xwecp.service.logic.pojo.DEL040055Result;
import com.xwtech.xwecp.service.logic.pojo.OrderSendBeen;
import com.xwtech.xwecp.service.logic.pojo.QRY050074Result;

public class NetInstallSendOrderServiceClientImpl extends BaseClientServiceImpl
		implements INetInstallSendOrderService {


	public QRY050074Result netInstallSendOrder(String orderId,
			String orderDate, String custName, String certType, String certId,
			String certAddr, String telNum, String provId, String region,
			String mainProdid, String mainProdidName, String prodTempalteId,
			String actId, String packId, String busidPackId, String rewardList,
			String totalPrice, String privPrice, String orderSource,
			String notes) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY050074";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_orderId = new RequestParameter("orderId", orderId);
		__requestData.getParams().add(__param_orderId);
		RequestParameter __param_orderDate = new RequestParameter("orderDate", orderDate);
		__requestData.getParams().add(__param_orderDate);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_certType = new RequestParameter("certType", certType);
		__requestData.getParams().add(__param_certType);
		RequestParameter __param_certId = new RequestParameter("certId", certId);
		__requestData.getParams().add(__param_certId);
		RequestParameter __param_certAddr = new RequestParameter("certAddr", certAddr);
		__requestData.getParams().add(__param_certAddr);
		RequestParameter __param_telNum = new RequestParameter("telNum", telNum);
		__requestData.getParams().add(__param_telNum);
		RequestParameter __param_provId = new RequestParameter("provId", provId);
		__requestData.getParams().add(__param_provId);
		RequestParameter __param_region = new RequestParameter("region", region);
		__requestData.getParams().add(__param_region);
		RequestParameter __param_mainProdid = new RequestParameter("mainProdid", mainProdid);
		__requestData.getParams().add(__param_mainProdid);
		RequestParameter __param_mainProdidName = new RequestParameter("mainProdidName", mainProdidName);
		__requestData.getParams().add(__param_mainProdidName);
		RequestParameter __param_prodTempalteId = new RequestParameter("prodTempalteId", prodTempalteId);
		__requestData.getParams().add(__param_prodTempalteId);
		RequestParameter __param_actId = new RequestParameter("actId", actId);
		__requestData.getParams().add(__param_actId);
		
		RequestParameter __param_packId = new RequestParameter("packId", packId);
		__requestData.getParams().add(__param_packId);
		RequestParameter __param_busidPackId = new RequestParameter("busidPackId", busidPackId);
		__requestData.getParams().add(__param_busidPackId);
		RequestParameter __param_rewardList = new RequestParameter("rewardList", rewardList);
		__requestData.getParams().add(__param_rewardList);
		RequestParameter __param_totalPrice = new RequestParameter("totalPrice", totalPrice);
		__requestData.getParams().add(__param_totalPrice);
		RequestParameter __param_privPrice = new RequestParameter("privPrice", privPrice);
		__requestData.getParams().add(__param_privPrice);
		RequestParameter __param_orderSource = new RequestParameter("orderSource", orderSource);
		__requestData.getParams().add(__param_orderSource);
		RequestParameter __param_notes = new RequestParameter("notes", notes);
		__requestData.getParams().add(__param_notes);
		__msg.setData(__requestData);
		__msg.getHead().setUser(LIInvocationContext.USER);
		__msg.getHead().setPwd(LIInvocationContext.PWD);
		LIInvocationContext __ic = LIInvocationContext.getContext();
		if(__ic != null)
		{
			__msg.getHead().setOpType(__ic.getOpType());
			__msg.getHead().setUserMobile(__ic.getUserMobile());
			__msg.getHead().setUserCity(__ic.getUserCity());
			__msg.getHead().setBizCode(__ic.getBizCode());
			__msg.getHead().setUserBrand(__ic.getUserBrand());
			__msg.getHead().setOperId(__ic.getOperId());
			((RequestData)(__msg.getData())).setContext(__ic.getContextParameter());
		}
		String __requestXML = __msg.toString();
		QRY050074Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY050074Result)
			{
				__result = (QRY050074Result)__ret;
			}
			else
			{
				__ret = new DEL040055Result();
				__result.setResultCode(__ret.getResultCode());
				__result.setErrorCode(__ret.getErrorCode());
				__result.setErrorMessage(__ret.getErrorMessage());
			}
		}
		catch(Exception __e)
		{
			__errorStack = __e;
			throw new LIException(__e);
		}
		finally
		{
			long __endTime = System.currentTimeMillis();
			__client.log(__cmd, __client.getPlatformConnection().getRemoteURL(), __requestXML, __responseXML, __msg, __response, __beginTime, __endTime, __errorStack);
		}
		return __result;
	}

	public QRY050074Result netInstallSendOrder(OrderSendBeen orderBeen)
			throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY050074";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_orderId = new RequestParameter("orderId", orderBeen.getOrderId());
		__requestData.getParams().add(__param_orderId);
		RequestParameter __param_orderDate = new RequestParameter("orderDate", orderBeen.getOrderDate());
		__requestData.getParams().add(__param_orderDate);
		RequestParameter __param_custName = new RequestParameter("custName", orderBeen.getCustName());
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_certType = new RequestParameter("certType", orderBeen.getCertType());
		__requestData.getParams().add(__param_certType);
		RequestParameter __param_certId = new RequestParameter("certId", orderBeen.getCertId());
		__requestData.getParams().add(__param_certId);
		RequestParameter __param_certAddr = new RequestParameter("certAddr", orderBeen.getCertAddr());
		__requestData.getParams().add(__param_certAddr);
		RequestParameter __param_telNum = new RequestParameter("telNum", orderBeen.getTelNum());
		__requestData.getParams().add(__param_telNum);
		RequestParameter __param_provId = new RequestParameter("provId", orderBeen.getProvId());
		__requestData.getParams().add(__param_provId);
		RequestParameter __param_region = new RequestParameter("region", orderBeen.getRegion());
		__requestData.getParams().add(__param_region);
		RequestParameter __param_mainProdid = new RequestParameter("mainProdid", orderBeen.getMainProdid());
		__requestData.getParams().add(__param_mainProdid);
		RequestParameter __param_mainProdidName = new RequestParameter("mainProdidName", orderBeen.getMainProdidName());
		__requestData.getParams().add(__param_mainProdidName);
		RequestParameter __param_prodTempalteId = new RequestParameter("prodTempalteId", orderBeen.getProdTempalteId());
		__requestData.getParams().add(__param_prodTempalteId);
		RequestParameter __param_actId = new RequestParameter("actId", orderBeen.getActId());
		__requestData.getParams().add(__param_actId);
		RequestParameter __param_privId = new RequestParameter("privId", orderBeen.getPrivId());
		__requestData.getParams().add(__param_privId);
		RequestParameter __param_packId = new RequestParameter("packId", orderBeen.getPackId());
		__requestData.getParams().add(__param_packId);
		RequestParameter __param_busidPackId = new RequestParameter("busidPackId", orderBeen.getBusidPackId());
		__requestData.getParams().add(__param_busidPackId);
		RequestParameter __param_rewardList = new RequestParameter("rewardList", orderBeen.getRewardList());
		__requestData.getParams().add(__param_rewardList);
		RequestParameter __param_totalPrice = new RequestParameter("totalPrice", orderBeen.getTotalPrice());
		__requestData.getParams().add(__param_totalPrice);
		RequestParameter __param_privPrice = new RequestParameter("privPrice", orderBeen.getPrivPrice());
		__requestData.getParams().add(__param_privPrice);
		RequestParameter __param_orderSource = new RequestParameter("orderSource", orderBeen.getOrderSource());
		__requestData.getParams().add(__param_orderSource);
		RequestParameter __param_notes = new RequestParameter("notes", orderBeen.getNotes());
		__requestData.getParams().add(__param_notes);
		__msg.setData(__requestData);
		__msg.getHead().setUser(LIInvocationContext.USER);
		__msg.getHead().setPwd(LIInvocationContext.PWD);
		LIInvocationContext __ic = LIInvocationContext.getContext();
		if(__ic != null)
		{
			__msg.getHead().setOpType(__ic.getOpType());
			__msg.getHead().setUserMobile(__ic.getUserMobile());
			__msg.getHead().setUserCity(__ic.getUserCity());
			__msg.getHead().setBizCode(__ic.getBizCode());
			__msg.getHead().setUserBrand(__ic.getUserBrand());
			__msg.getHead().setOperId(__ic.getOperId());
			((RequestData)(__msg.getData())).setContext(__ic.getContextParameter());
		}
		String __requestXML = __msg.toString();
		QRY050074Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY050074Result)
			{
				__result = (QRY050074Result)__ret;
			}
			else
			{
				__ret = new DEL040055Result();
				__result.setResultCode(__ret.getResultCode());
				__result.setErrorCode(__ret.getErrorCode());
				__result.setErrorMessage(__ret.getErrorMessage());
			}
		}
		catch(Exception __e)
		{
			__errorStack = __e;
			throw new LIException(__e);
		}
		finally
		{
			long __endTime = System.currentTimeMillis();
			__client.log(__cmd, __client.getPlatformConnection().getRemoteURL(), __requestXML, __responseXML, __msg, __response, __beginTime, __endTime, __errorStack);
		}
		return __result;
	}


}
