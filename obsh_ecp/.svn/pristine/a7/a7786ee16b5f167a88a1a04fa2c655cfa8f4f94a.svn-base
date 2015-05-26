package com.xwtech.xwecp.service.logic.resolver;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.AccountFeeInfo;
import com.xwtech.xwecp.service.logic.pojo.BillPackageInfo;
import com.xwtech.xwecp.service.logic.pojo.FixedPhone;
import com.xwtech.xwecp.service.logic.pojo.QRY010002Result;
import com.xwtech.xwecp.service.logic.pojo.QRY010010Result;
import com.xwtech.xwecp.service.logic.pojo.QRY010011Result;

/**
 * 个人宽带清单查询
 * 
 * @author 邵琪
 * 
 */
public class GetFixedPhoneResolver implements ITeletextResolver {
	private static final Logger logger = Logger.getLogger(GetFixedPhoneResolver.class);

	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception {
		try {
			/*
			String testXML = "<?xml version=\"1.0\" encoding=\"gbk\"?>"
				+"<operation_out><process_code>ac_internetcdr_query</process_code><request_type/><sysfunc_id>541</sysfunc_id>" +
						"<request_seq>1_115</request_seq><response_time>20100121163912</response_time><request_source>102010</request_source>" +
						"<response><resp_type>0</resp_type><resp_code>0000</resp_code><resp_desc/></response>" +
						"<content><XTABLE_INTERNET>开始时间~结束时间~时长~出流量~入流量~费用~;20100103203243~20100103220027~5264~45690196~51340395~0~;" +
						"20100104200721~20100104223137~8656~15292771~2651761~0~;20100105195548~20100105222551~9003~42838205~5310192~0~;" +
						"20100106113702~20100106115655~1193~1858281~434631~0~;20100106120041~20100106120238~117~91980~74462~0~;" +
						"20100107205033~20100107213217~2504~30946956~15958730~0~;20100108204714~20100108225930~7936~52078423~12261489~0~;" +
						"20100109103449~20100109121059~5770~8239496~1188877~0~;20100109212136~20100109223011~4115~8526970~2201341~0~;" +
						"20100110153254~20100110170315~5421~27531493~4431594~0~;20100110170606~20100110185400~6474~10834674~3140256~0~;" +
						"20100110185625~20100110190152~327~1516166~319030~0~;20100110190203~20100110202533~5010~54940897~6889645~0~;" +
						"20100111213305~20100111225323~4818~51356915~3823163~0~;20100112210818~20100112214130~1992~146023858~22274276~0~;" +
						"20100113214612~20100113230120~4508~23076251~3423182~0~;</XTABLE_INTERNET></content></operation_out>";
			*/
			byte[] tmp = new String((String) bossResponse).getBytes();
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			Element root = doc.getRootElement();
			List<FixedPhone> list;
			if (null != result && null != result.getResultCode()) {

				// 费用信息--开始
				list = new ArrayList<FixedPhone>();
				// 费用总计(元)
				long totalFee = 0;
				// 合计上网时长
				long durationTime = 0;
				String month = "";
				for (int i = 0; i < param.size(); i++) {
					RequestParameter req = param.get(i);
					if (req.getParameterName().equals("qryMonth")) {
						month = String.valueOf(req.getParameterValue());
					}
				}
				if (!"".equals(month) && Integer.valueOf(month) <= 201105) {
					XPath xpath = XPath.newInstance("/operation_out/content/XTABLE_INTERNET");
					Element infoElement = (Element) xpath.selectSingleNode(root);
					if(infoElement != null){
						String contentStr = infoElement.getText();
						String[] contents = contentStr.split(";");
						for (int i = 1; i < contents.length; i++) {
							if(contents[i].trim().length() > 0){
								String[] fields = contents[i].split("~");
								FixedPhone fixedPhone = new FixedPhone();
								fixedPhone.setStartTime(fields[0]);//开始时间
								fixedPhone.setEndTime(fields[1]);//结束时间
								fixedPhone.setCallDuration(Long.parseLong(fields[2]));//时长
								durationTime += Long.parseLong(fields[2]);
								fixedPhone.setCdrApnni(Long.parseLong(fields[3]));//入流量
								fixedPhone.setCdrApnoi(Long.parseLong(fields[4]));//出流量
								fixedPhone.setFreeFee1(Long.parseLong(fields[5]));//费用
								totalFee += Long.parseLong(fields[5]);
								list.add(fixedPhone);
							}
						}
					}
				} else {
					XPath xpath = XPath.newInstance("/operation_out/content/xtcdr");
					Element infoElement = (Element) xpath.selectSingleNode(root);
					if(infoElement != null){
						String contentStr = infoElement.getText();
						String[] contents = contentStr.split(";");
						for (int i = 1; i < contents.length; i++) {
							if(contents[i].trim().length() > 0){
								String[] fields = contents[i].split("~");
								FixedPhone fixedPhone = new FixedPhone();
								fixedPhone.setStartTime(fields[0]);//开始时间
								fixedPhone.setVisitArea(fields[1]);
								fixedPhone.setVisitType(fields[2]);
								//fixedPhone.setEndTime(fields[1]);//结束时间
								fixedPhone.setCallDuration(Long.parseLong(fields[3]));//时长
								durationTime += Long.parseLong(fields[3]);
								fixedPhone.setAllCdrApnni(Long.parseLong(fields[4]));
//								fixedPhone.setCdrApnni(Long.parseLong(fields[3]));//入流量
//								fixedPhone.setCdrApnoi(Long.parseLong(fields[4]));//出流量
								fixedPhone.setFreeFee1(Long.parseLong(fields[6]));//费用
								totalFee += Long.parseLong(fields[6]);
								list.add(fixedPhone);
							}
						}
					}
				}
				
				((QRY010010Result)result).setFixedPhoneList(list);
				((QRY010010Result)result).setDurationTime(durationTime);
				((QRY010010Result)result).setTotalFee(totalFee);
				
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		QRY010010Result result = new QRY010010Result();
		result.setResultCode("0000");
		GetFixedPhoneResolver gfpr = new GetFixedPhoneResolver();
		gfpr.resolve(result, "", null);
	}

}
