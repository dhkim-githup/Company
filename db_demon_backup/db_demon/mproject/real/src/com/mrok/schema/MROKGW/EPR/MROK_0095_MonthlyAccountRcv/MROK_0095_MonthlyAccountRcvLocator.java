/**
 * MROK_0095_MonthlyAccountRcvLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv;

public class MROK_0095_MonthlyAccountRcvLocator extends org.apache.axis.client.Service implements com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcv {

    public MROK_0095_MonthlyAccountRcvLocator() {
    }


    public MROK_0095_MonthlyAccountRcvLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MROK_0095_MonthlyAccountRcvLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MROK_0095_MonthlyAccountRcvSoap
    private java.lang.String MROK_0095_MonthlyAccountRcvSoap_address = "http://210.211.67.201/MROK_WS_PRD/MROK_0095_MonthlyAccountRcv/MROK_0095_MonthlyAccountRcv.asmx";

    public java.lang.String getMROK_0095_MonthlyAccountRcvSoapAddress() {
        return MROK_0095_MonthlyAccountRcvSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MROK_0095_MonthlyAccountRcvSoapWSDDServiceName = "MROK_0095_MonthlyAccountRcvSoap";

    public java.lang.String getMROK_0095_MonthlyAccountRcvSoapWSDDServiceName() {
        return MROK_0095_MonthlyAccountRcvSoapWSDDServiceName;
    }

    public void setMROK_0095_MonthlyAccountRcvSoapWSDDServiceName(java.lang.String name) {
        MROK_0095_MonthlyAccountRcvSoapWSDDServiceName = name;
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoap getMROK_0095_MonthlyAccountRcvSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MROK_0095_MonthlyAccountRcvSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMROK_0095_MonthlyAccountRcvSoap(endpoint);
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoap getMROK_0095_MonthlyAccountRcvSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoapStub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoapStub(portAddress, this);
            _stub.setPortName(getMROK_0095_MonthlyAccountRcvSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMROK_0095_MonthlyAccountRcvSoapEndpointAddress(java.lang.String address) {
        MROK_0095_MonthlyAccountRcvSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoapStub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoapStub(new java.net.URL(MROK_0095_MonthlyAccountRcvSoap_address), this);
                _stub.setPortName(getMROK_0095_MonthlyAccountRcvSoapWSDDServiceName());
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
        if ("MROK_0095_MonthlyAccountRcvSoap".equals(inputPortName)) {
            return getMROK_0095_MonthlyAccountRcvSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0095_MonthlyAccountRcv", "MROK_0095_MonthlyAccountRcv");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0095_MonthlyAccountRcv", "MROK_0095_MonthlyAccountRcvSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MROK_0095_MonthlyAccountRcvSoap".equals(portName)) {
            setMROK_0095_MonthlyAccountRcvSoapEndpointAddress(address);
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
