package ru.database;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

@Named(value = "access")
@SessionScoped
@SuppressWarnings({"unchecked"})
public class AccessBean extends DataBaseBean implements Serializable {
  
  protected HashMap<String,String> user = new HashMap<String,String>();
  public HashMap getUser() { return user; }  
  protected HttpSession sess;
  
  public HashMap userFIO() throws ServletException {
    HashMap map = new HashMap();
    HashMap user_map = this.userGetMap();
    this.initContext();
    this.sess = this.request.getSession();
    map.put("fio", user_map.get("last_name") + " " + user_map.get("first_name") + " " + user_map.get("middle_name"));
    map.put("io", user_map.get("first_name") + " " + user_map.get("middle_name"));
    map.put("i", user_map.get("first_name"));
    map.put("f", user_map.get("last_name"));
    return map;
  }
  
  public HashMap userGetMap() throws ServletException {
    this.initContext();
    this.sess = this.request.getSession();   
    HashMap map = new HashMap();
    try {
      map = (HashMap) this.sess.getAttribute("user_map");
    } catch(NullPointerException e){}
    return map;
  }
  
  public void userCheck() throws ServletException, SQLException {
    String name="",map="";    
    name = this.userPrincipal();
    this.initContext();
    this.sess = this.request.getSession();   
    
    try {
      map = this.sess.getAttribute("user_map").toString();
    } catch(NullPointerException e){}
  
    if(map.length() <=2 && name.length() > 2) {
      this.sql = "SELECT * FROM `A_USERS_PROFILES` WHERE `userid` = '"+name+"'";
      this.prepstmt = conn.prepareStatement(this.sql);
      this.res = prepstmt.executeQuery();   
      while(res.next()) {
        // `userid`, `last_name`, `first_name`, `middle_name`, `position`, `company`, `phone`, `mail`, `region`, `dt`
        this.user.put("userid", res.getString("userid"));
        this.user.put("last_name", res.getString("last_name"));
        this.user.put("first_name", res.getString("first_name"));
        this.user.put("middle_name", res.getString("middle_name"));
        this.user.put("position", res.getString("position"));
        this.user.put("company", res.getString("company"));
        this.user.put("phone", res.getString("phone"));
        this.user.put("mail", res.getString("mail"));
        this.user.put("region", res.getString("region"));
        this.user.put("dt", res.getString("dt"));
      }
      this.sess.setAttribute("user_map", this.user);    
    } 
    
  }
  
  public String userPrincipal() throws ServletException {
    String str = "",name="";
    this.initContext();
    try {
      str = this.request.getUserPrincipal().toString();
      if(!str.isEmpty()) {
        name = str;
      }      
    } catch(NullPointerException e) {}     
    return name;
  }
  
  public Boolean userIsLogged() throws ServletException {
    Boolean bool = false;
    try {
      if(this.userPrincipal().length() > 0) {
        bool = true;
      }
    } catch(NullPointerException e) {} 
    return bool;
  }
  
  public void userLogOut(String url) throws ServletException, IOException {
    this.initContext();
    this.request.logout();
    this.user.clear();
    this.sess = this.request.getSession();
    this.sess.invalidate();
    this.response.sendRedirect(url);
  }
  
  public String userSessionId() throws ServletException {
    String str = "";
    this.initContext();
    
    try {
      this.sess = this.request.getSession();
      str = this.sess.getId();
    } catch(NullPointerException e){}

    return str;
  }  
  
  public AccessBean() {}
  
}
