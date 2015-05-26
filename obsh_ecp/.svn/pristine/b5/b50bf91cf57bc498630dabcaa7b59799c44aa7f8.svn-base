package com.xwtech.xwecp.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.web.XWECPWebApp;

public class LInterfaceAccessLogger
{
	private static final Logger logger = Logger
			.getLogger(LInterfaceAccessLogger.class);
	
	private static final Logger liAccessFailLogger = Logger.getLogger("liAccessFailLogger");
	
	private static final Logger liAccessTeletextLogger = Logger.getLogger("liAccessTeletextLogger");
	
	private static final char COLUMN_SEPARATOR = (char)1;

	private static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	/**
	 * 日志策略: 直接入库, 入库失败写文本, 配合日志文本导入程序实现日志的完整性 数据库列: F_ACCESS_ID,
	 * F_LOGIC_NUMBER, F_ACCESS_TIME, F_RESPONSE_TIME, F_CLIENT_IP
	 * F_CHANNEL_NUM, F_CHANNEL_USER, F_IS_ERROR, F_RESULT_CODE, F_ERROR_CODE,
	 * F_ERROR_MSG, F_OP_TYPE, F_USER_MOBILE, F_USER_BRAND, F_USER_CITY
	 * F_BIZ_CODE, F_REQ_TEXT, F_RES_TEXT, F_ERROR_STACK, F_CLIENT_ACCESS_ID
	 * 新增一个字段用来跟踪用户
	 * F_USER_TRACE_ID
	 */
	public static void log(String xmlRequest, String xmlResponse, String accessId,
			ServiceMessage request, long accessTime, long responseTime,
			ServiceMessage response, String clientIp,
			Throwable stackTrace, String clientAccessId,String userTraceId)
	{
		final String f_access_id = accessId;
		final String f_logic_number = request.getHead().getCmd();
		final String f_access_time = getDateStr(accessTime);
		final String f_response_time = getDateStr(responseTime);
		final String f_client_ip = clientIp;
		final String f_channel_num = request.getHead().getChannel();
		final StringBuffer f_channel_user = new StringBuffer();
		RequestData data  = (RequestData)request.getData();
		final String f_oper_id = getParameters(data.getParams(),"context_fixed_oper_id")==null?null:getParameters(data.getParams(),"context_fixed_oper_id").toString();

		if (request.getHead().getUser() != null
				&& request.getHead().getUser().trim().length() > 0)
		{
			f_channel_user.append(request.getHead().getUser());
		}
		else
		{
			f_channel_user.append("UNKNOWN");
		}
		final String f_is_error = stackTrace == null ? "0" : "1";
		final String f_result_code = ((ResponseData) response.getData())
				.getServiceResult().getResultCode();
		final String f_error_code = ((ResponseData) response.getData())
				.getServiceResult().getErrorCode();
		final String f_error_msg = ((ResponseData) response.getData())
				.getServiceResult().getErrorMessage();
		final String f_op_type = request.getHead().getOpType();
		final String f_user_mobile = request.getHead().getUserMobile();
		final String f_user_brand = request.getHead().getUserBrand();
		final String f_user_city = request.getHead().getUserCity();
		final String f_biz_code = request.getHead().getBizCode();
		final String f_req_text = xmlRequest;
		final String f_res_text = xmlResponse;
		final StringBuffer f_error_stack = new StringBuffer();
		
		if (stackTrace != null)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			stackTrace.printStackTrace(pw);
			f_error_stack.append(sw.getBuffer().toString());
			pw.close();
			try
			{
				sw.close();
			}
			catch (IOException e)
			{
				logger.error(e, e);
			}
		}
		
		LInterfaceAccessLogBean lb = new LInterfaceAccessLogBean();
		lb.f_access_id = f_access_id;
		lb.f_access_time = f_access_time;
		lb.f_biz_code = f_biz_code;
		lb.f_channel_num = f_channel_num;
		lb.f_channel_user = f_channel_user.toString();
		lb.f_client_ip = f_client_ip;
		lb.f_error_code = f_error_code;
		lb.f_error_msg = f_error_msg;
		lb.f_error_stack = f_error_stack.toString();
		lb.f_is_error = f_is_error;
		lb.f_logic_number = f_logic_number;
		lb.f_op_type = f_op_type;
		lb.f_req_text = f_req_text;
		lb.f_res_text = f_res_text;
		lb.f_response_time = f_response_time;
		lb.f_result_code = f_result_code;
		lb.f_user_brand = f_user_brand;
		lb.f_user_city = f_user_city;
		lb.f_user_mobile = f_user_mobile;
		lb.f_client_access_id = clientAccessId;
		lb.f_trace_id = userTraceId;
		lb.f_oper_id = f_oper_id;
		writeLInterfaceAccessLog(lb);
		logger.debug("写逻辑接口访问日志["+f_access_id+"]!");
	}
/**
	 * 日志策略: 直接入库, 入库失败写文本, 配合日志文本导入程序实现日志的完整性 数据库列: F_ACCESS_ID,
	 * F_LOGIC_NUMBER, F_ACCESS_TIME, F_RESPONSE_TIME, F_CLIENT_IP
	 * F_CHANNEL_NUM, F_CHANNEL_USER, F_IS_ERROR, F_RESULT_CODE, F_ERROR_CODE,
	 * F_ERROR_MSG, F_OP_TYPE, F_USER_MOBILE, F_USER_BRAND, F_USER_CITY
	 * F_BIZ_CODE, F_REQ_TEXT, F_RES_TEXT, F_ERROR_STACK, F_CLIENT_ACCESS_ID
	 */
	public static void log(String xmlRequest, String xmlResponse, String accessId,
			ServiceMessage request, long accessTime, long responseTime,
			ServiceMessage response, String clientIp,
			Throwable stackTrace, String clientAccessId)
	{
		final String f_access_id = accessId;
		final String f_logic_number = request.getHead().getCmd();
		final String f_access_time = getDateStr(accessTime);
		final String f_response_time = getDateStr(responseTime);
		final String f_client_ip = clientIp;
		final String f_channel_num = request.getHead().getChannel();
		final StringBuffer f_channel_user = new StringBuffer();
		if (request.getHead().getUser() != null
				&& request.getHead().getUser().trim().length() > 0)
		{
			f_channel_user.append(request.getHead().getUser());
		}
		else
		{
			f_channel_user.append("UNKNOWN");
		}
		final String f_is_error = stackTrace == null ? "0" : "1";
		final String f_result_code = ((ResponseData) response.getData())
				.getServiceResult().getResultCode();
		final String f_error_code = ((ResponseData) response.getData())
				.getServiceResult().getErrorCode();
		final String f_error_msg = ((ResponseData) response.getData())
				.getServiceResult().getErrorMessage();
		final String f_op_type = request.getHead().getOpType();
		final String f_user_mobile = request.getHead().getUserMobile();
		final String f_user_brand = request.getHead().getUserBrand();
		final String f_user_city = request.getHead().getUserCity();
		final String f_biz_code = request.getHead().getBizCode();
		final String f_req_text = xmlRequest;
		final String f_res_text = xmlResponse;
		final StringBuffer f_error_stack = new StringBuffer();
		
		if (stackTrace != null)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			stackTrace.printStackTrace(pw);
			f_error_stack.append(sw.getBuffer().toString());
			pw.close();
			try
			{
				sw.close();
			}
			catch (IOException e)
			{
				logger.error(e, e);
			}
		}
		
		LInterfaceAccessLogBean lb = new LInterfaceAccessLogBean();
		lb.f_access_id = f_access_id;
		lb.f_access_time = f_access_time;
		lb.f_biz_code = f_biz_code;
		lb.f_channel_num = f_channel_num;
		lb.f_channel_user = f_channel_user.toString();
		lb.f_client_ip = f_client_ip;
		lb.f_error_code = f_error_code;
		lb.f_error_msg = f_error_msg;
		lb.f_error_stack = f_error_stack.toString();
		lb.f_is_error = f_is_error;
		lb.f_logic_number = f_logic_number;
		lb.f_op_type = f_op_type;
		lb.f_req_text = f_req_text;
		lb.f_res_text = f_res_text;
		lb.f_response_time = f_response_time;
		lb.f_result_code = f_result_code;
		lb.f_user_brand = f_user_brand;
		lb.f_user_city = f_user_city;
		lb.f_user_mobile = f_user_mobile;
		lb.f_client_access_id = clientAccessId;
			
		writeLInterfaceAccessLog(lb);
		logger.debug("写逻辑接口访问日志["+f_access_id+"]!");
	}


	private static String getDateStr(long time)
	{
		Date d = new Date();
		d.setTime(time);
		return format.format(d);
	}
	
	public static void writeLInterfaceAccessLog(LInterfaceAccessLogBean log)
	{
//		//不记录成功的记录
//		if("false".equals(XWECPWebApp.IS_RECORD_RIGHT) && "0".equals(log.f_result_code))
//		{
//			return;
//		}
		StringBuffer sb = new StringBuffer();
		sb.append(log.f_access_id == null ? "" : log.f_access_id).append(COLUMN_SEPARATOR);
		sb.append(log.f_logic_number == null ? "" : log.f_logic_number).append(COLUMN_SEPARATOR);
		sb.append(log.f_access_time == null ? "" : log.f_access_time).append(COLUMN_SEPARATOR);
		sb.append(log.f_response_time == null ? "" : log.f_response_time).append(COLUMN_SEPARATOR);
		sb.append(log.f_client_ip == null ? "" : log.f_client_ip).append(COLUMN_SEPARATOR);
		sb.append(log.f_channel_num == null ? "" : log.f_channel_num).append(COLUMN_SEPARATOR);
		sb.append(log.f_channel_user == null ? "" : log.f_channel_user).append(COLUMN_SEPARATOR);
		sb.append(log.f_is_error == null ? "" : log.f_is_error).append(COLUMN_SEPARATOR);
		sb.append(log.f_result_code == null ? "" : log.f_result_code).append(COLUMN_SEPARATOR);
		sb.append(log.f_error_code == null ? "" : log.f_error_code).append(COLUMN_SEPARATOR);
		sb.append(makeOnRow(log.f_error_msg)).append(COLUMN_SEPARATOR);
		sb.append(log.f_op_type == null ? "" : log.f_op_type).append(COLUMN_SEPARATOR);
		sb.append(log.f_user_mobile == null ? "" : log.f_user_mobile).append(COLUMN_SEPARATOR);
		sb.append(log.f_user_brand == null ? "" : log.f_user_brand).append(COLUMN_SEPARATOR);
		sb.append(log.f_user_city == null ? "" : log.f_user_city).append(COLUMN_SEPARATOR);
		sb.append(log.f_biz_code == null ? "" : log.f_biz_code).append(COLUMN_SEPARATOR);
		sb.append("").append(COLUMN_SEPARATOR);
		sb.append("").append(COLUMN_SEPARATOR);
		sb.append("").append(COLUMN_SEPARATOR);
		sb.append(log.f_client_access_id == null ? "" : log.f_client_access_id);
		sb.append(COLUMN_SEPARATOR);
		sb.append(log.f_trace_id == null ? "" : log.f_trace_id).append(COLUMN_SEPARATOR);;
		sb.append(log.f_oper_id == null ? "" : log.f_oper_id).append(COLUMN_SEPARATOR);;
		liAccessFailLogger.info(sb.toString());
		logger.debug(sb.toString());
		
		//排除清单查询的日志
		if("true".equals(XWECPWebApp.CDR_DIRECT2DB) || !"QRY010001".equals(log.f_logic_number))
		{
			StringBuffer teletextBuffer = new StringBuffer();
			teletextBuffer.append("请求流水号:" + log.f_access_id + ":请求报文:\n");
			teletextBuffer.append(log.f_req_text + "\n");
			teletextBuffer.append("请求流水号:" + log.f_access_id + ":响应报文:\n");
			teletextBuffer.append(log.f_res_text + "\n");
			teletextBuffer.append("请求流水号:" + log.f_access_id + ":异常栈:\n");
			teletextBuffer.append(log.f_error_stack + "\n\n");
			liAccessTeletextLogger.info(teletextBuffer.toString());
		}
	}
	
	private static String makeOnRow(String s)
	{
		if(s == null)
		{
			return "";
		}
		s = s.replaceAll("\\u000A", "");
		return s.replaceAll("\\u000D", "");
	}
	
	/**
	 * 从参数列表里获取参数值
	 * 
	 * @param params
	 * @param parameterName
	 * @return
	 */
	public static Object getParameters(final List<RequestParameter> params, String parameterName) {
		Object rtnVal = null;
		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals(parameterName)) {
				rtnVal = parameter.getParameterValue();
				break;
			}
		}
		return rtnVal;
	}
	
}
