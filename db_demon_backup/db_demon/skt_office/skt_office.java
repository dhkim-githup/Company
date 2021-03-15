/**
	SKT 사무용품 인터페이스 JSP 파일 대체위한 데몬 생성
	WORK : SKT 배송정보, 입고정보 전송 데몬 (주기: 30분)
**/
package skt_office;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import comm.mk_log;

public class skt_office {
	static private mk_log log = new mk_log();
	static public int log_flag = 1;
	
	static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
	static final String PROG_NM = "/user/home/mro/db_demon/skt_office/skt_office.java";
	
	static final String realURL = "http://203.236.1.130/mro/interface/outer/";
	static final String devURL = "http://erpwbqx1.sktelecom.com/mro/interface/outer/";
	//static final String localURL = "http://203.236.1.130/mro/test.asp";
	
	static final String DELIMITER = "|";
	
	public static void main(String[] args) {
		log.Write(log_flag, PROG_NM, "-------------Start skt_office daemon-------------");
		db = new comm.DB_Use_Db_Demon();
		
		// TODO : 운영 시에는 해당 부분 주석처리 해야 함
		/*db.dbURL="jdbc:oracle:thin:@172.16.3.24:1521:TESTDB";
		db.user_id="METS18940G";
		db.user_pw="METS18940G070%";*/
		
		db.CUST_ID = "M0767002";
    	db.PROG_NM = PROG_NM;
		db.DB_Conn();
		System.out.println("Db Connection !! ");
		log.Write(log_flag, PROG_NM, "Db Connection !!");
		
		// Call Methods 1 : 주문확인 정보 전송
		sendOrdSure(realURL);
		
		// Call Methods 2 : 배송정보 전송
		sendTraInfo(realURL);
		
		// Call Methods 3 : 입고정보 전송
		sendIbgoData(realURL);
		
		// Call Methods 3 : 반품입고정보 전송
		sendRejtIbgo(realURL);
		
		// Call Methods 4 : 카탈로그 정보 전송
		sendCatalogueInfo(realURL);
		
		db.DB_DisConn();
		System.out.println("Db Dis_Conn !! ");
		log.Write(log_flag, PROG_NM, "Db Dis_Conn !!");
	}
	
	public static void sendCatalogueInfo(String runMode) {
		String sendURL = runMode + "inf_catalog_ok.asp";
		System.out.println("[SKT_SEND_CATA] 연동 시작: " + sendURL);
		log.Write(log_flag, PROG_NM, "[SKT_SEND_CATA] 연동 시작: " + sendURL);
		
		Map<String, String> sendData = new HashMap<String, String>();
		String sendPrdCd = "";
		String sendPlisNm = "";
		String sendUnit = "";
		String sendContUpri = "";
		String sendUpri = "";
		String sendMakeCoNm = "";
		String sendSpec = "";
		String sendMinCnt = "";
		String sendSKTCateCd = "";
		String sendImgPath = "";
		String sendImgPath2 = "";
		String sendInfReqStat = "";
		String sendContFlag = "";
		String sendInfMroCd = "";
		
		String handleNo = getHandleNo();
		String ifResult = "";
		
		try {
			
			// 1. 상태 업데이트 프로시저 호출 준비
			String procedure_qry = "{call inf_skt.P_CATA_SEND_PROC('','M0767002','000005',?,?,"+handleNo+",'Y','',?,?)}";
			db.prepareCall(procedure_qry);
			db.cs.registerOutParameter(3,Types.INTEGER);
			db.cs.registerOutParameter(4,Types.VARCHAR);
		
			// 2. 보낼 데이터 SELECT 수행
			String select_qry = "SELECT * " +
								"  FROM skt_cata " +
								" WHERE enpri_cd = '000005' AND proc_flag = 'N' "; 
			db.executeQuery(select_qry);
			
			while(db.rs.next()) {
				int seq = db.rs.getInt("seq");
				String prd_cd = db.rs.getString("prd_cd");
				String plis_nm = db.rs.getString("plis_nm");
				String unit = db.rs.getString("unit");
				String cont_upri = db.rs.getString("cont_upri");
				String upri = db.rs.getString("upri");
				
				String make_co_nm = db.rs.getString("make_co_nm");
				String spec = db.rs.getString("spec").replaceAll("&", "%26");
				String min_cnt = db.rs.getString("min_cnt");
				String skt_cate_cd = db.rs.getString("skt_cate_cd");
				String img_path = db.rs.getString("img_path");
				
				String img_path2 = db.rs.getString("img_path2");
				String inf_req_stat = db.rs.getString("inf_req_stat");
				String cont_flag = db.rs.getString("cont_flag");
				String inf_mro_cd = db.rs.getString("inf_mro_cd");
				
				// 2-1. 보낼 파라미터 지정
				sendPrdCd += prd_cd+DELIMITER;
				sendPlisNm += plis_nm+DELIMITER;
				sendUnit += unit+DELIMITER;
				sendContUpri += cont_upri+DELIMITER;
				sendUpri += upri+DELIMITER;
				
				sendMakeCoNm += make_co_nm+DELIMITER;
				sendSpec += spec+DELIMITER;
				sendMinCnt += min_cnt+DELIMITER;
				sendSKTCateCd += skt_cate_cd+DELIMITER;
				sendImgPath += img_path+DELIMITER;
				
				sendImgPath2 += img_path2+DELIMITER;
				sendInfReqStat += inf_req_stat+DELIMITER;
				sendContFlag += cont_flag+DELIMITER;
				sendInfMroCd += inf_mro_cd+DELIMITER;
				
				// 2-2. 상태 업데이트 프로시저 수행 ( PROC_FLAG: N -> S )
				db.CsetString(1,prd_cd);
				db.CsetInt(2,seq);
				db.Cexecute(); 

				if(db.ERR_FLAG < 0 || db.cs.getInt(3) < 0) {
					db.rollback();
					alertError("[SKT OFFICE]CALL P_CATA_SEND_PROC ERROR");
				}
			}
			
			if("".equals(sendPrdCd)) {
				System.out.println("NO DATA :: SKT_SEND_CATA ");
				log.Write(log_flag, PROG_NM, "NO DATA :: SKT_SEND_CATA ");
				return;
			}
			
			// 3. Map 변수에 파라미터 저장
			sendData.put("pdt_cd", sendPrdCd);
			sendData.put("pdt_nm", sendPlisNm);
			sendData.put("pdt_unit", sendUnit);
			sendData.put("pdt_ctprice", sendContUpri);
			sendData.put("pdt_price", sendUpri);
			
			sendData.put("pdt_co", sendMakeCoNm);
			sendData.put("pdt_content", sendSpec);
			sendData.put("pdt_minorder", sendMinCnt);
			sendData.put("category_cd", sendSKTCateCd);
			sendData.put("pdt_bimage", sendImgPath);
			
			sendData.put("pdt_status", sendInfReqStat);
			sendData.put("pdt_fract",sendContFlag );
			sendData.put("pdt_image", sendImgPath2);
			sendData.put("company_cd", sendInfMroCd);
			
			sendData.put("return_url", "http://www.happynarae.com/1024size/skt_inf/skt_cata_send_res.jsp");
			sendData.put("handle_no", handleNo);
			
			System.out.println("[SKT_SEND_CATA] START HTTP POST");
			log.Write(log_flag, PROG_NM, "[SKT_SEND_CATA] START HTTP POST");
			ifResult = sendDataToSKT(sendURL, sendData);
			System.out.println("[SKT_SEND_CATA] HTTP POST RESULT :: " + ifResult + "\n");
			log.Write(log_flag, PROG_NM, "[SKT_SEND_CATA] HTTP POST RESULT :: " + ifResult + "\n");
			
			if("S".equals(ifResult)) {
				// TO-DO: 커밋으로 바꾸기
				db.commit();
				//db.rollback();
			} else {
				db.rollback();
			}
			
		} catch (SQLException e) {
			System.out.println("Error"+e);
			log.Write(log_flag, PROG_NM, "Error"+e);
            try{
            	db.rollback();
            	alertError("[SKT OFFICE]SKT_SEND_CATA ERROR :: DB UPDATE");
             }catch(Exception e1){}
		}
	}
	
	public static void sendOrdSure(String runMode) {
		String sendURL = runMode + "inf_orderconfirmrev.asp";
		System.out.println("[SKT_ORD_SURE] 연동 시작: " + sendURL);
		log.Write(log_flag, PROG_NM, "[SKT_ORD_SURE] 연동 시작: " + sendURL);
		
		Map<String, String> sendData = new HashMap<String, String>();
		String sendCordsNum = "";
		String sendClistNum = "";
		String sendDeliDati = "";
		
		String handleNo = getHandleNo();
		String ifResult = "";
		
		try {
			
			// 1. 상태 업데이트 쿼리 수행 준비
			String update_qry = "UPDATE skt_ord_sure " +
					"   SET proc_no = '"+handleNo+"', proc_flag = 'S', proc_dati = SYSDATE, sys_memo = '"+PROG_NM+"' " +
					" WHERE     enpri_cd = '000005' " +
					"       AND cords_num = ? " +
					"       AND clist_num = ? " +
					"       AND seq = ? " ;
			db.prepareStatement(update_qry);
		
			// 2. 보낼 데이터 SELECT 수행
			String select_qry = "SELECT cords_num, clist_num, deli_dati, seq " +
								"  FROM skt_ord_sure " +
								" WHERE enpri_cd = '000005' AND proc_flag = 'N' "; 
			db.executeQuery(select_qry);
			
			while(db.rs.next()) {
				String cords_num = db.rs.getString("cords_num");
				String clist_num = db.rs.getString("clist_num");
				String deli_dati = db.rs.getString("deli_dati");
				int seq = db.rs.getInt("seq");
				
				// 2-1. 보낼 파라미터 지정
				sendCordsNum += cords_num+DELIMITER;
				sendClistNum += clist_num+DELIMITER;
				sendDeliDati += deli_dati+DELIMITER;
				
				// 2-2. 상태 업데이트 프로시저 수행 ( PROC_FLAG: N -> S )
				db.PsetString(1,cords_num); 
				db.PsetString(2,clist_num);
				db.PsetInt(3,seq);
				if(db.PexecuteUpdate() < 0) {
					db.rollback();
					alertError("[SKT OFFICE]UPDATE SKT_ORD_SURE ERROR");
					return;
				}
			}
			
			if("".equals(sendCordsNum)) {
				System.out.println("NO DATA :: SKT_ORD_SURE ");
				log.Write(log_flag, PROG_NM, "NO DATA :: SKT_ORD_SURE ");
				return;
			}
			
			// 3. Map 변수에 파라미터 저장
			sendData.put("order_no", sendCordsNum);
			sendData.put("order_unitno", sendClistNum);
			sendData.put("delivery_dt", sendDeliDati);
			sendData.put("return_url", "http://www.happynarae.com/1024size/skt_inf/skt_ord_sure_res.jsp");
			sendData.put("handle_no", handleNo);
			
			System.out.println("[SKT_ORD_SURE] START HTTP POST");
			log.Write(log_flag, PROG_NM, "[SKT_ORD_SURE] START HTTP POST ");
			ifResult = sendDataToSKT(sendURL, sendData);
			System.out.println("[SKT_ORD_SURE] HTTP POST RESULT :: " + ifResult + "\n");
			log.Write(log_flag, PROG_NM, "[SKT_ORD_SURE] HTTP POST RESULT :: " + ifResult + "\n");
			
			if("S".equals(ifResult)) {
				// TO-DO: 커밋으로 바꾸기
				db.commit();
				db.rollback();
			} else {
				db.rollback();
			}
			
		} catch (SQLException e) {
			System.out.println("Error"+e);
			log.Write(log_flag, PROG_NM, "Error"+e);
            try{
            	db.rollback();
            	alertError("[SKT OFFICE]SKT_ORD_SURE ERROR :: DB UPDATE");
             }catch(Exception e1){}
		}
	}
	
	public static void sendTraInfo(String runMode) {		// 배송정보 전송 메소드
		String sendURL = runMode + "inf_deliveryinforev.asp";
		System.out.println("[SKT_TRA_INFO] 연동 시작: " + sendURL);
		log.Write(log_flag, PROG_NM, "[SKT_TRA_INFO] 연동 시작: " + sendURL);
		
		Map<String, String> sendData = new HashMap<String, String>();
		String sendCordsNum = "";
		String sendClistNum = "";
		String sendTraNum = "";
		
		String handleNo = getHandleNo();
		String ifResult = "";
		
		try {
		
			// 1. 상태 업데이트 프로시저 호출 준비
			String procedure_qry = "{call inf_skt.P_TRA_IFNO_SEND_PROC('','-------','000005',?,?,?,?,'S', "+handleNo+",'"+PROG_NM+"',?,?)}";
			db.prepareCall(procedure_qry);
			db.cs.registerOutParameter(5,Types.INTEGER);
			db.cs.registerOutParameter(6,Types.VARCHAR);
		
			// 2. 보낼 데이터 SELECT 수행
			String select_qry = "SELECT cords_num, clist_num, tra_num, seq " +
								"  FROM skt_tra_info " +
								" WHERE enpri_cd = '000005' AND proc_flag = 'N' "; 
			db.executeQuery(select_qry);
			
			while(db.rs.next()) {
				String cords_num = db.rs.getString("cords_num");
				String clist_num = db.rs.getString("clist_num");
				String tra_num = db.rs.getString("tra_num");
				int seq = db.rs.getInt("seq");
				
				// 2-1. 보낼 파라미터 지정
				sendCordsNum += cords_num+DELIMITER;
				sendClistNum += clist_num+DELIMITER;
				sendTraNum += tra_num+DELIMITER;
				
				// 2-2. 상태 업데이트 프로시저 수행 ( PROC_FLAG: N -> S )
				db.CsetString(1,cords_num);
				db.CsetString(2,clist_num);
				db.CsetString(3,tra_num);
				db.CsetInt(4,seq);
				db.Cexecute(); 

				if(db.ERR_FLAG < 0 || db.cs.getInt(5) < 0) {
					db.rollback();
					alertError("[SKT OFFICE]CALL P_TRA_IFNO_SEND_PROC ERROR");
				}
			}
			
			if("".equals(sendCordsNum)) {
				System.out.println("NO DATA :: SKT_TRA_INFO ");
				log.Write(log_flag, PROG_NM, "NO DATA :: SKT_TRA_INFO ");
				return;
			}
			
			// 3. Map 변수에 파라미터 저장
			sendData.put("order_no", sendCordsNum);
			sendData.put("order_unitno", sendClistNum);
			sendData.put("delivery_no", sendTraNum);
			sendData.put("return_url", "http://www.happynarae.com/1024size/skt_inf/skt_tra_info_res.jsp");
			sendData.put("handle_no", handleNo);
			
			System.out.println("[SKT_TRA_INFO] START HTTP POST");
			log.Write(log_flag, PROG_NM, "[SKT_TRA_INFO] START HTTP POST");
			ifResult = sendDataToSKT(sendURL, sendData);
			System.out.println("[SKT_TRA_INFO] HTTP POST RESULT :: " + ifResult + "\n");
			log.Write(log_flag, PROG_NM, "[SKT_TRA_INFO] HTTP POST RESULT :: " + ifResult + "\n");
			
			if("S".equals(ifResult)) {
				// TO-DO: 커밋으로 바꾸기
				db.commit();
				//db.rollback();
			} else {
				db.rollback();
			}
			
		} catch (SQLException e) {
			System.out.println("Error"+e);
			log.Write(log_flag, PROG_NM, "Error"+e);
            try{
            	db.rollback();
            	alertError("[SKT OFFICE]TRA_INFO ERROR :: DB UPDATE");
             }catch(Exception e1){}
		}
		
	}
	
	public static void sendIbgoData (String runMode) {
		String sendURL = runMode + "inf_ibgoinforev.asp";
		System.out.println("[SKT_IBGO_S] 연동 시작: " + sendURL);
		log.Write(log_flag, PROG_NM, "[SKT_IBGO_S] 연동 시작: " + sendURL);
		
		Map<String, String> sendData = new HashMap<String, String>();
		String sendCordsNum = "";
		String sendClistNum = "";
		String sendTraNum = "";
		String sendCompDati = "";
		String sendComp = "";
		
		String handleNo = getHandleNo();
		String ifResult = "";
		
		try {
		
			// 1. 상태 업데이트 쿼리 수행 준비
			String update_qry = "UPDATE skt_ibgo_s " +
					"   SET proc_no = '"+handleNo+"', proc_flag = 'S', proc_dati = SYSDATE, sys_memo = '"+PROG_NM+"' " +
					" WHERE     enpri_cd = '000005' " +
					"       AND ords_ty = '001' " +
					"       AND cords_num = ? " +
					"       AND clist_num = ? " +
					"       AND tra_num = ? " +
					"       AND seq = ? " ;
			db.prepareStatement(update_qry);
		
			// 2. 보낼 데이터 SELECT 수행
			String select_qry = "SELECT cords_num, clist_num, tra_num, seq, comp_dati, comp " +
								"  FROM skt_ibgo_s " +
								" WHERE enpri_cd = '000005' AND proc_flag = 'N' "; 
			db.executeQuery(select_qry);
			
			while(db.rs.next()) {
				String cords_num = db.rs.getString("cords_num");
				String clist_num = db.rs.getString("clist_num");
				String tra_num = db.rs.getString("tra_num");
				int seq = db.rs.getInt("seq");
				String comp_dati = db.rs.getString("comp_dati");
				String comp = db.rs.getString("comp");
				
				// 2-1. 보낼 파라미터 지정
				sendCordsNum += cords_num+DELIMITER;
				sendClistNum += clist_num+DELIMITER;
				sendTraNum += tra_num+DELIMITER;
				sendCompDati += comp_dati+DELIMITER;
				sendComp += comp+DELIMITER;
				
				// 2-2. 상태 업데이트 프로시저 수행 ( PROC_FLAG: N -> S )
				db.PsetString(1,cords_num); 
				db.PsetString(2,clist_num);
				db.PsetString(3,tra_num); 
				db.PsetInt(4,seq);
				if(db.PexecuteUpdate() < 0) {
					db.rollback();
					alertError("[SKT OFFICE]UPDATE SKT_IBGO_S ERROR");
					return;
				}
			}
			
			if("".equals(sendCordsNum)) {
				System.out.println("NO DATA :: SKT_IBGO_S ");
				log.Write(log_flag, PROG_NM, "NO DATA :: SKT_IBGO_S ");
				return;
			}
			
			// 3. Map 변수에 파라미터 저장
			sendData.put("order_no", sendCordsNum);
			sendData.put("order_unitno", sendClistNum);
			sendData.put("delivery_no", sendTraNum);
			sendData.put("ibgo_cnt", sendComp);
			sendData.put("ibgo_dt", sendCompDati);
			sendData.put("return_url", "http://www.happynarae.com/1024size/skt_inf/skt_ibgo_send_res.jsp");
			sendData.put("handle_no", handleNo);
			
			System.out.println("[SKT_IBGO_S] START HTTP POST");
			log.Write(log_flag, PROG_NM, "[SKT_IBGO_S] START HTTP POST");
			ifResult = sendDataToSKT(sendURL, sendData);
			System.out.println("[SKT_IBGO_S] HTTP POST RESULT :: " + ifResult + "\n");
			log.Write(log_flag, PROG_NM, "[SKT_IBGO_S] HTTP POST RESULT :: " + ifResult + "\n");
			
			if("S".equals(ifResult)) {
				// T-ODO: 커밋으로 바꾸기
				db.commit();
				//db.rollback();
			} else {
				db.rollback();
			}
			
		} catch (SQLException e) {
			System.out.println("Error"+e);
			log.Write(log_flag, PROG_NM, "Error"+e);
            try{
            	db.rollback();
            	alertError("[SKT OFFICE]SKT_IBGO_S ERROR :: DB UPDATE");
             }catch(Exception e1){}
		}
	}
	
	public static void sendRejtIbgo (String runMode) {
		String sendURL = runMode + "inf_orreturn_ipgo.asp";
		System.out.println("[SKT_REJT_IBGO_S] 연동 시작: " + sendURL);
		log.Write(log_flag, PROG_NM, "[SKT_REJT_IBGO_S] 연동 시작: " + sendURL);
		
		Map<String, String> sendData = new HashMap<String, String>();
		String sendCordsNum = "";
		String sendClistNum = "";
		String sendRejtDati = "";
		
		String handleNo = getHandleNo();
		String ifResult = "";
		
		try {
		
			// 1. 상태 업데이트 쿼리 수행 준비
			String update_qry = "UPDATE skt_ibgo_s " +
					"   SET proc_no = '"+handleNo+"', proc_flag = 'S', proc_dati = SYSDATE, sys_memo = '"+PROG_NM+"' " +
					" WHERE     enpri_cd = '000005' " +
					"       AND ords_ty = '002' " +
					"       AND cords_num = ? " +
					"       AND clist_num = ? " +
					"       AND tra_num = ? " +
					"       AND seq = ? " ;
			db.prepareStatement(update_qry);
		
			// 2. 보낼 데이터 SELECT 수행
			String select_qry = "SELECT cords_num, clist_num, tra_num, seq, rejt_dati " +
								"  FROM skt_ibgo_s " +
								" WHERE enpri_cd = '000005' AND ords_ty='002' AND proc_flag = 'N' "; 
			db.executeQuery(select_qry);
			
			while(db.rs.next()) {
				String cords_num = db.rs.getString("cords_num");
				String clist_num = db.rs.getString("clist_num");
				String tra_num = db.rs.getString("tra_num");
				int seq = db.rs.getInt("seq");
				String rejt_dati = db.rs.getString("rejt_dati");
				
				// 2-1. 보낼 파라미터 지정
				sendCordsNum += cords_num+DELIMITER;
				sendClistNum += clist_num+DELIMITER;
				sendRejtDati += rejt_dati+DELIMITER;
				
				// 2-2. 상태 업데이트 프로시저 수행 ( PROC_FLAG: N -> S )
				db.PsetString(1,cords_num); 
				db.PsetString(2,clist_num);
				db.PsetString(3,tra_num); 
				db.PsetInt(4,seq);
				if(db.PexecuteUpdate() < 0) {
					db.rollback();
					alertError("[SKT OFFICE]UPDATE SKT_IBGO_S ERROR (002)");
					return;
				}
			}
			
			if("".equals(sendCordsNum)) {
				System.out.println("NO DATA :: SKT_IBGO_S (002) ");
				log.Write(log_flag, PROG_NM, "NO DATA :: SKT_IBGO_S (002) ");
				return;
			}
			
			// 3. Map 변수에 파라미터 저장
			sendData.put("order_no", sendCordsNum);
			sendData.put("order_unitno", sendClistNum);
			sendData.put("returndt", sendRejtDati);
			sendData.put("return_url", "http://www.happynarae.com/1024size/skt_inf/skt_rejt_ibgo_send_res.jsp");
			sendData.put("handle_no", handleNo);
			
			System.out.println("[SKT_REJT_IBGO_S] START HTTP POST");
			log.Write(log_flag, PROG_NM, "[SKT_REJT_IBGO_S] START HTTP POST");
			ifResult = sendDataToSKT(sendURL, sendData);
			System.out.println("[SKT_REJT_IBGO_S] HTTP POST RESULT :: " + ifResult + "\n");
			log.Write(log_flag, PROG_NM, "[SKT_REJT_IBGO_S] HTTP POST RESULT :: " + ifResult + "\n");
			
			if("S".equals(ifResult)) {
				// T-ODO: 커밋으로 바꾸기
				db.commit();
				//db.rollback();
			} else {
				db.rollback();
			}
			
		} catch (SQLException e) {
			System.out.println("Error"+e);
			log.Write(log_flag, PROG_NM, "Error"+e);
            try{
            	db.rollback();
            	alertError("[SKT OFFICE]SKT_IBGO_S ERROR :: DB UPDATE (002)");
             }catch(Exception e1){}
		}
	}
	
	public static String sendDataToSKT(String sendURL, Map<String, String> sendData) {
		String ifResult = "E";
		
		// 1. URL 설정하고 접속하기
		try {
			// T-ODO: sendURL 로 변경
			URL url = new URL(sendURL);
			//URL url = new URL(localURL);
			
			HttpURLConnection http;
			http = (HttpURLConnection) url.openConnection(); // 접속 
			
            //-------------------------- 
            //   전송 모드 설정 - 기본적인 설정 
            //-------------------------- 
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정 
            http.setDoOutput(true); // 서버로 쓰기 모드 지정  
            http.setConnectTimeout(10000); // 접속 타임아웃 설정 (10초)
            http.setReadTimeout(10000);
            http.setRequestMethod("POST"); // 전송 방식은 POST
            
            //--------------------------
            // 헤더 세팅
            //--------------------------
            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다 
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            
            //-------------------------- 
            //   서버로 값 전송 
            //-------------------------- 
            StringBuffer buffer = new StringBuffer();

            //HashMap으로 전달받은 파라미터가 null이 아닌경우 버퍼에 넣어준다
            if (sendData != null) {
            	Iterator<String> iter = sendData.keySet().iterator();
        		
        		while(iter.hasNext()){
        			String key = iter.next();
        			buffer.append(key).append("=").append(sendData.get(key)).append("&");
        		}
            }

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "euc-kr");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            
            if( http.getResponseCode()== HttpURLConnection.HTTP_OK) {
            	ifResult = http.getHeaderField("result");
    	  	  	System.out.println("\t인터페이스 결과 ifResult = " + ifResult );
    	  	  	log.Write(log_flag, PROG_NM, "\t인터페이스 결과 ifResult = " + ifResult);
    	  	  	ifResult = "S";
            } 
            
            /*
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "euc-kr");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }
            ifResult = builder.toString();
	  	  	System.out.println("인터페이스 결과 ifResult = [" + ifResult +"]");
	  	  	*/
            
		} catch (MalformedURLException e) {
			ifResult = "E";
			System.out.println(" ERROR >>>>> URL openConnection " );
			alertError("[SKT OFFICE]URL openConnection ERROR");
			//e.printStackTrace();
		}  catch (SocketTimeoutException e) {
			ifResult = "E";
			System.out.println(" ERROR >>>>> Socket Timeout Exception" );
			alertError("[SKT OFFICE]SocketTimeoutException ERROR");
			//e.printStackTrace();
		} catch (IOException e) {
			ifResult = "E";
			System.out.println(" ERROR >>>>> URL sendDataToSKT" );
			alertError("[SKT OFFICE]URL sendDataToSKT ERROR");
			//e.printStackTrace();
		}
		
		return ifResult;
		
	}
	
	public static String getHandleNo() {
		String returnValue = "";
		
		String qry = "SELECT TO_CHAR(pg_call_seq.nextval) FROM DUAL";
		try {
			db.executeQuery(qry);
			db.rs.next(); 
			returnValue = db.rs.getString(1);
		}catch(SQLException e){
            System.out.println("Error"+e);
            try{
            	db.rollback();
             }catch(Exception e1){}
        }
		
		return returnValue;
	}
	
	public static void alertError(String msg) {
		System.out.println(" SKT OFFICE INF ERROR REPORT >>>>> ");
		try{
        	db.rollback();
         }catch(Exception e1){}
		
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
