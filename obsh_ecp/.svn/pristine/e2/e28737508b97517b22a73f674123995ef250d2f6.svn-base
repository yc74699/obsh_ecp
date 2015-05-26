package com.xwtech.xwecp.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.pojo.ScoreBean;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 字典查询接口类
 * @author user
 *
 */
public class DictQueryUtil {

	
    private static final Logger logger = Logger.getLogger(DictQueryUtil.class);
    
    private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	/**
	 * 查询办理渠道/过滤信息/操作信息/动感部落信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public  static List<ChannelInfo> queryOprChannel (String accessId,List<RequestParameter> params,BaseServiceInvocationResult res,
			BossTeletextUtil bossTeletextUtil,IRemote remote,WellFormedDAO wellFormedDAO,ParseXmlConfig config)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<ChannelInfo> channelInfoList = null;
		ChannelInfo channelInfo = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", params);
			logger.info(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_get_dictlist_78", generateCity(params)));
				logger.info(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = config.getElement(rspXml);
				resp_code = config.getChildText(config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = wellFormedDAO.transBossErrCode("QRY040004", "cc_cquerydetail_309", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List countList = config.getContentList(root, "list_size");
				if (null != countList && countList.size() > 0)
				{
					channelInfoList = new ArrayList<ChannelInfo>(countList.size());
					for (int i = 0; i < countList.size(); i++)
					{
						channelInfo = new ChannelInfo();
						channelInfo.setChannelId(config.getChildText((Element)countList.get(i), "dict_code"));
						channelInfo.setChannelName(config.getChildText((Element)countList.get(i), "dict_code_desc"));
						channelInfoList.add(channelInfo);
				    }
		         }
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return channelInfoList;
	} 
	
	
	/**
	 * 查询积分兑换类型/兑换金额字典
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public  static Map<String , Map<String,String>> queryScoreExchangeTypeAndFee(String accessId,List<RequestParameter> params,BaseServiceInvocationResult res,
			BossTeletextUtil bossTeletextUtil,IRemote remote,WellFormedDAO wellFormedDAO,ParseXmlConfig config)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<ScoreBean> scoreBeanList = null;
		ScoreBean scoreBean = null;
		Map<String, Map<String,String>>  scoreExchangeDetailMap = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = bossTeletextUtil.mergeTeletext("cc_cget_scorerule_529", params);
			logger.info(" ====== 查询积分兑换类型/兑换金额字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cget_scorerule_529", generateCity(params)));
				logger.info(" ====== 查询积分兑换类型/兑换金额字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = config.getElement(rspXml);
				resp_code = config.getChildText(config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = wellFormedDAO.transBossErrCode("QRY030001", "cc_cget_scorerule_529", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List scoreList = config.getContentList(root, "score_form_redeem_type");
				if (null != scoreList && scoreList.size() > 0)
				{
					scoreBeanList = new ArrayList<ScoreBean>(scoreList.size());
					for (int i = 0; i < scoreList.size(); i++)
					{
						Element scoreInfo = config.getElement((Element)scoreList.get(i), "cscoreformdt");
						scoreBean = new ScoreBean();
						scoreBean.setRedeemType(config.getChildText(scoreInfo, "score_form_redeem_type"));
						scoreBean.setServerName(config.getChildText(scoreInfo, "score_form_server_name"));
						scoreBean.setValue(config.getChildText(scoreInfo, "score_form_value"));
						scoreBeanList.add(scoreBean);
				    }
					scoreExchangeDetailMap = getScoreExchangeMapFromList(scoreBeanList);
		         }
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return scoreExchangeDetailMap;
	} 
	
	
	/**
	 * 查询积分兑换类型/兑换金额字典(新)
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public  static Map<String , Map<String,String>> queryScoreExchangeTypeAndFeeNew(String accessId,List<RequestParameter> params,BaseServiceInvocationResult res,
			BossTeletextUtil bossTeletextUtil,IRemote remote,WellFormedDAO wellFormedDAO,ParseXmlConfig config)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<ScoreBean> scoreBeanList = null;
		Map<String, Map<String,String>>  scoreExchangeDetailMap = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = bossTeletextUtil.mergeTeletext("cc_cget_scorerule_529", params);
			logger.info(" ====== 查询积分兑换类型/兑换金额字典(新) 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cget_scorerule_529", generateCity(params)));
				logger.info(" ====== 查询积分兑换类型/兑换金额字典(新) 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = getElement(rspXml.getBytes());
				resp_code = root.getChild("response").getChildText("resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = wellFormedDAO.transBossErrCode("QRY030001", "cc_cget_scorerule_529", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				Element content = root.getChild("content");
				String tableList = p.matcher(content.getChildText("XTABLE_SCO_REDEEM")).replaceAll("");
				String [] rows = tableList.split(";");
				scoreBeanList = new ArrayList<ScoreBean>(rows.length - 1);
				ScoreBean scoreBeanInfo = null;
				//积分兑换方式~积分兑换积分数~积分兑换名称~积分兑换描述~积分兑换开始时间~积分兑换结束时间~积分兑换地市~积分兑换金额~积分兑换类型~积分兑换大类~剩余可兑换数量~当日可兑换数量配置~当日可兑换数量~兑换物品描述~;
				for (int i = 1; i < rows.length; i ++) 
				{
					String [] cells = rows[i].split("~");
					scoreBeanInfo = new ScoreBean();
					scoreBeanInfo.setRedeemType(cells[0]);
					scoreBeanInfo.setServerName(cells[2]);
					scoreBeanInfo.setValue(cells[7]);
					scoreBeanList.add(scoreBeanInfo);
				}
				scoreExchangeDetailMap = getScoreExchangeMapFromList(scoreBeanList);
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return scoreExchangeDetailMap;
	} 
	
	
	/**
	 * @desc 将字典查询出的list转换成以字典Code为key的map
	 * @return
	 */
	public static Map<String, String> getDictMapFromList(List<ChannelInfo> list){
		Map<String, String> map = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			ChannelInfo channelInfo = null;
			for (int i = 0; i < size; i++) {
				channelInfo = list.get(i);
				map.put(channelInfo.getChannelId(), channelInfo.getChannelName());
			}
		}
		return map;
	}
	
	
	/**
	 * @desc 将查询积分兑换类型/兑换金额字典查询出的list转换成以字典Code为key的map
	 * @return
	 */
	public static Map<String, Map<String,String>> getScoreExchangeMapFromList(List<ScoreBean> list){
		Map<String, String> serverNameMap = null;
		Map<String, String> feeMap = null;
		Map<String,Map<String,String>> resultMap = null;
		if (list != null && list.size() > 0) {
			serverNameMap = new HashMap<String, String>(list.size());
			feeMap = new HashMap<String, String>(list.size());
			resultMap = new HashMap<String, Map<String,String>>(2);
			int size = list.size();
			ScoreBean scoreBean = null;
			for (int i = 0; i < size; i++) {
				scoreBean = list.get(i);
				serverNameMap.put(scoreBean.getRedeemType(), scoreBean.getServerName());
				feeMap.put(scoreBean.getRedeemType(), scoreBean.getValue());
			}
			resultMap.put("SERVERNAME", serverNameMap);
			resultMap.put("FEENAME", feeMap);
		}
		return resultMap;
	}
	
	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	public  static Element getElement(byte[] tmp)
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
	
	/**
	 * 组织地市信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	protected static RemoteCallContext generateCity (List<RequestParameter> params) throws Exception {
		
		RequestParameter userCityParam = getParameter(params, ServiceExecutor.ServiceConstants.USER_CITY);
		String userCity = userCityParam != null ? userCityParam.getParameterValue().toString() : "";
		RemoteCallContext remoteCallContext = new RemoteCallContext();
		remoteCallContext.setUserCity(userCity);
		
		return remoteCallContext;
	}
	
	/**
	 * 获取参数值
	 * @param params
	 * @param name
	 * @return
	 */
	private static RequestParameter getParameter(List<RequestParameter> params, String name)
	{
		if (params != null && params.size() > 0) {
			for(int i = 0; i<params.size(); i++)
			{
				RequestParameter param = params.get(i);
				if(param.getParameterName().equals(name))
				{
					return param;
				}
			}
		}
		
		return null;
	}
	
}
