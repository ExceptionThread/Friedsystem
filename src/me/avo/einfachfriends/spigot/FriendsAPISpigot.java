package me.avo.einfachfriends.spigot;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FriendsAPISpigot {
  public static List<String> getFriends(String uuid) {
    if (uuid == null)
      throw new NullPointerException("uuid is null"); 
    return SpigotUtils.getFriends(uuid);
  }
  
  public static List<String> getRequests(String uuid) {
    if (uuid == null)
      throw new NullPointerException("uuid is null"); 
    return SpigotUtils.getRequests(uuid);
  }
  
  public static void addFriend(String uuid, String newFriendUUID) {
    checkArgs(uuid, newFriendUUID, "uuid", "newFriendUUID");
    FriendsMain.instance.sendBungee("addfriend", new String[] { uuid, newFriendUUID });
  }
  
  public static void removeFriend(String uuid, String friendUUID) {
    checkArgs(uuid, friendUUID, "uuid", "friendUUID");
    FriendsMain.instance.sendBungee("removefriend", new String[] { uuid, friendUUID });
  }
  
  public static HashMap<String, Boolean> getSettings(String uuid) {
    checkArg(uuid, "uuid");
    return SpigotUtils.getSettings(uuid);
  }
  
  public static void setSetting(String uuid, String setting, boolean toggled) {
    checkArg(uuid, "uuid");
    if (setting == null)
      throw new IllegalArgumentException("setting can not be null"); 
    FriendsMain.instance.sendBungee("setsetting", new String[] { uuid, setting, String.valueOf(toggled) });
  }
  
  public static void addRequest(String uuid, String requesterUuid) {
    checkArgs(uuid, requesterUuid, "uuid", "requesterUuid");
    FriendsMain.instance.sendBungee("addrequest", new String[] { uuid, requesterUuid });
  }
  
  public static void removeRequest(String uuid, String requesterUuid) {
    checkArgs(uuid, requesterUuid, "uuid", "requesterUuid");
    FriendsMain.instance.sendBungee("removerequest", new String[] { uuid, requesterUuid });
  }
  
  public static boolean hasRequest(String uuid, String requesterUuid) {
    checkArgs(uuid, requesterUuid, "uuid", "requesterUuid");
    return SpigotUtils.hasRequestFrom(uuid, requesterUuid);
  }
  
  public static boolean isFriend(String uuid, String friendUuid) {
    checkArgs(uuid, friendUuid, "uuid", "friendUuid");
    return SpigotUtils.isFriendWith(uuid, friendUuid);
  }
  
  private static void checkArg(String a, String aN) throws NullPointerException, IllegalArgumentException {
    if (a == null)
      throw new NullPointerException("argument " + aN + " is null"); 
    try {
      UUID.fromString(a);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException(aN + " is not a valid UUID");
    } 
  }
  
  private static void checkArgs(String a, String b, String aN, String bN) throws NullPointerException, IllegalArgumentException {
    if (a == null || b == null)
      throw new NullPointerException("argument " + aN + " or " + bN + " is null"); 
    try {
      UUID.fromString(a);
      UUID.fromString(b);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException(aN + " or " + bN + " is not a valid UUID");
    } 
    if (a.equalsIgnoreCase(b))
      throw new IllegalArgumentException(aN + " cannot be the same as " + bN); 
  }
}
