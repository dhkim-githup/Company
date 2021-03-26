package China.comm;
import java.sql.*;

public class DB_Use{
	public	Connection conn;
	//private mk_log log = new mk_log();

//	private static final String driver="oracle.jdbc.driver.OracleDriver";
//	private static final String dbURL="jdbc:oracle:thin:@172.16.1.202:1521:ORA8";
	public String driver="oracle.jdbc.driver.OracleDriver";

	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";

	private String msg_buf1="", msg_buf2="", msg_buf11="", msg_buf21="", cs_msg21="", cs_msg22="",
					msg_ps21="", msg_ps22="",msg_ps31="", msg_ps32="";

	public Statement smt= null, smt2 = null, smt3 = null, ex_smt = null;
    public ResultSet rs = null, rs2 = null, rs3 = null;

	public String PROG_NM="-----", CUST_ID="-----", ERR_MSG="";
	public int ERR_FLAG = 0, ERR_CODE = 0, sel_qry_log=0, dml_qry_log=1;

	public PreparedStatement ps=null, ps2=null, ps3=null;
    public ResultSet prs = null, prs2 = null, prs3 = null;

	public CallableStatement cs = null, cs2=null;

	public DB_Use() {
	}

	public Connection DB_Conn() {

	 try {
		 	Class.forName(driver);
			conn=DriverManager.getConnection(dbURL,user_id,user_pw);

            conn.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			ERR_FLAG = -1; ERR_CODE = -1; conn = null;
			//System.out.println(e);
		} catch (Exception e) {
			ERR_FLAG = -1; ERR_CODE = -1; conn = null;
			//System.out.println(e);
		}
		return conn;
	}

	public void commit() throws SQLException {
		conn.commit();
	}

	public void rollback() throws SQLException {
		conn.rollback();
	}

	/**
	private void execQry(int p_flag, String p_qry) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			switch(p_flag) {
				case 1: if(smt == null) { smt = conn.createStatement(); }
					rs = smt.executeQuery(p_qry);
					break;
				case 2: if(smt2 == null) { smt2 = conn.createStatement(); }
					rs2 = smt2.executeQuery(p_qry);
					break;
				case 3: if(smt3 == null) { smt3 = conn.createStatement(); }
					rs3 = smt3.executeQuery(p_qry);
					break;
				case 4: if(smt4 == null) { smt4 = conn.createStatement(); }
					rs4 = smt4.executeQuery(p_qry);
					break;
			}
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+p_qry);
		} catch (Exception e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = -1000;
			// log.Write(PROG_NM,CUST_ID+">>=>ERR =>"+ERR_MSG);
		}
	}


	public void executeQuery(String sql) throws SQLException { execQry(1, sql); }
	public void executeQuery2(String sql) throws SQLException { execQry(2, sql); }
	public void executeQuery3(String sql) throws SQLException { execQry(3, sql); }
	public void executeQuery4(String sql) throws SQLException { execQry(4, sql); }

	public void exeQry(String sql) throws SQLException { execQry(1, sql); }
	public void exeQry2(String sql) throws SQLException { execQry(2, sql); }
	public void exeQry3(String sql) throws SQLException { execQry(3, sql); }
	public void exeQry4(String sql) throws SQLException { execQry(4, sql); }

	public int exeUp(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		int res = 0;
		try {
			// log.Write(PROG_NM,CUST_ID+">>=>DB_SURE="+sql);
			ex_smt = conn.createStatement();
			res = ex_smt.executeUpdate(sql);
		} catch (SQLException e) {
			res = -1;
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
			rollback();
		}
		if(ex_smt != null) ex_smt.close();
		return res;
	}

	public String GStr(String p_fld) throws SQLException { String h_val=rs.getString(p_fld); return (h_val == null?"":h_val); }
	public String GStr(String p_fld, String p_val) throws SQLException {
		String h_val=rs.getString(p_fld); return (h_val == null?p_val:h_val);
	}
	public String GStr(int p_seq) throws SQLException { String h_val=rs.getString(p_seq); return (h_val == null?"":h_val); }
	public String GStr(int p_seq, String p_val) throws SQLException {
		String h_val=rs.getString(p_seq); return (h_val == null?p_val:h_val);
	}
	public double GDbl(String p_fld) throws SQLException { double h_val=rs.getDouble(p_fld); return h_val; }
	public String GDbl(String p_fld, String p_val) throws SQLException {
		String h_val=rs.getString(p_fld); return (h_val == null?p_val:nf.format(GDbl(p_fld)));
	}
	public double GDbl(int p_seq) throws SQLException { double h_val=rs.getDouble(p_seq); return h_val; }
	public String GDbl(int p_seq, String p_val) throws SQLException {
		String h_val=rs.getString(p_seq); return (h_val == null?p_val:nf.format(GDbl(p_seq)));
	}

	public String GStr2(String p_fld) throws SQLException { String h_val=rs2.getString(p_fld); return(h_val == null?"":h_val); }
	public String GStr2(String p_fld, String p_val) throws SQLException {
		String h_val=rs2.getString(p_fld); return (h_val == null?p_val:h_val);
	}
	public String GStr2(int p_seq) throws SQLException { String h_val=rs2.getString(p_seq); return (h_val == null?"":h_val); }
	public String GStr2(int p_seq, String p_val) throws SQLException {
		String h_val=rs2.getString(p_seq); return (h_val == null?p_val:h_val);
	}
	public String GStr3(String p_fld) throws SQLException { String h_val=rs3.getString(p_fld); return (h_val == null?"":h_val); }
	public String GStr3(String p_fld, String p_val) throws SQLException {
		String h_val=rs3.getString(p_fld); return (h_val == null?p_val:h_val);
	}
	public String GStr3(int p_seq) throws SQLException { String h_val=rs3.getString(p_seq); return (h_val == null?"":h_val); }
	public String GStr3(int p_seq, String p_val) throws SQLException {
		String h_val=rs3.getString(p_seq); return (h_val == null?p_val:h_val);
	}
	**/

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

	public int executeUpdate(int p_log_write, String sql) throws SQLException { // 050523oh로그남기는것 구분
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
			msg_buf21 = ""; msg_buf11 = sql;
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+sql);
		}
	}

	public void PsetString(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_buf21 += "'"+p_val+"',";
			ps.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+" ==> "+msg_buf11+msg_buf21);
		}
	}

	public void PsetInt(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_buf21 += p_val+",";
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
		msg_buf21="";
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
		msg_buf21=""; // 050328 msg_buf2 => msg_buf21
		return res;
	}

	public void ps_close() {
		try {
			msg_buf11=""; msg_buf21="";
			if(prs != null) prs.close();
			if(ps != null) ps.close();
		} catch (Exception e) { }
	}

	public void prepareStatement2(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps22 = ""; msg_ps21 = sql;
			ps2 = conn.prepareStatement(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+sql);
		}
	}

	public void PsetString2(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps22 += "'"+p_val+"',";
			ps2.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
		}
	}

	public void PsetInt2(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps22 += p_val+",";
			ps2.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_CODE = e.getErrorCode(); ERR_MSG = e.toString();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR_MSG="+ERR_MSG+"==> "+msg_ps21+msg_ps22);
		}
	}

	public void PsetDouble2(int seq,double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps22 += p_val+",";
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
		msg_ps22="";
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
		msg_ps22="";
		return res;
	}

	public void ps_close2() {
		try {
			msg_ps21=""; msg_ps22="";
			if(prs2 != null) prs2.close();
			if(ps2 != null) ps2.close();
		} catch (Exception e) { }
	}

	public void prepareStatement3(String sql) throws SQLException {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps31 = sql;
			ps3 = conn.prepareStatement(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+sql+" -- "+ERR_MSG);
		}
	}

	public void PsetString3(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps32 += "'"+p_val+"',";
			ps3.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
		}
	}

	public void PsetInt3(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps32 += p_val+",";
			ps3.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_ps31+msg_ps32+" -- "+ERR_MSG);
		}
	}

	public void PsetDouble3(int seq,double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_ps32 += p_val+",";
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
		msg_ps32="";
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
		msg_ps32="";
		return res;
	}

	public void ps_close3() {
		try {
			msg_ps31=""; msg_ps32="";
			if(prs3 != null) prs3.close();
			if(ps3 != null) ps3.close();
		} catch (SQLException e) { }
	}

	public void prepareCall(String sql) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_buf2 = ""; msg_buf1 = sql;
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
			msg_buf2 += "'"+p_val+"',";
			cs.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_buf1+msg_buf2+" -- "+ERR_MSG);
		}
	}

	public void CsetInt(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_buf2 += p_val+",";
			cs.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+msg_buf1+msg_buf2+" -- "+ERR_MSG);
		}
	}

	public void CsetDouble(int seq, double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			msg_buf2 += p_val+",";
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
		msg_buf2="";
		return ERR_FLAG;
	}

	public void prepareCall2(String sql) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs_msg22 = ""; cs_msg21 = sql;
			cs2 = conn.prepareCall(sql);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+sql+" -- "+ERR_MSG);
		}
	}

	public void CsetString2(int seq,String p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs_msg22 += "'"+p_val+"',";
			cs2.setString(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+cs_msg21+cs_msg22+" -- "+ERR_MSG);
		}
	}

	public void CsetInt2(int seq,int p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs_msg22 += p_val+",";
			cs2.setInt(seq,p_val);
		} catch (SQLException e) {
			ERR_FLAG = -1; ERR_MSG = e.toString(); ERR_CODE = e.getErrorCode();
			// log.Write(PROG_NM,CUST_ID+">>=>DB_ERR= "+cs_msg21+cs_msg22+" -- "+ERR_MSG);
		}
	}

	public void CsetDouble2(int seq, double p_val) {
		ERR_FLAG = 0; ERR_CODE = 0;
		try {
			cs_msg22 += p_val+",";
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
		cs_msg22="";
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
			// log.Write(PROG_NM, "[■ DB DisConnect Ver.2]");
		} catch (Exception e) { }
	}
}
