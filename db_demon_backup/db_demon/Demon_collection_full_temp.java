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

public class Demon_collection_full_temp {
	
	static private mk_log log = new mk_log();
	static public int log_flag = 0;
	static public String prgm_nm = "Demon_cj_parcel";
	
	public String driver="oracle.jdbc.driver.OracleDriver";
	
	//행복나래 DB Info
	/**/
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";

	
	public	Connection conn;
	public PreparedStatement ps=null;
	public CallableStatement cs = null;
	public ResultSet rs = null;	
	public String qry = "";
	
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
	
	public void DB_DisConn() {
		try {
			conn.setAutoCommit(true);

			if(ps != null){ ps.close(); }
			if(cs != null){ cs.close(); }
			if(rs != null){ rs.close(); }
			if(conn != null){ conn.close(); }

		} catch (Exception e) {
			//System.out.println("ERR disConnection error !!"+e.getMessage());
		}
	}	
	
	public void start() throws Exception {
		try{
			DB_Conn();
			qry = "{call P_COLLECTION_FULL_TEMP}";
			cs = conn.prepareCall(qry);	
			cs.execute();	
			conn.commit();
			DB_DisConn();
		}catch(Exception e){
			
			//SMS 전송
			qry =   ""+
					" INSERT INTO EM_TRAN                                                                                       "+
					" (TRAN_PR,TRAN_PHONE ,TRAN_CALLBACK ,TRAN_STATUS ,TRAN_DATE ,TRAN_MSG ,TRAN_ETC1 ,TRAN_ETC2 ,TRAN_ETC3)    "+
					" SELECT EM_TRAN_PR.NEXTVAL  , HN_ENC.F_DEC_AES(A.MCOM_NUM)  ,'1644-5644', 1 , SYSDATE  , '"+"ERROR OCCUR AT P_COLLECTION_FULL:"+e.getMessage()+"', 'REGENPRI', 'REGSCO' , ''  "+
					"   FROM CUST_DTL A                                                                                         "+
					"  WHERE 1=1                                                                                                "+
					"    AND A.CUST_ID IN ('M0050962')                                                                          "+
					"";
			
			ps = conn.prepareStatement(qry);	
			ps.executeUpdate();
			
			conn.commit();
			
			DB_DisConn();			
			
			e.printStackTrace();
    		throw new Exception("ERROR OCCUR AT P_COLLECTION_FULL:"+e.getMessage());
		}finally{
			DB_DisConn();
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
		System.out.println(daytime+"-------------Start Demon_cj_parcel Daemon-------------");
		
		Demon_collection_full_temp daemon = new Demon_collection_full_temp();
		
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
