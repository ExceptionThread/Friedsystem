package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendToggleOnlineCMD extends SubCommand {
  public FriendToggleOnlineCMD() {
    super("toggleonline", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    boolean onlineNotifiactions = BungeeUtils.getSetting(pUuid, "online-notifications");
    BungeeUtils.setSetting(pUuid, "online-notifications", !onlineNotifiactions);
    String a = String.valueOf(onlineNotifiactions ? "de" : "") + "aktiviert";
    p.sendMessage(String.valueOf(Friends.prefix) + "On-/Offline-Nachrichten " + a + "!");
  }
}
