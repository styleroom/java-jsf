package ru.util;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Named(value = "util")
@RequestScoped
@SuppressWarnings({"unchecked"})
public class UtilBean implements Serializable {
  
  protected HttpServletRequest request;
  protected HttpServletResponse response;
  protected FacesContext facesContext;
  protected ServletContext servletContext;
  protected HttpSession sess;
  private Calendar calendar;
  
  public String enterLogin() {
    String str = "";
    return str;
  }
  
  public String enterPassword() {
    String str = "";
    return str;
  }  
  
  public String unixTimeStamp() {
    String str = "";
    return str + System.currentTimeMillis() / 1000L;
  }
  
  public String translitRustoEng(String str) {
    SortedMap map = this.translitGetMap();
    str = str.toLowerCase();
    String[] rus = str.split("");
    String key = "";
    String result = "";
    for(int i = 0; i<rus.length; i++) {
      key = rus[i];
      if(map.containsKey(key)) {
        result += map.get(key);
      } else {
        result += key;
      }
    } 
    return result;
  }  
  
  public SortedMap translitGetMap() {
    SortedMap<String, String> map = new TreeMap<String,String>();
    String[] rus = this.alphabet_rus;
    String[] eng = this.alphabet_eng;
    for(int i = 0; i<rus.length; i++) {
      map.put(rus[i], eng[i]);
    } 
    return map;
  }
  
  protected final String[] alphabet_rus = new String[]{
    "а","б","в","г","д","е","ё","ж","з","и","й","к","л","м","н","о","п",
    "р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ь","э","ю","я"
  };
  
  protected final String[] alphabet_eng = new String[]{
    "a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p",
    "r","s","t","u","f","h","c","ch","sh","sch","","y","","e","yu","ya"
  };  
  
  public String dateCalendar(String what) {
    String str = ""; 
    this.calendar = new GregorianCalendar();
    
    String  year        = "" + calendar.get(Calendar.YEAR),
            month       = String.format("%02d", calendar.get(Calendar.MONTH) + 1),
            day         = String.format("%02d", calendar.get(Calendar.DATE)),
            hours       = String.format("%02d", calendar.get(Calendar.HOUR)),
            minutes     = String.format("%02d", calendar.get(Calendar.MINUTE)),
            seconds     = String.format("%02d", calendar.get(Calendar.SECOND)),
            day_of_week = String.format("%02d", calendar.get(Calendar.DAY_OF_WEEK) - 1);
    
    String  day_week           = this.days_of_week().get(day_of_week),
            rus_date_digit     = day + "." + month + "." + year,
            rus_date_word      = day + " " + this.monthes().get(month) + " " + year,
            rus_datetime_digit = day + "." + month + "." + year + " " + hours + ":" + minutes + ":" + seconds;
    
    HashMap<String, String> map = new HashMap();    
    map.put("year", year);
    map.put("day_of_week", day_week);
    map.put("rus_date_digit", rus_date_digit);
    map.put("rus_date_word", rus_date_word);
    map.put("rus_datetime_digit", rus_datetime_digit);
    map.put("date_good_01", rus_date_digit + ", " + day_week);
    map.put("date_good_02", rus_date_word + ", " + day_week);
    map.put("reg_minsec", minutes + seconds);
    map.put("reg_unix_timestamp", this.unixTimeStamp());
    
    return map.get(what);
  }
  
  private HashMap<String, String> days_of_week() {
    return new HashMap(){{
      put("01","понедельник");
      put("02","вторник");
      put("03","среда");
      put("04","четверг");
      put("05","пятница");
      put("06","суббота");
      put("07","воскресенье");
    }};
  }  
  
  private HashMap<String, String> monthes() {
    return new HashMap(){{
      put("01","января");
      put("02","февраля");
      put("03","марта");
      put("04","апреля");
      put("05","мая");
      put("06","июня");
      put("07","июля");
      put("08","августа");
      put("09","сентября");
      put("10","октября");
      put("11","ноября");
      put("12","декабря");
    }};
  }
  
  public static String propGetOne(String path, String key) throws IOException {
    String str = "";
    Properties prop = UtilBean.loadProp(path);   
    if(prop.containsKey(key)) {
      str = prop.getProperty(key);
    }    
    return str;
  }
  
  public static Properties loadProp(String path) throws IOException {
    Properties prop = new Properties();
    path = "ru/util/" + path + ".properties";
    try {
      prop.load(UtilBean.class.getClassLoader().getResourceAsStream(path));
    } catch(NullPointerException e) {}       
    return prop;
  }
  
  public static String encryptPassword(String password)
      throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] bs;
 
    messageDigest.reset();
    bs = messageDigest.digest(password.getBytes());
 
    StringBuilder stringBuilder = new StringBuilder();
 
    //шестнадцатеричное кодирование дайджеста
    for (int i = 0; i < bs.length; i++) {
      String hexVal = Integer.toHexString(0xFF & bs[i]);
      if (hexVal.length() == 1) {
        stringBuilder.append("0");
      }
      stringBuilder.append(hexVal);
    }
 
    return stringBuilder.toString();
  } 
  
  public String createRusDateTime(String dt) {
    String str = "";
    //2013-04-04 16:52:07.0
    String mins = dt.substring(14, 16);
    String hour = dt.substring(11, 13);
    String day = dt.substring(8, 10);
    String month = dt.substring(5, 7);
    String year = dt.substring(0, 4);
    return day+"."+month+"."+year + " " + hour + ":" + mins;
  }  
  
  public String createRusDate(String dt) {
    String str = "";
    //2013-04-30
    String day = dt.substring(8, 10);
    String month = dt.substring(5, 7);
    String year = dt.substring(0, 4);
    return day+"."+month+"."+year;
  }
  
/**
 * создание описания нужной длины
 */  
public String createDescr(String content,int number) {
  String str = "";
  str = content.trim();
  if(str.length() == 0) {
    str = "";
  } else {
    str = UtilBean.stripTags(content);
    if(str.length() <= number) {
      number = str.length();
    } 
    if(str.length() != 0) {
      str = str.substring(0, number);
      str = str.substring(0, str.lastIndexOf(" ")).trim();
      // удалили переводы строк
      str = str.replaceAll("\r\n", "");
      // все пробелы которых 2 и более привели к одному
      str = str.replaceAll("\\s{2,}", " "); 
      // удалили ненужные символы
      str = str.replaceAll("[():-]", "");
    }      
  }
  return str;
}
  
  public String createAnons(String content,int number) {
    String str = "";
    str = UtilBean.stripTags(content);
    str = str.trim();
    if(str.length() <= number) {
      number = str.length();
    }    
    if(str.length() != 0) {
      str = str.substring(0, number);
      str = str.substring(0, str.lastIndexOf(" ")).trim() + "...";
    } else {
      str = "";
    }
    return str;
  }
  
  public String stripHtmlTags(String str) {
    String s = "";
    s = str.replaceAll("\\<.*?\\>", "");
    s = s.replaceAll("\\&.*?\\;", "");
    return s;
  }  
  
  /**
   * удаляем все HTML-теги и HTML-сущности (мнемоники)
   */
  public static String stripTags(String str) {
    String s = "";
    s = str.replaceAll("\\<.*?\\>", "");
    s = s.replaceAll("\\&.*?\\;", "");
    return s;
  }
  
   public void initContext() {
    this.facesContext = FacesContext.getCurrentInstance();
    this.servletContext = (ServletContext) facesContext.getExternalContext().getContext(); 
    this.request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    this.response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
   }  

  public UtilBean() {}
  
   public void refresh() {
     this.initContext();
     String viewId = this.facesContext.getViewRoot().getViewId();
     ViewHandler handler = this.facesContext.getApplication().getViewHandler();
     UIViewRoot root = handler.createView(this.facesContext, viewId);
     root.setViewId(viewId);
     this.facesContext.setViewRoot(root);    
   }  
  
  public String blahblahblah() {
    return "It's method blahblahblah from UtilBean";
  }
  
  public void gotoUrl(String url) throws IOException {
    response.sendRedirect(url);
  }
  
  public String checkSess() {
    String str = "";
    this.sess = (HttpSession) this.facesContext.getExternalContext().getSession(true);
    Enumeration enumer = sess.getAttributeNames();
    while(enumer.hasMoreElements()) {
      str += enumer.nextElement();
    }
    return str;
  }  
  
  public String cookieNow() {
    String str = "";
    this.initContext();
    Map<String, Object> myCookies = this.facesContext.getExternalContext().getRequestCookieMap();
    Cookie[] cookies = request.getCookies();
    int len = cookies.length;
    for(int i = 0; i<len;i++) {
      Cookie cookie = cookies[i];
      str += cookie.getName() + " = " + cookie.getValue() + "<br/>";
    }
    return str;
  }  
  
}
