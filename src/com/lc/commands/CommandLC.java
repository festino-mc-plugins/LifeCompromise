package com.lc.commands;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lc.LCPlayer;
import com.lc.LCPlayer.LCMode;
import com.lc.LCPlayerList;
import com.lc.commands.PermissionWorker.Result;
import com.lc.config.Config;
import com.lc.config.Config.Key;
import com.lc.utils.Utils;

public class CommandLC implements CommandExecutor {
	
	public static final String LC_COMMAND = "lc";
	private final Config config;
	private final LCPlayerList lcp_list;
	
	public CommandLC(Config config, LCPlayerList lcp_list) {
		this.config = config;
		this.lcp_list = lcp_list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
	{
		Result result = PermissionWorker.canDispatch(sender, cmd.getName(), args);
		if (result == Result.DENY_PERM) {
			denyPerm(sender);
		} else if (result == Result.DENY_NOPLAYER) {
			denyNoPlayer(sender);
		}
		
		if (args.length == 0) {
			if (sender instanceof Player) {
				LCPlayer lcp = lcp_list.load((Player) sender);
				String state = lcp.getSoftLC() ? "on" : "off";
				sender.sendMessage(ChatColor.GREEN + "LC is " + state);
			}
			return true;
		}
		
		String option = args[0];
		if (args.length > 0) {
			if (option.equalsIgnoreCase("?")) {
				sender.sendMessage("HELP!");
			}
			if (sender instanceof Player) {
				LCPlayer lcp = lcp_list.load((Player) sender);
				// TODO messages
				if (option.equalsIgnoreCase("on")) {
					lcp.setSoftLC(true);
					sender.sendMessage(ChatColor.GREEN + "LC is on");
					return true;
				} else if (option.equalsIgnoreCase("off")) {
					lcp.setSoftLC(false);
					sender.sendMessage(ChatColor.GREEN + "LC is off");
					return true;
				}
				if (option.equalsIgnoreCase("info")) {
					sender.sendMessage(ChatColor.GREEN + "Your data:");
					sender.sendMessage(ChatColor.GREEN + "    Temperature: " + lcp.getTemperature());
					sender.sendMessage(ChatColor.GREEN + "    Thirst: " + lcp.getThirst());
					sender.sendMessage(ChatColor.GREEN + "    Logged in: " + lcp.isLoggedIn());
					sender.sendMessage(ChatColor.GREEN + "    Soft LC: " + lcp.getSoftLC());
					sender.sendMessage(ChatColor.GREEN + "    Forced LC: " + lcp.getForcedLC());
					sender.sendMessage(ChatColor.GREEN + "    LC: " + lcp.isLC());
					return true;
				}
				// TODO refactor
				if (option.equalsIgnoreCase("output_ticks")) {
					if (args.length == 1) {
						sender.sendMessage(ChatColor.GREEN + option + " = " + lcp.getOutputTicks());
					} else {
						lcp.setOutputTicks(Integer.parseInt(args[1]));
						sender.sendMessage(ChatColor.GREEN + option + " set to " + lcp.getOutputTicks());
					}
					return true;
				}
			}
			
			if (sender.isOp()) {
				boolean forceON = option.equalsIgnoreCase("forceON");
				boolean forceOFF = option.equalsIgnoreCase("forceOFF");
				boolean forceNONE = option.equalsIgnoreCase("forceNONE");
				if (forceON || forceOFF || forceNONE) {
					if (args.length <= 1) {
						sender.sendMessage(ChatColor.RED + "Enter player nickname!");
						return false;
					}
					String nickname = args[1];
					UUID uuid = Utils.getUUID(nickname);
					if (uuid == null) {
						sender.sendMessage(ChatColor.RED + "Could not find player \"" + nickname + "\"!");
						return false;
					}
					LCPlayer lcp = lcp_list.load(uuid);
					if (lcp == null) {
						sender.sendMessage(ChatColor.RED + "Could not load player \"" + nickname + "\"!");
						return false;
					}
					if (forceON)
						lcp.setForcedLC(LCMode.FORCED_ON);
					if (forceOFF)
						lcp.setForcedLC(LCMode.FORCED_OFF);
					if (forceNONE)
						lcp.setForcedLC(LCMode.NONE);
					
					if (lcp.getPlayer() != null)
						nickname = lcp.getPlayer().getName();
					sender.sendMessage(ChatColor.GREEN + "forcedLC of the player \"" + nickname + "\" set to " + lcp.getForcedLC() + "!");
				}
			}
			
			if (option.equalsIgnoreCase("config")) {
				return onConfigCommand(sender, Arrays.copyOfRange(args, 1, args.length));
			}
			
			// debug ???
		}
		return false;
	}
	
	public boolean onConfigCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		} else {
			String option = args[0];
			if (option.equalsIgnoreCase("reload")) {
				config.load();
				sender.sendMessage("Config reloaded.");
				return true;
			}
			if (option.equalsIgnoreCase("save")) {
				config.save();
				sender.sendMessage("Config saved.");
				return true;
			}
			if (option.equalsIgnoreCase("set")) {
				if (args.length == 1) {
					// TODO help (options/"follow autocompleting")
					sender.sendMessage("FOLLOW AUTOCOMPLETION");
					return false;
				} else if (args.length > 1) {
					String key_str = args[1];
					if (!Key.isValidKey(key_str)) {
						// TODO msg
						sender.sendMessage("INVALID KEY, PLEASE FOLLOW AUTOCOMPLETION");
						return false;
					}
					Key key = Key.getKey(key_str);
					if (args.length == 2) {
						// TODO help var type
						sender.sendMessage("VALID TYPE: " + key.getValueClass());
						return false;
					} else if (args.length > 2) {
						String value_str = args[2];
						Object value = key.validateValue(value_str);
						if (value == null) {
							// TODO help var type
							sender.sendMessage("INVALID TYPE, " + key.getValueClass() + " EXPECTED");
							return false;
						}
						// TODO msg OK
						sender.sendMessage("OK, set \"" + key + "\" to " + value);
						config.set(key, value);
						return true;
					}
				}
			}
			if (option.equalsIgnoreCase("get")) {
				if (args.length == 1) {
					// TODO help (options/"follow autocompleting")
					sender.sendMessage("FOLLOW AUTOCOMPLETION");
					return false;
				} else if (args.length > 1) {
					String key_str = args[1];
					if (!Key.isValidKey(key_str)) {
						// TODO msg
						sender.sendMessage("INVALID KEY, PLEASE FOLLOW AUTOCOMPLETION");
						return false;
					}
					Key key = Key.getKey(key_str);
					
					// TODO msg OK
					sender.sendMessage("OK, value of \"" + key + "\" is " + config.get(key).toString());
				}
			}
			
		}
		return false;
	}

	public void denyPerm(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You don't have permission to perform this command.");
	}

	public void denyNoPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You must be player to perform this command. "
				+ "Check out \"" + LC_COMMAND + " ?\" for available commands.");
	}
}
