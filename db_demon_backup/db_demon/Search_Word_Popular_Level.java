/*
 * ���ǵ�� �˻��� �α⵵ ���� �����۾�
 */
//package comm;

import java.sql.*;

//���� �ҷ��� ���

class  Search_Word_Popular_Level
{
		static private comm.DB_Use_Db_Demon  db ;  //  DB �����ϴ� ����
		static private String err_occur=null; // �����߻��ý� ���

        //������
        public Search_Word_Popular_Level(){}

        public static void main(String[] args)
        {
        	db = new comm.DB_Use_Db_Demon();  //  DB �����ϴ� ����
        	String qry="";
        	try{

            	db.CUST_ID = "00002345";
            	db.PROG_NM = "/user/home/db_demon/mro_demon/Md_to_Message_Comment.java";
    			db.DB_Conn();
				System.out.println("Db Connection !! ");

				// ���� ����Ÿ ����
				qry=" DELETE from sm_popularity_prd ";
				 if(db.executeUpdate(qry) < 0) if(err_occur.equals("ERR"));

				// �űԵ���Ÿ ���� ( 1. ���ż���, ���Ű���, ����Ƚ����ǰ�ڵ�)
				 qry= "INSERT into sm_popularity_prd (prd_cd,ord_cust,ord_prd,ord_cnt) " 
					//+ "SELECT prd_cd, sum(ord_cust), count(distinct ord_prd), count(ord_cnt) FROM  " 
					+ "SELECT prd_cd, CASE WHEN p_spd_cata.f_get_prdimg(prd_cd,1) IS NULL THEN -1 ELSE Sum(ord_cust) END,CASE WHEN p_spd_cata.f_get_prdimg(prd_cd,1) IS NULL THEN -1 ELSE  Count(DISTINCT ord_prd) END,CASE WHEN p_spd_cata.f_get_prdimg(prd_cd,1) IS NULL THEN -1 ELSE  Count(ord_cnt) END FROM  "//sjjang 20181206 �α�˻��� ���غ��� : ����� 7��, �̹��� ������ �ڷ�
					+ "( " 
					//+ "SELECT decode(pi.hrank_yn,'Y',pi.prd_cd,'N',pi.hrank_prd_cd, pi.prd_cd) prd_cd ,od.cnt as ord_cust, od.cust_id as ord_prd, od.prd_cd as ord_cnt " //sjjang 20181214 �α�˻��� ���� : hrk_prd_link�� �ֽ�ȭ
					+ "SELECT Nvl(pi.prd_cd,od.prd_cd) prd_cd ,od.cnt AS ord_cust, od.cust_id AS ord_prd, od.prd_cd AS ord_cnt " 
					//+ "FROM ords_dtl od, prd_info pi " //sjjang 20181214 �α�˻��� ���� : hrk_prd_link�� �ֽ�ȭ
					//+ "WHERE od.prd_cd = pi.prd_cd " //sjjang 20181214 �α�˻��� ���� : hrk_prd_link�� �ֽ�ȭ
					+ "FROM ords_dtl od, hrk_prd_link pi " 
					+ "WHERE od.prd_cd = PI.PRD_CHILD(+)  " 
					//+ "AND pi.stat_flag='001' " //sjjang 20181214 �α�˻��� ���� : hrk_prd_link�� �ֽ�ȭ
					+ "AND od.ords_ty='001'  " 
					+ "AND od.proc_ty in ('001','004','005')   " 
					//+ "AND od.reg_dati > sysdate-30 " 
					+ "AND od.reg_dati > sysdate-14 " //sjjang 20181206 �α�˻��� ���غ��� : ����� 7��, �̹��� ������ �ڷ�
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