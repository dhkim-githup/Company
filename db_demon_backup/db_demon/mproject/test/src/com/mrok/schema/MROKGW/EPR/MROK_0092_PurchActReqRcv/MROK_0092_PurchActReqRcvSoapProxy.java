package com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv;

public class MROK_0092_PurchActReqRcvSoapProxy implements com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoap {
  private String _endpoint = null;
  private com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoap mROK_0092_PurchActReqRcvSoap = null;
  
  public MROK_0092_PurchActReqRcvSoapProxy() {
    _initMROK_0092_PurchActReqRcvSoapProxy();
  }
  
  public MROK_0092_PurchActReqRcvSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initMROK_0092_PurchActReqRcvSoapProxy();
  }
  
  private void _initMROK_0092_PurchActReqRcvSoapProxy() {
    try {
      mROK_0092_PurchActReqRcvSoap = (new com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvLocator()).getMROK_0092_PurchActReqRcvSoap();
      if (mROK_0092_PurchActReqRcvSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mROK_0092_PurchActReqRcvSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mROK_0092_PurchActReqRcvSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mROK_0092_PurchActReqRcvSoap != null)
      ((javax.xml.rpc.Stub)mROK_0092_PurchActReqRcvSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.MROK_0092_PurchActReqRcvSoap getMROK_0092_PurchActReqRcvSoap() {
    if (mROK_0092_PurchActReqRcvSoap == null)
      _initMROK_0092_PurchActReqRcvSoapProxy();
    return mROK_0092_PurchActReqRcvSoap;
  }
  
  public java.lang.String submitDocument_0092() throws java.rmi.RemoteException{
    if (mROK_0092_PurchActReqRcvSoap == null)
      _initMROK_0092_PurchActReqRcvSoapProxy();
    return mROK_0092_PurchActReqRcvSoap.submitDocument_0092();
  }
  
  
}