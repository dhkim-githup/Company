/*
 * MD ��� ���� SMS�ۺ� ���α׷� , ������ �ڼ�ȯ
 */
//package db_demon;

import java.sql.*;

//���� �ҷ��� ���

class  Md_to_Message_Comment
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
        //������
        public Md_to_Message_Comment(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
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