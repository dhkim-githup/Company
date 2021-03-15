package SET_NARAE_INSA;
 
import java.*;
import COMM.*;

public class SET_NARAE_ORGANIZATION {
	private  static final String Prog_Nm = "SET_NARAE_ORGANIZATION"; 
	private static mk_log log = new mk_log();
	
	public SET_NARAE_ORGANIZATION() {}

	public static void main(String[] args) {
		try{
			SET_NARAE_ORGANIZATION setOrg = new SET_NARAE_ORGANIZATION();			
			setOrg.SetData();
		}
		catch(Exception e){
			
		}
	}
	
	public void SetData() throws Exception {
		// �ູ���� DB ����
		DB_Use db = new DB_Use();				
		
		try {
			
			log.Write(Prog_Nm,Prog_Nm,"\t");
			// �ູ���� DB ����
			db.DB_Conn();
			log.Write(Prog_Nm,Prog_Nm,"�ູ����DB ���� �Ϸ� >> ORACLE 11G");
			
			String lsProc="";
			
			lsProc = "{call P_INF_GW.SP_CRT_ORGANIZATION(?)}";
			db.prepareCall(lsProc);	
			log.Write(Prog_Nm,Prog_Nm,"StoredProc ���� >> " + lsProc );			
			
			db.CsetString (1, "");
    	// �ӽ����̺��� �� ���
    	db.Cexecute();	
			// �۾��Ϸ�
    	db.commit();
			log.Write(Prog_Nm,Prog_Nm,"�۾��Ϸ�");
			
		}
		catch (Exception e) {
			db.rollback();
			
		}
		finally {
			db.DB_DisConn();
		}
	}	
	
}