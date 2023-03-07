package me.avo.einfachfriends.cmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.cmds.subcmds.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FriendsCMD extends Command {
  private static final HashMap<String, SubCommand> subcmds = new HashMap<>();
  
  public FriendsCMD() {
    super("friends", "", new String[] { "freunde", "friend" });
    loadSubCommands();
  }
  
  private void loadSubCommands() {
    List<SubCommand> subCommands = Arrays.asList(new SubCommand[] { 
          (SubCommand)new FriendAddCMD(), (SubCommand)new FriendRemoveCMD(), (SubCommand)new FriendJumpCMD(), (SubCommand)new FriendListCMD(), (SubCommand)new FriendAcceptCMD(), (SubCommand)new FriendDenyCMD(), (SubCommand)new FriendAcceptAllCMD(), (SubCommand)new FriendDenyAllCMD(), (SubCommand)new FriendRequestsCMD(), (SubCommand)new FriendToggleCMD(), 
          (SubCommand)new FriendToggleNotifyCMD(), (SubCommand)new FriendToggleJumpCMD(), (SubCommand)new FriendToggleMsgCMD(), (SubCommand)new FriendToggleOnlineCMD() });
    for (SubCommand subCommand : subCommands)
      subcmds.put(subCommand.getSubCommand().toLowerCase(), subCommand); 
  }
  
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer))
      return; 
    ProxiedPlayer p = (ProxiedPlayer)sender;
    CloudPlayer cp = CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId());
    if (args.length == 0) {
      String a = Friends.prefix + "/" + getName() + " ";
      String b = "§8» §8§l§m---------- §3§lFreunde §8§l§m----------§8 «\n";
      b = b + a + "add <Name> §8- §7Freund hinzufügen\n";
      b = b + a + "remove <Name> §8- §7Freundschaft beenden\n";
      b = b + a + "jump <Name> §8- §7Zum Freund springen\n";
      b = b + a + "list <Seite> §8- §7Freunde auflisten\n";
      b = b + a + "accept <Name> §8- §7Anfrage annehmen\n";
      b = b + a + "deny <Name> §8- §7Anfrage ablehnen\n";
      b = b + a + "acceptAll §8- §7Alle Anfragen annehmen\n";
      b = b + a + "denyAll §8- §7Alle Anfragen ablehnen\n";
      b = b + a + "requests §8- §7Alle Anfragen auflisten\n";
      b = b + a + "toggle §8- §7Freundschaftsanfragen de-/aktivieren\n";
      b = b + a + "togglenotify §8- §7Spielt-Benachrichtigungen de-/aktivieren\n";
      b = b + a + "toggleonline §8- §7On-/Offline-Benachrichtigungen de-/aktivieren\n";
      b = b + a + "togglejump §8- §7Nachspringen de-/aktivieren\n";
      b = b + a + "togglemsg §8- §7Privatnachrichten de-/aktivieren";
      p.sendMessage(b);
      return;
    } 
    String arg = args[0].toLowerCase();
    executeArg(p, cp, arg, args);
  }
  
  public static void executeArg(ProxiedPlayer p, CloudPlayer cp, String arg, String[] args) {
    SubCommand subCommand = subcmds.get(arg.toLowerCase());
    if (subCommand != null) {
      if (args.length == 1 && 
        subCommand.doesNeedsArgs()) {
        p.sendMessage(Friends.prefix + "/friends " + arg + " §c<Name>");
        return;
      } 
      subCommand.run(p, cp, args);
    } else {
      p.sendMessage(Friends.prefix + "§cDieser Befehl existiert nicht.");
    } 
  }
}
