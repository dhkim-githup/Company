
public class Demon_make_acco_open_market {
	static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
	
	public static void main(String[] args) {
		db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
    	try{

        	db.CUST_ID = "M0306965";
        	db.PROG_NM = "/user/home/mro/mro_demon/Demon_make_acco_open_market.java";
			db.DB_Conn();
			
			db.prepareCall("{call p_make_open_market_acco.make_open_market_acco(1)}");
			db.Cexecute();
			db.commit();
			
				
			
    	}catch (Exception e) {
    		
    		 System.out.println("Error"+e);
             try{
                 System.out.println(e);
                  db.rollback();
              }catch(Exception e1){}
			
		}finally{
			db.DB_DisConn();

		}
    	
	}

}
