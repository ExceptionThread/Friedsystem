package me.avo.einfachfriends.cmds;

import de.dytanic.cloudnet.api.CloudAPI;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.MySQL;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MsgCMD extends Command {
  public MySQL mysql;
  
  public MsgCMD(String name) {
    super(name, "", new String[] { "message" });
    this.mysql = Friends.instance.mysql;
  }
  
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer))
      return; 
    ProxiedPlayer p = (ProxiedPlayer)sender;
    if (args.length < 2) {
      p.sendMessage(Friends.prefix + "§c/msg <Name> <Nachricht>");
      return;
    } 
    String val = args[0];
    StringBuilder msg = new StringBuilder();
    for (int i = 1; i < args.length; i++)
      msg.append(args[i]).append(" "); 
    UUID toUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
    if (toUuid == null) {
      p.sendMessage(Friends.prefix + "§cDieser Spieler war noch nie auf dem Netzwerk.");
      return;
    } 
    boolean isFriends = BungeeUtils.isFriendWith(p.getUniqueId().toString(), toUuid.toString());
    if (isFriends) {
      ProxiedPlayer t = BungeeCord.getInstance().getPlayer(val);
      if (t != null) {
        boolean msgSetting = BungeeUtils.getSetting(toUuid.toString(), "msg");
        if (!msgSetting && !((String)Friends.replies.getOrDefault(t, "-")).equals(p.getName())) {
          p.sendMessage(Friends.prefix + "§c" + val + " hat Privatnachrichten deaktiviert!");
          return;
        } 
        String msgString = " §8§l• §5§lMSG §8» " + Friends.getDisplay(p) + " §8» " + Friends.getDisplay(t) + " §8» §e" + msg;
        Friends.replies.put(p, t.getName());
        Friends.replies.put(t, p.getName());
        t.sendMessage(msgString);
        p.sendMessage(msgString);
        return;
      } 
      p.sendMessage(Friends.prefix + "§c" + val + " ist nicht online!");
    } else {
      p.sendMessage(Friends.prefix + "§cDu kannst nur Freunden eine private Nachricht schreiben!");
    } 
  }
}
