/*
 * MD 1 시간씩  돌아가는 데몬 모음
 */
//package comm;

import java.sql.*;

//빈즈 불러서 사용

class  Demon_1_hour
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_1_hour(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_1_hour.java";
    			db.DB_Conn();

				System.out.println("Db Connection !! ");

				try{
				// Call Procedure
				System.out.println(" Call P_ORA_DEMON.P_ONE_HOUR !! ");
				db.prepareCall("{call P_ORA_DEMON.P_ONE_HOUR(1)}");
				db.Cexecute();
				db.commit();
				}catch(Exception e){
	                System.out.println("Error"+e);
	                try{
	                    System.out.println(e);
	                     db.rollback();
	                 }catch(Exception e1){}
	            }

				try{
				// Call Procedure-  mhHong
				System.out.println(" Call P_ANALYSIS_FR_JOB.sp_crt_merp_minus_margin !! ");
				db.prepareCall("{call P_ANALYSIS_FR_JOB.sp_crt_merp_minus_margin('1')}");
				db.Cexecute();
				db.commit();
				}catch(Exception e){
	                System.out.println("Error"+e);
	                try{
	                    System.out.println(e);
	                     db.rollback();
	                 }catch(Exception e1){}
	            }

				try{
				// Call Procedure - mhHong
				System.out.println(" Call P_ANALYSIS_FR_JOB.sp_crt_merp_chk_category !! ");
				db.prepareCall("{call P_ANALYSIS_FR_JOB.sp_crt_merp_chk_category('1')}");
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