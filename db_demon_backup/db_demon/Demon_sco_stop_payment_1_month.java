/*
 * MD ����12�� ��ȸ ����
 */
//package comm;

import java.sql.*;

//���� �ҷ��� ���

class  Demon_sco_stop_payment_1_month
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
        //������
        public Demon_sco_stop_payment_1_month(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_sco_stop_payment_1_month.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				// Call Procedure
				db.prepareCall("{call P_ORA_DEMON_MONTHLY.P_SCO_STOP_PAYMENT(1)}");
				db.Cexecute();
				db.commit();
				//db.rollback();
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