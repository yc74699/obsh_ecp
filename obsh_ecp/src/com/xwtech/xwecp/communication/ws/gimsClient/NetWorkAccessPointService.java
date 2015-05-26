/**
 * NetWorkAccessPointService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.xwtech.xwecp.communication.ws.gimsClient;

public interface NetWorkAccessPointService extends javax.xml.rpc.Service {
    public java.lang.String getNetWorkAccessServiceAddress();

    public com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPoint getNetWorkAccessService() throws javax.xml.rpc.ServiceException;

    public com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPoint getNetWorkAccessService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
