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
import com.xwtech.xwecp.service.logic.pojo.QRY010002Result;
import com.xwtech.xwecp.service.logic.pojo.QRY010011Result;

/**
 * 查询历史账单信息
 * 
 * @author 邵琪
 * 
 */
public class GetHistoryBillResolver implements ITeletextResolver {
	private static final Logger logger = Logger.getLogger(GetHistoryBillResolver.class);

	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception {
		try {
			byte[] tmp = new String((String) bossResponse).getBytes();
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			Element root = doc.getRootElement();
			List<AccountFeeInfo> list;
			if (null != result && null != result.getResultCode()) {

				// 费用信息--开始
				list = new ArrayList<AccountFeeInfo>();
				// 他人代付
				long other = 0;
				// 集团代付
				long group = 0;
				// 费用总计
				long count = 0;

				XPath xpath = XPath.newInstance("/operation_out/content/dbi_def_id/cdbilist");
				List<Element> infoElementList = (List<Element>) xpath.selectNodes(root);
				for (Element infoElement : infoElementList) {
					String name = infoElement.getChildText("dbi_def_name");
					String fee = infoElement.getChildText("dbi_amount");
					String code = infoElement.getChildText("dbi_def_id");
					String superCode = infoElement.getChildText("pre_def_id");
					String leval = infoElement.getChildText("def_level");
					String displayOrder = infoElement.getChildText("display_order");

					String paymentType = infoElement.getChildText("account_payment_type");
					String dbi_bill_discount = infoElement.getChildText("dbi_bill_discount");

					Long feeNum = (long) Double.parseDouble(fee);
					Long discountNum = (long) Double.parseDouble(dbi_bill_discount);
					// 计算集团代付和他人代付信息
					if ("3".equals(leval)) {
						if ("2".equals(paymentType)) {
							other += feeNum;
							other -= discountNum;
						} else if ("3".equals(paymentType)) {
							group += feeNum;
							group -= discountNum;
						}
					}
					// 界面展示时，我们取4：合计信息
					if ("4".equals(paymentType)) {
						if("PM".equals(code)){
							count -=  discountNum;
						}
						// 统计第一层的总和
						if ("1".equals(leval) && !"PM".equals(code)) {
							// infoElement.getChildText("dbi_def_name") + "____"
							// + fee);
							count += feeNum - discountNum;
						}

						AccountFeeInfo accountFeeInfo = new AccountFeeInfo();
						accountFeeInfo.setName(name);
						accountFeeInfo.setFee(feeNum - discountNum);
						accountFeeInfo.setCode(code);
						accountFeeInfo.setSuperCode(superCode);
						accountFeeInfo.setLeval(Integer.parseInt(leval));
						accountFeeInfo.setDisplayOrder(Integer.parseInt(displayOrder));
						list.add(accountFeeInfo);
					}
				}
				if(result instanceof QRY010002Result){
					QRY010002Result ret = (QRY010002Result) result;
					ret.setSumFee(count);
					ret.setOther(other);
					ret.setGroup(group);
					ret.setAccountFeeInfo(list);
				}else if(result instanceof QRY010011Result){
					QRY010011Result ret = (QRY010011Result) result;
					ret.setSumFee(count);
					ret.setOther(other);
					ret.setGroup(group);
					ret.setAccountFeeInfo(list);
				}
				// 费用信息--结束

				// 通信量信息--开始
				List<BillPackageInfo> billPkgList = new ArrayList<BillPackageInfo>();
				XPath xpathPkg = XPath.newInstance("/operation_out/content/package_user_id/package_dt");
				List<Element> pkgInfoElementList = (List<Element>) xpathPkg.selectNodes(root);
				Map<String,BillPackageInfo> pkgMap = new HashMap<String,BillPackageInfo>();
				for (Element pkgInfoElement : pkgInfoElementList) {
					// Show_type 2类型才为流量计算，其他忽略
					if (!"2".equals(pkgInfoElement.getChildText("package_show_type"))) {
						continue;
					}
					String freeItemId = pkgInfoElement.getChildText("package_freeitem_id");;
					if ("701".equals(freeItemId) 
							|| "801".equals(freeItemId) 
							|| "802".equals(freeItemId) 
							|| "803".equals(freeItemId) 
							|| "804".equals(freeItemId) || true) 
					{
						String code = pkgInfoElement.getChildText("package_package_code");
						String subId = pkgInfoElement.getChildText("package_fee_sub_id");
						int value = Integer.parseInt(pkgInfoElement.getChildText("package_package_value"));
						
						BillPackageInfo pckBean;
						if (pkgMap.containsKey(code)) {
							pckBean = pkgMap.get(code);
						} else {
							pckBean = new BillPackageInfo();
							String name = pkgInfoElement.getChildText("package_name");
							pckBean.setName(name);
							int order = Integer.parseInt(pkgInfoElement.getChildText("package_display_order"));
							pckBean.setDisplayOrder(order);
							pkgMap.put(code, pckBean);
						}

						// 本地主叫
						if ("1".equalsIgnoreCase(subId)) {
							if(pckBean.getBdzj() == null)
								pckBean.setBdzj(value);
							else
								pckBean.setBdzj(pckBean.getBdzj() + value);
						}
						// 本地被叫
						else if ("2".equalsIgnoreCase(subId)) {
							if(pckBean.getBdbj() == null)
								pckBean.setBdbj(value);
							else
								pckBean.setBdbj(pckBean.getBdbj() + value);
						}
						// 国内长途
						else if ("3".equalsIgnoreCase(subId)) {
							if(pckBean.getGnct() == null)
								pckBean.setGnct(value);
							else
								pckBean.setGnct(pckBean.getGnct() + value);
						}
						// 国内漫游
						else if ("4".equalsIgnoreCase(subId)) {
							if(pckBean.getGnmy() == null)
								pckBean.setGnmy(value);
							else
								pckBean.setGnmy(pckBean.getGnmy() + value);
						}
						// 国际长途
						else if ("5".equalsIgnoreCase(subId)) {
							if(pckBean.getGjct() == null)
								pckBean.setGjct(value);
							else
								pckBean.setGjct(pckBean.getGjct() + value);
						}
						// 国际漫游
						else if ("6".equalsIgnoreCase(subId)) {
							if(pckBean.getGjmy() == null)
								pckBean.setGjmy(value);
							else
								pckBean.setGjmy(pckBean.getGjmy() + value);
						}
						// 短信
						else if ("7".equalsIgnoreCase(subId)) {
							if(pckBean.getDx() == null)
								pckBean.setDx(value);
							else
								pckBean.setDx(pckBean.getDx() + value);
						}
						// 彩信
						else if ("8".equalsIgnoreCase(subId)) {
							if(pckBean.getCx() == null)
								pckBean.setCx(value);
							else
								pckBean.setCx(pckBean.getCx() + value);
						}
					}
					
				}
				billPkgList.addAll(pkgMap.values());
				if(result instanceof QRY010002Result){
					QRY010002Result ret = (QRY010002Result) result;
					ret.setBillPackageInfo(billPkgList);
				}else if(result instanceof QRY010011Result){
					QRY010011Result ret = (QRY010011Result) result;
					ret.setBillPackageInfo(billPkgList);
				}
				
				// 通信量信息--结束

			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
