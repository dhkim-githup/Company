/**
 * ��ö�� ���� ��û��
 * ����8��, ����1��, ����3��, ����5��
 *
 */

import java.sql.*;

import comm.DB_Use_Db_Demon;

//���� �ҷ��� ���

class  Demon_4_aday
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
        //������
        public Demon_4_aday(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_4_aday.java";
    			db.DB_Conn();

				System.out.println("Db Connection !! ");

				try{
				// Call Procedure
				System.out.println(" Call P_ORA_DEMON.P_4_ADAY !! ");
				db.prepareCall("{call P_ORA_DEMON.P_4_ADAY(1)}");
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