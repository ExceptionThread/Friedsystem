package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendToggleCMD extends SubCommand {
  public FriendToggleCMD() {
    super("toggle", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    boolean requests = BungeeUtils.getSetting(pUuid, "requests");
    BungeeUtils.setSetting(pUuid, "requests", !requests);
    String a = String.valueOf(requests ? "de" : "") + "aktiviert";
    p.sendMessage(String.valueOf(Friends.prefix) + "Freundschaftsanfragen " + a + "!");
  }
}
