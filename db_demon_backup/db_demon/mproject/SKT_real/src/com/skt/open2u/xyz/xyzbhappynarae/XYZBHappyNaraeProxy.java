package com.skt.open2u.xyz.xyzbhappynarae;

public class XYZBHappyNaraeProxy implements com.skt.open2u.xyz.xyzbhappynarae.XYZBHappyNarae {
  private String _endpoint = null;
  private com.skt.open2u.xyz.xyzbhappynarae.XYZBHappyNarae xYZBHappyNarae = null;
  
  public XYZBHappyNaraeProxy() {
    _initXYZBHappyNaraeProxy();
  }
  
  public XYZBHappyNaraeProxy(String endpoint) {
    _endpoint = endpoint;
    _initXYZBHappyNaraeProxy();
  }
  
  private void _initXYZBHappyNaraeProxy() {
    try {
      xYZBHappyNarae = (new com.skt.open2u.xyz.xyzbhappynarae.XYZBHappyNaraeServiceLocator()).getXYZBHappyNarae();
      if (xYZBHappyNarae != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)xYZBHappyNarae)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)xYZBHappyNarae)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (xYZBHappyNarae != null)
      ((javax.xml.rpc.Stub)xYZBHappyNarae)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.skt.open2u.xyz.xyzbhappynarae.XYZBHappyNarae getXYZBHappyNarae() {
    if (xYZBHappyNarae == null)
      _initXYZBHappyNaraeProxy();
    return xYZBHappyNarae;
  }
  
  public com.skt.open2u.xyz.xyzbhappynarae.FXYZINVSaveReturn fXYZINVSave(com.skt.open2u.xyz.xyzbhappynarae.FXYZINVSaveParameterINPUTRecord[] fXYZINVSaveParameter, common.types.WsRequestContext requestContext) throws java.rmi.RemoteException{
    if (xYZBHappyNarae == null)
      _initXYZBHappyNaraeProxy();
    return xYZBHappyNarae.fXYZINVSave(fXYZINVSaveParameter, requestContext);
  }
  
  
}