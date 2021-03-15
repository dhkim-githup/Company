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

//import comm.mk_log;

public class Demon_skcnc_stat_check {
	private comm.DB_Use_Db_Demon  db ;  
	//static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_skcnc_stat_check";

	/**
	 * @author jskim
	 */
	public HashMap proc(String po_num, String po_num_seq, String mro_acp_day, String send_code) throws Exception{
        
        HashMap<String, String> hm = new HashMap();
    	String link_skcnc = "";
    	//link_skcnc = "http://api.skcc.com/px/hrstest.skcc.com/internalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_oper
    	//link_skcnc = "http://api.skcc.com/px/hss.skcc.com/internalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_test
    	//link_skcnc = "http://api.skcc.com/px/hss.skcc.com/InternalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_주소변경 hrkim 151223(대소문자)
    	//link_skcnc = "http://203.235.209.97/InternalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_주소변경 hrkim 151223(대소문자)
    	//link_skcnc = "http://hss.skcc.com/InternalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_주소변경 hrkim 151223(대소문자)
    	link_skcnc = "http://api.skcc.com/px/203.235.209.97/InternalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_주소변경 kimjs 20160411 (proxy서버 문제로 임시로 ip로 변경)
    	link_skcnc = "http://api.skcc.com/InternalCompany/IF_HAPPYNARAE_A.jsp"; //cnc_주소변경 kimjs 20170906 (CNC 공인IP변경)
    		    
    	
    	URL url = null;
    	HttpURLConnection urlConnection = null;
    	
    	// URL 주소
    	String sUrl = link_skcnc;
    	      
    	// 파라미터 이름
    	String param = "?po_num="+po_num+"&po_num_seq="+po_num_seq+"&mro_acp_day="+mro_acp_day+"&send_code="+send_code;        
        
    	System.out.println("url:"+sUrl+param);
    	
    	String sErrMsg = "";
    	String rtnMsg = "";
    	String status = "";
    	String err_msg = "";
    	boolean isError = true;
    	try {
    	
    	    // Post방식으로 전송 하기
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
                sErrMsg = "I/F URL 호출 오류 입니다. 전산담당자에게 문의해주세요.";
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
                sErrMsg = "I/F 결과 처리 오류 입니다. 전산담당자에게 문의해주세요.";
                throw new Exception(sErrMsg);
            }
            
    	} catch(Exception e) {
    		e.printStackTrace();
            sErrMsg = "I/F 처리 중 알수없는 오류가 발생했습니다. 전산담당자에게 문의해주세요.";
            throw new Exception(sErrMsg);
    	}
    	
	    if(isError){
	    	sErrMsg = "Target URL 확인하시기 바랍니다.:"+rtnMsg;
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
    	
    	return hm;
	}	
	
	

	/**
	 * @author jskim
	 */
	public void start(){
		db = new comm.DB_Use_Db_Demon();
		db.DB_Conn();

    	int pSeq = 0;
    	String qry = "";
    	List poList = new ArrayList();

    	try{
			qry =   ""+
					"  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_skcnc_stat_check_kimjs','START',SYSDATE, 'OK', null ) "+					
					"";
			db.ps = db.conn.prepareStatement(qry);
			db.ps.executeUpdate();
			db.commit();
			
			
    		
    		qry =   " SELECT A.PO_NUM, A.PO_NUM_SEQ, TO_CHAR(A.MRO_ACP_DAY,'YYYYMMDDHH24MISS') MRO_ACP_DAY            "+
    				"       ,CASE WHEN A.PROC_TY IS NULL OR A.MRO_ACP_DAY IS NULL OR B.ORDS_NUM IS NULL THEN '000'    "+
    				"             ELSE (SELECT X.PROC_TY FROM ORDS_DTL X WHERE X.MASTER_NUM = B.MASTER_NUM AND X.ORDS_TY = B.ORDS_TY AND X.ORDS_KN IN ('001','003','005'))    "+
    				"         END SEND_CODE                                                                           "+
    				"       ,B.ORDS_NUM||'-'||B.LIST_NUM||'-'||B.PROC_TY ORDNUM                                                                             "+
    				"   FROM IF_PO@L_SKCNC A                                                                        "+
    				"       ,ORDS_DTL B                                                                               "+
    				"  WHERE 1=1                                                                                      "+
    				"    AND A.STATUS IN ('INS','INS_READ')                                                           "+
    				"    AND A.CST_REG_DAY > SYSDATE - 30                                                             "+
    				"     AND A.ENPRI_CD = B.ENPRI_CD                                                                 "+
    				"    AND A.PO_NUM||'-'||A.PO_NUM_SEQ = B.CUST_ORD_NUM                                          "+
    				"    AND NVL(A.SEND_CODE,'---') <> CASE WHEN A.PROC_TY IS NULL OR A.MRO_ACP_DAY IS NULL OR B.ORDS_NUM IS NULL THEN '000' "+
    				"                                       ELSE (SELECT X.PROC_TY FROM ORDS_DTL X WHERE X.MASTER_NUM = B.MASTER_NUM AND X.ORDS_TY = B.ORDS_TY AND X.ORDS_KN IN ('001','003','005'))  "+
    				"                                   END                                                           "+
    				//"    AND 'Y' = CASE WHEN B.ORDS_KN IN ('001','003','005') THEN 'Y' ELSE 'N' END                   "+
    				"  ORDER BY PO_NUM, PO_NUM_SEQ                                                                    ";

    		db.ps = db.conn.prepareStatement(qry);	  

    		pSeq = 1;
    		//db.ps.setString(pSeq++, "30");	  

    		db.prs = db.ps.executeQuery();	

    		String sPo_num = "";
    		String sPo_num_seq = "";
    		String sMro_acp_day = "";
    		String sSend_code = "";
    		String sOrdnum = "";

    		if(db.prs != null){
				while(db.prs.next()){
					sPo_num = db.prs.getString("PO_NUM");
					sPo_num_seq = db.prs.getString("PO_NUM_SEQ");
					sMro_acp_day = db.prs.getString("MRO_ACP_DAY");
					sSend_code = db.prs.getString("SEND_CODE");
					sOrdnum = db.prs.getString("ORDNUM");
					
					HashMap<String, String> mh = new HashMap();
					mh.put("PO_NUM", sPo_num);
					mh.put("PO_NUM_SEQ", sPo_num_seq);
					mh.put("MRO_ACP_DAY", sMro_acp_day);
					mh.put("SEND_CODE", sSend_code);
					//System.out.println(sOrdnum);
					poList.add(mh);
				}
			}
    		
    		
			qry =   ""+
					//"  UPDATE INF_SKCNC.IF_PO  "+
					"  UPDATE IF_PO@L_SKCNC  "+
					"     SET SEND_CODE  = CASE WHEN ? = '0' THEN ? ELSE SEND_CODE END "+
					"        ,RTN_CODE   = ?   "+
					"        ,RTN_MSG    = ?   "+
					"   WHERE PO_NUM     = ?   "+
					"     AND PO_NUM_SEQ = ?   ";

    		db.ps = db.conn.prepareStatement(qry);	    		
    		
    		for(int ii=0; ii<poList.size(); ii++){
    			HashMap mh = (HashMap)poList.get(ii);
    			
    			HashMap<String, String> rtnMap = new HashMap();
    			try{
    				//proc po status
    				rtnMap = this.proc((String)mh.get("PO_NUM"), (String)mh.get("PO_NUM_SEQ"), (String)mh.get("MRO_ACP_DAY"), (String)mh.get("SEND_CODE"));    				
    			}catch(Exception e){
    				rtnMap.put("RTN_CODE", "-1");
    				rtnMap.put("RTN_MSG", e.getMessage());
    			}
    			
    			System.out.println("RTN_CODE-->"+(String)rtnMap.get("RTN_CODE"));
    			System.out.println("SEND_CODE-->"+(String)mh.get("SEND_CODE"));
    			
    			if(!"".equals((String)rtnMap.get("RTN_CODE"))){
	        		pSeq = 1;
	        		db.ps.setString(pSeq++, (String)rtnMap.get("RTN_CODE"));
	        		db.ps.setString(pSeq++, (String)mh.get("SEND_CODE"));	  
	        		db.ps.setString(pSeq++, (String)rtnMap.get("RTN_CODE"));	  
	        		db.ps.setString(pSeq++, (String)rtnMap.get("RTN_MSG"));	  
	        		db.ps.setString(pSeq++, (String)mh.get("PO_NUM"));	  
	        		db.ps.setString(pSeq++, (String)mh.get("PO_NUM_SEQ"));	  
	
	        		db.ps.executeUpdate();	
	
	    			db.commit();
    			}
    			
    		}
    		
    		qry = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
					 + "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_skcnc_stat_check_kimjs','END',SYSDATE, 'OK', NULL ) ";					
			db.ps = db.conn.prepareStatement(qry);		
			db.ps.executeUpdate();	
			
    	}catch(SQLException e){   
            System.out.println("Error"+e);
            // log.Write(log_flag, prgm_nm, ""+e);
            try{
        		System.out.println(e);
                e.printStackTrace();
                db.rollback();
                
            	qry = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
  						 + "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_skcnc_stat_check_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) ";	
       			db.ps = db.conn.prepareStatement(qry);		
       			db.ps.executeUpdate();	   
       			db.commit();
       			
             }catch(Exception e1){
            	 // log.Write(log_flag, prgm_nm, e1.getMessage());
            	 e1.printStackTrace();
             }
        }catch(Exception e){
            System.out.println("Error"+e);
            // log.Write(log_flag, prgm_nm, ""+e);
            try{
                System.out.println(e);
                e.printStackTrace();
                db.rollback();
                
            	qry = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
 						 + "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_skcnc_stat_check_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) ";	
      			db.ps = db.conn.prepareStatement(qry);		
      			db.ps.executeUpdate();	   
      			db.commit();
             }catch(Exception e1){
            	 // log.Write(log_flag, prgm_nm, e1.getMessage());
            	 e1.printStackTrace();
             }
        }finally{
        	db.DB_DisConn();		
        	// log.Write(log_flag, prgm_nm, "db disconnection!!");
        }	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("-------------Start skcnc stat check-------------");
		// log.Write(log_flag, prgm_nm, "-------------Start skcnc stat check-------------");

		Demon_skcnc_stat_check demon = new Demon_skcnc_stat_check();

		demon.start();

		// log.Write(log_flag, prgm_nm, "-------------End skcnc stat check-------------");
		System.out.println("-------------End skcnc stat check-------------");
	}
}

