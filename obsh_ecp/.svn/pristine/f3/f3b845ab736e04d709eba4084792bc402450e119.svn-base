package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.FluxDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040072Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.StringUtil;

/**
 *  查询用户4G套餐上月使用情况：上个月开通了多少流量，还剩下多少流量。
 *  YangXQ 
 *  2014-6-13
 */
public class Query4GPackageUsageInvocation extends BaseInvocation implements ILogicalService
{

	private static final Logger logger = Logger.getLogger(QueryFluxBillDetailInvocation.class);
	private Map<String,String> map_GPRS=null;
	public Query4GPackageUsageInvocation()
	{
		/*
		 * 
		GPRS4G_5Y	4G上网流量套餐5元通用流量包30M   2000007048
		GPRS4G_10Y	4G上网流量套餐10元通用流量包70M   2000007049
		GPRS4G_20Y	4G上网流量套餐20元通用流量包150M   2000007050
		GPRS4G_KX_30Y	4G上网流量套餐30元通用流量包500M    2000003877
		GPRS4G_KX_40Y	4G上网流量套餐40元通用流量包700M   2000003809
		GPRS4G_50Y	4G上网流量套餐50元通用流量包1G   2000003779 
		GPRS4G_70Y	4G上网流量套餐70元通用流量包2G   2000003780 
		GPRS4G_100Y    	4G上网流量套餐100元通用流量包3G   2000003781
		GPRS4G_130Y	 4G上网流量套餐130元通用流量包4G   2000003782
		GPRS4G_180Y	 4G上网流量套餐180元通用流量包6G   2000003783
		GPRS4G_280Y	 4G上网流量套餐280元通用流量包11G    2000003815
		 */
		if(null == map_GPRS)
		{
			map_GPRS = new HashMap<String, String>();
			map_GPRS.put("2000007048","2000007048");
			map_GPRS.put("2000007049","2000007049");
			map_GPRS.put("2000007050","2000007050");
			map_GPRS.put("2000003877","2000003877");
			map_GPRS.put("2000003809","2000003809");
			map_GPRS.put("2000003779","2000003779");
			map_GPRS.put("2000003780","2000003780");
			map_GPRS.put("2000003781","2000003781");
			map_GPRS.put("2000003782","2000003782");
			map_GPRS.put("2000003783","2000003783");
			map_GPRS.put("2000003815","2000003815");
		}
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params)
	{
		QRY040072Result result = new QRY040072Result();
		List<FluxDetail> fluxDetailList = getFluxDetail(params,accessId,result);		
		result.setFluxDetailList(fluxDetailList); 
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<FluxDetail> getFluxDetail(List<RequestParameter> params,
			String accessId,QRY040072Result res) {
		String reqXml = "";
		String rspXml = "";
		
		//根据“手机号码”查询QRY040001,获取用户信息的userid,地市信息	
		reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params);		
		try {
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", super.generateCity(params)));
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> user = doc.selectNodes("/operation_out/content/user_info");
					if(null != user && user.size()>0)
					{
						for(Node node : user)
						{	
							// 向当前报文里，添加地市和userid
							String user_city = node.selectSingleNode("user_city").getText().trim();
							String user_id = node.selectSingleNode("user_id").getText().trim();	
							setParameter(params, "context_route_type", "1"); 
							setParameter(params, "context_route_value", user_city); 
							setParameter(params, "context_ddr_city",user_city); 
							setParameter(params, "context_user_id", user_id); 
							setParameter(params, "context_login_msisdn", user_city); 
							setParameter(params, "fixed_ddr_city",user_city); 	
						}		
					}else{
						return null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setParameter(params, "gprstype", "3");
	
		// 获取当前月的上月年份，格式YYYYMM
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);  //获取年份
		int month =ca.get(Calendar.MONTH);//获取上個月份 
		if (month==0){
			 year=year-1;
			 month=12;
		}
		String time=null;
		if(month<10){
		     time=year+"0"+month;
		}else{
			 time=year+""+month;
		 }
		setParameter(params, "cycle", time); 

		List<FluxDetail> details = new ArrayList<FluxDetail>();
		reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsallpkgflux_70", params);
		logger.debug(" ====== 查询用户所办理的流量套餐 ======\n" + reqXml);
		try {
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_4gqrygprsallpkgflux_70", super.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> gprsNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
					if(null != gprsNodes && gprsNodes.size()>0)
					{
						details = generateFluxDetails(gprsNodes,details);
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
	 * 封装成流量节点
	 * @param gprsNodes
	 * @return
	 */
	private List<FluxDetail> generateFluxDetails(List<Node> gprsNodes,List<FluxDetail> details) {

		for(Node node : gprsNodes)
		{
			FluxDetail detail = new FluxDetail();
			String name = node.selectSingleNode("gprs_product_name").getText().trim();
			detail.setFluxPkgName(StringUtil.convertNull(name));
			//设置流量类型1半包套餐、2全包套餐
			detail.setPkgType(StringUtil.convertNull(node.selectSingleNode("gprs_product_type").getText().trim()));
			//1按流量、2按时长
			detail.setRateType(StringUtil.convertNull(node.selectSingleNode("gprs_rate_type").getText().trim()));
			//套餐总量 按流量：单位K 按时长：单位秒
			detail.setTotal(StringUtil.convertNull(node.selectSingleNode("gprs_max_value").getText().trim()));
			//套餐使用量 按流量：单位K 按时长：单位秒
			detail.setUsed(StringUtil.convertNull(node.selectSingleNode("gprs_cumulate_value").getText().trim()));
			
			/**跨月套餐使用流量查询，增加3个出参：类型:0当月，1跨月，开始时间，结束时间*/
			int gprs_cumulate_type =0; // GPRS累计类型  0： 按月累计    1： 跨月累计
			int begin_date = 0;        // 累计量开始时间
			int end_date = 0 ;         // 累计量截止时间
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
			
			detail.setBegin_date(begin_date);
			detail.setEnd_date(end_date);
			detail.setGprs_cumulate_type(gprs_cumulate_type);	
			
			detail.setIsWlan("0");
			String pkgId = StringUtil.convertNull(node.selectSingleNode("gprs_product_id").getText().trim());
			if(4 == pkgId.length())
			{
				pkgId = "200000" + pkgId;
			}
			detail.setPkgId(pkgId); 
			// 过滤其他业务，只剩下4G上网流量套餐
			if(null != pkgId && null!=this.map_GPRS.get(pkgId) && this.map_GPRS.get(pkgId).equals(pkgId)){
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
	private Element checkReturnXml(String rspXml,QRY040072Result res)
	{
		Element root=null;
		if (this.getElement(rspXml.getBytes())!=null) {
			root = this.getElement(rspXml.getBytes());
		}
		String resp_code = null;
		String resp_desc = null;
		if (root!=null){
			resp_code = root.getChild("response").getChildText("resp_code");
			resp_desc = root.getChild("response").getChildText("resp_desc");
		}
		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		if( rspXml.getBytes() != null && rspXml.length() != 0)
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
