package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.tbank.rpc.message.TBankRequestMessage;
import com.xwtech.tbank.rpc.message.TBankResponseMessage;
import com.xwtech.tbank.rpc.message.request.BankDetailAccountRequestContent;
import com.xwtech.tbank.rpc.message.response.TBankBillDetail;
import com.xwtech.tbank.rpc.message.response.TBankQueryAccountResponseContent;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040083Result;
import com.xwtech.xwecp.service.logic.pojo.TbankDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.TbankJsonUtil;
/**
 * 流量银行存取明细查询
 * @author Mr Ou
 * @date 2014-07-18
 */
public class QueryTbankDetailInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryTbankDetailInvocation.class);
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) 
	{
		QRY040083Result result = new QRY040083Result();
		//请求入参
		String phoneNum =(String)getParameters(params,"phoneNum");//手机号码
		String oprType =(String)getParameters(params,"oprType");//业务类型 流量宝、话费宝
		String drawType =(String)getParameters(params,"drawType");//查询类型 存入、支取
		String beginDate =(String)getParameters(params,"beginDate");//开始时间
		String endDate =(String)getParameters(params,"endDate");//结束时间
		
		//流量银行接口方法
		String method = "queryDetailList";
		//组装报文对象
		TBankRequestMessage trm = new TBankRequestMessage();
		BankDetailAccountRequestContent tarContent = new BankDetailAccountRequestContent();
		
		tarContent.setMobileNo(phoneNum);
		tarContent.setAccountType(oprType);
		tarContent.setDrawType(drawType);
		tarContent.setStart(beginDate);
		tarContent.setEnd(endDate);
		tarContent.setQueryTime(new Date());
		trm.setSvcCont(tarContent);
		//发送请求与解析返回json
		result = getTbankResult(method,trm,accessId,params,result);
		
		return result;
	}
	/**
	 * 组装json报文字符串、发送请求与解析
	 * @param method
	 * @param trm
	 * @param accessId
	 * @param params
	 * @param result
	 * @return QRY040083Result
	 */
	protected QRY040083Result getTbankResult(String method,TBankRequestMessage trm,String accessId,List<RequestParameter> params,QRY040083Result result)
	{
		String reqJson = "";
		String rsJson = "";
		try
		{
			//组装请求的json字符串
			reqJson = TbankJsonUtil.internalWriteRequest(method,new Object[] { trm }, "-5797640080114934769");
			logger.info(" ====== 发送流量银行存取明细查询JSON报文 ======\n" + reqJson);
			if(null != reqJson && !"".equals(reqJson))
			{
			   rsJson = (String) this.remote.callJsonRemote(new StringTeletext(reqJson, accessId, "", this.generateCity(params)));
			   logger.info(" ====== 返回流量银行存取明细查询JSON报文 ======\n" + rsJson);
			   //解析返回json字符串
			   TBankResponseMessage readResponse = (TBankResponseMessage)TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(rsJson.getBytes("UTF-8")));
			   result.setResultCode(readResponse.getResponse().getRspType());
			   result.setErrorCode(readResponse.getResponse().getRspCode());
			   result.setErrorMessage(readResponse.getResponse().getRspDesc());
			   //根据返回json报文中多态对象取值
			   TBankQueryAccountResponseContent tBankResponse = (TBankQueryAccountResponseContent)readResponse.getSvcCont();
			   
			   List<TbankDetail> tbankDetails = result.getTbankDetails();
			   TbankDetail tbankDetail = null;
			   if(null != tBankResponse && null != tBankResponse.getBillList())
			   {
				   for(TBankBillDetail detail :tBankResponse.getBillList())
				   {
					   tbankDetail = new TbankDetail();
					   
					   tbankDetail.setChildBookId(detail.getChildBookId());
					   tbankDetail.setMobileNo(detail.getMobileNo());
					   tbankDetail.setStatus(detail.getStatus()+"");
					   tbankDetail.setDetailType(detail.getDetailType());
					   tbankDetail.setAmount(detail.getAmount());
					   
					   tbankDetail.setCreateTime(dealWithDate(detail.getCreateTime().getTime()+""));
					   tbankDetail.setOperateTime(dealWithDate(detail.getOperateTime().getTime()+""));
					   
					   tbankDetails.add(tbankDetail);
				   }
				   result.setTbankDetails(tbankDetails);
			   }
			   return result;
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			logger.info(" ====== 返回流量银行存取明细查询JSON报文错误 ======\n",e);
		}
		return null;
	}
	//将流量银行返回的毫秒的时间转换成 yyyy-MM-dd时间格式
	public static String dealWithDate(String StrTime)
	{
		String strDate = "";
		Date dat = new Date(Long.parseLong(StrTime));  
	    GregorianCalendar gc = new GregorianCalendar();   
		gc.setTime(dat);  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		strDate = format.format(gc.getTime());
		return strDate;
	}

}
