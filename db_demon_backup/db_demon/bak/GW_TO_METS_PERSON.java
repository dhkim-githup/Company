import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;
import comm.mk_log;

/**
 *
 */

/**
 * @author SAF03110005
 *
 */
public class GW_TO_METS {
	static private mk_log log = new mk_log();

	static String DB_URL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";	//운영
	static String USER="METS18940G";
	static String PASS="METS25519P58563W";


//	static String DB_URL="jdbc:oracle:thin:@172.16.1.224:1521:testdb";	//테스트
//	static String USER="METS_IMSI";
//	static String PASS="METS_IMSI";


	static String DB_URL_GW = "jdbc:mysql://172.16.3.10:3306/naongw"; //운영
//	static String DB_URL_GW = "jdbc:mysql://172.16.3.14:3306/naongw";	//테스트
	static String USER_GW = "ekpnaon_happy";
	static String PASS_GW = "skdhs!1";

	static String DB_URL_EACC = "jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	static String USER_EACC = "EACCO";
	static String PASS_EACC = "wjswkwjsVY1*";

	public static void main(String[] args){
		System.out.println("Start....");

		//GW 인사 정보 METS 특정 테이블 INSERT
		System.out.println( "Step 1 ");
		HashMap mp = getGwData();
		setMetsData( mp );

		//GW 인사 정보 METS 특정 테이블 INSERT - 팀장 정보만
		System.out.println( "Step 2 ");
		List list = getTeamJang();
		setTeamJang( list );

		//GW => METS.CUST_DTL INS, UPD 신규 사용자, 부서변경자에 한함
		System.out.println( "Step 3 ");
		List list2 = getListEmployee();
		setSyncMetsDB( list2 );

		//GW => METS.CUST_DTL 퇴사자 정보 전송 STAT_FLAG 003 UPD
		System.out.println( "Step 4 ");
		List list3 = getListRetireEmployee();
		setSyncMetsDB2( list3 );
		setSyncMetsDB5( list3 );


		//METS 접속권한, MERP 접속권한, 사무용품 구매 접속권한, SK 카드 사용권한 동기화
		System.out.println( "Step 5 ");
		List list4 = getListEmployee();
		setSyncMetsDB3( list4 );

		//Speedmall 운영관련 임직원 전환
		System.out.println( "Step 6 ");
		List list5 = getListEmployee();
		setSyncMetsDB4( list5 );

		//Speedmall 운영관련 개인고객 전환
		System.out.println( "Step 7 ");
		List list6 = getListEmployee();
		//setSyncMetsDB5( list6 );

		//GW정보 -> 전자전표 시스템
		List list7 = getListEmployee2();
		setSyncEACCDB( list7 );
		setSyncEACCDB2( list3 );

		// 휴일 가져오기
		//List holidayLst = getHoliday();
		//setMariaHoliday(holidayLst);

		//월 생일자 입력
		List list8 = getBirthEmpList();
		setBirthDay(list8);

		//그룹웨어 결재 룰 세팅 준비
		setRulSetting();

		log.Write( "Program Name : GW_TO_METS" , "------- END -------");
		System.out.println("Completed....");
	}

	private static void setSyncEACCDB2(List list3) {
		String strQry = "";

		Connection conn = null;

		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null;
		ResultSet rs1 = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL_EACC, USER_EACC, PASS_EACC);
			log.Write( "Program Name : GW_TO_EACC" , "A) Oracle DB Connection success... setSyncEACCDB ");
			conn.setAutoCommit(false);

			String delQry1 = " DELETE FROM MEMBER_GW WHERE SABUN = ?  ";
			String delQry2 = " DELETE FROM MEMBER_SAP WHERE SABUN = ?  ";
			String delQry3 = " DELETE FROM MEMBER_INFO WHERE SABUN = ?  ";

			HashMap<String, String> getHp = new HashMap();

			for( int i=0; i<list3.size(); i++){
				pstmt1 = conn.prepareStatement(delQry1);

				getHp = (HashMap) list3.get(i);

				String empCode = getHp.get("EMP_CODE");

				pstmt1.setString(1, empCode);
				pstmt1.executeUpdate();

				pstmt2 = conn.prepareStatement(delQry2);
				pstmt2.setString(1, empCode);
				pstmt2.executeUpdate();

				pstmt3 = conn.prepareStatement(delQry3);
				pstmt3.setString(1, empCode);
				pstmt3.executeUpdate();

				if( pstmt1 != null) pstmt1.close();
				if( pstmt2 != null) pstmt2.close();
				if( pstmt3 != null) pstmt3.close();
			} // end for

			conn.commit();

		}catch(Exception e){

			e.printStackTrace();

			log.Write( "setSyncEACCDB22 - ", e.getMessage());

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt1 != null ) pstmt1.close();
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt3 != null ) pstmt3.close();
				if( pstmt1 != null ) pstmt1.close();
				if( rs1 != null ) rs1.close();
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void setRulSetting() {
		Connection connMets = null;
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null;
		ResultSet rs1 = null, rs2 = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			connMets = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , " Oracle DB Connection success...");
			connMets.setAutoCommit(false);

			String sql = " SELECT A.EMP_ID, A.DEPT_ID, B.EMP_CODE, C.OFC_ID, C.OFC_NAME, C.OFC_ORDER, B.EMP_NAME "+
						" FROM GWIF_MY_JOB A, GWIF_EMPLOYEE B, GWIF_OFFICE C "+
						" WHERE 1=1  "+
						//" AND a.emp_id = 'M083620476'  "+
						" AND A.DEFAULT_YN = 'Y' "+
						" AND A.USE_YN = 'Y' "+
						" AND A.EMP_ID = B.EMP_ID "+
						" AND B.EMP_STATUS = 'W' "+
						" AND A.OFC_ID = C.OFC_ID "
						//+" AND ROWNUM < 3 "
						;

			pstmt1 = connMets.prepareStatement(sql);

			rs1 = pstmt1.executeQuery();

			Map<String,List<String>> mp = new HashMap<String,List<String>>();

			while( rs1.next() ) {
				String empId = rs1.getString("EMP_ID");
				String deptId = rs1.getString("DEPT_ID");

				System.out.println(empId + "\t " + deptId);

				String sql2 = " SELECT A.DEPT_ID							"+
						"	, A.DEPT_NAME							"+
						"	, A.UP_DEPT_IDX							"+
						"	, A.LV								"+
						"	, C.EMP_NAME							"+
						"	, C.EMP_ID							"+
						" FROM (								"+
						" SELECT DEPT_ID , DEPT_NAME, UP_DEPT_IDX, LEVEL LV			"+
						" FROM GWIF_DEPARTMENT							"+
						" WHERE DEPT_STATUS = 'U'						"+
						" AND DEPT_ID NOT IN ( 'C300151214','ROOT')				"+
						" START WITH DEPT_ID = ?					"+
						" CONNECT BY DEPT_ID = PRIOR UP_DEPT_IDX				"+
						" ) A, GWIF_MY_JOB B, GWIF_EMPLOYEE C,  GWIF_OFFICE D			"+
						" WHERE 1=1								"+
						" AND B.DEFAULT_YN = 'Y'						"+
						" AND B.USE_YN = 'Y'							"+
						" AND B.EMP_ID = C.EMP_ID						"+
						" AND C.EMP_STATUS = 'W'						"+
						" AND B.OFC_ID = D.OFC_ID						"+
						" AND A.DEPT_ID = B.DEPT_ID						"+
						" AND D.OFC_NAME IN ('대표이사','사업부장','실장','담당','센터장','팀장')	 "+
						" AND C.EMP_ID <> ?						";

				pstmt2 = connMets.prepareStatement(sql2);
				pstmt2.setString(1,deptId);
				pstmt2.setString(2,empId);

				rs2 = pstmt2.executeQuery();

				List<String> appArr = new ArrayList<String>();

				while( rs2.next() ) {
					appArr.add(rs2.getString("EMP_ID"));
				}

				mp.put(empId, appArr);

				System.out.println(mp);

				if( rs2 !=null ) rs2.close();
				if( pstmt2 !=null ) pstmt2.close();
			}

			insertEappLineRule(mp);


		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if( rs1 !=null ) rs1.close();
				if( rs2 !=null ) rs2.close();
				if( pstmt1 !=null ) pstmt1.close();
				if( pstmt2 !=null ) pstmt2.close();
				if( connMets !=null ) connMets.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void insertEappLineRule(Map<String, List<String>> mp) {
		Connection connGw = null;
		PreparedStatement pstmt1 = null, pstmt2 = null;

		try{
			Class.forName("com.mysql.jdbc.Driver");

			connGw = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			System.out.println("Maria DB Connection success...");
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...  ");

			connGw.setAutoCommit(false);

			pstmt1 = connGw.prepareStatement("DELETE FROM EAPP_LINE_RULE ");
			pstmt1.executeUpdate();

			Set set = mp.keySet();

			String sql3 = " INSERT INTO EAPP_LINE_RULE							"+
					" ( EMP_ID, NXT_ID1, NXT_ID2, NXT_ID3, NXT_ID4, NXT_ID5, NXT_ID6, REG_DATE )    "+
					" VALUES ( ?, ?, ?, ?, ?, ?, ?, NOW() )  				        ";

			pstmt2 = connGw.prepareStatement(sql3);

			for(Iterator it = set.iterator(); it.hasNext();) {
				String keyNm = (String) it.next();
				List lst =  mp.get(keyNm);

				//System.out.println("keyNm -->" + keyNm + "\t" + lst.size());
				pstmt2.setString(1, keyNm);

				for(int i=0; i<6;i++) {// 컬럼값이 6개라서 NXT_ID1 ~ 6

					try {
						//System.out.println( lst.get(i));
						pstmt2.setString( (i+2), (String) lst.get(i) );
					}catch(IndexOutOfBoundsException e) {
						//System.out.println("##");
						pstmt2.setString( (i+2), null );
					}
				}

				pstmt2.executeUpdate();
			}

			connGw.commit();

		}catch(Exception e) {

			try {
				connGw.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();

		}finally {
			try {
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt1 != null ) pstmt1.close();
				if( connGw != null ) connGw.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void setBirthDay(List list8) {
		Calendar cal = Calendar.getInstance();

		String sttYear = String.valueOf( cal.get( Calendar.YEAR ) );
		String sttMonthDay = String.format("%02d", (cal.get( Calendar.MONTH  ) + 1 ) ) + "01";
		String endMonthDay = String.format("%02d", (cal.get( Calendar.MONTH  ) + 1 ) ) + String.valueOf( cal.getActualMaximum(Calendar.DAY_OF_MONTH) );

		String cur = String.valueOf( System.currentTimeMillis() );

		if( cur.length() > 28 ) cur = cur.substring(1, 28);

		String schId = ("SCH"+ cur ) ;
		System.out.println(" schId " + schId );

		Connection conn = null;
		PreparedStatement pstmt1 = null, pstmt2 = null;

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			System.out.println("Maria DB Connection success...");
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...  ");

			conn.setAutoCommit(false);
			StringBuffer sb = new StringBuffer();

			sb.append("DELETE FROM SCD_SCHEDULE  ").append("\n");
			sb.append("WHERE CALENDAR_ID = 'SCD29033795796612958578'	").append("\n");
			sb.append("AND BGN_DT >= STR_TO_DATE(?, '%Y%m%d')	").append("\n");
			sb.append("AND END_DT <= DATE_ADD( STR_TO_DATE(?, '%Y%m%d'), INTERVAL 1 DAY )  ").append("\n");

			pstmt2 = conn.prepareStatement(sb.toString());
			pstmt2.setString(1, sttYear+sttMonthDay);
			pstmt2.setString(2, sttYear+endMonthDay);

			pstmt2.executeUpdate();

			HashMap<String, String> getHp = new HashMap();

			String sql1 = " INSERT INTO SCD_SCHEDULE values (?,'SCD29033795796612958578',?,NULL,str_to_date(?, '%Y%m%d %H%i%s'), date_add( str_to_date(?, '%Y%m%d %H%i%s') , INTERVAL 1 DAY ),'Y', NULL,'prog','Y', NULL, '','','confirmed','',NULL, NOW(),'','100000145',?, ?, 'SCD',?, NULL,'','','','','N','N',NOW(), NOW(), NULL,NULL ) ";

			pstmt1 = conn.prepareStatement(sql1);

			ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null;

			String empName = "", deptName = "", empId = "", photo = "", birth = "", posName = "", mobile = "";

			for( int i=0; i<list8.size(); i++){
				getHp = (HashMap) list8.get(i);

				empName = getHp.get("EMP_NAME");
				deptName = getHp.get("DEPT_NAME");
				empId = getHp.get("EMP_ID");
				photo = getHp.get("PHOTO");
				birth = getHp.get("BIRTH");
				posName = getHp.get("POS_NAME");
				mobile = getHp.get("MOBILE");

				pstmt1.setString(1, schId + String.format("%03d", i));
				pstmt1.setString(2, "["+deptName+"] " + empName + " " + posName + " 생일");
				pstmt1.setString(3, sttYear + birth );
				pstmt1.setString(4, sttYear + birth );
				pstmt1.setString(5, empId );
				pstmt1.setString(6, empId );
				pstmt1.setString(7, schId + String.format("%03d", i));

				pstmt1.executeUpdate();
			}

			conn.commit();

		}catch(Exception e) {

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();

		}finally {
			try {
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt1 != null ) pstmt1.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static List getBirthEmpList() {
		List list = new ArrayList();

		Calendar cal = Calendar.getInstance();

		String sttYear = String.valueOf( cal.get( Calendar.YEAR ) );
		String sttMonthDay = String.format("%02d", (cal.get( Calendar.MONTH  ) + 1 ) ) + "01";
		String endMonthDay = String.format("%02d", (cal.get( Calendar.MONTH  ) + 1 ) ) + String.valueOf( cal.getActualMaximum(Calendar.DAY_OF_MONTH) );

		Connection conn = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;

		HashMap tableMap = new HashMap();

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			System.out.println("Maria DB Connection success...");
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...  ");

			StringBuffer sb = new StringBuffer();

			sb.append("SELECT           ").append("\n");
			sb.append("        E.EMP_ID, E.EMP_NAME, D.DEPT_NAME, PS.MOBILE, CONCAT('http://gw.happynarae.co.kr/ekp/upload/body',  REPLACE( PS.PHOTO, '//', '/' ) ) PHOTO           ").append("\n");
			sb.append("        , SOLAR_YN, BIRTH, POS_NAME          ").append("\n");
			sb.append("    FROM           ").append("\n");
			sb.append("        MY_JOB MJ                     ").append("\n");
			sb.append("    INNER JOIN           ").append("\n");
			sb.append("        EMPLOYEE E                  ").append("\n");
			sb.append("            ON MJ.EMP_ID = E.EMP_ID            ").append("\n");
			sb.append("            AND E.CMP_ID =  'C300151214'            ").append("\n");
			sb.append("            AND E.EMP_STATUS = 'W'            ").append("\n");
			sb.append("            AND MJ.DEFAULT_YN = 'Y'            ").append("\n");
			sb.append("            AND USE_YN = 'Y'                  ").append("\n");
			sb.append("    INNER JOIN           ").append("\n");
			sb.append("        DEPARTMENT D                  ").append("\n");
			sb.append("            ON MJ.DEPT_ID = D.DEPT_ID            ").append("\n");
			sb.append("            AND D.DEPT_STATUS = 'U'                  ").append("\n");
			sb.append("    LEFT OUTER JOIN           ").append("\n");
			sb.append("        DEPARTMENT UPD                  ").append("\n");
			sb.append("            ON D.UP_DEPT_IDX = UPD.DEPT_ID            ").append("\n");
			sb.append("            AND UPD.UP_DEPT_IDX != '-1'                  ").append("\n");
			sb.append("    INNER JOIN           ").append("\n");
			sb.append("        POSITION P                  ").append("\n");
			sb.append("            ON E.POS_ID = P.POS_ID                  ").append("\n");
			sb.append("    INNER JOIN           ").append("\n");
			sb.append("        (           ").append("\n");
			sb.append("            SELECT           ").append("\n");
			sb.append("                BB.BIRTH,           ").append("\n");
			sb.append("                BB.PHOTO,           ").append("\n");
			sb.append("                BB.PSN_SITE_IMG,           ").append("\n");
			sb.append("                BB.PSN_ID,           ").append("\n");
			sb.append("                BB.SOLAR_YN,           ").append("\n");
			sb.append("                MOBILE                 ").append("\n");
			sb.append("            FROM           ").append("\n");
			sb.append("                (       SELECT           ").append("\n");
			sb.append("                    AA.BIRTH,           ").append("\n");
			sb.append("                    AA.PHOTO,           ").append("\n");
			sb.append("                    AA.PSN_SITE_IMG,           ").append("\n");
			sb.append("                    AA.PSN_ID,           ").append("\n");
			sb.append("                    AA.SOLAR_YN, MOBILE                  ").append("\n");
			sb.append("                FROM           ").append("\n");
			sb.append("                    (         SELECT           ").append("\n");
			sb.append("                        SUBSTRING(REPLACE(BIRTH, '-', ''), 5, 4) BIRTH,           ").append("\n");
			sb.append("                        PHOTO,           ").append("\n");
			sb.append("                        PSN_SITE_IMG,           ").append("\n");
			sb.append("                        PSN_ID,           ").append("\n");
			sb.append("                        SOLAR_YN, MOBILE                      ").append("\n");
			sb.append("                    FROM           ").append("\n");
			sb.append("                        PERSON                     ").append("\n");
			sb.append("                    WHERE           ").append("\n");
			sb.append("                        SOLAR_YN = 'S'                       ").append("\n");
			sb.append("                        AND BIRTH IS NOT NULL       ) AA      )BB                ").append("\n");
			sb.append("                WHERE           ").append("\n");
			sb.append("                    BB.BIRTH BETWEEN SUBSTRING( ?, 5, 4 ) AND SUBSTRING(  ? , 5, 4 )             ").append("\n");
			sb.append("            UNION                   ").append("\n");
			sb.append("             SELECT           ").append("\n");
			sb.append("                    SUBSTRING(REPLACE(B.SOLAR_DATE, '-', ''), 5, 4) BIRTH,           ").append("\n");
			sb.append("                    A.PHOTO,           ").append("\n");
			sb.append("                    A.PSN_SITE_IMG,           ").append("\n");
			sb.append("                    A.PSN_ID,           ").append("\n");
			sb.append("                    A.SOLAR_YN, A.MOBILE          ").append("\n");
			sb.append("                FROM           ").append("\n");
			sb.append("                    PERSON  A, (  SELECT           ").append("\n");
			sb.append("                        SUBSTRING(LUNAR_DATE, 5, 4)            ").append("\n");
			sb.append("                        , LUNAR_DATE          ").append("\n");
			sb.append("                        , SOLAR_DATE          ").append("\n");
			sb.append("                    FROM           ").append("\n");
			sb.append("                        LUNAR_DATE            ").append("\n");
			sb.append("                    WHERE           ").append("\n");
			sb.append("                        SOLAR_DATE BETWEEN   ? AND    ? ) B               ").append("\n");
			sb.append("                WHERE           ").append("\n");
			sb.append("                    A.SOLAR_YN = 'L'                         ").append("\n");
			sb.append("                    AND A.BIRTH IS NOT NULL              ").append("\n");
			sb.append("                    AND SUBSTRING(REPLACE(A.BIRTH, '-', ''), 5, 4 ) = SUBSTRING(B.LUNAR_DATE, 5, 4)                   ").append("\n");
			sb.append("                              ").append("\n");
			sb.append("                   ) PS                  ").append("\n");
			sb.append("                        ON MJ.PSN_ID = PS.PSN_ID                                     ").append("\n");
			sb.append("                ORDER BY           ").append("\n");
			sb.append("                    PS.BIRTH             ASC              ").append("\n");

			String qry = sb.toString();

			pstmt1 = conn.prepareStatement(qry);
			pstmt1.setString(1, sttYear+sttMonthDay);
			pstmt1.setString(2, sttYear+endMonthDay);
			pstmt1.setString(3, sttYear+sttMonthDay);
			pstmt1.setString(4, sttYear+endMonthDay);

			rs1 = pstmt1.executeQuery();

			while( rs1.next() ){
				HashMap<String, String> mp = new HashMap();

				mp.put("EMP_ID" , rs1.getString("EMP_ID"));
				mp.put("EMP_NAME" , rs1.getString("EMP_NAME"));
				mp.put("DEPT_NAME" , rs1.getString("DEPT_NAME"));
				mp.put("MOBILE" , rs1.getString("MOBILE"));
				mp.put("PHOTO" , rs1.getString("PHOTO"));
				mp.put("BIRTH" , rs1.getString("BIRTH"));
				mp.put("POS_NAME" , rs1.getString("POS_NAME"));

				list.add(mp);
			}
		}catch(Exception e){

		}finally{
			try {
				if( pstmt1 !=null ) pstmt1.close();
				if( rs1 !=null ) rs1.close();
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	private static void setMariaHoliday(List holidayLst) {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		Calendar cal = Calendar.getInstance();
		int yyyy = cal.get(Calendar.YEAR);

		String dateName = "", locdate = "", isHoliday = "";

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...");
			conn.setAutoCommit(false);

			String sql1 = " DELETE FROM HOLIDAY WHERE YYYY = ?  ";

			pstmt1 = conn.prepareStatement(sql1);

			pstmt1.setString(1, String.valueOf(yyyy) );
			pstmt1.executeUpdate();

			HashMap<String, String> getHp = new HashMap();

			String sql2 = " INSERT INTO HOLIDAY (HLD_DATE, HLD_NM, YYYY, REG_DATI, MEMO ) VALUES (?, ?, ?, NOW(), NULL ) ";

			pstmt2 = conn.prepareStatement(sql2);

			for( int i=0; i<holidayLst.size(); i++){
				getHp = (HashMap) holidayLst.get(i);

				dateName = getHp.get("dateName");
				locdate = getHp.get("locdate");
				isHoliday = getHp.get("isHoliday");

				if("Y".equals(isHoliday)){
					int pIdx = 1;

					pstmt2.setString(pIdx++, locdate);
					pstmt2.setString(pIdx++, dateName);
					pstmt2.setString(pIdx++, String.valueOf(yyyy));
				}

				pstmt2.executeUpdate();

			} // end for

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}finally{

			try {
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt1 != null ) pstmt1.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static List getHoliday() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		List lst = new ArrayList();

		try {
			for( int mm=1;mm<=12;mm++ ){
				String strMM = String.format("%02d", mm);

				String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo?serviceKey=dLFG92acUkd1mXJ2ZphOy%2B6ymf0kpSAXEo%2BN8s0Lcr4hd0akf45aa7V8A7x4ip0PR5MB6nG%2FXd2ckN%2BVHADMEg%3D%3D&solYear="+year+"&solMonth="+ strMM;

				DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			    f.setNamespaceAware(false);
			    f.setValidating(false);
			    DocumentBuilder b = f.newDocumentBuilder();
			    URLConnection urlConnection = new URL(url).openConnection();
			    urlConnection.addRequestProperty("Accept", "application/xml");
			    Document doc = b.parse(urlConnection.getInputStream());
			    doc.getDocumentElement().normalize();
			    //System.out.println ("Root element: " +  doc.getDocumentElement().getNodeName());

			    NodeList itemNodes = doc.getElementsByTagName("item");

			    for( int i=0; i<itemNodes.getLength(); i++ ){
			    	Map hm = new HashMap<String, String>();

			    	for(Node node = itemNodes.item(i).getFirstChild(); node != null ; node = node.getNextSibling() ){
			    		if( "dateName".equals(node.getNodeName()) ){
			    			hm.put("dateName", node.getTextContent() );
			    		}else if( "isHoliday".equals(node.getNodeName()) ){
			    			hm.put("isHoliday", node.getTextContent() );
			    		}else if( "locdate".equals(node.getNodeName()) ){
			    			hm.put("locdate", node.getTextContent() );
			    		}
			    	}

			    	lst.add(hm);
			    }
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lst;
	}

	private static void setSyncEACCDB(List list7) {
		String strQry = "";

		Connection conn = null;

		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null;
		ResultSet rs1 = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL_EACC, USER_EACC, PASS_EACC);
			log.Write( "Program Name : GW_TO_EACC" , "A) Oracle DB Connection success... setSyncEACCDB ");
			conn.setAutoCommit(false);

			String sql1 = " SELECT COUNT(*) FROM MEMBER_GW WHERE SABUN = ?  ";

			String insQry1 = "INSERT INTO MEMBER_GW ( NAME, SABUN, GWUSERID, DEPTNAME, JOBTITLE, DEPTCODE, JOBLEVEL, DATE_REGIST, DATE_EDIT, OFC_ID ) VALUES (?,?,?,?,?,?,?,SYSDATE,SYSDATE,?) ";
			String insQry2 = "INSERT INTO MEMBER_SAP ( NAME, SABUN, KOSTL, DATE_REGIST ) VALUES ( ?, ?, ?, SYSDATE ) ";
			String insQry3 = "INSERT INTO MEMBER_INFO ( SABUN, CD_USERTYPE, CD_STATUS, DATE_REGIST, DATE_EDIT ) VALUES ( ?, 0, 0, SYSDATE , SYSDATE ) ";

			String updQry1 = "UPDATE MEMBER_GW set NAME=?, GWUSERID=?, DEPTNAME=?, JOBTITLE=?, DEPTCODE=?, JOBLEVEL=?, OFC_ID=? WHERE SABUN=?";
			String updQry2 = "UPDATE MEMBER_SAP set NAME=?, KOSTL=? WHERE SABUN=?";

			HashMap<String, String> getHp = new HashMap();

			for( int i=0; i<list7.size(); i++){
				pstmt1 = conn.prepareStatement(sql1);

				getHp = (HashMap) list7.get(i);

				String empCode = getHp.get("EMP_CODE");
				String empNm = getHp.get("EMP_NAME");
				String posName = getHp.get("POS_NAME");
				String deptId = getHp.get("DEPT_ID");
				String uppDeptId = getHp.get("UPP_DEPT_ID");
				String ofcId = getHp.get("OFC_ID");
				String costC = getHp.get("COSTC");
				String userId = getHp.get("USER_ID");
				String deptNm = getHp.get("DEPT_NAME");

				int extCnt = 0;
				pstmt1.setString(1, empCode);

				rs1 = pstmt1.executeQuery();
				if( rs1.next() ) extCnt = rs1.getInt(1);

				System.out.println( empCode + " >> " + extCnt );

				if( extCnt == 0 ) {
					pstmt2 = conn.prepareStatement(insQry1);

					int pSeq = 1;
					pstmt2.setString(pSeq++, empNm);
					pstmt2.setString(pSeq++, empCode);
					pstmt2.setString(pSeq++, userId);
					pstmt2.setString(pSeq++, deptNm);
					pstmt2.setString(pSeq++, posName);
					pstmt2.setString(pSeq++, deptId);
					pstmt2.setString(pSeq++, uppDeptId);
					pstmt2.setString(pSeq++, ofcId);

					if( pstmt2.executeUpdate() > 0 ) {
						if( pstmt2 != null ) pstmt2.close();

						pstmt2 = conn.prepareStatement(insQry2);

						int pSeq2 = 1;
						pstmt2.setString(pSeq2++, empNm);
						pstmt2.setString(pSeq2++, empCode);
						pstmt2.setString(pSeq2++, costC);

						if( pstmt2.executeUpdate() > 0 ) {
							if( pstmt2 != null ) pstmt2.close();

							pstmt2 = conn.prepareStatement(insQry3);

							pstmt2.setString(1, empCode);

							if( pstmt2.executeUpdate() > 0 ) {

							}else {
							}
						}else {
						}
					}else {
					}

				}else if( extCnt == 1 ) {
					pstmt3 = conn.prepareStatement(updQry1);

					int pSeq2 = 1;
					pstmt3.setString(pSeq2++, empNm);
					pstmt3.setString(pSeq2++, userId);
					pstmt3.setString(pSeq2++, deptNm);
					pstmt3.setString(pSeq2++, posName);
					pstmt3.setString(pSeq2++, deptId);
					pstmt3.setString(pSeq2++, uppDeptId);
					pstmt3.setString(pSeq2++, ofcId);
					pstmt3.setString(pSeq2++, empCode);

					if( pstmt3.executeUpdate() > 0 ) {
						if( pstmt3 != null ) pstmt3.close();

						pstmt3 = conn.prepareStatement(updQry2);

						pstmt3.setString(1, empNm);
						pstmt3.setString(2, costC);
						pstmt3.setString(3, empCode);

						pstmt3.executeUpdate();
					}else {

					}
				}
				if( pstmt1 != null ) pstmt1.close();
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt3 != null ) pstmt3.close();
				if( rs1 != null ) rs1.close();
			} // end for

			conn.commit();

		}catch(Exception e){

			e.printStackTrace();

			log.Write( "setSyncEACCDB - ", e.getMessage());

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt1 != null ) pstmt1.close();
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt3 != null ) pstmt3.close();
				if( pstmt1 != null ) pstmt1.close();
				if( rs1 != null ) rs1.close();
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static List getListEmployee2() {
		List list = new ArrayList();

		Connection conn = null;
		Statement stmt1 = null;
		ResultSet rs1 = null;

		HashMap tableMap = new HashMap();

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			System.out.println("Maria DB Connection success...");
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...  ");

			stmt1 = conn.createStatement();
			StringBuffer sb = new StringBuffer();

			sb.append(" select empCode, empId, empName, posName 						          ").append("\n");
			sb.append("    , deptId 													          ").append("\n");
			sb.append("    ,  case  													          ").append("\n");
			sb.append("         when substr( substr( reverse( replace( replace( deptLoc, 'C300151214/', ''), deptId, '' )  ), 2), 1, instr( substr( reverse( replace( replace( deptLoc, 'C300151214/', ''), deptId, '' )  ), 2), '/') ) = ''           ").append("\n");
			sb.append("          then reverse( substr( reverse( replace( replace( deptLoc, 'C300151214/', ''), deptId, '' )  ), 2 ) )  	          ").append("\n");
			sb.append("         else  substr( reverse( substr( substr( reverse( replace( replace( deptLoc, 'C300151214/', ''), deptId, '' )  ), 2), 1, instr( substr( reverse( replace( replace( deptLoc, 'C300151214/', ''), deptId, '' )  ), 2), '/') ) ), 2 )            ").append("\n");
			sb.append("       end uppDeptId 	          ").append("\n");
			sb.append("    , ofcId             ").append("\n");
			sb.append("    , ( select x.dept_cd from department x where x.dept_id =  a.deptId ) costc	").append("\n");
			sb.append("    , userId, deptName															").append("\n");
			sb.append(" from v_employee_info3 	 a		    ").append("\n");
			sb.append(" where deptId NOT IN ( 'D993055064', 'D993055059' )").append("\n");
			sb.append(" order by 1	").append("\n");

			String qry = sb.toString();

			rs1 = stmt1.executeQuery(qry);

			while( rs1.next() ){
				HashMap<String, String> mp = new HashMap();

				mp.put("EMP_CODE" , rs1.getString("empCode"));
				mp.put("EMP_ID" , rs1.getString("empId"));
				mp.put("EMP_NAME" , rs1.getString("empName"));
				mp.put("POS_NAME" , rs1.getString("posName"));
				mp.put("DEPT_ID", rs1.getString("deptId"));
				mp.put("UPP_DEPT_ID", rs1.getString("uppDeptId"));
				mp.put("OFC_ID", rs1.getString("ofcId"));
				mp.put("COSTC", rs1.getString("costc"));
				mp.put("USER_ID", rs1.getString("userId"));
				mp.put("DEPT_NAME", rs1.getString("deptName"));

				list.add(mp);
			}
		}catch(Exception e){

		}finally{
			try {
				if( stmt1 !=null ) stmt1.close();
				if( rs1 !=null ) rs1.close();
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	private static void setSyncMetsDB5(List list6) {
		String strQry = "";

		int count=0;
		int max_addr_seq=1;

		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , "A) Oracle DB Connection success... setSyncMetsDB3 ");
			conn.setAutoCommit(false);

			PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null;

			String sql1 = " SELECT alias_id FROM CUST_DTL WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND STAT_FLAG in ('001', '003') AND EMP_NUM = ?  AND ROWNUM = 1 ";

			HashMap<String, String> getHp = new HashMap();

			ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null;

			for( int i=0; i<list6.size(); i++){
				pstmt1 = conn.prepareStatement(sql1);

				getHp = (HashMap) list6.get(i);

				String empCode = getHp.get("EMP_CODE");
				String metsLicense = getHp.get("METS_LICENSE");
				String merpLicense = getHp.get("MERP_LICENSE");
				String officeLicense = getHp.get("OFFICE_LICENSE");
				String skcardLicense = "001".equals( getHp.get("SKCARD_LICENSE") ) ? "003" : "001";
				String spId = getHp.get("SP_ID") == null ? "" : getHp.get("SP_ID");
				String spFlag = getHp.get("SP_FLAG") == null ? "" : getHp.get("SP_FLAG");
				String enpri_cd = "", addr_seq = "";
				String cust_id = "";

				pstmt1.setString(1, empCode);
				rs1 = pstmt1.executeQuery();

				String aliasId = "";

				if( rs1.next() ) aliasId = rs1.getString(1);


				if( !"".equals(spId) ){

				//if( !"".equals(spId) && ( "P".equals(spFlag) || "".equals(spFlag) ) ){

					strQry = " SELECT count(*) count FROM cust_dtl "
							+" WHERE "
							+" alias_id = ?  and cust_auth='502' and enpri_cd <> '007262' and stat_flag='001' ";

					pstmt2 = conn.prepareStatement(strQry);
					pstmt2.setString(1,spId);
					rs2 = pstmt2.executeQuery();

					if(rs2.next()){
						count = rs2.getInt("count");
					}

					if( pstmt2 != null ) pstmt2.close();

					if(count>0){
						// 추가정보 확인
						strQry = " SELECT enpri_cd, addr_seq FROM cust_dtl "
								+" WHERE "
								+" alias_id = ?  and cust_auth='502' and enpri_cd <> '007262' and stat_flag='001' ";

						pstmt3 = conn.prepareStatement(strQry);
						pstmt3.setString(1,spId);
						rs3 = pstmt3.executeQuery();
						if( rs3.next()){

							enpri_cd = rs3.getString("enpri_cd");
							addr_seq = rs3.getString("addr_seq");
						}

						if( pstmt3 != null ) pstmt3.close();

						strQry = " SELECT cust_id FROM cust_dtl "
								+" WHERE "
								+" alias_id = ?   ";

						pstmt4 = conn.prepareStatement(strQry);
						pstmt4.setString(1,aliasId);
						rs4 = pstmt4.executeQuery();
						if(rs4.next()){
							cust_id = rs4.getString("cust_id");
						}

						if( pstmt4 != null ) pstmt4.close();

						strQry =" MERGE INTO GW.MRO_CUST A "
								+ "USING ( SELECT CUST_ID FROM  cust_dtl  WHERE CUST_ID =? ) B "
								+ "ON  (A.CUST_ID = B.CUST_ID) "
								+ "WHEN MATCHED THEN "
								+ "UPDATE SET SP_ID=? "
								+ "WHERE a.cust_id = b.cust_id "
								+ "WHEn NOT MATCHED THEN "
								+ "INSERT (CUST_ID,SP_ID) "
								+ "VALUES(?,?) ";

						pstmt5 = conn.prepareStatement(strQry);

						pstmt5.setString(1,cust_id);
						pstmt5.setString(2,spId);
						pstmt5.setString(3,cust_id);
						pstmt5.setString(4,spId);

						if( pstmt5.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 1 " + cust_id + " >> " + spId);
						}

						if( pstmt5 != null ) pstmt5.close();

						strQry = " SELECT max(addr_seq)+1 max_addr_seq FROM enpri_addr WHERE enpri_cd='007262' AND stat_flag='001' ";
						pstmt6 = conn.prepareStatement(strQry);
						rs6 = pstmt6.executeQuery();

						if(rs6.next()){
							max_addr_seq = rs6.getInt("max_addr_seq");
						}

						strQry =  " UPDATE cust_dtl "
								+ " SET enpri_cd='007262' , busip_seq = '1' , addr_seq = '"+max_addr_seq+"' "
								+ " where enpri_cd='"+enpri_cd+"' and addr_seq ='"+addr_seq+"' and cust_id = (select cust_id from cust_dtl where alias_id = '"+spId+"' and speedcust_yn = 'Y')  ";

						pstmt6 = conn.prepareStatement(strQry);

						if( pstmt6.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 2 " + cust_id + " >> " + spId);
						}

						int temp_val = 0;
						strQry = " SELECT count(*) temp_val FROM cust_dtl WHERE enpri_cd='"+enpri_cd+"' and addr_seq ='"+addr_seq+"' ";
						pstmt6 = conn.prepareStatement(strQry);
						rs6 = pstmt6.executeQuery();
						if(rs6.next()){
							temp_val = rs6.getInt("temp_val");
						}

						if( pstmt6 != null ) pstmt6.close();

						if( temp_val > 1 ){

							strQry =  " INSERT INTO enpri_addr(ENPRI_CD, ADDR_SEQ, ZIP_SEQ, ETC_SNO, REG_MAN, REG_DATI, CATE_NUM, STAT_FLAG, ZIP, JUSO, SYS_MEMO, BUSIP_SEQ, JUSO2) "
									+ " SELECT '007262', '"+max_addr_seq+"', ZIP_SEQ, ETC_SNO, '"+cust_id+"', Sysdate, CATE_NUM, STAT_FLAG, ZIP, JUSO, '주소중복으로 인한 신규주소번호 생성', '1', JUSO2 "
									+ " from enpri_addr where enpri_cd='"+enpri_cd+"' and addr_seq ='"+addr_seq+"' ";

							pstmt7 = conn.prepareStatement(strQry);

							if(pstmt7.executeUpdate() < 0) {
								throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 3 " + cust_id + " >> " + spId);
							}

							if( pstmt7 != null ) pstmt7.close();

						}else{//sjjang 20150914 주소 중복 생성관련 방지처리

							strQry =  " UPDATE enpri_addr "
									+ " SET enpri_cd='007262' , busip_seq = '1' , addr_seq = '"+max_addr_seq+"' "
									+ " where enpri_cd='"+enpri_cd+"' and addr_seq ='"+addr_seq+"' ";

							pstmt8 = conn.prepareStatement(strQry);

							System.out.println(strQry);
							System.out.println(( i ) +" "+ cust_id + " >> " + spId );

							if(pstmt8.executeUpdate() < 0) {
								throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 4 " + cust_id + " >> " + spId);
							}

							if( pstmt8 != null ) pstmt8.close();

						}

					} // if(count>0){ end

				} // if( !"".equals(spId) ){ end

				if( rs2 != null ) rs2.close();
				if( rs3 != null ) rs3.close();
				if( rs4 != null ) rs4.close();
				if( rs5 != null ) rs5.close();
				if( rs6 != null ) rs6.close();
				if( rs7 != null ) rs7.close();

				if( pstmt1 != null ) pstmt1.close();
				if( pstmt2 != null ) pstmt2.close();
				if( pstmt3 != null ) pstmt3.close();
				if( pstmt4 != null ) pstmt4.close();
				if( pstmt5 != null ) pstmt5.close();
				if( pstmt6 != null ) pstmt6.close();
				if( pstmt7 != null ) pstmt7.close();
				if( pstmt8 != null ) pstmt8.close();

			} // end for

			if( rs1 != null ) rs1.close();

			if( pstmt1 != null ) pstmt1.close();

			conn.commit();

		}catch(Exception e){

			e.printStackTrace();

			log.Write( "setSyncMetsDB3 - ", e.getMessage());

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static void setSyncMetsDB4(List list5) {
		String strQry = "";

		int count=0;

		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , "A) Oracle DB Connection success... setSyncMetsDB3 ");
			conn.setAutoCommit(false);

			PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null;

			String sql1 = " SELECT alias_id FROM CUST_DTL WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND STAT_FLAG = '001' AND EMP_NUM = ?  AND ROWNUM = 1 ";

			HashMap<String, String> getHp = new HashMap();

			ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null;

			for( int i=0; i<list5.size(); i++){
				pstmt1 = conn.prepareStatement(sql1);

				getHp = (HashMap) list5.get(i);

				String empCode = getHp.get("EMP_CODE");
				String metsLicense = getHp.get("METS_LICENSE");
				String merpLicense = getHp.get("MERP_LICENSE");
				String officeLicense = getHp.get("OFFICE_LICENSE");
				String skcardLicense = "001".equals( getHp.get("SKCARD_LICENSE") ) ? "003" : "001";
				String spId = getHp.get("SP_ID") == null ? "" : getHp.get("SP_ID");
				String spFlag = getHp.get("SP_FLAG");
				String enpri_cd = "", addr_seq = "",  cust_id = "";
				int max_addr_seq = 0;

				pstmt1.setString(1, empCode);
				rs1 = pstmt1.executeQuery();

				String aliasId = "";

				if( rs1.next() ) aliasId = rs1.getString(1);

				if( !"".equals(spId) && "E".equals(spFlag) ){
					strQry = " SELECT count(*) count FROM cust_dtl "
							+" WHERE "
							+" alias_id = ?  and cust_auth='502' and enpri_cd <> 'A01639' and stat_flag='001' ";

					pstmt2 = conn.prepareStatement(strQry);
					pstmt2.setString(1,spId);
					rs2 = pstmt2.executeQuery();

					if(rs2.next()){

						count = rs2.getInt("count");
					}

					if(count>0){
						// 추가정보 확인
						strQry = " SELECT enpri_cd, addr_seq FROM cust_dtl "
								+" WHERE "
								+" alias_id = ?  and cust_auth='502' and enpri_cd <> 'A01639' and stat_flag='001' ";

						pstmt3 = conn.prepareStatement(strQry);
						pstmt3.setString(1,spId);
						rs3 = pstmt3.executeQuery();
						if( rs3.next()){

							enpri_cd = rs3.getString("enpri_cd");
							addr_seq = rs3.getString("addr_seq");
						}

						strQry = " SELECT cust_id FROM cust_dtl "
								+" WHERE "
								+" alias_id = ?   ";

						pstmt4 = conn.prepareStatement(strQry);
						pstmt4.setString(1,aliasId);
						rs4 = pstmt4.executeQuery();
						if(rs4.next()){
							cust_id = rs4.getString("cust_id");
						}


						strQry =" MERGE INTO GW.MRO_CUST A "
								+ "USING ( SELECT CUST_ID FROM  cust_dtl  WHERE CUST_ID =? ) B "
								+ "ON  (A.CUST_ID = B.CUST_ID) "
								+ "WHEN MATCHED THEN "
								+ "UPDATE SET SP_ID=? "
								+ "WHERE a.cust_id = b.cust_id "
								+ "WHEn NOT MATCHED THEN "
								+ "INSERT (CUST_ID,SP_ID) "
								+ "VALUES(?,?) ";

						pstmt5 = conn.prepareStatement(strQry);

						pstmt5.setString(1,cust_id);
						pstmt5.setString(2,spId);
						pstmt5.setString(3,cust_id);
						pstmt5.setString(4,spId);

						if( pstmt5.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 1 " + cust_id + " >> " + spId);
						}


						strQry = " SELECT max(addr_seq)+1 max_addr_seq FROM enpri_addr WHERE enpri_cd='A01639' AND stat_flag='001' ";
						pstmt6 = conn.prepareStatement(strQry);
						rs6 = pstmt6.executeQuery();

						if(rs6.next()){
							max_addr_seq = rs6.getInt("max_addr_seq");
						}

						strQry =  " UPDATE cust_dtl "
								+ " SET enpri_cd='A01639' , busip_seq = '1' , addr_seq = '"+max_addr_seq+"' "
								+ " where enpri_cd='"+enpri_cd+"' and addr_seq ='"+addr_seq+"' ";

						pstmt6 = conn.prepareStatement(strQry);

						if( pstmt6.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 2 " + cust_id + " >> " + spId);
						}

						strQry =  " UPDATE enpri_addr "
								+ " SET enpri_cd='A01639' , busip_seq = '1' , addr_seq = '"+max_addr_seq+"' "
								+ " where enpri_cd='"+enpri_cd+"' and addr_seq ='"+addr_seq+"' ";

						pstmt6 = conn.prepareStatement(strQry);

						if( pstmt6.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - setSyncMetsDB4 -- 3 " + cust_id + " >> " + spId);
						}


					} // if(count>0){ end

				} // if( !"".equals(spId) ){ end

				if( rs1 != null ) rs1.close();
				if( rs2 != null ) rs2.close();
				if( rs3 != null ) rs3.close();
				if( rs4 != null ) rs4.close();
				if( rs5 != null ) rs5.close();
				if( rs6 != null ) rs6.close();

				if( pstmt1 != null ) pstmt1.close();
				if( pstmt2 != null ) pstmt1.close();
				if( pstmt3 != null ) pstmt1.close();
				if( pstmt4 != null ) pstmt1.close();
				if( pstmt5 != null ) pstmt1.close();
				if( pstmt6 != null ) pstmt1.close();

			} // end for

			if( rs1 != null ) rs1.close();
			if( rs2 != null ) rs2.close();
			if( rs3 != null ) rs3.close();
			if( rs4 != null ) rs4.close();
			if( rs5 != null ) rs5.close();
			if( rs6 != null ) rs6.close();

			if( pstmt1 != null ) pstmt1.close();
			if( pstmt2 != null ) pstmt1.close();
			if( pstmt3 != null ) pstmt1.close();
			if( pstmt4 != null ) pstmt1.close();
			if( pstmt5 != null ) pstmt1.close();
			if( pstmt6 != null ) pstmt1.close();

			conn.commit();

		}catch(Exception e){

			log.Write( "setSyncMetsDB3 - ", e.getMessage());
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static void setSyncMetsDB3(List list4) {
		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , "A) Oracle DB Connection success... setSyncMetsDB3 ");
			conn.setAutoCommit(false);

			PreparedStatement pstmt1 = null, pstmt2 = null;

			String sql1 = " SELECT cust_id FROM CUST_DTL WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND STAT_FLAG = '001' AND EMP_NUM = ?   AND ROWNUM = 1 ";

			HashMap<String, String> getHp = new HashMap();

			ResultSet rs1 = null;

			for( int i=0; i<list4.size(); i++){
				pstmt1 = conn.prepareStatement(sql1);

				getHp = (HashMap) list4.get(i);

				String empCode = getHp.get("EMP_CODE");
				String metsLicense = getHp.get("METS_LICENSE");
				String merpLicense = getHp.get("MERP_LICENSE");
				String officeLicense = getHp.get("OFFICE_LICENSE");
				String skcardLicense = "001".equals( getHp.get("SKCARD_LICENSE") ) ? "003" : "001";
				String spId = getHp.get("SP_ID");
				String spFlag = getHp.get("SP_FLAG");

				pstmt1.setString(1, empCode);

				rs1 = pstmt1.executeQuery();

				String custId = "";

				if( rs1.next() ) custId = rs1.getString(1);


				String sql2 ="MERGE INTO GW.GW_CUST_ADD_INFO a " +
							" using ( SELECT CUST_ID FROM  cust_dtl  WHERE CUST_ID = ? ) b " +
							" on ( a.cust_id = b.cust_id ) " +
							" when matched then " +
							" update set gw_license = '001', mets_license = ?, merp_license = ?, office_license = ?, skcard_license = ? " +
							" when not matched then " +
							" insert ( CUST_ID, GW_LICENSE, METS_LICENSE, MERP_LICENSE, OFFICE_LICENSE, CUST_SETUP, SKCARD_LICENSE ) " +
							" values ( ?, '001', ?, ?, ?, 'N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,', ? ) ";

				pstmt2 = conn.prepareStatement(sql2);

				pstmt2.setString(1, custId);
				pstmt2.setString(2, metsLicense);
				pstmt2.setString(3, merpLicense);
				pstmt2.setString(4, officeLicense);
				pstmt2.setString(5, skcardLicense);
				pstmt2.setString(6, custId);
				pstmt2.setString(7, metsLicense);
				pstmt2.setString(8, merpLicense);
				pstmt2.setString(9, officeLicense);
				pstmt2.setString(10, skcardLicense);

				if( pstmt2.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - setSyncMetsDB3 -- 1 ");
				}

				if( pstmt1 !=null ) pstmt1.close();
				if( pstmt2 !=null ) pstmt2.close();
				if( rs1 !=null ) rs1.close();

			}

			conn.commit();

			if( rs1 !=null ) rs1.close();

			if( pstmt1 !=null ) pstmt1.close();
			if( pstmt2 !=null ) pstmt2.close();

		}catch(Exception e){

			log.Write( "setSyncMetsDB3 - ", e.getMessage());
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void setSyncMetsDB2(List list3) {
		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , "A) Oracle DB Connection success... setSyncMetsDB2 ");
			conn.setAutoCommit(false);

			PreparedStatement pstmt1 = null, pstmt2 = null;

			String sql1 = " SELECT CASE WHEN EXISTS ( SELECT 'x' FROM CUST_DTL WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND STAT_FLAG = '001' AND EMP_NUM = ?  ) THEN 'EXIST_METS' ELSE 'NO_METS' END METS FROM DUAL ";

			HashMap<String, String> getHp = new HashMap();

			ResultSet rs1 = null;

			for( int i=0; i<list3.size(); i++){
				pstmt1 = conn.prepareStatement(sql1);

				getHp = (HashMap) list3.get(i);

				String empName = getHp.get("EMP_NAME");
				String deptName = getHp.get("DEPT_NAME");
				String empCode = getHp.get("EMP_CODE");
				String ofcName = getHp.get("OFC_NAME");
				String posName = getHp.get("POS_NAME");
				String phone = getHp.get("PHONE");
				String email = getHp.get("EMAIL");
				String mobile = getHp.get("MOBILE");
				String birth = getHp.get("BIRTH");
				String deptId = getHp.get("DEPT_ID");
				String userId = getHp.get("USER_ID");

				pstmt1.setString(1, empCode);

				rs1 = pstmt1.executeQuery();

				String rst1 = "";

				if( rs1.next() ) rst1 = rs1.getString(1);

				if("EXIST_METS".equals(rst1)){
					// cust_dtl 생성
					String custId = getCustId();
					log.Write( "Program Name : GW_TO_METS" , " custId --> " + custId );
					String ognCd = getMetsOgnCd( deptId, deptName );
					String dutyCd = getDutyCd(posName);

					String sql2 = " UPDATE CUST_DTL SET STAT_FLAG = '003', SYS_MEMO = ?, MODI_DATI = SYSDATE, MODI_MAN = 'JavaBAT'  WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND STAT_FLAG = '001' AND EMP_NUM = ? ";

					pstmt2 = conn.prepareStatement(sql2);

					pstmt2.setString(1, "JAVA BATCH JOB에 의한 STAT_FLAG 001 -> 003");
					pstmt2.setString(2, empCode);

					if( pstmt2.executeUpdate() < 0 ){
						throw new Exception("알수 없는 에러 발생 - setSyncMetsDB -- 1 ");
					}
				}

				if( pstmt1 !=null ) pstmt1.close();
				if( pstmt2 !=null ) pstmt2.close();
				if( rs1 !=null ) rs1.close();
			}

			conn.commit();

			if( rs1 !=null ) rs1.close();

			if( pstmt1 !=null ) pstmt1.close();
			if( pstmt2 !=null ) pstmt2.close();

		}catch(Exception e){

			log.Write( "setSyncMetsDB2 - ", e.getMessage());
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( conn !=null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static List getListRetireEmployee() {
		List list = new ArrayList();

		Connection conn = null;
		Statement stmt1 = null;
		ResultSet rs1 = null;

		HashMap tableMap = new HashMap();

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);

			conn.setAutoCommit(false);

			System.out.println("Maria DB Connection success...");
			log.Write( "Program Name : GW_TO_METS - Retired " , " Maria DB Connection success...  ");

			stmt1 = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT				                               ").append("\n");
			sb.append("        A.EMP_CODE ,	                                               ").append("\n");
			sb.append("        A.EMP_NAME ,	                                               ").append("\n");
			sb.append("        IFNULL( C.POS_NAME, '사원') POS_NAME ,	               ").append("\n");
			sb.append("        IFNULL( OFC.OFC_NAME, '') OFC_NAME,			       ").append("\n");
			sb.append("        D.DEPT_NAME, A.PHONE, A.EMAIL, A.MOBILE, E.BIRTH,   ").append("\n");
			sb.append("        D.DEPT_ID, A.USER_ID, A.MAIL_PASSWORD, A.EMP_ID,	   ").append("\n");
			sb.append("        E.METS_LICENSE, E.MERP_LICENSE, E.OFFICE_LICENSE, E.SKCARD_LICENSE, E.SP_ID, E.SP_FLAG	   ").append("\n");
			sb.append("    FROM				                               ").append("\n");
			sb.append("        EMPLOYEE A,	                                               ").append("\n");
			sb.append("        MY_JOB B,		                                       ").append("\n");
			sb.append("        POSITION C,	                                               ").append("\n");
			sb.append("        DEPARTMENT D,	                                       ").append("\n");
			sb.append("        OFFICE OFC,		                                       ").append("\n");
			sb.append("        PERSON E,		                                       ").append("\n");
			sb.append("        COMPANY F                                                   ").append("\n");
			sb.append("    WHERE				                               ").append("\n");
			sb.append("        A.EMP_ID = B.EMP_ID                                         ").append("\n");
			sb.append("        AND A.PSN_ID = E.PSN_ID                                     ").append("\n");
			sb.append("        AND B.OFC_ID = OFC.OFC_ID                                   ").append("\n");
			sb.append("        AND A.POS_ID = C.POS_ID                                     ").append("\n");
			sb.append("        AND B.DEPT_ID = D.DEPT_ID                                   ").append("\n");
			sb.append("        AND A.CMP_ID =  'C300151214'                                ").append("\n");
			sb.append("        AND A.CMP_ID = F.CMP_ID                                     ").append("\n");
			sb.append("        AND A.USER_ID != 'systemadmin'                              ").append("\n");
			sb.append("        AND A.MAIL_LICENSE_TYPE = 'G'                               ").append("\n");
			sb.append("        AND B.DEFAULT_YN = 'Y'                                      ").append("\n");
			sb.append("        AND D.DEPT_STATUS = 'U'                                     ").append("\n");
			sb.append("        AND D.DEPT_LOC LIKE CONCAT('%',  'C300151214' , '%')        ").append("\n");
			sb.append("        AND B.DEPT_ID NOT LIKE CONCAT('%', 'DISASSIGN', '%')        ").append("\n");
			sb.append("        AND A.EMP_STATUS = 'R' 									   ").append("\n");
			sb.append("        AND A.EMP_CODE <> '100221'								   ").append("\n"); // 강대성 전 대표님 제외 함..
			sb.append("    ORDER BY                                                        ").append("\n");
			sb.append("        CASE 						       ").append("\n");
			sb.append("            WHEN OFC.OFC_ORDER=0 THEN 99999999 	               ").append("\n");
			sb.append("            ELSE OFC.OFC_ORDER				       ").append("\n");
			sb.append("        END,							       ").append("\n");
			sb.append("        C.POS_ORDER,						       ").append("\n");
			sb.append("        A.EMP_NAME						       ").append("\n");
			String qry = sb.toString();

			rs1 = stmt1.executeQuery(qry);

			while( rs1.next() ){
				HashMap<String, String> mp = new HashMap();

				mp.put("EMP_CODE" , rs1.getString("EMP_CODE"));
				mp.put("EMP_NAME" , rs1.getString("EMP_NAME"));
				mp.put("POS_NAME" , rs1.getString("POS_NAME"));
				mp.put("OFC_NAME" , rs1.getString("OFC_NAME"));
				mp.put("DEPT_NAME", rs1.getString("DEPT_NAME"));
				mp.put("PHONE", rs1.getString("PHONE"));
				mp.put("EMAIL", rs1.getString("EMAIL"));
				mp.put("MOBILE", rs1.getString("MOBILE"));
				mp.put("BIRTH", rs1.getString("BIRTH"));
				mp.put("DEPT_ID", rs1.getString("DEPT_ID") );
				mp.put("USER_ID", rs1.getString("USER_ID") );
				mp.put("MAIL_PASSWORD", rs1.getString("MAIL_PASSWORD") );
				mp.put("EMP_ID", rs1.getString("EMP_ID") );

				mp.put("METS_LICENSE", rs1.getString("METS_LICENSE") );
				mp.put("MERP_LICENSE", rs1.getString("MERP_LICENSE") );
				mp.put("OFFICE_LICENSE", rs1.getString("OFFICE_LICENSE") );
				mp.put("SKCARD_LICENSE", rs1.getString("SKCARD_LICENSE") );
				mp.put("SP_ID", rs1.getString("SP_ID") );
				mp.put("SP_FLAG", rs1.getString("SP_FLAG") );

				list.add(mp);
			}

			sb.setLength(0);

			sb.append(" UPDATE SCD_SCHEDULE						").append("\n");
			sb.append(" SET DISP_STATUS = 'cancelled'				").append("\n");
			sb.append("   , DEL_YN = 'Y'						").append("\n");
			sb.append("   , UPD_DT = NOW()						").append("\n");
			sb.append(" WHERE CALENDAR_ID = 'SCD29033795796612958578'		").append("\n");
			sb.append(" AND DISP_STATUS = 'cancelled' AND 	DEL_YN = 'Y'		").append("\n");
			sb.append(" AND UPD_EMP_ID IN (						").append("\n");
			sb.append("   SELECT							").append("\n");
			sb.append("           A.EMP_ID						").append("\n");
			sb.append("       FROM							").append("\n");
			sb.append("           EMPLOYEE A,					").append("\n");
			sb.append("           MY_JOB B,						").append("\n");
			sb.append("           POSITION C,					").append("\n");
			sb.append("           DEPARTMENT D,					").append("\n");
			sb.append("           OFFICE OFC,					").append("\n");
			sb.append("           PERSON E,						").append("\n");
			sb.append("           COMPANY F						").append("\n");
			sb.append("       WHERE							").append("\n");
			sb.append("           A.EMP_ID = B.EMP_ID				").append("\n");
			sb.append("           AND A.PSN_ID = E.PSN_ID				").append("\n");
			sb.append("           AND B.OFC_ID = OFC.OFC_ID				").append("\n");
			sb.append("           AND A.POS_ID = C.POS_ID				").append("\n");
			sb.append("           AND B.DEPT_ID = D.DEPT_ID				").append("\n");
			sb.append("           AND A.CMP_ID =  'C300151214'			").append("\n");
			sb.append("           AND A.CMP_ID = F.CMP_ID				").append("\n");
			sb.append("           AND A.USER_ID != 'systemadmin'			").append("\n");
			sb.append("           AND A.MAIL_LICENSE_TYPE = 'G'			").append("\n");
			sb.append("           AND B.DEFAULT_YN = 'Y'				").append("\n");
			sb.append("           AND D.DEPT_STATUS = 'U'				").append("\n");
			sb.append("           AND D.DEPT_LOC LIKE CONCAT('%',  'C300151214' , '%')            ").append("\n");
			sb.append("           AND B.DEPT_ID NOT LIKE CONCAT('%', 'DISASSIGN', '%')            ").append("\n");
			sb.append("           AND A.EMP_STATUS = 'R'				").append("\n");
			sb.append("           AND A.EMP_CODE <> '100221'	 )		").append("\n");

			qry = sb.toString();

			if( stmt1.executeUpdate(qry) < 0 ) throw new Exception("퇴사자 생일자 수정 시 Error 발생");

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}finally{
			try {

				if( stmt1 != null ) stmt1.close();
				if( rs1 != null ) rs1.close();
				if( conn != null ) conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	private static void setSyncMetsDB(List list2) {
		//System.out.println( list2);

		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , "A) Oracle DB Connection success... setSyncMetsDB ");
			conn.setAutoCommit(false);

			PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null;

			String sql1 = " SELECT CASE WHEN EXISTS ( SELECT 'x' FROM V_GW_EMPLOYEE WHERE EMP_NUM = ?  ) THEN 'EXIST_METS' ELSE 'NO_METS' END METS FROM DUAL ";
			String sql2 = " SELECT CASE WHEN EXISTS ( SELECT 'x' FROM V_GW_EMPLOYEE WHERE EMP_NUM = ? AND CUST_OGN_CD = ? ) THEN 'OGN_SAME' ELSE 'OGN_NOT_EQUAL' END METS FROM DUAL ";

			HashMap<String, String> getHp = new HashMap();

			ResultSet rs1 = null, rs2 = null;

			for( int i=0; i<list2.size(); i++){
				pstmt1 = conn.prepareStatement(sql1);
				pstmt2 = conn.prepareStatement(sql2);


				getHp = (HashMap) list2.get(i);

				String empName = getHp.get("EMP_NAME");
				String deptName = getHp.get("DEPT_NAME");
				String empCode = getHp.get("EMP_CODE");
				String ofcName = getHp.get("OFC_NAME");
				String posName = getHp.get("POS_NAME");
				String phone = getHp.get("PHONE");
				String email = getHp.get("EMAIL");
				String mobile = getHp.get("MOBILE");
				String birth = getHp.get("BIRTH");
				String deptId = getHp.get("DEPT_ID");
				String userId = getHp.get("USER_ID");
				String empLoc = getHp.get("EMP_LOC");
				String addrSeq = "";
				String password = "";

				if("100".equals(empLoc)){
					addrSeq = "778";
				}else if("101".equals(empLoc)){
					addrSeq = "471";
				}else if("102".equals(empLoc)){
					addrSeq = "462";
				}else if("103".equals(empLoc)){
					addrSeq = "460";
				}else{
					addrSeq = "";
				}


				/*
				if( getHp.get("MAIL_PASSWORD") != null){
					password = GW_TO_METS.Decrypt( getHp.get("MAIL_PASSWORD"), "ThugsLuv");
				}
				*/
				password = getHp.get("EMP_CODE");


				pstmt1.setString(1, empCode);

				log.Write( "Program Name : GW_TO_METS" , " empCode --> " + empCode );

				rs1 = pstmt1.executeQuery();

				String rst1 = "";
				String rst2 = "";

				if( rs1.next() ) rst1 = rs1.getString(1);

				if("NO_METS".equals(rst1)){
					// cust_dtl 생성
					String custId = getCustId();
					log.Write( "Program Name : GW_TO_METS" , " custId --> " + custId );
					String ognCd = getMetsOgnCd( deptId, deptName );
					String dutyCd = getDutyCd(posName);

					String sql4 = " INSERT INTO CUST_DTL (CUST_ID, ENPRI_CD, BUSIP_SEQ, OGN_CD, PASSWD, CUST_NM, TEL_NUM, MCOM_NUM, STAT_FLAG, EMAIL, DUTY_CD, EMP_NUM, CUST_AUTH, REG_MAN, REG_DATI, MAIL_ORDER, MAIL_RFQ, MAIL_SOUR, SMS_ORDER, SMS_RFQ, SMS_SOUR, MENU_TYPE, RCV_EMAIL2, ABSENT, RCV_SACC_REQ_MAIL, SYS_MEMO, TRA_INC_YN, ALIAS_ID, ADDR_SEQ ) "+
								  " VALUES (? ,'000001', 1, ?, ?, ?, ?, ?, '001', ?, ?, ?, '205', 'JavaBAT', SYSDATE, 'Y', 'N', 'N', 'Y', 'N', 'N', '001', '002', '001', '002', 'JAVA BATCH JOB 처리 등록', 'Y', ?, ? ) ";

					pstmt4 = conn.prepareStatement(sql4);

					pstmt4.setString(1, custId);
					pstmt4.setString(2, ognCd);
					pstmt4.setString(3, password);
					pstmt4.setString(4, empName);
					pstmt4.setString(5, phone);
					pstmt4.setString(6, mobile);
					pstmt4.setString(7, email);
					pstmt4.setString(8, dutyCd);
					pstmt4.setString(9, empCode);
					pstmt4.setString(10, userId);
					pstmt4.setString(11, addrSeq);

					System.out.println( userId );

					if( pstmt4.executeUpdate() < 0 ){
						System.out.println( userId + " >> ");

						throw new Exception("알수 없는 에러 발생 - setSyncMetsDB -- 1 " ) ;
					}

					String sql5 = " INSERT INTO GW.GW_CUST_ADD_INFO ( CUST_ID, MAIL_ALIAS_ID, GW_LICENSE, METS_LICENSE, MERP_LICENSE, OFFICE_LICENSE, CUST_SETUP, SKCARD_LICENSE ) "+
								  " VALUES ( ? , ?, '001', '001', '001', '001', 'N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,', '001' ) " ;

					pstmt5 = conn.prepareStatement(sql5);
					pstmt5.setString(1, custId);
					pstmt5.setString(2, userId);

					if( pstmt5.executeUpdate() < 0 ){
						throw new Exception("알수 없는 에러 발생 - setSyncMetsDB -- 2 ");
					}

					if( pstmt5 != null ) pstmt5.close();
					if( pstmt4 != null ) pstmt4.close();

				}else{
					//부서 비교
					pstmt2.setString(1, empCode);
					pstmt2.setString(2, deptId);

					rs2 = pstmt2.executeQuery();
					if( rs2.next() ) rst2 = rs2.getString(1);

					String ognCd = getMetsOgnCd( deptId, deptName );
					String dutyCd = getDutyCd(posName);

					String sql3 = "UPDATE CUST_DTL SET OGN_CD = ?, TEL_NUM = ?, MCOM_NUM = ?, EMAIL = ?, DUTY_CD = ?, SYS_MEMO = 'JAVA Batch Job >> Sync METS DB ', MODI_DATI = sysdate " +
						          " WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND EMP_NUM = ? AND OGN_CD <> ?  ";
					pstmt3 = conn.prepareStatement(sql3);

					pstmt3.setString(1, ognCd);
					pstmt3.setString(2, phone);
					pstmt3.setString(3, mobile);
					pstmt3.setString(4, email);
					pstmt3.setString(5, dutyCd);
					pstmt3.setString(6, empCode);
					pstmt3.setString(7, ognCd);

					if( pstmt3.executeUpdate() < 0 ){
						throw new Exception("알수 없는 에러 발생 - setSyncMetsDB -- 3 ");
					}

					if( pstmt3 != null ) pstmt3.close();
					if( rs2 != null ) rs2.close();
				}

				if( pstmt2 != null ) pstmt2.close();
				if( pstmt1 != null ) pstmt1.close();
				if( rs1 != null ) rs1.close();
				if( rs2 != null ) rs2.close();

			}

			conn.commit();

			if( rs1 != null ) rs1.close();
			if( rs2 != null ) rs2.close();
			if( pstmt1 != null ) pstmt1.close();
			if( pstmt2 != null ) pstmt2.close();
			if( pstmt3 != null ) pstmt3.close();
			if( pstmt4 != null ) pstmt4.close();
			if( pstmt5 != null ) pstmt5.close();

		}catch(Exception e){
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getDutyCd(String posName) {
		String dutyCd = "";
		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , " Oracle DB Connection success... :: getDutyCd ");
			conn.setAutoCommit(false);

			Statement stmt1 = null, stmt2 = null;
			stmt1 = conn.createStatement();

			String sql1 = " SELECT DUTY_CD FROM DUTY WHERE ENPRI_CD = '000001' AND HAN_NM = '"+posName+"' ";
			ResultSet rs1 = stmt1.executeQuery(sql1);

			if(rs1.next()){
				dutyCd = rs1.getString("DUTY_CD");
			}

			if( rs1 != null ) rs1.close();
			if( stmt1 != null ) stmt1.close();

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}finally{
			try {
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return dutyCd;
	}

	private static String getCustId() {
		String custId = "";
		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , " Oracle DB Connection success... :: getCustId ");
			conn.setAutoCommit(false);

			Statement stmt1 = null, stmt2 = null;
			stmt1 = conn.createStatement();

			String sql1 = " SELECT F_GET_CUST_ID('M') AS CUST_ID FROM DUAL ";
			ResultSet rs1 = stmt1.executeQuery(sql1);

			if(rs1.next()){
				custId = rs1.getString("CUST_ID");
			}

			if( rs1 != null ) rs1.close();
			if( stmt1 != null ) stmt1.close();

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}finally{
			try {
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return custId;
	}

	private static String getMetsOgnCd(String deptId, String deptName) {
		String ognCd = "";
		Connection conn = null;

		// D404730244, D291680127

		if( "D404730244".equals(deptId) || "D291680127".equals(deptId) || "D993055078".equals(deptId) ){  // 반도체 1, 2 팀이면..
			ognCd = "0000000087";
//		}else if( "D24551267".equals(deptId) || "D619719280".equals(deptId) ){  // ICT그룹, 통신·서비스팀 이면..
//			ognCd = "0000000086";
		}else if( "D100000011".equals(deptId) || "D571596470".equals(deptId) ){  // SE마케팅팀, SE구매그룹 이면..
			ognCd = "0000000074";

		}else{
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");

				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				log.Write( "Program Name : GW_TO_METS" , " Oracle DB Connection success... :: getMetsOgnCd ");

				conn.setAutoCommit(false);

				Statement stmt1 = null, stmt2 = null;
				PreparedStatement pstmt = null;
				stmt1 = conn.createStatement();
				stmt2 = conn.createStatement();

				String sql1 = " SELECT OGN_CD FROM OGN WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND OGN_HAN_NM = '"+deptName+"' AND CUST_OGN_CD = '"+deptId+"' AND STAT_FLAG = '001' AND ROWNUM = 1 ";
				ResultSet rs1 = stmt1.executeQuery(sql1);

				if(rs1.next()){
					ognCd = rs1.getString("OGN_CD");
				}else{

					String sql3 = "";
					String sql4 = " SELECT OGN_CD FROM OGN WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND CUST_OGN_CD = '"+deptId+"' AND STAT_FLAG = '001' AND ROWNUM = 1 ";

					ResultSet rs4 = stmt1.executeQuery(sql4);
					if(rs4.next()){
						ognCd = rs4.getString(1);

						sql3 = "UPDATE OGN SET OGN_HAN_NM = ? , SYS_MEMO = ?, MODI_DATI = sysdate, modi_man = ? WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND CUST_OGN_CD = ? AND STAT_FLAG = '001' ";
						pstmt = conn.prepareStatement(sql3);

						pstmt.setString(1, deptName);
						pstmt.setString(2, "JAVA BAT JOB으로 수정 처리");
						pstmt.setString(3, "JavaBAT");
						pstmt.setString(4, deptId);

						if( pstmt.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - getMetsOgnCd");
						}

						if( pstmt != null ) pstmt.close();

					}else{
						// OGN_CD 가 없으면 OGN_CD 를 만든다.
						String sql2 = " SELECT LPAD( MAX( OGN_CD ) + 1  , 10 , '0' ) FROM OGN WHERE ENPRI_CD = '000001' AND BUSIP_SEQ = 1 AND STAT_FLAG = '001' ";
						ResultSet rs2 = stmt2.executeQuery(sql2);

						if(rs2.next()){
							ognCd = rs2.getString(1);
						}

						sql3 = " INSERT INTO OGN ( ENPRI_CD, BUSIP_SEQ, OGN_CD, HRANK_OGN_CD, OGN_LVL, OGN_FLAG, CUST_OGN_CD, OGN_HAN_NM, OGN_ENG_NM, OGN_MNG_MAN, REF_FAC, STAT_FLAG, ADDR_SEQ, REG_MAN, REG_DATI, SYS_MEMO ) " +
						              " VALUES ('000001', 1, ?, NULL, '04', '001', ?, ?, ?, NULL, NULL,  '001', NULL, 'JavaBAT', sysdate, 'JAVA BAT JOB으로 등록 처리' ) ";

						pstmt = conn.prepareStatement(sql3);

						pstmt.setString(1, ognCd);
						pstmt.setString(2, deptId);
						pstmt.setString(3, deptName);
						pstmt.setString(4, deptName);

						if( pstmt.executeUpdate() < 0 ){
							throw new Exception("알수 없는 에러 발생 - getMetsOgnCd");
						}

						if( rs2 != null ) rs2.close();
						if( stmt2 != null ) stmt2.close();
						if( pstmt != null ) pstmt.close();
					}

					if( rs4 != null ) rs4.close();

				}

				if( rs1 != null ) rs1.close();
				if( stmt1 != null ) stmt1.close();

				conn.commit();

			}catch(Exception e){
				e.printStackTrace();
				
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}finally{
				try {
					if( conn != null ) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return ognCd;
	}

	private static List getListEmployee() {
		List list = new ArrayList();

		Connection conn = null;
		Statement stmt1 = null;
		ResultSet rs1 = null;

		HashMap tableMap = new HashMap();

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			System.out.println("Maria DB Connection success...");
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...  ");

			stmt1 = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT				                               ").append("\n");
			sb.append("        A.EMP_CODE ,	                                               ").append("\n");
			sb.append("        A.EMP_NAME ,	                                               ").append("\n");
			sb.append("        IFNULL( C.POS_NAME, '사원') POS_NAME ,	               ").append("\n");
			sb.append("        IFNULL( OFC.OFC_NAME, '') OFC_NAME,			       ").append("\n");
			sb.append("        D.DEPT_NAME, A.PHONE, A.SK_EMAIL EMAIL, A.MOBILE, E.BIRTH,   ").append("\n");
			sb.append("        D.DEPT_ID, A.USER_ID, A.MAIL_PASSWORD,			   ").append("\n");
			sb.append("        E.METS_LICENSE, E.MERP_LICENSE, E.OFFICE_LICENSE, E.SKCARD_LICENSE, E.SP_ID, E.SP_FLAG, A.EMP_LOC			   ").append("\n");
			sb.append("    FROM				                               ").append("\n");
			sb.append("        EMPLOYEE A,	                                               ").append("\n");
			sb.append("        MY_JOB B,		                                       ").append("\n");
			sb.append("        POSITION C,	                                               ").append("\n");
			sb.append("        DEPARTMENT D,	                                       ").append("\n");
			sb.append("        OFFICE OFC,		                                       ").append("\n");
			sb.append("        PERSON E,		                                       ").append("\n");
			sb.append("        COMPANY F                                                   ").append("\n");
			sb.append("    WHERE				                               ").append("\n");
			sb.append("        A.EMP_ID = B.EMP_ID                                         ").append("\n");
			sb.append("        AND A.PSN_ID = E.PSN_ID                                     ").append("\n");
			sb.append("        AND B.OFC_ID = OFC.OFC_ID                                   ").append("\n");
			sb.append("        AND A.POS_ID = C.POS_ID                                     ").append("\n");
			sb.append("        AND B.DEPT_ID = D.DEPT_ID                                   ").append("\n");
			sb.append("        AND A.CMP_ID =  'C300151214'                                ").append("\n");
			sb.append("        AND A.CMP_ID = F.CMP_ID                                     ").append("\n");
			sb.append("        AND A.USER_ID != 'systemadmin'                              ").append("\n");
			sb.append("        AND A.MAIL_LICENSE_TYPE = 'G'                               ").append("\n");
			sb.append("        AND B.DEFAULT_YN = 'Y'                                      ").append("\n");
			sb.append("        AND D.DEPT_STATUS = 'U'                                     ").append("\n");
			sb.append("        AND D.DEPT_LOC LIKE CONCAT('%',  'C300151214' , '%')        ").append("\n");
			sb.append("        AND B.DEPT_ID NOT LIKE CONCAT('%', 'DISASSIGN', '%')        ").append("\n");
			sb.append("        AND A.EMP_STATUS = 'W' AND D.DEPT_ID NOT IN ( 'D968067883') ").append("\n");
			//sb.append("        AND A.EMP_name in ( '한형석' )   ").append("\n");
			sb.append("        AND A.EMP_name not in ( '홍민호' )   ").append("\n");
			sb.append("    ORDER BY                                                        ").append("\n");
			sb.append("        CASE 						       ").append("\n");
			sb.append("            WHEN OFC.OFC_ORDER=0 THEN 99999999 	               ").append("\n");
			sb.append("            ELSE OFC.OFC_ORDER				       ").append("\n");
			sb.append("        END,							       ").append("\n");
			sb.append("        C.POS_ORDER,						       ").append("\n");
			sb.append("        A.EMP_NAME						       ").append("\n");
			String qry = sb.toString();

			rs1 = stmt1.executeQuery(qry);

			while( rs1.next() ){
				HashMap<String, String> mp = new HashMap();

				mp.put("EMP_CODE" , rs1.getString("EMP_CODE"));
				mp.put("EMP_NAME" , rs1.getString("EMP_NAME"));
				mp.put("POS_NAME" , rs1.getString("POS_NAME"));
				mp.put("OFC_NAME" , rs1.getString("OFC_NAME"));
				mp.put("DEPT_NAME", rs1.getString("DEPT_NAME"));
				mp.put("PHONE", rs1.getString("PHONE"));
				mp.put("EMAIL", rs1.getString("EMAIL"));
				mp.put("MOBILE", rs1.getString("MOBILE"));
				mp.put("BIRTH", rs1.getString("BIRTH"));
				mp.put("DEPT_ID", rs1.getString("DEPT_ID") );
				mp.put("USER_ID", rs1.getString("USER_ID") );
				mp.put("MAIL_PASSWORD", rs1.getString("MAIL_PASSWORD") );

				mp.put("METS_LICENSE", rs1.getString("METS_LICENSE") );
				mp.put("MERP_LICENSE", rs1.getString("MERP_LICENSE") );
				mp.put("OFFICE_LICENSE", rs1.getString("OFFICE_LICENSE") );
				mp.put("SKCARD_LICENSE", rs1.getString("SKCARD_LICENSE") );
				mp.put("SP_ID", rs1.getString("SP_ID") );
				mp.put("SP_FLAG", rs1.getString("SP_FLAG") );
				mp.put("EMP_LOC", rs1.getString("EMP_LOC") );

				list.add(mp);
			}
		}catch(Exception e){
			
		}finally{
			try {
				if( stmt1 != null ) stmt1.close();
				if( rs1 != null ) rs1.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	private static void setTeamJang(List lst) {
		Connection conn = null;

		HashMap<String, String> getHp = new HashMap();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , " Oracle DB Connection success...");
			conn.setAutoCommit(false);

			Statement stmt = null;
			PreparedStatement pstmt = null;

			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM SUPERVISOR ");

			String qry = "INSERT INTO SUPERVISOR ( EMP_NUM, CUST_NM, REG_DATI, SYS_MEMO, POS_NM, OFC_NM ) "+
						 "VALUES( ?,?,sysdate,?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String EMP_CODE = (String) getHp.get("EMP_CODE");
				String EMP_NAME = (String) getHp.get("EMP_NAME");
				String POS_NAME = (String) getHp.get("POS_NAME");
				String OFC_NAME = (String) getHp.get("OFC_NAME");

				pstmt.setString(idx++, EMP_CODE);
				pstmt.setString(idx++, EMP_NAME);
				pstmt.setString(idx++, "Crontab :: GW_TO_METS을 통해 등록");
				pstmt.setString(idx++, POS_NAME);
				pstmt.setString(idx++, OFC_NAME);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - TeamJang");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(SQLException se){
			se.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static List getTeamJang() {
		List list = new ArrayList();

		Connection conn = null;
		Statement stmt1 = null;
		ResultSet rs1 = null;

		HashMap tableMap = new HashMap();

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...");

			stmt1 = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT  DISTINCT     ").append("\n");
			sb.append("        A.EMP_CODE ,	").append("\n");
			sb.append("        A.EMP_NAME ,	").append("\n");
			sb.append("        C.POS_NAME ,	").append("\n");
			sb.append("        OFC.OFC_NAME	").append("\n");
			sb.append("    FROM				").append("\n");
			sb.append("        EMPLOYEE A,	").append("\n");
			sb.append("        MY_JOB B,	").append("\n");
			sb.append("        POSITION C,	").append("\n");
			sb.append("        DEPARTMENT D,	").append("\n");
			sb.append("        OFFICE OFC,		").append("\n");
			sb.append("        PERSON E,		").append("\n");
			sb.append("        COMPANY F        ").append("\n");
			sb.append("    WHERE				").append("\n");
			sb.append("        A.EMP_ID = B.EMP_ID        ").append("\n");
			sb.append("        AND A.PSN_ID = E.PSN_ID    ").append("\n");
			sb.append("        AND B.OFC_ID = OFC.OFC_ID  ").append("\n");
			sb.append("        AND A.POS_ID = C.POS_ID      ").append("\n");
			sb.append("        AND B.DEPT_ID = D.DEPT_ID    ").append("\n");
			sb.append("        AND A.CMP_ID =  'C300151214' ").append("\n");
			sb.append("        AND A.CMP_ID = F.CMP_ID      ").append("\n");
			sb.append("        AND A.USER_ID != 'systemadmin'  ").append("\n");
			sb.append("        AND A.MAIL_LICENSE_TYPE = 'G'   ").append("\n");
			//sb.append("        AND B.DEFAULT_YN = 'Y'          ").append("\n");
			sb.append("        AND D.DEPT_STATUS = 'U'         ").append("\n");
			sb.append("        AND D.DEPT_LOC LIKE CONCAT('%',  'C300151214' , '%') ").append("\n");
			sb.append("        AND B.USE_YN = 'Y'                                   ").append("\n");
			sb.append("        AND B.DEPT_ID NOT LIKE CONCAT('%', 'DISASSIGN', '%') ").append("\n");
			sb.append("        AND OFC.OFC_ID = 'O104000000' ").append("\n");
			sb.append("    ORDER BY                          ").append("\n");
			sb.append("        CASE 						 ").append("\n");
			sb.append("            WHEN OFC.OFC_ORDER=0 THEN 99999999 	").append("\n");
			sb.append("            ELSE OFC.OFC_ORDER					").append("\n");
			sb.append("        END,										").append("\n");
			sb.append("        C.POS_ORDER,								").append("\n");
			sb.append("        A.EMP_NAME            					").append("\n");
			String qry = sb.toString();

			rs1 = stmt1.executeQuery(qry);

			while( rs1.next() ){
				HashMap<String, String> mp = new HashMap();

				mp.put("EMP_CODE" ,rs1.getString("EMP_CODE"));
				mp.put("EMP_NAME" ,rs1.getString("EMP_NAME"));
				mp.put("POS_NAME" ,rs1.getString("POS_NAME"));
				mp.put("OFC_NAME" ,rs1.getString("OFC_NAME"));

				list.add(mp);
			}
		}catch(Exception e){

		}finally{
			try {
				if( stmt1 != null ) stmt1.close();
				if( rs1 != null ) rs1.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	private static void setMetsData(HashMap mp) {

		try{

			Set keySet = mp.keySet();
			Iterator it = keySet.iterator();
			while( it.hasNext() ){
				String key = (String) it.next();
				//System.out.println(key);
				//System.out.println( mp.get(key) );

				if( "POSITION".equals(key)){
					insertPosition( (ArrayList) mp.get(key));
				}else if( "PERSON".equals(key)){
					insertPerson( (ArrayList) mp.get(key));
				}else if( "OFFICE".equals(key)){
					insertOffice( (ArrayList) mp.get(key));
				}else if( "EMPLOYEE".equals(key)){
					insertEmployee( (ArrayList) mp.get(key));
				}else if( "DEPARTMENT".equals(key)){
					insertDepartment( (ArrayList) mp.get(key));
				}else if( "MY_JOB".equals(key)){
					insertMyJob( (ArrayList) mp.get(key));
				}else if( "COMPANY".equals(key)){
					insertCompany( (ArrayList) mp.get(key));
				}else if( "EMPLOYEE_KN".equals(key)){
					insertEmployeeKn( (ArrayList) mp.get(key));
				}else if( "EMPLOYEE_LOC".equals(key)){
					insertEmployeeLoc( (ArrayList) mp.get(key));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{

		}
	}

	private static void insertEmployeeLoc(ArrayList lst) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_EMPLOYEE_LOC ");

			String qry = "INSERT INTO GWIF_EMPLOYEE_LOC (EMP_ID, EMP_LOC, STAT_FLAG, REG_MAN, REG_DATI, SYS_MEMO) "+
						 "VALUES( ?,?,'001','javaBat',sysdate, 'javaBat - GW_TO_METS.java')";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String EMP_ID = (String) getHp.get("EMP_ID");
				String EMP_LOC = (String) getHp.get("EMP_LOC");

				pstmt.setString(idx++, EMP_ID);
				pstmt.setString(idx++, EMP_LOC);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - EMPLOYEE_LOC");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void insertEmployeeKn(ArrayList lst) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_EMPLOYEE_KN ");

			String qry = "INSERT INTO GWIF_EMPLOYEE_KN (EMP_ID, EMP_KN, STAT_FLAG, REG_MAN, REG_DATI, SYS_MEMO) "+
						 "VALUES( ?,?,'001','javaBat',sysdate, 'javaBat - GW_TO_METS.java')";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String EMP_ID = (String) getHp.get("EMP_ID");
				String EMP_KN = (String) getHp.get("EMP_KN");

				pstmt.setString(idx++, EMP_ID);
				pstmt.setString(idx++, EMP_KN);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - EMPLOYEE_KN");
				}
				
				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param arrayList
	 * @param conn
	 */
	private static void insertCompany(ArrayList lst ) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_COMPANY ");

			String qry = "INSERT INTO GWIF_COMPANY (CMP_ID, CMP_GRP_ID, CMP_DOMAIN, CMP_HOST, MAIL_DOMAIN, MAIL_HOST, CMP_LIC_NO, CMP_BIZ_NO, CMP_OPEN_DATE, CMP_ZIP, CMP_CATEGORY, CMP_KIND, CMP_TEL, CMP_FAX, CMP_EMP_COUNT, CMP_URL, CMP_GW_URL, CMP_NAME, CMP_CEO_NAME, CMP_ADDR, CMP_ADDR2, CMP_MGR_NAME, CMP_MGR_EMP_ID, CMP_MGR_DEPT, CMP_MGR_POSITION, CMP_MGR_TEL, CMP_MGR_EXT, CMP_MGR_MOBILE, CMP_MGR_EMAIL, ORG_DISPLAY, REG_DATE, MASTER_YN, DEL_FLAG_YN, PORTAL_DOMAIN, PORTAL_HOST, CMP_EN_NAME, SMS_USE_YN, FAX_USE_YN, SYS_CD, REPRSENT_EMAIL) "+
						 "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String CMP_ID = (String) getHp.get("CMP_ID");
				String CMP_GRP_ID = (String) getHp.get("CMP_GRP_ID");
				String CMP_DOMAIN = (String) getHp.get("CMP_DOMAIN");
				String CMP_HOST = (String) getHp.get("CMP_HOST");
				String MAIL_DOMAIN = (String) getHp.get("MAIL_DOMAIN");
				String MAIL_HOST = (String) getHp.get("MAIL_HOST");
				String CMP_LIC_NO = (String) getHp.get("CMP_LIC_NO");
				String CMP_BIZ_NO = (String) getHp.get("CMP_BIZ_NO");
				String CMP_OPEN_DATE = (String) getHp.get("CMP_OPEN_DATE");
				String CMP_ZIP = (String) getHp.get("CMP_ZIP");
				String CMP_CATEGORY = (String) getHp.get("CMP_CATEGORY");
				String CMP_KIND = (String) getHp.get("CMP_KIND");
				String CMP_TEL = (String) getHp.get("CMP_TEL");
				String CMP_FAX = (String) getHp.get("CMP_FAX");
				String CMP_EMP_COUNT = (String) getHp.get("CMP_EMP_COUNT");
				String CMP_URL = (String) getHp.get("CMP_URL");
				String CMP_GW_URL = (String) getHp.get("CMP_GW_URL");
				String CMP_NAME = (String) getHp.get("CMP_NAME");
				String CMP_CEO_NAME = (String) getHp.get("CMP_CEO_NAME");
				String CMP_ADDR = (String) getHp.get("CMP_ADDR");
				String CMP_ADDR2 = (String) getHp.get("CMP_ADDR2");
				String CMP_MGR_NAME = (String) getHp.get("CMP_MGR_NAME");
				String CMP_MGR_EMP_ID = (String) getHp.get("CMP_MGR_EMP_ID");
				String CMP_MGR_DEPT = (String) getHp.get("CMP_MGR_DEPT");
				String CMP_MGR_POSITION = (String) getHp.get("CMP_MGR_POSITION");
				String CMP_MGR_TEL = (String) getHp.get("CMP_MGR_TEL");
				String CMP_MGR_EXT = (String) getHp.get("CMP_MGR_EXT");
				String CMP_MGR_MOBILE = (String) getHp.get("CMP_MGR_MOBILE");
				String CMP_MGR_EMAIL = (String) getHp.get("CMP_MGR_EMAIL");
				String ORG_DISPLAY = (String) getHp.get("ORG_DISPLAY");
				String REG_DATE = (String) getHp.get("REG_DATE").replace(".0", "");
				String MASTER_YN = (String) getHp.get("MASTER_YN");
				String DEL_FLAG_YN = (String) getHp.get("DEL_FLAG_YN");
				String PORTAL_DOMAIN = (String) getHp.get("PORTAL_DOMAIN");
				String PORTAL_HOST = (String) getHp.get("PORTAL_HOST");
				String CMP_EN_NAME = (String) getHp.get("CMP_EN_NAME");
				String SMS_USE_YN = (String) getHp.get("SMS_USE_YN");
				String FAX_USE_YN = (String) getHp.get("FAX_USE_YN");
				String SYS_CD = (String) getHp.get("SYS_CD");
				String REPRSENT_EMAIL = (String) getHp.get("REPRSENT_EMAIL");

				pstmt.setString(idx++, CMP_ID);
				pstmt.setString(idx++, CMP_GRP_ID);
				pstmt.setString(idx++, CMP_DOMAIN);
				pstmt.setString(idx++, CMP_HOST);
				pstmt.setString(idx++, MAIL_DOMAIN);
				pstmt.setString(idx++, MAIL_HOST);
				pstmt.setString(idx++, CMP_LIC_NO);
				pstmt.setString(idx++, CMP_BIZ_NO);
				pstmt.setString(idx++, CMP_OPEN_DATE);
				pstmt.setString(idx++, CMP_ZIP);
				pstmt.setString(idx++, CMP_CATEGORY);
				pstmt.setString(idx++, CMP_KIND);
				pstmt.setString(idx++, CMP_TEL);
				pstmt.setString(idx++, CMP_FAX);
				pstmt.setString(idx++, CMP_EMP_COUNT);
				pstmt.setString(idx++, CMP_URL);
				pstmt.setString(idx++, CMP_GW_URL);
				pstmt.setString(idx++, CMP_NAME);
				pstmt.setString(idx++, CMP_CEO_NAME);
				pstmt.setString(idx++, CMP_ADDR);
				pstmt.setString(idx++, CMP_ADDR2);
				pstmt.setString(idx++, CMP_MGR_NAME);
				pstmt.setString(idx++, CMP_MGR_EMP_ID);
				pstmt.setString(idx++, CMP_MGR_DEPT);
				pstmt.setString(idx++, CMP_MGR_POSITION);
				pstmt.setString(idx++, CMP_MGR_TEL);
				pstmt.setString(idx++, CMP_MGR_EXT);
				pstmt.setString(idx++, CMP_MGR_MOBILE);
				pstmt.setString(idx++, CMP_MGR_EMAIL);
				pstmt.setString(idx++, ORG_DISPLAY);
				pstmt.setString(idx++, REG_DATE);
				pstmt.setString(idx++, MASTER_YN);
				pstmt.setString(idx++, DEL_FLAG_YN);
				pstmt.setString(idx++, PORTAL_DOMAIN);
				pstmt.setString(idx++, PORTAL_HOST);
				pstmt.setString(idx++, CMP_EN_NAME);
				pstmt.setString(idx++, SMS_USE_YN);
				pstmt.setString(idx++, FAX_USE_YN);
				pstmt.setString(idx++, SYS_CD);
				pstmt.setString(idx++, REPRSENT_EMAIL);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - COMPANY");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param arrayList
	 * @param conn
	 */
	private static void insertMyJob(ArrayList lst ) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_MY_JOB ");
			
			String qry = "INSERT INTO GWIF_MY_JOB (MYJ_ID, EMP_ID, DEPT_ID, PSN_ID, CMP_ID, OFC_ID, DEPT_ORDER, DEFAULT_YN, USE_YN) "+
						 "VALUES(?,?,?,?,?,?,?,?,?)";
			System.out.println( " lst.size() == > " + lst.size() );
			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String MYJ_ID = (String) getHp.get("MYJ_ID");
				String EMP_ID = (String) getHp.get("EMP_ID");
				String DEPT_ID = (String) getHp.get("DEPT_ID");
				String PSN_ID = (String) getHp.get("PSN_ID");
				String CMP_ID = (String) getHp.get("CMP_ID");
				String OFC_ID = (String) getHp.get("OFC_ID");
				String DEPT_ORDER = (String) getHp.get("DEPT_ORDER");
				String DEFAULT_YN = (String) getHp.get("DEFAULT_YN");
				String USE_YN = (String) getHp.get("USE_YN");

				pstmt.setString(idx++, MYJ_ID);
				pstmt.setString(idx++, EMP_ID);
				pstmt.setString(idx++, DEPT_ID);
				pstmt.setString(idx++, PSN_ID);
				pstmt.setString(idx++, CMP_ID);
				pstmt.setString(idx++, OFC_ID);
				pstmt.setString(idx++, DEPT_ORDER);
				pstmt.setString(idx++, DEFAULT_YN);
				pstmt.setString(idx++, USE_YN);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - MyJob");
				}
				
				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param arrayList
	 * @param conn
	 */
	private static void insertDepartment(ArrayList lst) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_DEPARTMENT ");

			String qry = "INSERT INTO GWIF_DEPARTMENT (DEPT_ID, CMP_ID, DEPT_CD, UP_DEPT_IDX, DEPT_NAME, DEPT_ORDER, DEPT_STATUS, DEPT_COMMENT, REG_DATE, DEPT_LOC, DEPT_DEPTH, DEPT_EN_NAME, DOCFILING_DEPT_ID, DEPT_DOCFILING_USE_YN, DEPT_SHORT_NAME, REPRSENT_EMAIL) "+
						 "VALUES(?,?,?,?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String DEPT_ID = (String) getHp.get("DEPT_ID");
				String CMP_ID = (String) getHp.get("CMP_ID");
				String DEPT_CD = (String) getHp.get("DEPT_CD");
				String UP_DEPT_IDX = (String) getHp.get("UP_DEPT_IDX");
				String DEPT_NAME = (String) getHp.get("DEPT_NAME");
				String DEPT_ORDER = (String) getHp.get("DEPT_ORDER");
				String DEPT_STATUS = (String) getHp.get("DEPT_STATUS");
				String DEPT_COMMENT = (String) getHp.get("DEPT_COMMENT");
				String REG_DATE = (String) getHp.get("REG_DATE")  == null ? "2015-12-22 00:00:00" : (String) getHp.get("REG_DATE").replace(".0", "");
				String DEPT_LOC = (String) getHp.get("DEPT_LOC");
				String DEPT_DEPTH = (String) getHp.get("DEPT_DEPTH");
				String DEPT_EN_NAME = (String) getHp.get("DEPT_EN_NAME");
				String DOCFILING_DEPT_ID = (String) getHp.get("DOCFILING_DEPT_ID");
				String DEPT_DOCFILING_USE_YN = (String) getHp.get("DEPT_DOCFILING_USE_YN");
				String DEPT_SHORT_NAME = (String) getHp.get("DEPT_SHORT_NAME");
				String REPRSENT_EMAIL = (String) getHp.get("REPRSENT_EMAIL");

				pstmt.setString(idx++, DEPT_ID);
				pstmt.setString(idx++, CMP_ID);
				pstmt.setString(idx++, DEPT_CD);
				pstmt.setString(idx++, UP_DEPT_IDX);
				pstmt.setString(idx++, DEPT_NAME);
				pstmt.setString(idx++, DEPT_ORDER);
				pstmt.setString(idx++, DEPT_STATUS);
				pstmt.setString(idx++, DEPT_COMMENT);
				pstmt.setString(idx++, REG_DATE);
				pstmt.setString(idx++, DEPT_LOC);
				pstmt.setString(idx++, DEPT_DEPTH);
				pstmt.setString(idx++, DEPT_EN_NAME);
				pstmt.setString(idx++, DOCFILING_DEPT_ID);
				pstmt.setString(idx++, DEPT_DOCFILING_USE_YN);
				pstmt.setString(idx++, DEPT_SHORT_NAME);
				pstmt.setString(idx++, REPRSENT_EMAIL);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - DEPARTMENT");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param arrayList
	 * @param conn
	 */
	private static void insertEmployee(ArrayList lst ) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_EMPLOYEE ");

			String qry = "INSERT INTO GWIF_EMPLOYEE (EMP_ID, PSN_ID, USER_ID, CMP_ID, POS_ID, EMP_NAME, ERROR_CNT, EMP_CODE, DENY_YN, PHONE, HIRE_DATE, SEC_LEVEL, RETIRE_DATE, EXT_NUM, PASSWORD_DATE, EMP_STATUS, EMP_LEVEL, EMAIL, MODIFY_DATE, REG_DATE, PASSWORD, MOBILE, EMP_CHARGE, FAX, DUTY_ID, EMP_EN_NAME, YHLD_OCCR_YY, CMP_EMAIL, MAIL_LICENSE_TYPE, SK_EMAIL) "+
						 "VALUES( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String EMP_ID = (String) getHp.get("EMP_ID");
				String PSN_ID = (String) getHp.get("PSN_ID");
				String USER_ID = (String) getHp.get("USER_ID");
				String CMP_ID = (String) getHp.get("CMP_ID");
				String POS_ID = (String) getHp.get("POS_ID");
				String EMP_NAME = (String) getHp.get("EMP_NAME");
				String ERROR_CNT = (String) getHp.get("ERROR_CNT");
				String EMP_CODE = (String) getHp.get("EMP_CODE");
				String DENY_YN = (String) getHp.get("DENY_YN");
				String PHONE = (String) getHp.get("PHONE");
				String HIRE_DATE = (String) getHp.get("HIRE_DATE");
				String SEC_LEVEL = (String) getHp.get("SEC_LEVEL");

				String RETIRE_DATE = "";
				try {
					RETIRE_DATE = (String) getHp.get("RETIRE_DATE") == null ? null : (String) getHp.get("RETIRE_DATE").substring(0, 10).replaceAll("-", "");
				}catch( StringIndexOutOfBoundsException e ) {
					RETIRE_DATE = "";
				}

				String EXT_NUM = (String) getHp.get("EXT_NUM");
				String PASSWORD_DATE = (String) getHp.get("PASSWORD_DATE") == null ? "2015-12-22 00:00:00" : (String) getHp.get("PASSWORD_DATE").replace(".0", "");
				String EMP_STATUS = (String) getHp.get("EMP_STATUS");
				String EMP_LEVEL = (String) getHp.get("EMP_LEVEL");
				String EMAIL = (String) getHp.get("EMAIL");
				String MODIFY_DATE = (String) getHp.get("MODIFY_DATE") == null ? "2015-12-22 00:00:00" : (String) getHp.get("MODIFY_DATE").replace(".0", "");
				String REG_DATE = (String) getHp.get("REG_DATE") == null ? "2015-12-22 00:00:00" : (String) getHp.get("REG_DATE").replace(".0", "");
				String PASSWORD = (String) getHp.get("PASSWORD");
				String MOBILE = (String) getHp.get("MOBILE");
				String EMP_CHARGE = (String) getHp.get("EMP_CHARGE");
				String FAX = (String) getHp.get("FAX");
				String DUTY_ID = (String) getHp.get("DUTY_ID");
				String EMP_EN_NAME = (String) getHp.get("EMP_EN_NAME");
				String YHLD_OCCR_YY = (String) getHp.get("YHLD_OCCR_YY");
				String CMP_EMAIL = (String) getHp.get("CMP_EMAIL");
				String MAIL_LICENSE_TYPE = (String) getHp.get("MAIL_LICENSE_TYPE");
				String SK_EMAIL = (String) getHp.get("SK_EMAIL");

				pstmt.setString(idx++, EMP_ID);
				pstmt.setString(idx++, PSN_ID);
				pstmt.setString(idx++, USER_ID);
				pstmt.setString(idx++, CMP_ID);
				pstmt.setString(idx++, POS_ID);
				pstmt.setString(idx++, EMP_NAME);
				pstmt.setString(idx++, ERROR_CNT);
				pstmt.setString(idx++, EMP_CODE);
				pstmt.setString(idx++, DENY_YN);
				pstmt.setString(idx++, PHONE);
				pstmt.setString(idx++, HIRE_DATE);
				pstmt.setString(idx++, SEC_LEVEL);
				pstmt.setString(idx++, RETIRE_DATE);
				pstmt.setString(idx++, EXT_NUM);
				pstmt.setString(idx++, PASSWORD_DATE);
				pstmt.setString(idx++, EMP_STATUS);
				pstmt.setString(idx++, EMP_LEVEL);
				pstmt.setString(idx++, EMAIL);
				pstmt.setString(idx++, MODIFY_DATE);
				pstmt.setString(idx++, REG_DATE);
				pstmt.setString(idx++, PASSWORD);
				pstmt.setString(idx++, MOBILE);
				pstmt.setString(idx++, EMP_CHARGE);
				pstmt.setString(idx++, FAX);
				pstmt.setString(idx++, DUTY_ID);
				pstmt.setString(idx++, EMP_EN_NAME);
				pstmt.setString(idx++, YHLD_OCCR_YY);
				pstmt.setString(idx++, CMP_EMAIL);
				pstmt.setString(idx++, MAIL_LICENSE_TYPE);
				pstmt.setString(idx++, SK_EMAIL);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - EMPLOYEE");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param arrayList
	 * @param conn
	 */
	private static void insertOffice(ArrayList lst ) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_OFFICE ");

			String qry = "INSERT INTO GWIF_OFFICE (OFC_ID, CMP_ID, OFC_NAME, OFC_ORDER, OFC_COMMENT, REG_DATE, OFC_EN_NAME, OFC_CD, REPRSENT_EMAIL) "+
						 "VALUES(?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String OFC_ID = (String) getHp.get("OFC_ID");
				String CMP_ID = (String) getHp.get("CMP_ID");
				String OFC_NAME = (String) getHp.get("OFC_NAME");
				String OFC_ORDER = (String) getHp.get("OFC_ORDER");
				String OFC_COMMENT = (String) getHp.get("OFC_COMMENT");
				String REG_DATE = (String) getHp.get("REG_DATE").replace(".0", "");
				String OFC_EN_NAME = (String) getHp.get("OFC_EN_NAME");
				String OFC_CD = (String) getHp.get("OFC_CD");
				String REPRSENT_EMAIL = (String) getHp.get("REPRSENT_EMAIL");

				pstmt.setString(idx++, OFC_ID);
				pstmt.setString(idx++, CMP_ID);
				pstmt.setString(idx++, OFC_NAME);
				pstmt.setString(idx++, OFC_ORDER);
				pstmt.setString(idx++, OFC_COMMENT);
				pstmt.setString(idx++, REG_DATE);
				pstmt.setString(idx++, OFC_EN_NAME);
				pstmt.setString(idx++, OFC_CD);
				pstmt.setString(idx++, REPRSENT_EMAIL);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - OFFICE");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param arrayList
	 * @param conn
	 */
	private static void insertPerson(ArrayList lst) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		Connection conn = gtm.getMetsConn();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_PERSON ");

			String qry = "INSERT INTO GWIF_PERSON (PSN_ID, EMP_NAME, LIC_NO, BIRTH, SOLAR_YN, ZIP_CODE, HOME_ADDR, HOME_ADDR2, EMP_CW_NAME, PHOTO, WED_DATE, HOME_TEL, PSN_MOOD, PSN_BLOG_URL, PSN_SITE_IMG, PSN_INFO_COLOR, PSN_INFO, PSN_NICK_NAME, MSN_NICKNAME, MOBILE) "+
						 "VALUES(?,?,HN_ENC.F_ENC_AES_TMP(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String PSN_ID = (String) getHp.get("PSN_ID");
				String EMP_NAME = (String) getHp.get("EMP_NAME");
				String LIC_NO = (String) getHp.get("LIC_NO");
				String BIRTH = (String) getHp.get("BIRTH");
				String SOLAR_YN = (String) getHp.get("SOLAR_YN");
				String ZIP_CODE = (String) getHp.get("ZIP_CODE");
				String HOME_ADDR = (String) getHp.get("HOME_ADDR");
				String HOME_ADDR2 = (String) getHp.get("HOME_ADDR2");
				String EMP_CW_NAME = (String) getHp.get("EMP_CW_NAME");
				String PHOTO = (String) getHp.get("PHOTO");
				String WED_DATE = (String) getHp.get("WED_DATE");
				String HOME_TEL = (String) getHp.get("HOME_TEL");
				String PSN_MOOD = (String) getHp.get("PSN_MOOD");
				String PSN_BLOG_URL = (String) getHp.get("PSN_BLOG_URL");
				String PSN_SITE_IMG = (String) getHp.get("PSN_SITE_IMG");
				String PSN_INFO_COLOR = (String) getHp.get("PSN_INFO_COLOR");
				String PSN_INFO = (String) getHp.get("PSN_INFO");
				String PSN_NICK_NAME = (String) getHp.get("PSN_NICK_NAME");
				String MSN_NICKNAME = (String) getHp.get("MSN_NICKNAME");
				String MOBILE = (String) getHp.get("MOBILE");

				pstmt.setString(idx++, PSN_ID);
				pstmt.setString(idx++, EMP_NAME);
				pstmt.setString(idx++, LIC_NO);
				pstmt.setString(idx++, BIRTH);
				pstmt.setString(idx++, SOLAR_YN);
				pstmt.setString(idx++, ZIP_CODE);
				pstmt.setString(idx++, HOME_ADDR);
				pstmt.setString(idx++, HOME_ADDR2);
				pstmt.setString(idx++, EMP_CW_NAME);
				pstmt.setString(idx++, PHOTO);
				pstmt.setString(idx++, WED_DATE);
				pstmt.setString(idx++, HOME_TEL);
				pstmt.setString(idx++, PSN_MOOD);
				pstmt.setString(idx++, PSN_BLOG_URL);
				pstmt.setString(idx++, PSN_SITE_IMG);
				pstmt.setString(idx++, PSN_INFO_COLOR);
				pstmt.setString(idx++, PSN_INFO);
				pstmt.setString(idx++, PSN_NICK_NAME);
				pstmt.setString(idx++, MSN_NICKNAME);
				pstmt.setString(idx++, MOBILE);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - PERSON");
				}

				if( pstmt != null ) pstmt.close();

			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * @param arrayList
	 */
	private static void insertPosition(ArrayList lst) {
		HashMap<String, String> getHp = new HashMap();
		
		GW_TO_METS gtm = new GW_TO_METS();
		
		Connection conn = gtm.getMetsConn();
		
		Statement stmt = null;
		PreparedStatement pstmt = null;
		try{
			stmt = conn.createStatement();
			int rst = stmt.executeUpdate("DELETE FROM GWIF_POSITION ");

			String qry = "INSERT INTO GWIF_POSITION (POS_ID, CMP_ID, POS_NAME, POS_ORDER, POS_COMMENT, REG_DATE, POS_EN_NAME, POS_CD, REPRSENT_EMAIL) VALUES(?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?)";

			for(int i=0;i<lst.size();i++){
				pstmt = conn.prepareStatement(qry);

				int idx = 1;

				getHp = (HashMap)lst.get(i);

				String POS_ID = (String) getHp.get("POS_ID");
				String CMP_ID = (String) getHp.get("CMP_ID");
				String POS_NAME = (String) getHp.get("POS_NAME");
				String POS_ORDER = (String) getHp.get("POS_ORDER");
				String POS_COMMENT = (String) getHp.get("POS_COMMENT");
				String REG_DATE = (String) getHp.get("REG_DATE").replace(".0", "");
				String POS_EN_NAME = (String) getHp.get("POS_EN_NAME");
				String POS_CD = (String) getHp.get("POS_CD");
				String REPRSENT_EMAIL = (String) getHp.get("REPRSENT_EMAIL");

				pstmt.setString(idx++, POS_ID);
				pstmt.setString(idx++, CMP_ID);
				pstmt.setString(idx++, POS_NAME);
				pstmt.setString(idx++, POS_ORDER);
				pstmt.setString(idx++, POS_COMMENT);
				pstmt.setString(idx++, REG_DATE);
				pstmt.setString(idx++, POS_EN_NAME);
				pstmt.setString(idx++, POS_CD);
				pstmt.setString(idx++, REPRSENT_EMAIL);

				if( pstmt.executeUpdate() < 0 ){
					throw new Exception("알수 없는 에러 발생 - POSITION");
				}

				if( pstmt != null ) pstmt.close();
			}

			conn.commit();

		}catch(Exception e){
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				if( pstmt != null ) pstmt.close();
				if( stmt != null ) stmt.close();
				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static HashMap getGwData() {
		List lst1 = new ArrayList();
		List lst2 = new ArrayList();
		List lst3 = new ArrayList();
		List lst4 = new ArrayList();
		List lst5 = new ArrayList();
		List lst6 = new ArrayList();
		List lst7 = new ArrayList();
		List lst8 = new ArrayList();
		List lst9 = new ArrayList();

		Connection conn = null;
		Statement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null, stmt6 = null, stmt7 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null;

		HashMap tableMap = new HashMap();

		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			log.Write( "Program Name : GW_TO_METS" , " Maria DB Connection success...");

			stmt1 = conn.createStatement();
			rs1 = stmt1.executeQuery("SELECT * FROM COMPANY");

			while(rs1.next()){
				HashMap<String, String> mp = new HashMap();

				mp.put("CMP_ID" ,rs1.getString("CMP_ID"));
				mp.put("CMP_GRP_ID" ,rs1.getString("CMP_GRP_ID"));
				mp.put("CMP_DOMAIN" ,rs1.getString("CMP_DOMAIN"));
				mp.put("CMP_HOST" ,rs1.getString("CMP_HOST"));
				mp.put("MAIL_DOMAIN" ,rs1.getString("MAIL_DOMAIN"));
				mp.put("MAIL_HOST" ,rs1.getString("MAIL_HOST"));
				mp.put("CMP_LIC_NO" ,rs1.getString("CMP_LIC_NO"));
				mp.put("CMP_BIZ_NO" ,rs1.getString("CMP_BIZ_NO"));
				mp.put("CMP_OPEN_DATE" ,rs1.getString("CMP_OPEN_DATE"));
				mp.put("CMP_ZIP" ,rs1.getString("CMP_ZIP"));
				mp.put("CMP_CATEGORY" ,rs1.getString("CMP_CATEGORY"));
				mp.put("CMP_KIND" ,rs1.getString("CMP_KIND"));
				mp.put("CMP_TEL" ,rs1.getString("CMP_TEL"));
				mp.put("CMP_FAX" ,rs1.getString("CMP_FAX"));
				mp.put("CMP_EMP_COUNT" ,rs1.getString("CMP_EMP_COUNT"));
				mp.put("CMP_URL" ,rs1.getString("CMP_URL"));
				mp.put("CMP_GW_URL" ,rs1.getString("CMP_GW_URL"));
				mp.put("CMP_NAME" ,rs1.getString("CMP_NAME"));
				mp.put("CMP_CEO_NAME" ,rs1.getString("CMP_CEO_NAME"));
				mp.put("CMP_ADDR" ,rs1.getString("CMP_ADDR"));
				mp.put("CMP_ADDR2" ,rs1.getString("CMP_ADDR2"));
				mp.put("CMP_MGR_NAME" ,rs1.getString("CMP_MGR_NAME"));
				mp.put("CMP_MGR_EMP_ID" ,rs1.getString("CMP_MGR_EMP_ID"));
				mp.put("CMP_MGR_DEPT" ,rs1.getString("CMP_MGR_DEPT"));
				mp.put("CMP_MGR_POSITION" ,rs1.getString("CMP_MGR_POSITION"));
				mp.put("CMP_MGR_TEL" ,rs1.getString("CMP_MGR_TEL"));
				mp.put("CMP_MGR_EXT" ,rs1.getString("CMP_MGR_EXT"));
				mp.put("CMP_MGR_MOBILE" ,rs1.getString("CMP_MGR_MOBILE"));
				mp.put("CMP_MGR_EMAIL" ,rs1.getString("CMP_MGR_EMAIL"));
				mp.put("ORG_DISPLAY" ,rs1.getString("ORG_DISPLAY"));
				mp.put("REG_DATE" ,rs1.getString("REG_DATE"));
				mp.put("MASTER_YN" ,rs1.getString("MASTER_YN"));
				mp.put("DEL_FLAG_YN" ,rs1.getString("DEL_FLAG_YN"));
				mp.put("PORTAL_DOMAIN" ,rs1.getString("PORTAL_DOMAIN"));
				mp.put("PORTAL_HOST" ,rs1.getString("PORTAL_HOST"));
				mp.put("CMP_EN_NAME" ,rs1.getString("CMP_EN_NAME"));
				mp.put("SMS_USE_YN" ,rs1.getString("SMS_USE_YN"));
				mp.put("FAX_USE_YN" ,rs1.getString("FAX_USE_YN"));
				mp.put("SYS_CD" ,rs1.getString("SYS_CD"));
				mp.put("REPRSENT_EMAIL" ,rs1.getString("REPRSENT_EMAIL"));

				lst1.add(mp);
			}

			tableMap.put("COMPANY", lst1);

			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery("SELECT * FROM DEPARTMENT  ");

			while(rs2.next()){
				HashMap<String, String> mp = new HashMap();

				mp.put("DEPT_ID" ,rs2.getString("DEPT_ID"));
				mp.put("CMP_ID" ,rs2.getString("CMP_ID"));
				mp.put("DEPT_CD" ,rs2.getString("DEPT_CD"));
				mp.put("UP_DEPT_IDX" ,rs2.getString("UP_DEPT_IDX"));
				mp.put("DEPT_NAME" ,rs2.getString("DEPT_NAME"));
				mp.put("DEPT_ORDER" ,rs2.getString("DEPT_ORDER"));
				mp.put("DEPT_STATUS" ,rs2.getString("DEPT_STATUS"));
				mp.put("DEPT_COMMENT" ,rs2.getString("DEPT_COMMENT"));
				mp.put("REG_DATE" ,rs2.getString("REG_DATE"));
				mp.put("DEPT_LOC" ,rs2.getString("DEPT_LOC"));
				mp.put("DEPT_DEPTH" ,rs2.getString("DEPT_DEPTH"));
				mp.put("DEPT_EN_NAME" ,rs2.getString("DEPT_EN_NAME"));
				mp.put("DOCFILING_DEPT_ID" ,rs2.getString("DOCFILING_DEPT_ID"));
				mp.put("DEPT_DOCFILING_USE_YN" ,rs2.getString("DEPT_DOCFILING_USE_YN"));
				mp.put("DEPT_SHORT_NAME" ,rs2.getString("DEPT_SHORT_NAME"));
				mp.put("REPRSENT_EMAIL" ,rs2.getString("REPRSENT_EMAIL"));

				lst2.add(mp);
			}

			tableMap.put("DEPARTMENT", lst2);

			stmt3 = conn.createStatement();
			rs3 = stmt3.executeQuery("SELECT * FROM POSITION ");

			while(rs3.next()){
				HashMap<String, String> mp = new HashMap();

				mp.put("POS_ID" ,rs3.getString("POS_ID"));
				mp.put("CMP_ID" ,rs3.getString("CMP_ID"));
				mp.put("POS_NAME" ,rs3.getString("POS_NAME"));
				mp.put("POS_ORDER" ,rs3.getString("POS_ORDER"));
				mp.put("POS_COMMENT" ,rs3.getString("POS_COMMENT"));
				mp.put("REG_DATE" ,rs3.getString("REG_DATE"));
				mp.put("POS_EN_NAME" ,rs3.getString("POS_EN_NAME"));
				mp.put("POS_CD" ,rs3.getString("POS_CD"));
				mp.put("REPRSENT_EMAIL" ,rs3.getString("REPRSENT_EMAIL"));

				lst3.add(mp);
			}

			tableMap.put("POSITION", lst3);

			stmt4 = conn.createStatement();
			rs4 = stmt4.executeQuery("SELECT * FROM OFFICE ");

			while(rs4.next()){
				HashMap<String, String> mp = new HashMap();

				mp.put("OFC_ID" ,rs4.getString("OFC_ID"));
				mp.put("CMP_ID" ,rs4.getString("CMP_ID"));
				mp.put("OFC_NAME" ,rs4.getString("OFC_NAME"));
				mp.put("OFC_ORDER" ,rs4.getString("OFC_ORDER"));
				mp.put("OFC_COMMENT" ,rs4.getString("OFC_COMMENT"));
				mp.put("REG_DATE" ,rs4.getString("REG_DATE"));
				mp.put("OFC_EN_NAME" ,rs4.getString("OFC_EN_NAME"));
				mp.put("OFC_CD" ,rs4.getString("OFC_CD"));
				mp.put("REPRSENT_EMAIL" ,rs4.getString("REPRSENT_EMAIL"));

				lst4.add(mp);
			}

			tableMap.put("OFFICE", lst4);

			stmt5 = conn.createStatement();
			rs5 = stmt5.executeQuery("select   EMP_ID,  PSN_ID,  USER_ID,  CMP_ID,  POS_ID,  EMP_NAME,  ERROR_CNT,  EMP_CODE,  DENY_YN,  PHONE,  HIRE_DATE,  SEC_LEVEL,  DATE_FORMAT( RETIRE_DATE, '%Y%m%d') RETIRE_DATE,  EXT_NUM,  PASSWORD_DATE,  EMP_STATUS,  EMP_LEVEL,  EMAIL,  MODIFY_DATE,  REG_DATE,  PASSWORD,  MOBILE,  EMP_CHARGE,  FAX,  DUTY_ID,  EMP_EN_NAME,  YHLD_OCCR_YY,  CMP_EMAIL,  MAIL_LICENSE_TYPE,  CMP_EMAIL,  MAIL_LICENSE_TYPE,  SK_EMAIL,  EMP_KN,  EMP_LOC from EMPLOYEE WHERE USER_ID != 'systemadmin' AND MAIL_LICENSE_TYPE = 'G'  ");

			while(rs5.next()){
				HashMap<String, String> mp = new HashMap();
				HashMap<String, String> mp2 = new HashMap();
				HashMap<String, String> mp3 = new HashMap();

				mp.put("EMP_ID" ,rs5.getString("EMP_ID"));
				mp.put("PSN_ID" ,rs5.getString("PSN_ID"));
				mp.put("USER_ID" ,rs5.getString("USER_ID"));
				mp.put("CMP_ID" ,rs5.getString("CMP_ID"));
				mp.put("POS_ID" ,rs5.getString("POS_ID"));
				mp.put("EMP_NAME" ,rs5.getString("EMP_NAME"));
				mp.put("ERROR_CNT" ,rs5.getString("ERROR_CNT"));
				mp.put("EMP_CODE" ,rs5.getString("EMP_CODE"));
				mp.put("DENY_YN" ,rs5.getString("DENY_YN"));
				mp.put("PHONE" ,rs5.getString("PHONE"));
				mp.put("HIRE_DATE" ,rs5.getString("HIRE_DATE"));
				mp.put("SEC_LEVEL" ,rs5.getString("SEC_LEVEL"));
				mp.put("RETIRE_DATE" ,rs5.getString("RETIRE_DATE"));
				mp.put("EXT_NUM" ,rs5.getString("EXT_NUM"));
				mp.put("PASSWORD_DATE" ,rs5.getString("PASSWORD_DATE"));
				mp.put("EMP_STATUS" ,rs5.getString("EMP_STATUS"));
				mp.put("EMP_LEVEL" ,rs5.getString("EMP_LEVEL"));
				mp.put("EMAIL" ,rs5.getString("EMAIL"));
				mp.put("MODIFY_DATE" ,rs5.getString("MODIFY_DATE"));
				mp.put("REG_DATE" ,rs5.getString("REG_DATE"));
				mp.put("PASSWORD" ,rs5.getString("PASSWORD"));
				mp.put("MOBILE" ,rs5.getString("MOBILE"));
				mp.put("EMP_CHARGE" ,rs5.getString("EMP_CHARGE"));
				mp.put("FAX" ,rs5.getString("FAX"));
				mp.put("DUTY_ID" ,rs5.getString("DUTY_ID"));
				mp.put("EMP_EN_NAME" ,rs5.getString("EMP_EN_NAME"));
				mp.put("YHLD_OCCR_YY" ,rs5.getString("YHLD_OCCR_YY"));
				mp.put("CMP_EMAIL" ,rs5.getString("CMP_EMAIL"));
				mp.put("MAIL_LICENSE_TYPE" ,rs5.getString("MAIL_LICENSE_TYPE"));
				mp.put("CMP_EMAIL" ,rs5.getString("CMP_EMAIL"));
				mp.put("MAIL_LICENSE_TYPE" ,rs5.getString("MAIL_LICENSE_TYPE"));
				mp.put("SK_EMAIL" ,rs5.getString("SK_EMAIL"));

				lst5.add(mp);

				mp2.put("EMP_ID" ,rs5.getString("EMP_ID"));
				mp2.put("EMP_KN", rs5.getString("EMP_KN"));

				lst8.add(mp2);

				mp3.put("EMP_ID" ,rs5.getString("EMP_ID"));
				mp3.put("EMP_LOC", rs5.getString("EMP_LOC"));

				lst9.add(mp3);

			}

			tableMap.put("EMPLOYEE", lst5);
			tableMap.put("EMPLOYEE_KN", lst8);
			tableMap.put("EMPLOYEE_LOC", lst9);

			stmt6 = conn.createStatement();
			rs6 = stmt6.executeQuery("select * from PERSON  ");
			int i = 0;
			while(rs6.next()){

				HashMap<String, String> mp = new HashMap();

				mp.put("PSN_ID" ,rs6.getString("PSN_ID"));
				mp.put("EMP_NAME" ,rs6.getString("EMP_NAME"));
				mp.put("LIC_NO" , decode( rs6.getString("LIC_NO") == null ? "" : rs6.getString("LIC_NO")) );
				mp.put("BIRTH" ,rs6.getString("BIRTH"));
				mp.put("SOLAR_YN" ,rs6.getString("SOLAR_YN"));
				mp.put("ZIP_CODE" ,rs6.getString("ZIP_CODE"));
				mp.put("HOME_ADDR" ,rs6.getString("HOME_ADDR"));
				mp.put("HOME_ADDR2" ,rs6.getString("HOME_ADDR2"));
				mp.put("EMP_CW_NAME" ,rs6.getString("EMP_CW_NAME"));
				mp.put("PHOTO" ,rs6.getString("PHOTO"));
				mp.put("WED_DATE" ,rs6.getString("WED_DATE"));
				mp.put("HOME_TEL" ,rs6.getString("HOME_TEL"));
				mp.put("PSN_MOOD" ,rs6.getString("PSN_MOOD"));
				mp.put("PSN_BLOG_URL" ,rs6.getString("PSN_BLOG_URL"));
				mp.put("PSN_SITE_IMG" ,rs6.getString("PSN_SITE_IMG"));
				mp.put("PSN_INFO_COLOR" ,rs6.getString("PSN_INFO_COLOR"));
				mp.put("PSN_INFO" ,rs6.getString("PSN_INFO"));
				mp.put("PSN_NICK_NAME" ,rs6.getString("PSN_NICK_NAME"));
				mp.put("MSN_NICKNAME" ,rs6.getString("MSN_NICKNAME"));
				mp.put("MOBILE" ,rs6.getString("MOBILE"));

				lst6.add(mp);
			}

			tableMap.put("PERSON", lst6);

			stmt7 = conn.createStatement();
			rs7 = stmt7.executeQuery("select * from MY_JOB  ");
			
			int count = 0;
			
			while(rs7.next()){
				HashMap<String, String> mp = new HashMap();

				mp.put("MYJ_ID" ,rs7.getString("MYJ_ID") == null ? "" : rs7.getString("MYJ_ID"));
				mp.put("EMP_ID" ,rs7.getString("EMP_ID") == null ? "" : rs7.getString("EMP_ID"));
				mp.put("DEPT_ID" ,rs7.getString("DEPT_ID") == null ? "" : rs7.getString("DEPT_ID"));
				mp.put("PSN_ID" ,rs7.getString("PSN_ID") == null ? "" : rs7.getString("PSN_ID"));
				mp.put("CMP_ID" ,rs7.getString("CMP_ID") == null ? "" : rs7.getString("CMP_ID"));
				mp.put("OFC_ID" ,rs7.getString("OFC_ID") == null ? "" : rs7.getString("OFC_ID"));
				mp.put("DEPT_ORDER" ,rs7.getString("DEPT_ORDER") == null ? "" : rs7.getString("DEPT_ORDER"));
				mp.put("DEFAULT_YN" ,rs7.getString("DEFAULT_YN") == null ? "" : rs7.getString("DEFAULT_YN"));
				mp.put("USE_YN" ,rs7.getString("USE_YN") == null ? "" : rs7.getString("USE_YN"));

				lst7.add(mp);
			}

			tableMap.put("MY_JOB", lst7);

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if( stmt1 != null ) stmt1.close();
				if( stmt2 != null ) stmt2.close();
				if( stmt3 != null ) stmt3.close();
				if( stmt4 != null ) stmt4.close();
				if( stmt5 != null ) stmt5.close();
				if( stmt6 != null ) stmt6.close();
				if( stmt7 != null ) stmt7.close();
				if( rs1 != null ) rs1.close();
				if( rs2 != null ) rs2.close();
				if( rs3 != null ) rs3.close();
				if( rs4 != null ) rs4.close();
				if( rs5 != null ) rs5.close();
				if( rs6 != null ) rs6.close();
				if( rs7 != null ) rs7.close();

				if( conn != null ) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return tableMap;
	}

	public static String Decrypt(String text, String key) throws Exception{

          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
          byte[] keyBytes= new byte[16];
          byte[] b= key.getBytes("UTF-8");
          int len= b.length;
          if (len > keyBytes.length) len = keyBytes.length;
          System.arraycopy(b, 0, keyBytes, 0, len);
          SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
          IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
          cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

          BASE64Decoder decoder = new BASE64Decoder();
          byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
          return new String(results,"UTF-8");
    }

	public static String decode(String dec) {
		return decode(dec, StringUtils.defaultString("naongw", "naongw"));
	}

	public static String decode(String dec, String cryptoType) {
		return decode(dec, null, cryptoType);
	}
	public static String decode(String dec, String cryptoKey, String cryptoType) {
		String decStr = null;

		byte[] byteKey = { (byte) 0x4e, (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x4e, (byte) 0x00, (byte) 0x47, (byte) 0x00, (byte) 0x57, (byte) 0x00, (byte) 0x21,
				(byte) 0x00, (byte) 0x7e, (byte) 0x00 };
		SeedCipher seed = new SeedCipher();
		decStr = seed.decryptAsString(Base64.decode(dec), byteKey);

		return decStr;
	}
	
	private Connection getMetsConn() {
		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			log.Write( "Program Name : GW_TO_METS" , " Oracle DB Connection success...");
			conn.setAutoCommit(false);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
		
	}
}