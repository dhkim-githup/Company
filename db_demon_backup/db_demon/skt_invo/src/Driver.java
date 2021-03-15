import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.skt.open2u.xyz.xyzbhappynarae.FXYZINVSaveParameterINPUTRecord;
import com.skt.open2u.xyz.xyzbhappynarae.FXYZINVSaveReturn;
import com.skt.open2u.xyz.xyzbhappynarae.XYZBHappyNarae;
import com.skt.open2u.xyz.xyzbhappynarae.XYZBHappyNaraeProxy;
import common.types.WsRequestContext;

public class Driver {

public static void main(String[] args) {
// TODO Auto-generated method stub



try {

XYZBHappyNaraeProxy proxyid =  new XYZBHappyNaraeProxy();




proxyid.setEndpoint("http://open2u.sktelecom.com/web/services/WsFrontController");


XYZBHappyNarae oparam = proxyid.getXYZBHappyNarae();

WsRequestContext wsReq =  new WsRequestContext(); 
wsReq.setBranchCode("");
wsReq.setUserId("V2012010927260");
wsReq.setLocaleXd("");
wsReq.setTerminalId("HappyNarae");

FXYZINVSaveParameterINPUTRecord[] fXYZINVSaveParameter = new FXYZINVSaveParameterINPUTRecord[10];

for (int i = 1; i < 3 ; i++) {
fXYZINVSaveParameter[i] = new FXYZINVSaveParameterINPUTRecord();
fXYZINVSaveParameter[i].setCNTRT_NO("4161100000");
fXYZINVSaveParameter[i].setINFKEY("if1111110101");
fXYZINVSaveParameter[i].setINSP_REQ_DATE("20180101");
fXYZINVSaveParameter[i].setINSP_REQ_PIC("지은철");
fXYZINVSaveParameter[i].setINSP_REQ_PIC_MOBILE_NO("01045628921");
fXYZINVSaveParameter[i].setINV_AMT("10000");
fXYZINVSaveParameter[i].setINV_NAME("검사조서 건명:::"+i);
fXYZINVSaveParameter[i].setINSP_REQ_PIC_EMAIL("ivz@naver.com");
fXYZINVSaveParameter[i].setITEM_SEQ(i+"");
}

FXYZINVSaveReturn asdf = oparam.fXYZINVSave(fXYZINVSaveParameter, wsReq);

System.out.println("INFKEY :::::::::::::::: "+asdf.getINFKEY()+" RESULT ::::::::::::  "+asdf.getRESULT()+"   MSG :::::::::::: "+asdf.getMSG());





} catch (RemoteException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}



}

}
