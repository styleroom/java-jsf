package ru.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Named(value = "dbBean")
@RequestScoped
@SuppressWarnings({"unchecked"})
public class DataBaseBean implements Serializable {
  
  @Resource(name="jdbc/mmkom_tender")

  protected DataSource ds;
  protected Connection conn;
  protected PreparedStatement prepstmt;
  protected ResultSet res;
  
  protected HashMap<String,String> row = new HashMap<String,String>();
  protected List<HashMap> arr = new ArrayList<HashMap>();
  protected List<String> list = new ArrayList<String>();
  
  protected HttpServletRequest request;
  protected HttpServletResponse response;
  protected FacesContext facesContext;
  protected ServletContext servletContext;
  
  protected String  sql = "",
                    table = "",
                    text = "",
                    id = "id", 
                    link = "link", 
                    descr = "descr",
                    name = "name",                   
                    content = "content", 
                    dt = "dt",
                    dt_start = "dt_start",
                    dt_end = "dt_end",
                    show = "show";
  
  /**
   * Create access to several contextes:
   * FacesContext,
   * ServletContext,
   * HttpServletRequest,
   * HttpServletResponse
   */  
  
   public void initContext() {
    this.facesContext = FacesContext.getCurrentInstance();
    this.servletContext = (ServletContext) facesContext.getExternalContext().getContext(); 
    this.request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    this.response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
   }  
  
  /**
   * Opens database connection
   */
   
  public void dbOpen() throws SQLException {
    this.conn = this.ds.getConnection();
  }
  
  /**
   * Closes database connection
   * and prepareStatement
   * and executeQuery result
   */

  public void dbClose() throws SQLException {
    try {
      this.res.close();
      this.prepstmt.close();
    } catch(NullPointerException e) {
    }finally {
      this.conn.close();
    }    
  }   

  public DataBaseBean() {}
}
