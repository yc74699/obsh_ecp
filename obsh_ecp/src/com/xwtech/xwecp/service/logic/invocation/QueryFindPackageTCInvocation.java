package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;  
import java.util.HashSet; 
import java.util.List; 
import java.util.Set;
import java.util.regex.Pattern;
 
import org.apache.log4j.Logger;
import org.jdom.Element; 
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp; 
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IYingXingCardDao;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService; 
import com.xwtech.xwecp.service.config.ServiceConfig;  
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;  
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result; 
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext; 
import com.xwtech.xwecp.util.CommonUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 用户套餐信息查询 （进行20元目标库查询过滤）
 * @author YangXQ
 * 2014-07-29
 */
public class QueryFindPackageTCInvocation extends BaseInvocation implements ILogicalService {
		
	private static final Logger logger = Logger.getLogger(QueryFindPackageTCInvocation.class);	 
	
	//迎新号码DAO
	private IYingXingCardDao yingXingDao;
	
	//默认目标库里没有，为false
	private boolean Flag = false;
	
	/**存放20元流量封顶的产品编码*/
	private Set<String> maxCode;
	
	private ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
	
	public QueryFindPackageTCInvocation()
	{	
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		if(null == maxCode)
		{
			maxCode = new HashSet<String>();
			maxCode.add("4052");
			maxCode.add("1277");
			maxCode.add("5652");
			maxCode.add("4051");
			maxCode.add("4825");
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> param)
	{
		QRY020001Result ret = new QRY020001Result();  
	    /** 判断 封顶目标库有无当前号码  */	
		String phone = (String)getParameters(param,"phoneNum");
		String city  = (String)getParameters(param,"context_ddr_city");	
		try {
			// 根据当前地市，查出流量封顶的目标库 
			yingXingDao = (IYingXingCardDao) (springCtx.getBean("yingXingDao"));

			List<String> phoneList = yingXingDao.getFluxFD(city);
			
			// 查询目标库是否有当前这个手机号码
			this.Flag =CommonUtil.secondSearch(phoneList,Long.valueOf(phone));
			
			// 若目标库里没有这个号码，即当前用户未开通
			if (!this.Flag)
			{
				ret.setResultCode(LOGIC_ERROR);
				ret.setErrorCode("-1"); 
				ret.setErrorMessage("当前号码不在目标库中");
				return ret;
			}
		} catch (DAOException e) { 
			e.printStackTrace();
		}
	
		
		/** 
		 * 确认目标库有当前号码后执行下面代码  
		*/	
		GommonBusiness dt = null;
		List<GommonBusiness> reList = new ArrayList<GommonBusiness>();
		String bizId = (String)getParameters(param,"bizId");;
				
		/** 获取详细套餐 */	
		List<GommonBusiness> gbLis = new ArrayList<GommonBusiness>();	
		try{ 			
			String reqXml = "";
			String rspXml = ""; 
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", param);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(  new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(param)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String resp_result = root.getChild("response").getChildText("resp_result"); 
					String resp_code   = root.getChild("response").getChildText("resp_code");
					String resp_desc   = root.getChild("response").getChildText("resp_desc");
					ret.setResultCode(resp_result);
					ret.setErrorCode(resp_code); 
					ret.setErrorMessage(resp_desc);
					
					if ("0000".equals(resp_code))
					{
						List<?> package_code = root.getChild("content").getChildren("package_code");
						if (null != package_code && package_code.size() > 0)
						{
							gbLis = new ArrayList<GommonBusiness>();
							for (int i = 0; i < package_code.size(); i++)
							{
								dt=new GommonBusiness();
								Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");								
								dt.setId(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
								dt.setName(p.matcher(cplanpackagedt.getChildText("package_name")).replaceAll(""));								
								if(p.matcher(cplanpackagedt.getChildText("package_state")).replaceAll("")!=null &&
										!p.matcher(cplanpackagedt.getChildText("package_state")).replaceAll("").equals("")){
									dt.setState(Integer.parseInt(p.matcher(cplanpackagedt.getChildText("package_state")).replaceAll("")));
								}
								
								dt.setBeginDate(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll(""));
								dt.setEndDate(p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll(""));
								dt.setReserve1(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
								dt.setReserve2(p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll(""));
								gbLis.add(dt);
							}
						}	
					}			
				}
			}	
			
			/** 将gbLis 里不是20元流量封顶的产品编码的套餐删掉，只剩下20元封顶  */
			List<GommonBusiness> gbLis2 = new ArrayList<GommonBusiness>();	
			if(null != gbLis && gbLis.size() > 0)
			{
				for(GommonBusiness gb:gbLis)
				{
					String code = gb.getId();
					if(maxCode.contains(code) || maxCode.contains(code.substring(code.length()-4, code.length())))
					{
						gbLis2.add(gb);
					}
				}
			}
			
			/** 若gbLis为空，即无20元流量套餐编码*/
			if(null == gbLis2 || gbLis2.size()== 0)
			{
				dt = new GommonBusiness ();
				dt.setId(bizId);  //业务编码
				dt.setName("");   //业务名称
				dt.setState(1);   //状态：未开通
				dt.setReserve1("");
				reList.add(dt); 
				ret.setGommonBusiness(reList);
				ret.setErrorMessage("未开通20元封顶业务");
			}else{	
				/** 判断状态state标识 */
				if (gbLis2 != null && gbLis2.size() > 0)
				{
					for (GommonBusiness gb : gbLis2)
					{
						// 未开通
						if (null == gb.getBeginDate()||"".equals(gb.getBeginDate()))
						{
							gb.setState(1);
						}	
						else if (null != gb.getBeginDate()){
							// 已开通
							if (Double.parseDouble(gb.getBeginDate())< Double.parseDouble(DateTimeUtil.getFirstdayOfNextMonth()))
							{
								gb.setState(2);
							}
							// 预约开通
							else   
							{
								gb.setState(3);
							}
						}
						// 预约关闭
						if (null != gb.getEndDate() && !"".equals(gb.getEndDate()))
						{
							gb.setState(4);
						}
					}
				}
				ret.setGommonBusiness(gbLis2);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return ret;
	}
}
	
	
