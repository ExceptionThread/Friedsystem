package me.avo.einfachfriends;

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
    String msg = "";
    for (int i = 0; i < args.length; i++)
      msg = String.valueOf(msg) + args[i] + " "; 
    if (!Friends.replies.containsKey(p))
      p.sendMessage(String.valueOf(Friends.prefix) + "§cDu hast niemanden zum schreiben!"); 
    BungeeCord.getInstance().getPluginManager().dispatchCommand((CommandSender)p, "msg " + (String)Friends.replies.get(p) + " " + msg);
  }
}
