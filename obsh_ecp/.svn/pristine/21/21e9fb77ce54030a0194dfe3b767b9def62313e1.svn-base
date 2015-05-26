/**
 * NetWorkAccessPointServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.xwtech.xwecp.communication.ws.gimsClient;

public class NetWorkAccessPointServiceLocator extends org.apache.axis.client.Service implements com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPointService {

    public NetWorkAccessPointServiceLocator() {
    }


    public NetWorkAccessPointServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NetWorkAccessPointServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NetWorkAccessService
    private java.lang.String NetWorkAccessService_address = "http://10.40.95.21/Gims/services/NetWorkAccessService";

    public java.lang.String getNetWorkAccessServiceAddress() {
        return NetWorkAccessService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NetWorkAccessServiceWSDDServiceName = "NetWorkAccessService";

    public java.lang.String getNetWorkAccessServiceWSDDServiceName() {
        return NetWorkAccessServiceWSDDServiceName;
    }

    public void setNetWorkAccessServiceWSDDServiceName(java.lang.String name) {
        NetWorkAccessServiceWSDDServiceName = name;
    }

    public com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPoint getNetWorkAccessService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NetWorkAccessService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNetWorkAccessService(endpoint);
    }

    public com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPoint getNetWorkAccessService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessServiceSoapBindingStub _stub = new com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getNetWorkAccessServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNetWorkAccessServiceEndpointAddress(java.lang.String address) {
        NetWorkAccessService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessPoint.class.isAssignableFrom(serviceEndpointInterface)) {
            	com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessServiceSoapBindingStub _stub = new com.xwtech.xwecp.communication.ws.gimsClient.NetWorkAccessServiceSoapBindingStub(new java.net.URL(NetWorkAccessService_address), this);
                _stub.setPortName(getNetWorkAccessServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("NetWorkAccessService".equals(inputPortName)) {
            return getNetWorkAccessService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.gxlu.com.cn/Gims/", "NetWorkAccessPointService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.gxlu.com.cn/Gims/", "NetWorkAccessService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NetWorkAccessService".equals(portName)) {
            setNetWorkAccessServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
