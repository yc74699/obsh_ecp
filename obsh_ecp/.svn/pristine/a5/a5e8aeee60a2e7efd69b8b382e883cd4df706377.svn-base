package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.II8jtrhDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040020Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 套餐使用情况查询 QRY040020 
 * 
 * @author taogang 2014-9-25 
 */
public class QueryUsedPackageInfoBossInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryUsedPackageInfoInvocation.class);
	/*使用情况的类型和中文对应*/
	private Map<String,String> useDetailMap;
	/*存放需要屏蔽的套餐编码*/
	private Set<String> shieldSet;
	/*存放流量叠加包套餐编码*/
	private Map<String,Integer> fluxPackage;
	/*亲情号码组合的套餐编码*/
	private Set<String> familNum;
	/*给各个渠道看的使用情况的flag已方便他们使用单位*/
	private Map<String, Integer> map;
	/*wlan计算单位是kb的套餐编码*/
//	private Set<String> wlan;
	/*港澳台的GPRS流量漫游套餐*/
	private Set<String> GatGprs;
	//流量季包、半年包GPRS流量查询套餐
	private Set<String> QuaGprs;
	/*4G产品对照表*/
	//private Set<String> LTEGprs;
	//查询I8套餐
	private  II8jtrhDao i8jtrhDao;
	//存放主，副号号码，userId
	private Map<String,String> idMap;
	//I8几种业务
	private Map<String, Integer> i8Shares;
	//放置返回的prodINFO,比如共享分钟总数,itemId
	private Map<String, String> i8prodInfoMap;
	//返回流量情况
	private List<String> i8LLLists;
	//I8中返回的是流量还是时长
	private Map<String,Integer>  i8ItemLLorSC;
	
	public QueryUsedPackageInfoBossInvocation()
	{
		if(null == i8ItemLLorSC)
		{
			i8ItemLLorSC = new HashMap<String, Integer>();
			i8ItemLLorSC.put("701",8);
			i8ItemLLorSC.put("801",8);
			i8ItemLLorSC.put("802",8);
			i8ItemLLorSC.put("806",4);
			i8ItemLLorSC.put("803",4);
			i8ItemLLorSC.put("804",8);
			i8ItemLLorSC.put("796",4);
			i8ItemLLorSC.put("797",8);
			i8ItemLLorSC.put("340",4);
			i8ItemLLorSC.put("2792", 4); //WLAN流量
		}
		if(null == i8LLLists)
		{
			i8LLLists = new ArrayList<String>();
			i8LLLists.add("806");
			i8LLLists.add("796");
			i8LLLists.add("340");
			i8LLLists.add("2792");
		}
		i8prodInfoMap = new HashMap<String, String>();
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		i8jtrhDao = (II8jtrhDao) (springCtx.getBean("i8jtrhDao"));
		if(null == i8Shares)
		{
			i8Shares = new HashMap<String, Integer>();
			//2非共享，1共享
			i8Shares.put("701",2);	//成员间通话免费（分钟）
			i8Shares.put("801",1);	//：成员共享免费语音（分钟）
			i8Shares.put("802",1);	//成员共享免费WLAN（分钟）
			i8Shares.put("803",1);  //成员共享免费省内WLAN&GPRS（分钟）
			i8Shares.put("804",1);  //成员共享免费漫游WLAN&GPRS（分钟）
			i8Shares.put("806",1);  //共享国内移动数据流量
			i8Shares.put("796", 2); //10M赠送流量
			i8Shares.put("797", 2); //WLAN时长
			i8Shares.put("340", 2); //WLAN流量
			i8Shares.put("2792", 2); //WLAN流量
		}

		if(null == idMap)
		{
			idMap = new HashMap<String, String>();
		}
		if (null == this.useDetailMap)
		{
			useDetailMap = new HashMap<String, String>();
			useDetailMap.put("1", "短信");
			useDetailMap.put("2", "彩信");
			useDetailMap.put("3", "移动数据流量");
			useDetailMap.put("4", "WLAN");
			useDetailMap.put("5", "金额");
			useDetailMap.put("6", "ip通话");
			useDetailMap.put("7", "通话时长");
			useDetailMap.put("8", "宽带时长");
			useDetailMap.put("9", "彩铃");
		}

		if(null == shieldSet)
		{
			shieldSet = new HashSet<String>();
			/*全球通生日套餐*/
			shieldSet.add("1849");
		}
		
		if(null == fluxPackage)
		{
			fluxPackage = new HashMap<String,Integer>();
			//存放流量叠加包编码
			fluxPackage.put("2000002505", 0);
			fluxPackage.put("2000002504", 0);
			fluxPackage.put("2000002631", 0);
			fluxPackage.put("2000003020", 0);
			fluxPackage.put("2000003540", 0);
		}
		
		if(null == familNum)
		{
			familNum = new HashSet<String>();
			familNum.add("4778");
			familNum.add("4779");
			familNum.add("4907");
		}
		
		if (null == this.map)
		{
			this.map = new HashMap<String,Integer>();
			this.map.put("337", 9);
			this.map.put("332", 4);
			this.map.put("373", 4);
			this.map.put("390", 7);
			this.map.put("787", 4);
			this.map.put("788", 8);
			this.map.put("610", 5);
			this.map.put("1", 2);
			this.map.put("2", 3);
			this.map.put("3", 4);
			this.map.put("4", 8);
			this.map.put("5", 5);
			this.map.put("6", 10);
			this.map.put("7", 7);
			this.map.put("8", 8);
			this.map.put("9", 6);
		}
		
		if(null == this.GatGprs)
		{
			this.GatGprs = new HashSet<String>();
			/*港澳台GPRS流量漫游*/
			GatGprs.add("3223");
			GatGprs.add("1979");
			GatGprs.add("3221");
			GatGprs.add("3222");
			GatGprs.add("2000003223");
			GatGprs.add("2000001979");
			GatGprs.add("2000003221");
			GatGprs.add("2000003222");
		}
		
		if(null == this.QuaGprs){
			//流量季包、半年包套餐产品编码
			this.QuaGprs = new HashSet<String>();
			QuaGprs.add("2000003748");
			QuaGprs.add("2000003749");
			QuaGprs.add("2000003750");
			QuaGprs.add("2000003751");
			QuaGprs.add("2000003752");
			QuaGprs.add("2000003753");
			
		}
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{

		QRY040020Result res = new QRY040020Result();
		List<PkgInfo> pkgInfoList = new ArrayList<PkgInfo>();
        //查询非流量套餐产品的使用情况(包括I8系列家庭套餐流量和流量季包、半年包套餐流量查询)
		pkgInfoList = queryPersonPackInfo(accessId,config,params,res,pkgInfoList);
		//查询流量套餐功能的产品使用情况(去除I8系列家庭套餐流量和流量季包、半年包套餐流量查询)
		pkgInfoList = queryGprsInfo(accessId,config,params,res,pkgInfoList);
		
		String packCode = isOpenFamilNumService(accessId,config,params,res);
		PkgInfo pkgInfo = null;
		
		if(null != packCode)
		{
			pkgInfo = queryFamilNumServDesc(accessId,config,params,res,packCode);
			if(null != pkgInfo)
			{
				pkgInfo = queryPackageUsage(accessId,config,params,res,pkgInfo);
				pkgInfoList.add(pkgInfo);
			}
			else
			{
				return res;
			}
		}
		res.setPkgInfoList(pkgInfoList);
		return res;
	}
	/**
	 * 把个人套餐和非流量套餐的使用情况的数据整合起来(包括流量季包、半年包)
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<PkgInfo> queryPersonPackInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res,List<PkgInfo> pkgInfoList)
	{	
		//用户所有已办理的套餐结果
		List<Node> userList = queryUserPackage(accessId,config,params,res); 
		
		//流量季包、半年包套餐使用情况结果
		if(null != userList && userList.size() > 0)
		{   
			int userSize = userList.size();

			for(int i = 0;i < userSize;i++)
			{
				Node userNode = userList.get(i);
				
				String packCode = userNode.selectSingleNode("package_code").getText().trim();
				//codeMap.put(packCode, 0);
				if(!this.QuaGprs.contains(packCode))
				{
					continue;
				}
				
				String stateDate = userNode.selectSingleNode("package_use_date").getText().trim();
				PkgInfo pkgInfo = new PkgInfo();
				pkgInfo.setPkgName(userNode.selectSingleNode("package_name").getText().trim());
				pkgInfo.setPkgId(packCode);
				
				List<PkgUsedInfo> subUsedInfoList = new ArrayList<PkgUsedInfo>();
				PkgUsedInfo pkgUsedInfo = new PkgUsedInfo();
				//查询流量季包半年包
				List<Node> quaList = qryPrivilegeLeftcross(accessId,config,params,packCode,stateDate,res);
				
				Node quaNote = quaList.get(0);
				
				int flag = Integer.parseInt(quaNote.selectSingleNode("dataunit").getText().trim());
				String type = quaNote.selectSingleNode("typeid").getText().trim();
				
				pkgUsedInfo.setFlag(flag);
				pkgUsedInfo.setFreeItemId(type);
				pkgUsedInfo.setPkgName(this.useDetailMap.get(type));
				
				Long total = parseData(quaNote.selectSingleNode("alldata").getText().trim(),
						flag);
				Long left = parseData(quaNote.selectSingleNode("leftdata").getText().trim(),
						flag);
				
				pkgUsedInfo.setTotal(total);
				pkgUsedInfo.setRemain(left);
				subUsedInfoList.add(pkgUsedInfo);
				pkgInfo.setSubUsedInfoList(subUsedInfoList);
				
				if(null != pkgInfo)
				{
					pkgInfoList.add(pkgInfo);
				}
			}
		}
		
		List<Node> useredList = queryUserPackageInfo(accessId,config,params,res);
		List<Node> usedListI8 = new ArrayList<Node>();
		try{
			
			if(null != userList && userList.size() > 0)
			{
				int userSize = userList.size();
				
				for(int i = 0;i < userSize;i++)
				{
					Node userNode = userList.get(i);
	
					String packCode = userNode.selectSingleNode("package_code").getText().trim();
	
					if(this.shieldSet.contains(packCode)){
						continue;
					}
					
					/*这个map是为了查看套餐所包含的的使用情况有几种而建立的*/
					Map<String,Integer> codeMap = new HashMap<String,Integer>();
					codeMap.put(packCode, 0);
					if(isI8JTRH(packCode) )
					{
						usedListI8.add(userNode);
						continue;
					}
					PkgInfo pkgInfo = null;
					if(null != useredList && useredList.size() > 0)
					{
						int useredSize = useredList.size();
						for(int j = 0;j < useredSize;j++)
						{
							Node useredNode = useredList.get(j);
							String packId = useredNode.selectSingleNode("a_package_id").getText().trim();
							String showType = useredNode.selectSingleNode("package_show_type").getText().trim();
							String freeitemId = useredNode.selectSingleNode("a_freeitem_id").getText().trim();
							if("3".equals(freeitemId)){
								continue;
							}
							
							List<PkgUsedInfo> subUsedInfoList = null;
							/*检查个人套餐和套餐使用情况是否匹配*/
							
							//查询用户是否有家庭整合套餐
							if(checkPackCode(codeMap,packId))
							{
								
								//非流量套餐使用明细结果
								int codeNum = Integer.valueOf(codeMap.get(packId).toString());
								/*如果发现个人办理的套餐有第一种使用情况的话就为pkgInfo开辟空间，这样做有助于节省内存*/
								if(codeNum == 1)
								{
									pkgInfo = new PkgInfo();
									subUsedInfoList = new ArrayList<PkgUsedInfo>();
									pkgInfo.setPkgName(userNode.selectSingleNode("package_name").getText().trim());
									pkgInfo.setPkgId(packId);
								}
								/*如果一种套餐是多重使用情况subUsedInfoList就需要使用已经有了的*/
								else if(codeNum > 1)
								{
									subUsedInfoList = pkgInfo.getSubUsedInfoList();
								}
								
								if(null != pkgInfo)
								{
									PkgUsedInfo pkgUsedInfo = new PkgUsedInfo();
									String type = useredNode.selectSingleNode("a_freeitem_id").getText().trim();
									pkgUsedInfo.setFreeItemId(type);
									pkgUsedInfo.setPkgName(this.useDetailMap.get(type));
									
									//返回给各个渠道
									int flag = returnType(type,showType);
									
									pkgUsedInfo.setFlag(flag);
									
									Long total = parseData(useredNode.selectSingleNode("a_freeitem_total_value").getText().trim(),
											flag);
									Long used = parseData(useredNode.selectSingleNode("a_freeitem_value").getText().trim(),
											flag);
									
									pkgUsedInfo.setTotal(total);
									pkgUsedInfo.setRemain(total - used);
									subUsedInfoList.add(pkgUsedInfo);
									
									pkgInfo.setSubUsedInfoList(subUsedInfoList);
								}
							}
						}
					}
					
					if(null != pkgInfo)
					{
						pkgInfoList.add(pkgInfo);
					}
				}
				//加载I8系列套餐展现
				if(0 < usedListI8.size())
				{
					List<PkgInfo> list = addI8Tcsyqk(usedListI8,params,accessId,res);
					if(null != list && 0 < list.size())
						pkgInfoList.addAll(list);
				}
			}
			return pkgInfoList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e, e);
		}
		return null;
	}
	private List<PkgInfo> addI8Tcsyqk(List<Node> usedListI8,List<RequestParameter> params,String accessId,QRY040020Result res) {
		List<PkgInfo> pkgInfos = new ArrayList<PkgInfo>();
		for(Node userNode : usedListI8)
		{
			PkgInfo pkgInfo = new PkgInfo();
			String pkgName = userNode.selectSingleNode("package_name").getText().trim();
			if(null != pkgName && !"".equals(pkgName))
			{
				pkgInfo.setPkgName(pkgName);
			}
			String packCode = userNode.selectSingleNode("package_code").getText().trim();
			if(null != packCode && !"".equals(packCode))
			{
				pkgInfo.setPkgId(packCode);
			}
			//查询用户是否是户主,查询户主的通信量
			RequestParameter parameter = getParameter(params, "context_user_id");
			if(null != parameter)
			{
				String value = (String) parameter.getParameterValue();
				//户主查询自己的消耗
				if(!StringUtil.isNull(value))
				{
					//查询主号的通信量
					Map<List<Node>,List<Node>> map =  getHouseHolderUsed(accessId,params,res,value);
					if(0 < map.size())
					{
						for(Entry<List<Node>,List<Node>> entry : map.entrySet())
						{
							List<Node> prodinfos =  entry.getKey();
							if(null != prodinfos && 0 < prodinfos.size())
								setProdInfoMap(prodinfos);
							List<Node> houseHolderNods =  entry.getValue();
							if(null != houseHolderNods && 0 < houseHolderNods.size())
								setHouseHolder(pkgInfo,accessId,params,res,houseHolderNods);
						}
						
						
					}
				
					

				}
				
			}
			pkgInfos.add(pkgInfo);
		}
		
		return pkgInfos;
	}

	private void setProdInfoMap(List<Node> prodinfos) {
		if(null != prodinfos && 0 < prodinfos.size())
		{
			for(Node node : prodinfos)
			{
				String freeitem_id = node.selectSingleNode("freeitem_id").getText().trim();
				String total =  node.selectSingleNode("freeitem_value").getText().trim();
				String pkgName = node.selectSingleNode("freeitem_desc").getText().trim();
				i8prodInfoMap.put(freeitem_id, total+","+pkgName);
			}
		}
	}

	private Map<List<Node>,List<Node>> getHouseHolderUsed( String accessId,
			List<RequestParameter> params, QRY040020Result res,
			String houseHolder) {
		List<Node> procalls = new ArrayList<Node>();
		String reqXml = "";
		String rspXml = "";
		List<Node> prodinfos = new ArrayList<Node>();
		Map<List<Node>,List<Node>> map = new HashMap<List<Node>, List<Node>>();
		try {
		setParameter(params, "qryType", "2");
		setParameter(params, "cycle", DateTimeUtil.getTodayChar6());
		String hostId = idMap.get("hoster_id");
		String userId = houseHolder;
		if(!StringUtil.isNull(hostId)) userId = hostId;
		setParameter(params, "subSid", userId);
		setParameter(params, "qrysubsid", "");
		reqXml = this.bossTeletextUtil.mergeTeletext("ac_acqryspandproduse_309", params);
		logger.debug(" ====== 查询用户所办理的个人套餐 ======\n" + reqXml);
		
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_acqryspandproduse_309", super.generateCity(params)));

				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> nodes = doc.selectNodes("/operation_out/content/prodcallinfo");
					if(null !=nodes && 0 < nodes.size()) 
					{
						procalls.addAll(nodes);
					}

					List<Node> prods = doc.selectNodes("/operation_out/content/prodinfo");
					if(null !=prods && 0 < prods.size()) 
					{
						prodinfos.addAll(prods);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		map.put(prodinfos, procalls);
		return map;
	}

	private void setHouseHolder(PkgInfo pkgInfo, String accessId,
			List<RequestParameter> params, QRY040020Result res,List<Node> procalls) {
		List<PkgUsedInfo> list = new ArrayList<PkgUsedInfo>();
		try
		{
			if(null != procalls && procalls.size()>0)
			{
				long total;
				String userId= "";
				RequestParameter parameter = getParameter(params, "context_user_id");
				if(null != parameter)
				{
					userId = (String) parameter.getParameterValue();
				}
				String vice_id = idMap.get("vice_id");
				userId  = vice_id == null ? userId : vice_id;
				Map<String,PkgUsedInfo> usedMap = new HashMap<String, PkgUsedInfo>();
				List<String> itemIds = new ArrayList<String>();
				List<Long> useds = new ArrayList<Long>();
				for(Node node : procalls)
				{
					//根据freeitem_id整合新的返回类型
					String itemId = node.selectSingleNode("freeitem_id").getText().trim();
					String calltipId = node.selectSingleNode("calltip").getText().trim();
					  //如果是国内漫游（主叫|被叫）不取值
					if("801".equals(itemId) && ("15".equals(calltipId) || "16".equals(calltipId))){
						continue;
					}
					if(null != itemId && !"".equals(itemId))
					{
						int isNeededItem = isNeedItem(itemId);
						//如果是共享的ID,需要加和其它成员
						String user_id = node.selectSingleNode("userid").getText().trim();
						
						if(null != user_id)
						{
							String use = node.selectSingleNode("package_package_value").getText().trim();
							//放到map中,map自动覆盖相同的ITEMID，这儿的小套餐名称与总量都相同
							PkgUsedInfo pkgUsedInfo = new PkgUsedInfo();
							String pkgName = node.selectSingleNode("package_freeitem_name").getText().trim();
							pkgUsedInfo.setPkgName(pkgName);
							pkgUsedInfo.setFreeItemId(itemId);
							pkgUsedInfo.setFlag(setI8Flag(itemId));
							String totalYW = node.selectSingleNode("package_freeitem_value").getText().trim();
							//total = Long.parseLong(totalYW);
							total = setI8LL(itemId, totalYW);
							pkgUsedInfo.setTotal(total);
							long use2 = 0;
							if(null != use && !"".equals(use))
							{
								use2 = setI8LL(itemId, use);
							}
							
							if(1 == isNeededItem)
							{
									itemIds.add(itemId);
									useds.add(use2);
									usedMap.put(itemId,pkgUsedInfo);
							}
							if(user_id.equals(userId))
							{
								if(2 == isNeededItem)
								{//只查出这个用户的不共享的套餐使用(赞时只加这个成员间共享)
									if(null != use && !"".equals(use))
									{
										pkgUsedInfo.setRemain(total-use2);
									}
									list.add(pkgUsedInfo);
								}
							}
						}
					}
				}	
				
				//组装共享的信息
				if(0 < usedMap.size() && 0 < itemIds.size() && 0 < useds.size())
				{
					for(Entry<String,PkgUsedInfo> entry : usedMap.entrySet())
					{
						String item = entry.getKey();
						PkgUsedInfo usedInfo= entry.getValue();
						long totalUse = 0;
						for(int i=0;i<itemIds.size();i++)
						{
							String itemId = itemIds.get(i);
							long usedd = useds.get(i);
							if(itemId.equals(item))
							{
								totalUse += usedd;
							}
						}
						long remain = usedInfo.getTotal()-totalUse;
						//增加判断使用量是否总量，如果大于则剩余量显示为0，而不显示为负数
						if(remain<0){
							remain=0;
							usedInfo.setRemain(remain);
						}else{
							usedInfo.setRemain(remain);
							
						}
						list.add(usedInfo);
					}
				}
				
				
				if(list.size() < i8Shares.size())
				{
					Set<String> set = i8Shares.keySet();
					Set<String> backSet = new HashSet<String>();
					for(PkgUsedInfo info : list)
					{
						backSet.add(info.getFreeItemId());
					}
					if(0 < backSet.size())
					set.removeAll(backSet);
					if(null != set  && set.size() > 0 )
					{
						for(String string : set)
						{
							PkgUsedInfo info= addPkgUsed(string);
							if(null != info)
							list.add(info);
						}
						
					}
				}
			}
			pkgInfo.setSubUsedInfoList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
	}
	private PkgUsedInfo addPkgUsed(String string) {
		// TODO Auto-generated method stub
		
		if(i8prodInfoMap.containsKey(string))
		{
			PkgUsedInfo info = new PkgUsedInfo();
			info.setFlag(setI8Flag(string));
			info.setFreeItemId(string);
			String value = i8prodInfoMap.get(string);
			String[] strings = value.split(",");
			String total = strings[0];
			long time = setI8LL(string, total);
			String name = strings[1];
			info.setTotal(time);
			info.setRemain(time);
			info.setPkgName(name);
			return info;
		}
		return null;
	}

	/**
	 * 需要返回的ITEM id
	 * @param itemId
	 * @param use
	 * @return  1是有共享的情况，无共享的情况
	 */
	private int isNeedItem(String itemId) {
		Integer integer = i8Shares.get(itemId);
		if(null == integer)
		{
			return 0;
		}
		else
		{
			return integer;
		}
	}
	private Map<Integer,Map<List<String>,List<Long>>> hasGXQK(String itemId,long use)
	{
		List<String> itemIds = new ArrayList<String>();
		List<Long> usedDatas = new ArrayList<Long>();
		//增加共享的情况
		List<String> itemIdss = new ArrayList<String>();
		List<Long> usedDatass = new ArrayList<Long>();
		Map<List<String>,List<Long>> innerMap = new HashMap<List<String>, List<Long>>();
		Map<Integer,Map<List<String>,List<Long>>> map = new HashMap<Integer,Map<List<String>,List<Long>>>();
		
		if("701".equals(itemId)  || "801".equals(itemId) || "802".equals(itemId) || "803".equals(itemId) || "804".equals(itemId))
		{
			if("801".equals(itemId) || "802".equals(itemId) || "803".equals(itemId) || "804".equals(itemId))
			{
				itemIdss.add(itemId);
				usedDatass.add(use);
				innerMap.put(itemIdss, usedDatass);
				map.put(0,innerMap);
			}
			else
			{
				itemIdss.add(itemId);
				usedDatass.add(use);
				innerMap.put(itemIds, usedDatas);
				map.put(1,innerMap);
			}
		}
		return map;
	}

	//流量套餐使用情况查询(去除I8系列家庭套餐流量和流量季包、半年包套餐流量查询)
	private List<PkgInfo> queryGprsInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res,List<PkgInfo> pkgInfoList){
		try{
			PkgInfo pkgInfo = null;
			List<PkgUsedInfo> subUsedInfoList = null;
			List<Node> lteList = queryGPRSPackageInfo(accessId, config ,params,res);
			//存放流量套餐数据
			List<PkgInfo> listP = new ArrayList<PkgInfo>();
			Map<String,PkgInfo> mapP =new HashMap<String,PkgInfo>();
			
			for(PkgInfo pkg:pkgInfoList)
			{
				mapP.put(pkg.getPkgId(), pkg);
			}
			if(null != lteList && lteList.size() > 0)
			{
				for(int i=0;i<lteList.size();i++)
				{
					Node lteNode = lteList.get(i);
					String productId = lteNode.selectSingleNode("gprs_product_id").getText().trim();
					//去除I8系列家庭套餐流量查询和季包半年包查询
					if(isI8JTRH(productId) || this.QuaGprs.contains(productId))  continue;
					String rateType = lteNode.selectSingleNode("gprs_rate_type").getText().trim();
					/**跨月套餐使用流量查询，增加3个出参：类型:0当月，1跨月，开始时间，结束时间*/
					int gprs_cumulate_type =0; // GPRS累计类型  0： 按月累计    1： 跨月累计
					int begin_date = 0;        // 累计量开始时间
					int end_date = 0 ;         // 累计量截止时间
					String gprs_cumulate_typeStr  =  lteNode.valueOf("gprs_cumulate_type");
					String begin_dateStr          =  lteNode.valueOf("begin_date");
					String end_dateStr            =  lteNode.valueOf("end_date"); 
					if(null != gprs_cumulate_typeStr && !"".equals(gprs_cumulate_typeStr)){
						gprs_cumulate_type = Integer.valueOf(gprs_cumulate_typeStr);
					}
					if(null != begin_dateStr && !"".equals(begin_dateStr)){
						begin_date = Integer.valueOf(begin_dateStr);	
					}
					if(null != end_dateStr && !"".equals(end_dateStr)){
						end_date = Integer.valueOf(end_dateStr);
					}	
					
					//合并和老接口产品编码一样的流量套餐
			    	Set<String> keys = mapP.keySet();
			    	//新拆流量使用情况
			    	long num = Integer.parseInt(StringUtil.convertNull(lteNode.selectSingleNode("gprs_product_num").getText().trim()));
			    	long total_int = Integer.parseInt(StringUtil.convertNull(lteNode.selectSingleNode("gprs_cumulate_value").getText().trim()));
			    	long total2 = Integer.parseInt(StringUtil.convertNull(lteNode.selectSingleNode("gprs_max_value").getText().trim()));
			    	
			    	long total_max = (total2/num) ;
			    	long need_int = num;
	    			// 修改流量总量为0的情况
	    			if(total2 > 0)
	    			{
	    				need_int = total_int/total_max + 1;
	    			}
			    	long tempFlag = 0;
					if(keys.contains(productId))
					{
						PkgInfo pkg = mapP.get(productId);
						subUsedInfoList = pkg.getSubUsedInfoList();
						
						//把有开重复的  搞分开
						for(int j=1;j<=num;j++)
						{
							PkgUsedInfo pkgUsedInfo= new PkgUsedInfo();
							
							if ("1".equals(rateType))
							{
								pkgUsedInfo.setFlag(4);
								pkgUsedInfo.setFreeItemId("3");
								tempFlag = 4;
							}
							else
							{
								pkgUsedInfo.setFlag(8);
								pkgUsedInfo.setFreeItemId("3");
								tempFlag = 6;
							}
							
							pkgUsedInfo.setPkgName("移动数据流量");
//							Long total = total_max ;
							Long total =parseData(total_max+"",
									(int) tempFlag);
							Long usedd = parseData(lteNode.selectSingleNode("gprs_cumulate_value").getText().trim(),
									(int) tempFlag);
							long used =0l;
							if(j < need_int)
	    					{
								used = total;
	    					}
	    					else if(j==need_int && j==1)
	    					{
	    						used = usedd;
	    					}
	    					else if(j==need_int && j > 1)
	    					{
	    						used =  parseData((total_int-(need_int-1) * total_max)+"",
										(int) tempFlag);
	    					}
	    					else
	    					{
	    						used = 0;
	    					}
							
							pkgUsedInfo.setTotal(total);
							pkgUsedInfo.setRemain(total - used);
							pkgUsedInfo.setGprs_cumulate_type(gprs_cumulate_type);
							pkgUsedInfo.setBegin_date(begin_date);
							pkgUsedInfo.setEnd_date(end_date);	
							subUsedInfoList.add(pkgUsedInfo);
							pkg.setSubUsedInfoList(subUsedInfoList);
						}
						
						
					}
					else
					{
						//添加流量套餐编码到listP中
                        String gprs_product_name = lteNode.selectSingleNode("gprs_product_name").getText().trim();
            			subUsedInfoList = new ArrayList<PkgUsedInfo>();
						pkgInfo = new PkgInfo();
					
						pkgInfo.setPkgName(gprs_product_name);
						pkgInfo.setPkgId(productId);
						
						for(int j=1;j<=num;j++)
						{
							PkgUsedInfo pkgUsedInfo= new PkgUsedInfo();
							
							if ("1".equals(rateType))
							{
								pkgUsedInfo.setFlag(4);
								pkgUsedInfo.setFreeItemId("3");
								tempFlag = 4;
							}
							else
							{
								pkgUsedInfo.setFlag(8);
								pkgUsedInfo.setFreeItemId("3");
								tempFlag = 6;
							}
							
							pkgUsedInfo.setPkgName("移动数据流量");
							Long total =parseData(total_max+"",(int) tempFlag);
							Long usedd = parseData(lteNode.selectSingleNode("gprs_cumulate_value").getText().trim(),
									(int) tempFlag);
							long used =0l;
							if(j < need_int)
	    					{
								used = total;
	    					}
	    					else if(j==need_int && j==1)
	    					{
	    						used = usedd;
	    					}
	    					else if(j==need_int && j > 1)
	    					{
	    						used =  parseData((total_int-(need_int-1) * total_max)+"",(int) tempFlag);
	    					}
	    					else
	    					{
	    						used = 0;
	    					}
							
							
							pkgUsedInfo.setTotal(total);
							pkgUsedInfo.setRemain(total - used);
							pkgUsedInfo.setGprs_cumulate_type(gprs_cumulate_type);
							pkgUsedInfo.setBegin_date(begin_date);
							pkgUsedInfo.setEnd_date(end_date);	
							subUsedInfoList.add(pkgUsedInfo);
						}
						pkgInfo.setSubUsedInfoList(subUsedInfoList);
						if(null != pkgInfo)
						{
							listP.add(pkgInfo);
						}
					}
				}
			}
			if(listP.size() >0)
			{
				for(PkgInfo p: listP)
				{
					pkgInfoList.add(p);
				}
			}
			return pkgInfoList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e, e);
		}
		return null;
	}
	/**
	 * 用于返回参数让各个渠道判断单位
	 * @param packId
	 * @param type
	 * @return
	 */
	private Integer returnType(String type,String showType)
	{
		switch(showType.charAt(0))
		{
			case '2':
				return 8;
			case '4':
				return 4;
			case '1':
				return 5;
			default:
				return this.map.get(type) == null ? 0 : this.map.get(type);
		}
	}
	
	/**
	 * 用于换算单位的
	 * @param value
	 * @param flag
	 * @return
	 */
	private Long parseData(String value, int flag) {
		Long retValue = Long.valueOf(value);
		if(flag == 4)
			return retValue * 1024;
		if(flag == 5)
			return retValue / 100;
		if(flag == 6)
			return retValue / 60;
		return retValue;
	}
	
	/**
	 * 查询用户所办理的个人套餐
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> queryUserPackage(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res)
	{
		String reqXml = "";
		String rspXml = "";
		setParameter(params, "biz_pkg_qry_scope", "1");// 查询方式   在用和预约套餐
		setParameter(params, "package_type", "0");
		//更新phoneNum 如果有I8
		 RequestParameter parameterNum = getParameter(params, "phoneNum");
		 String phoneNum = "";
		 if(null != parameterNum)
		 {
			 phoneNum = (String) parameterNum.getParameterValue();
		 }
		 
		String vice_num = idMap.get("vice_num");
		if(!StringUtil.isNull(vice_num))
		{
			phoneNum = vice_num;
		}
		setParameter(params, "phoneNum", phoneNum);
		
		
		 RequestParameter parameter = getParameter(params, "context_user_id");
		 String contextUserId = "";
		 if(null != parameter)
		 {
			 contextUserId = (String) parameter.getParameterValue();
		 }
		 
		String userId = idMap.get("vice_id");
		if(!StringUtil.isNull(userId))
		{
			contextUserId = userId;
		}
		setParameter(params, "context_user_id", contextUserId);
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
						return freeItemNodes;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		return null;
	}
	
	/**
	 * 查询用户个人套餐的使用明细
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> queryUserPackageInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res)
	{
		setParameter(params, "a_package_id", "0");
		String dateNow = DateTimeUtil.getTodayChar6();
		setParameter(params, "dbi_month",dateNow);

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_361", params);
			logger.debug(" ====== 查询用户套餐使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "ac_agetfreeitem_361", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/freeitem_dt");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						return freeItemNodes;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}

	/**
	 * 查询流量季包、半年包套餐使用情况
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> qryPrivilegeLeftcross(String accessId, ServiceConfig config, List<RequestParameter> params,String packCode,String stateDate,QRY040020Result res)
	{   
		setParameter(params, "prodid", packCode);
		setParameter(params, "prod_instance_id", "");
		String begin_date =  stateDate.substring(0,8);
		setParameter(params, "begin_date",begin_date);
		setParameter(params, "end_date","20201231");

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_privilegeleftcross_70", params);
			logger.debug(" ====== 查询流量季包、半年包套餐使用情况 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_privilegeleftcross_70", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/log_list/log");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						return freeItemNodes;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}

	/**
	 * 查询用户个人流量功能套餐的使用明细
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> queryGPRSPackageInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res)
	{
		setParameter(params, "gprstype", "3");
		String dateNow = DateTimeUtil.getTodayChar6();
		setParameter(params, "cycle",dateNow);

		try{//QueryUsedPackageInfoInvocation
			String reqXml = this.bossTeletextUtil.mergeTeletext("QUERYGPRSALLPKGFLUX_814", params);
			logger.debug(" ====== 查询用户个人流量套餐功能套餐的使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "QUERYGPRSALLPKGFLUX_814", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				
				Element root;
				if(null != rspXml)
				{
					root = this.getElement(rspXml.getBytes());
					if(null != root)
					{
		    			String resp_code = root.getChild("response").getChildText("resp_code");
		    			String resp_desc = root.getChild("response").getChildText("resp_desc");
		    			//Boss一期割接到Boss二期处理 Boss一期为一个0 Boss二期为4个0
						if("0000".equals(resp_code))
						{
							resp_code = "0";
						}
		    			String resultCode = "0".equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
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
		    					Document doc = DocumentHelper.parseText(rspXml);
		    					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
		    					if(null != freeItemNodes && freeItemNodes.size()>0)
		    					{
		    						return freeItemNodes;
		    					}
		    					res.setResultCode(resultCode);
		    					res.setErrorCode(resp_code);
		    					res.setErrorMessage(resp_desc);
		    				}
		    			}
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}
	/**
	 * 查询用户是否开通了亲情号码
	 * @return prodcode
	 */
	private String isOpenFamilNumService(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res)
	{
		try {
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmsngsubpack_352", params);
			logger.debug(" ====== 查询用户加入的亲情组合套餐信息 发送报文 ====== \n" + reqXml);
			
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetmsngsubpack_352", super.generateCity(params)));
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/package_user_id/cplanpackagedt");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						for(Node freeItemNode : freeItemNodes)
						{
							String packCode = freeItemNode.selectSingleNode("package_code").getText().trim();
							int usedDate = Integer.valueOf(freeItemNode.selectSingleNode("package_use_date").getText().trim());
							int nowDate = Integer.valueOf(DateTimeUtil.getTodayChar8());
							
							if(familNum.contains(packCode) && usedDate <  nowDate)
							{
								return packCode;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询亲情号码的介绍等情况
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param packCode
	 * @return
	 */
	private PkgInfo queryFamilNumServDesc(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res,String packCode)
	{
		BossParmDT bossParmDt = new BossParmDT();
		bossParmDt.setParm1(packCode);
		List<BossParmDT> parList = new ArrayList<BossParmDT>();
		parList.add(bossParmDt);
		RequestParameter par = new RequestParameter();
		par.setParameterName("codeCount");
		par.setParameterValue(parList);
		params.add(par);
		try{
			String rspXml = (String)this.remote.callRemote(new StringTeletext(
					this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params)
					,accessId, "cc_cgetpackbycode_605", this.generateCity(params)));
			Element root = checkReturnXml(rspXml,res);
			if(null != root)
			{
				Document doc = DocumentHelper.parseText(rspXml);
				List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/productbusinesspackage_package_code/cproductbusinesspackagedt");
				if(null != freeItemNodes && freeItemNodes.size()>0)
				{
					for(Node freeItemNode : freeItemNodes)
					{
						String packageCode = freeItemNode.selectSingleNode("productbusinesspackage_package_code").getText().trim();
						if(checkPackCode(packCode,packageCode))
						{
							PkgInfo pkgInfo = new PkgInfo();
							String pkgDec = freeItemNode.selectSingleNode("productbusinesspackage_package_desc").getText().trim();
							pkgInfo.setPkgDec(pkgDec);
							pkgInfo.setPkgId(packCode);
							String pkgName = freeItemNode.selectSingleNode("productbusinesspackage_package_name").getText().trim();
							pkgInfo.setPkgName(pkgName);
							pkgInfo.setPkgId(packageCode);
							return pkgInfo;
						}
					}
				}	
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询亲情号码的使用情况
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param pkgInfo
	 * @return
	 */
	private PkgInfo queryPackageUsage(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040020Result res,PkgInfo pkgInfo)
	{
		setParameter(params, "dbi_month_pr_number",DateTimeUtil.getTodayChar6());
		setParameter(params, "cdr_reduce_total","1");
		setParameter(params, "a_bg_bill_month","0");
		setParameter(params, "acrelation_revision",pkgInfo.getPkgId());
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetzoneinfo_518", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetzoneinfo_518", super.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					Element content = root.getChild("content");
					List<PkgUsedInfo> reList = new ArrayList<PkgUsedInfo>();
					String areCount = content.getChildText("arecord_count");
					
					if (null != areCount && !"".equals(areCount))
					{
						PkgUsedInfo pkg = new PkgUsedInfo();
						pkg.setPkgName("[短信]");
						pkg.setFreeItemId("1");
						pkg.setFlag(2);
						String strUse = content.getChildText("arecord_count");
						String strRemain = content.getChildText("mms_len");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0)
						{
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
						{
							reList.add(pkg);
						}
					}
					String dataDown = content.getChildText("data_down");
					//IP电话
					if (null != dataDown && !"".equals(dataDown)) {
						PkgUsedInfo pkg = new PkgUsedInfo();
						pkg.setPkgName("[IP电话]");
						pkg.setFreeItemId("6");
						pkg.setFlag(10);
						String strUse = content.getChildText("data_down");
						String strRemain = content.getChildText("fax_time");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
						{
							reList.add(pkg);
						}
					}
					String dataUp = content.getChildText("data_up");
					// 通话时间
					if (null != dataUp && !"".equals(dataUp)) {
						PkgUsedInfo pkg = new PkgUsedInfo();
						pkg.setPkgName("[通话时间]");
						pkg.setFreeItemId("7");
						pkg.setFlag(8);
						String strUse = content.getChildText("data_up");
						String strRemain = content.getChildText("rec_time");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0) {
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
						{
							reList.add(pkg);
						}
					}
					String accIstra = content.getChildText("acctbkitem_istransferable");
					// 彩信
					if (null != accIstra && !"".equals(accIstra)) {
						PkgUsedInfo pkg = new PkgUsedInfo();
						pkg.setPkgName("[彩信]");
						pkg.setFreeItemId("2");
						pkg.setFlag(3);
						String strUse = content.getChildText("acctbkitem_istransferable");
						String strRemain = content.getChildText("acctbkitem_usage_type");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0)
						{
							pkg.setTotal(Long.parseLong(strUse) + Long.parseLong(strRemain));
							pkg.setRemain(Long.parseLong(strRemain));
						}
						if (pkg.getTotal() > 0)
						{
							reList.add(pkg);
						}
					}
					String accDefa = content.getChildText("acctbkitem_default_flag");

					// GPRS 特殊套餐才显示GPRS/WLAN，否则不显示
					if (null != accDefa && !"".equals(accDefa) && !"0".equals(accDefa) ) {
						PkgUsedInfo pkg = new PkgUsedInfo();
						pkg.setPkgName("[GPRS]");
						pkg.setFreeItemId("3");
						pkg.setFlag(4);
						String strUse = content.getChildText("acctbkitem_default_flag");
						String strRemain = content.getChildText("acctbkitem_invprn_flag");
						if (strUse != null && strUse.trim().length() > 0 && strRemain != null && strRemain.trim().length() > 0)
						{
							pkg.setTotal(parseData(strUse,pkg.getFlag()) + parseData(strRemain,pkg.getFlag()));
							pkg.setRemain(parseData(strRemain,pkg.getFlag()));
						}
						if (pkg.getTotal() > 0)
						{
							reList.add(pkg);
						}
					}
					pkgInfo.setSubUsedInfoList(reList);
					return pkgInfo;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040020Result res)
	{
		if(null != rspXml)
		{
			Element root = this.getElement(rspXml.getBytes());
			if(null != root)
			{
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
			}
		}
		return null;
	}
	
	/**
	 * 用来把查询用户办理的套餐编码和用户套餐使用情况的编码对应的方法
	 * @param codeMap
	 * @param packCode
	 * @return boolean
	 */
	private boolean checkPackCode(Map<String,Integer> codeMap,String packCode)
	{
		String maxPackCode = "";
		if(packCode.length() == 4)
		{
			maxPackCode = "200000"+packCode;
		}
		else if(packCode.length() == 10)
		{
			maxPackCode = packCode.substring(6);
		}
		
		
		if(codeMap.containsKey(packCode) || codeMap.containsKey(maxPackCode))
		{
			int count;
			if(null == codeMap.get(packCode))
			{
				count = Integer.valueOf(codeMap.get(maxPackCode).toString()) + 1;
			}
			else
			{
				count = Integer.valueOf(codeMap.get(packCode).toString()) + 1;
			}
			
			codeMap.put(packCode,count);
			return true;
		}
		return false;
	}
	/**
	 * 匹配长编码和短编码
	 * @param codeMap
	 * @param packCode
	 * @return
	 */
	private boolean checkPackCode(String codeMap,String packCode)
	{
		String maxPackCode = "";
		if(packCode.length() == 4)
		{
			maxPackCode = "200000"+packCode;
		}
		else
		{
			maxPackCode = packCode.substring(6);
		}
		
		if(codeMap.equals(packCode) || codeMap.equals(maxPackCode))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 查询是否是家庭融合
	 * @param tcId
	 * @return
	 */
	private boolean isI8JTRH(String tcId)
	{
		//根据套餐ID查询是否是家庭整合
		try
		{
			if(null != tcId && !"".equals(tcId)) 
			{
				
				if(4 == tcId.length()) 
					tcId = "200000"+tcId;
				return i8jtrhDao.isT8JTHRH(tcId);
			}
			
		} catch (DAOException e) {
			e.printStackTrace();
			logger.error(e);
		}
		
		
		return false;
	}
	
	private int setI8Flag(String itemId)
	{
		if(!StringUtil.isNull(itemId))
		{
			if(i8ItemLLorSC.containsKey(itemId))
				return i8ItemLLorSC.get(itemId);
		}
		
		return 0;
	}
	/**
	 * 设置I8流量返回单位
	 * @param itemId
	 * @return
	 */
	private long setI8LL(String itemId,String num)
	{
		long l = Long.parseLong(num);
		//只计算i8Map中的几个流量情况
		if(i8LLLists.contains(itemId))
		{
			l = Long.parseLong(num) * 1024;
		}
		return l;	
	}
}