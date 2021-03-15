package comm.dev;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtils_dev {
	
	public static FTPClient ftpclient;

	public FTPUtils_dev(){
		connectFIMSServer();
	}
	
	public static void connectFIMSServer() {

		String ftpUrl = "203.236.17.151";
		String ftpId = "fimsdev";
		String ftpPwd = "vlatm77*";
		int ftpPort = 21;
		
		
		try {
			System.out.print("FTP접속(테스트)>> "+ ftpUrl);
			
			ftpclient = new FTPClient();

			ftpclient.setControlEncoding("UTF-8"); // 인코딩 설정
			
			// 서버 접속
			ftpclient.connect(ftpUrl, ftpPort);
			int reply = ftpclient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) {
				ftpclient.disconnect();
				System.out.println("FTP 연결 거부");
				return;
			}else {
				System.out.println("FTP 연결 성공");
				ftpclient.login(ftpId, ftpPwd); // 로그인
			}
			
		} catch (IOException e) {
			if(ftpclient.isConnected()) {
				try {
					ftpclient.disconnect();
				}catch (IOException f) {}
			}
		}
	}
	
	public boolean isConnected() {
		return ftpclient.isConnected();
	}
	
	public void disconnectFIMSServer() {
		// ftp 로그아웃
		try {
			ftpclient.logout();
			ftpclient.disconnect();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean uploadFileToFIMS(String dirName, String fileName) {
		
		String remonteFilePath = "/NAS01/MM/IF_TEST/"+dirName;
		String localPath = "./tempFile/";
		
		boolean result = false;
		
		try {
			
			ftpclient.changeWorkingDirectory(remonteFilePath); // 작업 디렉토리 변경
			ftpclient.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅
			
			System.out.println("파일업로드 시작 >>");
			File tempFile = new File(localPath+fileName);
			FileInputStream tempFIS = new FileInputStream(tempFile);
			
			result = ftpclient.storeFile(remonteFilePath+fileName, tempFIS);
			
			if(result) {
				System.out.println("전송 완료: "+remonteFilePath+fileName);
			}else {
				System.out.println("전송실패: "+localPath+fileName);
			}
			
			tempFIS.close();
			//tempFile.delete();
			
			
		} catch (IOException e) {
			if(ftpclient.isConnected()) {
				try {
					ftpclient.disconnect();
				}catch (IOException f) {}
			}
		}
		return result;
		
	}
	
	
	public void readFileFromFTP(String dirName, String fileName) {
		
		String remonteFilePath = "/NAS01/MM/IF_TEST/"+dirName;
		String localPath = "./tempFile/";
		
		try {
			
			ftpclient.changeWorkingDirectory(remonteFilePath); // 작업 디렉토리 변경
			ftpclient.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅
			
			System.out.println("파일다운로드 시작 >>");
			File tempFile = new File(localPath+fileName);
			FileOutputStream tempFOS = new FileOutputStream(tempFile);
			
			boolean result = ftpclient.retrieveFile(remonteFilePath+fileName, tempFOS);
			
			if(result) {
				System.out.println("다운로드 완료: "+remonteFilePath+fileName);
				
				tempFOS.close();
			}else {
				System.out.println("다운로드 실패(파일이 존재하지 않습니다.): "+remonteFilePath+fileName);
			}
			
		} catch(IOException e) {
			if(ftpclient.isConnected()) {
				try {
					ftpclient.disconnect();
				}catch (IOException f) {}
			}
		}
	}
	
	public String[] getNewFileList(String dirName) {
		String remonteFilePath = "/NAS01/MM/IF_TEST/"+dirName;
		
		String[] fileList = null;
		
		try {
			if (ftpclient.changeWorkingDirectory(remonteFilePath)) {
				fileList = ftpclient.listNames();
			}
			
		} catch (IOException e) {
			if(ftpclient.isConnected()) {
				try {
					ftpclient.disconnect();
				}catch (IOException f) {}
			}
		}
		return fileList;
	}

	public boolean renameFile(String oldFileName, String newFileName) {
		String remonteFilePath = "/NAS01/MM/IF_TEST/";		
		boolean result = false;
		
		try {
			result = ftpclient.rename(remonteFilePath+oldFileName, remonteFilePath+newFileName);
			
			if(result) {
				System.out.println("파일 이동 완료: "+remonteFilePath+newFileName);
			}else {
				System.out.println("파일 이동 실패: "+remonteFilePath+oldFileName);
			}
			
		} catch (IOException e) {
			if(ftpclient.isConnected()) {
				try {
					ftpclient.disconnect();
				}catch (IOException f) {}
			}
		}
		return result;
	}
}
