package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.permission.GroupEntityData;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class FriendListCMD extends SubCommand {
  public FriendListCMD() {
    super("list", false);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cloudPlayer, String[] args) {
    String val, pUuid = p.getUniqueId().toString();
    if (args.length > 1) {
      val = args[1];
    } else {
      val = "1";
    } 
    try {
      int size = Integer.parseInt(val);
      if (size < 1) {
        p.sendMessage(Friends.prefix + "§cKeine gültige Seitenanzahl.");
        return;
      } 
      List<String> friendsUuids = BungeeUtils.getFriends(pUuid);
      if (friendsUuids.size() == 0) {
        p.sendMessage(Friends.prefix + "§cDu hast leider keine Freunde :'c");
        return;
      } 
      p.sendMessage("§8» §8§l§m-----§3§l Freunde §8§l(§eS. " + size + "§8§l) §8§l§m-----§8 «");
      int max = (size - 1) * 10 + 10;
      if (max > friendsUuids.size())
        max = friendsUuids.size(); 
      if (max == 0) {
        p.sendMessage(Friends.prefix + "§cDiese Seite existiert nicht!");
        return;
      } 
      String result = "";
      for (int i = (size - 1) * 10; i < max; i++) {
        String friendUuid = friendsUuids.get(i);
        String name = CloudAPI.getInstance().getOfflinePlayer(UUID.fromString(friendUuid)).getName();
        String pfx = CloudAPI.getInstance().getPermissionGroup(((GroupEntityData)CloudAPI.getInstance().getOfflinePlayer(name).getPermissionEntity().getGroups().iterator().next()).getGroup()).getPrefix();
        pfx = (pfx.contains("&l") || pfx.contains("§l")) ? pfx.substring(0, 4) : pfx.substring(0, 2);
        if (BungeeCord.getInstance().getPlayer(name) == null) {
          result = result + " §8" + name + " §7ist §coffline";
        } else {
          result = result + " " + pfx + name + " §7ist §aonline §7auf §6" + BungeeCord.getInstance().getPlayer(name).getServer().getInfo().getName();
        } 
        result = result + "\n";
      } 
      p.sendMessage(result);
    } catch (NumberFormatException ex) {
      p.sendMessage(Friends.prefix + "§c<Seite> muss eine Zahl sein!");
    } catch (Exception ex) {
      ex.printStackTrace();
      p.sendMessage(Friends.prefix + "§cFehler: " + ex.getMessage());
    } 
  }
}
