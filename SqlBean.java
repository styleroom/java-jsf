package ru.database;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;;

@Named(value = "sql")
@RequestScoped
@SuppressWarnings({"unchecked"})
public class SqlBean extends DataBaseBean implements Serializable { 
   
  public List showLastNews(int number) throws SQLException {
    this.arr.clear();
    this.sql = "SELECT * FROM `news` WHERE `show` = 'yes' ORDER BY `dt` DESC LIMIT " + number;
    this.prepstmt = conn.prepareStatement(this.sql);
    this.res = prepstmt.executeQuery();     
    this.resNext(true,0);
    return this.arr;     
  }   
   
  public HashMap showLastTender() throws SQLException {
    this.row.clear();  
    this.sql = "SELECT * FROM `tenders` WHERE `show` = 'yes' ORDER BY `id` DESC LIMIT 1";
    this.prepstmt = conn.prepareStatement(this.sql);
    this.res = prepstmt.executeQuery();
    this.resNext(false,0);     
    return this.row;
  }   
   
  public List showLastTenders(int number) throws SQLException {
    this.arr.clear();
    this.sql = "SELECT * FROM `tenders` WHERE `show` = 'yes' ORDER BY `dt_end` DESC LIMIT " + number;
    this.prepstmt = conn.prepareStatement(this.sql);
    this.res = prepstmt.executeQuery();     
    this.resNext(true,0);
    return this.arr;     
  }   
  
  /**
   * Returns one page from table
   * 
   * @return HashMap
   */   
   
  public HashMap showPage() throws SQLException {
    this.row.clear();
    this.initContext();
    String page = this.request.getServletPath();    
    this.sql = "SELECT * FROM `pages` WHERE `link` = '"+page+"'";
    this.prepstmt = conn.prepareStatement(this.sql);
    this.res = prepstmt.executeQuery();
    this.resNext(false,0);     
    return this.row;
  }
  
  /**
   * Returns one row from table
   * 
   * @return HashMap
   */  
  
  public HashMap showRow(String sql) throws SQLException {
    this.row.clear();
    this.sql = sql;
    this.prepstmt = conn.prepareStatement(this.sql);
    this.res = prepstmt.executeQuery();
    this.resNext(false,0);     
    return this.row;
  }
  
  /**
   * Returns rows set from table
   * 
   * @return List
   */
   
  public List showArr(String sql) throws SQLException {
    this.arr.clear();
    this.sql = sql;
    this.prepstmt = conn.prepareStatement(this.sql);
    this.res = prepstmt.executeQuery();     
    this.resNext(true,0);
    return this.arr;     
  }
   
   /**
    * Creates anons from full text
    * and delete all html-tags too
    * 
    * @return string
    */
   
  protected String createAnons(String content,int number) {
      String str = "";
      number = number + 6;
      str = content.replaceAll("\\<.*?\\>", "");
      str = str.substring(0, number);
      str = str.substring(0, str.lastIndexOf(" ")).trim() + "...";
      return str;
  }
  
  /**
   * Iterates ResultSet and fill List or HashMap
   * 
   * @return List or HashMap
   */
   
  public void resNext(Boolean what,int anons) throws SQLException {

    while(this.res.next()) {

      HashMap map = new HashMap();

      try {
          map.put("id", res.getString(this.id));
      } catch(SQLException e) {
          map.put("id", null);
      } 

      try {
          map.put("link", res.getString(this.link));
      } catch(SQLException e) {
          map.put("link", null);
      }        

      try {
          map.put("descr", res.getString(this.descr));
      } catch(SQLException e) {
          map.put("descr", null);
      }

      try {
          map.put("name", res.getString(this.name));
      } catch(SQLException e) {
          map.put("name", null);
      }

      try {
          if(anons == 0) {
              this.text = res.getString(this.content);
          } else {
              this.text = this.createAnons(res.getString(this.content),anons);
          }
          map.put("content", this.text);
      } catch(SQLException e) {
          map.put("content", null);
      }

      try {
          map.put("dt", res.getString(this.dt));
      } catch(SQLException e) {
          map.put("dt", null);
      }
      
      try {
          map.put("dt_start", res.getString(this.dt_start));
      } catch(SQLException e) {
          map.put("dt_start", null);
      }  
      
      try {
          map.put("dt_end", res.getString(this.dt_end));
      } catch(SQLException e) {
          map.put("dt_end", null);
      }      

      try {
          map.put("show", res.getString(this.show));
      } catch(SQLException e) {
          map.put("show", null);
      }        

      if(what) {
          arr.add(map);
      } else {
          this.row = map;
      }

    }

  }     
  
  public SqlBean() {}
}
