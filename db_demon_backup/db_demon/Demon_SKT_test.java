import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Demon_SKT_test {
	
	static public String prgm_nm = "Demon_skcnc_stat_check";

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("-------------Start skcnc stat check-------------");

		Demon_SKT_test demon = new Demon_SKT_test();

        HashMap<String, String> hm = new HashMap();
    	String link_skcnc = "";
    	link_skcnc = "https://open2u.sktelecom.com/web/services/WsFrontController"; //cnc_�ּҺ��� kimjs 20170906 (CNC ����IP����)
    		    
    	
    	URL url = null;
    	HttpURLConnection urlConnection = null;
    	
    	// URL �ּ�
    	String sUrl = link_skcnc;
    	      
    	// �Ķ���� �̸�
    	String param = "";        
        
    	System.out.println("url:"+sUrl+param);
    	
    	String sErrMsg = "";
    	String rtnMsg = "";
    	String status = "";
    	String err_msg = "";
    	boolean isError = true;
    	try {
    	
    	    // Post������� ���� �ϱ�
    	    //url = new URL(sUrl+param);
    	    //urlConnection = url.openConnection();
    	    urlConnection = (HttpURLConnection) new URL(sUrl+param).openConnection();
    	    urlConnection.connect();
    	    
    	    //urlConnection.setDoOutput(true);
    	    //urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
    	    //urlConnection.setRequestProperty("Accept","*/*");     	    
    	    
    	    //InputStream is = urlConnection.getInputStream();
    	    
    	    isError = urlConnection.getResponseCode() >= 400;
    	    InputStream is = isError ? urlConnection.getErrorStream() : urlConnection.getInputStream();
    	    
    	    System.out.println("isError-->"+isError);
    	    
        	ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            byte[] buf = new byte[1024];
            int len = -1;
            try {
                while((len = is.read(buf, 0, buf.length)) != -1) {
                	baos.write(buf, 0, len);
                }
                rtnMsg = new String(baos.toByteArray(), 0, baos.size(), "EUC-KR");
                System.out.println("rtuMsg-->"+rtnMsg);
            } catch(IOException e) {
                e.printStackTrace();
                sErrMsg = "I/F URL ȣ�� ���� �Դϴ�. �������ڿ��� �������ּ���.";
                throw new Exception(sErrMsg);
            } finally{
            	if(baos != null) baos.close();
            	if(is != null) is.close();
            }
            
            try{
    	    	if(rtnMsg != null && !"".equals(rtnMsg)){
    	    		if(rtnMsg.indexOf("<err_code>") >= 0){
    	    			status = rtnMsg.substring(rtnMsg.indexOf("<err_code>")+10, rtnMsg.indexOf("</err_code>"));
    	    		}
    	    		if(rtnMsg.indexOf("<err_msg>") >= 0){
    	    			err_msg = rtnMsg.substring(rtnMsg.indexOf("<err_msg>")+9, rtnMsg.indexOf("</err_msg>"));
    	    		}
    	    	}
            }catch(Exception e){
            	e.printStackTrace();
                sErrMsg = "I/F ��� ó�� ���� �Դϴ�. �������ڿ��� �������ּ���.";
                throw new Exception(sErrMsg);
            }
            
    	} catch(Exception e) {
    		e.printStackTrace();
            sErrMsg = "I/F ó�� �� �˼����� ������ �߻��߽��ϴ�. �������ڿ��� �������ּ���.";
            
    	}
    	
	    if(isError){
	    	sErrMsg = "Target URL Ȯ���Ͻñ� �ٶ��ϴ�.:"+rtnMsg;
	    }
    	
    	sErrMsg = sErrMsg.replace("\"","'");
    	sErrMsg = sErrMsg.replace("\n","");
    	sErrMsg = sErrMsg.replace(System.getProperty("line.separator"), "");	
    	
    	if(!"".equals(sErrMsg)){
        	hm.put("RTN_CODE", "-1");
        	hm.put("RTN_MSG", sErrMsg);
    	}else{
    		hm.put("RTN_CODE", status);
        	hm.put("RTN_MSG", err_msg);
    	}
    	
    	System.out.println("sErrMsg-->"+sErrMsg);
    	System.out.println("RTN_CODE-->"+status);
    	System.out.println("RTN_MSG-->"+err_msg);
    	
    	
	}
}

