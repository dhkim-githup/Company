import comm.DB_Use_Db_Demon;
import comm.mk_log;
import java.util.*;
import java.sql.Types;

public class CJ_Demon {

	private DB_Use_Db_Demon db = new DB_Use_Db_Demon();
	private DB_Use_Db_Demon db1 = new DB_Use_Db_Demon();
	private mk_log log = new mk_log();
	String err_occur=null, ERR_MSG="";
	
	public CJ_Demon() {

	}

	private void db_conn(){
		//db.dbURL = "jdbc:oracle:thin:@192.168.1.211:1521:TESTDB"; // Local 테스트
		//db.dbURL = "jdbc:oracle:thin:@192.168.1.211:1521:TESTDB"; // 서버 테스트
		//db.user_id = "METS_IMSI"; // 테스트
		//db.user_pw = "METS_IMSI"; // 테스트
		//db.dbURL = "jdbc:oracle:thin:@211.43.195.63:1521:NDB10"; // Local 운영
		
		db.dbURL = "jdbc:oracle:thin:@172.16.1.208:1521:NDB10"; // 서버 운영
		db.user_id = "METS18940G"; //운영
		db.user_pw = "METS25519P58563W"; //운영
		
		db1.dbURL = "jdbc:oracle:thin:@203.248.116.44:1521:DRAGON"; // CJ서버
		db1.user_id = "mrokr"; //CJ ID
		db1.user_pw = "mrokr123"; //CJ PASS
		
		db.DB_Conn();
		db1.DB_Conn();	
				
	}
	
	private void db_proc(){
		db_conn();  // DB연결
		ArrayList al_data = new ArrayList(); //배열 선언 
		String str_ZIPNUM = "";
		String str_CLLDLVEMPNUM = "";
		String str_CLLDLVEMPNM = "";
		String str_CLLDLVEMPTELNUM = "";
		String str_CLLDLVBRANCD = "";
		
		String str_CLLDLVBRANNM = "";
		String str_CLLDLVBRANTELNUM = "";
		String str_CLSFCD = "";
		String str_ADDRSHORT = "";
		String str_AREAGU = "";
		
		String str_MODDATTM = "";
		String str_Err_flag = "OK";
		
		//CJ쪽 데이터 가져오는쿼리
		String str_qry = " SELECT ZIPNUM, CLLDLVEMPNUM, CLLDLVEMPNM, CLLDLVEMPTELNUM, CLLDLVBRANCD, " 
						+ "    CLLDLVBRANNM, CLLDLVBRANTELNUM, CLSFCD, ADDRSHORT, AREAGU, to_char(MODDATTM,'yyyymmdd') MODDATTM "
						+ " FROM V_IFST_POST_MROKR ";

		try{			
			//대상을 추출한다.
			db1.prepareStatement(str_qry);
			db1.PexecuteQuery();
			int cnt=0;
			log.Write("CJ_ZIP_DEMON", "CJ DATA INSERT");
			while(db1.prs.next()){
				Hashtable ht = new Hashtable();
				
				ht.put("ZIPNUM", db1.prs.getString("ZIPNUM"));
				ht.put("CLLDLVEMPNUM", db1.prs.getString("CLLDLVEMPNUM"));
				ht.put("CLLDLVEMPNM", db1.prs.getString("CLLDLVEMPNM"));
				ht.put("CLLDLVEMPTELNUM", db1.prs.getString("CLLDLVEMPTELNUM"));
				ht.put("CLLDLVBRANCD", db1.prs.getString("CLLDLVBRANCD"));
				
				ht.put("CLLDLVBRANNM", db1.prs.getString("CLLDLVBRANNM"));
				ht.put("CLLDLVBRANTELNUM", db1.prs.getString("CLLDLVBRANTELNUM"));
				ht.put("CLSFCD", db1.prs.getString("CLSFCD"));
				ht.put("ADDRSHORT", db1.prs.getString("ADDRSHORT"));
				ht.put("AREAGU", db1.prs.getString("AREAGU"));
				
				ht.put("MODDATTM", db1.prs.getString("MODDATTM"));
				al_data.add(ht);
			}
			log.Write("CJ_ZIP_DEMON", "CJ DATA INSERT END");			
			//mest insert쿼리
			String str_qry1 = " INSERT INTO IFST_POST_MROKR_CJ( "
							+"	            ZIPNUM, CLLDLVEMPNUM, CLLDLVEMPNM, CLLDLVEMPTELNUM, CLLDLVBRANCD, " 
							+"	            CLLDLVBRANNM, CLLDLVBRANTELNUM, CLSFCD, ADDRSHORT, AREAGU, MODDATTM) "
							+"	VALUES(?,?,?,?,?, ?,?,?,?,?,to_date(?,'yyyymmdd')) " ;
			//기존에 있는 정보는 삭제하는 쿼리
			String str_qry2 = " DELETE IFST_POST_MROKR_CJ ";
			
			//insert 구문 pre 
			db.prepareStatement(str_qry1);
			
			//기존에 있는 데이터는 삭제
			db.prepareStatement2(str_qry2);
			if(db.PexecuteUpdate2() < 0 ){
				log.Write("CJ_ZIP_DEMON", "METS DELETE ERROR");
				throw new Exception();
			}
			log.Write("CJ_ZIP_DEMON", "METS HISTORY DELETE");
			log.Write("CJ_ZIP_DEMON", "METS DATA INSERT");
			for(int i=0;i<al_data.size();i++) {
				Hashtable ht = (Hashtable) al_data.get(i);			
				str_ZIPNUM =  ht.get("ZIPNUM").toString();
				str_CLLDLVEMPNUM =  ht.get("CLLDLVEMPNUM").toString();
				str_CLLDLVEMPNM =  ht.get("CLLDLVEMPNM").toString();
				str_CLLDLVEMPTELNUM =  ht.get("CLLDLVEMPTELNUM").toString();
				str_CLLDLVBRANCD =  ht.get("CLLDLVBRANCD").toString();
				
				str_CLLDLVBRANNM =  ht.get("CLLDLVBRANNM").toString();
				str_CLLDLVBRANTELNUM =  ht.get("CLLDLVBRANTELNUM").toString();
				str_CLSFCD =  ht.get("CLSFCD").toString();
				str_ADDRSHORT =  ht.get("ADDRSHORT").toString();
				str_AREAGU =  ht.get("AREAGU").toString();
				
				str_MODDATTM =  ht.get("MODDATTM").toString();
				
				db.PsetString(1, str_ZIPNUM);
				db.PsetString(2, str_CLLDLVEMPNUM);
				db.PsetString(3, str_CLLDLVEMPNM); 
				db.PsetString(4, str_CLLDLVEMPTELNUM); 
				db.PsetString(5, str_CLLDLVBRANCD); 
				
				db.PsetString(6, str_CLLDLVBRANNM); 
				db.PsetString(7, str_CLLDLVBRANTELNUM); 
				db.PsetString(8, str_CLSFCD); 
				db.PsetString(9, str_ADDRSHORT); 
				db.PsetString(10, str_AREAGU); 
				
				db.PsetString(11, str_MODDATTM); 
				
				if(db.PexecuteUpdate() < 0 ){
					log.Write("CJ_ZIP_DEMON", "METS INSERT ERROR");
					throw new Exception();
				}
			}
			log.Write("CJ_ZIP_DEMON", "METS INSERT END");			db.commit();
			db1.commit();
		}catch(Exception e){
			log.Write("CJ_ZIP_DEMON", "Error=====>"+e);			str_Err_flag = "NO";
			try{
				db.rollback();
				db1.rollback();
			}catch(Exception e2){}
		}finally{			
			try{				
				str_qry = "{call sp_send_sms(?,?,?,?,?, ?,?,?)}";				
				db.prepareCall(str_qry);	            
				db.CsetString (1, "010-3189-0333"); // 받는사람	            
				db.CsetString (2, "1544-1644"); // 보내는 사람	            
				if("OK".equals(str_Err_flag)) {	            	
					db.CsetString (3, "CJ데몬 데이터전송 완료"); // tra_num	            
				}else{	            	
					db.CsetString (3, "CJ데몬 데이터전송 실패 에러를 확인하세요"); // tra_num	            
				}	            
					db.CsetString (4, ""); // tra_seq	            
					db.CsetString (5, ""); // tra_seq	            
					db.CsetString (6, ""); // tra_seq	            
					db.cs.registerOutParameter (7, Types.VARCHAR);	            
					db.cs.registerOutParameter (8, Types.VARCHAR);	            
					db.Cexecute();	           
					db.commit();			
				}catch(Exception e) {				
					try{db.rollback(); }catch(Exception e1){}				
					log.Write("CJ_ZIP_DEMON", "Error=====>"+e);		
				}
			db_disconn();  //DB접속 종료
		}		
	}
	
	private void db_disconn() {
		db.DB_DisConn();
		db1.DB_DisConn();
	}
	
	public static void main(String args[]) {
		CJ_Demon s = new CJ_Demon();
		
		
		s.db_proc();  // CJ 데이터 처리		
	}
}