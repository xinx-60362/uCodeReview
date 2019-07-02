//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package open.ucodereview.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

  private static final ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
  };
  private static final ThreadLocal<SimpleDateFormat> dateIdformat = new ThreadLocal<SimpleDateFormat>() {
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("yyyyMMddHHmmssms");
    }
  };

  public static String getDateString() {
    return timeFormat.get().format(new Date());
  }

  public static String getTimeStamp() {
    return dateIdformat.get().format(new Date());
  }
}
