/*
 * 10분씩 돌아가는 데몬 
 */
//package comm;

import java.sql.*;
import java.util.*;

import comm.mk_log;



//빈즈 불러서 사용

public class  Demon_csbill_10m
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
		static private mk_log log = new mk_log();
        //생성자
        public Demon_csbill_10m(){}

        public static void main(String[] args)
        {
        	
    		int log_flag = 0;
    		String prgm_nm = "csbill demon 10 minutes";
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	comm.OkMroUtil mcu = new comm.OkMroUtil(); 
        	
        	log.Write(log_flag, prgm_nm, "CSBill Demon Start!!");
        	
        	
			Connection msConn = null;
			PreparedStatement msPstmt = null;
			ResultSet msRs = null;

			Connection msConn2 = null;
			PreparedStatement msPstmt2 = null;
			ResultSet msRs2 = null;

			Connection msConn3 = null;
			PreparedStatement msPstmt3 = null;
			ResultSet msRs3 = null;

			Connection msConn4 = null;
			PreparedStatement msPstmt4 = null;
			ResultSet msRs4 = null;
        	
        	StringBuffer sbSelNotSendHappy = new StringBuffer();
        	StringBuffer sbSelCsbillCnt    = new StringBuffer();
        	StringBuffer sbInsCsbillMaster = new StringBuffer();
        	StringBuffer sbInsCsbillItem   = new StringBuffer();
        	StringBuffer sbInsCsbillTran   = new StringBuffer();
        	StringBuffer sbSelHappyItem    = new StringBuffer();
        	StringBuffer sbSelHappyTran    = new StringBuffer();
        	StringBuffer sbUpdHappyMaster  = new StringBuffer();
        	
        	int pSeq = 0;
        	int iInsCsbillMaster = 0;
        	int iInsCsbillItem = 0;
        	int iInsCsbillTran = 0;
        	int iUpdHappyMaster = 0;
        	String strQry = "";
        	try{
        		
            	db.CUST_ID = "M005769";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_csbill_10m.java";
    			db.DB_Conn();
    			
    			log.Write(log_flag, prgm_nm, "happynarae db connection!!");
				

    			log.Write(log_flag, prgm_nm, "happynarae non-send data select!!");
				sbSelNotSendHappy.append("  SELECT HEAD_MANA_IDEN                                          ").append("\n");
				sbSelNotSendHappy.append("        ,MANA_SEBU_TYPE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,MANA_DATA_TYPE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,MANA_PROC_STAT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,MANA_SEND_CHID                                          ").append("\n");
				sbSelNotSendHappy.append("        ,MANA_RECV_CHID                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_DOCU_DATE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_BILL_TYPE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_DEMA_INDI                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_DESC_TEXT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_PART_IDEN                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_ORGN_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_PART_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_ADDR_TEXT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUBSTRB (TRIM (SUPP_BUSI_TYPE), 1, 70) SUPP_BUSI_TYPE   ").append("\n");
				sbSelNotSendHappy.append("        ,SUBSTRB (TRIM (SUPP_BUSI_CLAS), 1, 70) SUPP_BUSI_CLAS   ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_CONT_DEPT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_PERS_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_PERS_TELE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_PERS_MAIL                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_PART_IDEN                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_ORGN_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_PART_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_ADDR_TEXT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUBSTRB (TRIM (BUYE_BUSI_TYPE), 1, 70) BUYE_BUSI_TYPE   ").append("\n");
				sbSelNotSendHappy.append("        ,SUBSTRB (TRIM (BUYE_BUSI_CLAS), 1, 70) BUYE_BUSI_CLAS   ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_CONT_DEPT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_PERS_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_PERS_TELE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_PERS_MAIL                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUMM_CHAR_AMOU                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUMM_TAX0_AMOU                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUMM_TOTA_AMOU                                          ").append("\n");
				sbSelNotSendHappy.append("        ,MANA_OPPO_FLAG                                          ").append("\n");
				sbSelNotSendHappy.append("        ,REG_ID                                                  ").append("\n");
				sbSelNotSendHappy.append("        ,REG_DATE                                                ").append("\n");
				sbSelNotSendHappy.append("        ,SEND_YN                                                 ").append("\n");
				sbSelNotSendHappy.append("        ,MRO_SENDYN                                              ").append("\n");
				sbSelNotSendHappy.append("        ,INF_SEND_MAN                                            ").append("\n");
				sbSelNotSendHappy.append("        ,INF_SEND_DATI                                           ").append("\n");
				sbSelNotSendHappy.append("        ,INF_STAT_MEMO                                           ").append("\n");
				sbSelNotSendHappy.append("        ,BILL_TRAN_FLAG                                          ").append("\n");
				sbSelNotSendHappy.append("        ,TRAN_FORM_IDEN                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_AMED_CODE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_AMED_TEXT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,SUPP_MPOB_IDEN                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_IDEN_TYPE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUYE_MPOB_IDEN                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUY2_CONT_DEPT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUY2_PERS_NAME                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUY2_PERS_TELE                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BUY2_PERS_MAIL                                          ").append("\n");
				sbSelNotSendHappy.append("        ,HEAD_DES2_TEXT                                          ").append("\n");
				sbSelNotSendHappy.append("        ,BIZM_STND_VERS                                          ").append("\n");
				sbSelNotSendHappy.append("    FROM dti_mrok_master                                         ").append("\n");
				sbSelNotSendHappy.append("   WHERE 1=1                                                     ").append("\n");
				sbSelNotSendHappy.append("     AND mro_read IS NULL                                        ").append("\n");
				sbSelNotSendHappy.append("     AND insert_yn = 'H'                                         ").append("\n");
				//sbSelNotSendHappy.append("     AND mana_send_chid IN ('hjwone','she0628')                  ").append("\n");
				//sbSelNotSendHappy.append("     AND reg_date >= to_date('20120626')                         ").append("\n");
				strQry = sbSelNotSendHappy.toString(); 			
				log.Write(log_flag, prgm_nm, strQry);
				db.prepareStatement(strQry); 
				db.PexecuteQuery();
				
				if(db.prs != null){
					log.Write(log_flag, prgm_nm, "happynarae put data!!");
					List<Object> lData = new ArrayList<Object>();
					while(db.prs.next()){

						HashMap<String,String> hm = new HashMap<String,String>();
						
						log.Write(1, prgm_nm, "HEAD_MANA_IDEN-->"+comm.OkMroUtil.checkNull(db.prs.getString("HEAD_MANA_IDEN"  )));
						hm.put("HEAD_MANA_IDEN"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_MANA_IDEN"  )));
						hm.put("MANA_SEBU_TYPE"     ,comm.OkMroUtil.checkNull(db.prs.getString("MANA_SEBU_TYPE"  )));
						hm.put("MANA_DATA_TYPE"     ,comm.OkMroUtil.checkNull(db.prs.getString("MANA_DATA_TYPE"  )));
						hm.put("MANA_PROC_STAT"     ,comm.OkMroUtil.checkNull(db.prs.getString("MANA_PROC_STAT"  )));
						hm.put("MANA_SEND_CHID"     ,comm.OkMroUtil.checkNull(db.prs.getString("MANA_SEND_CHID"  )));
						hm.put("MANA_RECV_CHID"     ,comm.OkMroUtil.checkNull(db.prs.getString("MANA_RECV_CHID"  )));
						hm.put("HEAD_DOCU_DATE"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_DOCU_DATE"  )));
						hm.put("HEAD_BILL_TYPE"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_BILL_TYPE"  )));
						hm.put("HEAD_DEMA_INDI"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_DEMA_INDI"  )));
						hm.put("HEAD_DESC_TEXT"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_DESC_TEXT"  )));
						hm.put("SUPP_PART_IDEN"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_PART_IDEN"  )));
						hm.put("SUPP_ORGN_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_ORGN_NAME"  )));
						hm.put("SUPP_PART_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_PART_NAME"  )));
						hm.put("SUPP_ADDR_TEXT"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_ADDR_TEXT"  )));
						hm.put("SUPP_BUSI_TYPE"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_BUSI_TYPE"  )));
						hm.put("SUPP_BUSI_CLAS"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_BUSI_CLAS"  )));
						hm.put("SUPP_CONT_DEPT"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_CONT_DEPT"  )));
						hm.put("SUPP_PERS_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_PERS_NAME"  )));
						hm.put("SUPP_PERS_TELE"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_PERS_TELE"  )));
						hm.put("SUPP_PERS_MAIL"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_PERS_MAIL"  )));
						hm.put("BUYE_PART_IDEN"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_PART_IDEN"  )));
						hm.put("BUYE_ORGN_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_ORGN_NAME"  )));
						hm.put("BUYE_PART_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_PART_NAME"  )));
						hm.put("BUYE_ADDR_TEXT"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_ADDR_TEXT"  )));
						hm.put("BUYE_BUSI_TYPE"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_BUSI_TYPE"  )));
						hm.put("BUYE_BUSI_CLAS"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_BUSI_CLAS"  )));
						hm.put("BUYE_CONT_DEPT"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_CONT_DEPT"  )));
						hm.put("BUYE_PERS_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_PERS_NAME"  )));
						hm.put("BUYE_PERS_TELE"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_PERS_TELE"  )));
						hm.put("BUYE_PERS_MAIL"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_PERS_MAIL"  )));
						hm.put("SUMM_CHAR_AMOU"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUMM_CHAR_AMOU"  )));
						hm.put("SUMM_TAX0_AMOU"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUMM_TAX0_AMOU"  )));
						hm.put("SUMM_TOTA_AMOU"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUMM_TOTA_AMOU"  )));
						hm.put("MANA_OPPO_FLAG"     ,comm.OkMroUtil.checkNull(db.prs.getString("MANA_OPPO_FLAG"  )));
						hm.put("REG_ID"             ,comm.OkMroUtil.checkNull(db.prs.getString("REG_ID"          )));
						hm.put("REG_DATE"           ,comm.OkMroUtil.checkNull(db.prs.getString("REG_DATE"        )));
						hm.put("SEND_YN"            ,comm.OkMroUtil.checkNull(db.prs.getString("SEND_YN"         )));
						hm.put("MRO_SENDYN"         ,comm.OkMroUtil.checkNull(db.prs.getString("MRO_SENDYN"      )));
						hm.put("INF_SEND_MAN"       ,comm.OkMroUtil.checkNull(db.prs.getString("INF_SEND_MAN"    )));
						hm.put("INF_SEND_DATI"      ,comm.OkMroUtil.checkNull(db.prs.getString("INF_SEND_DATI"   )));
						hm.put("INF_STAT_MEMO"      ,comm.OkMroUtil.checkNull(db.prs.getString("INF_STAT_MEMO"   )));
						hm.put("BILL_TRAN_FLAG"     ,comm.OkMroUtil.checkNull(db.prs.getString("BILL_TRAN_FLAG"  )));
						hm.put("TRAN_FORM_IDEN"     ,comm.OkMroUtil.checkNull(db.prs.getString("TRAN_FORM_IDEN"  )));
						hm.put("HEAD_AMED_CODE"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_AMED_CODE"  )));
						hm.put("HEAD_AMED_TEXT"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_AMED_TEXT"  )));
						hm.put("SUPP_MPOB_IDEN"     ,comm.OkMroUtil.checkNull(db.prs.getString("SUPP_MPOB_IDEN"  )));
						hm.put("BUYE_IDEN_TYPE"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_IDEN_TYPE"  )));
						hm.put("BUYE_MPOB_IDEN"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUYE_MPOB_IDEN"  )));
						hm.put("BUY2_CONT_DEPT"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUY2_CONT_DEPT"  )));
						hm.put("BUY2_PERS_NAME"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUY2_PERS_NAME"  )));
						hm.put("BUY2_PERS_TELE"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUY2_PERS_TELE"  )));
						hm.put("BUY2_PERS_MAIL"     ,comm.OkMroUtil.checkNull(db.prs.getString("BUY2_PERS_MAIL"  )));
						hm.put("HEAD_DES2_TEXT"     ,comm.OkMroUtil.checkNull(db.prs.getString("HEAD_DES2_TEXT"  )));
						hm.put("BIZM_STND_VERS"     ,comm.OkMroUtil.checkNull(db.prs.getString("BIZM_STND_VERS"  )));
						
						lData.add(hm);
						
					}
					
					
					if(db.ps != null) db.ps.close();
					if(db.prs != null) db.prs.close();
					
					log.Write(log_flag, prgm_nm, "csbill db connection!!");
					try{

						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
						String url = "jdbc:sqlserver://114.108.138.103:1433;databaseName=csbill_scm;SelectMethod=cursor";
						String userid = "mrok";
						String passwd = "mrok";
						
						
						msConn = DriverManager.getConnection(url,userid,passwd);
						msConn.setAutoCommit(false); 

						msConn2 = DriverManager.getConnection(url,userid,passwd);
						msConn2.setAutoCommit(false); 

						msConn3 = DriverManager.getConnection(url,userid,passwd);
						msConn3.setAutoCommit(false); 

						msConn4 = DriverManager.getConnection(url,userid,passwd);
						msConn4.setAutoCommit(false); 
						
					}catch(ClassNotFoundException mscnfe){
						mscnfe.printStackTrace();
						throw new Exception("CSBill MSSQL Connection 에러 : "+mscnfe.toString());
					}catch(Exception mse){
						mse.printStackTrace();
						throw new Exception("CSBill MSSQL 알수없는 에러 : "+mse.toString());
					}
					
					//csbill 존재여부 조회
					sbSelCsbillCnt.append(" select count(*) cnt                  ").append("\n");
					sbSelCsbillCnt.append("   from dti_mrok_master               ").append("\n");
					sbSelCsbillCnt.append("  where head_mana_iden = ?            ").append("\n");
					
					msPstmt = msConn.prepareStatement(sbSelCsbillCnt.toString());
					
					//csbill master insert
					sbInsCsbillMaster.append(" insert into dti_mrok_master  ").append("\n");
					sbInsCsbillMaster.append(" (HEAD_MANA_IDEN              ").append("\n");
					sbInsCsbillMaster.append(" ,MANA_SEBU_TYPE              ").append("\n");
					sbInsCsbillMaster.append(" ,MANA_DATA_TYPE              ").append("\n");
					sbInsCsbillMaster.append(" ,MANA_PROC_STAT              ").append("\n");
					sbInsCsbillMaster.append(" ,MANA_SEND_CHID              ").append("\n");
					sbInsCsbillMaster.append(" ,MANA_RECV_CHID              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_DOCU_DATE              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_BILL_TYPE              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_DEMA_INDI              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_DESC_TEXT              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_PART_IDEN              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_ORGN_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_PART_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_ADDR_TEXT              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_BUSI_TYPE              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_BUSI_CLAS              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_CONT_DEPT              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_PERS_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_PERS_TELE              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_PERS_MAIL              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_PART_IDEN              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_ORGN_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_PART_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_ADDR_TEXT              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_BUSI_TYPE              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_BUSI_CLAS              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_CONT_DEPT              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_PERS_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_PERS_TELE              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_PERS_MAIL              ").append("\n");
					sbInsCsbillMaster.append(" ,SUMM_CHAR_AMOU              ").append("\n");
					sbInsCsbillMaster.append(" ,SUMM_TAX0_AMOU              ").append("\n");
					sbInsCsbillMaster.append(" ,SUMM_TOTA_AMOU              ").append("\n");
					sbInsCsbillMaster.append(" ,MANA_OPPO_FLAG              ").append("\n");
					sbInsCsbillMaster.append(" ,REG_ID                      ").append("\n");
					sbInsCsbillMaster.append(" ,REG_DATE                    ").append("\n");
					sbInsCsbillMaster.append(" ,SEND_YN                     ").append("\n");
					sbInsCsbillMaster.append(" ,MRO_SENDYN                  ").append("\n");
					sbInsCsbillMaster.append(" ,BILL_TRAN_FLAG              ").append("\n");
					sbInsCsbillMaster.append(" ,TRAN_FORM_IDEN              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_AMED_CODE              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_AMED_TEXT              ").append("\n");
					sbInsCsbillMaster.append(" ,SUPP_MPOB_IDEN              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_IDEN_TYPE              ").append("\n");
					sbInsCsbillMaster.append(" ,BUYE_MPOB_IDEN              ").append("\n");
					sbInsCsbillMaster.append(" ,BUY2_CONT_DEPT              ").append("\n");
					sbInsCsbillMaster.append(" ,BUY2_PERS_NAME              ").append("\n");
					sbInsCsbillMaster.append(" ,BUY2_PERS_TELE              ").append("\n");
					sbInsCsbillMaster.append(" ,BUY2_PERS_MAIL              ").append("\n");
					sbInsCsbillMaster.append(" ,HEAD_DES2_TEXT              ").append("\n");
					sbInsCsbillMaster.append(" ,INSERT_YN                   ").append("\n");
					sbInsCsbillMaster.append(" ,BIZM_STND_VERS)             ").append("\n");
					sbInsCsbillMaster.append(" VALUES                       ").append("\n");
					sbInsCsbillMaster.append(" (?                           ").append("\n");   //HEAD_MANA_IDEN    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MANA_SEBU_TYPE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MANA_DATA_TYPE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MANA_PROC_STAT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MANA_SEND_CHID    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MANA_RECV_CHID    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_DOCU_DATE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_BILL_TYPE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_DEMA_INDI    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_DESC_TEXT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_PART_IDEN    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_ORGN_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_PART_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_ADDR_TEXT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_BUSI_TYPE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_BUSI_CLAS    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_CONT_DEPT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_PERS_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_PERS_TELE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_PERS_MAIL    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_PART_IDEN    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_ORGN_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_PART_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_ADDR_TEXT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_BUSI_TYPE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_BUSI_CLAS    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_CONT_DEPT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_PERS_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_PERS_TELE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_PERS_MAIL    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUMM_CHAR_AMOU    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUMM_TAX0_AMOU    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUMM_TOTA_AMOU    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MANA_OPPO_FLAG    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //REG_ID            
					sbInsCsbillMaster.append(" ,getdate()                   ").append("\n");   //REG_DATE          
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SEND_YN           
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //MRO_SENDYN        
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BILL_TRAN_FLAG    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //TRAN_FORM_IDEN    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_AMED_CODE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_AMED_TEXT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //SUPP_MPOB_IDEN    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_IDEN_TYPE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUYE_MPOB_IDEN    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUY2_CONT_DEPT    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUY2_PERS_NAME    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUY2_PERS_TELE    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //BUY2_PERS_MAIL    
					sbInsCsbillMaster.append(" ,?                           ").append("\n");   //HEAD_DES2_TEXT    
					sbInsCsbillMaster.append(" ,'Y'                         ").append("\n");   //INSERT_YN         
					sbInsCsbillMaster.append(" ,?)                          ").append("\n");   //BIZM_STND_VERS  
					msPstmt2 = msConn2.prepareStatement(sbInsCsbillMaster.toString());
					
					//csbill item insert
					sbInsCsbillItem.append(" INSERT INTO dti_mrok_item   ").append("\n");
					sbInsCsbillItem.append(" (HEAD_MANA_IDEN             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_SEQU_IDEN             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_TRAN_DATE             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_ITEM_NAME             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_DESC_TEXT             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_ITEM_AMOU             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_TOTA_AMOU             ").append("\n");
					sbInsCsbillItem.append(" ,LIST_TAX0_AMOU)            ").append("\n");
					sbInsCsbillItem.append(" values                      ").append("\n");
					sbInsCsbillItem.append(" (?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" ,?                          ").append("\n");
					sbInsCsbillItem.append(" )                           ").append("\n");
					msPstmt3 = msConn3.prepareStatement(sbInsCsbillItem.toString());
					
					//csbill tran insert
					sbInsCsbillTran.append(" INSERT INTO dti_mrok_tran   ").append("\n");
					sbInsCsbillTran.append(" (HEAD_MANA_IDEN             ").append("\n");
					sbInsCsbillTran.append(" ,TRAN_SEQ                   ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_CODE             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_NAME             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_DEFI_TEXT             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_UNIT_TEXT             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_QUAN             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_BASI_AMOU             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_AMOU             ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_ATTR1            ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_ATTR2            ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_ATTR3            ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_ATTR4            ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_ATTR5            ").append("\n");
					sbInsCsbillTran.append(" ,LIST_ITEM_ATTR6            ").append("\n");
					sbInsCsbillTran.append(" ,LIST_DESC_TEXT)            ").append("\n");
					sbInsCsbillTran.append(" VALUES                      ").append("\n");
					sbInsCsbillTran.append(" (?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?                          ").append("\n");
					sbInsCsbillTran.append(" ,?)                         ").append("\n");
					msPstmt4 = msConn4.prepareStatement(sbInsCsbillTran.toString());
					
					//happynarae item select
					sbSelHappyItem.append(" SELECT HEAD_MANA_IDEN       ").append("\n");
					sbSelHappyItem.append("       ,LIST_SEQU_IDEN       ").append("\n");
					sbSelHappyItem.append("       ,LIST_TRAN_DATE       ").append("\n");
					sbSelHappyItem.append("       ,LIST_ITEM_NAME       ").append("\n");
					sbSelHappyItem.append("       ,LIST_DESC_TEXT       ").append("\n");
					sbSelHappyItem.append("       ,LIST_ITEM_AMOU       ").append("\n");
					sbSelHappyItem.append("       ,LIST_TOTA_AMOU       ").append("\n");
					sbSelHappyItem.append("       ,LIST_TAX0_AMOU       ").append("\n");
					sbSelHappyItem.append("   FROM dti_mrok_item        ").append("\n");
					sbSelHappyItem.append("  WHERE HEAD_MANA_IDEN = ?   ").append("\n");
					db.prepareStatement2(sbSelHappyItem.toString());
					
					//happynarae tran select
					sbSelHappyTran.append("  SELECT HEAD_MANA_IDEN                                                                          ").append("\n");
					sbSelHappyTran.append("        ,TRAN_SEQ                                                                                ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_CODE                                                                          ").append("\n");
					sbSelHappyTran.append("        ,SUBSTRB (REPLACE (F_STR_CHK (TRIM (LIST_ITEM_NAME)), ':', 'ː'), 1, 70) LIST_ITEM_NAME   ").append("\n");
					sbSelHappyTran.append("        ,SUBSTRB (REPLACE (F_STR_CHK (TRIM (LIST_DEFI_TEXT)), ':', 'ː'), 1, 70) LIST_DEFI_TEXT   ").append("\n");
					sbSelHappyTran.append("        ,SUBSTRB (REPLACE (F_STR_CHK (TRIM (LIST_UNIT_TEXT)), ':', 'ː'), 1, 70) LIST_UNIT_TEXT   ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_QUAN                                                                          ").append("\n");
					sbSelHappyTran.append("        ,LIST_BASI_AMOU                                                                          ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_AMOU                                                                          ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_ATTR1                                                                         ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_ATTR2                                                                         ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_ATTR3                                                                         ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_ATTR4                                                                         ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_ATTR5                                                                         ").append("\n");
					sbSelHappyTran.append("        ,LIST_ITEM_ATTR6                                                                         ").append("\n");
					sbSelHappyTran.append("        ,SUBSTRB (REPLACE (F_STR_CHK (TRIM (LIST_DESC_TEXT)), ':', 'ː'), 1, 70) LIST_DESC_TEXT   ").append("\n");
					sbSelHappyTran.append("    FROM dti_mrok_tran                                                                           ").append("\n");
					sbSelHappyTran.append("   WHERE head_mana_iden = ?                                                                      ").append("\n");
					db.prepareStatement3(sbSelHappyTran.toString());
					
					
					//happynarae master update
					sbUpdHappyMaster.append(" UPDATE dti_mrok_master      ").append("\n");
					sbUpdHappyMaster.append("    SET insert_yn = 'Y'      ").append("\n");
					sbUpdHappyMaster.append("  WHERE HEAD_MANA_IDEN = ?   ").append("\n");
					db.prepareStatement(sbUpdHappyMaster.toString());
					
					
					for(int xx=0; xx<lData.size(); xx++){
						log.Write(log_flag, prgm_nm, "happynarae get data");
						String sHEAD_MANA_IDEN  = (String)((HashMap)lData.get(xx)).get("HEAD_MANA_IDEN" );
						String sMANA_SEBU_TYPE  = (String)((HashMap)lData.get(xx)).get("MANA_SEBU_TYPE" );
						String sMANA_DATA_TYPE  = (String)((HashMap)lData.get(xx)).get("MANA_DATA_TYPE" );
						String sMANA_PROC_STAT  = (String)((HashMap)lData.get(xx)).get("MANA_PROC_STAT" );
						String sMANA_SEND_CHID  = (String)((HashMap)lData.get(xx)).get("MANA_SEND_CHID" );
						String sMANA_RECV_CHID  = (String)((HashMap)lData.get(xx)).get("MANA_RECV_CHID" );
						String sHEAD_DOCU_DATE  = (String)((HashMap)lData.get(xx)).get("HEAD_DOCU_DATE" );
						String sHEAD_BILL_TYPE  = (String)((HashMap)lData.get(xx)).get("HEAD_BILL_TYPE" );
						String sHEAD_DEMA_INDI  = (String)((HashMap)lData.get(xx)).get("HEAD_DEMA_INDI" );
						String sHEAD_DESC_TEXT  = (String)((HashMap)lData.get(xx)).get("HEAD_DESC_TEXT" );
						String sSUPP_PART_IDEN  = (String)((HashMap)lData.get(xx)).get("SUPP_PART_IDEN" );
						String sSUPP_ORGN_NAME  = (String)((HashMap)lData.get(xx)).get("SUPP_ORGN_NAME" );
						String sSUPP_PART_NAME  = (String)((HashMap)lData.get(xx)).get("SUPP_PART_NAME" );
						String sSUPP_ADDR_TEXT  = (String)((HashMap)lData.get(xx)).get("SUPP_ADDR_TEXT" );
						String sSUPP_BUSI_TYPE  = (String)((HashMap)lData.get(xx)).get("SUPP_BUSI_TYPE" );
						String sSUPP_BUSI_CLAS  = (String)((HashMap)lData.get(xx)).get("SUPP_BUSI_CLAS" );
						String sSUPP_CONT_DEPT  = (String)((HashMap)lData.get(xx)).get("SUPP_CONT_DEPT" );
						String sSUPP_PERS_NAME  = (String)((HashMap)lData.get(xx)).get("SUPP_PERS_NAME" );
						String sSUPP_PERS_TELE  = (String)((HashMap)lData.get(xx)).get("SUPP_PERS_TELE" );
						String sSUPP_PERS_MAIL  = (String)((HashMap)lData.get(xx)).get("SUPP_PERS_MAIL" );
						String sBUYE_PART_IDEN  = (String)((HashMap)lData.get(xx)).get("BUYE_PART_IDEN" );
						String sBUYE_ORGN_NAME  = (String)((HashMap)lData.get(xx)).get("BUYE_ORGN_NAME" );
						String sBUYE_PART_NAME  = (String)((HashMap)lData.get(xx)).get("BUYE_PART_NAME" );
						String sBUYE_ADDR_TEXT  = (String)((HashMap)lData.get(xx)).get("BUYE_ADDR_TEXT" );
						String sBUYE_BUSI_TYPE  = (String)((HashMap)lData.get(xx)).get("BUYE_BUSI_TYPE" );
						String sBUYE_BUSI_CLAS  = (String)((HashMap)lData.get(xx)).get("BUYE_BUSI_CLAS" );
						String sBUYE_CONT_DEPT  = (String)((HashMap)lData.get(xx)).get("BUYE_CONT_DEPT" );
						String sBUYE_PERS_NAME  = (String)((HashMap)lData.get(xx)).get("BUYE_PERS_NAME" );
						String sBUYE_PERS_TELE  = (String)((HashMap)lData.get(xx)).get("BUYE_PERS_TELE" );
						String sBUYE_PERS_MAIL  = (String)((HashMap)lData.get(xx)).get("BUYE_PERS_MAIL" );
						String sSUMM_CHAR_AMOU  = (String)((HashMap)lData.get(xx)).get("SUMM_CHAR_AMOU" );
						String sSUMM_TAX0_AMOU  = (String)((HashMap)lData.get(xx)).get("SUMM_TAX0_AMOU" );
						String sSUMM_TOTA_AMOU  = (String)((HashMap)lData.get(xx)).get("SUMM_TOTA_AMOU" );
						String sMANA_OPPO_FLAG  = (String)((HashMap)lData.get(xx)).get("MANA_OPPO_FLAG" );
						String sREG_ID          = (String)((HashMap)lData.get(xx)).get("REG_ID"         );
						String sREG_DATE        = (String)((HashMap)lData.get(xx)).get("REG_DATE"       );
						String sSEND_YN         = (String)((HashMap)lData.get(xx)).get("SEND_YN"        );
						String sMRO_SENDYN      = (String)((HashMap)lData.get(xx)).get("MRO_SENDYN"     );
						String sINF_SEND_MAN    = (String)((HashMap)lData.get(xx)).get("INF_SEND_MAN"   );
						String sINF_SEND_DATI   = (String)((HashMap)lData.get(xx)).get("INF_SEND_DATI"  );
						String sINF_STAT_MEMO   = (String)((HashMap)lData.get(xx)).get("INF_STAT_MEMO"  );
						String sBILL_TRAN_FLAG  = (String)((HashMap)lData.get(xx)).get("BILL_TRAN_FLAG" );
						String sTRAN_FORM_IDEN  = (String)((HashMap)lData.get(xx)).get("TRAN_FORM_IDEN" );
						String sHEAD_AMED_CODE  = (String)((HashMap)lData.get(xx)).get("HEAD_AMED_CODE" );
						String sHEAD_AMED_TEXT  = (String)((HashMap)lData.get(xx)).get("HEAD_AMED_TEXT" );
						String sSUPP_MPOB_IDEN  = (String)((HashMap)lData.get(xx)).get("SUPP_MPOB_IDEN" );
						String sBUYE_IDEN_TYPE  = (String)((HashMap)lData.get(xx)).get("BUYE_IDEN_TYPE" );
						String sBUYE_MPOB_IDEN  = (String)((HashMap)lData.get(xx)).get("BUYE_MPOB_IDEN" );
						String sBUY2_CONT_DEPT  = (String)((HashMap)lData.get(xx)).get("BUY2_CONT_DEPT" );
						String sBUY2_PERS_NAME  = (String)((HashMap)lData.get(xx)).get("BUY2_PERS_NAME" );
						String sBUY2_PERS_TELE  = (String)((HashMap)lData.get(xx)).get("BUY2_PERS_TELE" );
						String sBUY2_PERS_MAIL  = (String)((HashMap)lData.get(xx)).get("BUY2_PERS_MAIL" );
						String sHEAD_DES2_TEXT  = (String)((HashMap)lData.get(xx)).get("HEAD_DES2_TEXT" );
						String sBIZM_STND_VERS  = (String)((HashMap)lData.get(xx)).get("BIZM_STND_VERS" );
						
						log.Write(log_flag, prgm_nm, "HEAD_MANA_IDEN->"+sHEAD_MANA_IDEN);
						
						//csbill 갯수조회
						pSeq = 1;
						msPstmt.setString(pSeq++, sHEAD_MANA_IDEN);
						msRs = msPstmt.executeQuery();
						
						int iCsbillCnt = -1;
						if(msRs != null){		
							
							while(msRs.next()){
								iCsbillCnt = Integer.parseInt(comm.OkMroUtil.checkNull(msRs.getString("cnt"    )));
							}
							log.Write(log_flag, prgm_nm, "iCsbillCnt->"+iCsbillCnt);
							//csbill서버에 존재하지 않으면 insert 시작
							if(iCsbillCnt == 0){
								log.Write(log_flag, prgm_nm, "csbill master insert batch add");
								pSeq = 1;
								msPstmt2.setString(pSeq++, sHEAD_MANA_IDEN );
								msPstmt2.setString(pSeq++, sMANA_SEBU_TYPE );
								msPstmt2.setString(pSeq++, sMANA_DATA_TYPE );
								msPstmt2.setString(pSeq++, sMANA_PROC_STAT );
								msPstmt2.setString(pSeq++, sMANA_SEND_CHID );
								msPstmt2.setString(pSeq++, sMANA_RECV_CHID );
								msPstmt2.setString(pSeq++, sHEAD_DOCU_DATE );
								msPstmt2.setString(pSeq++, sHEAD_BILL_TYPE );
								msPstmt2.setString(pSeq++, sHEAD_DEMA_INDI );
								msPstmt2.setString(pSeq++, sHEAD_DESC_TEXT );
								msPstmt2.setString(pSeq++, sSUPP_PART_IDEN );
								msPstmt2.setString(pSeq++, sSUPP_ORGN_NAME );
								msPstmt2.setString(pSeq++, sSUPP_PART_NAME );
								msPstmt2.setString(pSeq++, sSUPP_ADDR_TEXT );
								msPstmt2.setString(pSeq++, sSUPP_BUSI_TYPE );
								msPstmt2.setString(pSeq++, sSUPP_BUSI_CLAS );
								msPstmt2.setString(pSeq++, sSUPP_CONT_DEPT );
								msPstmt2.setString(pSeq++, sSUPP_PERS_NAME );
								msPstmt2.setString(pSeq++, sSUPP_PERS_TELE );
								msPstmt2.setString(pSeq++, sSUPP_PERS_MAIL );
								msPstmt2.setString(pSeq++, sBUYE_PART_IDEN );
								msPstmt2.setString(pSeq++, sBUYE_ORGN_NAME );
								msPstmt2.setString(pSeq++, sBUYE_PART_NAME );
								msPstmt2.setString(pSeq++, sBUYE_ADDR_TEXT );
								msPstmt2.setString(pSeq++, sBUYE_BUSI_TYPE );
								msPstmt2.setString(pSeq++, sBUYE_BUSI_CLAS );
								msPstmt2.setString(pSeq++, sBUYE_CONT_DEPT );
								msPstmt2.setString(pSeq++, sBUYE_PERS_NAME );
								msPstmt2.setString(pSeq++, sBUYE_PERS_TELE );
								msPstmt2.setString(pSeq++, sBUYE_PERS_MAIL );
								msPstmt2.setString(pSeq++, sSUMM_CHAR_AMOU );
								msPstmt2.setString(pSeq++, sSUMM_TAX0_AMOU );
								msPstmt2.setString(pSeq++, sSUMM_TOTA_AMOU );
								msPstmt2.setString(pSeq++, sMANA_OPPO_FLAG );
								msPstmt2.setString(pSeq++, sREG_ID         );
								msPstmt2.setString(pSeq++, sSEND_YN        );
								msPstmt2.setString(pSeq++, sMRO_SENDYN     );
								msPstmt2.setString(pSeq++, sBILL_TRAN_FLAG );
								msPstmt2.setString(pSeq++, sTRAN_FORM_IDEN );
								msPstmt2.setString(pSeq++, sHEAD_AMED_CODE );
								msPstmt2.setString(pSeq++, sHEAD_AMED_TEXT );
								msPstmt2.setString(pSeq++, sSUPP_MPOB_IDEN );
								msPstmt2.setString(pSeq++, sBUYE_IDEN_TYPE );
								msPstmt2.setString(pSeq++, sBUYE_MPOB_IDEN );
								msPstmt2.setString(pSeq++, sBUY2_CONT_DEPT );
								msPstmt2.setString(pSeq++, sBUY2_PERS_NAME );
								msPstmt2.setString(pSeq++, sBUY2_PERS_TELE );
								msPstmt2.setString(pSeq++, sBUY2_PERS_MAIL );
								msPstmt2.setString(pSeq++, sHEAD_DES2_TEXT );
								msPstmt2.setString(pSeq++, sBIZM_STND_VERS );
								msPstmt2.addBatch();	
								iInsCsbillMaster++;

								log.Write(log_flag, prgm_nm, "happynarae item data select");
								pSeq = 1;
								db.ps2.setString(pSeq++, sHEAD_MANA_IDEN);
								db.prs2 = db.ps2.executeQuery();
								
								if(db.prs2 != null){									
									while(db.prs2.next()){
										String sLIST_SEQU_IDEN = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_SEQU_IDEN"    ));
										String sLIST_TRAN_DATE = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_TRAN_DATE"    ));
										String sLIST_ITEM_NAME = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_ITEM_NAME"    ));
										String sLIST_DESC_TEXT = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_DESC_TEXT"    ));
										String sLIST_ITEM_AMOU = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_ITEM_AMOU"    ));
										String sLIST_TOTA_AMOU = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_TOTA_AMOU"    ));
										String sLIST_TAX0_AMOU = comm.OkMroUtil.checkNull(db.prs2.getString("LIST_TAX0_AMOU"    ));
										
										
										pSeq = 1;
										msPstmt3.setString(pSeq++, sHEAD_MANA_IDEN );
										msPstmt3.setString(pSeq++, sLIST_SEQU_IDEN );
										msPstmt3.setString(pSeq++, sLIST_TRAN_DATE );
										msPstmt3.setString(pSeq++, sLIST_ITEM_NAME );
										msPstmt3.setString(pSeq++, sLIST_DESC_TEXT );
										msPstmt3.setString(pSeq++, sLIST_ITEM_AMOU );
										msPstmt3.setString(pSeq++, sLIST_TOTA_AMOU );
										msPstmt3.setString(pSeq++, sLIST_TAX0_AMOU );
										msPstmt3.addBatch();	
										iInsCsbillItem++;
									}
								}else{
									throw new SQLException("happynarae item select error!!"+db.ERR_MSG);	
								}
								
								log.Write(log_flag, prgm_nm, "happynarae tran data select");
								pSeq = 1;
								db.ps3.setString(pSeq++, sHEAD_MANA_IDEN);
								db.prs3 = db.ps3.executeQuery();
								
								if(db.prs3 != null){
									while(db.prs3.next()){
										String sTRAN_SEQ        = comm.OkMroUtil.checkNull(db.prs3.getString("TRAN_SEQ"          ));
										String sLIST_ITEM_CODE  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_CODE"    ));
										String sLIST_ITEM_NAME  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_NAME"    ));
										String sLIST_DEFI_TEXT  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_DEFI_TEXT"    ));
										String sLIST_UNIT_TEXT  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_UNIT_TEXT"    ));
										String sLIST_ITEM_QUAN  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_QUAN"    ));
										String sLIST_BASI_AMOU  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_BASI_AMOU"    ));
										String sLIST_ITEM_AMOU  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_AMOU"    ));
										String sLIST_ITEM_ATTR1 = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_ATTR1"   ));
										String sLIST_ITEM_ATTR2 = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_ATTR2"   ));
										String sLIST_ITEM_ATTR3 = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_ATTR3"   ));
										String sLIST_ITEM_ATTR4 = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_ATTR4"   ));
										String sLIST_ITEM_ATTR5 = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_ATTR5"   ));
										String sLIST_ITEM_ATTR6 = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_ITEM_ATTR6"   ));
										String sLIST_DESC_TEXT  = comm.OkMroUtil.checkNull(db.prs3.getString("LIST_DESC_TEXT"    ));
										
										
										pSeq = 1;
										msPstmt4.setString(pSeq++, sHEAD_MANA_IDEN  );
										msPstmt4.setString(pSeq++, sTRAN_SEQ        );
										msPstmt4.setString(pSeq++, sLIST_ITEM_CODE  );
										msPstmt4.setString(pSeq++, sLIST_ITEM_NAME  );
										msPstmt4.setString(pSeq++, sLIST_DEFI_TEXT  );
										msPstmt4.setString(pSeq++, sLIST_UNIT_TEXT  );
										msPstmt4.setString(pSeq++, sLIST_ITEM_QUAN  );
										msPstmt4.setString(pSeq++, sLIST_BASI_AMOU  );
										msPstmt4.setString(pSeq++, sLIST_ITEM_AMOU  );
										msPstmt4.setString(pSeq++, sLIST_ITEM_ATTR1 );
										msPstmt4.setString(pSeq++, sLIST_ITEM_ATTR2 );
										msPstmt4.setString(pSeq++, sLIST_ITEM_ATTR3 );
										msPstmt4.setString(pSeq++, sLIST_ITEM_ATTR4 );
										msPstmt4.setString(pSeq++, sLIST_ITEM_ATTR5 );
										msPstmt4.setString(pSeq++, sLIST_ITEM_ATTR6 );
										msPstmt4.setString(pSeq++, sLIST_DESC_TEXT  );
										msPstmt4.addBatch();	
										iInsCsbillTran++;
									}
								}else{
									throw new SQLException("happynarae tran select error!!"+db.ERR_MSG);	
								}
								
								log.Write(log_flag, prgm_nm, "happynarae master update batch add");
								pSeq = 1;
								db.ps.setString(pSeq++, sHEAD_MANA_IDEN );
								db.ps.addBatch();
								iUpdHappyMaster++;
								
							}
						}else{
							throw new SQLException("csbill master select error!!"+db.ERR_MSG);	
						}
					}
		        	
					//iInsCsbillMaster = 0;
					//iInsCsbillItem = 0;
					//iInsCsbillTran = 0;
					//iUpdHappyMaster = 0;
					
		        	//batch start
					log.Write(log_flag, prgm_nm, "csbill master batch start->"+iInsCsbillMaster);
		        	int regcount[]  = null;
					if(iInsCsbillMaster > 0){
						int insCntCsbillMaster = 0;
						regcount = msPstmt2.executeBatch();
						for (int j = 0; j < regcount.length; j++) { 
							if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
								insCntCsbillMaster++;
							}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
								throw new SQLException("csbill dti_mrok_master 등록 "+(j + 1)+" 번째 배치 실행 에러발생."); 
							}else { 
								insCntCsbillMaster += regcount[j]; 
							} 
						}
					}
					
					log.Write(log_flag, prgm_nm, "csbill item batch start->"+iInsCsbillItem);
					if(iInsCsbillItem > 0){
						int insCntCsbillItem = 0;
						regcount = msPstmt3.executeBatch();
						for (int j = 0; j < regcount.length; j++) { 
							if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
								insCntCsbillItem++;
							}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
								throw new SQLException("csbill dti_mrok_item 등록 "+(j + 1)+" 번째 배치 실행 에러발생."); 
							}else { 
								insCntCsbillItem += regcount[j]; 
							} 
						}
					}
					
					log.Write(log_flag, prgm_nm, "csbill tran batch start->"+iInsCsbillTran);
					if(iInsCsbillTran > 0){
						int insCntCsbillTran = 0;
						regcount = msPstmt4.executeBatch();
						for (int j = 0; j < regcount.length; j++) { 
							if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
								insCntCsbillTran++;
							}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
								throw new SQLException("csbill dti_mrok_tran 등록 "+(j + 1)+" 번째 배치 실행 에러발생."); 
							}else { 
								insCntCsbillTran += regcount[j]; 
							} 
						}
					}
					
					log.Write(log_flag, prgm_nm, "happynarae master batch start->"+iUpdHappyMaster);
					if(iUpdHappyMaster > 0){
						int updCntHappyMaster = 0;
						regcount = db.ps.executeBatch();
						for (int j = 0; j < regcount.length; j++) { 
							if (regcount[j] == PreparedStatement.SUCCESS_NO_INFO) { 
								updCntHappyMaster++;
							}else if (regcount[j] == PreparedStatement.EXECUTE_FAILED) { 
								throw new SQLException("행복나래 dti_mrok_master 수정 "+(j + 1)+" 번째 배치 실행 에러발생."); 
							}else { 
								updCntHappyMaster += regcount[j]; 
							} 
						}
					}
					
				}else{					
					throw new SQLException("happynarae master select error!!"+db.ERR_MSG);
				}
				

				log.Write(log_flag, prgm_nm, "happynarae commit db");
				db.commit();
				
				log.Write(log_flag, prgm_nm, "csbill commit db");
				if(msConn != null) msConn.commit();
				if(msConn2 != null) msConn2.commit();
				if(msConn3 != null) msConn3.commit();
				if(msConn4 != null) msConn4.commit();
				
				log.Write(log_flag, prgm_nm, "csbill db disconnection!!");
				if(msRs   !=null)msRs.close();
				if(msPstmt!=null)msPstmt.close();
				if(msConn !=null)msConn.close();
				
				if(msRs2   !=null)msRs2.close();
				if(msPstmt2!=null)msPstmt2.close();
				if(msConn2 !=null)msConn2.close();
				
				if(msRs3   !=null)msRs3.close();
				if(msPstmt3!=null)msPstmt3.close();
				if(msConn3 !=null)msConn3.close();
				
				if(msRs4   !=null)msRs4.close();
				if(msPstmt4!=null)msPstmt4.close();
				if(msConn4 !=null)msConn4.close();
            }catch(SQLException e){            	
                System.out.println("Error"+e);
                log.Write(log_flag, prgm_nm, ""+e);
                try{
                    System.out.println(e);
                    e.printStackTrace();
                     db.rollback();
                     if(msConn != null) msConn.rollback();
                     if(msConn2 != null) msConn2.rollback();
                     if(msConn3 != null) msConn3.rollback();
                     if(msConn4 != null) msConn4.rollback();
                 }catch(Exception e1){
                	 log.Write(log_flag, prgm_nm, e1.getMessage());
                	 e1.printStackTrace();
                 }
            }catch(Exception e){
                System.out.println("Error"+e);
                log.Write(log_flag, prgm_nm, ""+e);
                try{
                    System.out.println(e);
                    e.printStackTrace();
                     db.rollback();
                     if(msConn != null) msConn.rollback();
                     if(msConn2 != null) msConn2.rollback();
                     if(msConn3 != null) msConn3.rollback();
                     if(msConn4 != null) msConn4.rollback();
                 }catch(Exception e1){
                	 log.Write(log_flag, prgm_nm, e1.getMessage());
                	 e1.printStackTrace();
                 }
            }finally{
            	db.DB_DisConn();		
            	log.Write(log_flag, prgm_nm, "happynarae db disconnection!!");
            	try{
	    			if(msRs   !=null)msRs.close();
	    			if(msPstmt!=null)msPstmt.close();
	    			if(msConn !=null)msConn.close();
	    			
	    			if(msRs2   !=null)msRs2.close();
	    			if(msPstmt2!=null)msPstmt2.close();
	    			if(msConn2 !=null)msConn2.close();
	    			
	    			if(msRs3   !=null)msRs3.close();
	    			if(msPstmt3!=null)msPstmt3.close();
	    			if(msConn3 !=null)msConn3.close();
	    			
	    			if(msRs4   !=null)msRs4.close();
	    			if(msPstmt4!=null)msPstmt4.close();
	    			if(msConn4 !=null)msConn4.close();
	    			log.Write(log_flag, prgm_nm, "csbill db disconnection!!");
            	}catch(Exception e){
                    log.Write(log_flag, prgm_nm, ""+e);
            		e.printStackTrace();
            	}
				System.out.println("Db Dis_Conn !! ");
				log.Write(log_flag, prgm_nm, "CSBill Demon End!!");
            }
        }
}