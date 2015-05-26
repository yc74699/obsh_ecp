package com.xwtech.xwecp.service.logic.invocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
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
import com.xwtech.xwecp.service.logic.pojo.QRY010037Result;
import com.xwtech.xwecp.service.logic.pojo.SmsBillDetail;
import com.xwtech.xwecp.service.logic.pojo.UssdBillDetail;
import com.xwtech.xwecp.service.logic.pojo.VpmnBillDetail;
import com.xwtech.xwecp.service.logic.pojo.WlanBillDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.TeletextParseUtils;

/**
 * 分布式语音清单
 * @author xufan
 * 2014-06-23
 */
public class BillNewQueryInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(BillNewQueryInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public BillNewQueryInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY010037Result res = new QRY010037Result();
		String cdr_type=(String)getParameters(params,"cdr_type");//判断清单类型
		
		try
		{
			res.setResultCode("0");
			res.setErrorMessage("");
			String beginDate = "";
			boolean findBeginDate = false;
			for(RequestParameter param : params)
			{
				String paramName = param.getParameterName();
				if(paramName.equals("begin_day"))
				{
					beginDate = (String)param.getParameterValue();
					findBeginDate = true;
				}
				if(findBeginDate)
					break;
			}
			//月份
			String month = beginDate.substring(0,6);
			RequestParameter paramMonth = new RequestParameter("month",month);
			params.add(paramMonth);
			res=getCdrTypeBill(accessId,config,params,res,Integer.parseInt(cdr_type));
			
			}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		
		return res;
	}
	/**
	 * 查询清单报文，根据cdr_type来选着属于哪个清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public QRY010037Result getCdrTypeBill (String accessId, ServiceConfig config, List<RequestParameter> params, QRY010037Result res,Integer cdr_type)
	{
		String rspXml = "";
		ErrorMapping errDt = null;
		Map<String,String>mapBill=new HashMap<String,String>();
		List<Map<String,String>>listMap=new ArrayList<Map<String,String>>();
		
		try
		{
			RemoteCallContext city = this.generateCity(params);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_DDBQUERY", params), 
					 accessId, "cc_DDBQUERY", city));
			rspXml=rspXml.trim();
			logger.debug(" ====== 查询语音清单返回报文 ======\n" + rspXml);
//			rspXml="<?xml \"1.0\" encoding=\"gbk\" standalone=\"no\" ?><operation_out><req_seq>0_2</req_seq><resp_seq>20141020112835</resp_seq><resp_time>20141020112835</resp_time><response><resp_type>0</resp_type><resp_code>0</resp_code><resp_desc><![CDATA[成功]]></resp_desc></response><content><ddb_nextno>1</ddb_nextno><Key>7:1000:1408914193000203976572014080120140801174112M734a31f6-1907-11e4-afb4-af597071c0c4.86375720M</Key><is_end>0</is_end><name_data>起始时间~上网方式~通话地点~总时长~基本收费流量~基本费用(元)~套餐费(元)~套餐~其他费用(元)~其他收费流量~总费用(元)</name_data><length_data>14~6~40~6~12~12~12~256~12~12~12</length_data><xtable_data>2014-07-31 23:59:44~手机2Gcmwap IPV4~南京~05:23:14~0M780K~0.00~0.00~流量优惠包30M（省内通用流量）|动感上网套餐（社会版2014）18元~0.00~0M0K~0.00~;2014-08-01 05:24:38~手机2Gcmwap IPV4~南京~01:09:57~0M11K~0.00~0.00~动感上网套餐（社会版2014）18元~0.00~0M0K~0.00~;2014-08-01 13:01:38~手机2Gcmwap IPV4~南京~01:18:32~0M93K~0.00~0.00~动感上网套餐（社会版2014）18元~0.00~0M0K~0.00~;2014-08-01 14:21:51~手机2Gcmwap IPV4~南京~02:15:12~0M0K~0.00~0.00~~0.00~0M0K~0.00~;2014-08-01 16:38:44~手机2Gcmwap IPV4~南京~01:00:46~0M11K~0.00~0.00~动感上网套餐（社会版2014）18元~0.00~0M0K~0.00~;</xtable_data><is_print></is_print></content></operation_out>";
//			rspXml = "<?xml version=\"1.0\" encoding=\"gbk\"?><operation_out><process_code>ac_gsmcdr_query</process_code><request_type/><sysfunc_id>501</sysfunc_id><request_seq>1_64</request_seq><response_time>20100111101300</response_time><request_source>102010</request_source><response><resp_type>0</resp_type><resp_code>0000</resp_code><resp_desc/></response><content><XTABLE_GSM>基站号~位置小区号~小计~其他话费~实收长途话费~实收基本话费~应收费用~通话时长~开始时间~开始日期~对方号码~状态类型~套餐费~应收基本通话费~应收长途话费~应收其他话费~套餐描述~服务标识~移动之家标识~;39F0~500E~0~0~0~0~40~21~030801~20100101~114~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~37~030840~20100101~12580~主叫本地~40~0~0~0~~2011~~;D590~500B~0~0~0~0~80~66~104508~20100101~13182801923~主叫本地~80~0~0~0~~2011~~;427D~5114~0~0~0~0~40~25~114511~20100101~13182801923~主叫本地~40~0~0~0~~2011~~;42C4~5114~0~0~0~0~40~28~134841~20100101~13951880339~被叫本地~0~0~0~0~~2011~~;B3CE~500B~0~0~0~0~40~25~182120~20100101~13182801923~主叫本地~40~0~0~0~~2011~~;1286~D106~0~0~0~0~315~266~173532~20100102~13773886563~主叫本地~315~0~0~0~~3011~~;1286~D106~0~0~0~0~40~10~200323~20100102~15195942183~主叫本地~40~0~0~0~~3011~~;D58F~500B~0~0~0~0~80~95~200358~20100102~15195942183~主叫本地~80~0~0~0~~2011~~;8DC3~D106~0~0~0~0~80~80~093228~20100103~15951883878~主叫本地~80~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~11~093735~20100103~15951883878~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~42~095317~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~13~105858~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;1249~D106~0~0~0~0~40~20~113002~20100103~13813076323~主叫本地~40~0~0~0~~3011~~;15C3~D107~0~0~0~0~40~10~144105~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;15C3~D107~0~0~0~0~40~22~144355~20100103~13813076323~被叫本地~0~0~0~0~~3011~~;15C3~D107~0~0~0~0~40~10~150301~20100103~13813076323~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~51~093923~20100104~02568599379~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~7~094125~20100104~02568599379~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~45~094151~20100104~02568599379~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~094604~20100104~02568599379~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~360~540~104202~20100104~02568599379~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~160~215~135557~20100104~13951758689~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~144112~20100104~13770794389~被叫本地~0~0~0~0~~3011~~;3036~500B~0~0~0~0~112~95~185719~20100104~13773886563~主叫本地~112~0~0~0~~2011~~;8DC3~D106~0~0~0~0~40~20~084624~20100105~13770794389~被叫本地~0~0~0~0~~3011~~;0C64~500E~0~0~0~0~80~110~102523~20100105~13800250222~被叫本地~0~0~0~0~~2011~~;0C64~500E~0~0~0~0~40~25~103714~20100105~13813893836~被叫本地~0~0~0~0~~2011~~;0C64~500E~0~0~0~0~80~63~110026~20100105~13913814503~主叫本地~80~0~0~0~~2011~~;0C64~500E~0~0~0~0~40~27~112012~20100105~13913814503~主叫本地~40~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~22~113800~20100105~13851852109~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~240~310~135909~20100105~13851852109~主叫本地~240~0~0~0~~2011~~;39F0~500E~0~0~0~0~120~156~143141~20100105~13770794389~主叫本地~0~0~0~0~~2011~~;D590~500B~0~0~0~0~0~79~151506~20100105~10086~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~18~093120~20100106~13851852109~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~240~304~104608~20100106~02161421911~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~120~165~110408~20100106~13851852109~主叫本地~120~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~6~135512~20100106~13770794389~主叫本地~0~0~0~0~~2011~~;8DC3~D106~0~0~0~0~40~56~143655~20100106~13770794389~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~58~145738~20100106~13851954864~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~15~153330~20100106~13851852109~主叫本地~40~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~161235~20100106~02161421911~被叫本地~0~0~0~0~~3011~~;8DC3~D106~0~0~0~0~40~35~163253~20100106~13813893836~主叫本地~40~0~0~0~~3011~~;1249~D106~0~0~0~0~40~6~175639~20100106~13813893836~被叫本地~0~0~0~0~~3011~~;1325~D106~0~0~0~0~80~64~201020~20100106~13851852109~主叫本地~80~0~0~0~~3011~~;1286~D106~0~0~0~0~40~11~201645~20100106~13851852109~主叫本地~40~0~0~0~~3011~~;485E~500B~0~0~0~0~40~14~231153~20100106~13851852109~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~12~091554~20100107~13404160223~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~24~093909~20100107~13770794389~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~19~104210~20100107~13813893836~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~30~104247~20100107~13913814503~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~28~105547~20100107~13913814503~被叫本地~0~0~0~0~~2011~~;C780~51F8~0~0~0~0~40~19~135502~20100107~13851852109~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~80~65~140319~20100107~15951883878~主叫本地~80~0~0~0~~2011~~;A3F2~500B~0~0~0~0~40~28~140801~20100107~13912987355~被叫本地~0~0~0~0~~2011~~;A3FC~500B~0~0~0~0~40~15~141150~20100107~13913814503~主叫本地~40~0~0~0~~2011~~;A3FC~500B~0~0~0~0~40~36~142556~20100107~15851804848~被叫本地~0~0~0~0~~2011~~;A3FC~500B~0~0~0~0~120~145~144314~20100107~13800250222~被叫本地~0~0~0~0~~2011~~;A3FC~500B~0~0~0~0~40~10~152328~20100107~13912987355~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~40~16~152456~20100107~13912987355~主叫本地~40~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~13~175311~20100107~13813382746~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~10~183828~20100107~13813893836~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~7~184202~20100107~13773886563~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~17~021610~20100108~13813076323~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~36~031830~20100108~15951883878~主叫本地~40~0~0~0~~2011~~;D590~500B~0~0~0~0~40~9~072755~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;485E~500B~0~0~0~0~80~83~093119~20100108~13851852109~被叫本地~0~0~0~0~~2011~~;485E~500B~0~0~0~0~154~132~121648~20100108~13773886563~主叫本地~154~0~0~0~~2011~~;D590~500B~0~0~0~0~40~25~124626~20100108~15951883878~主叫本地~40~0~0~0~~2011~~;4859~500B~0~0~0~0~40~13~124727~20100108~13814028619~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~40~28~142133~20100108~13814028619~主叫本地~40~0~0~0~~2011~~;2E9B~500B~0~0~0~0~80~107~142345~20100108~13814028619~主叫本地~80~0~0~0~~2011~~;4F11~500B~0~0~0~0~40~28~155339~20100108~15951883878~被叫本地~0~0~0~0~~2011~~;4859~500B~0~0~0~0~40~30~170758~20100108~13813893836~主叫本地~40~0~0~0~~2011~~;D590~500B~0~0~0~0~40~46~175637~20100108~13813893836~主叫本地~40~0~0~0~~2011~~;485E~500B~0~0~0~0~40~57~180601~20100108~13813893836~主叫本地~40~0~0~0~~2011~~;485E~500B~0~0~0~0~80~104~181643~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;485E~500B~0~0~0~0~80~66~211102~20100108~13813893836~主叫本地~80~0~0~0~~2011~~;485E~500B~0~0~0~0~160~207~211505~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;D590~500B~0~0~0~0~40~27~212514~20100108~13813893836~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~17~100252~20100109~13813994536~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~15~142156~20100109~13813893836~被叫本地~0~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~46~143652~20100109~13813893836~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~44~155654~20100109~86558812~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~38~155747~20100109~86558812~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~160~199~160238~20100109~4008205100~主叫本地~160~0~0~0~~2011~~;485E~500B~0~0~0~0~224~192~112908~20100110~13773886563~主叫本地~224~0~0~0~~2011~~;39F1~500E~0~0~0~0~120~124~085905~20100111~13913977172~被叫本地~0~0~0~0~~2011~~;39EF~500E~0~0~0~0~40~32~091841~20100111~13913814503~主叫本地~40~0~0~0~~2011~~;39F0~500E~0~0~0~0~40~12~095926~20100111~13913814503~主叫本地~40~0~0~0~~2011~~;39EF~500E~0~0~0~0~80~67~100027~20100111~13913814503~主叫本地~80~0~0~0~~2011~~;</XTABLE_GSM></content></operation_out>";
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				res.setResultCode("0".equals(resp_code)?"0":"1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (!"0".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010037", "cc_DDBQUERY", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0".equals(resp_code)))
				{
					res.setResultCode("0");
					try
					{
					
							List<Map<String,String>> contentList = null;//返回list值
							String ddbNextno = root.getChild("content").getChildText("ddb_nextno");
							String keyV = root.getChild("content").getChildText("Key");//是否可以打印	0：不可以，1：可以
							String isEnd = root.getChild("content").getChildText("is_end");//是否结束0:否，1：是
							String nameData = root.getChild("content").getChildText("name_data");//标题段	例如：通话时间～通话地点～时长～对方号码～金额（元）
							String lengthData = root.getChild("content").getChildText("length_data");//列宽	10～6～8～32～10
							String xtableData = root.getChild("content").getChildText("xtable_data");//话单记录	20140115 12:12:12~南京～10～1380000～0.1; 20140115 12:12:12~南京～10～1380000～0.1
							String retContent=nameData+";"+lengthData+";"+xtableData;
							contentList = TeletextParseUtils.parseXTABLE(retContent);
							if(contentList != null)
							{
								mapBill.put("ddbNextno", ddbNextno);
								mapBill.put("keyV", keyV);
								mapBill.put("isEnd", isEnd);
								mapBill.put("nameData", nameData);
								mapBill.put("lengthData", lengthData);
								listMap.add(mapBill);
								switch(cdr_type)
								{
									//查询语音话单
									case 1:
										res.setGsmBillDetail(this.getGsmCdrInfo(contentList));
										break;
									//查询IP直通车清单
									case 2:
										res.setIpcarBillDetail(this.getIpcarCdrInfo(contentList));
										break;
									//查询代理ISP清单
									case 3:
										res.setIspBillDetail(this.getIspCdrInfo(contentList));
										break;
									//查询梦网清单
									case 104:
										res.setMontnetBillDetail(this.getMontnetCdrInfo(contentList));
										break;
									//查询彩信清单
									case 5:
										res.setMmsBillDetail(this.getMmsCdrInfo(contentList));
										break;
									//查询短信清单
									case 6:
										res.setSmsBillDetail(this.getSmsCdrInfo(contentList));
										break;
									//查询国际短信清单
									case 600:
										res.setIneSmsBillDetail(this.getIneSmsCdrInfo(contentList));
										break;
									//查询GPRS清单
									case 7:
										res.setGprsBillDetail(this.getGprsCdrInfo(contentList));
										break;
									//查询短号码清单
									case 8:
										res.setVpmnBillDetail(this.getVpmnCdrInfo(contentList));
										break;
									//查询WLAN清单
									case 9:
										res.setWlanBillDetail(this.getWlanCdrInfo(contentList));
										break;
									//查询96121清单
									case 11:
										res.setAc121BillDetail(this.getAc121CdrInfo(contentList));
										break;
									//查询无线音乐清单
									case 18:
										res.setMpmusicBillDetail(this.getMpmusicCdrInfo(contentList));
										break;
									//查询LBS清单
									case 34:
										res.setLbsBillDetail(this.getLbsCdrInfo(contentList));
										break;
									//即时群聊清单
									case 41:
										res.setMeetBillDetail(this.getMeetCdrInfo(contentList));
										break;
									//视频通话清单
									case 46:
										res.setGsmVideoBillDetail(this.getGsmVideoCdrInfo(contentList));
										break;
									//随E行上网本清单
									case 99:
										res.setCmnetBillDetail(this.getCmnetCdrInfo(contentList));
										break;
									//自有梦网清单
									case 103:
										res.setMontnetBillDetail(this.getMontnetDSCdrInfo(contentList));
										break;
									//查询语音杂志
									case 68:
										res.setGsmMagaBillDetail(this.getGsmCdrMagazineInfo(contentList));
										break;
									//游戏消费详单
									case 36:
										res.setCdrsBillDetail(this.getGsmCDRQueryInfo(contentList));
										break;
										//游戏点数详单
									case 82:
										res.setCdrsPointBillDetail(this.getGsmCDRQueryPointInfo(contentList));
										break;
								}
							}
							res.setContentList(listMap);
					}
					catch (Exception ex)
					{
					}
				}
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
	public List<GsmBillDetail> getGsmCdrInfo (List<Map<String,String>>listBill)
	{
		List<GsmBillDetail> reList = new ArrayList<GsmBillDetail>();
		List<Map<String,String>> respList=listBill;
		try
		{
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									//状态类型~对方号码~通话时间~通话时长~通话地点~通信类型~应收基本通话费(元)~长途话费(元)~基本话费(元)~套餐费(元)~套餐
									GsmBillDetail bd = new GsmBillDetail();
									bd.setStatusType(m.get("状态类型"));
									bd.setOtherParty(m.get("对方号码"));
									bd.setStartTime(m.get("通话时间"));
									bd.setCallDuration(m.get("通话时长"));
									bd.setFirstCfee(Double.parseDouble((m.get("应收基本通话费(元)") == null || m.get("应收基本通话费(元)").equals("")) ? "0" : m.get("应收基本通话费(元)")));
									bd.setRealLfeeAndFirstOfee(Double.parseDouble((m.get("应收长途话费") == null || m.get("应收长途话费").equals("")) ? "0" : m.get("应收长途话费")) + Double.parseDouble((m.get("应收其他话费") == null || m.get("应收其他话费").equals("")) ? "0" : m.get("应收其他话费")));
									bd.setRealCfee(Double.parseDouble((m.get("基本话费(元)") == null || m.get("基本话费(元)").equals("")) ? "0" : m.get("基本话费(元)")));
									bd.setRealLfee(Double.parseDouble((m.get("长途话费(元)") == null || m.get("长途话费(元)").equals("")) ? "0" : m.get("长途话费(元)")));
									bd.setTotalFee(Double.parseDouble((m.get("小计(元)") == null || m.get("小计(元)").equals("")) ? "0" : m.get("小计")));
									bd.setFeeItem01(Double.parseDouble((m.get("套餐费(元)") == null || m.get("套餐费(元)").equals("")) ? "0" : m.get("套餐费(元)")));
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
		return reList;
	}
	
	
	/**
	 * 查询IP直通车清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<IpcarBillDetail> getIpcarCdrInfo (List<Map<String,String>>listBill)
	{
		List<IpcarBillDetail> reList = new ArrayList<IpcarBillDetail>();
		List<Map<String,String>> respList=listBill;	
		try
		{
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									//通信类型~对方号码~通话地点~起始时间~通话时长~基本话费(元)~长途话费(元)~套餐费(元)~小计(元)
									IpcarBillDetail bd = new IpcarBillDetail();
									bd.setVisitArear(m.get("通话地点"));
									bd.setFreeFee(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)"))?"0":m.get("套餐费(元)")));
									bd.setStatusType(m.get("通信类型"));
									bd.setOtherParty(m.get("对方号码"));
									bd.setStartTime(m.get("起始时间"));
									bd.setCallDuration(m.get("通话时长"));
									bd.setRealCfee(Double.parseDouble(m.get("基本话费(元)") == null || "".equals(m.get("基本话费(元)"))?"0":m.get("基本话费(元)")));
									bd.setRealLfee(Double.parseDouble(m.get("长途话费(元)") == null || "".equals(m.get("长途话费(元)"))?"0":m.get("长途话费(元)")));
									bd.setTotalFee(Double.parseDouble(m.get("小计(元)") == null || "".equals(m.get("小计(元)"))?"0":m.get("小计(元)")));
									reList.add(bd);
								}
							}
					}
					catch (Exception ex)
					{
						reList.clear();
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
	public List<IspBillDetail> getIspCdrInfo (List<Map<String,String>>listBill)
	{
		List<IspBillDetail> reList = new ArrayList<IspBillDetail>();
		List<Map<String,String>> respList=listBill;	
		
		try
		{
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
		return reList;
	}
	/**
	 * 查询梦网清单 代收
	 */
	public List<MontnetBillDetail> getMontnetDSCdrInfo (List<Map<String,String>>listBill)
	{
		List<MontnetBillDetail> reList = new ArrayList<MontnetBillDetail>();
		List<Map<String,String>> respList=listBill;	
							
		try
		{
							if(respList != null)
							{for(Map<String,String> m : respList)
							{
								//状态类型~sp服务(服务商名称)~业务端口~业务名称~时间~使用方式~通信费(元)~信息费(元)~费用(元)~漫游类型~计费类型
								MontnetBillDetail bd = new MontnetBillDetail();
								bd.setStatusType(m.get("状态类型"));
								bd.setCdrSpCode(m.get("sp服务(服务商名称)"));
								bd.setADetailName(m.get("业务名称"));
								//话单生成日期
								bd.setStartTime(m.get("时间"));
								//时长/流量
								bd.setOrgSmFee(Double.parseDouble(m.get("通信费(元)") == null || "".equals(m.get("通信费(元)"))?"0":m.get("通信费(元)")));
								bd.setInfoFee(Double.parseDouble(m.get("信息费(元)") == null || "".equals(m.get("信息费(元)"))?"0":m.get("信息费(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("费用(元)") == null || "".equals(m.get("费用(元)"))?"0":m.get("费用(元)")));
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
		return reList;
	}
	/**
	 * 查询梦网清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MontnetBillDetail> getMontnetCdrInfo (List<Map<String,String>>listBill)
	{
		List<MontnetBillDetail> reList = new ArrayList<MontnetBillDetail>();
		List<Map<String,String>> respList=listBill;	
		
		try
		{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//状态类型~业务提供商~业务代码~业务名称~时间~使用方式~通信费(元)~信息费(元)~费用(元)~漫游类型~计费类型
									MontnetBillDetail bd = new MontnetBillDetail();
									bd.setStatusType(m.get("状态类型"));
									bd.setCdrSpCode("移动");
									bd.setADetailName(m.get("业务名称"));
									//话单生成日期
									bd.setStartTime(m.get("时间"));
									//时长/流量
									bd.setOrgSmFee(Double.parseDouble(m.get("通信费(元)") == null || "".equals(m.get("通信费(元)"))?"0":m.get("通信费(元)")));
									bd.setInfoFee(Double.parseDouble(m.get("信息费(元)") == null || "".equals(m.get("信息费(元)"))?"0":m.get("信息费(元)")));
									bd.setTotalFee(Double.parseDouble(m.get("费用(元)") == null || "".equals(m.get("费用(元)"))?"0":m.get("费用(元)")));
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
		return reList;
	}
	
	/**
	 * 查询彩信清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MmsBillDetail> getMmsCdrInfo (List<Map<String,String>>listBill)
	{
		List<MmsBillDetail> reList = new ArrayList<MmsBillDetail>();
		List<Map<String,String>> respList=listBill;	
		
		try
		{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//通信方式~通信地点~sp服务~对方号码~彩信长度~起始时间~通信费(元)~信息费(元)~信息费(元)~套餐费(元)
								MmsBillDetail bd = new MmsBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setSpCode(m.get("sp服务"));
								bd.setMmsLen(Long.parseLong(m.get("彩信长度")));
								bd.setSendTime(m.get("起始时间"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setOrgSmFee(Double.parseDouble(m.get("基本费") == null || "".equals(m.get("基本费"))?"0":m.get("基本费")));
								bd.setInfoFee(Double.parseDouble(m.get("信息费(元)") == null || "".equals(m.get("信息费(元)"))?"0":m.get("信息费(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("通信费(元)") == null || "".equals(m.get("通信费(元)"))?"0":m.get("通信费(元)")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)"))?"0":m.get("套餐费(元)")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
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
	public List<SmsBillDetail> getSmsCdrInfo (List<Map<String,String>>listBill)
	{
		List<SmsBillDetail> reList = new ArrayList<SmsBillDetail>();
		List<Map<String,String>> respList=listBill;	
		
		try
		{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//起始时间~通信地点~对方号码~信息长度~通信方式~套餐费(元)~实收费(元)
								SmsBillDetail bd = new SmsBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("起始时间"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setTotalFee(Double.parseDouble(m.get("实收费(元)") == null || "".equals(m.get("实收费(元)"))?"0":m.get("实收费(元)")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)"))?"0":m.get("套餐费(元)")));
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
		return reList;
	}
	
	/**
	 * 查询国际短信清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<SmsBillDetail> getIneSmsCdrInfo (List<Map<String,String>>listBill)
	{
		List<SmsBillDetail> reList = new ArrayList<SmsBillDetail>();
		List<Map<String,String>> respList=listBill;	
		
		try
		{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//通信方式~对方号码~起始时间~通信地点~通信费(元)~套餐费(元)
								SmsBillDetail bd = new SmsBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("起始时间"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setTotalFee(Double.parseDouble(m.get("通信费(元)") == null || "".equals(m.get("通信费(元)"))?"0":m.get("通信费(元)")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)"))?"0":m.get("套餐费(元)")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
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
	public List<GprsBillDetail> getGprsCdrInfo (List<Map<String,String>>listBill)
	{
		List<GprsBillDetail> reList = new ArrayList<GprsBillDetail>();
		List<Map<String,String>> respList=listBill;	
		
		try
		{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								GprsBillDetail bd = new GprsBillDetail();
								//初始化业务Bean
								//起始时间~上网方式~通话地点~总时长~基本收费流量~基本费用(元)~套餐费(元)~套餐~其他费用(元)~其他收费流量~总费用(元)</
								bd.setStatusType(m.get("状态类型"));
								bd.setStartTime(m.get("起始时间"));
								bd.setCdrApnni(m.get("上网方式"));
								bd.setVisitArear(m.get("通话地点")); 
								bd.setBusyData(Double.parseDouble(m.get("基本收费流量")));    // 20150123    直接为数据不用转换，且为KB单位	
								bd.setDuration(setTimes(String.valueOf(m.get("总时长"))));    // Stirng类型转换成double    00:00:11
								bd.setIdlesseData(Double.parseDouble(m.get("其他收费流量"))); // 20150123    直接为数据不用转换，且为KB单位			
								bd.setBusyFee(Double.parseDouble(m.get("基本费用(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("总费用(元)")));
								bd.setOtherFee(Double.parseDouble(m.get("其他费用(元)")));
								bd.setPackageFee(Double.parseDouble(m.get("套餐费(元)")));
								String pckDscTmp = m.get("套餐");
								if(" ".trim().equals(pckDscTmp)) {
									bd.setMsnc("无");
								} else {
									bd.setMsnc(pckDscTmp);
								}
								bd.setGroupFee(Double.parseDouble(m.get("统付流量"))); // 20150123    直接为数据不用转换，且为KB单位
								// 明细
								bd.setDetail((m.get("明细") == null || m.get("明细").equals("")) ? "" : m.get("明细")); 
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						reList.clear();
					}
		return reList;
	}
	 //清单查询流量时用，返回double      2014-12-23 新大陆修改返回为..MB..KB
    private double setGPRS(String gprs) {
    	// gprs="12GB12KB";"12MB12KB";"12GB12MB12KB";"12KB";
    	int gprsIndexG=gprs.indexOf("GB");
    	int gprsIndexMb=gprs.indexOf("MB");
    	int gprsIndexKb=gprs.indexOf("KB");
    	double gprsG=0.0;
    	double gprsMb=0.0;
    	double gprsKb=0.0;
    	if(gprsIndexG>0){
    		gprsG=Double.parseDouble(String.valueOf(gprs.substring(0,gprsIndexG)));
    	}
    	
    	if(gprsIndexG<0 && gprsIndexMb>0){
    		gprsMb =Double.parseDouble(String.valueOf(gprs.substring(0,gprsIndexMb)));
    	}else if(gprsIndexMb>0){
    		gprsMb =Double.parseDouble(String.valueOf(gprs.substring(gprsIndexG+2,gprsIndexMb)));
    	}
    	
    	if(gprsIndexG >0 && gprsIndexMb <0 &&  gprsIndexKb>0){
    		gprsKb=Double.parseDouble(String.valueOf(gprs.substring(gprsIndexG+2,gprsIndexKb)));
    	}else if(gprsIndexMb <0 && gprsIndexKb>0){
    		gprsMb =Double.parseDouble(String.valueOf(gprs.substring(0,gprsIndexKb)));
    	}else if(gprsIndexKb>0){
    		gprsKb=Double.parseDouble(String.valueOf(gprs.substring(gprsIndexMb+2,gprsIndexKb)));
    	}
    	return (gprsG*1024*1024)+(gprsMb*1024)+gprsKb;
  }
    //清单查询时间用，返回double
	private double setTimes(String strTime){
//		int oneIndex=strTime.indexOf(":");
//    	int twoIndex=strTime.lastIndexOf(":");
//    	double hour=0.0;
//    	double minute=0.0;
//    	double second=0.0;
//    	if(oneIndex>0){
//    		hour=Double.parseDouble(String.valueOf(strTime.substring(1,oneIndex)));
//    	}
//    	if(twoIndex>0){
//    		minute =Double.parseDouble(String.valueOf(strTime.substring(oneIndex+1,twoIndex)));
//    		second =Double.parseDouble(String.valueOf(strTime.substring(twoIndex+1)));
//    	}
//    	return (hour*60*60)+(minute*60)+second;
		
		String[] tmp = strTime.split(":");
		if(tmp.length==3){
			return Integer.parseInt(tmp[0])*60*60+Integer.parseInt(tmp[1])*60+Integer.parseInt(tmp[2]);
		}else if(tmp.length==2){
			return Integer.parseInt(tmp[1])*60+Integer.parseInt(tmp[2]);
		}else{
			return Integer.parseInt(tmp[0]);
		}
	}
	/**
	 * 查询短号码清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<VpmnBillDetail> getVpmnCdrInfo (List<Map<String,String>>listBill)
	{
		List<VpmnBillDetail> reList = new ArrayList<VpmnBillDetail>();
		List<Map<String,String>> respList=listBill;
		
		try
		{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//通信方式~对方号码~集团短号~起始时间~通话时长~通信地点~基本话费(元)~长途话费(元)~应收其他话费(元)~小计(元)~套餐费(元)~套餐信息
								VpmnBillDetail bd = new VpmnBillDetail();
								bd.setStatusType(m.get("通信方式"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setCdrStartDate(m.get("起始时间"));			
								bd.setCallDuration(m.get("通话时长"));
								bd.setRealCfee(Double.parseDouble(m.get("基本话费(元)") == null || "".equals(m.get("基本话费(元)"))?"0":m.get("基本话费(元)")));
								bd.setRealLfee(Double.parseDouble(m.get("长途话费(元)") == null || "".equals(m.get("长途话费(元)"))?"0":m.get("长途话费(元)")));
								bd.setOtherFee(Double.parseDouble(m.get("应收其他话费(元)") == null || "".equals(m.get("应收其他话费(元)"))?"0":m.get("应收其他话费(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("小计(元)") == null || "".equals(m.get("小计(元)"))?"0":m.get("小计(元)")));
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)"))?"0":m.get("套餐费(元)")));
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
		return reList;
	}
	
	/**
	 * 查询WLAN清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<WlanBillDetail> getWlanCdrInfo (List<Map<String,String>>listBill)
	{
		List<WlanBillDetail> reList = new ArrayList<WlanBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//通信地点~起始时间~总时长~状态类型~上行数据流量~下行数据流量~上网方式~基本费(元)~信息费(元)~小计(元)~计费总流量
								WlanBillDetail bd = new WlanBillDetail();
								bd.setStatusType(m.get("状态类型"));
								bd.setStartTime(m.get("起始时间"));
								bd.setCallDuration(m.get("总时长"));
								bd.setDataUp(Long.parseLong(m.get("上行数据流量")));
								bd.setDataDown(Long.parseLong(m.get("下行数据流量")));
								bd.setIspCode(m.get("上网方式"));
								bd.setVisitArear(m.get("通信地点"));
								bd.setOrgSmFee(Double.parseDouble(m.get("基本费(元)") == null || "".equals(m.get("基本费(元)"))?"0":m.get("基本费(元)")));
								bd.setInfoFee(Double.parseDouble(m.get("信息费(元)") == null || "".equals(m.get("信息费(元)"))?"0":m.get("信息费(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("小计(元)") == null || "".equals(m.get("小计(元)"))?"0":m.get("小计(元)")));
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
		return reList;
	}
	
	
	/**
	 * 查询96121清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<Ac121BillDetail> getAc121CdrInfo (List<Map<String,String>>listBill)
	{
		List<Ac121BillDetail> reList = new ArrayList<Ac121BillDetail>();
		List<Map<String,String>> respList=listBill;
			
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//时间~使用方式~业务名称~业务端口~时长(时分秒)~费用(元)
								Ac121BillDetail bd = new Ac121BillDetail();
								bd.setBusiName(m.get("业务名称"));
								bd.setUseType(m.get("使用方式"));
								bd.setOtherParty(m.get("业务端口"));
								bd.setStartTime(m.get("时间"));
								bd.setCallDuration(m.get("时长(时分秒)"));
								bd.setNetFee(Double.parseDouble(m.get("费用(元)") == null || "".equals(m.get("费用(元)"))?"0":m.get("费用(元)")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
					}
		return reList;
	}
	
	/**
	 * 查询17202清单----不承载
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<Isp2BillDetail> getIsp2CdrInfo (List<Map<String,String>>listBill)
	{
		List<Isp2BillDetail> reList = new ArrayList<Isp2BillDetail>();
		List<Map<String,String>> respList=listBill;
		
					try
					{
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
		return reList;
	}
	
	
	/**
	 * 查询USSD清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<UssdBillDetail> getUssdCdrInfo (List<Map<String,String>>listBill)
	{
		List<UssdBillDetail> reList = new ArrayList<UssdBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
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
		return reList;
	}
	
	/**
	 * 查询无线音乐清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MpmusicBillDetail> getMpmusicCdrInfo (List<Map<String,String>>listBill)
	{
		List<MpmusicBillDetail> reList = new ArrayList<MpmusicBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
							if(respList != null)
							{
								for(Map<String,String> m : respList)
								{
									//业务类型~全网SP代码~业务名称~时间~费用(元)~赠送项目~赠送数~计费类型(名称）
									MpmusicBillDetail bd = new MpmusicBillDetail();
									bd.setSourceTypeName(m.get("业务类型"));
									bd.setSpCode(m.get("全网SP代码"));
									bd.setContentId(m.get("业务名称"));
									bd.setStartTime(m.get("时间"));
									bd.setRealInfoFee(Double.parseDouble(m.get("费用(元)") == null || "".equals(m.get("费用(元)"))?"0":m.get("费用(元)")));
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
		return reList;
	}
	
	
	/**
	 * 查询LBS清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<LbsBillDetail> getLbsCdrInfo (List<Map<String,String>>listBill)
	{
		List<LbsBillDetail> reList = new ArrayList<LbsBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//使用方式~业务名称~发起定位方号码~被定位方号码~时间~定位结果（X信息）~定位结果（Y信息）~费用(元)
								LbsBillDetail bd = new LbsBillDetail();
								bd.setTypeName(m.get("类型"));
								bd.setMsisdn(m.get("发起定位方号码"));
								bd.setOtherParty(m.get("被定位方号码"));
								bd.setRealInfoFee(Double.parseDouble("".equals((String)m.get("费用(元)"))?"0":(String)m.get("费用(元)")));
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
		return reList;
	}
	
	
	/**
	 * 即时群聊清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<MeetBillDetail> getMeetCdrInfo (List<Map<String,String>>listBill)
	{
		List<MeetBillDetail> reList = new ArrayList<MeetBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//对方号码~业务名称~时间~时长(时分秒)~实收基本费(元)~实收长途费(元)~实收其它费(元)~费用(元)
								MeetBillDetail bd = new MeetBillDetail();
								bd.setToStationId(m.get("对方号码"));
								bd.setBusiName(m.get("业务名称"));
								bd.setStartTime(m.get("时间"));
								bd.setCallDuration(m.get("时长(时分秒)"));
								bd.setNetFee(Double.parseDouble(m.get("实收长途费(元)") == null || "".equals(m.get("实收长途费(元)"))?"0":m.get("实收长途费(元)")));
								bd.setOtherFee(Double.parseDouble(m.get("实收其它费(元)") == null || "".equals(m.get("实收其它费(元)"))?"0":m.get("实收其它费(元)")));
								bd.setBaseFee(Double.parseDouble(m.get("实收基本费(元)") == null || "".equals(m.get("实收基本费(元)"))?"0":m.get("实收基本费(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("费用(元)") == null || "".equals(m.get("费用(元)"))?"0":m.get("费用(元)")));
								reList.add(bd);
							}
						}
					}
					catch (Exception ex)
					{
						reList.clear();
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
	public List<GsmVideoBillDetail> getGsmVideoCdrInfo (List<Map<String,String>>listBill)
	{
		List<GsmVideoBillDetail> reList = new ArrayList<GsmVideoBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//通信类型~对方号码~通信地点~开始时间~通信时长~基本费(元)~长途费(元)~小计(元)~套餐费(元)~套餐信息~网络
								GsmVideoBillDetail bd = new GsmVideoBillDetail();
								bd.setVisitArear(m.get("通信地点"));
								bd.setStatusType(m.get("通信类型"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("开始时间"));
								//处理时长，BOSS传过来的值的类型是long型。（格式：hh:mm:ss）
								bd.setCallDuration(m.get("通信时长"));
								bd.setRealCfee(Double.parseDouble(m.get("基本费(元)") == null || "".equals(m.get("基本费(元)")) ? "0" : m.get("基本费(元)")));//
								bd.setRealLfee(Double.parseDouble(m.get("长途费(元)") == null || "".equals(m.get("长途费(元)"))? "0" : m.get("长途费(元)")));
								bd.setTotalFee(Double.parseDouble(m.get("小计(元)") == null || "".equals(m.get("小计(元)"))?"0":m.get("小计(元)")));					
								bd.setFeeItem01(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)"))?"0":m.get("套餐费(元)")));
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
		return reList;
	}
	
	/**
	 * 随E行上网本清单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<CmnetBillDetail> getCmnetCdrInfo (List<Map<String,String>>listBill)
	{
		List<CmnetBillDetail> reList = new ArrayList<CmnetBillDetail>();
		List<Map<String,String>> respList=listBill;
					try
					{
								if(respList != null)
								{
									for(Map<String,String> m : respList)
									{
										//通话地点~开始时间~上网方式~总时长~基本收费流量~基本费用(元)~套餐费(元)~套餐编码~其他收费流量~其他费用(元)~通信费(元)										CmnetBillDetail bd = new CmnetBillDetail();
										CmnetBillDetail bd = new CmnetBillDetail();
										bd.setCdrApnni(m.get("上网方式"));
										bd.setVisitArear(m.get("通话地点"));
										bd.setOtherFee(Double.parseDouble(m.get("其他费用(元)") == null || "".equals(m.get("其他费用(元)")) ? "0" : m.get("其他费用(元)")));
										bd.setPkgFee(Double.parseDouble(m.get("套餐费(元)") == null || "".equals(m.get("套餐费(元)")) ? "0" : m.get("套餐费(元)")));
										bd.setStartTime(m.get("开始时间"));
										bd.setStatusType(m.get("状态类型"));
										bd.setFullTime(m.get("总时长"));
										bd.setAllTotalFee(Double.parseDouble(m.get("基本费用(元)") == null || "".equals(m.get("基本费用(元)")) ? "0" : m.get("基本费用(元)")));
										bd.setCallDuration(Double.parseDouble(m.get("基本收费流量") == null || "".equals(m.get("基本收费流量")) ? "0" : m.get("基本收费流量")));
										bd.setTotalFee(Double.parseDouble(m.get("通信费(元)") == null || "".equals(m.get("通信费(元)")) ? "0" : m.get("通信费(元)")));
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
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						reList.clear();
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
	public List<GsmMagaBillDetail> getGsmCdrMagazineInfo (List<Map<String,String>>listBill)
	{
		List<GsmMagaBillDetail> reList = new ArrayList<GsmMagaBillDetail>();
		List<Map<String,String>> respList=listBill;
			try{
						if(respList != null)
						{
							//状态类型~对方号码~通话时间~通话时长~通话地点~通信类型~基本话费(元)~长途话费(元)~小计(元)~套餐费(元)~套餐
							for(Map<String,String> m : respList)
							{
								GsmMagaBillDetail bd = new GsmMagaBillDetail();
								bd.setStatusType(m.get("状态类型"));
								bd.setOtherParty(m.get("对方号码"));
								bd.setStartTime(m.get("通话时间"));
								bd.setCallDuration(m.get("通话时长"));
								bd.setFirstCfee(Double.parseDouble((m.get("应收基本通话费") == null || m.get("应收基本通话费").equals("")) ? "0" : m.get("应收基本通话费")));
								bd.setRealLfeeAndFirstOfee(Double.parseDouble((m.get("应收长途话费") == null || m.get("应收长途话费").equals("")) ? "0" : m.get("应收长途话费")) + Double.parseDouble((m.get("应收其他话费") == null || m.get("应收其他话费").equals("")) ? "0" : m.get("应收其他话费")));
								bd.setRealCfee(Double.parseDouble((m.get("基本话费(元)") == null || m.get("基本话费(元)").equals("")) ? "0" : m.get("基本话费(元)")));
								bd.setRealLfee(Double.parseDouble((m.get("长途话费(元)") == null || m.get("长途话费(元)").equals("")) ? "0" : m.get("长途话费(元)")));
								bd.setTotalFee(Double.parseDouble((m.get("小计(元)") == null || m.get("小计(元)").equals("")) ? "0" : m.get("小计(元)")));
								bd.setFeeItem01(Double.parseDouble((m.get("套餐费(元)") == null || m.get("套餐费(元)").equals("")) ? "0" : m.get("套餐费(元)")));
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
		
		return reList;
	}
	/**
	 * 查询游戏点数详单
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List<CdrsBillDetail> getGsmCDRQueryInfo (List<Map<String,String>>listBill)
	{
		List<CdrsBillDetail> reList = new ArrayList<CdrsBillDetail>();
		List<Map<String,String>> respList=listBill;
		
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//业务代码~业务名称~企业代码~sp服务(服务商名称)~时间~计费类型~状态类型~漫游类型~使用方式~信息费(元)~费用(元)
								CdrsBillDetail cqbp = new CdrsBillDetail();
								cqbp.setBusinessName(m.get("业务名称"));
								cqbp.setServiceCode(m.get("企业代码"));
								cqbp.setBillType(m.get("计费类型"));
								cqbp.setBusinessPort(m.get("业务端口"));
								cqbp.setRoamType(m.get("漫游类型"));
								cqbp.setSpService(m.get("sp服务(服务商名称)"));
								cqbp.setStatus(m.get("状态类型"));
								cqbp.setTime(m.get("时间"));
								cqbp.setUseWay(m.get("使用方式"));
								cqbp.setBusinessCode(m.get("业务代码"));
								cqbp.setCommunicationPay(String.valueOf((m.get("通信费") == null || m.get("通信费").equals("")) ? "0" : m.get("通信费")));
								cqbp.setInfoPay(String.valueOf((m.get("信息费(元)") == null || m.get("信息费(元)").equals("")) ? "0" : m.get("信息费(元)")));
								cqbp.setMoney(String.valueOf((m.get("费用(元)") == null || m.get("费用(元)").equals("")) ? "0" : m.get("费用(元)")));
							
							
								reList.add(cqbp);
							}
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						reList.clear();
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
	public List<CdrsPointBillDetail> getGsmCDRQueryPointInfo (List<Map<String,String>>listBill)
	{
		List<CdrsPointBillDetail> reList = new ArrayList<CdrsPointBillDetail>();
		List<Map<String,String>> respList=listBill;
		
					try
					{
						if(respList != null)
						{
							for(Map<String,String> m : respList)
							{
								//业务名称~业务代码~消费时间~消费金额(元)~消费点数(100点=1元)
								CdrsPointBillDetail cqb = new CdrsPointBillDetail();
								cqb.setBusinessName(m.get("业务名称"));
								cqb.setServiceCode(m.get("业务代码"));
								cqb.setSpendingMoney(String.valueOf((m.get("消费金额(元)") == null || m.get("消费金额(元)").equals("")) ? "0" : m.get("消费金额(元)")));
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
		
		return reList;
	}
}