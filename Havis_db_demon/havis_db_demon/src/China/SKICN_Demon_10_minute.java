package China;
import java.sql.*;
import China.comm.*;

class  SKICN_Demon_10_minute
{
	static private DB_Use db ;  
	public SKICN_Demon_10_minute(){}

	public static void main(String[] args){
		db = new DB_Use();  
		try{
			db.CUST_ID = "00002345";
			db.PROG_NM = "/home/mro/demon/Demon_10_minute_v2.java";
			db.DB_Conn();
			System.out.println("DB Connection");
			// Call Procedure
			db.prepareCall("{call P_SKICN_DEMON.P_SKICN_DEMON_10(1)}");
			db.Cexecute();
			db.commit();
			System.out.println("Connect Succ");
		}catch(SQLException e){
			try{
				System.out.println(e);
				db.rollback();
			}catch(Exception e1){}
		}finally{
			db.DB_DisConn();
			System.out.println("Db Dis_Conn !! ");
		}
	}
}
