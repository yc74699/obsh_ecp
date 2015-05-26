package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010026Result;

public interface IQueryAgentSeqService
{
	public QRY010026Result queryAgentSeq(String id_type, String user_id, long ad_yearmonthday, long aquery_start_date, long aquery_end_date, long wfseq_type, String acctbkpayseq_undo_flag) throws LIException;

}