package me.avo.einfachfriends.utils;

import me.avo.einfachfriends.MySQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BungeeUtils {
  public static MySQL mysql;
  
  private static final HashMap<String, List<String>> friendsCache = new HashMap<>();

  private static final HashMap<String, List<String>> requestsCache = new HashMap<>();
  
  private static final HashMap<String, HashMap<String, Boolean>> settingsCache = new HashMap<>();
  
  public static List<String> getFriends(String uuid) {
    if (friendsCache.containsKey(uuid))
      return friendsCache.get(uuid); 
    List<String> friends = new ArrayList<>();
    try {
      Exception exception2, exception1 = null;
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    friendsCache.put(uuid, friends);
    return friends;
  }
  
  public static void removeFriend(String uuid, String friendUuid, boolean auto) {
    List<String> friends = getFriends(uuid);
    friends.remove(friendUuid);
    friendsCache.put(uuid, friends);
    mysql.update("DELETE FROM friends WHERE uuid=? AND friend=?;", new Object[] { uuid, friendUuid });
    if (!auto)
      removeFriend(friendUuid, uuid, true); 
  }
  
  public static void addFriend(String uuid, String newFriendUuid, boolean auto) {
    List<String> friends = getFriends(uuid);
    if (!friends.contains(newFriendUuid))
      friends.add(newFriendUuid); 
    friendsCache.put(uuid, friends);
    mysql.update("INSERT INTO friends VALUES (?, ?);", new Object[] { uuid, newFriendUuid });
    if (!auto)
      addFriend(newFriendUuid, uuid, true); 
  }
  
  public static HashMap<String, Boolean> getSettings(String uuid) {
    if (settingsCache.containsKey(uuid))
      return settingsCache.get(uuid); 
    HashMap<String, Boolean> settings = new HashMap<>();
    settingsCache.put(uuid, settings);
    return settings;
  }
  
  public static void setSetting(String uuid, String setting, boolean toggled) {
    HashMap<String, Boolean> settings = new HashMap<>(getSettings(uuid));
    settings.put(setting.toLowerCase(), Boolean.valueOf(toggled));
    settingsCache.put(uuid, settings);
    mysql.update("DELETE FROM settings WHERE uuid=? AND setting=?;", new Object[] { uuid, setting.toLowerCase() });
    mysql.update("INSERT INTO settings VALUES (?, ?, ?);", new Object[] { uuid, setting.toLowerCase(), Integer.valueOf(toggled ? 1 : 0) });
  }
  
  public static List<String> getRequests(String uuid) {
    if (requestsCache.containsKey(uuid))
      return requestsCache.get(uuid); 
    List<String> requests = new ArrayList<>();
    try {
      Exception exception2, exception1 = null;
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    requestsCache.put(uuid, requests);
    return requests;
  }
  
  public static void acceptRequest(String uuid, String requesterUuid) {
    removeRequest(uuid, requesterUuid);
    addFriend(uuid, requesterUuid, false);
  }
  
  public static void denyRequest(String uuid, String requesterUuid) {
    removeRequest(uuid, requesterUuid);
  }
  
  public static void request(String uuid, String requesterUuid) {
    List<String> requests = getRequests(uuid);
    requests.add(requesterUuid);
    requestsCache.put(uuid, requests);
    mysql.update("INSERT INTO requests VALUES (?, ?);", new Object[] { uuid, requesterUuid });
  }
  
  public static void removeRequest(String uuid, String requesterUuid) {
    List<String> requests = getRequests(uuid);
    requests.remove(requesterUuid);
    requestsCache.put(uuid, requests);
    mysql.update("DELETE FROM requests WHERE uuid=? AND request=?;", new Object[] { uuid, requesterUuid });
  }
  
  public static boolean getSetting(String uuid, String setting) {
    return ((Boolean)getSettings(uuid).getOrDefault(setting, Boolean.valueOf(true))).booleanValue();
  }
  
  public static boolean hasRequestFrom(String uuid, String fromUuid) {
    return getRequests(uuid).contains(fromUuid);
  }
  
  public static boolean isFriendWith(String uuid, String withUuid) {
    return getFriends(uuid).contains(withUuid);
  }
}
