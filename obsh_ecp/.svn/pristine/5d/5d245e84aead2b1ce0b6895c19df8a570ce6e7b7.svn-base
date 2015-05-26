package com.xwtech.xwecp.service.logic.invocation;


import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.service.logic.pojo.ScbHisDetail;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY030014Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import org.jdom.Element;

/**
 * 商城币余额与历史明细查询 汪洪广 2012-04-15
 */
public class ScbCoinQueryInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(ScbCoinQueryInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private ParseXmlConfig config;

	public ScbCoinQueryInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY030014Result res = new QRY030014Result();
		
		String bossTemplate_01 = "cc_qryshangchscore";// 查询商城币余额
		String bossTemplate_02 = "cc_qryshangchscorelog";// 查询商城币历史查询

		String reqXml = "";
		String rspXml = "";

		try {
			String oprNum = (String) this.getParameters(params, "oprNum");

			if (oprNum.equals("1"))
			{
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_01,params);
				logger.info("请求报文:"+reqXml);
				
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate_01, this.generateCity(params)));
				logger.info("应答报文:"+rspXml);
				
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.config.getElement(rspXml);
					String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
					res.setResultCode("0000".equals(resp_code) ? "0" : "1");
					if (!"0000".equals(resp_code))
					{
						res.setErrorCode(resp_code);
						res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"),"resp_desc"));
					}
					else
					{
						res.setAmount(this.config.getChildText(this.config.getElement(root, "content"), "score_value"));
					}
				}
			} 
			else
			{
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_02,params);
				logger.info(reqXml);
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate_02, this.generateCity(params)));
				logger.info(rspXml);
				
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.config.getElement(rspXml);
					String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
					res.setResultCode(BOSS_SUCCESS.equals(resp_code)?LOGIC_SUCESS:LOGIC_ERROR);
					if (!"0000".equals(resp_code))
					{
						res.setErrorCode(resp_code);
						res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"),"resp_desc"));
					}
					else 
					{
						res.setScbHisDetailList(returnScbHisDetailList(root));
					}
				}
			}
		} catch (Exception e) {
			res.setResultCode("1");
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 整合数据
	 * @return
	 */
	private List<ScbHisDetail>  returnScbHisDetailList(Element root)
	{
		List<ScbHisDetail> scbHisList = new ArrayList<ScbHisDetail>();
		ScbHisDetail scbHis = null;
		List scbList = this.getContentList(root, "opr_srldt");
		if (null != scbList && scbList.size() > 0)
		{
			for (int i = 0; i < scbList.size(); i++)
			{
				scbHis = new ScbHisDetail();
				Element scbDT = ((Element) scbList.get(i));
				for(int j = 0;j < scbList.size();j++)
				{
					if(i == j && scbList.size() != 1)
					{
						continue;
					}
					
					Element scbBT = ((Element) scbList.get(j));
					if (null != scbDT && null != scbBT)
					{
						//先判断scbHis这个对象里面有没有值，
						if(null == scbHis.getOprSrl())
						{
							scbHis.setOprSrl(scbDT.getChildText("opr_srl"));
							
							scbHis.setOprDate(scbDT.getChildText("opr_date"));
							
							scbHis.setOperator(scbDT.getChildText("opr_operator"));
							scbHis.setOperatorName(scbDT.getChildText("opr_operator_name"));
							scbHis.setIsRollback(scbDT.getChildText("isRollback"));
							scbHis.setOprSource(scbDT.getChildText("opr_source"));
							scbHis.setChgResonRemark(scbDT.getChildText("chg_reason_remark"));
							scbHis.setIsRollback(scbDT.getChildText("isrollback"));
							scbHis.setChgResonType(scbDT.getChildText("chg_reason_type"));
							scbHis.setSubSid(scbDT.getChildText("subsid"));
							scbHis.setOprType(scbDT.getChildText("opr_type"));
							scbHis.setIncome(scbDT.getChildText("is_income"));
						}
						//当操作流水相同时，数据合并
						if(scbHis.getOprSrl().equals(scbBT.getChildText("opr_srl")) && scbList.size() != 1 
								&& scbDT.getChildText("opr_date").equals(scbBT.getChildText("opr_date")))
						{
							//移除已整合的数据，防止数据干扰
							scbList.remove(i);
							scbList.remove(j);
							i--;
							j--;
							int value = Integer.parseInt(scbDT.getChildText("score_value"))+ Integer.parseInt(scbBT.getChildText("score_value"));
							scbHis.setScoreValue(""+value);
						}
						else
						{
							scbHis.setScoreValue(scbDT.getChildText("score_value"));
						}
					}
				}
				scbHisList.add(scbHis);
			}
		}
		return scbHisList;
	}
	/*
	 * 获取content下父节点信息
	 * 
	 * @param root @param name @return
	 */
	public List getContentList(Element root, String name) {
		List list = null;
		try {
			list = root.getChild("content").getChildren(name);
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

}
