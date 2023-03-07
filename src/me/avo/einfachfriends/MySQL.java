package me.avo.einfachfriends;

import java.sql.*;

public class MySQL {
  private Connection con;
  
  public MySQL() {
    try {
      String host = "127.0.0.1";
      int port = 3306;
      String db = "friends";
      String user = "dev";
      String password = "u!y7]bd#2Q4B(6M}";
      this
        .con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/friends?autoReconnect=true", "dev", "u!y7]bd#2Q4B(6M}");
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  void setupSQL() {
    update("CREATE TABLE IF NOT EXISTS friends (uuid VARCHAR(64), friend VARCHAR(64));");
    update("CREATE TABLE IF NOT EXISTS requests (uuid VARCHAR(64), request VARCHAR(64));");
    update("CREATE TABLE IF NOT EXISTS settings (uuid VARCHAR(64), setting VARCHAR(24), toggled INT(2));");
  }
  
  public Connection getConnection() {
    return this.con;
  }
  
  public boolean isConnected() {
    return (this.con != null);
  }
  
  public void disconnect() {
    try {
      this.con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  public ResultSet queryNext(String sql, Object... preparedParameters) {
    try {
      ResultSet rs = query(sql, preparedParameters);
      rs.next();
      return rs;
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    } 
  }
  
  public ResultSet query(String sql, Object... preparedParameters) {
    try {
      PreparedStatement ps = this.con.prepareStatement(sql);
      int id = 1;
      for (Object preparedParameter : preparedParameters) {
        ps.setObject(id, preparedParameter);
        id++;
      } 
      return ps.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public ResultSet query(String sql) {
    try {
      return this.con.prepareStatement(sql).executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void update(String sql, Object... preparedParameters) {
    try (PreparedStatement ps = this.con.prepareStatement(sql)) {
      int id = 1;
      for (Object preparedParameter : preparedParameters) {
        ps.setObject(id, preparedParameter);
        id++;
      } 
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    } 
  }
  
  public void update(String sql) {
    try (PreparedStatement ps = this.con.prepareStatement(sql)) {
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    } 
  }
}
