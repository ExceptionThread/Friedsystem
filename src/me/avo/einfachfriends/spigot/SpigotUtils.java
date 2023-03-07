package me.avo.einfachfriends.spigot;

import me.avo.einfachfriends.MySQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpigotUtils {
  private static MySQL mysql;
  
  static void setMySQL(MySQL mysql) {
    SpigotUtils.mysql = mysql;
  }
  
  static List<String> getFriends(String uuid) {
    List<String> friends = new ArrayList<>();
    try {
      Exception exception2, exception1 = null;
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    return friends;
  }
  
  static HashMap<String, Boolean> getSettings(String uuid) {
    HashMap<String, Boolean> settings = new HashMap<>();
    Exception exception2, exception1 = null;
    return settings;
  }
  
  static List<String> getRequests(String uuid) {
    List<String> requests = new ArrayList<>();
    try {
      Exception exception2, exception1 = null;
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    return requests;
  }
  
  static boolean getSetting(String uuid, String setting) {
    return ((Boolean)getSettings(uuid).getOrDefault(setting, Boolean.valueOf(true))).booleanValue();
  }
  
  static boolean hasRequestFrom(String uuid, String fromUuid) {
    return getRequests(uuid).contains(fromUuid);
  }
  
  static boolean isFriendWith(String uuid, String withUuid) {
    return getFriends(uuid).contains(withUuid);
  }
}
