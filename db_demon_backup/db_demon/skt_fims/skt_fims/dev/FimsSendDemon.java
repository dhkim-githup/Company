package skt_fims.dev;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comm.DB_Use_Db_Demon;
import comm.FileUtils;
import comm.mk_log;
import comm.dev.FTPUtils_local;

public class FimsSendDemon {

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

		log.Write(LOG_FLAG, "FimsSendDemon", " SKT FIMS INF SEND DEMON START >>>>> ");
		System.out.println(" SKT FIMS INF SEND DEMON START >>>>> ");

		db.dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
		db.user_id="METS_IMSI";
		db.user_pw="METS_IMSI";
		
		db.DB_Conn();
		log.Write(LOG_FLAG, "FimsSendDemon", "METS Db CONNECTION !! ");
		System.out.println("METS Db CONNECTION !! ");

		// 1. 납품지시내역 전송
		sendFimsOrdData();

		db.DB_DisConn();
		//ftpUtil.disconnectFIMSServer();
		log.Write(LOG_FLAG, "FimsSendDemon", "METS Db Dis_Conn !! ");
		System.out.println("METS Db Dis_Conn !! ");

		log.Write(LOG_FLAG, "FimsSendDemon", "Program End.");

	}// 메인 종료

	public static void sendFimsOrdData() {
		// 1. 파일 명 정의
		String time = FileUtils.makeCurrentTime();
		String fileName = "FimsOrdData_" + time + ".dat";
		String dirName = "if01/input/";

		// 2. PO데이터 대상 조회 및 지정
		String qry = "SELECT count(*) cnt FROM fims_ord_deli@L_FIMS " + " WHERE status IN ('INS', 'DEL') ";
		try {
			db.prepareStatement(qry);
			db.PexecuteQuery();
			if (db.prs.next()) {
				if (db.prs.getInt("cnt") == 0) {
					log.Write(LOG_FLAG, "FimsSendDemon", "1번>>>> 납품지시내역 신규 데이터 없음");
					System.out.println("1번>>>> 납품지시내역 신규 데이터 없음");
					return;
				}
			}
			log.Write(LOG_FLAG, "FimsSendDemon", "1번>>>> 납품지시내역 연동 시작: " + fileName);
			System.out.println("1번>>>> 납품지시내역 연동 시작: " + fileName);
			
			/*
			qry = "UPDATE fims_ord_deli@L_FIMS " + "   SET status = 'INS_READ' " + " WHERE status = 'INS' ";
			db.prepareCall(qry);
			db.executeQuery(qry);
			*/

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// 3. 파일 스트링 만들기
		qry = "SELECT VU_SKT_PO_SEQ, CNTRT_SEQ, CNTRT_MOD, CNTRT_TYPE, CNTRT_NO, "
				+ "     CNTRT_NAME, CNTRT_AMT, CNTRT_WRITE_DATE, CNTRT_START_DATE, CNTRT_END_DATE, "
				+ "     RFX_REG_NAME, BUSI_DEPT_NAME, BUSI_DEPT_CD, BUSI_EMAIL, BUSI_PHONE_NO, "
				+ "     BUSI_CELL_PHONE, ITEM_SEQ, PO_DATE, SRC_GRP_NAME, ITEM_NO, ITEM_NAME, "
				+ "     ITEM_SPEC, ITEM_QUANTITY, ITEM_UNIT, ITEM_PRICE, "
				+ "     TO_CHAR(PROC_DATI, 'YYYY-MM-DD HH24:MI:SS') PROC_DATI, DECODE(STATUS, 'INS', 'INS_READ', STATUS) STATUS, ERR_MSG, TO_CHAR(REG_DATI, 'YYYY-MM-DD HH24:MI:SS') REG_DATI " + "  FROM fims_ord_deli@L_FIMS "
				+ " WHERE status IN ('INS', 'DEL') ORDER BY CNTRT_SEQ ";

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

		if (ftpResult) { // 7. 업로드 성공 처리
			qry = "UPDATE fims_ord_deli@L_FIMS " + "   SET status = DECODE(status, 'INS', 'INS_SEND', 'DEL', 'DEL_READ'), " + "       proc_dati = SYSDATE, "
					+ "       ftp_file_name = ?, " + "       sys_memo = 'FIMS ORD DELI DATA 업로드 완료 ' "
					+ " WHERE status IN ('INS', 'DEL') ";

			try {
				db.prepareStatement(qry);
				db.PsetString(1, fileName);
				if(db.PexecuteUpdate()<0) {
					alertError("[DEMON] fims_ord_deli@L_FIMS 업데이트 (READ) 실패");
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
			
			qry = "UPDATE fims_ord_deli@L_FIMS " + "   SET status = DECODE(status, 'INS', 'INS_ERR', 'DEL', 'DEL_ERR'), " + "       proc_dati = SYSDATE, "
					+ "       ftp_file_name = ?, " + "       sys_memo = 'FIMS ORD DELI DATA 업로드 실패 ' "
					+ " WHERE status IN ('INS', 'DEL') ";

			try {
				db.prepareStatement(qry);
				db.PsetString(1, fileName);
				if(db.PexecuteUpdate()<0) {
					alertError("[DEMON] fims_ord_deli@L_FIMS 업데이트 (ERR) 실패");
				}
			} catch (SQLException e1) {
				try {
					System.out.println(e1);
					db.rollback();
				} catch (Exception e) {
				}
			}
			alertError("[DEMON] fims_ord_deli - FTP 업로드 실패");
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

}
