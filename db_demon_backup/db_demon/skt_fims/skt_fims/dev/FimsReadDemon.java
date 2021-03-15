package skt_fims.dev;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import comm.DB_Use_Db_Demon;
import comm.FileUtils;
import comm.mk_log;
import comm.dev.FTPUtils_local;

public class FimsReadDemon {

	static DB_Use_Db_Demon db = null;
	static FTPUtils_local ftpUtil = null;
	static mk_log log = null;
	final static int LOG_FLAG = 1; // 1: log 출력 0: log 출력안함. 

	public static void main(String[] args) {
		ftpUtil = new FTPUtils_local();
		
		if(!ftpUtil.isConnected()) {
			log.Write(LOG_FLAG, "FimsReadDemon", " FTP CONNECTION FAIL ");
			System.out.println("FTP 접속 실패 - 프로그램 종료");
			return;
		}
		
		db = new DB_Use_Db_Demon();
		log = new mk_log();
		
		log.Write(LOG_FLAG, "FimsReadDemon", " SKT FIMS INF READ DEMON START >>>>> ");
		System.out.println(" SKT FIMS INF READ DEMON START >>>>> ");
		
		db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
		db.user_id="METS_IMSI";
		db.user_pw="METS_IMSI";
		
		db.DB_Conn();
		log.Write(LOG_FLAG, "FimsReadDemon", "METS Db CONNECTION !! ");
		System.out.println("METS Db CONNECTION !! ");
		
		// 1. 자재수량 분배내역 읽기
		readConDivData();
		
		// 2. 인수승인 정보 읽기
		readInsuData();

		db.DB_DisConn();
		ftpUtil.disconnectFIMSServer();
		log.Write(LOG_FLAG, "FimsReadDemon", "METS Db Dis_Conn !! ");
		System.out.println("METS Db Dis_Conn !! ");
		
		log.Write(LOG_FLAG, "FimsReadDemon", "Program End.");
		
	}
	
	public static void readConDivData() {
		String dirName = "if02/output/";
		String succDirName = "if02/succ/";
		
		// FTP 서버에 읽을 파일 있는지 확인
		String[] fileList = ftpUtil.getNewFileList(dirName);
		
		if(fileList == null || fileList.length==0) {
			log.Write(LOG_FLAG, "FimsReadDemon", "2번>>>> 자재 수량 분배 신규 데이터 없음.");
			System.out.println("2번>>>> 자재 수량 분배 신규 데이터 없음.");
			return;
		}
		
		// 파일 처리
		for(String fileName:fileList) {
			log.Write(LOG_FLAG, "FimsReadDemon", "2번>>>> 자재수량분배내역 다운로드 시작: "+fileName);
			System.out.println("2번>>>> 자재수량분배내역 다운로드 시작: "+fileName);
			
			// FTP 파일 다운로드
			ftpUtil.readFileFromFTP(dirName, fileName);
			
			// 2. 파일 읽어오기
			try {
				ArrayList<String[]> readData = FileUtils.readBufferFile(fileName);
				
				if(readData == null) {
					return;
				}
				
				String qry = "INSERT INTO FIMS_DIV_CORP@L_FIMS (CON_DIV_SEQ, VU_SKT_PO_SEQ, CNTRT_SEQ, CNTRT_NO, ITEM_SEQ, " +
						"                           CORP_NM, CORP_TEL_NO, MANGAER_CORP, DIV_MANAGER_NM, MANAGER_TEL_NO, " +
						"                           DIV_DATE, ITEM_QTY, ZIP, ADDR, EMER_YN, " +
						"                           NOTICE, STATUS, FTP_FILE_NAME, HAVIS_REG_DAY, FTP_REG_DAY, " +
						"                           ERR_MSG, SYS_MEMO) " +
						"     VALUES (?, ?, ?, ?, ?, " +
						"         ?, ?, ?, ?, ?, " +
						"         ?, ?, ?, ?, ?, " +
						"         ?, ?, ?, '', SYSDATE, " +
						"         '', 'FTP READ DATA 입력: readConDivData()') "; 
				
				//3. 출력
				for(String[] al:readData) {
					int i = 1;
					
					db.prepareStatement(qry);
					for(String s: al) {
						//System.out.print(i + s+" ");
						if(i==7) {
							s = s.replace(")", "-");
						}else if(i==13) { //우편번호 - 삭제
							s = s.replace("-", "");
						}
						db.PsetString(i++, s);
					}
					db.PsetString(i++, fileName);
					
					//System.out.println("");
					
					if (db.PexecuteUpdate() < 0) {
						ftpUtil.renameFile(dirName+fileName, "if02/fail/"+fileName);
						throw new SQLException(db.ERR_MSG);
					}
					
				}
				
				db.commit();
				ftpUtil.renameFile(dirName+fileName, succDirName+fileName);
				
				log.Write(LOG_FLAG, "FimsReadDemon", "2번>>>> 다운로드 완료 / 건수: "+readData.size());
				System.out.println("2번>>>> 다운로드 완료 / 건수: "+readData.size());
				
			} catch (IOException e) {

				log.Write(LOG_FLAG, "FimsReadDemon", "Error" + e);
				System.out.println("Error" + e);
				try {
					db.rollback();
				} catch (Exception e1) {
				} finally {
					alertError("FimsReadDemon 2번>>>> Error");
				}
				
			} catch (SQLException e) {
				log.Write(LOG_FLAG, "FimsReadDemon", "Error" + e);
				System.out.println("Error" + e);
				try {
					db.rollback();
				} catch (Exception e1) {
				} finally {
					alertError("FimsReadDemon 2번>>>> Error");
				}
			}
		}
		
	}
	
	public static void readInsuData() {
		String dirName = "if05/output/";
		String succDirName = "if05/succ/";
		
		// FTP 서버에 읽을 파일 있는지 확인
		String[] fileList = ftpUtil.getNewFileList(dirName);
		
		if(fileList == null || fileList.length==0) {
			log.Write(LOG_FLAG, "FimsReadDemon", "5번>>>> 인수 승인 신규 데이터 없음.");
			System.out.println("5번>>>> 인수 승인 신규 데이터 없음.");
			return;
		}
		
		// 파일 처리
		for(String fileName:fileList) {
			log.Write(LOG_FLAG, "FimsReadDemon", "5번>>>> 인수 승인 내역 다운로드 시작: "+fileName);
			System.out.println("5번>>>> 인수 승인 내역 다운로드 시작: "+fileName);
			
			// FTP 파일 다운로드
			ftpUtil.readFileFromFTP(dirName, fileName);
			
			// 2. 파일 읽어오기
			try {
				ArrayList<String[]> readData = FileUtils.readBufferFile(fileName);
				
				if(readData == null) {
					return;
				}
				
				// 쿼리 정의
				String qry = "INSERT INTO FIMS_APPR@L_FIMS (DLVR_SEQ, CNTRT_SEQ, CNTRT_NO, ITEM_SEQ, CORP_ID,  " +
						"                            INSU_APPR_NM, INSU_APPR_DATE, STATUS, FTP_FILE_NAME, HAVIS_REG_DAY, " +
						"                            FTP_REG_DAY, ERR_MSG, SYS_MEMO) " +
						"    VALUES (?, ?, ?, ?, ?, " +
						"        ?, ?, ?, ?, '',  " +
						"        SYSDATE, '', 'FTP READ DATA 입력: readInsuData()') " ;
				
				//3. 출력
				for(String[] al:readData) {
					int i = 1;
					db.prepareStatement(qry);
					
					for(String s: al) {
						//System.out.print(s+" ");
						db.PsetString(i++, s);
					}
					db.PsetString(i++, fileName);
					//System.out.println("");
					
					if (db.PexecuteUpdate() < 0) {
						ftpUtil.renameFile(dirName+fileName, "if05/fail/"+fileName);
						throw new SQLException("INSERT ERROR");
					}
				}
				
				db.commit();
				ftpUtil.renameFile(dirName+fileName, succDirName+fileName);
				
				log.Write(LOG_FLAG, "FimsReadDemon", "5번>>>> 다운로드 완료 / 건수: "+readData.size());
				System.out.println("5번>>>> 다운로드 완료 / 건수: "+readData.size());
			} catch (IOException e) {
				e.printStackTrace();
				alertError("FimsReadDemon 5번>>>> Error");
			} catch (SQLException e) {
				log.Write(LOG_FLAG, "FimsReadDemon", "Error" + e);
				System.out.println("Error" + e);
				try {
					System.out.println(e);
					db.rollback();
				} catch (Exception e1) {
				}
				alertError("FimsReadDemon 5번>>>> Error");
			}
		}
		
	}
	
	public static void alertError(String msg) {
		
		System.out.println(" SKT FIMS INF ERROR REPORT >>>>> ");
		
		db.prepareCall("{call P_SMS_ALERT.P_ERROR_ALARM(?,'FIMS') }");
		
		try {
			db.CsetString(1, msg);
			db.Cexecute();
		} catch (SQLException e1) {
			try {
				System.out.println(e1);
				db.rollback();
			} catch (Exception e) {
			}
		}
		
		try {
			db.commit();
			//db.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
