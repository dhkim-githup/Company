import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*; 
import java.util.Date; 


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import comm.DB_Use_Db_Demon;
import comm.mk_log;

public class sebang_Demon {	
	
	public static void main(String[] args) {
		
		DB_Use_Db_Demon db = new DB_Use_Db_Demon();
		mk_log log = new mk_log();
		String err_occur=null, ERR_MSG="";	
		
		db.dbURL = "jdbc:oracle:thin:@172.16.1.208:1521:ndb10"; // ���� �
		db.user_id = "METS18940G"; //�
		db.user_pw = "METS25519P58563W"; //�
		/*		
		db.dbURL = "jdbc:oracle:thin:@172.16.1.224:1521:TESTDB"; // ���� �
		db.user_id = "METS_IMSI"; //�
		db.user_pw = "METS_IMSI"; //�
		*/
		db.DB_Conn();
		System.out.println("METS Db CONNECTION !! ");		
		
		try{	
			
				// ���� ���ϸ� ������ �����ö� ��¥�� �ؼ� ���� �����Դϴ�.				
				
				String strTodayDate = "yyyyMMdd";
				SimpleDateFormat formatter = new SimpleDateFormat(strTodayDate);
				Date date = new Date();
				String strYearM = formatter.format(date).toString();
				
				
				//System.out.println(strTodayDate);	
				
				// ���� ���� ���� 
				// test��
				/* 
				String sServer ="172.16.1.204";
				int iPort =21;
				String sId ="mro";
				String sPassword ="mp240";
				
				String sDownDir ="C://";
				
				String path = "/user/home/mro/src/test/";
				String backup_path ="/user/home/mro/src/test/";		
				*/
				
				
				//�� ���� ���� 
				/**/
				String sServer ="211.54.5.135";
				int iPort =21;
				String sId ="rocket";
				String sPassword ="rocket01";
				
				String sDownDir ="/user/home/mro/db_demon/sebang/data/";
				
//				String path = "/HNARAE/SAP_DEV_OUT/";	
				String path = "/HNARAE/SAP_PRD_OUT/";	
				String backup_path ="/BACKUP_OUT/";
				
							
				// �������� �� ������ ���� 
				//FtpDownloadFile fdf = new FtpDownloadFile();
		
				FTPClient ftpClient;
				
				ftpClient = new FTPClient();
			
				
				// FTP �α��� �ϱ� 
				
				try{
					ftpClient.connect(sServer,iPort);
					int reply;
					//���� �õ��� , �����ߴ��� ���� �ڵ� Ȯ��
					reply = ftpClient.getReplyCode();
					
					if(!FTPReply.isPositiveCompletion(reply)){
						ftpClient.disconnect();
						System.out.println("FTP ���� ���� �ź�");
					}else{
						System.out.println("�������� FTP ���� ����");
						ftpClient.login(sId,sPassword);
					}
					
				}catch(IOException ioe){
					if(ftpClient.isConnected()){
						try{
							ftpClient.disconnect();
						}catch(IOException f){
						}
					
					}
					System.out.println("FTP ���� ���� ����");
				}									
								
				String source ="";
				String backup_source="";
				String name ="";
				
				try{
					   
				   ftpClient.changeWorkingDirectory(path);
			//	   FTPFile[] ftpfiles = ftpClient.listFiles();
				   String[] fileNames = ftpClient.listNames();
				         
				         if (fileNames != null) {				        	
				             for (String file : fileNames) {
				            	 
				            	 source = path +file;	
				            	 backup_source = backup_path +file;	
				            	 
				         		 OutputStream output = null;
				         		
				         		try{			
				         			File local = new File(sDownDir, file);			
				         			output = new FileOutputStream(local);
				       			         			
				         		}catch(IOException ioe){
				         			System.out.println("�ٿ�ε��� ���丮 Ȯ��  ����");
				         		}				
				         		
				         	//	System.out.println(source);	
				         	//	System.out.println(file);
				         		
				         		try{
				         			if(ftpClient.retrieveFile(source, output)){
				         				System.out.println("���� �ٿ�ε� �Ϸ�==> " + file);
				         			}	
								
								 // ��� �ٲٱ� Ȥ�ó� �ؼ��ϴ� �غ��� 
									boolean result = ftpClient.rename(source, backup_source);		
				         		}catch(IOException ioe){
				         			System.out.println("���� �ٿ�ε� ���� ");			
				         		}  		
				         		
				         		
				         	//�ϴ� ���� �б�
				         	String outfilename = sDownDir + file;
				         	//������ �����ϱ� 
				         	String gubun ="";
				         	gubun = file.substring(7,11);
				         //	System.out.println(gubun);	 
				         	
				         	
				         		try{ 
				         			
									FileReader in = new FileReader(outfilename);
									int c;
									String s = new String();
									while((c= in.read()) != -1 ){
										s = s + (char)c;
									}
									//System.out.println("�ؽ�Ʈ�� ����ִ� ���� ����===============>" + s);
									
									if("POMT".equals(gubun)){
									// ���� ���ϱ� 
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
									// ���� ���ϱ� 
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
									
								// ���Ͱ� ¥���� | ������ ¥���� �򰥷��� �ΰ��� ������ 
								String Enter_balue[] = s.split("\n");								
								// System.out.println("�Ѱ���==>" + Enter_balue.length);		
									for(int i = 0; i < Enter_balue.length; i++){
									String db_value[] = Enter_balue[i].split("\\|");																
									int k =0; 
									// System.out.println("������==>" + db_value.length);
									// DB �ֱ� 
									int strSEQ=1;

									// ���� Ȯ�� �ϴ±��� 
									for(int j = 0; j < db_value.length; j++){
										k++;										
							         	if("POMT".equals(gubun)){
							         		//System.out.println("�������� ");
							         		//System.out.println(k+"��======>"+db_value[j]);						         	
							         	}else if("GRDT".equals(gubun)){
							         		//System.out.println(k+"��======>"+db_value[j]);
							         		//System.out.println("�԰�����");								         	}										
							         	}  // | ������ �ڸ���
									} 	
													
									if("POMT".equals(gubun)){
										
										try{  	
						         			
						         			db.PsetString2(1, strYearM);
						         			db.PexecuteQuery2();
						         			if(db.prs2.next()){
						         				strSEQ= db.prs2.getInt("SEQ");		
						         			}         	
						         			
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
								         		db.PsetString(24, "��������(��) â������");
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
								         		db.PsetString(37, "��������(��) â������");
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
								         		//System.out.println("PO Db Excute !! ");	
								         	
						         		
						         		}catch(SQLException e){
						                    System.out.println("Error"+e);
						                    try{
						                        System.out.println(e);
						                         db.rollback();
						                     }catch(Exception e1){}
						                }
										
										
									}else if("GRDT".equals(gubun)){
						         	
										try{  	
						         			// ���� ���ϱ� 
						         			
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
						         	}							
									
									
								} // ���Ͱ� �ڸ��� 
									in.close();
								}catch(IOException ie){
									System.out.println("������ �������� �ʽ��ϴ�.");
								}catch(Exception e){
									System.out.println(e);
								}	
				             } // for End
				         }
					   
				  }catch(IOException ioe){
				   System.out.println("���ϸ���Ʈ �����ֱ� ����");
				  }
				
				  //�α� �ƿ� �ϱ� 
				  try{
					  	ftpClient.logout();
					  	System.out.println("�������� FTP �α� �ƿ�");			
						ftpClient.disconnect();
				  }catch(IOException ioe){
						ioe.printStackTrace();
				  }	
		
		}catch(Exception e){
			
			System.out.println("���� �ٿ�ε� ���� " + e);
			return ;
		}	
		
	
	db.DB_DisConn();
	System.out.println("METS Db Dis_Conn !! ");
		
		
	}// ���� ����
	
} // ��ü ��


	
