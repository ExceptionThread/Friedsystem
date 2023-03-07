package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.cmds.FriendsCMD;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FriendDenyAllCMD extends SubCommand {
  public FriendDenyAllCMD() {
    super("denyall", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    try {
      for (String request : BungeeUtils.getRequests(pUuid)) {
        FriendsCMD.executeArg(p, cp, "deny", new String[] { "deny", CloudAPI.getInstance().getPlayerName(
                UUID.fromString(request)) });
      } 
    } catch (Exception ex) {
      ex.printStackTrace();
      p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + ex.getMessage());
    } 
  }
}
