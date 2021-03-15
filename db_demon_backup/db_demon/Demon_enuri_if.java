

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class Demon_enuri_if {
	
//	txt���� ��� ����
	private static String log_dir = "/user/home/image/src/file_att/engn_page/ep/";
	private static String dir = "/user/home/image/src/file_att/engn_page/";
	private static String txt = "EP.txt";
//	private static String log_dir = "D:\\ep\\";
//	private static String dir = "D:\\ep\\";
//	private static String txt = "EP.txt";
	BufferedWriter br = null;

//	�α׼���	
	private static String today;
	private static String filename;      					// �α�ȭ���̸�.
	private static String logTime ;       					// log�� �߻��� �ð�.
	private static boolean isExist = false;     			// �α� ȭ���� �����ϴ����� ����.
	
//	DB ����
	private String driver="oracle.jdbc.driver.OracleDriver";	
	/*���߼���*/
//	private String dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
//	private String user_id="METS_IMSI";
//	private String user_pw="METS_IMSI";	
	/*�����*/
	public String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:NDB10";
	public String user_id="METS18940G";
	public String user_pw="METS25519P58563W";	
	private	Connection conn;
	private Statement smt= null, smt2 = null, smt3 = null, smt4  = null, smt5 = null;
	private PreparedStatement ps=null, ps2=null, ps3=null, ps4=null, ps5=null;
	private CallableStatement cs = null;
	private ResultSet rs = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null;
	
	private String c_flag = "";
	private String MAPSEQ = "";
	private String qry = "";
	private String sMSG = "";
	
	String sMSG1 = "5-1. ������̺� ��ǰ ���";
	String sMSG2 = "5-2. ������̺� ��ǰ ����";
	String sMSG3 = "5-3. ������̺� ��ǰ ����";
	
	String insertQry = " INSERT INTO SPEED_ENURI_EP(MAPID, PNAME, PRICE, PGURL, DELIV, IGURL, BILLYN, PCARD, MAKER, CATE, CARDP, COUPO, MPRICE, DDELIV, INSTALLYN, CLASS, CREATE_DATE, UPDATE_DATE, STOCK, UPDATE_YN, MEMO) " + 
			 " VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?, 'I', SYSDATE,SYSDATE, ?, 'Y', '�ű�') ";
	
	String insertHistQry = " INSERT INTO SPEED_ENURI_HIST(MAPID, MAPSEQ, PNAME, PRICE, PGURL, DELIV, IGURL, BILLYN, PCARD, MAKER, CATE, CARDP, COUPO, MPRICE, DDELIV, INSTALLYN, CLASS, CREATE_DATE, STOCK, MEMO) " + 
		  	  " VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?, SYSDATE,?,?) ";
	
	String selectHistQry = " SELECT NVL(MAX(MAPSEQ)+1,1) MAPSEQ FROM SPEED_ENURI_HIST WHERE MAPID = ? ";
	
	String deleteQry = 	" UPDATE SPEED_ENURI_EP " + 
			"    SET CLASS = 'D', UPDATE_DATE = SYSDATE, MEMO = '����', UPDATE_YN = 'Y' " + 
			"  WHERE MAPID = ? ";	
	
	public Connection DB_Conn() {

		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(dbURL,user_id,user_pw);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			System.out.println("ERR driver unavailable !!"+e.getMessage());
			logWrite("DB_Conn : ClassNotFoundException ==>"+e);
		} catch (Exception e) {
			System.out.println("ERR driver not loaded !!"+e.getMessage());
			logWrite("DB_Conn : Exception ==>"+e);
		}
		return conn;
	}
	
	public void DB_DisConn() {
		try {
			conn.setAutoCommit(true);

			if(rs != null){ rs.close();}
			if(rs2 != null){ rs2.close();}
			if(rs3 != null){ rs3.close();}
			if(rs4 != null){ rs4.close();}
			if(rs5 != null){ rs5.close();}
			if(smt != null){ smt.close(); }
			if(smt2 != null){ smt2.close(); }
			if(smt3 != null){ smt3.close(); }
			if(smt4 != null){ smt4.close(); }
			if(smt5 != null){ smt5.close(); }
			if(ps != null){ ps.close(); }
			if(ps2 != null){ ps2.close(); }
			if(ps3 != null){ ps3.close(); }
			if(ps4 != null){ ps4.close(); }
			if(ps5 != null){ ps5.close(); }
			if(cs != null){ cs.close(); }
			if(conn != null){ conn.close(); }
		} catch (Exception e) {
			logWrite("DB_DisConn : Exception ==>"+e);
			e.printStackTrace();
		}
	}
	 
	public void start() throws Exception{
		/*
		 * (1) �ӽ����̺� ��ǰ���� �� ��ǰ���
		 * 1-1. �ӽ����̺� Delete
		 * 1-2 �ӽ����̺� Insert
		 * (2) �ӽ����̺� Select ������  list(map)�� ����
		 * (3) ������̺� Update_YN�� N���� ������Ʈ 
		 * (4) ������̺� ��ǰ ��� �� ����
		 * 1. ������̺� Insert
		 * 2. ������̺� ��ǰ ���������� Update
		 * (5) ������̺� ���� �� ��ǰ ������Ʈ
		 * 1. ǰ����ǰ ������Ʈ
		 * 2. ǰ�� ������ǰ ������Ʈ
		 * 3. ������̺� ��ǰ���� Update 
		 * (6) ������ Txt ���� ����
		 */
		
		try{
			DB_Conn();
			
			qry =   ""+
					"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_enuri_if_sgkang','START',SYSDATE, 'OK', NULL ) "+					
					"";			
			ps = conn.prepareStatement(qry);
			ps.executeUpdate();
			conn.commit();
			
			
			ps = conn.prepareStatement(insertQry);
			ps2 = conn.prepareStatement(deleteQry);
			ps3 = conn.prepareStatement(selectHistQry);
			ps4 = conn.prepareStatement(insertHistQry);

			sMSG = "1.�ӽ����̺� Delete";
			qry = 	" DELETE FROM SPEED_ENURI_TEMP "; 
			int res = 0;
			smt = conn.createStatement();
			smt.executeUpdate(qry);
			conn.commit();
			smt.close();
			logWrite(sMSG);
			System.out.println(sMSG);
			
			sMSG = "2.�ӽ����̺� Insert";
			qry = 	"   	INSERT INTO SPEED_ENURI_TEMP " +
					"    	SELECT sed.prd_cd AS mapid,  " + 
					"           (SELECT PLIS_nm  " + 
					"              FROM plis  " + 
					"             WHERE plis_cd = pi.plis_cd)  || ' ' || pi.make_co_nm ||  ' ' || pi.tmk_hang_nm || ' ' || pi.mdl_hang_nm " +  
//					"             WHERE plis_cd = pi.plis_cd) " +
					"              AS pname,  " + 
					"           CASE  " + 
					"              WHEN pi.tax_yn = 'A'  " + 
					"              THEN  " + 
					"                 p_spd_speed.f_get_sell_pri ('no_login', sed.prd_cd, 'DOWN') * 1.1  " +
					"              ELSE  " + 
					"                 p_spd_speed.f_get_sell_pri ('no_login', sed.prd_cd, 'DOWN') * 1.0 " +
					"           END  " + 
					"              AS price,  " + 
					"           CASE  " + 
					"              WHEN pi.having_child = 'Y'  " + 
					"              THEN  " + 
					"                 'http://www.speedmall.co.kr/mall/locator.jsp?em=gy'  " + 
					"                 || sed.prd_cd  " + 
					"              ELSE  " + 
					"                 'http://www.speedmall.co.kr/mall/locator.jsp?em=gn'  " + 
					"                 || sed.prd_cd  " + 
					"           END  " + 
					"              AS pgurl, " + 
					"         CASE  " + 
					"          WHEN 50000 <  DECODE(pi.tax_yn, 'A', p_spd_speed.f_get_sell_pri ('no_login', sed.prd_cd, 'DOWN') * 1.1, p_spd_speed.f_get_sell_pri ('no_login', sed.prd_cd, 'DOWN') * 1.0 )  " +
					"          THEN  " + 
					"             '������'  " + 
					"          ELSE  " + 
					"             '2500'  " + 
					"         END " + 
					"              AS deliv,      " +
					"           p_spd_cata.f_get_prdimg (sed.prd_cd, 1) AS igurl,  " + 
					"           'N' AS BILLYN,  " + 
					"           sei.event_memo AS PCARD,  " + 
					"           pi.make_co_nm AS MAKER,  " + 
					"             (SELECT grp_nm " + 
					"                FROM SPD_CATE_GRP " + 
					"               WHERE grp_cd = (SELECT grp_cd " + 
					"                                 FROM cust_cata_cate " + 
					"                                WHERE cate_cd = ccc.cate_lev2)) " + 
					"           || '_'  " + 
					"           || (SELECT cate_nm  " + 
					"                 FROM cust_cata_cate  " + 
					"                WHERE cate_cd = ccc.cate_lev2)  " + 
					"           || '_'  " + 
					"           || (SELECT cate_nm  " + 
					"                 FROM cust_cata_cate  " + 
					"                WHERE cate_cd = ccc.cate_lev3)  " + 
					"           || '_'  " + 
					"           || (SELECT cate_nm  " + 
					"                 FROM cust_cata_cate  " + 
					"                WHERE cate_cd = ccc.cate_lev4)  " + 
					"              AS cate,  " + 
					"           '' AS CARDP,  " + 
					"           '' AS COUPO,  " + 
					"           0 AS MPRICE,  " + 
					"           '' AS DDELIV,  " + 
					"           'N' AS INSTALLYN,  " + 
					"           '' AS CLASS,  " + 
					"           SYSDATE AS create_date,  " + 
					"           '' AS update_date,  " + 
					"           NVL2 (p_spd_cata.F_GET_PRD_OPTION_STOCK (pi.prd_cd, 'CODE'), 'N', 'Y')  " + 
					"              AS STOCK,  " + 
					"           '' AS MEMO  " + 
					"      FROM speed_event_info  sei,  " + 
					"           speed_event_dtl  sed,  " + 
					"           prd_info  pi,  " + 
					"           cust_cata_cate  ccc,  " + 
					"           cust_cata_dtl  ccd  " + 
					"     WHERE     sei.event_cd = '2016092801'  " + 
					"           AND sei.event_cd = sed.event_cd  " + 
					"           AND sei.stat_flag = '001'  " + 
					"           AND sed.stat_flag = '001'  " + 
					"           AND sed.prd_cd = pi.prd_cd  " + 
					"           AND ccd.cata_num = '0001'  " + 
					"           AND ccd.dp_yn = 'Y'  " + 
					"           AND ccd.prd_cd = pi.prd_cd  " + 
					"           AND ccc.CATA_NUM = '0001'  " + 
					"           AND ccc.dp_yn = 'Y'  " + 
					"           AND ccc.cate_lev2 IS NOT NULL  " + 
					"           AND ccc.cata_num = ccd.cata_num  " + 
					"           AND ccc.dp_yn = ccd.dp_yn  " + 
					"           AND ccd.stat_flag = '001'  " + 
					"           AND ccc.cate_cd = ccd.cate_cd  " + 
					"  ORDER BY sed.prd_cd "; 
					
	 		res = 0;
		 	smt = conn.createStatement();
			res = smt.executeUpdate(qry);
			
			if(res > 0) {
				conn.commit();
			} else {
				System.out.println(sMSG + " ����");
				logWrite(sMSG + " ����");
				throw new SQLException(sMSG + " ����");
			}
			smt.close();
			logWrite(sMSG);
			System.out.println(sMSG);
			
//			3. �ӽ����̺� Select ������  list(map)�� ����
			sMSG = "3.���� ������  select";
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();	
			String qry = "";	
			qry =   " SELECT A.MAPID, " + 
					"        B.MAPID MAPID_2, " +
					"        A.PNAME, " + 
					"        B.PNAME PNAME_2, " + 
					"        A.PRICE, " + 
					"        B.PRICE PRICE_2, " + 
					"        A.PGURL, " + 
					"        B.PGURL PGURL_2, " +
					
					"        A.DELIV, " + 
					"        B.DELIV DELIV_2, " +
					
					"        A.IGURL, " + 
					"        B.IGURL IGURL_2, " +
					
					"        A.BILLYN, " + 
					"        B.BILLYN BILLYN_2, " +
					
					"        A.PCARD, " + 
					"        B.PCARD PCARD_2, " +
					
					"        A.MAKER, " + 
					"        B.MAKER MAKER_2, " +
					
					"        A.CATE, " + 
					"        B.CATE CATE_2, " +
					
					"        A.CARDP, " + 
					"        B.CARDP CARDP_2, " +
					
					"        A.COUPO, " + 
					"        B.COUPO COUPO_2, " +
					
					"        A.MPRICE, " + 
					"        B.MPRICE MPRICE_2, " +
					
					"        A.DDELIV, " + 
					"        B.DDELIV DDELIV_2, " +
					
					"        A.INSTALLYN, " + 
					"        B.INSTALLYN INSTALLYN_2, " +
					
					"        A.STOCK, " + 
					"        B.STOCK STOCK_2, " +
					 
					"        B.CLASS, " +
					"        B.MEMO " + 
					"   FROM    (SELECT * " + 
					"              FROM SPEED_ENURI_TEMP) A " + 
					"        FULL OUTER JOIN " + 
					"           (SELECT * " + 
					"              FROM SPEED_ENURI_EP) B " +
					"        ON A.mapid = B.mapid " +
					"   ORDER BY mapid ";
				
			smt = conn.createStatement();
			rs = smt.executeQuery(qry);
				
			while(rs.next()) {
				HashMap<String, String> hMap= new HashMap<String, String>();
				hMap.put("MAPID", 	checkNull(rs.getString("MAPID")));
				hMap.put("MAPID_2", checkNull(rs.getString("MAPID_2")));
				hMap.put("PNAME", 	checkNull(rs.getString("PNAME")));
				hMap.put("PNAME_2", checkNull(rs.getString("PNAME_2")));
				hMap.put("PRICE", 	checkNull(rs.getString("PRICE")));
				hMap.put("PRICE_2", checkNull(rs.getString("PRICE_2")));
				hMap.put("PGURL", 	checkNull(rs.getString("PGURL")));
				hMap.put("PGURL_2", checkNull(rs.getString("PGURL_2")));
				hMap.put("DELIV", 	checkNull(rs.getString("DELIV")));
				hMap.put("DELIV_2", checkNull(rs.getString("DELIV_2")));
				hMap.put("IGURL", 	checkNull(rs.getString("IGURL")));
				hMap.put("IGURL_2", 	checkNull(rs.getString("IGURL_2")));
				hMap.put("BILLYN", 	checkNull(rs.getString("BILLYN")));
				hMap.put("BILLYN_2", checkNull(rs.getString("BILLYN_2")));
				hMap.put("PCARD", 	checkNull(rs.getString("PCARD")));
				hMap.put("PCARD_2", checkNull(rs.getString("PCARD_2")));
				hMap.put("MAKER", 	checkNull(rs.getString("MAKER")));
				hMap.put("MAKER_2", checkNull(rs.getString("MAKER_2")));
				hMap.put("CATE", 	checkNull(rs.getString("CATE")));				
				hMap.put("CATE_2", checkNull(rs.getString("CATE_2")));
				hMap.put("CARDP", 	checkNull(rs.getString("CARDP")));
				hMap.put("CARDP_2", checkNull(rs.getString("CARDP_2")));
				hMap.put("COUPO", 	checkNull(rs.getString("COUPO")));
				hMap.put("COUPO_2", checkNull(rs.getString("COUPO_2")));
				hMap.put("MPRICE", 	checkNull(rs.getString("MPRICE")));
				hMap.put("MPRICE_2", checkNull(rs.getString("MPRICE_2")));
				hMap.put("DDELIV", 	checkNull(rs.getString("DDELIV")));
				hMap.put("DDELIV_2", checkNull(rs.getString("DDELIV_2")));
				hMap.put("INSTALLYN", 	checkNull(rs.getString("INSTALLYN")));
				hMap.put("INSTALLYN_2",	checkNull(rs.getString("INSTALLYN_2")));
				hMap.put("STOCK", 	checkNull(rs.getString("STOCK")));
				hMap.put("STOCK_2", checkNull(rs.getString("STOCK_2")));
				hMap.put("CLASS", 	checkNull(rs.getString("CLASS")));
				hMap.put("MEMO", 	checkNull(rs.getString("MEMO")));
				
				list.add(hMap);
			}
			smt.close();
			logWrite(sMSG);
			System.out.println(sMSG);
			
			sMSG = "4.EP Update_yn �ʱ�ȭ";
			qry = 	" UPDATE SPEED_ENURI_EP SET UPDATE_YN = 'N' ";
			res = 0;
			smt = conn.createStatement();
			res = smt.executeUpdate(qry);
			
			if(res > 0) {
				conn.commit();
			}
			smt.close();
			logWrite(sMSG);
			System.out.println(sMSG);
			
			for(int i=0; i<list.size(); i++){
				HashMap<String, String> hMap = list.get(i);
				c_flag = hMap.get("CLASS");
				// 5-1. ������̺� ��ǰ ���
				if(!hMap.get("MAPID").equals("") && hMap.get("MAPID_2").equals("") && !hMap.get("STOCK").equals("N")){
					ps.setString(1,hMap.get("MAPID"));
					ps.setString(2,hMap.get("PNAME"));
					ps.setString(3,hMap.get("PRICE"));
					ps.setString(4,hMap.get("PGURL"));
					ps.setString(5,hMap.get("DELIV"));
					ps.setString(6,hMap.get("IGURL"));
					ps.setString(7,hMap.get("BILLYN"));
					ps.setString(8,hMap.get("PCARD"));
					ps.setString(9,hMap.get("MAKER"));
					ps.setString(10,hMap.get("CATE"));
					ps.setString(11,hMap.get("CARDP"));
					ps.setString(12,hMap.get("COUPO"));
					ps.setString(13,hMap.get("MPRICE"));
					ps.setString(14,hMap.get("DDELIV"));
					ps.setString(15,hMap.get("INSTALLYN"));
					ps.setString(16,hMap.get("STOCK"));
					
					if(ps.executeUpdate() > 0) {
						hMap.put("CLASS", "I");
						hMap.put("MEMO", "�ű�");
						epHistInsert(hMap);						
					} else {
						System.out.println(sMSG1+ " ����");
						logWrite(sMSG1+ " ����");
						throw new SQLException(sMSG1 + " ����");
					}
				// 5-2. ������̺� ��ǰ ����
				} else if(hMap.get("MAPID").equals("") && !hMap.get("MAPID_2").equals("") && !c_flag.equals("D")){
				
					ps2.setString(1,hMap.get("MAPID_2"));
					if(ps2.executeUpdate() > 0) {
						hMap.put("MAPID", 		hMap.get("MAPID_2"));
						hMap.put("PNAME", 		hMap.get("PNAME_2"));
						hMap.put("PRICE", 		hMap.get("PRICE_2"));
						hMap.put("PGURL", 		hMap.get("PGURL_2"));
						hMap.put("DELIV", 		hMap.get("DELIV_2"));
						hMap.put("IGURL", 		hMap.get("IGURL_2"));
						hMap.put("BILLYN", 		hMap.get("BILLYN_2"));
						hMap.put("PCARD", 		hMap.get("PCARD_2"));
						hMap.put("MAKER", 		hMap.get("MAKER_2"));
						hMap.put("CATE", 		hMap.get("CATE_2"));
						hMap.put("CARDP", 		hMap.get("CARDP_2"));
						hMap.put("COUPO", 		hMap.get("COUPO_2"));
						hMap.put("MPRICE", 		hMap.get("MPRICE_2"));
						hMap.put("DDELIV", 		hMap.get("DDELIV_2"));
						hMap.put("INSTALLYN", 	hMap.get("INSTALLYN_2"));
						hMap.put("CLASS", "D");
						hMap.put("MEMO", "����");
						epHistInsert(hMap);
					} else {
						System.out.println(sMSG2 + " ����");
						logWrite(sMSG2 + " ����");
						throw new SQLException(sMSG2 + " ����");
					}
				
				// 5-3. ������̺� ���� �� ��ǰ ������Ʈ
				}else {
					String Column = "";
					String Columns[] = null;
					String cclass = "U";
					qry = "UPDATE SPEED_ENURI_EP SET ";
					
					// ǰ����ǰ UPDATE
					if(hMap.get("STOCK").equals("N") && hMap.get("STOCK_2").equals("Y")){
						cclass = "N";
						Column += "STOCK,";
					//ǰ��������ǰ UPDATE
					} else if(hMap.get("STOCK").equals("Y") && hMap.get("STOCK_2").equals("N")){
						cclass = "Y";
						Column += "STOCK,";
					}
					
					//�����ǰ UPDATE
					if(!hMap.get("PNAME").equals(hMap.get("PNAME_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "PNAME,";
					}
					if(!hMap.get("PRICE").equals(hMap.get("PRICE_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "PRICE,";
					}
					if(!hMap.get("PGURL").equals(hMap.get("PGURL_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "PGURL,";
					}
					if(!hMap.get("DELIV").equals(hMap.get("DELIV_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "DELIV,";
					}
					if(!hMap.get("IGURL").equals(hMap.get("IGURL_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "IGURL,";
					}
					if(!hMap.get("PCARD").equals(hMap.get("PCARD_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "PCARD,";
					}
					if(!hMap.get("MAKER").equals(hMap.get("MAKER_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "MAKER,";
					}
					if(!hMap.get("CATE").equals(hMap.get("CATE_2")) && (!hMap.get("CLASS").equals("D") && !hMap.get("CLASS").equals("")) ){
						Column += "CATE,";
					}		
					//���ǵ�� ������ǰ ���� �� �ٽ� �־��� ���
					if(hMap.get("STOCK").equals("Y") && hMap.get("CLASS").equals("D") ){
						cclass = "I";
						Column += "MAPID,";
					}
					
					if(!Column.equals("")){
						Column = Column.substring(0, Column.length()-1);
						Columns = Column.split(",");
						for(int k=0; k<Columns.length; k++){
							qry += Columns[k] + " = " + "'"+hMap.get(Columns[k])+"', ";
						}
						qry += " CLASS = ?, UPDATE_DATE = SYSDATE, UPDATE_YN = 'Y', MEMO = ? WHERE MAPID = ? ";
						
						ps5 = conn.prepareStatement(qry);
						ps5.setString(1, cclass);
						ps5.setString(2, "����("+Column+")");
						ps5.setString(3, hMap.get("MAPID"));
						if(ps5.executeUpdate() > 0) {
							hMap.put("CLASS", cclass);
							hMap.put("MEMO", "����("+Column+")");
							epHistInsert(hMap);
						} else {
							System.out.println(sMSG3 + " ����");
							logWrite(sMSG3 + " ����");
							throw new SQLException(sMSG3 + " ����");
						}
						ps5.close();
					}
				}
			}// for ����
			logWrite("5.insert,update,delete complite ");
			System.out.println("5.insert,update,delete complite ");
			conn.commit();
			
			sMSG = "6.������ Txt ���� ����";
			int cnt = 0;
			qry = 	" SELECT COUNT(*) CNT FROM SPEED_ENURI_EP WHERE UPDATE_YN = 'Y' ";			
			smt = conn.createStatement();
			rs = smt.executeQuery(qry);
			if(rs.next()) {
				cnt = rs.getInt("CNT");
			}
			if(cnt > 0) {
				br = new BufferedWriter(new FileWriter(dir+txt, false));
				qry =	" SELECT '0' AS MAPID, '��ǰ�ڵ�<|>��ǰ��<|>����<|>URL<|>���<|>IMGURL<|>��꼭<|>������<|>������<|>�з�<|>ī�����ΰ�<|>����<|>����ϰ���<|>�����ۺ�<|>��ġ��' AS enuri " +
								"   FROM DUAL " + 
								" UNION ALL " +
								" SELECT    MAPID, MAPID " +
								"        || '<|>' " + 
								"        || PNAME " + 
								"        || '<|>' " + 
								"        || PRICE " + 
								"        || '<|>' " + 
								"        || PGURL " + 
								"        || '<|>' " + 
								"        || DELIV " + 
								"        || '<|>' " + 
								"        || IGURL " + 
								"        || '<|>' " + 
								"        || 'N' " +
								"        || '<|>' " +
								"        || PCARD " + 
								"        || '<|>' " +
								"        || MAKER " + 
								"        || '<|>' " + 
								"        || CATE " + 
								"        || '<|>' " + 
								"        || '' " + 
								"        || '<|>' " + 
								"        || '' " + 
								"        || '<|>' " +
								"        || '' " + 
								"        || '<|>' " +
								"        || '' " + 
								"        || '<|>' " + 
								"        || '' " +
								"           AS ENURI " + 
								"   FROM Speed_ENURI_EP " +
								"  WHERE CLASS <> 'D' AND STOCK <> 'N' " + 
								"   ORDER BY MAPID ";
				
					
						smt = conn.createStatement();
						rs = smt.executeQuery(qry);

						while(rs.next()) {
							try {
								br.write(rs.getString("ENURI")+"\r\n");
								br.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						rs.close();
						smt.close();
						br.close();
					
					
				logWrite(sMSG);
				System.out.println(sMSG);
			}
			conn.commit();
			
			qry =   ""+
			"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
			"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_enuri_if_sgkang','END',SYSDATE, 'OK', NULL ) "+					
			"";
			ps = conn.prepareStatement(qry);
			ps.executeUpdate();
			conn.commit();
			
		}catch(Exception e) {
			conn.rollback();
			logWrite("start catch : "+sMSG + " ����:"+e.getMessage());
			System.out.println("start catch : "+sMSG + " ����:"+e.getMessage());
			qry =   ""+
					"  INSERT INTO demon_list_15(reg_dati, prog_nm, memo, reg_day, status , err_msg)  "+
					"  VALUES (TO_CHAR(SYSDATE,'YYYYMMDD'),'Demon_enuri_if_sgkang','END',SYSDATE, 'ERR', '"+sMSG+" ����:"+e.getMessage()+"') "+
					"";
			System.out.println("qry::"+qry);
			ps = conn.prepareStatement(qry);
			ps.executeUpdate();
			conn.commit();
			
			qry = "{call P_SCO.SP_SEND_SCO_MAIL(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    		
			cs = conn.prepareCall(qry);
			
			int pSeq = 1;
			cs.setString(pSeq++, "1");  
			cs.setString(pSeq++, "2");  
			cs.setString(pSeq++, "ENURI IF ���� �˸� �����Դϴ�.");  
			cs.setString(pSeq++, "ENURI IF ���� �����帳�ϴ�. ������ Ȯ�����ֽñ� �ٶ��ϴ�.");  
			cs.setString(pSeq++, "��������$$##^^"+sMSG+" ����:"+e.getMessage());  
			cs.setString(pSeq++, "S0667472");
			cs.setString(pSeq++, "S0667472");  
			cs.setString(pSeq++, "000");  
			cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_RTN      
			cs.registerOutParameter(pSeq++, java.sql.Types.VARCHAR);		//R_MSG
			cs.execute();	
			
			String sReturn = cs.getString(9);
			String sMessage = cs.getString(10);
			conn.commit();
			
			//SMS ����
			qry =   ""+
					" INSERT INTO EM_TRAN                                                                                       "+
					" (TRAN_PR,TRAN_PHONE ,TRAN_CALLBACK ,TRAN_STATUS ,TRAN_DATE ,TRAN_MSG ,TRAN_ETC1 ,TRAN_ETC2 ,TRAN_ETC3)    "+
					" SELECT EM_TRAN_PR.NEXTVAL  , A.MCOM_NUM  ,'1644-5644', 1 , SYSDATE  , '"+sMSG+" ����:"+e.getMessage()+"'"+ ", 'REGENPRI', 'REGSCO' , ''  "+
					"   FROM CUST_DTL A                                                                                         "+
					"  WHERE 1=1                                                                                                "+
					"    AND A.CUST_ID IN ('S0667472')                                                                          "+
					"";
			ps = conn.prepareStatement(qry);	
			ps.executeUpdate();
			conn.commit();
		}finally {
			DB_DisConn();
			logWrite("7.end finally ");
			System.out.println("7.end finally ");
		}
	}
	
	public void epHistInsert(HashMap<String, String> hMap){
		
		try {
			ps3.setString(1, hMap.get("MAPID"));
			rs3 = ps3.executeQuery();
			if(rs3.next()) {
				MAPSEQ = rs3.getString("MAPSEQ");
			}
			
			ps4.setString(1,hMap.get("MAPID"));
			ps4.setString(2,MAPSEQ);
			ps4.setString(3,hMap.get("PNAME"));
			ps4.setString(4,hMap.get("PRICE"));
			ps4.setString(5,hMap.get("PGURL"));
			ps4.setString(6,hMap.get("DELIV"));
			ps4.setString(7,hMap.get("IGURL"));
			ps4.setString(8,hMap.get("BILLYN"));
			ps4.setString(9,hMap.get("PCARD"));
			ps4.setString(10,hMap.get("MAKER"));
			
			ps4.setString(11,hMap.get("CATE"));			
			ps4.setString(12,hMap.get("CARDP"));
			ps4.setString(13,hMap.get("COUPO"));
			ps4.setString(14,hMap.get("MPRICE"));
			ps4.setString(15,hMap.get("DDELIV"));
			ps4.setString(16,hMap.get("INSTALLYN"));
			ps4.setString(17,hMap.get("CLASS"));
			ps4.setString(18,hMap.get("STOCK"));
			ps4.setString(19,hMap.get("MEMO"));
			
			if(ps4.executeUpdate() < 0) {
				System.out.println("epHistInsert ERR:"+hMap.get("MAPID"));
				logWrite("epHistInsert ERR:"+hMap.get("MAPID"));
			}
		} catch (SQLException e) {
			System.out.println("epHistInsert catch : "+ e.getMessage());
			logWrite("epHistInsert catch : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static String checkNull(String s) {
		if (s == null){
			return "";
		}else{
			return s.trim();
		}
	}
	
	// ȭ���� �����ϸ� ����ȭ�Ͽ� �α׸� �߰�, �׷��� ������ ���ο� ȭ���� �߰����� �α׸� �ִ´�.
	private static void logWrite(String s) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		today = sdf.format(new java.util.Date());
		filename = "EnuriEP_"+today+".log";

		SimpleDateFormat sdf2 = new SimpleDateFormat("H:mm:ss");
		logTime = "[" + sdf2.format(new java.util.Date()) + "]" ;

		File file1 = new File(log_dir+filename);  //���ϰ�ü�� �����Ҷ� �α����ϸ����� ����

		if(file1.exists()){                   // ������ �ý��ۿ� �ִ��� Ȯ��
			isExist = true;
		}

		FileWriter file = null;
		try{
			file = new FileWriter(log_dir + filename, isExist);

			file.write(logTime + s +"\r\n");
			file.flush();
			file.close();
		}
		catch (Exception e)	{
			logWrite("logWrite ERR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		logWrite("[ENURI IF START]");
		System.out.println("[ENURI IF START]");
		Demon_enuri_if eif = new Demon_enuri_if();
		try{
			eif.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		logWrite("[ENURI IF END]");
		System.out.println("[ENURI IF END]");
	}
}

