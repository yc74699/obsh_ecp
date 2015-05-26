package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.CwebCustInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebPackageInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebServiceInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebIncrementInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebSelfPlatinfoBean;
import com.xwtech.xwecp.service.logic.pojo.DEL011005Result;

public interface ICnetInstallPrd
{
	public DEL011005Result transactBroadBand(String ddrCity, List<CwebCustInfoBean> cwebCustInfoList, List<CwebPackageInfoBean> cwebPackageInfoList, List<CwebServiceInfoBean> cwebServiceInfoList, List<CwebIncrementInfoBean> cwebIncrementInfoList, List<CwebSelfPlatinfoBean> cwebSelfPlatinfoList) throws LIException;

}