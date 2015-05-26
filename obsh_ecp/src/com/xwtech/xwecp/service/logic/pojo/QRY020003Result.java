package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PkgUseState;

public class QRY020003Result extends BaseServiceInvocationResult
{
	private List<PkgUseState> pkgUseState = new ArrayList<PkgUseState>();

	public void setPkgUseState(List<PkgUseState> pkgUseState)
	{
		this.pkgUseState = pkgUseState;
	}

	public List<PkgUseState> getPkgUseState()
	{
		return this.pkgUseState;
	}

}