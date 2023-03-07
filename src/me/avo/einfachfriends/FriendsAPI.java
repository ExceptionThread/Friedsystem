package me.avo.einfachfriends;

import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class FriendsAPI {
  public static List<String> getFriends(String uuid) {
    if (uuid == null)
      throw new NullPointerException("uuid is null"); 
    return BungeeUtils.getFriends(uuid);
  }
  
  public static List<String> getRequests(String uuid) {
    if (uuid == null)
      throw new NullPointerException("uuid is null"); 
    return BungeeUtils.getRequests(uuid);
  }
  
  public static void addFriend(ProxiedPlayer player, ProxiedPlayer newFriend) {
    if (player == null || newFriend == null)
      throw new NullPointerException("argument player or newFriend is null"); 
    addFriend(player.getUniqueId().toString(), newFriend.getUniqueId().toString());
  }
  
  public static void addFriend(String uuid, String newFriendUUID) {
    checkArgs(uuid, newFriendUUID, "uuid", "newFriendUUID");
    BungeeUtils.addFriend(uuid, newFriendUUID, false);
  }
  
  public static void removeFriend(String uuid, String friendUUID) {
    checkArgs(uuid, friendUUID, "uuid", "friendUUID");
    BungeeUtils.removeFriend(uuid, friendUUID, false);
  }
  
  public static void removeRequest(String uuid, String requesterUuid) {
    checkArgs(uuid, requesterUuid, "uuid", "requesterUuid");
    BungeeUtils.removeRequest(uuid, requesterUuid);
  }
  
  public static boolean hasRequest(String uuid, String requesterUuid) {
    checkArgs(uuid, requesterUuid, "uuid", "requesterUuid");
    return BungeeUtils.hasRequestFrom(uuid, requesterUuid);
  }
  
  public static boolean isFriend(String uuid, String friendUuid) {
    checkArgs(uuid, friendUuid, "uuid", "friendUuid");
    return BungeeUtils.isFriendWith(uuid, friendUuid);
  }
  
  private static void checkArgs(String a, String b, String aN, String bN) throws NullPointerException, IllegalArgumentException {
    if (a == null || b == null)
      throw new NullPointerException("argument " + aN + " or " + bN + " is null"); 
    try {
      UUID.fromString(a);
      UUID.fromString(b);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new IllegalArgumentException(String.valueOf(aN) + " or " + bN + " is not a valid UUID");
    } 
    if (a.equalsIgnoreCase(b))
      throw new IllegalArgumentException(String.valueOf(aN) + " cannot be the same as " + bN); 
  }
}
