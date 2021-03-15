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

public class Demon_cj_parcel_rcpt {
	
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
		String sMSG = "";
		
		try{
			cj_DB_Conn();
			
			
			/*
			 * 동기화된 행복나래 택배 결과 테이블을 사용하여 결과 처리 한다.
			 */
			sMSG = "행복나래 택배 처리결과 테이블에서 처리대상 조회";
			
			
	    	qry =   ""+
					" SELECT  "+
					"   CUST_ID "+
					" , RCPT_YMD "+
					" , CUST_USE_NO "+
					" , RCPT_DV "+
					" , WORK_DV_CD "+
					" , REQ_DV_CD "+
					" , MPCK_KEY "+
					" , MPCK_SEQ "+
					" , CAL_DV_CD "+
					" , FRT_DV_CD "+
					" , CNTR_ITEM_CD "+
					" , BOX_TYPE_CD "+
					" , BOX_QTY "+
					" , FRT "+
					" , CUST_MGMT_DLCM_CD "+
					" , SENDR_NM "+
					" , SENDR_TEL_NO1 "+
					" , SENDR_TEL_NO2 "+
					" , SENDR_TEL_NO3 "+
					" , SENDR_CELL_NO1 "+
					" , SENDR_CELL_NO2 "+
					" , SENDR_CELL_NO3 "+
					" , SENDR_SAFE_NO1 "+
					" , SENDR_SAFE_NO2 "+
					" , SENDR_SAFE_NO3 "+
					" , SENDR_ZIP_NO "+
					" , SENDR_ADDR "+
					" , SENDR_DETAIL_ADDR "+
					" , RCVR_NM "+
					" , RCVR_TEL_NO1 "+
					" , RCVR_TEL_NO2 "+
					" , RCVR_TEL_NO3 "+
					" , RCVR_CELL_NO1 "+
					" , RCVR_CELL_NO2 "+
					" , RCVR_CELL_NO3 "+
					" , RCVR_SAFE_NO1 "+
					" , RCVR_SAFE_NO2 "+
					" , RCVR_SAFE_NO3 "+
					" , RCVR_ZIP_NO "+
					" , RCVR_ADDR "+
					" , RCVR_DETAIL_ADDR "+
					" , ORDRR_NM "+
					" , ORDRR_TEL_NO1 "+
					" , ORDRR_TEL_NO2 "+
					" , ORDRR_TEL_NO3 "+
					" , ORDRR_CELL_NO1 "+
					" , ORDRR_CELL_NO2 "+
					" , ORDRR_CELL_NO3 "+
					" , ORDRR_SAFE_NO1 "+
					" , ORDRR_SAFE_NO2 "+
					" , ORDRR_SAFE_NO3 "+
					" , ORDRR_ZIP_NO "+
					" , ORDRR_ADDR "+
					" , ORDRR_DETAIL_ADDR "+
					" , INVC_NO "+
					" , ORI_INVC_NO "+
					" , ORI_ORD_NO "+
					" , COLCT_EXPCT_YMD "+
					" , COLCT_EXPCT_HOUR "+
					" , SHIP_EXPCT_YMD "+
					" , SHIP_EXPCT_HOUR "+
					" , PRT_ST "+
					" , ARTICLE_AMT "+
					" , REMARK_1 "+
					" , REMARK_2 "+
					" , REMARK_3 "+
					" , COD_YN "+
					" , GDS_CD "+
					" , GDS_NM "+
					" , GDS_QTY "+
					" , UNIT_CD "+
					" , UNIT_NM "+
					" , GDS_AMT "+
					" , ETC_1 "+
					" , ETC_2 "+
					" , ETC_3 "+
					" , ETC_4 "+
					" , ETC_5 "+
					" , DLV_DV "+
					" , RCPT_ERR_YN "+
					" , RCPT_ERR_MSG "+
					" , EAI_PRGS_ST "+
					" , EAI_ERR_MSG "+
					" , REG_EMP_ID "+
					" , to_char(REG_DTIME ,'yyyymmddhh24miss') REG_DTIME "+
					" , MODI_EMP_ID "+
					" , to_char(MODI_DTIME ,'yyyymmddhh24miss') MODI_DTIME "+
					" FROM V_RCPT_HAPPYNARAE010 "+
					"  WHERE 1=1                                                                                   "+
					"    AND REG_DTIME >= SYSDATE - 2                                                             "+
	    			"";
			
	    	cj_ps = cj_conn.prepareStatement(qry);
			pSeq = 1;
			
			cj_rs = cj_ps.executeQuery();
			
			List cjList = new ArrayList();
			
			if(cj_rs != null){
				while(cj_rs.next()){
					HashMap hm = new HashMap();
					hm.put("CUST_ID"           , cj_rs.getString("CUST_ID"           ));
					hm.put("RCPT_YMD"          , cj_rs.getString("RCPT_YMD"          ));
					hm.put("CUST_USE_NO"       , cj_rs.getString("CUST_USE_NO"       ));
					hm.put("RCPT_DV"           , cj_rs.getString("RCPT_DV"           ));
					hm.put("WORK_DV_CD"        , cj_rs.getString("WORK_DV_CD"        ));
					hm.put("REQ_DV_CD"         , cj_rs.getString("REQ_DV_CD"         ));
					hm.put("MPCK_KEY"          , cj_rs.getString("MPCK_KEY"          ));
					hm.put("MPCK_SEQ"          , cj_rs.getString("MPCK_SEQ"          ));
					hm.put("CAL_DV_CD"         , cj_rs.getString("CAL_DV_CD"         ));
					hm.put("FRT_DV_CD"         , cj_rs.getString("FRT_DV_CD"         ));
					hm.put("CNTR_ITEM_CD"      , cj_rs.getString("CNTR_ITEM_CD"      ));
					hm.put("BOX_TYPE_CD"       , cj_rs.getString("BOX_TYPE_CD"       ));
					hm.put("BOX_QTY"           , cj_rs.getString("BOX_QTY"           ));
					hm.put("FRT"               , cj_rs.getString("FRT"               ));
					hm.put("CUST_MGMT_DLCM_CD" , cj_rs.getString("CUST_MGMT_DLCM_CD" ));
					hm.put("SENDR_NM"          , cj_rs.getString("SENDR_NM"          ));
					hm.put("SENDR_TEL_NO1"     , cj_rs.getString("SENDR_TEL_NO1"     ));
					hm.put("SENDR_TEL_NO2"     , cj_rs.getString("SENDR_TEL_NO2"     ));
					hm.put("SENDR_TEL_NO3"     , cj_rs.getString("SENDR_TEL_NO3"     ));
					hm.put("SENDR_CELL_NO1"    , cj_rs.getString("SENDR_CELL_NO1"    ));
					hm.put("SENDR_CELL_NO2"    , cj_rs.getString("SENDR_CELL_NO2"    ));
					hm.put("SENDR_CELL_NO3"    , cj_rs.getString("SENDR_CELL_NO3"    ));
					hm.put("SENDR_SAFE_NO1"    , cj_rs.getString("SENDR_SAFE_NO1"    ));
					hm.put("SENDR_SAFE_NO2"    , cj_rs.getString("SENDR_SAFE_NO2"    ));
					hm.put("SENDR_SAFE_NO3"    , cj_rs.getString("SENDR_SAFE_NO3"    ));
					hm.put("SENDR_ZIP_NO"      , cj_rs.getString("SENDR_ZIP_NO"      ));
					hm.put("SENDR_ADDR"        , cj_rs.getString("SENDR_ADDR"        ));
					hm.put("SENDR_DETAIL_ADDR" , cj_rs.getString("SENDR_DETAIL_ADDR" ));
					hm.put("RCVR_NM"           , cj_rs.getString("RCVR_NM"           ));
					hm.put("RCVR_TEL_NO1"      , cj_rs.getString("RCVR_TEL_NO1"      ));
					hm.put("RCVR_TEL_NO2"      , cj_rs.getString("RCVR_TEL_NO2"      ));
					hm.put("RCVR_TEL_NO3"      , cj_rs.getString("RCVR_TEL_NO3"      ));
					hm.put("RCVR_CELL_NO1"     , cj_rs.getString("RCVR_CELL_NO1"     ));
					hm.put("RCVR_CELL_NO2"     , cj_rs.getString("RCVR_CELL_NO2"     ));
					hm.put("RCVR_CELL_NO3"     , cj_rs.getString("RCVR_CELL_NO3"     ));
					hm.put("RCVR_SAFE_NO1"     , cj_rs.getString("RCVR_SAFE_NO1"     ));
					hm.put("RCVR_SAFE_NO2"     , cj_rs.getString("RCVR_SAFE_NO2"     ));
					hm.put("RCVR_SAFE_NO3"     , cj_rs.getString("RCVR_SAFE_NO3"     ));
					hm.put("RCVR_ZIP_NO"       , cj_rs.getString("RCVR_ZIP_NO"       ));
					hm.put("RCVR_ADDR"         , cj_rs.getString("RCVR_ADDR"         ));
					hm.put("RCVR_DETAIL_ADDR"  , cj_rs.getString("RCVR_DETAIL_ADDR"  ));
					hm.put("ORDRR_NM"          , cj_rs.getString("ORDRR_NM"          ));
					hm.put("ORDRR_TEL_NO1"     , cj_rs.getString("ORDRR_TEL_NO1"     ));
					hm.put("ORDRR_TEL_NO2"     , cj_rs.getString("ORDRR_TEL_NO2"     ));
					hm.put("ORDRR_TEL_NO3"     , cj_rs.getString("ORDRR_TEL_NO3"     ));
					hm.put("ORDRR_CELL_NO1"    , cj_rs.getString("ORDRR_CELL_NO1"    ));
					hm.put("ORDRR_CELL_NO2"    , cj_rs.getString("ORDRR_CELL_NO2"    ));
					hm.put("ORDRR_CELL_NO3"    , cj_rs.getString("ORDRR_CELL_NO3"    ));
					hm.put("ORDRR_SAFE_NO1"    , cj_rs.getString("ORDRR_SAFE_NO1"    ));
					hm.put("ORDRR_SAFE_NO2"    , cj_rs.getString("ORDRR_SAFE_NO2"    ));
					hm.put("ORDRR_SAFE_NO3"    , cj_rs.getString("ORDRR_SAFE_NO3"    ));
					hm.put("ORDRR_ZIP_NO"      , cj_rs.getString("ORDRR_ZIP_NO"      ));
					hm.put("ORDRR_ADDR"        , cj_rs.getString("ORDRR_ADDR"        ));
					hm.put("ORDRR_DETAIL_ADDR" , cj_rs.getString("ORDRR_DETAIL_ADDR" ));
					hm.put("INVC_NO"           , cj_rs.getString("INVC_NO"           ));
					hm.put("ORI_INVC_NO"       , cj_rs.getString("ORI_INVC_NO"       ));
					hm.put("ORI_ORD_NO"        , cj_rs.getString("ORI_ORD_NO"        ));
					hm.put("COLCT_EXPCT_YMD"   , cj_rs.getString("COLCT_EXPCT_YMD"   ));
					hm.put("COLCT_EXPCT_HOUR"  , cj_rs.getString("COLCT_EXPCT_HOUR"  ));
					hm.put("SHIP_EXPCT_YMD"    , cj_rs.getString("SHIP_EXPCT_YMD"    ));
					hm.put("SHIP_EXPCT_HOUR"   , cj_rs.getString("SHIP_EXPCT_HOUR"   ));
					hm.put("PRT_ST"            , cj_rs.getString("PRT_ST"            ));
					hm.put("ARTICLE_AMT"       , cj_rs.getString("ARTICLE_AMT"       ));
					hm.put("REMARK_1"          , cj_rs.getString("REMARK_1"          ));
					hm.put("REMARK_2"          , cj_rs.getString("REMARK_2"          ));
					hm.put("REMARK_3"          , cj_rs.getString("REMARK_3"          ));
					hm.put("COD_YN"            , cj_rs.getString("COD_YN"            ));
					hm.put("GDS_CD"            , cj_rs.getString("GDS_CD"            ));
					hm.put("GDS_NM"            , cj_rs.getString("GDS_NM"            ));
					hm.put("GDS_QTY"           , cj_rs.getString("GDS_QTY"           ));
					hm.put("UNIT_CD"           , cj_rs.getString("UNIT_CD"           ));
					hm.put("UNIT_NM"           , cj_rs.getString("UNIT_NM"           ));
					hm.put("GDS_AMT"           , cj_rs.getString("GDS_AMT"           ));
					hm.put("ETC_1"             , cj_rs.getString("ETC_1"             ));
					hm.put("ETC_2"             , cj_rs.getString("ETC_2"             ));
					hm.put("ETC_3"             , cj_rs.getString("ETC_3"             ));
					hm.put("ETC_4"             , cj_rs.getString("ETC_4"             ));
					hm.put("ETC_5"             , cj_rs.getString("ETC_5"             ));
					hm.put("DLV_DV"            , cj_rs.getString("DLV_DV"            ));
					hm.put("RCPT_ERR_YN"       , cj_rs.getString("RCPT_ERR_YN"       ));
					hm.put("RCPT_ERR_MSG"      , cj_rs.getString("RCPT_ERR_MSG"      ));
					hm.put("EAI_PRGS_ST"       , cj_rs.getString("EAI_PRGS_ST"       ));
					hm.put("EAI_ERR_MSG"       , cj_rs.getString("EAI_ERR_MSG"       ));
					hm.put("REG_EMP_ID"        , cj_rs.getString("REG_EMP_ID"        ));
					hm.put("REG_DTIME"         , cj_rs.getString("REG_DTIME"         ));
					hm.put("MODI_EMP_ID"       , cj_rs.getString("MODI_EMP_ID"       ));
					hm.put("MODI_DTIME"        , cj_rs.getString("MODI_DTIME"        ));
					cjList.add(hm);					
				}
			}else{
				throw new SQLException(sMSG+" 오류");
			}

			cj_DB_DisConn();
			
			
			
			if_DB_Conn();
			
			
			sMSG = "CJ 택배 결과값 행복나래 삭제";
			qry =   ""+
					" DELETE FROM CJ_RCPT_HAPPYNARAE010_TEMP "+
					"";
			
			if_ps = if_conn.prepareStatement(qry);
			
			pSeq = 1;
			if_ps.executeUpdate();	
			
			if_conn.commit();
			
			
			
			qry =   ""+
					" INSERT INTO CJ_RCPT_HAPPYNARAE010_TEMP "+
					" (CUST_ID, RCPT_YMD, CUST_USE_NO, RCPT_DV, WORK_DV_CD, REQ_DV_CD, MPCK_KEY, MPCK_SEQ, CAL_DV_CD, FRT_DV_CD, CNTR_ITEM_CD, BOX_TYPE_CD, BOX_QTY, FRT, CUST_MGMT_DLCM_CD, SENDR_NM, SENDR_TEL_NO1, SENDR_TEL_NO2, SENDR_TEL_NO3, SENDR_CELL_NO1, SENDR_CELL_NO2, SENDR_CELL_NO3, SENDR_SAFE_NO1, SENDR_SAFE_NO2, SENDR_SAFE_NO3, SENDR_ZIP_NO, SENDR_ADDR, SENDR_DETAIL_ADDR, RCVR_NM, RCVR_TEL_NO1, RCVR_TEL_NO2, RCVR_TEL_NO3, RCVR_CELL_NO1, RCVR_CELL_NO2, RCVR_CELL_NO3, RCVR_SAFE_NO1, RCVR_SAFE_NO2, RCVR_SAFE_NO3, RCVR_ZIP_NO, RCVR_ADDR, RCVR_DETAIL_ADDR, ORDRR_NM, ORDRR_TEL_NO1, ORDRR_TEL_NO2, ORDRR_TEL_NO3, ORDRR_CELL_NO1, ORDRR_CELL_NO2, ORDRR_CELL_NO3, ORDRR_SAFE_NO1, ORDRR_SAFE_NO2, ORDRR_SAFE_NO3, ORDRR_ZIP_NO, ORDRR_ADDR, ORDRR_DETAIL_ADDR, INVC_NO, ORI_INVC_NO, ORI_ORD_NO, COLCT_EXPCT_YMD, COLCT_EXPCT_HOUR, SHIP_EXPCT_YMD, SHIP_EXPCT_HOUR, PRT_ST, ARTICLE_AMT, REMARK_1, REMARK_2, REMARK_3, COD_YN, GDS_CD, GDS_NM, GDS_QTY, UNIT_CD, UNIT_NM, GDS_AMT, ETC_1, ETC_2, ETC_3, ETC_4, ETC_5, DLV_DV, RCPT_ERR_YN, RCPT_ERR_MSG, EAI_PRGS_ST, EAI_ERR_MSG, REG_EMP_ID, REG_DTIME, MODI_EMP_ID, MODI_DTIME ) "+
					" values "+
					" (?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,to_date(?,'yyyymmddhh24miss')  ,?  ,to_date(?,'yyyymmddhh24miss') ) "+					
					"";
			
			if_ps = if_conn.prepareStatement(qry);	
			
			for(int ii=0; ii<cjList.size(); ii++){
				pSeq = 1;
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CUST_ID"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CUST_ID"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCPT_YMD"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCPT_YMD"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CUST_USE_NO"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CUST_USE_NO"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCPT_DV"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCPT_DV"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("WORK_DV_CD"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("WORK_DV_CD"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REQ_DV_CD"         ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REQ_DV_CD"         ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("MPCK_KEY"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("MPCK_KEY"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("MPCK_SEQ"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("MPCK_SEQ"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CAL_DV_CD"         ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CAL_DV_CD"         ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("FRT_DV_CD"         ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("FRT_DV_CD"         ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CNTR_ITEM_CD"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CNTR_ITEM_CD"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("BOX_TYPE_CD"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("BOX_TYPE_CD"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("BOX_QTY"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("BOX_QTY"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("FRT"               ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("FRT"               ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("CUST_MGMT_DLCM_CD" ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("CUST_MGMT_DLCM_CD" ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_NM"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_NM"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_TEL_NO1"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_TEL_NO1"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_TEL_NO2"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_TEL_NO2"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_TEL_NO3"     ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_CELL_NO1"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_CELL_NO1"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_CELL_NO2"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_CELL_NO2"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_CELL_NO3"    ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_SAFE_NO1"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_SAFE_NO1"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_SAFE_NO2"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_SAFE_NO2"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_SAFE_NO3"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_SAFE_NO3"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_ZIP_NO"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_ZIP_NO"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_ADDR"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_ADDR"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SENDR_DETAIL_ADDR" ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SENDR_DETAIL_ADDR" ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_NM"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_NM"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_TEL_NO1"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_TEL_NO1"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_TEL_NO2"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_TEL_NO2"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_TEL_NO3"      ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_CELL_NO1"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_CELL_NO1"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_CELL_NO2"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_CELL_NO2"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_CELL_NO3"     ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_SAFE_NO1"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_SAFE_NO1"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_SAFE_NO2"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_SAFE_NO2"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_SAFE_NO3"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_SAFE_NO3"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_ZIP_NO"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_ZIP_NO"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_ADDR"         ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCVR_ADDR"         ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCVR_DETAIL_ADDR"  ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_NM"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_NM"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_TEL_NO1"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_TEL_NO1"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_TEL_NO2"     ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_TEL_NO2"     ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_TEL_NO3"     ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_CELL_NO1"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_CELL_NO1"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_CELL_NO2"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_CELL_NO2"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_CELL_NO3"    ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_SAFE_NO1"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_SAFE_NO1"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_SAFE_NO2"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_SAFE_NO2"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_SAFE_NO3"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_SAFE_NO3"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_ZIP_NO"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_ZIP_NO"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_ADDR"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORDRR_ADDR"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORDRR_DETAIL_ADDR" ) == null ? "" : " ");
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("INVC_NO"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("INVC_NO"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORI_INVC_NO"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORI_INVC_NO"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ORI_ORD_NO"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ORI_ORD_NO"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("COLCT_EXPCT_YMD"   ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("COLCT_EXPCT_YMD"   ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("COLCT_EXPCT_HOUR"  ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("COLCT_EXPCT_HOUR"  ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SHIP_EXPCT_YMD"    ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SHIP_EXPCT_YMD"    ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("SHIP_EXPCT_HOUR"   ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("SHIP_EXPCT_HOUR"   ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("PRT_ST"            ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("PRT_ST"            ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ARTICLE_AMT"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ARTICLE_AMT"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REMARK_1"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REMARK_1"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REMARK_2"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REMARK_2"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REMARK_3"          ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REMARK_3"          ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("COD_YN"            ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("COD_YN"            ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("GDS_CD"            ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("GDS_CD"            ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("GDS_NM"            ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("GDS_NM"            ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("GDS_QTY"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("GDS_QTY"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("UNIT_CD"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("UNIT_CD"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("UNIT_NM"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("UNIT_NM"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("GDS_AMT"           ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("GDS_AMT"           ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ETC_1"             ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ETC_1"             ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ETC_2"             ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ETC_2"             ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ETC_3"             ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ETC_3"             ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ETC_4"             ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ETC_4"             ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("ETC_5"             ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("ETC_5"             ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("DLV_DV"            ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("DLV_DV"            ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCPT_ERR_YN"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCPT_ERR_YN"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("RCPT_ERR_MSG"      ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("RCPT_ERR_MSG"      ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("EAI_PRGS_ST"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("EAI_PRGS_ST"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("EAI_ERR_MSG"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("EAI_ERR_MSG"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REG_EMP_ID"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REG_EMP_ID"        ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("REG_DTIME"         ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("REG_DTIME"         ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("MODI_EMP_ID"       ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("MODI_EMP_ID"       ));
				if_ps.setString(pSeq++, ((HashMap)cjList.get(ii)).get("MODI_DTIME"        ) == null ? "" : (String)((HashMap)cjList.get(ii)).get("MODI_DTIME"        ));
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
			
			
			/*
			 * CJ 택배 결과 값을 행복나래로 가져온다.
			 */
			sMSG = "CJ 택배 결과값 행복나래 등록";
			
			qry =   ""+
					" SELECT  "+
					"   CUST_ID "+
					" , RCPT_YMD "+
					" , CUST_USE_NO "+
					" , RCPT_DV "+
					" , WORK_DV_CD "+
					" , REQ_DV_CD "+
					" , MPCK_KEY "+
					" , MPCK_SEQ "+
					" , CAL_DV_CD "+
					" , FRT_DV_CD "+
					" , CNTR_ITEM_CD "+
					" , BOX_TYPE_CD "+
					" , BOX_QTY "+
					" , FRT "+
					" , CUST_MGMT_DLCM_CD "+
					" , SENDR_NM "+
					" , SENDR_TEL_NO1 "+
					" , SENDR_TEL_NO2 "+
					" , SENDR_TEL_NO3 "+
					" , SENDR_CELL_NO1 "+
					" , SENDR_CELL_NO2 "+
					" , SENDR_CELL_NO3 "+
					" , SENDR_SAFE_NO1 "+
					" , SENDR_SAFE_NO2 "+
					" , SENDR_SAFE_NO3 "+
					" , SENDR_ZIP_NO "+
					" , SENDR_ADDR "+
					" , SENDR_DETAIL_ADDR "+
					" , RCVR_NM "+
					" , RCVR_TEL_NO1 "+
					" , RCVR_TEL_NO2 "+
					" , RCVR_TEL_NO3 "+
					" , RCVR_CELL_NO1 "+
					" , RCVR_CELL_NO2 "+
					" , RCVR_CELL_NO3 "+
					" , RCVR_SAFE_NO1 "+
					" , RCVR_SAFE_NO2 "+
					" , RCVR_SAFE_NO3 "+
					" , RCVR_ZIP_NO "+
					" , RCVR_ADDR "+
					" , RCVR_DETAIL_ADDR "+
					" , ORDRR_NM "+
					" , ORDRR_TEL_NO1 "+
					" , ORDRR_TEL_NO2 "+
					" , ORDRR_TEL_NO3 "+
					" , ORDRR_CELL_NO1 "+
					" , ORDRR_CELL_NO2 "+
					" , ORDRR_CELL_NO3 "+
					" , ORDRR_SAFE_NO1 "+
					" , ORDRR_SAFE_NO2 "+
					" , ORDRR_SAFE_NO3 "+
					" , ORDRR_ZIP_NO "+
					" , ORDRR_ADDR "+
					" , ORDRR_DETAIL_ADDR "+
					" , INVC_NO "+
					" , ORI_INVC_NO "+
					" , ORI_ORD_NO "+
					" , COLCT_EXPCT_YMD "+
					" , COLCT_EXPCT_HOUR "+
					" , SHIP_EXPCT_YMD "+
					" , SHIP_EXPCT_HOUR "+
					" , PRT_ST "+
					" , ARTICLE_AMT "+
					" , REMARK_1 "+
					" , REMARK_2 "+
					" , REMARK_3 "+
					" , COD_YN "+
					" , GDS_CD "+
					" , GDS_NM "+
					" , GDS_QTY "+
					" , UNIT_CD "+
					" , UNIT_NM "+
					" , GDS_AMT "+
					" , ETC_1 "+
					" , ETC_2 "+
					" , ETC_3 "+
					" , ETC_4 "+
					" , ETC_5 "+
					" , DLV_DV "+
					" , RCPT_ERR_YN "+
					" , RCPT_ERR_MSG "+
					" , EAI_PRGS_ST "+
					" , EAI_ERR_MSG "+
					" , REG_EMP_ID "+
					" , to_char(REG_DTIME ,'yyyymmddhh24miss') REG_DTIME "+
					" , MODI_EMP_ID "+
					" , to_char(MODI_DTIME ,'yyyymmddhh24miss') MODI_DTIME "+
					" FROM CJ_RCPT_HAPPYNARAE010 A "+
					" WHERE 1=1                         "+
					" AND REG_DTIME >= SYSDATE - 2     "+
					" AND NOT EXISTS (                  "+
					" SELECT 'X'                        "+
					" FROM CJ_RCPT_HAPPYNARAE010_TEMP X "+
					" WHERE 1=1                         "+
					" AND X.CUST_ID     = A.CUST_ID     "+
					" AND X.RCPT_YMD    = A.RCPT_YMD    "+
					" AND X.CUST_USE_NO = A.CUST_USE_NO "+
					" AND X.RCPT_DV     = A.RCPT_DV     "+
					" AND X.WORK_DV_CD  = A.WORK_DV_CD  "+
					" AND X.REQ_DV_CD   = A.REQ_DV_CD   "+
					" AND X.MPCK_KEY    = A.MPCK_KEY    "+
					" AND X.MPCK_SEQ    = A.MPCK_SEQ    "+
					" )                                 "+
					" ";
			
			if_ps = if_conn.prepareStatement(qry);	
			
			if_rs = if_ps.executeQuery();
			
			List lRsltList = new ArrayList();
			
			if(if_rs != null){
				while(if_rs.next()){
					HashMap hm = new HashMap();
					hm.put("CUST_ID"           , if_rs.getString("CUST_ID"           ));
					hm.put("RCPT_YMD"          , if_rs.getString("RCPT_YMD"          ));
					hm.put("CUST_USE_NO"       , if_rs.getString("CUST_USE_NO"       ));
					hm.put("RCPT_DV"           , if_rs.getString("RCPT_DV"           ));
					hm.put("WORK_DV_CD"        , if_rs.getString("WORK_DV_CD"        ));
					hm.put("REQ_DV_CD"         , if_rs.getString("REQ_DV_CD"         ));
					hm.put("MPCK_KEY"          , if_rs.getString("MPCK_KEY"          ));
					hm.put("MPCK_SEQ"          , if_rs.getString("MPCK_SEQ"          ));
					hm.put("CAL_DV_CD"         , if_rs.getString("CAL_DV_CD"         ));
					hm.put("FRT_DV_CD"         , if_rs.getString("FRT_DV_CD"         ));
					hm.put("CNTR_ITEM_CD"      , if_rs.getString("CNTR_ITEM_CD"      ));
					hm.put("BOX_TYPE_CD"       , if_rs.getString("BOX_TYPE_CD"       ));
					hm.put("BOX_QTY"           , if_rs.getString("BOX_QTY"           ));
					hm.put("FRT"               , if_rs.getString("FRT"               ));
					hm.put("CUST_MGMT_DLCM_CD" , if_rs.getString("CUST_MGMT_DLCM_CD" ));
					hm.put("SENDR_NM"          , if_rs.getString("SENDR_NM"          ));
					hm.put("SENDR_TEL_NO1"     , if_rs.getString("SENDR_TEL_NO1"     ));
					hm.put("SENDR_TEL_NO2"     , if_rs.getString("SENDR_TEL_NO2"     ));
					hm.put("SENDR_TEL_NO3"     , if_rs.getString("SENDR_TEL_NO3"     ));
					hm.put("SENDR_CELL_NO1"    , if_rs.getString("SENDR_CELL_NO1"    ));
					hm.put("SENDR_CELL_NO2"    , if_rs.getString("SENDR_CELL_NO2"    ));
					hm.put("SENDR_CELL_NO3"    , if_rs.getString("SENDR_CELL_NO3"    ));
					hm.put("SENDR_SAFE_NO1"    , if_rs.getString("SENDR_SAFE_NO1"    ));
					hm.put("SENDR_SAFE_NO2"    , if_rs.getString("SENDR_SAFE_NO2"    ));
					hm.put("SENDR_SAFE_NO3"    , if_rs.getString("SENDR_SAFE_NO3"    ));
					hm.put("SENDR_ZIP_NO"      , if_rs.getString("SENDR_ZIP_NO"      ));
					hm.put("SENDR_ADDR"        , if_rs.getString("SENDR_ADDR"        ));
					hm.put("SENDR_DETAIL_ADDR" , if_rs.getString("SENDR_DETAIL_ADDR" ));
					hm.put("RCVR_NM"           , if_rs.getString("RCVR_NM"           ));
					hm.put("RCVR_TEL_NO1"      , if_rs.getString("RCVR_TEL_NO1"      ));
					hm.put("RCVR_TEL_NO2"      , if_rs.getString("RCVR_TEL_NO2"      ));
					hm.put("RCVR_TEL_NO3"      , if_rs.getString("RCVR_TEL_NO3"      ));
					hm.put("RCVR_CELL_NO1"     , if_rs.getString("RCVR_CELL_NO1"     ));
					hm.put("RCVR_CELL_NO2"     , if_rs.getString("RCVR_CELL_NO2"     ));
					hm.put("RCVR_CELL_NO3"     , if_rs.getString("RCVR_CELL_NO3"     ));
					hm.put("RCVR_SAFE_NO1"     , if_rs.getString("RCVR_SAFE_NO1"     ));
					hm.put("RCVR_SAFE_NO2"     , if_rs.getString("RCVR_SAFE_NO2"     ));
					hm.put("RCVR_SAFE_NO3"     , if_rs.getString("RCVR_SAFE_NO3"     ));
					hm.put("RCVR_ZIP_NO"       , if_rs.getString("RCVR_ZIP_NO"       ));
					hm.put("RCVR_ADDR"         , if_rs.getString("RCVR_ADDR"         ));
					hm.put("RCVR_DETAIL_ADDR"  , if_rs.getString("RCVR_DETAIL_ADDR"  ));
					hm.put("ORDRR_NM"          , if_rs.getString("ORDRR_NM"          ));
					hm.put("ORDRR_TEL_NO1"     , if_rs.getString("ORDRR_TEL_NO1"     ));
					hm.put("ORDRR_TEL_NO2"     , if_rs.getString("ORDRR_TEL_NO2"     ));
					hm.put("ORDRR_TEL_NO3"     , if_rs.getString("ORDRR_TEL_NO3"     ));
					hm.put("ORDRR_CELL_NO1"    , if_rs.getString("ORDRR_CELL_NO1"    ));
					hm.put("ORDRR_CELL_NO2"    , if_rs.getString("ORDRR_CELL_NO2"    ));
					hm.put("ORDRR_CELL_NO3"    , if_rs.getString("ORDRR_CELL_NO3"    ));
					hm.put("ORDRR_SAFE_NO1"    , if_rs.getString("ORDRR_SAFE_NO1"    ));
					hm.put("ORDRR_SAFE_NO2"    , if_rs.getString("ORDRR_SAFE_NO2"    ));
					hm.put("ORDRR_SAFE_NO3"    , if_rs.getString("ORDRR_SAFE_NO3"    ));
					hm.put("ORDRR_ZIP_NO"      , if_rs.getString("ORDRR_ZIP_NO"      ));
					hm.put("ORDRR_ADDR"        , if_rs.getString("ORDRR_ADDR"        ));
					hm.put("ORDRR_DETAIL_ADDR" , if_rs.getString("ORDRR_DETAIL_ADDR" ));
					hm.put("INVC_NO"           , if_rs.getString("INVC_NO"           ));
					hm.put("ORI_INVC_NO"       , if_rs.getString("ORI_INVC_NO"       ));
					hm.put("ORI_ORD_NO"        , if_rs.getString("ORI_ORD_NO"        ));
					hm.put("COLCT_EXPCT_YMD"   , if_rs.getString("COLCT_EXPCT_YMD"   ));
					hm.put("COLCT_EXPCT_HOUR"  , if_rs.getString("COLCT_EXPCT_HOUR"  ));
					hm.put("SHIP_EXPCT_YMD"    , if_rs.getString("SHIP_EXPCT_YMD"    ));
					hm.put("SHIP_EXPCT_HOUR"   , if_rs.getString("SHIP_EXPCT_HOUR"   ));
					hm.put("PRT_ST"            , if_rs.getString("PRT_ST"            ));
					hm.put("ARTICLE_AMT"       , if_rs.getString("ARTICLE_AMT"       ));
					hm.put("REMARK_1"          , if_rs.getString("REMARK_1"          ));
					hm.put("REMARK_2"          , if_rs.getString("REMARK_2"          ));
					hm.put("REMARK_3"          , if_rs.getString("REMARK_3"          ));
					hm.put("COD_YN"            , if_rs.getString("COD_YN"            ));
					hm.put("GDS_CD"            , if_rs.getString("GDS_CD"            ));
					hm.put("GDS_NM"            , if_rs.getString("GDS_NM"            ));
					hm.put("GDS_QTY"           , if_rs.getString("GDS_QTY"           ));
					hm.put("UNIT_CD"           , if_rs.getString("UNIT_CD"           ));
					hm.put("UNIT_NM"           , if_rs.getString("UNIT_NM"           ));
					hm.put("GDS_AMT"           , if_rs.getString("GDS_AMT"           ));
					hm.put("ETC_1"             , if_rs.getString("ETC_1"             ));
					hm.put("ETC_2"             , if_rs.getString("ETC_2"             ));
					hm.put("ETC_3"             , if_rs.getString("ETC_3"             ));
					hm.put("ETC_4"             , if_rs.getString("ETC_4"             ));
					hm.put("ETC_5"             , if_rs.getString("ETC_5"             ));
					hm.put("DLV_DV"            , if_rs.getString("DLV_DV"            ));
					hm.put("RCPT_ERR_YN"       , if_rs.getString("RCPT_ERR_YN"       ));
					hm.put("RCPT_ERR_MSG"      , if_rs.getString("RCPT_ERR_MSG"      ));
					hm.put("EAI_PRGS_ST"       , if_rs.getString("EAI_PRGS_ST"       ));
					hm.put("EAI_ERR_MSG"       , if_rs.getString("EAI_ERR_MSG"       ));
					hm.put("REG_EMP_ID"        , if_rs.getString("REG_EMP_ID"        ));
					hm.put("REG_DTIME"         , if_rs.getString("REG_DTIME"         ));
					hm.put("MODI_EMP_ID"       , if_rs.getString("MODI_EMP_ID"       ));
					hm.put("MODI_DTIME"        , if_rs.getString("MODI_DTIME"        ));
					lRsltList.add(hm);					
				}
			}else{
				throw new SQLException(sMSG+" 오류");
			}
			
			
			System.out.println("insert into count -->"+lRsltList.size());
			
			
			
			if_conn.commit();
			
			
			
			if_DB_DisConn();
			
			
			
			
			cj_DB_Conn();
			
			qry =   ""+
					" INSERT INTO V_RCPT_HAPPYNARAE010 "+
					" (CUST_ID, RCPT_YMD, CUST_USE_NO, RCPT_DV, WORK_DV_CD, REQ_DV_CD, MPCK_KEY, MPCK_SEQ, CAL_DV_CD, FRT_DV_CD, CNTR_ITEM_CD, BOX_TYPE_CD, BOX_QTY, FRT, CUST_MGMT_DLCM_CD, SENDR_NM, SENDR_TEL_NO1, SENDR_TEL_NO2, SENDR_TEL_NO3, SENDR_CELL_NO1, SENDR_CELL_NO2, SENDR_CELL_NO3, SENDR_SAFE_NO1, SENDR_SAFE_NO2, SENDR_SAFE_NO3, SENDR_ZIP_NO, SENDR_ADDR, SENDR_DETAIL_ADDR, RCVR_NM, RCVR_TEL_NO1, RCVR_TEL_NO2, RCVR_TEL_NO3, RCVR_CELL_NO1, RCVR_CELL_NO2, RCVR_CELL_NO3, RCVR_SAFE_NO1, RCVR_SAFE_NO2, RCVR_SAFE_NO3, RCVR_ZIP_NO, RCVR_ADDR, RCVR_DETAIL_ADDR, ORDRR_NM, ORDRR_TEL_NO1, ORDRR_TEL_NO2, ORDRR_TEL_NO3, ORDRR_CELL_NO1, ORDRR_CELL_NO2, ORDRR_CELL_NO3, ORDRR_SAFE_NO1, ORDRR_SAFE_NO2, ORDRR_SAFE_NO3, ORDRR_ZIP_NO, ORDRR_ADDR, ORDRR_DETAIL_ADDR, INVC_NO, ORI_INVC_NO, ORI_ORD_NO, COLCT_EXPCT_YMD, COLCT_EXPCT_HOUR, SHIP_EXPCT_YMD, SHIP_EXPCT_HOUR, PRT_ST, ARTICLE_AMT, REMARK_1, REMARK_2, REMARK_3, COD_YN, GDS_CD, GDS_NM, GDS_QTY, UNIT_CD, UNIT_NM, GDS_AMT, ETC_1, ETC_2, ETC_3, ETC_4, ETC_5, DLV_DV, RCPT_ERR_YN, RCPT_ERR_MSG, EAI_PRGS_ST, EAI_ERR_MSG, REG_EMP_ID, REG_DTIME, MODI_EMP_ID, MODI_DTIME ) "+
					" values "+
					" (?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,SYSDATE  ,?  ,SYSDATE ) "+					
					"";
			
			cj_ps = cj_conn.prepareStatement(qry);	

			for(int ii=0; ii<lRsltList.size(); ii++){
				
				HashMap hm = new HashMap();
				hm = (HashMap)lRsltList.get(ii);
				
				String sCUST_ID              = hm.get("CUST_ID"           ) == null ? "" : (String)hm.get("CUST_ID"           );
				String sRCPT_YMD             = hm.get("RCPT_YMD"          ) == null ? "" : (String)hm.get("RCPT_YMD"          );
				String sCUST_USE_NO          = hm.get("CUST_USE_NO"       ) == null ? "" : (String)hm.get("CUST_USE_NO"       );
				String sRCPT_DV              = hm.get("RCPT_DV"           ) == null ? "" : (String)hm.get("RCPT_DV"           );
				String sWORK_DV_CD           = hm.get("WORK_DV_CD"        ) == null ? "" : (String)hm.get("WORK_DV_CD"        );
				String sREQ_DV_CD            = hm.get("REQ_DV_CD"         ) == null ? "" : (String)hm.get("REQ_DV_CD"         );
				String sMPCK_KEY             = hm.get("MPCK_KEY"          ) == null ? "" : (String)hm.get("MPCK_KEY"          );
				String sMPCK_SEQ             = hm.get("MPCK_SEQ"          ) == null ? "" : (String)hm.get("MPCK_SEQ"          );
				String sCAL_DV_CD            = hm.get("CAL_DV_CD"         ) == null ? "" : (String)hm.get("CAL_DV_CD"         );
				String sFRT_DV_CD            = hm.get("FRT_DV_CD"         ) == null ? "" : (String)hm.get("FRT_DV_CD"         );
				String sCNTR_ITEM_CD         = hm.get("CNTR_ITEM_CD"      ) == null ? "" : (String)hm.get("CNTR_ITEM_CD"      );
				String sBOX_TYPE_CD          = hm.get("BOX_TYPE_CD"       ) == null ? "" : (String)hm.get("BOX_TYPE_CD"       );
				String sBOX_QTY              = hm.get("BOX_QTY"           ) == null ? "" : (String)hm.get("BOX_QTY"           );
				String sFRT                  = hm.get("FRT"               ) == null ? "" : (String)hm.get("FRT"               );
				String sCUST_MGMT_DLCM_CD    = hm.get("CUST_MGMT_DLCM_CD" ) == null ? "" : (String)hm.get("CUST_MGMT_DLCM_CD" );
				String sSENDR_NM             = hm.get("SENDR_NM"          ) == null ? "" : (String)hm.get("SENDR_NM"          );
				String sSENDR_TEL_NO1        = hm.get("SENDR_TEL_NO1"     ) == null ? "" : (String)hm.get("SENDR_TEL_NO1"     );
				String sSENDR_TEL_NO2        = hm.get("SENDR_TEL_NO2"     ) == null ? "" : (String)hm.get("SENDR_TEL_NO2"     );
				String sSENDR_TEL_NO3        = hm.get("SENDR_TEL_NO3"     ) == null ? "" : (String)hm.get("SENDR_TEL_NO3"     );
				String sSENDR_CELL_NO1       = hm.get("SENDR_CELL_NO1"    ) == null ? "" : (String)hm.get("SENDR_CELL_NO1"    );
				String sSENDR_CELL_NO2       = hm.get("SENDR_CELL_NO2"    ) == null ? "" : (String)hm.get("SENDR_CELL_NO2"    );
				String sSENDR_CELL_NO3       = hm.get("SENDR_CELL_NO3"    ) == null ? "" : (String)hm.get("SENDR_CELL_NO3"    );
				String sSENDR_SAFE_NO1       = hm.get("SENDR_SAFE_NO1"    ) == null ? "" : (String)hm.get("SENDR_SAFE_NO1"    );
				String sSENDR_SAFE_NO2       = hm.get("SENDR_SAFE_NO2"    ) == null ? "" : (String)hm.get("SENDR_SAFE_NO2"    );
				String sSENDR_SAFE_NO3       = hm.get("SENDR_SAFE_NO3"    ) == null ? "" : (String)hm.get("SENDR_SAFE_NO3"    );
				String sSENDR_ZIP_NO         = hm.get("SENDR_ZIP_NO"      ) == null ? "" : (String)hm.get("SENDR_ZIP_NO"      );
				String sSENDR_ADDR           = hm.get("SENDR_ADDR"        ) == null ? "" : (String)hm.get("SENDR_ADDR"        );
				String sSENDR_DETAIL_ADDR    = hm.get("SENDR_DETAIL_ADDR" ) == null ? "" : (String)hm.get("SENDR_DETAIL_ADDR" );
				String sRCVR_NM              = hm.get("RCVR_NM"           ) == null ? "" : (String)hm.get("RCVR_NM"           );
				String sRCVR_TEL_NO1         = hm.get("RCVR_TEL_NO1"      ) == null ? "" : (String)hm.get("RCVR_TEL_NO1"      );
				String sRCVR_TEL_NO2         = hm.get("RCVR_TEL_NO2"      ) == null ? "" : (String)hm.get("RCVR_TEL_NO2"      );
				String sRCVR_TEL_NO3         = hm.get("RCVR_TEL_NO3"      ) == null ? "" : (String)hm.get("RCVR_TEL_NO3"      );
				String sRCVR_CELL_NO1        = hm.get("RCVR_CELL_NO1"     ) == null ? "" : (String)hm.get("RCVR_CELL_NO1"     );
				String sRCVR_CELL_NO2        = hm.get("RCVR_CELL_NO2"     ) == null ? "" : (String)hm.get("RCVR_CELL_NO2"     );
				String sRCVR_CELL_NO3        = hm.get("RCVR_CELL_NO3"     ) == null ? "" : (String)hm.get("RCVR_CELL_NO3"     );
				String sRCVR_SAFE_NO1        = hm.get("RCVR_SAFE_NO1"     ) == null ? "" : (String)hm.get("RCVR_SAFE_NO1"     );
				String sRCVR_SAFE_NO2        = hm.get("RCVR_SAFE_NO2"     ) == null ? "" : (String)hm.get("RCVR_SAFE_NO2"     );
				String sRCVR_SAFE_NO3        = hm.get("RCVR_SAFE_NO3"     ) == null ? "" : (String)hm.get("RCVR_SAFE_NO3"     );
				String sRCVR_ZIP_NO          = hm.get("RCVR_ZIP_NO"       ) == null ? "" : (String)hm.get("RCVR_ZIP_NO"       );
				String sRCVR_ADDR            = hm.get("RCVR_ADDR"         ) == null ? "" : (String)hm.get("RCVR_ADDR"         );
				String sRCVR_DETAIL_ADDR     = hm.get("RCVR_DETAIL_ADDR"  ) == null ? "" : (String)hm.get("RCVR_DETAIL_ADDR"  );
				String sORDRR_NM             = hm.get("ORDRR_NM"          ) == null ? "" : (String)hm.get("ORDRR_NM"          );
				String sORDRR_TEL_NO1        = hm.get("ORDRR_TEL_NO1"     ) == null ? "" : (String)hm.get("ORDRR_TEL_NO1"     );
				String sORDRR_TEL_NO2        = hm.get("ORDRR_TEL_NO2"     ) == null ? "" : (String)hm.get("ORDRR_TEL_NO2"     );
				String sORDRR_TEL_NO3        = hm.get("ORDRR_TEL_NO3"     ) == null ? "" : (String)hm.get("ORDRR_TEL_NO3"     );
				String sORDRR_CELL_NO1       = hm.get("ORDRR_CELL_NO1"    ) == null ? "" : (String)hm.get("ORDRR_CELL_NO1"    );
				String sORDRR_CELL_NO2       = hm.get("ORDRR_CELL_NO2"    ) == null ? "" : (String)hm.get("ORDRR_CELL_NO2"    );
				String sORDRR_CELL_NO3       = hm.get("ORDRR_CELL_NO3"    ) == null ? "" : (String)hm.get("ORDRR_CELL_NO3"    );
				String sORDRR_SAFE_NO1       = hm.get("ORDRR_SAFE_NO1"    ) == null ? "" : (String)hm.get("ORDRR_SAFE_NO1"    );
				String sORDRR_SAFE_NO2       = hm.get("ORDRR_SAFE_NO2"    ) == null ? "" : (String)hm.get("ORDRR_SAFE_NO2"    );
				String sORDRR_SAFE_NO3       = hm.get("ORDRR_SAFE_NO3"    ) == null ? "" : (String)hm.get("ORDRR_SAFE_NO3"    );
				String sORDRR_ZIP_NO         = hm.get("ORDRR_ZIP_NO"      ) == null ? "" : (String)hm.get("ORDRR_ZIP_NO"      );
				String sORDRR_ADDR           = hm.get("ORDRR_ADDR"        ) == null ? "" : (String)hm.get("ORDRR_ADDR"        );
				String sORDRR_DETAIL_ADDR    = hm.get("ORDRR_DETAIL_ADDR" ) == null ? "" : (String)hm.get("ORDRR_DETAIL_ADDR" );
				String sINVC_NO              = hm.get("INVC_NO"           ) == null ? "" : (String)hm.get("INVC_NO"           );
				String sORI_INVC_NO          = hm.get("ORI_INVC_NO"       ) == null ? "" : (String)hm.get("ORI_INVC_NO"       );
				String sORI_ORD_NO           = hm.get("ORI_ORD_NO"        ) == null ? "" : (String)hm.get("ORI_ORD_NO"        );
				String sCOLCT_EXPCT_YMD      = hm.get("COLCT_EXPCT_YMD"   ) == null ? "" : (String)hm.get("COLCT_EXPCT_YMD"   );
				String sCOLCT_EXPCT_HOUR     = hm.get("COLCT_EXPCT_HOUR"  ) == null ? "" : (String)hm.get("COLCT_EXPCT_HOUR"  );
				String sSHIP_EXPCT_YMD       = hm.get("SHIP_EXPCT_YMD"    ) == null ? "" : (String)hm.get("SHIP_EXPCT_YMD"    );
				String sSHIP_EXPCT_HOUR      = hm.get("SHIP_EXPCT_HOUR"   ) == null ? "" : (String)hm.get("SHIP_EXPCT_HOUR"   );
				String sPRT_ST               = hm.get("PRT_ST"            ) == null ? "" : (String)hm.get("PRT_ST"            );
				String sARTICLE_AMT          = hm.get("ARTICLE_AMT"       ) == null ? "" : (String)hm.get("ARTICLE_AMT"       );
				String sREMARK_1             = hm.get("REMARK_1"          ) == null ? "" : (String)hm.get("REMARK_1"          );
				String sREMARK_2             = hm.get("REMARK_2"          ) == null ? "" : (String)hm.get("REMARK_2"          );
				String sREMARK_3             = hm.get("REMARK_3"          ) == null ? "" : (String)hm.get("REMARK_3"          );
				String sCOD_YN               = hm.get("COD_YN"            ) == null ? "" : (String)hm.get("COD_YN"            );
				String sGDS_CD               = hm.get("GDS_CD"            ) == null ? "" : (String)hm.get("GDS_CD"            );
				String sGDS_NM               = hm.get("GDS_NM"            ) == null ? "" : (String)hm.get("GDS_NM"            );
				String sGDS_QTY              = hm.get("GDS_QTY"           ) == null ? "" : (String)hm.get("GDS_QTY"           );
				String sUNIT_CD              = hm.get("UNIT_CD"           ) == null ? "" : (String)hm.get("UNIT_CD"           );
				String sUNIT_NM              = hm.get("UNIT_NM"           ) == null ? "" : (String)hm.get("UNIT_NM"           );
				String sGDS_AMT              = hm.get("GDS_AMT"           ) == null ? "" : (String)hm.get("GDS_AMT"           );
				String sETC_1                = hm.get("ETC_1"             ) == null ? "" : (String)hm.get("ETC_1"             );
				String sETC_2                = hm.get("ETC_2"             ) == null ? "" : (String)hm.get("ETC_2"             );
				String sETC_3                = hm.get("ETC_3"             ) == null ? "" : (String)hm.get("ETC_3"             );
				String sETC_4                = hm.get("ETC_4"             ) == null ? "" : (String)hm.get("ETC_4"             );
				String sETC_5                = hm.get("ETC_5"             ) == null ? "" : (String)hm.get("ETC_5"             );
				String sDLV_DV               = hm.get("DLV_DV"            ) == null ? "" : (String)hm.get("DLV_DV"            );
				String sRCPT_ERR_YN          = hm.get("RCPT_ERR_YN"       ) == null ? "" : (String)hm.get("RCPT_ERR_YN"       );
				String sRCPT_ERR_MSG         = hm.get("RCPT_ERR_MSG"      ) == null ? "" : (String)hm.get("RCPT_ERR_MSG"      );
				String sEAI_PRGS_ST          = hm.get("EAI_PRGS_ST"       ) == null ? "" : (String)hm.get("EAI_PRGS_ST"       );
				String sEAI_ERR_MSG          = hm.get("EAI_ERR_MSG"       ) == null ? "" : (String)hm.get("EAI_ERR_MSG"       );
				String sREG_EMP_ID           = hm.get("REG_EMP_ID"        ) == null ? "" : (String)hm.get("REG_EMP_ID"        );
				//String sREG_DTIME            = hm.get("REG_DTIME"         ) == null ? "" : (String)hm.get("REG_DTIME"         );
				String sMODI_EMP_ID          = hm.get("MODI_EMP_ID"       ) == null ? "" : (String)hm.get("MODI_EMP_ID"       );
				//String sMODI_DTIME           = hm.get("MODI_DTIME"        ) == null ? "" : (String)hm.get("MODI_DTIME"        );
				
				pSeq = 1;
				cj_ps.setString(pSeq++, sCUST_ID               == null ? "" : sCUST_ID              );
				cj_ps.setString(pSeq++, sRCPT_YMD              == null ? "" : sRCPT_YMD             );
				cj_ps.setString(pSeq++, sCUST_USE_NO           == null ? "" : sCUST_USE_NO          );
				cj_ps.setString(pSeq++, sRCPT_DV               == null ? "" : sRCPT_DV              );
				cj_ps.setString(pSeq++, sWORK_DV_CD            == null ? "" : sWORK_DV_CD           );
				cj_ps.setString(pSeq++, sREQ_DV_CD             == null ? "" : sREQ_DV_CD            );
				cj_ps.setString(pSeq++, sMPCK_KEY              == null ? "" : sMPCK_KEY             );
				cj_ps.setString(pSeq++, sMPCK_SEQ              == null ? "" : sMPCK_SEQ             );
				cj_ps.setString(pSeq++, sCAL_DV_CD             == null ? "" : sCAL_DV_CD            );
				cj_ps.setString(pSeq++, sFRT_DV_CD             == null ? "" : sFRT_DV_CD            );
				cj_ps.setString(pSeq++, sCNTR_ITEM_CD          == null ? "" : sCNTR_ITEM_CD         );
				cj_ps.setString(pSeq++, sBOX_TYPE_CD           == null ? "" : sBOX_TYPE_CD          );
				cj_ps.setString(pSeq++, sBOX_QTY               == null ? "" : sBOX_QTY              );
				cj_ps.setString(pSeq++, sFRT                   == null ? "" : sFRT                  );
				cj_ps.setString(pSeq++, sCUST_MGMT_DLCM_CD     == null ? "" : sCUST_MGMT_DLCM_CD    );
				cj_ps.setString(pSeq++, sSENDR_NM              == null ? "" : sSENDR_NM             );
				cj_ps.setString(pSeq++, sSENDR_TEL_NO1         == null ? "" : sSENDR_TEL_NO1        );
				cj_ps.setString(pSeq++, sSENDR_TEL_NO2         == null ? "" : sSENDR_TEL_NO2        );
				cj_ps.setString(pSeq++, sSENDR_TEL_NO3         == null ? "" : sSENDR_TEL_NO3        );
				cj_ps.setString(pSeq++, sSENDR_CELL_NO1        == null ? "" : sSENDR_CELL_NO1       );
				cj_ps.setString(pSeq++, sSENDR_CELL_NO2        == null ? "" : sSENDR_CELL_NO2       );
				cj_ps.setString(pSeq++, sSENDR_CELL_NO3        == null ? "" : sSENDR_CELL_NO3       );
				cj_ps.setString(pSeq++, sSENDR_SAFE_NO1        == null ? "" : sSENDR_SAFE_NO1       );
				cj_ps.setString(pSeq++, sSENDR_SAFE_NO2        == null ? "" : sSENDR_SAFE_NO2       );
				cj_ps.setString(pSeq++, sSENDR_SAFE_NO3        == null ? "" : sSENDR_SAFE_NO3       );
				cj_ps.setString(pSeq++, sSENDR_ZIP_NO          == null ? "" : sSENDR_ZIP_NO         );
				cj_ps.setString(pSeq++, sSENDR_ADDR            == null ? "" : sSENDR_ADDR           );
				cj_ps.setString(pSeq++, sSENDR_DETAIL_ADDR     == null ? "" : sSENDR_DETAIL_ADDR    );
				cj_ps.setString(pSeq++, sRCVR_NM               == null ? "" : sRCVR_NM              );
				cj_ps.setString(pSeq++, sRCVR_TEL_NO1          == null ? "" : sRCVR_TEL_NO1         );
				cj_ps.setString(pSeq++, sRCVR_TEL_NO2          == null ? "" : sRCVR_TEL_NO2         );
				cj_ps.setString(pSeq++, sRCVR_TEL_NO3          == null ? "" : sRCVR_TEL_NO3         );
				cj_ps.setString(pSeq++, sRCVR_CELL_NO1         == null ? "" : sRCVR_CELL_NO1        );
				cj_ps.setString(pSeq++, sRCVR_CELL_NO2         == null ? "" : sRCVR_CELL_NO2        );
				cj_ps.setString(pSeq++, sRCVR_CELL_NO3         == null ? "" : sRCVR_CELL_NO3        );
				cj_ps.setString(pSeq++, sRCVR_SAFE_NO1         == null ? "" : sRCVR_SAFE_NO1        );
				cj_ps.setString(pSeq++, sRCVR_SAFE_NO2         == null ? "" : sRCVR_SAFE_NO2        );
				cj_ps.setString(pSeq++, sRCVR_SAFE_NO3         == null ? "" : sRCVR_SAFE_NO3        );
				cj_ps.setString(pSeq++, sRCVR_ZIP_NO           == null ? "" : sRCVR_ZIP_NO          );
				cj_ps.setString(pSeq++, sRCVR_ADDR             == null ? "" : sRCVR_ADDR            );
				cj_ps.setString(pSeq++, sRCVR_DETAIL_ADDR      == null ? "" : sRCVR_DETAIL_ADDR     );
				cj_ps.setString(pSeq++, sORDRR_NM              == null ? "" : sORDRR_NM             );
				cj_ps.setString(pSeq++, sORDRR_TEL_NO1         == null ? "" : sORDRR_TEL_NO1        );
				cj_ps.setString(pSeq++, sORDRR_TEL_NO2         == null ? "" : sORDRR_TEL_NO2        );
				cj_ps.setString(pSeq++, sORDRR_TEL_NO3         == null ? "" : sORDRR_TEL_NO3        );
				cj_ps.setString(pSeq++, sORDRR_CELL_NO1        == null ? "" : sORDRR_CELL_NO1       );
				cj_ps.setString(pSeq++, sORDRR_CELL_NO2        == null ? "" : sORDRR_CELL_NO2       );
				cj_ps.setString(pSeq++, sORDRR_CELL_NO3        == null ? "" : sORDRR_CELL_NO3       );
				cj_ps.setString(pSeq++, sORDRR_SAFE_NO1        == null ? "" : sORDRR_SAFE_NO1       );
				cj_ps.setString(pSeq++, sORDRR_SAFE_NO2        == null ? "" : sORDRR_SAFE_NO2       );
				cj_ps.setString(pSeq++, sORDRR_SAFE_NO3        == null ? "" : sORDRR_SAFE_NO3       );
				cj_ps.setString(pSeq++, sORDRR_ZIP_NO          == null ? "" : sORDRR_ZIP_NO         );
				cj_ps.setString(pSeq++, sORDRR_ADDR            == null ? "" : sORDRR_ADDR           );
				cj_ps.setString(pSeq++, sORDRR_DETAIL_ADDR     == null ? "" : sORDRR_DETAIL_ADDR    );
				cj_ps.setString(pSeq++, sINVC_NO               == null ? "" : sINVC_NO              );
				cj_ps.setString(pSeq++, sORI_INVC_NO           == null ? "" : sORI_INVC_NO          );
				cj_ps.setString(pSeq++, sORI_ORD_NO            == null ? "" : sORI_ORD_NO           );
				cj_ps.setString(pSeq++, sCOLCT_EXPCT_YMD       == null ? "" : sCOLCT_EXPCT_YMD      );
				cj_ps.setString(pSeq++, sCOLCT_EXPCT_HOUR      == null ? "" : sCOLCT_EXPCT_HOUR     );
				cj_ps.setString(pSeq++, sSHIP_EXPCT_YMD        == null ? "" : sSHIP_EXPCT_YMD       );
				cj_ps.setString(pSeq++, sSHIP_EXPCT_HOUR       == null ? "" : sSHIP_EXPCT_HOUR      );
				cj_ps.setString(pSeq++, sPRT_ST                == null ? "" : sPRT_ST               );
				cj_ps.setString(pSeq++, sARTICLE_AMT           == null ? "" : sARTICLE_AMT          );
				cj_ps.setString(pSeq++, sREMARK_1              == null ? "" : sREMARK_1             );
				cj_ps.setString(pSeq++, sREMARK_2              == null ? "" : sREMARK_2             );
				cj_ps.setString(pSeq++, sREMARK_3              == null ? "" : sREMARK_3             );
				cj_ps.setString(pSeq++, sCOD_YN                == null ? "" : sCOD_YN               );
				cj_ps.setString(pSeq++, sGDS_CD                == null ? "" : sGDS_CD               );
				cj_ps.setString(pSeq++, sGDS_NM                == null ? "" : sGDS_NM               );
				cj_ps.setString(pSeq++, sGDS_QTY               == null ? "" : sGDS_QTY              );
				cj_ps.setString(pSeq++, sUNIT_CD               == null ? "" : sUNIT_CD              );
				cj_ps.setString(pSeq++, sUNIT_NM               == null ? "" : sUNIT_NM              );
				cj_ps.setString(pSeq++, sGDS_AMT               == null ? "" : sGDS_AMT              );
				cj_ps.setString(pSeq++, sETC_1                 == null ? "" : sETC_1                );
				cj_ps.setString(pSeq++, sETC_2                 == null ? "" : sETC_2                );
				cj_ps.setString(pSeq++, sETC_3                 == null ? "" : sETC_3                );
				cj_ps.setString(pSeq++, sETC_4                 == null ? "" : sETC_4                );
				cj_ps.setString(pSeq++, sETC_5                 == null ? "" : sETC_5                );
				cj_ps.setString(pSeq++, sDLV_DV                == null ? "" : sDLV_DV               );
				cj_ps.setString(pSeq++, sRCPT_ERR_YN           == null ? "" : sRCPT_ERR_YN          );
				cj_ps.setString(pSeq++, sRCPT_ERR_MSG          == null ? "" : sRCPT_ERR_MSG         );
				cj_ps.setString(pSeq++, sEAI_PRGS_ST           == null ? "" : sEAI_PRGS_ST          );
				cj_ps.setString(pSeq++, sEAI_ERR_MSG           == null ? "" : sEAI_ERR_MSG          );
				cj_ps.setString(pSeq++, sREG_EMP_ID            == null ? "" : sREG_EMP_ID           );
				//cj_ps.setString(pSeq++, sREG_DTIME             == null ? "" : sREG_DTIME            );
				cj_ps.setString(pSeq++, sMODI_EMP_ID           == null ? "" : sMODI_EMP_ID          );
				//cj_ps.setString(pSeq++, sMODI_DTIME            == null ? "" : sMODI_DTIME           );
				
				cj_ps.executeUpdate();
				
			}
			
			
			cj_conn.commit();
			
			cj_DB_DisConn();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if_DB_DisConn();
			if_DB_DisConn2();
			cj_DB_DisConn();
			cj_DB_DisConn2();

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
		System.out.println(daytime+"-------------Start Demon_cj_parcel_rcpt Daemon-------------");
		
		Demon_cj_parcel_rcpt daemon = new Demon_cj_parcel_rcpt();
		
		try{
			daemon.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		Date date2 = new Date();
		String daytime2 = format.format(date);
		System.out.println(daytime2+"-------------End Demon_cj_parcel_rcpt Daemon-------------");

	}

}
