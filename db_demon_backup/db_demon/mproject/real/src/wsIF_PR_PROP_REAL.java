/*
 * dhkim 2012.07.16
 * PR �� ���� ���ݼۺ� , ������ ȣ��
 */


import com.mrok.schema.MROKGW.EPR.MROK_0092_PurchActReqRcv.*;

public class wsIF_PR_PROP_REAL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MROK_0092_PurchActReqRcvSoapProxy proxy = new MROK_0092_PurchActReqRcvSoapProxy();


		String rtn="";
		try{
			rtn = proxy.submitDocument_0092();

			System.out.println("RTN_CODE:"+rtn);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
