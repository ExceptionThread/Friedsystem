package me.avo.einfachfriends.cmds.subcmds;

import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class SubCommand {
  private final String subCommand;
  
  private final boolean needsArgs;
  
  public SubCommand(String subCommand, boolean needsArgs) {
    this.subCommand = subCommand;
    this.needsArgs = needsArgs;
  }
  
  public boolean doesNeedsArgs() {
    return this.needsArgs;
  }
  
  public String getSubCommand() {
    return this.subCommand;
  }
  
  public abstract void run(ProxiedPlayer paramProxiedPlayer, CloudPlayer paramCloudPlayer, String[] paramArrayOfString);
}
