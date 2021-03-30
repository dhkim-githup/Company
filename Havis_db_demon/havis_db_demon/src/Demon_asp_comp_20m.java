/*
 * 매일 1시간에 1번씩 ASP 고객사 자동 완료처리 데몬
 * 2021.03.05 dhkim , 쿼리에 'AND td.comp_dati_cust >= SYSDATE - 300' 추가하여 속도향상
 * 2021.03.30 dhkim , 쿼리에 'AND td.comp_dati_cust >= SYSDATE - 300' -> td.comp_dati 변경 , asp 변경, 쿼리 한부분 삭제처리  , maps 부분 삭제 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import comm.mk_log;
import comm.DB_Use_Db_Demon;

//빈즈 불러서 사용

public class  Demon_asp_comp_20m
{
		//static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
		static private DB_Use_Db_Demon db = new DB_Use_Db_Demon();		
		static private mk_log log = new mk_log();
        //생성자
        public Demon_asp_comp_20m(){}

        public static void main(String[] args)
        {
        	System.out.println("Program Start  !! ");
        	
    		int log_flag = 0;
    		String prgm_nm = "Comp demon 20 minutes";
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	//comm.OkMroUtil mcu = new comm.OkMroUtil(); 
        	
        	log.Write(log_flag, prgm_nm, "Comp Demon Start!!");
        	
        	PreparedStatement ps4 = null;
        	ResultSet prs4 = null;
        	
			Connection connKeps = null;
			CallableStatement csKeps = null;
			PreparedStatement psKeps = null;
			ResultSet rsKeps = null;

			Connection connNeps = null;
			CallableStatement csNeps = null;
			PreparedStatement psNeps = null;
			ResultSet rsNeps = null;

			Connection connSeps = null;
			CallableStatement csSeps = null;
			PreparedStatement psSeps = null;
			ResultSet rsSeps = null;
			
			String sDemonLog = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
							 + "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_asp_comp_20m_kimjs','START',SYSDATE, 'OK', NULL ) ";			
			
			
			//String userid, passwd, url = "";
        	int pSeq = 0;
        	
        	String strQry = "";
        	try{
        		db.CUST_ID = "M005769";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_asp_comp_20m.java";
    			db.DB_Conn();
    			
				log.Write(log_flag, prgm_nm, "====================ASP Cop comp process Start!!=======================");
        		

    			db.ps = db.conn.prepareStatement(sDemonLog);
    			db.ps.executeUpdate();		
    			db.conn.commit();
    			

				//if(db.ps != null) db.ps.close();
				//if(db.prs != null) db.prs.close();
								
    			log.Write(log_flag, prgm_nm, "happynarae comp data select!!");
    			StringBuffer sbSelList  = new StringBuffer();    			
    			
    			sbSelList.append("   SELECT '1COMP' gbn                                                                                  ").append("\n");
    			sbSelList.append("         ,td.ords_num                                                                                  ").append("\n");
    			sbSelList.append("         ,td.list_num                                                                                  ").append("\n");
    			sbSelList.append("         ,td.tra_seq                                                                                   ").append("\n");
    			sbSelList.append("         ,td.tra_num                                                                                   ").append("\n");
    			sbSelList.append("         ,TO_CHAR(td.comp_dati, 'yyyymmdd') comp_dati                                                  ").append("\n");
    			sbSelList.append("         ,NVL(td.comp, td.cnt) comp                                                                    ").append("\n");
    			sbSelList.append("         ,td.canl                                                                                      ").append("\n");
    			sbSelList.append("         ,td.refu                                                                                      ").append("\n");
    			sbSelList.append("         ,td.exch                                                                                      ").append("\n");
    			sbSelList.append("         ,od.acep_man                                                                                  ").append("\n");
    			sbSelList.append("         ,(SELECT MAX(TO_CHAR(y.tx_acco_dati, 'yyyymmdd'))                                             ").append("\n");
    			sbSelList.append("             FROM acco_led x, bill_led y                                                               ").append("\n");
    			sbSelList.append("            WHERE x.ords_num = td.ords_num                                                             ").append("\n");
    			sbSelList.append("              AND x.list_num = td.list_num                                                             ").append("\n");
    			sbSelList.append("              AND x.tra_seq = td.tra_seq                                                               ").append("\n");
    			sbSelList.append("              AND x.ords_ty = td.ords_ty                                                               ").append("\n");
    			sbSelList.append("              AND x.buy_sell_flag = '001'                                                              ").append("\n");
    			sbSelList.append("              AND x.acco_num = y.acco_num) tx_acco_dati                                                ").append("\n");
    			sbSelList.append("         ,NULL enpri_cd                                                                                ").append("\n");
    			sbSelList.append("         ,NULL db_user                                                                                 ").append("\n");
    			sbSelList.append("         ,NULL asp_ords_num                                                                            ").append("\n");
    			sbSelList.append("         ,NULL asp_list_num                                                                            ").append("\n");
    			sbSelList.append("     FROM ords_dtl od                                                                                  ").append("\n");
    			sbSelList.append("         ,tra_dtl td                                                                                   ").append("\n");
    			//sbSelList.append("         ,cust_dtl cd                                                                                  ").append("\n");
    			//sbSelList.append("         ,enpri_info ei                                                                                ").append("\n");
    			//sbSelList.append("         ,enpri_dtl ed                                                                                 ").append("\n");
    			sbSelList.append("    WHERE 1=1                                                                                          ").append("\n");
    			sbSelList.append("      AND od.ords_num = td.ords_num                                                                    ").append("\n");
    			sbSelList.append("      AND od.list_num = td.list_num                                                                    ").append("\n");
    			sbSelList.append("      AND od.upri = td.upri                                                                            ").append("\n");
    			sbSelList.append("      AND od.buy_pri = td.buy_pri                                                                      ").append("\n");
    			sbSelList.append("      AND td.comp_dati >= sysdate-300                                                                  ").append("\n");				
    			//sbSelList.append("      AND od.acep_id = cd.cust_id                                                                      ").append("\n");
    			//sbSelList.append("      AND cd.enpri_cd = ei.enpri_cd                                                                    ").append("\n");
    			//sbSelList.append("      AND cd.enpri_cd = ed.enpri_cd                                                                    ").append("\n");
    			//sbSelList.append("      AND cd.busip_seq = ed.busip_seq                                                                  ").append("\n");
    			sbSelList.append("      AND od.ords_kn IN ('003','005','013','015')                                                      ").append("\n");
    			//sbSelList.append("      AND NVL(od.asp_flag,'002') <> '001'                                                              ").append("\n");
    			sbSelList.append("      AND td.comp_dati_happy IS NULL                                                                   ").append("\n");
    			sbSelList.append("      AND nvl(td.comp_kn,'---') <> 'ACC'                                                               ").append("\n");
    			sbSelList.append("      AND td.comp_dati_cust IS NULL                                                                    ").append("\n");
    			sbSelList.append("      AND td.comp_dati IS NOT NULL                                                                     ").append("\n");
    			sbSelList.append("      AND EXISTS (SELECT 'x'                                                                           ").append("\n");
    			sbSelList.append("                    FROM acco_led x                                                                    ").append("\n");
    			sbSelList.append("                   WHERE x.ords_num = td.ords_num                                                      ").append("\n");
    			sbSelList.append("                     AND x.list_num = td.list_num                                                      ").append("\n");
    			sbSelList.append("                     AND x.tra_seq = td.tra_seq                                                        ").append("\n");
    			sbSelList.append("                     AND x.ords_ty = td.ords_ty                                                        ").append("\n");
    			sbSelList.append("                     AND x.buy_sell_flag = '001')                                                      ").append("\n");
    			sbSelList.append("    UNION                                                                                              ").append("\n");
    			sbSelList.append("   SELECT '1COMP' gbn                                                                                  ").append("\n");
    			sbSelList.append("         ,td.ords_num                                                                                  ").append("\n");
    			sbSelList.append("         ,td.list_num                                                                                  ").append("\n");
    			sbSelList.append("         ,td.tra_seq                                                                                   ").append("\n");
    			sbSelList.append("         ,td.tra_num                                                                                   ").append("\n");
    			sbSelList.append("         ,TO_CHAR(td.comp_dati, 'yyyymmdd') comp_dati                                                  ").append("\n");
    			sbSelList.append("         ,NVL(td.comp, td.cnt) comp                                                                    ").append("\n");
    			sbSelList.append("         ,td.canl                                                                                      ").append("\n");
    			sbSelList.append("         ,td.refu                                                                                      ").append("\n");
    			sbSelList.append("         ,td.exch                                                                                      ").append("\n");
    			sbSelList.append("         ,od.acep_man                                                                                  ").append("\n");
    			sbSelList.append("         ,(SELECT MAX(TO_CHAR(y.tx_acco_dati, 'yyyymmdd'))                                             ").append("\n");
    			sbSelList.append("             FROM acco_led x, bill_led y                                                               ").append("\n");
    			sbSelList.append("            WHERE x.ords_num = td.ords_num                                                             ").append("\n");
    			sbSelList.append("              AND x.list_num = td.list_num                                                             ").append("\n");
    			sbSelList.append("              AND x.tra_seq = td.tra_seq                                                               ").append("\n");
    			sbSelList.append("              AND x.ords_ty = td.ords_ty                                                               ").append("\n");
    			sbSelList.append("              AND x.buy_sell_flag = '001'                                                              ").append("\n");
    			sbSelList.append("              AND x.acco_num = y.acco_num) tx_acco_dati                                                ").append("\n");
    			sbSelList.append("         ,NULL enpri_cd                                                                                ").append("\n");
    			sbSelList.append("         ,NULL db_user                                                                                 ").append("\n");
    			sbSelList.append("         ,NULL asp_ords_num                                                                            ").append("\n");
    			sbSelList.append("         ,NULL asp_list_num                                                                            ").append("\n");
    			sbSelList.append("     FROM ords_dtl od                                                                                  ").append("\n");
    			sbSelList.append("         ,tra_dtl td                                                                                   ").append("\n");
    			sbSelList.append("         ,cust_dtl cd                                                                                  ").append("\n");
    			sbSelList.append("         ,enpri_info ei                                                                                ").append("\n");
    			sbSelList.append("         ,enpri_dtl ed                                                                                 ").append("\n");
    			sbSelList.append("    WHERE 1=1                                                                                          ").append("\n");
    			sbSelList.append("      AND od.ords_num = td.ords_num                                                                    ").append("\n");
    			sbSelList.append("      AND od.list_num = td.list_num                                                                    ").append("\n");
    			sbSelList.append("      AND od.upri = td.upri                                                                            ").append("\n");
    			sbSelList.append("      AND od.buy_pri = td.buy_pri                                                                      ").append("\n");
    			sbSelList.append("      AND td.comp_dati >= sysdate-300                                                                  ").append("\n");								
    			sbSelList.append("      AND od.acep_id = cd.cust_id                                                                      ").append("\n");
    			sbSelList.append("      AND cd.enpri_cd = ei.enpri_cd                                                                    ").append("\n");
    			sbSelList.append("      AND cd.enpri_cd = ed.enpri_cd                                                                    ").append("\n");
    			sbSelList.append("      AND cd.busip_seq = ed.busip_seq                                                                  ").append("\n");
    			sbSelList.append("      AND ei.enpri_cd IN ('A43773','A44160','A51698')                                                  ").append("\n");
    			sbSelList.append("      AND ( od.cust_ord_num LIKE '4500%'  OR                                                           ").append("\n");
    			sbSelList.append("            (ei.enpri_cd IN ('A43773') AND  od.cust_ord_num IS NULL ) )                                ").append("\n");
    			sbSelList.append("      AND od.ords_kn IN ('003','005','013','015')                                                      ").append("\n");
    			//sbSelList.append("      AND NVL(od.asp_flag,'002') <> '001'                                                              ").append("\n");
    			sbSelList.append("      AND NVL(ed.tra_proc_ty,'002') = '001'                                                            ").append("\n");
    			sbSelList.append("      AND td.comp_dati_happy IS NULL                                                                   ").append("\n");
    			sbSelList.append("      AND nvl(td.comp_kn,'---') <> 'ACC'                                                               ").append("\n");
    			sbSelList.append("      AND td.comp_dati_cust IS NULL                                                                    ").append("\n");
    			sbSelList.append("      AND td.comp_dati IS NOT NULL                                                                     ").append("\n");
    			sbSelList.append("      AND EXISTS (SELECT 'x'                                                                           ").append("\n");
    			sbSelList.append("                    FROM acco_led x                                                                    ").append("\n");
    			sbSelList.append("                   WHERE x.ords_num = td.ords_num                                                      ").append("\n");
    			sbSelList.append("                     AND x.list_num = td.list_num                                                      ").append("\n");
    			sbSelList.append("                     AND x.tra_seq = td.tra_seq                                                        ").append("\n");
    			sbSelList.append("                     AND x.ords_ty = td.ords_ty                                                        ").append("\n");
    			sbSelList.append("                     AND x.buy_sell_flag = '001')                                                      ").append("\n");
//    			sbSelList.append("    UNION ALL                                                                                          ").append("\n");
//    			sbSelList.append("   SELECT '2ASP' gbn                                                                                   ").append("\n");
//    			sbSelList.append("         ,td.ords_num                                                                                  ").append("\n");
//    			sbSelList.append("         ,td.list_num                                                                                  ").append("\n");
//    			sbSelList.append("         ,td.tra_seq                                                                                   ").append("\n");
//    			sbSelList.append("         ,td.tra_num                                                                                   ").append("\n");
//    			sbSelList.append("         ,TO_CHAR(td.comp_dati, 'yyyymmdd') comp_dati                                                  ").append("\n");
//    			sbSelList.append("         ,td.comp                                                                                      ").append("\n");
//    			sbSelList.append("         ,td.canl                                                                                      ").append("\n");
//    			sbSelList.append("         ,td.refu                                                                                      ").append("\n");
//    			sbSelList.append("         ,td.exch                                                                                      ").append("\n");
//    			sbSelList.append("         ,td.acep_man                                                                                  ").append("\n");
//    			sbSelList.append("         ,(SELECT MAX(TO_CHAR(y.tx_acco_dati, 'yyyymmdd'))                                             ").append("\n");
//    			sbSelList.append("             FROM acco_led x, bill_led y                                                               ").append("\n");
//    			sbSelList.append("            WHERE x.ords_num = td.ords_num                                                             ").append("\n");
//    			sbSelList.append("              AND x.list_num = td.list_num                                                             ").append("\n");
//    			sbSelList.append("              AND x.tra_seq = td.tra_seq                                                               ").append("\n");
//    			sbSelList.append("              AND x.ords_ty = td.ords_ty                                                               ").append("\n");
//    			sbSelList.append("              AND x.buy_sell_flag = '001'                                                              ").append("\n");
//    			sbSelList.append("              AND x.acco_num = y.acco_num) tx_acco_dati                                                ").append("\n");
//    			sbSelList.append("         ,SUBSTR (od.cust_ord_num, 1, 6) enpri_cd                                                      ").append("\n");
//    			sbSelList.append("         ,(SELECT x.db_user FROM enpri_info x                                                          ").append("\n");
//    			sbSelList.append("            WHERE x.enpri_cd = ed.enpri_cd) db_user                                                    ").append("\n");
//    			sbSelList.append("         ,SUBSTR (od.cust_ord_num, 8, 7) asp_ords_num                                                  ").append("\n");
//    			sbSelList.append("         ,SUBSTR (od.cust_ord_num, 16) asp_list_num                                                    ").append("\n");
//    			sbSelList.append("     FROM ords_dtl od                                                                                  ").append("\n");
//    			sbSelList.append("         ,tra_dtl td                                                                                   ").append("\n");
//    			sbSelList.append("         ,cust_dtl cd                                                                                  ").append("\n");
//    			sbSelList.append("         ,enpri_dtl ed                                                                                 ").append("\n");
//    			sbSelList.append("    WHERE 1=1                                                                                          ").append("\n");
//    			sbSelList.append("      AND od.ords_num = td.ords_num                                                                    ").append("\n");
//    			sbSelList.append("      AND od.list_num = td.list_num                                                                    ").append("\n");
//    			sbSelList.append("      AND od.upri = td.upri                                                                            ").append("\n");
//    			sbSelList.append("      AND od.buy_pri = td.buy_pri                                                                      ").append("\n");
//    			sbSelList.append("      AND td.comp_dati >= sysdate-300                                                                  ").append("\n");								
//    			sbSelList.append("      AND od.acep_id = cd.cust_id                                                                      ").append("\n");
//    			sbSelList.append("      AND cd.enpri_cd = ed.enpri_cd                                                                    ").append("\n");
//    			sbSelList.append("      AND cd.busip_seq = ed.busip_seq                                                                  ").append("\n");
//    			sbSelList.append("      AND od.ords_kn IN ('003','009')                                                                  ").append("\n");
//    			sbSelList.append("      AND od.asp_flag = '001'                                                                          ").append("\n");
//    			sbSelList.append("      AND NVL(ed.tra_proc_ty,'002') <> '001'                                                           ").append("\n");
//    			sbSelList.append("      AND td.comp_dati_happy IS NULL                                                                   ").append("\n");
//    			sbSelList.append("      AND nvl(td.comp_kn,'---') <> 'ACC'                                                               ").append("\n");
//    			sbSelList.append("      AND td.comp_dati_cust IS NULL                                                                    ").append("\n");
//    			sbSelList.append("      AND td.comp_dati IS NOT NULL                                                                     ").append("\n");
//    			sbSelList.append("      AND EXISTS (SELECT 'x'                                                                           ").append("\n");
//    			sbSelList.append("                    FROM acco_led x                                                                    ").append("\n");
//    			sbSelList.append("                   WHERE x.ords_num = td.ords_num                                                      ").append("\n");
//    			sbSelList.append("                     AND x.list_num = td.list_num                                                      ").append("\n");
//    			sbSelList.append("                     AND x.tra_seq = td.tra_seq                                                        ").append("\n");
//    			sbSelList.append("                     AND x.ords_ty = td.ords_ty                                                        ").append("\n");
//    			sbSelList.append("                     AND x.buy_sell_flag = '001')                                                      ").append("\n");
    			sbSelList.append("    UNION ALL                                                                                          ").append("\n");    			
    			sbSelList.append("   SELECT '3BACK' gbn                                                                                  ").append("\n");
    			sbSelList.append("         ,A.ords_num                                                                                   ").append("\n");
    			sbSelList.append("         ,A.list_num                                                                                   ").append("\n");
    			sbSelList.append("         ,A.tra_seq                                                                                    ").append("\n");
    			sbSelList.append("         ,NULL tra_num                                                                                 ").append("\n");
    			sbSelList.append("         ,NULL comp_dati                                                                               ").append("\n");
    			sbSelList.append("         ,NULL comp                                                                                    ").append("\n");
    			sbSelList.append("         ,NULL canl                                                                                    ").append("\n");
    			sbSelList.append("         ,NULL refu                                                                                    ").append("\n");
    			sbSelList.append("         ,NULL exch                                                                                    ").append("\n");
    			sbSelList.append("         ,NULL acep_man                                                                                ").append("\n");
    			sbSelList.append("         ,NULL tx_acco_dati                                                                            ").append("\n");
    			sbSelList.append("         ,NULL enpri_cd                                                                                ").append("\n");
    			sbSelList.append("         ,NULL db_user                                                                                 ").append("\n");
    			sbSelList.append("         ,NULL asp_ords_num                                                                            ").append("\n");
    			sbSelList.append("         ,NULL asp_list_num                                                                            ").append("\n");
    			sbSelList.append("     FROM tra_dtl A                                                                                    ").append("\n");
    			sbSelList.append("         ,ords_dtl b                                                                                   ").append("\n");
    			sbSelList.append("         ,enpri_dtl c                                                                                  ").append("\n");
    			sbSelList.append("         ,cust_dtl d                                                                                   ").append("\n");
    			sbSelList.append("    WHERE 1=1                                                                                          ").append("\n");
    			sbSelList.append("      AND A.ords_num = b.ords_num                                                                      ").append("\n");
    			sbSelList.append("      AND A.list_num = b.list_num                                                                      ").append("\n");
    			sbSelList.append("      AND a.upri = b.upri                                                                              ").append("\n");
    			sbSelList.append("      AND a.buy_pri = b.buy_pri                                                                        ").append("\n");
    			sbSelList.append("      AND b.acep_id = d.cust_id                                                                        ").append("\n");
    			sbSelList.append("      AND d.enpri_cd = c.enpri_cd                                                                      ").append("\n");
    			sbSelList.append("      AND d.busip_seq = c.busip_seq                                                                    ").append("\n");
    			//sbSelList.append("      AND NVL(b.asp_flag,'002') <> '001'                                                               ").append("\n");
    			sbSelList.append("      AND NVL(c.tra_proc_ty,'002') <> '001'                                                            ").append("\n");
    			sbSelList.append("      AND A.comp_kn = 'ACC'                                                                            ").append("\n");
    			sbSelList.append("      AND A.comp_dati_cust >= sysdate-300                                                              ").append("\n");
    			sbSelList.append("      AND NOT EXISTS (SELECT 'x'                                                                       ").append("\n");
    			sbSelList.append("                        FROM acco_led x                                                                ").append("\n");
    			sbSelList.append("                       WHERE x.ords_num = A.ords_num                                                   ").append("\n");
    			sbSelList.append("                         AND x.list_num = A.list_num                                                   ").append("\n");
    			sbSelList.append("                         AND x.tra_seq = A.tra_seq                                                     ").append("\n");
    			sbSelList.append("                         AND x.ords_ty = A.ords_ty                                                     ").append("\n");
    			sbSelList.append("                         AND x.buy_sell_flag IN ('001','002'))                                         ").append("\n");
    			sbSelList.append("    ORDER BY 1,2,3                                                                                     ").append("\n");
    			
    			strQry = sbSelList.toString(); 			
				log.Write(log_flag, prgm_nm, strQry);
				//db.prepareStatement(strQry); 
				//db.PexecuteQuery();
				db.ps = db.conn.prepareStatement(strQry);
				db.prs = db.ps.executeQuery();
				
				List<Object> lData = new ArrayList<Object>();
				if(db.prs != null){
					log.Write(log_flag, prgm_nm, "happynarae put comp data!!");
					
					while(db.prs.next()){
						
						HashMap<String,String> hm = new HashMap<String,String>();
						
						log.Write(log_flag, prgm_nm, "comp data selected : ords_num-->"+comm.OkMroUtil.checkNull(db.prs.getString("ords_num"  )));
						hm.put("gbn"          ,comm.OkMroUtil.checkNull(db.prs.getString("gbn"         )));
						hm.put("ords_num"     ,comm.OkMroUtil.checkNull(db.prs.getString("ords_num"    )));
						hm.put("list_num"     ,comm.OkMroUtil.checkNull(db.prs.getString("list_num"    )));
						hm.put("tra_seq"      ,comm.OkMroUtil.checkNull(db.prs.getString("tra_seq"     )));
						hm.put("tra_num"      ,comm.OkMroUtil.checkNull(db.prs.getString("tra_num"     )));
						hm.put("comp_dati"    ,comm.OkMroUtil.checkNull(db.prs.getString("comp_dati"   )));
						hm.put("comp"         ,comm.OkMroUtil.checkNull(db.prs.getString("comp"        )));
						hm.put("canl"         ,comm.OkMroUtil.checkNull(db.prs.getString("canl"        )));
						hm.put("refu"         ,comm.OkMroUtil.checkNull(db.prs.getString("refu"        )));
						hm.put("exch"         ,comm.OkMroUtil.checkNull(db.prs.getString("exch"        )));
						hm.put("acep_man"     ,comm.OkMroUtil.checkNull(db.prs.getString("acep_man"    )));
						hm.put("tx_acco_dati" ,comm.OkMroUtil.checkNull(db.prs.getString("tx_acco_dati")));
						hm.put("enpri_cd"     ,comm.OkMroUtil.checkNull(db.prs.getString("enpri_cd"    )));
						hm.put("db_user"      ,comm.OkMroUtil.checkNull(db.prs.getString("db_user"     )));
						hm.put("asp_ords_num" ,comm.OkMroUtil.checkNull(db.prs.getString("asp_ords_num")));
						hm.put("asp_list_num" ,comm.OkMroUtil.checkNull(db.prs.getString("asp_list_num")));
						
						lData.add(hm);
					}
				}else{
					throw new SQLException("happynarae comp data select error");
				}
				
				//if(db.ps != null) db.ps.close();
				//if(db.prs != null) db.prs.close();
					
				//db.DB_DisConn();
				
				//db.DB_Conn();
				
				int iDataCnt = lData.size();
				
				if(iDataCnt > 0){
					
					StringBuffer sbUpdCompDatiCust = new StringBuffer();
					sbUpdCompDatiCust.append("    UPDATE tra_dtl                     ").append("\n");
					sbUpdCompDatiCust.append("       SET comp_dati_happy = to_date(?,'yyyy-mm-dd')         ").append("\n");
					sbUpdCompDatiCust.append("          ,comp_kn = 'ACC'             ").append("\n");
					sbUpdCompDatiCust.append("          ,sys_memo = 'Demon_asp_comp_20m 에서 ASP 업체 매출 확정된 SD 주문서 고객 완료일자 처리함'  ").append("\n");
					sbUpdCompDatiCust.append("     WHERE ords_num = ?                ").append("\n");
					sbUpdCompDatiCust.append("       AND list_num = ?                ").append("\n");
					sbUpdCompDatiCust.append("       AND tra_seq  = ?                ").append("\n");
	    			db.ps = db.conn.prepareStatement(sbUpdCompDatiCust.toString());		
					
	    			
	    			String comp_qry = "{?= call p_tra_mng.SCO_DELI_END(?,?,?,?,?,?,?,?,?,?,'1')}";
	    			db.cs2 = db.conn.prepareCall(comp_qry);
					
					
					StringBuffer sbUpdCnclDatiHappy = new StringBuffer();
					sbUpdCnclDatiHappy.append("    UPDATE tra_dtl                     ").append("\n");
					sbUpdCnclDatiHappy.append("       SET comp_dati_cust = null  ").append("\n");
					sbUpdCnclDatiHappy.append("          ,comp_dati_happy = null ").append("\n");
					sbUpdCnclDatiHappy.append("          ,comp_kn = null             ").append("\n");
					sbUpdCnclDatiHappy.append("          ,sell_dati = null             ").append("\n");
					sbUpdCnclDatiHappy.append("          ,buy_dati = null             ").append("\n");
					sbUpdCnclDatiHappy.append("          ,sys_memo = 'Demon_asp_comp_20m 에서 고객사 매출 확정되었으나 매출 삭제로 인해 주문서 고객 완료일자 취소 처리함'  ").append("\n");
					sbUpdCnclDatiHappy.append("     WHERE ords_num = ?                ").append("\n");
					sbUpdCnclDatiHappy.append("       AND list_num = ?                ").append("\n"); 
					sbUpdCnclDatiHappy.append("       AND tra_seq  = ?                ").append("\n");
	    			db.ps2 = db.conn.prepareStatement(sbUpdCnclDatiHappy.toString());	
	    			
	    			
	    			StringBuffer sbUpdCnclProcHappy = new StringBuffer();
	    			sbUpdCnclProcHappy.append("    UPDATE ords_dtl                     ").append("\n");
	    			sbUpdCnclProcHappy.append("       SET proc_ty = '004'  ").append("\n");
	    			sbUpdCnclProcHappy.append("          ,sys_memo = 'Demon_asp_comp_20m 에서 고객사 매출 확정되었으나 매출 삭제로 인해 주문서 완료처리 배송중으로 변경함'  ").append("\n");
	    			sbUpdCnclProcHappy.append("     WHERE ords_num = ?                ").append("\n");
	    			sbUpdCnclProcHappy.append("       AND list_num = ?                ").append("\n");
	    			db.ps3 = db.conn.prepareStatement(sbUpdCnclProcHappy.toString());
	    			
	    			StringBuffer sbUpdTxAccoDati = new StringBuffer();
	    			sbUpdTxAccoDati.append("    UPDATE tra_dtl                     ").append("\n");
	    			sbUpdTxAccoDati.append("       SET comp_dati_cust = ?  ").append("\n");
	    			sbUpdTxAccoDati.append("          ,sys_memo = 'Demon_asp_comp_20m 에서 고객사 매출 확정일자 변경으로 인한 거래명세서 고객완료일자 수정'  ").append("\n");
	    			sbUpdTxAccoDati.append("     WHERE ords_num = ?                ").append("\n");
	    			sbUpdTxAccoDati.append("       AND list_num = ?                ").append("\n");
	    			sbUpdTxAccoDati.append("       AND tra_seq  = ?                ").append("\n");
	    			ps4 = db.conn.prepareStatement(sbUpdTxAccoDati.toString());
					
	    			
					for(int ii=0; ii<lData.size(); ii++){
						log.Write(log_flag, prgm_nm, "loop count -->"+ii);
						String gbn          = (String)((HashMap)lData.get(ii)).get("gbn");
						String ords_num     = (String)((HashMap)lData.get(ii)).get("ords_num");
						String list_num     = (String)((HashMap)lData.get(ii)).get("list_num");
						String tra_seq      = (String)((HashMap)lData.get(ii)).get("tra_seq" );
						String tra_num      = (String)((HashMap)lData.get(ii)).get("tra_num" );
						String enpri_cd     = (String)((HashMap)lData.get(ii)).get("enpri_cd");
						String db_user      = (String)((HashMap)lData.get(ii)).get("db_user" );
						String asp_ords_num = (String)((HashMap)lData.get(ii)).get("asp_ords_num");
						String asp_list_num = (String)((HashMap)lData.get(ii)).get("asp_list_num");
						String comp         = (String)((HashMap)lData.get(ii)).get("comp");
						String canl         = (String)((HashMap)lData.get(ii)).get("canl");
						String refu         = (String)((HashMap)lData.get(ii)).get("refu");
						String exch         = (String)((HashMap)lData.get(ii)).get("exch");
						String comp_dati    = (String)((HashMap)lData.get(ii)).get("comp_dati");
						String acep_man     = (String)((HashMap)lData.get(ii)).get("acep_man");
						String tx_acco_dati = (String)((HashMap)lData.get(ii)).get("tx_acco_dati");
						
						comp = "".equals(comp) || comp == null ? "0" : comp;
						canl = "".equals(canl) || canl == null ? "0" : canl;
						refu = "".equals(refu) || refu == null ? "0" : refu;
						exch = "".equals(exch) || exch == null ? "0" : exch;
						
						System.out.println(ords_num+"<>"+list_num+"<>"+tra_seq);
						log.Write(log_flag, prgm_nm, "process type:"+gbn);
						if(!"3BACK".equals(gbn)){
							
							if(!"4ACCO".equals(gbn)){
							
								log.Write(log_flag, prgm_nm, "non 3BACK happynarae tra info procedure call!! update tra_dtl");
								
				    			pSeq = 1;
				    			db.ps.setString(pSeq++, tx_acco_dati       );
				    			db.ps.setString(pSeq++, ords_num           );
				    			db.ps.setString(pSeq++, list_num           );
				    			db.ps.setString(pSeq++, tra_seq            );
				    			
				    			db.ps.executeUpdate();	
				    			
				    			int err_cnt = 0;

				    			log.Write(log_flag, prgm_nm, "non 3BACK happynarae tra info procedure call!! sco_deli_comp");
								
								db.cs2.registerOutParameter(1,Types.INTEGER);
								db.CsetString2(2, ords_num);
								db.CsetDouble2(3, Double.parseDouble(list_num));
								db.CsetString2(4, tra_num);
								db.CsetDouble2(5, Double.parseDouble(tra_seq));
								db.CsetDouble2(6, Double.parseDouble(comp));
								db.CsetDouble2(7, Double.parseDouble(canl));
								db.CsetDouble2(8, Double.parseDouble(refu));
								db.CsetDouble2(9, Double.parseDouble(exch));
								db.CsetString2(10, tx_acco_dati);						
								db.CsetString2(11, acep_man); // 인수자
																	
								try{
									db.cs2.execute();
								}catch(Exception e){
									err_cnt = -1;
								}
								
								log.Write(log_flag, prgm_nm, "result SCO_DELI_END-->"+err_cnt);

								if(err_cnt < 0) {
									log.Write(log_flag, prgm_nm, "non 3BACK happynarae rollback db '"+ords_num+"','"+list_num+"',  '"+tra_num+"','"+tra_seq+"',"+comp+","+canl+","+refu+","+exch+",'"+tx_acco_dati+"','"+acep_man+"'");
									db.rollback();
									//throw new SQLException("happynarae asp p_tra_mng.SCO_DELI_END procedure error");
								}else{
									int nRet = db.cs2.getInt(1);
									
									if("2ASP".equals(gbn)){
									
										log.Write(log_flag, prgm_nm, "happynarae ASP tra info procedure call!!");
										
										StringBuffer sbGetAspTraInfo = new StringBuffer();
										sbGetAspTraInfo.append("{call TRAS.P_GET_ASP_TRA_INFO(?,?,?,?,?,?,?,?)}").append("\n");
										strQry = sbGetAspTraInfo.toString(); 
										db.cs = db.conn.prepareCall(strQry);
										
										pSeq = 1;
										db.CsetString(pSeq++, ords_num);
										db.CsetString(pSeq++, list_num);
										db.CsetString(pSeq++, tra_seq );
										db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_ords_num       
										db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_list_num 
										db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_tra_seq       
										db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_tra_num 	       
										db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_res
										
										db.cs.execute();
										
										if(db.ERR_FLAG < 0){
											log.Write(log_flag, prgm_nm, "happynarae ASP rollback db '"+ords_num+"','"+list_num+"',  '"+tra_num+"','"+tra_seq+"'");
											throw new SQLException("happynarae ASP tra info procedure call error");
										}
										
										String asp_ords_num_tmp = db.cs.getString(4);
										String asp_list_num_tmp = db.cs.getString(5);
										String asp_tra_seq      = db.cs.getString(6);
										String asp_tra_num      = db.cs.getString(7);
										String sRes             = db.cs.getString(8);
										
										log.Write(log_flag, prgm_nm,asp_tra_seq);
										log.Write(log_flag, prgm_nm,asp_tra_num);
										log.Write(log_flag, prgm_nm,sRes);
				
										if(!"EXIST".equals(sRes)){
											throw new SQLException("happynarae asp tra info procedure no data error");
										}
										
										log.Write(log_flag, prgm_nm, "asp tra comp procedure call!!");
										StringBuffer sbSetAspTraInfo = new StringBuffer();
										sbSetAspTraInfo.append("{call TRAS.P_AUTO_DELI_COMP(?,?,?,?,?,?,?,?,?,?,?,'','',?)}").append("\n");
										String strAspQry = sbSetAspTraInfo.toString(); 
										
										if("keps".equals(db_user)){
											
										}else if("NEPS".equals(db_user)){
											
										}else if("seps".equals(db_user)){
											
										}else{
											throw new SQLException("there no db_user for "+db_user+"!!"+db.ERR_MSG);
										}
										
										log.Write(log_flag, prgm_nm, "happynarae non 3BACK, 2ASP commit db");
										db.commit();
										/*
										if(db.ps != null) db.ps.close();
										if(db.prs != null) db.prs.close();
										if(db.cs != null) db.cs.close();
										if(db.ps2 != null) db.ps2.close();
										if(db.prs2 != null) db.prs2.close();
										if(db.cs2 != null) db.cs2.close();
										if(db.ps3 != null) db.ps3.close();
										if(db.prs3 != null) db.prs3.close();
										
										log.Write(log_flag, prgm_nm, "asp db disconnection!!");
										if(csKeps!=null)csKeps.close();
										if(psKeps!=null)psKeps.close();
										if(rsKeps!=null)rsKeps.close();
										
										if(csNeps!=null)csNeps.close();
										if(psNeps!=null)psNeps.close();
										if(rsNeps!=null)rsNeps.close();
										
										if(csSeps!=null)csSeps.close();
										if(psSeps!=null)psSeps.close();
										if(rsSeps!=null)rsSeps.close();
										*/
									}else{
										log.Write(log_flag, prgm_nm, "happynarae non 3BACK, 1COMP commit db");
										db.commit();
										/*
										if(db.ps != null) db.ps.close();
										if(db.prs != null) db.prs.close();
										if(db.cs != null) db.cs.close();
										if(db.ps2 != null) db.ps2.close();
										if(db.prs2 != null) db.prs2.close();
										if(db.cs2 != null) db.cs2.close();
										if(db.ps3 != null) db.ps3.close();
										if(db.prs3 != null) db.prs3.close();
										*/
									}
								}
							}else{
								
								log.Write(log_flag, prgm_nm, "4ACCO happynarae tra info update!!");
								log.Write(log_flag, prgm_nm, "order info : "+ords_num+"-"+list_num+"-"+tra_seq+"-"+tx_acco_dati);
				    			
				    			pSeq = 1;
				    			ps4.setString(pSeq++, tx_acco_dati       );
				    			ps4.setString(pSeq++, ords_num           );
				    			ps4.setString(pSeq++, list_num           );
				    			ps4.setString(pSeq++, tra_seq            );
				    			
				    			ps4.executeUpdate();		
				    			
				    			log.Write(log_flag, prgm_nm, "happynarae 4ACCO commit db");
								db.commit();
								
							}
							
						}else{
							log.Write(log_flag, prgm_nm, "3BACK happynarae tra info procedure call!!");
							log.Write(log_flag, prgm_nm, "order info : "+ords_num+"-"+list_num+"-"+tra_seq);
			    			
			    			pSeq = 1;
			    			db.PsetString2(pSeq++, ords_num           );
			    			db.PsetString2(pSeq++, list_num           );
			    			db.PsetString2(pSeq++, tra_seq            );
			    			
			    			db.ps2.executeUpdate();		
			    			
			    			
			    			pSeq = 1;
			    			db.PsetString3(pSeq++, ords_num           );
			    			db.PsetString3(pSeq++, list_num           );
			    			
			    			db.ps3.executeUpdate();		
			    			
			    			log.Write(log_flag, prgm_nm, "happynarae 3BACK commit db");
							db.commit();
							/*
							if(db.ps != null) db.ps.close();
							if(db.prs != null) db.prs.close();
							if(db.cs != null) db.cs.close();
							if(db.ps2 != null) db.ps2.close();
							if(db.prs2 != null) db.prs2.close();
							if(db.cs2 != null) db.cs2.close();
							if(db.ps3 != null) db.ps3.close();
							if(db.prs3 != null) db.prs3.close();
							*/
						}
					}
				}
				
				sDemonLog = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
						 + "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_asp_comp_20m_kimjs','END',SYSDATE, 'OK', NULL ) ";					
				db.ps = db.conn.prepareStatement(sDemonLog);		
    			db.ps.executeUpdate();		
    			
				log.Write(log_flag, prgm_nm, "====================ASP Cop comp process End!!=======================");
				
            }catch(SQLException e){            	
                System.out.println("Error-->"+e);
                log.Write(log_flag, prgm_nm, ""+e);
                try{                	                	
            		System.out.println(e);
                    e.printStackTrace();
                    db.rollback();
                    //if(connKeps != null) connKeps.rollback();
                    //if(connNeps != null) connNeps.rollback();
                    //if(connSeps != null) connSeps.rollback();
                    
    				sDemonLog = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
      						 + "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_asp_comp_20m_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) ";	
           			db.ps = db.conn.prepareStatement(sDemonLog);		
           			db.ps.executeUpdate();	
           			db.conn.commit();
                 }catch(Exception e1){
                	 log.Write(log_flag, prgm_nm, e1.getMessage());
                	 e1.printStackTrace();
                 }
            }catch(Exception e){
                System.out.println("Error-->"+e);
                log.Write(log_flag, prgm_nm, ""+e);
                try{
        			System.out.println(e);
                    e.printStackTrace();
                    db.rollback();
                    //if(connKeps != null) connKeps.rollback();
                    //if(connNeps != null) connNeps.rollback();
                    //if(connSeps != null) connSeps.rollback();
                    
                    sDemonLog = "  INSERT INTO demon_log(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "
                    		+ "  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_asp_comp_20m_kimjs','END',SYSDATE, 'ERR', '"+e.getMessage()+"' ) ";	
                    db.ps = db.conn.prepareStatement(sDemonLog);		
                    db.ps.executeUpdate();	       
           			db.conn.commit();         	
                 }catch(Exception e1){
                	 log.Write(log_flag, prgm_nm, e1.getMessage());
                	 e1.printStackTrace();
                 }
            }finally{
            	db.DB_DisConn();		
            	log.Write(log_flag, prgm_nm, "happynarae db disconnection!!");
            	try{

					if(ps4!=null)ps4.close();
					if(prs4!=null)prs4.close();
					
					if(csKeps!=null)csKeps.close();
					if(psKeps!=null)psKeps.close();
					if(rsKeps!=null)rsKeps.close();
					if(connKeps !=null)connKeps.close();
					
					if(csNeps!=null)csNeps.close();
					if(psNeps!=null)psNeps.close();
					if(rsNeps!=null)rsNeps.close();
					if(connNeps !=null)connNeps.close();
					
					if(csSeps!=null)csSeps.close();
					if(psSeps!=null)psSeps.close();
					if(rsSeps!=null)rsSeps.close();
					if(connSeps !=null)connSeps.close();
	    			log.Write(log_flag, prgm_nm, "ASP db disconnection!!");
            	}catch(Exception e){
                    log.Write(log_flag, prgm_nm, ""+e);
            		e.printStackTrace();
            	}
				System.out.println("Db Dis_Conn !! ");
				log.Write(log_flag, prgm_nm, "ASP Demon End!!");
            }
        }
}