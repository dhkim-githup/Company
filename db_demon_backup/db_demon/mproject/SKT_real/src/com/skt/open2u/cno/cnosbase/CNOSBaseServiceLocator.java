/**
 * CNOSBaseServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.skt.open2u.cno.cnosbase;

public class CNOSBaseServiceLocator extends org.apache.axis.client.Service implements com.skt.open2u.cno.cnosbase.CNOSBaseService {

    public CNOSBaseServiceLocator() {
    }


    public CNOSBaseServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CNOSBaseServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CNOSBase
    private java.lang.String CNOSBase_address = "http://localhost:7001/web/services/WsFrontController";

    public java.lang.String getCNOSBaseAddress() {
        return CNOSBase_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CNOSBaseWSDDServiceName = "CNOSBase";

    public java.lang.String getCNOSBaseWSDDServiceName() {
        return CNOSBaseWSDDServiceName;
    }

    public void setCNOSBaseWSDDServiceName(java.lang.String name) {
        CNOSBaseWSDDServiceName = name;
    }

    public com.skt.open2u.cno.cnosbase.CNOSBase getCNOSBase() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CNOSBase_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCNOSBase(endpoint);
    }

    public com.skt.open2u.cno.cnosbase.CNOSBase getCNOSBase(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.skt.open2u.cno.cnosbase.CNOSBaseSoapBindingStub _stub = new com.skt.open2u.cno.cnosbase.CNOSBaseSoapBindingStub(portAddress, this);
            _stub.setPortName(getCNOSBaseWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCNOSBaseEndpointAddress(java.lang.String address) {
        CNOSBase_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.skt.open2u.cno.cnosbase.CNOSBase.class.isAssignableFrom(serviceEndpointInterface)) {
                com.skt.open2u.cno.cnosbase.CNOSBaseSoapBindingStub _stub = new com.skt.open2u.cno.cnosbase.CNOSBaseSoapBindingStub(new java.net.URL(CNOSBase_address), this);
                _stub.setPortName(getCNOSBaseWSDDServiceName());
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
        if ("CNOSBase".equals(inputPortName)) {
            return getCNOSBase();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("componentNs:cnosbase.cno.open2u.skt.com/cno.CNOSBase", "CNOSBaseService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("componentNs:cnosbase.cno.open2u.skt.com/cno.CNOSBase", "CNOSBase"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CNOSBase".equals(portName)) {
            setCNOSBaseEndpointAddress(address);
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
