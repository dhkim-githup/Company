

import java.sql.*;

import comm.DB_Use_Db_CN_Demon;

//빈즈 불러서 사용

class  Demon_china_mail
{
		static private comm.DB_Use_Db_CN_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_china_mail(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_CN_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_china_mail.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				try{
					// Call Procedure
					System.out.println("Call  P_CN_DEMON.S_PROCESS_MAIL_STEP !! ");
					db.prepareCall("{call P_CN_DEMON.S_PROCESS_MAIL_STEP(1)}");
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
