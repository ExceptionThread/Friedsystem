package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendToggleMsgCMD extends SubCommand {
  public FriendToggleMsgCMD() {
    super("togglemsg", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    boolean msg = BungeeUtils.getSetting(pUuid, "msg");
    BungeeUtils.setSetting(pUuid, "msg", !msg);
    String a = String.valueOf(msg ? "de" : "") + "aktiviert";
    p.sendMessage(String.valueOf(Friends.prefix) + "Privatnachrichten " + a + "!");
  }
}
