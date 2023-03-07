package me.avo.einfachfriends;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import de.dytanic.cloudnet.lib.player.permission.GroupEntityData;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class FriendsCMD extends Command {
    private MySQL mysql;

    public FriendsCMD(String name) {
        super("friends", "", new String[]{"freunde", "friend"});
        this.mysql = Friends.instance.mysql;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer))
            return;
        ProxiedPlayer p = (ProxiedPlayer) sender;
        CloudPlayer cp = CloudAPI.getInstance().getOnlinePlayer(p.getUniqueId());
        if (args.length == 0) {
            p.sendMessage("§8» §7§m----------§a§l Einfach§4§lFreunde §7§m----------§8 «");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §cadd <Name> §7Freund hinzufügen");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §cremove <Name> §7Freundschaft beenden");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §cjump <Name> §7Zum Freund springen");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §clist <Seite> §7Freunde auflisten");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §caccept <Name> §7Anfrage annehmen");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §cdeny <Name> §7Anfrage ablehnen");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §cacceptAll §7Alle Anfragen annehmen");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §cdenyAll §7Alle Anfragen ablehnen");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §crequests §7Alle Anfragen auflisten");
            p.sendMessage(String.valueOf(Friends.prefix) + "§e/" + getName() + " §ctoggle §7Anfragen de-/aktivieren");
            return;
        }
        String arg = args[0].toLowerCase();
        executeArg(p, cp, arg, args, (!arg.equals("acceptall") && !arg.equals("denyall") && !arg.equals("requests") &&
                !arg.equals("toggle")));
    }

    void executeArg(ProxiedPlayer p, CloudPlayer cp, String arg, String[] args, boolean needArgs) {
        if (args.length == 1 && needArgs) {
            if (arg.equalsIgnoreCase("list")) {
                executeArg(p, cp, arg, new String[]{"list", "1"}, true);
            } else {
                p.sendMessage(String.valueOf(Friends.prefix) + "§e/friends " + arg + " §c<Name>");
            }
            return;
        }
        if (needArgs) {
            String val = args[1];
            String str1;
            switch ((str1 = arg).hashCode()) {
                case -1423461112:
                    if (!str1.equals("accept"))
                        break;
                    try {
                        UUID fromUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
                        if (fromUuid == null) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf dem Netzwerk.");
                            return;
                        }
                        if (!Utils.hasRequestFrom(p.getUniqueId().toString(), fromUuid.toString())) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDu hast keine Anfrage von " + val + " erhalten!");
                        } else {
                            Utils.acceptRequest(p.getUniqueId().toString(), fromUuid.toString());
                            p.sendMessage(
                                    String.valueOf(Friends.prefix) + "§aDu hast die Freundschaftsanfrage von §e" + val + " §aangenommen!");
                            ProxiedPlayer t = BungeeCord.getInstance().getPlayer(fromUuid);
                            if (t != null)
                                t.sendMessage(String.valueOf(Friends.prefix) + "§aDu bist nun mit §e" + p.getName() + " §abefreundet!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + ex.getMessage());
                    }
                    return;
                case -934610812:
                    if (!str1.equals("remove"))
                        break;
                    try {
                        ProxiedPlayer sender = p;
                        UUID senderUuid = sender.getUniqueId();
                        UUID uniqueId = CloudAPI.getInstance().getPlayerUniqueId(val);
                        if (uniqueId == null) {
                            sender.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf EinfachGames!");
                            return;
                        }
                        OfflinePlayer offlinePlayer = CloudAPI.getInstance().getOfflinePlayer(uniqueId);
                        if (offlinePlayer != null && offlinePlayer.getPermissionEntity() != null) {
                            if (Utils.isFriendWith(p.getUniqueId().toString(), uniqueId.toString())) {
                                Utils.removeFriend(senderUuid.toString(), uniqueId.toString(), false);
                                p.sendMessage(String.valueOf(Friends.prefix) + "§eDu hast die Freundschaft mit " + val + " aufgelöst!");
                                ProxiedPlayer t = BungeeCord.getInstance().getPlayer(val);
                                if (t != null)
                                    t.sendMessage(String.valueOf(Friends.prefix) + "§e" + p.getName() + " hat die Freundschaft mit dir aufgelöst!");
                            } else {
                                sender.sendMessage(String.valueOf(Friends.prefix) + "§cDu bist nicht mit " + val + " befreundet!");
                            }
                        } else {
                            sender.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf EinfachGames!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + ex.getMessage());
                    }
                    return;
                case 96417:
                    if (!str1.equals("add"))
                        break;
                    if (val.equalsIgnoreCase(p.getName()) && (args.length != 3 || !args[2].equals("--testCommand"))) {
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cDu kannst dich nicht selbst als Freund haben :'c");
                    } else {
                        try {
                            UUID newRequestUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
                            if (newRequestUuid == null) {
                                p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf dem Netzwerk.");
                                return;
                            }
                            List<String> friends = Utils.getFriends(p.getUniqueId().toString());
                            if (friends.contains(newRequestUuid.toString())) {
                                p.sendMessage(String.valueOf(Friends.prefix) + "§cDu bist bereits mit " + val + " befreundet!");
                            } else {
                                if (Utils.getRequests(newRequestUuid.toString()).contains(p.getUniqueId().toString())) {
                                    p.sendMessage(String.valueOf(Friends.prefix) + "§cDu hast diesem Spieler bereits eine Anfrage gesendet!");
                                    return;
                                }
                                ResultSet rs = Utils.mysql.getConnection().prepareStatement("SELECT * FROM settings WHERE uuid='" + newRequestUuid.toString() + "';").executeQuery();
                                if (rs.next()) {
                                    if (rs.getInt("toggled") == 0) {
                                        p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler nimmt keine Anfragen an!");
                                        return;
                                    }
                                    Utils.request(newRequestUuid.toString(), p.getUniqueId().toString());
                                    p.sendMessage(String.valueOf(Friends.prefix) + "§aDu hast §e" + val + " §aeine Freundschaftsanfrage gesendet!");
                                    ProxiedPlayer t = BungeeCord.getInstance().getPlayer(newRequestUuid);
                                    if (t != null) {
                                        t.sendMessage(String.valueOf(Friends.prefix) + "§e" + p.getName() + " §ahat dir eine Freundschaftsanfrage gesendet!");
                                        sendAcceptDenyMessage(t, p);
                                    }
                                } else {
                                    p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf dem Netzwerk.");
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + exception.getMessage());
                        }
                    }
                    return;
                case 3079692:
                    if (!str1.equals("deny"))
                        break;
                    try {
                        UUID fromUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
                        if (fromUuid == null) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf dem Netzwerk.");
                            return;
                        }
                        if (!Utils.hasRequestFrom(p.getUniqueId().toString(), fromUuid.toString())) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDu hast keine Anfrage von " + val + " erhalten!");
                        } else {
                            Utils.denyRequest(p.getUniqueId().toString(), fromUuid.toString());
                            p.sendMessage(
                                    String.valueOf(Friends.prefix) + "§aDu hast die Freundschaftsanfrage von §e" + val + " §aabgelehnt!");
                            ProxiedPlayer t = BungeeCord.getInstance().getPlayer(fromUuid);
                            if (t != null)
                                t.sendMessage(String.valueOf(Friends.prefix) + "§e" + p.getName() +
                                        " §ahat deine Freundschaftsanfrage abgelehnt!");
                        }
                    } catch (Exception exception) {
                        NumberFormatException ex;
                        //ex.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: NumberFormatException");
                    }
                    return;
                case 3273774:
                    if (!str1.equals("jump"))
                        break;
                    try {
                        UUID withUuid = CloudAPI.getInstance().getPlayerUniqueId(val);
                        if (withUuid == null) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler war noch nie auf dem Netzwerk.");
                            return;
                        }
                        if (!Utils.isFriendWith(p.getUniqueId().toString(), withUuid.toString())) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler ist nicht in deiner Freundesliste!");
                        } else if (BungeeCord.getInstance().getPlayer(withUuid) == null) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Spieler ist nicht online!");
                        } else {
                            p.sendMessage(String.valueOf(Friends.prefix) + "Du springst nun zu " + val + "!");
                            p.connect(BungeeCord.getInstance().getPlayer(val).getServer().getInfo());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + ex.getMessage());
                    }
                    return;
                case 3322014:
                    if (!str1.equals("list"))
                        break;
                    try {
                        int size = Integer.parseInt(val);
                        if (size < 1) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "§cKeine gültige Seitenanzahl.");
                        } else {
                            List<String> friendsUuids = Utils.getFriends(p.getUniqueId().toString());
                            if (friendsUuids.size() == 0) {
                                if (size == 1) {
                                    p.sendMessage(String.valueOf(Friends.prefix) + "§cDu hast leider keine Freunde :'c");
                                    return;
                                }
                                p.sendMessage(String.valueOf(Friends.prefix) + "§cDiese Seite existiert nicht!");
                                return;
                            }
                            p.sendMessage("§8» §7§m-----§4§l Freunde §7(§eS. " + size + "§7) §7§m-----§8 «");
                            int max = (size - 1) * 10 + 10;
                            if (max > friendsUuids.size())
                                max = friendsUuids.size();
                            if (max == 0)
                                p.sendMessage(String.valueOf(Friends.prefix) + "§cDiese Seite existiert nicht!");
                            for (int i = (size - 1) * 10; i < max; i++) {
                                String friendUuid = friendsUuids.get(i);
                                String name = CloudAPI.getInstance().getOfflinePlayer(UUID.fromString(friendUuid)).getName();
                                String pfx = CloudAPI.getInstance().getPermissionGroup(((GroupEntityData) CloudAPI.getInstance().getOfflinePlayer(name).getPermissionEntity().getGroups().iterator().next()).getGroup()).getPrefix();
                                pfx = (pfx.contains("&l") || pfx.contains("§l")) ? pfx.substring(0, 4) : pfx.substring(0, 2);
                                if (BungeeCord.getInstance().getPlayer(name) == null) {
                                    p.sendMessage(" §8" + name + " §7ist §coffline");
                                } else {
                                    p.sendMessage(" " + pfx + name + " §7ist §aonline §7auf §6" + BungeeCord.getInstance().getPlayer(name).getServer().getInfo().getName());
                                }
                            }
                        }
                    } catch (NumberFormatException numberFormatException) {
                        p.sendMessage(String.valueOf(Friends.prefix) + "§c<Seite> muss eine Zahl sein!");
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + exception.getMessage());
                    }
                    return;
            }
            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Befehl ist nicht verfügbar.");
        } else {
            String str;
            switch ((str = arg).hashCode()) {
                case -2117777511:
                    if (!str.equals("acceptall"))
                        break;
                    try {
                        for (String request : Utils.getRequests(p.getUniqueId().toString())) {
                            executeArg(p, cp, "accept", new String[]{"accept",
                                    CloudAPI.getInstance().getPlayerName(UUID.fromString(request))}, true);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + ex.getMessage());
                    }
                    return;
                case -868304044:
                    if (!str.equals("toggle"))
                        break;
                    try {
                        int toggled = this.mysql
                                .query("SELECT * FROM settings WHERE uuid='" + cp.getUniqueId().toString() + "';")
                                .getInt("toggled");
                        if (toggled == 1) {
                            this.mysql.update("UPDATE settings SET toggled=0 WHERE uuid='" + cp.getUniqueId().toString() + "';");
                            p.sendMessage(String.valueOf(Friends.prefix) + "Freundschaftsanfragen deaktiviert.");
                        } else {
                            this.mysql.update("UPDATE settings SET toggled=1 WHERE uuid='" + cp.getUniqueId().toString() + "';");
                            p.sendMessage(String.valueOf(Friends.prefix) + "Freundschaftsanfragen aktiviert.");
                        }
                    } catch (SQLException sQLException) {
                        Exception ex = null;
                        //ex.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler bei der Datenbankverbindung.");
                    } catch (Exception ex) {
                        Exception request;
                        //request.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: Im Friendsystem");
                    }
                    return;
                case -393257020:
                    if (!str.equals("requests"))
                        break;
                    try {
                        List<String> requestsUuids = Utils.getRequests(p.getUniqueId().toString());
                        byte b = 0;
                        if (requestsUuids.size() == 0) {
                            p.sendMessage(String.valueOf(Friends.prefix) + "Du hast keine Freundschaftsanfragen.");
                        } else {
                            String reqs = Friends.prefix;
                            for (String requestUuid : requestsUuids) {
                                b++;
                                if (b == 1)
                                    reqs = String.valueOf(reqs) + "Du hast " + requestsUuids.size() + " Freundschaftsanfragen von ";
                                if (b < 4) {
                                    reqs = String.valueOf(reqs) + CloudAPI.getInstance().getOfflinePlayer(UUID.fromString(requestUuid)).getName() + "§7, §e";
                                    continue;
                                }
                                reqs = String.valueOf(reqs) + "§7 und §e" + (requestsUuids.size() - 3) + " weiteren Spielern.";
                                break;
                            }
                            if (reqs.endsWith("§7, §e"))
                                reqs = reqs.substring(0, reqs.length() - "§7, §e".length());
                            p.sendMessage(reqs);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + exception.getMessage());
                    }
                    return;
                case 3322014:
                    if (!str.equals("list"))
                        break;
                    executeArg(p, cp, "list", new String[]{"list", "1"}, true);
                    return;
                case 1552887829:
                    if (!str.equals("denyall"))
                        break;
                    try {
                        for (String str1 : Utils.getRequests(p.getUniqueId().toString())) {
                            executeArg(p, cp, "deny", new String[]{"deny", CloudAPI.getInstance().getPlayerName(UUID.fromString(str1))}, true);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        p.sendMessage(String.valueOf(Friends.prefix) + "§cFehler: " + exception.getMessage());
                    }
                    return;
            }
            p.sendMessage(String.valueOf(Friends.prefix) + "§cDieser Befehl ist nicht verfügbar.");
        }
    }

    private void sendAcceptDenyMessage(ProxiedPlayer t, ProxiedPlayer p) {
        TextComponent full = new TextComponent(Friends.prefix);
        TextComponent accept = new TextComponent("§8[§aAnnehmen§8]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + p.getName()));
        TextComponent air = new TextComponent(" ");
        TextComponent deny = new TextComponent("§8[§cAblehnen§8]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + p.getName()));
        full.addExtra((BaseComponent) accept);
        full.addExtra((BaseComponent) air);
        full.addExtra((BaseComponent) deny);
        t.sendMessage((BaseComponent) full);
    }
}
