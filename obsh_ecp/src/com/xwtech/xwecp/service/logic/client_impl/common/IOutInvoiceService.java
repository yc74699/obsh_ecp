package com.xwtech.xwecp.service.logic.client_impl.common;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050072Result;

public interface IOutInvoiceService
{
	public QRY050072Result OutInvoice(String edition_no, String invoice_begin_no, String invoice_end_no, String operid) throws LIException;

}