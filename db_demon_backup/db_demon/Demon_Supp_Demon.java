import comm.DB_Use_Db_Demon;
import comm.mk_log;

import java.util.*;
import java.sql.*;

public class Demon_Supp_Demon {

	private DB_Use_Db_Demon db = new DB_Use_Db_Demon();
	private DB_Use_Db_Demon asp = new DB_Use_Db_Demon();
//	private mk_log log = new mk_log();
	String err_occur=null, ERR_MSG="";
	static Connection mdb;
	static Statement mstmt;
	static PreparedStatement pmstmt;
	static ResultSet mrs;
	
	
	public Demon_Supp_Demon(){}
	
	private void db_conn() throws SQLException{
		/*db.dbURL = "jdbc:oracle:thin:@172.16.1.224:1521:TESTDB"; // Local 테스트
		db.user_id = "mets_imsi"; // 테스트
		db.user_pw = "mets_imsi"; // 테스트
		*/
		
		db.DB_Conn();	
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");			
		}catch(ClassNotFoundException ce){
			System.out.println("err");
			ce.printStackTrace();
		}
		//String mdbURL = "jdbc:sqlserver://interface.officedepot.co.kr:1433;";
		String mdbURL = "jdbc:sqlserver://121.254.130.140:1433;";
		String muser_id = "happynarae";
		String muser_pw = "happynarae";
		try {
			mdb = DriverManager.getConnection(mdbURL,muser_id,muser_pw);
			mdb.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mstmt = mdb.createStatement();
						
	}
	public boolean In_Db(comm.DB_Use_Db_Demon db,  String to_cust, String fr_cust, String content, String m_kind, String m_type, String origin,  String info ) {
		 String flag = "", b_msg = "";
		 boolean TorF = true;
		 CallableStatement               cStmt_scoprd = null; // 프로시져 호출시 사용한다.

		try{
			cStmt_scoprd =db.conn.prepareCall("{call P_MAKE_SEND_MAIL.In_Db(?,?,?,?,?,?,?,?)}");
	        cStmt_scoprd.setString(1, to_cust);
	        cStmt_scoprd.setString(2, fr_cust);
	        cStmt_scoprd.setString(3, content);
	        cStmt_scoprd.setString(4, m_kind);
	        cStmt_scoprd.setString(5, m_type);
	        cStmt_scoprd.setString(6, origin );
	        cStmt_scoprd.setString(7, info );
	        cStmt_scoprd.registerOutParameter(8, Types.VARCHAR);

	        cStmt_scoprd.execute();

	        flag = (String)cStmt_scoprd.getObject(8);
			flag = flag.trim();

			System.out.println( "Flag." + flag) ; 
			b_msg = "m_kind=["+m_kind+"] m_type=["+m_type+"] to_cust=["+to_cust+"] fr_cust=["+fr_cust+"] ";
			if ( flag.equals("NO")){
				TorF = false;
				//log.Write("M_Indb.java(BEAN)", "MAIL_IN_NO => "+b_msg);
			} else {
				//log.Write("M_Indb.java(BEAN)", "MAIL_IN_YES => "+b_msg);
			}
		}catch(Exception e){
				System.out.println( "1.에러가 발생했습니다. 에러사유 " + e) ;
				//log.Write("M_Indb.java(BEAN)", "MAIL_IN_ERR=>"+to_cust+"-"+fr_cust+"-"+m_kind+"-"+b_msg + e);
				TorF = false;
			}
		  return TorF;
		}
	
	private void db_proc() throws SQLException{//수주처리
		db_conn();  // DB연결
		String qry2="",qry2_ords_num="";
		int qry2_list_num=0;
		try{
			
		    String qry = " select                      ORDS_NUM, LIST_NUM,type_flag,return_flag, ORD_DATI, HAN_NM, PRD_CD, CUST_PRD_CD, SUPP_PLIS_CD, SHRT_DOC, ADDR, UNIT, CNT, BUY_PRI, UPRI, MEMO, ORD_MEMO, BALJU_MEMO, RFQ_DIVISION_MAN, CUST_NM, CUST_ID_ENPRI_NM, COP_NM, THIS_BSHOP_NM, ACEP_CUST_NM, HRANK_OGN_NM, OGN_HAN_NM, TEL_NUM, MCOM_NUM, ACEP_MAN, P_MDL_NM, DELI_DMND_DATI, DELI_REQ_DAY, DELI_MTD, SBUSI_NUM, SCOP_NM, SCEO_NM, SADDR, STEL, SFAX, LOCATION, ACEPNM1, ACEPNM2, MST_CD, STRE_NM, TAX_YN, TRANS, IS_DS, TRA_INFO_FLAG, TRA_PRI_SHOW, ADD_PAGE,zip_code,tra_type,b_acep_man,b_ords_num,b_po_num,b_cust_ord_num, REG_MAN, to_char(REG_DATE,'yyyy-mm-dd hh24:mi:ss') REG_DATE,b_order_type from inf_supp_suju@L_INF_OFFICEDEPOT where send_state = '000' AND sco_cd = 'A54140' ";
	    	qry2 = " update inf_supp_suju@L_INF_OFFICEDEPOT set send_state = ?,send_date = sysdate,sys_memo=? where sco_cd = 'A54140' and ords_num = ? and list_num = ? ";
	    	db.prepareStatement2(qry2);
		    String mqry = " insert into SK_HAPPY_ORDER(ORDS_NUM, LIST_NUM,type_flag,return_flag, ORD_DATI, HAN_NM, PRD_CD, CUST_PRD_CD, SUPP_PLIS_CD, SHRT_DOC, ADDR, UNIT, CNT, BUY_PRI, UPRI, MEMO, ORD_MEMO, BALJU_MEMO, RFQ_DIVISION_MAN, CUST_NM, CUST_ID_ENPRI_NM, COP_NM, THIS_BSHOP_NM, ACEP_CUST_NM, HRANK_OGN_NM, OGN_HAN_NM, TEL_NUM, MCOM_NUM, ACEP_MAN, P_MDL_NM, DELI_DMND_DATI, DELI_REQ_DAY, DELI_MTD, SBUSI_NUM, SCOP_NM, SCEO_NM, SADDR, STEL, SFAX, LOCATION, ACEPNM1, ACEPNM2, MST_CD, STRE_NM, TAX_YN, TRANS, IS_DS, TRA_INFO_FLAG, TRA_PRI_SHOW, ADD_PAGE,zip_code,tra_type,b_acep_man,b_ords_num,b_po_num,b_cust_ord_num, REG_MAN, REG_DATE,b_order_type) "
		    			+ " values(                           ?,        ?,        ?,          ?,        ?,      ?,      ?,           ?, 		   ?,        ?,    ?,    ?,   ?,       ?,    ?,    ?,        ?,          ?,                ?,       ?,                ?,      ?,             ?,            ?,            ?,          ?,       ?, 		?, 		  ?, 		?, 				?, 			  ?, 		?, 		   ?,       ?,       ?,     ?, 	  ?, 	?, 		  ?, 	   ?, 		?,   	?, 		 ?, 	 ?, 	?, 	   ?, 			  ?, 			?, 		  ?,?       ,?       ,?         ,?         ,?       ,?             ,       ?, convert(datetime,?),?) ";
	    	pmstmt = mdb.prepareStatement(mqry);
		    db.executeQuery(qry);
		    while(db.rs.next()){
		    	int i =1;
		    	pmstmt.setString(i,db.rs.getString(i++) );//ords_num
		    	pmstmt.setInt(i,db.rs.getInt(i++) );//list_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//type_flag
		    	pmstmt.setString(i,db.rs.getString(i++) );//return_flag
		    	pmstmt.setString(i,db.rs.getString(i++) );//ord_dati
		    	pmstmt.setString(i,db.rs.getString(i++) );//han_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//prd_cd
		    	pmstmt.setString(i,db.rs.getString(i++) );//cust_prd_cd
		    	pmstmt.setString(i,db.rs.getString(i++) );//supp_plis_cd
		    	pmstmt.setString(i,db.rs.getString(i++) );//shrt_doc
		    	pmstmt.setString(i,db.rs.getString(i++) );//addr
		    	pmstmt.setString(i,db.rs.getString(i++) );//unit
		    	pmstmt.setString(i,db.rs.getString(i++) );//cnt
		    	pmstmt.setString(i,db.rs.getString(i++) );//buy_pri
		    	pmstmt.setString(i,db.rs.getString(i++) );//upri
		    	pmstmt.setString(i,db.rs.getString(i++) );//memo
		    	pmstmt.setString(i,db.rs.getString(i++) );//ord_memo
		    	pmstmt.setString(i,db.rs.getString(i++) );//balju_memo
		    	pmstmt.setString(i,db.rs.getString(i++) );//rfq_division_man
		    	pmstmt.setString(i,db.rs.getString(i++) );//cust_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//CUST_ID_ENPRI_NM
		    	pmstmt.setString(i,db.rs.getString(i++) );//COP_NM
		    	pmstmt.setString(i,db.rs.getString(i++) );//this_bshop_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//acep_cust_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//hrank_ogn_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//ogn_han_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//tel_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//mcom_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//acep_man
		    	pmstmt.setString(i,db.rs.getString(i++) );//p_mdl_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//deli_dmnd_dati
		    	pmstmt.setString(i,db.rs.getString(i++) );//deli_req_day
		    	pmstmt.setString(i,db.rs.getString(i++) );//deli_mtd
		    	pmstmt.setString(i,db.rs.getString(i++) );//sbusi_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//scop_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//sceo_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//saddr
		    	pmstmt.setString(i,db.rs.getString(i++) );//stel
		    	pmstmt.setString(i,db.rs.getString(i++) );//sfax
		    	pmstmt.setString(i,db.rs.getString(i++) );//location
		    	pmstmt.setString(i,db.rs.getString(i++) );//acepnm1
		    	pmstmt.setString(i,db.rs.getString(i++) );//acepnm2
		    	pmstmt.setString(i,db.rs.getString(i++) );//mst_cd
		    	pmstmt.setString(i,db.rs.getString(i++) );//stre_nm
		    	pmstmt.setString(i,db.rs.getString(i++) );//tax_yn
		    	pmstmt.setString(i,db.rs.getString(i++) );//trans
		    	pmstmt.setString(i,db.rs.getString(i++) );//is_ds
		    	pmstmt.setString(i,db.rs.getString(i++) );//tra_info_flag
		    	pmstmt.setString(i,db.rs.getString(i++) );//tra_pri_show
		    	pmstmt.setString(i,db.rs.getString(i++) );//add_page
		    	pmstmt.setString(i,db.rs.getString(i++) );//zip_code
		    	pmstmt.setString(i,db.rs.getString(i++) );//tra_type
		    	pmstmt.setString(i,db.rs.getString(i++) );//b_acep_man
		    	pmstmt.setString(i,db.rs.getString(i++) );//b_ords_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//b_po_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//b_cust_ord_num
		    	pmstmt.setString(i,db.rs.getString(i++) );//reg_man
		    	pmstmt.setString(i,db.rs.getString(i++) );//reg_date
		    	pmstmt.setString(i,db.rs.getString(i++) );//b_order_type
		    	
		    	if(pmstmt.executeUpdate() < 0) {
		    		mdb.rollback();
		    	}else{				
			    	qry2_ords_num = db.rs.getString("ords_num");
			    	qry2_list_num = db.rs.getInt("list_num");
			    	db.PsetString2(1, "001");
			    	db.PsetString2(2, "협력사 수주정보 전송 성공");
			    	db.PsetString2(3, qry2_ords_num);
			    	db.PsetInt2(4, qry2_list_num);
			    	db.PexecuteUpdate2();
		    	}		    	
			}
			commit();
		}catch(Exception e){
			e.printStackTrace();
			rollback();
			//System.out.println(("Exception : "+qry2_ords_num+"-"+qry2_list_num));
			//System.out.println(("Exception : "+e));
	    	db.PsetString(1, "003");
	    	db.PsetString(2, "협력사 수주정보 전송 실패 : "+e);
	    	db.PsetString(3, qry2_ords_num);
	    	db.PsetInt(4, qry2_list_num);
	    	db.PexecuteUpdate2();
			db.commit();
		}finally{
			db_disconn();  //DB접속 종료
			System.out.println("suju send done");
		}
	}
	private void db_suju_acept() throws SQLException{//수주처리 접수
		db_conn();
		try{
			//데이터 가져오기
			//String pre_qry = " update inf_supp_suju@L_INF_OFFICEDEPOT set DELI_REQ_DATI = ?, AMPM = ?,DELI_REQ_CHG_DATI=?,reg_man_proc=?,REG_DATI_PROC = to_date(?,'yyyymmddhh24miss'), send_state_proc = '000',sys_memo = '수주처리자료 접수' "
			String pre_qry = " update inf_supp_suju@L_INF_OFFICEDEPOT set DELI_REQ_DATI = ?, AMPM = ?,reg_man_proc=?,REG_DATI_PROC = to_date(?,'yyyymmddhh24miss'), send_state_proc = '000',sys_memo = '수주처리자료 접수' "
					       + " where ords_num = ? and list_num = ? and send_state = '001' and sco_cd = 'A54140' ";
			db.prepareStatement(pre_qry);
			//db.prepareStatement2(" select count(*) cnt from inf_supp_suju@L_INF_OFFICEDEPOT where ords_num = ? and list_num = ? and send_state = '001' and sco_cd = 'A54140' ");
			db.prepareStatement2(" select count(*) cnt from inf_supp_suju@L_INF_OFFICEDEPOT iss,ords_dtl od where iss.ords_num = ? and iss.list_num = ? and iss.send_state = '001' and iss.sco_cd = 'A54140' and iss.ords_num = od.ords_num and iss.list_num = od.list_num and od.proc_ty <> '006' AND NOT EXISTS (SELECT * FROM tra_dtl WHERE ords_num = od.ords_num AND list_num = od.list_num) ");

			pmstmt = mdb.prepareStatement(" update SK_HAPPY_ORDER_PROC set read_yn = ?,read_date = GETDATE() where ords_num = ? and list_num = ? ");
				
			mrs = mstmt.executeQuery(" Select ORDS_NUM, LIST_NUM, DELI_REQ_DATI, AMPM, REG_MAN, replace(CONVERT(CHAR(19), REG_DATE, 112)+replace(CONVERT(CHAR(19), REG_DATE, 108),':',''),' ','') as reg_date from SK_HAPPY_ORDER_PROC a  where read_yn = 'N' "//where read_yn <> 'Y' "
									+" and deli_req_dati is not null and ampm is not null "+" and reg_date >  CONVERT (DATETIME, '2017-01-07 00:00:00') ");
			while(mrs.next()){
				String re_ords_num = mrs.getString("ords_num");
			    int re_list_num = mrs.getInt("list_num");
				db.PsetString2(1, re_ords_num);
				db.PsetInt2(2, re_list_num);
				db.PexecuteQuery2();
				if(db.prs2.next()){
					if(db.prs2.getInt("cnt")>0){
				    	int i = 1;
				    	db.PsetString(i++, mrs.getString("DELI_REQ_DATI"));
				    	db.PsetString(i++, mrs.getString("AMPM"));
				    	//db.PsetString(i++, mrs.getString("DELI_REQ_DATI"));
				    	db.PsetString(i++, mrs.getString("REG_MAN"));
				    	db.PsetString(i++, mrs.getString("reg_date"));
				    	db.PsetString(i++, re_ords_num);
				    	db.PsetInt(i++, re_list_num);
						if(db.PexecuteUpdate() < 0) {
							db.rollback();
						}else{
					    	pmstmt.setString(1,"Y" );//read_yn
					    	pmstmt.setString(2,re_ords_num );//reg_date
					    	pmstmt.setInt(3,re_list_num );//reg_date
					    	if(pmstmt.executeUpdate() < 0) {}
						}
					}else{
					    	pmstmt.setString(1,"X" );//read_yn
					    	pmstmt.setString(2,re_ords_num );//reg_date
					    	pmstmt.setInt(3,re_list_num );//reg_date
					    	if(pmstmt.executeUpdate() < 0) {}
					}
				}
			}
			commit();
		}catch(Exception e){
			e.printStackTrace();
			rollback();
		}finally{
			db_disconn();
			System.out.println("suju acept in done");
		}
	}
	
	private void db_basong_in() throws SQLException{//배송요청정보 입력
		db_conn();
		String qry = "";
		try{
			//배송요청정보 입력
			String pre_qry = " INSERT INTO inf_supp_besong@L_INF_OFFICEDEPOT(SEQ_NUM,sco_cd,ORDS_NUM, LIST_NUM, REG_SEQ, TRA_NUM, OCC_DATI, CNT, RADIO_DELI, SEL_DELI_COM, INPUT_DELI_MAN, INPUT_NUMBER, VALID_YN, SPRIT_MTD, REG_MAN, REG_DATE, send_state) "
					       //+ " values(s_inf_supp_besong_num.nextval,'A54140',?, ?, ?, ?, to_date(?,'yyyymmddhh24miss'), ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'yyyymmddhh24miss'), '000') ";
					       + " values((select max(seq_num)+1 from inf_supp_besong@L_INF_OFFICEDEPOT),'A54140',?, ?, ?, ?, to_date(?,'yyyymmddhh24miss'), ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'yyyymmddhh24miss'), '000') ";
			db.prepareStatement(pre_qry);
			db.prepareStatement2(" select count(*) cnt from inf_supp_suju@L_INF_OFFICEDEPOT iss,ords_dtl od where iss.ords_num = ? and iss.list_num = ? and iss.send_state = '001' and iss.send_state_proc = '001' and iss.sco_cd = 'A54140' AND iss.ords_num = od.ords_num AND iss.list_num = od.list_num AND od.proc_ty <> '006' AND NOT EXISTS (SELECT * FROM tra_dtl WHERE ords_num = od.ords_num AND list_num = od.list_num) ");
			
			pmstmt = mdb.prepareStatement(" update SK_HAPPY_DELIVERY set read_yn = ?,read_date = GETDATE() where ords_num = ? and list_num = ? and REG_SEQ = ? ");
			
			qry = " select ORDS_NUM, LIST_NUM, REG_SEQ, TRA_NUM, replace(CONVERT(CHAR(19), OCC_DATI, 112)+replace(CONVERT(CHAR(19), OCC_DATI, 108),':',''),' ','') as occ_dati, CNT, RADIO_DELI, SEL_DELI_COM, INPUT_DELI_MAN, INPUT_NUMBER, VALID_YN, SPRIT_MTD, REG_MAN, replace(CONVERT(CHAR(19), REG_DATE, 112)+replace(CONVERT(CHAR(19), REG_DATE, 108),':',''),' ','') as reg_date, READ_YN, READ_DATE,'000' from SK_HAPPY_DELIVERY a "
			 	+ " where read_yn = 'N'  "//+ " where read_yn <> 'Y'  "
				+" and RADIO_DELI in ('MYCAR','QUICK','PARCEL') and (case radio_deli when 'PARCEL' then valid_yn else 'Y' end)  = 'Y' and SPRIT_MTD in ('ACEP_MAN','ORDS_NUM','PO_NUM','CUST_ORD_NUM') "+" and reg_date >  CONVERT (DATETIME, '2017-01-07 00:00:00') ";
			mrs = mstmt.executeQuery(qry);
			while(mrs.next()){
				db.PsetString2(1, mrs.getString("ords_num"));
				db.PsetInt2(2, mrs.getInt("list_num"));
				db.PexecuteQuery2();
				if(db.prs2.next()){
					if(db.prs2.getInt("cnt")>0){
						db.PsetString(1, mrs.getString("ords_num"));
						db.PsetInt(2, mrs.getInt("list_num"));
						db.PsetInt(3, mrs.getInt("REG_SEQ"));
						db.PsetString(4, mrs.getString("TRA_NUM"));
						db.PsetString(5, mrs.getString("OCC_DATI"));
						db.PsetInt(6, mrs.getInt("cnt"));
						db.PsetString(7, mrs.getString("radio_deli"));
						db.PsetString(8, mrs.getString("sel_deli_com"));
						db.PsetString(9, mrs.getString("input_deli_man"));
						db.PsetString(10, mrs.getString("input_number"));
						db.PsetString(11, mrs.getString("valid_yn"));
						db.PsetString(12, mrs.getString("sprit_mtd"));
						db.PsetString(13, mrs.getString("reg_man"));
						db.PsetString(14, mrs.getString("reg_date"));
						if(db.PexecuteUpdate() < 0) {
							db.rollback();
						}else{
							pmstmt.setString(1,"Y" );//read_yn
							pmstmt.setString(2,mrs.getString("ords_num") );//reg_date
							pmstmt.setInt(3,mrs.getInt("list_num") );//reg_date
							pmstmt.setString(4,mrs.getString("REG_SEQ") );//reg_date
							pmstmt.executeUpdate();
						}
					}/*else{
							pmstmt.setString(1,"X" );//read_yn
							pmstmt.setString(2,mrs.getString("ords_num") );//reg_date
							pmstmt.setInt(3,mrs.getInt("list_num") );//reg_date
							pmstmt.setString(4,mrs.getString("REG_SEQ") );//reg_date
							pmstmt.executeUpdate();
					}*/
				}
			}
			commit();
		}catch(Exception e){
			rollback();
			e.printStackTrace();
		}finally{
			db_disconn();
			System.out.println("basong in done");
		}
	}	
	
	private void db_done_in() throws SQLException{//배송완료 데이터 등록
		db_conn();
		String qry = "";
		try{
			//배송요청정보 입력
			String pre_qry = " update inf_supp_besong@L_INF_OFFICEDEPOT set PROC_DATE = ?,comp=cnt, REG_DATE_proc = to_date(?,'yyyymmddhh24miss'),reg_man_proc=?,send_state_proc = '000',sys_memo = '협력사 배송요청 정보 등록'||to_char(sysdate,'yyyymmdd') "
					       + " where tra_num = trim(?) and send_state = '001' and sco_cd = 'A54140' ";
			db.prepareStatement(pre_qry);
			db.prepareStatement2(" select count(*) cnt from inf_supp_besong@L_INF_OFFICEDEPOT where tra_num = trim(?) and send_state = '001' and sco_cd = 'A54140' ");//체크용
			
			pmstmt = mdb.prepareStatement(" update SK_HAPPY_DELIVERY_PROC set read_yn = 'Y',read_date = GETDATE() where tra_num = ? ");

			qry = " select TRA_NUM, PROC_DATE, REG_MAN, replace(CONVERT(CHAR(19), REG_DATE, 112)+replace(CONVERT(CHAR(19), REG_DATE, 108),':',''),' ','') as reg_date from SK_HAPPY_DELIVERY_PROC a "
				+ " where read_yn <> 'Y' "
				+ " 	and tra_num is not null "+" and reg_date >  CONVERT (DATETIME, '2017-01-07 00:00:00') ";

			mrs = mstmt.executeQuery(qry);
			while(mrs.next()){
		      db.PsetString2(1, mrs.getString("TRA_NUM"));
		      db.PexecuteQuery2();
			  if(db.prs2.next()){
				if(db.prs2.getInt("cnt")>0){
					int i=1;
			    	db.PsetString(i++, mrs.getString("PROC_DATE"));
			    	db.PsetString(i++, mrs.getString("REG_DATE"));
			    	db.PsetString(i++, mrs.getString("REG_MAN"));
			    	db.PsetString(i++, mrs.getString("TRA_NUM"));
					if(db.PexecuteUpdate() < 0) {
						db.rollback();
					}else{
						pmstmt.setString(1,mrs.getString("TRA_NUM") );
						pmstmt.executeUpdate();
					}
				}
			  }
			}
			commit();
		}catch(Exception e){
			rollback();
			e.printStackTrace();
		}finally{
			db_disconn();
			System.out.println("done information done");			
		}
	}

	private void commit() {try {db.commit();mdb.commit();} catch (SQLException e) {e.printStackTrace();}}
	private void rollback() {try {db.rollback();mdb.rollback();} catch (SQLException e) {e.printStackTrace();}}
	private void db_disconn() {db.DB_DisConn();try {mdb.close();} catch (SQLException e) {e.printStackTrace();}}
	//private void DB_Close_Asp(){asp.DB_DisConn();}
	
	public static void main(String args[]) throws SQLException {
		try{
		Demon_Supp_Demon s = new Demon_Supp_Demon();
			try{System.out.println("Demon_Supp_Demon suju send start");
				s.db_proc();// : 수주처리 전송
				System.out.println("Demon_Supp_Demon suju send end");
			}catch(Exception e){e.printStackTrace();}
			try{System.out.println("Demon_Supp_Demon suju acept start");
				s.db_suju_acept();// : 수주처리 접수
				System.out.println("Demon_Supp_Demon suju acept end");
			}catch(Exception e){e.printStackTrace();}
			try{System.out.println("Demon_Supp_Demon suju besong start");
				s.db_basong_in();// : 배송요청처리 정보 입력
				System.out.println("Demon_Supp_Demon suju besong end");
			}catch(Exception e){e.printStackTrace();}
			try{System.out.println("Demon_Supp_Demon suju besong_done start");
				s.db_done_in();// : 배송완료 데이터 등록
				System.out.println("Demon_Supp_Demon suju besong_done end");
			}catch(Exception e){e.printStackTrace();}
		}catch(Exception e){//System.out.println("data fail : "+e);
			e.printStackTrace();
		}finally{
			//db_disconn();  //DB접속 종료
		}
	}
	
}
