package skt_fims.dev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import comm.DB_Use_Db_Demon;
import comm.FileUtils;
import comm.OkMroUtil;
import comm.mk_log;
import comm.dev.FTPUtils_local;

public class FimsSendDemon10min {

	static DB_Use_Db_Demon db = null;
	static FTPUtils_local ftpUtil = null;
	static mk_log log = null;
	final static int LOG_FLAG = 1; // 1: log 출력 0: log 출력안함.

	public static void main(String[] args) {
		ftpUtil = new FTPUtils_local();

		if (!ftpUtil.isConnected()) {
			System.out.println("FTP 접속 오류");
			return;
		}

		db = new DB_Use_Db_Demon();
		log = new mk_log();

		log.Write(LOG_FLAG, "FimsSendDemon", " SKT FIMS INF 10MIN SEND DEMON START >>>>> ");
		System.out.println(" SKT FIMS INF 10MIN SEND DEMON START >>>>> ");
		
		db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
		db.user_id="METS_IMSI";
		db.user_pw="METS_IMSI";

		db.DB_Conn();
		log.Write(LOG_FLAG, "FimsSendDemon - 10min", "METS Db CONNECTION !! ");
		System.out.println("METS Db CONNECTION !! ");

		// 2. 배송정보 연동
		//sendFimsDeliScoData();

		// 3. 배송내역 연동
		//sendDeliHisData();
		
		// 3. 배송내역 연동 - 웹서비스
		sendWebDeliDlvrData();

		db.DB_DisConn();
		ftpUtil.disconnectFIMSServer();
		log.Write(LOG_FLAG, "FimsSendDemon", "METS Db Dis_Conn !! ");
		System.out.println("METS Db Dis_Conn !! ");

		log.Write(LOG_FLAG, "FimsSendDemon", "Program End.");

	}// 메인 종료

	public static void sendFimsDeliScoData() {
		// 1. 파일 명 정의
		String time = FileUtils.makeCurrentTime();
		String fileName = "FimsDeliSco_" + time + ".dat";
		String dirName = "if03/input/";

		// 2. 배송정보 대상 조회 및 지정
		String qry = "SELECT count(*) cnt FROM FIMS_DELI_SCO@L_FIMS A " + 
				" WHERE status IN ('INS', 'MOD', 'DEL') " + 
				" AND NOT EXISTS (SELECT 'x' FROM FIMS_DELI_DLVR@L_FIMS WHERE STATUS = 'DEL' AND CON_DIV_SEQ = A.CON_DIV_SEQ) ";
		try {
			db.prepareStatement(qry);
			db.PexecuteQuery();
			if (db.prs.next()) {
				if (db.prs.getInt("cnt") == 0) {
					log.Write(LOG_FLAG, "FimsSendDemon", "3번>>>> 제조사 배송 정보 신규 데이터 없음");
					System.out.println("3번>>>> 제조사 배송 정보 신규 데이터 없음");
					return;
				}
			}else {
				log.Write(LOG_FLAG, "FimsSendDemon", "3번>>>> 제조사 배송 정보 신규 데이터 오류");
				System.out.println("3번>>>> 제조사 배송 정보 신규 데이터 오류");
				return;
			}
			log.Write(LOG_FLAG, "FimsSendDemon", "3번>>>> 제조사 배송 정보 연동 시작: " + fileName);
			System.out.println("3번>>>> 제조사 배송 정보 연동 시작: " + fileName);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// 3. 파일 스트링 만들기
		qry = "SELECT PROD_DIV_SEQ, CON_DIV_SEQ, CNTRT_SEQ, CNTRT_NO, ITEM_SEQ,  "
				+ "     PROD_CORP, CON_PROD_QTY, RCV_EXP_DATE, ZIP, ADDR,  "
				+ "     NOTICE, STATUS "
				+ "  FROM FIMS_DELI_SCO@L_FIMS A  " 
				+ " WHERE status IN ('INS', 'MOD', 'DEL') "
				+ " AND NOT EXISTS (SELECT 'x' FROM FIMS_DELI_DLVR@L_FIMS WHERE STATUS = 'DEL' AND CON_DIV_SEQ = A.CON_DIV_SEQ) "
				+ " order by havis_reg_day, status ";

		try {
			db.prepareStatement(qry);
			db.PexecuteQuery();

			ResultSet selectResult = db.prs;

			// 4. 데이터 스트링으로 반환
			ArrayList<String> poList = FileUtils.makeRowString(selectResult);

			// 5. 파일쓰기
			FileUtils.writeBufferFile(fileName, poList);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 6. 파일 업로드
		boolean ftpResult = ftpUtil.uploadFileToFIMS(dirName, fileName);

		if (ftpResult) {
			// 7. 업로드 성공 처리
			//qry = "UPDATE FIMS_DELI_SCO@L_FIMS A " + "   SET status = DECODE(status, 'INS', 'INS_READ', 'MOD', 'MOD_READ_'||TO_CHAR(SYSDATE, 'yymmddhh24mi'), 'DEL', 'DEL_READ'), "
			qry = "UPDATE FIMS_DELI_SCO@L_FIMS A " + "   SET status = DECODE(status, 'INS', 'INS_READ', 'MOD', 'MOD_READ', 'DEL', 'DEL_READ'), "
					+ "       FTP_REG_DAY = SYSDATE, "
					+ "       ftp_file_name = ?, " 
					+ "       sys_memo = 'FIMS ORD DELI DATA 업로드 완료 ' "
					+ " WHERE status IN ('INS', 'MOD', 'DEL') "
					+ " AND NOT EXISTS (SELECT 'x' FROM FIMS_DELI_DLVR@L_FIMS WHERE STATUS = 'DEL' AND CON_DIV_SEQ = A.CON_DIV_SEQ) ";

			try {
				db.prepareStatement(qry);
				db.PsetString(1, fileName);
				if(db.PexecuteUpdate()<0) {
					alertError("[DEMON] FIMS_DELI_SCO@L_FIMS 업데이트 (READ) 실패");
				}
			} catch (SQLException e1) {
				try {
					System.out.println(e1);
					db.rollback();
				} catch (Exception e) {
				}
			}
			
		} else {
			log.Write(LOG_FLAG, "FimsSendDemon", "FTP 업로드 실패");
			System.out.println("FTP 업로드 실패");
			
			qry = "UPDATE FIMS_DELI_SCO@L_FIMS " + "   SET status = DECODE(status, 'INS', 'INS_ERR', 'MOD', 'MOD_ERR', 'DEL', 'DEL_ERR'), " 
					+ "       FTP_REG_DAY = SYSDATE, "
					+ "       ftp_file_name = ?, " 
					+ "       sys_memo = 'FIMS ORD DELI DATA 업로드 실패 ' "
					+ " WHERE status IN ('INS', 'MOD', 'DEL') "
					+ " AND NOT EXISTS (SELECT 'x' FROM FIMS_DELI_DLVR@L_FIMS WHERE STATUS = 'DEL' AND CON_DIV_SEQ = A.CON_DIV_SEQ) ";

			try {
				db.prepareStatement(qry);
				db.PsetString(1, fileName);
				if(db.PexecuteUpdate()<0) {
					alertError("[DEMON] FIMS_DELI_SCO@L_FIMS 업데이트 (ERR) 실패");
				}
			} catch (SQLException e1) {
				try {
					System.out.println(e1);
					db.rollback();
				} catch (Exception e) {
				}
			}
			alertError("FimsSendDemon10min 3번>>>> FTP 업로드 실패");
		}

		try {
			db.commit();
			//db.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void sendWebDeliDlvrData() {
		Map<String, String> dataSetMap = new HashMap<String, String>();
		String ifResult = "";

	   	// 1. 배송정보 대상 조회 및 지정
	 	String qry = "SELECT count(*) cnt FROM FIMS_DELI_DLVR@L_FIMS " + " WHERE status IN ('INS', 'MOD', 'DEL') ";
	 	try {
	 		db.prepareStatement(qry);
	 		db.PexecuteQuery();
	 		if (db.prs.next()) {
	 			if (db.prs.getInt("cnt") == 0) {
	 				log.Write(LOG_FLAG, "FimsSendDemon", "4번>>>> 제조사 배송 내역 신규 데이터 없음");
	 				System.out.println("4번>>>> 제조사 배송 내역 신규 데이터 없음");
	 				return;
	 			}
	 		}
	 		log.Write(LOG_FLAG, "FimsSendDemon", "4번>>>> 제조사 배송 내역 연동 시작 ");
	 		System.out.println("4번>>>> 제조사 배송 내역 연동 시작 ");

	 	} catch (SQLException e1) {
	 		e1.printStackTrace();
	 	}
	 	
	 	// 3. 전송할 데이터 MultipartEntity 에 저장
	 	qry = "SELECT DLVR_SEQ, PROD_DIV_SEQ, CNTRT_SEQ, CNTRT_NO, "
	 			+ "     ITEM_SEQ, DLV_QTY, FIMS_DELI_DLVR, PROC_TY, TRA_NUM, STATUS " 
	 			+ "  FROM FIMS_DELI_DLVR@L_FIMS "
	 			+ " WHERE status IN ('INS', 'MOD', 'DEL') ";
 		try {
 			db.prepareStatement(qry);
 			db.PexecuteQuery();
 			
 			while(db.prs.next()) {
 				String dlvr_seq = db.prs.getString("DLVR_SEQ");
 				String status = db.prs.getString("STATUS");
 				
 				dataSetMap.put("DLVR_SEQ", dlvr_seq);
				dataSetMap.put("PROD_DIV_SEQ", db.prs.getString("PROD_DIV_SEQ"));
				dataSetMap.put("CNTRT_SEQ", db.prs.getString("CNTRT_SEQ"));
				dataSetMap.put("CNTRT_NO", db.prs.getString("CNTRT_NO"));
				dataSetMap.put("ITEM_SEQ", db.prs.getString("ITEM_SEQ"));
				
				dataSetMap.put("DLV_QTY", db.prs.getString("DLV_QTY"));
				dataSetMap.put("FIMS_DELI_DLVR", db.prs.getString("FIMS_DELI_DLVR"));
				dataSetMap.put("PROC_TY", db.prs.getString("PROC_TY"));
				dataSetMap.put("TRA_NUM", OkMroUtil.checkNull(db.prs.getString("TRA_NUM")));
				dataSetMap.put("STATUS", status);
				
				// 4. 웹서비스I/F 전송 
				ifResult = transmitFIMS(dataSetMap);
				
				if("S".equals(ifResult)) {
					// 5. 웹서비스I/F 성공 처리
					qry = "UPDATE FIMS_DELI_DLVR@L_FIMS " 
							+ "   SET status = DECODE(status, 'INS', 'INS_READ', 'MOD', 'MOD_READ', 'DEL', 'DEL_READ'),  "
							+ "       FTP_REG_DAY = SYSDATE, "
							+ "       ftp_file_name = '[DEMON] WEB SERVICE I/F', " 
							+ "       sys_memo = '[DEMON] FIMS_DELI_DLVR DATA I/F 완료 ' "
							+ " WHERE dlvr_seq = ? AND status = ? ";

					db.prepareStatement(qry);
					db.PsetString(1, dlvr_seq);
					db.PsetString(2, status);
					if(db.PexecuteUpdate() < 0) {
						db.rollback();
						alertError("[DEMON] FIMS_DELI_DLVR@L_FIMS 업데이트 (READ) 실패: "+dlvr_seq);
					}else {
						db.commit();
					}
					
				}else {
					// 5-2. 웹서비스I/F 실패 처리
					qry = "UPDATE FIMS_DELI_DLVR@L_FIMS " 
							+ "   SET status = DECODE(status, 'INS', 'INS_ERR', 'MOD', 'MOD_ERR', 'DEL', 'DEL_ERR'),  "
							+ "       FTP_REG_DAY = SYSDATE, "
							+ "       ftp_file_name = '[DEMON] WEB SERVICE I/F', " 
							+ "       err_msg = ?, " 
							+ "       sys_memo = '[DEMON] WEB SERVICE I/F 실패 RESULT IS IN ERR_MSG ' "
							+ " WHERE dlvr_seq = ? AND status = ? ";

					db.prepareStatement(qry);
					db.PsetString(1, ifResult);
					db.PsetString(2, dlvr_seq);
					db.PsetString(3, status);
					if(db.PexecuteUpdate() < 0) {
						db.rollback();
						alertError("[DEMON] FIMS_DELI_DLVR@L_FIMS 업데이트 (ERR) 실패: "+dlvr_seq);
					}else {
						db.commit();
						alertError("[DEMON] SYSTEM ERROR 4단계 웹서비스 실패 ("+dlvr_seq+")");
					}
				}
 			}

	 	} catch (SQLException e) {
	 		e.printStackTrace();
	 	}
	}
	
	
	public static void sendDeliHisData() {
		// 1. 파일 명 정의
		String time = FileUtils.makeCurrentTime();
		String fileName = "FimsDeliDlvr_" + time + ".dat";
		String dirName = "if04/input/";

		// 2. 배송정보 대상 조회 및 지정
		String qry = "SELECT count(*) cnt FROM FIMS_DELI_DLVR@L_FIMS " + " WHERE status IN ('INS', 'MOD', 'DEL') ";
		try {
			db.prepareStatement(qry);
			db.PexecuteQuery();
			if (db.prs.next()) {
				if (db.prs.getInt("cnt") == 0) {
					log.Write(LOG_FLAG, "FimsSendDemon", "4번>>>> 제조사 배송 내역 신규 데이터 없음");
					System.out.println("4번>>>> 제조사 배송 내역 신규 데이터 없음");
					return;
				}
			}
			log.Write(LOG_FLAG, "FimsSendDemon", "4번>>>> 제조사 배송 내역 연동 시작: " + fileName);
			System.out.println("4번>>>> 제조사 배송 내역 연동 시작: " + fileName);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// 3. 파일 스트링 만들기
		qry = "SELECT DLVR_SEQ, PROD_DIV_SEQ, CNTRT_SEQ, CNTRT_NO, "
				+ "     ITEM_SEQ, DLV_QTY, FIMS_DELI_DLVR, PROC_TY, TRA_NUM, STATUS " 
				+ "  FROM FIMS_DELI_DLVR@L_FIMS "
				+ " WHERE status IN ('INS', 'MOD', 'DEL') ";

		try {
			db.prepareStatement(qry);
			db.PexecuteQuery();

			ResultSet selectResult = db.prs;

			// 4. 데이터 스트링으로 반환
			ArrayList<String> poList = FileUtils.makeRowString(selectResult);

			// 5. 파일쓰기
			FileUtils.writeBufferFile(fileName, poList);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 6. 파일 업로드
		boolean ftpResult = ftpUtil.uploadFileToFIMS(dirName, fileName);

		if (ftpResult) {
			// 7. 업로드 성공 처리
			qry = "UPDATE FIMS_DELI_DLVR@L_FIMS " 
					//+ "   SET status = DECODE(status, 'INS', 'INS_READ', 'MOD', 'MOD_READ_'||TO_CHAR(SYSDATE, 'yymmddhh24mi'), 'DEL', 'DEL_READ'),  "
					+ "   SET status = DECODE(status, 'INS', 'INS_READ', 'MOD', 'MOD_READ', 'DEL', 'DEL_READ'),  "
					+ "       FTP_REG_DAY = SYSDATE, "
					+ "       ftp_file_name = ?, " 
					+ "       sys_memo = 'FIMS_DELI_DLVR DATA 업로드 완료 ' "
					+ " WHERE status IN ('INS', 'MOD', 'DEL') ";

			try {
				db.prepareStatement(qry);
				db.PsetString(1, fileName);
				db.PexecuteQuery();
			} catch (SQLException e1) {
				try {
					System.out.println(e1);
					db.rollback();
				} catch (Exception e) {
				}
			}
		} else {
			log.Write(LOG_FLAG, "FimsSendDemon", "FTP 업로드 실패");
			System.out.println("FTP 업로드 실패");
			
			qry = "UPDATE FIMS_DELI_DLVR@L_FIMS " 
					+ "   SET status = DECODE(status, 'INS', 'INS_ERR', 'MOD', 'MOD_ERR', 'DEL', 'DEL_ERR'), " 
					+ "       FTP_REG_DAY = SYSDATE, "
					+ "       ftp_file_name = ?, " 
					+ "       sys_memo = 'FIMS_DELI_DLVR DATA 업로드 실패 ' "
					+ " WHERE status IN ('INS', 'MOD', 'DEL') ";

			try {
				db.prepareStatement(qry);
				db.PsetString(1, fileName);
				db.PexecuteQuery();
			} catch (SQLException e1) {
				try {
					System.out.println(e1);
					db.rollback();
				} catch (Exception e) {
				}
			}
			alertError("FimsSendDemon10min 4번>>>> FTP 업로드 실패");
		}

		try {
			db.commit();
			//db.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public static String transmitFIMS(Map<String, String> dataSetMap) {
		//String realURL = "";
		String devURL = "http://skhy.happynarae.co.kr:8080//havis_ep2/1024size/popup/fims_deli_webservice_local.jsp";
	
		String ifResult = "";
		
		HttpClient client = new DefaultHttpClient();
		// 1. URL 지정
		HttpPost post = new HttpPost(devURL);   
	   	MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	  
	   	try {
	  		// 2. 전송할 데이터 MultipartEntity 에 저장
	  		entity.addPart("DLVR_SEQ", new StringBody(dataSetMap.get("DLVR_SEQ")));
	  		entity.addPart("PROD_DIV_SEQ", new StringBody(dataSetMap.get("PROD_DIV_SEQ")));
	  		entity.addPart("CNTRT_SEQ", new StringBody(dataSetMap.get("CNTRT_SEQ")));
	  		entity.addPart("CNTRT_NO", new StringBody(dataSetMap.get("CNTRT_NO")));
	  		entity.addPart("ITEM_SEQ", new StringBody(dataSetMap.get("ITEM_SEQ")));
	  		
	  		entity.addPart("DLV_QTY", new StringBody(dataSetMap.get("DLV_QTY")));
	  		entity.addPart("FIMS_DELI_DLVR", new StringBody(dataSetMap.get("FIMS_DELI_DLVR")));
	  		entity.addPart("PROC_TY", new StringBody(dataSetMap.get("PROC_TY")));
	  		entity.addPart("TRA_NUM", new StringBody(dataSetMap.get("TRA_NUM")));
	  		entity.addPart("STATUS", new StringBody(dataSetMap.get("STATUS")));
	  		
	  		post.setEntity(entity);
	  		
	  		// 3. 호출 및 응답내역 수신
	  		HttpResponse response = client.execute(post);
	  		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	  		//ifResult = rd.readLine();
	  	    ifResult = response.getHeaders("result")[0].getValue().toString();
	  	  	System.out.println("인터페이스 결과 ifResult = [" + ifResult +"]");
	  	  	
	  	  	rd.close();
	  		
	   	} catch (IOException e) {
	        ifResult = "F";
	        return ifResult;
	    }
	  return ifResult;
	}
}
