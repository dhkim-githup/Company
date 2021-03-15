package comm;

import java.util.StringTokenizer;
import java.util.Vector;

public class OkMroUtil {
	/**
	 * �ΰ�ó��
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
	 * �ΰ�ó��
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
	 * �ѱۿ����� '��'���� �����ش�.
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
     * ���ڸ� �޾� ���ϴ� ��¥ �������� ��ȯ�Ͽ� ��ȯ�Ѵ�.
     * ��)str2dateformat("20050607","-");
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
     * substring()�ϴٰ� ������ �߻��ϸ� ""����..
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
	 * �����Ϻ��� ���ñ����� �Ⱓ�� ���Ѵ�.
	 *
	 * @param year ������ ��
	 * @param month ������ ��
	 * @param day ������ ��
	 * @param hour ������ ��
	 * @param minute ������ ��
	 * @return �Ⱓ�� 1/1000��(ms)��
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
	 * �����Ϻ��� �����ϱ����� �Ⱓ�� ���Ѵ�.
	 *
	 * @param from ������
	 * @param to ������
	 * @return �Ⱓ�� 1/1000��(ms)
	 */
	public static long getPeriod(java.util.Date from, java.util.Date to) {
		return to.getTime() - from.getTime();
	}



  /**
   * ���ڿ��� "bar"�� �������� �и��Ͽ� ���ڿ� �迭�� �Ҵ�
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
	 * dhkim ���ڿ��� �����Ͽ� �����ϴ� �Լ�
	 * @param value ù��° �Ķ���ͷ� ��Ʈ����
	 * @param divis �ι�°�ٶ����ͷ� ��Ʈ���� ������
	 * @return ���Ͱ� ����
	 */
  public static Vector vgetSplitStr(String value, String divis) {
      int fromIndex = 0;
      int toIndex   = 0;

      // ���ͼ���
      Vector vSplit=new Vector();
      // �� �� ��������
      value=value.trim();
      // ��ü��������
      int iValue=value.length();

      // divis ���ڰ� ���������� ��ȯ
      while (value.indexOf(divis)>=0) {
      	// ó�� ���鹮���� ��ġ�� toIndex�� ǥ��
          toIndex  = value.indexOf(divis);
          // ù��° ���ڿ� ���� ( ���Ϳ� ���� )
          vSplit.addElement(value.substring(fromIndex, toIndex));
          // ����� ���ڿ��� ������ ���ڿ� ����
          value=value.substring(toIndex, iValue);
          // �� �� ��������
          value=value.trim();
          // ��ü������ �� ���
          iValue=value.length();
      }
      // ������ ���ڿ��� ���Ϳ� ����
      value=value.trim();
      vSplit.addElement(value);
      // ���� ���ϰ� (����)
      return vSplit;
  }

//	���� ���ڿ��� ġȯ�ϴ� �Լ�
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

//	ġȯ�ؾ� �� �׸���� �Ѱ����� ��Ƽ� ó��
	public static String convertString(String str) {
	String strTemp;

	str = checkNull(str);
	//strTemp = replace (str, "\'", "\\'");
	strTemp = replace (str, "\'", "��");
	strTemp = replace (strTemp, "&", "&amp;");
	strTemp = replace (strTemp, "\"", "&quot;");
	strTemp = replace (strTemp, "<", "&lt;");
	strTemp = replace (strTemp, ">", "&gt;");

	//return checkNull(strTemp);
	return strTemp.trim();
	}
	/**
	 * �����ϴ� �Լ� dhkim
	 * @param strA
	 * @return
	 */
	public static String numberCut(String strA){
		int intLen = strA.length(); // ��ü����
		int intIndex = strA.indexOf("."); // �Ҽ�����ġ
		String strRe="";

	try{
		if(intIndex>0){ // �Ҽ����� �ִٸ�
			//strRe =  strA.substring(intIndex+1,intLen);
			// ù°�ڸ��� ��������
			strRe =  strA.substring(intIndex+1,intIndex+2);
			if("".equals(strRe)){ strRe="0"; } // ���� ���ٸ�
		}else{ // ���ٸ�
			strRe ="0";
		}
	}catch(Exception e){
		strRe ="0";
	}
		return strRe;
	}

	/**
	 *
	 * @param strTotalRow : ������ ��ü ����Ÿ�� �ο찹��
	 * @param strRow : �� ȭ�鿡 �ѷ��� �ο찹��
	 * @param strCurrent : ����ȭ���� ������ �ѹ�
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
	 * @param strTotalRow : ������ ��ü ����Ÿ�� �ο찹��
	 * @param strRow : �� ȭ�鿡 �ѷ��� �ο찹��
	 * @param strCurrent : ����ȭ���� ������ �ѹ�
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
