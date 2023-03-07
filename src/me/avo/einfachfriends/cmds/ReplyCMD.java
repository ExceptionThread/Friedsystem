package me.avo.einfachfriends.cmds;

import me.avo.einfachfriends.Friends;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCMD extends Command {
  public ReplyCMD(String name) {
    super(name, "", new String[] { "r" });
  }
  
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer))
      return; 
    ProxiedPlayer p = (ProxiedPlayer)sender;
    if (args.length == 0) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§c/" + getName() + " <Nachricht>");
      return;
    } 
    StringBuilder msg = new StringBuilder();
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = args).length, b = 0; b < i; ) {
      String arg = arrayOfString[b];
      msg.append(arg).append(" ");
      b++;
    } 
    String replyTo = (String)Friends.replies.getOrDefault(p, null);
    if (replyTo == null) {
      p.sendMessage(String.valueOf(Friends.prefix) + "§cDu hast niemanden zum antworten!");
    } else {
      BungeeCord.getInstance().getPluginManager().dispatchCommand((CommandSender)p, "msg " + replyTo + " " + msg);
    } 
  }
}
