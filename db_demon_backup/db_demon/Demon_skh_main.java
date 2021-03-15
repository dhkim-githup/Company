

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
		//db.dbURL = "jdbc:oracle:thin:@192.168.1.211:1521:TESTDB"; // Local �׽�Ʈ
		//db.dbURL = "jdbc:oracle:thin:@192.168.1.211:1521:TESTDB"; // ���� �׽�Ʈ
		//db.user_id = "METS_IMSI"; // �׽�Ʈ
		//db.user_pw = "METS_IMSI"; // �׽�Ʈ
		//db.dbURL = "jdbc:oracle:thin:@211.43.195.63:1521:NDB10"; // Local �
		db.dbURL = "jdbc:oracle:thin:@172.16.1.208:1521:NDB10"; // ���� �
		db.user_id = "METS18940G"; //�
		db.user_pw = "METS25519P58563W"; //�
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
		String b_url = "";//�̵���� ���� ȣ�� URL
		String str_Err_flag = "OK";
		int i_row_cnt=0;
		try{
			str_qry = " update skh_ords_info "
				+ "	set iv_sure_dati = to_date('20000101','yyyymmdd'), sys_memo = to_char(sysdate,'yyyymmdd')||' - 3�ϰ�� �� ������ ���� ���� ��ǥ ���� by ����' "
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
				
				int i_cnt=str_url_result.indexOf("^"); // ��������ġ
				str_first = str_url_result.substring(0,i_cnt).trim(); // ������ ���ڸ� : 0�̸� ����
				str_second = str_url_result.substring(i_cnt+1,str_url_result.length()).trim(); // ������ ���ڸ�
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
					db.CsetString (1, "010-3189-0333"); // �޴»��
					db.CsetString (2, "1544-1644"); // ������ ���
					if("OK".equals(str_Err_flag)) {
						db.CsetString (3, i_row_cnt + "�� SKH���� ���������� �Ϸ�"); // tra_num
					}else{
						db.CsetString (3, str_ords_num+"SKH���� ���������� ���� ������ Ȯ���ϼ���"); // tra_num
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
				log.Write("SKH����", "Error=====>"+e);
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
