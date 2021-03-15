import comm.DB_Use_Db_Demon;
import comm.mk_log;
import comm.OkMroUtil;

import java.sql.Types;
import java.util.*;

public class HD_Demon {

	private DB_Use_Db_Demon db = new DB_Use_Db_Demon();
	private DB_Use_Db_Demon db1 = new DB_Use_Db_Demon();
	private mk_log log = new mk_log();
	private OkMroUtil ok_util = new OkMroUtil();
	String err_occur=null, ERR_MSG="";
	
	public HD_Demon() {

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
		
		db1.dbURL = "jdbc:oracle:thin:@203.228.216.29:1521:hdftp"; // CJ서버
		db1.user_id = "hdmro"; //CJ ID
		db1.user_pw = "mro0324"; //CJ PASS
		
		db.DB_Conn();
		db1.DB_Conn();	
				
	}
	
	private void db_disconn() {
		db.DB_DisConn();
		db1.DB_DisConn();
	}
	
	private void db_proc(){
		db_conn();  // DB연결
		ArrayList al_data = new ArrayList(); //배열 선언 
		String str_Err_flag = "OK";
		try{		
			//CJ쪽 데이터 가져오는쿼리
			String str_qry = " SELECT ZIP_NO, SVC_CD, TML_CD, TML_NM, FILT_CD, "
							+"       CITY_DO, CITY_GUN_GU, GU, EUP_MUIN_DONG, BLD_RI, "
							+"       BEG_STT_NO, LAST_STT_NO, DLV_CYC_CD, TRS_YMD, SHIP_FARE, "
							+"      AIR_FARE, DLV_BRNSHP_NM " 
							+" from VMROKOR_ZIP ";	
			
			//대상을 추출한다.
			db1.prepareStatement(str_qry);
			db1.PexecuteQuery();

			log.Write("HD_ZIP_DEMON", "HD DATA SEARCH");
			
			
			while(db1.prs.next()){
				Hashtable ht = new Hashtable();
				ht.put("ZIP_NO", ok_util.checkNull(db1.prs.getString("ZIP_NO")));
				ht.put("SVC_CD", ok_util.checkNull(db1.prs.getString("SVC_CD")));
				ht.put("TML_CD", ok_util.checkNull(db1.prs.getString("TML_CD")));
				ht.put("TML_NM", ok_util.checkNull(db1.prs.getString("TML_NM")));
				ht.put("FILT_CD", ok_util.checkNull(db1.prs.getString("FILT_CD")));

				ht.put("CITY_DO", ok_util.checkNull(db1.prs.getString("CITY_DO")));
				ht.put("CITY_GUN_GU", ok_util.checkNull(db1.prs.getString("CITY_GUN_GU")));
				ht.put("GU", ok_util.checkNull(db1.prs.getString("GU")));
				ht.put("EUP_MUIN_DONG", ok_util.checkNull(db1.prs.getString("EUP_MUIN_DONG")));
				ht.put("BLD_RI", ok_util.checkNull(db1.prs.getString("BLD_RI")));

				ht.put("BEG_STT_NO", ok_util.checkNull(db1.prs.getString("BEG_STT_NO")));
				ht.put("LAST_STT_NO", ok_util.checkNull(db1.prs.getString("LAST_STT_NO")));
				ht.put("DLV_CYC_CD", ok_util.checkNull(db1.prs.getString("DLV_CYC_CD")));
				ht.put("TRS_YMD", ok_util.checkNull(db1.prs.getString("TRS_YMD")));
				ht.put("SHIP_FARE", ok_util.checkNull(db1.prs.getString("SHIP_FARE")));
				
				ht.put("AIR_FARE", ok_util.checkNull(db1.prs.getString("AIR_FARE")));
				ht.put("DLV_BRNSHP_NM", ok_util.checkNull(db1.prs.getString("DLV_BRNSHP_NM")));
				al_data.add(ht);
			}
			log.Write("HD_ZIP_DEMON", "HD SEARCH END");
			
			String str_ZIP_NO = "";
			String str_SVC_CD = "";
			String str_TML_CD = "";
			String str_TML_NM = "";
			String str_FILT_CD = "";
			
			String str_CITY_DO = "";
			String str_CITY_GUN_GU = "";
			String str_GU = "";
			String str_EUP_MUIN_DONG = "";
			String str_BLD_RI = "";
			
			String str_BEG_STT_NO = "";
			String str_LAST_STT_NO = "";
			String str_DLV_CYC_CD = "";
			String str_TRS_YMD = "";
			String str_SHIP_FARE = "";
			
			String str_AIR_FARE = "";
			String str_DLV_BRNSHP_NM = "";
			String str_reg_man = "";
			
			//기존에 있는 정보는 삭제하는 쿼리
			String str_qry1 = " DELETE VMROKOR_ZIP_HD ";	
			
			//mets insert쿼리
			String str_qry2 = " insert into VMROKOR_ZIP_HD(ZIP_NO, SVC_CD, TML_CD, TML_NM, FILT_CD, "
							+ "	                           CITY_DO, CITY_GUN_GU, GU, EUP_MUIN_DONG, BLD_RI, "
							+ "	                           BEG_STT_NO, LAST_STT_NO, DLV_CYC_CD, TRS_YMD, SHIP_FARE, " 
							+ "	                           AIR_FARE, DLV_BRNSHP_NM, reg_dati, reg_man) " 
							+ "	values(?,?,?,?,?, "
							+ "	       ?,?,?,?,?, "
							+ "	       ?,?,?,?,?, "
							+ "	       ?,?,sysdate,?) ";		
			
			//기존에 있는 데이터는 삭제 
			db.prepareStatement(str_qry1);
			if(db.PexecuteUpdate() < 0 ){
				log.Write("HD_ZIP_DEMON", "METS DATA DELETE ERROR");
				throw new Exception();
			}
			
			//insert 구문 pre
			db.prepareStatement2(str_qry2);
			
			log.Write("HD_ZIP_DEMON", "METS HISTORY DATA DELETE");
			log.Write("HD_ZIP_DEMON", "METS DATA INPUT START");
			
			for(int i=0;i<al_data.size();i++) {
				Hashtable ht = (Hashtable) al_data.get(i);			
				str_ZIP_NO = String.valueOf(ht.get("ZIP_NO"));
				str_SVC_CD = String.valueOf(ht.get("SVC_CD"));
				str_TML_CD = String.valueOf(ht.get("TML_CD"));
				str_TML_NM = String.valueOf(ht.get("TML_NM"));
				str_FILT_CD = String.valueOf(ht.get("FILT_CD"));
				
				str_CITY_DO = String.valueOf(ht.get("CITY_DO"));
				str_CITY_GUN_GU = String.valueOf(ht.get("CITY_GUN_GU"));
				str_GU = String.valueOf(ht.get("GU"));
				str_EUP_MUIN_DONG = String.valueOf(ht.get("EUP_MUIN_DONG"));
				str_BLD_RI = String.valueOf(ht.get("BLD_RI"));
				
				str_BEG_STT_NO = String.valueOf(ht.get("BEG_STT_NO"));
				str_LAST_STT_NO = String.valueOf(ht.get("LAST_STT_NO"));
				str_DLV_CYC_CD = String.valueOf(ht.get("DLV_CYC_CD"));
				str_TRS_YMD = String.valueOf(ht.get("TRS_YMD"));
				str_SHIP_FARE = String.valueOf(ht.get("SHIP_FARE"));
				
				str_AIR_FARE = String.valueOf(ht.get("AIR_FARE"));
				str_DLV_BRNSHP_NM = String.valueOf(ht.get("DLV_BRNSHP_NM"));
				str_reg_man = "DEMON INSERT";
				
				db.PsetString2(1, str_ZIP_NO);
				db.PsetString2(2, str_SVC_CD);
				db.PsetString2(3, str_TML_CD);
				db.PsetString2(4, str_TML_NM);
				db.PsetString2(5, str_FILT_CD);
				
				db.PsetString2(6, str_CITY_DO);
				db.PsetString2(7, str_CITY_GUN_GU);
				db.PsetString2(8, str_GU);
				db.PsetString2(9, str_EUP_MUIN_DONG);
				db.PsetString2(10, str_BLD_RI);
				
				db.PsetString2(11, str_BEG_STT_NO);
				db.PsetString2(12, str_LAST_STT_NO);
				db.PsetString2(13, str_DLV_CYC_CD);
				db.PsetString2(14, str_TRS_YMD);
				db.PsetString2(15, str_SHIP_FARE);
				
				db.PsetString2(16, str_AIR_FARE);
				db.PsetString2(17, str_DLV_BRNSHP_NM);
				db.PsetString2(18, str_reg_man);
				
				if(db.PexecuteUpdate2() < 0 ){
					log.Write("HD_ZIP_DEMON", "METS INSERT ERROR");
					throw new Exception();
				}
			}
			log.Write("HD_ZIP_DEMON", "METS DATA INSERT END");
			
			db.commit();
			db1.commit();

		}catch(Exception e){
			log.Write("HD_ZIP_DEMON", "Error==>"+e);
			str_Err_flag = "NO";
			try{
				db.rollback();
				db1.rollback();
			}catch(Exception e2){}
		}finally{
			try{	
				String str_qry = "{call sp_send_sms(?,?,?,?,?, ?,?,?)}";
				db.prepareCall(str_qry);
				db.CsetString (1, "010-3189-0333"); // 받는사람       
				db.CsetString (2, "1544-1644"); // 보내는 사람
				if("OK".equals(str_Err_flag)) {
					db.CsetString (3, "HD데몬 데이터전송 완료"); // tra_num
				}else{
					db.CsetString (3, "HD데몬 데이터전송 실패 에러를 확인하세요"); // tra_num
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
					log.Write("HD_ZIP_DEMON", "Error=====>"+e);
				}
			db_disconn();  //DB접속 종료
		}		
	}
	
	public static void main(String args[]) {
		HD_Demon s = new HD_Demon();
		
		
		s.db_proc();  // CJ 데이터 처리		
	}
}






