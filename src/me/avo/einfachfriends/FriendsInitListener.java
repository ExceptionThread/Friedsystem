package me.avo.einfachfriends;

import com.google.common.collect.Lists;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class FriendsInitListener implements Listener {
  public MySQL mysql;
  
  @EventHandler
  public void onDisconnect(PlayerDisconnectEvent e) {
    ProxiedPlayer p = e.getPlayer();
    messageFriends(p, String.valueOf(Friends.prefix) + p.getName() + " §7ist nun §coffline§7!");
  }
  
  @EventHandler
  public void onSwitch(ServerSwitchEvent e) {
    ProxiedPlayer p = e.getPlayer();
    messageFriends(p, 
        String.valueOf(Friends.prefix) + p.getName() + " §7spielt nun auf §6" + p.getServer().getInfo().getName() + "§7!");
  }
  
  @EventHandler
  public void onJoin(PostLoginEvent e) {
    if (this.mysql == null)
      this.mysql = Friends.instance.mysql;
    ProxiedPlayer p = e.getPlayer();
    try {
      ResultSet rs = this.mysql.getConnection()
        .prepareStatement("SELECT * FROM requests WHERE uuid='" + p.getUniqueId().toString() + "';")
        .executeQuery();
      if (rs.next()) {
        String requestsUuidsString = rs.getString("requests");
        ArrayList<String> requestsUuids = new ArrayList<>();
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = requestsUuidsString.split(";")).length, b = 0; b < i; ) {
          String requestUuid = arrayOfString[b];
          if (requestUuid.length() >= 10)
            requestsUuids.add(requestUuid); 
          b++;
        } 
        messageFriends(p, String.valueOf(Friends.prefix) + p.getName() + " §7ist nun §aonline§7!");
        if (requestsUuids.size() == 0)
          return; 
        p.sendMessage(
            String.valueOf(Friends.prefix) + "Du hast noch " + requestsUuids.size() + " Freundschaftsanfragen offen!");
        return;
      } 
      this.mysql.update("INSERT INTO friends VALUES ('" + p.getUniqueId().toString() + "', '');");
      this.mysql.update("INSERT INTO requests VALUES ('" + p.getUniqueId().toString() + "', '');");
      this.mysql.update("INSERT INTO settings VALUES ('" + p.getUniqueId().toString() + "', 1);");
      messageFriends(p, String.valueOf(Friends.prefix) + p.getName() + " §7ist nun §aonline§7!");
      return;
    } catch (SQLException ex) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler beim Verbinden zur Freunde-Datenbank: " + ex.getMessage());
      return;
    } 
  }
  
  private void messageFriends(ProxiedPlayer p, String string) {
    ResultSet rs = this.mysql.query("SELECT * FROM friends WHERE uuid='" + p.getUniqueId().toString() + "';");
    try {
      ArrayList<String> friends = Lists.newArrayList(Arrays.toString((Object[])rs.getString("friends").split(";")));
      for (String friend : friends) {
        if (friend.length() < 10)
          continue; 
        ProxiedPlayer fr = BungeeCord.getInstance().getPlayer(UUID.fromString(friend));
        if (fr != null)
          fr.sendMessage(string); 
      } 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
}
