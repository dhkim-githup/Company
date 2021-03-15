package com.skt.open2u.cno.cnosbase;

public class CNOSBaseProxy implements com.skt.open2u.cno.cnosbase.CNOSBase {
  private String _endpoint = null;
  private com.skt.open2u.cno.cnosbase.CNOSBase cNOSBase = null;
  
  public CNOSBaseProxy() {
    _initCNOSBaseProxy();
  }
  
  public CNOSBaseProxy(String endpoint) {
    _endpoint = endpoint;
    _initCNOSBaseProxy();
  }
  
  private void _initCNOSBaseProxy() {
    try {
      cNOSBase = (new com.skt.open2u.cno.cnosbase.CNOSBaseServiceLocator()).getCNOSBase();
      if (cNOSBase != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cNOSBase)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cNOSBase)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cNOSBase != null)
      ((javax.xml.rpc.Stub)cNOSBase)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.skt.open2u.cno.cnosbase.CNOSBase getCNOSBase() {
    if (cNOSBase == null)
      _initCNOSBaseProxy();
    return cNOSBase;
  }
  
  public com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturn fCNOHappynaraePoList(com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListParameter fCNOHappynaraePoListParameter, common.types.WsRequestContext requestContext) throws java.rmi.RemoteException{
    if (cNOSBase == null)
      _initCNOSBaseProxy();
    return cNOSBase.fCNOHappynaraePoList(fCNOHappynaraePoListParameter, requestContext);
  }
  
  public com.skt.open2u.cno.cnosbase.FCNOHappynaraGrListReturn fCNOHappynaraGrList(com.skt.open2u.cno.cnosbase.FCNOHappynaraGrListParameter fCNOHappynaraGrListParameter, common.types.WsRequestContext requestContext) throws java.rmi.RemoteException{
    if (cNOSBase == null)
      _initCNOSBaseProxy();
    return cNOSBase.fCNOHappynaraGrList(fCNOHappynaraGrListParameter, requestContext);
  }
  
  
}