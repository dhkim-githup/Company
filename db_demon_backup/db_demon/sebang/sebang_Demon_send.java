import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*; 
import java.util.Date; 



import comm.DB_Use_Db_Demon;
import comm.mk_log;

public class sebang_Demon_send {	
	
	public static void main(String[] args) {
		
		DB_Use_Db_Demon db = new DB_Use_Db_Demon();
		mk_log log = new mk_log();
		String err_occur=null, ERR_MSG="";	
		
		db.dbURL = "jdbc:oracle:thin:@172.16.1.208:1521:ndb10"; // 서버 운영
		db.user_id = "METS18940G"; //운영
		db.user_pw = "METS25519P58563W"; //운영
		/*		
		db.dbURL = "jdbc:oracle:thin:@172.16.1.224:1521:TESTDB"; // 서버 운영
		db.user_id = "METS_IMSI"; //운영
		db.user_pw = "METS_IMSI"; //운영
		*/
		db.DB_Conn();
		System.out.println("METS Db CONNECTION !! ");		
		
		try{	
			
				// 추후 파일명 형식을 가져올때 날짜로 해서 만들 예정입니다.				
				
				String strTodayDate = "yyyyMMdd";
				SimpleDateFormat formatter = new SimpleDateFormat(strTodayDate);
				Date date = new Date();
				String strYearM = formatter.format(date).toString();
				
				
				
				String sDownDir ="/user/home/mro/db_demon/sebang/data/";			
		
								
				String source ="";
				String backup_source="";
				String name ="";
				
				try{
					   						         		
				         	String file ="1A14800POMT20200117-070558-415";
				         	//일단 파일 읽기
				         	String outfilename = sDownDir + file;
				         	//구분자 정의하기 
				         	String gubun ="";
				         	gubun = file.substring(7,11);
				         //	System.out.println(gubun);	
				         	
				         			
							FileReader in = new FileReader(outfilename);
							int c;
							String s = new String();
							while((c= in.read()) != -1 ){
								s = s + (char)c;
							}
							//System.out.println("텍스트에 들어있는 문자 내용===============>" + s);	

							if("POMT".equals(gubun)){
							// 순차 구하기 
							String qry2 ="SELECT NVL(MAX(SEQ),0)+1 SEQ FROM INF_SEBANG.IF_PO WHERE REG_DATI = ? ";
							db.prepareStatement2(qry2);
							String qry = " INSERT INTO INF_SEBANG.IF_PO(ENPRI_CD, REG_DATI, SEQ, PO_NUM, PO_NUM_SEQ, MATERIAL_GRP, MATERIAL_CODE, "
							+        " MRO_CODE, PLIS_NM, SPEC, UNIT, CNT, DELI_HOPE_DAY, MEMO, REG_MAN, STATUS, CST_REG_DAY, MRO_ACP_DAY, MRO_STAT, "
							+        "ERR_MSG, RFQ_EMP_NUM, CUST_NM, SEBANG_BUSIP_CODE, SEBANG_BUSIP_NM, SEBANG_OGN_CD, SEBANG_OGN_NM, TEL_NUM, MCOM_NUM, "
							+        "FAX_NUM, E_MAIL, ZIP_CODE, JUSO, SEBANG_STATUS, PCH_EMP_NUM, PCH_CUST_NM, PCH_BUSIP_CODE, PCH_BUSIP_NM, PCH_OGN_CD, "
							+        "PCH_OGN_NM, PCH_TEL_NUM, PCH_MCOM_NUM, PCH_FAX_NUM, PCH_E_MAIL, PCH_ZIP_CODE, PCH_JUSO, SAP_PLANT_CODE, SCO_CODE, "
							+        "SCO_NM, FAST_FLAG, INVEST_NUM, BUDGET_ROOM_CODE, BUDGET_BU_CODE, BUDGET_OGN_CODE, ACNT_KN_CD, UPRI, PRI, CURRENCY, EXCHANGE_RETE, FILE_NAME) "									
							+" VALUES(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'yyyymmddhh24miss'), ?, ?, "
							+        "?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+        "?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) ";								
							db.prepareStatement(qry);
							}else if("GRDT".equals(gubun)){
							// 순차 구하기 
							String qry2 ="SELECT NVL(MAX(SEQ),0)+1 SEQ FROM INF_SEBANG.IF_GR WHERE REG_DATI = ? ";
							db.prepareStatement2(qry2);
							
							String qry = " INSERT INTO INF_SEBANG.IF_GR(ENPRI_CD, REG_DATI, SEQ, GR_NUM, GR_NUM_SEQ, PO_NUM, " 
							+        " PO_NUM_SEQ, CNT, IN_DATI, ACEP_MAN, MEMO, REG_MAN, STATUS,  MRO_ACP_DAY, "
							+        " ERR_MSG, JAJAE_YEAR, JAJAE_NUM, PLIS_NM, SPEC, UNIT, UPRI, USD, PRI, PLANT_INFO, "
							+        " BUSIP_CODE, OGN_CODE, REF_NO, FILE_NAME) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'yyyy-mm-dd'), ?, ?, ?, ?,  ?, "
							+        " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?) ";	
							db.prepareStatement(qry);
							}

									
						// 엔터값 짜르고 | 구분자 짜르고 헷갈려서 두개로 나눈다 
							String Enter_balue[] = s.split("\n");								
						//    System.out.println("총갯수==>" + Enter_balue.length);		
							for(int i = 0; i < Enter_balue.length; i++){
							String db_value[] = Enter_balue[i].split("\\|");																
							int k =0; 
							// System.out.println("구분자==>" + db_value.length);
							// DB 넣기 
							int strSEQ=1;

							// 내역 확인 하는구문 
							/*
							for(int j = 0; j < db_value.length; j++){
								k++;										
								if("POMT".equals(gubun)){
									//System.out.println("발주정보 ");
								}else if("GRDT".equals(gubun)){
									//System.out.println(k+"번======>"+db_value[j]);
									//System.out.println("입고정보");								         	}										
								}  // | 구분자 자르기
							} 	
							*/						

											
							if("POMT".equals(gubun)){	
								
								try{  	

									db.PsetString2(1, strYearM);
									db.PexecuteQuery2();
									if(db.prs2.next()){
										strSEQ= db.prs2.getInt("SEQ");		
									}

									
										/*
										if(strSEQ>149){

										System.out.println("2===>" + strYearM);
										System.out.println("3===>>" + strSEQ);
										System.out.println("4===>" + db_value[1]);
										System.out.println("5===>" + db_value[1]);
										System.out.println("6===>" + db_value[4]);
										System.out.println("7===>" + db_value[5]);
										System.out.println("8===>" + "");
										System.out.println("9===>" + db_value[6]);
										System.out.println("10===>" + db_value[7]+db_value[8]+db_value[9]);
										System.out.println("11===>" + db_value[37]);
										System.out.println("12===>" + db_value[36]);
										System.out.println("13===>" + db_value[14]);
										System.out.println("14===>" + db_value[29]);
										System.out.println("15===>" + db_value[16]);
										System.out.println("16===>" + "INS");
										System.out.println("17===>" + db_value[12]+db_value[13]);
										System.out.println("18===>" + "");
										System.out.println("19===>" + "");
										System.out.println("20===>" + "");
										System.out.println("21===>" + db_value[22]);
										System.out.println("22===>" + db_value[23]);
										System.out.println("23===>" + "1");
										System.out.println("24===>" + "세방전지(주) 창원공장");
										if(db_value.length == 44){
										System.out.println("25===>" + db_value[42]);
										System.out.println("26===>" + db_value[43]);
										}else{
										System.out.println("25===>" + db_value[38]);
										System.out.println("26===>" + db_value[39]);
										}
										System.out.println("27===>" + db_value[24]);
										System.out.println("28===>" + db_value[25]);
										System.out.println("29===>" + "");
										System.out.println("30===>" + "");
										System.out.println("31===>" + db_value[26]);
										System.out.println("32===>" + db_value[27]+db_value[28]);
										System.out.println("33===>" + "");
										System.out.println("34===>" + db_value[16]);
										System.out.println("35===>" + db_value[17]);
										System.out.println("36===>" + "1");
										System.out.println("37===>" + "세방전지(주) 창원공장");
										System.out.println("38===>" + db_value[21]);
										System.out.println("39===>" + db_value[21]);
										System.out.println("40===>" + db_value[18]);
										System.out.println("41===>" + "");
										System.out.println("42===>" + "");
										System.out.println("43===>" + "");
										System.out.println("44===>" + db_value[26]);
										System.out.println("45===>" + db_value[27]+db_value[28]);
										System.out.println("46===>" + db_value[3]);
										System.out.println("47===>" + db_value[10]);
										System.out.println("48===>" + db_value[11]);
										System.out.println("49===>" + db_value[15]);
										System.out.println("50===>" + db_value[30]);
										System.out.println("51===>" + db_value[32]);
										System.out.println("52===>" + db_value[33]);
										System.out.println("53===>" + db_value[34]);
										System.out.println("54===>" + db_value[35]);
										System.out.println("55===>" + db_value[38]);
										System.out.println("56===>" + db_value[39]);
										System.out.println("57===>" + db_value[40]);
										System.out.println("58===>" + db_value[41]);
										System.out.println("59===>" + file);
										}
																			
										*/


										db.PsetString(1, "005186");
										db.PsetString(2, strYearM);
										db.PsetInt(3, strSEQ);												
										db.PsetString(4, db_value[1]);												
										db.PsetString(5, db_value[2]);
										db.PsetString(6, db_value[4]);
										db.PsetString(7, db_value[5]);									
										db.PsetString(8, "");												
										db.PsetString(9, db_value[6]);												
										db.PsetString(10, db_value[7] +db_value[8]+db_value[9]);
										db.PsetString(11, db_value[37]);											
										db.PsetString(12, db_value[36]);
										db.PsetString(13, db_value[14]);
										db.PsetString(14, db_value[29]);
										db.PsetString(15, db_value[16]);
										if("C".equals(db_value[0])){
											db_value[0] = "INS";
										}												
										db.PsetString(16, db_value[0]);		
																															
										db.PsetString(17, db_value[12]+db_value[13]);
										db.PsetString(18, "");
										db.PsetString(19, "");
										db.PsetString(20, "");
										

										db.PsetString(21, db_value[22]);
										db.PsetString(22, db_value[23]);
										db.PsetString(23, "1");
										db.PsetString(24, "세방전지(주) 창원공장");
										if(db_value.length == 44){	
										db.PsetString(25, db_value[42]);
										db.PsetString(26, db_value[43]);
										}else{
										db.PsetString(25, db_value[38]);
										db.PsetString(26, db_value[39]);
										}	
										db.PsetString(27, db_value[24]);
										db.PsetString(28, db_value[25]);
										db.PsetString(29, "");
										db.PsetString(30, "");											

										db.PsetString(31, db_value[26]);
										db.PsetString(32, db_value[27]+" "+db_value[28]);
										db.PsetString(33, "");		

										db.PsetString(34, db_value[16]);
										db.PsetString(35, db_value[17]);
										db.PsetString(36, "1");
										db.PsetString(37, "세방전지(주) 창원공장");
										db.PsetString(38, db_value[21]);
										db.PsetString(39, db_value[21]);
										db.PsetString(40, db_value[18]);
										db.PsetString(41, "");
										db.PsetString(42, "");
										db.PsetString(43, "");
										db.PsetString(44, db_value[26]);
										db.PsetString(45, db_value[27]+" "+db_value[28]);
										db.PsetString(46, db_value[3]);		
										db.PsetString(47, db_value[10]);
										db.PsetString(48, db_value[11]);
										db.PsetString(49, db_value[15]);
										db.PsetString(50, db_value[30]);
										db.PsetString(51, db_value[32]);
										db.PsetString(52, db_value[33]);
										db.PsetString(53, db_value[34]);	
										db.PsetString(54, db_value[35]);
										db.PsetString(55, db_value[38]);
										db.PsetString(56, db_value[39]);												
										db.PsetString(57, db_value[40]);												
										db.PsetString(58, db_value[41]);			
										db.PsetString(59, file);
										
										//System.out.println(""+qry);

										
										if(db.PexecuteUpdate() < 0 ){throw new Exception();}							         	
							  
										db.commit();
										//System.out.println("PO Db Excute !! "+ db_value[1] +db_value[2]  );					

									}catch(SQLException e){
					                    System.out.println("Error"+e);
					                    try{
					                        System.out.println(e);
					                         db.rollback();
					                     }catch(Exception e1){}
					                }
								
										
										
									}else if("GRDT".equals(gubun)){
						         	
										try{  	
						         			
						         			db.PsetString2(1, strYearM);
						         			db.PexecuteQuery2();
						         			if(db.prs2.next()){
						         				strSEQ= db.prs2.getInt("SEQ");		
						         			}
						         								         			
						         			
							         		db.PsetString(1, "005186");
							         		db.PsetString(2, strYearM);
							         		db.PsetInt(3, strSEQ);
							         		db.PsetString(4, db_value[3].substring(0,10));
							         		db.PsetString(5, db_value[3].substring(10,14)+db_value[4]);
							         		db.PsetString(6, db_value[5]);
							         		db.PsetString(7, db_value[6]);
							         		db.PsetInt(8, Integer.parseInt(db_value[10]));
											if(db_value.length == 20){	
							         		db.PsetString(9, db_value[15]);
											}else{
											db.PsetString(9, strYearM);
											}
							         		db.PsetString(10, "");
							         		db.PsetString(11, "");
							         		db.PsetString(12, "");
							         		if("C".equals(db_value[0])){
							         			db_value[0] = "INS";
							         		}						

							         		db.PsetString(13, db_value[0]);
							         		db.PsetString(14, "");
							         	    db.PsetString(15, "");	  								         		
							         		db.PsetString(16, db_value[2]);
							         		db.PsetString(17, db_value[7]);
							         		db.PsetString(18, "");
							         		db.PsetString(19, "");
							         		db.PsetString(20, db_value[9]);
							         		db.PsetString(21, db_value[10]);
							         		db.PsetString(22, db_value[11]);
							         		db.PsetString(23, db_value[12]);
										
										if(db_value.length == 16){	
							         		db.PsetString(24, db_value[16]);
											db.PsetString(25, db_value[17]);
							         		db.PsetString(26, "");
							         		db.PsetString(27, "");
											db.PsetString(28, file);
							         	}else{	
											db.PsetString(24, "");
											db.PsetString(25, "");
							         		db.PsetString(26, "");
							         		db.PsetString(27, "");
											db.PsetString(28, file);
							         	}
											if(db.PexecuteUpdate() < 0 ){
							         			throw new Exception();							         			
							         		}	
							         		db.commit();
							         		System.out.println("GR Db Excute !! ");	
						         		
									}catch(SQLException e){
					                    System.out.println("Error"+e);
					                    try{
					                        System.out.println(e);
					                         db.rollback();
					                     }catch(Exception e1){}
					                }						
									
									
								} // 엔터값 자르기 
									in.close();
								
				             } // for End
				         
					   
				  }catch(IOException ioe){
				   System.out.println("파일리스트 보여주기 실패");
				  }				
				
		
		}catch(Exception e){
			
			System.out.println("파일 다운로드 실패 " + e);
			return ;
		}	
		
	
	db.DB_DisConn();
	System.out.println("METS Db Dis_Conn !! ");
		
		
	}// 메인 종료
	
} // 전체 끝


	
