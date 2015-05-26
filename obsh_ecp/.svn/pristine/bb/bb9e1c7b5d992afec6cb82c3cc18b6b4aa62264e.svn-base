package com.xwtech.xwecp.communication.ws.gimsClient;


public class Test {
		
	public static void main(String[] args) throws Exception
	{
		final String req = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Header/><SOAP-ENV:Body><GetNetWorkAccess.Request><UptownId>362413</UptownId><AddressId>33326929</AddressId></GetNetWorkAccess.Request></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		NetWorkAccessPointServiceLocator  npl = new NetWorkAccessPointServiceLocator();
		NetWorkAccessPoint np = (NetWorkAccessPoint)npl.getNetWorkAccessService();
		System.out.print(np.getNetWorkAccess(req));
	}

}

