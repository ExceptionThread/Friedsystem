package me.avo.einfachfriends;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static MySQL mysql;
  
  public static List<String> getFriends(String uuid) {
    try {
      List<String> friends = new ArrayList<>();
      ResultSet rs = mysql.getConnection().prepareStatement("SELECT * FROM friends WHERE uuid='" + uuid + "';")
        .executeQuery();
      if (rs.next()) {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = rs.getString("friends").split(";")).length, b = 0; b < i; ) {
          String friend = arrayOfString[b];
          if (friend.length() >= 10)
            friends.add(friend); 
          b++;
        } 
      } 
      return friends;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } 
  }
  
  public static String getFriendsString(String uuid0) {
    String friends = "";
    for (String uuid : getFriends(uuid0))
      friends = String.valueOf(friends) + uuid + ";"; 
    if (friends.endsWith(";"))
      friends = friends.substring(0, friends.length() - 1); 
    return friends;
  }
  
  public static void setFriends(String uuid, List<String> friendsUuids) {
    String friends = "";
    for (String uuid0 : friendsUuids)
      friends = String.valueOf(friends) + uuid0 + ";"; 
    if (friends.endsWith(";"))
      friends = friends.substring(0, friends.length() - 1); 
    mysql.update("UPDATE friends SET friends='" + friends + "' WHERE uuid='" + uuid + "';");
  }
  
  public static void removeFriend(String uuid, String friendUuid, boolean auto) {
    List<String> friends = getFriends(uuid);
    friends.remove(friendUuid);
    setFriends(uuid, friends);
    if (!auto)
      removeFriend(friendUuid, uuid, true); 
  }
  
  public static void addFriend(String uuid, String newFriendUuid, boolean auto) {
    List<String> friends = getFriends(uuid);
    if (!isFriendWith(uuid, newFriendUuid))
      friends.add(newFriendUuid); 
    setFriends(uuid, friends);
    if (!auto)
      addFriend(newFriendUuid, uuid, true); 
  }
  
  public static List<String> getRequests(String uuid) {
    try {
      List<String> requests = new ArrayList<>();
      ResultSet rs = mysql.getConnection().prepareStatement("SELECT * FROM requests WHERE uuid='" + uuid + "';")
        .executeQuery();
      if (rs.next()) {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = rs.getString("requests").split(";")).length, b = 0; b < i; ) {
          String request = arrayOfString[b];
          if (request.length() >= 10)
            requests.add(request); 
          b++;
        } 
      } 
      return requests;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } 
  }
  
  public static void acceptRequest(String uuid, String newFriendUuid) {
    List<String> requests = getRequests(uuid);
    requests.remove(newFriendUuid);
    setRequests(uuid, requests);
    addFriend(uuid, newFriendUuid, false);
  }
  
  public static void denyRequest(String uuid, String newFriendUuid) {
    List<String> requests = getRequests(uuid);
    requests.remove(newFriendUuid);
    setRequests(uuid, requests);
  }
  
  public static void request(String uuid, String requesterUuid) {
    List<String> requests = getRequests(uuid);
    requests.add(requesterUuid);
    setRequests(uuid, requests);
  }
  
  public static boolean hasRequestFrom(String uuid, String fromUuid) {
    return getRequests(uuid).contains(fromUuid);
  }
  
  public static void setRequests(String uuid, List<String> requestsUuids) {
    String requests = "";
    for (String uuid0 : requestsUuids)
      requests = String.valueOf(requests) + uuid0 + ";"; 
    if (requests.endsWith(";"))
      requests = requests.substring(0, requests.length() - 1); 
    mysql.update("UPDATE requests SET requests='" + requests + "' WHERE uuid='" + uuid + "';");
  }
  
  public static boolean isFriendWith(String uuid, String withUuid) {
    return getFriends(uuid).contains(withUuid);
  }
}
