/**
 * 10분씩 돌아가는 데
 */

import java.sql.*;

//빈즈 불러서 사용

class  Demon_10_minute_v2
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_10_minute_v2(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_10_minute_v2.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				// Call Procedure
				db.prepareCall("{call P_ORA_DEMON_10.P_10_DEMON(1)}");
				db.Cexecute();
				db.commit();
				System.out.println("Db Excute !! ");
            }catch(SQLException e){
                System.out.println("Error"+e);
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
