package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ColumNar;

public class QRY010024Result extends BaseServiceInvocationResult
{
	private List<ColumNar> columNars = new ArrayList<ColumNar>();

	public void setColumNars(List<ColumNar> columNars)
	{
		this.columNars = columNars;
	}

	public List<ColumNar> getColumNars()
	{
		return this.columNars;
	}

}