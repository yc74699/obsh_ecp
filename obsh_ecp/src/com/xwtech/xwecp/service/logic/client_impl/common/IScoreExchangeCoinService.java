package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.ScoreChangeCoin;
import com.xwtech.xwecp.service.logic.pojo.DEL020003Result;

public interface IScoreExchangeCoinService
{
	public DEL020003Result scoreExchangeCoin(String smsFlag, String scoreExchangeCatalog, String ddrCity, String scoreGsmUserId, String scoreDescription, String scoreDetailDestUserId, List<ScoreChangeCoin> scoreChangeCoins) throws LIException;

}