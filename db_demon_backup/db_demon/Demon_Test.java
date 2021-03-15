
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





public class Demon_Test {
	
	public String driver="oracle.jdbc.driver.OracleDriver";
	
	/**/
	public String dbURL="jdbc:oracle:thin:@210.98.159.153:1521:OPENDB";
	public String user_id="happynarae";
	public String user_pw="happynarae!@#$";

	/**
	
	public String dbURL="jdbc:oracle:thin:@172.16.1.224:1521:testdb";
	public String user_id="METS_IMSI";
	public String user_pw="METS_IMSI";
	
	/**/
	
	public	Connection conn, conn2;
	
	private Statement smt= null, smt2 = null;
	public PreparedStatement ps=null, ps2=null;
	public CallableStatement cs = null, cs2=null;
	
	public ResultSet rs = null, rs2 = null;
	
	public String qry, qry2 = "";
	
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
		return conn;
		
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
	
	public void start() throws Exception {
		
		int pSeq = 1;
		String cnt = "";
		String prog_nm = "Ebill_mail_send";
		
		
		try{
			DB_Conn();
			
			qry =  " SELECT count(*) cnt               "+
					"   FROM V_RCPT_HAPPYNARAE010           ";
			
			ps = conn.prepareStatement(qry);	
			rs = ps.executeQuery();
			
			if(rs != null){
				while(rs.next()){
					cnt = rs.getString("cnt");
					
					System.out.println("cnt--->"+cnt);
					
				}
			}
			
			
			
			conn.commit();
			
			DB_DisConn();
		}catch(Exception e){
			e.printStackTrace();
    		throw new Exception("전자세금계산서 메일 전송 중 오류가발생했습니다.");
		}finally{
			DB_DisConn();
			DB_DisConn2();
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
		System.out.println(daytime+"-------------Start Ebill_mail_send-------------");
		
		Demon_Test daemon = new Demon_Test();
		
		try{
			daemon.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		Date date2 = new Date();
		String daytime2 = format.format(date);
		System.out.println(daytime2+"-------------End Ebill_mail_send-------------");

	}

}
