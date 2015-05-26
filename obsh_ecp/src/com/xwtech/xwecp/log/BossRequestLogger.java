package com.xwtech.xwecp.log;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.web.XWECPWebApp;


public class BossRequestLogger
{
	private static final Logger logger = Logger.getLogger(BossRequestLogger.class);
	
	private static final Logger bossRequestFailLogger = Logger.getLogger("bossRequestFailLogger");
	
	private static final Logger bossRequestTeletextLogger = Logger.getLogger("bossRequestTeletextLogger");
	
	private static final char COLUMN_SEPARATOR = (char)1;
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	//private static final String[] IM_BOSS_ID ={"cc_cproductswitch_354","cc_cchgpkgforpro_76_TCBG","cc_cchgpkgforpro_76_boss"};
	
	private static final String[] CDR_BOSS_ID = {"ac_gsmcdr_query_501", "ac_ipcarcdr_query_504", "ac_ispcdr_query_506",
												 "ac_montnetcdr_query_511", "ac_mmscdr_query_507", "ac_smscdr_query_503", 
												 "ac_gprscdr_query_502", "ac_vpmncdr_query_505", "ac_wlancdr_query_508", 
												 "ac_121cdr_query_522", "ac_isp2cdr_query_509", "ac_ussdcdr_query_510", 
												 "ac_mpmusiccdr_query_520", "ac_lbscdr_query_521", "ac_meet_query_523", 
												 "ac_gsmcdr_video_query_539", "ac_cmnetcdr_query_540","ac_gsmcdr_query_520"};

	private static ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;

	private static ILogDAO logDAO = (ILogDAO) (springCtx.getBean("logDAO"));

	public static void log(String bossRequestId, String accessId, String identity, long beginTime, long endTime, String requestMsg, String responseMsg, boolean isError, Throwable stackTrace, String resultCode, String errorCode, String errorMsg)
	{
		StringBuffer f_error_stack = new StringBuffer();
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
		BossRequestLogBean lb = new BossRequestLogBean();
		lb.f_boss_req_id = bossRequestId;
		lb.f_access_id = accessId;
		lb.f_access_time = getDateStr(beginTime);
		lb.f_boss_id = identity == null ? "" : identity;
		lb.f_error_code = errorCode == null ? "" : errorCode;
		lb.f_error_msg = errorMsg == null ? "" : errorMsg;
		lb.f_error_stack = f_error_stack.toString();
		lb.f_is_error = isError ? "1" : "0";
		lb.f_req_text = requestMsg;
		lb.f_res_text = responseMsg;
		lb.f_response_time =  String.valueOf(endTime);
		lb.f_result_code = resultCode == null ? "" : resultCode;		
		
		writeBossRequestLog(lb);
		
		logger.debug("写BOSS接口访问日志["+accessId+"]!");
	}
	/**
	 * bossRequest增加traceId
	 * @param traceId 追踪标识
	 */
	public static void log(String bossRequestId, String accessId, String identity, long beginTime, long endTime, String requestMsg, String responseMsg, boolean isError, Throwable stackTrace, String resultCode, String errorCode, String errorMsg,String traceId)
	{
		StringBuffer f_error_stack = new StringBuffer();
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
		BossRequestLogBean lb = new BossRequestLogBean();
		lb.f_boss_req_id = bossRequestId;
		lb.f_access_id = accessId;
		lb.f_access_time = getDateStr(beginTime);
		lb.f_boss_id = identity == null ? "" : identity;
		lb.f_error_code = errorCode == null ? "" : errorCode;
		lb.f_error_msg = errorMsg == null ? "" : errorMsg;
		lb.f_error_stack = f_error_stack.toString();
		lb.f_is_error = isError ? "1" : "0";
		lb.f_req_text = requestMsg;
		lb.f_res_text = responseMsg;
		lb.f_response_time =  String.valueOf(endTime);
		lb.f_result_code = resultCode == null ? "" : resultCode;		
		lb.f_trace_id = traceId == null ? "" : traceId;
		writeBossRequestLog(lb);
		
		logger.debug("写BOSS接口访问日志["+accessId+"]!");
	}
	private static String getDateStr(long time)
	{
		Date d = new Date();
		d.setTime(time);
		return format.format(d);
	}
	
	public static void writeBossRequestLog(BossRequestLogBean log)
	{
		
		/*成功是否记录报文
		 *根据表T_BOSSLOG_RECORD_FLAG配置，
		if("false".equals(XWECPWebApp.IS_RECORD_RIGHT) && "0".equals(log.f_result_code) && isRecordBossId(log.f_boss_id))
		{
			return; 
		}
		*/
		StringBuffer sb = new StringBuffer();
		//sb.append(log.f_boss_req_id == null ? "" : log.f_boss_req_id).append(COLUMN_SEPARATOR);
		sb.append(log.f_access_id == null ? "" : log.f_access_id).append(COLUMN_SEPARATOR);
		sb.append(log.f_boss_id == null ? "" : log.f_boss_id).append(COLUMN_SEPARATOR);
		sb.append(log.f_access_time == null ? "" : log.f_access_time).append(COLUMN_SEPARATOR);
		sb.append(log.f_response_time == null ? "" : log.f_response_time).append(COLUMN_SEPARATOR);
		sb.append(log.f_is_error == null ? "" : log.f_is_error).append(COLUMN_SEPARATOR);
		sb.append(log.f_result_code == null ? "" : log.f_result_code).append(COLUMN_SEPARATOR);
		sb.append(log.f_error_code == null ? "" : log.f_error_code).append(COLUMN_SEPARATOR);
		sb.append(log.f_error_msg == null ? "" : log.f_error_msg);
		sb.append(COLUMN_SEPARATOR);
		sb.append(log.f_trace_id == null ? "" : log.f_trace_id);
		//sb.append(log.f_error_msg == null ? "" : log.f_error_msg).append(COLUMN_SEPARATOR);

		//sb.append("").append(COLUMN_SEPARATOR);
		//sb.append("").append(COLUMN_SEPARATOR);
		//sb.append("").append(COLUMN_SEPARATOR);
		bossRequestFailLogger.info(sb.toString());
		logger.debug(sb.toString());
		
		/**
		 *不记录成功报文
		*/
		if("false".equals(XWECPWebApp.IS_RECORD_RIGHT) && "0".equals(log.f_result_code) && isRecordBossId(log.f_boss_id))
		{
//			return; 
		}
		
		//屏蔽清单查询
//		if("true".equals(XWECPWebApp.CDR_DIRECT2DB)
//				|| (!StringUtils.isBlank(log.f_boss_id) && StringUtils.join(CDR_BOSS_ID, "|").indexOf(log.f_boss_id) == -1))
//		{
			StringBuffer teletextBuffer = new StringBuffer();
			teletextBuffer.append("请求流水号:" + log.f_access_id + ":请求报文:\n");
			teletextBuffer.append(log.f_req_text + "\n");
			teletextBuffer.append("请求流水号:" + log.f_access_id + ":响应报文:\n");
			teletextBuffer.append(log.f_res_text + "\n");
			teletextBuffer.append("请求流水号:" + log.f_access_id + ":异常栈:\n");
			teletextBuffer.append(log.f_error_stack + "\n\n");
			bossRequestTeletextLogger.info(teletextBuffer.toString());
//		}
	}

	private static boolean isRecordBossId(String f_boss_id) 
	{
		Map<String,String> recordBossLog = logDAO.getBossLogRecord();
		if(recordBossLog != null)
		{
			//0:不记录，1:记录,数据bossid不存在也不记录
			if(recordBossLog.get(f_boss_id) != null)
			{
				return recordBossLog.get(f_boss_id).equals("0") ? true : false;
			}
			return true;
			
		}
		return true;
	}
}
