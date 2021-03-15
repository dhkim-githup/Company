import java.sql.SQLException;


public class Demon_bond_alert_weekly {
	static private comm.DB_Use_Db_Demon  db ;  //  DB ï¿½ï¿½ï¿½ï¿½ï¿½Ï´ï¿½ ï¿½ï¿½ï¿½ï¿½
    //ï¿½ï¿½ï¿½ï¿½
    public Demon_bond_alert_weekly(){}

    public static void main(String[] args)
    {
    	db = new comm.DB_Use_Db_Demon();  //  DB ï¿½ï¿½ï¿½ï¿½ï¿½Ï´ï¿½ ï¿½ï¿½ï¿½ï¿½
    	try{

        	db.CUST_ID = "M0306965";
        	db.PROG_NM = "/user/home/mro/mro_demon/Demon_bond_alert_weekly.java";
			db.DB_Conn();
			System.out.println("Db Connection !! ");

			// Call Procedure
			try{
				db.prepareCall("{call P_ORA_DEMON_MAIL.P_SEND_BOND_MAIL_FOR_MARKETER(1)}");
				db.Cexecute();
				db.commit();
			}catch(Exception e){
				System.out.println(e.getMessage());
				db.rollback();
			}
			/*  Demon_bond_alert_day.java ·Î ÀÌµ¿ hrkim 151105
			try{
				db.prepareCall("{call P_ORA_DEMON_MAIL.P_SEND_BOND_MAIL_FOR_CEO(1)}");
				db.Cexecute();
				db.commit();
			}catch(Exception e){
				System.out.println(e.getMessage());
				db.rollback();
			}
			*/
			
			//db.rollback();
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
