import java.sql.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.JVMRandom;
import org.apache.xerces.impl.dv.util.Base64;

import com.jcraft.jsch.*;

import sun.misc.BASE64Encoder;

import comm.mk_log;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import oracle.sql.BLOB;

public class HR_TO_GW3 {

	static private mk_log log = new mk_log();

	static final String DB_URL="jdbc:oracle:thin:@172.16.3.32:1521:orcl";
	static final String USER="NAON";
	static final String PASS="NAON0103";

	//static final String DB_URL_GW = "jdbc:mysql://172.16.3.10:3306/naongw";	//운영
	static final String DB_URL_GW = "jdbc:mysql://172.16.3.14:3306/naongw";	//Test
	static final String USER_GW = "ekpnaon_happy";
	static final String PASS_GW = "skdhs!1";

	static final String svUsr = "naongw";
	// static final String svHost = "172.16.3.10";			//운영 서버
	static final String svHost = "172.16.3.14";				//Test 서버
	static final int svPort = 22;
	static final String svPw = "21044833";

	static Connection hrConn = null;
	static Connection gwConn = null;

	static final String baseUrl = "/home/naon/gw/webapps/groupware1/upload/body";

	static JSch jsch = null;
	static Session session = null;
	static Channel channel = null;
	static ChannelSftp sftpChannel = null;
	static SftpATTRS attrs = null;

	public static void main(String[] args) throws SQLException {
		System.out.println("Start....");
		log.Write("HR_TO_GW	---->		", "");

		HR_TO_GW3 htg = new HR_TO_GW3();

		try {
			System.out.println("---------------- Start ----------------");
			log.Write("HR_TO_GW	---->\t", "############################### START ###############################");

			hrConn = getHrConn();
			gwConn = getGwConn();

			/**  직위 동기화 처리 Start */
			System.out.println( "Step 1 ");
			log.Write("HR_TO_GW	---->\t", "############################### Step 1 ###############################");
			List<Map<String,String>> hrPosition = htg.getHrPoistionInfo();	//HR로 부터 직위 정보를 가져온다.
			htg.insPositionMapping(hrPosition);								//HR로 부터 가져온 직위 정보를 GW 내 직위 정보 매핑 테이블에 DEL 후 INS 한다.
			htg.insPositionMappingGw();										//GW내 저장된 Position 정보를 매핑테이블에 넣는다.
			htg.syncPosition_HR_GW_Mapping();								//매핑 테이블 내 Positon 정보가 없는값음 생성 시킨다.
			// 직위 동기화 처리 End

			// 직책 동기화 처리 Start
			System.out.println( "Step 2 ");
			log.Write("HR_TO_GW	---->\t", "############################### Step 2 ###############################");
			List<Map<String,String>> hrOffice = htg.getHrOfficeInfo();	//HR로 부터 직책 정보를 가져온다.
			htg.insOfficeMapping(hrOffice);								//HR로 부터 가져온 직책 정보를 GW 내 직책 정보 매핑 테이블에 DEL 후 INS 한다.
			htg.insOfficeMappingGw();									//GW내 저장된 Office 정보를 매핑테이블에 넣는다.
			htg.syncOffice_HR_GW_Mapping();								//매핑 테이블 내 Office 정보가 없는값음 생성 시킨다.
			// 직책 동기화 처리 End

			// 조직 동기화 처리 Start
			System.out.println( "Step 3 ");
			log.Write("HR_TO_GW	---->\t", "############################### Step 3 ###############################");
			List<Map<String,String>> hrOrg = htg.getHrOrg();			//HR로 부터 조직 정보를 가져온다.
			htg.insOrgMapping(hrOrg);									//HR로 부터 가져온 조직 정보를 GW 내 조직 정보 매핑 테이블에 DEL 후 INS 한다.
			htg.insOrgMappingGw();										//GW내 저장된 조직 정보를 매핑테이블에 넣는다.
			htg.syncOrg_HR_GW_Mapping();								//매핑 테이블 내 조직 정보가 없는값음 생성 시킨다.
			// 조직 동기화 처리 End

			//직원 정보 동기화 처리 Start
			System.out.println( "Step 4 ");
			log.Write("HR_TO_GW	---->\t", "############################### Step 4 ###############################");
			List<Map<String, String>> hrEmp = htg.getHrEmployeeList();	//HR로 부터 직원 정보를 가져온다.
			htg.insEmpMapping(hrEmp);									//HR로 부터 가져온 직원 정보를 GW 내 직원 정보 매핑 테이블에 DEL 후 INS 한다.
			htg.insEmpMappingGw();										//GW내 저장된 직원 정보를 매핑테이블에 넣는다.
			htg.syncEmp_HR_GW_Mapping();								//매핑 테이블 내 직원 정보가 없는값음 생성 시킨다.
			//직원 정보 동기화 처리 End

			// 사원 - 부서 매핑 정보 처리 Start
			System.out.println( "Step 5 ");
			log.Write("HR_TO_GW	---->\t", "############################### Step 5 ###############################");
			List<Map<String, String>> hrJob = htg.getHrJobList();		//HR로 부터 부서 - 사원 매핑 정보를 가져온다.
			System.out.println( hrJob );
			//htg.insJobMapping(hrJob);
			//htg.insJobMappingGw();
			htg.syncJob_HR_GW_Mapping(hrJob);


			// 사진 동기화 처리
			System.out.println( "Step 6 ");
			log.Write("HR_TO_GW	---->\t", "############################### Step 6 ###############################");
			htg.syncEmpPhoto();

			System.out.println("---------------- End ----------------");
			log.Write("HR_TO_GW	---->\t", "############################### END ###############################");

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if( hrConn != null ) hrConn.close();
			if( gwConn != null ) gwConn.close();

		}
	}

	private void syncJob_HR_GW_Mapping(List<Map<String, String>> hrJob)  throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		Map<String, String> getMp = new HashMap<String, String>();

		String sql1="SELECT EMP_ID, PSN_ID, CMP_ID FROM EMPLOYEE  WHERE EMP_CODE = ? AND EMP_STATUS = 'W' ";
		String sql2="SELECT DEPT_ID, SEQ FROM HR_GW_DEPARTMENT  WHERE ORG_CD = ? AND NOW() BETWEEN STR_TO_DATE(STA_YMD, '%Y%m%d') AND STR_TO_DATE(END_YMD, '%Y%m%d' ) AND USE_YN = 'Y' ";
		String sql3="SELECT OFC_ID FROM HR_GW_OFFICE  WHERE HR_CD = ? AND NOW() BETWEEN STR_TO_DATE(STA_YMD, '%Y%m%d') AND STR_TO_DATE(END_YMD, '%Y%m%d' )";
		String sql4="SELECT count(*) FROM MY_JOB WHERE EMP_ID = ? AND DEPT_ID = ? AND PSN_ID = ? AND CMP_ID = ?  AND DEFAULT_YN = 'Y' AND USE_YN = 'Y' ";
		String sql4_1="UPDATE MY_JOB SET OFC_ID = ( SELECT X.OFC_ID FROM HR_GW_OFFICE X WHERE X.HR_CD = ? ) WHERE EMP_ID = ? AND DEPT_ID = ? AND PSN_ID = ? AND CMP_ID = ?  AND DEFAULT_YN = 'Y' AND USE_YN = 'Y' AND OFC_ID <> ( SELECT X.OFC_ID FROM HR_GW_OFFICE X WHERE X.HR_CD = ? )  ";
		String sql5="INSERT INTO MY_JOB (EMP_ID, DEPT_ID, PSN_ID, CMP_ID, OFC_ID, DEPT_ORDER, DEFAULT_YN, USE_YN ) VALUES (?,?,?,?,?,?,'Y','Y')";
		String sql5_1="DELETE FROM MY_JOB WHERE EMP_ID = ? AND DEPT_ID <> ? AND PSN_ID = ? AND CMP_ID = ?   ";
		String sql6="SELECT count(*) FROM MY_JOB WHERE EMP_ID = ? AND DEPT_ID = 'DISASSIGN_C300151214' AND PSN_ID = ? AND CMP_ID = ? AND DEFAULT_YN = 'Y' AND USE_YN = 'Y' ";
		String sql7="INSERT INTO MY_JOB (EMP_ID, DEPT_ID, PSN_ID, CMP_ID, OFC_ID, DEPT_ORDER, DEFAULT_YN, USE_YN ) VALUES (?,'DISASSIGN_C300151214',?,?,?,?,'Y','Y') ";
		String sql7_1="DELETE FROM MY_JOB WHERE EMP_ID = ? AND DEPT_ID <> 'DISASSIGN_C300151214' AND PSN_ID = ? AND CMP_ID = ?   ";
		String sql8="UPDATE MY_JOB SET DEFAULT_YN = 'N', USE_YN = 'N', DEPT_ID = 'DELETE_C300151214' WHERE EMP_ID = ? AND PSN_ID = ? AND CMP_ID = ?  ";
		String sql9="DELETE FROM MY_JOB WHERE ( EMP_ID, PSN_ID, CMP_ID ) IN ( SELECT EMP_ID, PSN_ID, CMP_ID  FROM EMPLOYEE WHERE EMP_CODE = ? AND  EMP_STATUS = 'R' ) ";
		String sql10="INSERT INTO MY_JOB (EMP_ID, DEPT_ID, PSN_ID, CMP_ID, OFC_ID, DEPT_ORDER, DEFAULT_YN, USE_YN ) SELECT EMP_ID, 'DELETE_C300151214', PSN_ID, CMP_ID, ? OFC_ID , 0 DEPT_ORDER, 'Y' DEFAULT_YN, 'Y'  USE_YN FROM EMPLOYEE WHERE EMP_CODE = ? AND EMP_STATUS = 'R' ";

		try {
			for(int i=0;i<hrJob.size();i++) {
				getMp = (Map<String,String>) hrJob.get(i);

				String empNo = getMp.get("EMP_NO");
				String orgCd = getMp.get("ORG_CD");
				String inOffiYn = getMp.get("IN_OFFI_YN");
				String dutyCd = getMp.get("DUTY_CD");

//				System.out.println( "empNo : "+ empNo + "\t orgCd:" +orgCd + "\t inOffiYn:"+  inOffiYn+ "\t dutyCd:"+  dutyCd );

				int pSeq1= 1;
				pstmt1 = gwConn.prepareStatement(sql1);
				pstmt1.setString(pSeq1++, empNo);

				rs1 = pstmt1.executeQuery();

				String empId="", psnId="", cmpId="", deptId="", seq="", ofcId="";

				if( rs1.next() ) {
					empId = nullToSpace( rs1.getString("EMP_ID") );
					psnId = rs1.getString("PSN_ID");
					cmpId = rs1.getString("CMP_ID");
				}

				if(rs1!=null) rs1.close();
				if(pstmt1!=null) pstmt1.close();

				int pSeq2= 1;
				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(pSeq2++, orgCd);

				rs2 = pstmt2.executeQuery();

				if( rs2.next() ) {
					deptId =  nullToSpace( rs2.getString("DEPT_ID") ) ;
					seq = rs2.getString("SEQ");
				}

				if(rs2!=null) rs2.close();
				if(pstmt2!=null) pstmt2.close();

				int pSeq3= 1;
				pstmt3 = gwConn.prepareStatement(sql3);
				pstmt3.setString(pSeq3++, dutyCd);

				rs3 = pstmt3.executeQuery();

				if( rs3.next() ) {
					ofcId = rs3.getString("OFC_ID");
				}

				if(rs3!=null) rs3.close();
				if(pstmt3!=null) pstmt3.close();

				System.out.println( "empNo : "+ empNo + "\t orgCd:" +orgCd + "\t inOffiYn:"+  inOffiYn+ "\t dutyCd:"+  dutyCd + "\t empId : "+ empId + "\t psnId:" +psnId + "\t cmpId:"+  cmpId+ "\t deptId:"+  deptId+ "\t seq:"+  seq+ "\t ofcId:"+  ofcId );


				if("Y".equals(inOffiYn)) {//재직자

					if( !"".equals(deptId) ) { // 부서지정자
						int pSeq4=1;
						pstmt4 = gwConn.prepareStatement(sql4);
						pstmt4.setString(pSeq4++, empId);
						pstmt4.setString(pSeq4++, deptId);
						pstmt4.setString(pSeq4++, psnId);
						pstmt4.setString(pSeq4++, cmpId);
						//pstmt4.setString(pSeq4++, ofcId);
						//pstmt4.setString(pSeq4++, seq);

						rs4 = pstmt4.executeQuery();

						int isExist = 0;

						if(rs4.next()) isExist = rs4.getInt(1);

						if(rs4!=null) rs4.close();
						if(pstmt4!=null) pstmt4.close();

						if(isExist == 0 ) {// my_job 테이블에 자료가 없으면 넣는다.
							int pSeq5=1;
							pstmt5 = gwConn.prepareStatement(sql5);
							pstmt5.setString(pSeq5++, empId);
							pstmt5.setString(pSeq5++, deptId);
							pstmt5.setString(pSeq5++, psnId);
							pstmt5.setString(pSeq5++, cmpId);
							pstmt5.setString(pSeq5++, ofcId);
							pstmt5.setString(pSeq5++, seq);

							if( pstmt5.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB INS ERR -- 1.1");

							if(pstmt5!=null) pstmt5.close();
						}else {
							int pSeq4_1=1;
							pstmt4 = gwConn.prepareStatement(sql4_1);
							pstmt4.setString(pSeq4_1++, dutyCd);
							pstmt4.setString(pSeq4_1++, empId);
							pstmt4.setString(pSeq4_1++, deptId);
							pstmt4.setString(pSeq4_1++, psnId);
							pstmt4.setString(pSeq4_1++, cmpId);
							pstmt4.setString(pSeq4_1++, dutyCd);

							if( pstmt4.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB UPD ERR -- 1.1");

							if(pstmt4!=null) pstmt4.close();
						}

						int pSeq5=1;
						pstmt5 = gwConn.prepareStatement(sql5_1);
						pstmt5.setString(pSeq5++, empId);
						pstmt5.setString(pSeq5++, deptId);
						pstmt5.setString(pSeq5++, psnId);
						pstmt5.setString(pSeq5++, cmpId);

						if( pstmt5.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB DEL ERR -- 1.1");

						if(pstmt5!=null) pstmt5.close();

					}else { // 부서미지정자
						int pSeq6=1;
						pstmt6 = gwConn.prepareStatement(sql6);
						pstmt6.setString(pSeq6++, empId);
						pstmt6.setString(pSeq6++, psnId);
						pstmt6.setString(pSeq6++, cmpId);

						rs6 = pstmt6.executeQuery();

						int isExist = 0;

						if(rs6.next()) isExist = rs6.getInt(1);

						if(rs6!=null) rs6.close();
						if(pstmt6!=null) pstmt6.close();

						if(isExist == 0 ) {// my_job 테이블에 자료가 없으면 넣는다.
							int pSeq7=1;
							pstmt7 = gwConn.prepareStatement(sql7);
							pstmt7.setString(pSeq7++, empId);
							pstmt7.setString(pSeq7++, psnId);
							pstmt7.setString(pSeq7++, cmpId);
							pstmt7.setString(pSeq7++, ofcId);
							pstmt7.setString(pSeq7++, "0");

							if( pstmt7.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB INS ERR -- 1.2");

							if(pstmt7!=null) pstmt7.close();

						}

						int pSeq7=1;
						pstmt7 = gwConn.prepareStatement(sql7_1);
						pstmt7.setString(pSeq7++, empId);
						pstmt7.setString(pSeq7++, psnId);
						pstmt7.setString(pSeq7++, cmpId);

						if( pstmt7.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB DEL ERR -- 1.2");

						if(pstmt7!=null) pstmt7.close();

					}

				}else { //퇴직자
					//System.out.println( "empNo : "+ empNo + "\t orgCd:" +orgCd + "\t inOffiYn:"+  inOffiYn+ "\t dutyCd:"+  dutyCd + "\t empId : "+ empId + "\t psnId:" +psnId + "\t cmpId:"+  cmpId+ "\t deptId:"+  deptId+ "\t seq:"+  seq+ "\t ofcId:"+  ofcId );

					if( "".equals(empId) ) { // 그룹웨어에 없는 건
						int pSeq9=1;
						pstmt9 = gwConn.prepareStatement(sql9);
						pstmt9.setString(pSeq9++, empNo);

						if( pstmt9.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB DEL ERR -- 2.1");
						if(pstmt9!=null) pstmt9.close();

						int pSeq10=1;
						pstmt10 = gwConn.prepareStatement(sql10);
						pstmt10.setString(pSeq10++, ofcId);
						pstmt10.setString(pSeq10++, empNo);

						if( pstmt10.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB UPD ERR -- 2.2");
						if(pstmt10!=null) pstmt10.close();


					}else { // 그룹웨어에 있으면 삭제 처리
						int pSeq8=1;
						pstmt8 = gwConn.prepareStatement(sql8);
						pstmt8.setString(pSeq8++, empId);
						pstmt8.setString(pSeq8++, psnId);
						pstmt8.setString(pSeq8++, cmpId);

						if( pstmt8.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB INS ERR -- 1.1");

						if(pstmt8!=null) pstmt8.close();
					}
				}
			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void syncEmpPhoto() throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1="SELECT EMP_NO, IMG_DATA FROM VI_INF_PHM_EMP WHERE IN_OFFI_YN = 'Y' ORDER BY 1  ";
		String sql2="SELECT PSN_ID FROM EMPLOYEE WHERE EMP_CODE = ? AND EMP_STATUS = 'W'  ";
		String sql3="SELECT PHOTO FROM PERSON WHERE PSN_ID = ? ";
		String sql4="UPDATE PERSON SET PHOTO = ? WHERE PSN_ID = ? ";

		String tempFileLoc = "";
		String sprt = System.getProperty("file.separator");

		if( System.getProperty("os.name").indexOf("Windows") >= 0) {
			tempFileLoc="D:\\temp\\hrPhoto"+sprt;
		}else {
			tempFileLoc="/user/home/mro"+sprt;
		}

		try {
			pstmt1 = hrConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			gwSvConn();

			while(rs1.next()) {
				String empNo = rs1.getString("EMP_NO");
				BLOB imgData=(BLOB) rs1.getBlob("IMG_DATA");

				//System.out.println(empNo);

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, empNo);
				rs2=pstmt2.executeQuery();

				String psnId="";

				if(rs2.next()) psnId= rs2.getString(1);

				System.out.println(empNo + "\t"+ psnId);

				pstmt3 = gwConn.prepareStatement(sql3);
				pstmt3.setString(1, psnId);
				rs3=pstmt3.executeQuery();

				String photoUrl="";
				if(rs3.next()) photoUrl= nullToSpace( rs3.getString("PHOTO") );

				//System.out.println( isSameFile(imgData, photoUrl ) );

				String ext="";

				try {
					InputStream is1 = imgData.getBinaryStream(); // 파일 확장자 판단 용
					InputStream dbIs = imgData.getBinaryStream();

					Magic parser = new Magic();
					byte[] fData = new byte[1024];
					is1.read(fData);

					MagicMatch match = new MagicMatch();
					try {
						match = parser.getMagicMatch(fData);
						ext = match.getMimeType();
					}catch(NoClassDefFoundError e) {
						System.out.println("e.getMessage --> " + e.getMessage());
						ext = "";
					}

					if( "image/jpeg".equals(ext) && !"".equals(psnId) ) {

						if( !isSameFile(imgData, photoUrl ) ) {
							FileOutputStream fos = new FileOutputStream(tempFileLoc + empNo + ".jpg");

							int fr = 0;
							while( (fr=dbIs.read()) != -1){
								fos.write(fr);
							}

							fos.close();

							String remoteFileNm = String.valueOf( System.currentTimeMillis() ) + "_" +  empNo + ".jpg";
							Calendar cal = Calendar.getInstance();
							String sttYear = String.valueOf( cal.get( Calendar.YEAR ) );
							String sttMonth = String.format("%02d", (cal.get( Calendar.MONTH  ) + 1 ) )  ;
							String sttdate = String.format("%02d", (cal.get( Calendar.DATE  ) ) );

							if( upLoad( tempFileLoc + empNo + ".jpg", baseUrl+"/profile/"+sttYear+"/"+sttMonth+"/"+sttdate, remoteFileNm ) ) { // 임시저장소 위치, 리모트Dir, (저장할) 리모트 파일명
								pstmt4 = gwConn.prepareStatement(sql4);
								pstmt4.setString(1, "/profile"+"/"+sttYear+"/"+sttMonth+"/"+sttdate+"/"+remoteFileNm);
								pstmt4.setString(2, psnId);
								System.out.println( "/profile"+"/"+sttYear+"/"+sttMonth+"/"+sttdate+"/"+remoteFileNm );
								System.out.println( psnId );

								if( pstmt4.executeUpdate() < 0 ) throw new Exception(">>> PERSON PHOTO name UPD PROC ERR -- 2");

								File delFil = new File(tempFileLoc + empNo + ".jpg");
								if( delFil.delete() ) {
									System.out.println("File in Temp Location is Deleted.. ");
								}
							}

							if(!"".equals(photoUrl)) {
								System.out.println("##############################################################");
								String tmpLoc = (baseUrl + photoUrl).substring( 0, (baseUrl + photoUrl).lastIndexOf("/")  ).replaceAll("//", "/");
								String svFileNm = (baseUrl + photoUrl).substring( (baseUrl + photoUrl).lastIndexOf("/") + 1 );

								removeOldPhoto( tmpLoc, svFileNm);
								System.out.println( tmpLoc );
								System.out.println( svFileNm);

								System.out.println("##############################################################");
							}
						}
					}

					is1.close();
					dbIs.close();

					if(rs2!=null) rs2.close();
					if(rs3!=null) rs3.close();
					if(pstmt2!=null) pstmt2.close();
					if(pstmt3!=null) pstmt3.close();

				}catch(Exception e) {
					System.out.println(e.getMessage() + " --> \t"+ empNo + "\t"+ psnId);
				}
			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();

			gwSvDisConn();
		}
	}

	private void syncJob_HR_GW_Mapping() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		StringBuffer sb1 = new StringBuffer();
		sb1.append("SELECT EMP_NO, ORG_CD, DUTY_CD, DEFAULT_YN, STA_YMD, END_YMD, EMP_ID, PSN_ID, OFC_ID, DEPT_ID, DEPT_ORDER FROM HR_GW_MYJOB	").append("\r\n");
		sb1.append("WHERE EMP_ID IS NOT NULL																									").append("\r\n");
		sb1.append("AND NOW() BETWEEN STR_TO_DATE(STA_YMD,'%Y%m%d') AND STR_TO_DATE(END_YMD, '%Y%m%d')											").append("\r\n");
		String sql1=sb1.toString();

		String sql2="SELECT count(*) FROM MY_JOB WHERE EMP_ID = ? AND DEPT_ID = ? AND PSN_ID = ? AND OFC_ID = ? ";
		String sql3="INSERT INTO MY_JOB (EMP_ID, DEPT_ID, PSN_ID, CMP_ID, OFC_ID, DEPT_ORDER, DEFAULT_YN, USE_YN ) VALUES (?,?,?,'C300151214',?,?,?,'Y') ";
		String sql3_1="UPDATE MY_JOB SET DEFAULT_YN = ? WHERE  EMP_ID = ? AND DEPT_ID = ? AND PSN_ID = ? AND CMP_ID = 'C300151214' AND OFC_ID = ? ";

		String sql4="SELECT count(*) FROM MY_JOB WHERE EMP_ID = ? AND DEPT_ID = 'DISASSIGN_C300151214' AND PSN_ID = ? AND OFC_ID = ? AND DEFAULT_YN = ? AND USE_YN = 'Y' ";
		String sql5="INSERT INTO MY_JOB (EMP_ID, DEPT_ID, PSN_ID, CMP_ID, OFC_ID, DEPT_ORDER, DEFAULT_YN, USE_YN ) VALUES (?,'DISASSIGN_C300151214',?,'C300151214',?,?,?,'Y') ";

		StringBuffer sb6 = new StringBuffer();
		sb6.append("UPDATE MY_JOB A 																											                        ").append("\r\n");
		sb6.append("SET DEPT_ID = 'DELETE_C300151214', USE_YN = 'N', DEFAULT_YN = 'N', OFC_ID = '0'												                        ").append("\r\n");
		sb6.append("WHERE DEPT_ID NOT IN ( 'DELETE_C300151214', 'DISASSIGN_C300151214' )														                        ").append("\r\n");
		sb6.append("AND NOT EXISTS ( SELECT 'X' FROM HR_GW_MYJOB X WHERE X.EMP_ID= A.EMP_ID AND X.PSN_ID = A.PSN_ID AND X.OFC_ID = A.OFC_ID AND X.DEPT_ID = A.DEPT_ID AND NOW() BETWEEN STR_TO_DATE(STA_YMD,'%Y%m%d') AND STR_TO_DATE(END_YMD, '%Y%m%d') )	").append("\r\n");
		String sql6=sb6.toString();

		// 퇴사자는 삭제 처리 플래그로 업데이트한다.
		StringBuffer sb6_1 = new StringBuffer();
		sb6_1.append("UPDATE MY_JOB																																		").append("\r\n");
		sb6_1.append("SET DEPT_ID = 'DELETE_C300151214', DEFAULT_YN = 'N', USE_YN = 'Y'																					").append("\r\n");
		sb6_1.append("WHERE MYJ_ID IN (																																	").append("\r\n");
		sb6_1.append("  SELECT * FROM (																																	").append("\r\n");
		sb6_1.append("    SELECT MYJ_ID FROM MY_JOB A																													").append("\r\n");
		sb6_1.append("    WHERE  DEPT_ID NOT IN ( 'DELETE_C300151214', 'DISASSIGN_C300151214' )																			").append("\r\n");
		sb6_1.append("    AND DEFAULT_YN = 'Y'																															").append("\r\n");
		sb6_1.append("    AND USE_YN = 'Y'																																").append("\r\n");
		sb6_1.append("    AND EXISTS ( SELECT 'X' FROM EMPLOYEE X WHERE X.EMP_ID = A.EMP_ID AND X.PSN_ID = A.PSN_ID AND X.EMP_STATUS = 'R')								").append("\r\n");
		sb6_1.append("  ) B 																																			").append("\r\n");
		sb6_1.append(") 																																				").append("\r\n");
		String sql6_1=sb6_1.toString();

		// 부서 지정인데 기본 값이 설정 안되어지면 기본으로 설정한다.
		StringBuffer sb7 = new StringBuffer();
		sb7.append("UPDATE MY_JOB																").append("\r\n");
		sb7.append("SET MY_JOB.DEFAULT_YN = 'Y'													").append("\r\n");
		sb7.append("WHERE MYJ_ID IN (															").append("\r\n");
		sb7.append("  SELECT * FROM (															").append("\r\n");
		sb7.append("  SELECT A.MYJ_ID FROM MY_JOB A												").append("\r\n");
		sb7.append("  WHERE DEFAULT_YN = 'N'													").append("\r\n");
		sb7.append("  AND DEPT_ID NOT IN ( 'DELETE_C300151214', 'DISASSIGN_C300151214' )		").append("\r\n");
		sb7.append("  AND NOT EXISTS ( SELECT 'X' FROM MY_JOB X WHERE X.EMP_ID = A.EMP_ID AND X.PSN_ID = A.PSN_ID AND X.DEFAULT_YN = 'Y' AND X.DEPT_ID NOT IN ( 'DELETE_C300151214', 'DISASSIGN_C300151214' ) AND X.USE_YN = 'Y'	 ) 						").append("\r\n");
		sb7.append("  ) A																		").append("\r\n");
		sb7.append(") 																			").append("\r\n");
		String sql7=sb7.toString();

		// 부서 지정되었는데 미지정된 건이 있다면 삭제 한다.
		StringBuffer sb8 = new StringBuffer();
		sb8.append("DELETE FROM MY_JOB															").append("\r\n");
		sb8.append("WHERE MYJ_ID IN (															").append("\r\n");
		sb8.append("  SELECT * FROM (															").append("\r\n");
		sb8.append("    SELECT MYJ_ID FROM MY_JOB a												").append("\r\n");
		sb8.append("    WHERE DEPT_ID = 'DISASSIGN_C300151214'									").append("\r\n");
		sb8.append("    AND USE_YN = 'Y'														").append("\r\n");
		sb8.append("    AND EXISTS ( SELECT 'X' FROM MY_JOB X WHERE X.EMP_ID = A.EMP_ID AND X.PSN_ID = A.PSN_ID AND X.DEFAULT_YN = 'Y' AND X.DEPT_ID NOT IN ( 'DELETE_C300151214', 'DISASSIGN_C300151214' ) AND X.USE_YN = 'Y'	 ) 						").append("\r\n");
		sb8.append("  ) B																		").append("\r\n");
		sb8.append(")																			").append("\r\n");
		String sql8=sb8.toString();

		// 미지정이 2개 이상인 사람은 한개 빼고 다 삭제 한다.
		StringBuffer sb9 = new StringBuffer();
		sb9.append("DELETE FROM MY_JOB															").append("\r\n");
		sb9.append("WHERE ( EMP_ID, PSN_ID ) IN (												").append("\r\n");
		sb9.append("  SELECT * FROM (															").append("\r\n");
		sb9.append("    SELECT EMP_ID, PSN_ID													").append("\r\n");
		sb9.append("    FROM MY_JOB A															").append("\r\n");
		sb9.append("    WHERE 1=1 																").append("\r\n");
		sb9.append("    AND DEPT_ID = 'DISASSIGN_C300151214'									").append("\r\n");
		sb9.append("    AND NOT EXISTS ( SELECT 'X' FROM DEPARTMENT X WHERE X.DEPT_ID NOT IN ( 'DISASSIGN_C300151214', 'DELETE_C300151214' ) AND X.DEPT_STATUS = 'U' AND X.DEPT_ID = A.DEPT_ID )				").append("\r\n");
		sb9.append("    GROUP BY EMP_ID, PSN_ID													").append("\r\n");
		sb9.append("    HAVING COUNT(*) > 1														").append("\r\n");
		sb9.append("  ) B																		").append("\r\n");
		sb9.append(")																			").append("\r\n");
		sb9.append("AND DEPT_ID = 'DISASSIGN_C300151214'										").append("\r\n");
		sb9.append("AND DEFAULT_YN = 'N'														").append("\r\n");
		sb9.append("AND USE_YN = 'Y'															").append("\r\n");
		String sql9=sb9.toString();

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String empNo = rs1.getString("EMP_NO");
				String orgCd = rs1.getString("ORG_CD");
				String dutyCd = rs1.getString("DUTY_CD");
				String defaultYn = rs1.getString("DEFAULT_YN");
				String staYmd = rs1.getString("STA_YMD");
				String endYmd = rs1.getString("END_YMD");
				String empId = rs1.getString("EMP_ID");
				String psnId = rs1.getString("PSN_ID");
				String ofcId = rs1.getString("OFC_ID");
				String deptId = rs1.getString("DEPT_ID");
				String deptOrder = rs1.getString("DEPT_ORDER");

				System.out.println( empNo +"\t"+ orgCd +"\t"+dutyCd +"\t"+ defaultYn+"\t"+staYmd +"\t"+endYmd +"\t"+empId +"\t"+psnId +"\t"+ofcId +"\t"+ deptId + "\t" + deptOrder);

				if( deptId == null ) { // 부서값이 없으면 미지정으로 인식 시킨다.
					int pSeq4=1;
					pstmt4 = gwConn.prepareStatement(sql4);
					pstmt4.setString(pSeq4++, empId);
					pstmt4.setString(pSeq4++, psnId);
					pstmt4.setString(pSeq4++, ofcId);
					pstmt4.setString(pSeq4++, defaultYn);

					rs4 = pstmt4.executeQuery();

					int isExists=0;
					if(rs4.next()) isExists = rs4.getInt(1);

					if(isExists == 0) {
						int pSeq5=1;
						pstmt5 = gwConn.prepareStatement(sql5);
						pstmt5.setString(pSeq5++, empId);
						pstmt5.setString(pSeq5++, psnId);
						pstmt5.setString(pSeq5++, ofcId);
						pstmt5.setString(pSeq5++, "1");
						pstmt5.setString(pSeq5++, defaultYn);

						if( pstmt5.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB INS ERR -- 1.1");
					}

					if(rs4!=null) rs4.close();
					if(pstmt4!=null) pstmt4.close();
					if(pstmt5!=null) pstmt5.close();

				}else {
					int pSeq2=1;
					pstmt2 = gwConn.prepareStatement(sql2);
					pstmt2.setString(pSeq2++, empId);
					pstmt2.setString(pSeq2++, deptId);
					pstmt2.setString(pSeq2++, psnId);
					pstmt2.setString(pSeq2++, ofcId);

					rs2 = pstmt2.executeQuery();

					int isExists=0;
					if(rs2.next()) isExists = rs2.getInt(1);

					if(isExists == 0) {
						int pSeq3=1;
						pstmt3 = gwConn.prepareStatement(sql3);
						pstmt3.setString(pSeq3++, empId);
						pstmt3.setString(pSeq3++, deptId);
						pstmt3.setString(pSeq3++, psnId);
						pstmt3.setString(pSeq3++, ofcId);
						pstmt3.setString(pSeq3++, deptOrder);
						pstmt3.setString(pSeq3++, defaultYn);

						if( pstmt3.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB INS ERR -- 1.2");
					}else {
						int pSeq3=1;
						pstmt3 = gwConn.prepareStatement(sql3_1);
						pstmt3.setString(pSeq3++, defaultYn);
						pstmt3.setString(pSeq3++, empId);
						pstmt3.setString(pSeq3++, deptId);
						pstmt3.setString(pSeq3++, psnId);
						pstmt3.setString(pSeq3++, ofcId);

						if( pstmt3.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB UPD ERR -- 1.3");
					}

					if(rs2!=null) rs2.close();
					if(pstmt2!=null) pstmt2.close();
					if(pstmt3!=null) pstmt3.close();
				}
			}

			pstmt6 = gwConn.prepareStatement(sql6);
			if( pstmt6.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB UPD ERR -- 2");
			if(pstmt6!=null) pstmt6.close();

			pstmt6 = gwConn.prepareStatement(sql6_1);
			if( pstmt6.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB UPD ERR -- 3");

			pstmt7 = gwConn.prepareStatement(sql7);
			if( pstmt7.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB UPD ERR -- 4");

			pstmt8 = gwConn.prepareStatement(sql8);
			if( pstmt8.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB DEL ERR -- 5");

			pstmt9 = gwConn.prepareStatement(sql9);
			if( pstmt9.executeUpdate() < 0 ) throw new Exception(">>> MY_JOB DEL ERR -- 6");

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insJobMappingGw() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		StringBuffer sb1 = new StringBuffer();
		sb1.append("UPDATE HR_GW_MYJOB A	").append("\r\n");
		sb1.append("SET EMP_ID = ( SELECT X.EMP_ID FROM HR_GW_EMPLOYEE X WHERE X.EMP_NO = A.EMP_NO AND X.EMP_ID IS NOT NULL )	").append("\r\n");
		sb1.append("  , PSN_ID = ( SELECT Y.PSN_ID FROM HR_GW_EMPLOYEE X, EMPLOYEE Y WHERE X.EMP_NO = A.EMP_NO AND X.EMP_ID IS NOT NULL AND X.EMP_ID = Y.EMP_ID AND A.EMP_NO = Y.EMP_CODE )	").append("\r\n");
		sb1.append("  , OFC_ID = ( SELECT X.OFC_ID FROM HR_GW_OFFICE X WHERE X.HR_CD = A.DUTY_CD AND NOW() BETWEEN STR_TO_DATE(X.STA_YMD, '%Y%m%d') AND STR_TO_DATE(X.END_YMD, '%Y%m%d' ) ) 							").append("\r\n");
		sb1.append("  , DEPT_ID = ( SELECT X.DEPT_ID FROM HR_GW_DEPARTMENT X WHERE X.ORG_CD = A.ORG_CD AND NOW() BETWEEN STR_TO_DATE(X.STA_YMD, '%Y%m%d') AND STR_TO_DATE(X.END_YMD, '%Y%m%d' ) AND X.USE_YN = 'Y' )	").append("\r\n");
		sb1.append("  , DEPT_ORDER = ( SELECT X.SEQ FROM HR_GW_DEPARTMENT X WHERE X.ORG_CD = A.ORG_CD AND NOW() BETWEEN STR_TO_DATE(X.STA_YMD, '%Y%m%d') AND STR_TO_DATE(X.END_YMD, '%Y%m%d' ) AND X.USE_YN = 'Y' )	").append("\r\n");

		String sql1 = sb1.toString();

		try {
			pstmt1 = gwConn.prepareStatement(sql1);

			if( pstmt1.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_MYJOB UPD ERR -- 1");

			gwConn.commit();
		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insJobMapping(List<Map<String, String>> hrJob) throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		Map<String, String> getMp = new HashMap<String, String>();

		String sql1 = "DELETE FROM HR_GW_MYJOB  ";
		String sql2 = "INSERT INTO HR_GW_MYJOB ( EMP_NO, ORG_CD, DUTY_CD, DEFAULT_YN, STA_YMD, END_YMD ) VALUES ( ?, ?, ?, ?, ?, ? ) ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			if( pstmt1.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_MYJOB DEL - Err ");

			for(int i=0;i<hrJob.size();i++) {
				getMp = (Map<String,String>) hrJob.get(i);

				String empNo = getMp.get("EMP_NO");
				String orgCd = getMp.get("ORG_CD");
				String dutyCd = getMp.get("DUTY_CD");
				String defaultYn = getMp.get("DEFAULT_YN");
				String staYmd = getMp.get("STA_YMD");
				String endYmd = getMp.get("END_YMD");

				int pSeq = 1;
				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(pSeq++, empNo);
				pstmt2.setString(pSeq++, orgCd);
				pstmt2.setString(pSeq++, dutyCd);
				pstmt2.setString(pSeq++, defaultYn);
				pstmt2.setString(pSeq++, staYmd);
				pstmt2.setString(pSeq++, endYmd);

				if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_MYJOB INS - Err");

				if(pstmt2!=null) pstmt2.close();
			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

	}

	private List<Map<String, String>> getHrJobList() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuffer sqlSb = new StringBuffer();

		try {
			sqlSb.append("SELECT EMP_NO, ORG_CD, IN_OFFI_YN, DUTY_CD   ").append("\r\n");
			sqlSb.append("FROM HR_GW_EMPLOYEE                                               	").append("\r\n");
			sqlSb.append("WHERE 1=1																").append("\r\n");
			//sqlSb.append("AND SYSDATE BETWEEN STA_YMD AND END_YMD 								").append("\r\n");
			//sqlSb.append("AND EXISTS ( SELECT 'x' FROM VI_INF_ORM_ORG X WHERE X.ORG_CD = A.ORG_CD AND SYSDATE BETWEEN X.STA_YMD AND X.END_YMD )		").append("\r\n");
			//sqlSb.append("AND EXISTS ( SELECT 'x' FROM VI_INF_PHM_EMP X WHERE X.EMP_NO = A.EMP_NO AND X.IN_OFFI_YN = 'Y' ) 							").append("\r\n");
			sqlSb.append("ORDER BY 1															").append("\r\n");

			pstmt1 = gwConn.prepareStatement(sqlSb.toString());

			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				Map<String, String> mp = new HashMap<String, String>();

				mp.put("EMP_NO", rs1.getString("EMP_NO") );
				mp.put("ORG_CD", rs1.getString("ORG_CD") );
				mp.put("IN_OFFI_YN", rs1.getString("IN_OFFI_YN") );
				mp.put("DUTY_CD", rs1.getString("DUTY_CD") );

				list.add(mp);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}


		return list;
	}

	private void syncEmp_HR_GW_Mapping() throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		// 매핑정보결과를 가져온다. ( 이메일 있는건에 대해서만 )
		StringBuffer sb1 = new StringBuffer();
		sb1.append("SELECT EMP_NO, EMP_NM, EMP_FOR_NM1, CTZ_NO, ORG_LOC_CD, ORG_CD, ( SELECT X.POS_ID FROM HR_GW_POSITION X WHERE X.HR_CD = A.POS_CD ) POS_CD, DUTY_CD, GENDER_CD, HIRE_YMD, RETIRE_YMD, IN_OFFI_YN, BIRTH_YMD, SOLAR_TYPE, EMP_KIND_CD, EMAIL, HOME_NO, COMTEL_NO, MOBILE_NO, ADDR, EMP_ID FROM HR_GW_EMPLOYEE A ").append("\r\n");
		sb1.append("WHERE 1=1 ").append("\r\n");
		//sb1.append("AND EMP_ID IS NULL ").append("\r\n");
		sb1.append("AND IN_OFFI_YN = 'Y' ").append("\r\n");
		sb1.append("AND A.POS_CD NOT IN ( '16','17','18' ) ").append("\r\n");
		//sb1.append("AND IFNULL( EMAIL, '' ) NOT IN ( '' ) ").append("\r\n");
		String sql1 = sb1.toString();

		// 매핑정보에서 GW의 emp_id 의 값이 없는 건은 INSERT 한다.
		StringBuffer sb2 = new StringBuffer();
		sb2.append("INSERT INTO EMPLOYEE ( EMP_ID, PSN_ID, CMP_ID, USER_ID, EMP_NAME, EMP_EN_NAME, PASSWORD, EMP_CODE, POS_ID, SEC_LEVEL, PHONE, MOBILE, EMAIL, EMP_CHARGE, EMP_STATUS, ERROR_CNT, DENY_YN, REG_DATE, PASSWORD_DATE, EMP_KN, EMP_LOC, SK_EMAIL, DUTY_ID, MAIL_LICENSE_TYPE, EMP_LEVEL, CMP_EMAIL ) ").append("\r\n");
		sb2.append("VALUES ( ?,  ?, 'C300151214', ?, ?, ?, ?, ?, ?, '10', ?, ?, ?, '', 'W', 0, 'N', NOW(), NOW(), ?, ?, ?, 0, 'G', 10, ? ) ").append("\r\n");
		String sql2 = sb2.toString();

		// 매핑정보에서 퇴직 건으로 표기 된건에 대해서는 삭제 마킹 처리 한다.
		StringBuffer sb3 = new StringBuffer();
		sb3.append("UPDATE EMPLOYEE ").append("\r\n");
		sb3.append("SET EMP_STATUS = 'R' ").append("\r\n");
		sb3.append("WHERE EMP_ID = ? AND EMP_CODE = ? AND EMP_STATUS = 'W' AND EMP_CODE NOT IN ('123456789') ").append("\r\n");
		String sql3= sb3.toString();

		// 매핑정보에서 직위, 이메일, 핸드폰, 사무실 번호가 다른건에 대해서는 갱신 처리 한다.
		StringBuffer sb4 = new StringBuffer();
		sb4.append("UPDATE EMPLOYEE ").append("\r\n");
		sb4.append("SET POS_ID = ?, PHONE = ?, EMAIL = ?, MOBILE = ?, CMP_EMAIL = ?, SK_EMAIL = ? ").append("\r\n");
		sb4.append("WHERE EMP_ID = ? AND EMP_CODE = ? AND EMP_STATUS = 'W' ").append("\r\n");
		sb4.append("AND ( ( IFNULL( POS_ID, '')  <> ? ) OR ( IFNULL( PHONE, '')  <> ? ) OR ( IFNULL( EMAIL, '')  <> ? ) OR (IFNULL(MOBILE,'') <> ? ) OR ( IFNULL(CMP_EMAIL,'') <> ? ) OR ( IFNULL( SK_EMAIL, '') <> ? ) ) ").append("\r\n");
		String sql4 = sb4.toString();

		StringBuffer sb5 = new StringBuffer();
		sb5.append(" INSERT INTO PERSON ( PSN_ID, EMP_NAME, LIC_NO, BIRTH, SOLAR_YN, PSN_INFO_COLOR, MOBILE, METS_LICENSE, OFFICE_LICENSE, SKCARD_LICENSE ) ").append("\r\n");
		sb5.append(" VALUES (?, ?, ?, ?, ?, '000000', ?, '001', '001', '001' ) ").append("\r\n");
		String sql5 = sb5.toString();

		StringBuffer sb6 = new StringBuffer();
		sb6.append("UPDATE PERSON ").append("\r\n");
		sb6.append("SET BIRTH = ?, SOLAR_YN = ?, MOBILE = ?, LIC_NO = ? ").append("\r\n");
		sb6.append("WHERE PSN_ID IN ( ").append("\r\n");
		sb6.append("  SELECT PSN_ID FROM EMPLOYEE WHERE EMP_ID = ? AND EMP_CODE = ?  ").append("\r\n");
		sb6.append(")				  ").append("\r\n");
		sb6.append("AND ( ( IFNULL( BIRTH, '') <> ? ) OR ( IFNULL( SOLAR_YN, '') <> ? ) OR ( IFNULL( MOBILE, '') <> ? ) OR ( IFNULL( LIC_NO, '') <> ? ) )  ").append("\r\n");
		String sql6 = sb6.toString();

		String sql7 = " UPDATE HR_GW_EMPLOYEE SET EMP_ID = ? WHERE EMP_NO = ? ";

		String sql8 = " UPDATE EMPLOYEE A SET EMP_STATUS = 'R' WHERE NOT EXISTS ( SELECT 'X' FROM HR_GW_EMPLOYEE X WHERE X.EMP_ID = A.EMP_ID ) AND EMP_STATUS = 'W' AND EMP_CODE NOT IN ('123456789') " ;

		HR_TO_GW3 htg = new HR_TO_GW3();

		try {
			pstmt1 = gwConn.prepareStatement( sql1 );
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String empNo = rs1.getString("EMP_NO");
				String empNm = rs1.getString("EMP_NM");
				String empForNm1 = rs1.getString("EMP_FOR_NM1");
				String ctzNo = rs1.getString("CTZ_NO");
				String orgLocCd = rs1.getString("ORG_LOC_CD");
				String posCd = rs1.getString("POS_CD");
				String genderCd = rs1.getString("GENDER_CD");
				String hireYmd = rs1.getString("HIRE_YMD");
				String retireYmd = rs1.getString("RETIRE_YMD");
				String inOffiYn = rs1.getString("IN_OFFI_YN");
				String birthYmd = rs1.getString("BIRTH_YMD");
				String solarType = rs1.getString("SOLAR_TYPE");
				String empKindCd = rs1.getString("EMP_KIND_CD");
				String email = rs1.getString("EMAIL");
				String comtelNo = rs1.getString("COMTEL_NO");
				String mobileNo = rs1.getString("MOBILE_NO");
				String empId = rs1.getString("EMP_ID");

				System.out.println( "empId --> " + empId );

				String psnId="";
				if( empId == null ) {// 매핑 정보가 없으면 GW에 EMPLOYEE 테이블에 Insert 한다.
					psnId = String.format("%09d", Long.parseLong( String.valueOf( JVMRandom.nextLong(999999999) ) ) );
					empId = "M"+psnId;

					System.out.println(empNo + "\t"+ empNm + "\t" + psnId + "\t" + empId+ "\t" + empId );

					int pSeq = 1;
					pstmt2 = gwConn.prepareStatement(sql2);
					pstmt2.setString(pSeq++, empId);
					pstmt2.setString(pSeq++, psnId);
					pstmt2.setString(pSeq++, empNo);
					pstmt2.setString(pSeq++, empNm);
					pstmt2.setString(pSeq++, empForNm1);
					pstmt2.setString(pSeq++, getEncryPassword(empNo) );
					pstmt2.setString(pSeq++, empNo );
					pstmt2.setString(pSeq++, posCd );
					pstmt2.setString(pSeq++, comtelNo );
					pstmt2.setString(pSeq++, mobileNo );
					pstmt2.setString(pSeq++, email );
					pstmt2.setString(pSeq++, null );
					pstmt2.setString(pSeq++, null );
					pstmt2.setString(pSeq++, email );
					pstmt2.setString(pSeq++, email );

					if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> EMPLOYEE INS ERR -- 1");
					if( pstmt2!=null ) pstmt2.close();

					pSeq = 1;

					pstmt5 = gwConn.prepareStatement(sql5);
					pstmt5.setString(pSeq++, psnId);
					pstmt5.setString(pSeq++, empNm);
					pstmt5.setString(pSeq++, ctzNo);
					pstmt5.setString(pSeq++, birthYmd.substring(0, 4) + "-" + birthYmd.substring(4, 6) + "-" + birthYmd.substring(6) );
					pstmt5.setString(pSeq++, "1".equals( solarType ) ? "S" : "L" );
					pstmt5.setString(pSeq++, mobileNo );

					if( pstmt5.executeUpdate() < 0 ) throw new Exception(">>> PERSON INS ERR -- 1");
					if( pstmt5!=null ) pstmt5.close();

					pSeq = 1;

					pstmt7 = gwConn.prepareStatement(sql7);
					pstmt7.setString(pSeq++, empId);
					pstmt7.setString(pSeq++, empNo);

					if( pstmt7.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_EMPLOYEE UPD ERR -- 1");
					if( pstmt7!=null ) pstmt7.close();

				}else {
					if("N".equals(inOffiYn)) { // 퇴직이면 마킹 처리 한다.
						int pSeq = 1;
						pstmt3 = gwConn.prepareStatement(sql3);
						pstmt3.setString(pSeq++, empId);
						pstmt3.setString(pSeq++, empNm);

						if( pstmt3.executeUpdate() < 0 ) throw new Exception(">>> EMPLOYEE UPD ERR -- 1");
						if( pstmt3!=null ) pstmt3.close();

					}else if("Y".equals(inOffiYn)){
						int pSeq = 1;
						pstmt4 = gwConn.prepareStatement(sql4);
						pstmt4.setString(pSeq++, posCd);
						pstmt4.setString(pSeq++, comtelNo);
						pstmt4.setString(pSeq++, email);
						pstmt4.setString(pSeq++, mobileNo);
						pstmt4.setString(pSeq++, email);
						pstmt4.setString(pSeq++, email);
						pstmt4.setString(pSeq++, empId);
						pstmt4.setString(pSeq++, empNo);
						pstmt4.setString(pSeq++, posCd);
						pstmt4.setString(pSeq++, comtelNo);
						pstmt4.setString(pSeq++, email);
						pstmt4.setString(pSeq++, mobileNo);
						pstmt4.setString(pSeq++, email);
						pstmt4.setString(pSeq++, email);

						if( pstmt4.executeUpdate() < 0 ) throw new Exception(">>> EMPLOYEE UPD ERR -- 2");
						if( pstmt4!=null ) pstmt4.close();

						pSeq = 1;
						pstmt6 = gwConn.prepareStatement(sql6);
						pstmt6.setString(pSeq++, birthYmd.substring(0, 4) + "-" + birthYmd.substring(4, 6) + "-" + birthYmd.substring(6));
						pstmt6.setString(pSeq++, "1".equals( solarType ) ? "S" : "L");
						pstmt6.setString(pSeq++, mobileNo);
						pstmt6.setString(pSeq++, ctzNo);
						pstmt6.setString(pSeq++, empId);
						pstmt6.setString(pSeq++, empNo);
						pstmt6.setString(pSeq++, birthYmd.substring(0, 4) + "-" + birthYmd.substring(4, 6) + "-" + birthYmd.substring(6));
						pstmt6.setString(pSeq++, "1".equals( solarType ) ? "S" : "L");
						pstmt6.setString(pSeq++, mobileNo);
						pstmt6.setString(pSeq++, ctzNo);

						if( pstmt6.executeUpdate() < 0 ) throw new Exception(">>> PERSON UPD ERR -- 2");
						if( pstmt6!=null ) pstmt6.close();
					}
				}
			}

			pstmt8 = gwConn.prepareStatement(sql8);
			if( pstmt8.executeUpdate() < 0 ) throw new Exception(">>> EMPLOYEE UPD - Err ");
			if( pstmt8!=null ) pstmt8.close();

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

	}

	private void insEmpMappingGw() throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1 = " SELECT EMP_CODE, EMP_ID FROM EMPLOYEE WHERE EMP_STATUS = 'W' AND EMP_CODE NOT IN ( '123456789' ) ";
		String sql2 = " SELECT COUNT(*) FROM HR_GW_EMPLOYEE WHERE EMP_NO = ?  AND IN_OFFI_YN = 'Y'  ";
		String sql3 = " UPDATE EMPLOYEE SET EMP_STATUS = 'R' WHERE EMP_ID = ? ";
		String sql4 = " UPDATE HR_GW_EMPLOYEE SET EMP_ID = CASE WHEN POS_CD in ( '16','17','18' ) THEN NULL ELSE ? END WHERE EMP_NO = ?  AND IN_OFFI_YN = 'Y'  ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String empCode = rs1.getString("EMP_CODE");
				String empId = rs1.getString("EMP_ID");

//				System.out.println(deptId +"\t" +deptNm);

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, empCode);
				rs2 = pstmt2.executeQuery();

				int isExists = 0;
				if( rs2.next() ) isExists = rs2.getInt(1);

				if( isExists == 0 ) { // 사번으로 검색시 없으면 삭제마킹 처리
					pstmt3 = gwConn.prepareStatement(sql3);
					pstmt3.setString(1, empCode);

					if( pstmt3.executeUpdate() < 0 ) throw new Exception(">>> EMPLOYEE UPD ERR -- 1");
				}else {
					pstmt4 = gwConn.prepareStatement(sql4);
					pstmt4.setString(1, empId);
					pstmt4.setString(2, empCode);

					if( pstmt4.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_EMPLOYEE UPD ERR -- 2");
				}

				if(pstmt4!=null) pstmt4.close();
				if(pstmt3!=null) pstmt3.close();
				if(pstmt2!=null) pstmt2.close();
				if(rs2!=null) rs2.close();
			}

			gwConn.commit();
		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insEmpMapping(List<Map<String, String>> hrEmp) throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		Map<String, String> getMp = new HashMap<String, String>();

		String sql1 = "DELETE FROM HR_GW_EMPLOYEE ";
		String sql2 = "INSERT INTO HR_GW_EMPLOYEE (EMP_NO, EMP_NM, EMP_FOR_NM1, CTZ_NO, ORG_LOC_CD, ORG_CD, POS_CD, DUTY_CD, GENDER_CD, HIRE_YMD, RETIRE_YMD, IN_OFFI_YN, BIRTH_YMD, SOLAR_TYPE, EMP_KIND_CD, EMAIL, HOME_NO, COMTEL_NO, MOBILE_NO, ADDR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			if(pstmt1.executeUpdate()<0) throw new Exception(">>> HR_GW_EMPLOYEE DEL - Err ");

			for(int i=0;i<hrEmp.size();i++) {
				getMp = (Map<String,String>) hrEmp.get(i);

				String empNo	  = nullToSpace( getMp.get("EMP_NO") );			   //System.out.println(" empNo --> " + empNo );
				String empNm     = nullToSpace( getMp.get("EMP_NM") );             //System.out.println(" empNm --> " + empNm );
				String empForNm1 = nullToSpace( getMp.get("EMP_FOR_NM1") );        //System.out.println(" empForNm1 --> " + empForNm1 );
				String ctzNo     = nullToSpace( getMp.get("CTZ_NO") );             //System.out.println(" ctzNo --> " + encode( ctzNo.substring(0, 7) ) );
				ctzNo	  		 = encode( ctzNo.substring(0, 7) ); 			   ///주민번호는 6 + 1 자리로 암호화 저장
				String orgLocCd  = nullToSpace( getMp.get("ORG_LOC_CD") );         //System.out.println(" orgLocCd --> " + orgLocCd );
				String orgCd     = nullToSpace( getMp.get("ORG_CD") );             //System.out.println(" orgCd --> " + orgCd );
				String posCd     = nullToSpace( getMp.get("POS_CD") );             //System.out.println(" posCd --> " + posCd );
				String dutyCd    = nullToSpace( getMp.get("DUTY_CD") );            //System.out.println(" dutyCd --> " + dutyCd );
				String genderCd  = nullToSpace( getMp.get("GENDER_CD") );          //System.out.println(" genderCd --> " + genderCd );
				String hireYmd   = nullToSpace( getMp.get("HIRE_YMD") );           //System.out.println(" hireYmd --> " + hireYmd );
				String retireYmd = nullToSpace( getMp.get("RETIRE_YMD") );         //System.out.println(" retireYmd --> " + retireYmd );
				String inOffiYn  = nullToSpace( getMp.get("IN_OFFI_YN") );         //System.out.println(" inOffiYn --> " + inOffiYn );
				String birthYmd  = nullToSpace( getMp.get("BIRTH_YMD") );          //System.out.println(" birthYmd --> " + birthYmd );
				String solarType  = nullToSpace( getMp.get("SOLAR_TYPE") );        //System.out.println(" solarType --> " + solarType );
				String empKindCd  = nullToSpace( getMp.get("EMP_KIND_CD") );       // System.out.println(" solarType --> " + solarType );
				String email     = nullToSpace( getMp.get("EMAIL") );              //System.out.println(" email --> " + email );
				String homeNo    = nullToSpace( getMp.get("HOME_NO") );            //System.out.println(" homeNo --> " + homeNo );
				String comtelNo  = nullToSpace( getMp.get("COMTEL_NO") );          //System.out.println(" comtelNo --> " + comtelNo );
				String mobileNo  = nullToSpace( getMp.get("MOBILE_NO") );          //System.out.println(" mobileNo --> " + mobileNo );
				String addr      = nullToSpace( getMp.get("ADDR") );               //System.out.println(" addr --> " + addr );

				int pSeq = 1;

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(pSeq++, empNo);
				pstmt2.setString(pSeq++, empNm);
				pstmt2.setString(pSeq++, empForNm1);
				pstmt2.setString(pSeq++, ctzNo);
				pstmt2.setString(pSeq++, orgLocCd);
				pstmt2.setString(pSeq++, orgCd);
				pstmt2.setString(pSeq++, posCd);
				pstmt2.setString(pSeq++, dutyCd);
				pstmt2.setString(pSeq++, genderCd);
				pstmt2.setString(pSeq++, hireYmd);
				pstmt2.setString(pSeq++, retireYmd);
				pstmt2.setString(pSeq++, inOffiYn);
				pstmt2.setString(pSeq++, birthYmd);
				pstmt2.setString(pSeq++, solarType);
				pstmt2.setString(pSeq++, empKindCd);
				pstmt2.setString(pSeq++, email);
				pstmt2.setString(pSeq++, homeNo);
				pstmt2.setString(pSeq++, comtelNo);
				pstmt2.setString(pSeq++, mobileNo);
				pstmt2.setString(pSeq++, addr);

				//-------------------------------------------------------------------------------------------
				// 추가 요청건 회신 후 처리 하기
				//-------------------------------------------------------------------------------------------

				if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_EMPLOYEE INS - Err");

				if(pstmt2!=null) pstmt2.close();

			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

	}

	private List<Map<String, String>> getHrEmployeeList() throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuffer sqlSb = new StringBuffer();

		try {
			sqlSb.append("SELECT EMP_NO, EMP_NM, EMP_FOR_NM1, CTZ_NO, ORG_LOC_CD, ORG_CD, POS_CD, DUTY_CD, GENDER_CD, TO_CHAR(HIRE_YMD,'yyyymmdd') HIRE_YMD, TO_CHAR(RETIRE_YMD,'yyyymmdd') RETIRE_YMD, IN_OFFI_YN, TO_CHAR(BIRTH_YMD, 'yyyymmdd') BIRTH_YMD, SOLAR_TYPE, EMP_KIND_CD, EMAIL, HOME_NO, COMTEL_NO, MOBILE_NO, ADDR	").append("\r\n");
			sqlSb.append("FROM VI_INF_PHM_EMP	").append("\r\n");
			sqlSb.append("WHERE 1=1	").append("\r\n");
			sqlSb.append("ORDER BY RETIRE_YMD NULLS FIRST, EMP_NO	").append("\r\n");

//			System.out.println( gwConn );

			pstmt1 = hrConn.prepareStatement(sqlSb.toString());

			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				Map<String,String> mp = new HashMap<String,String>();

				mp.put("EMP_NO", rs1.getString("EMP_NO") );
				mp.put("EMP_NM", rs1.getString("EMP_NM") );
				mp.put("EMP_FOR_NM1", rs1.getString("EMP_FOR_NM1") );
				mp.put("CTZ_NO", rs1.getString("CTZ_NO") );
				mp.put("ORG_LOC_CD", rs1.getString("ORG_LOC_CD") );
				mp.put("ORG_CD", rs1.getString("ORG_CD") );
				mp.put("POS_CD", rs1.getString("POS_CD") );
				mp.put("DUTY_CD", rs1.getString("DUTY_CD") );
				mp.put("GENDER_CD", rs1.getString("GENDER_CD") );
				mp.put("HIRE_YMD", rs1.getString("HIRE_YMD") );
				mp.put("RETIRE_YMD", rs1.getString("RETIRE_YMD") );
				mp.put("IN_OFFI_YN", rs1.getString("IN_OFFI_YN") );
				mp.put("BIRTH_YMD", rs1.getString("BIRTH_YMD") );
				mp.put("SOLAR_TYPE", rs1.getString("SOLAR_TYPE") );
				mp.put("EMP_KIND_CD", rs1.getString("EMP_KIND_CD") );
				mp.put("EMAIL", rs1.getString("EMAIL") );
				mp.put("HOME_NO", rs1.getString("HOME_NO") );
				mp.put("COMTEL_NO", rs1.getString("COMTEL_NO") );
				mp.put("MOBILE_NO", rs1.getString("MOBILE_NO") );
				mp.put("ADDR", rs1.getString("ADDR") );

				list.add(mp);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

		return list;
	}

	private void syncOrg_HR_GW_Mapping() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		// HR의 계층 정보를 가져온다.
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORG_CD, ORG_NM, NVL( SUPER_ORG_CD, '-' ) SUPER_ORG_CD ,SEQ, TO_CHAR( STA_YMD, 'yyyymmdd') STA_YMD, TO_CHAR( END_YMD, 'yyyymmdd') END_YMD, USE_YN, LEVEL LV	" ).append("\r\n");
		sb.append("FROM (	" ).append("\r\n");
		sb.append("    SELECT * FROM VI_INF_ORM_ORG	" ).append("\r\n");
		sb.append("	   WHERE USE_YN = 'Y'	" ).append("\r\n");
		sb.append("	   AND SYSDATE BETWEEN STA_YMD AND END_YMD " ).append("\r\n");
		sb.append(")	" ).append("\r\n");
		sb.append("START WITH SUPER_ORG_CD IS NULL	" ).append("\r\n");
		sb.append("CONNECT BY PRIOR ORG_CD = SUPER_ORG_CD	" ).append("\r\n");
		sb.append("ORDER SIBLINGS BY SEQ	" ).append("\r\n");

		String sql2 = " UPDATE HR_GW_DEPARTMENT SET LV = ? WHERE  ORG_CD=? AND ORG_NM=? AND SUPER_ORG_CD=? AND SEQ=? AND STA_YMD=? AND END_YMD=? AND USE_YN=? "; // 매핑 테이블의 계층 정보를 UPD
		String sql3 = " SELECT ORG_CD, ORG_NM, SEQ, LV FROM HR_GW_DEPARTMENT WHERE now() between str_to_date( STA_YMD, '%Y%m%d') and str_to_date( END_YMD, '%Y%m%d')  AND USE_YN = 'Y' AND DEPT_ID IS NULL "; // 매핑되지 않는건을 찾는다.
		String sql4 = " SELECT CONCAT( 'D', LPAD( IFNULL( SUBSTR( MAX( DEPT_ID ), 2) + 1, 1 ), 9, '0') )  FROM DEPARTMENT WHERE DEPT_ID NOT IN ('C300151214','ROOT') AND DEPT_STATUS IN ('D','U')  "; // DeptId 를 가져온다.

		// GW부서정보를 생성한다.
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" INSERT INTO DEPARTMENT	").append("\r\n");
		sb1.append(" (DEPT_ID, CMP_ID, DEPT_CD, UP_DEPT_IDX, DEPT_NAME, DEPT_EN_NAME, DEPT_ORDER, DEPT_STATUS, ");
		sb1.append("DEPT_COMMENT, REG_DATE, DEPT_LOC, DEPT_DEPTH, DOCFILING_DEPT_ID, DEPT_DOCFILING_USE_YN, DEPT_SHORT_NAME) 	").append("\r\n");
		sb1.append(" VALUES (?,'C300151214',NULL,'-',?,NULL,?,'U','',NOW(),'',?,'','N','')		").append("\r\n"); // ? DEPT_ID, ? DEPT_NAME, ? DEPT_ORDER (SEQ 값 ), ? DEPT_DEPTH (LV 값)

		// 매핑 테이블의 GW에서 생성한 부서 DEPT_ID를 넣는다.
		StringBuffer sb2 = new StringBuffer();
		sb2.append(" UPDATE HR_GW_DEPARTMENT A	").append("\r\n");
		sb2.append(" SET DEPT_ID = ( SELECT X.DEPT_ID FROM DEPARTMENT X WHERE X.DEPT_NAME = A.ORG_NM AND X.DEPT_STATUS = 'U' AND X.UP_DEPT_IDX = '-')	").append("\r\n");
		sb2.append(" WHERE 1=1	").append("\r\n");
		sb2.append(" AND now() between str_to_date( STA_YMD, '%Y%m%d') and str_to_date( END_YMD, '%Y%m%d')  AND USE_YN = 'Y' 	").append("\r\n");
		sb2.append(" AND DEPT_ID IS NULL	").append("\r\n");

		// GW에서 새로 생성한 부서정보의 상위 부서 코드를 UPD 한다.
		StringBuffer sb3 = new StringBuffer();
		sb3.append(" UPDATE DEPARTMENT  A	").append("\r\n");
		sb3.append(" SET UP_DEPT_IDX = ifnull(( SELECT X.DEPT_ID FROM HR_GW_DEPARTMENT X	").append("\r\n");
		sb3.append("                     WHERE X.ORG_CD = ( 	").append("\r\n");
		sb3.append("                       SELECT Y.SUPER_ORG_CD FROM HR_GW_DEPARTMENT Y	").append("\r\n");
		sb3.append("                       WHERE Y.DEPT_ID = A.DEPT_ID	").append("\r\n");
		sb3.append("                       AND now() between str_to_date( STA_YMD, '%Y%m%d') and str_to_date( END_YMD, '%Y%m%d')  AND USE_YN = 'Y' 	").append("\r\n");
		sb3.append("                     )").append("\r\n");
		sb3.append("                     AND now() between str_to_date( STA_YMD, '%Y%m%d') and str_to_date( END_YMD, '%Y%m%d')  AND USE_YN = 'Y' ").append("\r\n");
		sb3.append("                    ) , 'ROOT')").append("\r\n");
		//sb3.append(" WHERE DEPT_STATUS = 'U' AND UP_DEPT_IDX = '-'  ").append("\r\n");
		sb3.append(" WHERE DEPT_STATUS = 'U'  AND DEPT_ID NOT IN ( 'ROOT', 'C300151214' )   ").append("\r\n");

		//신규 생성한 부서의 DEPT_LOC를 갱신한다.
		//String sql8 = "UPDATE DEPARTMENT A SET DEPT_LOC = F_DEPT_TREE(A.CMP_ID,A.DEPT_ID) WHERE DEPT_STATUS = 'U' and DEPT_LOC = '' ";
		String sql8 = "UPDATE DEPARTMENT A SET DEPT_LOC = F_DEPT_TREE(A.CMP_ID,A.DEPT_ID) WHERE DEPT_STATUS = 'U'   AND DEPT_ID NOT IN ( 'ROOT', 'C300151214' )   ";

		try {
			pstmt1 = hrConn.prepareStatement(sb.toString());
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String orgCd=rs1.getString("ORG_CD");
				String orgNm=rs1.getString("ORG_NM");
				String superOrgCd=rs1.getString("SUPER_ORG_CD");
				String seq=rs1.getString("SEQ");
				String staYmd=rs1.getString("STA_YMD");
				String endYmd=rs1.getString("END_YMD");
				String useYn=rs1.getString("USE_YN");
				String lv=rs1.getString("LV");

//				System.out.println( orgCd + "\t" + orgNm+ "\t" + superOrgCd+ "\t" +seq + "\t" +staYmd + "\t" +endYmd + "\t" +useYn + "\t" + lv);

				int pSeq2 = 1;

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(pSeq2++, lv);
				pstmt2.setString(pSeq2++, orgCd);
				pstmt2.setString(pSeq2++, orgNm);
				pstmt2.setString(pSeq2++, superOrgCd);
				pstmt2.setString(pSeq2++, seq);
				pstmt2.setString(pSeq2++, staYmd);
				pstmt2.setString(pSeq2++, endYmd);
				pstmt2.setString(pSeq2++, useYn);

				if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_DEPARTMENT UPD ERR -- 1 ");

				if(pstmt2!=null) pstmt2.close();
			}

			pstmt3 = gwConn.prepareStatement(sql3);
			rs3 = pstmt3.executeQuery();

			while(rs3.next()) {
				String orgCd=rs3.getString("ORG_CD");
				String orgNm=rs3.getString("ORG_NM");
				String seq=rs3.getString("SEQ");
				String lv=rs3.getString("LV");

//				System.out.println( orgCd + "\t" + orgNm + "\t" + seq + "\t" + lv);

				pstmt4 = gwConn.prepareStatement(sql4);
				rs4 = pstmt4.executeQuery();

				String deptId="";
				if( rs4.next() ) deptId = rs4.getString(1);
//				System.out.println("deptId -- > " + deptId);

				int pSeq = 1;
//				System.out.println( sb1.toString() );

				pstmt5 = gwConn.prepareStatement(sb1.toString());
				pstmt5.setString(pSeq++, deptId);
				pstmt5.setString(pSeq++, orgNm);
				pstmt5.setString(pSeq++, seq);
				pstmt5.setString(pSeq++, lv);

				if( pstmt5.executeUpdate() < 0 ) throw new Exception(">>> DEPARTMENT INS ERR -- 1 ");

				if(rs4!=null) rs4.close();
				if(pstmt4!=null) pstmt4.close();
				if(pstmt5!=null) pstmt5.close();
			}

			// 매핑 테이블의 GW에서 생성한 부서 DEPT_ID를 넣는다.
			pstmt6 = gwConn.prepareStatement(sb2.toString());
			if( pstmt6.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_DEPARTMENT UPD ERR -- 1 ");

			// GW에서 새로 생성한 부서정보의 상위 부서 코드를 UPD 한다.
			pstmt7 = gwConn.prepareStatement(sb3.toString());
			if( pstmt7.executeUpdate() < 0 ) throw new Exception(">>> DEPARTMENT UP_DEPT_IDX UPD ERR -- 2 ");

			pstmt8 = gwConn.prepareStatement(sql8);
			if( pstmt8.executeUpdate() < 0 ) throw new Exception(">>> DEPARTMENT DEPT_LOC UPD ERR -- 3 ");

			gwConn.commit();
		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

	}

	private void insOrgMappingGw() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1 = " SELECT DEPT_ID, DEPT_NAME FROM DEPARTMENT WHERE DEPT_ID NOT IN ( 'ROOT' ) AND DEPT_STATUS = 'U' ";
		String sql2 = " SELECT COUNT(*) FROM HR_GW_DEPARTMENT WHERE ORG_NM = ? AND now() between str_to_date( sta_ymd, '%Y%m%d') and str_to_date( end_ymd, '%Y%m%d') AND USE_YN = 'Y'  ";
		String sql3 = " UPDATE DEPARTMENT SET DEPT_STATUS = 'D' WHERE DEPT_ID = ? ";
		String sql4 = " UPDATE HR_GW_DEPARTMENT SET DEPT_ID = ? WHERE ORG_NM = ? AND now() between str_to_date( sta_ymd, '%Y%m%d') and str_to_date( end_ymd, '%Y%m%d')  AND USE_YN = 'Y' ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String deptId = rs1.getString("DEPT_ID");
				String deptNm = rs1.getString("DEPT_NAME");

//				System.out.println(deptId +"\t" +deptNm);

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, deptNm);
				rs2 = pstmt2.executeQuery();

				int isExists = 0;
				if( rs2.next() ) isExists = rs2.getInt(1);

				if( isExists == 0 ) { // 부서명으로 검색시 없으면 삭제마킹 처리
					pstmt3 = gwConn.prepareStatement(sql3);
					pstmt3.setString(1, deptId);

					if( pstmt3.executeUpdate() < 0 ) throw new Exception(">>> DEPARTMENT UPD ERR -- 1");
				}else {
					pstmt4 = gwConn.prepareStatement(sql4);
					pstmt4.setString(1, deptId);
					pstmt4.setString(2, deptNm);

					if( pstmt4.executeUpdate() < 0 ) throw new Exception(">>> DEPARTMENT UPD ERR -- 2");
				}

				if(pstmt4!=null) pstmt4.close();
				if(pstmt3!=null) pstmt3.close();
				if(pstmt2!=null) pstmt2.close();
				if(rs2!=null) rs2.close();
			}

			gwConn.commit();
		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insOrgMapping(List<Map<String, String>> hrOrg) throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		Map<String, String> getMp = new HashMap<String, String>();

		String sql1 = "DELETE FROM HR_GW_DEPARTMENT ";
		String sql2 = "INSERT INTO HR_GW_DEPARTMENT (ORG_CD, ORG_NM, SUPER_ORG_CD, SEQ, STA_YMD, END_YMD, USE_YN  ) VALUES (?,?,?,?,?,?,?) ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			if( pstmt1.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_DEPARTMENT DEL - Err ");

			for(int i=0;i<hrOrg.size();i++) {
				getMp = (Map<String,String>) hrOrg.get(i);

				String orgCd = getMp.get("ORG_CD");
				String orgNm = getMp.get("ORG_NM");
				String superOrgCd = getMp.get("SUPER_ORG_CD") == null ? "-" : getMp.get("SUPER_ORG_CD") ;
				String seq = getMp.get("SEQ");
				String staYmd = getMp.get("STA_YMD");
				String endYmd = getMp.get("END_YMD");
				String useYn = getMp.get("USE_YN");

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, orgCd);			//System.out.println(orgCd);
				pstmt2.setString(2, orgNm);			//System.out.println(orgNm);
				pstmt2.setString(3, superOrgCd);    //System.out.println(superOrgCd);
				pstmt2.setString(4, seq);           //System.out.println(seq);
				pstmt2.setString(5, staYmd);        //System.out.println(staYmd);
				pstmt2.setString(6, endYmd);        //System.out.println(endYmd);
				pstmt2.setString(7, useYn);         //System.out.println(useYn);

				if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_DEPARTMENT INS - Err");

				if(pstmt2!=null) pstmt2.close();
			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private List<Map<String, String>> getHrOrg() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuffer sqlSb = new StringBuffer();

		try {
			sqlSb.append("SELECT ORG_CD, ORG_NM, SUPER_ORG_CD, SEQ, TO_CHAR( STA_YMD, 'yyyymmdd') STA_YMD, TO_CHAR( END_YMD, 'yyyymmdd' ) END_YMD, USE_YN	").append("\r\n");
			sqlSb.append("FROM VI_INF_ORM_ORG	").append("\r\n");

//			System.out.println( gwConn );

			pstmt1 = hrConn.prepareStatement(sqlSb.toString());

			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				Map<String,String> mp = new HashMap<String,String>();

				mp.put("ORG_CD", rs1.getString("ORG_CD") );
				mp.put("ORG_NM", rs1.getString("ORG_NM") );
				mp.put("SUPER_ORG_CD", rs1.getString("SUPER_ORG_CD") );
				mp.put("SEQ", rs1.getString("SEQ") );
				mp.put("STA_YMD", rs1.getString("STA_YMD") );
				mp.put("END_YMD", rs1.getString("END_YMD") );
				mp.put("USE_YN", rs1.getString("USE_YN") );

				list.add(mp);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

		return list;
	}

	private void syncOffice_HR_GW_Mapping() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1 = " SELECT HR_CD, OFC_NM, OFC_ID FROM HR_GW_OFFICE WHERE OFC_ID IS NULL ";
		String sql2 = "  SELECT CONCAT( 'O', LPAD( IFNULL( SUBSTR( MAX( OFC_ID ), 2) + 1, 1 ), 9, '0') )  FROM OFFICE ";
		String sql3 = " INSERT INTO OFFICE (OFC_ID, CMP_ID, OFC_NAME, REG_DATE ) VALUES (?,'C300151214',?,now()) ";
		String sql4 = " UPDATE HR_GW_OFFICE SET OFC_ID = ? WHERE OFC_NM = ? ";

		// 총경리, 부총경리는 팀장, 그룹장 하고 순서를 동일 시 한다.
		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE OFFICE A ").append("\n");
		sb.append(" SET OFC_ORDER =  CASE WHEN A.OFC_NAME = '총경리' THEN ( SELECT  CAST( HR_CD AS SIGNED) FROM HR_GW_OFFICE X WHERE  X.OFC_NM = '팀장'  )		").append("\n");
		sb.append("			  			  WHEN A.OFC_NAME = '부총경리' THEN ( SELECT  CAST( HR_CD AS SIGNED) FROM HR_GW_OFFICE X WHERE  X.OFC_NM = '그룹장'  )	").append("\n");
		sb.append("			  			  WHEN A.OFC_NAME = '사업부장' THEN ( SELECT  CAST( HR_CD AS SIGNED) FROM HR_GW_OFFICE X WHERE  X.OFC_NM = '실장'  )	").append("\n");
		sb.append("			  			  WHEN A.OFC_NAME in ('본부원','실원','팀원','지사원','그룹원')  THEN ( SELECT  CAST( HR_CD AS SIGNED) FROM HR_GW_OFFICE X WHERE  X.OFC_NM = '팀원'  )	").append("\n");
		sb.append("			   		 	  ELSE ( SELECT  CAST( HR_CD AS SIGNED) FROM HR_GW_OFFICE X WHERE X.OFC_ID = A.OFC_ID   )							").append("\n");
		sb.append("		      		 END 																													").append("\n");
		sb.append(" WHERE EXISTS ( SELECT 'X' FROM HR_GW_OFFICE X WHERE X.OFC_ID = A.OFC_ID ) ").append("\n");

		String sql5 = sb.toString();

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()){
				String hrCd = rs1.getString("HR_CD");
				String ofcNm = rs1.getString("OFC_NM");

//				System.out.println( hrCd + "\t" + posNm );

				pstmt2 = gwConn.prepareStatement(sql2);
				rs2 = pstmt2.executeQuery();

				String ofcId="";
				if( rs2.next() ) ofcId = rs2.getString(1);

				pstmt3 = gwConn.prepareStatement(sql3);
				pstmt3.setString(1, ofcId);
				pstmt3.setString(2, ofcNm);

				if( pstmt3.executeUpdate() < 0 ) throw new Exception("INS ERR -- 1");

				if(rs2!=null) rs2.close();
				if(pstmt2!=null) pstmt2.close();
				if(pstmt3!=null) pstmt3.close();

				pstmt4 = gwConn.prepareStatement(sql4);
				pstmt4.setString(1, ofcId);
				pstmt4.setString(2, ofcNm);

				if( pstmt4.executeUpdate() < 0 ) throw new Exception("UPD ERR -- 2 ");

			}

			pstmt5 = gwConn.prepareStatement(sql5);
			if( pstmt5.executeUpdate() < 0 ) throw new Exception("UPD ERR -- 3 ");

			gwConn.commit();

		}catch(Exception e){
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insOfficeMappingGw() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1 = "SELECT OFC_NAME, OFC_ID FROM OFFICE WHERE OFC_ID LIKE 'O%' ";
		String sql2 = "SELECT COUNT(*) FROM HR_GW_OFFICE WHERE OFC_NM = ? AND now() between str_to_date( sta_ymd, '%Y%m%d') and str_to_date( end_ymd, '%Y%m%d') ";
		String sql3 = "DELETE FROM POSITION WHERE OFC_ID = ? ";
		String sql4 = "UPDATE HR_GW_OFFICE SET OFC_ID = ? WHERE OFC_NM = ? AND now() between str_to_date( sta_ymd, '%Y%m%d') and str_to_date( end_ymd, '%Y%m%d') ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String ofcNm = rs1.getString("OFC_NAME");
				String ofcId = rs1.getString("OFC_ID");

//				System.out.println(posNm + "\t" + posId);

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, ofcNm);
				rs2 = pstmt2.executeQuery();

				int isExists=0;
				if( rs2.next() ) isExists = rs2.getInt(1);

				if( isExists == 0 ) { // 직위명으로 검색시 없으면 삭제 처리
					pstmt3 = gwConn.prepareStatement(sql3);
					pstmt3.setString(1, ofcId);

					if( pstmt3.executeUpdate() < 0 ) throw new Exception("DEL ERR ");

				}else { // 있으면 갱신
					pstmt4 = gwConn.prepareStatement(sql4);
					pstmt4.setString(1, ofcId);
					pstmt4.setString(2, ofcNm);

					if( pstmt4.executeUpdate() < 0 ) throw new Exception("UPD ERR ");
				}

				if(rs2!=null) rs2.close();
				if(pstmt2!=null) pstmt2.close();
				if(pstmt3!=null) pstmt3.close();
				if(pstmt4!=null) pstmt4.close();

			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insOfficeMapping(List<Map<String, String>> hrOffice) throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		Map<String, String> getMp = new HashMap<String, String>();

		String sql1 = "DELETE FROM HR_GW_OFFICE  ";
		String sql2 = "INSERT INTO HR_GW_OFFICE (HR_CD, OFC_NM, STA_YMD, END_YMD ) VALUES (?,?,?,? ) ";

		try {

			pstmt1 = gwConn.prepareStatement(sql1);
			if( pstmt1.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_OFFICE DEL - Err ");

			for(int i=0;i<hrOffice.size();i++) {
				getMp = (Map<String,String>) hrOffice.get(i);

				String hrCd = getMp.get("CD");
				String hrCdNm = getMp.get("CD_NM");
				String staYmd = getMp.get("STA_YMD");
				String endYmd = getMp.get("END_YMD");

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, hrCd);
				pstmt2.setString(2, hrCdNm);
				pstmt2.setString(3, staYmd);
				pstmt2.setString(4, endYmd);

				if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_OFFICE INS - Err");

				if(pstmt2!=null) pstmt2.close();
			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private List<Map<String, String>> getHrOfficeInfo() throws SQLException{
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		StringBuffer sqlSb = new StringBuffer();

		try {
			sqlSb.append("SELECT CD, CD_NM, TO_CHAR( STA_YMD, 'yyyymmdd') STA_YMD, TO_CHAR( END_YMD, 'yyyymmdd' ) END_YMD	").append("\r\n");
			sqlSb.append("FROM VI_INF_COMMON_CODE	").append("\r\n");
			sqlSb.append("WHERE CD_KIND = 'PHM_DUTY_CD'	").append("\r\n");

//			System.out.println( gwConn );

			pstmt1 = hrConn.prepareStatement(sqlSb.toString());

			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				Map<String,String> mp = new HashMap<String,String>();

				mp.put("CD", rs1.getString("CD") );
				mp.put("CD_NM", rs1.getString("CD_NM") );
				mp.put("STA_YMD", rs1.getString("STA_YMD") );
				mp.put("END_YMD", rs1.getString("END_YMD") );

				list.add(mp);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

		return list;
	}

	private void syncPosition_HR_GW_Mapping() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1 = " SELECT HR_CD, POS_NM, POS_ID FROM HR_GW_POSITION WHERE POS_ID IS NULL ";
		String sql2 = " SELECT CONCAT( 'P', LPAD( IFNULL( SUBSTR( MAX( POS_ID ), 2) + 1, 1 ), 9, '0') ) FROM POSITION ";

		String sql3 = " INSERT INTO POSITION (POS_ID, CMP_ID, POS_NAME, REG_DATE ) VALUES (?,'C300151214',?,now()) ";
		String sql4 = " UPDATE HR_GW_POSITION SET POS_ID = ? WHERE POS_NM = ? ";

		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE POSITION A ").append("\n");
		sb.append(" SET POS_ORDER = CASE WHEN A.POS_NAME = '사원' THEN ( SELECT CAST( HR_CD AS SIGNED) FROM HR_GW_POSITION X WHERE X.POS_ID = A.POS_ID ) + 2 ").append("\n");
		sb.append(" 					 WHEN A.POS_NAME = '선임' THEN ( SELECT CAST( HR_CD AS SIGNED) FROM HR_GW_POSITION X WHERE X.POS_ID = A.POS_ID ) - 1").append("\n");
		sb.append(" 					 WHEN A.POS_NAME = '주임' THEN ( SELECT CAST( HR_CD AS SIGNED) FROM HR_GW_POSITION X WHERE X.POS_ID = A.POS_ID ) - 1 ").append("\n");
		sb.append(" 					 ELSE ( SELECT CAST( HR_CD AS SIGNED) FROM HR_GW_POSITION X WHERE X.POS_ID = A.POS_ID ) 						 ").append("\n");
		sb.append(" 				END																													 ").append("\n");
		sb.append(" WHERE EXISTS ( SELECT 'X' FROM HR_GW_POSITION X WHERE X.POS_ID = A.POS_ID ) ").append("\n");

		String sql5 = sb.toString();

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()){
				String hrCd = rs1.getString("HR_CD");
				String posNm = rs1.getString("POS_NM");

//				System.out.println( hrCd + "\t" + posNm );

				pstmt2 = gwConn.prepareStatement(sql2);
				rs2 = pstmt2.executeQuery();

				String posId = "";
				if( rs2.next() ) posId = rs2.getString(1);

				pstmt3 = gwConn.prepareStatement(sql3);
				pstmt3.setString(1, posId);
				pstmt3.setString(2, posNm);

				if( pstmt3.executeUpdate() < 0 ) throw new Exception("INS ERR -- 1");

				if(rs2!=null) rs2.close();
				if(pstmt2!=null) pstmt2.close();
				if(pstmt3!=null) pstmt3.close();

				pstmt4 = gwConn.prepareStatement(sql4);
				pstmt4.setString(1, posId);
				pstmt4.setString(2, posNm);

				if( pstmt4.executeUpdate() < 0 ) throw new Exception("UPD ERR -- 2 ");

			}

			pstmt5 = gwConn.prepareStatement(sql5);
			if( pstmt5.executeUpdate() < 0 ) throw new Exception("UPD ERR -- 3 ");

			gwConn.commit();

		}catch(Exception e){
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insPositionMappingGw() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		String sql1 = "SELECT POS_NAME, POS_ID FROM POSITION WHERE POS_ID NOT IN ('0') ";
		String sql2 = "SELECT COUNT(*) FROM HR_GW_POSITION WHERE POS_NM = ? AND now() between str_to_date( sta_ymd, '%Y%m%d') and str_to_date( end_ymd, '%Y%m%d') ";
		String sql3 = "DELETE FROM POSITION WHERE POS_ID = ? ";
		String sql4 = "UPDATE HR_GW_POSITION SET POS_ID = ? WHERE POS_NM = ? AND now() between str_to_date( sta_ymd, '%Y%m%d') and str_to_date( end_ymd, '%Y%m%d') ";

		try {
			pstmt1 = gwConn.prepareStatement(sql1);
			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				String posNm = rs1.getString("POS_NAME");
				String posId = rs1.getString("POS_ID");

//				System.out.println(posNm + "\t" + posId);

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, posNm);
				rs2 = pstmt2.executeQuery();

				int isExists=0;
				if( rs2.next() ) isExists = rs2.getInt(1);

				if( isExists == 0 ) { // 직위명으로 검색시 없으면 삭제 처리
					pstmt3 = gwConn.prepareStatement(sql3);
					pstmt3.setString(1, posId);

					if( pstmt3.executeUpdate() < 0 ) throw new Exception("DEL ERR ");

				}else { // 있으면 갱신
					pstmt4 = gwConn.prepareStatement(sql4);
					pstmt4.setString(1, posId);
					pstmt4.setString(2, posNm);

					if( pstmt4.executeUpdate() < 0 ) throw new Exception("UPD ERR ");
				}

				if(rs2!=null) rs2.close();
				if(pstmt2!=null) pstmt2.close();
				if(pstmt3!=null) pstmt3.close();
				if(pstmt4!=null) pstmt4.close();

			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}
	}

	private void insPositionMapping(List<Map<String, String>> hrPosition) throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		Map<String, String> getMp = new HashMap<String, String>();

		String sql1 = "DELETE FROM HR_GW_POSITION  ";
		String sql2 = "INSERT INTO HR_GW_POSITION (HR_CD, POS_NM, STA_YMD, END_YMD ) VALUES (?,?,?,? ) ";

		try {

			pstmt1 = gwConn.prepareStatement(sql1);
			if( pstmt1.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_POSITION DEL - Err ");

			for(int i=0;i<hrPosition.size();i++) {
				getMp = (Map<String,String>) hrPosition.get(i);

				String hrCd = getMp.get("CD");
				String hrCdNm = getMp.get("CD_NM");
				String staYmd = getMp.get("STA_YMD");
				String endYmd = getMp.get("END_YMD");

				pstmt2 = gwConn.prepareStatement(sql2);
				pstmt2.setString(1, hrCd);
				pstmt2.setString(2, hrCdNm);
				pstmt2.setString(3, staYmd);
				pstmt2.setString(4, endYmd);

				if( pstmt2.executeUpdate() < 0 ) throw new Exception(">>> HR_GW_POSITION INS - Err");

				if(pstmt2!=null) pstmt2.close();
			}

			gwConn.commit();

		}catch(Exception e) {
			gwConn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

	}

	/*	HR의 직위정보를 가져온다 */
	private List<Map<String,String>> getHrPoistionInfo() throws SQLException {
		PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null, pstmt6 = null, pstmt7 = null, pstmt8 = null, pstmt9 = null, pstmt10 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null, rs6 = null, rs7 = null, rs8 = null, rs9 = null, rs10 = null;

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		StringBuffer sqlSb = new StringBuffer();

		try {
			sqlSb.append("SELECT CD, CD_NM, TO_CHAR( STA_YMD, 'yyyymmdd') STA_YMD, TO_CHAR( END_YMD, 'yyyymmdd' ) END_YMD	").append("\r\n");
			sqlSb.append("FROM VI_INF_COMMON_CODE	").append("\r\n");
			sqlSb.append("WHERE CD_KIND = 'PHM_POS_CD' 	").append("\r\n");

//			System.out.println( gwConn );

			pstmt1 = hrConn.prepareStatement(sqlSb.toString());

			rs1 = pstmt1.executeQuery();

			while(rs1.next()) {
				Map<String,String> mp = new HashMap<String,String>();

				mp.put("CD", rs1.getString("CD") );
				mp.put("CD_NM", rs1.getString("CD_NM") );
				mp.put("STA_YMD", rs1.getString("STA_YMD") );
				mp.put("END_YMD", rs1.getString("END_YMD") );

				list.add(mp);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt1!=null) pstmt1.close();
			if(pstmt2!=null) pstmt2.close();
			if(pstmt3!=null) pstmt3.close();
			if(pstmt4!=null) pstmt4.close();
			if(pstmt5!=null) pstmt5.close();
			if(pstmt6!=null) pstmt6.close();
			if(pstmt7!=null) pstmt7.close();
			if(pstmt8!=null) pstmt8.close();
			if(pstmt9!=null) pstmt9.close();
			if(pstmt10!=null) pstmt10.close();

			if(rs1!=null) rs1.close();
			if(rs2!=null) rs2.close();
			if(rs3!=null) rs3.close();
			if(rs4!=null) rs4.close();
			if(rs5!=null) rs5.close();
			if(rs6!=null) rs6.close();
			if(rs7!=null) rs7.close();
			if(rs8!=null) rs8.close();
			if(rs9!=null) rs9.close();
			if(rs10!=null) rs10.close();
		}

		return list;
	}

	/* GW DB Connection 정보 얻어오기 */
	private static Connection getGwConn() {
		Connection conn = null;

		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL_GW, USER_GW, PASS_GW);
			conn.setAutoCommit(false);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {

		}

		return conn;
	}

	/* HR DB Connection 정보 얻어온다 */
	private static Connection getHrConn() {
		Connection conn = null;

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {

		}

		return conn;
	}

	private static void closeAll() throws SQLException {

	}

	private static String nullToSpace(String str) {
		if( str == null ) return "";
		else return str;
	}

	private static String encode(String enc) {
		return encodeLicNo(enc, StringUtils.defaultString("naongw", "naongw"));
	}

	private static String encodeLicNo(String enc, String cryptoType) {
		String encStr = null;

		byte[] byteKey = { (byte) 0x4e, (byte) 0x00, (byte) 0x41, (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x4e, (byte) 0x00, (byte) 0x47, (byte) 0x00, (byte) 0x57, (byte) 0x00, (byte) 0x21,
				(byte) 0x00, (byte) 0x7e, (byte) 0x00 };
		SeedCipher seed = new SeedCipher();
		encStr = StringUtils.remove(Base64.encode(seed.encrypt(enc, byteKey)),"\n");

		return encStr;
	}

	private static byte[] getSHA256(String input){
		String res = "";
		byte[] sha256Byte = null;
		try{
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

			sha256.update(input.getBytes());
			sha256Byte = sha256.digest();
		}
		finally{
			return sha256Byte;
		}
	}

	private static String getEncryPassword(String pw) {
		return new BASE64Encoder().encode( getSHA256( pw ) );
	}

	private static void gwSvConn()  {
		jsch = new JSch();

		try {
			session = jsch.getSession(svUsr, svHost, svPort);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(svPw);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();

			sftpChannel = (ChannelSftp)channel;

		}catch(JSchException  e) {
			System.out.println("GW Connection Fail --> "+ e.getMessage() );
			e.printStackTrace();
		}finally {

		}
	}

	private static void gwSvDisConn()  {
		if(session.isConnected() || channel.isConnected() || sftpChannel.isConnected() ) {
			attrs = null;

			sftpChannel.disconnect();
			channel.disconnect();
			session.disconnect();

			try {
				jsch.removeAllIdentity();
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("GW DisConnection Success");
		}
	}

	private static boolean upLoad(String fileNm, String remoteDir, String svFileNm) {
		boolean isUploaded = false;

		FileInputStream fis = null;

		makeDir( remoteDir );

		try {
			System.out.println(remoteDir);

			File file = new File(fileNm);

			fis = new FileInputStream(file);

			sftpChannel.cd(remoteDir);
			sftpChannel.put(fis, svFileNm);
			fis.close();

			System.out.println("File Upload Success");

			isUploaded=true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		}

		return isUploaded;
	}

	private static boolean removeOldPhoto(String remoteDir, String remoteFileNm ) {
		boolean isDeleted = false;

		try {
			sftpChannel.cd(remoteDir);
			sftpChannel.rm(remoteFileNm);

			isDeleted = true;
		}catch(Exception e) {
			System.out.println("GW Photo Delete Fail --> "+ e.getMessage() );
		}finally {
		}

		return isDeleted;
	}

	private static void makeDir(String inPath)  {

		try {

			if(inPath.contains("/")) {
				String[] compo = inPath.split("/");
				String path = "/"+compo[1];

				for(int i=2;i<compo.length;i++) {
					path += "/"+compo[i];

					try {
						attrs = sftpChannel.stat(path);
					}catch(Exception e){
						sftpChannel.mkdir(path);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isSameFile(BLOB imgData, String photoUrl) {
		boolean isSame = false;

		String tempFileLoc="";
		String sprt = System.getProperty("file.separator");

		if( System.getProperty("os.name").indexOf("Windows") >= 0) {
			tempFileLoc="D:\\temp\\hrPhoto"+sprt;
		}else {
			tempFileLoc="/user/home/mro"+sprt;
		}

		try {
			if("".equals(photoUrl)) {

			}else {
				String tmpLoc = (baseUrl + photoUrl).substring( 0, (baseUrl + photoUrl).lastIndexOf("/")  ).replaceAll("//", "/");
				String svFileNm = (baseUrl + photoUrl).substring( (baseUrl + photoUrl).lastIndexOf("/") + 1 );

				sftpChannel.cd(tmpLoc);
				File file = new File( svFileNm );
				BufferedInputStream remoteIs = new BufferedInputStream(sftpChannel.get(file.getName()));
				File newFile = new File( tempFileLoc + sprt + file.getName() );

				OutputStream os = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(os);

				byte[] buffer = new byte[1024];

				int readCount;
	            while ((readCount = remoteIs.read(buffer)) > 0) {
	                bos.write(buffer, 0, readCount);
	            }

	            remoteIs.close();
	            bos.close();
	            os.close();

				InputStream tmpIs = new DataInputStream(new FileInputStream(newFile));
				InputStream dbIs = imgData.getBinaryStream();

				int fr1 = 0, fr2 = 0;

				isSame = true;

				while( ( (fr1=tmpIs.read()) != -1) && (fr2=dbIs.read()) != -1 ){
					if( fr1 != fr2 ) {
						System.out.println("파일 != ");
						isSame=false;
						break;
					}
				}

				tmpIs.close();
				dbIs.close();


				newFile.delete();
			}
		}catch(Exception e) {
			System.out.println("DB Photo, Remote Photo Err. --> "+ e.getMessage() );
		}finally {
		}

		return isSame;
	}

}
