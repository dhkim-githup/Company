import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Properties;

import comm.mk_log;

public class Demon_ebill_alive_check {
	private comm.DB_Use_Db_Demon  db ;  
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_ebill_alive_check";
	
    // 웹 서버로 부터 받은 웹 페이지 결과를 콘솔에 출력하는 메소드
    public static void printByInputStream(InputStream is) throws Exception {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(1024); 
        byte[] buf = new byte[1024];
        int len = -1;
        try {
            while((len = is.read(buf, 0, buf.length)) != -1) {
            	baos.write(buf, 0, len);            	
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        String rtnMsg = new String(baos.toByteArray(), 0, baos.size(), "UTF-8");
        //System.out.println("rtnMsg-->"+rtnMsg);
        if(rtnMsg.indexOf("I'm Alive!!") < 0){
        	System.out.println("eSign Server is Dead!!");
        	throw new Exception("eSign Server is Dead!!");
        }else{
        	System.out.println("eSign Server is Alive!!");
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
	public void checkAlive(String sUrl) throws Exception{
        URL url = null;
        URLConnection urlConnection = null;

        // URL 주소
        if(sUrl == null || "".equals(sUrl)){
        	sUrl = "http://localhost/ebillAliveCheck.do";
        	//OS check
    		Properties p = System.getProperties();
    		String osName = (String)p.get("os.name");
			if(!osName.startsWith("Windows")){
				sUrl = "http://172.16.1.183/ebillAliveCheck.do";
			}
        }
        //System.out.println(sUrl);
        try {
            // Post방식으로 전송 하기
            url = new URL(sUrl);
            urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            
            InputStream is = urlConnection.getInputStream();
            printByInputStream(is);            
        }catch(ConnectException ce){
        	ce.printStackTrace();
        	throw new Exception("eSign Server is Dead!!");
        }catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
	}	
	
	

	/**
	 * @param sSco_cd supply company code
	 */
	public void checkAlive(){
		db = new comm.DB_Use_Db_Demon();
		db.DB_Conn();

    	int pSeq = 0;
    	String qry = "";

    	try{
    		
        	try{
    			
        		//Check eSign Server Alive
    			this.checkAlive("");
    			
        	}catch(Exception e){
        		e.printStackTrace();
        		//sms 전송
    			qry = "{call P_EBILL_MNG.SP_EBILL_ISSUE_SEND_SMS(?, ?, ?, ?, ?, ?, ?)}";

    			db.cs = db.conn.prepareCall(qry);	

    			pSeq = 1;
    			db.cs.setString(pSeq++, "");  
    			db.cs.setString(pSeq++, "");  
    			db.cs.setString(pSeq++, e.getMessage());  
    			db.cs.setString(pSeq++, "ERR");  
    			db.cs.setString(pSeq++, "");  
    			db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
    			db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 

    			db.cs.execute();	

    			String sReturn = db.cs.getString(6);
    			String sMsg    = db.cs.getString(7);

    			db.conn.commit();

    			//mail 전송
    			qry = "{call P_EBILL_MNG.SP_EBILL_SEND_MAIL(?, ?, ?, ?, ?, ?, ?)}";

    			db.cs = db.conn.prepareCall(qry);	

    			pSeq = 1;
    			db.cs.setString(pSeq++, "");  
    			db.cs.setString(pSeq++, "ERR");   
    			db.cs.setString(pSeq++, "");  
    			db.cs.setString(pSeq++, e.getMessage());  
    			db.cs.setString(pSeq++, "");  
    			db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
    			db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 

    			db.cs.execute();	

    			sReturn = db.cs.getString(6);
    			sMsg    = db.cs.getString(7);

    			db.conn.commit();    
    			
    			throw new Exception(e);
        	}
    		
    		int hhss = 0;
    		qry = " select to_char(sysdate, 'hh24mi') hhss from dual ";
    		db.ps = db.conn.prepareStatement(qry);	  
    		db.prs = db.ps.executeQuery();	
    		
    		if(db.prs != null){
				while(db.prs.next()){
					hhss = Integer.parseInt(db.prs.getString("hhss"));
				}
			}
    		
    		//System.out.println("hhss-->"+hhss);
    		
    		int batchSeq = 0;
    		if(hhss >= 630 && hhss <= 730){
    			batchSeq = 1;
    		}else if(hhss >= 2130 && hhss <= 2230){
    			batchSeq = 2;
    		}
    		
	    	if(batchSeq > 0){	  
	    		int cntProc = 0;
	    		qry = " select count(*) cnt from ebill_doc where proc_ty = '005' ";
	    		
	    		db.ps = db.conn.prepareStatement(qry);	  
	    		db.prs = db.ps.executeQuery();
	    		
	    		if(db.prs != null){
					while(db.prs.next()){
						cntProc = db.prs.getInt("cnt");
					}
				}
	    		
	    		if(cntProc > 0){
	    		
		
					qry =   "  SELECT JOB_GBN,BATCH_DAY,RVS_NO,TO_CHAR(START_DATE,'YYYYMMDD') START_DATE,TO_CHAR(END_DATE,'YYYYMMDD') END_DATE "+
					        "        ,(SELECT COUNT(*) FROM EBILL_BATCH_DTL X WHERE X.JOB_GBN = A.JOB_GBN AND X.BATCH_DAY = A.BATCH_DAY AND X.RVS_NO = A.RVS_NO AND X.PROC_DATE IS NOT NULL) PROC_CNT "+
				        	"    FROM EBILL_BATCH_INFO A                                                              "+
				        	"   WHERE JOB_GBN = 'SND'                                                                 "+
				    		"     AND BATCH_DAY = TO_CHAR(SYSDATE, 'YYYYMMDD')                                        "+
				    		"     AND BATCH_MAN = '00000001'                                                          ";
					if(batchSeq == 1){
						qry += "     AND TO_CHAR(REG_DATE,'YYYYMMDDHH24MISS') >= TO_CHAR(SYSDATE, 'YYYYMMDD')||'053000'  ";
						qry += "     AND TO_CHAR(REG_DATE,'YYYYMMDDHH24MISS') <= TO_CHAR(SYSDATE, 'YYYYMMDD')||'063000'  ";
					}else if(batchSeq == 2){
						qry += "     AND TO_CHAR(REG_DATE,'YYYYMMDDHH24MISS') >= TO_CHAR(SYSDATE, 'YYYYMMDD')||'203000'  ";
						qry += "     AND TO_CHAR(REG_DATE,'YYYYMMDDHH24MISS') <= TO_CHAR(SYSDATE, 'YYYYMMDD')||'213000'  ";
					}
					
					//System.out.println(qry);
		
		    		db.ps = db.conn.prepareStatement(qry);	  
		    		db.prs = db.ps.executeQuery();	
		
		    		String job_gbn = "";
		    		String batch_day = "";
		    		String rvs_no = "";
		    		String start_date = "";
		    		String end_date = "";
		    		String proc_cnt = "";
		
		    		if(db.prs != null){
						while(db.prs.next()){
							job_gbn = db.prs.getString("JOB_GBN");
							batch_day = db.prs.getString("batch_day");
							rvs_no = db.prs.getString("rvs_no");
							start_date = db.prs.getString("start_date");
							end_date = db.prs.getString("end_date");
							proc_cnt = db.prs.getString("proc_cnt");
						}
					}
	
	//	    		System.out.println("job_gbn-->"+job_gbn);
	//	    		System.out.println("batch_day-->"+batch_day);
	//	    		System.out.println("rvs_no-->"+rvs_no);
	//	    		System.out.println("start_date-->"+start_date);
	//	    		System.out.println("end_date-->"+end_date);
	//	    		System.out.println("proc_cnt-->"+proc_cnt);
		    		
		    		
		    		if("".equals(job_gbn)){
		    			//sms 전송
						qry = "{call P_EBILL_MNG.SP_EBILL_ISSUE_SEND_SMS(?, ?, ?, ?, ?, ?, ?)}";
		
						db.cs = db.conn.prepareCall(qry);	
		
						pSeq = 1;
						db.cs.setString(pSeq++, "");  
						db.cs.setString(pSeq++, "");  
						db.cs.setString(pSeq++, "전자세금계산서 국세청 전송 데몬 작동체크 확인바랍니다.");  
						db.cs.setString(pSeq++, "ERR");  
						db.cs.setString(pSeq++, "");  
						db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
						db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
		
						db.cs.execute();	
		
						String sReturn = db.cs.getString(6);
						String sMsg    = db.cs.getString(7);
		
						db.conn.commit();
		
						//mail 전송
						qry = "{call P_EBILL_MNG.SP_EBILL_SEND_MAIL(?, ?, ?, ?, ?, ?, ?)}";
		
						db.cs = db.conn.prepareCall(qry);	
		
						pSeq = 1;
						db.cs.setString(pSeq++, "");  
						db.cs.setString(pSeq++, "ERR");   
						db.cs.setString(pSeq++, "");  
						db.cs.setString(pSeq++, "전자세금계산서 국세청 전송 데몬 작동체크 확인바랍니다.");  
						db.cs.setString(pSeq++, "");  
						db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
						db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
		
						db.cs.execute();	
		
						sReturn = db.cs.getString(5);
						sMsg    = db.cs.getString(6);
		
						db.conn.commit();
		    		}else{
		    			qry =   "  INSERT INTO EBILL_BATCH_CHECK               "+
		    					"  SELECT ? JOB_GBN                            "+
		    					"        ,? BATCH_DAY                          "+
		    					"        ,? RVS_NO                             "+
		    					"        ,NVL(MAX(CHECK_SEQ),0)+1 CHECK_SEQ    "+
		    					"        ,SYSDATE CHECK_DATE                   "+
		    					"        ,? PROC_CNT                           "+
		    					"    FROM EBILL_BATCH_CHECK                    "+
		    					"   WHERE JOB_GBN = ?                          "+
		    					"     AND BATCH_DAY = ?                        "+
		    					"     AND RVS_NO = ?                           ";
		
		        		db.ps = db.conn.prepareStatement(qry);	  
		
		        		pSeq = 1;
		        		db.ps.setString(pSeq++, job_gbn);	  
		        		db.ps.setString(pSeq++, batch_day);	  
		        		db.ps.setString(pSeq++, rvs_no);	  
		        		db.ps.setString(pSeq++, proc_cnt);	  
		        		db.ps.setString(pSeq++, job_gbn);	  
		        		db.ps.setString(pSeq++, batch_day);	  
		        		db.ps.setString(pSeq++, rvs_no);
		
		        		db.ps.executeUpdate();	
		
		    			db.commit();
		
		    			
		    			qry =   "  SELECT MAX(CHECK_SEQ) CHECK_SEQ "+
		    		        	"    FROM EBILL_BATCH_CHECK A                  "+
		    					"   WHERE JOB_GBN = ?                          "+
		    					"     AND BATCH_DAY = ?                        "+
		    					"     AND RVS_NO = ?                           ";
		
		        		db.ps = db.conn.prepareStatement(qry);	  
		
		        		pSeq = 1;
		        		db.ps.setString(pSeq++, job_gbn);	  
		        		db.ps.setString(pSeq++, batch_day);	  
		        		db.ps.setString(pSeq++, rvs_no);	  
		
		        		db.prs = db.ps.executeQuery();	
		
		        		String maxCheck_seq = "";
		
		        		if(db.prs != null){
		    				while(db.prs.next()){
		    					maxCheck_seq = db.prs.getString("check_seq");
		    				}
		    			}
		        		
		        		if(!"1".equals(maxCheck_seq)){
		        			/*
		        			qry =   "  SELECT (SELECT X.PROC_CNT FROM EBILL_BATCH_CHECK X WHERE X.JOB_GBN = A.JOB_GBN AND X.BATCH_DAY = A.BATCH_DAY AND X.RVS_NO = A.RVS_NO AND X.CHECK_SEQ = A.CHECK_SEQ-1) - A.PROC_CNT DIF_PROC_CNT "+
		        		        	"    FROM EBILL_BATCH_CHECK A                  "+
		        					"   WHERE JOB_GBN = ?                          "+
		        					"     AND BATCH_DAY = ?                        "+
		        					"     AND RVS_NO = ?                           "+
		        					"     AND CHECK_SEQ = ?                        ";
		
		            		db.ps = db.conn.prepareStatement(qry);	  
		            		
		            		pSeq = 1;
		            		db.ps.setString(pSeq++, job_gbn);	  
		            		db.ps.setString(pSeq++, batch_day);	  
		            		db.ps.setString(pSeq++, rvs_no);	  
		            		db.ps.setString(pSeq++, maxCheck_seq);	  
		
		            		db.prs = db.ps.executeQuery();	
		
		            		int dif_proc_cnt = 0;
		
		            		if(db.prs != null){
		        				while(db.prs.next()){
		        					dif_proc_cnt = Integer.parseInt(db.prs.getString("dif_proc_cnt"));
		        				}
		        			}
		
		            		if(dif_proc_cnt <= 0){
		            			if(end_date == null || "".equals(end_date)){
		            				//sms 전송
		            				qry = "{call P_EBILL_MNG.SP_EBILL_ISSUE_SEND_SMS(?, ?, ?, ?, ?, ?, ?)}";
		
		            				db.cs = db.conn.prepareCall(qry);	
		
		            				pSeq = 1;
		            				db.cs.setString(pSeq++, "");  
		            				db.cs.setString(pSeq++, "");  
		            				db.cs.setString(pSeq++, "전자세금계산서 국세청 전송 데몬 처리지연 확인바랍니다.");  
		            				db.cs.setString(pSeq++, "ERR");  
		            				db.cs.setString(pSeq++, "");  
		            				db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
		            				db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
		
		            				db.cs.execute();	
		
		            				String sReturn = db.cs.getString(6);
		            				String sMsg    = db.cs.getString(7);
		
		            				db.conn.commit();
		
		            				//mail 전송
		            				qry = "{call P_EBILL_MNG.SP_EBILL_SEND_MAIL(?, ?, ?, ?, ?, ?, ?)}";
		
		            				db.cs = db.conn.prepareCall(qry);	
		
		            				pSeq = 1;
		            				db.cs.setString(pSeq++, "");  
		            				db.cs.setString(pSeq++, "ERR");  
		            				db.cs.setString(pSeq++, "");  
		            				db.cs.setString(pSeq++, "전자세금계산서 국세청 전송 데몬 처리지연 확인바랍니다.");  
		            				db.cs.setString(pSeq++, "");  
		            				db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
		            				db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
		
		            				db.cs.execute();	
		
		            				sReturn = db.cs.getString(5);
		            				sMsg    = db.cs.getString(6);
		
		            				db.conn.commit();
		            			}
		            		}
		            		*/
		        		}
		    		}
	    		}
	    	}		
    	}catch(SQLException e){   
            //System.out.println("Error"+e);
            log.Write(log_flag, prgm_nm, ""+e);
            try{
        		//System.out.println(e);
                e.printStackTrace();
                db.rollback();
             }catch(Exception e1){
            	 log.Write(log_flag, prgm_nm, e1.getMessage());
            	 e1.printStackTrace();
             }
        }catch(Exception e){
            //System.out.println("Error"+e);
            log.Write(log_flag, prgm_nm, ""+e);
            try{
                //System.out.println(e);
                e.printStackTrace();
                db.rollback();
             }catch(Exception e1){
            	 log.Write(log_flag, prgm_nm, e1.getMessage());
            	 e1.printStackTrace();
             }
        }finally{
        	db.DB_DisConn();		
        	log.Write(log_flag, prgm_nm, "db disconnection!!");
        }	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("-------------Start NTS daemon alive check-------------");
		log.Write(log_flag, prgm_nm, "-------------Start NTS daemon alive check-------------");

		Demon_ebill_alive_check demon = new Demon_ebill_alive_check();

		demon.checkAlive();

		log.Write(log_flag, prgm_nm, "-------------End NTS daemon alive check-------------");
		System.out.println("-------------End NTS daemon alive check-------------");
	}
}

