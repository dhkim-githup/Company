/**
 * 2014.10.23 dhkim 
 * 1�о� ���ư��� ����. 
 * �Ʒ� ����ܿ� �ٸ� ���� ����. �ݵ�� ����� dhkim ���� Ȯ�� �� ó���ؾ���.
 * �Ʒ� ������ ���� �������� ���ư��� ����� �ٸ� ������ ������� ����.
 */

import java.sql.*;

//���� �ҷ��� ���

class  Demon_1_minute
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
        //������
        public Demon_1_minute(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
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