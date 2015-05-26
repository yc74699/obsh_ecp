package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040099Result;
import com.xwtech.xwecp.service.logic.pojo.UserPkgInfo;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 新查询用户开通的套餐（用户显示必选套餐与自选套餐中的可选套餐）
 * @author suns
 *
 */
public class QueryUserPackageInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryUserPackageInvocation.class);
	private static final Map<String, String> kxtcMap = new HashMap<String, String>();
	static
	{
		//增加可选套餐列表 普通
		kxtcMap.put("2000003766","短信包10元");   
		kxtcMap.put("2000003767","短信包20元");  
		kxtcMap.put("2000003768","短信包30元");  
		kxtcMap.put("2000003769","彩信包10元");  
		kxtcMap.put("2000003770","彩信包20元");  
		kxtcMap.put("2000003771","彩信包30元"); 
		/*//4G自选套餐语音包5元本地（校园）	3853
		4G自选套餐语音包9元本地（校园）	3854
		4G自选套餐语音包18元本地（校园）	3855
		4G自选套餐语音包18元（校园）	3856
		4G自选套餐短信包5元（校园）	3864
		4G自选套餐短信包10元（校园）	3865
		4G自选套餐短信包20元（校园）	3866
		4G自选套餐短信包30元（校园）	3867
		4G自选套餐WLAN及宽带10元（校园）	3868
		4G自选套餐WLAN及宽带20元（校园）	3869
		4G自选套餐WLAN及宽带40元（校园）	3870*/
		kxtcMap.put("2000003853","4G自选套餐语音包5元本地（校园）"); 
		kxtcMap.put("2000003854","4G自选套餐语音包9元本地（校园）"); 
		kxtcMap.put("2000003855","4G自选套餐语音包18元本地（校园）"); 
		kxtcMap.put("2000003856","4G自选套餐语音包18元（校园）"); 
		kxtcMap.put("2000003864","4G自选套餐短信包5元（校园）"); 
		kxtcMap.put("2000003865","4G自选套餐短信包10元（校园）"); 
		kxtcMap.put("2000003866","4G自选套餐短信包20元（校园）"); 
		kxtcMap.put("2000003867","4G自选套餐短信包20元（校园）"); 
		kxtcMap.put("2000003868","4G自选套餐WLAN及宽带10元（校园）"); 
		kxtcMap.put("2000003869","4G自选套餐WLAN及宽带20元（校园）"); 
		kxtcMap.put("2000003870","4G自选套餐WLAN及宽带40元（校园）"); 
	}
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY040099Result result = new QRY040099Result();
		// 取用户当前的套餐
		List<UserPkgInfo> pkgInfos = getUserPkgs(accessId, config, params,result);
		//取得用户的产品
		String prodId = getUserProd(accessId, config, params,result);
	
		//取产品下的必选套餐集合与用户套餐，选出用户的必选套餐
		List<UserPkgInfo> pkgInfosNew = getBxtc(accessId, config, params,result,pkgInfos,prodId);
		//通过静态变态取得所有的可选套餐
		List<UserPkgInfo> pkgInfosKxtc = getKxtc(pkgInfos);
		if(0 < pkgInfos.size())   pkgInfosNew.addAll(pkgInfosKxtc);
		result.setUserPkgList(pkgInfosNew);
		return result;
	}
	
	/**
	 * 
	 * @param pkgInfos
	 * @return
	 */
	private List<UserPkgInfo> getKxtc(List<UserPkgInfo> pkgInfos)
	{
		List<UserPkgInfo> kxtcPkgs = new ArrayList<UserPkgInfo>();
		if(null != pkgInfos && 0 < pkgInfos.size())
		{
			for(UserPkgInfo info : pkgInfos)
			{
				String pkgCode = info.getPkgCode();
				if(pkgCode.length() == 4)  pkgCode = "200000"+pkgCode;
				if(kxtcMap.containsKey(pkgCode))
				{
					info.setIsBxtc("0");
					String pkgName = info.getPkgName();
					if(StringUtil.isNull(pkgName)) info.setPkgName(kxtcMap.get(pkgCode));
					kxtcPkgs.add(info);
				}
			}
		}
		return kxtcPkgs;
	}

	/**
	 * 取必选套餐
	 * @param accessId
	 * @param config
	 * @param params
	 * @param result
	 * @param pkgInfos
	 * @param prodId
	 * @return
	 */
	private List<UserPkgInfo> getBxtc(String accessId, ServiceConfig config,
			List<RequestParameter> params, QRY040099Result result,
			List<UserPkgInfo> pkgInfos, String prodId)
	{
		List<UserPkgInfo> infos = new ArrayList<UserPkgInfo>();
		if(!StringUtil.isNull(prodId))
		{
			params.add(new RequestParameter("proId",prodId));
			//取这个产品下的必选套餐
			String reqXml="";
			String rspXml="";
			
			try
			{
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpropackcfg_377", params);
				
				logger.debug(" ====== 查询套餐 ======\n" + reqXml);
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,accessId, "cc_cgetpropackcfg_377", super.generateCity(params)));
		
					if (null != reqXml && !"".equals(reqXml))
					{
						logger.debug(" ====== 返回报文 ======\n" + rspXml);
						Element root = checkReturnXml(rspXml,result);
						if(null != root)
						{
							Document doc = DocumentHelper.parseText(rspXml);
							List<Node> pkgNodes = doc.selectNodes("/operation_out/content/productbasepackagecfg_product_id/productbasepackagecfgdt");
							if(null != pkgNodes && 0 < pkgNodes.size())
							{
								for(Node node : pkgNodes)
								{
									String pkgCode = node.selectSingleNode("productbasepackagecfg_package_code").getText();
									String pkgBagCod = node.selectSingleNode("productbasepackagecfg_pack_id").getText();
									if(!StringUtil.isNull(pkgCode))
									{
										for(UserPkgInfo info : pkgInfos)
										{
											String curPackageCode = info.getPkgCode();
											//比较取出所有的必选套餐
											if(pkgCode.equals(curPackageCode))
											{
												info.setPkgBagCode(pkgBagCod);
												info.setIsBxtc("1");
												infos.add(info);
												break;
											}
											
										}
									}
								}
							}
						}
					}
			}
			catch (Exception e)
			{
				logger.error(e, e);
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		return infos;
	}


	/**
	 * 取得用户的产品信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param result
	 * @return
	 */
	private String getUserProd(String accessId, ServiceConfig config,
			List<RequestParameter> params, QRY040099Result result)
	{
		String reqXml = "";
		String rspXml = "";
		String prodId = "";
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);
			
			logger.debug(" ====== 查询套餐 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,accessId, "cc_cgetproinfo_345", super.generateCity(params)));
	
				if (null != reqXml && !"".equals(reqXml))
				{
					logger.debug(" ====== 返回报文 ======\n" + rspXml);
					Element root = checkReturnXml(rspXml,result);
					if(null != root)
					{
						Document doc = DocumentHelper.parseText(rspXml);
						Node prodNode = doc.selectSingleNode("/operation_out/content/userproductinfo_product_info_id/cuserproductinfodt/userproductinfo_product_id");
						if(null != prodNode)
						{
							prodId=prodNode.getText();
							result.setProdId(StringUtil.convertNull(prodId));
						}
					}
				}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			e.printStackTrace();
		}
		return prodId;
	}

	private List<UserPkgInfo> getUserPkgs(String accessId,ServiceConfig config, List<RequestParameter> params,
			                              QRY040099Result result)
	{
		String reqXml = "";
		String rspXml = "";
		List<UserPkgInfo> retList = new ArrayList<UserPkgInfo>();
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
			
			logger.debug(" ====== 查询套餐 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,accessId, "cc_find_package_62_TC", super.generateCity(params)));
	
				if (null != reqXml && !"".equals(reqXml))
				{

					logger.debug(" ====== 返回报文 ======\n" + rspXml);
					Element root = checkReturnXml(rspXml,result);
					if(null != root)
					{
						Document doc = DocumentHelper.parseText(rspXml);
						List<Node> gprsNodes = doc.selectNodes("/operation_out/content/package_code/cplanpackagedt");
						if(null != gprsNodes && gprsNodes.size()>0)
						{
							for(Node node : gprsNodes)
							{
								UserPkgInfo info = new UserPkgInfo();
								info.setEndDate(StringUtil.convertNull(node.selectSingleNode("package_end_date").getText()));
								info.setStartDate(StringUtil.convertNull(node.selectSingleNode("package_use_date").getText()));
								info.setPkgName(StringUtil.convertNull(node.selectSingleNode("package_name").getText()));
								info.setPkgCode(StringUtil.convertNull(node.selectSingleNode("package_code").getText()));
								retList.add(info);
							}
								
							
						}
					}
				}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			e.printStackTrace();
		}
		return retList;
	}
	
		/**
		 * 检查返回的xml是否为空并且返回结果是否为成功
		 * @param rspXml
		 * @param res
		 * @return Element
		 */
		private Element checkReturnXml(String rspXml,QRY040099Result res)
		{
			if(null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				if(null != root)
				{			
					Element response = root.getChild("response");
					if(null != response)
					{
						String resp_code = response.getChildText("resp_code");
						String resp_desc = response.getChildText("resp_desc");
						
						String resultCode = BOSS_SUCCESS.equals(resp_code) || "0".equals(resp_code)? LOGIC_SUCESS : LOGIC_ERROR;
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
