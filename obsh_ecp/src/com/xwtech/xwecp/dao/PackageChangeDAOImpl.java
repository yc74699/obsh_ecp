package com.xwtech.xwecp.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.xwtech.xwecp.pojo.FamMemUserProductId;
import com.xwtech.xwecp.pojo.PackageBean;
import com.xwtech.xwecp.pojo.PkgExgNeedBusiInfo;
import com.xwtech.xwecp.pojo.PkgExgSelectBean;
import com.xwtech.xwecp.service.logic.pojo.CountryNetworkInfo;
import com.xwtech.xwecp.util.StringUtil;

public class PackageChangeDAOImpl extends BaseDAO implements IPackageChangeDAO {

	/* (non-Javadoc)
	 * @see com.xwtech.xwecp.dao.IPackageChangeDAO#getPackageInfo()
	 */
	@SuppressWarnings("unchecked")
	public List<PackageBean> getPackageInfo() throws DAOException {
		
		List<PackageBean> pakcageInfo = (List<PackageBean>)(this.getCache().get("PKG_DA"));
		if(pakcageInfo == null) {
			pakcageInfo = new ArrayList<PackageBean>();
			List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList("SELECT * FROM T_PKG_BOSS_DESC_DA");
			if(ret != null && ret.size() > 0) {
				for(Map m : ret) {
					PackageBean bean = new PackageBean();
					bean.setPkgNum(StringUtil.convertNull((String)m.get("F_PKG_NUM")));
					bean.setPkgNumBoss(StringUtil.convertNull((String)m.get("F_PKG_NUM_BOSS")));
					pakcageInfo.add(bean);
				}
			}
			this.getCache().add("PKG_DA", pakcageInfo);
		}
		
		return pakcageInfo;
		
	}

	/* (non-Javadoc)
	 * @see com.xwtech.xwecp.dao.IPackageChangeDAO#allowChangePackage(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean allowChangePackage(String pakNum) throws DAOException {
		
		List<Map> pakcageInfo = (List<Map>)(this.getCache().get("PKG_EXG_EXCEPT_LIST_" + pakNum));
		if(pakcageInfo == null) {
			String sql = "SELECT F_CHANNEL_NUM, F_AREA_NUM, F_PRODUCT_ID_BOSS, F_BZ FROM T_PKG_EXG_LIST WHERE F_PRODUCT_ID_BOSS = ? ";
			Object obj[] = new Object[1];
			obj[0] = pakNum;
			pakcageInfo = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			this.getCache().add("PKG_EXG_EXCEPT_LIST_" + pakNum, pakcageInfo);
		}
		
		if(pakcageInfo != null && pakcageInfo.size() > 0) {
			return true;
		} 
		
		return false;
	}
	public boolean allowCodeChangePackage(String pakNum,String city) throws DAOException {
		
		List<Map> pakcageInfo = (List<Map>)(this.getCache().get("PKG_EXG_EXCEPT_LIST_" + pakNum+"_"+city));
		if(pakcageInfo == null) {
			String sql = "SELECT F_CHANNEL_NUM, F_AREA_NUM, F_PRODUCT_ID_BOSS, F_BZ FROM T_PKG_EXG_LIST WHERE F_PRODUCT_ID_BOSS = ? AND F_AREA_NUM=? ";
			Object obj[] = new Object[2];
			obj[0] = pakNum;
			obj[1] = city;
			pakcageInfo = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			this.getCache().add("PKG_EXG_EXCEPT_LIST_" + pakNum+"_"+city, pakcageInfo);
		}
		
		if(pakcageInfo != null && pakcageInfo.size() > 0) {
			return true;
		} 
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.xwtech.xwecp.dao.IPackageChangeDAO#getPkgExgSelect(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PkgExgSelectBean> getPkgExgSelect(String area, String proId)
			throws DAOException {
		
		List<PkgExgSelectBean> pkgExgSelectLst = (List<PkgExgSelectBean>)(this.getCache().get("PKG_EXG_SELECT_LIST_"+area+"_"+proId));
		if(pkgExgSelectLst == null) {
			String sql = "select s.f_channel_num, s.f_area_num, s.f_imp_group_num, s.f_imp_pkg_num_ecp, s.f_bz"
						+ " from t_pkg_exg_select_list s, t_pkg_exg_select_group_da g where s.f_imp_group_num = g.f_group_num and s.f_area_num = ?"
						+ " and g.f_product_id_boss = ?";

			Object obj[] = new Object[2];
			obj[0] = area;
			obj[1] = proId;
			List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			pkgExgSelectLst = new ArrayList<PkgExgSelectBean>();
			if(ret != null && ret.size() > 0) {
				for(Map m : ret) {
					PkgExgSelectBean tmp  = new PkgExgSelectBean();
					tmp.setChannelNum(StringUtil.convertNull((String)m.get("F_CHANNEL_NUM")));
					tmp.setArerNum(StringUtil.convertNull((String)m.get("F_AREA_NUM")));
					tmp.setImpGroupNum(StringUtil.convertNull((String)m.get("F_IMP_GROUP_NUM")));
					tmp.setImpPkgNumEcp(StringUtil.convertNull((String)m.get("F_IMP_PKG_NUM_ECP")));
					tmp.setBz(StringUtil.convertNull((String)m.get("F_BZ")));
					pkgExgSelectLst.add(tmp);
				}
			}
			this.getCache().add("PKG_EXG_SELECT_LIST_"+area+"_"+proId, pkgExgSelectLst);
		}
		
		return pkgExgSelectLst;
	}

	/* (non-Javadoc)
	 * @see com.xwtech.xwecp.dao.IPackageChangeDAO#getPkgExgNeedBusiLst(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PkgExgNeedBusiInfo> getPkgExgNeedBusiLst(String area, String pkgNumEcp, String proId) throws DAOException {
		
		List<PkgExgNeedBusiInfo> pkgExgNeedBusiLst = (List<PkgExgNeedBusiInfo>)(this.getCache().get("PKG_EXG_NEED_BUSI_LIST_" + area + "_" + pkgNumEcp));
		if(pkgExgNeedBusiLst == null) {
			String sql = "SELECT N.F_CHANNEL_NUM, N.F_AREA_NUM, N.F_PKG_NUM_ECP, N.F_BUSI_TYPE, N.F_BUSI_NUM_ECP, N.F_BZ, B.F_BUSINESS_NAME " 
						+ " FROM T_PKG_EXG_NEED_BUSI_LIST N, T_BUSINESS_DA B WHERE N.F_BUSI_NUM_ECP = B.F_BUSINESS_NUM AND F_AREA_NUM = ? AND F_PKG_NUM_ECP = ?";
			
			Object obj[] = new Object[2];
			obj[0] = area;
			obj[1] = pkgNumEcp;
			pkgExgNeedBusiLst = new ArrayList<PkgExgNeedBusiInfo>();
			List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			
			if(ret != null && ret.size() > 0) {
				for(Map m : ret) {
					PkgExgNeedBusiInfo tmp  = new PkgExgNeedBusiInfo();
					tmp.setChannelNum(StringUtil.convertNull((String)m.get("F_CHANNEL_NUM")));
					tmp.setAreaNum(StringUtil.convertNull((String)m.get("F_AREA_NUM")));
					tmp.setPkgNumEcp(StringUtil.convertNull((String)m.get("F_PKG_NUM_ECP")));
					tmp.setBusiNumEcp(StringUtil.convertNull((String)m.get("F_BUSI_NUM_ECP")));
					tmp.setBz(StringUtil.convertNull((String)m.get("F_BZ")));
					tmp.setBusiNumBoss1(((BigDecimal)m.get("F_BUSI_TYPE")).toString());
					tmp.setBizName(StringUtil.convertNull((String)m.get("F_BUSINESS_NAME")));
					pkgExgNeedBusiLst.add(tmp);
				}
			}
			this.getCache().add("PKG_EXG_NEED_BUSI_LIST_" + area + "_" + pkgNumEcp, pkgExgNeedBusiLst);
		}
		
		return pkgExgNeedBusiLst;
	}

	/* 在线入网 根据地市查询产品
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProIdByCity(String city)
			throws DAOException {
		
		List retList = new ArrayList();
		String sql = " select t.F_PRODUCT_ID_BOSS from t_zxrw_product_list t where t.F_CHANNEL_NUM='obsh' and t.F_AREA_NUM = ? ";
		Object obj[] = new Object[1];
		obj[0] = city;
		List<Map> list = this.getJdbcTemplate().queryForList(sql,obj);
		if(list != null){
			for(int i = 0; i < list.size(); i++)
			{
				Map m = list.get(i);
				retList.add(String.valueOf(StringUtil.convertNull((String)m.get("F_PRODUCT_ID_BOSS"))));
			}
		}
		return retList;
	}
	
	/* 在线入网 根据地市查询套餐列表
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPkgIdByCity(String city)
			throws DAOException {
		
		List retList = new ArrayList();
		String sql = " select t.F_PKG_ID_BOSS from t_zxrw_pkg_list t where t.F_CHANNEL_NUM='obsh' and t.F_AREA_NUM = ? ";
		Object obj[] = new Object[1];
		obj[0] = city;
		List<Map> list = this.getJdbcTemplate().queryForList(sql,obj);
		if(list != null){
			for(int i = 0; i < list.size(); i++)
			{
				Map m = list.get(i);
				retList.add(String.valueOf(StringUtil.convertNull((String)m.get("F_PKG_ID_BOSS"))));
			}
		}
		return retList;
	}
	
	
	/**
	 * 根据地市查询每个地市乡情网名称
	 * @return
	 * @throws DAOException
	 */
	public List<String> getXQGroupName(String city) throws DAOException {
		List<String> groupNameList = (List<String>)(this.getCache().get("CONTY_GROUP_NAME_" + city));
		if(groupNameList == null) {
			String sql = "select F_GROUP_NAME from T_GROUP_DA where F_CITY = ? ";
			Object obj[] = new Object[1];
			obj[0] = city;
			groupNameList = new ArrayList<String>();
			List<Map> list = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			if(list != null){
				for(int i = 0; i < list.size(); i++)
				{
					Map m = list.get(i);
					groupNameList.add(String.valueOf(StringUtil.convertNull((String)m.get("F_GROUP_NAME"))));
				}
			}
			this.getCache().add("CONTY_GROUP_NAME_" + city, groupNameList);
		}
		
		return groupNameList;
	}
	
	/* 
	 * 查询流量套餐配置库套餐编码（查询流量套餐使用情况）
	 */
	public List<Map> getFluxPkgCode(String fluxPkgType) throws DAOException {
		
		List<Map> fluxPkgLst = (List<Map>)(this.getCache().get("PKG_FLUX_LIST_" + fluxPkgType));
		if(fluxPkgLst == null) {
			String sql = "select F_PKG_CODE, F_PKG_TYPE, F_PKG_NAME, F_TYPE_NAME from T_FLUX_PKG_INFO where F_PKG_UNIT='4' and F_PKG_TYPE = ? ";
			Object obj[] = new Object[1];
			obj[0] = fluxPkgType;
			fluxPkgLst = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			this.getCache().add("PKG_FLUX_LIST_" + fluxPkgType, fluxPkgLst);
		}
		
		return fluxPkgLst;
	}
	/*
	 *查询乡情网的id
	 * */
	public List<CountryNetworkInfo> getCountryInfo(String region,String countryId)
	{
		List<CountryNetworkInfo> couNetInfoList = (List<CountryNetworkInfo>)(this.getCache().get("t_group_da_new_"+region+"_"+countryId));

		if(couNetInfoList == null)
		{
			String sql = "select distinct t.f_group_name,t.f_dict_name,t.f_cust_id from t_group_da_new t " +
					"where t.f_region = ? and t.f_country_id = ?";
			Object obj[] = new Object[2];
			obj[0] = region;
			obj[1] = countryId;
			List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			couNetInfoList = new ArrayList<CountryNetworkInfo>();
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			if(ret != null && ret.size() > 0)
			{
				for(Map m : ret)
				{
					CountryNetworkInfo  countryNetworkInfo = new CountryNetworkInfo();
					countryNetworkInfo.setRegion(region);
					countryNetworkInfo.setCountryId(countryId);
					String dictName = p.matcher(StringUtil.convertNull(m.get("f_dict_name").toString())).replaceAll("");
					countryNetworkInfo.setDictName(dictName);
					String groupName = p.matcher(StringUtil.convertNull(m.get("f_group_name").toString())).replaceAll("");
					countryNetworkInfo.setGroupName(groupName);
					String custId = p.matcher(StringUtil.convertNull(m.get("f_cust_id").toString())).replaceAll("");
					countryNetworkInfo.setCustId(custId);
					couNetInfoList.add(countryNetworkInfo);
				}
				this.getCache().add("t_group_da_new_"+region+"_"+countryId, couNetInfoList);
			}
		}
		return couNetInfoList;
	}
	public List<CountryNetworkInfo> getCountryInfo(String region,String countryId,String name)
	{	
		Object obj[];
		String sql;
		if(null != countryId && !"".equals(countryId))
		{
			sql = "select distinct t.f_group_name,t.f_dict_name,t.f_cust_id" +
					" from t_group_da_new t where t.f_region = ? and t.f_country_id = ? and t.f_group_name like ?";
			obj = new Object[3];
			obj[0] = region;
			obj[1] = countryId;
			obj[2] = "%"+name+"%";
		}
		else
		{
			sql = "select distinct t.f_group_name,t.f_dict_name,t.f_cust_id" +
					" from t_group_da_new t where t.f_region = ? and t.f_group_name like ?";
			obj = new Object[2];
			obj[0] = region;
			obj[1] = "%"+name+"%";
		}
		List<Map<String,String>> ret = (List<Map<String, String>>)this.getJdbcTemplate().queryForList(sql, obj);
		List<CountryNetworkInfo> couNetInfoList = new ArrayList<CountryNetworkInfo>();
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		if(ret != null && ret.size() > 0)
		{
			for(Map<String,String> m : ret)
			{
				CountryNetworkInfo  countryNetworkInfo = new CountryNetworkInfo();
				countryNetworkInfo.setRegion(region);
				countryNetworkInfo.setCountryId(countryId);
				String dictName = p.matcher(StringUtil.convertNull(m.get("f_dict_name").toString())).replaceAll("");
				countryNetworkInfo.setDictName(dictName);
				String groupName = p.matcher(StringUtil.convertNull(m.get("f_group_name").toString())).replaceAll("");
				countryNetworkInfo.setGroupName(groupName);
				String custId = p.matcher(StringUtil.convertNull(m.get("f_cust_id").toString())).replaceAll("");
				countryNetworkInfo.setCustId(custId);
				couNetInfoList.add(countryNetworkInfo);
			}
		}
		return couNetInfoList;
	}

	public List<CountryNetworkInfo> getCountryInfo(String custId)
			throws DAOException {
		String sql = "select * from t_group_da_new t where t.f_cust_id = ?";
		Object obj[] = new Object[1];
		obj[0] = custId;
		List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
		List<CountryNetworkInfo>  couNetInfoList = new ArrayList<CountryNetworkInfo>();
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		if(ret != null && ret.size() > 0)
		{
			for(Map m : ret)
			{
				CountryNetworkInfo  countryNetworkInfo = new CountryNetworkInfo();
				String region = p.matcher(StringUtil.convertNull(m.get("f_region").toString())).replaceAll("");
				countryNetworkInfo.setDictName(region);
				countryNetworkInfo.setRegion(region);
				String countryId = p.matcher(StringUtil.convertNull(m.get("f_country_id").toString())).replaceAll("");
				countryNetworkInfo.setDictName(countryId);
				countryNetworkInfo.setCountryId(countryId);
				String dictName = p.matcher(StringUtil.convertNull(m.get("f_dict_name").toString())).replaceAll("");
				countryNetworkInfo.setDictName(dictName);
				String groupName = p.matcher(StringUtil.convertNull(m.get("f_group_name").toString())).replaceAll("");
				countryNetworkInfo.setGroupName(groupName);
				String subsId = p.matcher(StringUtil.convertNull(m.get("f_subs_id").toString())).replaceAll("");
				countryNetworkInfo.setSubsId(subsId);
				String busiName = p.matcher(StringUtil.convertNull(m.get("f_business_name").toString())).replaceAll("");
				countryNetworkInfo.setBusiName(busiName);
				countryNetworkInfo.setCustId(custId);
				couNetInfoList.add(countryNetworkInfo);
			}
		}
		return couNetInfoList;
	}

	public List<FamMemUserProductId> getOperFamilyMainProdId(String region)
			throws DAOException {
		List<FamMemUserProductId> famMemUserProList = (List<FamMemUserProductId>)(this.getCache().get("t_family_meb_ber_"+region));
		if(famMemUserProList == null)
		{
			String sql = "select * from t_family_meb_ber t where t.region = ? ";
			Object obj[] = new Object[1];
			obj[0] = region;
			List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			famMemUserProList = new ArrayList<FamMemUserProductId>();
			if(ret != null && ret.size() > 0)
			{
				for(Map m : ret)
				{
					FamMemUserProductId famMemUseProd = new FamMemUserProductId();
					famMemUseProd.setCityName(m.get("cityname").toString());
					famMemUseProd.setFamilyProdId(m.get("familyprodid").toString());
					famMemUseProd.setMainProdId(m.get("mainprodid").toString());
					famMemUseProd.setMainProdName(m.get("mainprodname").toString());
					famMemUseProd.setRegion(region);
					
					famMemUserProList.add(famMemUseProd);
				}
				this.getCache().add("t_family_meb_ber_"+region,famMemUserProList);
			}
		}
		return famMemUserProList;
	}

	public List<String> getProPkg(String productId) throws DAOException {
		List<String> listPkg = (List<String>)(this.getCache().get("t_pkg_cfg"+productId));
		if(listPkg==null)
		{
			String sql = "select * from T_PKG_CFG t where t.f_pkg_product = ? ";
			Object obj[] = new Object[1];
			obj[0] = productId;
			List<Map> ret = (List<Map>)this.getJdbcTemplate().queryForList(sql, obj);
			listPkg = new ArrayList<String>();
			if(ret != null && ret.size() > 0)
			{
				for(Map m : ret)
				{
					listPkg.add(String.valueOf(m.get("F_PKG_NUM")));
				}
				this.getCache().add("t_pkg_cfg"+productId,listPkg);
			}
		}
		return listPkg;
	}
}