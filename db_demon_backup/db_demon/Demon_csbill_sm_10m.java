import java.io.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import comm.mk_log;

public class Demon_csbill_sm_10m 
{
	public 	Connection conn= null, conn1=null;
	public PreparedStatement ps=null, ps1 =null, ps2=null, ps3=null, ps4=null, ps5=null;
	public ResultSet prs = null, prs1=null, prs2=null, prs3=null, prs4=null, prs5=null;
    public ResultSet rs = null, rs2 = null, rs3 = null;
	public String qry = "";
//	public mk_log mk = new mk_log(); 
/*
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10"; // 서버 운영
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";
*/
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10"; // 서버 운영
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";
	public String driver="oracle.jdbc.driver.OracleDriver";

	//public String ms_dbURL="jdbc:sqlserver://121.254.231.228:1433;SelectMethod=cursor;DatabaseName=csbill_scm"; // 서버 운영
	public String ms_dbURL="jdbc:sqlserver://114.108.138.103:1433;SelectMethod=cursor;DatabaseName=csbill_scm"; //  2012.07.19 shpark csbill ip change
	public String ms_user_id="mrok";
	public String ms_user_pw="mrok";
	public String ms_driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";	                         
								
	public mk_log log = new mk_log();
	public int log_flag = 0;
	public String prgm_nm = "csbill demon speedmall 10 minutes";
	
	public Demon_csbill_sm_10m() {
		String acco_num = "";
		String date="";
		int temp=0;
		ArrayList al_sel_list = new ArrayList();
		ArrayList al_data = new ArrayList();
		Hashtable ht;
		try
		{
			/*ORACLE에 붙어서 전자 세금계산서정보를 얻어 온다.*/		
			Class.forName(driver);
			conn=DriverManager.getConnection(dbURL,user_id,user_pw);
			conn.setAutoCommit(false); 

			ps4 = conn.prepareStatement(f_sql("pre_sel"));	//mro master 정보 받아오는 쿼리
			prs4 = ps4.executeQuery();

			while(prs4.next()) {		
				Hashtable ht_temp = new Hashtable();
				ht_temp.put("HEAD_MANA_IDEN", prs4.getString("HEAD_MANA_IDEN"));
				al_sel_list.add(ht_temp);
			}
			if(al_sel_list.size()>0) {
				log.Write(log_flag,prgm_nm,"Start-처리를 시작합니다.");
				ht = new Hashtable();

				
				ps = conn.prepareStatement(f_sql("sel_master"));	//mro master 정보 받아오는 쿼리
				ps1 = conn.prepareStatement(f_sql("sel_item"));		//mro item 정보 받아오는 쿼리
				ps5 = conn.prepareStatement(f_sql("post_udp"));		//전송 후 엠알오 mro_read값 변경

				/*MS-SQL에 붙어서 전자 세금계산서를 전송한다.*/		
				Class.forName(ms_driver);
				conn1=DriverManager.getConnection(ms_dbURL,ms_user_id,ms_user_pw);
				conn1.setAutoCommit(false); 

				ps2 = conn1.prepareStatement(f_sql("ins_master"));	//csbill master 정보 받아오는 쿼리
				ps3 = conn1.prepareStatement(f_sql("ins_item"));		//csbill item 정보 받아오는 쿼리

				for(int i=0;i<al_sel_list.size();i++) {
					try	{
						
					
						Hashtable ht_sel_list = (Hashtable) al_sel_list.get(i);
						acco_num = ht_sel_list.get("HEAD_MANA_IDEN").toString();
						ps.setString(1,	acco_num		);//0
						prs = ps.executeQuery();

						if(prs.next()) {
							ht.put("MANA_SEBU_TYPE",		checkNull(prs.getString("MANA_SEBU_TYPE")));
							ht.put("MANA_DATA_TYPE",		checkNull(prs.getString("MANA_DATA_TYPE")));

							ht.put("MANA_PROC_STAT",		checkNull(prs.getString("MANA_PROC_STAT")));
							ht.put("MANA_SEND_CHID",		checkNull(prs.getString("MANA_SEND_CHID")));
							ht.put("MANA_RECV_CHID",		checkNull(prs.getString("MANA_RECV_CHID")));

							ht.put("HEAD_DOCU_DATE",		checkNull(prs.getString("HEAD_DOCU_DATE")));
							ht.put("HEAD_BILL_TYPE",		checkNull(prs.getString("HEAD_BILL_TYPE")));

							ht.put("HEAD_DEMA_INDI",		checkNull(prs.getString("HEAD_DEMA_INDI")));
							ht.put("HEAD_DESC_TEXT",		checkNull(prs.getString("HEAD_DESC_TEXT")));
							ht.put("SUPP_PART_IDEN",		checkNull(prs.getString("SUPP_PART_IDEN")));

							ht.put("SUPP_ORGN_NAME",		checkNull(prs.getString("SUPP_ORGN_NAME")));
							ht.put("SUPP_PART_NAME",		checkNull(prs.getString("SUPP_PART_NAME")));
							ht.put("SUPP_ADDR_TEXT",		checkNull(prs.getString("SUPP_ADDR_TEXT")));

							ht.put("SUPP_BUSI_TYPE",		checkNull(prs.getString("SUPP_BUSI_TYPE")));
							ht.put("SUPP_BUSI_CLAS",		checkNull(prs.getString("SUPP_BUSI_CLAS")));
							ht.put("SUPP_CONT_DEPT",		checkNull(prs.getString("SUPP_CONT_DEPT")));

							ht.put("SUPP_PERS_NAME",		checkNull(prs.getString("SUPP_PERS_NAME")));
							ht.put("SUPP_PERS_TELE",		checkNull(prs.getString("SUPP_PERS_TELE")));
							ht.put("SUPP_PERS_MAIL",		checkNull(prs.getString("SUPP_PERS_MAIL")));
							
							ht.put("BUYE_PART_IDEN",		checkNull(prs.getString("BUYE_PART_IDEN")));
							ht.put("BUYE_ORGN_NAME",		checkNull(prs.getString("BUYE_ORGN_NAME")));
							ht.put("BUYE_PART_NAME",		checkNull(prs.getString("BUYE_PART_NAME")));
							
							ht.put("BUYE_ADDR_TEXT",		checkNull(prs.getString("BUYE_ADDR_TEXT")));
							ht.put("BUYE_BUSI_TYPE",		checkNull(prs.getString("BUYE_BUSI_TYPE")));
							ht.put("BUYE_BUSI_CLAS",		checkNull(prs.getString("BUYE_BUSI_CLAS")));

							ht.put("BUYE_CONT_DEPT",		checkNull(prs.getString("BUYE_CONT_DEPT")));
							ht.put("BUYE_PERS_NAME",		checkNull(prs.getString("BUYE_PERS_NAME")));
							ht.put("BUYE_PERS_TELE",		checkNull(prs.getString("BUYE_PERS_TELE")));

							ht.put("BUYE_PERS_MAIL",		checkNull(prs.getString("BUYE_PERS_MAIL")));
							ht.put("SUMM_CHAR_AMOU",		checkNull(prs.getString("SUMM_CHAR_AMOU")));
							ht.put("SUMM_TAX0_AMOU",		checkNull(prs.getString("SUMM_TAX0_AMOU")));

							ht.put("SUMM_TOTA_AMOU",		checkNull(prs.getString("SUMM_TOTA_AMOU")));
							ht.put("MANA_OPPO_FLAG",		checkNull(prs.getString("MANA_OPPO_FLAG")));

							ht.put("REG_ID",		checkNull(prs.getString("REG_ID")));
							ht.put("REG_DATE",		checkNull(prs.getString("REG_DATE")));

							ht.put("SEND_YN",		checkNull(prs.getString("SEND_YN")));
							ht.put("MRO_SENDYN",		checkNull(prs.getString("MRO_SENDYN")));
							ht.put("INF_SEND_MAN",		checkNull(prs.getString("INF_SEND_MAN")));

							ht.put("INF_SEND_DATI",		checkNull(prs.getString("INF_SEND_DATI")));
							ht.put("INF_STAT_MEMO",		checkNull(prs.getString("INF_STAT_MEMO")));
							
							ht.put("BILL_TRAN_FLAG",		checkNull(prs.getString("BILL_TRAN_FLAG")));
							ht.put("TRAN_FORM_IDEN",		checkNull(prs.getString("TRAN_FORM_IDEN")));
							
							ht.put("HEAD_AMED_CODE",		checkNull(prs.getString("HEAD_AMED_CODE")));
							ht.put("HEAD_AMED_TEXT",		checkNull(prs.getString("HEAD_AMED_TEXT")));
							ht.put("SUPP_MPOB_IDEN",		checkNull(prs.getString("SUPP_MPOB_IDEN")));

							ht.put("BUYE_IDEN_TYPE",		checkNull(prs.getString("BUYE_IDEN_TYPE")));
							ht.put("BUYE_MPOB_IDEN",		checkNull(prs.getString("BUYE_MPOB_IDEN")));
							ht.put("BUY2_CONT_DEPT",		checkNull(prs.getString("BUY2_CONT_DEPT")));
							
							ht.put("BUY2_PERS_NAME",		checkNull(prs.getString("BUY2_PERS_NAME")));
							ht.put("BUY2_PERS_TELE",		checkNull(prs.getString("BUY2_PERS_TELE")));
							ht.put("BUY2_PERS_MAIL",		checkNull(prs.getString("BUY2_PERS_MAIL")));

							ht.put("HEAD_DES2_TEXT",		checkNull(prs.getString("HEAD_DES2_TEXT")));
							ht.put("BIZM_STND_VERS",		checkNull(prs.getString("BIZM_STND_VERS")));
						}

						ps1.setString(1,	acco_num		);//0
						prs1 = ps1.executeQuery();

						if(prs1.next()) {
							ht.put("HEAD_MANA_IDEN",		checkNull(prs1.getString("HEAD_MANA_IDEN")));//1
							ht.put("LIST_SEQU_IDEN",		checkNull(prs1.getString("LIST_SEQU_IDEN")));//2
							ht.put("LIST_TRAN_DATE",		checkNull(prs1.getString("LIST_TRAN_DATE")));//3

							ht.put("LIST_ITEM_NAME",		checkNull(prs1.getString("LIST_ITEM_NAME")));//4
							ht.put("LIST_DESC_TEXT",		checkNull(prs1.getString("LIST_DESC_TEXT")));//5

							ht.put("LIST_ITEM_AMOU",		checkNull(prs1.getString("LIST_ITEM_AMOU")));//6

							ht.put("LIST_TOTA_AMOU",		checkNull(prs1.getString("LIST_TOTA_AMOU")));//7
							ht.put("LIST_TAX0_AMOU",		checkNull(prs1.getString("LIST_TAX0_AMOU")));//8
						}

						ps2.setString(1,	(String)	ht.get("HEAD_MANA_IDEN")		);//0
						ps2.setString(2,	(String)	ht.get("MANA_SEBU_TYPE")		);//1
						ps2.setString(3,	(String)	ht.get("MANA_DATA_TYPE")		);//2

						ps2.setString(4,	(String)	ht.get("MANA_PROC_STAT")		);//3
						ps2.setString(5,	(String)	ht.get("MANA_SEND_CHID")		);//4
						ps2.setString(6,	(String)	ht.get("MANA_RECV_CHID")		);//5

						ps2.setString(7,	(String)	ht.get("HEAD_DOCU_DATE")		);//6
						ps2.setString(8,	(String)	ht.get("HEAD_BILL_TYPE")		);//7

						ps2.setString(9,	(String)	ht.get("HEAD_DEMA_INDI")		);//8
						ps2.setString(10,	(String)	ht.get("HEAD_DESC_TEXT")		);//9
						ps2.setString(11,	(String)	ht.get("SUPP_PART_IDEN")		);//10

						ps2.setString(12,	(String)	ht.get("SUPP_ORGN_NAME")		);//11
						ps2.setString(13,	(String)	ht.get("SUPP_PART_NAME")		);//12
						ps2.setString(14,	(String)	ht.get("SUPP_ADDR_TEXT")		);//13

						ps2.setString(15,	(String)	ht.get("SUPP_BUSI_TYPE")		);//14
						ps2.setString(16,	(String)	ht.get("SUPP_BUSI_CLAS")		);//15
						ps2.setString(17,	(String)	ht.get("SUPP_CONT_DEPT")		);//16

						ps2.setString(18,	(String)	ht.get("SUPP_PERS_NAME")		);//17
						ps2.setString(19,	(String)	ht.get("SUPP_PERS_TELE")		);//18
						ps2.setString(20,	(String)	ht.get("SUPP_PERS_MAIL")		);//19

						ps2.setString(21,	(String)	ht.get("BUYE_PART_IDEN")		);//20
						ps2.setString(22,	(String)	ht.get("BUYE_ORGN_NAME")		);//21
						ps2.setString(23,	(String)	ht.get("BUYE_PART_NAME")		);//22

						ps2.setString(24,	(String)	ht.get("BUYE_ADDR_TEXT")		);//23
						ps2.setString(25,	(String)	ht.get("BUYE_BUSI_TYPE")		);//24
						ps2.setString(26,	(String)	ht.get("BUYE_BUSI_CLAS")		);//25

						ps2.setString(27,	(String)	ht.get("BUYE_CONT_DEPT")		);//26
						ps2.setString(28,	(String)	ht.get("BUYE_PERS_NAME")		);//27
						ps2.setString(29,	(String)	ht.get("BUYE_PERS_TELE")		);//28

						ps2.setString(30,	(String)	ht.get("BUYE_PERS_MAIL")		);//29
						ps2.setString(31,	(String)	ht.get("SUMM_CHAR_AMOU")		);//30
						ps2.setString(32,	(String)	ht.get("SUMM_TAX0_AMOU")		);//31

						ps2.setString(33,	(String)	ht.get("SUMM_TOTA_AMOU")		);//32
						ps2.setString(34,	(String)	ht.get("MANA_OPPO_FLAG")		);//33

						ps2.setString(35,	(String)	ht.get("REG_ID")				);//34
						ps2.setString(36,	(String)	ht.get("REG_DATE")				);//35

						ps2.setString(37,	(String)	ht.get("SEND_YN")				);//36
						ps2.setString(38,	(String)	ht.get("MRO_SENDYN")			);//37

						//ps2.setString(,	(String)	ht.get("INF_SEND_MAN")		);//38
						//ps2.setString(,	(String)	ht.get("INF_SEND_DATI")		);//39
						//ps2.setString(,	(String)	ht.get("INF_STAT_MEMO")		);//40

						ps2.setString(39,	(String)	ht.get("BILL_TRAN_FLAG")		);//41
						ps2.setString(40,	(String)	ht.get("TRAN_FORM_IDEN")		);//42

						ps2.setString(41,	(String)	ht.get("HEAD_AMED_CODE")		);//43
						ps2.setString(42,	(String)	ht.get("HEAD_AMED_TEXT")		);//44
						ps2.setString(43,	(String)	ht.get("SUPP_MPOB_IDEN")		);//45

						ps2.setString(44,	(String)	ht.get("BUYE_IDEN_TYPE")		);//46
						ps2.setString(45,	(String)	ht.get("BUYE_MPOB_IDEN")		);//47
						ps2.setString(46,	(String)	ht.get("BUY2_CONT_DEPT")		);//48

						ps2.setString(47,	(String)	ht.get("BUY2_PERS_NAME")		);//49
						ps2.setString(48,	(String)	ht.get("BUY2_PERS_TELE")		);//50
						ps2.setString(49,	(String)	ht.get("BUY2_PERS_MAIL")		);//51

						ps2.setString(50,	(String)	ht.get("HEAD_DES2_TEXT")		);//52
						ps2.setString(51,	(String)	"Y"								);//52-1 //CSBILL전송여부
						ps2.setString(52,	(String)	ht.get("BIZM_STND_VERS")		);//53

						ps2.executeUpdate();

						ps3.setString(1,	(String)	ht.get("HEAD_MANA_IDEN")		);//1
						ps3.setString(2,	(String)	ht.get("LIST_SEQU_IDEN")		);//2
						ps3.setString(3,	(String)	ht.get("LIST_TRAN_DATE")		);//3

						ps3.setString(4,	(String)	ht.get("LIST_ITEM_NAME")		);//4
						ps3.setString(5,	(String)	ht.get("LIST_DESC_TEXT")		);//5

						ps3.setString(6,	(String)	ht.get("LIST_ITEM_AMOU")		);//6

						ps3.setString(7,	(String)	ht.get("LIST_TOTA_AMOU")		);//7
						ps3.setString(8,	(String)	ht.get("LIST_TAX0_AMOU")		);//8

						ps3.executeUpdate();

						ps5.setString(1,	acco_num		);//0
						temp = ps5.executeUpdate();
						System.out.println(temp);
						conn.commit();
						conn1.commit();
					}catch (Exception e){
						try{
							conn.rollback(); 
							conn1.rollback(); 
						} catch (Exception ei){}
						log.Write(log_flag,prgm_nm,"에러 발생 : "+acco_num+":Error=> "+ e);
					}
				}
				
				conn.commit();
				conn1.commit();
			}else{
				System.out.println("Data Not Found");
				log.Write(log_flag,prgm_nm,"Data Not Found");
			}
			log.Write(log_flag,prgm_nm,"End-처리가 완료 되었습니다. ");
		}
		catch (Exception e){
			try{
				conn.rollback(); 
				conn1.rollback(); 
			} catch (Exception ei){}
			log.Write(log_flag,prgm_nm,"에러 발생 : Error=> "+ e);
		}finally{
			try{ 
				conn.close(); 
				conn1.close();
				if(ps != null){ ps.close(); }
				if(ps1 != null){ ps1.close(); }
				if(ps2 != null){ ps2.close(); }
				if(ps3 != null){ ps3.close(); }
				if(ps4 != null){ ps4.close(); }
				if(ps5 != null){ ps5.close(); }
				if(prs != null){ prs.close(); }
				if(prs1 != null){ prs1.close(); }
				if(prs2 != null){ prs2.close(); }
				if(prs3 != null){ prs3.close(); }
				if(prs4 != null){ prs4.close(); }
				if(prs5 != null){ prs5.close(); }
				if(rs != null){ rs.close(); }
				if(rs2 != null){ rs2.close(); }
			}catch (Exception ei){}
		}
	}

	private String f_sql(String s) {
		String r_qry = "";
		if ("sel_master".equals(s)){
			r_qry = " select  "
				+" MANA_SEBU_TYPE, "//1
				+" MANA_DATA_TYPE, "//2
				 
				+" MANA_PROC_STAT,  "//3
				+" MANA_SEND_CHID, "//4
				+" MANA_RECV_CHID, "//5

				+" HEAD_DOCU_DATE, "//6
				+" HEAD_BILL_TYPE, "//7

				+" HEAD_DEMA_INDI, "//8
				+" HEAD_DESC_TEXT, "//9
				+" SUPP_PART_IDEN, "//10

				+" SUPP_ORGN_NAME, "//11
				+" SUPP_PART_NAME, "//12
				+" SUPP_ADDR_TEXT, "//13

				+" SUPP_BUSI_TYPE, "//14
				+" SUPP_BUSI_CLAS, "//15
				+" SUPP_CONT_DEPT, "//16

				+" SUPP_PERS_NAME, "//17
				+" SUPP_PERS_TELE, "//18
				+" SUPP_PERS_MAIL, "//19

				+" BUYE_PART_IDEN, "//20
				+" BUYE_ORGN_NAME, "//21
				+" BUYE_PART_NAME, "//22

				+" BUYE_ADDR_TEXT, "//23
				+" BUYE_BUSI_TYPE, "//24
				+" BUYE_BUSI_CLAS, "//25

				+" BUYE_CONT_DEPT, "//26
				+" BUYE_PERS_NAME, "//27
				+" BUYE_PERS_TELE, "//28

				+" BUYE_PERS_MAIL, "//29
				+" SUMM_CHAR_AMOU, "//30
				+" SUMM_TAX0_AMOU, "//31

				+" SUMM_TOTA_AMOU, "//32
				+" MANA_OPPO_FLAG, "//33

				+" REG_ID, "//34
				+" sysdate REG_DATE, "//35

				+" SEND_YN, "//36
				+" MRO_SENDYN, "//37
				+" INF_SEND_MAN, "//38

				+" INF_SEND_DATI, "//39
				+" INF_STAT_MEMO, "//40

				+" BILL_TRAN_FLAG, "//41
				+" TRAN_FORM_IDEN, "//42

				+" HEAD_AMED_CODE, "//43
				+" HEAD_AMED_TEXT, "//44
				+" SUPP_MPOB_IDEN, "//45

				+" BUYE_IDEN_TYPE, "//46
				+" BUYE_MPOB_IDEN, "//47
				+" BUY2_CONT_DEPT, "//48

				+" BUY2_PERS_NAME, "//49
				+" BUY2_PERS_TELE, "//50
				+" BUY2_PERS_MAIL, "//51

				+" HEAD_DES2_TEXT, "//52
				+" BIZM_STND_VERS "//53

				+" from dti_mrok_master "
				+" where head_mana_iden = ? ";
		}else if ("sel_item".equals(s)) {
			r_qry = " SELECT " 
				+"     HEAD_MANA_IDEN, LIST_SEQU_IDEN, LIST_TRAN_DATE, "
				+"     LIST_ITEM_NAME, LIST_DESC_TEXT, "
				+"     LIST_ITEM_AMOU, "
				+"     LIST_TOTA_AMOU, LIST_TAX0_AMOU "
				+" from dti_mrok_item " 
				+" where head_mana_iden = ? ";
		}else if ("ins_master".equals(s)) {
			r_qry = "insert into dti_mrok_master "
							 +           "(HEAD_MANA_IDEN, MANA_SEBU_TYPE, MANA_DATA_TYPE, " //0,1,2
							 +           "MANA_PROC_STAT, MANA_SEND_CHID, MANA_RECV_CHID, "//3,4,5
							 +           "HEAD_DOCU_DATE, HEAD_BILL_TYPE, " //6,7
							 +           "HEAD_DEMA_INDI, HEAD_DESC_TEXT, SUPP_PART_IDEN, " //8,9,10
							 +           "SUPP_ORGN_NAME, SUPP_PART_NAME, SUPP_ADDR_TEXT, " //11,12,13
							 +           "SUPP_BUSI_TYPE, SUPP_BUSI_CLAS, SUPP_CONT_DEPT, " //14,15,16
							 +           "SUPP_PERS_NAME, SUPP_PERS_TELE, SUPP_PERS_MAIL, " //17,18,19
							 +           "BUYE_PART_IDEN, BUYE_ORGN_NAME, BUYE_PART_NAME, " //20,21,22
							 +           "BUYE_ADDR_TEXT, BUYE_BUSI_TYPE, BUYE_BUSI_CLAS, " //23,24,25
							 +           "BUYE_CONT_DEPT, BUYE_PERS_NAME, BUYE_PERS_TELE, " //26,27,28
							 +           "BUYE_PERS_MAIL, SUMM_CHAR_AMOU, SUMM_TAX0_AMOU, " //29,30,31
							 +           "SUMM_TOTA_AMOU, MANA_OPPO_FLAG, " //32,33
							 +           "REG_ID, REG_DATE, " //34,35
							 +           "SEND_YN, MRO_SENDYN,  " //36,37
							 +           "BILL_TRAN_FLAG, TRAN_FORM_IDEN, " //38,39
							 +           "HEAD_AMED_CODE, HEAD_AMED_TEXT, SUPP_MPOB_IDEN, " //40,41,42 //3.0신규추가분
							 +           "BUYE_IDEN_TYPE, BUYE_MPOB_IDEN, BUY2_CONT_DEPT, " //43,44,45
							 +           "BUY2_PERS_NAME, BUY2_PERS_TELE, BUY2_PERS_MAIL, " //46,47,48
							 +           "HEAD_DES2_TEXT, INSERT_YN, BIZM_STND_VERS) " //49, 50, 51
							 + " values(?,?,?, ?,?,?, ?,?, ?,?,?,  "//0,1,2, 3,4,5, 6,7, 8,9,10
								+" ?,?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?, "//11,12,13 14,15,16, 17,18,19, 20,21,22 23,24,25
								+" ?,?,?, ?,?,?, ?,?, ?,?, ?,?,  " //26,27,28 29,30,31 32,33 34,35 36,37
								+" ?,?, ?,?,?, ?,?,?, ?,?,?, ?,?,?)"; //38,39, 40,41,42 43,44,45, 46,47,48 49,50,51
		}else if ("ins_item".equals(s)) {
			r_qry = " insert into dti_mrok_item "
				+			" (HEAD_MANA_IDEN, LIST_SEQU_IDEN, LIST_TRAN_DATE , "
				+			" LIST_ITEM_NAME, LIST_DESC_TEXT, "
				+			" LIST_ITEM_AMOU, "
				+			" LIST_TOTA_AMOU, LIST_TAX0_AMOU) "
				+			" values (?,?,?, ?,?, ?, ?,?) ";
		}else if ("pre_sel".equals(s)) {
			r_qry = " SELECT HEAD_MANA_IDEN "
				+ "	FROM dti_mrok_master "
				+ "	WHERE mro_read = '00' ";
		}else if ("post_udp".equals(s)) {
			r_qry = " update dti_mrok_master "
				+ "	set mro_read = '01' "
				+ "	WHERE  head_mana_iden = ? ";
		}

		return r_qry;
	}

	private String checkNull(String s) {
		if (s == null)
			return "";
		else
			return trim(s);
	}

	public static String trim(String s) {
		int i = 0;
		char ac[] = s.toCharArray();
		int j = ac.length;
		int k;
		for (k = j; i < k && (ac[i] <= ' ' || ac[i] == '\u3000'); i++)
			;
		for (; i < k && (ac[k - 1] <= ' ' || ac[k - 1] == '\u3000'); k--)
			;
		return i <= 0 && k >= j ? s : s.substring(i, k);
	}

	public static void main(String args[]) {
		Demon_csbill_sm_10m cb = new Demon_csbill_sm_10m();
	}
}
