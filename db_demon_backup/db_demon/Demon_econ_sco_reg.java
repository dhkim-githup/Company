import java.sql.SQLException;
import java.util.Properties;

import comm.mk_log;
import java.net.*;
import java.io.*;




public class Demon_econ_sco_reg {
	private comm.DB_Use_Db_Demon  db ;  
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_ebill_nts_send";
	
	
    // 웹 서버로 부터 받은 웹 페이지 결과를 콘솔에 출력하는 메소드
    public static void printByInputStream(InputStream is) {
        byte[] buf = new byte[1024];
        int len = -1;
       
        try {
            while((len = is.read(buf, 0, buf.length)) != -1) {
                System.out.write(buf, 0, len);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
 
    // 웹 서버로 파라미터명과 값의 쌍을 전송하는 메소드
    public static void printByOutputStream(OutputStream os, String msg) {
        try {
            byte[] msgBuf = msg.getBytes("UTF-8");
            os.write(msgBuf, 0, msgBuf.length);
            os.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * @param sSco_cd supply company code
	 */
	public void run(String sUrl){
    	
        URL url = null;
        URLConnection urlConnection = null;
       
        // URL 주소
        if(sUrl == null || "".equals(sUrl)){
        	sUrl = "http://localhost/econScoRegBatch.do";
        	
        	//OS check
    		Properties p = System.getProperties();
    		String osName = (String)p.get("os.name");
			if(!osName.startsWith("Windows")){
				//sUrl = "http://esign.happynarae.co.kr/ntsSendBatch.do";
				//sUrl = "http://121.78.69.199/ntsSendBatch.do";
				sUrl = "http://172.16.1.183/econScoRegBatch.do";
			}
        }
 
        // 파라미터 이름
        String paramName = "animal";
 
        // 파라미터 이름에 대한 값
        String paramValue = "dog";
 
        try {
            // Get방식으로 전송 하기
//            url = new URL(sUrl + "?" + paramName + "=" + paramValue);
//            urlConnection = url.openConnection();
//            printByInputStream(urlConnection.getInputStream());
            
            // Post방식으로 전송 하기
            url = new URL(sUrl);
            urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            
            //printByOutputStream(urlConnection.getOutputStream(), paramName + "=" + paramValue);
            printByInputStream(urlConnection.getInputStream());
            
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("-------------Start Econ Sco Reg-------------");
		log.Write(log_flag, prgm_nm, "-------------Start Econ Sco Reg-------------");

		String sUrl = "";
		sUrl = args.length > 0 ? "".equals(args[0]) ? "" : args[0] : "";
		
		Demon_econ_sco_reg demon = new Demon_econ_sco_reg();
		
		demon.run(sUrl);
		
		log.Write(log_flag, prgm_nm, "-------------End Econ Sco Reg-------------");
		System.out.println("-------------End Econ Sco Reg-------------");
	}

}
