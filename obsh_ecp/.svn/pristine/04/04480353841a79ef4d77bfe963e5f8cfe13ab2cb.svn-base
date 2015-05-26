package com.xwtech.xwecp.service.logic.resolver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.OtherScores;
import com.xwtech.xwecp.service.logic.pojo.QRY030002Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreDT;
import com.xwtech.xwecp.service.logic.pojo.ScoreDetail;

/**
 * 查询用户积分
 * @author yuantao
 *
 */
public class GetScoreInfoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetScoreInfoResolver.class);
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
			            List<RequestParameter> param) throws Exception
	{
		try
		{
			QRY030002Result ret = (QRY030002Result)result;
			ScoreDT scoreDt = ret.getScoreDT();
			List<ScoreDetail> scoreDetail = ret.getScoreDetail();
			List<ScoreDetail> returnScore = new ArrayList();
			List<OtherScores> returnOther = new ArrayList();
			
			if (ret.getRemainScore() != null && !"".equals(ret.getRemainScore()))
			{
				if (ret.getRemainScore().indexOf(".") != -1)
					ret.setRemainScore(ret.getRemainScore().substring(0, ret.getRemainScore().indexOf(".")));
				else
					ret.setRemainScore(ret.getRemainScore());
			}
			else
			{
				ret.setRemainScore("0");
			}
			
			if (null != scoreDt)
			{
				OtherScores other1 = new OtherScores(); 
				other1.setScoreName("转赠积分");
				
				if (null != scoreDt.getGiftScore() && !"".equals(scoreDt.getGiftScore()))
				{
					if (scoreDt.getGiftScore().indexOf(".") != -1)
						scoreDt.setGiftScore(scoreDt.getGiftScore().substring(0, scoreDt.getGiftScore().indexOf(".")));
					else
						scoreDt.setGiftScore(scoreDt.getGiftScore());
					other1.setScore(scoreDt.getGiftScore());
				}
				else
				{
					other1.setScore("0");
				}
				
				OtherScores other2 = new OtherScores(); 
				other2.setScoreName("已兑换转赠积分");
				
				OtherScores other3 = new OtherScores(); 
				other3.setScoreName("本年已兑换积分");
				
				if (null != scoreDt.getExchangedScore() && !"".equals(scoreDt.getExchangedScore()))
				{
					if (scoreDt.getExchangedScore().indexOf(".") != -1)
						scoreDt.setExchangedScore(scoreDt.getExchangedScore().substring(0, scoreDt.getExchangedScore().indexOf(".")));
					else
						scoreDt.setExchangedScore(scoreDt.getExchangedScore());
				}
				else
				{
					scoreDt.setExchangedScore("0");
				}
				if (null != scoreDt.getGiftScore() && !"".equals(scoreDt.getGiftScore()))
				{
					if (scoreDt.getGiftScore().indexOf(".") != -1)
						scoreDt.setGiftScore(scoreDt.getGiftScore().substring(0, scoreDt.getGiftScore().indexOf(".")));
					else
						scoreDt.setGiftScore(scoreDt.getGiftScore());
				}
				else
				{
					scoreDt.setGiftScore("0");
				}
				
				long sa = Long.parseLong(scoreDt.getExchangedScore());
				long sd = Long.parseLong(scoreDt.getGiftScore());
				
				if (sa >= sd) 
				{
					other2.setScore(String.valueOf(sd));
					other3.setScore(String.valueOf(sa - sd));
				}
				else
				{
					other2.setScore(String.valueOf(sa));
					other3.setScore("0");
				}
				
				OtherScores other4 = new OtherScores(); 
				other4.setScoreName("剩余可兑换积分");
				
				if (null != scoreDt.getLeavingsScore() && !"".equals(scoreDt.getLeavingsScore()))
				{
					if (scoreDt.getLeavingsScore().indexOf(".") != -1)
						scoreDt.setLeavingsScore(scoreDt.getLeavingsScore().substring(0, scoreDt.getLeavingsScore().indexOf(".")));
					else
						scoreDt.setLeavingsScore(scoreDt.getLeavingsScore());
				}
				else
				{
					scoreDt.setLeavingsScore("0");
				}
				
				other4.setScore(scoreDt.getLeavingsScore());
				
				returnOther.add(other1);
				returnOther.add(other2);
				returnOther.add(other3);
				returnOther.add(other4);
				ret.setOtherScores(returnOther);
			}
			
			if (scoreDetail != null)
			{
				for (ScoreDetail dt : scoreDetail)
				{
					ScoreDetail s1 = new ScoreDetail();
					s1.setDataTime(dt.getDataTime());  //年份
					s1.setType(1);
					
					if (null != dt.getScore() && !"".equals(dt.getScore()))
					{
						if (dt.getScore().indexOf(".") != -1)
							dt.setScore(dt.getScore().substring(0, dt.getScore().indexOf(".")));
						else
							dt.setScore(dt.getScore());
					}
					else
					{
						dt.setScore("0");
					}
					
					s1.setScore(dt.getScore());  //消费积分
					returnScore.add(s1);
					
					ScoreDetail s2 = new ScoreDetail();
					s2.setDataTime(dt.getDataTime());  //年份
					s2.setType(2);
					
					if (null != dt.getReserve1() && !"".equals(dt.getReserve1()))
					{
						if (dt.getReserve1().indexOf(".") != -1)
							dt.setReserve1(dt.getReserve1().substring(0, dt.getReserve1().indexOf(".")));
						else
							dt.setReserve1(dt.getReserve1());
					}
					else
					{
						dt.setReserve1("0");
					}
					
					s2.setScore(dt.getReserve1());  //奖励积分
					returnScore.add(s2);
					
					ScoreDetail s3 = new ScoreDetail();
					s3.setDataTime(dt.getDataTime());  //年份
					s3.setType(3);
					
					if (null != dt.getReserve2() && !"".equals(dt.getReserve2()))
					{
						if (dt.getReserve2().indexOf(".") != -1)
							dt.setReserve2(dt.getReserve2().substring(0, dt.getReserve2().indexOf(".")));
						else
							dt.setReserve2(dt.getReserve2());
					}
					else
					{
						dt.setReserve2("0");
					}
					
					s3.setScore(dt.getReserve2());  //兑换积分
					returnScore.add(s3);
				}
				ret.setScoreDetail(returnScore);
			}
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
