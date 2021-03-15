package com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv;

public class MROK_0089_PerfDataPoRcvSoapProxy implements com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap {
  private String _endpoint = null;
  private com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap mROK_0089_PerfDataPoRcvSoap = null;
  
  public MROK_0089_PerfDataPoRcvSoapProxy() {
    _initMROK_0089_PerfDataPoRcvSoapProxy();
  }
  
  public MROK_0089_PerfDataPoRcvSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initMROK_0089_PerfDataPoRcvSoapProxy();
  }
  
  private void _initMROK_0089_PerfDataPoRcvSoapProxy() {
    try {
      mROK_0089_PerfDataPoRcvSoap = (new com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvLocator()).getMROK_0089_PerfDataPoRcvSoap();
      if (mROK_0089_PerfDataPoRcvSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mROK_0089_PerfDataPoRcvSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mROK_0089_PerfDataPoRcvSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mROK_0089_PerfDataPoRcvSoap != null)
      ((javax.xml.rpc.Stub)mROK_0089_PerfDataPoRcvSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.MROK_0089_PerfDataPoRcvSoap getMROK_0089_PerfDataPoRcvSoap() {
    if (mROK_0089_PerfDataPoRcvSoap == null)
      _initMROK_0089_PerfDataPoRcvSoapProxy();
    return mROK_0089_PerfDataPoRcvSoap;
  }
  
  public java.lang.String submitDocument_0089() throws java.rmi.RemoteException{
    if (mROK_0089_PerfDataPoRcvSoap == null)
      _initMROK_0089_PerfDataPoRcvSoapProxy();
    return mROK_0089_PerfDataPoRcvSoap.submitDocument_0089();
  }
  
  
}