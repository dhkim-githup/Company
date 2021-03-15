package comm;

import java.util.StringTokenizer;
import java.util.Vector;

public class OkMroUtil {
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
	 * 널값처리
	 * @param s
	 * @param v
	 * @return
	 */
	public static String checkNull(String s, String v) {
		if (s == null)
			return v;
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


	/**
     * 문자를 받아 원하는 날짜 포멧으로 변환하여 반환한다.
     * 예)str2dateformat("20050607","-");
     * @param strValue
     * @param c
     * @return
     */
    public static String str2dtFormat(String strValue, String c) {
      String str = "";
      try {
        if (strValue.length() == 8) {
          str = strValue.substring(0, 4) + c + strValue.substring(4, 6) + c + strValue.substring(6, 8);
        }else if (strValue.length() == 6) {
            str = strValue.substring(0, 4) + c + strValue.substring(4, 6);
        } else {
          return strValue;
        }
        Integer.parseInt(strValue);
      } catch (Exception e) {
        System.out.println("str2dateformat() Exception ");
        System.out.println("ERRMSG : " + e.toString());
      }
      return str;
    }


    /**
     * substring()하다가 에러가 발생하면 ""리턴..
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static String substring(String data, int start, int end){
      String sRtn="";
      try{
        sRtn = data.substring(start,end);
      }catch(Exception e){};
      return sRtn;
    }
    public static String substring(String data, int start){
      String sRtn="";
      try{
        sRtn = data.substring(start);
      }catch(Exception e){};
      return sRtn;
    }


    /**
	 * 시작일부터 오늘까지의 기간을 구한다.
	 *
	 * @param year 시작한 년
	 * @param month 시작한 월
	 * @param day 시작한 일
	 * @param hour 시작한 시
	 * @param minute 시작한 분
	 * @return 기간의 1/1000초(ms)값
	 */
	public static long getPeriod(String yyyymmddhm_f, String yyyymmddhm_t) {
		int year_f = Integer.parseInt(substring(yyyymmddhm_f, 0,4));
		int month_f = Integer.parseInt(substring(yyyymmddhm_f, 4,6));
		int day_f = Integer.parseInt(substring(yyyymmddhm_f, 6,8));
		int hour_f = Integer.parseInt(substring(yyyymmddhm_f, 8,10));
		int minute_f = Integer.parseInt(substring(yyyymmddhm_f, 10,12));

		int year_t = Integer.parseInt(substring(yyyymmddhm_t, 0,4));
		int month_t = Integer.parseInt(substring(yyyymmddhm_t, 4,6));
		int day_t = Integer.parseInt(substring(yyyymmddhm_t, 6,8));
		int hour_t = Integer.parseInt(substring(yyyymmddhm_t, 8,10));
		int minute_t = Integer.parseInt(substring(yyyymmddhm_t, 10,12));


		java.util.Calendar meet_f = java.util.Calendar.getInstance();
		java.util.Calendar meet_t = java.util.Calendar.getInstance();
		meet_f.set(year_f, month_f-1, day_f, hour_f, minute_f);
		meet_t.set(year_t, month_t-1, day_t, hour_t, minute_t);

		return getPeriod(meet_f.getTime(), meet_t.getTime());
	}

	/**
	 * 시작일부터 종료일까지의 기간을 구한다.
	 *
	 * @param from 시작일
	 * @param to 종료일
	 * @return 기간의 1/1000초(ms)
	 */
	public static long getPeriod(java.util.Date from, java.util.Date to) {
		return to.getTime() - from.getTime();
	}



  /**
   * 문자열을 "bar"를 기준으로 분리하여 문자열 배열에 할당
   * @param bar
   * @param sRow
   * @return
   */
  public static String divideField(String bar, String sRow, int point)
    {
        if(sRow.indexOf(bar) < 0){
          return "";
        }
        String sTmpStr = "";

        if ((sRow.substring((sRow.length()-1), sRow.length())).equals(bar)) {
            sRow = sRow + " ";
        }

        for (int i = 0; i < sRow.length(); i++) {
            if (i < sRow.length()) {
                if (((sRow.substring(i, i+1)).equals(bar)) && ((sRow.substring(i+1, i+2)).equals(bar))) {
                    sTmpStr = sTmpStr + sRow.substring(i, i+1) + " ";
                }
                else {
                    sTmpStr = sTmpStr + sRow.substring(i, i+1);
                }
            }
        }
        sRow = sTmpStr;
        StringTokenizer stValue = new StringTokenizer(sRow, bar);
        String[] sRtnValue = new String[stValue.countTokens()];

        for (int i = 0; i < sRtnValue.length; i++) {
            sRtnValue[i] = stValue.nextToken();
        }

        return sRtnValue.length > point ? sRtnValue[point] : "";
    }

  /**
	 * dhkim 문자열을 구분하여 리턴하는 함수
	 * @param value 첫번째 파라메터로 스트링값
	 * @param divis 두번째바라페터로 스트링값 구분자
	 * @return 벡터값 리턴
	 */
  public static Vector vgetSplitStr(String value, String divis) {
      int fromIndex = 0;
      int toIndex   = 0;

      // 벡터선언
      Vector vSplit=new Vector();
      // 앞 뒤 공백제거
      value=value.trim();
      // 전체사이즈계산
      int iValue=value.length();

      // divis 문자가 있을때까지 순환
      while (value.indexOf(divis)>=0) {
      	// 처음 공백문자의 위치를 toIndex에 표시
          toIndex  = value.indexOf(divis);
          // 첫번째 문자열 저장 ( 벡터에 저장 )
          vSplit.addElement(value.substring(fromIndex, toIndex));
          // 추출된 문자열을 제외한 문자열 저장
          value=value.substring(toIndex, iValue);
          // 앞 뒤 공백제거
          value=value.trim();
          // 전체사이즈 재 계산
          iValue=value.length();
      }
      // 마지막 문자열을 벡터에 저장
      value=value.trim();
      vSplit.addElement(value);
      // 최종 리턴값 (벡터)
      return vSplit;
  }

//	실제 문자열을 치환하는 함수
	public static String replace(String str, String pattern, String replace) {
	int spos = 0;
	    int epos = 0;

	    StringBuffer result = new StringBuffer();

	    while ((epos = str.indexOf(pattern, spos)) >= 0) {
	        result.append(str.substring(spos, epos));
	        result.append(replace);
	        spos = epos+pattern.length();
	    }
	    result.append(str.substring(spos));
	    return result.toString();
	}

//	치환해야 할 항목들을 한곳으로 모아서 처리
	public static String convertString(String str) {
	String strTemp;

	str = checkNull(str);
	//strTemp = replace (str, "\'", "\\'");
	strTemp = replace (str, "\'", "´");
	strTemp = replace (strTemp, "&", "&amp;");
	strTemp = replace (strTemp, "\"", "&quot;");
	strTemp = replace (strTemp, "<", "&lt;");
	strTemp = replace (strTemp, ">", "&gt;");

	//return checkNull(strTemp);
	return strTemp.trim();
	}
	/**
	 * 절사하는 함수 dhkim
	 * @param strA
	 * @return
	 */
	public static String numberCut(String strA){
		int intLen = strA.length(); // 전체길이
		int intIndex = strA.indexOf("."); // 소숫점위치
		String strRe="";

	try{
		if(intIndex>0){ // 소숫점이 있다면
			//strRe =  strA.substring(intIndex+1,intLen);
			// 첫째자리만 가져오기
			strRe =  strA.substring(intIndex+1,intIndex+2);
			if("".equals(strRe)){ strRe="0"; } // 값이 없다면
		}else{ // 없다면
			strRe ="0";
		}
	}catch(Exception e){
		strRe ="0";
	}
		return strRe;
	}

	/**
	 *
	 * @param strTotalRow : 쿼리된 전체 데이타의 로우갯수
	 * @param strRow : 한 화면에 뿌려질 로우갯수
	 * @param strCurrent : 현재화면의 페이지 넘버
	 * @return
	 */
	public static String getPageList(String strTotalRow, String strRow, String strCurrent) {
		int totalNum = Integer.parseInt(strTotalRow);
	    int limitNum = Integer.parseInt(strRow);
	    int currentNum = Integer.parseInt(strCurrent);
	    int l = 10;
	    int totalPage = (int) Math.ceil((double) totalNum / (double) limitNum);
	    int danPage = (int)Math.ceil((double)totalPage / (double)l);
	    int prePage = (int) Math.ceil((double) currentNum / (double) l);
	    int danLocation = ((int) Math.ceil((double) currentNum / (double) l) - 1) * l + 1;
	    int curDanLocation = (danLocation + l) - 1 <= totalPage ? (danLocation + l) - 1 : totalPage;

	    StringBuffer stringbuffer = new StringBuffer();

	    String splitImg = "";
	    for (int j2 = danLocation; j2 <= curDanLocation; j2++){

	    	if(	j2 < 1 || j2 == curDanLocation){
	    		splitImg = "";
	    	}else{
	    		splitImg = " | ";
	    	}

	    	if (j2 == currentNum){
	        	stringbuffer.append("<b>" + j2 + "</b>" + splitImg);
	    	}
	      else{
	        stringbuffer.append(" <a href=\"javascript:pageList('" + j2
	            + "')\">" + j2 + "</a> " + splitImg);
	      }
		}


	    return stringbuffer.toString();
	  }

	/**
	 *
	 * @param strTotalRow : 쿼리된 전체 데이타의 로우갯수
	 * @param strRow : 한 화면에 뿌려질 로우갯수
	 * @param strCurrent : 현재화면의 페이지 넘버
	 * @return
	 */

	public static String getPageList2(String strTotalRow, String strRow, String strCurrent) {
		int totalNum = Integer.parseInt(strTotalRow);
	    int limitNum = Integer.parseInt(strRow);
	    int currentNum = Integer.parseInt(strCurrent);
	    int l = 10;
	    int totalPage = (int) Math.ceil((double) totalNum / (double) limitNum);
	    int danPage = (int)Math.ceil((double)totalPage / (double)l);
	    int prePage = (int) Math.ceil((double) currentNum / (double) l);
	    int danLocation = ((int) Math.ceil((double) currentNum / (double) l) - 1) * l + 1;
	    int curDanLocation = (danLocation + l) - 1 <= totalPage ? (danLocation + l) - 1 : totalPage;

	    StringBuffer stringbuffer = new StringBuffer();

	    if (prePage > 1){
			stringbuffer.append("<a href=\"javascript:pageList('"
				+ (danLocation - 1)
				+ "')\"><img src='/fullsize/imgs/icon/btn_left.gif' border=0 width=10 height=11 align=absmiddle></a>&nbsp;");
	    }else{
	    	stringbuffer.append("<img src='/fullsize/imgs/icon/btn_left.gif' border=0 width=10 height=11 align=absmiddle>&nbsp;");
	    }


	    for (int j2 = danLocation; j2 <= curDanLocation; j2++){
		}

	    if (prePage < danPage){
	      	stringbuffer.append("<a href=\"javascript:pageList('"
	              + (curDanLocation + 1)
	              + "')\"><img src='//fullsize/imgs/icon/btn_right.gif' border=0 width=10 height=11 align=absmiddle></a>");
	    }else{
	    	stringbuffer.append("<img src='/fullsize/imgs/icon/btn_right.gif' border=0 width=10 height=11 align=absmiddle>");
	    }
	    return stringbuffer.toString();
	  }

}
