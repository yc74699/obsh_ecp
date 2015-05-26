package com.xwtech.xwecp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.service.logic.pojo.CombineBean;
/**
 * 精确营销数据库查询逻辑修改
 * @author 张斌
 * 2014-8-21
 */
public class ItBossDaoImpl extends BaseDAO implements IItBossDao {

	@SuppressWarnings("unchecked")
	public List<CombineBean> getCombineByChannel(String bossCodes,
			String channelId, String areaId) {
		String key = "ITYXA_INFO_"+bossCodes+"_"+channelId;

		List<CombineBean> ls = (List<CombineBean>)(this.getCache().get(key));
		if(ls == null) {
			 ls = new ArrayList<CombineBean>();
			String[] bossCode =  bossCodes.split(",");
			try {
				List combineList = null;
				String sql = "select t.attach_path,t.plan_id, t.plan_name, t.plan_desc, t.area_id, t.exec_begin_date, t.exec_end_date, t.act_begin_date, t.act_end_date, t.plan_state, t.plan_type,"
							+"t.create_time, t.state_change_time, t.state_change_time, t.last_plan_id, t.is_exec_succ, t.channel_type, t.chl_state, t.channel_sub_type, t.floor, t.accessory_id,"
							+"t.suggest_type, t.suggest_num, t.suggest_desc, t.link_address, t.suggst_func, t.client_version,t.priority,t.iswinorpop,t.internet_id,t.boss_id from bt_it_jqyxa t where ";
				String[] args = new String[(bossCode.length)+1];
				StringBuffer buffer = new StringBuffer();
				for (int i = 0;i<bossCode.length;i++) {
					if (i == 0) {
						if(1==bossCode.length)
						{
							buffer.append("t.plan_id in (?)");
						}else
						{
							buffer.append("t.plan_id in (?");
						}
					} else if(i==bossCode.length-1)
					{
						buffer.append(",?)");
					} else
					{
						buffer.append(",?");
					}
				args[i] = bossCode[i];
				}
				sql +=buffer.toString();
				
				sql += " and t.channel_type=? and sysdate between to_date(t.exec_begin_date,'YYYY-MM-DD') and to_date(t.exec_end_date,'YYYY-MM-DD') order by t.priority DESC";
				args[bossCode.length] = channelId;
				combineList = this.getJdbcTemplate().queryForList(sql,args);
				for (int i = 0; i < combineList.size(); i++) {
					Map tempMap = (Map)combineList.get(i);
		
					CombineBean combine = new CombineBean();
					combine.setBusi_name((String)tempMap.get("plan_name"));
					combine.setPage_url((String)tempMap.get("link_address") == null ? "" : (String)tempMap.get("link_address"));
					combine.setBegin_time((String)tempMap.get("exec_begin_date"));
					combine.setEnd_time((String)tempMap.get("exec_end_date"));
					combine.setBusi_num((String)tempMap.get("plan_id"));
					combine.setPage_name((String)tempMap.get("suggest_desc"));
					combine.setPlan_level((String)tempMap.get("priority"));
					combine.setChannelSubType((String)tempMap.get("channel_sub_type"));
					combine.setFloor((String)tempMap.get("floor"));
					String field3 = (String)tempMap.get("area_id");
					combine.setIsWinOrPop((String)tempMap.get("iswinorpop") == null ? "" : (String)tempMap.get("iswinorpop"));
					combine.setInternet_id((String)tempMap.get("internet_id") == null ? "" : (String)tempMap.get("internet_id"));
					combine.setBoss_id((String)tempMap.get("boss_id") == null ? "" : (String)tempMap.get("boss_id"));
					field3 = field3 == null ? "" : field3;
					combine.setField3(field3);
					String webTransId = "";
					String remark = "3";
//					if(!field3.equals(areaId) && !"99".equals(field3))
//					{//用户地市筛选
//						continue;
//					}
					//网厅
					if("7".equals(channelId))
					{
						 webTransId = "WT-";
							webTransId += changeItToWt(combine.getChannelSubType(),combine.getFloor()) + "-IT-";
							if (null != combine.getPage_url() && combine.getPage_url().indexOf("bcma.jsp") != -1) {
								webTransId += "01-";
								remark = "1";
							} else if (null != combine.getPage_url() && combine.getPage_url().indexOf("act_js") != -1) {
								webTransId += "02-";
								remark = "2";
							} else {
								webTransId += "03-";
							}
							webTransId +=combine.getBusi_num();	
							combine.setWebTransId(webTransId);
							combine.setScene(changeItToWt(combine.getChannelSubType(),combine.getFloor()));
					}
					//客户端
					else if("11".equals(channelId))
					{
						 webTransId = "AP-";
						webTransId += changeItToZTKHT(combine.getChannelSubType(),1) + "-IT-";
						if (null != combine.getPage_url() && combine.getPage_url().indexOf("WBLYHHD") != -1) {
							webTransId += "01-";
							remark = "01";
						} else if (null != combine.getPage_url() && combine.getPage_url().indexOf("activity") != -1) {
							webTransId += "02-";
							remark = "02";
						} else {
							webTransId += "03-";
							remark = "03";
						}
						webTransId +=combine.getBusi_num();	
						combine.setWebTransId(webTransId);
						combine.setScene(changeItToZTKHT(combine.getChannelSubType(),2));
					}
					//把掌厅与网厅区分开
					else
					{
							webTransId += changeItToZT(combine.getChannelSubType(),1) + "-IT-";
							if (null != combine.getPage_url() && combine.getPage_url().indexOf("WBLYHHD") != -1) {
								webTransId += "01-";
								remark = "1";
							} else if (null != combine.getPage_url() && combine.getPage_url().indexOf("activity") != -1) {
								webTransId += "02-";
								remark = "2";
							} else {
								webTransId += "03-";
							}
							webTransId +=combine.getBusi_num();	
							combine.setWebTransId(webTransId);
							combine.setScene(changeItToZT(combine.getChannelSubType(),2));
					}
					combine.setRemark(remark);
					//增加图片名称
					String picName = (String) tempMap.get("attach_path") == null ? "" : (String) tempMap.get("attach_path");
					if(picName.indexOf("/") > -1)
					{
						picName = picName.substring(picName.lastIndexOf("/")+1,picName.length());
					}
					combine.setPage_pic3(picName);
					ls.add(combine);
				}
				boolean cacheRet = this.getCache().add(key, ls);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ls;
	}
	
	private String changeItToZTKHT(String channelSubType, int i)
	{
		String result = "";
		if("1".equals(channelSubType))
		{
//			if(1==i) result = "JSC_0";
//			if(2==i) result = "JSC_0";
			result = "JSC0";
		}
		if("2".equals(channelSubType))
		{
			result = "JSC1";
		}
		if("3".equals(channelSubType))
		{
			result = "JSC2";
		}
		else
		{
			result = channelSubType;
		}
		return result;
	}

	/**
	 * 掌厅拼接SCENCE
	 * @param subType
	 * @return
	 */
	public String changeItToZT (String subType,int type) {
		String result = "";
		if(1 == type)
		{
    		if ("1".equals(subType)) 
    		{
    			result = "ZT-RSC";
    		}
    		if ("2".equals(subType)) 
    		{
    			result = "ZT-DYRX";
    		}
    		if ("3".equals(subType)) 
    		{
    			result = "ZT-RQYW";
    		}
    		if ("4".equals(subType)) 
    		{
    			result = "ZT-LLSYJD";
    		}
    		if ("5".equals(subType)) 
    		{
    			result = "ZT-TCSYQK";
    		}
    		if ("6".equals(subType)) 
    		{
    			result = "ZT-DYRX";
    		}
    		if ("7".equals(subType)) 
    		{
    			result = "ZT-RQYW";
    		}
		}
		if(2 == type)
		{
	  		if ("1".equals(subType)) 
    		{
    			result = "ZT_RSC";
    		}
	  		else if ("2".equals(subType)) 
    		{
    			result = "ZT_DYRX";
    		}
	  		else if ("3".equals(subType)) 
    		{
    			result = "ZT_RQYW";
    		}
	  		else if ("4".equals(subType)) 
    		{
    			result = "ZT_LLSYJD";
    		}
	  		else if ("5".equals(subType)) 
    		{
    			result = "ZT_TCSYQK";
    		}
	  		else if("6".equals(subType)) 
    		{
    			result = "ZT_DYRX";
    		}
	  		else if ("7".equals(subType)) 
    		{
    			result = "ZT_RQYW";
    		}else
    		{
    			result = subType;
    		}
		}
		return result;
	}

	
	public String changeItToWt (String subType,String floor) {
		String result = "";
		if ("1".equals(subType)) {
			if ("1".equals(floor)) {
				result = "3F01";
			} else if ("2".equals(floor)) {
				result = "2F01";
			} else if ("3".equals(floor)) {
				result = "5F01";
			} else if ("4".equals(floor)) {
				result = "4F01";
			} else if ("5".equals(floor)) {
				result = "1F01";
			} else if ("6".equals(floor)) {
				result = "6F01";
			} else if ("7".equals(floor)) {
				result = "7F01";
			} else if ("8".equals(floor)) {
				result = "8F01";
			}
		} else if ("2".equals(subType)) {
			result = "RT01";
		} else if ("3".equals(subType)) {
			result = "MT01";
		} else if ("4".equals(subType)) {
			result = "TCSYQK01";
		} else if ("5".equals(subType)) {
			result = "ZDCX01";
		} else if ("6".equals(subType)) {
			result = "TCJYWCX01";
		} else{
			result = subType;
		}
		return result;
	}

}
