
import java.io.ByteArrayOutputStream;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.io.BufferedReader;


public class Demon_ebill_mail_send1 {
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_ebill_mail_send";

    // 웹 서버로 부터 받은 웹 페이지 결과를 콘솔에 출력하는 메소드
    public static void printByInputStream(InputStream is) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];
        int len = -1;
        try {
            while((len = is.read(buf, 0, buf.length)) != -1) {
            	//System.out.write(buf, 0, len);
            	baos.write(buf, 0, len);
            }
            String rtnMsg = new String(baos.toByteArray(), 0, baos.size(), "UTF-8");
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
	public void checkAlive(String sUrl){
        URL url = null;
        URLConnection urlConnection = null;

        // URL 주소
        if(sUrl == null || "".equals(sUrl)){
        	sUrl = "http://localhost/ebillMailSend.do";
        	//OS check
    		Properties p = System.getProperties();
    		String osName = (String)p.get("os.name");
			if(!osName.startsWith("Windows")){
				//sUrl = "http://esign.happynarae.co.kr/ntsSendBatch.do";
				//sUrl = "http://121.78.69.199/ntsSendBatch.do";
				sUrl = "http://172.16.1.183/ebillMailSend.do";
			}
			
			sUrl ="http://210.5.158.31:9011/hy/?uid=80090&auth=AEDEE49EB5AA955A2AD65FBE2A5112A1&mobile=18651517737&msg=%5B%E5%A5%BD%E4%BD%A9%E7%BA%B3%E7%91%9E%5D%E8%AE%A2%E8%B4%A7%E6%8E%A5%E6%94%B6%E5%AE%8C%E6%88%90.&expid=0&encode=utf-8";
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
            //printByInputStream(urlConnection.getInputStream());
			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String rVal = "";
			String line = "";
			while( ( line = rd.readLine() ) != null ){
				rVal = line;
			}
			System.out.println(rVal);
			rd.close();
			
        } catch(Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("-------------Start ebillMailSend-------------");

		String sUrl = "";
		sUrl = args.length > 0 ? "".equals(args[0]) ? "" : args[0] : "";

		Demon_ebill_mail_send1 demon = new Demon_ebill_mail_send1();
		demon.checkAlive(sUrl);
		
		System.out.println("-------------End ebillMailSend-------------");
	}
}
