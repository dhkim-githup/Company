

import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.*;

import comm.DB_Use_Db_Demon;
import comm.mk_log;

public class Demon_skh_main 
{
	private DB_Use_Db_Demon db = new DB_Use_Db_Demon();
	private mk_log log = new mk_log();
	
	private int LOG_FLAG = 1;
	private String prog_nm = "Demon_skh_main.java";
	private String s_cust_id = "jylee";
	private String str_qry = "";
	
	private void Demon_skh_main() {
		
	}
	
	private void db_conn() {
		//db.dbURL = "jdbc:oracle:thin:@192.168.1.211:1521:TESTDB"; // Local 테스트
		//db.dbURL = "jdbc:oracle:thin:@192.168.1.211:1521:TESTDB"; // 서버 테스트
		//db.user_id = "METS_IMSI"; // 테스트
		//db.user_pw = "METS_IMSI"; // 테스트
		//db.dbURL = "jdbc:oracle:thin:@211.43.195.63:1521:NDB10"; // Local 운영
		db.dbURL = "jdbc:oracle:thin:@172.16.1.208:1521:NDB10"; // 서버 운영
		db.user_id = "METS18940G"; //운영
		db.user_pw = "METS25519P58563W"; //운영
		db.DB_Conn();
	}

	private void db_disconn() {
		db.DB_DisConn();
	}
	
	private void cancel_proc() {
		String str_url_result ="", str_first="", str_second="", b_res_msg="";
		URL url=null;
		URLConnection conn = null;
		InputStream in = null;
		BufferedReader reader = null;
		String line = "";
		String str_ords_num="";
		String err_occur = "";
		String b_url = "";//미들웨어 서버 호출 URL
		String str_Err_flag = "OK";
		int i_row_cnt=0;
		try{
			str_qry = " update skh_ords_info "
				+ "	set iv_sure_dati = to_date('20000101','yyyymmdd'), sys_memo = to_char(sysdate,'yyyymmdd')||' - 3일경과 후 포스팅 되지 않은 전표 삭제 by 데몬' "
				+ "	WHERE ords_num = ? "; 		
			db.prepareStatement2(str_qry);
			
			str_qry = " update skh_ords_info "
				+ "	set iv_sure_dati = to_date('20000101','yyyymmdd'), sys_memo = ? "
				+ "	WHERE ords_num = ? "; 		
			db.prepareStatement3(str_qry);
			
			str_qry = "  SELECT ords_num  "
					 + " FROM skh_ords_info  "
 					 + " WHERE iv_sure_dati is null "
					 + " and iv_no is not null "
					 + " and reg_dati > to_date('20100518','yyyymmdd')  "
					 + " and reg_dati < sysdate - 3";	
		
			db.prepareStatement(str_qry);
			db.PexecuteQuery();
			while(db.prs.next()) {
				i_row_cnt++;
				//b_url = "http://219.253.33.229:8082/skholdings/parking.jsp?cust_id="+s_cust_id+"&ords_num=2222&proc_flag=1DC";
				str_ords_num = db.prs.getString("ords_num");
				b_url = "http://192.168.1.201:8082/skholdings/parking.jsp?ords_num="+str_ords_num+"&proc_flag=1DC";
				url=new URL(b_url);
				conn = url.openConnection();
				in=conn.getInputStream() ;
				reader=new BufferedReader(new InputStreamReader(in));
				
				while( (line=reader.readLine()) !=null) {
					str_url_result=str_url_result+line.trim();
				}
				reader.close();
				
				int i_cnt=str_url_result.indexOf("^"); // 구분자위치
				str_first = str_url_result.substring(0,i_cnt).trim(); // 구분자 앞자리 : 0이면 정상
				str_second = str_url_result.substring(i_cnt+1,str_url_result.length()).trim(); // 구분자 뒷자리
				b_res_msg = str_second;
				System.out.println(s_cust_id+"=>["+str_first+"]-["+str_second+"] !!");
				
				str_url_result = "";
				
				log.Write(LOG_FLAG, prog_nm, s_cust_id+"=> Break Point ----------------5 =>"+str_ords_num+"/"+str_first+str_second);
				reader.close();
				
				if("0".equals(str_first)) {
					log.Write(LOG_FLAG, prog_nm, s_cust_id+"=> Break Point ----------------6 =>"+str_ords_num+"/"+str_first+str_second);
					db.PsetString2(1, str_ords_num);
					db.PexecuteUpdate2();
					if(db.ERR_FLAG < 0) { if(err_occur.equals("ERR")); }
					log.Write(LOG_FLAG, prog_nm, s_cust_id+"=> Break Point ----------------6 =>"+db.ERR_FLAG);
				}else{
					log.Write(LOG_FLAG, prog_nm, s_cust_id+"=> Break Point ----------------7 =>"+str_ords_num+"/"+str_first+str_second);
					db.PsetString3(1, str_first+str_second);
					db.PsetString3(2, str_ords_num);
					db.PexecuteUpdate3();
					if(db.ERR_FLAG < 0) { if(err_occur.equals("ERR")); }
				}
				
			}
			db.commit();
		}catch(Exception e){
			try {
				db.rollback();
				str_Err_flag = "NO";
			}catch(Exception ei){}
			log.Write(prog_nm, "Error" +e);
		}finally{
			try{
				if(!"0".equals(i_row_cnt)) {
					str_qry = "{call sp_send_sms(?,?,?,?,?, ?,?,?)}";
					db.prepareCall(str_qry);
					db.CsetString (1, "010-3189-0333"); // 받는사람
					db.CsetString (2, "1544-1644"); // 보내는 사람
					if("OK".equals(str_Err_flag)) {
						db.CsetString (3, i_row_cnt + "건 SKH데몬 데이터전송 완료"); // tra_num
					}else{
						db.CsetString (3, str_ords_num+"SKH데몬 데이터전송 실패 에러를 확인하세요"); // tra_num
					}
					db.CsetString (4, ""); // tra_seq
					db.CsetString (5, ""); // tra_seq
					db.CsetString (6, ""); // tra_seq
					db.cs.registerOutParameter (7, Types.VARCHAR);
					db.cs.registerOutParameter (8, Types.VARCHAR);
					db.Cexecute();
					db.commit();
				}
			}catch(Exception e) {
				try{db.rollback(); }catch(Exception e1){}
				log.Write("SKH데몬", "Error=====>"+e);
			}
		}
	}
	 
	public static void main(String[] args) 
	{
		Demon_skh_main dsm = new Demon_skh_main();
		dsm.db_conn();
		
		dsm.cancel_proc();
		
		dsm.db_disconn();
	}
	
}
