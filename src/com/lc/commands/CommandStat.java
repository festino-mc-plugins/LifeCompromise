package com.lc.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lc.LCPlayer;
import com.lc.LCPlayerList;
import com.lc.PlayerHandler;
import com.lc.commands.PermissionWorker.Result;
import com.lc.config.Config;
import com.lc.utils.Utils;
import com.lc.utils.UtilsArmor;

public class CommandStat implements CommandExecutor {
	
	public static final String STAT_COMMAND = "stat";
	private final Config config;
	private final LCPlayerList lcp_list;
	private final PlayerHandler UTILS_INSTANCES;
	
	public CommandStat(Config config, LCPlayerList lcp_list, PlayerHandler main_handler) {
		this.config = config;
		this.lcp_list = lcp_list;
		this.UTILS_INSTANCES = main_handler;
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
		
		LCPlayer lcp = null;
		boolean sender_is_player = sender instanceof Player;
		if (sender_is_player) {
			lcp = get((Player) sender);
		}
		
		// TODO error messages
		if (args.length == 0) {
			if (sender_is_player) {
				sender.sendMessage(getStat(lcp));
			}
		}
		else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("?")) {
				// help
				sender.sendMessage("ANY HELP?");
			}
			
			if (args[0].equalsIgnoreCase("info")) {
				if (args.length > 1) {
					String nickname = args[1];
					UUID uuid = Utils.getUUID(nickname);
					if (uuid != null) {
						lcp = lcp_list.load(uuid);
					}
				}
				
				if (lcp != null) {
					sender.sendMessage(getDetailedStat(lcp));
				}
			}
			
			if (args[0].equalsIgnoreCase("set")) {
				if (args.length == 1) {
					sender.sendMessage("Missing argument " + "<temp/thirst>");
					return false;
				}
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("temp") || args[1].equalsIgnoreCase("thirst")) {
						if (args.length > 2) {
							try {
								double d = Double.parseDouble(args[2]);
								if (args[1].equalsIgnoreCase("temp")) {
									lcp.setTemperature(d);
									sender.sendMessage("Temperature set to " + d);
								} else {
									lcp.setThirst(d);
									sender.sendMessage("Thirst set to " + d);
								}
							} catch(Exception ex) {
								sender.sendMessage("Parse error");
							}
						} else {
							sender.sendMessage("Expected new value");
						}
					} else {
						sender.sendMessage("Invalid arg, expected " + "<temp/thirst>");
						return false;
					}
				}
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				if (args.length == 1) {
					sender.sendMessage("Missing argument " + "<temp/thirst>");
					return false;
				}
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("temp")) {
						sender.sendMessage("Temperature is " + lcp.getTemperature());
					} else if (args[1].equalsIgnoreCase("thirst")) {
						sender.sendMessage("Thirst is " + lcp.getThirst());
					} else {
						sender.sendMessage("Invalid arg, expected " + "<temp/thirst>");
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public String getStat(LCPlayer lcp) {
		String nickname = lcp.getPlayer().getName();
		Location loc = lcp.getPlayer().getLocation();
		double fire_resist = UTILS_INSTANCES.utils_temp.getFireResist(lcp.getPlayer());
		String res =
				String.format(ChatColor.GREEN + "%s %s:", nickname, "stat") + "\n" +
				String.format("    %s: %f", "Thirst", lcp.getThirst()) + "\n" +
				String.format("    %s: %f", "Temperature", lcp.getTemperature()) + "\n" +
				String.format("    %s: %f", "Air temperature", UTILS_INSTANCES.utils_temp.getAirTemperature(loc, fire_resist));
		return res;
	}
	
	public String getDetailedStat(LCPlayer lcp) {
		Player p = lcp.getPlayer();
		String nickname = p.getName();
		Location loc = p.getLocation();
		double fire_resist = UTILS_INSTANCES.utils_temp.getFireResist(p);
		double Tair = UTILS_INSTANCES.utils_temp.getAirTemperature(loc, fire_resist);
		double dT = Tair - lcp.getTemperature();
		String res =
				String.format(ChatColor.GREEN + "%s %s:\n", nickname, "stat") +
				String.format("    %s: %f (lvl: %s)\n", "Thirst", lcp.getThirst(), lcp.getThirstDebuff().toString()) +
				//String.format("\t%s: %f", "Normal temperature", lcp.getTemperature()) + "\n" +
				String.format("    %s: %f (lvl: %s)\n", "Temperature", lcp.getTemperature(), lcp.getTemperatureDebuff().toString()) +
				String.format("    %s: %f\n", "Speed temperature", UTILS_INSTANCES.utils_temp.getTemperatureSpeed(p, dT > 0)) +
				String.format("    %s: %f\n", "Air temperature", Tair) +
				String.format("    %s: %f\n", "\"Biomity\"", UTILS_INSTANCES.utils_temp.getBiomityBySunlight(loc)) +
				String.format("    %s: %f\n", "BiomeTemperture", UTILS_INSTANCES.utils_temp.getBiomeTemperature(loc)) +
				String.format("    %s: %f\n", "BlocksTemperture", UTILS_INSTANCES.utils_temp.getBlocksTemperature(loc, fire_resist)) +
				String.format("    %s: %f\n", "RandomTemperature", UTILS_INSTANCES.utils_temp.getRandomTemperature()) +
				String.format("    %s: %f (lvl: %s)\n", "ArmorWeight", UtilsArmor.calc(p), lcp.getArmorWeight().toString()); // TODO fix "LC_ON = false" NPE 
		return res;
	}
	
	public LCPlayer get(Player p) {
		return lcp_list.load(p);
	}

	public void denyPerm(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You don't have permission to perform this command.");
	}

	public void denyNoPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You must be player to perform this command. "
				+ "Check out \"" + STAT_COMMAND + " ?\" for available commands.");
	}
}
