package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040115Result;
import com.xwtech.xwecp.service.logic.pojo.ProvchainOperDetail;

/**
 * 终端串号信息同步
 * @author wanghuan
 *
 */
public interface IProvChainService {
	/*
	 * <Oper_srl>$operSrl</Oper_srl>
		<Optype>$opType</Optype>
		<orgid>$orgid</orgid>
		<Brand_id>$brandId</Brand_id>
		<Res_type_id>$resTypeId</Res_type_id>
		<Vendor_id>$vendorId</Vendor_id>
		#foreach ($i in $operDetail)
		<Oper_detail>
			<Imei>$i.imei</Imei>
		</Oper_detail>
		#end
	 */
	public DEL040115Result synchronousPhoneNum(String operSrl , String opType, String orgId,String brandId,
			String resTypeId,String vendorId,List<ProvchainOperDetail> operList)  throws LIException;
}
