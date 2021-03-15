/**
 * 포인트 관련 데몬(삭제 및 메일 문자)
 * @author shpark
 *
 */


import java.sql.*;

import comm.DB_Use_Db_Demon;

class  Demon_skn_1month
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_skn_1month(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_skn_1month.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");
				// Call Procedure
				db.prepareCall("{call P_METS_DEMON.SP_SKN_ACCOUNT(?,?)}");
				db.cs.registerOutParameter(1, Types.VARCHAR);	// 리턴값 : 0 정상 , 1 에러
				db.cs.registerOutParameter(2, Types.VARCHAR);	// 리턴메세지
				db.Cexecute();
				
				String dstrRNum = "";
				String dstrRMsg = "";

				dstrRNum = db.cs.getString(1);
				dstrRMsg = db.cs.getString(2);

				if(!"0".equals(dstrRNum)){
				db.rollback();
				}


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