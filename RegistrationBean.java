package ru.database;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;
import javax.enterprise.context.Conversation;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.NonexistentConversationException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import ru.util.UtilBean;

@Named(value = "reg")
@ConversationScoped
@SuppressWarnings({"unchecked"})
public class RegistrationBean extends DataBaseBean implements Serializable {
  
  protected UtilBean utb = new UtilBean();
  
  public Boolean newUSERDB() throws SQLException, NoSuchAlgorithmException {
    Boolean bool = false;
    
    String ust = utb.unixTimeStamp();
    String ust_start = ust.subSequence(0,4).toString();
    String ust_end = ust.subSequence(6,10).toString();
    String str = utb.translitRustoEng(this.last_name);    
    
    String ent_login = str + ust_end;
    String ent_password = ust_end + "-" + ust_start; 
           ent_password = UtilBean.encryptPassword(ent_password);
    
    this.sql = "INSERT INTO `A_USERS` "
                + "VALUES ("
                + "'"+ent_login+"', "
                + "'"+ent_password+"'"
                + ")";
    this.prepstmt = conn.prepareStatement(this.sql);
    int upd1 = prepstmt.executeUpdate(sql);    

    this.sql = "INSERT INTO `A_USERS_GROUPS` "
                + "VALUES ("
                + "'guest', "
                + "'"+ent_login+"'"
                + ")";
    this.prepstmt = conn.prepareStatement(this.sql);
    int upd2 = prepstmt.executeUpdate(sql);    

    this.sql = "INSERT INTO `A_USERS_PROFILES` "
                + "VALUES ("
                + "'"+ent_login+"', "
                + "'"+last_name+"', "
                + "'"+first_name+"', "
                + "'"+middle_name+"', "
                + "'"+position+"', "
                + "'"+company+"', "
                + "'"+phone+"', "
                + "'"+email+"', "
                + "'"+region+"', "
                + "NOW()"
                + ")";
    this.prepstmt = conn.prepareStatement(this.sql);
    int upd3 = prepstmt.executeUpdate(sql);

    if(upd1 == 1 && upd2 == 1 && upd3 == 1) {
      bool = true;
    }
    
    return bool;
  }

  
  protected String enter_login;
  public String getEnter_login() {
    String ust = utb.unixTimeStamp();
    String ust_end = ust.subSequence(6,10).toString();
    String str = utb.translitRustoEng(this.last_name);
    return str + ust_end;
  }
  public void setEnter_login(String enter_login) {
    this.enter_login = enter_login;
  }
  
  protected String enter_password;
  public String getEnter_password() {    
    String ust = utb.unixTimeStamp();
    String ust_end = ust.subSequence(6,10).toString();
    String ust_start = ust.subSequence(0,4).toString();    
    return ust_end + "-" + ust_start;
  }
  public void setEnter_password(String enter_password) {
    this.enter_password = enter_password;
  }  
  
  private @Inject Conversation conversation;  
  // Control start and end of conversation
  /**conversation begin*/
  public void start() throws ServletException { 
    if (conversation.isTransient()) {
      conversation.setTimeout(60000);
      conversation.begin();
    }
  }
  /**conversation end*/
  public void end() throws ServletException { 
    if (!conversation.isTransient()) {
      conversation.end();
    }
  }   

  private String check_all_fields;
  public String getCheck_all_fields() { 
    check_all_fields = this.last_name + this.first_name + this.middle_name +
            this.position + this.company + this.phone + this.email + this.region;
    String trim = check_all_fields.replaceAll("null", "");
    return trim;
  }
  
  protected String last_name;
  public String getLast_name() { return last_name; }
  public void setLast_name(String last_name) { this.last_name = last_name; }
  
  protected String first_name;
  public String getFirst_name() { return first_name; }
  public void setFirst_name(String first_name) { this.first_name = first_name;}

  protected String middle_name;
  public String getMiddle_name() { return middle_name; }
  public void setMiddle_name(String middle_name) { this.middle_name = middle_name; }

  protected String position;
  public String getPosition() { return position; }
  public void setPosition(String position) { this.position = position; }

  protected String company;
  public String getCompany() { return company; }
  public void setCompany(String company) { this.company = company; }
  
  protected String phone;
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  protected String email;
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  protected String region;
  public String getRegion() { return region; }
  public void setRegion(String region) { this.region = region; }

  public RegistrationBean() {}
  
  public String newUSER() throws NoSuchAlgorithmException {
    
    String ust = utb.unixTimeStamp();
    String ust_start = ust.subSequence(0,4).toString();
    String ust_end = ust.subSequence(6,10).toString();
    String str = utb.translitRustoEng(this.last_name);    
    
    String ent_login = str + ust_end;
    String ent_password = ust_end + "-" + ust_start;
    
    return "записываем в A_USERS enter_login и MD5(enter_password): " + ent_login + " = " + UtilBean.encryptPassword(ent_password) + "<hr/>"
    + "записываем в A_USERS_GROUPS guest и enter_login: guest = " + ent_login + "<hr/>"
    + "записываем в A_USERS_PROFILES enter_login и остальные данные: " + ent_login + "<br/>"
            + "Фамилия: " + this.last_name + "<br/>"
            + "Имя: " + this.first_name + "<br/>"
            + "Отчество: " + this.middle_name + "<br/>"
            + "Должность: " + this.position + "<br/>"
            + "Компания: " + this.company + "<br/>"
            + "Телефон: " + this.phone + "<br/>"
            + "Почта: " + this.email + "<br/>"
            + "Фамилия: " + this.region + "<br/>";
  } 
  
}
