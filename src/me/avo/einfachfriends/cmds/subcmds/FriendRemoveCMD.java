package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FriendRemoveCMD extends SubCommand {
  public FriendRemoveCMD() {
    super("remove", true);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    String val = args[1];
    try {
      UUID senderUuid = p.getUniqueId();
      UUID uniqueId = CloudAPI.getInstance().getPlayerUniqueId(val);
      if (uniqueId == null) {
        p.sendMessage(Friends.prefix + "§cDieser Spieler war noch nie auf dem Netzwerk.");
        return;
      } 
      OfflinePlayer offlinePlayer = CloudAPI.getInstance().getOfflinePlayer(uniqueId);
      if (offlinePlayer != null && offlinePlayer.getPermissionEntity() != null) {
        if (BungeeUtils.isFriendWith(pUuid, uniqueId.toString())) {
          BungeeUtils.removeFriend(senderUuid.toString(), uniqueId.toString(), false);
          p.sendMessage(Friends.prefix + "Du hast die Freundschaft mit " + val + " aufgelöst!");
          ProxiedPlayer t = BungeeCord.getInstance().getPlayer(val);
          if (t != null)
            t.sendMessage(Friends.prefix + 
                Friends.getDisplay(p) + " hat die Freundschaft mit dir aufgelöst!"); 
        } else {
          p.sendMessage(Friends.prefix + "§cDu bist nicht mit " + val + " befreundet!");
        } 
      } else {
        p.sendMessage(Friends.prefix + "§cDieser Spieler war noch nie auf dem Netzwerk.");
      } 
    } catch (Exception ex) {
      ex.printStackTrace();
      p.sendMessage(Friends.prefix + "§cFehler: " + ex.getMessage());
    } 
  }
}
