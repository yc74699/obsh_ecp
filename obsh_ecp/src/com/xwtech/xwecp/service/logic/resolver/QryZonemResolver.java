package com.xwtech.xwecp.service.logic.resolver;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.OtherScores;
import com.xwtech.xwecp.service.logic.pojo.QRY030002Result;
import com.xwtech.xwecp.service.logic.pojo.RingTongBill;
import com.xwtech.xwecp.service.logic.pojo.ScoreDT;
import com.xwtech.xwecp.service.logic.pojo.ScoreDetail;

public class QryZonemResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QryZonemResolver.class);
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		try
		{
			QRY030002Result ret = (QRY030002Result)result;
			ScoreDT scoreDt = ret.getScoreDT();
			List<OtherScores> returnOther = new ArrayList();
			
			if (null != scoreDt)
			{
				OtherScores o1 = new OtherScores();
				o1.setScoreName("客户M值");
				o1.setScore(ret.getRemainScore()==null?"0":ret.getRemainScore());
				returnOther.add(o1);
				
				OtherScores o2 = new OtherScores();
				o2.setScoreName("今年客户M值");
				o2.setScore(scoreDt.getUserId()==null?"0":scoreDt.getUserId());
				returnOther.add(o2);
				
				OtherScores o3 = new OtherScores();
				o3.setScoreName("去年客户M值");
				o3.setScore(scoreDt.getGiftScore()==null?"0":scoreDt.getGiftScore());
				returnOther.add(o3);
				
				OtherScores o4 = new OtherScores();
				o4.setScoreName("前年客户M值");
				o4.setScore(scoreDt.getExchangedScore()==null?"0":scoreDt.getExchangedScore());
				returnOther.add(o4);
				
				OtherScores o5 = new OtherScores();
				o5.setScoreName("今年客户消费M值");
				o5.setScore(scoreDt.getLeavingsScore()==null?"0":scoreDt.getLeavingsScore());
				returnOther.add(o5);
				
				OtherScores o6 = new OtherScores();
				o6.setScoreName("今年客户奖励M值");
				o6.setScore(scoreDt.getChangeFlag()==null?"0":scoreDt.getChangeFlag());
				returnOther.add(o6);
				
				OtherScores o7 = new OtherScores();
				o7.setScoreName("今年已兑换M值");
				o7.setScore(scoreDt.getRetCode()==null?"0":scoreDt.getRetCode());
				returnOther.add(o7);
			}
			ret.setOtherScores(returnOther);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	/*public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
			            List<RequestParameter> param) throws Exception
	{
		try
		{
			QRY030002Result ret = (QRY030002Result)result;
			List oList = new ArrayList();
			byte[] tmp = new String((String)bossResponse).getBytes();
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			Element root = doc.getRootElement();
			//Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			//String str = p.matcher(root.getChild("content").getChildText("XTABLE_GSM")).replaceAll("");
			
			Element m_Content = root.getChild("content");
			
			ret.setRemainScore(Long.parseLong(m_Content.getChildText("zonemvalue_total_mvalue")));
			
			OtherScores o1 = new OtherScores();  //其他积分 总的M值
			o1.setScoreName("总的M值");
			try
			{
				o1.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_total_mvalue")));
			}
			catch (Exception ex)
			{
				o1.setScore(0);
			}
			
			oList.add(o1);
			
			OtherScores o2 = new OtherScores();  //其他积分 今年的M值
			o2.setScoreName("今年的M值");
			try
			{
				o2.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_mvalue")));
			}
			catch (Exception ex)
			{
				o2.setScore(0);
			}
			oList.add(o2);
			
			OtherScores o3 = new OtherScores();  //其他积分 去年的M值
			o3.setScoreName("去年的M值");
			try
			{
				o3.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_last_mvalue")));
			}
			catch (Exception ex)
			{
				o3.setScore(0);
			}
			oList.add(o3);
			
			OtherScores o4 = new OtherScores();  //其他积分 前年的M值
			o4.setScoreName("前年的M值");
			try
			{
				o4.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_pre_mvalue")));
			}
			catch (Exception ex)
			{
				o4.setScore(0);
			}
			oList.add(o4);
			
			OtherScores o5 = new OtherScores();  //其他积分 今年已消费的M值
			o5.setScoreName("今年已消费的M值");
			try
			{
				o5.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_present_mvalue")));
			}
			catch (Exception ex)
			{
				o5.setScore(0);
			}
			oList.add(o5);
			
			OtherScores o6 = new OtherScores();  //其他积分 今年客户奖励M值
			o6.setScoreName("今年客户奖励M值");
			try
			{
				o6.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_bounty_mvalue")));
			}
			catch (Exception ex)
			{
				o6.setScore(0);
			}
			oList.add(o6);
			
			OtherScores o7 = new OtherScores();  //其他积分 今年已兑换M值
			o7.setScoreName("今年已兑换M值");
			try
			{
				o7.setScore(Long.parseLong(m_Content.getChildText("zonemvalue_used_mvalue")));
			}
			catch (Exception ex)
			{
				o7.setScore(0);
			}
			oList.add(o7);
			
			ret.setOtherScores(oList);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}*/
}
