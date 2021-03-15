/***************************************
* This program is Jog Demon
* File Name : Jog.java
* Date : 2004 . 12 . 15
* Made by Kim Do Wan
* 사용하는 함수
* 1. jog_stick() - DB 관련 처리
* 2010.02.22 컨넥션을 연결했다 끊도록 한다.
* 2014.10.24 dhkim 프로시져에서 전체 처리를 하도록 변경
*********************************/

//import java.net.*;
//import java.io.*;
//import java.awt.*;
import java.text.SimpleDateFormat;
//import java.util.*;
//import java.sql.*;


// 빈즈 불러서 사용
//import comm.DB_Use;
//import comm.Han;
//import comm.mk_log;

public class Jog
{

	static private String prog_nm = "Jog:SilJuk Make";
	static private comm.DB_Use  db ;  //  DB 연결하는 빈즈
	//static private comm.mk_log	log ;  //  DB 연결하는 빈즈
	public Jog ()
	{
		System.out.println("HI Welcome to Manager\n");
	}
    public static void main(String arg[])
	{

 	    Jog jog = new Jog(); //서버 인스턴트를 한개 생성합니다.

		String year="";
		String Hour="";
		int rtn = 0;
		int int_sleep=1000; // 쉬는시간
		int_sleep = int_sleep*10; // 10초   (1000 = 1 초)

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss"); // HH 24시간 hh 12시간 계산
		String today = "";
		try{
   		
   		    int chk = 0;
   		    while(true){
   		    	today = sf.format(new java.util.Date());
				//log.Write("Jog", prog_nm, "start");
				year 	= today.substring(4,8).toString();
				Hour 	= today.substring(8,14).toString();

				rtn 	= jog.jog_stick(); // 실행 함수 호출
				if(rtn > 0){
					System.out.println("TIME LOOP :  DATE : " + year+ ":"+ Hour  + " - "+rtn+"\n");
					chk = 0;
				}else{
					chk ++;
					if(chk == 600){
						chk = 0;
						System.out.println("TIME LOOP :  DATE : " + year+ ":"+ Hour  + " - 경과! ");
					}
				}
				//log.Write("Jog", prog_nm, "end "+rtn);

				Thread.sleep(int_sleep); // 위시간만큼 휴식

	   		}
	   	}catch(Exception e){
			System.out.println(e+ " Jog Error . \n");
		}
   }

	/***********************************************************
		DB 연결해서 사용하는 곳
	************************************************************/
	public  int jog_stick() {
		db = new comm.DB_Use();  //  DB 연결하는 빈즈
		//log = new comm.mk_log();  //  DB 연결하는 빈즈

		String qry = "";
		int Count = 0;
		try{
			// DB 연결
			if(db.conn == null){
				db.CUST_ID = "00002345"; db.PROG_NM = prog_nm;
				db.DB_Conn();
				System.out.println("db connecting !!");
			}
			
				//log.Write("Jog", prog_nm, "P_JOTT.up_jott_demon Start");				
				System.out.println("P_JOTT.up_jott_demon ---Start");	
			db.prepareCall("{call P_JOTT.up_jott_demon(1)}");				
			db.Cexecute();
			db.commit();
			Count++;				
				System.out.println("P_JOTT.up_jott_demon ---End");	

		}catch(Exception e){
			try{
			db.rollback();
			}catch(Exception er){}
			System.out.println(e+ " jog_stick Error . \n");
		    //log.Write("Jog", prog_nm, "P_JOTT.up_jott_demon Err"+e);
		}finally{
			db.DB_DisConn();
			System.out.println("DB DisConn !!");
		}

		return Count;

	} // End jog_stick

} // End Jog
