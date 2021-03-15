/*
 * 스피드몰 검색어 인기도 월별 취합작업
 */
//package comm;

import java.sql.*;

//빈즈 불러서 사용

class  Search_Word_Popular_Level
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB 연결하는 빈즈
		static private String err_occur=null; // 에러발생시시 사용

        //생성자
        public Search_Word_Popular_Level(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB 연결하는 빈즈
        	String qry="";
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/db_demon/mro_demon/Md_to_Message_Comment.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				// 기존 데이타 삭제
				qry=" DELETE from sm_popularity_prd ";
				 if(db.executeUpdate(qry) < 0) if(err_occur.equals("ERR"));

				// 신규데이타 생성 ( 1. 구매수량, 구매고객수, 구매횟수상품코드)
				 qry= "INSERT into sm_popularity_prd (prd_cd,ord_cust,ord_prd,ord_cnt) " 
					//+ "SELECT prd_cd, sum(ord_cust), count(distinct ord_prd), count(ord_cnt) FROM  " 
					+ "SELECT prd_cd, CASE WHEN p_spd_cata.f_get_prdimg(prd_cd,1) IS NULL THEN -1 ELSE Sum(ord_cust) END,CASE WHEN p_spd_cata.f_get_prdimg(prd_cd,1) IS NULL THEN -1 ELSE  Count(DISTINCT ord_prd) END,CASE WHEN p_spd_cata.f_get_prdimg(prd_cd,1) IS NULL THEN -1 ELSE  Count(ord_cnt) END FROM  "//sjjang 20181206 인기검색어 기준변경 : 계산일 7일, 이미지 없으면 뒤로
					+ "( " 
					//+ "SELECT decode(pi.hrank_yn,'Y',pi.prd_cd,'N',pi.hrank_prd_cd, pi.prd_cd) prd_cd ,od.cnt as ord_cust, od.cust_id as ord_prd, od.prd_cd as ord_cnt " //sjjang 20181214 인기검색어 변경 : hrk_prd_link로 최신화
					+ "SELECT Nvl(pi.prd_cd,od.prd_cd) prd_cd ,od.cnt AS ord_cust, od.cust_id AS ord_prd, od.prd_cd AS ord_cnt " 
					//+ "FROM ords_dtl od, prd_info pi " //sjjang 20181214 인기검색어 변경 : hrk_prd_link로 최신화
					//+ "WHERE od.prd_cd = pi.prd_cd " //sjjang 20181214 인기검색어 변경 : hrk_prd_link로 최신화
					+ "FROM ords_dtl od, hrk_prd_link pi " 
					+ "WHERE od.prd_cd = PI.PRD_CHILD(+)  " 
					//+ "AND pi.stat_flag='001' " //sjjang 20181214 인기검색어 변경 : hrk_prd_link로 최신화
					+ "AND od.ords_ty='001'  " 
					+ "AND od.proc_ty in ('001','004','005')   " 
					//+ "AND od.reg_dati > sysdate-30 " 
					+ "AND od.reg_dati > sysdate-14 " //sjjang 20181206 인기검색어 기준변경 : 계산일 7일, 이미지 없으면 뒤로
					+ "AND exists (SELECT * FROM enpri_dtl WHERE enpri_cd=od.enpri_cd AND busip_seq=od.busip_seq AND  speedcust_yn = 'Y')  " 
					+ ") a " 
					+ "group by prd_cd ";

				 if(db.executeUpdate(qry) < 0) if(err_occur.equals("ERR"));

				db.commit();
				System.out.println("Db Excute !! ");
            }catch(SQLException e){
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