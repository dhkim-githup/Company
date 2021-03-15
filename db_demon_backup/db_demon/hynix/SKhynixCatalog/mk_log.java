package SKhynixCatalog;

import java.io.*;
import java.text.*;

public class mk_log {

	private static String today;           				 	//�ý��� ��¥
	//private static String dir = "/home/mro/Err_log/";	//�α� ȭ���� ����� ���丮.
	private static String dir = "./Err_log/";	//�α� ȭ���� ����� ���丮.


	private  String filename;      					// �α�ȭ���̸�.
	private static String err ;            					// Error�� �߻��� ȭ���̸��� �߻��� �ð�.
	private static String errtime ;       					// Error�� �߻��� �ð�.
	private static boolean isExist = false;     			// �α� ȭ���� �����ϴ����� ����.

	// ȭ�ϸ��� �����ϴ� �Լ�.(Ex : 20001025.log)
	public mk_log() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		today = sdf.format(new java.util.Date());
		filename = today+".log";
		ErrorTime() ;
	}

	// �ڽŸ��� ȭ�ϸ��� �����ϴ� �Լ�.(Ex : Hoon1025.log)
	public mk_log(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		today = sdf.format(new java.util.Date());
		filename = str+today+".log";
		ErrorTime() ;
	}


	// Error�� �� �ð��� Check�ϱ����� �Լ�.
	public void ErrorTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
		errtime = sdf.format(new java.util.Date()) ;
	}

	// JSP���� Call�ϴ� �Լ�.
	public void Write(String filename, String elog) {
		mk_log log = new mk_log() ;
		log.file_write(filename, elog) ;
	}

	// JSP���� Call�ϴ� �Լ�.
	// �Էµ� Flag�� ���� �α��ۼ� ���� ó��
	public void Write(int write_flag, String filename, String elog) {
		if(write_flag == 1) {
			mk_log log = new mk_log() ;
			log.file_write(filename, elog) ;
		}
	}

	// �ڽ��� Title�� �̸��� ���� ��� JSP���� Call�ϴ� �Լ�.
	public void Write(String name, String filename, String elog) {
		mk_log log = new mk_log(name) ;
		log.file_write(filename, elog) ;
	}

	// �ڽ��� Title�� �̸��� ���� ��� JSP���� Call�ϴ� �Լ�.
	// �Էµ� Flag�� ���� �α��ۼ� ���� ó��
	public void Write(int write_flag, String name, String filename, String elog) {
		if(write_flag == 1) {
			mk_log log = new mk_log(name) ;
			log.file_write(filename, elog) ;
		}
	}

	// ȭ���� �����ϸ� ����ȭ�Ͽ� �α׸� �߰�, �׷��� ������ ���ο� ȭ���� �߰����� �α׸� �ִ´�.
	private void file_write(String errfile, String s) {
		int filesize = errfile.length();

		if(filesize < 26){	// ���ϸ��� �������̸� �����ϱ� ����.
			for(int i = 0; i < (25-filesize) ; i++){
				errfile += " " ;
			}
		}

		File file1 = new File(dir+filename);  //���ϰ�ü�� �����Ҷ� �α����ϸ����� ����

		if(file1.exists()){                   // ������ �ý��ۿ� �ִ��� Ȯ��
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
