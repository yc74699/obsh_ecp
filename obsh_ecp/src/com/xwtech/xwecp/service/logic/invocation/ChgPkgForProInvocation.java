package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040032Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品变更，变更时先查询原来订购的产品，然后传入需要的产品CODE
 * @author chenxiaoming_1037
 * 2011-11-04
 */
public class ChgPkgForProInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(ChgPkgForProInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private static final Map<String,String> packageType = new HashMap<String,String>();
	
	public ChgPkgForProInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		//可加入可以变更的套餐
		packageType.put("1018", "动感地带网聊套餐");
		packageType.put("1068", "随E行套餐");
//		packageType.put("1013", "省内漫游优惠套餐");
//		packageType.put("1022", "语音套餐");
		packageType.put("1031", "语音套餐");
//		packageType.put("1013", "taocan");
		packageType.put("1049", "手机上网伴侣套餐");
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL040032Result res = new DEL040032Result ();
		String reqXml = "";
		String rspXml = "";
		boolean isExistOldCode = false;
		try
		{
			logger.info("Del040032");
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
			logger.debug(" ====== 查询套餐信息发送报文 ======\n" + reqXml);
			logger.info("reqXml: "+reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");				
//					res.setResultCode(resultCode);
//					res.setErrorCode(resp_code);
//					res.setErrorMessage(resp_desc);
					
					if (BOSS_SUCCESS.equals(resp_code))
					{
						List<Element> package_code = root.getChild("content").getChildren("package_code");
						if (null != package_code && package_code.size() > 0)
						{
							for (int i = 0; i < package_code.size(); i++)
							{
								Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");
								
								String endDate = getNodeValueByNodeName(cplanpackagedt,"package_end_date");
										
								String packCode = getNodeValueByNodeName(cplanpackagedt,"package_code");
								
								String packType = getNodeValueByNodeName(cplanpackagedt,"package_type");
								
								//判断packCode是否满足条件
								if(isValidCode(packCode,packType,endDate))
								{
									//增加packCode到params中
									addParams(params,packCode,packType);
									isExistOldCode = true;
									break;
								}
								else
								{
									continue;
								}
							}
						}
					}
				}
				//存在oldPackCode可进行产品变更
				if(isExistOldCode)
				{
					try
					{
						reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgpkgforpro_76_WL", params);
						rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchgpkgforpro_76", this.generateCity(params)));
						if (null != rspXml && !"".equals(rspXml))
						{
							Element root = this.getElement(rspXml.getBytes());
							String resp_code = root.getChild("response").getChildText("resp_code");
							String resp_desc = root.getChild("response").getChildText("resp_desc");
							String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
							res.setResultCode(resultCode);
							res.setErrorCode(resp_code);
							res.setErrorMessage(resp_desc);
						}
					}
					catch (Exception e)
					{
						logger.error(e, e);
					}
				}
				else
				{
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode("-1");
					res.setErrorMessage("不存在可以变更的套餐!");
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}

	//增加packCode,packType到params中
    private void addParams(List<RequestParameter> params, String packCode,String packType) 
    {
    	RequestParameter paraCode = new RequestParameter();
    	paraCode.setParameterName("oldPkgCode");
    	paraCode.setParameterValue(packCode);
    	
    	RequestParameter paraType = new RequestParameter();
    	paraType.setParameterName("packType");
    	paraType.setParameterValue(packType);
    	
		params.add(paraCode);
		params.add(paraType);
	}
    
    //判断packCode是否满足条件
    private boolean isValidCode(String packCode, String packType, String endDate) 
	{
    	//首先判断两个packCode与packType，packCode截取后四位
    	if(packCode.endsWith(packType) && packCode.length() > packType.length())
    	{
    		return false;
    	}
    	//失效日期为空，表示该产品在使用，可以进行变更
    	if(null != endDate && !"".equals(endDate))
    	{
    		return false;
    	}
    	//判断产品是否与变更
    	if(!isChgPackType(packType))
    	{
    		return false;
    	}
    	return true;
	}

	private boolean isChgPackType(String packType) 
	{
		if(packageType.containsKey(packType))
		{
			return true;
		}
		return false;
	}
}
