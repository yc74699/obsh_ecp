package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.JTVWPackageInfo;
import com.xwtech.xwecp.service.logic.pojo.PackageManage;
import com.xwtech.xwecp.service.logic.pojo.PackagePrice;
import com.xwtech.xwecp.service.logic.pojo.QRY210010Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;

/**
 * boss业务查询
 * 集团网厅--同事网最优套餐获取
 * @author 陈宜勇（集团网厅）
 *
 */
public class CheckUserPackageMessageAction extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(CheckUserPackageMessageAction.class);
	
	/**
	 * city:地市
	 * customer_code:集团编码
	 */
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		
		List<JTVWPackageInfo> jtvwPackageInfos = new ArrayList<JTVWPackageInfo>();
		JTVWPackageInfo jtInfo = null;
		QRY210010Result res = new QRY210010Result();
		res.setResultCode("0");
		String reqXml = "";
		String rspXml = "";
		String city = (String) getParameters(params, "ddrC6ity");
		String customer_codes=(String) getParameters(params, "customerCode");
		String telNumber=(String) getParameters(params, "telNumber");
		String[] customer_code = customer_codes.split("#");
		ErrorMapping errDt = null;
		try {

			for(int j  = 0; j < customer_code.length; j++)
			{
				jtInfo = new JTVWPackageInfo();
				jtInfo.setCustomer_code(customer_code[j]);
				//组装入参
				params.add(new RequestParameter("context_ddr_city", city));
				
				removeRequest(params,"context_customer_code");
				params.add(new RequestParameter("context_customer_code", customer_code[j]));
				
				//组装发送报文
				reqXml = this.bossTeletextUtil.mergeTeletext("qrygrporderprodinfo_001", params);
				logger.info(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml)){
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "qrygrporderprodinfo_001", this.generateCity(params)));
					logger.info(" ====== 接收报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml)){
						Element root = this.getElement(rspXml.getBytes());
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						PackageManage pm = new PackageManage();
						if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
							XPath xpath = XPath.newInstance("/operation_out/content/package_info");
							List<Element> list = xpath.selectNodes(root);
							Boolean flag = false;
							for (Element element : list) {
								PackageManage temp = new PackageManage();
								temp.setPackage_name(element.getChildText("package_name").trim());
								temp.setPackage_number(element.getChildText("package_number").trim());
								temp.setPackage_type(element.getChildText("package_type").trim());
								temp.setPackageCode(element.getChildText("package_code").trim());
								String str=element.getChildText("package_name").trim();
								String package_1="";
								String package_2="";
								String package_name="";
								if(str.indexOf("元包")==-1){
									continue;
								}else{
									String char_1=str.charAt(str.indexOf("元包")-1)+"";
									String char_2=str.charAt(str.indexOf("元包")-2)+"";
									package_2=str.substring(str.indexOf("元包"),str.indexOf("钟")+1);
									package_1=char_2+char_1;
									Pattern pattern = Pattern.compile("[0-9]{1,}");  
									Matcher matcher = pattern.matcher((CharSequence) package_1);
									System.out.println(matcher.matches());
									if(!matcher.matches()){
										if(package_1.equals("10")){
											package_name=package_1+package_2;
										}else{
											package_name=char_1+package_2;
										}
									}else{
										if(package_1.equals("10")){
											package_name=package_1+package_2;
										}
									}
								}
								temp.setProdname(package_name);
								String package_code="";
								if(package_name.equals("1元包本地主叫200分钟")){
									package_code="2000001586";
								}else if(package_name.equals("3元包本地主叫500分钟")){
									package_code="2000001587";
								}else if(package_name.equals("5元包本地主叫800分钟")){
									package_code="2000001588";
								}else if(package_name.equals("5元包省内主叫500分钟")){
									package_code="2000001589";
								}else if(package_name.equals("10元包省内主叫800分钟")){
									package_code="2000001590";
								}
								if(package_name.equals("1元包本地主叫200分钟") || package_name.equals("3元包本地主叫500分钟") || package_name.equals("5元包本地主叫800分钟") || package_name.equals("5元包省内主叫500分钟") || package_name.equals("10元包省内主叫800分钟")){
									if(pm.getPackage_number()!=null){
										if(Integer.parseInt(temp.getPackage_number())>Integer.parseInt(pm.getPackage_number())){
											pm = temp;
											pm.setProdid(package_code);
											flag = false;
										}else{
											flag = true;
										}
										
									}
									if(!flag){
									pm = temp;
									pm.setProdid(package_code);
									}
								}
								
							}
						}
						if (!BOSS_SUCCESS.equals(errCode)){
							errDt = this.wellFormedDAO.transBossErrCode("QRY210010", "qrygrporderprodinfo_001", errCode);
							if (null != errDt){
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
						jtInfo.setPackageCode(pm.getPackageCode());
						jtInfo.setPackageDesc(pm.getPackage_name());
						jtInfo.setProdid(pm.getProdid());
						jtInfo.setProdname(pm.getProdname());
						
						removeRequest(params,"package_code");
						params.add(new RequestParameter("package_code", pm.getPackageCode()));
						//组装发送报文
						reqXml = this.bossTeletextUtil.mergeTeletext("getvpmngrpsubsmeminfo_002", params);
						logger.info(" ====== 发送报文 ======\n" + reqXml);
						if (null != reqXml && !"".equals(reqXml)){
							//发送并接收报文
							rspXml = (String)this.remote.callRemote(
										new StringTeletext(reqXml, accessId, "getvpmngrpsubsmeminfo_002", this.generateCity(params)));
							logger.info(" ====== 接收报文 ======\n" + rspXml);
							if (null != rspXml && !"".equals(rspXml)){
								Element roots = this.getElement(rspXml.getBytes());
								errCode = roots.getChild("response").getChildText("resp_code");
								errDesc = roots.getChild("response").getChildText("resp_desc");
								String telNo="";
								if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
									XPath xpath = XPath.newInstance("/operation_out/content/member_info");
									List<Element> list = xpath.selectNodes(roots);
									for (Element element : list) {
										telNo=element.getChildText("phone_number").trim();
									}
								}
								if (!BOSS_SUCCESS.equals(errCode)){
									errDt = this.wellFormedDAO.transBossErrCode("QRY210010", "getvpmngrpsubsmeminfo_002", errCode);
									if (null != errDt){
										errCode = errDt.getLiErrCode();
										errDesc = errDt.getLiErrMsg();
									}
								}
								jtInfo.setTelNumber(telNo);
							}
						}
						
						params.add(new RequestParameter("phoneNum",telNumber));
						reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params);
						logger.info(" ====== 发送报文 ======\n" + reqXml);
						if (null != reqXml && !"".equals(reqXml)){
							//发送并接收报文
							rspXml = (String)this.remote.callRemote(
										new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", this.generateCity(params)));
							logger.info(" ====== 接收报文 ======\n" + rspXml);
							if (null != rspXml && !"".equals(rspXml)){
								Element roots = this.getElement(rspXml.getBytes());
								errCode = roots.getChild("response").getChildText("resp_code");
								errDesc = roots.getChild("response").getChildText("resp_desc");
								if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
									XPath xpath = XPath.newInstance("/operation_out/content/user_info");
									List<Element> list = xpath.selectNodes(roots);
									for (Element element : list) {
										String brand=element.getChildText("user_brand_id").trim();
										jtInfo.setBrand(brand);
										
										params.add(new RequestParameter("bizId","0"));
										removeRequest(params,"context_user_id");
										params.add(new RequestParameter("context_user_id",element.getChildText("user_id").trim()));
										reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62", params);
										logger.info(" ====== 发送报文 ======\n" + reqXml);
										if (null != reqXml && !"".equals(reqXml)){
											//发送并接收报文
											rspXml = (String)this.remote.callRemote(
														new StringTeletext(reqXml, accessId, "cc_find_package_62", this.generateCity(params)));
											logger.info(" ====== 接收报文 ======\n" + rspXml);
											if (null != rspXml && !"".equals(rspXml)){
												Element rootes = this.getElement(rspXml.getBytes());
												errCode = rootes.getChild("response").getChildText("resp_code");
												errDesc = rootes.getChild("response").getChildText("resp_desc");
												if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
													XPath xpaths = XPath.newInstance("/operation_out/content/package_code");
													List<Element> lists = xpaths.selectNodes(rootes);
													for (Element elements : lists) {
														if(elements.getChild("cplanpackagedt").getChildText("package_end_date").equals("")){
															if(jtInfo.getBrand().equals("5")||jtInfo.getBrand().equals("7")||jtInfo.getBrand().equals("8")||jtInfo.getBrand().equals("9")||jtInfo.getBrand().equals("10")||jtInfo.getBrand().equals("12")){
																jtInfo.setPrice(wellFormedDAO.findPackagePriceAndTime(jtInfo.getProdid(), "0000000001",0));
																jtInfo.setPkgTime(wellFormedDAO.findPackagePriceAndTime(jtInfo.getProdid(), "0000000001",1));
															}else if(jtInfo.getBrand().equals("6")||jtInfo.getBrand().equals("11")){
																jtInfo.setPrice(wellFormedDAO.findPackagePriceAndTime(jtInfo.getProdid(), "0000000002",0));
																jtInfo.setPkgTime(wellFormedDAO.findPackagePriceAndTime(jtInfo.getProdid(), "0000000002",1));
															}else if(jtInfo.getBrand().equals("1")){
																List<PackagePrice> ls=wellFormedDAO.findPackagePriceList(jtInfo.getProdid());
																for (int i = 0; i < ls.size(); i++) {
																	if(ls.get(i).getPackage_code().equals(elements.getChild("cplanpackagedt").getChildText("package_code"))){
																		jtInfo.setPrice(Integer.parseInt(ls.get(i).getPrice()));
																		jtInfo.setPkgTime(Integer.parseInt(ls.get(i).getPkgTime()));
																		break;
																	}
																}
															}
														}
													}
												}
												
											}
											if (!BOSS_SUCCESS.equals(errCode)){
												errDt = this.wellFormedDAO.transBossErrCode("", "cc_find_package_62", errCode);
												if (null != errDt){
													errCode = errDt.getLiErrCode();
													errDesc = errDt.getLiErrMsg();
												}
											}
											
										}
									}
								}
								if (!BOSS_SUCCESS.equals(errCode)){
									errDt = this.wellFormedDAO.transBossErrCode("QRY040001", "cc_cgetusercust_69", errCode);
									if (null != errDt){
										errCode = errDt.getLiErrCode();
										errDesc = errDt.getLiErrMsg();
									}
								}
							}
						}
					}
				}
				jtvwPackageInfos.add(jtInfo);
			}
			res.setJtvwPackageInfos(jtvwPackageInfos);
		} catch (Exception e) {
			logger.info(e);
		}
		return res;
	}
}
