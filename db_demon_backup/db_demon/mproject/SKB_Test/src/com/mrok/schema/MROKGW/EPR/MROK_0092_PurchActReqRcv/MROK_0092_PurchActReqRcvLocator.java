/**
 * MROK_0092_PurchActReqRcvLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv;

public class MROK_0092_PurchActReqRcvLocator extends org.apache.axis.client.Service implements com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcv {

    public MROK_0092_PurchActReqRcvLocator() {
    }


    public MROK_0092_PurchActReqRcvLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MROK_0092_PurchActReqRcvLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MROK_0092_PurchActReqRcvSoap
    private java.lang.String MROK_0092_PurchActReqRcvSoap_address = "http://210.211.67.201/MROK_WS_DEV/MROK_0092_PurchActReqRcv/MROK_0092_PurchActReqRcv.asmx";

    public java.lang.String getMROK_0092_PurchActReqRcvSoapAddress() {
        return MROK_0092_PurchActReqRcvSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MROK_0092_PurchActReqRcvSoapWSDDServiceName = "MROK_0092_PurchActReqRcvSoap";

    public java.lang.String getMROK_0092_PurchActReqRcvSoapWSDDServiceName() {
        return MROK_0092_PurchActReqRcvSoapWSDDServiceName;
    }

    public void setMROK_0092_PurchActReqRcvSoapWSDDServiceName(java.lang.String name) {
        MROK_0092_PurchActReqRcvSoapWSDDServiceName = name;
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoap getMROK_0092_PurchActReqRcvSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MROK_0092_PurchActReqRcvSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMROK_0092_PurchActReqRcvSoap(endpoint);
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoap getMROK_0092_PurchActReqRcvSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoapStub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoapStub(portAddress, this);
            _stub.setPortName(getMROK_0092_PurchActReqRcvSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMROK_0092_PurchActReqRcvSoapEndpointAddress(java.lang.String address) {
        MROK_0092_PurchActReqRcvSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoapStub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoapStub(new java.net.URL(MROK_0092_PurchActReqRcvSoap_address), this);
                _stub.setPortName(getMROK_0092_PurchActReqRcvSoapWSDDServiceName());
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
        if ("MROK_0092_PurchActReqRcvSoap".equals(inputPortName)) {
            return getMROK_0092_PurchActReqRcvSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0092_PurchActReqRcv", "MROK_0092_PurchActReqRcv");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0092_PurchActReqRcv", "MROK_0092_PurchActReqRcvSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MROK_0092_PurchActReqRcvSoap".equals(portName)) {
            setMROK_0092_PurchActReqRcvSoapEndpointAddress(address);
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
