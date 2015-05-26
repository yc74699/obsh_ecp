package com.xwtech.xwecp.service.logic.resolver;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.Exchange;
import com.xwtech.xwecp.service.logic.pojo.QRY030001Result;

public class QueryExchangeResolver  implements ITeletextResolver
{
    private static final Logger logger = Logger.getLogger(QueryExchangeResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public QueryExchangeResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	
	
	public void resolve(BaseServiceInvocationResult result,
			Object bossResponse, List<RequestParameter> param) throws Exception {
		
		String bossResponseStr = bossResponse.toString();
		QRY030001Result res = (QRY030001Result)result;
		logger.info("===========积分/M值兑换规则查询开始=============================");
		List<Exchange> exchangeList = null;
		
		//param 中extType属性
		//5: 积分可兑换话费列表
		//"":M值可兑换话费列表
		String extType = "";
		// 1 : 积分  2.M值
		int queryType = 0;
		for(int i = 0 ; i < param.size() ; i ++)
		{
			if(param.get(i).getParameterName().equals("extType"))
			{
				extType = (String) param.get(i).getParameterValue();
			}
		}
		
		queryType = extType.equals("5") ? 1 : 2; 
		
		try
		{
			if (null != bossResponseStr && !"".equals(bossResponseStr))
			{
				Element root = this.getElement(bossResponseStr.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				
				if (null != resp_code && (resp_code.equals("0000")))
				{
					Element content = root.getChild("content");
					String tableList = p.matcher(content.getChildText("XTABLE_SCO_REDEEM")).replaceAll("");
					String [] rows = tableList.split(";");
					
					exchangeList = new ArrayList<Exchange>(rows.length - 1);
					Exchange exchange = null;
					
					
					//积分兑换方式~积分兑换积分数~积分兑换名称~积分兑换描述~积分兑换开始时间~积分兑换结束时间~积分兑换地市~积分兑换金额~积分兑换类型~积分兑换大类~剩余可兑换数量~当日可兑换数量配置~当日可兑换数量~兑换物品描述~
					for (int i = 1; i < rows.length; i ++) 
				   {
						String [] cells = rows[i].split("~");
						
						if(queryType == 1)
						{
							if(cells[8].equals("1"))
							{
								exchange = new Exchange();
								exchange.setExtNo(cells[0]);
								exchange.setNeedScores(cells[1]);
								exchange.setBizDesc(cells[2]);
								exchange.setBizId(cells[8]);
								exchangeList.add(exchange);
							}
						}
						if(queryType == 2)
						{
							if(cells[8].equals("100"))
							{
								exchange = new Exchange();
								exchange.setExtNo(cells[0]);
								exchange.setNeedScores(cells[1]);
								exchange.setBizDesc(cells[2]);
								exchange.setBizId(cells[8]);
								exchangeList.add(exchange);
							}
						}
				   }
				}
				res.setExchangeList(exchangeList);
				
			}
			else
			{
				res.setResultCode("1");
			}
			logger.info("===========积分/M值兑换规则查询结束=============================");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			res.setResultCode("1");
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

}
