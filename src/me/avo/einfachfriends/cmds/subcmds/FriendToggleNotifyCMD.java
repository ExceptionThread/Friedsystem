package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendToggleNotifyCMD extends SubCommand {
  public FriendToggleNotifyCMD() {
    super("togglenotify", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    boolean playNotifications = BungeeUtils.getSetting(pUuid, "play-notifications");
    BungeeUtils.setSetting(pUuid, "play-notifications", !playNotifications);
    String a = String.valueOf(playNotifications ? "de" : "") + "aktiviert";
    p.sendMessage(String.valueOf(Friends.prefix) + "Freundes-Benachrichtigungen " + a + "!");
  }
}
