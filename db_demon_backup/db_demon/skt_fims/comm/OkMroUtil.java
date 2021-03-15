package comm;

public class OkMroUtil {
	
	/*문자열 파일 구분자 (\n, |) 제외*/
	public static String checkRowString(String s) {
		s = checkNull(s);
		s = s.replace("\n", " ").replace("|", " ");
		
		return s;
	}
	
	/**
	 * 널값처리
	 * @param s
	 * @return
	 */
	public static String checkNull(String s) {
		if (s == null)
			return "";
		else
			return trim(s);
	}

	/** 
	 * 한글에대한 'ㄱ'빈값을 없애준다.
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		int i = 0;
		char ac[] = s.toCharArray();
		int j = ac.length;
		int k;
		for (k = j; i < k && (ac[i] <= ' ' || ac[i] == '\u3000'); i++)
			;
		for (; i < k && (ac[k - 1] <= ' ' || ac[k - 1] == '\u3000'); k--)
			;
		return i <= 0 && k >= j ? s : s.substring(i, k);
	}

}
