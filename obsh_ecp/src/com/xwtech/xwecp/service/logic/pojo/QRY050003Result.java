package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.GamePoint;

public class QRY050003Result extends BaseServiceInvocationResult
{
	private List<GamePoint> gamePoint = new ArrayList<GamePoint>();

	public void setGamePoint(List<GamePoint> gamePoint)
	{
		this.gamePoint = gamePoint;
	}

	public List<GamePoint> getGamePoint()
	{
		return this.gamePoint;
	}

}