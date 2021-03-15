import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import comm.mk_log;

public class Demon_cj_parcel {
	
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_cj_parcel";
	
	public String driver="oracle.jdbc.driver.OracleDriver";
	
	//행복나래 DB Info
	/**/
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";

	/**
	
	public String dbURL="jdbc:oracle:thin:@172.16.1.224:1521:testdb";
	public String user_id="METS_IMSI";
	public String user_pw="METS_IMSI";
	
	/**/
	
	// I/F DB Info
	/**/
	public String if_dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String if_user_id="INF_USER";
	public String if_user_pw="54INFUSER89";

	/**
	
	public String if_dbURL="jdbc:oracle:thin:@172.16.1.224:1521:testdb";
	public String if_user_id="METS_IMSI";
	public String if_user_pw="METS_IMSI";
	
	/**/
	
	// CJ DB Info
	/**/
	public String cj_dbURL="jdbc:oracle:thin:@210.98.159.153:1521:OPENDB";
	public String cj_user_id="happynarae";
	public String cj_user_pw="happynarae!@#$";

	/**
	
	public String cj_dbURL="jdbc:oracle:thin:@210.98.159.153:1523:OPENDBT";
	public String cj_user_id="happynarae";
	public String cj_user_pw="happynaraedev!@#$";
	
	/**/

	
	public	Connection conn, conn2, if_conn, if_conn2, cj_conn, cj_conn2;
	
	private Statement smt= null, smt2 = null, if_smt= null, if_smt2 = null, cj_smt= null, cj_smt2 = null;
	public PreparedStatement ps=null, ps2=null, if_ps=null, if_ps2=null, cj_ps=null, cj_ps2=null;
	public CallableStatement cs = null, cs2=null, if_cs = null, if_cs2=null, cj_cs = null, cj_cs2=null;
	public ResultSet rs = null, rs2 = null, if_rs = null, if_rs2 = null, cj_rs = null, cj_rs2 = null;
	public String qry, qry2, if_qry, if_qry2, cj_qry, cj_qry2 = "";
	
	public String sMessage = "";
		
	public Connection DB_Conn() {

		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(dbURL,user_id,user_pw);

			conn.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean: driver not loaded !!"+e.getMessage());
		}
		return conn;
		
	}
	
	public Connection DB_Conn2() {

		try {
			Class.forName(driver);
			conn2=DriverManager.getConnection(dbURL,user_id,user_pw);

			conn2.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean2: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean2: driver not loaded !!"+e.getMessage());
		}
		return conn2;
		
	}
	

	public Connection if_DB_Conn() {

		try {
			Class.forName(driver);
			if_conn=DriverManager.getConnection(if_dbURL,if_user_id,if_user_pw);

			if_conn.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean: driver not loaded !!"+e.getMessage());
		}
		return if_conn;
		
	}
	
	public Connection if_DB_Conn2() {

		try {
			Class.forName(driver);
			if_conn2=DriverManager.getConnection(if_dbURL,if_user_id,if_user_pw);

			if_conn2.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean2: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean2: driver not loaded !!"+e.getMessage());
		}
		return if_conn2;
		
	}
	

	public Connection cj_DB_Conn() {

		try {
			Class.forName(driver);
			cj_conn=DriverManager.getConnection(cj_dbURL,cj_user_id,cj_user_pw);

			cj_conn.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean: driver not loaded !!"+e.getMessage());
		}
		return cj_conn;
		
	}
	
	public Connection cj_DB_Conn2() {

		try {
			Class.forName(driver);
			cj_conn2=DriverManager.getConnection(cj_dbURL,cj_user_id,cj_user_pw);

			cj_conn2.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean2: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean2: driver not loaded !!"+e.getMessage());
		}
		return cj_conn2;
		
	}
	
	public void DB_DisConn() {
		try {
			conn.setAutoCommit(true);

			if(rs != null){ rs.close();}
			if(smt != null){ smt.close(); }
			if(ps != null){ ps.close(); }
			if(cs != null){ cs.close(); }
			if(conn != null){ conn.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection error !!"+e.getMessage());
		}
	}	
	
	public void DB_DisConn2() {
		try {
			conn2.setAutoCommit(true);

			if(rs2 != null){ rs2.close(); }
			if(smt2 != null){ smt2.close(); }
			if(ps2 != null){ ps2.close(); }
			if(cs2 != null){ cs2.close(); }
			if(conn2 != null){ conn2.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection2 error !!"+e.getMessage());
		}
	}
	
	public void if_DB_DisConn() {
		try {
			if_conn.setAutoCommit(true);

			if(if_rs != null){ if_rs.close();}
			if(if_smt != null){ if_smt.close(); }
			if(if_ps != null){ if_ps.close(); }
			if(if_cs != null){ if_cs.close(); }
			if(if_conn != null){ if_conn.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection error !!"+e.getMessage());
		}
	}	
	
	public void if_DB_DisConn2() {
		try {
			if_conn2.setAutoCommit(true);

			if(if_rs2 != null){ if_rs2.close(); }
			if(if_smt2 != null){ if_smt2.close(); }
			if(if_ps2 != null){ if_ps2.close(); }
			if(if_cs2 != null){ if_cs2.close(); }
			if(if_conn2 != null){ if_conn2.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection2 error !!"+e.getMessage());
		}
	}
	
	public void cj_DB_DisConn() {
		try {
			cj_conn.setAutoCommit(true);

			if(cj_rs != null){ cj_rs.close();}
			if(cj_smt != null){ cj_smt.close(); }
			if(cj_ps != null){ cj_ps.close(); }
			if(cj_cs != null){ cj_cs.close(); }
			if(cj_conn != null){ cj_conn.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection error !!"+e.getMessage());
		}
	}	
	
	public void cj_DB_DisConn2() {
		try {
			cj_conn2.setAutoCommit(true);

			if(cj_rs2 != null){ cj_rs2.close(); }
			if(cj_smt2 != null){ cj_smt2.close(); }
			if(cj_ps2 != null){ cj_ps2.close(); }
			if(cj_cs2 != null){ cj_cs2.close(); }
			if(cj_conn2 != null){ cj_conn2.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection2 error !!"+e.getMessage());
		}
	}
	
	public void start() throws Exception {
		
		int pSeq = 1;
		int iTotCnt = 0;
		String sMSG = "";
		int errCnt = 0;
		List<String> lErrList = new ArrayList<String>();
		
		try{
			DB_Conn();
			qry =   ""+
					"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_cj_parcel_kimjs','START',SYSDATE, 'OK', NULL ) "+					
					"";
			ps = conn.prepareStatement(qry);
			ps.executeUpdate();
			conn.commit();
			DB_DisConn();
			
			
			cj_DB_Conn();
			
			
			/*
			 * 동기화된 행복나래 택배 결과 테이블을 사용하여 결과 처리 한다.
			 */
			sMSG = "행복나래 택배 처리결과 테이블에서 처리대상 조회";
			
			
	    	qry =   ""+
	    			" SELECT SERIAL, CUST_ID, RCPT_DV, INVC_NO, CUST_USE_NO, CRG_ST, SCAN_YMD, SCAN_HOUR           "+
					"       , DEALT_BRAN_NM, DEALT_BRAN_TEL, DEALT_EMP_NM, DEALT_EMP_TEL, ACPTR_NM, NO_CLDV_RSN_CD "+
					"       , DETAIL_RSN, EAI_PRGS_ST, EAI_ERR_MSG, REG_EMP_ID, to_char(REG_DTIME,'yyyymmddhh24miss') REG_DTIME, MODI_EMP_ID, to_char(MODI_DTIME,'yyyymmddhh24miss') MODI_DTIME "+
					"   FROM V_TRACE_HAPPYNARAE020                                                                 "+
					"  WHERE 1=1                                                                                   "+
					"    AND (REG_DTIME >= SYSDATE - 1                                                             "+
					"     OR EAI_PRGS_ST = '01')                                                                   "+
	    			"";
			
	    	cj_ps = cj_conn.prepareStatement(qry);
			pSeq = 1;
			
			cj_rs = cj_ps.executeQuery();
			
			List cjList = new ArrayList();
			
			if(cj_rs != null){
				while(cj_rs.next()){
					HashMap hm = new HashMap();
					hm.put("SERIAL", cj_rs.getString("SERIAL"));
					hm.put("CUST_ID", cj_rs.getString("CUST_ID"));
					hm.put("RCPT_DV", cj_rs.getString("RCPT_DV"));
					hm.put("INVC_NO", cj_rs.getString("INVC_NO"));
					hm.put("CUST_USE_NO", cj_rs.getString("CUST_USE_NO"));
					hm.put("CRG_ST", cj_rs.getString("CRG_ST"));
					hm.put("SCAN_YMD", cj_rs.getString("SCAN_YMD"));
					hm.put("SCAN_HOUR", cj_rs.getString("SCAN_HOUR"));
					hm.put("DEALT_BRAN_NM", cj_rs.getString("DEALT_BRAN_NM"));
					hm.put("DEALT_BRAN_TEL", cj_rs.getString("DEALT_BRAN_TEL"));
					hm.put("DEALT_EMP_NM", cj_rs.getString("DEALT_EMP_NM"));
					hm.put("DEALT_EMP_TEL", cj_rs.getString("DEALT_EMP_TEL"));
					hm.put("ACPTR_NM", cj_rs.getString("ACPTR_NM"));
					hm.put("NO_CLDV_RSN_CD", cj_rs.getString("NO_CLDV_RSN_CD"));
					hm.put("DETAIL_RSN", cj_rs.getString("DETAIL_RSN"));
					hm.put("EAI_PRGS_ST", cj_rs.getString("EAI_PRGS_ST"));
					hm.put("EAI_ERR_MSG", cj_rs.getString("EAI_ERR_MSG"));
					hm.put("REG_EMP_ID", cj_rs.getString("REG_EMP_ID"));
					hm.put("REG_DTIME", cj_rs.getString("REG_DTIME"));
					hm.put("MODI_EMP_ID", cj_rs.getString("MODI_EMP_ID"));
					hm.put("MODI_DTIME", cj_rs.getString("MODI_DTIME"));
					cjList.add(hm);					
				}
			}else{
				throw new SQLException(sMSG+" 오류");
			}

			cj_DB_DisConn();
			
			

			
			
			
			/*
			 * CJ 택배 결과 값을 행복나래로 가져온다.
			 */
			if_DB_Conn();
			
			sMSG = "CJ 택배 결과값 행복나래 삭제";
			qry =   ""+
					" DELETE FROM CJ_TRACE_HAPPYNARAE020_TEMP "+
					"";
			
			if_ps = if_conn.prepareStatement(qry);
			
			pSeq = 1;
			if_ps.executeUpdate();		
			
			

			qry = ""+
					" INSERT INTO CJ_TRACE_HAPPYNARAE020_TEMP "+
					" (SERIAL, CUST_ID, RCPT_DV, INVC_NO, CUST_USE_NO, CRG_ST, SCAN_YMD, SCAN_HOUR "+
					" , DEALT_BRAN_NM, DEALT_BRAN_TEL, DEALT_EMP_NM, DEALT_EMP_TEL, ACPTR_NM, NO_CLDV_RSN_CD "+
					" , DETAIL_RSN, EAI_PRGS_ST, EAI_ERR_MSG, REG_EMP_ID, REG_DTIME, MODI_EMP_ID, MODI_DTIME) "+
					" VALUES "+
					" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyymmddhh24miss'),?,to_date(?,'yyyymmddhh24miss')) "+
			      "";
			
			if_ps = if_conn.prepareStatement(qry);	
			
			for(int ii=0; ii<cjList.size(); ii++){
				pSeq = 1;
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SERIAL"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SERIAL"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CUST_ID"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CUST_ID"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCPT_DV"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCPT_DV"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("INVC_NO"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("INVC_NO"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CUST_USE_NO"   ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CUST_USE_NO"   ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CRG_ST"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CRG_ST"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SCAN_YMD"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SCAN_YMD"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SCAN_HOUR"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SCAN_HOUR"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("DEALT_BRAN_NM" ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("DEALT_BRAN_NM" ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("DEALT_BRAN_TEL") == null ? "" : (String)((HashMap)cjList.get(ii)).get("DEALT_BRAN_TEL"));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("DEALT_EMP_NM"  ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("DEALT_EMP_NM"  ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("DEALT_EMP_TEL" ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("DEALT_EMP_TEL" ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ACPTR_NM"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ACPTR_NM"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("NO_CLDV_RSN_CD") == null ? "" : (String)((HashMap)cjList.get(ii)).get("NO_CLDV_RSN_CD"));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("DETAIL_RSN"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("DETAIL_RSN"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("EAI_PRGS_ST"   ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("EAI_PRGS_ST"   ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("EAI_ERR_MSG"   ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("EAI_ERR_MSG"   ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REG_EMP_ID"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REG_EMP_ID"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REG_DTIME"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REG_DTIME"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("MODI_EMP_ID"   ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("MODI_EMP_ID"   ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("MODI_DTIME"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("MODI_DTIME"    ));
				if_ps.addBatch();	
			}
			
			int regcount[]  = null;
			int updCntHappyPartner = 0;
			regcount = if_ps.executeBatch();
			for (int j = 0; j < regcount.length; j++) { 
				if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
					updCntHappyPartner++;
				}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
					throw new SQLException("행복나래 ma_partner_duzon 수정 "+(j + 1)+" 번째 배치 실행 에러발생."); 
				}else { 
					updCntHappyPartner += regcount[j]; 
				} 
			}
			
			
			sMSG = "CJ 택배 결과값 행복나래 등록";
			qry =   ""+
					" INSERT INTO CJ_TRACE_HAPPYNARAE020                                                           "+
					" SELECT SERIAL, CUST_ID, RCPT_DV, INVC_NO, CUST_USE_NO, CRG_ST, SCAN_YMD, SCAN_HOUR           "+
					"       ,DEALT_BRAN_NM, DEALT_BRAN_TEL, DEALT_EMP_NM, DEALT_EMP_TEL, ACPTR_NM, NO_CLDV_RSN_CD  "+
					"       ,DETAIL_RSN, EAI_PRGS_ST, EAI_ERR_MSG, REG_EMP_ID, REG_DTIME, MODI_EMP_ID, MODI_DTIME  "+
					"       ,'N'                                                                                   "+
					"   FROM CJ_TRACE_HAPPYNARAE020_TEMP A                                                         "+
					"  WHERE 1=1                                                                                   "+
					"    AND REG_DTIME >= SYSDATE - 30                                                             "+
					"    AND EAI_PRGS_ST = '01'                                                                    "+
					"    AND NOT EXISTS (SELECT 'X' FROM CJ_TRACE_HAPPYNARAE020 X                                  "+
					"                     WHERE X.SERIAL = A.SERIAL                                                "+
					"                   )                                                                          "+
					"";
			
			if_ps = if_conn.prepareStatement(qry);	
			if_ps.executeUpdate();
			
			if_conn.commit();
			
			
			
			
			/*
			 * 동기화된 행복나래 택배 결과 테이블을 사용하여 결과 처리 한다.
			 */
			sMSG = "행복나래 택배 처리결과 테이블에서 처리대상 조회";
			
			
	    	qry =   ""+
	    			" SELECT SERIAL, CUST_ID, RCPT_DV, INVC_NO, CUST_USE_NO, CRG_ST, SCAN_YMD, SCAN_HOUR           "+
					"       ,DEALT_BRAN_NM, DEALT_BRAN_TEL, DEALT_EMP_NM, DEALT_EMP_TEL, ACPTR_NM, NO_CLDV_RSN_CD  "+
					"       ,DETAIL_RSN, EAI_PRGS_ST, EAI_ERR_MSG, REG_EMP_ID, REG_DTIME, MODI_EMP_ID, MODI_DTIME  "+
					"       ,PROC_YN                                                                               "+
					"   FROM CJ_TRACE_HAPPYNARAE020                                                                "+
					"  WHERE 1=1                                                                                   "+
					"    AND PROC_YN = 'N'                                                                         "+
					"    AND (REG_DTIME >= SYSDATE - 30                                                            "+
					"     OR EAI_PRGS_ST = '01')                                                                   "+
	    			"";
			
	    	if_ps = if_conn.prepareStatement(qry);
			pSeq = 1;
			
			if_rs = if_ps.executeQuery();
			
			List lRsltList = new ArrayList();
			
			if(if_rs != null){
				while(if_rs.next()){
					HashMap hm = new HashMap();
					hm.put("SERIAL", if_rs.getString("SERIAL"));
					hm.put("CUST_ID", if_rs.getString("CUST_ID"));
					hm.put("RCPT_DV", if_rs.getString("RCPT_DV"));
					hm.put("INVC_NO", if_rs.getString("INVC_NO"));
					hm.put("CUST_USE_NO", if_rs.getString("CUST_USE_NO"));
					hm.put("CRG_ST", if_rs.getString("CRG_ST"));
					hm.put("SCAN_YMD", if_rs.getString("SCAN_YMD"));
					hm.put("SCAN_HOUR", if_rs.getString("SCAN_HOUR"));
					hm.put("DEALT_BRAN_NM", if_rs.getString("DEALT_BRAN_NM"));
					hm.put("DEALT_BRAN_TEL", if_rs.getString("DEALT_BRAN_TEL"));
					hm.put("DEALT_EMP_NM", if_rs.getString("DEALT_EMP_NM"));
					hm.put("DEALT_EMP_TEL", if_rs.getString("DEALT_EMP_TEL"));
					hm.put("ACPTR_NM", if_rs.getString("ACPTR_NM"));
					hm.put("NO_CLDV_RSN_CD", if_rs.getString("NO_CLDV_RSN_CD"));
					hm.put("DETAIL_RSN", if_rs.getString("DETAIL_RSN"));
					hm.put("EAI_PRGS_ST", if_rs.getString("EAI_PRGS_ST"));
					hm.put("EAI_ERR_MSG", if_rs.getString("EAI_ERR_MSG"));
					hm.put("REG_EMP_ID", if_rs.getString("REG_EMP_ID"));
					hm.put("REG_DTIME", if_rs.getString("REG_DTIME"));
					hm.put("MODI_EMP_ID", if_rs.getString("MODI_EMP_ID"));
					hm.put("MODI_DTIME", if_rs.getString("MODI_DTIME"));
					hm.put("PROC_YN", if_rs.getString("PROC_YN"));
					lRsltList.add(hm);					
				}
			}else{
				throw new SQLException(sMSG+" 오류");
			}
			
			if_DB_DisConn();
			
				
			iTotCnt = lRsltList.size();//전체 처리갯수			
			int iLoopSize = lRsltList.size()%50 == 0 ? lRsltList.size()/50 : lRsltList.size()/50+1;
			
			sMSG = "전체 처리갯수 50개씩 처리 전체갯수:"+iTotCnt+" 반복횟수:"+iLoopSize;
			
			for(int ii=0; ii<iLoopSize; ii++){
				
				List lProcList = new ArrayList();
				for(int jj=0; jj<50; jj++){
					//System.out.println((ii*50 + jj) +"-->"+ ((HashMap)lRsltList.get(ii*50 + jj)).get("SERIAL"));
					lProcList.add((HashMap)lRsltList.get(ii*50 + jj));
					if(lRsltList.size() == (ii*50 + jj)+1){
						break;
					}
				}
				
				
				for(int kk=0; kk<lProcList.size(); kk++){
					HashMap<String, Object> procMap = new HashMap();
					procMap = (HashMap)lProcList.get(kk);
					
					String sSerial         = (String)procMap.get("SERIAL");
					String sCust_id        = (String)procMap.get("CUST_ID");
					String sRcpt_dv        = (String)procMap.get("RCPT_DV");
					String sInvc_no        = (String)procMap.get("INVC_NO");
					String sCust_use_no    = (String)procMap.get("CUST_USE_NO");
					String sCrg_st         = (String)procMap.get("CRG_ST");
					String sScan_ymd       = (String)procMap.get("SCAN_YMD");
					String sScan_hour      = (String)procMap.get("SCAN_HOUR");
					String sDealt_bran_nm  = (String)procMap.get("DEALT_BRAN_NM");
					String sDealt_bran_tel = (String)procMap.get("DEALT_BRAN_TEL");
					String sDealt_emp_nm   = (String)procMap.get("DEALT_EMP_NM");
					String sDealt_emp_tel  = (String)procMap.get("DEALT_EMP_TEL");
					String sAcptr_nm       = (String)procMap.get("ACPTR_NM");
					String sNo_cldv_rsn_cd = (String)procMap.get("NO_CLDV_RSN_CD");
					String sDetail_rsn     = (String)procMap.get("DETAIL_RSN");
					String sEai_prgs_st    = (String)procMap.get("EAI_PRGS_ST");
					String sEai_err_msg    = (String)procMap.get("EAI_ERR_MSG");
					String sReg_emp_id     = (String)procMap.get("REG_EMP_ID");
					String sReg_dtime      = (String)procMap.get("REG_DTIME");
					String sModi_emp_id    = (String)procMap.get("MODI_EMP_ID");
					String sModi_dtime     = (String)procMap.get("MODI_DTIME");
					String sProc_yn        = (String)procMap.get("PROC_YN");
					
					System.out.println("SERIAL------------------>"+sSerial+"   sInvc_no------>"+sInvc_no);
					
					DB_Conn();
					
					boolean isSucc = false;
					
					try{
						//배송완료일때만 처리함 반품건은 처리 안하고 정상건만 처리한다.반품건은 창고에서 검수완료처리함
						if("91".equals(sCrg_st) && "01".equals(sRcpt_dv)){
							sMSG = ii+"번째 중 "+kk+"번째처리 SERIAL:"+sSerial+" CUST_USE_NO:"+sCust_use_no;
							
							if(sCust_use_no == null || "".equals(sCust_use_no) || sCust_use_no.indexOf("-") < 0 || sCust_use_no.length() != (sCust_use_no.getBytes()).length){
								//continue;
							}else{
						
								sMSG = "택배 IF 수신 Update";//System.out.println(sMSG);						
								qry =   ""+
										" UPDATE HOMEDELI_DTL                 "+
										"    SET INF_RECV_DATI = SYSDATE      "+
										"       ,SYS_MEMO = 'CJ INTERFACE DATA GETTING....'  "+
										"  WHERE BOXREF_NUM||'-'||BOX_SEQ = ? "+
										"    AND INF_RECV_DATI IS NULL        "+
										"";
								
								ps = conn.prepareStatement(qry);	
			
								pSeq = 1;
								ps.setString(pSeq++, sCust_use_no);
								
								ps.executeUpdate();
								
								
						    	
								sMSG = "수신 안된 송장 조회";//System.out.println(sMSG);
								qry =   ""+
						    			" SELECT COUNT(*) CNT                 "+
						    			"   FROM HOMEDELI_DTL                 "+
						    			"  WHERE BOXREF_NUM = SUBSTRB(?,1,11) "+
						    			"    AND INF_RECV_DATI IS NULL        "+
						    			"    AND CANCEL_MEMO IS NULL          "+
						    			"    AND HOMEDELI_NO = ?              "+
						    			"";
								
								ps = conn.prepareStatement(qry);
								
								pSeq = 1;
								ps.setString(pSeq++, sCust_use_no);
								ps.setString(pSeq++, sInvc_no);
								
								rs = ps.executeQuery();
								
								int iHomedeli_cnt = 0;
								if(rs != null){
									while(rs.next()){
										iHomedeli_cnt = rs.getInt("CNT");
									}
								}else{
									throw new SQLException(sMSG + " 오류");
								}
								
								
								sMSG = "인터페이스 구분 조회";//System.out.println(sMSG);
								qry =   ""+
						    			" SELECT DELI_TY                 "+
						    			"   FROM HOMEDELI_DTL                 "+
						    			"  WHERE BOXREF_NUM = SUBSTRB(?,1,11) "+
						    			"    AND HOMEDELI_NO = ?              "+
						    			"";
								
								ps = conn.prepareStatement(qry);
								
								pSeq = 1;
								ps.setString(pSeq++, sCust_use_no);
								ps.setString(pSeq++, sInvc_no);
								
								rs = ps.executeQuery();
								
								String sDeli_ty = "";
								if(rs != null){
									while(rs.next()){
										sDeli_ty = rs.getString("DELI_TY");
									}
								}else{
									throw new SQLException(sMSG+" 오류");
								}
								
								//System.out.println("sDeli_ty-->"+sDeli_ty+" iHomedeli_cnt-->"+iHomedeli_cnt);
								//인터페이스 구분 정상건이고 IF 미수신건 없을때 행복나래 완료처리
								if(!"002".equals(sDeli_ty) && !"004".equals(sDeli_ty)){
									if(iHomedeli_cnt < 1){
										sMSG = "완료처리 할 거래명세서 조회";//System.out.println(sMSG);
										qry =   ""+
												"SELECT TD.TRA_NUM                                   "+
												"      ,TD.ORDS_NUM                                  "+
												"      ,TD.LIST_NUM                                  "+
												"      ,TD.TRA_SEQ                                   "+
												//"      ,NVL (TD.CNT, 0) CNT                          "+//20151111 김진성 수량에서 완료수량으로 변경함
												"      ,NVL (NVL(TD.COMP, TD.CNT), 0) CNT            "+
												"      ,NVL (TD.CANL, 0) CANL                        "+
												"      ,NVL (TD.REFU, 0) REFU                        "+
												"      ,NVL (TD.EXCH, 0) EXCH                        "+
												"      ,TD.CAUSE                                     "+
												"      ,TD.COMP_DATI_CUST                            "+
												"      ,NVL (OD.ASP_FLAG, EI.ASP_FLAG) ASP_FLAG      "+
												"      ,ED.TRA_PROC_TY                               "+
												"      ,EI.URL                                       "+
												"      ,EI.DB_USER                                   "+
												"      ,EI.DB_PW                                     "+
												"      ,(SELECT CUST_NM FROM CUST_DTL                "+
												"         WHERE CUST_ID = OD.ACEP_ID) ACEP_MAN       "+
												"      ,TD.DELI_STAT_FLAG                            "+
												"      ,BI.DELI_COMP_ID                              "+
												"  FROM TRA_DTL TD                                   "+
												"      ,ORDS_DTL OD                                  "+
												"      ,CUST_DTL CD                                  "+
												"      ,ENPRI_INFO EI                                "+
												"      ,ENPRI_DTL ED                                 "+
												"      ,BOXREF_INFO BI                               "+
												" WHERE 1=1                                          "+
												"   AND TD.TRA_NUM = SUBSTRB (?, 1, 11)              "+
												"   AND TD.ORDS_NUM = OD.ORDS_NUM                    "+
												"   AND TD.LIST_NUM = OD.LIST_NUM                    "+
												"   AND NVL (OD.REQ_ACEP_ID, OD.ACEP_ID) = CD.CUST_ID"+
												"   AND CD.ENPRI_CD = EI.ENPRI_CD                    "+
												"   AND CD.ENPRI_CD = ED.ENPRI_CD                    "+
												"   AND CD.BUSIP_SEQ = ED.BUSIP_SEQ                  "+
												"   AND TD.TRA_NUM = BI.BOXREF_NUM(+)                "+
								    			"";
										
										ps = conn.prepareStatement(qry);
										
										pSeq = 1;
										ps.setString(pSeq++, sCust_use_no);
										
										rs = ps.executeQuery();
										
	
										
										List traList = new ArrayList();
										
										if(rs != null){
											while(rs.next()){
												HashMap hm = new HashMap();
												hm.put("TRA_NUM"       , rs.getString("TRA_NUM")            );
												hm.put("ORDS_NUM"      , rs.getString("ORDS_NUM")           );
												hm.put("LIST_NUM"      , rs.getString("LIST_NUM")           );
												hm.put("TRA_SEQ"       , rs.getString("TRA_SEQ")            );
												hm.put("CNT"           , rs.getString("CNT")                );
												hm.put("CANL"          , rs.getString("CANL")               );
												hm.put("REFU"          , rs.getString("REFU")               );
												hm.put("EXCH"          , rs.getString("EXCH")               );
												hm.put("CAUSE"         , rs.getString("CAUSE")              );
												hm.put("COMP_DATI_CUST", rs.getString("COMP_DATI_CUST")     );
												hm.put("ASP_FLAG"      , rs.getString("ASP_FLAG")           );
												hm.put("TRA_PROC_TY"   , rs.getString("TRA_PROC_TY")        );
												hm.put("URL"           , rs.getString("URL")                );
												hm.put("DB_USER"       , rs.getString("DB_USER")            );
												hm.put("DB_PW"         , rs.getString("DB_PW")              );
												hm.put("ACEP_MAN"      , rs.getString("ACEP_MAN")           );
												hm.put("DELI_STAT_FLAG", rs.getString("DELI_STAT_FLAG")     );
												hm.put("DELI_COMP_ID"  , rs.getString("DELI_COMP_ID")       );
												traList.add(hm);	
											}
										}else{
											throw new SQLException(sMSG+" 오류");
										}
										
										String sTra_num        = "";
										String sOrds_num       = "";
										String sList_num       = "";
										String sTra_seq        = "";
										String sCnt            = "";
										String sCanl           = "";
										String sRefu           = "";
										String sExch           = "";
										String sCause          = "";
										String sComp_dati_cust = "";
										String sAsp_flag       = "";
										String sTra_proc_ty    = "";
										String sUrl            = "";
										String sDb_user        = "";
										String sDb_pw          = "";
										String sAcep_man       = "";
										String sDeli_stat_flag = "";
										String sDeli_comp_id   = "";
										String sDeli_comp_dati = sScan_ymd;
										String sDeli_comp_memo = "CJ대한통운 배송완료로 인한 인터페이스 완료처리";
										
										for(int ll=0; ll<traList.size(); ll++){
											sTra_num        = (String)((HashMap)traList.get(ll)).get("TRA_NUM"       );
											sOrds_num       = (String)((HashMap)traList.get(ll)).get("ORDS_NUM"      );
											sList_num       = (String)((HashMap)traList.get(ll)).get("LIST_NUM"      );
											sTra_seq        = (String)((HashMap)traList.get(ll)).get("TRA_SEQ"       );
											sCnt            = (String)((HashMap)traList.get(ll)).get("CNT"           );
											sCanl           = (String)((HashMap)traList.get(ll)).get("CANL"          );
											sRefu           = (String)((HashMap)traList.get(ll)).get("REFU"          );
											sExch           = (String)((HashMap)traList.get(ll)).get("EXCH"          );
											sCause          = (String)((HashMap)traList.get(ll)).get("CAUSE"         );
											sComp_dati_cust = (String)((HashMap)traList.get(ll)).get("COMP_DATI_CUST");
											sAsp_flag       = (String)((HashMap)traList.get(ll)).get("ASP_FLAG"      );
											sTra_proc_ty    = (String)((HashMap)traList.get(ll)).get("TRA_PROC_TY"   );
											sUrl            = (String)((HashMap)traList.get(ll)).get("URL"           );
											sDb_user        = (String)((HashMap)traList.get(ll)).get("DB_USER"       );
											sDb_pw          = (String)((HashMap)traList.get(ll)).get("DB_PW"         );
											sAcep_man       = (String)((HashMap)traList.get(ll)).get("ACEP_MAN"      );
											sDeli_stat_flag = (String)((HashMap)traList.get(ll)).get("DELI_STAT_FLAG");
											sDeli_comp_id   = (String)((HashMap)traList.get(ll)).get("DELI_COMP_ID"  );
											
											
											//출고진행상태 출고 진행상태 --001 출고지시, 002 피킹, 003 상차지시, 004 상차존적치, 005 상차완료 , 006 출고완료
											if("006".equals(sDeli_stat_flag)){
												log.Write(log_flag, prgm_nm, "이미 완료처리된 데이터 : "+sTra_num+":"+sOrds_num+":"+sList_num+":"+sTra_seq+":"+sCnt+":"+sDeli_comp_id);
											}else{
												if("004".equals(sDeli_stat_flag)){
													
													sMSG = "상차완료 안된 건 조회";//System.out.println(sMSG);
													qry =   ""+
											    			" SELECT COUNT(*) CNT           "+
											    			"   FROM TRA_DTL                "+
											    			"  WHERE TRA_NUM = ?            "+
											    			"    AND DELI_STAT_FLAG = '004' "+
											    			"";
													
													ps = conn.prepareStatement(qry);
													
													pSeq = 1;
													ps.setString(pSeq++, sTra_num);
													
													rs = ps.executeQuery();
													
													int iTra_cnt = 0;
													if(rs != null){
														while(rs.next()){
															iTra_cnt = rs.getInt("CNT");
														}
													}else{
														throw new SQLException(sMSG+" 오류");
													}
													
													//상차완료 안된건 강제 완료처리
													if(iTra_cnt > 0){
														sMSG = "상차완료 안된건 강제 완료처리";//System.out.println(sMSG);
														SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
														Date date = new Date();
														String daytime = format.format(date);
														
														qry = "{call P_CHULGO_MNG.STORE_OUT_UPCAR_DONE(?, ?, ?, ?)}";
											    		
														cs = conn.prepareCall(qry);	
														
														pSeq = 1;
														cs.setString(pSeq++, sTra_num);  
														cs.setString(pSeq++, "0002");  
														cs.setString(pSeq++, "INFDEMON");  
														cs.setString(pSeq++, daytime);
														
														cs.execute();	
													}
													
													
												}
												
												
												sMSG = "배송 완료일자 없을시 오늘 날짜로 생성";//System.out.println(sMSG);
												if("".equals(sDeli_comp_dati)){
													SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
													Date date = new Date();
													sDeli_comp_dati = format.format(date);
												}
												
												
												//maps 처리
												String sMapsRslt = "";
												if("".equals(sComp_dati_cust) && "001".equals(sAsp_flag) && ("002".equals(sTra_proc_ty) || "003".equals(sTra_proc_ty))){
													sMSG = "MAPS 처리";//System.out.println(sMSG);
													sMapsRslt = this.proc_maps(sDb_user, sTra_num, sOrds_num, sList_num, sTra_seq, sCnt, sCanl, sDeli_comp_dati, sAcep_man);
												}
												
												
												sMSG = "주문정보찾기";//System.out.println(sMSG);
												if("".equals(sOrds_num) || sOrds_num == null){
													throw new SQLException(sMSG+" 오류");
												}
												
												
												//출고완료처리
												sMSG = "출고 완료처리";//System.out.println(sMSG);
												qry = "{call P_STORE_IN_OUT_MNG.OUTPUT_STOCK_END(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
									    		
												cs = conn.prepareCall(qry);	
												
												pSeq = 1;
												cs.setString(pSeq++, sOrds_num);  
												cs.setString(pSeq++, sList_num);  
												cs.setString(pSeq++, sTra_num);  
												cs.setString(pSeq++, sTra_seq);  
												cs.setString(pSeq++, sCnt);  
												cs.setString(pSeq++, sCanl);  
												cs.setString(pSeq++, "0");  
												cs.setString(pSeq++, sExch);  
												cs.setString(pSeq++, sDeli_comp_dati);  
												cs.setString(pSeq++, sAcep_man);   
												cs.setString(pSeq++, "");  
												//cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//NRET      
												
												//System.out.println("출고완료처리 sOrds_num-->"+sOrds_num);
												//System.out.println("출고완료처리 sList_num-->"+sList_num);
												//System.out.println("출고완료처리 sTra_num-->"+sTra_num);
												//System.out.println("출고완료처리 sTra_seq-->"+sTra_seq);
												//System.out.println("출고완료처리 sCnt-->"+sCnt);
												//System.out.println("출고완료처리 sCanl-->"+sCanl);
												//System.out.println("출고완료처리 sExch-->"+sExch);
												//System.out.println("출고완료처리 sDeli_comp_dati-->"+sDeli_comp_dati);
												//System.out.println("출고완료처리 sAcep_man-->"+sAcep_man);
												
												cs.execute();	
												
												//String sReturn = cs.getString(11);
												
												
												sMSG = "출고완료처리 안된 송장 조회";//System.out.println(sMSG);
												qry =   ""+
										    			" SELECT COUNT(*) CNT           "+
										    			"   FROM BOXREF_INFO            "+
										    			"  WHERE BOXREF_NUM = ?         "+
										    			"    AND DELI_COMP_DATI IS NULL "+
										    			"";
												
												ps = conn.prepareStatement(qry);
												
												pSeq = 1;
												ps.setString(pSeq++, sTra_num);
												
												rs = ps.executeQuery();
												
												int iBoxref_cnt = 0;
												if(rs != null){
													while(rs.next()){
														iBoxref_cnt = rs.getInt("CNT");
													}
												}else{
													throw new SQLException(sMSG+" 오류");
												}
												
												
												if(iBoxref_cnt > 0){										
													sMSG = "배송 완료처리";//System.out.println(sMSG);
													SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
													Date date = new Date();
			
													//System.out.println("배송완료처리-->"+sTra_num);
													//System.out.println("배송완료처리-->"+sDeli_comp_dati.substring(0,4)+"-"+sDeli_comp_dati.substring(4,6)+"-"+sDeli_comp_dati.substring(6));
													//System.out.println("배송완료처리-->"+sAcep_man);
													//System.out.println("배송완료처리-->"+format.format(date));
													
													qry = "{call P_CHULGO_MNG.STORE_OUT_DELI_COMP_DONE(?, to_date(?,'yyyy-mm-dd'), ?, ?, ?, ?, ?)}";
										    		
													cs = conn.prepareCall(qry);	
													
													pSeq = 1;
													cs.setString(pSeq++, sTra_num);  
													cs.setString(pSeq++, sDeli_comp_dati.substring(0,4)+"-"+sDeli_comp_dati.substring(4,6)+"-"+sDeli_comp_dati.substring(6));  
													cs.setString(pSeq++, sAcep_man);  
													cs.setString(pSeq++, "");  
													cs.setString(pSeq++, "INF");  
													cs.setString(pSeq++, "INF");  
													cs.setString(pSeq++, format.format(date));  
													
													cs.execute();	
												}
											}//end sDeli_stat_flag <> '006'
										}//end for
									}//end 미수신건
								}//end 인터페이스 구분 정상 건
							}//Cust_use_no 있는 건
						}//배송완료처리 끝	
						
						
						if_DB_Conn();
						sMSG = "행복나래 택배 IF 결괴 테이블 Update";//System.out.println(sMSG);
						
						qry =   ""+
						        " UPDATE CJ_TRACE_HAPPYNARAE020      "+
								"    set EAI_PRGS_ST    = '03'       "+
								"       ,MODI_EMP_ID    = ?          "+
								"       ,MODI_DTIME     = SYSDATE    "+
								"       ,PROC_YN        = 'Y'        "+
								"  WHERE SERIAL         = ?          "+
								"";
						
						if_ps = if_conn.prepareStatement(qry);	

						pSeq = 1;
						if_ps.setString(pSeq++, "happynarae");
						if_ps.setString(pSeq++, sSerial);
						
						if_ps.executeUpdate();
						
						
						
						
						cj_DB_Conn();
						sMSG = "CJ 택배 IF 결괴 테이블 Update";//System.out.println(sMSG);
						
						
						qry =   ""+
						        " UPDATE V_TRACE_HAPPYNARAE020  "+
								"    set EAI_PRGS_ST    = '03'       "+
								"       ,MODI_EMP_ID    = ?          "+
								"       ,MODI_DTIME     = SYSDATE    "+
								"  WHERE SERIAL         = ?          "+
								"";
						
						cj_ps = cj_conn.prepareStatement(qry);	

						pSeq = 1;
						cj_ps.setString(pSeq++, "happynarae");
						cj_ps.setString(pSeq++, sSerial);
						
						cj_ps.executeUpdate();	
						
						isSucc = true;
						//System.out.println("isSucc-->"+isSucc);
						
					}catch(Exception e){
						e.printStackTrace();
						isSucc = false;
						errCnt++;
						System.out.println("SERIAL:"+sSerial+" CUST_USE_NO"+sCust_use_no+">>"+sMSG+":"+e.getMessage());
						log.Write(log_flag, prgm_nm, "SERIAL:"+sSerial+" CUST_USE_NO"+sCust_use_no+">>"+sMSG+":"+e.getMessage());
						lErrList.add("SERIAL:"+sSerial+" CUST_USE_NO"+sCust_use_no+">>"+sMSG+":"+e.getMessage());
					}
					
					if(isSucc){
						if(conn != null && !conn.isClosed())conn.commit();
						if(conn2 != null && !conn2.isClosed())conn2.commit();	
						if(if_conn != null && !if_conn.isClosed())if_conn.commit();
						if(cj_conn != null && !cj_conn.isClosed())cj_conn.commit();
					}else{
						if(conn != null && !conn.isClosed())conn.rollback();
						if(conn2 != null && !conn2.isClosed())conn2.rollback();	
						if(if_conn != null && !if_conn.isClosed())if_conn.rollback();
						if(cj_conn != null && !cj_conn.isClosed())cj_conn.rollback();
						
					}					
					
					
					if(rs != null){ rs.close();}
					if(smt != null){ smt.close(); }
					if(ps != null){ ps.close(); }
					if(cs != null){ cs.close(); }
					if(rs2 != null){ rs2.close();}
					if(smt2 != null){ smt2.close(); }
					if(ps2 != null){ ps2.close(); }
					if(cs2 != null){ cs2.close(); }
					if(if_rs != null){ if_rs.close();}
					if(if_smt != null){ if_smt.close(); }
					if(if_ps != null){ if_ps.close(); }
					if(if_cs != null){ if_cs.close(); }
					if(cj_rs != null){ cj_rs.close();}
					if(cj_smt != null){ cj_smt.close(); }
					if(cj_ps != null){ cj_ps.close(); }
					if(cj_cs != null){ cj_cs.close(); }
					
					cj_DB_DisConn();
					if_DB_DisConn();
					DB_DisConn();
					
		  		}// end for lProcList
			}// end for iLoopSize
			
			
			DB_Conn();
			qry =   ""+
					"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_cj_parcel_kimjs','END',SYSDATE, 'OK', NULL ) "+					
					"";
			ps = conn.prepareStatement(qry);
			ps.executeUpdate();
			conn.commit();
			DB_DisConn();
			
		}catch(Exception e){
			e.printStackTrace();
			
			DB_Conn();
			qry =   ""+
					"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_cj_parcel_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) "+					
					"";
			ps = conn.prepareStatement(qry);
			ps.executeUpdate();
			conn.commit();
			DB_DisConn();
			
			
			//메일 및 문자메시지 전송
			//CJ 택배 IF 처리중 오류가 발생했습니다.
			sMSG = "오류 알림 메일 전송";//System.out.println(sMSG);
			
			DB_Conn2();
			
			qry = "{call P_SCO.SP_SEND_SCO_MAIL(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    		
			cs2 = conn2.prepareCall(qry);	
			
			pSeq = 1;
			cs2.setString(pSeq++, "1");  
			cs2.setString(pSeq++, "2");  
			cs2.setString(pSeq++, "CJ 택배 IF 오류 알림 메일립니다.");  
			cs2.setString(pSeq++, "CJ 택배 IF 오류 보내드립니다. 내용을 확인해주시기 바랍니다.");  
			cs2.setString(pSeq++, "오류내용$$##^^"+e.getMessage());  
			cs2.setString(pSeq++, "M0050962");  
			cs2.setString(pSeq++, "M0050962");  
			cs2.setString(pSeq++, "000");  
			cs2.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_RTN      
			cs2.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_MSG      
			
			cs2.execute();	
			
			String sReturn = cs2.getString(9);
			String sMessage = cs2.getString(10);
			
			conn2.commit();
			
			//SMS 전송
			qry =   ""+
					" INSERT INTO EM_TRAN                                                                                       "+
					" (TRAN_PR,TRAN_PHONE ,TRAN_CALLBACK ,TRAN_STATUS ,TRAN_DATE ,TRAN_MSG ,TRAN_ETC1 ,TRAN_ETC2 ,TRAN_ETC3)    "+
					" SELECT EM_TRAN_PR.NEXTVAL  , A.MCOM_NUM  ,'1644-5644', 1 , SYSDATE  , 'CJ 택배 IF 오류발생.', 'REGENPRI', 'REGSCO' , ''  "+
					"   FROM CUST_DTL A                                                                                         "+
					"  WHERE 1=1                                                                                                "+
					"    AND A.CUST_ID IN ('M0050962')                                                                          "+
					"";
			
			ps2 = conn2.prepareStatement(qry);	
			ps2.executeUpdate();
			
			conn2.commit();
			
			DB_DisConn2();
		}finally{
			DB_DisConn();
			DB_DisConn2();
			if_DB_DisConn();
			if_DB_DisConn2();
			cj_DB_DisConn();
			cj_DB_DisConn2();
		}
		
		try{
			
			if(errCnt > 0){
				
				DB_Conn();
				qry =   ""+
						"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
						"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_cj_parcel_kimjs','END',SYSDATE, 'ERR', 'CJ 택배 IF 오류' ) "+					
						"";
				ps = conn.prepareStatement(qry);
				ps.executeUpdate();
				conn.commit();
				DB_DisConn();
				
				
				//메일 및 문자메시지 전송
				//CJ 택배 IF 처리중 오류가 발생했습니다.
				sMSG = "오류 알림 메일 전송";//System.out.println(sMSG);
				DB_Conn();
				
				String sErrMsg = "";
				for(int ii=0; ii<lErrList.size(); ii++){
					sErrMsg = sErrMsg + (String)lErrList.get(ii) + "$$##^^";
				}
				sErrMsg = sErrMsg.substring(0, sErrMsg.length()-6);
				
				qry = "{call P_SCO.SP_SEND_SCO_MAIL(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				pSeq = 1;
				cs.setString(pSeq++, ""+lErrList.size());  
				cs.setString(pSeq++, "1");  
				cs.setString(pSeq++, "CJ 택배 IF 결과처리 오류 알림 메일립니다.");  
				cs.setString(pSeq++, "CJ 택배 IF 결과처리 오류 보내드립니다. 내용을 확인해주시기 바랍니다.");  
				cs.setString(pSeq++, sErrMsg);  
				cs.setString(pSeq++, "M0050962");  
				cs.setString(pSeq++, "M0050962");  
				cs.setString(pSeq++, "000");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_RTN      
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_MSG      
				
				cs.execute();	
				
				String sReturn = cs.getString(9);
				String sMessage = cs.getString(10);
				
				conn.commit();
				
				//SMS 전송
				qry =   ""+
						" INSERT INTO EM_TRAN                                                                                       "+
						" (TRAN_PR,TRAN_PHONE ,TRAN_CALLBACK ,TRAN_STATUS ,TRAN_DATE ,TRAN_MSG ,TRAN_ETC1 ,TRAN_ETC2 ,TRAN_ETC3)    "+
						" SELECT EM_TRAN_PR.NEXTVAL  , A.MCOM_NUM  ,'1644-5644', 1 , SYSDATE  , 'CJ 택배 IF 결과처리시 오류발생.', 'REGENPRI', 'REGSCO' , ''  "+
						"   FROM CUST_DTL A                                                                                         "+
						"  WHERE 1=1                                                                                                "+
						"    AND A.CUST_ID IN ('M0050962')                                                                          "+
						"";
				
				ps = conn.prepareStatement(qry);	
				ps.executeUpdate();
				
				conn.commit();
				
				DB_DisConn();
			}
			
		}catch(Exception e){
			e.printStackTrace();
    		throw new Exception("세금계산서 데몬 등록 처리중 오류가발생했습니다.");
		}finally{
			DB_DisConn();
		}		
		
	}
	
	/*
	 * MAPS 완료처리
	 */
	public String proc_maps(String sDb_user, String sTra_num, String sOrds_num, String sList_num, String sTra_seq
			               ,String sComp, String sCanl, String sComp_dati, String sAcep_man) throws Exception{
		
		String sDB = "";
		
		if("SEPS".equals(sDb_user.toUpperCase())) sDB = "@L_SE";
		else if("NEPS".equals(sDb_user.toUpperCase())) sDB = "@L_NA";
		else if("KEPS".equals(sDb_user.toUpperCase())) sDB = "@L_KU";
		else if("HWMAPS".equals(sDb_user.toUpperCase())) sDB = "@L_HONG";
		
		String sOut_sto_num = sTra_num+"-"+sOrds_num+"-"+sList_num+"-"+sTra_seq;
		
		int pSeq = 1;
		String sMSG = "";
		
		
		sMSG="MAPS 거래명세서 조회";//System.out.println(sMSG);
		qry =   ""+
    			" SELECT ORDS_NUM, LIST_NUM, TRA_SEQ, TRA_NUM, ORDS_TY, TO_CHAR(TO_DATE(?, 'YYYY-MM-DD'), 'YYYYMMDD') COMP_DATI "+
				"   FROM tra_dtl"+sDB+"   "+
				"  WHERE 1=1              "+
				"    AND oust_sto_num = ? "+
    			"";
		
		ps = conn.prepareStatement(qry);
		pSeq = 1;
		ps.setString(pSeq++, sComp_dati);
		ps.setString(pSeq++, sOut_sto_num);
		
		rs = ps.executeQuery();
		
		List lRsltList = new ArrayList();
		
		if(rs != null){
			while(rs.next()){
				HashMap hm = new HashMap();
				hm.put("ORDS_NUM", rs.getString("ORDS_NUM"));
				hm.put("LIST_NUM", rs.getString("LIST_NUM"));
				hm.put("TRA_SEQ", rs.getString("TRA_SEQ"));
				hm.put("TRA_NUM", rs.getString("TRA_NUM"));
				hm.put("ORDS_TY", rs.getString("ORDS_TY"));
				hm.put("COMP_DATI", rs.getString("COMP_DATI"));
				lRsltList.add(hm);					
			}
		}else{
			throw new SQLException(sMSG+" 오류");
		}
		
		for(int ii=0; ii<lRsltList.size(); ii++){
			HashMap<String, Object> procMap = new HashMap();
			procMap = (HashMap)lRsltList.get(ii);
			
			String sMaps_ords_num        = (String)procMap.get("ORDS_NUM");
			String sMaps_list_num        = (String)procMap.get("LIST_NUM");
			String sMaps_tra_seq         = (String)procMap.get("TRA_SEQ");
			String sMaps_tra_num         = (String)procMap.get("TRA_NUM");
			String sMaps_ords_ty         = (String)procMap.get("ORDS_TY");
			String sMaps_comp_dati       = (String)procMap.get("COMP_DATI");
			
			if(!"002".equals(sMaps_ords_ty)){
				sMSG = "MAPS 완료처리";//System.out.println(sMSG);
				
				qry = "{call p_chulgo_mng.sp_input_stock_end_maps(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				pSeq = 1;
				cs.setString(pSeq++, sDb_user);  
				cs.setString(pSeq++, sMaps_ords_num);  
				cs.setString(pSeq++, sMaps_list_num);  
				cs.setString(pSeq++, sMaps_tra_num);  
				cs.setString(pSeq++, sMaps_tra_seq);  
				cs.setString(pSeq++, sComp);  
				cs.setString(pSeq++, sCanl);  
				cs.setString(pSeq++, sMaps_comp_dati);  
				cs.setString(pSeq++, sAcep_man);  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_RTN      
				cs.setString(pSeq++, "");        
				cs.setString(pSeq++, "0");  
				
				cs.execute();	
			}else if("KEPS".equals(sDb_user.toUpperCase()) && "002".equals(sMaps_ords_ty)){
				sMSG = "MAPS 반품 완료처리";//System.out.println(sMSG);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				
				qry = "{call p_chulgo_mng.sp_ban_store_in_out_2(?, ?, ?, ?, ?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				pSeq = 1;
				cs.setString(pSeq++, sDb_user);  
				cs.setString(pSeq++, sMaps_ords_num);  
				cs.setString(pSeq++, sMaps_list_num);  
				cs.setString(pSeq++, sComp);  
				cs.setString(pSeq++, sMaps_comp_dati);  
				cs.setString(pSeq++, "00000001");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_RTN      
				
				cs.execute();	
			}
			
		}
		
		
		return "";
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Date date = new Date();
		String daytime = format.format(date);
		System.out.println(daytime+"-------------Start Demon_cj_parcel Daemon-------------");
		
		Demon_cj_parcel daemon = new Demon_cj_parcel();
		
		try{
			daemon.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		Date date2 = new Date();
		String daytime2 = format.format(date);
		System.out.println(daytime2+"-------------End Demon_cj_parcel Daemon-------------");

	}

}
