import java.util.Calendar;

public class SendMailSms {

	static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
    //생성자

	public static void main(String[] args) {
		db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈

		String sql1="", sql2="", sql3="";
		String mailContent = "<html> <head> <title>[개인정보처리 위탁 계약] 필수 체결 안내문</title>\r\n" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html charset=UTF-8\"></head>\r\n" +
				"<STYLE type=\"text/css\">\r\n" +
				"a:link {  text-decoration:nonecolor: #766B50}\r\n" +
				"a:visited { text-decoration nonecolor: #766B50}\r\n" +
				"a:active {  text-decoration:underlinecolor: #968763}\r\n" +
				"a:hover { text-decoration:underlinecolor: #968763}\r\n" +
				"</style> \r\n" +
				"<body leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\r\n" +
				"<table width=\"750\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\r\n" +
				"<tr>\r\n" +
				"	<td><img src=\"http://www.happynarae.com/fullsize/public/alert_popup/imgs/mail_180608_top.JPG\" border=\"0\">\r\n" +
				"	</td>\r\n" +
				"</tr>\r\n" +
				"<tr>\r\n" +
				"	<td><a href=\"http://www.happynarae.com/\" target=\"_blank\" alt=\"행복나래전자계약사이트\"><img src=\"http://www.happynarae.com/fullsize/public/alert_popup/imgs/mail_180608_md1.JPG\" border=\"0\"></a><a href=\"http://www.happynarae.com/file_att/web_documents/전자계약_체결_Guide_정보처리_위탁_계약.pdf\" target=\"_blank\" alt=\"전자계약체결Guide정보처리위탁계약다운로드\"><img src=\"http://www.happynarae.com/fullsize/public/alert_popup/imgs/mail_180608_md2.JPG\" border=\"0\"></a>\r\n" +
				"	</td>\r\n" +
				"</tr>\r\n" +
				"<tr>\r\n" +
				"	<td><img src=\"http://www.happynarae.com/fullsize/public/alert_popup/imgs/mail_180608_bottom.JPG\" border=\"0\">\r\n" +
				"	</td>\r\n" +
				"</tr>\r\n" +
				"</table>\r\n" +
				"</body>\r\n" +
				"</html>";

		String smsContent = "[행복나래]\r\n개인정보처리 위탁 계약 체결을 위해 사이트 접속 후 계약체결 진행 부탁드립니다.";

		try{

        	db.CUST_ID = "00002345";
        	db.PROG_NM = "/user/home/mro/mro_demon/SendMailSms.java";
			db.DB_Conn();
			System.out.println("Db Connection !! ");

			/*
			sql1 = "SELECT SMAN_CD, P_GET.F_CUST( SMAN_CD, 'email') EMAIL, P_GET.F_CUST( SMAN_CD, 'mcom_num') MCOM, CONT_YN  " +
					"FROM ( " +
					"    SELECT SMAN_CD " +
					"        , (SELECT DECODE (COUNT (*), '0', 'N', 'Y')  " +
					"                FROM ECON_INFO EI, ECON_DTL ED  " +
					"               WHERE     EI.ECON_NUM = ED.ECON_NUM  " +
					"                     AND EI.PROC_TY = '009'  " +
					"                     AND CONT_TY = '001'  " +
					"                     AND ED.SCO_CD = A.SCO_CD   " +
					"                     AND ED.MEMB_CD = A.MEMB_CD)   " +
					"        || (SELECT DECODE (COUNT (*), '0', 'N', 'Y')  " +
					"                FROM ECON_INFO EI, ECON_DTL ED  " +
					"               WHERE     EI.ECON_NUM = ED.ECON_NUM  " +
					"                     AND EI.PROC_TY = '009'  " +
					"                     AND CONT_TY = '006'  " +
					"                     AND ED.SCO_CD = A.SCO_CD   " +
					"                     AND ED.MEMB_CD = A.MEMB_CD) CONT_YN                 " +
					"    FROM SCO_DTL A " +
					"    WHERE NOT EXISTS (SELECT 'x' FROM ENPRI_DTL_INFO X WHERE X.ENPRI_CD = A.SCO_CD AND X.BUSIP_SEQ = A.MEMB_CD AND X.GUBUN = 'SCO_STOP_PAYMENT')  " +
					//"    AND EXISTS ( SELECT 'x' FROM SCO_INFO X WHERE X.SCO_CD = A.SCO_CD AND NVL (X.SCO_TY, '002') = '002' )  " +
					"    AND STAT_FLAG IN ('001','007') " +
					") " +
					"WHERE CONT_YN LIKE '%N%'  UNION ALL select 'M0306965', P_GET.F_CUST( 'M0306965', 'email') EMAIL, P_GET.F_CUST( 'M0306965', 'mcom_num') MCOM, 'NN' CONT_YN  from dual ";

			*/
			sql1 = "SELECT SMAN_CD, P_GET.F_CUST( SMAN_CD, 'email') EMAIL, P_GET.F_CUST( SMAN_CD, 'mcom_num') MCOM, CONT_YN , to_char( sysdate, 'yyyymmdd') today , to_char(sysdate, 'd') dat " +
					"FROM (   " +
					"    SELECT SMAN_CD   " +
					"        , (SELECT DECODE (COUNT (*), '0', 'N', 'Y')    " +
					"                FROM ECON_INFO EI, ECON_DTL ED    " +
					"               WHERE     EI.ECON_NUM = ED.ECON_NUM    " +
					"                     AND EI.PROC_TY = '009'    " +
					"                     AND CONT_TY = '007'    " +
					"                     AND ED.SCO_CD = A.SCO_CD     " +
					"                     AND ED.MEMB_CD = A.MEMB_CD  " +
					"                     )              CONT_YN                   " +
					"    FROM SCO_DTL A   " +
					"    WHERE NOT EXISTS (SELECT 'x' FROM ENPRI_DTL_INFO X WHERE X.ENPRI_CD = A.SCO_CD AND X.BUSIP_SEQ = A.MEMB_CD AND X.GUBUN = 'SCO_STOP_PAYMENT')    " +
					"    AND STAT_FLAG IN ('001','007')  AND SCO_CD NOT IN ( '001268' ) " +
					")   " +
					"WHERE CONT_YN = 'N'   " +
					"UNION ALL select 'M0306965', P_GET.F_CUST( 'M0306965', 'email') EMAIL, P_GET.F_CUST( 'M0306965', 'mcom_num') MCOM, 'NNN' CONT_YN , to_char( sysdate, 'yyyymmdd') year, to_char(sysdate, 'd') dat from dual    " +
					"UNION ALL select 'M0705788', P_GET.F_CUST( 'M0705788', 'email') EMAIL, P_GET.F_CUST( 'M0705788', 'mcom_num') MCOM, 'NNN' CONT_YN , to_char( sysdate, 'yyyymmdd') year, to_char(sysdate, 'd') dat from dual  " ;




			//sql1 = "select 'M0306965', P_GET.F_CUST( 'M0306965', 'email') EMAIL, P_GET.F_CUST( 'M0306965', 'mcom_num') MCOM, 'NN' CONT_YN  from dual";

			//sql1 = sql1 + " AND ROWNUM < 2 ";

			db.executeQuery(sql1);

			sql2 = "INSERT INTO SEND_MAIL_SPEED ( MAIL_NUM, MAIL_SEQ, TO_EMAIL, FROM_EMAIL, FROM_NAME, SUBJECT, CONTENTS, MAIL_KIND, MAIL_TYPE, SE_RE_DIV, ORIGIN_NUM, TO_CUST_ID, FR_CUST_ID, REG_DATE, PROC_DATE, STAT_FLAG, SMS_FLAG, RECEIVE_FLAG, READ_DATE, ORIGIN_SEQ, LINK, CONTENTS_CLOB, LINK_URL, CC_TO_EMAIL, MAIL_INFO ) " +
				   "VALUES ( Lpad(seq_send_mail_speed.NEXTVAL,10,'0'), 1, ?, 'shkang11@sk.com', '강세훈', '[행복나래] 협력사 전자계약 체결 요청', '..', 'MERP', 'WMS', 'Y', 1, '00000001', 'MERP', SYSDATE, NULL, '001', 'N', 0, NULL, 1, NULL, ?, NULL, '', 'MAIL_SEND' ) ";

			sql3 = "INSERT INTO em_tran ( TRAN_PR, TRAN_REFKEY, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,      TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3 ) " +
				   "VALUES ( EM_TRAN_PR.NEXTVAL, NULL, ?, '02-2104-4960', 1, Sysdate , ?,  NULL, NULL,NULL) ";

			Calendar cal = Calendar.getInstance();

			int dayNum = cal.get( Calendar.DAY_OF_WEEK);

			String sttYear = String.valueOf( cal.get( Calendar.YEAR ) );
			String sttMonth = String.format("%02d", (cal.get( Calendar.MONTH  ) + 1 ) )  ;
			String sttdate = String.format("%02d", (cal.get( Calendar.DATE  ) ) );

			//String toDay = sttYear + sttMonth + sttdate;

			//System.out.println(dayNum + " ==> "+ toDay);

			while(db.rs.next()) {
				String email = db.rs.getString("EMAIL");
				String mcom = db.rs.getString("MCOM");
				String today = db.rs.getString("TODAY");
				int dat = db.rs.getInt("DAT");

				if( ( Integer.parseInt( today ) >= 20180614 ) &&  ( Integer.parseInt( today ) <= 20180629 ) ) {
					if( dat == 1 || dat == 7 || "20180613".equals(today) ) {

					}else {
						db.prepareStatement(sql2);

						db.PsetString(1, email);
						db.PsetString(2, mailContent);

						System.out.println( "Email --> " + db.PexecuteUpdate() );

						if( db.ps != null ) db.ps.close();

						if( "20180614".equals(today) || "20180620".equals(today) ) {
							System.out.println("Send SMS");

							db.prepareStatement(sql3);

							db.PsetString(1, mcom);
							db.PsetString(2, smsContent);

							System.out.println( "SMS --> " +  db.PexecuteUpdate() );
						}

						if( db.ps != null ) db.ps.close();
					}
				}else {
					System.out.println("NOT NOT NOT ");
				}
			}

			db.commit();
			System.out.println("End");

        }catch(Exception e){
            System.out.println("Error"+e);
            try{
                System.out.println(e);
                 db.rollback();
             }catch(Exception e1){}
        }finally{
        	db.DB_DisConn();
			System.out.println("Db Dis_Conn !! ");
        }

	}

}
