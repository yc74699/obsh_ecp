package com.xwtech.xwecp.service.logic.resolver;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.DbiInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010014Result;

/**
 * 获取消费状况信息
 * 
 * @author 邵琪
 * 
 */
public class GetDbiInfoResolver implements ITeletextResolver {
	private static final Logger logger = Logger.getLogger(GetDbiInfoResolver.class);

	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception {
		try {
			QRY010014Result ret = (QRY010014Result) result;
			byte[] tmp = new String((String) bossResponse).getBytes();
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			Element root = doc.getRootElement();
			List<DbiInfo> dbiInfoList;
			if (null != ret && null != ret.getResultCode()) {
				dbiInfoList = new ArrayList<DbiInfo>();
				XPath xpath = XPath.newInstance("/operation_out/content/result_date_ym/result_date_ym");
				List<Element> infoElementList = (List<Element>) xpath.selectNodes(root);
				Calendar cal_5 = Calendar.getInstance();
				cal_5.add(Calendar.MONTH, -5);
				String d1 = DateFormatUtils.format(cal_5.getTime(), "yyyyMM");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				for (Element infoElement : infoElementList) {
					String result_date_ym = infoElement.getText();
					//200904~月基本费~100~语音通信费~60~增值业务费~2700~代收费~100~PM~-2960~其他~0
					String[] data = result_date_ym.split("~");
					if("999999".equals(data[0])||sdf.parse(data[0]).before(sdf.parse(d1))){
						continue;
					}
					DbiInfo dbiInfo = new DbiInfo();
					long sum = 0;
					sum+=Long.parseLong(data[2]);
					sum+=Long.parseLong(data[4]);
					sum+=Long.parseLong(data[6]);
					sum+=Long.parseLong(data[8]);
					sum+=Long.parseLong(data[12]);
					dbiInfo.setMonth(data[0]);
					dbiInfo.setBaseFee(Long.parseLong(data[2]));
					dbiInfo.setSoundFee(Long.parseLong(data[4]));
					dbiInfo.setIncrementFee(Long.parseLong(data[6]));
					dbiInfo.setInsteadFee(Long.parseLong(data[8]));
					dbiInfo.setOtherFee(Long.parseLong(data[12]));
					dbiInfo.setTotalFee(sum);
					dbiInfoList.add(dbiInfo);
				}

				ret.setDbiInfo(dbiInfoList);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
