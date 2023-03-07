package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendToggleJumpCMD extends SubCommand {
  public FriendToggleJumpCMD() {
    super("togglejump", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    boolean jump = BungeeUtils.getSetting(pUuid, "jump");
    BungeeUtils.setSetting(pUuid, "jump", !jump);
    String a = String.valueOf(jump ? "de" : "") + "aktiviert";
    p.sendMessage(String.valueOf(Friends.prefix) + "Nachspringen " + a + "!");
  }
}
