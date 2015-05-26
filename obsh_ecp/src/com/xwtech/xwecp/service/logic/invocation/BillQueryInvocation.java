package com.xwtech.xwecp.service.logic.invocation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.Ac121BillDetail;
import com.xwtech.xwecp.service.logic.pojo.CdrsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.CdrsPointBillDetail;
import com.xwtech.xwecp.service.logic.pojo.CmnetBillDetail;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GprsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.GsmMagaBillDetail;
import com.xwtech.xwecp.service.logic.pojo.GsmVideoBillDetail;
import com.xwtech.xwecp.service.logic.pojo.IpcarBillDetail;
import com.xwtech.xwecp.service.logic.pojo.Isp2BillDetail;
import com.xwtech.xwecp.service.logic.pojo.IspBillDetail;
import com.xwtech.xwecp.service.logic.pojo.LbsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MeetBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MmsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MontnetBillDetail;
import com.xwtech.xwecp.service.logic.pojo.MpmusicBillDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;
import com.xwtech.xwecp.service.logic.pojo.SmsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.UssdBillDetail;
import com.xwtech.xwecp.service.logic.pojo.VpmnBillDetail;
import com.xwtech.xwecp.service.logic.pojo.WlanBillDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.TeletextParseUtils;

/**
 * 语音话单
 * @author gongww
 * 2009-12-04
 */
public class BillQueryInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(BillQueryInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public BillQueryInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY010001Result res = new QRY010001Result();

		try
		{
			res.setResultCode("0");
			res.setErrorMessage("");
			String beginDate = "";
			int queryType = 0;
			boolean findBeginDate = false;
			boolean findQueryType = false;
			for(RequestParameter param : params)
			{
				String paramName = param.getParameterName();
				if(paramName.equals("beginDate"))
				{
					beginDate = (String)param.getParameterValue();
					findBeginDate = true;
				}
				else if(paramName.equals("queryType"))
				{
					queryType = Integer.parseInt(param.getParameterValue().toString());
					findQueryType = true;
				}
				if(findBeginDate && findQueryType)
					break;
			}
			
			//月份
			String month = beginDate.substring(0,6);
			RequestParameter paramMonth = new RequestParameter("month",month);
			params.add(paramMonth);
			
			switch(queryType)
			{
				//查询语音话单
				case 1:
					res.setGsmBillDetail(this.getGsmCdrInfo(accessId, config, params, res));
					break;
				//查询IP直通车清单
				case 2:
					res.setIpcarBillDetail(this.getIpcarCdrInfo(accessId, config, params, res));
					break;
				//查询代理ISP清单
				case 3:
					res.setIspBillDetail(this.getIspCdrInfo(accessId, config, params, res));
					break;
				//查询梦网清单
				case 4:
					res.setMontnetBillDetail(this.getMontnetCdrInfo(accessId, config, params, res));
					break;
				//查询彩信清单
				case 5:
					res.setMmsBillDetail(this.getMmsCdrInfo(accessId, config, params, res));
					break;
				//查询短信清单
				case 6:
					res.setSmsBillDetail(this.getSmsCdrInfo(accessId, config, params, res));
					break;
				//查询国际短信清单
				case 7:
					res.setIneSmsBillDetail(this.getIneSmsCdrInfo(accessId, config, params, res));
					break;
				//查询GPRS清单
				case 8:
					res.setGprsBillDetail(this.getGprsCdrInfo(accessId, config, params, res));
					break;
				//查询短号码清单
				case 9:
					res.setVpmnBillDetail(this.getVpmnCdrInfo(accessId, config, params, res));
					break;
				//查询WLAN清单
				case 10:
					res.setWlanBillDetail(this.getWlanCdrInfo(accessId, config, params, res));
					break;
				//查询96121清单
				case 11:
					res.setAc121BillDetail(this.getAc121CdrInfo(accessId, config, params, res));
					break;
				//查询17202清单
				case 12:
					res.setIsp2BillDetail(this.getIsp2CdrInfo(accessId, config, params, res));
					break;
				//查询USSD清单
				case 13:
					res.setUssdBillDetail(this.getUssdCdrInfo(accessId, config, params, res));
					break;
				//查询无线音乐清单
				case 14:
					res.setMpmusicBillDetail(this.getMpmusicCdrInfo(accessId, config, params, res));
					break;
				//查询LBS清单
				case 15:
					res.setLbsBillDetail(this.getLbsCdrInfo(accessId, config, params, res));
					break;
				//即时群聊清单
				case 16:
					res.setMeetBillDetail(this.getMeetCdrInfo(accessId, config, params, res));
					break;
				//视频通话清单
				case 17:
					res.setGsmVideoBillDetail(this.getGsmVideoCdrInfo(accessId, config, params, res));
					break;
				//随E行上网本清单
				case 18:
					res.setCmnetBillDetail(this.getCmnetCdrInfo(accessId, config, params, res));
					break;
				//自有梦网清单
				case 19:
					res.setMontnetBillDetail(this.getMontnetDSCdrInfo(accessId, config, params, res));
					break;
				//查询语音杂志
				case 20:
					res.setGsmMagaBillDetail(this.getGsmCdrMagazineInfo(accessId, config, params, res));
					break;
				//游戏消费详单
				case 22:
					res.setCdrsBillDetail(this.getGsmCDRQueryInfo(accessId, config, params, res));
					break;
					//游戏点数详单
				case 23:
					res.setCdrsPointBillDetail(this.getGsmCDRQueryPointInfo(accessId, config, params, res));
					break;
			}
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		
		return res;
	}
	
	
	/**
	 * 查询语音话单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<GsmBillDetail> getGsmCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<GsmBillDetail> reList = new ArrayList<GsmBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_gsmcdr_query_501", params), 
					 accessId, "ac_gsmcdr_query_501", city));
			logger.debug(" ====== 查询语音话单返回报文 ======\n" + rspXml);
			
			//rspXml = "<?xml version=\"1.0\" encoding=\"gbk\"?><operation_out><process_code>ac_gsmcdr_query</process_code><request_type/><sysfunc_id>501</sysfunc_id><request_seq>1_64</request_seq><response_time>20100111101300</response_time><request_source>102010</request_source><response><resp_type>0</resp_type><resp_code>0000</resp_code><resp_desc/></response><content><XTABLE_GSM>基站号~位置小区号~小计~其他话费~实收长途话费~实收基本话费~应收费用~通话时长~开始时间~开始日期~对方号码~状态类型~套餐费~应收基本通话费~应收长途话费~应收其他话费~套餐描述~服务标识~移动之家标识~;39F0~500E~0~0~0~0~40~21~030801~20100101~114~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~37~030840~20100101~12580~主叫本地~40~0~0~0~~2011~~;D590~500B~0~0~0~0~80~66~104508~20100101~13182801923~主叫本地~80~0~0~0~~2011~~;427D~5114~0~0~0~0~40~25~114511~20100101~13182801923~主叫本地~40~0~0~0~~2011~~;42C4~5114~0~0~0~0~40~28~134841~20100101~13951880339~被叫本地~0~0~0~0~~2011~~;B3CE~500B~0~0~0~0~40~25~182120~20100101~13182801923~主叫本地~40~0~0~0~~2011~~;1286~D106~0~0~0~0~315~266~173532~20100102~13773886563~主叫本地~315~0~0~0~~3011~~;1286~D106~0~0~0~0~40~10~200323~20100102~15195942183~主叫本地~40~0~0~0~~3011~~;D58F~500B~0~0~0~0~80~95~200358~20100102~15195942183~主叫本地~80~0~0~0~~2011~~;8DC3~D106~0~0~0~0~80~80~093228~20100103~15951883878~主叫本地~80~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~11~093735~20100103~15951883878~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~42~095317~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~13~105858~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;1249~D106~0~0~0~0~40~20~113002~20100103~13813076323~主叫本地~40~0~0~0~~3011~~;15C3~D107~0~0~0~0~40~10~144105~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;15C3~D107~0~0~0~0~40~22~144355~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;15C3~D107~0~0~0~0~40~10~150301~20100103~13813076323~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~51~093923~20100104~02568599379~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~7~094125~20100104~02568599379~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~45~094151~20100104~02568599379~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~094604~20100104~02568599379~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~360~540~104202~20100104~02568599379~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~160~215~135557~20100104~13951758689~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~144112~20100104~13770794389~被叫本地~0~0~0~0~~3011~~;3036~500B~0~0~0~0~112~95~185719~20100104~13773886563~主叫本地~112~0~0~0~~2011~~;8DC3~D106~0~0~0~0~40~20~084624~20100105~13770794389~被叫本地~0~0~0~0~~3011~~;0C64~500E~0~0~0~0~80~110~102523~20100105~13800250222~被叫本地~0~0~0~0~~2011~~;0C64~500E~0~0~0~0~40~25~103714~20100105~13813893836~被叫本地~0~0~0~0~~2011~~;0C64~500E~0~0~0~0~80~63~110026~20100105~13913814503~主叫本地~80~0~0~0~~2011~~;0C64~500E~0~0~0~0~40~27~112012~20100105~13913814503~主叫本地~40~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~22~113800~20100105~13851852109~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~240~310~135909~20100105~13851852109~主叫本地~240~0~0~0~~2011~~;39F0~500E~0~0~0~0~120~156~143141~20100105~13770794389~主叫本地~0~0~0~0~~2011~~;D590~500B~0~0~0~0~0~79~151506~20100105~10086~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~18~093120~20100106~13851852109~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~240~304~104608~20100106~02161421911~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~120~165~110408~20100106~13851852109~主叫本地~120~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~6~135512~20100106~13770794389~主叫本地~0~0~0~0~~2011~~;8DC3~D106~0~0~0~0~40~56~143655~20100106~13770794389~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~58~145738~20100106~13851954864~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~15~153330~20100106~13851852109~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~161235~20100106~02161421911~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~163253~20100106~13813893836~主叫本地~40~0~0~0~~3011~~;1249~D106~0~0~0~0~40~6~175639~20100106~13813893836~被叫本地~0~0~0~0~~3011~~;1325~D106~0~0~0~0~80~64~201020~20100106~13851852109~主叫本地~80~0~0~0~~3011~~;1286~D106~0~0~0~0~40~11~201645~20100106~13851852109~主叫本地~40~0~0~0~~3011~~;485E~500B~0~0~0~0~40~14~231153~20100106~13851852109~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~12~091554~20100107~13404160223~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~24~093909~20100107~13770794389~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~19~104210~20100107~13813893836~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~30~104247~20100107~13913814503~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~28~105547~20100107~13913814503~被叫本地~0~0~0~0~~2011~~;C780~51F8~0~0~0~0~40~19~135502~20100107~13851852109~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~80~65~140319~20100107~15951883878~主叫本地~80~0~0~0~~2011~~;A3F2~500B~0~0~0~0~40~28~140801~20100107~13912987355~被叫本地~0~0~0~0~~2011~~;A3FC~500B~0~0~0~0~40~15~141150~20100107~13913814503~主叫本地~40~0~0~0~~2011~~;A3FC~500B~0~0~0~0~40~36~142556~20100107~15851804848~被叫本地~0~0~0~0~~2011~~;A3FC~500B~0~0~0~0~120~145~144314~20100107~13800250222~被叫本地~0~0~0~0~~2011~~;A3FC~500B~0~0~0~0~40~10~152328~20100107~13912987355~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~40~16~152456~20100107~13912987355~主叫本地~40~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~13~175311~20100107~13813382746~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~10~183828~20100107~13813893836~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~7~184202~20100107~13773886563~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~17~021610~20100108~13813076323~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~36~031830~20100108~15951883878~主叫本地~40~0~0~0~~2011~~;D590~500B~0~0~0~0~40~9~072755~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;485E~500B~0~0~0~0~80~83~093119~20100108~13851852109~被叫本地~0~0~0~0~~2011~~;485E~500B~0~0~0~0~154~132~121648~20100108~13773886563~主叫本地~154~0~0~0~~2011~~;D590~500B~0~0~0~0~40~25~124626~20100108~15951883878~主叫本地~40~0~0~0~~2011~~;4859~500B~0~0~0~0~40~13~124727~20100108~13814028619~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~40~28~142133~20100108~13814028619~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~80~107~142345~20100108~13814028619~主叫本地~80~0~0~0~~2011~~;4F11~500B~0~0~0~0~40~28~155339~20100108~15951883878~被叫本地~0~0~0~0~~2011~~;4859~500B~0~0~0~0~40~30~170758~20100108~13813893836~主叫本地~40~0~0~0~~2011~~;D590~500B~0~0~0~0~40~46~175637~20100108~13813893836~主叫本地~40~0~0~0~~2011~~;485E~500B~0~0~0~0~40~57~180601~20100108~13813893836~主叫本地~40~0~0~0~~2011~~;485E~500B~0~0~0~0~80~104~181643~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;485E~500B~0~0~0~0~80~66~211102~20100108~13813893836~主叫本地~80~0~0~0~~2011~~;485E~500B~0~0~0~0~160~207~211505~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;D590~500B~0~0~0~0~40~27~212514~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~17~100252~20100109~13813994536~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~15~142156~20100109~13813893836~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~46~143652~20100109~13813893836~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~44~155654~20100109~86558812~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~38~155747~20100109~86558812~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~160~199~160238~20100109~4008205100~主叫本地~160~0~0~0~~2011~~;485E~500B~0~0~0~0~224~192~112908~20100110~13773886563~主叫本地~224~0~0~0~~2011~~;39F1~500E~0~0~0~0~120~124~085905~20100111~13913977172~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~32~091841~20100111~13913814503~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~12~095926~20100111~13913814503~主叫本地~40~0~0~0~~2011~~;39EF~500E~0~0~0~0~80~67~100027~20100111~13913814503~主叫本地~80~0~0~0~~2011~~;</XTABLE_GSM></content></operation_out>";
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_gsmcdr_query_501", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
					
							List<Map<String,String>> respList = null;
							String retContent = root.getChild("content").getChildText("xtcdr");
							respList = TeletextParseUtils.parseXTABLE(retContent);
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									GsmBillDetail bd = new GsmBillDetail();
									bd.setStatusType(m.get("状态类型"));
									bd.setOtherParty(m.get("对方号码"));
									bd.setStartTime(m.get("通话时间"));
									bd.setCallDuration(m.get("通话时长"));
									bd.setFirstCfee(Double.parseDouble((m.get("应收基本通话费") == null || m.get("应收基本通话费").equals("")) ? "0" : m.get("应收基本通话费")));
									bd.setRealLfeeAndFirstOfee(Double.parseDouble((m.get("应收长途话费") == null || m.get("应收长途话费").equals("")) ? "0" : m.get("应收长途话费")) + Double.parseDouble((m.get("应收其他话费") == null || m.get("应收其他话费").equals("")) ? "0" : m.get("应收其他话费")));
									bd.setRealCfee(Double.parseDouble((m.get("基本话费") == null || m.get("基本话费").equals("")) ? "0" : m.get("基本话费")));
									bd.setRealLfee(Double.parseDouble((m.get("长途话费") == null || m.get("长途话费").equals("")) ? "0" : m.get("长途话费")));
									bd.setTotalFee(Double.parseDouble((m.get("小计") == null || m.get("小计").equals("")) ? "0" : m.get("小计")));
									bd.setFeeItem01(Double.parseDouble((m.get("套餐费") == null || m.get("套餐费").equals("")) ? "0" : m.get("套餐费")));
									bd.setVisitArear(m.get("通话地点"));
									bd.setCallType(m.get("通信方式"));
									bd.setRoamType(m.get("通信类型"));
									bd.setFreeCode(m.get("产品编码"));
									//套餐描述
									String pckDscTmp = m.get("套餐");
									if(" ".trim().equals(pckDscTmp)) {
										bd.setPkgCode("无");
									} else {
										bd.setPkgCode(pckDscTmp);
									}
									reList.add(bd);
								}
							}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		
		return reList;
	}
	
	
	/**
	 * 查询IP直通车清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<IpcarBillDetail> getIpcarCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<IpcarBillDetail> reList = new ArrayList<IpcarBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_ipcarcdr_query_504", params), 
					 accessId, "ac_ipcarcdr_query_504", city));
			logger.debug(" ====== 查询IP直通车清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_ipcarcdr_query_504", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
					
							List<Map<String,String>> respList = null;
							String retContent = root.getChild("content").getChildText("xtcdr");
							respList = TeletextParseUtils.parseXTABLE(retContent);
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									IpcarBillDetail bd = new IpcarBillDetail();
									bd.setVisitArear(m.get("通话地点"));
									bd.setFreeFee(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
									bd.setStatusType(m.get("通信类型"));
									bd.setOtherParty(m.get("对方号码"));
									bd.setStartTime(m.get("起始时间"));
									bd.setCallDuration(m.get("通话时长"));
									bd.setRealCfee(Double.parseDouble(m.get("基本话费") == null || "".equals(m.get("基本话费"))?"0":m.get("基本话费")));
									bd.setRealLfee(Double.parseDouble(m.get("长途话费") == null || "".equals(m.get("长途话费"))?"0":m.get("长途话费")));
									bd.setTotalFee(Double.parseDouble(m.get("小计") == null || "".equals(m.get("小计"))?"0":m.get("小计")));
									reList.add(bd);
								}
							}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询代理ISP清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<IspBillDetail> getIspCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<IspBillDetail> reList = new ArrayList<IspBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_ispcdr_query_506", params), 
					 accessId, "ac_ispcdr_query_506", city));
			logger.debug(" ====== 查询代理ISP清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_ispcdr_query_506", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
							List<Map<String,String>> respList = null;
							String retContent = root.getChild("content").getChildText("xtcdr");
							respList = TeletextParseUtils.parseXTABLE(retContent);
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									IspBillDetail bd = new IspBillDetail();
									bd.setStatusType(m.get("状态类型"));
									bd.setCallingStationId(m.get("主叫号码"));
									bd.setCalleeNum(m.get("被叫号码"));
									bd.setStartTime(m.get("起始时间"));
									bd.setCallDuration(m.get("总时长"));
									bd.setIspCode(m.get("ISP服务商"));
									bd.setNetFee(Double.parseDouble(m.get("费用") == null || "".equals(m.get("费用"))?"0":m.get("费用")));
									bd.setPkgFee(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
									bd.setTotalFee(Double.parseDouble(m.get("总费用") == null || "".equals(m.get("总费用"))?"0":m.get("总费用")));
									reList.add(bd);
								}
							}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	/**
	 * 查询梦网清单 代收
	 */
	public List<MontnetBillDetail> getMontnetDSCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<MontnetBillDetail> reList = new ArrayList<MontnetBillDetail>();
		String rspXml = "";
		String rspXmll = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXmll = (String)this.remote.callRemote(new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_montnettcdr_query_511", params), 
					 accessId, "ac_montnettcdr_query_511", city));
			logger.debug(" ====== 查询梦网代收业务清单返回报文 ======\n" + rspXml);
			if (null != rspXmll && !"".equals(rspXmll))
			{
				Element root = this.config.getElement(rspXmll);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_montnetcdr_query_511", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								MontnetBillDetail bd = new MontnetBillDetail();
								bd.setStatusType(m.get("状态类型"));
								bd.setCdrSpCode(m.get("业务提供商"));
								bd.setADetailName(m.get("业务名称"));
								//话单生成日期
								bd.setStartTime(m.get("时间"));
								//时长/流量
								bd.setOrgSmFee(Double.parseDouble(m.get("通信费") == null || "".equals(m.get("通信费"))?"0":m.get("通信费")));
								bd.setInfoFee(Double.parseDouble(m.get("信息费") == null || "".equals(m.get("信息费"))?"0":m.get("信息费")));
								bd.setTotalFee(Double.parseDouble(m.get("费用") == null || "".equals(m.get("费用"))?"0":m.get("费用")));
								bd.setAActiveFlag(m.get("计费类型"));
								bd.setCdrServiceCode(m.get("业务代码"));
								bd.setUserType(m.get("使用方式"));
								bd.setRoamType(m.get("漫游类型"));
								reList.add(bd);
							}
						}
						
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	/**
	 * 查询梦网清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MontnetBillDetail> getMontnetCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<MontnetBillDetail> reList = new ArrayList<MontnetBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_montnetcdr_query_511", params), 
					 accessId, "ac_montnetcdr_query_511", city));
			logger.debug(" ====== 查询梦网自有业务清单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_montnettcdr_query_511", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
									MontnetBillDetail bd = new MontnetBillDetail();
									bd.setStatusType(m.get("状态类型"));
									bd.setCdrSpCode("移动");
									bd.setADetailName(m.get("业务名称"));
									//话单生成日期
									bd.setStartTime(m.get("时间"));
									//时长/流量
									bd.setOrgSmFee(Double.parseDouble(m.get("通信费") == null || "".equals(m.get("通信费"))?"0":m.get("通信费")));
									bd.setInfoFee(Double.parseDouble(m.get("信息费") == null || "".equals(m.get("信息费"))?"0":m.get("信息费")));
									bd.setTotalFee(Double.parseDouble(m.get("费用") == null || "".equals(m.get("费用"))?"0":m.get("费用")));
									bd.setAActiveFlag(m.get("计费类型"));
									bd.setCdrServiceCode(m.get("业务端口"));
									bd.setUserType(m.get("使用方式"));
									bd.setRoamType(m.get("漫游类型"));
									reList.add(bd);
							}
						}
						
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询彩信清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MmsBillDetail> getMmsCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<MmsBillDetail> reList = new ArrayList<MmsBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_mmscdr_query_507", params), 
					 accessId, "ac_mmscdr_query_507", this.generateCity(params)));
			logger.debug(" ====== 查询彩信清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_mmscdr_query_507", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								MmsBillDetail bd = new MmsBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setSpCode(m.get("sp服务"));
								bd.setMmsLen(Long.parseLong(m.get("彩信长度")));
								bd.setSendTime(m.get("起始时间"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setOrgSmFee(Double.parseDouble(m.get("基本费") == null || "".equals(m.get("基本费"))?"0":m.get("基本费")));
								bd.setInfoFee(Double.parseDouble(m.get("信息费") == null || "".equals(m.get("信息费"))?"0":m.get("信息费")));
								bd.setTotalFee(Double.parseDouble(m.get("通信费") == null || "".equals(m.get("通信费"))?"0":m.get("通信费")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询短信清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<SmsBillDetail> getSmsCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<SmsBillDetail> reList = new ArrayList<SmsBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_smscdr_query_503", params), 
					 accessId, "ac_smscdr_query_503", this.generateCity(params)));
			logger.debug(" ====== 查询短信清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_smscdr_query_503", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								SmsBillDetail bd = new SmsBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("起始时间"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setTotalFee(Double.parseDouble(m.get("实收费") == null || "".equals(m.get("实收费"))?"0":m.get("实收费")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
								//修改
								bd.setInfoLen(Long.parseLong(m.get("信息长度")));
								reList.add(bd);
								
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询国际短信清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<SmsBillDetail> getIneSmsCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<SmsBillDetail> reList = new ArrayList<SmsBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_ineSmscdr_query_503", params), 
					 accessId, "ac_ineSmscdr_query_503", this.generateCity(params)));
			logger.debug(" ====== 查询国际短信清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_ineSmscdr_query_503", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								SmsBillDetail bd = new SmsBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("起始时间"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setTotalFee(Double.parseDouble(m.get("通信费") == null || "".equals(m.get("通信费"))?"0":m.get("通信费")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询GPRS清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<GprsBillDetail> getGprsCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<GprsBillDetail> reList = new ArrayList<GprsBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_gprscdr_query_502", params), 
					 accessId, "ac_gprscdr_query_502", this.generateCity(params)));
			logger.debug(" ====== 查询GPRS清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_gprscdr_query_502", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								GprsBillDetail bd = new GprsBillDetail();
								//初始化业务Bean
								bd.setStatusType(m.get("状态类型"));
								bd.setStartTime(m.get("起始时间"));
								bd.setCdrApnni(m.get("上网方式"));
								bd.setVisitArear(m.get("通话地点"));
								bd.setDuration(Double.parseDouble(m.get("总时长")));
								bd.setTotalFlus(Double.parseDouble(m.get("上传流量"))+Double.parseDouble(m.get("下载流量")));
								bd.setBusyData(Double.parseDouble(m.get("基本收费流量")));
								bd.setIdlesseData(Double.parseDouble(m.get("其他收费流量")));
								bd.setBusyFee(Double.parseDouble(m.get("基本费用")));
								bd.setTotalFee(Double.parseDouble(m.get("总费用")));
								bd.setOtherFee(Double.parseDouble(m.get("其他费用")));
								bd.setPackageFee(Double.parseDouble(m.get("套餐费")));
								String pckDscTmp = m.get("套餐");
								if(" ".trim().equals(pckDscTmp)) {
									bd.setMsnc("无");
								} else {
									bd.setMsnc(pckDscTmp);
								}
//								bd.setGroupFee(Double.parseDouble(null!=m.get("统付流量")?"0":m.get("统付流量")));
								// 统付流量单位：B转成KB
//								bd.setGroupFee(Math.round(Double.parseDouble((m.get("统付流量") == null || m.get("统付流量").equals("")) ? "0" : m.get("统付流量"))/1024));
								// 统付流量 ，单位KB ，展示形式 XXX
								bd.setGroupFee(Double.parseDouble(m.get("统付流量")));
								// 明细
								bd.setDetail((m.get("明细") == null || m.get("明细").equals("")) ? "" : m.get("明细")); 
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	
	/**
	 * 查询短号码清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<VpmnBillDetail> getVpmnCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<VpmnBillDetail> reList = new ArrayList<VpmnBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_vpmncdr_query_505", params), 
					 accessId, "ac_vpmncdr_query_505", this.generateCity(params)));
			logger.debug(" ====== 查询短号码清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_vpmncdr_query_505", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								VpmnBillDetail bd = new VpmnBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setCdrStartDate(m.get("起始时间"));			
								bd.setCallDuration(m.get("通话时长"));
								bd.setRealCfee(Double.parseDouble(m.get("基本话费") == null || "".equals(m.get("基本话费"))?"0":m.get("基本话费")));
								bd.setRealLfee(Double.parseDouble(m.get("长途话费") == null || "".equals(m.get("长途话费"))?"0":m.get("长途话费")));
								bd.setOtherFee(Double.parseDouble(m.get("应收其他话费") == null || "".equals(m.get("应收其他话费"))?"0":m.get("应收其他话费")));
								bd.setTotalFee(Double.parseDouble(m.get("小计") == null || "".equals(m.get("小计"))?"0":m.get("小计")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
								bd.setVisitArear(m.get("通信地点"));		
								//套餐描述
								String pckDscTmp = m.get("套餐信息");
								String pckCode = "";
								if (!"".equals(pckDscTmp)) {
									String[] codes = pckDscTmp.split(",");
									if (codes != null && codes.length > 0) {
										for (int i = 0; i < codes.length; i++) {
											String tmpCode = codes[i].split("\\|")[0];
											if(!"".equals(tmpCode)){
												pckCode = pckCode + "," + tmpCode;
											}
										}
									}
								}
								
								//集团短号
								String shortNum = m.get("集团短号");
								if(null != shortNum || !"".equals(shortNum))
								{
									bd.setShortNum(shortNum);
								}
								if(!"".equals(pckCode)){
									pckCode = pckCode.substring(1);
								}
								bd.setTpRemark(pckCode);
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询WLAN清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<WlanBillDetail> getWlanCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<WlanBillDetail> reList = new ArrayList<WlanBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_wlancdr_query_508", params), 
					 accessId, "ac_wlancdr_query_508", this.generateCity(params)));
			logger.debug(" ====== 查询WLAN清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_wlancdr_query_508", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								WlanBillDetail bd = new WlanBillDetail();
								bd.setStatusType(m.get("状态类型"));
								bd.setStartTime(m.get("起始时间"));
								bd.setCallDuration(m.get("总时长"));
								bd.setDataUp(Long.parseLong(m.get("上行数据流量")));
								bd.setDataDown(Long.parseLong(m.get("下行数据流量")));
								bd.setIspCode(m.get("上网方式"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setOrgSmFee(Double.parseDouble(m.get("基本费") == null || "".equals(m.get("基本费"))?"0":m.get("基本费"))/100);
								bd.setInfoFee(Double.parseDouble(m.get("信息费") == null || "".equals(m.get("信息费"))?"0":m.get("信息费"))/100);
								bd.setTotalFee(Double.parseDouble(m.get("小计") == null || "".equals(m.get("小计"))?"0":m.get("小计"))/100);
								bd.setAuthType(m.get("认证类型"));
								bd.setRateTotalflow(m.get("计费总流量") == null || "".equals(m.get("计费总流量")) ? 0 : Long.parseLong((String)m.get("计费总流量")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	
	/**
	 * 查询96121清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<Ac121BillDetail> getAc121CdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<Ac121BillDetail> reList = new ArrayList<Ac121BillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_121cdr_query_522", params), 
					 accessId, "ac_121cdr_query_522", this.generateCity(params)));
			logger.debug(" ====== 查询96121清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_121cdr_query_522", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								Ac121BillDetail bd = new Ac121BillDetail();
								bd.setBusiName(m.get("业务名称"));
								bd.setUseType(m.get("使用方式"));
								bd.setOtherParty(m.get("业务端口"));
								bd.setStartTime(m.get("时间"));
								bd.setCallDuration(m.get("时长(时分秒)"));
								bd.setNetFee(Double.parseDouble(m.get("费用") == null || "".equals(m.get("费用"))?"0":m.get("费用")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询17202清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<Isp2BillDetail> getIsp2CdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<Isp2BillDetail> reList = new ArrayList<Isp2BillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_isp2cdr_query_509", params), 
					 accessId, "ac_isp2cdr_query_509", this.generateCity(params)));
			logger.debug(" ====== 查询17202清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_isp2cdr_query_509", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								Isp2BillDetail bd = new Isp2BillDetail();
								bd.setCallingStationId(m.get("主叫号码"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setType(m.get("上网方式"));
								bd.setPackageFee(Double.parseDouble(m.get("套餐费用") == null || "".equals(m.get("套餐费用"))?"0":m.get("套餐费用")));
								bd.setCalleeNum(m.get("被叫号码"));
								bd.setStartTime(m.get("起始时间"));
								bd.setCallDuration(m.get("时长"));
								bd.setTotalFee(Double.parseDouble(m.get("通信费") == null || " ".trim().equals(m.get("通信费"))?"0":m.get("通信费")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	
	/**
	 * 查询USSD清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<UssdBillDetail> getUssdCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<UssdBillDetail> reList = new ArrayList<UssdBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_ussdcdr_query_510", params), 
					 accessId, "ac_ussdcdr_query_510", this.generateCity(params)));
			logger.debug(" ====== 查询USSD清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_ussdcdr_query_510", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("XTABLE_USSD");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								UssdBillDetail bd = new UssdBillDetail();
								bd.setCdrMsisdn(m.get("计费号码"));
								bd.setUssdId(m.get("业务接入号"));
								//USSD清单页面上的开始时间
								bd.setStartTime(m.get("开始时间"));
								bd.setCallDuration(m.get("通话时长"));
								bd.setCallFee(Double.parseDouble(m.get("对话通信费") == null || "".equals(m.get("对话通信费"))?"0":m.get("对话通信费")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询无线音乐清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MpmusicBillDetail> getMpmusicCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<MpmusicBillDetail> reList = new ArrayList<MpmusicBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_mpmusiccdr_query_520", params), 
					 accessId, "ac_mpmusiccdr_query_520", this.generateCity(params)));
			logger.debug(" ====== 查询无线音乐清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_mpmusiccdr_query_520", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
							List<Map<String,String>> respList = null;
							String retContent = root.getChild("content").getChildText("xtcdr");
							respList = TeletextParseUtils.parseXTABLE(retContent);
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									MpmusicBillDetail bd = new MpmusicBillDetail();
									bd.setSourceTypeName(m.get("业务类型"));
									bd.setSpCode(m.get("全网SP代码"));
									bd.setContentId(m.get("业务名称"));
									bd.setStartTime(m.get("时间"));
									bd.setRealInfoFee(Double.parseDouble(m.get("费用") == null || "".equals(m.get("费用"))?"0":m.get("费用")));
									bd.setFreetimeItem(m.get("赠送项目"));
									bd.setFreetime(Long.parseLong(m.get("赠送数")));
									bd.setChargeTypeName(m.get("计费类型(名称）"));
									reList.add(bd);
								}
							}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	
	/**
	 * 查询LBS清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<LbsBillDetail> getLbsCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<LbsBillDetail> reList = new ArrayList<LbsBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_lbscdr_query_521", params), 
					 accessId, "ac_lbscdr_query_521", this.generateCity(params)));
			logger.debug(" ====== 查询LBS清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_lbscdr_query_521", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								LbsBillDetail bd = new LbsBillDetail();
								bd.setTypeName(m.get("类型"));
								bd.setMsisdn(m.get("发起定位方号码"));
								bd.setOtherParty(m.get("被定位方号码"));
								bd.setRealInfoFee(Double.parseDouble("".equals((String)m.get("费用"))?"0":(String)m.get("费用")));
								bd.setStartTime(m.get("时间"));
								bd.setXInfo(m.get("定位结果（X信息）"));
								bd.setYInfo(m.get("定位结果（Y信息）"));
								bd.setBusiName(m.get("业务名称"));
								bd.setUseType(m.get("使用方式"));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	
	/**
	 * 即时群聊清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MeetBillDetail> getMeetCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<MeetBillDetail> reList = new ArrayList<MeetBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_meet_query_523", params), 
					 accessId, "ac_meet_query_523", this.generateCity(params)));
			logger.debug(" ====== 即时群聊清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_meet_query_523", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								MeetBillDetail bd = new MeetBillDetail();
								bd.setToStationId(m.get("对方号码"));
								bd.setBusiName(m.get("业务名称"));
								bd.setStartTime(m.get("时间"));
								bd.setCallDuration(m.get("时长(时分秒)"));
								bd.setNetFee(Double.parseDouble(m.get("实收长途费") == null || "".equals(m.get("实收长途费"))?"0":m.get("实收长途费")));
								bd.setOtherFee(Double.parseDouble(m.get("实收其它费") == null || "".equals(m.get("实收其它费"))?"0":m.get("实收其它费")));
								bd.setBaseFee(Double.parseDouble(m.get("实收基本费") == null || "".equals(m.get("实收基本费"))?"0":m.get("实收基本费")));
								bd.setTotalFee(Double.parseDouble(m.get("费用") == null || "".equals(m.get("费用"))?"0":m.get("费用")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	
	/**
	 * 视频通话清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<GsmVideoBillDetail> getGsmVideoCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<GsmVideoBillDetail> reList = new ArrayList<GsmVideoBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_gsmcdr_video_query_539", params), 
					 accessId, "ac_gsmcdr_video_query_539", this.generateCity(params)));
			logger.debug(" ====== 查询视频通话清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_gsmcdr_video_query_539", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								GsmVideoBillDetail bd = new GsmVideoBillDetail();
								bd.setVisitArear(m.get("通信地点"));
								bd.setStatusType(m.get("通信类型"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("开始时间"));
								//处理时长，BOSS传过来的值的类型是long型。（格式：hh:mm:ss）
								bd.setCallDuration(m.get("通信时长"));
								bd.setRealCfee(Double.parseDouble(m.get("基本费") == null || "".equals(m.get("基本费")) ? "0" : m.get("基本费")));//
								bd.setRealLfee(Double.parseDouble(m.get("长途费") == null || "".equals(m.get("长途费"))? "0" : m.get("长途费")));
								bd.setTotalFee(Double.parseDouble(m.get("实收通信费") == null || "".equals(m.get("实收通信费"))?"0":m.get("实收通信费")));					
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费"))?"0":m.get("套餐费")));
								bd.setServiceCode(m.get("网络"));
								//套餐描述
								String pckDscTmp = m.get("套餐信息");
								String pckCode = "";
								if (pckDscTmp!=null && !"".equals(pckDscTmp)) {
									String[] codes = pckDscTmp.split(",");
									if (codes != null && codes.length > 0) {
										for (int i = 0; i < codes.length; i++) {
											String tmpCode = codes[i].split("\\|")[0];
											if(!"".equals(tmpCode)){
												pckCode = pckCode + "," + tmpCode;
											}
										}
									}
								}
								if(!"".equals(pckCode)){
									pckCode = pckCode.substring(1);
								}
								bd.setTpRemark(pckCode);
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 随E行上网本清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<CmnetBillDetail> getCmnetCdrInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<CmnetBillDetail> reList = new ArrayList<CmnetBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_cmnetcdr_query_540", params), 
					 accessId, "ac_cmnetcdr_query_540", this.generateCity(params)));
			logger.debug(" ====== 随E行上网本清单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_cmnetcdr_query_540", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						List<?> children = root.getChild("content").getChildren();
						for(int j = 0; j < children.size(); j++)
						{
							Element e = (Element)children.get(j);
							if(e.getName().startsWith("xtcdr"))
							{
								String retContent = e.getText();
								respList = TeletextParseUtils.parseXTABLE(retContent);
								if(respList != null)
								{
									for(Map<String,String> m : respList)
									{
										CmnetBillDetail bd = new CmnetBillDetail();
										bd.setCdrApnni(m.get("上网方式"));
										bd.setVisitArear(m.get("通话地点"));
										bd.setDataUpAndDown(Double.parseDouble(m.get("总流量")));
										bd.setOtherFee(Double.parseDouble(m.get("其他费用") == null || "".equals(m.get("其他费用")) ? "0" : m.get("其他费用")));
										bd.setPkgFee(Double.parseDouble(m.get("套餐费") == null || "".equals(m.get("套餐费")) ? "0" : m.get("套餐费")));
										bd.setStartTime(m.get("起始时间"));
										bd.setStatusType(m.get("状态类型"));
										bd.setFullTime(m.get("总时长"));
										bd.setAllTotalFee(Double.parseDouble(m.get("基本费用") == null || "".equals(m.get("基本费用")) ? "0" : m.get("基本费用")));
										bd.setCallDuration(Double.parseDouble(m.get("基本收费流量") == null || "".equals(m.get("基本收费流量")) ? "0" : m.get("基本收费流量")));
										bd.setTotalFee(Double.parseDouble(m.get("总费用") == null || "".equals(m.get("总费用")) ? "0" : m.get("总费用")));
										bd.setRealLfee(Double.parseDouble(m.get("其他收费流量") == null || "".equals(m.get("其他收费流量")) ? "0" : m.get("其他收费流量")));
										//套餐描述
										String pckDscTmp = m.get("套餐编码");
										String pckCode = "";
										if (!"".equals(pckDscTmp)) {
											String[] codes = pckDscTmp.split(",");
											if (codes != null && codes.length > 0) {
												for (int i = 0; i < codes.length; i++) {
													String tmpCode = codes[i].split("\\|")[0];
													if(!"".equals(tmpCode)){
														pckCode = pckCode + "," + tmpCode;
													}
												}
											}
										}
										if(!"".equals(pckCode)){
											pckCode = pckCode.substring(1);
										}
										
										bd.setTpremark(pckCode);
										reList.add(bd);
									}
								}
								break;
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		return reList;
	}
	
	/**
	 * 查询语音杂志
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<GsmMagaBillDetail> getGsmCdrMagazineInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<GsmMagaBillDetail> reList = new ArrayList<GsmMagaBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("ac_gsmcdr_query_520", params), 
					 accessId, "ac_gsmcdr_query_520", city));
			logger.debug(" ====== 查询语音话单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_gsmcdr_query_501", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								GsmMagaBillDetail bd = new GsmMagaBillDetail();
								bd.setStatusType(m.get("状态类型"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("通话时间"));
								bd.setCallDuration(m.get("通话时长"));
								bd.setFirstCfee(Double.parseDouble((m.get("应收基本通话费") == null || m.get("应收基本通话费").equals("")) ? "0" : m.get("应收基本通话费")));
								bd.setRealLfeeAndFirstOfee(Double.parseDouble((m.get("应收长途话费") == null || m.get("应收长途话费").equals("")) ? "0" : m.get("应收长途话费")) + Double.parseDouble((m.get("应收其他话费") == null || m.get("应收其他话费").equals("")) ? "0" : m.get("应收其他话费")));
								bd.setRealCfee(Double.parseDouble((m.get("基本话费") == null || m.get("基本话费").equals("")) ? "0" : m.get("基本话费")));
								bd.setRealLfee(Double.parseDouble((m.get("长途话费") == null || m.get("长途话费").equals("")) ? "0" : m.get("长途话费")));
								bd.setTotalFee(Double.parseDouble((m.get("小计") == null || m.get("小计").equals("")) ? "0" : m.get("小计")));
								bd.setFeeItem01(Double.parseDouble((m.get("套餐费") == null || m.get("套餐费").equals("")) ? "0" : m.get("套餐费")));
								bd.setVisitArear(m.get("通话地点"));
								bd.setCallType(m.get("通信方式"));
								bd.setRoamType(m.get("通信类型"));
								bd.setFreeCode(m.get("产品编码"));
								//套餐描述
								String pckDscTmp = m.get("套餐");
								if(" ".trim().equals(pckDscTmp)) {
									bd.setPkgCode("无");
								} else {
									bd.setPkgCode(pckDscTmp);
								}
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		
		return reList;
	}
	/**
	 * 查询游戏详单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<CdrsBillDetail> getGsmCDRQueryInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<CdrsBillDetail> reList = new ArrayList<CdrsBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					new StringTeletext(
							this.bossTeletextUtil.mergeTeletext("ac_cdr_query_542", params), 
							accessId, "ac_cdr_query_541", city));
			logger.debug(" ====== 查询游戏详单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_cdr_query_542", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								CdrsBillDetail cqbp = new CdrsBillDetail();

								cqbp.setBusinessName(m.get("业务名称"));
								cqbp.setServiceCode(m.get("业务代码"));
								cqbp.setBillType(m.get("计费类型"));
								cqbp.setBusinessPort(m.get("业务端口"));
								cqbp.setRoamType(m.get("漫游类型"));
								cqbp.setSpService(m.get("sp服务(服务商名称)"));
								cqbp.setStatus(m.get("状态类型"));
								cqbp.setTime(m.get("时间"));
								cqbp.setUseWay(m.get("使用方式"));
								cqbp.setBusinessCode(m.get("企业代码"));
								cqbp.setCommunicationPay(String.valueOf((m.get("通信费") == null || m.get("通信费").equals("")) ? "0" : m.get("通信费")));
								cqbp.setInfoPay(String.valueOf((m.get("信息费") == null || m.get("信息费").equals("")) ? "0" : m.get("信息费")));
								cqbp.setMoney(String.valueOf((m.get("费用") == null || m.get("费用").equals("")) ? "0" : m.get("费用")));
							
							
								reList.add(cqbp);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		
		return reList;
	}
	/**
	 * 查询游戏点数详单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<CdrsPointBillDetail> getGsmCDRQueryPointInfo (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010001Result res)
	{
		List<CdrsPointBillDetail> reList = new ArrayList<CdrsPointBillDetail>();
		String rspXml = "";
		ErrorMapping errDt = null;
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					new StringTeletext(
							this.bossTeletextUtil.mergeTeletext("ac_cdr_query_541", params), 
							accessId, "ac_cdr_query_541", city));
			logger.debug(" ====== 查询游戏详单返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010001", "ac_cdr_query_541", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
						List<Map<String,String>> respList = null;
						String retContent = root.getChild("content").getChildText("xtcdr");
						respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								CdrsPointBillDetail cqb = new CdrsPointBillDetail();
								cqb.setBusinessName(m.get("业务名称"));
								cqb.setServiceCode(m.get("业务代码"));
								cqb.setSpendingMoney(String.valueOf((m.get("消费金额") == null || m.get("消费金额").equals("")) ? "0" : m.get("消费金额")));
								cqb.setSpendingPoint(String.valueOf((m.get("消费点数(100点=1元)") == null || m.get("消费点数(100点=1元)").equals("")) ? "0" : m.get("消费点数(100点=1元)")));
								cqb.setSpendingTime(String.valueOf(m.get("消费时间")));
								reList.add(cqb);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			reList=null;
		}
		
		return reList;
	}
}