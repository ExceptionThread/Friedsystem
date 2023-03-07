package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class FriendRequestsCMD extends SubCommand {
  public FriendRequestsCMD() {
    super("requests", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cloudPlayer, String[] args) {
    String pUuid = p.getUniqueId().toString();
    try {
      List<String> requestsUuids = BungeeUtils.getRequests(pUuid);
      int shown = 0;
      if (requestsUuids.size() == 0) {
        p.sendMessage(Friends.prefix + "Du hast keine Freundschaftsanfragen.");
        return;
      } 
      String reqs = Friends.prefix;
      for (String requestUuid : requestsUuids) {
        shown++;
        if (shown == 1)
          reqs = reqs + "Du hast " + requestsUuids.size() + " Freundschaftsanfragen von "; 
        if (shown < 4) {
          reqs = reqs + Friends.getDisplay(requestUuid) + "§7, ";
          continue;
        } 
        reqs = reqs + "§7 und §e" + (requestsUuids.size() - 3) + " §7weiteren Spielern.";
      } 
      if (reqs.endsWith("§7, "))
        reqs = reqs.substring(0, reqs.length() - "§7, ".length()); 
      p.sendMessage(reqs);
    } catch (Exception ex) {
      ex.printStackTrace();
      p.sendMessage(Friends.prefix + "§cFehler: " + ex.getMessage());
    } 
  }
}
