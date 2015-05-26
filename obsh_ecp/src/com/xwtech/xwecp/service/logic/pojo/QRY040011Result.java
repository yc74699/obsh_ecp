package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;

public class QRY040011Result extends BaseServiceInvocationResult
{
	private List<PkgDetail> pkgDetail = new ArrayList<PkgDetail>();

	public void setPkgDetail(List<PkgDetail> pkgDetail)
	{
		this.pkgDetail = pkgDetail;
	}

	public List<PkgDetail> getPkgDetail()
	{
		return this.pkgDetail;
	}

}