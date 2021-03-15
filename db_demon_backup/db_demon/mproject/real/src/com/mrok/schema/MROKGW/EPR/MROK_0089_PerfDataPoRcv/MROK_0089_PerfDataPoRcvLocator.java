/**
 * MROK_0089_PerfDataPoRcvLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv;

public class MROK_0089_PerfDataPoRcvLocator extends org.apache.axis.client.Service implements com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcv {

    public MROK_0089_PerfDataPoRcvLocator() {
    }


    public MROK_0089_PerfDataPoRcvLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MROK_0089_PerfDataPoRcvLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MROK_0089_PerfDataPoRcvSoap
    private java.lang.String MROK_0089_PerfDataPoRcvSoap_address = "http://210.211.67.201/MROK_WS_PRD/MROK_0089_PerfDataPoRcv/MROK_0089_PerfDataPoRcv.asmx";

    public java.lang.String getMROK_0089_PerfDataPoRcvSoapAddress() {
        return MROK_0089_PerfDataPoRcvSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MROK_0089_PerfDataPoRcvSoapWSDDServiceName = "MROK_0089_PerfDataPoRcvSoap";

    public java.lang.String getMROK_0089_PerfDataPoRcvSoapWSDDServiceName() {
        return MROK_0089_PerfDataPoRcvSoapWSDDServiceName;
    }

    public void setMROK_0089_PerfDataPoRcvSoapWSDDServiceName(java.lang.String name) {
        MROK_0089_PerfDataPoRcvSoapWSDDServiceName = name;
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap getMROK_0089_PerfDataPoRcvSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MROK_0089_PerfDataPoRcvSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMROK_0089_PerfDataPoRcvSoap(endpoint);
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap getMROK_0089_PerfDataPoRcvSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoapStub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoapStub(portAddress, this);
            _stub.setPortName(getMROK_0089_PerfDataPoRcvSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMROK_0089_PerfDataPoRcvSoapEndpointAddress(java.lang.String address) {
        MROK_0089_PerfDataPoRcvSoap_address = address;
    }


    // Use to get a proxy class for MROK_0089_PerfDataPoRcvSoap12
    private java.lang.String MROK_0089_PerfDataPoRcvSoap12_address = "http://210.211.67.201/MROK_WS_PRD/MROK_0089_PerfDataPoRcv/MROK_0089_PerfDataPoRcv.asmx";

    public java.lang.String getMROK_0089_PerfDataPoRcvSoap12Address() {
        return MROK_0089_PerfDataPoRcvSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MROK_0089_PerfDataPoRcvSoap12WSDDServiceName = "MROK_0089_PerfDataPoRcvSoap12";

    public java.lang.String getMROK_0089_PerfDataPoRcvSoap12WSDDServiceName() {
        return MROK_0089_PerfDataPoRcvSoap12WSDDServiceName;
    }

    public void setMROK_0089_PerfDataPoRcvSoap12WSDDServiceName(java.lang.String name) {
        MROK_0089_PerfDataPoRcvSoap12WSDDServiceName = name;
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap getMROK_0089_PerfDataPoRcvSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MROK_0089_PerfDataPoRcvSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMROK_0089_PerfDataPoRcvSoap12(endpoint);
    }

    public com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap getMROK_0089_PerfDataPoRcvSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap12Stub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap12Stub(portAddress, this);
            _stub.setPortName(getMROK_0089_PerfDataPoRcvSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMROK_0089_PerfDataPoRcvSoap12EndpointAddress(java.lang.String address) {
        MROK_0089_PerfDataPoRcvSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoapStub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoapStub(new java.net.URL(MROK_0089_PerfDataPoRcvSoap_address), this);
                _stub.setPortName(getMROK_0089_PerfDataPoRcvSoapWSDDServiceName());
                return _stub;
            }
            if (com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap12Stub _stub = new com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap12Stub(new java.net.URL(MROK_0089_PerfDataPoRcvSoap12_address), this);
                _stub.setPortName(getMROK_0089_PerfDataPoRcvSoap12WSDDServiceName());
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
        if ("MROK_0089_PerfDataPoRcvSoap".equals(inputPortName)) {
            return getMROK_0089_PerfDataPoRcvSoap();
        }
        else if ("MROK_0089_PerfDataPoRcvSoap12".equals(inputPortName)) {
            return getMROK_0089_PerfDataPoRcvSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0089_PerfDataPoRcv", "MROK_0089_PerfDataPoRcv");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0089_PerfDataPoRcv", "MROK_0089_PerfDataPoRcvSoap"));
            ports.add(new javax.xml.namespace.QName("http://schema.mrok.com/MROKGW/EPR/MROK_0089_PerfDataPoRcv", "MROK_0089_PerfDataPoRcvSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MROK_0089_PerfDataPoRcvSoap".equals(portName)) {
            setMROK_0089_PerfDataPoRcvSoapEndpointAddress(address);
        }
        else 
if ("MROK_0089_PerfDataPoRcvSoap12".equals(portName)) {
            setMROK_0089_PerfDataPoRcvSoap12EndpointAddress(address);
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
