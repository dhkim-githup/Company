import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import SKhynixCatalog.DB_Use;
import SKhynixCatalog.DB_UseCn;
import SKhynixCatalog.Date;
import SKhynixCatalog.mk_log;

public class Demon_SKHYCatalogSync {
	private  static final String Prog_Nm = "SKHYCatalogSync";
	
    private static mk_log log = new mk_log();
    
    public Demon_SKHYCatalogSync() {
    }
    
    public static void main(String[] args){
    	Demon_SKHYCatalogSync Sync = new Demon_SKHYCatalogSync();
    	Date DT = new Date();    	
    	
    	DB_Use db = new DB_Use();
    	db.DB_Conn();
    	
    	DB_UseCn dbcn = new DB_UseCn();
    	dbcn.DB_Conn();
    	
    	try {
    		    		
    		String sQry = "";
    		sQry += "select ";
    		sQry += "	COP, THIS_BSHOP_NM, PLIS_NM, PRD_CD, CUST_PRD_CD ";
    		sQry += "	, SHRT_DOC, MDL_HANG_NM, TMK_HANG_NM, MAKE_CO_NM, UNIT ";
    		sQry += "	, THE_HOME, PLANT, SCO_NM, ENPRI_CD, BUSIP_SEQ ";
    		sQry += "	, SCO_CD, BUY_PRI, SELL_PRI, IMG1, CUR ";
    		sQry += "	, CUR_BUY, GM_UNIT ";
    		sQry += "from V_GEN_PRD_INFO_SKHY ";
    		
    		
    		/*******************************************************************************
		    PURPOSE : 한국 하이닉스 카탈로그와 동기회 
		    REVISIONS :
		    DATE        Author      Description
		    ---------   ----------  ----------------------------------------------------
		    150627      mhhong      1. Create this Function       
		    *******************************************************************************/
    		/*******************************************************************************
    		P_CATALOG_CN.SP_GEN_PRD_INFO_SKHY_CRT (    		
    				    P_ENPRI_CD nvarchar2
    				    , P_BUSIP_SEQ nvarchar2
    				    , P_CUST_PRD_CD nvarchar2
    				    , P_COP nvarchar2
    				    , P_THIS_BSHOP_NM nvarchar2
    				    , P_PLIS_NM nvarchar2
    				    , P_PRD_CD nvarchar2
    				    , P_SHRT_DOC nvarchar2
    				    , P_MDL_HANG_NM nvarchar2
    				    , P_TMK_HANG_NM nvarchar2
    				    , P_MAKE_CO_NM nvarchar2
    				    , P_UNIT nvarchar2
    				    , P_THE_HOME nvarchar2
    				    , P_PLANT nvarchar2
    				    , P_SCO_NM nvarchar2
    				    , P_SCO_CD nvarchar2
    				    , P_BUY_PRI nvarchar2
    				    , P_SELL_PRI nvarchar2
    				    , P_IMG1 nvarchar2
    				    , P_CUR nvarchar2
    				    , P_CUR_BUY nvarchar2
    				    , P_GM_UNIT nvarchar2
    				)    		
    		******************************************************************************/
    		String sProc = "{call P_CATALOG_CN.SP_GEN_PRD_INFO_SKHY_CRT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    		
    		db.prepareStatement(sQry);    		
    		db.PexecuteQuery();
    		
    		dbcn.prepareCall(sProc);
    		
    		while (db.prs.next()){
    			
    			dbcn.CsetString(1, db.prs.getString("ENPRI_CD"));
    			dbcn.CsetString(2, db.prs.getString("BUSIP_SEQ"));
    			dbcn.CsetString(3, db.prs.getString("CUST_PRD_CD"));
    			dbcn.CsetString(4, db.prs.getString("COP"));
    			dbcn.CsetString(5, db.prs.getString("THIS_BSHOP_NM"));
    			dbcn.CsetString(6, db.prs.getString("PLIS_NM"));
    			dbcn.CsetString(7, db.prs.getString("PRD_CD"));
    			dbcn.CsetString(8, db.prs.getString("SHRT_DOC"));
    			dbcn.CsetString(9, db.prs.getString("MDL_HANG_NM"));
    			dbcn.CsetString(10, db.prs.getString("TMK_HANG_NM"));
    			dbcn.CsetString(11, db.prs.getString("MAKE_CO_NM"));
    			dbcn.CsetString(12, db.prs.getString("UNIT"));    			
    			dbcn.CsetString(13, db.prs.getString("THE_HOME"));
    			dbcn.CsetString(14, db.prs.getString("PLANT"));
    			dbcn.CsetString(15, db.prs.getString("SCO_NM"));
    			dbcn.CsetString(16, db.prs.getString("SCO_CD"));
    			dbcn.CsetString(17, db.prs.getString("BUY_PRI"));
    			dbcn.CsetString(18, db.prs.getString("SELL_PRI"));
    			dbcn.CsetString(19, db.prs.getString("IMG1"));
    			dbcn.CsetString(20, db.prs.getString("CUR"));
    			dbcn.CsetString(21, db.prs.getString("CUR_BUY"));
    			dbcn.CsetString(22, db.prs.getString("GM_UNIT"));
    		
    			dbcn.Cexecute();    			
    		}
    		    
    		dbcn.commit();
    		
    		String sQry2 = "";
    		String strEnpriCd="", strBusipSeq="", strCustPrdCd="";
    		
    		sQry2 += "SELECT ENPRI_CD, BUSIP_SEQ, CUST_PRD_CD ";
    		sQry2 += "FROM ( ";
    		sQry2 += "    SELECT ENPRI_CD, BUSIP_SEQ, CUST_PRD_CD FROM GEN_PRD_INFO_SKHY ";
    		sQry2 += "    MINUS ";
    		sQry2 += "    SELECT ENPRI_CD, BUSIP_SEQ, CUST_PRD_CD FROM V_GEN_PRD_INFO_SKHY@L_METS ";
    		sQry2 += "    ) ";
    		
    		String sProc2="";    		
    		
    		/*******************************************************************************
    			PURPOSE : 한국 하이닉스 카탈로그와 동기화 - 삭제
    			REVISIONS :
    			DATE        Author      Description
    			---------   ----------  ----------------------------------------------------
    			150627      mhhong      1. Create this Function       
    		*******************************************************************************/
    		/*******************************************************************************
    		P_CATALOG_CN.SP_GEN_PRD_INFO_SKHY_DEL (
    				P_ENPRI_CD VARCHAR2
    				, P_BUSIP_SEQ VARCHAR2
    				, P_CUST_PRD_CD VARCHAR2
    		)
    		*******************************************************************************/
    		    		
    		dbcn.prepareStatement(sQry2);
    		
    		sProc2 = "{call P_CATALOG_CN.SP_GEN_PRD_INFO_SKHY_DEL(?,?,?)}";
    		dbcn.prepareCall(sProc2);    		
    		
    		dbcn.PexecuteQuery();
    		
    		while (dbcn.prs.next()){
    			strEnpriCd = dbcn.prs.getString("ENPRI_CD");
    			strBusipSeq = dbcn.prs.getString("BUSIP_SEQ");
    			strCustPrdCd = dbcn.prs.getString("CUST_PRD_CD");
    			
    			dbcn.CsetString(1, strEnpriCd);
    			dbcn.CsetString(2, strBusipSeq);
    			dbcn.CsetString(3, strCustPrdCd);
    			
    			dbcn.Cexecute();
    			
    		}
    		
    		dbcn.commit();
    		
    	}    	
    	catch(Exception ex){
    		
    		
    	}
    	finally{
    		db.DB_DisConn();
    		dbcn.DB_DisConn();
    		
    	}
    }
}
