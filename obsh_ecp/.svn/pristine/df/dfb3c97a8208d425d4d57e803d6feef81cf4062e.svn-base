package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY610044Result;
import com.xwtech.xwecp.service.logic.pojo.Usableteldt;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 
 * @author wangh
 * 高校迎新
 * 通过新生随机码查询可用的在线入网号码
 *
 */
public class QueryTelNumListInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryTelNumListInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private ParseXmlConfig config;
	
	public QueryTelNumListInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.config = new ParseXmlConfig();
	}
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		
		QRY610044Result res = new QRY610044Result();
		res.setResultCode("0"); // 成功
		String reqXml = ""; 	// 发送报文
		String rspXml = ""; 	// 接收报文
		String resp_code = ""; 	// 返回码
		
		try {
			// 初始化准备
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgettelnumlist_3001", params);
			logger.debug(" ======  发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgettelnumlist_3001", super.generateCity(params)));
				logger.debug(" ====== 接收报文 ====== \n" + rspXml);
				
				
				// 解析BOSS报文
				if (null != rspXml && !"".equals(rspXml)) {
					// 解析报文 根节点
					Element root = this.config.getElement(rspXml);
					// 获取错误编码
					resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
					
					Element resRoot = this.config.getElement(root, "content");
					
					if("0".equals(resp_code) || "0000".equals(resp_code)){
						res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"), "resp_desc") == null ? null: this.config.getChildText(this.config.getElement(root, "response"), "resp_desc"));
						
						Element usabletellist = this.config.getElement(resRoot, "usabletellist");
						// 取第二层节点
						List<Element> childElmfeeDetails = usabletellist.getChildren();
						
						if (null != childElmfeeDetails && childElmfeeDetails.size() > 0) {
							for (Element elm : childElmfeeDetails) {
								Usableteldt usable = new Usableteldt();
								usable.setTelNum(elm.getChildText("telnum") == "" ? null : elm.getChildText("telnum"));
								res.getUsableteldtList().add(usable);
							}
						}
					}else{
						res.setResultCode(resp_code);
					}
				}
			}else{
				res.setResultCode("1"); // 失败
			}
		} catch (Exception e) {
			logger.error(e, e);
			res.setResultCode("1");
		}
		
		return res;
	}

}
