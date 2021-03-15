import java.sql.*;

import comm.DB_Use_Db_Demon;

//빈즈 불러서 사용

class  Demon_Am2
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
        //생성자
        public Demon_Am2(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_Am2.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");


	        	try{
					// Call Procedure
					System.out.println("Call  P_ANALYSIS_FR_JOB.JOB_TO_ONE ('1') !! ");
					db.prepareCall("{call P_ANALYSIS_FR_JOB.JOB_TO_ONE ('1')}");
					db.Cexecute();
					db.commit();
	        	}catch(Exception e){
	                System.out.println("Error"+e);
	                try{
	                    System.out.println(e);
	                     db.rollback();
	                 }catch(Exception e1){}
	            }
	        	
	        	/* 2013.04.23 dhkim 이철희팀장 요청 추가 */
	        	try{
					// Call Procedure
					System.out.println("Call  P_AUTO_CRETEC_DELAY_RPT('1') !! ");
					db.prepareCall("{call P_AUTO_CRETEC_DELAY_RPT('1')}");
					db.Cexecute();
					db.commit();
	        	}catch(Exception e){
	                System.out.println("Error"+e);
	                try{
	                    System.out.println(e);
	                     db.rollback();
	                 }catch(Exception e1){}
	            }
	        	
	        	
	        	/* 2012.08.21 dhkim  미발행 관련 데몬.. 이관호 작업건 */
	        	try{
					// Call Procedure
					System.out.println("Call  P_make_acco_demon.SP_ACCO_LED_ACT_DEMON ('1') , P_make_acco_demon.SP_ACCO_LED_ACT_DEMON2 ('1') !! ");
					db.prepareCall("{call P_make_acco_demon.SP_ACCO_LED_ACT_DEMON ('1')}"); //  -- 매출미발행 관련
					db.Cexecute();
					db.commit();

					db.prepareCall("{call P_make_acco_demon.SP_ACCO_LED_ACT_DEMON2 ('1')}"); // -- -- 매입미발행 관련
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