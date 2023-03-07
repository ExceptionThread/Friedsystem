package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FriendJumpCMD extends SubCommand {
  public FriendJumpCMD() {
    super("jump", true);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    String val = args[1];
    UUID withUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
    if (withUuid == null) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§c" + val + " war noch nie auf dem Netzwerk.");
      return;
    } 
    if (!BungeeUtils.isFriendWith(pUuid, withUuid.toString())) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§c" + val + " ist nicht in deiner Freundesliste!");
    } else if (BungeeCord.getInstance().getPlayer(withUuid) == null) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§c" + val + " ist nicht online!");
    } else {
      boolean jump = BungeeUtils.getSetting(withUuid.toString(), "jump");
      if (!jump) {
        p.sendMessage(String.valueOf(Friends.prefix) + "§c" + val + " hat Nachspringen deaktiviert!");
        return;
      } 
      p.sendMessage(String.valueOf(Friends.prefix) + "Du springst nun zu " + val + "!");
      p.connect(BungeeCord.getInstance().getPlayer(val).getServer().getInfo());
    } 
  }
}
