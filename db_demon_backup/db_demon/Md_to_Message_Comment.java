/*
 * MD 댓글 관련 SMS송부 프로그램 , 관련자 박성환
 */
//package db_demon;

import java.sql.*;

//빈즈 불러서 사용

class  Md_to_Message_Comment
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Md_to_Message_Comment(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/db_demon/mro_demon/Md_to_Message_Comment.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				// Call Procedure
				db.prepareCall("{call SP_SMS_PRD_APP(?)}");
				db.CsetString(1, "1");
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