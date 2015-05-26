package com.xwtech.xwecp.dao;

import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.service.logic.pojo.CountryNetworkInfo;
import com.xwtech.xwecp.pojo.FamMemUserProductId;
import com.xwtech.xwecp.pojo.PackageBean;
import com.xwtech.xwecp.pojo.PkgExgNeedBusiInfo;
import com.xwtech.xwecp.pojo.PkgExgSelectBean;

public interface IPackageChangeDAO {
	
	/**
	 * //获取套餐档案信息
	 * @return
	 * @throws DAOException
	 */
	public List<PackageBean> getPackageInfo() throws DAOException;
	
	/**
	 * //获取主选套餐编码
	 * @return
	 * @throws DAOException
	 */
	public List<String> getProPkg(String productId) throws DAOException;
	
	/**
	 * //判断此套餐是否可以套餐变更
	 * @param pakNum
	 * @return
	 * @throws DAOException
	 */
	public boolean allowChangePackage(String pakNum) throws DAOException;
	public boolean allowCodeChangePackage(String pakNum,String ctiy) throws DAOException;
	
	/**
	 * 套餐变更”转入套餐备选列表
	 * @param area
	 * @return
	 * @throws DAOException
	 */
	public List<PkgExgSelectBean> getPkgExgSelect(String area, String proId) throws DAOException;
	
	
	/**
	 * 套餐变更”转出套餐必选业务列表
	 * @param area
	 * @param pkgNumEcp
	 * @return
	 * @throws DAOException
	 */
	public List<PkgExgNeedBusiInfo> getPkgExgNeedBusiLst(String area, String pkgNumEcp, String proId) throws DAOException;
	
	/**
	 * 在线入网 根据地市查询产品
	 * @return
	 * @throws DAOException
	 */
	public List<String> getProIdByCity(String city) throws DAOException;
	
	
	/**
	 * 在线入网 根据地市查询套餐
	 * @return
	 * @throws DAOException
	 */
	public List<String> getPkgIdByCity(String city) throws DAOException;
	
	/**
	 * 根据地市查询每个地市乡情网名称
	 * @return
	 * @throws DAOException
	 */
	public List<String> getXQGroupName(String city) throws DAOException;
	
	/**
	 * 查询流量套餐配置库套餐编码（查询流量套餐使用情况）
	 * @return
	 * @throws DAOException
	 */
	public List<Map> getFluxPkgCode(String fluxPkgType) throws DAOException;
	
	/**
	 * 根据地市编码，区县编码查询乡情网的信息
	 * @param region
	 * @param countryId
	 * @return
	 * @throws DAOException 
	 */
	public List<CountryNetworkInfo> getCountryInfo(String region,String countryId)throws DAOException;
	
	/**
	 * 根据地市编码，区县编码以及乡情网名称模糊查询乡情网的信息
	 * @param region
	 * @param countryId
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public List<CountryNetworkInfo> getCountryInfo(String region,String countryId,String name)throws DAOException;
	
	/**
	 * 根据集团编码查询乡情网的信息
	 * @param custId
	 * @return
	 * @throws DAOException
	 */
	public List<CountryNetworkInfo> getCountryInfo(String custId)throws DAOException;
	
	/**
	 * 根据用户的地市编码查询支持办理亲情号码的的主体产品id
	 * @param region
	 * @return
	 * @throws DAOException
	 */
	public List<FamMemUserProductId> getOperFamilyMainProdId(String region)throws DAOException;
	
}
