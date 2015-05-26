package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.RelationNum;
import com.xwtech.xwecp.service.logic.pojo.PkgUse;

public class QRY050001Result extends BaseServiceInvocationResult
{
	private List<RelationNum> relationNum = new ArrayList<RelationNum>();

	private List<PkgUse> pkgUse = new ArrayList<PkgUse>();
	
	private List<PkgDetail> pkgDetail = new ArrayList<PkgDetail>();

	public void setRelationNum(List<RelationNum> relationNum)
	{
		this.relationNum = relationNum;
	}

	public List<RelationNum> getRelationNum()
	{
		return this.relationNum;
	}

	public void setPkgUse(List<PkgUse> pkgUse)
	{
		this.pkgUse = pkgUse;
	}

	public List<PkgUse> getPkgUse()
	{
		return this.pkgUse;
	}

	public List<PkgDetail> getPkgDetail() {
		return pkgDetail;
	}

	public void setPkgDetail(List<PkgDetail> pkgDetail) {
		this.pkgDetail = pkgDetail;
	}
}