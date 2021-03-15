

import java.sql.*;

import comm.DB_Use_Db_Demon;

//빈즈 불러서 사용

class  Demon_Am9
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_Am9(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_Am9.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				try{
					// Call Procedure
					System.out.println("Call  P_ORA_DEMON.P_AUTO_RFQ_DEMON !! ");
					db.prepareCall("{call P_ORA_DEMON.P_AUTO_RFQ_DEMON(1)}");
					db.Cexecute();
					db.commit();
	        	}catch(Exception e){
	                System.out.println("Error"+e);
	                try{
	                    System.out.println(e);
	                     db.rollback();
	                 }catch(Exception e1){}
	            }	   

					        

				System.out.println("Db Excute !! ");
            }catch(Exception e){
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
