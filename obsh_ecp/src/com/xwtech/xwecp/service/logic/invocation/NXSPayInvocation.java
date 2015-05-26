package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import com.csii.payment.client.core.MerchantSignVerify;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040100Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;
/**
 * 农信社充值
 * @author xwtec
 *
 */
public class NXSPayInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(NXSPayInvocation.class);
	
	private static final String NXS_URL = "http://10.208.68.100/payment-access/generalconnect.do";
	private static final String OBSH_URL = "http://service.js.10086.cn/wscz.jsp#WSCZYL";
	private static final String merchantId = "800025";
	private static final String transId ="ZF04";
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		//请求取得流水
		DEL040100Result result = new DEL040100Result();
		String bucardgetcltsrl = getBucardgetcltsrl(accessId,config,params,result);
		if(!StringUtil.isNull(bucardgetcltsrl))
		{
			//根据流水充值
			nxsCZWithSrl(bucardgetcltsrl,accessId,params,result);
		}
		return result;
	}
	/**
	 * 农信社充值
	 * @param bucardgetcltsrl
	 * @param accessId
	 * @param params
	 * @param result
	 */
	private void nxsCZWithSrl(String bucardgetcltsrl, String accessId,
			List<RequestParameter> params, DEL040100Result result)
	{
		String reqXml = "";
		String rspXml = "";
		//组装充值报文
		params.add(new RequestParameter("clt_operating_srl",bucardgetcltsrl));
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_bucheckdeal_rcc_607", params);
			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_bucheckdeal_rcc_607", super.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			Element element = checkReturnXml(rspXml, result);
			String phone = (String) (getParameter(params, "phoneNum") == null ? "" : getParameter(params, "phoneNum").getParameterValue());
			String amount = getParameter(params, "amount")== null ? "" : (String)getParameter(params, "amount").getParameterValue();
			if(null != element && "0".equals(result.getResultCode()))
			{
				//充值成功,生成返回form
				generateUrl(bucardgetcltsrl,phone,amount,result);
				result.setCltSrl(bucardgetcltsrl);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.debug(e);
		}
	
	}
	
	private void generateUrl(String srl,String userNum,String amount,DEL040100Result result)
	{

        String plain =  getPlain(srl,amount,userNum,NXS_URL,OBSH_URL,merchantId);
        String signature = getSignature(plain);
        
        if(!StringUtil.isNull(plain) && !StringUtil.isNull(signature))
        {
        	result.setSignature(signature);
        	result.setPlain(plain);
        	result.setPayUrl(NXS_URL);
        	//把没有签好的也设成resultCode=1
            //if(plain.length())
        }
        //加入没有签名的情况返回为resultCode=1
        else
        {
        	result.setResultCode("1");
        }
        
        /*  //mod 支付宝跳转重新打开一个新页面添加属性【target=\"_blank\"】
        sbHtml.append("<form target=\"_blank\" id=\"nxs_submit\" name=\"nxs_submit\" action=\"" + NXS_URL
                      +  "\" method=\"" + "post\"" + " charset=\"GBK\""
                      + ">");
      sbHtml.append("<input type=\"hidden\" name=\"Plain" + "" + "\" value=\"" +plain + "\"/>");
      sbHtml.append("<input type=\"hidden\" name=\"Signature" + "" + "\" value=\"" + signature + "\"/>");
      sbHtml.append("<input type=\"hidden\" name=\"user_num" + "" + "\" value=\"" + userNum + "\"/>");
      sbHtml.append("<input type=\"hidden\" name=\"srl" + "" + "\" value=\"手机话费*商品订单号：" + srl + "\"/>");
      sbHtml.append("<input type=\"hidden\" name=\"notify_url" + "" + "\" value=\"" + OBSH_URL + "\"/>");
        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("</form>");
	   //	return sbHtml.toString();
   	*/	
    }
	private String getSignature(String plainData)
	{
		String signa="";
		try{
			signa=MerchantSignVerify.merchantSignData_ABA(plainData);
		}catch(Exception e) {
           //signa="ERROR";
			e.printStackTrace();
			logger.info(e);
		}
		return signa;
	}
	private String getPlain(String orderId,String amount,String phoneNum,String url,String obshUrl,String merchantId)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("transId="+transId+"~|~channelId=002~|~merchantId=").append(merchantId).append("~|~orderId=")
		      .append(orderId).append("~|~transAmt=").append(amount).append("~|~transDateTime=").append(DateTimeUtil.getTodayChar14())
		      .append("~|~currencyType=156~|~customerName=~|~productInfo=").append(phoneNum).append("~|~customerEMail=~|~merURL=")
		      .append(obshUrl).append("~|~msgExt=");
//		String plain = "transId=ZF04~|~channelId=002~|~merchantId="+merchantId+"~|~orderId="+orderId+"~|~transAmt="+amount+"~|~transDateTime=" +
//				DateTimeUtil.getTodayChar14()+"~|~currencyType=156~|~customerName=test~|~productInfo="+phoneNum+"~|~customerEMail=test@hotmail.com~|~merURL="+obshUrl+"~|~msgExt=";
		String plain = buffer.toString();
		return plain;
	}
	/**
	 * 取得流水号
	 * @param accessId
	 * @param config
	 * @param params
	 * @param result
	 * @return
	 */
	private String getBucardgetcltsrl(String accessId, ServiceConfig config,
			List<RequestParameter> params, DEL040100Result result)
	{
		String reqXml = "";
		String rspXml = "";
		String srl = "";

		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_bucardgetcltsrl_607", params);
			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", super.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			Element element = checkReturnXml(rspXml, result);
			if(null != element)
			{
				//取得流水号
				XPath xpath = XPath.newInstance("/operation_out/content/clt_operating_srl");
				srl = ((Element) xpath.selectSingleNode(element)).getText();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.debug(e);
		}
	
		
		return srl;
	}
	
	private Element checkReturnXml(String rspXml,DEL040100Result res)
	{
		if(null != rspXml)
		{
			Element root = this.getElement(rspXml.getBytes());
			if(null != root)
			{
	    		String resp_code = root.getChild("response").getChildText("resp_code");
	    		String resp_desc = root.getChild("response").getChildText("resp_desc");
	    		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
	    		if(null != rspXml && !"".equals(rspXml))
	    		{
	    			if(!LOGIC_SUCESS.equals(resultCode) || null == root)
	    			{
	    				res.setResultCode(resultCode);
	    				res.setErrorCode(resp_code);
	    				res.setErrorMessage(resp_desc);
	    			}
	    			else
	    			{
	    				res.setResultCode(resultCode);
	    				res.setErrorCode(resp_code);
	    				res.setErrorMessage(resp_desc);
	    				return root;
	    			}
	    		}
			}
		}
		return null;
	}
}
