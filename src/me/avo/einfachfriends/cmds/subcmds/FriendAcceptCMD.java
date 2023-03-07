package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FriendAcceptCMD extends SubCommand {
  public FriendAcceptCMD() {
    super("accept", true);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    String val = args[1];
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
    BungeeUtils.acceptRequest(pUuid, fromUuid.toString());
    BungeeUtils.removeRequest(fromUuid.toString(), pUuid);
    BungeeUtils.removeRequest(pUuid, fromUuid.toString());
    p.sendMessage(Friends.prefix + "§aDu hast die Freundschaftsanfrage von " + 
        Friends.getDisplay(fromUuid.toString()) + " §aangenommen!");
    ProxiedPlayer t = BungeeCord.getInstance().getPlayer(fromUuid);
    if (t != null)
      t.sendMessage(Friends.prefix + "§aDu bist nun mit " + Friends.getDisplay(p) + " §abefreundet!"); 
  }
}
