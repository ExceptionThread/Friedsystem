package me.avo.einfachfriends;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.util.ArrayList;

public class MsgCMD extends Command {
  public Friends.MySQL mysql;
  
  public MsgCMD(String name) {
    super(name, "", new String[] { "message" });
    this.mysql = (Friends.MySQL)Friends.instance.mysql;
  }
  
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer))
      return; 
    ProxiedPlayer p = (ProxiedPlayer)sender;
    CloudPlayer cp = CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId());
    if (args.length < 2) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§c/msg <Name> <Nachricht>");
      return;
    } 
    String msg = "";
    for (int i = 1; i < args.length; i++)
      msg = String.valueOf(msg) + args[i] + " "; 
    ResultSet rs = this.mysql.query("SELECT * FROM friends WHERE uuid='" + cp.getUniqueId().toString() + "';");
    try {
      String friendsUuidsString = rs.getString("friends");
      ArrayList<String> friendsUuids = new ArrayList<>();
      byte b;
      int j;
      String[] arrayOfString;
      for (j = (arrayOfString = friendsUuidsString.split(";")).length, b = 0; b < j; ) {
        String friendUuid = arrayOfString[b];
        friendsUuids.add(friendUuid);
        b++;
      } 
      if (friendsUuids.contains(CloudAPI.getInstance().getPlayerUniqueId(args[0]).toString())) {
        String msgString = " §8• §5MSG §8» §e" + p.getName() + " §8> §e" + args[0] + " §8» §e" + msg;
        if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
          Friends.replies.put(p, BungeeCord.getInstance().getPlayer(args[0]).getName());
          Friends.replies.put(BungeeCord.getInstance().getPlayer(args[0]), p.getName());
          BungeeCord.getInstance().getPlayer(args[0]).sendMessage(msgString);
          p.sendMessage(msgString);
        } else {
          p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler ist nicht online!");
        } 
      } else {
        p.sendMessage(String.valueOf(Friends.prefix) + "§cDu kannst nur Freunden eine private Nachricht schreiben!");
      } 
    } catch (Exception ex) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler beim Senden der Nachricht: " + ex.getMessage());
      ex.printStackTrace();
    } 
  }
}
