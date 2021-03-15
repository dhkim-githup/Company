/*
 * dhkim 2012.07.16
 * 실적데이타 관련 웹서비스 호출
 */



import com.mrok.schema.MROKGW.EPR.MROK_0089_PerfDataPoRcv.*;

public class wsIF_STA_PO_REAL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MROK_0089_PerfDataPoRcvSoapProxy proxy = new MROK_0089_PerfDataPoRcvSoapProxy();
		String rtn="";
		try{
			rtn = proxy.submitDocument_0089();
			System.out.println("RTN_CODE:"+rtn);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
