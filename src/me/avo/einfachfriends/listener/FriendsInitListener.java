package me.avo.einfachfriends.listener;

import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.MySQL;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class FriendsInitListener implements Listener {
  public MySQL mysql;
  
  @EventHandler
  public void onDisconnect(PlayerDisconnectEvent e) {
    ProxiedPlayer p = e.getPlayer();
    messageFriends(p, Friends.prefix + Friends.getDisplay(p) + " §7ist nun §coffline§7!", "online-notifications");
  }
  
  @EventHandler
  public void onSwitch(ServerSwitchEvent e) {
    ProxiedPlayer p = e.getPlayer();
    messageFriends(p, Friends.prefix + Friends.getDisplay(p) + " §7spielt nun auf §6" + p.getServer().getInfo()
        .getName() + "§7!", "play-notifications");
  }
  
  @EventHandler
  public void onJoin(PostLoginEvent e) {
    if (this.mysql == null)
      this.mysql = Friends.instance.mysql; 
    ProxiedPlayer p = e.getPlayer();
    messageFriends(p, Friends.prefix + Friends.getDisplay(p) + " §7ist nun §aonline§7!", "online-notifications");
    List<String> requests = BungeeUtils.getRequests(p.getUniqueId().toString());
    if (requests.size() > 0)
      p.sendMessage(Friends.prefix + "Du hast noch " + requests.size() + " Freundschaftsanfragen offen!"); 
  }
  
  private void messageFriends(ProxiedPlayer p, String string, String setting) {
    List<String> friends = BungeeUtils.getFriends(p.getUniqueId().toString());
    for (String friend : friends) {
      ProxiedPlayer friendPlayer = BungeeCord.getInstance().getPlayer(UUID.fromString(friend));
      if (friendPlayer != null && BungeeUtils.getSetting(friend, setting))
        friendPlayer.sendMessage(string); 
    } 
  }
}
