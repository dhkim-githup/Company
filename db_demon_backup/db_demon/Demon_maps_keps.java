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

public class Demon_maps_keps {
	
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_maps_keps";
	
	public String driver="oracle.jdbc.driver.OracleDriver";
	
	//행복나래 DB Info
	/**
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";
	
	/**/
	public String dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
	public String user_id="METS_IMSI";
	public String user_pw="METS_IMSI";
	/**/
	
	// 강남그룹  DB Info
	/**
	public String ku_dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String ku_user_id="keps";
	public String ku_user_pw="keps_P81245846W";
	/**/
	public String ku_dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
	public String ku_user_id="keps";
	public String ku_user_pw="keps";

	
	// MAPS I/F DB Info
	/**/
	public String if_dbURL="jdbc:oracle:thin:@172.16.1.220:1521:ORA10";
	public String if_user_id="INF_MAPS";
	public String if_user_pw="MSMT9271S9";


	
	public	Connection conn, conn2, if_conn, if_conn2, ku_conn, ku_conn2;
	
	private Statement smt= null, smt2 = null, if_smt= null, if_smt2 = null, ku_smt= null, ku_smt2 = null;
	public PreparedStatement ps=null, ps2=null, if_ps=null, if_ps2=null, ku_ps=null, ku_ps2=null;
	public CallableStatement cs = null, cs2=null, if_cs = null, if_cs2=null, ku_cs = null, ku_cs2=null;
	public ResultSet rs = null, rs2 = null, if_rs = null, if_rs2 = null, ku_rs = null, ku_rs2 = null;
	public String qry, qry2, if_qry, if_qry2, ku_qry, ku_qry2 = "";
	
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
	

	public Connection ku_DB_Conn() {

		try {
			Class.forName(driver);
			ku_conn=DriverManager.getConnection(ku_dbURL,ku_user_id,ku_user_pw);

			ku_conn.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean: driver not loaded !!"+e.getMessage());
		}
		return ku_conn;
		
	}
	
	public Connection ku_DB_Conn2() {

		try {
			Class.forName(driver);
			ku_conn2=DriverManager.getConnection(ku_dbURL,ku_user_id,ku_user_pw);

			ku_conn2.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			System.out.println("ERR ConnectionBean2: driver unavailable !!"+e.getMessage());
		} catch (Exception e) {
			System.out.println("ERR ConnectionBean2: driver not loaded !!"+e.getMessage());
		}
		return ku_conn2;
		
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
	
	public void ku_DB_DisConn() {
		try {
			ku_conn.setAutoCommit(true);

			if(ku_rs != null){ ku_rs.close();}
			if(ku_smt != null){ ku_smt.close(); }
			if(ku_ps != null){ ku_ps.close(); }
			if(ku_cs != null){ ku_cs.close(); }
			if(ku_conn != null){ ku_conn.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection error !!"+e.getMessage());
		}
	}	
	
	public void ku_DB_DisConn2() {
		try {
			ku_conn2.setAutoCommit(true);

			if(ku_rs2 != null){ ku_rs2.close(); }
			if(ku_smt2 != null){ ku_smt2.close(); }
			if(ku_ps2 != null){ ku_ps2.close(); }
			if(ku_cs2 != null){ ku_cs2.close(); }
			if(ku_conn2 != null){ ku_conn2.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection2 error !!"+e.getMessage());
		}
	}
	
	public void start() throws Exception {
		
		int pSeq = 1;
		String sProc_name = "";
		String sReturn = "";
		String sMsg = "";
		
		try{
			//MAPS PO 등록
			log.Write(log_flag, prgm_nm, "INSERT MAPS PO");
			ku_DB_Conn();
			
			qry =   ""+
					"  INSERT INTO MAPS_IF_PO                                                                                                                                                                                                  "+
					"  (ENPRI_CD, BUSIP_SEQ, REG_DATI, SEQ, PO_NUM, PO_NUM_SEQ, GBN, CUST_ID, ACEP_ID, CUST_ORD_NUM, CUST_BUY_NUM, ORDS_KN, ORDS_TY, PROC_TY, METS_ENPRI_CD, CUST_BUSIP_CD                                                     "+
					"  ,ZIP, ZNM, ETC_SNO, CATE_CD, PRD_CD, PLIS_CD, SUPP_PLIS_CD, PLIS_NM, SPEC, UNIT, MAKE_CO_NM, MDL_NM, CNT, REM_QTY, UPRI, DELI_DMND_DATI, DELI_REQ_DAY                                                                   "+
					"  ,CUST_OLD_ORDS_NUM, ORD_MEMO, MEMO, ACNT_KN_CD, ACNT_KN_NM, PRE_PROC, PRE_COMP_DATI, MASTER_NUM, R_CUST_ID, R_ACEP_ID                                                                                                   "+
					"  ,REQ_CUST_ID, REQ_EMP_NUM, REQ_CUST_NM, REQ_ENPRI_CD, REQ_BUSIP_CODE, REQ_BUSIP_NM, REQ_OGN_CD, REQ_OGN_NM, REQ_TEL_NUM, REQ_MCOM_NUM, REQ_FAX_NUM, REQ_E_MAIL, REQ_ZIP_CODE, REQ_JUSO                                  "+
					"  ,PCH_CUST_ID, PCH_EMP_NUM, PCH_CUST_NM, PCH_ENPRI_CD, PCH_BUSIP_CODE, PCH_BUSIP_NM, PCH_OGN_CD, PCH_OGN_NM, PCH_TEL_NUM, PCH_MCOM_NUM, PCH_FAX_NUM, PCH_E_MAIL, PCH_ZIP_CODE, PCH_JUSO                                  "+
					"  ,PR_NUM, PR_NUM_SEQ, H_ENPRI_CD, H_BUSIP_SEQ, H_ALIAS_ID, H_CUST_ID, H_ADDR_SEQ, H_CUST_NM, H_TEL_NUM, H_MCOM_NUM, H_EMAIL, H_ORD_ITEM_ID, H_PO_ITEM_ID, REG_MAN, STATUS, CST_REG_DAY, MRO_ACP_DAY, ERR_MSG)            "+
					"  SELECT A.ENPRI_CD                                                                                                                                                                                                       "+
					"        ,A.BUSIP_SEQ                                                                                                                                                                                                      "+
					"        ,TO_CHAR(SYSDATE, 'YYYYMMDD') REG_DATI                                                                                                                                                                            "+
					"        ,(SELECT NVL(MAX(X.SEQ),0)+1 FROM MAPS_IF_PO X WHERE X.ENPRI_CD = A.ENPRI_CD AND X.BUSIP_SEQ = A.BUSIP_SEQ AND X.REG_DATI = TO_CHAR(SYSDATE, 'YYYYMMDD'))                                                         "+
					"         + ROW_NUMBER() OVER(PARTITION BY A.ENPRI_CD, A.BUSIP_SEQ ORDER BY A.ENPRI_CD, A.BUSIP_SEQ, A.ORDS_NUM, A.LIST_NUM) - 1 SEQ                                                                                       "+
					"        ,A.ORDS_NUM PO_NUM                                                                                                                                                                                                "+
					"        ,A.LIST_NUM PO_NUM_SEQ                                                                                                                                                                                            "+
					"        ,CASE WHEN (SELECT COUNT(*) FROM ORDS_DTL X WHERE X.MASTER_NUM = A.MASTER_NUM AND X.ORDS_TY = A.ORDS_TY AND X.PROC_TY IN ('006','107')) > 0 THEN 'CNL' ELSE 'ORD' END GBN                                         "+
					"        ,A.CUST_ID                                                                                                                                                                                                        "+
					"        ,A.ACEP_ID                                                                                                                                                                                                        "+
					"        ,(SELECT Y.METS_ENPRI_CD||'-'||X.ORDS_NUM||'-'||X.LIST_NUM FROM ORDS_DTL X, ENPRI_INFO Y                                                                                                                          "+
					"           WHERE X.ENPRI_CD = Y.ENPRI_CD AND X.MASTER_NUM = A.MASTER_NUM AND X.ORDS_KN = '002' AND X.ORDS_TY = A.ORDS_TY AND ROWNUM = 1) CUST_ORD_NUM                                                                     "+
					"        ,(SELECT Y.METS_ENPRI_CD||'-'||X.ORDS_NUM||'-'||X.LIST_NUM FROM ORDS_DTL X, ENPRI_INFO Y                                                                                                                          "+
					"           WHERE X.ENPRI_CD = Y.ENPRI_CD AND X.MASTER_NUM = A.MASTER_NUM AND X.ORDS_KN = '009'                                                                                                                            "+
					"             AND DECODE(X.ORDS_TY,'002','001',X.ORDS_TY) = DECODE(A.ORDS_TY,'002','001',A.ORDS_TY) AND ROWNUM = 1) CUST_BUY_NUM                                                                                           "+
					"        ,A.ORDS_KN                                                                                                                                                                                                        "+
					"        ,A.ORDS_TY                                                                                                                                                                                                        "+
					"        ,A.PROC_TY                                                                                                                                                                                                        "+
					"        ,(SELECT X.METS_ENPRI_CD FROM ENPRI_INFO X WHERE X.ENPRI_CD = A.ENPRI_CD) METS_ENPRI_CD                                                                                                                           "+
					"        ,(SELECT X.CUST_BUSIP_CD FROM ENPRI_DTL X WHERE X.ENPRI_CD = A.ENPRI_CD AND X.BUSIP_SEQ = A.BUSIP_SEQ) CUST_BUSIP_CD                                                                                              "+
					"        ,(SELECT X.ZIP FROM ZIP_CODE X, ENPRI_ADDR Y WHERE X.ZIP_SEQ = Y.ZIP_SEQ AND Y.ENPRI_CD = A.ENPRI_CD AND Y.ADDR_SEQ = A.ADDR_SEQ) ZIP                                                                             "+
					"        ,(SELECT X.ZNM1||' '||X.ZNM2||' '||X.ZNM3 FROM ZIP_CODE X, ENPRI_ADDR Y WHERE X.ZIP_SEQ = Y.ZIP_SEQ AND Y.ENPRI_CD = A.ENPRI_CD AND Y.ADDR_SEQ = A.ADDR_SEQ) ZNM                                                  "+
					"        ,(SELECT X.ETC_SNO FROM ENPRI_ADDR X WHERE X.ENPRI_CD = A.ENPRI_CD AND X.ADDR_SEQ = A.ADDR_SEQ) ETC_SNO                                                                                                           "+
					"        ,A.CATE_CD                                                                                                                                                                                                        "+
					"        ,A.PRD_CD                                                                                                                                                                                                         "+
					"        ,A.PLIS_CD                                                                                                                                                                                                        "+
					"        ,A.SUPP_PLIS_CD                                                                                                                                                                                                   "+
					"        ,A.PLIS_NM                                                                                                                                                                                                        "+
					"        ,A.SPEC                                                                                                                                                                                                           "+
					"        ,A.UNIT                                                                                                                                                                                                           "+
					"        ,A.MAKE_CO_NM                                                                                                                                                                                                     "+
					"        ,A.MDL_NM                                                                                                                                                                                                         "+
					"        ,A.CNT                                                                                                                                                                                                            "+
					"        ,A.REM_QTY                                                                                                                                                                                                        "+
					"        ,A.UPRI                                                                                                                                                                                                           "+
					"        ,A.DELI_DMND_DATI                                                                                                                                                                                                 "+
					"        ,A.DELI_REQ_DAY                                                                                                                                                                                                   "+
					"        ,NULL CUST_OLD_ORDS_NUM                                                                                                                                                                                           "+
					"        ,A.ORD_MEMO                                                                                                                                                                                                       "+
					"        ,A.MEMO                                                                                                                                                                                                           "+
					"        ,A.ACNT_KN_CD                                                                                                                                                                                                     "+
					"        ,(SELECT X.ACNT_KN_NM FROM ACNT_KN X WHERE X.ENPRI_CD = A.ENPRI_CD AND X.ACNT_KN_CD = A.ACNT_KN_CD AND ROWNUM = 1) ACNT_KN_NM                                                                                     "+
					"        ,A.PRE_PROC                                                                                                                                                                                                       "+
					"        ,A.PRE_COMP_DATI                                                                                                                                                                                                  "+
					"        ,A.MASTER_NUM                                                                                                                                                                                                     "+
					"        ,A.REQ_CUST_ID R_CUST_ID                                                                                                                                                                                          "+
					"        ,A.REQ_ACEP_ID R_ACEP_ID                                                                                                                                                                                          "+
					"        ,B.CUST_ID REQ_CUST_ID, B.EMP_NUM REQ_EMP_NUM, B.CUST_NM REQ_CUST_NM                                                                                                                                              "+
					"        ,(SELECT X.METS_ENPRI_CD FROM ENPRI_INFO X WHERE X.ENPRI_CD = B.ENPRI_CD) REQ_ENPRI_CD                                                                                                                            "+
					"        ,(SELECT X.CUST_BUSIP_CD FROM ENPRI_DTL X WHERE X.ENPRI_CD = B.ENPRI_CD AND X.BUSIP_SEQ = B.BUSIP_SEQ) REQ_BUSIP_CODE                                                                                             "+
					"        ,(SELECT X.THIS_BSHOP_NM FROM ENPRI_DTL X WHERE X.ENPRI_CD = B.ENPRI_CD AND X.BUSIP_SEQ = B.BUSIP_SEQ) REQ_BUSIP_NM                                                                                               "+
					"        ,B.OGN_CD REQ_OGN_CD                                                                                                                                                                                              "+
					"        ,(SELECT X.OGN_HAN_NM FROM OGN X WHERE X.ENPRI_CD = B.ENPRI_CD AND X.BUSIP_SEQ = B.BUSIP_SEQ AND X.OGN_CD = B.OGN_CD)  REQ_OGN_NM                                                                                 "+
					"        ,B.TEL_NUM REQ_TEL_NUM, B.MCOM_NUM REQ_MCOM_NUM, B.FAX_NUM REQ_FAX_NUM, B.EMAIL REQ_E_MAIL                                                                                                                        "+
					"        ,(SELECT X.ZIP FROM ZIP_CODE X, ENPRI_ADDR Y WHERE X.ZIP_SEQ = Y.ZIP_SEQ AND Y.ENPRI_CD = B.ENPRI_CD AND Y.ADDR_SEQ = B.ADDR_SEQ) REQ_ZIP_CODE                                                                    "+
					"        ,(SELECT X.ZNM1||' '||X.ZNM2||' '||X.ZNM3||' '||Y.ETC_SNO FROM ZIP_CODE X, ENPRI_ADDR Y WHERE X.ZIP_SEQ = Y.ZIP_SEQ AND Y.ENPRI_CD = B.ENPRI_CD AND Y.ADDR_SEQ = B.ADDR_SEQ) REQ_JUSO                             "+
					"        ,C.CUST_ID PCH_CUST_ID, C.EMP_NUM PCH_EMP_NUM, C.CUST_NM PCH_CUST_NM                                                                                                                                              "+
					"        ,(SELECT X.METS_ENPRI_CD FROM ENPRI_INFO X WHERE X.ENPRI_CD = C.ENPRI_CD) PCH_ENPRI_CD                                                                                                                            "+
					"        ,(SELECT X.CUST_BUSIP_CD FROM ENPRI_DTL X WHERE X.ENPRI_CD = C.ENPRI_CD AND X.BUSIP_SEQ = C.BUSIP_SEQ) PCH_BUSIP_CODE                                                                                             "+
					"        ,(SELECT X.THIS_BSHOP_NM FROM ENPRI_DTL X WHERE X.ENPRI_CD = C.ENPRI_CD AND X.BUSIP_SEQ = C.BUSIP_SEQ) PCH_BUSIP_NM                                                                                               "+
					"        ,C.OGN_CD PCH_OGN_CD                                                                                                                                                                                              "+
					"        ,(SELECT X.OGN_HAN_NM FROM OGN X WHERE X.ENPRI_CD = C.ENPRI_CD AND X.BUSIP_SEQ = C.BUSIP_SEQ AND X.OGN_CD = C.OGN_CD)  PCH_OGN_NM                                                                                 "+
					"        ,C.TEL_NUM PCH_TEL_NUM, C.MCOM_NUM PCH_MCOM_NUM, C.FAX_NUM PCH_FAX_NUM, C.EMAIL PCH_E_MAIL                                                                                                                        "+
					"        ,(SELECT X.ZIP FROM ZIP_CODE X, ENPRI_ADDR Y WHERE X.ZIP_SEQ = Y.ZIP_SEQ AND Y.ENPRI_CD = C.ENPRI_CD AND Y.ADDR_SEQ = C.ADDR_SEQ) PCH_ZIP_CODE                                                                    "+
					"        ,(SELECT X.ZNM1||' '||X.ZNM2||' '||X.ZNM3||' '||Y.ETC_SNO FROM ZIP_CODE X, ENPRI_ADDR Y WHERE X.ZIP_SEQ = Y.ZIP_SEQ AND Y.ENPRI_CD = C.ENPRI_CD AND Y.ADDR_SEQ = C.ADDR_SEQ) PCH_JUSO                             "+
					"        ,(SELECT X.ORDS_NUM FROM ORDS_DTL X WHERE X.MASTER_NUM = A.MASTER_NUM AND DECODE(X.ORDS_TY,'002','001',X.ORDS_TY) = DECODE(A.ORDS_TY,'002','001',A.ORDS_TY) AND X.ORDS_KN = '009') PR_NUM                         "+
					"        ,(SELECT X.LIST_NUM FROM ORDS_DTL X WHERE X.MASTER_NUM = A.MASTER_NUM AND DECODE(X.ORDS_TY,'002','001',X.ORDS_TY) = DECODE(A.ORDS_TY,'002','001',A.ORDS_TY) AND X.ORDS_KN = '009') PR_NUM_SEQ                     "+
					"        ,NULL H_ENPRI_CD,NULL H_BUSIP_SEQ, NULL H_ALIAS_ID,NULL H_CUST_ID, NULL H_ADDR_SEQ, NULL H_CUST_NM, NULL H_TEL_NUM, NULL H_MCOM_NUM, NULL H_EMAIL, NULL H_ORD_ITEM_ID, NULL H_PO_ITEM_ID                          "+
					"        ,A.REG_MAN REG_MAN,'INS' STATUS,SYSDATE CST_REG_DAY,NULL MRO_ACP_DAY,NULL ERR_MSG                                                                                                                                 "+
					"    FROM ORDS_DTL A                                                                                                                                                                                                       "+
					"        ,CUST_DTL B                                                                                                                                                                                                       "+
					"        ,CUST_DTL C                                                                                                                                                                                                       "+
					"   WHERE 1=1                                                                                                                                                                                                              "+
					"     AND A.MUL_MAN = B.CUST_ID                                                                                                                                                                                            "+
					"     AND A.CUST_ID = C.CUST_ID                                                                                                                                                                                            "+
					"     AND A.ORDS_KN = '002'                                                                                                                                                                                                "+
					"     AND A.PROC_TY IN ('001','006','107')                                                                                                                                                                                             "+
					"     AND A.SCO_CD = '000001'                                                                                                                                                                                              "+
					"     AND A.ORDS_MK_DATI >= SYSDATE - 10                                                                                                                                                                                   "+
					"     AND NOT EXISTS (SELECT 'X' FROM MAPS_IF_PO X WHERE X.PO_NUM = A.ORDS_NUM AND X.PO_NUM_SEQ = A.LIST_NUM                                                                                                               "+
					"                        AND X.GBN = (CASE WHEN (SELECT COUNT(*) FROM ORDS_DTL Y                                                                                                                                           "+
					"                                                 WHERE Y.MASTER_NUM = A.MASTER_NUM AND Y.ORDS_TY = A.ORDS_TY AND Y.PROC_TY IN ('006','107')) > 0 THEN 'CNL' ELSE 'ORD' END))                                              "+			
					"";
			ku_ps = ku_conn.prepareStatement(qry);
			ku_ps.executeUpdate();
			ku_conn.commit();
			
			ku_DB_DisConn();
			
			
			
			//KEPS 프로시저 순차적으로 실행
			DB_Conn();
			//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_M_HAPPY_MAN 행복나래 사업장 담당자 처리
			try{
				sProc_name = "P_INF_KEPS.P_M_HAPPY_MAN";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_M_CHK_H_CUST 행복나래 발주 담당자 MAPS_IF_PO 테이블 동기화
			try{
				sProc_name = "P_INF_KEPS.P_M_CHK_H_CUST";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_M_SYNC_CUST MAPS의 주문관련 고객 정보 행복나래 시스템 사용자 등록 및 MAPP_IF_CUST 테이블 동기화
			try{
				sProc_name = "P_INF_KEPS.P_M_SYNC_CUST";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_M_REQ_PRI MAPS가격요청
			try{
				sProc_name = "P_INF_KEPS.P_M_REQ_PRI";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_M_INS_PO MAPS PO처리
	    	try{
				sProc_name = "P_INF_KEPS.P_M_INS_PO";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_M_INS_STO MAPS 거래명세서 처리
	    	try{	    		
				sProc_name = "P_INF_KEPS.P_M_INS_STO";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
	    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_H_SEND_PRI 행복나래에서 MAPS로 가격 송부
	    	try{
				sProc_name = "P_INF_KEPS.P_H_SEND_PRI";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
	    		
    		
    		//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_KEPS.P_H_ORD_PROC 행복나래에서 주문 및 거래명세서 생성 및 상태 변경
	    	try{
				sProc_name = "P_INF_KEPS.P_H_ORD_PROC";
				qry = "{call "+sProc_name+"(?, ?, ?)}";
	    		
				cs = conn.prepareCall(qry);	
				
				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
				pSeq = 1;
				cs.setString(pSeq++, "1");  
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
				
				cs.execute();	
				
				sReturn = "";
				sMsg = "";
				
				sReturn = cs.getString(2);
				sMsg    = cs.getString(3);
				
				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
				
				if(!"0".equals(sReturn)){
					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
				}
				
				log.Write(log_flag, prgm_nm, sProc_name+" commit");
				
	    		conn.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
    		
	    	//표준 패키지 호출.. 테스트 용으로 운영시 제거해야함..	
			//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_STD_IF_N16.P_INS_PR 표준 PR
//	    	try{
//				sProc_name = "P_INF_STD_IF_N16.P_INS_PR";
//				qry = "{call "+sProc_name+"(?, ?, ?)}";
//	    		
//				cs = conn.prepareCall(qry);	
//				
//				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
//				pSeq = 1;
//				cs.setString(pSeq++, "1");  
//				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
//				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
//				
//				cs.execute();	
//				
//				sReturn = "";
//				sMsg = "";
//				
//				sReturn = cs.getString(2);
//				sMsg    = cs.getString(3);
//				
//				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
//				
//				if(!"0".equals(sReturn)){
//					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
//					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
//					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
//				}
//				
//				log.Write(log_flag, prgm_nm, sProc_name+" commit");
//				
//	    		conn.commit();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
    		
	    		
			//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_STD_IF_N16.P_INS_PO 표준 PO
//	    	try{
//				sProc_name = "P_INF_STD_IF_N16.P_INS_PO";
//				qry = "{call "+sProc_name+"(?, ?, ?)}";
//	    		
//				cs = conn.prepareCall(qry);	
//				
//				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
//				pSeq = 1;
//				cs.setString(pSeq++, "1");  
//				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
//				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
//				
//				cs.execute();	
//				
//				sReturn = "";
//				sMsg = "";
//				
//				sReturn = cs.getString(2);
//				sMsg    = cs.getString(3);
//				
//				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
//				
//				if(!"0".equals(sReturn)){
//					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
//					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
//					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
//				}
//				
//				log.Write(log_flag, prgm_nm, sProc_name+" commit");
//				
//	    		conn.commit();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
    		
	    		
			//▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣▣
			//P_INF_STD_IF_N16.P_INS_PO_RETURN 표준 PO 반품
//	    	try{
//				sProc_name = "P_INF_STD_IF_N16.P_INS_PO_RETURN";
//				qry = "{call "+sProc_name+"(?, ?, ?)}";
//	    		
//				cs = conn.prepareCall(qry);	
//				
//				log.Write(log_flag, prgm_nm, sProc_name+" Setting SQL parameters");
//				pSeq = 1;
//				cs.setString(pSeq++, "1");  
//				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_RETURN       
//				cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//P_MSG 
//				
//				cs.execute();	
//				
//				sReturn = "";
//				sMsg = "";
//				
//				sReturn = cs.getString(2);
//				sMsg    = cs.getString(3);
//				
//				log.Write(prgm_nm, sProc_name+" 결과메시지 :"+sReturn+"<>"+sMsg);
//				
//				if(!"0".equals(sReturn)){
//					log.Write(prgm_nm, sProc_name+" 처리중 오류발생.\\n"+sMsg);
//					//throw new Exception(sProc_name+" 처리중 오류발생.\\n"+sMsg);
//					this.sendMsg(conn, sProc_name+" 처리중 오류발생."+sMsg);
//				}
//				
//				log.Write(log_flag, prgm_nm, sProc_name+" commit");
//				
//	    		conn.commit();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
    		
    		
			DB_DisConn();
			
		}catch(Exception e){
			conn.rollback();			
			e.printStackTrace();
		}finally{
			if_DB_DisConn();
			if_DB_DisConn2();
			ku_DB_DisConn();
			ku_DB_DisConn2();
			DB_DisConn();
			DB_DisConn2();
		}
		
	}
	
	public void sendMsg(Connection conn, String sMsg) throws Exception {
		int pSeq = 1;
		
		try{
			String qry = ""+
			        " INSERT INTO em_tran@MRODB "+
			        "( TRAN_PR, TRAN_REFKEY, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,      TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3 ) "+
			        " VALUES  "+
			        " ( EM_TRAN_PR.NEXTVAL@MRODB, NULL , '01041723189','0221044955', 1, Sysdate , ?,  NULL, NULL,NULL) " + 
			        "";
			/*		
			ps = conn.prepareStatement(qry);
			
			pSeq = 1;
			ps.setString(pSeq++, sMsg);
			
			ps.executeUpdate();
			
			conn.commit();
    		*/
		}catch(Exception e){
			conn.rollback();			
			e.printStackTrace();
		}finally{

		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Date date = new Date();
		String daytime = format.format(date);
		System.out.println(daytime+"-------------Start Demon_maps_keps Daemon-------------");
		
		Demon_maps_keps daemon = new Demon_maps_keps();
		
		try{
			daemon.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		Date date2 = new Date();
		String daytime2 = format.format(date);
		System.out.println(daytime2+"-------------End Demon_maps_keps Daemon-------------");
	}

}
