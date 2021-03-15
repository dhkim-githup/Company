package SKhynixCatalog;

import java.io.*;
import java.text.*;

public class mk_log {

	private static String today;           				 	//시스템 날짜
	//private static String dir = "/home/mro/Err_log/";	//로그 화일이 저장될 디렉토리.
	private static String dir = "./Err_log/";	//로그 화일이 저장될 디렉토리.


	private  String filename;      					// 로그화일이름.
	private static String err ;            					// Error가 발생한 화일이름과 발생한 시간.
	private static String errtime ;       					// Error가 발생한 시간.
	private static boolean isExist = false;     			// 로그 화일이 존재하는지의 여부.

	// 화일명을 결정하는 함수.(Ex : 20001025.log)
	public mk_log() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		today = sdf.format(new java.util.Date());
		filename = today+".log";
		ErrorTime() ;
	}

	// 자신만의 화일명을 결정하는 함수.(Ex : Hoon1025.log)
	public mk_log(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		today = sdf.format(new java.util.Date());
		filename = str+today+".log";
		ErrorTime() ;
	}


	// Error가 난 시간을 Check하기위한 함수.
	public void ErrorTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
		errtime = sdf.format(new java.util.Date()) ;
	}

	// JSP에서 Call하는 함수.
	public void Write(String filename, String elog) {
		mk_log log = new mk_log() ;
		log.file_write(filename, elog) ;
	}

	// JSP에서 Call하는 함수.
	// 입력된 Flag에 따라 로그작성 여부 처리
	public void Write(int write_flag, String filename, String elog) {
		if(write_flag == 1) {
			mk_log log = new mk_log() ;
			log.file_write(filename, elog) ;
		}
	}

	// 자신의 Title을 이름을 넣은 경우 JSP에서 Call하는 함수.
	public void Write(String name, String filename, String elog) {
		mk_log log = new mk_log(name) ;
		log.file_write(filename, elog) ;
	}

	// 자신의 Title을 이름을 넣은 경우 JSP에서 Call하는 함수.
	// 입력된 Flag에 따라 로그작성 여부 처리
	public void Write(int write_flag, String name, String filename, String elog) {
		if(write_flag == 1) {
			mk_log log = new mk_log(name) ;
			log.file_write(filename, elog) ;
		}
	}

	// 화일의 존재하면 기존화일에 로그를 추가, 그렇지 않으면 새로운 화일을 추가한후 로그를 넣는다.
	private void file_write(String errfile, String s) {
		int filesize = errfile.length();

		if(filesize < 26){	// 파일명의 일정길이를 유지하기 위함.
			for(int i = 0; i < (25-filesize) ; i++){
				errfile += " " ;
			}
		}

		File file1 = new File(dir+filename);  //파일객체를 생성할때 로그파일명으로 생성

		if(file1.exists()){                   // 파일이 시스템에 있는지 확인
			isExist = true;
		}

		err = "[" + errfile + " ; " + errtime + "]" ;

		FileWriter file = null;
		try{
			file = new FileWriter(dir + filename, isExist);

			file.write(err + s +"\n");
			file.flush();
			file.close();
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		finally {}
	}
}
