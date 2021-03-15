/****************************************************
 * 5분 주기 SAP 데몬 (FOR MERP > SAP FILE FTP I/F)
 * CALL P_SAP_IF.SP_SAP_IF_5_MINUTE
 * => SP_SAP_IF_ADOCU
 * => SP_SAP_IF_ADOCU_CNCL
 * => SP_SAP_IF_PAY_CNCL   
 * 
 * 2020.07.01 SWCHO
 ****************************************************/
import java.sql.SQLException;

public class Demon_sap_if_5_minute {	

	static private comm.DB_Use_Db_Demon  db ; 
    //생성자
    public Demon_sap_if_5_minute(){}

    public static void main(String[] args)
    {
    	db = new comm.DB_Use_Db_Demon(); 
    	try{

        	db.CUST_ID = "M0705788";
        	db.PROG_NM = "/user/home/mro/db_demon/Demon_sap_if_5_minute.java";
			db.DB_Conn();
			System.out.println("Db Connection !! ");

			// Call Procedure
			db.prepareCall("{call P_SAP_IF.SP_SAP_IF_5_MINUTE@l_mets_imsi(1)}");
			db.Cexecute();
			db.commit();
			System.out.println("Db Excute !! P_SAP_IF.SP_SAP_IF_5_MINUTE@l_mets_imsi(1) ");
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