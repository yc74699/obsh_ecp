package com.xwtech.xwecp.test;


import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;


public class TeletextResolverTest implements ITeletextResolver
{
	private static final Logger logger = Logger
			.getLogger(TeletextResolverTest.class);

	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception
	{
		/*
		L10012Result ret = (L10012Result)(result);
		String retXML = (String)(bossResponse);
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(new StringBufferInputStream(retXML));
		Element root = doc.getRootElement();
		List<Element> hisEl = root.getChild("user-history").getChildren("history");
		logger.info(hisEl.size());
		for(int i = 0;i<hisEl.size();i++)
		{
			UserHistory uh = new UserHistory();
			ret.getUserHistory().add(uh);
			String phoneNumber = hisEl.get(i).getChildText("phoneNumber");
			String phoneTime = hisEl.get(i).getChildText("phoneTime");
			uh.setPhoneNumber(phoneNumber);
			uh.setPhoneTime(phoneTime);
			List<Element> feeDetailEl = hisEl.get(i).getChild("fee").getChildren("feeDetail");			
			for(int j = 0;j<feeDetailEl.size();j++)
			{
				FeeDetail fd = new FeeDetail();
				uh.getFeeDetail().add(fd);
				String _phoneNumber = feeDetailEl.get(j).getChildText("phoneNumber");
				String _fee = feeDetailEl.get(j).getChildText("fee");
				String _userId = feeDetailEl.get(j).getChild("userInfo").getChildText("userId");
				String _userBrand = feeDetailEl.get(j).getChild("userInfo").getChildText("userId");
				String _userBrandId = feeDetailEl.get(j).getChild("userInfo").getChildText("userBrandId");
				fd.setFee(Double.parseDouble(_fee));
				fd.setPhoneNumber(_phoneNumber);
				UserInfo ui = new UserInfo();
				fd.setUserInfo(ui);
				ui.setUserId(_userId);
				UserBrand ub = new UserBrand();
				ui.setUserBrand(ub);
				ub.setUserBrand(_userBrand);
				ub.setUserBrandId(Integer.parseInt(_userBrandId));
			}
		}
		*/
	}
}
