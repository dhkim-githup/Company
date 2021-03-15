package comm;
import java.sql.*;

public class DB_Use_Db_Demon{
	public	Connection conn;
	//private mk_log log = new mk_log();
	
	public String driver="oracle.jdbc.driver.OracleDriver";
	/**/
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:ndb10";
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W"; 
	
	/*
	public String dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
	public String user_id="METS_IMSI";
	public String user_pw="METS_IMSI"; 
	*/
	public Statement smt= null, smt2 = null, smt3 = null, ex_smt = null;
    public ResultSet rs = null, rs2 = null, rs3 = null;

	public String PROG_NM="-----", CUST_ID="-----", ERR_MSG="";
	public int ERR_FLAG = 0, ERR_CODE = 0, sel_qry_log=0, dml_qry_log=1; 

	public PreparedStatement ps=null, ps2=null, ps3=null;
    public ResultSet prs = null, prs2 = null, prs3 = null;

	public CallableStatement cs = null, cs2=null;

	public DB_Use_Db_Demon() {
	}

	public Connection DB_Conn() {

	 try {
		 	Class.forName(driver);
			conn=DriverManager.getConnection(dbURL,user_id,user_pw);

            conn.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			ERR_FLAG = -1; ERR_CODE = -1; conn = null;
			// log.Write(PROG_NM,"ERR ConnectionBean: driver unavailable !!");
		} catch (Exception e) {
			ERR_FLAG = -1; ERR_CODE = -1; conn = null;
			// log.Write(PROG_NM,"ERR ConnectionBean: driver not loaded !!");
		}
		return conn;
	}

	public void commit() throws SQLException {
		conn.commit();
	}

	public void rollback() throws SQLException {
		conn.rollback();
	}

	public void executeQuery(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
        try {
			if(smt == null) { smt = conn.createStatement(); }
			rs = smt.executeQuery(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
		} catch (Exception e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = -1000;
			// log.Write(PROG_NM,CUST_ID+">>=>ERR =>"+ERR_MSG);
		}
	}

	public void executeQuery2(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
        try {
			if(smt2 == null) { smt2 = conn.createStatement(); }
			rs2 = smt2.executeQuery(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
		}
	}

	public void executeQuery3(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
        try {
			if(smt3 == null) { smt3 = conn.createStatement(); }
			rs3 = smt3.executeQuery(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		int res = 0;
		try {
			//// log.Write(dml_qry_log,PROG_NM,CUST_ID+">>=>DB_SURE="+sql);
			ex_smt = conn.createStatement();
			res = ex_smt.executeUpdate(sql);
		} catch (SQLException e) {
			res = -1; ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
			rollback();
		}
		if(ex_smt != null) ex_smt.close(); return res;
	}

	public int executeUpdate(int p_log_write, String sql) throws SQLException { // 050523oh�α׳���°� ����
		dml_qry_log = p_log_write;
		return executeUpdate(sql);
	}

	public void rs_close() {
		try {
			if(rs != null) rs.close();
			if(smt != null) smt.close();
		} catch (Exception e) { }
	}

	public void rs2_close() {
		try {
			if(rs2 != null) rs2.close();
			if(smt2 != null) smt2.close();
		} catch (Exception e) { }
	}

	public void rs3_close() {
		try {
			if(rs3 != null) rs3.close();
			if(smt3 != null) smt3.close();
		} catch (Exception e) { }
	}

	public void prepareStatement(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
		}
	}

	public void PsetString(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+msg_buf11+msg_buf21);
		}
	}

	public void PsetInt(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+msg_buf11+msg_buf21);
		}
	}

	public void PexecuteQuery() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			//// log.Write(sel_qry_log,PROG_NM,CUST_ID+">>=>DB_SURE= "+msg_buf11+msg_buf21);
			prs = ps.executeQuery();
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+msg_buf11+msg_buf21);
		}
	}

	public int PexecuteUpdate() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		int res = 0;
		try {
			//// log.Write(dml_qry_log,PROG_NM,CUST_ID+">>=>DB_SUER= "+msg_buf11+msg_buf21);
			res = ps.executeUpdate();
		} catch (SQLException e) {
			res = -1;
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+msg_buf11+msg_buf21);
			rollback();
		}
		return res;
	}

	public void ps_close() {
		try {
			if(prs != null) prs.close();
			if(ps != null) ps.close();
		} catch (Exception e) { }
	}

	public void prepareStatement2(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps2 = conn.prepareStatement(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+sql);
		}
	}

	public void PsetString2(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps2.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
		}
	}

	public void PsetInt2(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps2.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
		}
	}

	public void PsetDouble2(int seq,double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps2.setDouble(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
		}
	}

	public void PexecuteQuery2() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			//// log.Write(sel_qry_log,PROG_NM,CUST_ID+">>=>DB_SUER= "+msg_ps21+msg_ps22);
			prs2 = ps2.executeQuery();
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
		}
	}

	public int PexecuteUpdate2() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		int res = 0;
		try {
			//// log.Write(dml_qry_log,PROG_NM,CUST_ID+">>=>DB_SUER= "+msg_ps21+msg_ps22);
			res = ps2.executeUpdate();
		} catch (SQLException e) {
			res = -1;
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
			rollback();
		}
		return res;
	}

	public void ps_close2() {
		try {
			if(prs2 != null) prs2.close();
			if(ps2 != null) ps2.close();
		} catch (Exception e) { }
	}

	public void prepareStatement3(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps3 = conn.prepareStatement(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+sql+" -- "+ERR_MSG);
		}
	}

	public void PsetString3(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps3.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
		}
	}

	public void PsetInt3(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps3.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
		}
	}

	public void PsetDouble3(int seq,double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			ps3.setDouble(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
		}
	}

	public void PexecuteQuery3() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			//// log.Write(sel_qry_log,PROG_NM,CUST_ID+">>=>DB_SUER= "+msg_ps31+msg_ps32);
			prs3 = ps3.executeQuery();
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
		}
	}

	public int PexecuteUpdate3() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		int res = 0;
		try {
			//// log.Write(dml_qry_log,PROG_NM,CUST_ID+">>=>DB_SUER= "+msg_ps31+msg_ps32);
			res = ps3.executeUpdate();
		} catch (SQLException e) {
			res = -1;
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
			rollback();
		}
		return res;
	}

	public void ps_close3() {
		try {
			if(prs3 != null) prs3.close();
			if(ps3 != null) ps3.close();
		} catch (SQLException e) { }
	}

	public void prepareCall(String sql) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			if(cs != null) cs.close();
			cs = conn.prepareCall(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+sql+" -- "+ERR_MSG);
		}
	}

	public void CsetString(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_buf1+msg_buf2+" -- "+ERR_MSG);
		}
	}

	public void CsetInt(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_buf1+msg_buf2+" -- "+ERR_MSG);
		}
	}

	public void CsetDouble(int seq, double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs.setDouble(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_buf1+msg_buf2+" -- "+ERR_MSG);
		}
	}

	public int Cexecute() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			//// log.Write(PROG_NM,CUST_ID+">>=>DB_SUER= "+msg_buf1+msg_buf2);
			cs.execute();
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_buf1+msg_buf2+" -- "+ERR_MSG);
			rollback();
		} catch (Exception e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = -1;
			// log.Write(PROG_NM,CUST_ID+">>=>ERR= "+e);
		}
		return ERR_FLAG;
	}

	public void prepareCall2(String sql) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs2 = conn.prepareCall(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+sql+" -- "+ERR_MSG);
		}
	}

	public void CsetString2(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs2.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+cs_msg21+cs_msg22+" -- "+ERR_MSG);
		}
	}

	public void CsetInt2(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs2.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+cs_msg21+cs_msg22+" -- "+ERR_MSG);
		}
	}

	public void CsetDouble2(int seq, double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs2.setDouble(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+cs_msg21+cs_msg22+" -- "+ERR_MSG);
		}
	}

	public int Cexecute2() throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			//// log.Write(PROG_NM,CUST_ID+">>=>DB_SUER= "+cs_msg21+cs_msg22);
			cs2.execute();
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+cs_msg21+cs_msg22+" -- "+ERR_MSG);
			rollback();
		} catch (Exception e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = -1;
			// log.Write(PROG_NM,CUST_ID+">>=>ERR= "+e);
		}
		return ERR_FLAG;
	}

	public void DB_DisConn() {
		try {
			conn.setAutoCommit(true);

			if(rs != null){ rs.close();}
			if(rs2 != null){ rs2.close(); }
			if(rs3 != null){ rs3.close(); }

			if(smt != null){ smt.close(); }
			if(smt2 != null){ smt2.close(); }
			if(smt3 != null){ smt3.close(); }
			if(ex_smt != null){ ex_smt.close(); }

			if(prs != null){ prs.close(); }
			if(prs2 != null){ prs2.close(); }
			if(prs3 != null){ prs3.close(); }

			if(ps != null){ ps.close(); }
			if(ps2 != null){ ps2.close(); }
			if(ps3 != null){ ps3.close(); }

			if(cs != null){ cs.close(); }
			if(cs2 != null){ cs2.close(); }

			if(conn != null){ conn.close(); }

			//// log.Write(PROG_NM, "############ DisConnect  Connection Beans - DB_Use_Db_Demon DisConnetc Ver.1 ##############");
			// log.Write(PROG_NM, "[�� DB DisConnect Ver.2]");
		} catch (Exception e) { }
	}
}
