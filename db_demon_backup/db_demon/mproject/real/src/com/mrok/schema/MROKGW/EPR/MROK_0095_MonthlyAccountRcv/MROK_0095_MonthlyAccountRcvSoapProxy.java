package com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv;

public class MROK_0095_MonthlyAccountRcvSoapProxy implements com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoap {
  private String _endpoint = null;
  private com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoap mROK_0095_MonthlyAccountRcvSoap = null;
  
  public MROK_0095_MonthlyAccountRcvSoapProxy() {
    _initMROK_0095_MonthlyAccountRcvSoapProxy();
  }
  
  public MROK_0095_MonthlyAccountRcvSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initMROK_0095_MonthlyAccountRcvSoapProxy();
  }
  
  private void _initMROK_0095_MonthlyAccountRcvSoapProxy() {
    try {
      mROK_0095_MonthlyAccountRcvSoap = (new com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvLocator()).getMROK_0095_MonthlyAccountRcvSoap();
      if (mROK_0095_MonthlyAccountRcvSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mROK_0095_MonthlyAccountRcvSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mROK_0095_MonthlyAccountRcvSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mROK_0095_MonthlyAccountRcvSoap != null)
      ((javax.xml.rpc.Stub)mROK_0095_MonthlyAccountRcvSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.MROK_0095_MonthlyAccountRcvSoap getMROK_0095_MonthlyAccountRcvSoap() {
    if (mROK_0095_MonthlyAccountRcvSoap == null)
      _initMROK_0095_MonthlyAccountRcvSoapProxy();
    return mROK_0095_MonthlyAccountRcvSoap;
  }
  
  public java.lang.String submitDocument_0095() throws java.rmi.RemoteException{
    if (mROK_0095_MonthlyAccountRcvSoap == null)
      _initMROK_0095_MonthlyAccountRcvSoapProxy();
    return mROK_0095_MonthlyAccountRcvSoap.submitDocument_0095();
  }
  
  
}