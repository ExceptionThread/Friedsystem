package me.avo.einfachfriends.spigot;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.avo.einfachfriends.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FriendsMain extends JavaPlugin {
  public static FriendsMain instance;
  
  public static MySQL mysql;
  
  public void onEnable() {
    instance = this;
    mysql = new MySQL();
    SpigotUtils.setMySQL(mysql);
    Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin)instance, "syplex:friends");
  }
  
  public void sendBungee(String command, String[] args) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("api");
    out.writeUTF(command);
    StringBuilder argsString = new StringBuilder();
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = args).length, b = 0; b < i; ) {
      String arg = arrayOfString[b];
      argsString.append(arg).append(";");
      b++;
    } 
    if (argsString.toString().endsWith(";"))
      argsString = new StringBuilder(argsString.substring(0, argsString.length() - 1)); 
    out.writeUTF(argsString.toString());
    Player player = (Player)Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
    if (player != null) {
      player.sendPluginMessage((Plugin)this, "syplex:friends", out.toByteArray());
    } else {
      System.out.print("[ATTENTION] Friendsystem - Could not send plugin message: player is null");
    } 
  }
}
