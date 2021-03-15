/**
 * 2014.10.23 dhkim 
 * 1분씩 돌아가는 데몬. 
 * 아래 데몬외에 다른 구문 금지. 반드시 담당자 dhkim 에게 확인 후 처리해야함.
 * 아래 구문은 물류 재고관리가 돌아가는 관계로 다른 구문을 허용하지 않음.
 * 2021.03.15 dhkim, 사용안함
 */

import java.sql.*;

//빈즈 불러서 사용

class  Demon_1_minute
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_1_minute(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00009358";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_11_minute.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				// Call Procedure
				db.prepareCall("{call P_JOTT.UP_JOTT_DEMON(1)}");
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