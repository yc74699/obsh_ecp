package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.NewScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;

/**
 * BOSS实时积分查询
 * @author Mr Ou
 * @data  20140926
 */
public class QueryScoreNewInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryScoreNewInvocation.class);

	/**
	 * 0  总消费积分      phonescore
	 * 1 总转项转移积分  bountyscore
	 * 3 总以兑换积分  exchangedscore
	 * 5 总网龄奖励积分  Agescore
	 * 6 总品牌奖励积分 brandscore
	 * 7 当月可用积分 Availscore
     **/
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY030011Result result = new QRY030011Result();
		result = qryBossSco(result,accessId, config,params);
		return result;
	}
	
	
	//查询积分用boss提供的接口
	public QRY030011Result qryBossSco(QRY030011Result result,String accessId, ServiceConfig config,
			List<RequestParameter> params)
	{
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<NewScoreDetail> scores = null;
		
		String phonescore = "";  //总消费积分 
		String bountyscore = ""; //总转项转移积分
		String exchangedscore = ""; //总以兑换积分
		String agescore = "";//总网龄奖励积分
		String brandscore = "";//总品牌奖励积分
		String availscore = "";//当月可用积分
		
		
		try {
				reqXml = this.bossTeletextUtil.mergeTeletext("GETAVAILINTEGRAL_818", params);
				
				logger.debug(" ====== 新大陆实时积分查询接口请求报文======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "GETAVAILINTEGRAL_818", this.generateCity(params)));
				logger.debug(" ====== 新大陆实时积分查询接口返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					//Boss一期割接到Boss二期处理 Boss一期为一个0 Boss二期为4个0
					if("0000".equals(errCode))
					{
						errCode = "0";
					}
					if (!"0".equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY030011", "GETAVAILINTEGRAL818", errCode);
						if (null != errDt)
						{
							result.setErrorCode(errDt.getLiErrCode());
							result.setErrorMessage(errDt.getLiErrMsg());
						}
					}
					result.setResultCode("0".equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					result.setErrorCode(errCode);
					result.setErrorMessage(errDesc);
					if (null != errCode && ("0".equals(errCode))) 
					{
						scores = new ArrayList<NewScoreDetail>();
						if(true)
						{
							//0  总消费积分
							NewScoreDetail score0 = new NewScoreDetail();
							score0.setScorenId("0");
							phonescore = root.getChild("content").getChildText("phonescore");
							score0.setScore(Long.parseLong(phonescore));
							scores.add(score0);
							//1 总转项转移积分
							NewScoreDetail score1 = new NewScoreDetail();
							score1.setScorenId("1");
							bountyscore = root.getChild("content").getChildText("bountyscore");
							score1.setScore(Long.parseLong(bountyscore));
							scores.add(score1);
							//3 总以兑换积分
							NewScoreDetail score3 = new NewScoreDetail();
							score3.setScorenId("3");
							exchangedscore = root.getChild("content").getChildText("exchangedscore");
							score3.setScore(Long.parseLong(exchangedscore));
							scores.add(score3);
							//5 总网龄奖励积分
							NewScoreDetail score5 = new NewScoreDetail();
							score5.setScorenId("5");
							agescore = root.getChild("content").getChildText("agescore");
							score5.setScore(Long.parseLong(agescore));
							scores.add(score5);
							//6 总品牌奖励积分 
							NewScoreDetail score6 = new NewScoreDetail();
							score6.setScorenId("6");
							brandscore = root.getChild("content").getChildText("brandscore");
							score6.setScore(Long.parseLong(brandscore));
							scores.add(score6);
							//7 当月可用积分
							NewScoreDetail score7 = new NewScoreDetail();
							score7.setScorenId("7");
							availscore = root.getChild("content").getChildText("availscore");
							score7.setScore(Long.parseLong(availscore));
							scores.add(score7);
							
						}
						result.setNewScoreDetail(scores);
					}
				}
		} 
		catch (Exception e) 
		{
			logger.error(e, e);
		}
		return result;
	}
}