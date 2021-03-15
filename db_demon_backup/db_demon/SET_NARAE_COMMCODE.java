package SET_NARAE_INSA;

import java.*;
import COMM.*;

public class SET_NARAE_COMMCODE {
	private  static final String Prog_Nm = "SET_NARAE_COMMCODE"; 
	private static mk_log log = new mk_log();
	
	public SET_NARAE_COMMCODE() {}

	public static void main(String[] args) {
		try{
			SET_NARAE_COMMCODE setComm = new SET_NARAE_COMMCODE();			
			setComm.SetData();
		}
		catch(Exception e){
			
		}
	}
	
	public void SetData() throws Exception {
		// 행복나래 DB 선언
		DB_Use db = new DB_Use();				
		
		try {
			
			log.Write(Prog_Nm,Prog_Nm,"\t");
			// 행복나래 DB 접속
			db.DB_Conn();
			log.Write(Prog_Nm,Prog_Nm,"행복나래DB 접속 완료 >> ORACLE 11G");
			
			String lsProc="";
			
			lsProc = "{call P_INF_GW.SP_CRT_COMMONCODE(?)}";
			db.prepareCall(lsProc);	
			log.Write(Prog_Nm,Prog_Nm,"StoredProc 선언 >> " + lsProc );			
			
			db.CsetString (1, "");
    	// 임시테이블에 값 등록
    	db.Cexecute();	
			// 작업완료
    	db.commit();
			log.Write(Prog_Nm,Prog_Nm,"작업완료");  
			
		}
		catch (Exception e) {
			db.rollback();
			
		}
		finally {
			db.DB_DisConn();
		}
	}	
}
