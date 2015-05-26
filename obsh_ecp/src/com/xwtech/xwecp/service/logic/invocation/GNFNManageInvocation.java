package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.FamMemUserProductId;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040047Result;
import com.xwtech.xwecp.service.logic.pojo.ProdInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;

/**
 * 国内亲情号码2012版
 * @author taogang
 *
 */
public class GNFNManageInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(GNFNManageInvocation.class);
	
	private IPackageChangeDAO packageChangeDAO;
	
	private int opertype;
	public GNFNManageInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));
		opertype = 0;
	}
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params) {
		DEL040047Result res = new DEL040047Result();
		
		operMethod(accessId,config,params,res);
		
		return res;
	}
	
	private void operMethod(String accessId,ServiceConfig config, List<RequestParameter> params,DEL040047Result res)
	{
		String operType = (String) getParameters(params,"operType");
		opertype = Integer.parseInt(operType);
		
		setMainProdId(params);
		
		switch(opertype)
		{
			// 查询亲情号码
			case 0:
				if(checkUserProductId(accessId,config,params,res))
				{
					queryFamMem(accessId,config,params,res);
				}
				break;
			// 添加亲情号码
			case 1:
				if(checkUserProductId(accessId,config,params,res))
				{
					operFamMem(accessId,config,params,res);
				}
				break;
			case 2:
				operFamMem(accessId,config,params,res);
				break;
			case 3:
				operFamMem(accessId,config,params,res);
				break;
		}
	}
	/**
	 * 查询已经办理的亲情号码套餐的情况
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private void queryFamMem(String accessId,ServiceConfig config, List<RequestParameter> params,DEL040047Result res)
	{
		Element root = sendXml("cc_cufamilyMemberqey",params,accessId,res);
		if(null != root)
		{
			Element prodinfo = root.getChild("content").getChild("prodinfo");
			if(null != prodinfo)
			{
				List<ProdInfo> prodInfos = new ArrayList<ProdInfo>();
				String[] arr = parseAppendAttr(prodinfo.getChildText("append_attr"));
				for(int i = 0;i<arr.length;i++)
				{
					ProdInfo prodInfo = new ProdInfo();
					prodInfo.setEndDate(prodinfo.getChildText("enddate"));
					prodInfo.setProdId(prodinfo.getChildText("prodid"));
					prodInfo.setProdName(prodinfo.getChildText("prodname"));
					prodInfo.setStartDate(prodinfo.getChildText("startdate"));
					prodInfo.setSubMsisdn(arr[i]);
					prodInfos.add(prodInfo);
				}
				res.setProdInfos(prodInfos);
//				checkUserProductId(accessId,config,params,res);
			}
		}
	}
	
	/**
	 * 判断用户的主体产品能不能办理亲情号码
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private boolean checkUserProductId(String accessId,ServiceConfig config, List<RequestParameter> params,DEL040047Result res)
	{
		String region = (String)getParameters(params,"context_ddr_city");
		if("11".equals(region))
		{
			return true;
		}
		try {
			List<FamMemUserProductId> famMemUserProList = this.packageChangeDAO.getOperFamilyMainProdId(region);
			Element root = sendXml("cc_cgetproinfo_345",params,accessId,res);
			if(null != root)
			{
				String mainProdId = root.getChild("content").getChild("userproductinfo_product_info_id").getChild("cuserproductinfodt").getChildText("userproductinfo_product_id");
				for(int i = 0;i < famMemUserProList.size();i++)
				{
					if(famMemUserProList.get(i).getMainProdId().equals(mainProdId))
					{
						return true;
					}
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		res.setResultCode("-5200");
		res.setErrorCode("-5200");
		res.setErrorMessage("用户的主体产品不能办理亲情号码");
		return false;
	}
	/**
	 * 组装报文里面的参数
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private void operFamMem(String accessId,ServiceConfig config, List<RequestParameter> params,DEL040047Result res)
	{
		String phoneNum = (String)getParameters(params,"subPhoneNum");
		String flag = (String)getParameters(params,"flag");
		String type = (String)getParameters(params,"type");
		String[] phoneNums = phoneNum.split(",");
		
		String operType = returnOperType(flag);
		String subMsisdn = returnSubMsisdn(flag,phoneNums,operType,type);
		
		setParameter(params, "operType", operType);
		setParameter(params, "subMsisdn", subMsisdn);

		sendFamMemXML(accessId,config,params,res);
	}
	
	/**
	 * 把亲情号码根据地市套餐编码放入到参数列表里面
	 * @param params
	 */
	private void setMainProdId(List<RequestParameter> params)
	{
		String region = (String)getParameters(params,"context_ddr_city");
		
		if("11".equals(region))
		{
			setParameter(params, "prodid", "2000002155");
		}
		else
		{
			setParameter(params, "prodid", "2000001840");
		}
	}
	
	/**
	 * 接口入参拼接字符串用的
	 * @param flag
	 * @param phoneNums
	 * @param operType
	 * @param type
	 * @return
	 */
	private String returnSubMsisdn(String flag,String[] phoneNums,String operType,String type)
	{
		switch(opertype)
		{
			case 1:
				if("0".equals(flag) && 1 == phoneNums.length)
				{
					return "p100035="+phoneNums[0]+"="+operType+"="+type;
				}
				else if("0".equals(flag) && 2 == phoneNums.length)
				{
					return "p100035="+phoneNums[0]+"="+operType+"="+type+"#p100080="+phoneNums[1]+"="+operType+"="+type;
				}
				else
				{
					return "p100080="+phoneNums[0]+"="+operType+"="+type;
				}
			case 2:
				if("0".equals(flag))
				{
					return "p100035="+phoneNums[0]+"="+operType+"="+type;
				}
				else if("1".equals(flag))
				{
					return "p100080="+phoneNums[0]+"="+operType+"="+type;
				}
				else
				{
					return "p100035="+phoneNums[0]+"="+operType+"="+type+"#p100080="+phoneNums[1]+"="+operType+"="+type;
				}
			case 3:
				return "p100035="+phoneNums[0]+"="+operType+"="+type;
		}
		return null;
	}
	/**
	 * 返回需要传入报文模板的操作方式
	 * @param flag
	 * @return
	 */
	private String returnOperType(String flag)
	{
		switch(opertype)
		{
			case 1:
				if("0".equals(flag))
				{
					return "PCOpRec";
				}
				else
				{
					return "PCOpMod";
				}
			case 2:
				return "PCOpMod";
			case 3:
				return "PCOpDel";
		}
		return null;
	}
	/**
	 * 发送办理亲情号码的报文
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private void sendFamMemXML(String accessId,ServiceConfig config, List<RequestParameter> params,DEL040047Result res)
	{
		Element root = sendXml("cc_cufamilyMemberAdd",params,accessId,res);
	}
	
	/**
	 * 分隔收到的字符串的
	 * @param sub
	 * @return
	 */
	private String[] parseAppendAttr(String sub) 
	{
		String[] subMsisdn = null;
		if(!"".equals(sub))
		{
			subMsisdn = sub.split("#");
		}
		return subMsisdn;
	}
	/**
	 * 组装报文模板，并发送报文
	 * @param interfaceName
	 * @param params
	 * @param accessId
	 * @param res
	 * @return
	 */
	private Element sendXml(String interfaceName,List<RequestParameter> params,String accessId,DEL040047Result res)
	{
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext(interfaceName, params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, interfaceName, this.generateCity(params)));
				Element personalInfo = checkReturnXml(rspXml,res);
				return personalInfo;
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,DEL040047Result res)
	{
		Element root = this.getElement(rspXml.getBytes());
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
		return null;
	}
}