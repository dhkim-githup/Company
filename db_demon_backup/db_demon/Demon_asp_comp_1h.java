/*
 * 매일 1시간에 1번씩 ASP 고객사 자동 완료처리 데몬
 */
//package comm;

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

/*
 
SELECT td.ords_num                                        
      ,td.list_num                                        
      ,td.tra_seq                                         
      ,td.tra_num                                         
      ,to_char(td.comp_dati, 'yyyymmdd') comp_dati        
      ,nvl(td.comp, td.cnt) comp                          
      ,td.canl                                            
      ,td.refu                                            
      ,td.exch                                            
      ,od.acep_man 
      ,(select to_char(y.tx_acco_dati, 'yyyymmdd')      
          from acco_led x, bill_led y                   
         where x.ords_num = td.ords_num               
           and x.list_num = td.list_num                
           and x.tra_seq = td.tra_seq                 
           and x.ords_ty = td.ords_ty                 
           and x.buy_sell_flag = '001'                
           and x.acco_num = y.acco_num) tx_acco_dati   
  FROM ords_dtl od                                        
      ,tra_dtl td                                         
      ,cust_dtl cd                                        
      ,enpri_info ei                                      
      ,enpri_dtl ed                                       
 WHERE 1=1                                                
   AND od.ords_num = td.ords_num                          
   AND od.list_num = td.list_num                          
   AND od.acep_id = cd.cust_id                            
   AND cd.enpri_cd = ei.enpri_cd                          
   AND cd.enpri_cd = ed.enpri_cd                          
   AND cd.busip_seq = ed.busip_seq                        
   AND od.ords_kn IN ('003','005','013','015')            
   AND nvl(od.asp_flag,'002') <> '001'                    
   AND nvl(ed.tra_proc_ty,'002') <> '001'                 
   AND td.comp_dati_cust IS NULL                          
   AND td.comp_dati IS NOT NULL                           
   AND EXISTS (SELECT 'x'                                 
                 FROM acco_led x                          
                WHERE x.ords_num = td.ords_num            
                  AND x.list_num = td.list_num            
                  AND x.tra_seq = td.tra_seq      
                  AND x.ords_ty = td.ords_ty        
                  AND x.buy_sell_flag = '001')            
 UNION                                                    
SELECT td.ords_num                                        
      ,td.list_num                                        
      ,td.tra_seq                                         
      ,td.tra_num                                         
      ,to_char(td.comp_dati, 'yyyymmdd') comp_dati        
      ,nvl(td.comp, td.cnt) comp                          
      ,td.canl                                            
      ,td.refu                                            
      ,td.exch                                            
      ,od.acep_man        
      ,(select to_char(y.tx_acco_dati, 'yyyymmdd')      
          from acco_led x, bill_led y                   
         where x.ords_num = td.ords_num               
           and x.list_num = td.list_num                
           and x.tra_seq = td.tra_seq                 
           and x.ords_ty = td.ords_ty                 
           and x.buy_sell_flag = '001'                
           and x.acco_num = y.acco_num) tx_acco_dati  
  FROM ords_dtl od                                        
      ,tra_dtl td                                         
      ,cust_dtl cd                                        
      ,enpri_info ei                                      
      ,enpri_dtl ed                                       
 WHERE 1=1                                                
   AND od.ords_num = td.ords_num                          
   AND od.list_num = td.list_num                          
   AND od.acep_id = cd.cust_id                            
   AND cd.enpri_cd = ei.enpri_cd                          
   AND cd.enpri_cd = ed.enpri_cd                          
   AND cd.busip_seq = ed.busip_seq                        
   AND ei.enpri_cd IN ('A43773','A44160')                 
   AND od.cust_ord_num LIKE '4500%'                       
   AND od.ords_kn IN ('003','005','013','015')            
   AND nvl(od.asp_flag,'002') <> '001'                    
   AND nvl(ed.tra_proc_ty,'002') = '001'                  
   AND td.comp_dati_cust IS NULL                          
   AND td.comp_dati IS NOT NULL                           
   AND EXISTS (SELECT 'x'                                 
                 FROM acco_led x                          
                WHERE x.ords_num = td.ords_num            
                  AND x.list_num = td.list_num            
                  AND x.tra_seq = td.tra_seq    
                  AND x.ords_ty = td.ords_ty          
                  AND x.buy_sell_flag = '001')            



SELECT td.ords_num                                        
      ,td.list_num                                        
      ,td.tra_seq                                         
      ,td.tra_num                                         
      ,SUBSTR (od.cust_ord_num, 1, 6) enpri_cd            
      ,ei.db_user                                         
      ,SUBSTR (od.cust_ord_num, 8, 7) asp_ords_num        
      ,SUBSTR (od.cust_ord_num, 16) asp_list_num          
      ,td.comp                                            
      ,td.canl                                            
      ,td.refu                                            
      ,td.exch                                            
      ,to_char(td.comp_dati, 'yyyymmdd') comp_dati        
      ,td.acep_man          
      ,(select to_char(y.tx_acco_dati, 'yyyymmdd')      
          from acco_led x, bill_led y                   
         where x.ords_num = td.ords_num               
           and x.list_num = td.list_num                
           and x.tra_seq = td.tra_seq                 
           and x.ords_ty = td.ords_ty                 
           and x.buy_sell_flag = '001'                
           and x.acco_num = y.acco_num) tx_acco_dati                                   
  FROM ords_dtl od                                        
      ,tra_dtl td                                         
      ,cust_dtl cd                                        
      ,enpri_info ei                                      
      ,enpri_dtl ed                                       
 WHERE 1=1                                                
   AND od.ords_num = td.ords_num                          
   AND od.list_num = td.list_num                          
   AND od.acep_id = cd.cust_id                            
   AND cd.enpri_cd = ei.enpri_cd                          
   AND cd.enpri_cd = ed.enpri_cd                          
   AND cd.busip_seq = ed.busip_seq                        
   AND od.ords_kn IN ('003','009')                        
   AND od.asp_flag = '001'                                
   AND nvl(ed.tra_proc_ty,'002') <> '001'                 
   AND td.comp_dati_cust IS NULL                          
   AND td.comp_dati IS NOT NULL                           
   AND EXISTS (SELECT 'x'                                 
                 FROM acco_led x                          
                WHERE x.ords_num = td.ords_num            
                  AND x.list_num = td.list_num            
                  AND x.tra_seq = td.tra_seq    
                  AND x.ords_ty = td.ords_ty          
                  AND x.buy_sell_flag = '001')            



SELECT A.ords_num                                       
      ,A.list_num                                       
      ,A.tra_seq                                        
  FROM tra_dtl A                                        
      ,ords_dtl b                                       
      ,enpri_dtl c                                      
      ,cust_dtl d                                       
 WHERE 1=1                                              
   AND A.ords_num = b.ords_num                          
   AND A.list_num = b.list_num                          
   AND b.acep_id = d.cust_id                            
   AND d.enpri_cd = c.enpri_cd                          
   AND d.busip_seq = c.busip_seq                        
   AND nvl(b.asp_flag,'002') <> '001'                   
   AND nvl(c.tra_proc_ty,'002') <> '001'                
   AND A.comp_kn = 'ACC'        
   AND A.comp_dati_cust >= to_date('20130101')                             
   AND NOT EXISTS (SELECT 'x'                           
                     FROM acco_led x                    
                    WHERE x.ords_num = A.ords_num       
                      AND x.list_num = A.list_num       
                      AND x.tra_seq = A.tra_seq        
                      AND x.ords_ty = A.ords_ty 
                      AND x.buy_sell_flag IN ('001','002'))       
 
 */

//빈즈 불러서 사용

public class  Demon_asp_comp_1h
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
		static private mk_log log = new mk_log();
        //생성자
        public Demon_asp_comp_1h(){}

        public static void main(String[] args)
        {
        	
    		int log_flag = 1;
    		String prgm_nm = "Asp comp demon 1 hour";
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	comm.OkMroUtil mcu = new comm.OkMroUtil(); 
        	
        	log.Write(log_flag, prgm_nm, "Asp comp Demon Start!!");
        	
        	
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
			
			
			
			
			String userid, passwd, url = "";
        	int pSeq = 0;
        	
        	String strQry = "";
        	try{
        		db.CUST_ID = "M005769";
            	db.PROG_NM = "/user/home/mro/mro_demon/Demon_asp_comp_1h.java";
    			db.DB_Conn();
    			
    			log.Write(log_flag, prgm_nm, "====================Cop comp process Start!!=======================");
    			
        		log.Write(log_flag, prgm_nm, "happynarae comp data select!!");
    			StringBuffer sbSelCompList  = new StringBuffer();
    			sbSelCompList.append("   SELECT td.ords_num                                        ").append("\n");
    			sbSelCompList.append("         ,td.list_num                                        ").append("\n");
    			sbSelCompList.append("         ,td.tra_seq                                         ").append("\n");
    			sbSelCompList.append("         ,td.tra_num                                         ").append("\n");
    			sbSelCompList.append("         ,to_char(td.comp_dati, 'yyyymmdd') comp_dati        ").append("\n");
    			sbSelCompList.append("         ,nvl(td.comp, td.cnt) comp                          ").append("\n");
    			sbSelCompList.append("         ,td.canl                                            ").append("\n");
    			sbSelCompList.append("         ,td.refu                                            ").append("\n");
    			sbSelCompList.append("         ,td.exch                                            ").append("\n");
    			sbSelCompList.append("         ,od.acep_man                                        ").append("\n");
    			sbSelCompList.append("         ,(select to_char(y.tx_acco_dati, 'yyyymmdd')        ").append("\n");
				sbSelCompList.append("             from acco_led x, bill_led y                     ").append("\n");
				sbSelCompList.append("            where x.ords_num = td.ords_num                   ").append("\n");
				sbSelCompList.append("              and x.list_num = td.list_num                   ").append("\n");
				sbSelCompList.append("              and x.tra_seq = td.tra_seq                     ").append("\n");
				sbSelCompList.append("              and x.ords_ty = td.ords_ty                     ").append("\n");
				sbSelCompList.append("              and x.buy_sell_flag = '001'                    ").append("\n");
				sbSelCompList.append("              and x.acco_num = y.acco_num) tx_acco_dati      ").append("\n");
    			sbSelCompList.append("     FROM ords_dtl od                                        ").append("\n");
    			sbSelCompList.append("         ,tra_dtl td                                         ").append("\n");
    			sbSelCompList.append("         ,cust_dtl cd                                        ").append("\n");
    			sbSelCompList.append("         ,enpri_info ei                                      ").append("\n");
    			sbSelCompList.append("         ,enpri_dtl ed                                       ").append("\n");
    			sbSelCompList.append("    WHERE 1=1                                                ").append("\n");
    			sbSelCompList.append("      AND od.ords_num = td.ords_num                          ").append("\n");
    			sbSelCompList.append("      AND od.list_num = td.list_num                          ").append("\n");
    			sbSelCompList.append("      AND od.acep_id = cd.cust_id                            ").append("\n");
    			sbSelCompList.append("      AND cd.enpri_cd = ei.enpri_cd                          ").append("\n");
    			sbSelCompList.append("      AND cd.enpri_cd = ed.enpri_cd                          ").append("\n");
    			sbSelCompList.append("      AND cd.busip_seq = ed.busip_seq                        ").append("\n");
    			sbSelCompList.append("      AND od.ords_kn IN ('003','005','013','015')            ").append("\n");
    			sbSelCompList.append("      AND nvl(od.asp_flag,'002') <> '001'                    ").append("\n");
    			sbSelCompList.append("      AND nvl(ed.tra_proc_ty,'002') <> '001'                 ").append("\n");
    			sbSelCompList.append("      AND td.comp_dati_cust IS NULL                          ").append("\n");
    			sbSelCompList.append("      AND td.comp_dati IS NOT NULL                           ").append("\n");
    			sbSelCompList.append("      AND EXISTS (SELECT 'x'                                 ").append("\n");
    			sbSelCompList.append("                    FROM acco_led x                          ").append("\n");
    			sbSelCompList.append("                   WHERE x.ords_num = td.ords_num            ").append("\n");
    			sbSelCompList.append("                     AND x.list_num = td.list_num            ").append("\n");
    			sbSelCompList.append("                     AND x.tra_seq = td.tra_seq              ").append("\n");
    			sbSelCompList.append("                     AND x.ords_ty = td.ords_ty              ").append("\n");
    			sbSelCompList.append("                     AND x.buy_sell_flag = '001')            ").append("\n");
    			sbSelCompList.append("    UNION                                                    ").append("\n");
    			sbSelCompList.append("   SELECT td.ords_num                                        ").append("\n");
    			sbSelCompList.append("         ,td.list_num                                        ").append("\n");
    			sbSelCompList.append("         ,td.tra_seq                                         ").append("\n");
    			sbSelCompList.append("         ,td.tra_num                                         ").append("\n");
    			sbSelCompList.append("         ,to_char(td.comp_dati, 'yyyymmdd') comp_dati        ").append("\n");
    			sbSelCompList.append("         ,nvl(td.comp, td.cnt) comp                          ").append("\n");
    			sbSelCompList.append("         ,td.canl                                            ").append("\n");
    			sbSelCompList.append("         ,td.refu                                            ").append("\n");
    			sbSelCompList.append("         ,td.exch                                            ").append("\n");
    			sbSelCompList.append("         ,od.acep_man                                        ").append("\n");
    			sbSelCompList.append("         ,(select to_char(y.tx_acco_dati, 'yyyymmdd')        ").append("\n");
				sbSelCompList.append("             from acco_led x, bill_led y                     ").append("\n");
				sbSelCompList.append("            where x.ords_num = td.ords_num                   ").append("\n");
				sbSelCompList.append("              and x.list_num = td.list_num                   ").append("\n");
				sbSelCompList.append("              and x.tra_seq = td.tra_seq                     ").append("\n");
				sbSelCompList.append("              and x.ords_ty = td.ords_ty                     ").append("\n");
				sbSelCompList.append("              and x.buy_sell_flag = '001'                    ").append("\n");
				sbSelCompList.append("              and x.acco_num = y.acco_num) tx_acco_dati      ").append("\n");
    			sbSelCompList.append("     FROM ords_dtl od                                        ").append("\n");
    			sbSelCompList.append("         ,tra_dtl td                                         ").append("\n");
    			sbSelCompList.append("         ,cust_dtl cd                                        ").append("\n");
    			sbSelCompList.append("         ,enpri_info ei                                      ").append("\n");
    			sbSelCompList.append("         ,enpri_dtl ed                                       ").append("\n");
    			sbSelCompList.append("    WHERE 1=1                                                ").append("\n");
    			sbSelCompList.append("      AND od.ords_num = td.ords_num                          ").append("\n");
    			sbSelCompList.append("      AND od.list_num = td.list_num                          ").append("\n");
    			sbSelCompList.append("      AND od.acep_id = cd.cust_id                            ").append("\n");
    			sbSelCompList.append("      AND cd.enpri_cd = ei.enpri_cd                          ").append("\n");
    			sbSelCompList.append("      AND cd.enpri_cd = ed.enpri_cd                          ").append("\n");
    			sbSelCompList.append("      AND cd.busip_seq = ed.busip_seq                        ").append("\n");
    			sbSelCompList.append("      AND ei.enpri_cd IN ('A43773','A44160')                 ").append("\n");
    			sbSelCompList.append("      AND od.cust_ord_num LIKE '4500%'                       ").append("\n");
    			sbSelCompList.append("      AND od.ords_kn IN ('003','005','013','015')            ").append("\n");
    			sbSelCompList.append("      AND nvl(od.asp_flag,'002') <> '001'                    ").append("\n");
    			sbSelCompList.append("      AND nvl(ed.tra_proc_ty,'002') = '001'                  ").append("\n");
    			sbSelCompList.append("      AND td.comp_dati_cust IS NULL                          ").append("\n");
    			sbSelCompList.append("      AND td.comp_dati IS NOT NULL                           ").append("\n");
    			sbSelCompList.append("      AND EXISTS (SELECT 'x'                                 ").append("\n");
    			sbSelCompList.append("                    FROM acco_led x                          ").append("\n");
    			sbSelCompList.append("                   WHERE x.ords_num = td.ords_num            ").append("\n");
    			sbSelCompList.append("                     AND x.list_num = td.list_num            ").append("\n");
    			sbSelCompList.append("                     AND x.tra_seq = td.tra_seq              ").append("\n");
    			sbSelCompList.append("                     AND x.ords_ty = td.ords_ty              ").append("\n");
    			sbSelCompList.append("                     AND x.buy_sell_flag = '001')            ").append("\n");
    			
    			strQry = sbSelCompList.toString(); 			
				log.Write(log_flag, prgm_nm, strQry);
				db.prepareStatement(strQry); 
				db.PexecuteQuery();
				
				List<Object> lCompData = new ArrayList<Object>();
				if(db.prs != null){
					log.Write(log_flag, prgm_nm, "happynarae put comp data!!");
					
					while(db.prs.next()){
						
						HashMap<String,String> hm = new HashMap<String,String>();
						
						//log.Write(log_flag, prgm_nm, "acco_num-->"+comm.OkMroUtil.checkNull(db.prs.getString("acco_num"  )));
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
						
						lCompData.add(hm);
					}
				}else{
					throw new SQLException("happynarae comp data select error");
				}
					
				int iCompDataCnt = lCompData.size();
				
				if(iCompDataCnt > 0){
					
					StringBuffer sbUpdCompDatiHappy = new StringBuffer();
					sbUpdCompDatiHappy.append("    UPDATE tra_dtl                     ").append("\n");
					sbUpdCompDatiHappy.append("       SET comp_dati_happy = to_date(?,'yyyy-mm-dd')         ").append("\n");
					sbUpdCompDatiHappy.append("          ,comp_kn = 'ACC'             ").append("\n");
					sbUpdCompDatiHappy.append("          ,sys_memo = 'Demon_asp_comp_1h 에서 고객사 매출 확정된 SD 주문서 고객 완료일자 처리함'  ").append("\n");
					sbUpdCompDatiHappy.append("     WHERE ords_num = ?                ").append("\n");
					sbUpdCompDatiHappy.append("       AND list_num = ?                ").append("\n");
					sbUpdCompDatiHappy.append("       AND tra_seq  = ?                ").append("\n");
	    			db.prepareStatement2(sbUpdCompDatiHappy.toString());	
	    			
	    			
	    			String comp_qry = "{?= call p_tra_mng.SCO_DELI_END(?,?,?,?,?,?,?,?,?,?,'1')}";
					db.prepareCall2(comp_qry);	    			
	    			
					for(int ii=0; ii<lCompData.size(); ii++){
						String ords_num     = (String)((HashMap)lCompData.get(ii)).get("ords_num"    );
						String list_num     = (String)((HashMap)lCompData.get(ii)).get("list_num"    );
						String tra_seq      = (String)((HashMap)lCompData.get(ii)).get("tra_seq"     );
						String tra_num      = (String)((HashMap)lCompData.get(ii)).get("tra_num"     );
						String comp_dati    = (String)((HashMap)lCompData.get(ii)).get("comp_dati"   );
						String comp         = (String)((HashMap)lCompData.get(ii)).get("comp"        );
						String canl         = (String)((HashMap)lCompData.get(ii)).get("canl"        );
						String refu         = (String)((HashMap)lCompData.get(ii)).get("refu"        );
						String exch         = (String)((HashMap)lCompData.get(ii)).get("exch"        );
						String acep_man     = (String)((HashMap)lCompData.get(ii)).get("acep_man"    );
						String tx_acco_dati = (String)((HashMap)lCompData.get(ii)).get("tx_acco_dati");
						
						
						log.Write(log_flag, prgm_nm, "happynarae tra comp_dati_cust update!!");
						log.Write(log_flag, prgm_nm, "order info : "+ords_num+"-"+list_num+"-"+tra_seq);
						
	
		    			
		    			pSeq = 1;
		    			db.PsetString2(pSeq++, tx_acco_dati       );
		    			db.PsetString2(pSeq++, ords_num           );
		    			db.PsetString2(pSeq++, list_num           );
		    			db.PsetString2(pSeq++, tra_seq            );
		    			
		    			db.ps2.executeUpdate();		
		    			
		    			

						
						
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

						if(db.Cexecute2() < 0) {
							log.Write(log_flag, prgm_nm, "happynarae rollback db '"+ords_num+"','"+list_num+"',  '"+tra_num+"','"+tra_seq+"',"+comp+","+canl+","+refu+","+exch+",'"+tx_acco_dati+"','"+acep_man+"'");
							db.rollback();
							//throw new SQLException("happynarae p_tra_mng.SCO_DELI_END procedure error");
						}else{
							log.Write(log_flag, prgm_nm, "happynarae commit db");
							db.commit();
						}
						
						int nRet = db.cs2.getInt(1);
		    			
						
					}
					
	    			
					
					
	    			if(db.ps != null) db.ps.close();
					if(db.prs != null) db.prs.close();
	    			if(db.ps2 != null) db.ps2.close();
					if(db.prs2 != null) db.prs2.close();					
					
				}
				log.Write(log_flag, prgm_nm, "====================Cop comp process End!!=======================");
				
				
				
				log.Write(log_flag, prgm_nm, "====================ASP Cop comp process Start!!=======================");
        		
        		Class.forName("oracle.jdbc.driver.OracleDriver");
        		
    			
    			log.Write(log_flag, prgm_nm, "happynarae asp info data select!!");
    			StringBuffer sbSelAspInfo  = new StringBuffer();
    			sbSelAspInfo.append("   SELECT DISTINCT db_user, db_pw, ip_addr FROM enpri_info  ").append("\n");
    			sbSelAspInfo.append("    WHERE db_user IS NOT NULL                               ").append("\n");
    			
    			strQry = sbSelAspInfo.toString(); 			
				log.Write(log_flag, prgm_nm, strQry);
				db.prepareStatement(strQry); 
				db.PexecuteQuery();
				
				if(db.prs != null){
					while(db.prs.next()){
						HashMap<String,String> hm = new HashMap<String,String>();
						
						log.Write(log_flag, prgm_nm, "db user selected : db_user-->"+comm.OkMroUtil.checkNull(db.prs.getString("db_user"  )));
						userid = comm.OkMroUtil.checkNull(db.prs.getString("db_user"  ));
						passwd = comm.OkMroUtil.checkNull(db.prs.getString("db_pw"  ));
						url = comm.OkMroUtil.checkNull(db.prs.getString("ip_addr"  ));
						
						if("keps".equals(userid)){
							connKeps = DriverManager.getConnection(url,userid,passwd);
							connKeps.setAutoCommit(false); 
						}else if("NEPS".equals(userid)){
							connNeps = DriverManager.getConnection(url,userid,passwd);
							connNeps.setAutoCommit(false); 
						}else if("seps".equals(userid)){
							connSeps = DriverManager.getConnection(url,userid,passwd);
							connSeps.setAutoCommit(false); 
						}else{
							throw new SQLException("there no db_user for "+userid+"!!"+db.ERR_MSG);
						}
					}
				}else{
					throw new SQLException("select asp info error!!"+db.ERR_MSG);
				}
				
								
    			log.Write(log_flag, prgm_nm, "happynarae asp comp data select!!");
    			StringBuffer sbSelAspCompList  = new StringBuffer();
    			sbSelAspCompList.append("   SELECT td.ords_num                                        ").append("\n");
    			sbSelAspCompList.append("         ,td.list_num                                        ").append("\n");
    			sbSelAspCompList.append("         ,td.tra_seq                                         ").append("\n");
    			sbSelAspCompList.append("         ,td.tra_num                                         ").append("\n");
    			sbSelAspCompList.append("         ,SUBSTR (od.cust_ord_num, 1, 6) enpri_cd            ").append("\n");
    			sbSelAspCompList.append("         ,ei.db_user                                         ").append("\n");
    			sbSelAspCompList.append("         ,SUBSTR (od.cust_ord_num, 8, 7) asp_ords_num        ").append("\n");
    			sbSelAspCompList.append("         ,SUBSTR (od.cust_ord_num, 16) asp_list_num          ").append("\n");
    			sbSelAspCompList.append("         ,td.comp                                            ").append("\n");
    			sbSelAspCompList.append("         ,td.canl                                            ").append("\n");
    			sbSelAspCompList.append("         ,td.refu                                            ").append("\n");
    			sbSelAspCompList.append("         ,td.exch                                            ").append("\n");
    			sbSelAspCompList.append("         ,to_char(td.comp_dati, 'yyyymmdd') comp_dati        ").append("\n");
    			sbSelAspCompList.append("         ,td.acep_man                                        ").append("\n");
    			sbSelAspCompList.append("         ,(select to_char(y.tx_acco_dati, 'yyyymmdd')        ").append("\n");
    			sbSelAspCompList.append("             from acco_led x, bill_led y                     ").append("\n");
    			sbSelAspCompList.append("            where x.ords_num = td.ords_num                   ").append("\n");
    			sbSelAspCompList.append("              and x.list_num = td.list_num                   ").append("\n");
    			sbSelAspCompList.append("              and x.tra_seq = td.tra_seq                     ").append("\n");
				sbSelAspCompList.append("              and x.ords_ty = td.ords_ty                     ").append("\n");
				sbSelAspCompList.append("              and x.buy_sell_flag = '001'                    ").append("\n");
				sbSelAspCompList.append("              and x.acco_num = y.acco_num) tx_acco_dati      ").append("\n");
    			sbSelAspCompList.append("     FROM ords_dtl od                                        ").append("\n");
    			sbSelAspCompList.append("         ,tra_dtl td                                         ").append("\n");
    			sbSelAspCompList.append("         ,cust_dtl cd                                        ").append("\n");
    			sbSelAspCompList.append("         ,enpri_info ei                                      ").append("\n");
    			sbSelAspCompList.append("         ,enpri_dtl ed                                       ").append("\n");
    			sbSelAspCompList.append("    WHERE 1=1                                                ").append("\n");
    			sbSelAspCompList.append("      AND od.ords_num = td.ords_num                          ").append("\n");
    			sbSelAspCompList.append("      AND od.list_num = td.list_num                          ").append("\n");
    			sbSelAspCompList.append("      AND od.acep_id = cd.cust_id                            ").append("\n");
    			sbSelAspCompList.append("      AND cd.enpri_cd = ei.enpri_cd                          ").append("\n");
    			sbSelAspCompList.append("      AND cd.enpri_cd = ed.enpri_cd                          ").append("\n");
    			sbSelAspCompList.append("      AND cd.busip_seq = ed.busip_seq                        ").append("\n");
    			sbSelAspCompList.append("      AND od.ords_kn IN ('003','009')                        ").append("\n");
    			sbSelAspCompList.append("      AND od.asp_flag = '001'                                ").append("\n");
    			sbSelAspCompList.append("      AND nvl(ed.tra_proc_ty,'002') <> '001'                 ").append("\n");
    			sbSelAspCompList.append("      AND td.comp_dati_cust IS NULL                          ").append("\n");
    			sbSelAspCompList.append("      AND td.comp_dati IS NOT NULL                           ").append("\n");
    			sbSelAspCompList.append("      AND EXISTS (SELECT 'x'                                 ").append("\n");
    			sbSelAspCompList.append("                    FROM acco_led x                          ").append("\n");
    			sbSelAspCompList.append("                   WHERE x.ords_num = td.ords_num            ").append("\n");
    			sbSelAspCompList.append("                     AND x.list_num = td.list_num            ").append("\n");
    			sbSelAspCompList.append("                     AND x.tra_seq = td.tra_seq              ").append("\n");
    			sbSelAspCompList.append("                     AND x.ords_ty = td.ords_ty              ").append("\n");
    			sbSelAspCompList.append("                     AND x.buy_sell_flag = '001')            ").append("\n");
    			
    			strQry = sbSelAspCompList.toString(); 			
				log.Write(log_flag, prgm_nm, strQry);
				db.prepareStatement(strQry); 
				db.PexecuteQuery();
				
				List<Object> lData = new ArrayList<Object>();
				if(db.prs != null){
					log.Write(log_flag, prgm_nm, "happynarae put asp comp data!!");
					
					while(db.prs.next()){
						
						HashMap<String,String> hm = new HashMap<String,String>();
						
						log.Write(log_flag, prgm_nm, "asp comp data selected : ords_num-->"+comm.OkMroUtil.checkNull(db.prs.getString("ords_num"  )));
						hm.put("ords_num"     ,comm.OkMroUtil.checkNull(db.prs.getString("ords_num"    )));
						hm.put("list_num"     ,comm.OkMroUtil.checkNull(db.prs.getString("list_num"    )));
						hm.put("tra_seq"      ,comm.OkMroUtil.checkNull(db.prs.getString("tra_seq"     )));
						hm.put("tra_num"      ,comm.OkMroUtil.checkNull(db.prs.getString("tra_num"     )));
						hm.put("enpri_cd"     ,comm.OkMroUtil.checkNull(db.prs.getString("enpri_cd"    )));
						hm.put("db_user"      ,comm.OkMroUtil.checkNull(db.prs.getString("db_user"     )));
						hm.put("asp_ords_num" ,comm.OkMroUtil.checkNull(db.prs.getString("asp_ords_num")));
						hm.put("asp_list_num" ,comm.OkMroUtil.checkNull(db.prs.getString("asp_list_num")));
						hm.put("comp"         ,comm.OkMroUtil.checkNull(db.prs.getString("comp"        )));
						hm.put("canl"         ,comm.OkMroUtil.checkNull(db.prs.getString("canl"        )));
						hm.put("refu"         ,comm.OkMroUtil.checkNull(db.prs.getString("refu"        )));
						hm.put("exch"         ,comm.OkMroUtil.checkNull(db.prs.getString("exch"        )));
						hm.put("comp_dati"    ,comm.OkMroUtil.checkNull(db.prs.getString("comp_dati"   )));
						hm.put("acep_man"     ,comm.OkMroUtil.checkNull(db.prs.getString("acep_man"    )));
						hm.put("tx_acco_dati" ,comm.OkMroUtil.checkNull(db.prs.getString("tx_acco_dati")));
						
						lData.add(hm);
					}
				}else{
					throw new SQLException("happynarae asp comp data select error");
				}
					
				int iAspDataCnt = lData.size();
				
				if(iAspDataCnt > 0){
					
					StringBuffer sbUpdCompDatiCust = new StringBuffer();
					sbUpdCompDatiCust.append("    UPDATE tra_dtl                     ").append("\n");
					sbUpdCompDatiCust.append("       SET comp_dati_happy = to_date(?,'yyyy-mm-dd')         ").append("\n");
					sbUpdCompDatiCust.append("          ,comp_kn = 'ACC'             ").append("\n");
					sbUpdCompDatiCust.append("          ,sys_memo = 'Demon_asp_comp_1h 에서 ASP 업체 매출 확정된 SD 주문서 고객 완료일자 처리함'  ").append("\n");
					sbUpdCompDatiCust.append("     WHERE ords_num = ?                ").append("\n");
					sbUpdCompDatiCust.append("       AND list_num = ?                ").append("\n");
					sbUpdCompDatiCust.append("       AND tra_seq  = ?                ").append("\n");
	    			db.prepareStatement2(sbUpdCompDatiCust.toString());		
	    			
					
	    			
	    			String comp_qry = "{?= call p_tra_mng.SCO_DELI_END(?,?,?,?,?,?,?,?,?,?,'1')}";
					db.prepareCall2(comp_qry);
					
	    			
					for(int ii=0; ii<lData.size(); ii++){
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
						
						
						log.Write(log_flag, prgm_nm, "happynarae asp tra info procedure call!!");
						
		    			pSeq = 1;
		    			db.PsetString2(pSeq++, tx_acco_dati       );
		    			db.PsetString2(pSeq++, ords_num           );
		    			db.PsetString2(pSeq++, list_num           );
		    			db.PsetString2(pSeq++, tra_seq            );
		    			
		    			db.ps2.executeUpdate();		
		    			

		    			
						
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

						if(db.Cexecute2() < 0) {
							log.Write(log_flag, prgm_nm, "happynarae rollback db '"+ords_num+"','"+list_num+"',  '"+tra_num+"','"+tra_seq+"',"+comp+","+canl+","+refu+","+exch+",'"+tx_acco_dati+"','"+acep_man+"'");
							db.rollback();
							//throw new SQLException("happynarae asp p_tra_mng.SCO_DELI_END procedure error");
						}else{
						
							int nRet = db.cs2.getInt(1);
							
							
							log.Write(log_flag, prgm_nm, "happynarae asp tra info procedure call!!");
							
							StringBuffer sbGetAspTraInfo = new StringBuffer();
							sbGetAspTraInfo.append("{call TRAS.P_GET_ASP_TRA_INFO(?,?,?,?,?,?,?,?)}").append("\n");
							strQry = sbGetAspTraInfo.toString(); 
							db.prepareCall(strQry);
							
							pSeq = 1;
							db.CsetString(pSeq++, ords_num);
							db.CsetString(pSeq++, list_num);
							db.CsetString(pSeq++, tra_seq );
							db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_ords_num       
							db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_list_num 
							db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_tra_seq       
							db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_asp_tra_num 	       
							db.cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//r_res
							
							db.Cexecute();
							
							if(db.ERR_FLAG < 0){
								log.Write(log_flag, prgm_nm, "happynarae rollback db '"+ords_num+"','"+list_num+"',  '"+tra_num+"','"+tra_seq+"'");
								throw new SQLException("happynarae asp tra info procedure call error");
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
								log.Write(log_flag, prgm_nm, "keps asp comp_dati data select!!");
				    			StringBuffer sbSelKepsCompYn  = new StringBuffer();
				    			sbSelKepsCompYn.append("  SELECT count(*) cnt      ").append("\n");
				    			sbSelKepsCompYn.append("    FROM tra_dtl           ").append("\n");
				    			sbSelKepsCompYn.append("   WHERE ords_num = ?      ").append("\n");
				    			sbSelKepsCompYn.append("     AND list_num = ?      ").append("\n");
				    			sbSelKepsCompYn.append("     AND tra_seq = ?       ").append("\n");
				    			sbSelKepsCompYn.append("     AND comp_dati IS NULL ").append("\n");
								
				    			String aspQry = sbSelKepsCompYn.toString(); 			
								log.Write(log_flag, prgm_nm, aspQry);
								psKeps = connKeps.prepareStatement(aspQry);
								
								pSeq = 1;
								psKeps.setString(pSeq++, asp_ords_num);
								psKeps.setString(pSeq++, asp_list_num);
								psKeps.setString(pSeq++, asp_tra_seq);
								
								rsKeps = psKeps.executeQuery();
								
								int iAspCompDatiCnt = 0;
								if(rsKeps != null){
									log.Write(log_flag, prgm_nm, "keps asp comp_dati data!!");
									
									while(rsKeps.next()){
										iAspCompDatiCnt = rsKeps.getInt("cnt");
									}
								}else{
									throw new SQLException("keps asp comp_dati select error");
								}
								
								if(iAspCompDatiCnt > 0){
									log.Write(log_flag, prgm_nm, "keps asp tra comp procedure call!!");
									csKeps = connKeps.prepareCall(strAspQry);
									
									pSeq = 1;
									csKeps.setString(pSeq++, ords_num);
									csKeps.setString(pSeq++, asp_ords_num);
									csKeps.setString(pSeq++, asp_list_num);
									csKeps.setString(pSeq++, asp_tra_num );
									csKeps.setString(pSeq++, asp_tra_seq );
									csKeps.setString(pSeq++, comp );
									csKeps.setString(pSeq++, canl );
									csKeps.setString(pSeq++, refu );
									csKeps.setString(pSeq++, exch );
									csKeps.setString(pSeq++, tx_acco_dati );
									csKeps.setString(pSeq++, acep_man );	       
									csKeps.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		
										
									csKeps.execute();
									
									log.Write(log_flag, prgm_nm, "keps asp tra comp procedure call complete!!");
									
									String sResAsp    = csKeps.getString(12);
									
									log.Write(log_flag, prgm_nm,sResAsp);
		
									if(!"SUCC".equals(sResAsp)){
										connKeps.rollback();
										throw new SQLException("keps asp tra comp procedure data error");
									}else{
										connKeps.commit();
									}
								}
							}else if("NEPS".equals(db_user)){
								log.Write(log_flag, prgm_nm, "neps asp comp_dati data select!!");
				    			StringBuffer sbSelNepsCompYn  = new StringBuffer();
				    			sbSelNepsCompYn.append("  SELECT count(*) cnt      ").append("\n");
				    			sbSelNepsCompYn.append("    FROM tra_dtl           ").append("\n");
				    			sbSelNepsCompYn.append("   WHERE ords_num = ?      ").append("\n");
				    			sbSelNepsCompYn.append("     AND list_num = ?      ").append("\n");
				    			sbSelNepsCompYn.append("     AND tra_seq = ?       ").append("\n");
				    			sbSelNepsCompYn.append("     AND comp_dati IS NULL ").append("\n");
								
				    			String aspQry = sbSelNepsCompYn.toString(); 			
								log.Write(log_flag, prgm_nm, aspQry);
								psNeps = connNeps.prepareStatement(aspQry);
								
								pSeq = 1;
								psNeps.setString(pSeq++, asp_ords_num);
								psNeps.setString(pSeq++, asp_list_num);
								psNeps.setString(pSeq++, asp_tra_seq);
								
								rsNeps = psNeps.executeQuery();
								
								int iAspCompDatiCnt = 0;
								if(rsNeps != null){
									log.Write(log_flag, prgm_nm, "neps asp comp_dati data!!");
									
									while(rsNeps.next()){
										iAspCompDatiCnt = rsNeps.getInt("cnt");
									}
								}else{
									throw new SQLException("Neps asp comp_dati select error");
								}
								
								if(iAspCompDatiCnt > 0){
									log.Write(log_flag, prgm_nm, "neps asp tra comp procedure call!!");
									csNeps = connNeps.prepareCall(strAspQry);
									
									pSeq = 1;
									csNeps.setString(pSeq++, ords_num);
									csNeps.setString(pSeq++, asp_ords_num);
									csNeps.setString(pSeq++, asp_list_num);
									csNeps.setString(pSeq++, asp_tra_num );
									csNeps.setString(pSeq++, asp_tra_seq );
									csNeps.setString(pSeq++, comp );
									csNeps.setString(pSeq++, canl );
									csNeps.setString(pSeq++, refu );
									csNeps.setString(pSeq++, exch );
									csNeps.setString(pSeq++, tx_acco_dati );
									csNeps.setString(pSeq++, acep_man );       
									csNeps.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);	
										
									csNeps.execute();
									
									log.Write(log_flag, prgm_nm, "Neps asp tra comp procedure call complete!!");
									
									String sResAsp    = csNeps.getString(12);
									
									log.Write(log_flag, prgm_nm,sResAsp);
		
									if(!"SUCC".equals(sResAsp)){
										connNeps.rollback();
										throw new SQLException("neps asp tra comp procedure data error");
									}else{
										connNeps.commit();
									}
								}
							}else if("seps".equals(db_user)){
								log.Write(log_flag, prgm_nm, "Seps asp comp_dati data select!!");
				    			StringBuffer sbSelSepsCompYn  = new StringBuffer();
				    			sbSelSepsCompYn.append("  SELECT count(*) cnt      ").append("\n");
				    			sbSelSepsCompYn.append("    FROM tra_dtl           ").append("\n");
				    			sbSelSepsCompYn.append("   WHERE ords_num = ?      ").append("\n");
				    			sbSelSepsCompYn.append("     AND list_num = ?      ").append("\n");
				    			sbSelSepsCompYn.append("     AND tra_seq = ?       ").append("\n");
				    			sbSelSepsCompYn.append("     AND comp_dati IS NULL ").append("\n");
								
				    			String aspQry = sbSelSepsCompYn.toString(); 			
								log.Write(log_flag, prgm_nm, aspQry);
								psSeps = connSeps.prepareStatement(aspQry);
								
								pSeq = 1;
								psSeps.setString(pSeq++, asp_ords_num);
								psSeps.setString(pSeq++, asp_list_num);
								psSeps.setString(pSeq++, asp_tra_seq);
								
								rsSeps = psSeps.executeQuery();
								
								int iAspCompDatiCnt = 0;
								if(rsSeps != null){
									log.Write(log_flag, prgm_nm, "Seps asp comp_dati data!!");
									
									while(rsSeps.next()){
										iAspCompDatiCnt = rsSeps.getInt("cnt");
									}
								}else{
									throw new SQLException("Seps asp comp_dati select error");
								}
								
								if(iAspCompDatiCnt > 0){
									log.Write(log_flag, prgm_nm, "seps asp tra comp procedure call!!");
									csSeps = connSeps.prepareCall(strAspQry);
									
									pSeq = 1;
									csSeps.setString(pSeq++, ords_num);
									csSeps.setString(pSeq++, asp_ords_num);
									csSeps.setString(pSeq++, asp_list_num);
									csSeps.setString(pSeq++, asp_tra_num );
									csSeps.setString(pSeq++, asp_tra_seq );
									csSeps.setString(pSeq++, comp );
									csSeps.setString(pSeq++, canl );
									csSeps.setString(pSeq++, refu );
									csSeps.setString(pSeq++, exch );
									csSeps.setString(pSeq++, tx_acco_dati );
									csSeps.setString(pSeq++, acep_man );
									csSeps.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);	
										
									csSeps.execute();
									
									log.Write(log_flag, prgm_nm, "Seps asp tra comp procedure call complete!!");
									
									String sResAsp    = csSeps.getString(12);
									
									log.Write(log_flag, prgm_nm,sResAsp);
		
									if(!"SUCC".equals(sResAsp)){
										connSeps.rollback();
										throw new SQLException("seps asp tra comp procedure data error");
									}else{
										connSeps.commit();
									}
								}
							}else{
								throw new SQLException("there no db_user for "+db_user+"!!"+db.ERR_MSG);
							}
						}
					}

					
					
					log.Write(log_flag, prgm_nm, "happynarae commit db");
					db.commit();
					if(db.ps != null) db.ps.close();
					if(db.prs != null) db.prs.close();
					
					log.Write(log_flag, prgm_nm, "asp commit db");
					if(connKeps != null) connKeps.commit();
					if(connNeps != null) connNeps.commit();
					if(connSeps != null) connSeps.commit();
					
					log.Write(log_flag, prgm_nm, "asp db disconnection!!");
					if(csKeps!=null)csKeps.close();
					if(psKeps!=null)psKeps.close();
					if(connKeps !=null)connKeps.close();
					
					if(csNeps!=null)csNeps.close();
					if(psNeps!=null)psNeps.close();
					if(connNeps !=null)connNeps.close();
					
					if(csSeps!=null)csSeps.close();
					if(psSeps!=null)psSeps.close();
					if(connSeps !=null)connSeps.close();
				}
				
				log.Write(log_flag, prgm_nm, "====================ASP Cop comp process End!!=======================");
				
    			
				
				log.Write(log_flag, prgm_nm, "====================Cop comp process Start!!=======================");
    			
        		log.Write(log_flag, prgm_nm, "happynarae comp cancel data select!!");
    			StringBuffer sbSelCnclList  = new StringBuffer();
    			sbSelCnclList.append("   SELECT A.ords_num                                       ").append("\n");
    			sbSelCnclList.append("         ,A.list_num                                       ").append("\n");
    			sbSelCnclList.append("         ,A.tra_seq                                        ").append("\n");
    			sbSelCnclList.append("     FROM tra_dtl A                                        ").append("\n");
    			sbSelCnclList.append("         ,ords_dtl b                                       ").append("\n");
    			sbSelCnclList.append("         ,enpri_dtl c                                      ").append("\n");
    			sbSelCnclList.append("         ,cust_dtl d                                       ").append("\n");
    			sbSelCnclList.append("    WHERE 1=1                                              ").append("\n");
    			sbSelCnclList.append("      AND A.ords_num = b.ords_num                          ").append("\n");
    			sbSelCnclList.append("      AND A.list_num = b.list_num                          ").append("\n");
    			sbSelCnclList.append("      AND b.acep_id = d.cust_id                            ").append("\n");
    			sbSelCnclList.append("      AND d.enpri_cd = c.enpri_cd                          ").append("\n");
    			sbSelCnclList.append("      AND d.busip_seq = c.busip_seq                        ").append("\n");
    			sbSelCnclList.append("      AND nvl(b.asp_flag,'002') <> '001'                   ").append("\n");
    			sbSelCnclList.append("      AND nvl(c.tra_proc_ty,'002') <> '001'                ").append("\n");
    			sbSelCnclList.append("      AND A.comp_kn = 'ACC'                                ").append("\n");
    			sbSelCnclList.append("      AND A.comp_dati_cust >= to_date('20130101')          ").append("\n");
    			sbSelCnclList.append("      AND NOT EXISTS (SELECT 'x'                           ").append("\n");
    			sbSelCnclList.append("                        FROM acco_led x                    ").append("\n");
    			sbSelCnclList.append("                       WHERE x.ords_num = A.ords_num       ").append("\n");
    			sbSelCnclList.append("                         AND x.list_num = A.list_num       ").append("\n");
    			sbSelCnclList.append("                         AND x.tra_seq = A.tra_seq         ").append("\n");
    			sbSelCnclList.append("                         AND x.ords_ty = A.ords_ty         ").append("\n");
    			sbSelCnclList.append("                         AND x.buy_sell_flag in ('001','002'))      ").append("\n");
    			
    			strQry = sbSelCnclList.toString(); 			
				log.Write(log_flag, prgm_nm, strQry);
				db.prepareStatement(strQry); 
				db.PexecuteQuery();
				
				List<Object> lCnclData = new ArrayList<Object>();
				if(db.prs != null){
					log.Write(log_flag, prgm_nm, "happynarae put comp cancel data loop start!!");
					
					while(db.prs.next()){
						
						HashMap<String,String> hm = new HashMap<String,String>();
						
						log.Write(log_flag, prgm_nm, "comp cancel data selected : ords_num-->"+comm.OkMroUtil.checkNull(db.prs.getString("ords_num"    )));
						hm.put("ords_num"     ,comm.OkMroUtil.checkNull(db.prs.getString("ords_num"    )));
						hm.put("list_num"     ,comm.OkMroUtil.checkNull(db.prs.getString("list_num"    )));
						hm.put("tra_seq"      ,comm.OkMroUtil.checkNull(db.prs.getString("tra_seq"     )));
						
						lCnclData.add(hm);
					}
				}else{
					throw new SQLException("happynarae comp cancel data select error");
				}
					
				int iCnclDataCnt = lCnclData.size();
				
				log.Write(log_flag, prgm_nm, "====================comp cancel count iCnclDataCnt:"+iCnclDataCnt+"=======================");
				
				if(iCnclDataCnt > 0){
					
					StringBuffer sbUpdCnclDatiHappy = new StringBuffer();
					sbUpdCnclDatiHappy.append("    UPDATE tra_dtl                     ").append("\n");
					sbUpdCnclDatiHappy.append("       SET comp_dati_cust = null  ").append("\n");
					sbUpdCnclDatiHappy.append("          ,comp_dati_happy = null ").append("\n");
					sbUpdCnclDatiHappy.append("          ,comp_kn = null             ").append("\n");
					sbUpdCnclDatiHappy.append("          ,sys_memo = 'Demon_asp_comp_1h 에서 고객사 매출 확정되었으나 매출 삭제로 인해 주문서 고객 완료일자 취소 처리함'  ").append("\n");
					sbUpdCnclDatiHappy.append("     WHERE ords_num = ?                ").append("\n");
					sbUpdCnclDatiHappy.append("       AND list_num = ?                ").append("\n");
					sbUpdCnclDatiHappy.append("       AND tra_seq  = ?                ").append("\n");
	    			db.prepareStatement2(sbUpdCnclDatiHappy.toString());	
	    			
	    			StringBuffer sbUpdCnclProcHappy = new StringBuffer();
	    			sbUpdCnclProcHappy.append("    UPDATE ords_dtl                     ").append("\n");
	    			sbUpdCnclProcHappy.append("       SET proc_ty = '004'  ").append("\n");
	    			sbUpdCnclProcHappy.append("          ,sys_memo = 'Demon_asp_comp_1h 에서 고객사 매출 확정되었으나 매출 삭제로 인해 주문서 완료처리 배송중으로 변경함'  ").append("\n");
	    			sbUpdCnclProcHappy.append("     WHERE ords_num = ?                ").append("\n");
	    			sbUpdCnclProcHappy.append("       AND list_num = ?                ").append("\n");
	    			db.prepareStatement3(sbUpdCnclProcHappy.toString());	
	    			
	    			for(int ii=0; ii<lCnclData.size(); ii++){
						String ords_num     = (String)((HashMap)lCnclData.get(ii)).get("ords_num" );
						String list_num     = (String)((HashMap)lCnclData.get(ii)).get("list_num" );
						String tra_seq      = (String)((HashMap)lCnclData.get(ii)).get("tra_seq"  );
						
						
						log.Write(log_flag, prgm_nm, "happynarae tra comp_dati_cust cancel update!!");
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
		    			
						log.Write(log_flag, prgm_nm, "happynarae commit db");
						db.commit();
						
						
					}
	    			
					
					
	    			if(db.ps != null) db.ps.close();
					if(db.prs != null) db.prs.close();
	    			if(db.ps2 != null) db.ps2.close();
					if(db.prs2 != null) db.prs2.close();
	    			if(db.ps3 != null) db.ps3.close();
					if(db.prs3 != null) db.prs3.close();
					
					
				}
				log.Write(log_flag, prgm_nm, "====================Cop comp cancel process End!!=======================");
				
				
				
				
            }catch(SQLException e){            	
                System.out.println("Error"+e);
                log.Write(log_flag, prgm_nm, ""+e);
                try{
            		System.out.println(e);
                    e.printStackTrace();
                    db.rollback();
                    if(connKeps != null) connKeps.rollback();
                    if(connNeps != null) connNeps.rollback();
                    if(connSeps != null) connSeps.rollback();
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
                     if(connKeps != null) connKeps.rollback();
                     if(connNeps != null) connNeps.rollback();
                     if(connSeps != null) connSeps.rollback();
                 }catch(Exception e1){
                	 log.Write(log_flag, prgm_nm, e1.getMessage());
                	 e1.printStackTrace();
                 }
            }finally{
            	db.DB_DisConn();		
            	log.Write(log_flag, prgm_nm, "happynarae db disconnection!!");
            	try{
					if(csKeps!=null)csKeps.close();
					if(psKeps!=null)psKeps.close();
					if(connKeps !=null)connKeps.close();
					
					if(csNeps!=null)csNeps.close();
					if(psNeps!=null)psNeps.close();
					if(connNeps !=null)connNeps.close();
					
					if(csSeps!=null)csSeps.close();
					if(psSeps!=null)psSeps.close();
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