package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import me.avo.einfachfriends.Friends;
import me.avo.einfachfriends.utils.BungeeUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class FriendAddCMD extends SubCommand {
  public FriendAddCMD() {
    super("add", true);
  }
  
  public void run(ProxiedPlayer p, CloudPlayer cp, String[] args) {
    String pUuid = p.getUniqueId().toString();
    String val = args[1];
    if (val.equalsIgnoreCase(p.getName())) {
      p.sendMessage(Friends.prefix + "§cDu kannst dich nicht selbst als Freund/in haben :'c");
      return;
    } 
    UUID newRequestUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
    if (newRequestUuid == null) {
      p.sendMessage(Friends.prefix + "§c" + val + " war noch nie auf dem Netzwerk.");
      return;
    } 
    String targetUuid = newRequestUuid.toString();
    List<String> friends = BungeeUtils.getFriends(pUuid);
    if (friends.contains(targetUuid)) {
      p.sendMessage(Friends.prefix + "§cDu bist bereits mit " + 
          Friends.getDisplay(targetUuid) + " §cbefreundet!");
      return;
    } 
    if (BungeeUtils.getRequests(targetUuid).contains(pUuid)) {
      p.sendMessage(Friends.prefix + "§cDu hast " + Friends.getDisplay(targetUuid) + " §cbereits eine Anfrage gesendet!");
      return;
    } 
    boolean requests = BungeeUtils.getSetting(targetUuid, "requests");
    if (!requests) {
      p.sendMessage(Friends.prefix + 
          Friends.getDisplay(targetUuid) + " §chat Freundschaftsanfragen deaktiviert!");
      return;
    } 
    BungeeUtils.request(targetUuid, pUuid);
    p.sendMessage(Friends.prefix + "§aDu hast " + Friends.getDisplay(targetUuid) + " §aeine Freundschaftsanfrage gesendet!");
    ProxiedPlayer t = BungeeCord.getInstance().getPlayer(newRequestUuid);
    if (t != null) {
      t.sendMessage(Friends.prefix + Friends.getDisplay(p) + " §ahat dir eine Freundschaftsanfrage gesendet!");
      sendAcceptDenyMessage(t, p);
    } 
  }
  
  private void sendAcceptDenyMessage(ProxiedPlayer t, ProxiedPlayer p) {
    TextComponent full = new TextComponent(Friends.prefix);
    TextComponent accept = new TextComponent("§8[§aAnnehmen§8]");
    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + p.getName()));
    TextComponent air = new TextComponent(" ");
    TextComponent deny = new TextComponent("§8[§cAblehnen§8]");
    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + p.getName()));
    full.addExtra((BaseComponent)accept);
    full.addExtra((BaseComponent)air);
    full.addExtra((BaseComponent)deny);
    t.sendMessage((BaseComponent)full);
  }
}
