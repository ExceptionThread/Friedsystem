package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FriendDenyCMD extends SubCommand {
  public FriendDenyCMD() {
    super("deny", true);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    String val = args[1];
    try {
      UUID fromUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
      if (fromUuid == null) {
        p.sendMessage(Friends.prefix + "§cDieser Spieler war noch nie auf dem Netzwerk.");
        return;
      } 
      if (!BungeeUtils.hasRequestFrom(pUuid, fromUuid.toString())) {
        p.sendMessage(Friends.prefix + "§cDu hast keine Anfrage von " + Friends.getDisplay(fromUuid.toString()) + " erhalten!");
        return;
      } 
      if (BungeeUtils.isFriendWith(pUuid, fromUuid.toString())) {
        p.sendMessage(Friends.prefix + "§cDu bist bereits mit diesem Spieler befreundet!");
        return;
      } 
      BungeeUtils.denyRequest(pUuid, fromUuid.toString());
      BungeeUtils.removeRequest(fromUuid.toString(), pUuid);
      BungeeUtils.removeRequest(pUuid, fromUuid.toString());
      p.sendMessage(Friends.prefix + "§aDu hast die Freundschaftsanfrage von " + 
          Friends.getDisplay(fromUuid.toString()) + " §aabgelehnt!");
      ProxiedPlayer t = BungeeCord.getInstance().getPlayer(fromUuid);
      if (t != null)
        t.sendMessage(Friends.prefix + Friends.getDisplay(p) + " §ahat deine Freundschaftsanfrage abgelehnt!"); 
    } catch (Exception ex) {
      ex.printStackTrace();
      p.sendMessage(Friends.prefix + "§cFehler: " + ex.getMessage());
    } 
  }
}
