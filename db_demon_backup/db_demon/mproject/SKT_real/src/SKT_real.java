import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skt.open2u.cno.cnosbase.CNOSBaseProxy;
import com.skt.open2u.cno.cnosbase.FCNOHappynaraGrListParameter;
import com.skt.open2u.cno.cnosbase.FCNOHappynaraGrListReturn;
import com.skt.open2u.cno.cnosbase.FCNOHappynaraGrListReturnRESULT_LISTRecord;
import com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListParameter;
import com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturn;
import com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord;
import common.types.WsRequestContext;

public class SKT_real {

	private static String driver="oracle.jdbc.driver.OracleDriver";

	// REAL DB
	private static String dbURL="jdbc:oracle:thin:@172.16.1.208:1521:ndb10";
	private static String user_id="INF_SKT", user_pw="PW08SKT2LC0M", db_flag="REAL";
	
	// TEST DB
//	private static String dbURL="jdbc:oracle:thin:@172.16.1.224:1521:TESTDB";
//	private static String user_id="INF_SKT", user_pw="INF_SKT", db_flag="TEST";
	
	public static Connection conn;
	
    private static Statement smt= null;
    private static PreparedStatement ps=null;
    private static CallableStatement cs = null;
    private static Statement ex_smt = null;

    private static ResultSet rs = null, prs = null;
    private static String sMessage = "";

    private static String qry = "", b_ins="", b_val="";
    private static int res = 0;

    public static void main(String[] args) {
        //배포해야 할곳이 cni s 와 xyz
        
        CNOSBaseProxy proxyid  = new CNOSBaseProxy();
        FCNOHappynaraePoListParameter param = new FCNOHappynaraePoListParameter();
        FCNOHappynaraGrListParameter param2 = new FCNOHappynaraGrListParameter();
        WsRequestContext wsReq =  new WsRequestContext();
        
        //개발기
        //String endPointAddress = "http://d2out.sktelecom.com/web/services/WsFrontController";
        
        //운영기
       // String endPointAddress = "https://open2u.sktelecom.com/web/services/WsFrontController"; shpark 2018-03-09
	    String endPointAddress = "http://open2u.sktelecom.com/web/services/WsFrontController";

        System.out.println("--------- SKT_real["+endPointAddress+"]---------------!!!!!!");        
        try {
        	proxyid.setEndpoint(endPointAddress);
            
            wsReq.setBranchCode("");
            wsReq.setUserId("");
            wsReq.setLocaleXd("");
            wsReq.setTerminalId("");
          
			//계약내역
			FCNOHappynaraePoListReturn result1 = proxyid.fCNOHappynaraePoList(param,wsReq);
			FCNOHappynaraePoListReturnRESULT_LISTRecord[] record = result1.getRESULT_LIST();
			
    		DB_Conn();
    		ex_smt = conn.createStatement();
			
	        System.out.println("-------------------  PO Start!!!!!!!!!!");			

			if (record != null) {
        	    int i_po_cnt = record.length;
                System.out.println("PO cnt = ["+i_po_cnt+"] !!");
//        	    i_po_cnt = 0;                

    			String in_cntrt_seq, in_cntrt_mod, in_cntrt_type, in_cntrt_no, in_cntrt_name, in_cntrt_amt, in_cntrt_write_date, in_cntrt_start_date, 
    					in_cntrt_end_date, in_rfx_reg_name, in_busi_dept_name, in_busi_dept_cd, in_busi_email, in_busi_phone_no,
    					in_busi_cell_phone, in_item_seq, in_po_date, in_src_grp_name, in_item_no, in_item_name,
    					in_item_spec, in_item_quantity, in_item_unit, in_item_price;            

    			b_ins = "INSERT INTO vu_skt_po( "+
						   " vu_skt_po_seq, cntrt_seq, cntrt_mod, cntrt_type, cntrt_no, cntrt_name, cntrt_amt, cntrt_write_date, cntrt_start_date, "+ 
						   " cntrt_end_date, rfx_reg_name, busi_dept_name, busi_dept_cd, busi_email, busi_phone_no, "+
						   " busi_cell_phone, item_seq, po_date, src_grp_name, item_no, item_name, "+
						   " item_spec, item_quantity, item_unit, item_price) ";
    			
                for (int i = 0; i < i_po_cnt; i++) {
                    FCNOHappynaraePoListReturnRESULT_LISTRecord rs = record[i];
//                    System.out.println("PO["+i+"] -----------------------------");
                    in_cntrt_seq = rs.getCNTRT_SEQ();
                    in_cntrt_mod = rs.getCNTRT_MOD();
                    in_cntrt_type = rs.getCNTRT_TYPE();
                    in_cntrt_no = rs.getCNTRT_NO();
                    in_cntrt_name = rs.getCNTRT_NAME(); in_cntrt_name = f_StringChk(in_cntrt_name);
                    in_cntrt_amt = rs.getCNTRT_AMT();
                    in_cntrt_write_date = rs.getCNTRT_WRITE_DATE();
                    in_cntrt_start_date = rs.getCNTRT_START_DATE();
                    in_cntrt_end_date = rs.getCNTRT_END_DATE();
                    if(in_cntrt_end_date.compareTo("2016-01-01") < 0) {  
//                    	System.out.println("Samll="+in_cntrt_end_date); 
                    	continue; // 계약종결일자가 2015-12-31이전이면 무시한다.
                    }
                    in_rfx_reg_name = rs.getRFX_REG_NAME();
                    in_busi_dept_name = rs.getBUSI_DEPT_NAME();
                    in_busi_dept_cd = rs.getBUSI_DEPT_CD();
                    in_busi_email = rs.getBUSI_EMAIL();
                    in_busi_phone_no = rs.getBUSI_PHONE_NO();
                    in_busi_cell_phone = rs.getBUSI_CELL_PHONE();
                    in_item_seq = rs.getITEM_SEQ();
                    in_po_date = rs.getPO_DATE();
                    in_src_grp_name = rs.getSRC_GRP_NAME();
                    in_item_no = rs.getITEM_NO();
                    in_item_name = rs.getITEM_NAME();
                    in_item_spec = rs.getITEM_SPEC();
                    in_item_quantity = rs.getITEM_QUANTITY();
                    in_item_unit = rs.getITEM_UNIT();
                    in_item_price = rs.getITEM_PRICE();

                    b_val = in_cntrt_seq+", '"+in_cntrt_mod+"', '"+in_cntrt_type+"', '"+in_cntrt_no+"', '"+in_cntrt_name+"', "+
                    		in_cntrt_amt+", '"+in_cntrt_write_date+"', '"+in_cntrt_start_date+"', '"+in_cntrt_end_date+"', "+
                    		"'"+in_rfx_reg_name+"', '"+in_busi_dept_name+"', '"+in_busi_dept_cd+"', '"+in_busi_email+"', "+
                    		"'"+in_busi_phone_no+"', '"+in_busi_cell_phone+"', "+in_item_seq+", '"+in_po_date+"', "+
                    		"'"+in_src_grp_name+"', '"+in_item_no+"', '"+in_item_name+"', '"+in_item_spec+"', "+
                    		in_item_quantity+", '"+in_item_unit+"', "+in_item_price;

//                    System.out.println("getCNTRT_SEQ["+i+"] = "+in_cntrt_seq);
//                    System.out.println("getCNTRT_MOD["+i+"] = "+in_cntrt_mod);
//                    System.out.println("getCNTRT_TYPE["+i+"] = "+in_cntrt_type);
//                    System.out.println("getCNTRT_NO["+i+"] = "+in_cntrt_no);
//                    System.out.println("getCNTRT_NAME["+i+"] = "+in_cntrt_name);
//                    System.out.println("getCNTRT_AMT["+i+"] = "+in_cntrt_amt);
//                    System.out.println("getCNTRT_WRITE_DATE["+i+"] = "+in_cntrt_write_date);
//                    System.out.println("getCNTRT_START_DATE["+i+"] = "+in_cntrt_start_date);
//                    System.out.println("getCNTRT_END_DATE["+i+"] = "+in_cntrt_end_date);
//                    System.out.println("getRFX_REG_NAME["+i+"] = "+in_rfx_reg_name);
//                    System.out.println("getBUSI_DEPT_NAME["+i+"] = "+in_busi_dept_name);
//                    System.out.println("getBUSI_DEPT_CD["+i+"] = "+in_busi_dept_cd);
//                    System.out.println("getBUSI_EMAIL["+i+"] = "+in_busi_email);
//                    System.out.println("getBUSI_PHONE_NO["+i+"] = "+in_busi_phone_no);
//                    System.out.println("getBUSI_CELL_PHONE["+i+"] = "+in_busi_cell_phone);
//                    System.out.println("getITEM_SEQ["+i+"] = "+in_item_seq);
//                    System.out.println("getPO_DATE["+i+"] = "+in_po_date);
//                    System.out.println("getSRC_GRP_NAME["+i+"] = "+in_src_grp_name);
//                    System.out.println("getITEM_NO["+i+"] = "+in_item_no);
//                    System.out.println("getITEM_NAME["+i+"] = "+in_item_name);
//                    System.out.println("getITEM_SPEC["+i+"] = "+in_item_spec);
//                    System.out.println("getITEM_QUANTITY["+i+"] = "+in_item_quantity);
//                    System.out.println("getITEM_UNIT["+i+"] = "+in_item_unit);
//                    System.out.println("getITEM_PRICE["+i+"] = "+in_item_price);
                    qry = b_ins+"VALUES(sq_vu_skt_po_seq.NEXTVAL,"+b_val+")";
//                    System.out.println("PO Qry["+i+"] = "+qry);

                    try {
                        res = ex_smt.executeUpdate(qry);
                        conn.commit();
                    } catch (SQLException e) {
                        conn.rollback();
                        System.out.println("PO_Insert Err="+e);
                        System.out.println("PO_Insert Err_qry="+qry);                        
                    }
                    System.out.print("."); if(i%100==0) System.out.println(" ");                   
                }
           }
           System.out.println("\n-------------------  PO End!!!!!!!!!!");           
           //검사조서 내역
           FCNOHappynaraGrListReturn result2 = proxyid.fCNOHappynaraGrList(param2,wsReq);
           FCNOHappynaraGrListReturnRESULT_LISTRecord[] record2 = result2.getRESULT_LIST();

           System.out.println("\n-------------------  GR Start!!!!!!!!!!");
           if (record2 != null) {
        	   int i_gr_cnt = record2.length;
               System.out.println("GR cnt = ["+i_gr_cnt+"] !!");
  //      	   i_gr_cnt = 0;

        	   String in_inv_seq, in_cntrt_no, in_dg_dr_no,
	        	   in_dg_amt, in_insp_amt, in_insp_req_date, in_insp_req_pic,
	        	   in_reg_id, in_insp_req_amt, in_tax_no, in_insp_req_nm, in_dg_date,
	        	   in_item_cd, in_item_nm, in_inv_cnt, in_unit, in_unit_price, in_inv_amt,
	        	   in_item_seq, in_insp_nm, in_dept_name, in_dept_cd, in_email, in_phone_no,
	        	   in_cell_phone_no;

        	   b_ins = "INSERT INTO vu_skt_gr( "+
							" vu_skt_gr_seq, inv_seq, cntrt_no, dg_dr_no, dg_amt, insp_amt, insp_req_date, "+
							" insp_req_pic, reg_id, insp_req_amt, tax_no, insp_req_nm, dg_date, "+
							" item_cd, item_nm, inv_cnt, unit, unit_price, inv_amt, item_seq, "+
							" insp_nm, dept_name, dept_cd, email, phone_no, cell_phone_no) ";

        	   qry = "SELECT cntrt_no FROM vu_skt_po WHERE cntrt_no=? AND rownum = 1";
//               System.out.println("000="+qry);        	   
        	   ps = conn.prepareStatement(qry);
               for (int i = 0; i < i_gr_cnt; i++) {
                   FCNOHappynaraGrListReturnRESULT_LISTRecord rs = record2[i];
//                   System.out.println("GR["+i+"] -----------------------------");
                   
                   in_inv_seq = rs.getINV_SEQ();
                   in_cntrt_no = rs.getCNTRT_NO();
                   
                   // PO테이블에 없는 계약번호에 대한 GR은 제외
                   ps.setString(1,in_cntrt_no);
                   prs = ps.executeQuery();
                   if(!prs.next()) {
//                       System.out.println("Not Found=["+in_cntrt_no+"] !!");                	   
                	   continue; 
                   }
                  
                   in_dg_dr_no = rs.getDG_DR_NO();	// 납품지시번호
                   in_dg_amt = rs.getDG_AMT();		if("".equals(in_dg_amt)) { in_dg_amt = "0"; }	// 남품지시금액
                   in_insp_amt = rs.getINSP_AMT();	// 검사금액
                   in_insp_req_date = rs.getINSP_REQ_DATE();	// 검사요청일
                   in_insp_req_pic = rs.getINSP_REQ_PIC();		// 검사요청사
                   in_reg_id = rs.getREG_ID();
                   in_insp_req_amt = rs.getINSP_REQ_AMT();
                   in_tax_no = rs.getTAX_NO();
                   in_insp_req_nm = rs.getINSP_REQ_NM(); in_insp_req_nm = f_StringChk(in_insp_req_nm);
                   in_dg_date = rs.getDG_DATE();
                   in_item_cd = rs.getITEM_CD();
                   in_item_nm = rs.getITEM_NM();
                   in_inv_cnt = rs.getINV_CNT();
                   in_unit = rs.getUNIT();
                   in_unit_price = rs.getUNIT_PRICE();
                   in_inv_amt = rs.getINV_AMT();
                   in_item_seq = rs.getITEM_SEQ();
                   in_insp_nm = rs.getINSP_NM();
                   in_dept_name = rs.getDEPT_NAME();
                   in_dept_cd = rs.getDEPT_CD();
                   in_email = rs.getEMAIL();
                   in_phone_no = rs.getPHONE_NO();
                   in_cell_phone_no = rs.getCELL_PHONE_NO();
                   
                   b_val = in_inv_seq+", '"+in_cntrt_no+"', '"+in_dg_dr_no+"', "+
                		in_dg_amt+", "+in_insp_amt+", '"+in_insp_req_date+"', '"+in_insp_req_pic+"', "+
                		"'"+in_reg_id+"', "+in_insp_req_amt+", '"+in_tax_no+"', '"+in_insp_req_nm+"', '"+in_dg_date+"', "+
                		"'"+in_item_cd+"', '"+in_item_nm+"', "+in_inv_cnt+", '"+in_unit+"', "+in_unit_price+", "+
                		in_inv_amt+", "+in_item_seq+", '"+in_insp_nm+"', '"+in_dept_name+"', '"+in_dept_cd+"', "+
                		"'"+in_email+"', '"+in_phone_no+"', '"+in_cell_phone_no+"'";

//                   System.out.println("getINV_SEQ["+i+"] = "+in_inv_seq);
//                   System.out.println("getCNTRT_NO["+i+"] = "+in_cntrt_no);
//                   System.out.println("getDG_DR_NO["+i+"] = "+in_dg_dr_no);
//                   System.out.println("getDG_AMT["+i+"] = "+in_dg_amt);
//                   System.out.println("getINSP_AMT["+i+"] = "+in_insp_amt);
//                   System.out.println("getINSP_REQ_DATE["+i+"] = "+in_insp_req_date);
//                   System.out.println("getINSP_REQ_PIC["+i+"] = "+in_insp_req_pic);
//                   System.out.println("getREG_ID["+i+"] = "+in_reg_id);
//                   System.out.println("getINSP_REQ_AMT["+i+"] = "+in_insp_req_amt);
//                   System.out.println("getTAX_NO["+i+"] = "+in_tax_no);
//                   System.out.println("getINSP_REQ_NM["+i+"] = "+in_insp_req_nm);
//                   System.out.println("getDG_DATE["+i+"] = "+in_dg_date);
//                   System.out.println("getITEM_CD["+i+"] = "+in_item_cd);
//                   System.out.println("getITEM_NM["+i+"] = "+in_item_nm);
//                   System.out.println("getINV_CNT["+i+"] = "+in_inv_cnt);
//                   System.out.println("getUNIT["+i+"] = "+in_unit);
//                   System.out.println("getUNIT_PRICE["+i+"] = "+in_unit_price);
//                   System.out.println("getINV_AMT["+i+"] = "+in_inv_amt);
//                   System.out.println("getITEM_SEQ["+i+"] = "+in_item_seq);
//                   System.out.println("getINSP_NM["+i+"] = "+in_insp_nm);
//                   System.out.println("getDEPT_NAME["+i+"] = "+in_dept_name);
//                   System.out.println("getDEPT_CD["+i+"] = "+in_dept_cd);
//                   System.out.println("getEMAIL["+i+"] = "+in_email);
//                   System.out.println("getPHONE_NO["+i+"] = "+in_phone_no);

                   qry = b_ins+"VALUES(sq_vu_skt_gr_seq.NEXTVAL, "+b_val+")";
//                   System.out.println("GR Qry["+i+"] = "+qry);

                   try {
                       res = ex_smt.executeUpdate(qry);
                       conn.commit();
                   } catch (SQLException e) {
                       conn.rollback();
                       System.out.println("GR_Insert Err="+e);
                       System.out.println("GR_Insert Err_qry="+qry);                        
                   }
                   System.out.print("."); if(i%200==0) System.out.println(" ");
               }
			}
			System.out.println("\n-------------------  GR End!!!!!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Err="+e);            
        } finally {
        	DB_DisConn();
        }
    }

	// DB연결
    private static Connection DB_Conn() {
        try {
            Class.forName(driver);
            conn=DriverManager.getConnection(dbURL,user_id,user_pw);
            conn.setAutoCommit(false);
            System.out.println(db_flag+" DB Connectioned !!");            
        } catch (ClassNotFoundException e) {
            System.out.println("ERR ConnectionBean: driver unavailable !!"+e.getMessage());
        } catch (Exception e) {
            System.out.println("ERR ConnectionBean: driver not loaded !!"+e.getMessage());
        }
        return conn;
    }
    
    // DB연결 해제
    private static void DB_DisConn() {
        try {
        	if(conn != null){ conn.setAutoCommit(true); }
            if(rs != null){ rs.close();}
            if(smt != null){ smt.close(); }
            if(ps != null){ ps.close(); }
            if(prs != null){ prs.close(); }
            if(cs != null){ cs.close(); }
            if(conn != null){ conn.close(); }
        } catch (Exception e) {
                System.out.println("ERR disConnection error !!"+e.getMessage());
        }
    }

    // 문자변환
    private static String f_StringChk(String p_val) {
    	String r_val = p_val.replace('\'', '´');
        return r_val;
    }
}