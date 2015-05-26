package com.xwtech.xwecp.service.logic.invocation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.FluxDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040107Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.StringUtil;
/**
 * 流量账单-流量套餐使用明细(池化)
 * @author wangh
 *
 */
public class QueryFluxBillDetailCHInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryFluxBillDetailInvocation.class);
	//二十元封顶套餐系列
	private  List<String>  list20YWD ;
	public QueryFluxBillDetailCHInvocation()
	{
		list20YWD = new ArrayList<String>();
		//写死20元封顶套餐
		list20YWD.add("2000004052");
		list20YWD.add("2000001277");
		list20YWD.add("2000005652");
		list20YWD.add("2000004051");
		list20YWD.add("2000004825");
	}
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		// TODO Auto-generated method stub
		QRY040107Result result = new QRY040107Result();
	
		List<FluxDetail> fluxDetailList = getFluxDetail(params,accessId,result);
		List<FluxDetail> wlanDetailList = addWlan(params, accessId, result);
		
		if(null != wlanDetailList && 0 < wlanDetailList.size())
		{
			//设置WLAN的名称
			setWlanNames(wlanDetailList,fluxDetailList,params,accessId,result);
			
			fluxDetailList.addAll(wlanDetailList);
		}
		//化简流量时长，如果是按流量算化成M,如果是时长化成小时
		/**
		if(null != fluxDetailList && 0 < fluxDetailList.size() )
		{
			for(int i=0 ; i < fluxDetailList.size(); i++)
			{
				FluxDetail detail = fluxDetailList.get(i);
				BigDecimal total = new BigDecimal(detail.getTotal());
				BigDecimal used = new BigDecimal(detail.getUsed());
				String rateType  = detail.getRateType();
				//按流量
				if("1".equals(rateType))
				{
					total=total.divide(new BigDecimal(1024),2,BigDecimal.ROUND_DOWN);
					detail.setTotal((total)+"");
					used=used.divide(new BigDecimal(1024),2,BigDecimal.ROUND_DOWN);
					detail.setUsed((used)+"");
				}
				//按时长
				if("2".equals(rateType))
				{
					total=total.divide(new BigDecimal(60),2,BigDecimal.ROUND_DOWN);
					detail.setTotal((total)+"");
					used=used.divide(new BigDecimal(60),2,BigDecimal.ROUND_DOWN);
					detail.setUsed((used)+"");
				}
			}
		}*/
		result.setFluxDetailList(fluxDetailList); 
		return result;
	}

	/**
	 * 设置WLAN名称
	 */
	private void setWlanNames(List<FluxDetail> wlanDetailList,
			List<FluxDetail> fluxDetailList,List<RequestParameter> params,String accessId,QRY040107Result result) {
		//先查cc_gprs4gAll中有没有名字
		int i = 0;
		List<Integer> list = new ArrayList<Integer>();
		if(null != fluxDetailList && 0 < fluxDetailList.size())
		{
			for(FluxDetail wlan : wlanDetailList)
			{
				String fluxId = wlan.getPkgId();
				for(FluxDetail detail : fluxDetailList)
				{
					String wlanId = detail.getPkgId();
					if(fluxId.equals(wlanId))
					{
						wlan.setFluxPkgName(detail.getFluxPkgName()+" WLAN");
						list.add(i);
					}
				}
				i++;
			}
		}
		//如果有些名字没在flux中没找到，查询cc_find_package寻找名称
		if(list.size() < wlanDetailList.size())
		{
			//如果list中有已经取到的名字，删除实例
			if(0 < list.size())
			{
				for(int ii=0;ii < list.size();ii++)
				{
					wlanDetailList.remove(list.get(ii));
				}
				//查询剩下的赋名字
				findLeftWlanNames(wlanDetailList,params,accessId,result);
			}
			if(0 == list.size())
				findLeftWlanNames(wlanDetailList,params,accessId,result);
		}
	}

	private void findLeftWlanNames(List<FluxDetail> wlanDetailList,
			List<RequestParameter> params, String accessId,
			QRY040107Result res) {
		// TODO Auto-generated method stub
		String reqXml = "";
		String rspXml = "";
		setParameter(params, "biz_pkg_qry_scope", "1");// 查询方式   在用和预约套餐
		setParameter(params, "package_type", "0");
		
		reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_YYTC", params);
		logger.debug(" ====== 查询用户所办理的个人套餐 ======\n" + reqXml);
		try {
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_YYTC", super.generateCity(params)));

				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/package_code/cplanpackagedt");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						for(FluxDetail detail : wlanDetailList)
						{
							String wlanId = detail.getPkgId();
							for(Node node : freeItemNodes)
							{
								String nodeId = node.selectSingleNode("package_code").getText().trim();
								if(wlanId.indexOf(nodeId) > -1)
								{
									String nodeName = node.selectSingleNode("package_code").getText().trim();
									detail.setFluxPkgName(nodeName+ " WLAN");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<FluxDetail> getFluxDetail(List<RequestParameter> params,
			String accessId,QRY040107Result res) {
		
		List<FluxDetail> details = new ArrayList<FluxDetail>();
		
		String reqXml = "";
		String rspXml = "";
		setParameter(params, "gprstype", "3");
		RequestParameter parameter = getParameter(params, "month");
		if(null != parameter)
		{
			String month = (String) parameter.getParameterValue();
			setParameter(params, "cycle", month);
		}
		//RequestParameter parameter = getParameter(params, "context_user_id");
		reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsallpkgflux_ch", params);
		logger.debug(" ====== 查询用户所办理的流量套餐 ======\n" + reqXml);
		try {
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_4gqrygprsallpkgflux_ch", super.generateCity(params)));

				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> gprsNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
					if(null != gprsNodes && gprsNodes.size()>0)
					{
						
						details = generateFluxDetails(gprsNodes,details,res,params,accessId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		return details;
	}
	/**
	 * 查询不规则流量GPRS
	 * @param  
	 * @return 
	 */
//	private  List<String> getAnomalyGprs() {
//		return this.wellFormedDAO.getAnomalyGprs();
//	}
	
	/**
	 * 封装成流量节点
	 * @param gprsNodes
	 * @return
	 */

	private List<FluxDetail> generateFluxDetails(List<Node> gprsNodes,List<FluxDetail> details,QRY040107Result res,List<RequestParameter> params
													,String accessId) {
		
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		// 根据当前地市，查出流量封顶的目标库 
//    	IYingXingCardDao yingXingDao = (IYingXingCardDao) springCtx.getBean("yingXingDao");
    	String phone = (String)getParameters(params,"context_login_msisdn");
    	String city  = (String)getParameters(params,"context_ddr_city");
//    	try
//    	{
//    		List<String> phoneList = yingXingDao.getFluxFD(city);
//    		is20YFD =CommonUtil.secondSearch(phoneList,Long.valueOf(phone));
//    	}
//    	catch (DAOException e)
//    	{
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
    	boolean breakNeed = false;
    	boolean is20YFD = false;
//    	List<String> anomalylist  = this.getAnomalyGprs();
		int gprs_cumulate_type =0; // GPRS累计类型  0： 按月累计    1： 跨月累计
		int begin_date = 0;        // 累计量开始时间
		int end_date = 0 ;         // 累计量截止时间
		for(Node node : gprsNodes)
		{
			
			String pkgId = StringUtil.convertNull(node.selectSingleNode("gprs_product_id").getText().trim());
			//去除不规则流量GPRS 
			
			if(4 == pkgId.length())
			{
				pkgId = "200000" + pkgId;
			}
			//把20元封顶单独显示
			if(list20YWD.contains(pkgId)) 
			{
				//查询二十元封顶的流量使用
				FluxDetail detail = new FluxDetail();
				getUsedFlux(res,params ,accessId,detail);
				is20YFD = true;
				detail.setIsWlan("0");
				detail.setRateType("1");
				detail.setTotal("");
				details.add(detail);
				breakNeed = true;
			}
			else
			{
    //			if( null != anomalylist && anomalylist.contains(pkgId) )  continue;
    			int num = Integer.parseInt(StringUtil.convertNull(node.selectSingleNode("gprs_product_num").getText().trim()));
    			int total_int = Integer.parseInt(StringUtil.convertNull(node.selectSingleNode("gprs_cumulate_value").getText().trim()));
    			int total = Integer.parseInt(StringUtil.convertNull(node.selectSingleNode("gprs_max_value").getText().trim()));
    			/**跨月套餐使用流量查询，增加3个出参：类型:0当月，1跨月，开始时间，结束时间*/
				String gprs_cumulate_typeStr  =  node.valueOf("gprs_cumulate_type");
				String begin_dateStr          =  node.valueOf("begin_date");
				String end_dateStr            =  node.valueOf("end_date"); 
				if(null != gprs_cumulate_typeStr && !"".equals(gprs_cumulate_typeStr)){
					gprs_cumulate_type = Integer.valueOf(gprs_cumulate_typeStr);
				}
				if(null != begin_dateStr && !"".equals(begin_dateStr)){
					begin_date = Integer.valueOf(begin_dateStr);	
				}
				if(null != end_dateStr && !"".equals(end_dateStr)){
					end_date = Integer.valueOf(end_dateStr);
				}	    			
				
				int need_int = total_int/total + 1;
    			for(int i=1;i<=num;i++)
    			{
    				    FluxDetail detail = new FluxDetail();
    				
    					//套餐总量 按流量：单位K 按时长：单位秒
    					detail.setTotal(StringUtil.convertNull(node.selectSingleNode("gprs_max_value").getText().trim()));
    					//套餐使用量 按流量：单位K 按时长：单位秒
    //					detail.setUsed(StringUtil.convertNull(node.selectSingleNode("gprs_cumulate_value").getText().trim()));
    //					int total = Integer.parseInt(StringUtil.convertNull(node.selectSingleNode("gprs_max_value").getText().trim()));
    //					if(i <= need_int && i==1)
    //					{
    //						detail.setUsed(StringUtil.convertNull(node.selectSingleNode("gprs_cumulate_value").getText().trim()));
    //					}
    //					else
    						
    					if(i < need_int)
    					{
    						detail.setUsed(StringUtil.convertNull(node.selectSingleNode("gprs_max_value").getText().trim()));
    					}
    					else if(i==need_int && i==1)
    					{
    						detail.setUsed(StringUtil.convertNull(node.selectSingleNode("gprs_cumulate_value").getText().trim()));
    
    					}
    					else if(i==need_int && i > 1)
    					{
    						detail.setUsed((total_int-(need_int-1) * total) + "");
    					}
    					else
    					{
    						detail.setUsed("0");
    					}
    					detail.setIsWlan("0");
    				//1按流量、2按时长
    				detail.setRateType(StringUtil.convertNull(node.selectSingleNode("gprs_rate_type").getText().trim()));
    				//设置流量类型1半包套餐、2全包套餐
    				detail.setPkgType(StringUtil.convertNull(node.selectSingleNode("gprs_product_type").getText().trim()));
    				String name = node.selectSingleNode("gprs_product_name").getText().trim();
    				detail.setFluxPkgName(StringUtil.convertNull(name));
    				detail.setPkgId(pkgId);
    				detail.setGprs_cumulate_type(gprs_cumulate_type);
    				detail.setEnd_date(end_date);
    				detail.setBegin_date(begin_date);
    				details.add(detail);
    				if(breakNeed) break;
    			}
			}
		
		}
		if(is20YFD && 1 < details.size())
		{
			for(FluxDetail detail : details)
			{
				String pkgId = detail.getPkgId();
				if(!list20YWD.contains(pkgId)) details.remove(detail);
			}
		}
		
		return details;
	}

	/**
	 * 
	 * 获取20元封顶的月流量使用情况
	 * */
	private void getUsedFlux(QRY040107Result res,List<RequestParameter> params ,String accessId,FluxDetail detail)
	{
		setParameter(params, "dayNumber", "31");
		setParameter(params, "idType", "0");
		String date = (String)getParameters(params,"month");
		setParameter(params, "date",date);
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		try{
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetgprsflux_717", params);
			logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetgprsflux_717", super.generateCity(params)));
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
				if (null != rspXml && !"".equals(rspXml)) 
				{
					root = this.getElement(rspXml.getBytes());
					String resp_desc ="";
					if(null != root)
					{
						resp_code = root.getChild("response").getChildText("resp_code");
						resp_desc = root.getChild("response").getChildText("resp_desc");
					}
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;

					if (LOGIC_SUCESS.equals(resultCode))
					{
						String errCode = root.getChild("content").getChildText("error_code");
						String useFlux = root.getChild("content").getChildText("gprsbill_total_fee");
						String tempFlux = formatGPRSDetail(useFlux);
						res.setResultCode(resultCode);
						res.setErrorCode(errCode);
						res.setErrorMessage(resp_desc);
						//单位KB
						detail.setUsed(tempFlux);
						return;
					}
					else
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e,e);
		}
	}
	private String formatGPRSDetail(String gprsFlux)
	{
		double flux = Double.parseDouble((String) gprsFlux);
		DecimalFormat df = new DecimalFormat("#0.00");
		gprsFlux = df.format(flux / 1024);
		
		return gprsFlux;
	}
	
	/**
	 * 增加WLAN
	 * @param params
	 * @param accessId
	 * @param res
	 * @param month
	 * @return
	 */
	private List<FluxDetail>  addWlan(List<RequestParameter> params,String accessId,QRY040107Result res)
	{
		setParameter(params, "a_package_id", "0");
		RequestParameter parameter = getParameter(params, "month");
		if(null != parameter)
		{
			String month = (String) parameter.getParameterValue();
			setParameter(params, "dbi_month", month);
		}
		RequestParameter mobilePara = getParameter(params, "context_login_msisdn");
		if(null != parameter)
		{
			String phone = (String) mobilePara.getParameterValue();
			setParameter(params, "phoneNum", phone);
		}
		List<FluxDetail> details = null;
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_ch", params);
			logger.debug(" ====== 查询用户套餐使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "ac_agetfreeitem_ch", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/freeitem_dt");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						details = setWlanDetail(freeItemNodes);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return details;
	}
	
	/**
	 * 取WLAN节点
	 * @param freeItemNodes
	 * @return
	 */
	private List<FluxDetail> setWlanDetail(List<Node> freeItemNodes) {
		List<FluxDetail> details = new ArrayList<FluxDetail>();
		for(Node node : freeItemNodes)
		{
			String wlanType = node.selectSingleNode("a_freeitem_id").getText().trim();
			if("4".equals(wlanType))
			{
				FluxDetail detail = new FluxDetail();
				String pkgId = StringUtil.convertNull(node.selectSingleNode("a_package_id").getText().trim());
				if(4 == pkgId.length())
				{
					pkgId = "200000" + pkgId;
				}
				detail.setPkgId(pkgId);
				String total = StringUtil.convertNull(node.selectSingleNode("a_freeitem_total_value").getText().trim());
				String used = StringUtil.convertNull(node.selectSingleNode("a_freeitem_value").getText().trim());
				//String isWlan = node.selectSingleNode("a_package_id").getText().trim();
				String isWlan = "1";
				String fluxType = StringUtil.convertNull(node.selectSingleNode("package_show_type").getText().trim());
				//查出的4为以流量为计算单位，以8为时长单位
				if("4".equals(fluxType)) fluxType= "1"; 
				if("2".equals(fluxType))  fluxType= "2";
				//detail.setFluxPkgName("WLAN");
				detail.setIsWlan(isWlan);
				detail.setRateType(fluxType);
				detail.setPkgType("0");// 无半包全包说法
				detail.setTotal(total);
				detail.setUsed(used);
				details.add(detail);
			}
		}
		return details;
		
	}

	
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040107Result res)
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
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
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
