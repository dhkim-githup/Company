package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileUtils {
	public static void writeBufferFile(String fileName, ArrayList<String> dataList) throws IOException {
		String dirPath = "./tempFile/";
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(dirPath+fileName, true));
			
			for(int i=0; i<dataList.size(); i++) {
				bw.write(dataList.get(i));
				bw.newLine();
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bw.close();
		}
	}
	
	
	public static ArrayList<String[]> readBufferFile(String fileName) throws IOException {
		ArrayList<String[]> readData = new ArrayList<String[]>();
		
		String[] lineArray = null;
		
		String dirPath = "./tempFile/";
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			fr = new FileReader(dirPath+fileName);
			br = new BufferedReader(fr);
		}  catch(Exception e) {
			System.out.println("ERROR_readBufferFile():: 지정된 파일을 찾을 수 없습니다.  "+dirPath+fileName);
			return null;
			//e.printStackTrace();
		}	
			
		String line = "";
			
		while((line = br.readLine())!= null){
			lineArray = line.split("\\|");
			readData.add(lineArray);
		}

		fr.close();
		br.close();
		
		File tempFile = new File(dirPath+fileName);
		if(tempFile.exists()) {
			if(!tempFile.delete()) {
				System.out.println("파일삭제 실패");
			}
		}
		
		
		return readData;
		
	}
	
	public static String makeCurrentTime() {
		String timeString = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
		timeString = sdf.format(new java.util.Date());

		return timeString;
	}
	
	
	public static ArrayList<String> makeRowString(ResultSet selectResult) {
		ArrayList<String> returnStr = new ArrayList<String>();

		try {
			ResultSetMetaData rsmd = selectResult.getMetaData();

			int colCount = rsmd.getColumnCount();

			while (selectResult.next()) {
				String rowStr = "";

				for (int i = 1; i <= colCount; i++) {
					rowStr += OkMroUtil.checkRowString(selectResult.getString(i)) + "|";
				}

				// 끝에 파이프라인 지우기
				rowStr = rowStr.substring(0, rowStr.length() - 1);

				returnStr.add(rowStr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return returnStr;
	}

}
