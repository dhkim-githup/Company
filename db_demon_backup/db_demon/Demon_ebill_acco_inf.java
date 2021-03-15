import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import comm.mk_log;

public class Demon_ebill_acco_inf {
	private comm.DB_Use_Db_Demon  db ;  
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_ebill_acco_inf";

    // �� ������ ���� ���� �� ������ ����� �ֿܼ� ����ϴ� �޼ҵ�
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

    // �� ������ �Ķ���͸�� ���� ���� �����ϴ� �޼ҵ�
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

        // URL �ּ�
        if(sUrl == null || "".equals(sUrl)){
        	sUrl = "http://localhost/ebillAccoInf.do";
        	//OS check
    		Properties p = System.getProperties();
    		String osName = (String)p.get("os.name");
			if(!osName.startsWith("Windows")){
				//sUrl = "http://esign.happynarae.co.kr/ebillAccoInf.do";
				//sUrl = "http://121.78.69.199/ebillAccoInf.do";
				sUrl = "http://172.16.1.183/ebillAccoInf.do";
			}
        }
		System.out.println("------------->"+sUrl+"<-------------");

        // �Ķ���� �̸�
        String paramName = "animal";
        // �Ķ���� �̸��� ���� ��
        String paramValue = "dog";

        try {
            // Get������� ���� �ϱ�
//            url = new URL(sUrl + "?" + paramName + "=" + paramValue);
//            urlConnection = url.openConnection();
//            printByInputStream(urlConnection.getInputStream());

            // Post������� ���� �ϱ�
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
		System.out.println("-------------Start ebillAccoInf daemon call-------------");
		log.Write(log_flag, prgm_nm, "-------------Start ebillAccoInf daemon call-------------");

		String sUrl = "";
		sUrl = args.length > 0 ? "".equals(args[0]) ? "" : args[0] : "";

		Demon_ebill_acco_inf demon = new Demon_ebill_acco_inf();
		demon.checkAlive(sUrl);

		log.Write(log_flag, prgm_nm, "-------------End ebillAccoInf daemon call-------------");
		System.out.println("-------------End ebillAccoInf daemon call-------------");
	}
}
