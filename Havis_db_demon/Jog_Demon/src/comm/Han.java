package comm;

import java.io.UnsupportedEncodingException;

public class Han{
    public Han() {}	
    public static String ko(String s) {
		try {
		   if (s != null)
				return (new String(s.getBytes("8859_1"),"EUC_KR"));
		   return s;
		} catch (UnsupportedEncodingException e) {
		   return "Encoding Error";
		}
    }

    public static String ko_d(String s) {
		try {
		   if (s != null)
				return (new String(s.getBytes("EUC_KR"),"8859_1"));
		   return s;
		} catch (UnsupportedEncodingException e) {
		   return "Decoding Error";
		}
    }

    public String ks(String s) {
		try{
			if (s != null)
				return (new String(s.getBytes("8859_1"),"KSC5601"));
			return s;
		} catch(Exception e) {
			return "Encoding Error";
		}
    }

    public String ks_d(String s) {
        try{
			if (s != null)
				return (new String(s.getBytes("KSC5601"),"8859_1"));
			return s;
		} catch(Exception e) {
			return "Decoding Error";
		}
    }
}
