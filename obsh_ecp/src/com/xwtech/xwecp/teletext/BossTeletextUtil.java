package com.xwtech.xwecp.teletext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.lilystudio.util.StringReader;
import org.springframework.jdbc.core.RowMapper;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;

public class BossTeletextUtil {
	private static final Logger logger = Logger.getLogger(BossTeletextUtil.class);

	private WellFormedDAO wellFormedDAO;

	private String charset = "UTF-8";

	private Map<String, IExternalFunctionExecutor> externalFunctionMap = new HashMap<String, IExternalFunctionExecutor>();

	private VelocityEngine engine = null;
	
	//装载割接到Boss的boss接口 (抢流量红包) 为了向割接到Boss接口添加req_channel 渠道号
	private static StringBuffer bossCmds = new StringBuffer();
	
	//抢流量红包Boss对应渠道号
    private static Map<String,String> channelMap = new HashMap<String,String>();
	
	static
	{
		bossCmds.append("QUERYGPRSALLPKGFLUX_814")   //GPRS分套餐流量查询
		        .append(",")
		        .append("QUERYGPRSALLPKGFLUX_814_CH")//GPRS分套餐流量查询 池化
		        .append(",")
		        .append("QUERYGPRSFLUX_02")          //GPRS流量查询
		        .append(",")
		        .append("QUERYGPRSDAYFLUX")          //查询日流量
		        .append(",")
		        .append("GETAVAILINTEGRAL_818")    //实时积分查询      
		        .append(",")
		        .append("cc_aqueryspaccbal_361")   //专有账户查询
		        .append(",")
		        .append("cc_cgetusercustaccbalance_361") //余额及有效期查询
		        .append(",")
		        .append("ac_acqryrealtimebill_361")    //实时账单查询
		        .append(",")
		        .append("cc_cgetuseraccscore_361")     //积分查询
		        .append(",")
		        .append("ac_agetfreeitem_361")         //累积量查询
		        .append(",")
		        .append("ac_agetfreeitem_361_ch");   //累积量查询 池化
		
		channelMap.put("obsh_channel", "4");    //网厅 抢流量红包渠道号
		channelMap.put("icc_channel", "4");    // 门户抢流量红包渠道号
		channelMap.put("wx_channel", "4");    //微厅 抢流量红包渠道号
		channelMap.put("active_channel", "4");    //活动 抢流量红包渠道号
		channelMap.put("wap_channel", "19");    //掌厅 抢流量红包渠道号
		channelMap.put("sms_channel", "22");    //短厅 抢流量红包渠道号
		channelMap.put("openapi_channel", "105"); //能力平台抢流量红包渠道号
		channelMap.put("jsmcc_channel", "77");    //手机客户端抢流量红包渠道号
		         
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public WellFormedDAO getWellFormedDAO() {
		return wellFormedDAO;
	}

	public void initialize() {
		logger.info("初始化velocity engine!");
		if (engine == null) {
			engine = new VelocityEngine(); // 初始化并取得Velocity引擎

			try {
				engine.init();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				logger.error("初始化并取得Velocity引擎错误！！！");
			}
		}
	}

	public void setWellFormedDAO(WellFormedDAO wellFormedDAO) {
		this.wellFormedDAO = wellFormedDAO;
	}

	/**
	 * 查询发送报文模板
	 * 
	 * @param cmd
	 *            命令字
	 * @return
	 */
	public String getTeletextTemplateFromDatabase(String cmd) {
		String textTemplate = "";
		Map mapResult = null;

		String sql = "select tbs.F_BOSS_IN_TMP  from  T_BOSS_INT_DA tbs where tbs.F_BOSS_INT_NUM='" + cmd + "'";
		try {
			mapResult = (Map) this.wellFormedDAO.getJdbcTemplate().queryForObject(sql, new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, String> mapRs = new HashMap<String, String>();
					ResultSetMetaData metaData = rs.getMetaData();

					for (int num = 1; num <= metaData.getColumnCount(); num++) {
						if (metaData.getColumnType(num) == Types.CLOB) {

							mapRs.put(metaData.getColumnName(num).toLowerCase(), BossTeletextUtil.this.wellFormedDAO.getLobHandler().getClobAsString(rs, num));
						}
					}
					return mapRs;
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("操作数据库出现错误！！！");

		}
		textTemplate = (String) mapResult.get("f_boss_in_tmp");

		return textTemplate;
	}

	/**
	 * 获取发送报文模板
	 * 
	 * @param cmd
	 *            命令字
	 * @return
	 */
	public String getTeletextTemplate(String cmd) {

		String mergeTeletext = "";
		String cacheTemp = "";
		if (cmd != null && !"".equals(cmd)) {
			try {
				cacheTemp = (String) wellFormedDAO.getCache().get("BOSS_IN_TEMPLATE_CODE_KEY_" + cmd);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				logger.error("读取缓存出错！！！");
				return null;
			}
			if (cacheTemp != null && !"".equals(cacheTemp)) {
				mergeTeletext = cacheTemp;
			} else {
				// 查询发送报文模板
				mergeTeletext = this.getTeletextTemplateFromDatabase(cmd);
				wellFormedDAO.getCache().add("BOSS_IN_TEMPLATE_CODE_KEY_" + cmd, mergeTeletext);
			}
		}
		return mergeTeletext;
	}

	/**
	 * 组装发送至BOSS的报文
	 * 
	 * @param cmd
	 *            命令字
	 * @param param
	 *            参数
	 * @return
	 */
	public String mergeTeletext(String cmd, List<RequestParameter> param) {
		String mergeTeletext = "";
		if (cmd != null && !"".equals(cmd)) {
			String text = getTeletextTemplate(cmd);
			VelocityContext context = new VelocityContext(); // 生成数据容器对象

			// 这里需要设置数据的值
			if (param != null) {
				// 开始加载报文头
				context.put("request_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				context.put("request_seq", RequestSeqCreater.getRequestSeq(0));
//				context.put("context_fixed_oper_id",(param));s
				// 结束加载报文头
				
				Object req_channel = getParameters(param,"context_req_channel"); //获取渠道编号
				String channel = String.valueOf(getParameters(param,"fixed_invoke_channel"));  //获取req_channel
				
				//抢流量红包割接的接口、需要割接渠道对应的渠道编号和param集合里没有req_channel条件满足
                if(bossCmds.indexOf(cmd) != -1 && null == req_channel && channelMap.containsKey(channel))
                {
                	setParameter(param,"context_req_channel",channelMap.get(channel));
                }
                
				for (int i = 0; i < param.size(); i++) 
				{
					if("context_fixed_oper_id".equals(param.get(i).getParameterName()))
					{
						String [] arrOper=String.valueOf(param.get(i).getParameterValue()).split("_");
						if(arrOper.length == 4)
						{//长度等于4进行拆分
						     String context_fixed_oper_id= String.valueOf(param.get(i).getParameterValue());//获取oper_i字段
						     context.put("context_oper_id", context_fixed_oper_id.substring(context_fixed_oper_id.lastIndexOf("_")+1));//重新赋值
						}
						//不符合规范的fixed_oper_id值设置为空
						else
						{
							 context.put("context_oper_id","");
						}
					}else
					{
						context.put(param.get(i).getParameterName(), param.get(i).getParameterValue());
					}
				}

				StringWriter writer = new StringWriter(); // 设置接收模板数据的输出流

				try {
					// 匹配替换参数
					boolean flag = engine.evaluate(context, writer, "", new StringReader(text));
					if (flag) {
						mergeTeletext = writer.toString();
					}

					mergeTeletext = this.evaluateFunctions(mergeTeletext);

					// logger.debug(" ====== mergeTeletext ====== \n"
					// + mergeTeletext);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					logger.error("替换摸板信息出错！！！");
					return null;
				}
			}
		}
		return mergeTeletext;
	}

	private String evaluateFunctions(String in) throws Exception {
		String pStr = "\\%.{2,200}[\\(.*\\)]++";
		String groupStr = "\\%(.*?\\()(.*)?\\)";
		Pattern groupPattern = Pattern.compile(groupStr);
		Pattern p = Pattern.compile(pStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(in);
		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {
			String tmp = matcher.group();
			Matcher m = groupPattern.matcher(tmp);
			if (m.find()) {
				if (m.groupCount() == 2) {
					String functionName = m.group(1);
					functionName = functionName.substring(0, functionName.length() - 1); // 去掉结尾的"("
					String parameter = m.group(2);
					String value = "";
					if (parameter.startsWith("\"") && parameter.endsWith("\"")) // 如果直接是字串
					{
						parameter = parameter.substring(1, parameter.length() - 1); // 去掉两端的引号
						// 执行函数
						value = this.executeExternalFunction(functionName, parameter);
					} else if (parameter.matches(pStr)) // 如果参数本身又是一个函数
					{
						value = this.recursiveEvaluateFunction(functionName, parameter);
					}
					matcher.appendReplacement(buf, value);
				}
			}
		}
		matcher.appendTail(buf);
		String ret = buf.toString();
		// return ret.replaceAll("\\\\$", "$");
		return ret;
	}

	private String recursiveEvaluateFunction(String functionName, String parameter) {
		if (parameter.startsWith("\"") && parameter.endsWith("\"")) {
			parameter = parameter.substring(1, parameter.length() - 1); // 去掉两端的引号
			return this.executeExternalFunction(functionName, parameter);
		} else {
			String groupStr = "\\%(.*?\\()(.*)?\\)";
			Pattern groupPattern = Pattern.compile(groupStr);
			Matcher m = groupPattern.matcher(parameter);
			if (m.find()) {
				if (m.groupCount() == 2) {
					String subName = m.group(1);
					subName = subName.substring(0, subName.length() - 1); // 去掉结尾的"("
					String subParam = m.group(2);
					return this.executeExternalFunction(functionName, recursiveEvaluateFunction(subName, subParam));
				}
			}
			return functionName + "(" + parameter + ")";
		}
	}

	private String executeExternalFunction(String functionName, String parameter) {
		IExternalFunctionExecutor functionExecutor = this.externalFunctionMap.get(functionName);
		if (functionExecutor != null) {
			return functionExecutor.execute(parameter);
		}
		logger.warn("未配置函数[" + functionName + "]!");
		return functionName + "(" + parameter + ")";
	}

	private static final String readFileTxt(String path) throws Exception {
		File f = new File(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int nRead = 0;
		FileInputStream fis = new FileInputStream(f);
		while ((nRead = fis.read(buf)) > 0) {
			baos.write(buf, 0, nRead);
			baos.flush();
		}
		fis.close();
		return baos.toString("GB2312");
	}
	
	public Map<String, IExternalFunctionExecutor> getExternalFunctionMap() {
		return externalFunctionMap;
	}

	public void setExternalFunctionMap(Map<String, IExternalFunctionExecutor> externalFunctionMap) {
		this.externalFunctionMap = externalFunctionMap;
	}

	public static void main1(String arg[]) throws Exception {
		String testString = readFileTxt("D:\\MyProjects\\网营产品化\\boss_in.txt");
		BossTeletextUtil btu = new BossTeletextUtil();
		btu.initialize();
		btu.externalFunctionMap.put("to_boss_code", new IExternalFunctionExecutor() {
			public String execute(String parameter) {
				return "boss_code_" + parameter;
			}
		});
	}
	
	public static void main2(String arg[]) throws Exception 
	{
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(
			    VelocityEngine.RUNTIME_LOG_LOGSYSTEM, logger);

		engine.init();
		VelocityContext context = new VelocityContext(); // 生成数据容器对象
		context.put("name", "jhr");
		context.put("age", "27");
		
		StringWriter writer = new StringWriter();
		engine.evaluate(context, writer, "", "my name is $name, my age is $age.");
		
		System.out.println(writer.toString());
		
	}
	
	public static void main(String[] args)
	{
		String pStr = "\\%.{2,200}[\\(.*\\)]++";
		String groupStr = "\\%(.*?\\()(.*)?\\)";
		Pattern groupPattern = Pattern.compile(groupStr);
		Pattern p = Pattern.compile(pStr, Pattern.CASE_INSENSITIVE);
		
		String inString = "<operatingtointerboss_function_code>%to_boss_code2(\"123\")</operatingtointerboss_function_code>\n"
				        + "<operatingtointerboss_deal_type>%to_boss_code(\"$456\")</operatingtointerboss_deal_type>";
				       
		Matcher matcher = p.matcher(inString);
		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {
			String tmp = matcher.group();
			Matcher m = groupPattern.matcher(tmp);
			if (m.find()) {
				if (m.groupCount() == 2) {
					String functionName = m.group(1);
					functionName = functionName.substring(0, functionName.length() - 1); // 去掉结尾的"("
					String parameter = m.group(2);
					String value = "";
					if (parameter.startsWith("\"") && parameter.endsWith("\"")) // 如果直接是字串
					{
						parameter = parameter.substring(1, parameter.length() - 1); // 去掉两端的引号
					} 
					else if (parameter.matches(pStr)) // 如果参数本身又是一个函数
					{
						
					}
					matcher.appendReplacement(buf, value);
				}
			}
		}
		matcher.appendTail(buf);
		String ret = buf.toString();
		// return ret.replaceAll("\\\\$", "$"); 
	}
	
	/**
	 * 从参数列表里获取参数值
	 * 
	 * @param params
	 * @param parameterName
	 * @return
	 */
	protected Object getParameters(final List<RequestParameter> params, String parameterName) {
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
	
	/**
	 * 设置参数值
	 * @param params
	 * @param name
	 * @return
	 */
	protected void setParameter(List<RequestParameter> params, String name, String value)
	{
		if (params != null && params.size() > 0) {
			boolean flag = true;
			for(int i = 0; i<params.size(); i++)
			{
				RequestParameter param = params.get(i);
				if(param.getParameterName().equals(name))
				{
					flag = false;
					param.setParameterValue(value);
				}
			}
			
			if (flag) {
				params.add(new RequestParameter(name, value));
			}
		}
	}
	

}
