package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.tbank.rpc.message.TBankRequestMessage;
import com.xwtech.tbank.rpc.message.TBankResponseMessage;
import com.xwtech.tbank.rpc.message.request.TBankQueryAccountRequestContent;
import com.xwtech.tbank.rpc.message.response.TBankMainAccountBook;
import com.xwtech.tbank.rpc.message.response.TBankQueryAccountResponseContent;
import com.xwtech.tbank.rpc.message.response.TBankSubAccountBook;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.AccountStatus;
import com.xwtech.xwecp.service.logic.pojo.ChildAccountBook;
import com.xwtech.xwecp.service.logic.pojo.MainAccountBook;
import com.xwtech.xwecp.service.logic.pojo.QRY040082Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.TbankJsonUtil;
/**
 * 流量银行查询用户状态、主账本、子账本
 * @author Mr Ou
 * @date 2014-07-18
 */
public class QueryAccountStatusAndDetailInvocation extends BaseInvocation implements ILogicalService{

	private static final Logger logger = Logger.getLogger(QueryAccountStatusAndDetailInvocation.class);
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		QRY040082Result result = new QRY040082Result();
		//根据操作类型解析流量银行查询用户状态、主账本、子账本
		result = getTbankDealwithResult(accessId,params,result);
		return result;
	}
	
	/**
	 * 解析流量银行查询用户状态、主账本、子账本
	 * @param accessId
	 * @param params
	 * @param result
	 * @return QRY040082Result
	 */
	protected QRY040082Result getTbankDealwithResult(String accessId,List<RequestParameter> params,QRY040082Result result)
	{
		//请求参数
		String phoneNum =(String)getParameters(params,"phoneNum");//手机号码
		String oprtype =(String)getParameters(params,"oprtype");//1:查询账户状态 2:查询主账本 3:查询子账本明细
		String accountOrBookId =(String)getParameters(params,"accountOrBookId");//账户id或账本id
		String bizNo =(String)getParameters(params,"bizNo");//由账户查询主账本类型 流量宝、话费宝
		String beginDate =(String)getParameters(params,"beginDate");//子账本查询明细开始时间
		String endDate =(String)getParameters(params,"endDate");//子账本查询明细结束时间
		
		//流量银行接口方法
		String method = "queryUsedLeaveMoney";
		//组装报文对象
		TBankRequestMessage trm = new TBankRequestMessage();
		TBankQueryAccountRequestContent tarContent = new TBankQueryAccountRequestContent();
		//解析返回json报文对象
		TBankResponseMessage readResponse = null;
		//返回报文体中多态对象
		TBankQueryAccountResponseContent accountResponse = null;
		//请求返回的报文
		String rspJson = "";
		
		//查询账户状态
		if(!"".equals(oprtype) && "1".equals(oprtype))
		{
			trm.setBIPCode(oprtype);
			
			tarContent.setMobileNo(phoneNum);
			tarContent.setQueryTime(new Date());
			
			trm.setSvcCont(tarContent);
			
			try 
			{
				rspJson = getTbankResult(method,trm,accessId,params,result);
				readResponse = (TBankResponseMessage)TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(rspJson.getBytes("UTF-8")));
				
			    accountResponse = (TBankQueryAccountResponseContent)readResponse.getSvcCont();
				
			    if(null != accountResponse)
			    {
			    	AccountStatus accountStatus = result.getAccountStatus();
				    accountStatus.setAccountStatus(accountResponse.getAccountStatus()+"");
				    accountStatus.setAccountId(accountResponse.getAccountId());
				    result.setAccountStatus(accountStatus);
			    }
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				logger.debug("===流量银行查询账户状态返回报文错误！===\n",e);
			}
		}
		//查询主账本
		else if(!"".equals(oprtype) && "2".equals(oprtype))
		{
			trm.setBIPCode(oprtype);
			
			tarContent.setMobileNo(phoneNum);
			tarContent.setBizNo(bizNo);//按业务类型查询主账本
			tarContent.setAccountId(accountOrBookId);
			tarContent.setQueryTime(new Date());
			
			trm.setSvcCont(tarContent);
			
			try 
			{
				rspJson = getTbankResult(method,trm,accessId,params,result);
				readResponse = (TBankResponseMessage)TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(rspJson.getBytes("UTF-8")));
				accountResponse = (TBankQueryAccountResponseContent)readResponse.getSvcCont();
				
				if(null != accountResponse && null != accountResponse.getMainAccountBooks())
				{
					List<MainAccountBook> mainAccountBooks = result.getMainAccountBooks();
					MainAccountBook mainAccountBook = null;
					
					for(TBankMainAccountBook tbankBook : accountResponse.getMainAccountBooks())
					{
						mainAccountBook = new MainAccountBook();
						mainAccountBook.setAccountId(tbankBook.getAccountId());
						mainAccountBook.setBookId(tbankBook.getBookId());
						mainAccountBook.setBalance(tbankBook.getBalance()+"");
						mainAccountBook.setStatus(tbankBook.getStatus()+"");
						mainAccountBooks.add(mainAccountBook);
					}
					result.setMainAccountBooks(mainAccountBooks);
				}
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				logger.debug("===流量银行查询主账本返回报文错误！===\n",e);
			}
		}
		//查询子账本
		else if(!"".equals(oprtype) && "3".equals(oprtype))
		{
            trm.setBIPCode(oprtype);
			
			tarContent.setMobileNo(phoneNum);
			tarContent.setBookAccountNo(accountOrBookId);//主账本id
			tarContent.setStart(beginDate);//子账本查询开始时间
			tarContent.setEnd(endDate);//子账本查询结束时间
			tarContent.setQueryTime(new Date());
			
			trm.setSvcCont(tarContent);
			
			try 
			{
				rspJson = getTbankResult(method,trm,accessId,params,result);
				readResponse = (TBankResponseMessage)TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(rspJson.getBytes("UTF-8")));
				accountResponse = (TBankQueryAccountResponseContent)readResponse.getSvcCont();
				
				if(null != accountResponse && null != accountResponse.getChildBooks())
				{
					List<ChildAccountBook> childAccountBooks = result.getChildAccountBooks();
					ChildAccountBook childAccountBook = null;
					
					for(TBankSubAccountBook tbankBook : accountResponse.getChildBooks())
					{
						childAccountBook = new ChildAccountBook();
						childAccountBook.setBookId(tbankBook.getAccountBookId());
						childAccountBook.setChildBookId(tbankBook.getChildBookId());
						childAccountBook.setStatus(tbankBook.getStatus()+"");
						childAccountBook.setBalance(tbankBook.getBalance()+"");
						childAccountBook.setWithDrawlAmount(tbankBook.getWithDrawlAmount()+"");
						childAccountBook.setDeadlineBegin(tbankBook.getDeadlineBegin()+"");
						childAccountBook.setDeadlineEnd(tbankBook.getDeadlineEnd()+"");
						childAccountBooks.add(childAccountBook);
					}
					result.setChildAccountBooks(childAccountBooks);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				logger.debug("===流量银行查询子账本返回报文错误！===\n",e);
			}
		}
		else
		{
			result.setResultCode("-1");
			result.setErrorMessage("操作类型错误!(1:查询账户状态、2:查询主账本、3:查询子账本)");
		}
		//操作类型不正确的时候，不会执行以下代码
		if(null != rspJson  && !"".equals(rspJson))
		{
		   result.setResultCode(readResponse.getResponse().getRspType());
		   result.setErrorCode(readResponse.getResponse().getRspCode());
		   result.setErrorMessage(readResponse.getResponse().getRspDesc());
		}
		
		return result;
	}
	/**
	 * 组装json报文字符串、发送请求与解析返回json
	 * @param method
	 * @param trm
	 * @param accessId
	 * @param params
	 * @param result
	 * @return rsJson
	 */
	protected String getTbankResult(String method,TBankRequestMessage trm,String accessId,List<RequestParameter> params,QRY040082Result result)
	{
		String reqJson = "";
		String rsJson = "";
		try
		{
			//组装请求的json字符串
			reqJson = TbankJsonUtil.internalWriteRequest(method,new Object[] { trm }, "-5797640080114934769");
			logger.info(" ====== 发送流量银行查询用户状态与账本明细JSON报文 ======\n" + reqJson);
			if(null != reqJson && !"".equals(reqJson))
			{
			   rsJson = (String) this.remote.callJsonRemote(new StringTeletext(reqJson, accessId, "", this.generateCity(params)));
			   logger.info(" ====== 返回流量银行查询用户状态与账本明细JSON报文 ======\n" + rsJson);
			   return rsJson;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.debug("===流量银行查询返回报文错误！\n===",e);
		}
		return null;
	}
}
