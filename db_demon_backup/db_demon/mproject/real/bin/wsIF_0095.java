/*
 * dhkim 2017.07.10
 * SK네트웍스 월1회 실적송부 
 */



import com.mrok.schema.MROKGW.EPR.MROK_0095_MonthlyAccountRcv.*;

public class wsIF_0095 {

	     /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub
        	MROK_0095_MonthlyAccountRcvSoapProxy proxy = new MROK_0095_MonthlyAccountRcvSoapProxy();


                String rtn="";
                try{
                        rtn = proxy.submitDocument_0095();

                        System.out.println("RTN_CODE:"+rtn);
                }catch(Exception e){
                        e.printStackTrace();
                }
        }
}