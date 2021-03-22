import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

import oracle.sql.CLOB;

import java.security.Security;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import comm.mk_log;


public class Demon_inicis_tra_sap {
	private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_inicis_tra";
	
	
	public String SendDataHttps(String SendURL, String SendData){
		URL sendUrl = null;
		HttpsURLConnection urlCon = null;
		OutputStreamWriter outStream = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer("");
		
		String sResult = ""; // 반환될 값
		try{
			sendUrl = new URL(SendURL);
			urlCon = (HttpsURLConnection)sendUrl.openConnection();
			//urlCon.setHostnameVerifier(new CustomizedHostnameVerifier());
			urlCon.setDoOutput(true); // post 방식:true
			urlCon.setDoInput(true); // 데이터를 첨부하는 경우 true
			urlCon.setUseCaches(false);
			//outStream = new OutputStreamWriter(urlCon.getOutputStream());
			//outStream.write(SendData);
			//outStream.flush();
			//outStream.close();
			
			reader = new java.io.BufferedReader(new java.io.InputStreamReader(urlCon.getInputStream()));
			
			while((sResult=reader.readLine()) != null){
				if(!sResult.equals("")){
					sResult.trim();
					sb.append(sResult);
				}
			}
			reader.close();
			return sb.toString();
		
		}catch(Exception e){
			e.printStackTrace();
			return "Fail";
		}finally{
			try{
				if(outStream != null) outStream.close(); outStream = null;
				if(reader != null) reader.close(); reader = null;
				if(urlCon != null) urlCon.disconnect(); urlCon = null;
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}	
	
	/**
	 * 이니시스 인터페이스를 통한 전문데이터 수신 및 수신 원문 데이터 저장 후 전문데이터 분리하여 저장 및 수납처리
	 * @param urls 인터페이스 url
	 * @param id 인터페이스 id
	 * @param pass 인터페이스 패스워드
	 * @param date 정산대사 요청일자
	 * @param gmid 인터페이스 그룹id
	 */
	public void getData(String[][] urls, String[] id, String[] pass, String date, String gmid){
    	
    	log.Write(log_flag, prgm_nm, "Start getData");
    	log.Write(log_flag, prgm_nm, "id:"+id+"<>pass:"+pass+"<>date:"+date+"<>gmid:"+gmid);
		
		String parameters = "";
		String responseMessage = "";
		String result = "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date currdate = new Date();
		
		date = "".equals(date) ? getAgoMonth(formatter.format(currdate).toString(), 0, 0) : date;
		log.Write(log_flag, prgm_nm, "trading day:"+date);
		//InputStream is = null;
		//InputStreamReader isr = null;
		//OutputStreamWriter osw = null;
		BufferedReader br = null;
		StringBuffer sb = null;
		
		//ssl protocol setting
		/*
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internl.www.protocol");
		com.sun.net.ssl.internal.ssl.Provider provider = new com.sun.net.ssl.internal.ssl.Provider();
		Security.addProvider(provider);
		*/
		
		
		String qry =   "";
		String sIni_num = "";
		int pSeq = 1;
		try{
			log.Write(log_flag, prgm_nm, "db connection at getData");
			db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
			
			db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTCN";
			db.user_id="INICIS";
			db.user_pw="INICIS";
								
			db.DB_Conn();
			
			/*
			qry =   ""+
					"  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_inicis_tra_kimjs','START',SYSDATE, 'OK', NULL ) "+					
					"";
			db.prepareStatement(qry);
			db.PexecuteUpdate();
			db.commit();		
			db.conn.commit();
			*/
			
			log.Write(log_flag, prgm_nm, "get ini_num");
			StringBuffer sbIni_num = new StringBuffer();
			sbIni_num.append(" SELECT 'T'||TRIM(TO_CHAR(S_INI_TRA_INFO.NEXTVAL,'000000000')) ini_num FROM dual ").append("\n");
			
			db.prepareStatement(sbIni_num.toString());
			
			db.PexecuteQuery();
			if(db.ERR_FLAG < 0){
				throw new SQLException(db.ERR_MSG);
			}
			
			if(db.prs != null){		
				while(db.prs.next()){
					sIni_num = db.prs.getString("ini_num");
				}
			}else{
				throw new SQLException(db.ERR_MSG);
			}	
			
			log.Write(log_flag, prgm_nm, "sIni_num-->"+sIni_num);
			
			
		
			for(int jj=0; jj<id.length; jj++){
				
				
				for(int ii=0; ii<urls.length; ii++){
					
					if("DS".equals(urls[ii][1])){
						parameters = "?urlid="+id[jj]+"&passwd="+pass[jj]+"&date="+date+"&flgurl=32";
					}else{
						parameters = "?urlid="+id[jj]+"&passwd="+pass[jj]+"&date="+date;
					}
					
					log.Write(log_flag, prgm_nm, "seq:"+ii+"<>url:"+urls[ii][0]+"<>type:"+urls[ii][1]);
					try{
						URL httpsUrl = new URL(urls[ii][0]+parameters);
						//URLConnection conn = httpsUrl.openConnection();
						HttpsURLConnection conn = (HttpsURLConnection)httpsUrl.openConnection();
						
						conn.setUseCaches(false);
						conn.setConnectTimeout(40000);
						conn.setDoOutput(true);
						conn.connect();
						
						responseMessage = conn.getHeaderField(0);
						log.Write(log_flag, prgm_nm, "Response Message:"+responseMessage);
						
						//osw = new OutputStreamWriter(conn.getOutputStream());
						//osw.write("write data");
						//osw.flush();
						//osw.close();
						
						br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						sb = new StringBuffer();
						
						result = "";
						while((result=br.readLine()) != null){
							if(!result.equals("")){
								result.trim();
								sb.append(result);
							}
							//sb.append(br.readLine());
						}
						
						//System.out.println(sIni_num+"<>"+urls[ii][1]+"<>"+id[jj]+"-->"+sb.toString());
						//원 전문데이터 등록 
						log.Write(log_flag, prgm_nm, "call method insertData");
						int rslt = insertData(sIni_num, date, id[jj], urls[ii][1], urls[ii][0]+parameters, sb);
						if(rslt < 0){
							log.Write(log_flag, prgm_nm, "An error occured inserting the trading data at "+ii+"th");
							throw new Exception("An error occured inserting the trading data at "+ii+"th");
						}
						
						//원 전문데이터 잘라서 등록(승인대사)
						/*
						log.Write(log_flag, prgm_nm, "call method insertDtlData");
						rslt = insertDtlData(sIni_num, date, id[jj], urls[ii][1], urls[ii][0]+parameters, sb);
						if(rslt < 0){
							log.Write(log_flag, prgm_nm, "An error occured inserting the trading data at "+ii+"th");
						}
						*/
						
						//원 전문데이터 잘라서 등록(정산대사)
						log.Write(log_flag, prgm_nm, "call method insertDSDtlData");
						rslt = insertDSDtlData(sIni_num, date, id[jj], urls[ii][1], urls[ii][0]+parameters, sb);
						if(rslt < 0){
							log.Write(log_flag, prgm_nm, "An error occured inserting the trading data at "+ii+"th");
							throw new Exception("An error occured inserting the trading data at "+ii+"th");
						}
						
						br.close();
						
					}catch(Exception e){
						log.Write(log_flag, prgm_nm, "An error occured:"+e.getMessage());
						e.printStackTrace();
						throw new Exception(e.getMessage());
					}
				}
			}
			
			//원 전문데이터 잘라서 등록(정산대사)
			log.Write(log_flag, prgm_nm, "call method insertDSPayData");
			
			int rslt = insertDSPayData(sIni_num);
			
			if(rslt < 0){
				log.Write(log_flag, prgm_nm, "An error occured inserting the trading pay data");
				throw new Exception("An error occured inserting the trading pay data");
			}
			
    		
			//수납처리
			rslt = procData(sIni_num);
			if(rslt < 0){
				log.Write(log_flag, prgm_nm, "An error occured processing the trading data");
				throw new Exception("An error occured processing the trading data");
			}
			
			/*
			qry =   ""+
					"  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_inicis_tra_kimjs','END',SYSDATE, 'OK', NULL ) "+					
					"";
			db.prepareStatement(qry);
			db.PexecuteUpdate();
			db.commit();
			*/
			
			log.Write(log_flag, prgm_nm, "End getData");
		
		}catch(SQLException e){   
			
	        log.Write(log_flag, prgm_nm, ""+e);
	        try{
	            e.printStackTrace();
	            db.rollback();
	            /*
				qry =   ""+
						"  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
						"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_inicis_tra_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) "+					
						"";	
				db.prepareStatement(qry);
				db.PexecuteUpdate();
				db.commit();
				*/
	         }catch(Exception e1){
	        	 log.Write(log_flag, prgm_nm, e1.getMessage());
	        	 e1.printStackTrace();
	         }
	    }catch(Exception e){
	    	
	        log.Write(log_flag, prgm_nm, ""+e);
	        try{
	            e.printStackTrace();
	            db.rollback();
	            /*
				qry =   ""+
						"  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
						"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_inicis_tra_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) "+					
						"";	
				
				db.prepareStatement(qry);
				db.PexecuteUpdate();
				db.commit();
				*/	            
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
	 * 인터페이스로 받아온 전문을 원본 그대로 저장
	 * @param ini_num
	 * @param date
	 * @param id
	 * @param type
	 * @param url
	 * @param message
	 * @return
	 */
	public int insertData(String ini_num, String date, String id, String type, String url, StringBuffer message) throws Exception{
		int rslt = 0;
		log.Write(log_flag, prgm_nm, "db connection at insertData");
		db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
		
		db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTCN";
		db.user_id="INICIS";
		db.user_pw="INICIS";	
		
		db.DB_Conn();
		
    	int pSeq = 0;
    	
    	try{
    		log.Write(log_flag, prgm_nm, "Defining the SQL to Insert INI_TRA_INFO");
    		StringBuffer sbInsTraData = new StringBuffer();
    		sbInsTraData.append("  INSERT INTO INI_TRA_INFO                      ").append("\n");
    		sbInsTraData.append("  (INI_NUM, TRA_ID, TRA_TY, TRA_DAY, URL, MSG, REG_DATI)  ").append("\n");
    		sbInsTraData.append("  VALUES                                        ").append("\n");
    		sbInsTraData.append("        (? --INI_NUM                            ").append("\n");
    		sbInsTraData.append("        ,? --TRA_ID                             ").append("\n");
    		sbInsTraData.append("        ,? --TRA_TY                             ").append("\n");
    		sbInsTraData.append("        ,? --TRA_DAY                            ").append("\n");
    		sbInsTraData.append("        ,? --URL                                ").append("\n");
    		sbInsTraData.append("        ,empty_clob() --MSG                     ").append("\n");
    		sbInsTraData.append("        ,SYSDATE --REG_DATI                     ").append("\n");
    		sbInsTraData.append("        )                                       ").append("\n");
    		
			db.ps = db.conn.prepareStatement(sbInsTraData.toString());	
			
			String msg = message == null || "null".equals(message) ? "" : message.toString();
			msg = "null".equals(msg) ? "" : msg;
			
			log.Write(log_flag, prgm_nm, "Setting SQL parameters");
			pSeq = 1;
			db.ps.setString(pSeq++, ini_num);
			db.ps.setString(pSeq++, id     );
			db.ps.setString(pSeq++, type   );
			db.ps.setString(pSeq++, date   );
			db.ps.setString(pSeq++, url    );
			//db.ps.setString(pSeq++, msg    );
			
			db.ps.executeUpdate();	
			
			
			log.Write(log_flag, prgm_nm, "Defining the SQL to Select");
			StringBuffer sbSelTraData  = new StringBuffer();
			sbSelTraData.append("   SELECT MSG                             ").append("\n");
			sbSelTraData.append("     FROM INI_TRA_INFO                    ").append("\n");
			sbSelTraData.append("    WHERE INI_NUM = ?                     ").append("\n");
			sbSelTraData.append("      AND TRA_ID = ?                      ").append("\n");
			sbSelTraData.append("      AND TRA_TY = ?                      ").append("\n");
 			sbSelTraData.append("      FOR UPDATE                          ").append("\n");
    		
    		db.ps = db.conn.prepareStatement(sbSelTraData.toString());			
			
    		pSeq = 1;
			db.ps.setString(pSeq++, ini_num);
			db.ps.setString(pSeq++, id     );
			db.ps.setString(pSeq++, type   );
    		
			db.prs = db.ps.executeQuery();
			
			if(db.prs != null){
				while(db.prs.next()){
					Clob clob = db.prs.getClob("MSG");
					Writer wr = ((CLOB)clob).getCharacterOutputStream(0);
					Reader rd = new CharArrayReader(msg.toCharArray());
					
					char[] buffer = new char[1024];
					int read = 0;
					while((read = rd.read(buffer, 0, 1024)) != -1){
						wr.write(buffer, 0, read);
					}
					
					rd.close();
					wr.close();
				}
			}else{
				throw new SQLException("select CLOB info error!!"+db.ERR_MSG);
			}
			
			log.Write(log_flag, prgm_nm, "commit db");
			db.commit();
			
    	}catch(SQLException e){   
    		rslt = -1;
            //System.out.println("Error"+e);
            log.Write(log_flag, prgm_nm, ""+e);
    		//System.out.println(e);
            e.printStackTrace();
            db.rollback();
            throw new Exception(e.getMessage());
        }catch(Exception e){
        	rslt = -1;
            //System.out.println("Error"+e);
            log.Write(log_flag, prgm_nm, ""+e);
            //System.out.println(e);
            e.printStackTrace();
            db.rollback();
            throw new Exception(e.getMessage());
        }
		
		return rslt;
	}
	
	/**
	 * 인터페이스를 통해 받아온 전문을 구분자로 분리하여 데이터 저장
	 * @param ini_num
	 * @param date
	 * @param id
	 * @param type
	 * @param url
	 * @param message
	 * @return
	 */
	public int insertDSDtlData(String ini_num, String date, String id, String type, String url, StringBuffer message) throws Exception{
		int rslt = 0;
		
		if(!"0001".equals(message.toString()) && !"0002".equals(message.toString()) && !"0003".equals(message.toString()) && message.indexOf("NO DATA") < 0){					
			
			log.Write(log_flag, prgm_nm, "db connection at insertDSDtlData");
			db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
			
			db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTCN";
			db.user_id="INICIS";
			db.user_pw="INICIS";	
			
			db.DB_Conn();
			
	    	int pSeq = 0;
	    	
	    	try{
	    		StringBuffer sbInsIniTraDtl = new StringBuffer();
	    		
	    		sbInsIniTraDtl.append("  INSERT INTO INI_TRA_DTL            ").append("\n");
	    		sbInsIniTraDtl.append("  (                                  ").append("\n");
	    		sbInsIniTraDtl.append("   INI_NUM                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_ID                            ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_TY                            ").append("\n");
	    		sbInsIniTraDtl.append("  ,GBN                               ").append("\n");
	    		sbInsIniTraDtl.append("  ,SEQ                               ").append("\n");
	    		sbInsIniTraDtl.append("  ,PROC_DATE                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,ROW_NUM                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,PAY_METHOD                        ").append("\n");
	    		sbInsIniTraDtl.append("  ,MRCH_ID                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,PAY_DAY                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,BUY_ENPRI_CD                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,APP_ENPRI_CD                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,TID                               ").append("\n");
	    		sbInsIniTraDtl.append("  ,ORG_TID                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,OID                               ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_GBN                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,APRV_DATE                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,CNCL_DATE                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,APRV_NUM                          ").append("\n");
	    		sbInsIniTraDtl.append("  ,CRNC_GBN                          ").append("\n");
	    		sbInsIniTraDtl.append("  ,PAY_PRI                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,CHARG                             ").append("\n");
	    		sbInsIniTraDtl.append("  ,VAT                               ").append("\n");
	    		sbInsIniTraDtl.append("  ,CHARGE_BACK                       ").append("\n");
	    		sbInsIniTraDtl.append("  ,RE_PAY_PRI                        ").append("\n");
	    		sbInsIniTraDtl.append("  ,ERR_CD                            ").append("\n");
	    		sbInsIniTraDtl.append("  ,ERR_MSG                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,EXCP_VAL1                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,EXCP_VAL2                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,EXCP_VAL3                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,EXCP_VAL4                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,EXCP_VAL5                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_DAY                           ").append("\n");
	    		sbInsIniTraDtl.append("  ,REG_DATI                          ").append("\n");
	    		sbInsIniTraDtl.append("  )                                  ").append("\n");
	    		sbInsIniTraDtl.append("  SELECT                             ").append("\n");
	    		sbInsIniTraDtl.append("   ?                  --INI_NUM      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --TRA_ID       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --TRA_TY       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --GBN          ").append("\n");
	    		sbInsIniTraDtl.append("  ,NVL(MAX(SEQ),0)+1  --SEQ          ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --PROC_DATE    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --ROW_NUM      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --PAY_METHOD   ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --MRCH_ID      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --PAY_DAY      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --BUY_ENPRI_CD ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --APP_ENPRI_CD ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --TID          ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --ORG_TID      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --OID          ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --TRA_GBN      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --APRV_DATE    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --CNCL_DATE    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --APRV_NUM     ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --CRNC_GBN     ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --PAY_PRI      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --CHARG        ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --VAT          ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --CHARGE_BACK  ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --RE_PAY_PRI   ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --ERR_CD       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --ERR_MSG      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --EXCP_VAL1    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --EXCP_VAL2    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --EXCP_VAL3    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --EXCP_VAL4    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --EXCP_VAL5    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?                  --TRA_DAY      ").append("\n");
	    		sbInsIniTraDtl.append("  ,SYSDATE            --REG_DATI     ").append("\n");
	    		sbInsIniTraDtl.append("    FROM INI_TRA_DTL                 ").append("\n");
	    		sbInsIniTraDtl.append("   WHERE 1=1                         ").append("\n");
	    		sbInsIniTraDtl.append("    AND INI_NUM = ?                  ").append("\n");
	    		sbInsIniTraDtl.append("    AND TRA_ID  = ?                  ").append("\n");
	    		sbInsIniTraDtl.append("    AND TRA_TY  = ?                  ").append("\n");
	    		sbInsIniTraDtl.append("    AND GBN     = ?                  ").append("\n");
	    		
	    		db.ps = db.conn.prepareStatement(sbInsIniTraDtl.toString());	  
	    		
	    		String messages[] = (message.toString()).split("<br>");
	    		
	    		String sPK = "";
	    		for(int ii=0; ii< messages.length; ii++){
	    			//System.out.println(messages[ii]);
	    			String datas[] = messages[ii].split("\\|", -1);
	    			
	    			String sGBN          = datas[0 ];  //  구분 H, B, T (Head node, Body node, Trailer node)                         
	    			String sPROC_DATE    = datas[1 ];  //  Processing Date YYYYMMDDHH24MISS                                          
	    			String sROW_NUM      = datas[2 ];  //  갯수 Header-총 record 갯수, Body-순차, Tail-Body node의 총 record 갯수    
	    			String sPAY_METHOD   = datas[3 ];  //  결재구분 COMM_CD.CD_FLAG=640                                              
	    			String sMRCH_ID      = datas[4 ];  //  상점ID (Head-그룹ID, Body-상점ID)                                         
	    			String sPAY_DAY      = datas[5 ];  //  입금일자 YYYYMMDD                                                         
	    			String sBUY_ENPRI_CD = datas[6 ];  //  매입사코드(COMM_CD.CD_FLAG=641, 642) 결재구분 CC:매입사코드, MO:이통사코드
	    			String sAPP_ENPRI_CD = datas[7 ];  //  승인사코드(COMM_CD.CD_FLAG=641) 결재구분 CC:발급사코드                    
	    			String sTID          = datas[8 ];  //  거래ID                                                                    
	    			String sORG_TID      = datas[9 ];  //  원거래ID                                                                  
	    			String sOID          = datas[10];  //  상점주문번호                                                              
	    			String sTRA_GBN      = datas[11];  //  거래구분(A:승인, C:취소, P:부분취소)                                      
	    			String sAPRV_DATE    = datas[12];  //  승인일시                                                                  
	    			String sCNCL_DATE    = datas[13];  //  취소일시                                                                  
	    			String sAPRV_NUM     = datas[14];  //  승인번호                                                                  
	    			String sCRNC_GBN     = datas[15];  //  통화구분(기본 KRW, 외화:USD)                                              
	    			String sPAY_PRI      = datas[16];  //  정산금액 - Include sign(+ or -), Tail 부는 총합                           
	    			String sCHARG        = datas[17];  //  정산수수료 - Include sign(+ or -), Tail 부는 총합                         
	    			String sVAT          = datas[18];  //  부가세 - Include sign(+ or -), Tail 부는 총합                             
	    			String sCHARGE_BACK  = datas[19];  //  Chargeback - Include sign(+ or -), Tail 부 총합만 제공                    
	    			String sRE_PAY_PRI   = datas[20];  //  입금금액 - Include sign(+ or -), Tail 부는 총합                           
	    			String sERR_CD       = datas[21];  //  에러코드(COMM_CD.CD_FLAG=643)                                             
	    			String sERR_MSG      = datas[22];  //  에러메세지(COMM_CD.CD_FLAG=643)                                           
	    			String sEXCP_VAL1    = datas[23];  //  예외필드1 - 구분자(|)만 존재 값은 없음                                    
	    			String sEXCP_VAL2    = datas[24];  //  예외필드2 - 구분자(|)만 존재 값은 없음                                    
	    			String sEXCP_VAL3    = datas[25];  //  예외필드3 - 구분자(|)만 존재 값은 없음                                    
	    			String sEXCP_VAL4    = datas[26];  //  예외필드4 - 구분자(|)만 존재 값은 없음                                    
	    			String sEXCP_VAL5    = datas[27];  //  예외필드5 - 구분자(|)만 존재 값은 없음                                    

	    			

	    			/*
	    			System.out.println("▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣");
	    			System.out.println("datas.length:"+datas.length  );
	    			System.out.println("ini_num      :"+ini_num      );
	    			System.out.println("id           :"+id           );
	    			System.out.println("type         :"+type         );
	    			System.out.println("sGBN         :"+sGBN         );
	    			System.out.println("sPROC_DATE   :"+sPROC_DATE   );
	    			System.out.println("sROW_NUM     :"+sROW_NUM     );
	    			System.out.println("sPAY_METHOD  :"+sPAY_METHOD  );
	    			System.out.println("sMRCH_ID     :"+sMRCH_ID     );
	    			System.out.println("sPAY_DAY     :"+sPAY_DAY     );
	    			System.out.println("sBUY_ENPRI_CD:"+sBUY_ENPRI_CD);
	    			System.out.println("sAPP_ENPRI_CD:"+sAPP_ENPRI_CD);
	    			System.out.println("sTID         :"+sTID         );
	    			System.out.println("sORG_TID     :"+sORG_TID     );
	    			System.out.println("sOID         :"+sOID         );
	    			System.out.println("sTRA_GBN     :"+sTRA_GBN     );
	    			System.out.println("sAPRV_DATE   :"+sAPRV_DATE   );
	    			System.out.println("sCNCL_DATE   :"+sCNCL_DATE   );
	    			System.out.println("sAPRV_NUM    :"+sAPRV_NUM    );
	    			System.out.println("sCRNC_GBN    :"+sCRNC_GBN    );
	    			System.out.println("sPAY_PRI     :"+sPAY_PRI     );
	    			System.out.println("sCHARG       :"+sCHARG       );
	    			System.out.println("sVAT         :"+sVAT         );
	    			System.out.println("sCHARGE_BACK :"+sCHARGE_BACK );
	    			System.out.println("sRE_PAY_PRI  :"+sRE_PAY_PRI  );
	    			System.out.println("sERR_CD      :"+sERR_CD      );
	    			System.out.println("sERR_MSG     :"+sERR_MSG     );
	    			System.out.println("sEXCP_VAL1   :"+sEXCP_VAL1   );
	    			System.out.println("sEXCP_VAL2   :"+sEXCP_VAL2   );
	    			System.out.println("sEXCP_VAL3   :"+sEXCP_VAL3   );
	    			System.out.println("sEXCP_VAL4   :"+sEXCP_VAL4   );
	    			System.out.println("sEXCP_VAL5   :"+sEXCP_VAL5   );
	    			System.out.println("date         :"+date         );
	    			System.out.println("▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣");
	    			*/		
	    			
	    			
	    			
	    			pSeq = 1;				

	    			db.ps.setString(pSeq++, ini_num        ); 
	    			db.ps.setString(pSeq++, id             ); 
	    			db.ps.setString(pSeq++, type           ); 
	    			db.ps.setString(pSeq++, sGBN           ); 
	    			db.ps.setString(pSeq++, sPROC_DATE     ); 
	    			db.ps.setString(pSeq++, sROW_NUM       ); 
	    			db.ps.setString(pSeq++, sPAY_METHOD    ); 
	    			db.ps.setString(pSeq++, sMRCH_ID       ); 
	    			db.ps.setString(pSeq++, sPAY_DAY       ); 
	    			db.ps.setString(pSeq++, sBUY_ENPRI_CD  ); 
	    			db.ps.setString(pSeq++, sAPP_ENPRI_CD  ); 
	    			db.ps.setString(pSeq++, sTID           ); 
	    			db.ps.setString(pSeq++, sORG_TID       ); 
	    			db.ps.setString(pSeq++, sOID           ); 
	    			db.ps.setString(pSeq++, sTRA_GBN       ); 
	    			db.ps.setString(pSeq++, sAPRV_DATE     ); 
	    			db.ps.setString(pSeq++, sCNCL_DATE     ); 
	    			db.ps.setString(pSeq++, sAPRV_NUM      ); 
	    			db.ps.setString(pSeq++, sCRNC_GBN      ); 
	    			db.ps.setString(pSeq++, sPAY_PRI       ); 
	    			db.ps.setString(pSeq++, sCHARG         ); 
	    			db.ps.setString(pSeq++, sVAT           ); 
	    			db.ps.setString(pSeq++, sCHARGE_BACK   ); 
	    			db.ps.setString(pSeq++, sRE_PAY_PRI    ); 
	    			db.ps.setString(pSeq++, sERR_CD        ); 
	    			db.ps.setString(pSeq++, sERR_MSG       ); 
	    			db.ps.setString(pSeq++, sEXCP_VAL1     ); 
	    			db.ps.setString(pSeq++, sEXCP_VAL2     ); 
	    			db.ps.setString(pSeq++, sEXCP_VAL3     ); 
	    			db.ps.setString(pSeq++, sEXCP_VAL4     ); 
	    			db.ps.setString(pSeq++, sEXCP_VAL5     ); 
	    			db.ps.setString(pSeq++, date           ); 
	    			db.ps.setString(pSeq++, ini_num        ); 
	    			db.ps.setString(pSeq++, id             ); 
	    			db.ps.setString(pSeq++, type           ); 
	    			db.ps.setString(pSeq++, sGBN           ); 
	    			
	    			
					db.ps.addBatch();
						
	    		}
	    		
	    		
	    		int totCntDuzon = 0;
	    		int[] regcount = db.ps.executeBatch();
	    		for (int j = 0; j < regcount.length; j++) { 
	    			if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
	    				totCntDuzon++;
	    			}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
	    				throw new SQLException("INI_TRA_DTL 등록 "+(j + 1)+" 번째 배치 실행 에러발생."); 
	    			}else { 
	    				totCntDuzon += regcount[j]; 
	    			} 
	    		}
	    		
	    		log.Write(log_flag, prgm_nm, "commit db dtl info");
	    		db.commit();
				
	    	}catch(SQLException e){   
	    		rslt = -1;
	            //System.out.println("Error"+e);
	            log.Write(log_flag, prgm_nm, ""+e);
        		//System.out.println(e);
                e.printStackTrace();
                db.rollback();
                throw new Exception(e.getMessage());
	        }catch(Exception e){
	        	rslt = -1;
	            //System.out.println("Error"+e);
	            log.Write(log_flag, prgm_nm, ""+e);
                //System.out.println(e);
                e.printStackTrace();
                db.rollback();
                throw new Exception(e.getMessage());
	        }
		}
		
		return rslt;
	}
	
	
	/**
	 * 인터페이스를 통해 받아온 전문을 구분자로 분리하여 데이터 저장
	 * @param ini_num
	 * @param date
	 * @param id
	 * @param type
	 * @param url
	 * @param message
	 * @return
	 */
	public int insertDtlData(String ini_num, String date, String id, String type, String url, StringBuffer message) throws Exception{
		int rslt = 0;
		
		if(!"0001".equals(message.toString()) && !"0002".equals(message.toString()) && !"0003".equals(message.toString()) && !"CB".equals(type)){					
			
			log.Write(log_flag, prgm_nm, "db connection at insertDtlData");
			db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
			
			db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTCN";
			db.user_id="INICIS";
			db.user_pw="INICIS";	
			
			db.DB_Conn();
			
	    	int pSeq = 0;
	    	
	    	try{
	    		StringBuffer sbInsIniTraDtl = new StringBuffer();
	    		
	    		sbInsIniTraDtl.append("  INSERT INTO INI_TRA_DTL      ").append("\n");
	    		sbInsIniTraDtl.append("  (                            ").append("\n");
	    		sbInsIniTraDtl.append("   TRADE_ID                    ").append("\n");
	    		sbInsIniTraDtl.append("  ,OID                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,INI_ID                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,PAY_METHOD                  ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_STAT                    ").append("\n");
	    		sbInsIniTraDtl.append("  ,APRV_DATE                   ").append("\n");
	    		sbInsIniTraDtl.append("  ,APRV_TIME                   ").append("\n");
	    		sbInsIniTraDtl.append("  ,CNCL_DATE                   ").append("\n");
	    		sbInsIniTraDtl.append("  ,CNCL_TIME                   ").append("\n");
	    		sbInsIniTraDtl.append("  ,TID                         ").append("\n");
	    		sbInsIniTraDtl.append("  ,ACEP_MAN                    ").append("\n");
	    		sbInsIniTraDtl.append("  ,PRD_NM                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,PAY_PRI                     ").append("\n");
	    		sbInsIniTraDtl.append("  ,ORG_NM                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,ORG_NUM                     ").append("\n");
	    		sbInsIniTraDtl.append("  ,INI_NUM                     ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_DAY                     ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_ID                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,TRA_TY                      ").append("\n");
	    		sbInsIniTraDtl.append("  ,PROC_YN                     ").append("\n");
	    		sbInsIniTraDtl.append("  ,PROC_PRI                    ").append("\n");
	    		sbInsIniTraDtl.append("  ,REG_DATI                    ").append("\n");
	    		sbInsIniTraDtl.append("  )                            ").append("\n");
	    		sbInsIniTraDtl.append("  VALUES                       ").append("\n");
	    		sbInsIniTraDtl.append("  (                            ").append("\n");
	    		sbInsIniTraDtl.append("   ?            --TRADE_ID     ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --OID          ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --INI_ID       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --PAY_METHOD   ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --TRA_STAT     ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --APRV_DATE    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --APRV_TIME    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --CNCL_DATE    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --CNCL_TIME    ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --TID          ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --ACEP_MAN     ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --PRD_NM       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --PAY_PRI      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --ORG_NM       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --ORG_NUM      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --INI_NUM      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --TRA_DAY      ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --TRA_ID       ").append("\n");
	    		sbInsIniTraDtl.append("  ,?            --TRA_TY       ").append("\n");
	    		sbInsIniTraDtl.append("  ,'N'          --PROC_YN      ").append("\n");
	    		sbInsIniTraDtl.append("  ,NULL         --PROC_PRI     ").append("\n");
	    		sbInsIniTraDtl.append("  ,SYSDATE      --REG_DATI     ").append("\n");
	    		sbInsIniTraDtl.append("  )                            ").append("\n");
	    		
	    		db.ps = db.conn.prepareStatement(sbInsIniTraDtl.toString());	  
	    		
	    		StringBuffer sbSelIniTraDtlCnt = new StringBuffer();
	    		
	    		sbSelIniTraDtlCnt.append("  SELECT COUNT(*) CNT    ").append("\n");
	    		sbSelIniTraDtlCnt.append("    FROM INI_TRA_DTL     ").append("\n");
	    		sbSelIniTraDtlCnt.append("   WHERE TRADE_ID = ?    ").append("\n");
	    		sbSelIniTraDtlCnt.append("     AND OID = ?         ").append("\n");
	    		
	    		db.ps2 = db.conn.prepareStatement(sbSelIniTraDtlCnt.toString());
	    		
	    		String messages[] = (message.toString()).split("<br>");
	    		
	    		String sPK = "";
	    		for(int ii=0; ii< messages.length; ii++){
	    			//System.out.println(messages[ii]);
	    			String datas[] = messages[ii].split("\\|", -1);
	    			
	    			String sTRADE_ID   = datas[14];  //14  TID(Full)	
	    			String sOID        = datas[8 ];  //8   주문번호	
	    			String sINI_ID     = datas[0 ];  //0   상점ID      
	    			String sPAY_METHOD = datas[1 ];  //1   지불수단	
	    			String sTRA_STAT   = datas[2 ];  //2   거래상태	
	    			String sAPRV_DATE  = datas[3 ];  //3   승인일자	
	    			String sAPRV_TIME  = datas[4 ];  //4   승인시간	
	    			String sCNCL_DATE  = datas[5 ];  //5   취소일자	
	    			String sCNCL_TIME  = datas[6 ];  //6   취소시간	
	    			String sTID        = datas[7 ];  //7   TID		
	    			String sACEP_MAN   = datas[9 ];  //9   수취인		
	    			String sPRD_NM     = datas[10];  //10  상품명		
	    			String sPAY_PRI    = datas[11];  //11  입금금액		
	    			String sORG_NM     = datas[12];  //12  은행명		
	    			String sORG_NUM    = datas[13];  //13  가상계좌번호	
	    			
	    			int iDuplPk = sPK.indexOf(sTRADE_ID+"|"+sOID+"|"+sTRA_STAT+"|"+sPAY_PRI);
	    			sPK = sPK+sTRADE_ID+"|"+sOID+"|"+sTRA_STAT+"|"+sPAY_PRI+"||";
	    			
	    			/*
	    			System.out.println("▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣");
	    			System.out.println("datas.length:"+datas.length);
	    			System.out.println("sTRADE_ID  :"+sTRADE_ID  );
	    			System.out.println("sOID       :"+sOID       );
	    			System.out.println("sINI_ID    :"+sINI_ID    );
	    			System.out.println("sPAY_METHOD:"+sPAY_METHOD);
	    			System.out.println("sTRA_STAT  :"+sTRA_STAT  );
	    			System.out.println("sAPRV_DATE :"+sAPRV_DATE );
	    			System.out.println("sAPRV_TIME :"+sAPRV_TIME );
	    			System.out.println("sCNCL_DATE :"+sCNCL_DATE );
	    			System.out.println("sCNCL_TIME :"+sCNCL_TIME );
	    			System.out.println("sTID       :"+sTID       );
	    			System.out.println("sACEP_MAN  :"+sACEP_MAN  );
	    			System.out.println("sPRD_NM    :"+sPRD_NM    );
	    			System.out.println("sPAY_PRI   :"+sPAY_PRI   );
	    			System.out.println("sORG_NM    :"+sORG_NM    );
	    			System.out.println("sORG_NUM   :"+sORG_NUM   );
	    			System.out.println("ini_num    :"+ini_num    );
	    			System.out.println("date       :"+date       );
	    			System.out.println("id         :"+id         );
	    			System.out.println("type       :"+type       );
	    			System.out.println("▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣");
	    			*/
	    			pSeq = 1;
	    			db.ps2.setString(pSeq++, sTRADE_ID );   
	    			db.ps2.setString(pSeq++, sOID      );    
	    			db.prs2 = db.ps2.executeQuery();
	    			
	    			int iDtlCnt = 0;
	    			if(db.prs2 != null){		
	    				while(db.prs2.next()){
	    					iDtlCnt = Integer.parseInt(db.prs2.getString("cnt"));
	    				}
	    			}else{
	    				throw new SQLException("an error occured when inquery duplicate data from ini_tra_dtl");
	    			}
	    			
	    			//System.out.println("duplicated count in Rows:"+iDuplPk);
	    			//System.out.println("duplicated count:"+iDtlCnt);
	    			
	    			if(iDtlCnt == 0 && iDuplPk < 0){
	    			
		    			pSeq = 1;				
		    			db.ps.setString(pSeq++, sTRADE_ID    ); 
		    			db.ps.setString(pSeq++, sOID         ); 
		    			db.ps.setString(pSeq++, sINI_ID      ); 
		    			db.ps.setString(pSeq++, sPAY_METHOD  ); 
		    			db.ps.setString(pSeq++, sTRA_STAT    ); 
		    			db.ps.setString(pSeq++, sAPRV_DATE   ); 
		    			db.ps.setString(pSeq++, sAPRV_TIME   ); 
		    			db.ps.setString(pSeq++, sCNCL_DATE   ); 
		    			db.ps.setString(pSeq++, sCNCL_TIME   ); 
		    			db.ps.setString(pSeq++, sTID         ); 
		    			db.ps.setString(pSeq++, sACEP_MAN    ); 
		    			db.ps.setString(pSeq++, sPRD_NM      ); 
		    			db.ps.setString(pSeq++, sPAY_PRI     ); 
		    			db.ps.setString(pSeq++, sORG_NM      ); 
		    			db.ps.setString(pSeq++, sORG_NUM     ); 
		    			db.ps.setString(pSeq++, ini_num      ); 
		    			db.ps.setString(pSeq++, date         ); 
		    			db.ps.setString(pSeq++, id           ); 
		    			db.ps.setString(pSeq++, type         ); 
		    			
						db.ps.addBatch();
						
	    			}
	    		}
	    		
	    		
	    		int totCntDuzon = 0;
	    		int[] regcount = db.ps.executeBatch();
	    		for (int j = 0; j < regcount.length; j++) { 
	    			if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
	    				totCntDuzon++;
	    			}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
	    				throw new SQLException("INI_TRA_DTL 등록 "+(j + 1)+" 번째 배치 실행 에러발생."); 
	    			}else { 
	    				totCntDuzon += regcount[j]; 
	    			} 
	    		}
	    		
	    		log.Write(log_flag, prgm_nm, "commit db dtl info");
	    		db.commit();
				
	    	}catch(SQLException e){   
	    		rslt = -1;
	            //System.out.println("Error"+e);
	            log.Write(log_flag, prgm_nm, ""+e);
        		//System.out.println(e);
                e.printStackTrace();
                db.rollback();
                throw new Exception(e.getMessage());
	        }catch(Exception e){
	        	rslt = -1;
	            //System.out.println("Error"+e);
	            log.Write(log_flag, prgm_nm, ""+e);
                //System.out.println(e);
                e.printStackTrace();
                db.rollback();
                throw new Exception(e.getMessage());
	        }
		}
		
		return rslt;
	}
	
	
	/**
	 * 인터페이스를 통해 받아온 전문을 구분자로 분리하여 데이터 저장
	 * @param ini_num
	 * @return
	 */
	public int insertDSPayData(String ini_num) throws Exception{
		int rslt = 0;
			
		log.Write(log_flag, prgm_nm, "db connection at insertDSDtlData");
		db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
		
		db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTCN";
		db.user_id="INICIS";
		db.user_pw="INICIS";	
		
		db.DB_Conn();
		
    	int pSeq = 0;
    	
    	try{
			log.Write(log_flag, prgm_nm, "Defining the SQL to Insert INI_TRA_PAY");
    		StringBuffer sbInsTraPayData = new StringBuffer();
    		sbInsTraPayData.append("   INSERT INTO INI_TRA_PAY     ").append("\n");
    		sbInsTraPayData.append("         (INI_NUM              ").append("\n");
    		sbInsTraPayData.append("         ,TID                  ").append("\n");
    		sbInsTraPayData.append("         ,OID                  ").append("\n");
    		sbInsTraPayData.append("         ,TRA_GBN              ").append("\n");
    		sbInsTraPayData.append("         ,ORG_TID              ").append("\n");
    		sbInsTraPayData.append("         ,APRV_NUM             ").append("\n");
    		sbInsTraPayData.append("         ,MRCH_ID              ").append("\n");
    		sbInsTraPayData.append("         ,PAY_DAY              ").append("\n");
    		sbInsTraPayData.append("         ,PAY_METHOD           ").append("\n");
    		sbInsTraPayData.append("         ,PAY_PRI              ").append("\n");
    		sbInsTraPayData.append("         ,CHARG                ").append("\n");
    		sbInsTraPayData.append("         ,VAT                  ").append("\n");
    		sbInsTraPayData.append("         ,CHARGE_BACK          ").append("\n");
    		sbInsTraPayData.append("         ,RE_PAY_PRI           ").append("\n");
    		sbInsTraPayData.append("         ,PROC_YN              ").append("\n");
    		sbInsTraPayData.append("         ,PROC_PRI             ").append("\n");
    		sbInsTraPayData.append("         ,TRA_ID               ").append("\n");
    		sbInsTraPayData.append("         ,TRA_TY               ").append("\n");
    		sbInsTraPayData.append("         ,TRA_DAY              ").append("\n");
    		sbInsTraPayData.append("         ,REG_DATI)            ").append("\n");
    		sbInsTraPayData.append("   SELECT INI_NUM              ").append("\n");
    		sbInsTraPayData.append("         ,TID                  ").append("\n");
    		sbInsTraPayData.append("         ,OID                  ").append("\n");
    		sbInsTraPayData.append("         ,TRA_GBN              ").append("\n");
    		sbInsTraPayData.append("         ,ORG_TID              ").append("\n");
    		sbInsTraPayData.append("         ,APRV_NUM             ").append("\n");
    		sbInsTraPayData.append("         ,MRCH_ID              ").append("\n");
    		sbInsTraPayData.append("         ,PAY_DAY              ").append("\n");
    		sbInsTraPayData.append("         ,PAY_METHOD           ").append("\n");
    		sbInsTraPayData.append("         ,PAY_PRI              ").append("\n");
    		sbInsTraPayData.append("         ,CHARG                ").append("\n");
    		sbInsTraPayData.append("         ,VAT                  ").append("\n");
    		sbInsTraPayData.append("         ,CHARGE_BACK          ").append("\n");
    		sbInsTraPayData.append("         ,RE_PAY_PRI           ").append("\n");
    		sbInsTraPayData.append("         ,'N' PROC_YN          ").append("\n");
    		sbInsTraPayData.append("         ,0 PROC_PRI           ").append("\n");
    		sbInsTraPayData.append("         ,TRA_ID               ").append("\n");
    		sbInsTraPayData.append("         ,TRA_TY               ").append("\n");
    		sbInsTraPayData.append("         ,TRA_DAY              ").append("\n");
    		sbInsTraPayData.append("         ,SYSDATE REG_DATI     ").append("\n");
    		sbInsTraPayData.append("     FROM ini_tra_dtl          ").append("\n");
    		sbInsTraPayData.append("    WHERE ini_num = ?          ").append("\n");
    		sbInsTraPayData.append("      AND tra_ty = 'DS'        ").append("\n");
    		sbInsTraPayData.append("      AND gbn = 'B'            ").append("\n");
    		sbInsTraPayData.append("      AND OID is not null      ").append("\n"); //hrkim 150312 oid가 null 이 아닌건만 처리 (이니시스테스트 데이터때문)
    		
    		db.ps = db.conn.prepareStatement(sbInsTraPayData.toString());	  
			
    		log.Write(log_flag, prgm_nm, "Setting SQL parameters");
			pSeq = 1;
			db.ps.setString(pSeq++, ini_num);
			
			db.ps.executeUpdate();	
			
			log.Write(log_flag, prgm_nm, "commit db dtl info");
			db.commit();
			
    	}catch(SQLException e){   
    		rslt = -1;
            //System.out.println("Error"+e);
            log.Write(log_flag, prgm_nm, ""+e);
    		//System.out.println(e);
            e.printStackTrace();
            db.rollback();
            throw new Exception(e.getMessage());
        }catch(Exception e){
        	rslt = -1;
            //System.out.println("Error"+e);
            log.Write(log_flag, prgm_nm, ""+e);
            //System.out.println(e);
            e.printStackTrace();
            db.rollback();
            throw new Exception(e.getMessage());
        }
		
		return rslt;
	}
	
	

	/**
	 * 저장된 인터페이스 데이터를 가지고 수납처리
	 * @param ini_num
	 * @return
	 */
	public int procData(String ini_num) throws Exception{
		int rslt = 0;
		log.Write(log_flag, prgm_nm, "db connection at procData");
		db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
		
		db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTCN";
		db.user_id="INICIS";
		db.user_pw="INICIS";	
		
		db.DB_Conn();
		
    	int pSeq = 0;
    	
    	try{
    		log.Write(log_flag, prgm_nm, "Defining the SQL to Process P_PAY_LED.SP_INI_TRA_PAY");
    		StringBuffer sbIniTraProc = new StringBuffer();
    		sbIniTraProc.append("{call P_PAY_LED.SP_INI_TRA_PAY(?, ?, ?)}").append("\n");
    		
			db.cs = db.conn.prepareCall(sbIniTraProc.toString());	
			
			log.Write(log_flag, prgm_nm, "Setting SQL parameters");
			pSeq = 1;
			db.CsetString(pSeq++, ini_num);  
			db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
			db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
			
			db.cs.execute();	
			
			String sReturn = "";
			String sMsg = "";
			if(db.ERR_FLAG < 0){
				log.Write(prgm_nm, "P_PAY_LED.SP_INI_TRA_PAY 프로시저 실행 오류");
				throw new SQLException(db.ERR_MSG);
			}else{
				sReturn = db.cs.getString(2);
				sMsg    = db.cs.getString(3);
				
				log.Write(prgm_nm, "P_PAY_LED.SP_INI_TRA_PAY 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"OK".equals(sReturn)){
					log.Write(prgm_nm, "INICIS Interface 수납처리중 오류발생.\\n"+sMsg);
					throw new Exception("INICIS Interface 수납처리중 오류발생.\\n"+sMsg);
				}
			}
			
			log.Write(log_flag, prgm_nm, "commit db proc data");
    		db.commit();
			
    	}catch(SQLException e){   
    		rslt = -1;
            log.Write(log_flag, prgm_nm, ""+e);
            e.printStackTrace();
            db.rollback();
            throw new Exception(e.getMessage());
        }catch(Exception e){
        	rslt = -1;
            log.Write(log_flag, prgm_nm, ""+e);
            e.printStackTrace();
            db.rollback();
            throw new Exception(e.getMessage());
        }
		
		return rslt;
	}
	
	/**
	 * 일자에 대하여 파라미터 만큼 계산된 일자 반환
	 * @param currDate
	 * @param agoMon
	 * @param agoDay
	 * @return
	 */
	public String getAgoMonth(String currDate, int agoMon, int agoDay) {
		
		Calendar c1 = Calendar.getInstance();

		int iCurrYear = Integer.parseInt(currDate.substring(0, 4));
		int iCurrMonth = Integer.parseInt(currDate.substring(4, 6));
		int iCurrDay = Integer.parseInt(currDate.substring(6, 8));

		c1.set(iCurrYear, iCurrMonth - 1, iCurrDay);

		c1.add(Calendar.MONTH, agoMon);
		c1.add(Calendar.DAY_OF_MONTH, agoDay);

		String sAgoYear = "" + c1.get(Calendar.YEAR);
		String sAgoMonth = (c1.get(Calendar.MONTH) + 1) < 10 ? "0" + (c1.get(Calendar.MONTH) + 1) : "" + (c1.get(Calendar.MONTH) + 1);
		String sAgoDay = c1.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c1.get(Calendar.DAY_OF_MONTH) : "" + c1.get(Calendar.DAY_OF_MONTH);

		String sAgoDate = sAgoYear + sAgoMonth + sAgoDay;

		return sAgoDate;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("-------------Start inicis trade data interface-------------");
		log.Write(log_flag, prgm_nm, "-------------Start inicis trade data interface-------------");
		
		String[] id = {"ESmrocokr1","mrocokr002","speedmall2","speedmall3","speedmall4","IESspeed00","naraemall1"};
		String[] pass = {"mrok^^0705","mrok^^0705","mrok^^0705","mrok^^0705","mrok^^0705","mrok^^0705","mrok^^0705"};
		String date = "";
		String gmid = "";
		
		
		date = args.length > 0 ? "".equals(args[0]) ? "" : args[0] : "";
		
		
		/**/
		String[][] urls = {
				           {"https://iniweb.inicis.com/mall/cr/urlsvc/UrlSendCalCulAll.jsp","DS"} //정산대사
				          };
		/**
		String[][] urls = {
				           {"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendCard.jsp","CC"} //신용카드
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendACCT.jsp","CA"} //계좌이체
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendVACCT.jsp","VA"} //가상계좌
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendHPP.jsp","MO"} //휴대폰
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendARS.jsp","MC"} //전화결제
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendCULT.jsp","CU"} //문화상품권
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendTEEN.jsp","TC"} //틴캐쉬결제
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendBKCULT.jsp","BK"} //도서상품권
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendDGCULT.jsp","DG"} //게임상품권
				          ,{"https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendAll.jsp","CB"} //통합내역				          
				          };		
		/**/
		
		Demon_inicis_tra_sap dit = new Demon_inicis_tra_sap();
		
		//for(int ii=0; ii<id.length; ii++){
			dit.getData(urls, id, pass, date, gmid);
		//}
		
		//String rslt = dit.SendDataHttps("https://iniweb.inicis.com/DefaultWebApp/service/urlsvc/UrlSendCard.jsp?urlid=speedmall2&passwd=mrok^^0705&date=20130506", "SendData");
		//System.out.println(rslt);
		
		
		log.Write(log_flag, prgm_nm, "-------------End inicis trade data interface-------------");
		System.out.println("-------------End inicis trade data interface-------------");
	}

}
