package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 无线音乐俱乐部
 * @author yuantao
 * 2009-12-04
 */
public class GetMusicInfoInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(GetMusicInfoInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	private Map<Integer,String> map;
	
	public GetMusicInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
		if (this.map == null)
		{
			this.map = new HashMap<Integer,String>();
			this.map.put(0, "WXYYJLB_PTHY");
			this.map.put(1, "WXYYJLB_GJHY");
			this.map.put(2, "WXYYJLB_TJHY");
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020001Result res = new QRY020001Result();
		List<GommonBusiness> reList = new ArrayList<GommonBusiness>();
		GommonBusiness dt = null;
		
		try
		{
			res.setResultCode("0");
			res.setErrorMessage("");
			//查询会员信息
			this.getMusicInfo(accessId, config, params, reList, res);
			//会员预约信息查询
			this.queryMusic(accessId, config, params, reList, res);
			if (null != reList && reList.size() > 0)
			{
				for (int i = 0; i < this.map.size(); i++)
				{
					String id = String.valueOf(this.map.get(i));
					boolean flag = true;
					
					for (GommonBusiness g : reList)
					{
						if (id.equals(g.getId()))
						{
							flag = false;
						}
					}
					if (flag)
					{
						dt = new GommonBusiness();
						dt.setState(1);
						dt.setId(id);
						reList.add(dt);
					}
				}
			}
			else
			{
				for (int i = 0; i < this.map.size(); i++)
				{
					String id = String.valueOf(this.map.get(i));
					dt = new GommonBusiness();
					dt.setState(1);
					dt.setId(id);
					reList.add(dt);
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		for (GommonBusiness busi : reList)
		{
			busi.setState(getState(busi));
		}
		
		res.setGommonBusiness(reList);
		return res;
	}
	
	/**
	 * 查询会员信息
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public void getMusicInfo (String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<GommonBusiness> reList, 
			QRY020001Result res)
	{
		String rspXml = "";
		GommonBusiness dt = null;
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cgetmusicinfo_391", params), 
					 accessId, "cc_cgetmusicinfo_391", this.generateCity(params)));
			logger.debug(" ====== 查询会员信息返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					res.setErrorMessage(resp_desc);
					errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetmusicinfo_391", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code) || "-12850".equals(resp_code)))
				{
					res.setResultCode("0");
					List srlList = null;
					try
					{
						srlList = root.getChild("content").getChildren("musicclubmember_operating_srl");
					}
					catch (Exception ex)
					{
						srlList = null;
					}
					
					if (null != srlList && srlList.size() > 0)
					{
						for (int i = 0; i < srlList.size(); i++)
						{
							Element musicDt = ((Element)srlList.get(i)).getChild("cmusicclubmemberdt");
							if (null != musicDt)
							{
								dt = new GommonBusiness();
								dt.setBeginDate(p.matcher(musicDt.getChildText("musicclubmember_start_date")).replaceAll(""));
								dt.setEndDate(p.matcher(musicDt.getChildText("musicclubmember_end_date")).replaceAll(""));
								String lev = p.matcher(musicDt.getChildText("musicclubmember_member_level")).replaceAll("");
								if (null != lev && "1".equals(lev))
								{
									dt.setId("WXYYJLB_PTHY");
								}
								else
								{
									dt.setId("WXYYJLB_GJHY");
								}
								//dt.setState(2);
								reList.add(dt);
							}
						}
						
					}else{
						setParameter(params, "bizId", "WXYYJLB_TJHY");
						dt=getsmscall(accessId,config, params, res);
						if(null != dt)
						{
    						dt.setId("WXYYJLB_TJHY");
    						//dt.setState(2);
    						reList.add(dt);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	} 
	
	/**
	 * 特级会员查询--增值业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public GommonBusiness getsmscall (String accessId, ServiceConfig config, 
			List<RequestParameter> params,QRY020001Result res){
		String rspXml = "";
		GommonBusiness dt = null;
		ErrorMapping errDt = null;
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cgetsmscall_602", params), 
					 accessId, "cc_cgetsmscall_602", this.generateCity(params)));
			logger.debug(" ====== 查询会员信息返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					res.setErrorMessage(resp_desc);
					errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetsmscall_602", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				else
				{
					 dt = new GommonBusiness();
					 Element csmscalldt = root.getChild("content").getChild("csmscalldt");
					 dt.setBeginDate(p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll(""));
					 dt.setEndDate(p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll(""));
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return dt;
	}
	
	/**
	 * 会员预约信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param reList
	 */
	public void queryMusic (String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<GommonBusiness> reList, QRY020001Result res)
	{
		String rspXml = "";
		GommonBusiness dt = null;
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cquerymusic_392", params), 
					 accessId, "cc_cquerymusic_392", this.generateCity(params)));
			logger.debug(" ====== 会员预约信息查询返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				//String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cquerymusic_392", resp_code);
					if (null != errDt)
					{
						//add by tkk
						resp_code = errDt.getLiErrCode();
						//add end
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				
				if (null != resp_code && ("0000".equals(resp_code) || "-13088".equals(resp_code)))
				{
					res.setResultCode("0");
					List dtList = null;
					try
					{
						dtList = root.getChild("content").getChildren("cmusiccluboperationbkdt");
					}
					catch (Exception ex)
					{
						dtList = null;
					}
					
					if (null != dtList && dtList.size() > 0)
					{
						for (int i = 0; i < dtList.size(); i++)
						{
							dt = new GommonBusiness();
							String lev = ((Element)dtList.get(i)).getChildText("musiccluboperationbk_member_level");
							if (null != lev && "1".equals(lev.trim()))
							{
								dt.setId("WXYYJLB_PTHY");
							}
							else
							{
								dt.setId("WXYYJLB_GJHY");
							}
							dt.setState(2);
							dt.setBeginDate(((Element)dtList.get(i)).getChildText("musiccluboperationbk_start_date"));
							dt.setEndDate("");
							reList.add(dt);
						}
					}/*else{
						setParameter(params, "bizId", "WXYYJLB_TJHY");
						dt=getsmscall(accessId,config, params, res);
						dt.setId("WXYYJLB_TJHY");
						dt.setState(2);
						reList.add(dt);
					}*/
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	public int getState(GommonBusiness busi){
	    int state = 1;
		if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
		{
		   state = 4;
		}
		else if(null != busi.getBeginDate() && !"".equals(busi.getBeginDate()))
		{
		     if (busi.getBeginDate().equals(getFirstdayOfNextMonth()) || busi.getBeginDate().equals(getNextDayOfMonth()))
		     {
				state = 3;
			 }
		     else
		     {
				state = 2;
			}
		}
		else
		{
			state = 1 ;
		}
		
		return state; 
	}
	private String getNextDayOfMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}

	private String getFirstdayOfNextMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;

	}
}

