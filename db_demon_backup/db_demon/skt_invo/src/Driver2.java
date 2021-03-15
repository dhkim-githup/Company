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
wsReq.setUserId("");
wsReq.setLocaleXd("");
wsReq.setTerminalId("");

FXYZINVSaveParameterINPUTRecord[] fXYZINVSaveParameter = new FXYZINVSaveParameterINPUTRecord[10];


fXYZINVSaveParameter[0] = new FXYZINVSaveParameterINPUTRecord();
fXYZINVSaveParameter[0].setCNTRT_NO("4260104641");
fXYZINVSaveParameter[0].setINFKEY("F000000059");
fXYZINVSaveParameter[0].setINSP_REQ_DATE("20180101");
fXYZINVSaveParameter[0].setINSP_REQ_PIC("지은철");
fXYZINVSaveParameter[0].setINSP_REQ_PIC_MOBILE_NO("01045628921");
fXYZINVSaveParameter[0].setINV_AMT("10000");
fXYZINVSaveParameter[0].setINV_NAME("최종 행복나래 연동 테스트");
fXYZINVSaveParameter[0].setINSP_REQ_PIC_EMAIL("ivz@naver.com");
fXYZINVSaveParameter[0].setITEM_SEQ("1");


FXYZINVSaveReturn asdf = oparam.fXYZINVSave(fXYZINVSaveParameter, wsReq);

System.out.println("INFKEY :::::::::::::::: "+asdf.getINFKEY());
System.out.println("INFKEY :::::::::::::::: "+asdf.getRESULT());
System.out.println("INFKEY :::::::::::::::: "+asdf.getMSG());


} catch (RemoteException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}



}

}
