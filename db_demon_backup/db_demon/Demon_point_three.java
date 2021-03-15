/**
 * ����Ʈ ���� ����(���� �� ���� ����)
 * @author shpark
 *
 */


import java.sql.*;

import comm.DB_Use_Db_Demon;

class  Demon_point_three
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
        //������
        public Demon_point_three(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_point_three.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");
				// Call Procedure
				db.prepareCall("{call P_SPEED_DEMON.F_GET_POINT_THREE_YEAR_DEL(?,?)}");
				db.cs.registerOutParameter(1, Types.VARCHAR);	// ���ϰ� : 0 ���� , 1 ����
				db.cs.registerOutParameter(2, Types.VARCHAR);	// ���ϸ޼���
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