package me.avo.einfachfriends;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import me.avo.einfachfriends.cmds.FriendsCMD;
import me.avo.einfachfriends.cmds.MsgCMD;
import me.avo.einfachfriends.cmds.ReplyCMD;
import me.avo.einfachfriends.listener.FriendsAPIListener;
import me.avo.einfachfriends.listener.FriendsInitListener;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashMap;
import java.util.UUID;

public class Friends extends Plugin {
  public static MySQL mysql;
  
  public static Friends instance;
  
  public static String prefix = " §8§l• §4§lFreunde §8§l» §e";
  
  public static HashMap<ProxiedPlayer, String> replies = new HashMap<>();
  
  public void onEnable() {
    instance = this;
    this.mysql = new MySQL();
    init();
  }
  
  public void onDisable() {
    this.mysql.disconnect();
  }
  
  void init() {
    BungeeUtils.mysql = this.mysql;
    PluginManager pm = BungeeCord.getInstance().getPluginManager();
    BungeeCord.getInstance().registerChannel("syplex:friends");
    pm.registerListener(this, (Listener)new FriendsAPIListener());
    pm.registerListener(this, (Listener)new FriendsInitListener());
    pm.registerCommand(this, (Command)new FriendsCMD());
    pm.registerCommand(this, (Command)new MsgCMD("msg"));
    pm.registerCommand(this, (Command)new ReplyCMD("reply"));
  }
  
  public static String getDisplay(ProxiedPlayer p) {
    return getDisplay(p.getUniqueId().toString());
  }
  
  public static String getDisplay(String uuid) {
    OfflinePlayer op = CloudAPI.getInstance().getOfflinePlayer(UUID.fromString(uuid));
    return op.getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool())
      .getPrefix().substring(0, 2) + op.getName();
  }
}
