
public class Demon_make_cal_mng {
	static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
	
	public static void main(String[] args) {
		db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
    	try{

        	db.CUST_ID = "M0306965";
        	db.PROG_NM = "/user/home/mro/mro_demon/Demon_make_cal_mng.java";
			db.DB_Conn();
			
			try{
				// Call Procedure
				System.out.println("Call  p_make_cal_mng.makeCalMng1(1)  ");
				db.prepareCall("{call p_make_cal_mng.makeCalMng1(1)}");
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
				// Call Procedure
				System.out.println("Call  p_make_cal_mng.makeCalMng2(1)  ");
				db.prepareCall("{call p_make_cal_mng.makeCalMng2(1)}");
				db.Cexecute();
				db.commit();
        	}catch(Exception e){
                System.out.println("Error"+e);
                try{
                    System.out.println(e);
                     db.rollback();
                 }catch(Exception e1){}
            }
			
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
