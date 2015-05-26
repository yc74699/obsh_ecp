package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DecibleInfo;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.MusicClubMember;
import com.xwtech.xwecp.service.logic.pojo.QRY020008Result;
import com.xwtech.xwecp.service.logic.pojo.YearMusicInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 无线音乐分贝
 * @author yuantao
 * 2010-01-20
 */
public class QueryDecibleInfoInvocation extends BaseInvocation implements ILogicalService
{
private static final Logger logger = Logger.getLogger(QueryPkgUseStateInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	public QueryDecibleInfoInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020008Result res = new QRY020008Result();
		
		try
		{
			//会员信息查询
			this.getMusicInfo(accessId, config, params, res);
			//存在会员信息
			if (null != res.getMusicClubMember() && res.getMusicClubMember().size() > 0)
			{
				//音乐分贝详细信息
				this.getDecibelInfo(accessId, config, params, res, res.getMusicClubMember());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	public void getDecibelInfo (String accessId, ServiceConfig config, 
            List<RequestParameter> params, QRY020008Result res, 
            List<MusicClubMember> musicList)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //返回报文
		String resp_code = "";  //结果码
		YearMusicInfo yearDt = null;  //年度使用情况
		List<YearMusicInfo> list = new ArrayList();  //年度使用情况列表
		DecibleInfo decibleDt = null;  //音乐贝使用情况
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqrymusicvalue_417", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cqrymusicvalue_417", this.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}
			//解析报文
			if (null != rspXml && !"".equals(rspXml))
			{
				//解析报文 根节点
				Element root = this.config.getElement(rspXml);
				//获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				//设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cqrymusicvalue_417");
				//成功
				if ("0000".equals(resp_code))
				{
					//当前年份
					String todayYear = this.config.getTodayYear();
					Element content = this.config.getElement(root, "content");
					List<Element> valueList = this.config.getContentList(root, "preyearmusicvalue_user_id");
					if (null != valueList && valueList.size() > 0)
					{
						for (Element e : valueList)
						{
							yearDt = new YearMusicInfo ();
							Element valueDt = this.config.getElement(e, "cpreyearmusicvaluedt");
							//年份编码
							String yearNum = this.config.getChildText(valueDt, "preyearmusicvalue_year_num");
							
							yearDt.setYear(yearNum);
							//设置年份 ----crm接口返回数据改了。直接返回 2011,2012格式的年份，不是0,1这样的形式了
							
//							if ("0".equals(yearNum))
//							{
//								yearDt.setYear(todayYear);
//							}
//							else if ("1".equals(yearNum))
//							{
//								yearDt.setYear(String.valueOf(Integer.valueOf(todayYear) - 1));
//							}
//							else
//							{
//								yearDt.setYear(String.valueOf(Integer.valueOf(todayYear) - 2));
//							}
							//消费音乐贝
							yearDt.setConsumption(this.config.getChildText(valueDt, "preyearmusicvalue_charge_value"));
							//奖励音乐贝
							yearDt.setHortation( String.valueOf(
									Integer.parseInt(this.config.getChildText(valueDt, "preyearmusicvalue_service_value")) 
								  + Integer.parseInt(this.config.getChildText(valueDt, "preyearmusicvalue_bounty_value"))));
							//已兑换音乐贝
							yearDt.setUse(this.config.getChildText(valueDt, "preyearmusicvalue_exchanged_value"));
							
							list.add(yearDt);
							
							if (!"0".equals(yearNum))
							{
								decibleDt = new DecibleInfo();
								Double sa = Double.parseDouble(yearDt.getUse());
								Double sd = Double.parseDouble(this.config.getChildText(valueDt, "preyearmusicvalue_gift_value"));
								if (sa >= sd)
								{
									//已兑换转赠
									decibleDt.setUsePresent(String.valueOf(sd));
									//本年已兑换
									decibleDt.setYearUse(String.valueOf(sa - sd));
								}
								else
								{
									//已兑换转赠
									decibleDt.setUsePresent(String.valueOf(sa));
									//本年已兑换
									decibleDt.setYearUse("0");
								}
								//转赠
								decibleDt.setPresent(String.valueOf(sd));
								//剩余可兑换
								decibleDt.setRemain(this.config.getChildText(content, "score_score_value"));
								
								((MusicClubMember)musicList.get(0)).setDecibleInfo(decibleDt);
							}
							
						}
					}
				}
			}
			((MusicClubMember)musicList.get(0)).setYearMusicInfo(list);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 会员信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public void getMusicInfo (String accessId, ServiceConfig config, 
			                  List<RequestParameter> params, QRY020008Result res)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //返回报文
		String resp_code = "";  //结果码
		MusicClubMember dt = null;  //套餐列表
		List<MusicClubMember> list = new ArrayList();  //套餐返回列表
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmusicinfo_391", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetmusicinfo_391", this.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}
			//解析报文
			if (null != rspXml && !"".equals(rspXml))
			{
				//解析报文 根节点
				Element root = this.config.getElement(rspXml);
				//获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				//设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cgetmusicinfo_391");
				//成功
				if ("0000".equals(resp_code))
				{
					List<Element> srlList = this.config.getContentList(root, "musicclubmember_operating_srl");
					if (null != srlList && srlList.size() > 0)
					{
						for (Element e : srlList)
						{
							Element musicDt = this.config.getElement(e, "cmusicclubmemberdt");
							dt = new MusicClubMember ();
							//业务编码
							dt.setPkgId(this.config.getChildText(musicDt, "musicclubmember_package_id"));
							//业务类别
							dt.setMemberType(this.config.getChildText(musicDt, "musicclubmember_member_level"));
							//开始日期
							dt.setBeginDate(this.config.getChildText(musicDt, "musicclubmember_start_date"));
							//结束日期
							dt.setEndDate(this.config.getChildText(musicDt, "musicclubmember_end_date"));
							//状态
							dt.setStatus(this.config.getChildText(musicDt, "musicclubmember_status"));
							
							list.add(dt);
						}
					}
				}
			}
			res.setMusicClubMember(list);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 设置结果信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo (String accessId, ServiceConfig config, 
            List<RequestParameter> params, QRY020008Result res, String resp_code, String xmlName)
	{
		ErrorMapping errDt = null;  //错误编码解析
		
		try
		{
			String errCode = resp_code;
			
			//失败
			if (!"0000".equals(resp_code))
			{
				//解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY020008", xmlName, resp_code);
				if (null != errDt)
				{
					errCode = errDt.getLiErrCode();
					res.setErrorCode(errDt.getLiErrCode());  //设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg());  //设置错误信息
				}
			}
			//设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(errCode)?"0":"1");
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
