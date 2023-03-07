package me.avo.einfachfriends.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class FriendsAPIListener implements Listener {
  @EventHandler
  public void onMessage(PluginMessageEvent e) {
    String channel = e.getTag();
    if (!channel.equals("syplex:friends"))
      return; 
    ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
    String subChannel = in.readUTF();
    if (subChannel.equalsIgnoreCase("BungeeCmd")) {
      String uuid = in.readUTF();
      String command = in.readUTF();
      ProxiedPlayer p = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
      BungeeCord.getInstance().getPluginManager().dispatchCommand((CommandSender)p, command.substring(1));
      System.out.print("[Syplex Friends Channel] Received plugin message [" + channel + "] [" + subChannel + "] {" + uuid + "," + command + "}");
    } 
    if (subChannel.equalsIgnoreCase("api")) {
      String command = in.readUTF();
      String argsString = in.readUTF();
      String[] args = argsString.split(";");
      System.out.print("[Syplex Friends Channel] Received plugin message [" + channel + "] [" + subChannel + "] " + command + " {" + argsString + "}");
      if (command.equals("addfriend"))
        BungeeUtils.addFriend(args[0], args[1], false); 
      if (command.equals("removefriend"))
        BungeeUtils.removeFriend(args[0], args[1], false); 
      if (command.equals("addrequest"))
        BungeeUtils.request(args[0], args[1]); 
      if (command.equals("removerequest"))
        BungeeUtils.removeRequest(args[0], args[1]); 
      if (command.equals("setsetting"))
        BungeeUtils.setSetting(args[0], args[1], Boolean.parseBoolean(args[2])); 
    } 
  }
}
